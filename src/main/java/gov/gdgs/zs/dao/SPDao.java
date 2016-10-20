package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SPDao extends BaseDao{
	/*-------------------------------中心端-------------------------------------*/
	/**
	 * 未审批查询
	 * @param uid
	 * @return List
	 */
	public Map<String,Object> wspcx(int uid){
		String param ="41,42,43,44";//41:其他从业人员转籍;42:其他从业人员信息变更;43:其他从业人员注销;44:其他从业人员转执业;全显示请留""
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT ");
		sb.append("		d.PERANT_ID as lx,a.LCLXID as lid, d.MC as wsxm, COUNT(c.id) wss ");
		sb.append("		FROM zs_splc a,fw_user_role e,dm_lclx d,zs_splcbz b");
		sb.append("		LEFT JOIN zs_spzx c ON c.LCBZID=b.id AND c.ztbj='Y'");
		sb.append("		WHERE a.ID=b.LCID AND b.ROLEID=e.role_id AND d.ID=a.LCLXID AND a.ZTBJ=2 AND a.LCLXID not in (29"+(param.length()>0?(","+param):" ")+") and e.USER_ID = ?");
		sb.append("		GROUP BY a.LCLXID");
		sb.append("		order by d.PERANT_ID,a.LCLXID");
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{uid});
		List<String> dl = new ArrayList<String>();
		dl.add("事务所审批类型");
		dl.add("执业税务师审批类型");
		dl.add("非执业注册税务师审批类型");
		dl.add("年检审批类型");
		dl.add("其他从业人员中心审批");
		Map<String,Object> mp = new HashMap<String,Object>();
		mp.put("ls", ls);
		mp.put("dl", dl);
		return mp;
	}
	
	/**
	 * 查看流程
	 * @param lid
	 * @return
	 */
	public List<Map<String,Object>> cklc(int lid){
		String sql = "select a.ID,a.LCMC,a.LCMS from zs_splc a where a.LCLXID =? and a.ZTBJ=2 order by a.ID desc";
		return this.jdbcTemplate.query(sql,new Object[]{lid},
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("lcmc", rs.getObject("LCMC"));
				map.put("lcms", rs.getObject("LCMS"));
				map.put("xxlc", jdbcTemplate.queryForList("SELECT b.lcbz,c.DESCRIPTION as js,case b.spbz WHEN 'Y' THEN '是' WHEN 'N' THEN '否' ELSE NULL END AS tjbz"
						+ ",CASE b.bhbz WHEN 'Y' THEN '是' WHEN 'N' THEN '否' ELSE NULL END AS bhbz,CASE b.sfhq WHEN 'Y' THEN '是' WHEN 'N' THEN '否' ELSE NULL END AS sfhq"
						+ " FROM zs_splcbz b,fw_role c WHERE b.lcid = ? and c.ID=b.roleid ORDER BY lcbz",new Object[]{rs.getObject("ID")}));
				return map;
				}
			});
	}
	
	/**
	 * 机构设立未审批明细查询
	 * @param pn,ps,uid,lcid,qury
	 * @return
	 */
	public Map<String,Object> jgslspcx(int pn,int ps,int uid,int lclxid,Map<String,Object> qury){
		Condition condition = new Condition();
		StringBuffer sb = new StringBuffer();
			condition.add("e.dwmc", Condition.FUZZY, qury.get("dwmc"));
			condition.add("c.tjsj", Condition.GREATER_EQUAL, qury.get("sbsj"));
			condition.add("c.tjsj", Condition.LESS_EQUAL, qury.get("sbsj2"));
			sb.append("	SELECT 	SQL_CALC_FOUND_ROWS	@rownum:=@rownum+1 AS 'key',");
			sb.append("		e.dwmc, d.MC as wsxm,c.id,c.sjid,DATE_FORMAT(c.tjsj,'%Y-%m-%d') AS tjsj,e.id as jgid,a.id as lcid,b.lcbz,");
			sb.append("		group_concat(concat(b.LCBZ,'.',h.DESCRIPTION)) as dqlcbz");
			sb.append("		FROM zs_splc a,dm_lclx d,zs_splcbz b,zs_spzx c,zs_jg e,fw_user_role g,fw_role h,(SELECT @rownum:=?) zs_jg");
			sb.append(condition.getSql());
			sb.append("		and a.ID=b.LCID AND b.ROLEID=g.role_id and g.USER_ID=? AND d.ID=a.LCLXID AND a.ZTBJ=2 and b.ROLEID=h.ID AND a.LCLXID<>'29' and a.LCLXID=? ");
			sb.append("		and c.LCBZID=b.id AND c.ztbj='Y' and e.ID=c.sjid group by e.dwmc order by c.TJSJ desc");
			sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(uid);
		params.add(lclxid);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		if(ls.size()!=0){
			String lcbzmx = this.jdbcTemplate.queryForObject("select group_concat(concat(a.LCBZ,'.',b.DESCRIPTION)) as lcbzmx from zs_splcbz a,fw_role b where a.LCID=? and a.ROLEID=b.ID order by a.LCBZ",
					new Object[]{ls.get(0).get("lcid")}, String.class);
			ob.put("lcbzmx", lcbzmx);
			ob.put("dqlcbz", ls.get(0).get("dqlcbz"));
		}
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		return ob;
	}
	/**
	 * 机构未审批明细查询
	 * @param pn,ps,uid,lcid,qury
	 * @return
	 */
	public Map<String,Object> jgspcx(int pn,int ps,int uid,int lclxid,Map<String,Object> qury){
		Condition condition = new Condition();
		StringBuffer sb = new StringBuffer();
		condition.add("e.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("c.tjsj", Condition.GREATER_EQUAL, qury.get("sbsj"));
		condition.add("c.tjsj", Condition.LESS_EQUAL, qury.get("sbsj2"));
		sb.append("	SELECT 	SQL_CALC_FOUND_ROWS	@rownum:=@rownum+1 AS 'key',");
		sb.append("		e.dwmc, d.MC as wsxm,c.id,c.sjid,DATE_FORMAT(c.tjsj,'%Y-%m-%d') AS tjsj,e.id as jgid,a.id as lcid,b.lcbz,");
		sb.append("		case f.yjxx when f.yjxx then f.yjxx else '无' end as yjxx,group_concat(concat(b.LCBZ,'.',h.DESCRIPTION)) as dqlcbz");
		sb.append("		FROM zs_splc a,dm_lclx d,zs_splcbz b,zs_spzx c,zs_jg e,zs_jgyjxxb f,fw_user_role g,fw_role h,(SELECT @rownum:=?) zs_jg");
		sb.append(condition.getSql());
		if(qury.containsKey("zsid")){
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			sb.append("and c.ZSJG_ID in (select id from zs_jg i where i.PARENTJGID="+hashids.decode(qury.get("zsid")+"")[0]+")");
		}
		sb.append("		and a.ID=b.LCID AND b.ROLEID=g.role_id and g.USER_ID=? AND d.ID=a.LCLXID AND a.ZTBJ=2 and b.ROLEID=h.ID AND a.LCLXID<>'29' and a.LCLXID=? and e.id=f.id");
		sb.append("		and c.LCBZID=b.id AND c.ztbj='Y' and e.ID=c.ZSJG_ID group by c.id order by c.TJSJ desc");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(uid);
		params.add(lclxid);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		if(ls.size()!=0){
			String lcbzmx = this.jdbcTemplate.queryForObject("select group_concat(concat(a.LCBZ,'.',b.DESCRIPTION)) as lcbzmx from zs_splcbz a,fw_role b where a.LCID=? and a.ROLEID=b.ID order by a.LCBZ",
					new Object[]{ls.get(0).get("lcid")}, String.class);
			ob.put("lcbzmx", lcbzmx);
			ob.put("dqlcbz", ls.get(0).get("dqlcbz"));
		}
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		return ob;
	}
	/**
	 * 人员未审批明细查询
	 * @param pn,ps,uid,lcid,qury
	 * @return
	 */
	public Map<String,Object> ryspcx(int pn,int ps,int uid,int lclxid,Map<String,Object> qury){
		Condition condition = new Condition();
		StringBuffer sb = new StringBuffer();
		String zdm="FZY_ID";
		String bm2="zs_fzysws";
		String bm="";
		String zd="";
		String where="";
		switch(lclxid){
		case 13:bm="zs_fzyzzy";break;
		case 14:bm="zs_fzyswszj";break;
		case 15:bm="zs_fzyzx";break;
		case 18:bm="zs_fzybgsp";break;
		case 20:bm="zs_fzybasp";zdm="FZYSWS_ID";break;
		case 43:bm="zs_cyryzx";bm2="zs_cyry";zdm="CYRY_ID";break;
		case 44:bm="zs_cyryzzy";bm2="zs_cyry";zdm="CYRY_ID";break;
		
		case 46:bm="zs_fzyzzy";bm2="zs_jg l,zs_fzysws";zdm="FZY_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 5:bm="zs_zyswsbasp";bm2="zs_jg l,zs_zysws";zdm="ZYSWS_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 6:bm="zs_zyswsbgsp";bm2="zs_jg l,zs_zysws";zdm="ZYSWS_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 7:bm="zs_zyswszfzy";bm2="zs_jg l,zs_zysws";zdm="ZYSWS_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 8:bm="zs_zyswszj";bm2="zs_jg l,zs_zysws";zdm="ZYSWS_ID";zd="l.dwmc,";
		 	where=" and c.ZSJG_ID = l.id ";break;
		case 9:bm="zs_zyswssndz";bm2="zs_jg l,zs_zysws";zdm="RY_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 10:bm="zs_zyswszx";bm2="zs_jg l,zs_zysws";zdm="ZYSWS_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 12:bm="zs_zcswsnj";bm2="zs_jg l,zs_zysws";zdm="SWS_ID";zd="l.dwmc,";
			where=" and c.ZSJG_ID = l.id ";break;
		case 38:bm="zs_zysws";bm2="zs_jg l,zs_zysws";zdm="id";zd="l.dwmc,";
		where=" and c.ZSJG_ID = l.id ";break;
		case 39:bm="zs_zysws";bm2="zs_jg l,zs_zysws";zdm="id";zd="l.dwmc,";
		where=" and c.ZSJG_ID = l.id ";break;
		}
		condition.add("f.xming", Condition.FUZZY, qury.get("xming"));
		condition.add("c.tjsj", Condition.GREATER_EQUAL, qury.get("sbsj"));
		condition.add("c.tjsj", Condition.LESS_EQUAL, qury.get("sbsj2"));
		sb.append("	SELECT 	SQL_CALC_FOUND_ROWS	@rownum:=@rownum+1 AS 'key',");
		sb.append("		f.xming,f.sfzh,k.MC as xb, "+zd+" ");
		sb.append("		d.MC as wsxm,c.id,c.sjid,DATE_FORMAT(c.tjsj,'%Y-%m-%d') AS tjsj,a.id as lcid,b.lcbz,");
		sb.append("		group_concat(concat(b.LCBZ,'.',h.DESCRIPTION)) as dqlcbz");
		sb.append("		FROM zs_splc a,dm_lclx d,zs_splcbz b,zs_spzx c,fw_user_role g,zs_ryjbxx f,"+bm+" i,"+bm2+" j,");
		sb.append("		fw_role h,dm_xb k,(SELECT @rownum:=?) zs_jg");
		sb.append(condition.getSql()+where);
		if(qury.containsKey("zsid")){
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			sb.append("and c.ZSJG_ID in (select id from zs_jg l where l.PARENTJGID="+hashids.decode(qury.get("zsid")+"")[0]+")");
		}
		sb.append("		and a.ID=b.LCID AND b.ROLEID=g.role_id ");
		sb.append("		 AND d.ID=a.LCLXID AND a.ZTBJ=2 and g.USER_ID=?");
		sb.append("		and b.ROLEID=h.ID  AND a.LCLXID<>'29' and a.LCLXID=?");
		sb.append("		 and c.SJID=i.ID and j.ID=i."+zdm+" and f.ID=j.RY_ID and k.ID=f.XB_DM");
		sb.append("		and c.LCBZID=b.id AND c.ztbj='Y' ");
		sb.append("		  group by c.sjid order by c.TJSJ desc");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(uid);
		params.add(lclxid);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		if(ls.size()!=0){
			String lcbzmx = this.jdbcTemplate.queryForObject(
					"select group_concat(concat(a.LCBZ,'.',b.DESCRIPTION)) as lcbzmx from zs_splcbz a,fw_role b where a.LCID=? and a.ROLEID=b.ID order by a.LCBZ",
					new Object[]{ls.get(0).get("lcid")}, String.class);
			ob.put("lcbzmx", lcbzmx);
			ob.put("dqlcbz", ls.get(0).get("dqlcbz"));
		}
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		return ob;
	}
	
	/**
	 * 审批详细信息查看
	 * @param sjid
	 * @return List
	 */
	public Object spmxxx (String lcid,String sjid){
		StringBuffer sb = new StringBuffer();
		switch (lcid) {
		case "jgbgsp"://机构变更审批详细信息
			return this.jdbcTemplate.queryForList("select MC,XZHI,JZHI from zs_jgbgxxb where jgbgspb_id = ?",new Object[]{sjid});
		case "jgzxsp"://机构注销审批详细信息
			return this.jdbcTemplate.queryForMap("select b.MC as zxyy,a.BZ as zxsm from zs_jgzx a,dm_jgzxyy b where a.id=? and b.ID=a.zxyy_id ",new Object[]{sjid});
		case "jghbsp"://机构合并审批详细信息
			return this.jdbcTemplate.queryForMap("select * from zs_jghb a where a.id=? ",new Object[]{sjid});
		case "zyba"://执业备案审批详细信息
			sb.append("		select c.ID,c.XMING,d.MC as XB,c.SFZH,c.TXDZ,c.SRI,c.YZBM,c.DHHM,c.BYYX,c.YDDH,e.MC as CS,g.MC as ZZMM,c.BYSJ,c.XPIAN,");
			sb.append("		b.ZYZGZSBH,DATE_FORMAT(b.ZGZSQFRQ,'%Y-%m-%d') as ZGZSQFRQ, h.MC as ZW,DATE_FORMAT(b.RHSJ,'%Y-%m-%d') AS RHSJ,i.mc as XL,");
			sb.append("			DATE_FORMAT(b.SWDLYWKSSJ,'%Y-%m-%d') as SWDLYWKSSJ,b.ZYZSBH,DATE_FORMAT(b.ZYZCRQ,'%Y-%m-%d') as ZYZCRQ,b.GRHYBH,");
			sb.append("		case b.czr_dm when 1 then \"是\"  when 2 then \"否\" else null end as CZR,");
			sb.append("		case b.fqr_dm when 1 then \"是\"  when 2 then \"否\" else null end as FQR,");
			sb.append("		b.CZE,c.RYDAZT");
			sb.append("		from zs_zyswsbasp a,zs_zysws b,zs_ryjbxx c,dm_xb d,dm_cs e,dm_mz f,dm_zzmm g,dm_zw h,dm_xl i");
			sb.append("		where a.ZYSWS_ID=b.ID and b.RY_ID=c.id and d.ID=c.XB_DM and e.ID=c.CS_DM and f.ID=c.MZ_DM");
			sb.append("		and g.ID=c.ZZMM_DM and h.ID=b.ZW_DM  and i.id=c.xl_dm and a.id=?");
			Map<String, Object> zyba = this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{sjid});
			zyba.put("zyjl", this.jdbcTemplate.queryForList("select id,qzny,xxxx,zmr from zs_jl where ry_ID=? and QZNY is not null and qzny<>'' order by qzny",
					new Object[]{zyba.get("ID")}));
			return zyba;
		case "zybgsp"://执业变更审批详细信息
			return this.jdbcTemplate.queryForList("select MC,XZHI,JZHI from zs_zyswsbgxxb where bgspb_id = ?",new Object[]{sjid});
		case "zyzxsp"://执业注销审批详细信息
			return this.jdbcTemplate.queryForMap(
					"select c.MC as zxlx,DATE_FORMAT(b.ZXRQ,'%Y-%m-%d') as ZXRQ,b.SWSYJ from zs_zyswszx b,dm_zyswszxyy c where b.ZYSWSZXYY_DM=c.ID and b.id=?",
					new Object[]{sjid});
		case "zyzcsp"://执业转出审批详细信息
			sb.append("		select b.ZYZGZSBH,DATE_FORMAT(b.ZGZSQFRQ,'%Y-%m-%d') as ZGZSQFRQ,b.ZYZSBH,DATE_FORMAT(b.ZYZCRQ,'%Y-%m-%d') as ZYZCRQ,");
			sb.append("		DATE_FORMAT(b.SWDLYWKSSJ,'%Y-%m-%d') as ywkssj,c.mc as zw,case b.SZ_DM when 0 then '否' when 1 then '是' else null end as sz,");
			sb.append("		case b.FQR_DM when 2 then '否' when 1 then '是' else null end as fqr,");
			sb.append("		case b.CZR_DM when 2 then '否' when 1 then '是' else null end as czr,b.CZE,");
			sb.append("		DATE_FORMAT(b.JSSJ,'%Y-%m-%d') as JSSJ,DATE_FORMAT(b.RHSJ,'%Y-%m-%d') as RHSJ");
			sb.append("		 from zs_zysws b,dm_zw c where b.id=? and c.id=b.ZW_DM");
			return this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{sjid});
		case "zyzjsp"://执业转籍审批详细信息
			return this.jdbcTemplate.queryForMap("select ZJYY,ZJYYRQ,DRS,XJGMC,XJGDH from zs_zyswszj where id = ?",new Object[]{sjid});
		case "zyzfzy"://执业转非执业审批详细信息
			return this.jdbcTemplate.queryForMap("select FZYSQ,XDWYJ,DATE_FORMAT(TBRQ,'%Y-%m-%d') as TBRQ from zs_zyswszfzy where id = ?",new Object[]{sjid});
		case "fzyba"://非执业备案审批详细信息
			sb.append("select b.ID,c.XMING,d.MC as XB,c.SFZH,c.TXDZ,c.SRI,c.YZBM,c.DHHM,c.BYYX,c.YDDH,e.MC as CS,g.MC as ZZMM,c.BYSJ,c.XPIAN,");
			sb.append("		b.ZYZGZSBH,DATE_FORMAT(b.ZGZSQFRQ,'%Y-%m-%d') as ZGZSQFRQ,b.FZYHYBH, h.MC as ZW,b.ZZDW,DATE_FORMAT(b.RHSJ,'%Y-%m-%d') AS RHSJ,i.mc as XL");
			sb.append("		from zs_fzybasp a,zs_fzysws b,zs_ryjbxx c,dm_xb d,dm_cs e,dm_mz f,dm_zzmm g,dm_zw h,dm_xl i");
			sb.append("		where a.FZYSWS_ID=b.ID and b.RY_ID=c.id and d.ID=c.XB_DM and e.ID=c.CS_DM and f.ID=c.MZ_DM");
			sb.append("		and g.ID=c.ZZMM_DM and h.ID=b.ZW_DM and a.id=? and i.id=c.xl_dm");
			Map<String, Object> fzba = this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{sjid});
			fzba.put("fzjl", this.jdbcTemplate.queryForList("select id,qzny,xxxx,zmr from zs_fzyjl where FZY_ID=? and QZNY is not null and qzny<>'' order by qzny",
					new Object[]{fzba.get("ID")}));
			return fzba;
		case "fzyzjsp"://非执业转籍审批详细信息
			return this.jdbcTemplate.queryForMap("select ZJYY,ZJYYRQ,TBRQ from zs_fzyswszj where id = ?",new Object[]{sjid});
		case "fzyzxsp"://非执业注销审批详细信息
			return this.jdbcTemplate.queryForMap("select b.mc,a.ZXYY,a.LRR from zs_fzyzx a,dm_fzyzxlb b where a.ZXLB_DM=b.ID and a.id=?",new Object[]{sjid});
		case "fzyzzysp"://非执业转执业审批详细信息
			return this.jdbcTemplate.queryForMap("select a.ZYSQ,a.YDW,b.DWMC from zs_fzyzzy a,zs_jg b where a.XDW=b.ID and a.id=?",new Object[]{sjid});
		case "fzybgsp"://非执业变更审批详细信息
			return this.jdbcTemplate.queryForList("select MC,XZHI,JZHI from zs_fzybgxxb where BGSPB_ID = ?",new Object[]{sjid});
		
		case "jgsl"://事务所设立备案审批详细信息
			sb.append("		select a.dwmc,(select mc from dm_cs where a.CS_DM =ID) as cs,	a.fddbr,a.dzhi,a.sjlzxsbwh,a.zcdz, ");
			sb.append("		date_format(a.sglzxsbsj,'%Y-%m-%d') as sglzxsbsj,date_format(a.zjpzsj,'%Y-%m-%d')");
			sb.append("		as zjpzsj,a.yzbm,a.zjpzwh,a.czhen,a.dhua,a.szyx, ");
			sb.append("		a.txyxming,a.xtyyx,a.xtyphone,a.JGZCH as zsbh,	a.zczj,a.jyfw,");
			sb.append("		(select count(id) from zs_zysws where jg_id=a.id)+");
			sb.append("		(select count(id) from zs_cyry where jg_id=a.id and CYRYZT_DM=1) as zrs, ");
			sb.append("		(select mc from dm_jgxz where a.JGXZ_DM =ID) as swsxz,a.szphone,a.gsyhmcbh,a.dzyj,a.yhdw,date_format(a.yhsj,'%Y-%m-%d') as yhsj, ");
			sb.append("		a.gzbh,a.gzdw,a.gzry,date_format(a.gzsj,'%Y-%m-%d') as gzsj,a.yzbh,a.yzdw,");
			sb.append("		a.yzry,date_format(a.yzsj,'%Y-%m-%d') as yzsj, ");
			sb.append("		a.tthybh,date_format(a.rhsj,'%Y-%m-%d') as rhsj,a.khh,a.khhzh,a.fj,a.swdjhm,a.jbqk, ");
			sb.append("		a.glzd,a.gddh,a.bgcszczm,a.yyzzhm,DATE_FORMAT(a.swszsclsj,'%Y-%m-%d') AS clsj,");
			sb.append("		a.jgdmzh,a.wangzhi,a.CS_DM as csdm,a.JGXZ_DM as jgxzdm from	zs_jg a 	"); 
			sb.append("		WHERE	a.id =?");
			String sql = "SELECT @rownum:=@rownum+1 AS 'key',b.* FROM zs_nbjgsz b,(SELECT @rownum:=0) zs_jg WHERE  b.jg_id=? ";
			List<Map<String, Object>> tl = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{sjid});
			Map<String,Object> ll =tl.get(0);
			ll.put("nbjgsz", this.jdbcTemplate.queryForList(sql,new Object[]{sjid}));
			return ll;
		case "jgnj"://机构年检
			sb.append("		SELECT ");
			sb.append("		c.dwmc,c.JGZCH as zsbh,d.mc as jgxz,c.yzbm,c.DZHI as bgdz,c.DHUA as dhhm,a.*,");
			sb.append("		case a.ztdm when 3 then '已年检' when 2 then '已自检'  "
					+ " else null end as njzt, CASE a.WGCL_DM WHEN 1 THEN '年检予以通过' WHEN 2 THEN '年检不予通过，"
					+ "责令2个月整改' WHEN 6 THEN '年检不予以通过' WHEN 7 THEN '资料填写有误，请重新填写' ELSE NULL END AS njcl,"
					+ "DATE_FORMAT(a.zjsj,'%Y-%m-%d') AS zjrq,DATE_FORMAT(c.SWSZSCLSJ ,'%Y-%m-%d') AS clsj,"
					+ "DATE_FORMAT(a.fzrsj,'%Y-%m-%d') AS qzrq");
			sb.append("	 FROM  zs_jg_njb a,zs_jg c,dm_jgxz d where 1=1 ");
			sb.append("	and a.ZSJG_ID = c.ID  and d.ID = c.JGXZ_DM and a.id=?");
			return this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{sjid});
		case "zyzs"://执业税务师转所审批详细信息
			sb.append("		select (select dwmc from zs_jg c where c.id=a.yjg_id) as yjg,");
			sb.append("		(select dwmc from zs_jg b where id=a.xjg_id) as xjg,");
			sb.append("		a.ZYSWSYJ,a.YSWSYJ,date_format(a.YSWSYJRQ, '%Y-%m-%d') as YSWSYJRQ,");
			sb.append("		a.XSWSYJ,date_format(a.XSWSYJRQ, '%Y-%m-%d') as XSWSYJRQ,");
			sb.append("		date_format(a.TBRQ, '%Y-%m-%d') as TBRQ,a.SZQM,date_format(a.QMSJ, '%Y-%m-%d') as QMSJ,");
			sb.append("		a.XSZQM,date_format(a.XQMSJ, '%Y-%m-%d') as XQMSJ");
			sb.append("		 from zs_zyswssndz a where a.id=?");
			return this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{sjid});
		case "zyswsnj":
			sb.append("	select max( a.nd),a.nd,a.ID,c.XMING,c.DHHM,d.ZYZSBH,d.ZYZGZSBH,");
			sb.append("	b.dwmc,a.swsfzryj,e1.MC as xb,f1.mc as xl,a.CZBL,DATE_FORMAT(a.SWSFZRSJ,'%Y-%m-%d') AS SWSFZRSJ,a.SWSFZR,");
			sb.append("	DATE_FORMAT(c.SRI,'%Y-%m-%d') AS SRI,c.SFZH,a.BAFS,a.ZJWGDM,a.NJWGDM,a.ZJ,a.SWSFZRYJ,");
			sb.append("	CASE a.ZTDM WHEN 1 THEN '保存'  WHEN 2 THEN '未审批' WHEN 0 THEN '退回' WHEN 3 THEN '已年检' ELSE NULL END AS njzt,");
			sb.append("	DATE_FORMAT( f.SPSJ,'%Y-%m-%d') AS spsj,f.SPYJ,f.SPRNAME");
			sb.append("	FROM  ( zs_zcswsnj a left join(zs_jg b,zs_ryjbxx c,zs_zysws d,dm_xb e1,dm_xl f1) on (");
			sb.append("	a.ZSJG_ID=b.ID AND c.id=d.RY_ID");
			sb.append("	and c.XL_DM=f1.id and c.XB_DM=e1.ID");
			sb.append("	AND d.id = a.sws_id ) ) left join(zs_spzx e,zs_spxx f,zs_splcbz g,zs_splc h) on (");
			sb.append("	f.SPID=e.ID AND g.ID=f.LCBZID and a.ZTDM <> 1");
			sb.append("	AND h.ID=g.LCID AND h.LCLXID='12'");
			sb.append("	AND a.ID=e.SJID )");
			sb.append("	WHERE  a.id=? group by nd");
			return this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{sjid});
		}
		return null;
	}
	
	 /**
	  * 中心端审批审核处理方法
	  * @param Map:spid,uid,uname,spyj,ispass
	  * @return boolean
	  * @throws Exception
	  */
		public boolean sptj(Map<String,Object> spsq) throws Exception{
		 StringBuffer sb = new StringBuffer();
		 sb.append("	 select a.ID,b.LCBZ,b.SPBZLX,b.BHBZLX,b.LCID,c.LCLXID,b.ROLEID,e.SJID from zs_spxx a,zs_splcbz b,zs_splc c,zs_spzx e,fw_user_role f ");
		 sb.append("	 where a.LCBZID=b.ID and b.LCID=c.ID and a.SPID=e.ID and f.ROLE_ID=b.ROLEID and c.ZTBJ=2");
		 sb.append("	 and a.spid = ? ");
		 sb.append("	 and f.USER_ID=?");
		 Map<String, Object> mp = this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{spsq.get("spid"),spsq.get("uid")});
		 String sql ="update zs_spxx set SPYJ=?,ISPASS=?,USERID=?,SPRNAME=?,SPSJ=sysdate() where id =?";
		 Object ispass=new Object();
		 if(spsq.containsKey("ispass")){
			 ispass=spsq.get("ispass");
		 }else if(spsq.containsKey("jgnj")){
			 if((int)spsq.get("jgnj")==1){
				 ispass="Y";
			 }else{
				 ispass="N";
			 }
		 }else if(spsq.containsKey("zynj")){
			 if((int)spsq.get("zynj")==1){
				 ispass="Y";
			 }else{
				 ispass="N";
			 }
		 }
		 this.jdbcTemplate.update(sql,new Object[]{spsq.get("spyj"),ispass,spsq.get("uid"),spsq.get("uname"),mp.get("ID")});
		 if(spsq.containsKey("ispass")&&spsq.get("ispass").equals("Y")){
			 int c = (int)mp.get("SPBZLX");
			 if(c==1||c==2){
				 this.jdbcTemplate.update("update zs_spzx set ZTBJ='N', QRBJ=null where id =?",new Object[]{spsq.get("spid")});
				 switch((int)mp.get("LCLXID")){
				 case 1:
					 this.jdbcTemplate.update("update zs_jg set TGZT_DM=6, SGLZXYJ=?,SPR=?,JGZT_DM=11,YXBZ=1 where id =?",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 if(this.jdbcTemplate.queryForObject("select ROLE_ID from fw_user_role where USER_ID=?",new Object[]{spsq.get("uid")},int.class)!=4){
						 this.jdbcTemplate.update("update fw_user_role set ROLE_ID=114 where USER_ID =?",
								 new Object[]{spsq.get("uid")});
					 }else if(this.jdbcTemplate.queryForList("select id from zs_jg where id=? and parentjgid is not null",new Object[]{mp.get("SJID")}).size()!=0){
						 if(this.jdbcTemplate.queryForList("select id from zs_jg where id=? and parentjgid is not null and parentjgid>0",new Object[]{mp.get("SJID")}).size()!=0){
							 this.jdbcTemplate.update("update fw_user_role set ROLE_ID=17 where USER_ID =?",
									 new Object[]{spsq.get("uid")});
						 }else{
							 this.jdbcTemplate.update("update fw_user_role set ROLE_ID=18 where USER_ID =?",
									 new Object[]{spsq.get("uid")});
						 }
					 }else{
						 this.jdbcTemplate.update("update fw_user_role set ROLE_ID=3 where USER_ID =?",
								 new Object[]{spsq.get("uid")});
					 }
					 break;
				 case 2:
					 this.jdbcTemplate.update("update zs_jgbgspb set SPZT_DM='8',YJIAN=?,SPRQ=sysdate(),SPR_ID=? where id =?",
							 new Object[]{spsq.get("spyj"),spsq.get("uid"),mp.get("SJID")});
					 this.jdbcTemplate.update("update zs_jg a,zs_jgbglsb b,zs_jgbgspb c set a.DWMC=b.DWMC,a.CS_DM=b.CS_DM,a.JGXZ_DM=b.JGXZ_DM,a.DZHI=b.DZHI,a.ZCZJ=b.ZCZJ,a.ZCDZ=b.ZCDZ,a.YYZZHM=b.YYZZHM,a.SWSZSCLSJ=b.SWSZSCLSJ where c.id =? and c.JGBGLSB_ID = b.id and c.jg_id = a.id",
							 new Object[]{mp.get("SJID")});
					 for(Map<String, Object> rec:this.jdbcTemplate.queryForList("select a.*,b.jg_id as jgid from zs_jgbgxxb a,zs_jgbgspb b where a.JGBGSPB_ID=b.id and b.id=?",mp.get("SJID"))){//插入变更项目历史信息
							this.jdbcTemplate.update("insert into zs_jglsbgxxb (MC,JZHI,XZHI,GXSJ,JGB_ID,ID) values(?,?,?,sysdate(),?,?)",
									new Object[]{rec.get("MC"),rec.get("JZHI"),rec.get("XZHI"),rec.get("jgid"),new Common().newUUID()});
						}
					 break;
				 case 4:
					 this.jdbcTemplate.update("update zs_jg a,zs_jgzx b set a.JGZT_DM='9',a.yxbz='0',b.SPZT='2',b.ZXRQ=sysdate() where b.id =? and a.id=b.jg_id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 5:
					 this.jdbcTemplate.update("update zs_zyswsbasp a,zs_zysws b,zs_ryjbxx c set a.SPZT_DM='2',a.YJIAN=?,a.SPRQ=sysdate(),a.SPR=?,c.RYZT_DM='1',c.YXBZ='1',b.RHSJ=sysdate(),b.ZYZT_DM='1',b.RYSPGCZT_DM='1',b.YXBZ='1',b.ZYZCRQ=sysdate() where a.id =? and a.ZYSWS_ID=b.id and b.ry_id=c.id",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 6:
					 this.jdbcTemplate.update("update zs_zyswsbgsp set SPZT_DM='2',YJIAN=?,SPRQ=sysdate(),SPR=? where id =?",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 this.jdbcTemplate.update(
							 "update zs_zysws a,zs_ryjbxx b,zs_zyswsbgls c,zs_zyswsbgsp d set a.ZYZGZSBH=c.ZYZGZSBH,a.ZGZSQFRQ=c.ZGZSQFRQ,a.ZYZSBH=c.ZYZSBH,a.ZYZCRQ=c.ZYZCRQ,a.SWDLYWKSSJ=c.SWDLYWKSSJ,a.CZE=c.CZE,a.CZR_DM=c.CZR_DM,a.FQR_DM=c.FQR_DM,a.RHSJ=c.RHSJ,a.GRHYBH=c.GRHYBH,a.ZW_DM=c.ZW_DM,b.XMING=c.XMING,"
							 + "b.SFZH=c.SFZH,b.CS_DM=c.CS_DM,b.XB_DM=c.XB_DM,b.MZ_DM=c.MZ_DM,b.XL_DM=c.XL_DM,b.ZZMM_DM=c.ZZMM_DM,b.SRI=c.SRI,b.TXDZ=c.TXDZ,b.YDDH=c.YDDH,b.YZBM=c.YZBM,b.DHHM=c.DHHM,b.BYYX=c.BYYX,b.BYSJ=c.BYSJ,b.RYDAZT=c.RYDAZT,a.RYSPGCZT_DM='1'  where a.RY_ID=b.ID and c.ID=d.SWSBGLSB_ID and d.ZYSWS_ID=a.ID and d.ID=?"
					 		,new Object[]{mp.get("SJID")});
					 for(Map<String, Object> rec:this.jdbcTemplate.queryForList("select a.*,b.ZYSWS_ID as zyid from zs_zyswsbgxxb a,zs_zyswsbgsp b where a.BGSPB_ID=b.id and b.id=?",
							 		mp.get("SJID"))){//插入变更项目历史信息
								this.jdbcTemplate.update("insert into zs_zyswslsbgxxb (MC,JZHI,XZHI,GXSJ,ZYSWS_ID,ID) values(?,?,?,sysdate(),?,?)",
										new Object[]{rec.get("MC"),rec.get("JZHI"),rec.get("XZHI"),rec.get("zyid"),new Common().newUUID()});
							}
					 break;
				 case 7:
					 Map<String, Object> fzyy = this.jdbcTemplate.queryForMap("select c.ID,b.ZYZGZSBH,b.ZGZSQFRQ,b.ZW_DM from zs_zyswszfzy a,zs_zysws b,zs_ryjbxx c where a.ZYSWS_ID=b.ID and c.ID=b.RY_ID and a.id=?",
							 new Object[]{mp.get("SJID")});
					 List<Object> listValue = new ArrayList<Object>();  //Map转List
						Iterator<String> it = fzyy.keySet().iterator();  
						while (it.hasNext()) {  
							String key = it.next().toString();  
							listValue.add(fzyy.get(key));  
						};
					 Number fzyid = this.insertAndGetKeyByJdbc("insert into zs_fzysws (RY_ID,ZYZGZSBH,ZGZSQFRQ,ZW_DM,FZYZT_DM,RYSPGCZT_DM,LRRQ,YXBZ) values(?,?,?,?,1,1,sysdate(),1)",
							 listValue.toArray(),new String[] {"ID"});
					 this.jdbcTemplate.update("update zs_zyswszfzy a,zs_zysws b,zs_ryjbxx c set b.RYSPGCZT_DM=1,b.ZYZT_DM=9,b.YXBZ=0,c.RYSF_DM=2,a.SPZT_DM=2,a.FZYSWS_ID=?,a.SGLZXYJRQ=sysdate() where a.ZYSWS_ID=b.ID and c.ID=b.RY_ID and a.id=?",
							 new Object[]{fzyid,mp.get("SJID")});
					 break;
				 case 8:
					 this.jdbcTemplate.update("update zs_zyswszj a,zs_zysws b,zs_ryjbxx c set a.SPZT_DM='2',c.RYZT_DM='6',c.YXBZ='0',b.ZYZT_DM='6',b.RYSPGCZT_DM='1',b.YXBZ='0' where a.id =? and a.ZYSWS_ID=b.id and b.ry_id=c.id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 9:
					 this.jdbcTemplate.update("update zs_zyswssndz a,zs_zysws b  set a.SGLZXYJ=?,a.SGLZXYJRQ=sysdate(),a.SPZT_DM=2,b.RYSPGCZT_DM='1',b.JG_ID=a.XJG_ID where a.id =? and a.RY_ID=b.id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 10:
					 this.jdbcTemplate.update("update zs_zyswszx a,zs_zysws b,zs_ryjbxx c set a.SPZT_DM='2',c.RYZT_DM='5',c.YXBZ='0',b.ZYZT_DM='5',b.RYSPGCZT_DM='1',b.YXBZ='0' where a.id =? and a.ZYSWS_ID=b.id and b.ry_id=c.id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 13:
					 Map<String, Object> zzyy = this.jdbcTemplate.queryForMap("select c.ID,b.ZYZGZSBH,b.ZGZSQFRQ,b.ZW_DM,a.XDW from zs_fzyzzy a,zs_fzysws b,zs_ryjbxx c where a.FZY_ID=b.ID and c.ID=b.RY_ID and a.id=?",
							 new Object[]{mp.get("SJID")});
					 List<Object> listValuezzyy = new ArrayList<Object>();  //Map转List
						Iterator<String> it2 = zzyy.keySet().iterator();  
						while (it2.hasNext()) {  
							String key = it2.next().toString();  
							listValuezzyy.add(zzyy.get(key));  
						};
					 Number zyid = this.insertAndGetKeyByJdbc("insert into zs_zysws (RY_ID,ZYZGZSBH,ZGZSQFRQ,ZW_DM,JG_ID,ZYZT_DM,RYSPGCZT_DM,LRRQ,YXBZ) values(?,?,?,?,?,1,1,sysdate(),1)",
							 listValuezzyy.toArray(),new String[] {"ID"});
					 this.jdbcTemplate.update("update zs_fzyzzy a,zs_fzysws b,zs_ryjbxx c set b.RYSPGCZT_DM=1,b.FZYZT_DM=7,b.YXBZ=0,c.RYSF_DM=1,a.RYSPZT=1,a.ZYSWS_ID=?,a.XDWYJ=?,a.LRR=?,a.BDRQ=sysdate() where a.FZY_ID=b.ID and c.ID=b.RY_ID and a.id=?",
							 new Object[]{zyid,spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 14:
					 this.jdbcTemplate.update("update zs_fzyswszj a,zs_fzysws b,zs_ryjbxx c set a.RYSPZT_DM='1',c.RYZT_DM='6',c.YXBZ='0',b.FZYZT_DM='6',b.RYSPGCZT_DM='1',b.YXBZ='0' where a.id =? and a.FZY_ID=b.id and b.ry_id=c.id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 15:
					 this.jdbcTemplate.update("update zs_fzyzx a,zs_fzysws b,zs_ryjbxx c set a.RYSPZT='2',a.SPSJ=sysdate(),c.RYZT_DM='5',c.YXBZ='0',b.FZYZT_DM='5',b.RYSPGCZT_DM='1',b.YXBZ='0' where a.id =? and a.FZY_ID=b.id and b.ry_id=c.id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 18:
					 this.jdbcTemplate.update("update zs_fzybasp set SPZT_DM='2',YJIAN=?,SPRQ=sysdate(),SPR=? where id =?",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 this.jdbcTemplate.update(
							 "update zs_fzysws a,zs_ryjbxx b,zs_fzybgls c,zs_fzybgsp d set a.ZYZGZSBH=c.ZYZGZSBH,a.ZGZSQFRQ=c.ZGZSQFRQ,a.FZYZCZSBH=c.FZYZCZSBH,a.FZYZCRQ=c.FZYZCRQ,a.ZZDW=c.ZZDW,a.ZW_DM=c.ZW_DM,a.DJRQ=c.DJRQ,a.DJR=c.DJR,a.SFQTZJZYSWS=c.SFQTZJZYSWS,a.QTZJZGZSBH=c.QTZJZGZSBH,a.QTZJZYZSBH=c.QTZJZYZSBH,"
							 + "a.FZYHYBH=c.FZYHYBH,a.SCBH=c.SCBH,a.CZE=c.CZE,a.CZZMSBH=c.CZZMSBH,a.FQR_DM=c.FQR_DM,a.CZR_DM=c.CZR_DM,a.ZSJGID=c.ZSJGID,a.FZYSCBH=c.FZYSCBH,a.XZSJGID=c.XZSJGID,a.RHSJ=c.RHSJ,b.XMING=c.XMING,"
							 + "b.SFZH=c.SFZH,b.CS_DM=c.CS_DM,b.XB_DM=c.XB_DM,b.MZ_DM=c.MZ_DM,b.XL_DM=c.XL_DM,b.ZZMM_DM=c.ZZMM_DM,b.SRI=c.SRI,b.TXDZ=c.TXDZ,b.YDDH=c.YDDH,b.YZBM=c.YZBM,b.DHHM=c.DHHM,a.RYSPGCZT_DM='1'  where a.RY_ID=b.ID and c.ID=d.BGLSBID and d.FZY_ID=a.ID and d.ID=?"
					 		,new Object[]{mp.get("SJID")});
					 for(Map<String, Object> rec:this.jdbcTemplate.queryForList("select a.*,b.FZY_ID as fzyid from zs_fzybgxxb a,zs_fzybgsp b where a.BGSPB_ID=b.id and b.id=?",
							 		mp.get("SJID"))){//插入变更项目历史信息
								this.jdbcTemplate.update("insert into zs_zyswslsbgxxb (MC,JZHI,XZHI,GXSJ,FZY_ID,ID) values(?,?,?,sysdate(),?,?)",
										new Object[]{rec.get("MC"),rec.get("JZHI"),rec.get("XZHI"),rec.get("fzyid"),new Common().newUUID()});
							}
					 break;
				 case 20://select * from zs_splcbz where lcid='402881831be2e6af011be3c184d2003a' 审批步骤需改为2
					 this.jdbcTemplate.update("update zs_fzybasp a,zs_fzysws b,zs_ryjbxx c set a.SPZT_DM='2',a.SPR=?,c.RYZT_DM='1',c.YXBZ='1', b.RHSJ=sysdate(),b.FZYZT_DM='1',b.RYSPGCZT_DM='1',b.YXBZ='1',b.FZYZCRQ=sysdate()  where a.id =? and a.FZYSWS_ID=b.id and b.ry_id=c.id",
							 new Object[]{spsq.get("uid"),mp.get("SJID")});
					 break;
				 case 38:
					 this.jdbcTemplate.update("update zs_zysws a set a.RYSPZT_DM='1',a.jg_id='-2' where a.id =?",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 39:
					 this.jdbcTemplate.update("update zs_zysws a set a.RYSPZT_DM='1'  where a.id =?",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 46:
					 Map<String, Object> zzyy2 = this.jdbcTemplate.queryForMap("select c.ID,b.ZYZGZSBH,b.ZGZSQFRQ,b.ZW_DM,a.XDW from zs_fzyzzy a,zs_fzysws b,zs_ryjbxx c where a.FZY_ID=b.ID and c.ID=b.RY_ID and a.id=?",
							 new Object[]{mp.get("SJID")});
					 List<Object> listValuezzyy2 = new ArrayList<Object>();  //Map转List
						Iterator<String> it3 = zzyy2.keySet().iterator();  
						while (it3.hasNext()) {  
							String key = it3.next().toString();  
							listValuezzyy2.add(zzyy2.get(key));  
						};
					 Number zyid2 = this.insertAndGetKeyByJdbc("insert into zs_zysws (RY_ID,ZYZGZSBH,ZGZSQFRQ,ZW_DM,JG_ID,ZYZT_DM,RYSPGCZT_DM,LRRQ,YXBZ) values(?,?,?,?,?,1,1,sysdate(),1)",
							 listValuezzyy2.toArray(),new String[] {"ID"});
					 this.jdbcTemplate.update("update zs_fzyzzy a,zs_fzysws b,zs_ryjbxx c set b.RYSPGCZT_DM=1,b.FZYZT_DM=7,b.YXBZ=0,c.RYSF_DM=1,a.RYSPZT=2,a.ZYSWS_ID=?,a.XDWYJ=?,a.LRR=?,a.BDRQ=sysdate() where a.FZY_ID=b.ID and c.ID=b.RY_ID and a.id=?",
							 new Object[]{zyid2,spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 };
			 }else{
				 this.jdbcTemplate.update("update zs_spzx set LCBZID=?, QRBJ=null where id =?",
						 new Object[]{this.jdbcTemplate.queryForObject("select id from zs_splcbz where lcid=? and lcbz=?",
								 new Object[]{mp.get("LCID"),(int)mp.get("LCBZ")+1}, Object.class),spsq.get("spid")});
			 };
		 }else if(spsq.containsKey("ispass")&&spsq.get("ispass").equals("N")){
			 int c = (int)mp.get("BHBZLX");
			 if(c==1||c==2){
				 this.jdbcTemplate.update("update zs_spzx set ZTBJ='N', QRBJ=null where id =?",new Object[]{spsq.get("spid")});
				 switch((int)mp.get("LCLXID")){
				 case 1:
					 this.jdbcTemplate.update("update zs_jg set  SGLZXYJ=?,SPR=?,JGZT_DM=6 where id =?",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 2:
					 this.jdbcTemplate.update("update zs_jgbgspb set SPZT_DM='3',YJIAN=?,SPRQ=sysdate(),SPR_ID=? where id =?",
							 new Object[]{spsq.get("spyj"),spsq.get("uid"),mp.get("SJID")});
					 break;
				 case 4:
					 this.jdbcTemplate.update("update zs_jgzx set SPZT='3',ZXRQ=sysdate() where id =?",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 5:
					 this.jdbcTemplate.update("update zs_zyswsbasp a,zs_zysws b,zs_ryjbxx c set a.SPZT_DM='3',a.YJIAN=?,a.SPRQ=sysdate(),a.SPR=?,c.RYZT_DM='2',c.YXBZ='0',b.ZYZT_DM='2',b.RYSPGCZT_DM='3',b.YXBZ='0' where a.id =? and a.ZYSWS_ID=b.id and b.ry_id=c.id",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 6:
					 this.jdbcTemplate.update("update zs_zysws a,zs_zyswsbgsp b set a.RYSPGCZT_DM='1',b.SPZT_DM='3',b.YJIAN=?,b.SPRQ=sysdate(),b.SPR=? where b.id =? and b.ZYSWS_ID=a.id ",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 7:
					 this.jdbcTemplate.update("update zs_zyswszfzy a,zs_zysws b set b.RYSPGCZT_DM=1,a.SPZT_DM=3,a.SGLZXYJRQ=sysdate() where a.ZYSWS_ID=b.ID and a.id=?",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 8:
					 this.jdbcTemplate.update("update zs_zyswszj a,zs_zysws b set a.SPZT_DM='3',b.RYSPGCZT_DM='1' where a.id =? and a.ZYSWS_ID=b.id ",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 9:
					 this.jdbcTemplate.update("update zs_zyswssndz a,zs_zysws b  set a.SGLZXYJ=?,a.SGLZXYJRQ=sysdate(),a.SPZT_DM=3,b.RYSPGCZT_DM='1' where a.id =? and a.RY_ID=b.id",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 10:
					 this.jdbcTemplate.update("update zs_zyswszx a,zs_zysws b set a.SPZT_DM='3',b.RYSPGCZT_DM='1' where a.id =? and a.ZYSWS_ID=b.id ",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 13:
					 this.jdbcTemplate.update("update zs_fzyzzy a,zs_fzysws b set b.RYSPGCZT_DM=1,a.RYSPZT_DM=2,a.BDRQ=sysdate(),XDWYJ=?,LRR=? where a.FZY_ID=b.ID and a.id=?",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 14:
					 this.jdbcTemplate.update("update zs_fzyswszj a,zs_fzysws b set a.RYSPZT_DM='2',b.RYSPGCZT_DM='1' where a.id =? and a.FZY_ID=b.id ",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 15:
					 this.jdbcTemplate.update("update zs_fzyzx a,zs_fzysws b set a.RYSPZT='3',b.RYSPGCZT_DM='1' where a.id =? and a.FZY_ID=b.id ",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 18:
					 this.jdbcTemplate.update("update zs_fzysws a,zs_fzybgsp b set a.RYSPGCZT_DM='1',b.SPZT_DM='3',b.YJIAN=?,b.SPRQ=sysdate(),b.SPR=? where b.id =? and b.FZY_ID=a.id ",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 case 20:
					 this.jdbcTemplate.update("update zs_fzybasp a,zs_fzysws b,zs_ryjbxx c set a.SPZT_DM='3',a.SPR=?,c.RYZT_DM='2',c.YXBZ='0',b.FZYZT_DM='2',b.RYSPGCZT_DM='3',b.YXBZ='0'  where a.id =? and a.FZYSWS_ID=b.id and b.ry_id=c.id",
							 new Object[]{spsq.get("uid"),mp.get("SJID")});
					 break;
				 case 38:
					 this.jdbcTemplate.update("update zs_zysws a set a.RYSPZT_DM='1' where a.id =?",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 39:
					 this.jdbcTemplate.update("update zs_zysws a set a.RYSPZT_DM='1',a.jg_id='-2' where a.id =?",
							 new Object[]{mp.get("SJID")});
					 break;
				 case 46:
					 this.jdbcTemplate.update("update zs_fzyzzy a,zs_fzysws b set b.RYSPGCZT_DM=1,a.RYSPZT_DM=1,a.BDRQ=sysdate(),XDWYJ=?,LRR=? where a.FZY_ID=b.ID and a.id=?",
							 new Object[]{spsq.get("spyj"),spsq.get("uname"),mp.get("SJID")});
					 break;
				 }
			 }else{
				 this.jdbcTemplate.update("update zs_spzx set LCBZID=?, QRBJ='Y' where id =?",
						 new Object[]{this.jdbcTemplate.queryForObject("select id from zs_splcbz where lcid=? and lcbz=?",
								 new Object[]{mp.get("LCID"),(int)mp.get("LCBZ")-1}, Object.class),spsq.get("spid")});
			 }
		 }else if(spsq.containsKey("jgnj")){
			 if((int)spsq.get("jgnj")==1){
				 this.jdbcTemplate.update("update zs_jg_njb a set a.ZTDM=3,a.WGCL_DM=1 where a.id=?",
						 new Object[]{mp.get("SJID")});
			 }else{
				 this.jdbcTemplate.update("update zs_jg_njb a set a.WGCL_DM=? where a.id=?",
						 new Object[]{spsq.get("jgnj"),mp.get("SJID")});
			 }
		 }else if(spsq.containsKey("zynj")){
			 if((int)spsq.get("zynj")==1){
				 this.jdbcTemplate.update("update zs_zcswsnj a set a.ZTDM=3,a.WGCL_DM=1 where a.id=?",
						 new Object[]{mp.get("SJID")});
			 }else{
				 this.jdbcTemplate.update("update zs_zcswsnj a set a.WGCL_DM=? where a.id=?",
						 new Object[]{spsq.get("zynj"),mp.get("SJID")});
			 }
		 }
		 return true;
	 }
	 /**
	  * 上级驳回意见
	  * @param spid,lcbz
	  * @return
	  */
	 public Map<String, Object> sjbhyj(String spid,int lcbz){
		 return this.jdbcTemplate.queryForMap("select a.spyj from zs_spxx a,zs_splcbz b where a.spid=? and b.ID=a.lcbzid and b.LCBZ=?",
				 new Object[]{spid,lcbz});
	 }
	 
	/*-------------------------------事务所端-------------------------------------*/
	 /**
	  * 事务所端未审批查询
	  * @param uid
	  * @return
	  */
	 public List<Map<String, Object>> swswspcx(Integer uid,final Integer jgid){
			String param ="41,42,43,44";//41:其他从业人员转籍;42:其他从业人员信息变更;43:其他从业人员注销;44:其他从业人员转执业;全显示请留""
			StringBuffer sb = new StringBuffer();
			sb.append("		SELECT ");
			sb.append("		d.PERANT_ID as lx,a.LCLXID as lid, d.MC as wsxm, COUNT(c.id) wss ");
			sb.append("		FROM zs_splc a,fw_user_role e,dm_lclx d,zs_splcbz b");
			sb.append("		LEFT JOIN zs_spzx c ON c.LCBZID=b.id AND c.ztbj='Y' and c.ZSJG_ID in (select id from zs_jg f where f.PARENTJGID=?)");
			sb.append("		WHERE a.ID=b.LCID AND b.ROLEID=e.role_id AND d.ID=a.LCLXID AND a.ZTBJ=2 AND a.LCLXID not in (29"+(param.length()>0?(","+param):" ")+") and e.USER_ID = ?");
			sb.append("		GROUP BY a.LCLXID");
			sb.append("		order by d.PERANT_ID,a.LCLXID");
			return this.jdbcTemplate.query(sb.toString(),new Object[]{jgid,uid},
					new RowMapper<Map<String,Object>>() {
				public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
					Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
					String id = hashids.encode(jgid);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("jgid", id);
					map.put("lid", rs.getObject("lid"));
					map.put("wsxm", rs.getObject("wsxm"));
					map.put("wss", rs.getObject("wss"));
					return map;
				}
			});
		}
	 /**
		 * 事务所变更审批项目申请
		 * @param spxm
		 */
	public void swsbgsq(Map<String, Object> spxm) throws Exception{
		List<Map<String, Object>> forupdate = (List<Map<String, Object>>) spxm.remove("bgjl");//获取变更项目
		int id=(int)spxm.remove("uid");
		int jgid=(int)spxm.remove("jgid");
		List<Object> listValue = new ArrayList<Object>();  //Map转List
		Iterator<String> it = spxm.keySet().iterator();  
		while (it.hasNext()) {  
			String key = it.next().toString();  
			listValue.add(spxm.get(key));  
		};
		String uuid = new Common().newUUID();
		String sql ="insert into zs_jgbgspb (ID,JG_ID,SPZT_DM,BGRQ,TXR_ID,JGBGLSB_ID) values(?,?,'1',sysdate(),?,?)";
		String sql2 ="insert into zs_jgbgxxb (MC,JZHI,XZHI,GXSJ,JGBGSPB_ID) values(?,?,?,sysdate(),?)";
		String sql3 ="insert into zs_jgbglsb (DWMC,CS_DM,JGXZ_DM,DZHI,ZCZJ,ZCDZ,YYZZHM,SWSZSCLSJ) values(?,?,?,?,?,?,?,?)";
		Number rs1 = this.insertAndGetKeyByJdbc(sql3,listValue.toArray(),new String[] {"ID"});//插入临时表，获取自动生成id
		this.jdbcTemplate.update(sql, new Object[]{uuid,jgid,id,rs1});//插入业务表
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		//判断是否分所
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{jgid}).size()==0){
			spsq.put("lclx", "402881831be2e6af011be3ab8b84000c");
		}else{
			spsq.put("lclx", "40288087228378910122838ecac50022");
		}
		spsq.put("jgid", jgid);
		swsSPqq(spsq);//生成审批表记录
		for(Map<String, Object> rec:forupdate){//插入变更项目信息
			this.jdbcTemplate.update(sql2,new Object[]{rec.get("mc"),rec.get("jzhi"),rec.get("xzhi"),uuid});
		}
	}
	/**
	 * 事务所设立资料填报审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void swsslzltb(Map<String, Object> sqxm) throws Exception{
		List<List<String>> nb = (List<List<String>>) sqxm.remove("nbjgsz");
		if(this.jdbcTemplate.queryForList("select id from zs_nbjgsz where jg_id=?",
				new Object[]{sqxm.get("jgid")}).size()>0){
			for(List<String> rec:nb){
				String nbSql="update zs_nbjgsz set BMMC=?,JBZN=?,RS=? where JG_ID=?";
				rec.add(sqxm.get("jgid")+"");
				this.jdbcTemplate.update(nbSql,rec.toArray());
			}
		}else{
			for(List<String> rec:nb){
				String nbSql="insert into zs_nbjgsz (BMMC,JBZN,RS,ID,JG_ID) values(?,?,?,replace(uuid(),'-',''),?)";
				rec.add(sqxm.get("jgid")+"");
				this.jdbcTemplate.update(nbSql,rec.toArray());
			}
		}
		sqxm.remove("uid");
		List<Object> listValue = new ArrayList<Object>();  //Map转List
		Iterator<String> it = sqxm.keySet().iterator();  
		while (it.hasNext()) {  
			String key = it.next().toString();  
			listValue.add(sqxm.get(key));  
		};
		String sql ="update zs_jg set DWMC=?,GZ_DM=?,CS_DM=?,FDDBR=?,DZHI=?,JGZCH=?,JGXZ_DM=?,ZCZJ=?,ZCDZ=?,YZBM=?,"
				+ "CZHEN=?,DHUA=?,KHH=?,KHHZH=?,YYZZHM=?,JYFW=?,SWDJHM=?,JGDMZH=?,SZYX=?,SZPHONE=?,TXYXMING=?,XTYYX=?,XTYPHONE=?,DZYJ=?,GSZZH=?,GSYHMCBH=?,"
				+ "YHDW=?,YHSJ=?,GZBH=?,GZDW=?,GZRY=?,GZSJ=?,YZBH=?,YZDW=?,YZRY=?,YZSJ=?,SJLZXSBWH=?,SGLZXSBSJ=?,ZJPZWH=?,ZJPZSJ=?,FJ=?,JBQK=?,GLZD=?,GDDH=?,BGCSZCZM=?,TGZT_DM=5 where id=?";
		this.jdbcTemplate.update(sql,listValue.toArray());
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", sqxm.get("jgid"));
		spsq.put("lclx", "402882891f4b1acc011f546cf7d10090");
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 事务所分所设立审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void swsfsslsq(Map<String, Object> sqxm) throws Exception{
		List<List<String>> nb = (List<List<String>>) sqxm.remove("nbjgsz");
		sqxm.remove("uid");
		List<Object> listValue = new ArrayList<Object>();  //Map转List
		Iterator<String> it = sqxm.keySet().iterator();  
		while (it.hasNext()) {  
			String key = it.next().toString();  
			listValue.add(sqxm.get(key));  
		};
		String sql ="insert into zs_jg (DWMC,XJG,CS_DM,FDDBR,DZHI,JGZCH,JGXZ_DM,ZCZJ,ZCDZ,YZBM,"
				+ "CZHEN,DHUA,KHH,KHHZH,YYZZHM,JYFW,SWDJHM,JGDMZH,SZYX,SZPHONE,TXYXMING,XTYYX,XTYPHONE,DZYJ,GSZZH,GSYHMCBH,"
				+ "YHDW,YHSJ,GZBH,GZDW,GZRY,GZSJ,YZBH,YZDW,YZRY,YZSJ,SJLZXSBWH,SGLZXSBSJ,ZJPZWH,ZJPZSJ,FJ,JBQK,GLZD,GDDH,BGCSZCZM,JGZT_DM,PARENTJGID,LRRQ,YXBZ)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'5',?,sysdate(),'0')";
		Number rs = this.insertAndGetKeyByJdbc(sql,listValue.toArray(),new String[] {"ID"});
		for(List<String> rec:nb){
			String nbSql="insert into zs_nbjgsz (BMMC,JBZN,RS,ID,JG_ID) values(?,?,?,replace(uuid(),'-',''),?)";
			rec.add(rs.toString());
			this.jdbcTemplate.update(nbSql,rec.toArray());
		}
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", rs);
		spsq.put("lclx", "4028808722837891012283900818002d");
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 事务所合并审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void swshbsq(Map<String, Object> sqxm) throws Exception{
		String sql ="insert into zs_jghb (JG_ID,SFMC,XSWSMC,GSMCYHBH,SQR,HBSJ,SBSJ,HBZT) values(?,?,?,?,?,?,sysdate(),'1')";
		Number rs = this.insertAndGetKeyByJdbc(sql, new Object[]{sqxm.get("jgid"),
				sqxm.get("SFMC"),sqxm.get("XSWSMC"),sqxm.get("GSMCYHBH"),
				sqxm.get("SQR"),sqxm.get("HBSJ")},new String[] {"ID"});
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", rs);
		spsq.put("lclx", "402881831be2e6af011be3aceac6000e");
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 事务所注销审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void swszxsq(Map<String, Object> sqxm) throws Exception{
		String sql ="insert into zs_jgzx (ZXYY_ID,JG_ID,BZ,SPZT) values(?,?,?,'1')";
		Number rs = this.insertAndGetKeyByJdbc(sql, new Object[]{sqxm.get("zxyy"),sqxm.get("jgid"),sqxm.get("zxsm")},new String[] {"ID"});
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", rs);
		spsq.put("lclx", "402881831be2e6af011be3adc72c0011");
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	
	/**
	 * 执业变更申请
	 * @param spxm
	 */
	public void zyswsbgsq(Map<String, Object> spxm) throws Exception{
		List<Map<String, Object>> forupdate = (List<Map<String, Object>>) spxm.remove("bgjl");//获取变更项目
		String uuid = new Common().newUUID();
		String uuid2 = new Common().newUUID();
		int id=(int)spxm.remove("uid");
		int jgid=(int)spxm.remove("jgid");
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		int zyswsid = (int)hashids.decode((String)spxm.remove("zyswsid"))[0];
		spxm.put("uuid", uuid);
		List<Object> listValue = new ArrayList<Object>();  //Map转List
		Iterator<String> it = spxm.keySet().iterator();  
		while (it.hasNext()) {  
			String key = it.next().toString();  
			listValue.add(spxm.get(key));  
		};
		String sql ="insert into zs_zyswsbgsp (ID,ZYSWS_ID,SWSBGLSB_ID,SPZT_DM,BGRQ,TXR) values(?,?,?,'1',sysdate(),?)";
		String sql2 ="insert into zs_zyswsbgxxb (ID,MC,JZHI,XZHI,GXSJ,BGSPB_ID) values(?,?,?,?,sysdate(),?)";
		String sql3 ="insert into zs_zyswsbgls (XMING,CS_DM,XB_DM,MZ_DM,SRI,XL_DM,SFZH,ZZMM_DM,TXDZ,YDDH,YZBM,ZW_DM,DHHM,BYYX,ZYZGZSBH,BYSJ,ZGZSQFRQ,SWDLYWKSSJ,ZYZSBH,ZYZCRQ,GRHYBH,RHSJ,CZR_DM,CZE,FQR_DM,RYDAZT,ID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql3,listValue.toArray());//插入临时表，获取自动生成id
		this.jdbcTemplate.update(sql, new Object[]{uuid2,zyswsid,uuid,this.jdbcTemplate.queryForObject("select names from fw_users where id =?",new Object[]{id}, String.class)});//插入业务表
		for(Map<String, Object> rec:forupdate){//插入变更项目信息
			this.jdbcTemplate.update(sql2,new Object[]{new Common().newUUID(),rec.get("mc"),rec.get("jzhi"),rec.get("xzhi"),uuid2});
		}
		this.jdbcTemplate.update("update zs_zysws set RYSPGCZT_DM='4'where id =?",zyswsid);
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid2);
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{jgid}).size()==0){
			spsq.put("lclx", "402881831be2e6af011be3b118f60016");
		}else{
			spsq.put("lclx", "40288087228378910122838768f00004");
		}
		spsq.put("jgid", jgid);
		swsSPqq(spsq);//生成审批表记录
	}
	 /**
	  * 执业转籍申请
	  * @param sqxm
	  * @throws Exception
	  */
	public void zyzjsq(Map<String, Object> sqxm) throws Exception{
		String sql ="insert into zs_zyswszj (ID,DRS,XJGMC,XJGDH,ZJYYRQ,ZJYY,ZYSWS_ID,TBRQ,SPZT_DM) values(?,?,?,?,?,?,?,sysdate(),'1')";
		String uuid = new Common().newUUID();
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		this.jdbcTemplate.update(sql, new Object[]{uuid,sqxm.get("drs"),sqxm.get("xjgmc"),sqxm.get("xjgdh"),sqxm.get("zjyyrq"),sqxm.get("zjyy"),hashids.decode((String)sqxm.get("zyswsid"))[0]});
		this.jdbcTemplate.update("update zs_zysws a set a.RYSPGCZT_DM='6' where a.id=?",hashids.decode((String)sqxm.get("zyswsid"))[0]);
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{sqxm.get("jgid")}).size()==0){
			spsq.put("lclx", "402881831be2e6af011be3b4ac9d001b");
		}else{
			spsq.put("lclx", "40288087228378910122838d6886001a");
		}
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 执业注销审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void zyzxsq(Map<String, Object> sqxm) throws Exception{
		String uuid = new Common().newUUID();
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		String sql ="insert into zs_zyswszx (ID,ZYSWSZXYY_DM,ZXRQ,SWSYJ,ZYSWS_ID,SPZT_DM) values(?,?,?,?,?,'1')";
		this.jdbcTemplate.update(sql, new Object[]{uuid,sqxm.get("ZYSWSZXYY_DM"),sqxm.get("ZXRQ"),sqxm.get("SWSYJ"),hashids.decode((String)sqxm.get("zyswsid"))[0]});
		this.jdbcTemplate.update("update zs_zysws a set a.RYSPGCZT_DM='9' where a.id=?",hashids.decode((String)sqxm.get("zyswsid"))[0]);
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{sqxm.get("jgid")}).size()==0){
			spsq.put("lclx", "402881831be2e6af011be3b6e94e0020");
		}else{
			spsq.put("lclx", "40288087228378910122838c66870012");
		}
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 执业调入审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void zydrsq(Map<String, Object> sqxm) throws Exception{
		this.jdbcTemplate.update("update zs_zysws a set a.RYSPGCZT_DM='12',a.jg_id=? where a.id=?",new Object []{sqxm.get("jgid"),sqxm.get("ryid")});
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", sqxm.get("zyid"));
		spsq.put("lclx", "402882891d46ef7b011d470758a20007");
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 执业转出审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void zyzcsq(Map<String, Object> sqxm) throws Exception{
		String uuid = new Common().newUUID();
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		this.jdbcTemplate.update("update zs_zysws a set a.RYSPGCZT_DM='11' where a.id=?",hashids.decode((String)sqxm.get("zyswsid"))[0]);
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{sqxm.get("jgid")}).size()==0){
			spsq.put("lclx", "402882891d46ef7b011d470555220004");
		}else{
			spsq.put("lclx", "40288087228378910122838caad30016");
		}
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 执业转所审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void zyzssq(Map<String, Object> sqxm) throws Exception{
		String uuid = new Common().newUUID();
		List<Map<String, Object>> xjg = this.jdbcTemplate.queryForList("select id from zs_jg where dwmc=? and yxbz=1",sqxm.get("XJG_ID"));
		if(xjg.size()==0){
			throw new Exception("事务所名称不存在");
		}
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		String sql ="insert into zs_zyswssndz (ID,YJG_ID,XJG_ID,RY_ID,ZYSWSYJ,ZYSWSTXRQ,YSWSYJ,YSWSYJRQ,TBRQ,SPZT_DM,SZQM,QMSJ) values(?,?,?,?,?,?,?,?,sysdate(),1,?,?)";
		this.jdbcTemplate.update(sql, new Object[]{uuid,sqxm.get("jgid"),xjg.get(0).get("id"),hashids.decode((String)sqxm.get("zyswsid"))[0],
				sqxm.get("ZYSWSYJ"),sqxm.get("ZYSWSTXRQ"),sqxm.get("YSWSYJ"),sqxm.get("YSWSYJRQ"),sqxm.get("SZQM"),sqxm.get("QMSJ")});
		this.jdbcTemplate.update("update zs_zysws a set a.RYSPGCZT_DM='7' where a.id=?",hashids.decode((String)sqxm.get("zyswsid"))[0]);
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{sqxm.get("jgid")}).size()==0){
			spsq.put("lclx", "402881831be2e6af011be3b60a58001e");
		}else{
			spsq.put("lclx", "402880872283789101228389ddaa0009");
		}
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	/**
	 * 执业转非执业审批申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void zyzfzysq(Map<String, Object> sqxm) throws Exception{
		String uuid = new Common().newUUID();
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		String sql ="insert into zs_zyswszfzy (ID,FZYSQ,XDWYJ,TBR,ZYSWS_ID,SPZT_DM,TBRQ) values(?,?,?,?,?,'1',sysdate())";
		this.jdbcTemplate.update(sql, new Object[]{uuid,sqxm.get("FZYSQ"),sqxm.get("XDWYJ"),sqxm.get("uid"),hashids.decode((String)sqxm.get("zyswsid"))[0]});
		this.jdbcTemplate.update("update zs_zysws a set a.RYSPGCZT_DM='8' where a.id=?",hashids.decode((String)sqxm.get("zyswsid"))[0]);
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{sqxm.get("jgid")}).size()==0){
			spsq.put("lclx", "402881831be2e6af011be3b2d1640019");
		}else{
			spsq.put("lclx", "40288087228378910122838b46de000e");
		}
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	
	 /**
	  * 非执业税务师备案申请
	  * @param sqxx
	  * @throws Exception
	  */
	public void fzyswsba(Map<String, Object> sqxx) throws Exception {
		String sql ="insert into zs_ryjbxx (XMING,XB_DM,SRI,SFZH,TXDZ,YZBM,DHHM,YDDH,CS_DM,MZ_DM,XL_DM,ZZMM_DM,BYYX,BYSJ,XPIAN,RYZT_DM,RYSF_DM,LRRQ,YXBZ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'3','2',sysdate(),'0')";
		String sql2 ="insert into zs_fzysws (RY_ID,ZYZGZSBH,ZGZSQFRQ,FZYHYBH,ZW_DM,ZZDW,RHSJ,FZYZT_DM,RYSPGCZT_DM,YXBZ) values(?,?,?,?,?,?,?,'3','2','0')";
		String sql3 ="insert into zs_fzyjl (FZY_ID,QZNY,XXXX,ZMR) values(?,?,?,?)";
		Number rs = this.insertAndGetKeyByJdbc(sql, new Object[]{sqxx.get("XMING"),sqxx.get("XB_DM"),
				sqxx.get("SRI"),sqxx.get("SFZH"),sqxx.get("TXDZ"),sqxx.get("YZBM"),
				sqxx.get("DHHM"),sqxx.get("YDDH"),sqxx.get("CS_DM"),sqxx.get("MZ_DM"),
				sqxx.get("XL_DM"),sqxx.get("ZZMM_DM"),sqxx.get("BYYX"),sqxx.get("BYSJ"),
				sqxx.get("XPIAN")},new String[] {"ID"});//插入ry表，获取自动生成id
		Number rs2 = this.insertAndGetKeyByJdbc(sql2, new Object[]{rs,sqxx.get("ZYZGZSBH"),
				sqxx.get("ZGZSQFRQ"),sqxx.get("FZYHYBH"),sqxx.get("ZW_DM"),sqxx.get("ZZDW"),
				sqxx.get("RHSJ")},new String[] {"ID"});
		for(Map<String, Object> rec:(List<Map<String, Object>>)sqxx.get("FZYJL")){
			if(rec.get("QZNY").toString().equals(""))continue;
			this.jdbcTemplate.update(sql3,new Object[]{rs2,rec.get("QZNY"),rec.get("XXXX"),rec.get("ZMR")});
		}
		String suid = new Common().newUUID();
		this.jdbcTemplate.update("insert into zs_fzybasp (ID,FZYSWS_ID,SPZT_DM,SPRQ) values(?,?,'1',sysdate())",new Object[]{suid,rs2});
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", suid);
		spsq.put("lclx", "402881831be2e6af011be3c184d2003a");
		spsq.put("csdm", sqxx.get("CS_DM"));
		swsSPqq(spsq);
	}
	/**
	 * 非执业转籍申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void fzyzjsq(Map<String, Object> sqxm) throws Exception{
		String sql ="insert into zs_fzyswszj (ID,FZY_ID,ZJYY,ZJYYRQ,TBRQ,RYSPZT) values(?,?,?,?,sysdate(),'0')";
		String uuid = new Common().newUUID();
		this.jdbcTemplate.update(sql, new Object[]{uuid,sqxm.get("FID"),sqxm.get("ZJYY"),sqxm.get("ZJYYRQ")});
		this.jdbcTemplate.update("update zs_fzysws a set a.RYSPGCZT_DM='6' where a.id=?",sqxm.get("FID"));
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		spsq.put("lclx", "402881831be2e6af011be3c6d5f70040");
		spsq.put("csdm", sqxm.get("CS"));
		swsSPqq(spsq);
	}
	/**
	 * 非执业转执业申请
	 * @param sqxm
	 * @throws Exception
	 */
	public void fzyzzysq(Map<String, Object> sqxm) throws Exception{
		String uuid = new Common().newUUID();
		String sql ="insert into zs_fzyzzy (ID,FZY_ID,XORGID,RYSPZT,TBRQ,YDW,XDW) values(?,?,?,'0',sysdate(),?,?)";
		this.jdbcTemplate.update(sql, new Object[]{uuid,sqxm.get("ryid"),sqxm.get("jgid"),sqxm.get("ydw"),sqxm.get("jgid")});
		this.jdbcTemplate.update("update zs_fzysws a set a.RYSPGCZT_DM='13' where a.id=?",sqxm.get("ryid"));
		Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
		spsq.put("sid", uuid);
		spsq.put("lclx", "402880823e15ef82013e15f18a6f0056");
		spsq.put("jgid", sqxm.get("jgid"));
		swsSPqq(spsq);
	}
	
	/**
	  * 审批申请处理方法
	  * @param Map:sid,lclx,(选填：jgid,csdm)
	  * @return boolean
	  * @throws Exception
	  */
	 boolean swsSPqq(Map<String,Object> spsq) throws Exception{
		String sql ="select b.id,b.lcbz,b.roleid from zs_splcbz b where  b.LCID =? order by b.LCBZ";
		 List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,new Object[]{spsq.get("lclx")});//查询该流程需要步骤
		List<Object> listValue = new ArrayList<Object>();//设置插入单据表参数
		String suid = new Common().newUUID();
		listValue.add(suid);
		listValue.add(spsq.get("sid"));
		listValue.add(ls.get(0).get("id"));//获取第一步步骤id
		String insterspzx = "";
		String insterspzx2 = "";
		if(spsq.containsKey("jgid")){//是否填写机构id
			listValue.add(spsq.get("jgid"));
			insterspzx+="ZSJG_ID,";
			insterspzx2+="?,";
		}
		if(spsq.containsKey("csdm")){//是否填写城市代码
			listValue.add(spsq.get("csdm"));
			insterspzx+="CS_ID,";
			insterspzx2+="?,";
		}
		this.jdbcTemplate.update("insert into zs_spzx (ID,SJID,LCBZID,ZTBJ,"+insterspzx+"TJSJ) values(?,?,?,'Y',"+insterspzx2+"sysdate())",listValue.toArray());
		for(Map<String, Object> rec:ls){//根据步骤生成对应审批节点数记录
			this.jdbcTemplate.update("insert into zs_spxx (ID,SPID,LCBZID) values(?,?,?)",new Object[]{new Common().newUUID(),suid,rec.get("id")});
		}
		return true;
	}
	
	 /*-------------------------------非审批申请-------------------------------------*/
		 /**
			 * 事务所普通项目更新
			 * @param ptxm
			 */
			public void updatePTXM(Map<String, Object> ptxm)throws Exception{
				List<Map<String, Object>> forupdate = (List<Map<String, Object>>) ptxm.remove("bgjl");//获取并移除bgjl属性
				if(ptxm.containsKey("nbjgsz")){
					List<List<String>> nb = (List<List<String>>) ptxm.remove("nbjgsz");
					for(List<String> rec:nb){
						String fir=rec.get(0)+"";
						if(fir.equals("null")){
							continue;
						}
						String nbSql="update zs_nbjgsz set BMMC=?,JBZN=?,RS=? where JG_ID=?";
						rec.add((String) ptxm.get("jgid"));
						this.jdbcTemplate.update(nbSql,rec.toArray());
					}
				}
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				int jgid = (int)hashids.decode((String)ptxm.get("jgid"))[0];
				ptxm.put("jgid", jgid);
				List<Object> listValue = new ArrayList<Object>();  //Map转List
				Iterator<String> it = ptxm.keySet().iterator();  
				while (it.hasNext()) {  
					String key = it.next().toString();  
					listValue.add(ptxm.get(key));  
				};
				String sql ="update zs_jg set DHUA=?,CZHEN=?,jyfw=?,yzbm=?,SZPHONE=?,JGZCH=?,SWDJHM=?,KHH=?,KHHZH=?,TXYXMING=?,XTYPHONE=?,XTYYX=?,SZYX=?,JGDMZH=?,GSYHMCBH=?,wangzhi=?,dzyj=?,yhdw=?,yhsj=?,gzbh=?,gzdw=?,gzry=?,gzsj=?,yzbh=?,yzdw=?,yzry=?,yzsj=?,TTHYBH=?,rhsj=?,JBQK=?,GLZD=?,GDDH=?,BGCSZCZM=? where id =?";
				String sql2 ="insert into zs_jglsbgxxb (MC,JZHI,XZHI,GXSJ,JGB_ID,ID) values(?,?,?,sysdate(),?,replace(uuid(),'-',''))";
				this.jdbcTemplate.update(sql,listValue.toArray());//更新数据库
				for(Map<String, Object> rec:forupdate){//插入变更项目信息
					this.jdbcTemplate.update(sql2,new Object[]{rec.get("mc"),rec.get("jzhi"),rec.get("xzhi"),ptxm.get("jgid")});
				}
					
			}
		/**
		 * 执业转入分所申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void zyzrfssq(Map<String, Object> sqxm) throws Exception{
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			Object zyid = hashids.decode((String)sqxm.get("zyswsid"))[0];
			Object jgid = hashids.decode((String)sqxm.get("pid"))[0];
			this.jdbcTemplate.update("update zs_zysws a set a.jg_id=? where a.id=?",new Object[]{jgid,zyid});
		}
		/**
		 * 从业转入分所申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cyzrfssq(Map<String, Object> sqxm) throws Exception{
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			Object ryid = hashids.decode((String)sqxm.get("ryid"))[0];
			Object jgid = hashids.decode((String)sqxm.get("pid"))[0];
			this.jdbcTemplate.update("update zs_cyry a set a.jg_id=? where a.id=?",new Object[]{jgid,ryid});
		}
		/**
		 * 从业调入申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cydrsq(Map<String, Object> sqxm) throws Exception{
			this.jdbcTemplate.update("update zs_cyry a set a.jg_id=? where a.id=?",new Object []{sqxm.get("jgid"),sqxm.get("ryid")});
		}	
		/**
		 * 从业注销申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cyzxsq(Map<String, Object> sqxm) throws Exception{
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			Object ryid = hashids.decode((String)sqxm.get("ryid"))[0];
			this.jdbcTemplate.update("update zs_cyry a set a.CYRYZT_DM=5 where a.id=?",new Object []{ryid});
			this.jdbcTemplate.update("insert into zs_cyryzx (CYRY_ID,ZXLB_DM,ZXYY,BDRQ,RYSPZT,ID) values(?,?,?,?,2,replace(uuid(),'-',''))",
					new Object []{ryid,sqxm.get("ZXYY_DM"),sqxm.get("ZXRQ"),sqxm.get("SWSYJ")});
		}	
		/**
		 * 从业调出申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cydcsq(Map<String, Object> sqxm) throws Exception{
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			Object ryid = hashids.decode((String)sqxm.get("ryid"))[0];
			this.jdbcTemplate.update("update zs_cyry a set a.CYRYZT_DM=4 where a.id=?",new Object []{ryid});
			this.jdbcTemplate.update("insert into zs_cyrydc (CYRY_ID,DZYY,SWSYJ,BDRQ,DRDW,LRR,ID) values(?,?,?,?,?,(select USERNAME from fw_users where id=? limit 1),replace(uuid(),'-',''))",
					new Object []{ryid,sqxm.get("DZYY"),sqxm.get("SWSYJ"),sqxm.get("BDRQ"),sqxm.get("DRDW"),sqxm.get("uid")});
		}	
		/**
		 * 从业转出申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cyzcsq(Map<String, Object> sqxm) throws Exception{
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			Object ryid = hashids.decode((String)sqxm.get("ryid"))[0];
			this.jdbcTemplate.update("update zs_cyry a set a.JG_ID=-2 where a.id=?",new Object []{ryid});
		}	
		/**
		 * 执业调入主所申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void zydrzssq(Map<String, Object> sqxm) throws Exception{
			this.jdbcTemplate.update("update zs_zysws a set a.jg_id=? where a.id=?",new Object []{sqxm.get("jgid"),sqxm.get("ryid")});
		}	
		/**
		 * 从业调入主所申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cydrzssq(Map<String, Object> sqxm) throws Exception{
			this.jdbcTemplate.update("update zs_cyry a set a.jg_id=? where a.id=?",new Object []{sqxm.get("jgid"),sqxm.get("ryid")});
		}
		/**
		 * 从业人员信息变更申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cyrybgsq(Map<String, Object> sqxm) throws Exception{
			List<List<String>> nb = (List<List<String>>) sqxm.remove("nbjgsz");
			List<Map<String, Object>> forupdate = (List<Map<String, Object>>) sqxm.remove("bgjl");
			Object uid = sqxm.remove("uid");
			Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
			Object ryid = hashids.decode((String)sqxm.remove("ryid"))[0];
			sqxm.remove("jgid");
			sqxm.put("id", ryid);
			List<Object> listValue = new ArrayList<Object>();  //Map转List
			Iterator<String> it = sqxm.keySet().iterator();  
			while (it.hasNext()) {  
				String key = it.next().toString();  
				listValue.add(sqxm.get(key));  
			};
			String sql ="update zs_cyry a,zs_ryjbxx b set b.XMING=?,b.CS_DM=?,b.XB_DM=?,b.MZ_DM=?,b.SRI=?,b.XL_DM=?,b.SFZH=?,"
					+ "b.ZZMM_DM=?,b.TXDZ=?,b.YDDH=?,b.YZBM=?,a.ZW_DM=?,b.DHHM=?,b.BYYX=?,a.XZSNGZGW=?,b.BYSJ=?,a.LRSJ=?,a.SWDLYWKSSJ=?,"
					+ "a.ZGXLZYMC=?,a.ZGXLFZJGJSJ=?,b.RYDAZT=?,b.xpian=? where a.ry_id=b.id and b.id=?";
			this.jdbcTemplate.update(sql,listValue.toArray());
			List<Map<String, Object>> jls = this.jdbcTemplate.queryForList("select a.id from zs_jl a where a.ry_id=?",ryid);
			for(Map<String, Object> rec:forupdate){//插入变更表
				String jlbSql="insert into zs_cyrylsbgxxb (ID,MC,JZHI,XZHI,GXSJ,CYRY_ID,GXRY_ID) values(replace(uuid(),'-',''),?,?,?,sysdate(),(select id from zs_cyry where ry_id=?),?)";
				this.jdbcTemplate.update(jlbSql,new Object[]{rec.get("mc"),rec.get("jzhi"),rec.get("xzhi"),ryid,uid});
			}
			if(jls.size()>0){
				for(List<String> rec:nb){//更新人员简历
					int rowNum = nb.indexOf(rec);
					String nbSql="update zs_jl set QZNY=?,XXXX=?,ZMR=? where ID=?";
					try {
						rec.add(jls.get(rowNum).get("id")+"");
					} catch (Exception e) {
						String insetnbSql="insert into zs_jl (QZNY,XXXX,ZMR,ID,RY_ID) values(?,?,?,replace(uuid(),'-',''),?)";
						rec.add(ryid.toString());
						this.jdbcTemplate.update(insetnbSql,rec.toArray());
					}
					this.jdbcTemplate.update(nbSql,rec.toArray());
				}
			}else{
				for(List<String> rec:nb){//插入人员简历
					String nbSql="insert into zs_jl (QZNY,XXXX,ZMR,ID,RY_ID) values(?,?,?,replace(uuid(),'-',''),?)";
					rec.add(ryid.toString());
					this.jdbcTemplate.update(nbSql,rec.toArray());
				}
			}
		}
		/**
		 * 从业人员备案申请
		 * @param sqxm
		 * @throws Exception
		 */
		public void cyrybasq(Map<String, Object> sqxm) throws Exception{
			List<List<String>> nb = (List<List<String>>) sqxm.remove("nbjgsz");
			sqxm.remove("uid");
			Object jgid = sqxm.remove("jgid");
			sqxm.remove("ryid");
			List<Object> cyb = new ArrayList<Object>();
			cyb.add(sqxm.remove("ZW_DM"));
			cyb.add(sqxm.remove("xzsngzgw"));
			cyb.add(sqxm.remove("lrsj"));
			cyb.add(sqxm.remove("swdlywkssj"));
			cyb.add(sqxm.remove("zgxlzymc"));
			cyb.add(sqxm.remove("zgxlfzjgjsj"));
			List<Object> listValue = new ArrayList<Object>();  //Map转List
			Iterator<String> it = sqxm.keySet().iterator();  
			while (it.hasNext()) {  
				String key = it.next().toString();  
				listValue.add(sqxm.get(key));  
			};
			String sql ="insert into zs_ryjbxx (XMING,CS_DM,XB_DM,MZ_DM,SRI,XL_DM,SFZH,ZZMM_DM,TXDZ,YDDH,YZBM,DHHM,BYYX,BYSJ,"
					+ "RYDAZT,xpian,RYZT_DM,RYSF_DM,LRRQ,YXBZ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,3,sysdate(),1)";
			Number rs = this.insertAndGetKeyByJdbc(sql,listValue.toArray(),new String[] {"ID"});
			String sqlCy="insert into zs_cyry (ZW_DM,XZSNGZGW,LRSJ,SWDLYWKSSJ,ZGXLZYMC,ZGXLFZJGJSJ,RY_ID,JG_ID,CYRYZT_DM,YXBZ) values(?,?,?,?,?,?,?,?,1,1)";
			cyb.add(rs);
			cyb.add(jgid);
			Number rsCy = this.insertAndGetKeyByJdbc(sqlCy,cyb.toArray(),new String[] {"ID"});
			for(List<String> rec:nb){//插入人员简历
				String nbSql="insert into zs_jl (QZNY,XXXX,ZMR,ID,RY_ID) values(?,?,?,replace(uuid(),'-',''),?)";
					rec.add(rsCy.toString());
				this.jdbcTemplate.update(nbSql,rec.toArray());
			}
		}

	/**
	 * 非执业备案查询
	 * @param sfzh
	 * @return
	 */
		public List<Map<String, Object>> getFzyswsBa(String sfzh) {
			StringBuffer sb = new StringBuffer();
			sb.append("  select d.ZTBJ,e.spyj,a.RYZT_DM,a.ID ,e.SPSJ,a.XMING,u.`NAMES` ");
			sb.append("  from zs_ryjbxx a,zs_fzysws b,zs_fzybasp c,zs_spzx d,zs_spxx e ");
			sb.append("  left join fw_users u  ");
			sb.append("  on u.ID = e.USERID ");
			sb.append("  where a.ID=b.RY_ID   ");
			sb.append("  and b.ID=c.FZYSWS_ID  ");
			sb.append("  and d.SJID=c.ID  ");
			sb.append("  and d.ID=e.SPID ");
			sb.append("  and a.SFZH= ? ");
			sb.append("  order by a.id desc  ");
			List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(), new Object[]{sfzh});
			return ls;
		}
	
	/**
	 *审批历史记录查询 
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 */
	public Map<String,Object> splsjlcx(int pn,int ps,Map<String, Object> qury) {
		Condition condition = new Condition();
		condition.add("c.lclxid", Condition.EQUAL, qury.get("splx"));
		if(qury.containsKey("sqsj")){
			String sbsj = new Common().getTime2MysqlDateTime((String)qury.get("sqsj"));
			condition.add("a.tjsj", Condition.GREATER_EQUAL, sbsj);
		}
		if(qury.containsKey("sqsj2")){
			String sbsj = new Common().getTime2MysqlDateTime((String)qury.get("sqsj2"));
			condition.add("a.tjsj", Condition.LESS_EQUAL, sbsj);
		}
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',c.LCMC,");
		sb.append("		IF(a.CS_ID IS NULL,(SELECT dwmc FROM zs_jg WHERE id=a.zsjg_id),");
		sb.append("		(SELECT mc FROM dm_cs WHERE id=a.cs_id)) AS sqdw,");
		sb.append("		(select DESCRIPTION from fw_role where id=b.ROLEID) as fzr,");
		sb.append("		case a.ZTBJ when 'Y' then '审批中' when 'N' then '已审批' else null end as spzt,");
		sb.append("		date_format(a.TJSJ,'%Y-%m-%d') as tjsj");
		sb.append("		FROM zs_spzx a,zs_splcbz b,zs_splc c,(select @rownum:=?) zs_ry ");
		sb.append(condition.getSql());
		sb.append("		and a.LCBZID=b.ID AND b.LCID=c.ID AND c.ZTBJ=2");
		sb.append("		order by a.tjsj desc LIMIT ?, ?");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		ob.put("page", meta);
		return ob;
	}
}

