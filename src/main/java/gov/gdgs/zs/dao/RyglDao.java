package gov.gdgs.zs.dao;


import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.google.common.base.Objects;

@Repository
public class RyglDao extends BaseDao{
	/**
	 * 人员查询
	 * @param pn ps
	 * @param qury
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> rycx(int pn,int ps,Map<String, Object> qury) {
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		Condition condition2 = new Condition();
		condition.add("a.xming", Condition.FUZZY, qury.get("xm"));
		condition.add("a.rysf_dm", Condition.EQUAL, qury.get("rysfdm"));
		condition.add("a.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		condition.add("a.CS_DM", Condition.EQUAL, qury.get("cs"));
		condition.add("a.xb_DM", Condition.EQUAL, qury.get("xb"));
		condition.add("a.xl_dm", Condition.EQUAL, qury.get("xl"));
		condition2.add("a.xl_dm", Condition.EQUAL, qury.get("xl"));
		StringBuffer sb = new StringBuffer();
		sb.append("	select SQL_CALC_FOUND_ROWS ");
		sb.append("		@rownum:=@rownum+1 as 'key',");
		sb.append("				a.id,");
		sb.append("				a.xming,");
		sb.append("				d.mc as xb,");
		sb.append("				date_format(a.SRI,'%Y-%m-%d') as srrq,");
		sb.append("				a.sfzh,");
		sb.append("				b.mc as cs,");
		sb.append("				c.mc as mz,");
		sb.append("				f.mc as xl,");
		sb.append("				e.mc as rysf,a.rysf_dm as rysfdm");
		sb.append("				from zs_ryjbxx a,dm_cs b,dm_mz c,dm_xb d,dm_rysf e,dm_xl f,(select @rownum:=?) zs_ry");
		sb.append("		"+condition.getSql()+" ");
		if(qury.containsKey("dwmc")){
			Object obj=qury.get("dwmc");
			if (!Objects.equal(obj, "") && !Objects.equal(obj, null)) {
				sb.append("		and a.ID in (");
				sb.append("		select j.RY_ID from zs_cyry j,zs_jg h where j.JG_ID=h.id and h.id='"+obj+"' union");
				sb.append("		select g.RY_ID from zs_zysws g,zs_jg h where g.jg_id=h.ID and h.id='"+obj+"' union");
				sb.append("		select i.RY_ID from zs_fzysws i,zs_jg h where i.ZZDW=h.dwmc and h.id='"+obj+"') ");
			}
		}
		if(qury.containsKey("ZW")){
			Object obj=qury.get("ZW");
			if (!Objects.equal(obj, "") && !Objects.equal(obj, null)) {
				sb.append("		and a.ID in (");
				sb.append("		select j.RY_ID from zs_cyry j where j.ZW_DM='"+obj+"' union");
				sb.append("		select g.RY_ID from zs_zysws g where g.ZW_DM='"+obj+"' union");
				sb.append("		select i.RY_ID from zs_fzysws i where i.ZW_DM='"+obj+"') ");
			}
		}
		if(qury.containsKey("ZYZGZSBH")){
			Object obj=qury.get("ZYZGZSBH");
			if (!Objects.equal(obj, "") && !Objects.equal(obj, null)) {
				sb.append("		and a.ID in (");
				sb.append("		select g.RY_ID from zs_zysws g where g.ZYZGZSBH like'"+obj+"%')");
			}
		}
		if(qury.containsKey("ZYZSBH")){
			Object obj=qury.get("ZYZSBH");
			if (!Objects.equal(obj, "") && !Objects.equal(obj, null)) {
				sb.append("		and a.ID in (");
				sb.append("		select g.RY_ID from zs_zysws g where g.ZYZSBH like'"+obj+"%')");
			}
		}
		if(qury.containsKey("CZE")){
			Object obj=qury.get("CZE");
			if (!Objects.equal(obj, "") && !Objects.equal(obj, null)) {
				sb.append("		and a.ID in (");
				sb.append("		select g.RY_ID from zs_zysws g where g.CZE like'"+obj+"%')");
			}
		}
		if(qury.containsKey("YDDH")){
			Object obj=qury.get("YDDH");
			if (!Objects.equal(obj, "") && !Objects.equal(obj, null)) {
				sb.append("		and a.ID in (");
				sb.append("		select k.id from zs_ryjbxx k where k.YDDH like'"+obj+"%')");
			}
		}
		sb.append("				and a.xb_dm= d.id");
		sb.append("				and a.cs_dm=b.id");
		sb.append("				and a.mz_dm=c.id");
		sb.append("				and a.xl_dm=f.id");
		sb.append("				and a.rysf_dm=e.id");
		sb.append("				and a.yxbz='1'");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "xm":
				if(asc){
					sb.append("		    order by convert( a.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( a.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "xb":
				if(asc){
					sb.append("		    order by a.xb_dm ");
				}else{
					sb.append("		    order by a.xb_dm desc");
				}
				break;
			case "cs":
				if(asc){
					sb.append("		    order by convert( b.mc USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( b.mc USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "srrq":
				if(asc){
					sb.append("		    order by a.sri ");
				}else{
					sb.append("		    order by a.sri desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				switch (rs.getObject("rysfdm").toString()) {
				case "1":
					link.put("herf_xxzl", url+"/ryxx/zyryxx/"+id);
					link.put("herf_bgjl", url+"/ryxx/zyrybgjl/"+id);
					link.put("herf_zsjl", url+"/ryxx/zyryzsjl/"+id);
					link.put("herf_zjjl", url+"/ryxx/zyryzjjl/"+id);
					link.put("herf_zzjl", url+"/ryxx/zyryzzjl/"+id);
					link.put("herf_spzt", url+"/ryxx/zyryspzt/"+id);
					link.put("herf_njjl", url+"/ryxx/zyrynjjl/"+id);
					break;
				case "2":
					link.put("herf_xxzl", url+"/ryxx/fzyryxx/"+id);
					link.put("herf_bgjl", url+"/ryxx/fzyrybgjl/"+id);
					link.put("herf_zsjl", url+"/ryxx/fzyryzxjl/"+id);
					link.put("herf_zjjl", url+"/ryxx/fzyryzjjl/"+id);
					link.put("herf_zzjl", url+"/ryxx/fzyryzfjl/"+id);
					break;
				case "3":
					link.put("herf_xxzl", url+"/ryxx/cyryxx/"+id);
					link.put("herf_bgjl", url+"/ryxx/cyrybgjl/"+id);
					break;
				
				}
				map.put("key", rs.getObject("key"));
				map.put("xh", rs.getObject("key"));
				map.put("_links", link);
				map.put("xm", rs.getObject("xming"));
				map.put("xb", rs.getObject("xb"));
				map.put("cs", rs.getObject("cs"));
				map.put("srrq", rs.getObject("srrq"));
				map.put("sfzh", rs.getObject("sfzh"));
				map.put("rysfdm", rs.getObject("rysfdm"));
				map.put("rysf", rs.getObject("rysf"));
				map.put("mz", rs.getObject("mz"));
				map.put("xl", rs.getObject("xl"));
				return map;
				}
	});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
		}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员详细信息
	 */
	public Map<String,Object> zyryxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("	select @rownum:=@rownum+1 as 'key',b.dwmc,");	
		sb.append("	c.xming as xm,");	
		sb.append("	f.mc as cs,");	
		sb.append("	d.mc as xb, ");	
		sb.append("	g.mc as mz,");	
		sb.append("	date_format(c.sri,'%Y-%m-%d') as csny,");	
		sb.append("		(select e.mc from dm_xl e where e.id=c.xl_dm) as xl,");	
		sb.append("	c.sfzh,");	
		sb.append("	(select j.ZYRYBALB_DM from zs_zyrybayyb j where j.ZYSWS_ID=a.id) as rslb,");	
		sb.append("	(select j.DCS from zs_zyrybayyb j where j.ZYSWS_ID=a.id) as DCS,");	
		sb.append("	(select j.YJGMC from zs_zyrybayyb j where j.ZYSWS_ID=a.id) as YJGMC,");	
		sb.append("	(select j.YJGDH from zs_zyrybayyb j where j.ZYSWS_ID=a.id) as YJGDH,");	
		sb.append("	(select k.YJIAN from zs_zyswsbasp k where k.ZYSWS_ID=a.id) as bhyy,");	
		sb.append("	c.txdz,");	
		sb.append("	c.yddh,");	
		sb.append("		c.yzbm,");	
		sb.append("	i.mc as zw,");	
		sb.append("	c.dhhm, ");	
		sb.append("	c.byyx,");	
		sb.append("	a.zyzgzsbh,");	
		sb.append("	date_format(c.bysj,'%Y-%m-%d') as bysj,");	
		sb.append("		date_format(a.zgzsqfrq,'%Y-%m-%d') as qfrq,");	
		sb.append("	date_format(a.swdlywkssj,'%Y-%m-%d') as ywkssj,");	
		sb.append("	 a.zyzsbh,");	
		sb.append("	date_format(a.zyzcrq,'%Y-%m-%d') as zyzcrq, ");	
		sb.append("	a.grhybh,");	
		sb.append("	date_format(a.rhsj,'%Y-%m-%d') as rhsj,");	
		sb.append("	case a.czr_dm when 1 then \"是\"  when 2 then \"否\" else null end as czr,");	
		sb.append("	a.cze,");	
		sb.append("	case a.fqr_dm when 1 then \"是\"  when 2 then \"否\" else null end as fqr,");	
		sb.append("	c.rydazt,");	
		sb.append("	c.xpian,CAST(c.XB_DM AS CHAR) as XB_DM,c.XL_DM,c.CS_DM,c.MZ_DM,c.ZZMM_DM,CAST(a.ZW_DM AS CHAR) as ZW_DM,a.czr_dm,a.fqr_dm");	
		sb.append("	from ");	
		sb.append("	zs_zysws a,zs_jg b ,zs_ryjbxx c ,");	
		sb.append("	dm_xb d,dm_cs f,dm_mz g,dm_zw i,(select @rownum:=0) zs_sws");	
		sb.append("	where");	
		sb.append("		b.id=a.jg_id");	
		sb.append("		 and a.ry_id = c.ID ");	
		sb.append("		and d.ID = c.XB_DM");	
		sb.append("		and f.ID = c.CS_DM");	
		sb.append("		and g.ID = c.MZ_DM");	
		sb.append("	and i.ID = a.ZW_DM");	
		sb.append("	and c.ID = ?");	
		String sql = "SELECT @rownum:=@rownum+1 as 'key',a.qzny,a.xxxx,a.zmr FROM zs_jl a,zs_ryjbxx c,(select @rownum:=0) zs_sws WHERE a.ry_id = c.id  and a.xxxx is not null and c.ID = ? order by a.ID";
		List<Map<String, Object>> tl = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
		Map<String,Object> ll =new HashMap<String,Object>();
		if(tl.size()!=0){
			ll =tl.get(0);
		}
		ll.put("ryjl", this.jdbcTemplate.queryForList(sql,new Object[]{id}));
		return ll;
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员变更记录
	 */
	public List<Map<String,Object>> zyrybgjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key',a.mc as bgmc,a.jzhi,a.xzhi,date_format(a.gxsj,'%Y-%m-%d') as gxsj ");
		sb.append("		from zs_zyswslsbgxxb a,zs_zysws b ,(select @rownum:=0) zs_sws");
		sb.append("		where a.ZYSWS_ID = b.ID ");
		sb.append("		and b.ry_id = ?");
		sb.append("		order by a.gxsj");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员转所记录
	 */
	public List<Map<String,Object>> zyryzsjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', ");
		sb.append("		c.mc as spztmc,a.zyswsyj as bryj,a.yswsyj as ydwyj,");
		sb.append("		DATE_FORMAT( a.tbrq,'%Y-%m-%d') AS tbrq  ");
		sb.append("		 from zs_zyswssndz a ,zs_zysws b,dm_spzt c,(select @rownum:=0) zs_sws ");
		sb.append("		where a.RY_ID = b.ID ");
		sb.append("		and a.spzt_dm = c.ID ");
		sb.append("		and b.ry_id =? ");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员转籍记录
	 */
	public List<Map<String,Object>> zyryzjjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', ");
		sb.append("		a.zysws_id ,c.mc as spztmc,a.zjyy as zjyj,a.yswsyj as dwyj,");
		sb.append("		DATE_FORMAT( a.tbrq,'%Y-%m-%d') AS tbrq   ");
		sb.append("		from zs_zyswszj a ,dm_spzt c,zs_zysws d,(select @rownum:=0) zs_sws");
		sb.append("		 where d.ry_id = ? ");
		sb.append("		 and a.spzt_dm = c.ID and d.id= a.zysws_id");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员转执记录
	 */
	public List<Map<String,Object>> zyryzzjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', ");
		sb.append("		a.zysq as zzsq,a.xdwyj as dwyj,DATE_FORMAT( a.tbrq,'%Y-%m-%d') AS tbrq,DATE_FORMAT( c.SPSJ,'%Y-%m-%d') AS spsj ");
		sb.append("	 from (select * from zs_fzyzzy union");
		sb.append("		 select * from zs_cyryzzy )as a,");
		sb.append("		 zs_spzx b,");
		sb.append("		 zs_spxx c,");
		sb.append("		 zs_zysws f,(select @rownum:=0) zs_sws ");
		sb.append("		 where a.zysws_id = f.id ");
		sb.append("		 and a.ID = b.sjid");
		sb.append("		and c.SPID = b.ID ");
		sb.append("		and c.ISPASS = 'Y' ");
		sb.append("		   and f.ry_ID = ?");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员审批状态
	 */
	public String zyryspzt(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select ");
		sb.append("		distinct  a.mc ");
		sb.append("		 from dm_ryspgczt a, zs_zysws b ");
		sb.append("		 where a.ID = b.RYSPGCZT_DM ");
		sb.append("		 and b.ry_ID = ?");
		return this.jdbcTemplate.queryForObject(sb.toString(),new Object[]{id},String.class);
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 执业人员年检记录
	 */
	public List<Map<String,Object>> zyrynjjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', ");
		sb.append("	 c.ID,a.nd,b.dwmc,a.swsfzryj,");
		sb.append("		CASE a.ZTDM WHEN 1 THEN '保存'  WHEN 2 THEN '自检' WHEN 0 THEN '退回' WHEN 3 THEN '已年检' ELSE NULL END AS njzt,");
		sb.append("		 DATE_FORMAT( f.SPSJ,'%Y-%m-%d') AS spsj");
		sb.append("		 FROM  zs_zcswsnj a,zs_jg b,zs_ryjbxx c,zs_zysws d,zs_spzx e,zs_spxx f,zs_splcbz g,zs_splc h,(select @rownum:=0) zs_sws ");
		sb.append("		 WHERE a.ZSJG_ID=b.ID  ");
		sb.append("		 AND c.id=d.RY_ID   ");
		sb.append("		  AND d.id = a.sws_id ");
		sb.append("		  AND f.SPID=e.ID ");
		sb.append("		  AND f.ISPASS='Y'");
		sb.append("		 AND g.ID=f.LCBZID ");
		sb.append("		 AND h.ID=g.LCID ");
		sb.append("		 AND h.LCLXID='12' ");
		sb.append("		 AND a.ZTDM= 3 ");
		sb.append("		 AND a.ID=e.SJID ");
		sb.append("		  AND d.ry_ID=? ");
		sb.append("		  ORDER BY a.ND");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 非执业人员详细信息
	 */
	public Map<String,Object> fzyryxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("	select @rownum:=@rownum+1 as 'key',");	
		sb.append("		a.zzdw,");
		sb.append("		h.XMING as xm,");
		sb.append("		d.MC AS cs,");
		sb.append("		b.MC as xb,");
		sb.append("		e.MC AS mz,");
		sb.append("		DATE_FORMAT(h.sri,'%Y-%m-%d') AS csny, ");
		sb.append("		c.MC as xl,");
		sb.append("		h.sfzh,");
		sb.append("		f.MC AS zzmm,");
		sb.append("		h.txdz,");
		sb.append("		h.yddh,");
		sb.append("		h.yzbm,");
		sb.append("		g.mc as zw,");
		sb.append("		h.dhhm,");
		sb.append("		h.byyx,");
		sb.append("		a.zyzgzsbh, ");
		sb.append("		DATE_FORMAT(h.BYSJ,'%Y-%m-%d') AS bysj, ");
		sb.append("		a.fzyhybh,");
		sb.append("		DATE_FORMAT(a.RHSJ,'%Y-%m-%d') AS rhsj,");
		sb.append("		DATE_FORMAT(a.ZGZSQFRQ,'%Y-%m-%d') AS zgzsqfrq,");
		sb.append("		a.fzyzczsbh,");
		sb.append("		DATE_FORMAT(a.FZYZCRQ,'%Y-%m-%d') AS fzyzcrq");
		sb.append("		from zs_fzysws a,zs_ryjbxx h,dm_xb b,dm_xl c,dm_cs d,dm_mz e,dm_zzmm f,dm_zw g,(select @rownum:=0) zs_sws");
		sb.append("		where a.fzyzt_dm = 1");
		sb.append("		and h.XB_DM =b.ID");
		sb.append("		and h.XL_DM = c.ID");
		sb.append("		and h.cs_dm = d.ID");
		sb.append("		and h.mz_dm = e.ID");
		sb.append("		and h.zzmm_dm = f.ID");
		sb.append("		and a.zw_dm = g.ID");
		sb.append("		and a.RY_ID = h.ID");
		sb.append("		and a.RY_ID=?");
		sb.append("		order by a.ID");
		String sql = "SELECT @rownum:=@rownum+1 as 'key',a.qzny,a.xxxx,a.zmr FROM zs_fzyjl a,(select @rownum:=0) zs_sws,zs_fzysws b WHERE a.xxxx is not null and a.xxxx<>'' and a.FZY_ID = b.ID and b.RY_ID=? order by a.ID";
		List<Map<String, Object>> tl = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
		Map<String,Object> ll =tl.get(0);
		ll.put("ryjl", this.jdbcTemplate.queryForList(sql,new Object[]{id}));
		return ll;
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 非执业人员变更记录
	 */
	public List<Map<String,Object>> fzyrybgjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key',a.mc as bgmc,a.jzhi,a.xzhi,date_format(a.gxsj,'%Y-%m-%d') as gxsj ");
		sb.append("		from zs_fzylsbgxxb a,zs_fzysws b,(select @rownum:=0) zs_sws");
		sb.append("		where b.RY_ID = ?");
		sb.append("		and a.FZY_ID = b.id");
		sb.append("		order by a.gxsj");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 非执业人员注销记录
	 */
	public List<Map<String,Object>> fzyryzxjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', ");
		sb.append("		a.fzy_id,a.zxyy,date_format(a.bdrq,'%Y-%m-%d') as bdrq,a.lrr");
		sb.append("		FROM  zs_fzyzx a,zs_fzysws b,(select @rownum:=0) zs_sws");
		sb.append("		where b.RY_ID = ?");
		sb.append("		and a.FZY_ID = b.id");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 非执业人员转籍记录
	 */
	public List<Map<String,Object>> fzyryzjjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', ");
		sb.append("		 a.fzy_id,a.zjyy,a.zjyyrq,date_format( a.tbrq,'%Y-%m-%d') AS tbrq ");
		sb.append("		 FROM zs_fzyswszj a ,zs_fzysws b,(select @rownum:=0) zs_sws");
		sb.append("		where b.RY_ID = ?");
		sb.append("		and a.FZY_ID = b.id");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 非执业人员转非记录
	 */
	public List<Map<String,Object>> fzyryzfjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("			select  @rownum:=@rownum+1 as 'key',a.fzysq ,a.xdwyj,DATE_FORMAT( a.tbrq,'%Y-%m-%d') AS bdrq,");
		sb.append("			DATE_FORMAT( a.SGLZXYJRQ,'%Y-%m-%d') as spsj,g.dwmc as ydwmc ");
		sb.append("		 from zs_zyswszfzy a,");
		sb.append("			 zs_fzysws f,zs_zysws b,");
		sb.append("			 zs_jg g,(select @rownum:=0) zs_sws");
		sb.append("			 where a.fzysws_id = f.id");
		sb.append("			 and a.zysws_id  = b.id");
		sb.append("			 and b.jg_id  = g.id");
		sb.append("			 and a.spzt_dm  = 2");
		sb.append("			 and f.RY_ID = ?");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 从业人员详细信息
	 */
	public Map<String,Object> cyryxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("	select @rownum:=@rownum+1 as 'key',c.dwmc,");	
		sb.append("		b.XMING as xm,");
		sb.append("		f.MC as cs,");
		sb.append("		d.MC as xb,");
		sb.append("		g.MC as mz,");
		sb.append("		date_format(b.SRI,'%Y-%m-%d') as csny,");
		sb.append("		e.MC as xl,");
		sb.append("		b.sfzh,");
		sb.append("		h.MC as zzmm,");
		sb.append("		b.txdz,b.XL_DM,");
		sb.append("		b.yddh,b.CS_DM,");
		sb.append("		b.yzbm,b.MZ_DM,");
		sb.append("		i.mc as zw,a.ZW_DM,");
		sb.append("		b.dhhm,b.ZZMM_DM,");
		sb.append("		b.byyx,b.XB_DM,");
		sb.append("		a.xzsngzgw,");
		sb.append("		date_format(b.BYSJ,'%Y-%m-%d') as bysj,");
		sb.append("		date_format(a.LRSJ,'%Y-%m-%d') as lrsj,");
		sb.append("		date_format(a.SWDLYWKSSJ,'%Y-%m-%d') as swdlywkssj,");
		sb.append("		a.zgxlzymc,");
		sb.append("		a.ZGXLFZJGJSJ as zgxlfzjgjsj,b.xpian,");
		sb.append("		b.rydazt");
		sb.append("		from zs_cyry a,zs_ryjbxx b,zs_jg c,dm_xb d,dm_xl e,dm_cs f,dm_mz g,dm_zzmm h,dm_zw i");
		sb.append("		where a.RY_ID = b.ID");
		sb.append("		and a.JG_ID = c.ID");
		sb.append("		and b.xb_dm = d.ID");
		sb.append("		and b.XL_DM = e.ID");
		sb.append("		and b.CS_DM = f.ID");
		sb.append("		and b.MZ_DM = g.ID");
		sb.append("		and b.ZZMM_DM = h.ID");
		sb.append("		and a.ZW_DM = i.ID");
		sb.append("		and c.id <> '-2'");
		sb.append("		and b.ID = ?");
		String sql = "SELECT @rownum:=@rownum+1 as 'key',a.qzny,a.xxxx,a.zmr FROM zs_jl a,zs_ryjbxx c,(select @rownum:=0) zs_sws WHERE a.ry_id = c.id  and a.xxxx is not null and c.ID = ? order by a.ID";
		List<Map<String, Object>> tl = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
		Map<String,Object> ll =tl.get(0);
		ll.put("ryjl", this.jdbcTemplate.queryForList(sql,new Object[]{id}));
		return ll;
	}
	/**
	 * 
	 * 
	 * @param id
	 * @return 从业人员变更记录
	 */
	public List<Map<String,Object>> cyrybgjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key',a.mc as bgmc,a.jzhi,a.xzhi,date_format(a.gxsj,'%Y-%m-%d') as gxsj ");
		sb.append("		from zs_cyrylsbgxxb a,zs_cyry b ,(select @rownum:=0) zs_sws");
		sb.append("		where a.cyry_id  = b.ID ");
		sb.append("		and b.ry_id = ?");
		sb.append("		order by a.gxsj");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * 事务所端执业人员查询
	 * @param jgid
	 * @return 
	 */
	public Map<String, Object> swszycx(int pn,int ps,int jgid,Map<String, Object> qury){
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		condition.add("b.xming", Condition.FUZZY, qury.get("xm"));
		condition.add("a.RYSPGCZT_DM", Condition.EQUAL, qury.get("ryzt"));
		condition.add("b.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		condition.add("b.CS_DM", Condition.EQUAL, qury.get("cs"));
		condition.add("b.xb_DM", Condition.EQUAL, qury.get("xb"));
		condition.add("b.xl_dm", Condition.EQUAL, qury.get("xl"));
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',a.id as zyswsid, b.id,b.xming,d.mc as xb,b.sfzh,a.zyzsbh,e.mc as cs,g.mc as zw,c.mc as ryzt, ");
		sb.append("		(select f.mc from dm_xl f where f.id=b.xl_dm) as xl,  ");
		sb.append("		a.ryspgczt_dm from zs_zysws a,zs_ryjbxx b,dm_ryspgczt c,dm_xb d,dm_cs e,dm_zw g,(select @rownum:=?) zs_ry  ");
		sb.append(condition.getSql());
		sb.append("		and  a.JG_ID=? and b.ID=a.ry_id and c.ID=a.RYSPGCZT_DM and ZYZT_DM in (1,3)");
		sb.append("		and b.XB_DM=d.ID and b.CS_DM=e.ID  and a.ZW_DM=g.ID");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "xm":
				if(asc){
					sb.append("		    order by convert( b.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( b.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "xl":
				if(asc){
					sb.append("		    order by b.xl_dm ");
				}else{
					sb.append("		    order by b.xl_dm desc");
				}
				break;
			case "zw":
				if(asc){
					sb.append("		    order by a.ZW_DM");
				}else{
					sb.append("		    order by a.ZW_DM desc");
				}
				break;
			case "ryzt":
				if(asc){
					sb.append("		    order by a.RYSPGCZT_DM ");
				}else{
					sb.append("		    order by a.RYSPGCZT_DM desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(jgid);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				link.put("herf_xxzl", url+"/ryxx/zyryxx/"+id);
				link.put("herf_bgjl", url+"/ryxx/zyrybgjl/"+id);
				link.put("herf_zsjl", url+"/ryxx/zyryzsjl/"+id);
				link.put("herf_zjjl", url+"/ryxx/zyryzjjl/"+id);
				link.put("herf_zzjl", url+"/ryxx/zyryzzjl/"+id);
				link.put("herf_njjl", url+"/ryxx/zyrynjjl/"+id);
				map.put("key", rs.getObject("key"));
				map.put("xh", rs.getObject("key"));
				map.put("_links", link);
				map.put("xm", rs.getObject("xming"));
				map.put("xb", rs.getObject("xb"));
				map.put("cs", rs.getObject("cs"));
				map.put("sfzh", rs.getObject("sfzh"));
				map.put("zyzsbh", rs.getObject("zyzsbh"));
				map.put("zw", rs.getObject("zw"));
				map.put("ryzt", rs.getObject("ryzt"));
				map.put("xl", rs.getObject("xl"));
				map.put("ryztdm", rs.getObject("ryspgczt_dm"));
				map.put("zyswsid", hashids.encode(rs.getLong("zyswsid")));
				map.put("ryid", id);
				return map;
				}
	});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	/**
	 * 
	 * 事务所端从业人员查询
	 * @param jgid
	 * @return 
	 */
	public Map<String, Object> swscycx(int pn,int ps,int jgid,Map<String, Object> qury){
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		condition.add("b.xming", Condition.FUZZY, qury.get("xm"));
		condition.add("b.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		condition.add("b.CS_DM", Condition.EQUAL, qury.get("cs"));
		condition.add("b.xb_DM", Condition.EQUAL, qury.get("xb"));
		condition.add("b.xl_dm", Condition.EQUAL, qury.get("xl"));
		condition.add("a.zw_dm", Condition.EQUAL, qury.get("zw"));
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key', b.id,a.id as cyid,b.xming,d.mc as xb,b.sfzh,e.mc as cs,f.mc as xl,g.mc as zw,a.CYRYZT_DM as ztdm");
		sb.append("		 from zs_cyry a,zs_ryjbxx b,dm_xb d,dm_cs e,dm_xl f,dm_zw g,(select @rownum:=?) zs_ry ");
		sb.append(condition.getSql());
		sb.append("		and  a.JG_ID=? and b.ID=a.ry_id and a.CYRYZT_DM in (1,3,14)");
		sb.append("		and b.XB_DM=d.ID and b.CS_DM=e.ID and f.ID=b.XL_DM and a.ZW_DM=g.ID");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "xm":
				if(asc){
					sb.append("		    order by convert( b.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( b.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "xl":
				if(asc){
					sb.append("		    order by b.xl_dm ");
				}else{
					sb.append("		    order by b.xl_dm desc");
				}
				break;
			case "zw":
				if(asc){
					sb.append("		    order by a.ZW_DM");
				}else{
					sb.append("		    order by a.ZW_DM desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(jgid);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				String cyid = hashids.encode(rs.getLong("cyid"));
				link.put("herf_xxzl", url+"/ryxx/cyryxx/"+id);
				link.put("herf_bgjl", url+"/ryxx/cyrybgjl/"+id);
				map.put("key", rs.getObject("key"));
				map.put("xh", rs.getObject("key"));
				map.put("_links", link);
				map.put("xm", rs.getObject("xming"));
				map.put("xb", rs.getObject("xb"));
				map.put("cs", rs.getObject("cs"));
				map.put("sfzh", rs.getObject("sfzh"));
				map.put("zw", rs.getObject("zw"));
				map.put("xl", rs.getObject("xl"));
				map.put("ryid", cyid);
				return map;
			}
		});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	//非执业备案通过列表
	public Map<String,Object> fzybatg(int pn,int ps,Map<String,Object> qury) {
		Condition condition = new Condition();
		condition.add("c.XMING", Condition.FUZZY, qury.get("XMING"));
		condition.add("b.ZYZGZSBH", Condition.EQUAL, qury.get("ZYZGZSBH"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT sql_calc_found_rows	 @rownum:=@rownum+1 'key', c.XMING,f.mc as XB,b.ZYZGZSBH,b.ZZDW,b.FZYZCZSBH,");
		sb.append(" DATE_FORMAT(d.TJSJ,'%Y-%m-%d') as TJSJ, ");
		sb.append(" DATE_FORMAT(e.SPSJ,'%Y-%m-%d') as SPSJ ");
		sb.append("		FROM zs_fzybasp a,zs_fzysws b,zs_ryjbxx c,zs_spzx d,zs_spxx e,dm_xb f,(SELECT @rownum:=?) tmp");
		sb.append("		 "+condition.getSql()+" ");
		sb.append("		and a.FZYSWS_ID=b.ID AND b.RY_ID=c.ID AND a.ID=d.SJID AND d.ID=e.SPID AND a.SPZT_DM=2 and e.ISPASS='Y' and b.FZYZT_DM=1");
		sb.append("		and f.ID=c.XB_DM order by e.SPSJ desc");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		return ob;
	}
	//执业转非执业通过列表
	public Map<String,Object> fzyzzytg(int pn,int ps,Map<String,Object> qury) {
		Condition condition = new Condition();
		condition.add("c.XMING", Condition.FUZZY, qury.get("XMING"));
		condition.add("b.ZYZGZSBH", Condition.EQUAL, qury.get("ZYZGZSBH"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT sql_calc_found_rows @rownum:=@rownum+1 as 'key', c.XMING,d.MC AS XB,");
		sb.append("	b.ZYZGZSBH,b.FZYZCZSBH,b.ZZDW,date_format(a.TBRQ,'%Y-%m-%d') as TBRQ,date_format(a.SGLZXYJRQ,'%Y-%m-%d') as SGLZXYJRQ");
		sb.append("		FROM zs_zyswszfzy a,zs_fzysws b,zs_ryjbxx c,dm_xb d,(select @rownum:=?) zs_ry");
		sb.append("		 "+condition.getSql()+" ");
		sb.append("		and a.FZYSWS_ID=b.ID AND b.RY_ID=c.ID AND a.SPZT_DM='2' and d.ID=c.XB_DM");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		return ob;
	}
	//非执业税务师转籍
	public Object fzyzjcx(String sfzh) {
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT c.XMING,'跨省转籍审批中' AS SPZT,'0' as JGLX ");
		sb.append("	FROM zs_fzyswszj a,zs_fzysws b,zs_ryjbxx c,dm_xb d ");
		sb.append("	WHERE a.FZY_ID=b.ID AND b.RY_ID=c.ID AND d.ID=c.XB_DM AND a.RYSPZT='0' and b.FZYZT_DM='1' and c.SFZH=? ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),sfzh);
		if(ls.size()!=0){
			return ls.get(0);
		}else{
			StringBuffer sb2 = new StringBuffer();
			sb2.append("		SELECT b.XMING,c.MC AS XB,a.ID as FID,b.CS_DM as CS,'1' as JGLX");
			sb2.append("			FROM zs_fzysws a,zs_ryjbxx b,dm_xb c");
			sb2.append("			WHERE a.RY_ID=b.ID AND b.XB_DM=c.ID AND a.FZYZT_DM='1' AND a.RYSPGCZT_DM='1'");
			sb2.append("			and b.SFZH=?");
			List<Map<String, Object>> ls2 = this.jdbcTemplate.queryForList(sb2.toString(),sfzh);
			if(ls2.size()!=0){
				return ls2.get(0);
			}
		}
		return false;
	}
	/**
	 * 执业税务师转籍统计
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 */
	public Map<String,Object> zyswszjtj(int pn,int ps,Map<String, Object> qury) {
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		condition.add("d.xming", Condition.FUZZY, qury.get("xm"));
		condition.add("c.DWMC", Condition.FUZZY, qury.get("yjg"));
		condition.add("d.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		condition.add("d.CS_DM", Condition.EQUAL, qury.get("cs"));
		condition.add("d.xb_DM", Condition.EQUAL, qury.get("xb"));
		condition.add("b.ZYZSBH", Condition.EQUAL, qury.get("zczs"));
		StringBuffer sb = new StringBuffer();
		sb.append("	select SQL_CALC_FOUND_ROWS ");
		sb.append("		@rownum:=@rownum+1 as 'key',");
		sb.append("		d.id,d.XMING,b.ZYZSBH,c.DWMC,e.MC as jgxz,a.XJGMC,a.DRS,a.XJGDH");
		sb.append("		FROM zs_zyswszj a,zs_zysws b,zs_jg c,zs_ryjbxx d,dm_jgxz e,(select @rownum:=?) zs_ry ");
		sb.append(condition.getSql());
		sb.append("		and a.ZYSWS_ID=b.ID AND b.JG_ID=c.ID AND a.SPZT_DM=2 AND b.RY_ID=d.ID");
		sb.append("		and c.JGXZ_DM=e.ID");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "XMING":
				if(asc){
					sb.append("		    order by convert( d.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( d.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "jgxz":
				if(asc){
					sb.append("		    order by c.JGXZ_DM ");
				}else{
					sb.append("		    order by c.JGXZ_DM desc");
				}
				break;
			case "DWMC":
				if(asc){
					sb.append("		    order by convert( c.DWMC USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( c.DWMC USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				link.put("herf_xxzl", url+"/ryxx/zyryxx/"+id);
				link.put("herf_bgjl", url+"/ryxx/zyrybgjl/"+id);
				link.put("herf_zsjl", url+"/ryxx/zyryzsjl/"+id);
				link.put("herf_zjjl", url+"/ryxx/zyryzjjl/"+id);
				link.put("herf_zzjl", url+"/ryxx/zyryzzjl/"+id);
				link.put("herf_spzt", url+"/ryxx/zyryspzt/"+id);
				link.put("herf_njjl", url+"/ryxx/zyrynjjl/"+id);
				map.put("key", rs.getObject("key"));
				map.put("_links", link);
				map.put("XMING", rs.getObject("XMING"));
				map.put("ZYZSBH", rs.getObject("ZYZSBH"));
				map.put("DWMC", rs.getObject("DWMC"));
				map.put("jgxz", rs.getObject("jgxz"));
				map.put("XJGMC", rs.getObject("XJGMC"));
				map.put("DRS", rs.getObject("DRS"));
				map.put("XJGDH", rs.getObject("XJGDH"));
				return map;
				}
	});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
		}
	/**
	 * 执业税务师转出统计
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 */
	public Map<String,Object> zyswszctj(int pn,int ps,Map<String, Object> qury) {
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		condition.add("d.xming", Condition.FUZZY, qury.get("xm"));
		condition.add("c.DWMC", Condition.FUZZY, qury.get("yjg"));
		condition.add("d.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		condition.add("d.CS_DM", Condition.EQUAL, qury.get("cs"));
		condition.add("d.xb_DM", Condition.EQUAL, qury.get("xb"));
		condition.add("b.ZYZSBH", Condition.EQUAL, qury.get("zczs"));
		StringBuffer sb = new StringBuffer();
		sb.append("	select SQL_CALC_FOUND_ROWS ");
		sb.append("		@rownum:=@rownum+1 as 'key',");
		sb.append("		d.id, d.XMING,b.ZYZSBH,c.DWMC as yjg,e.DWMC as xjg,e.DZHI,e.DHUA");
		sb.append("		FROM zs_zyswssndz a,zs_zysws b,zs_jg c,zs_ryjbxx d,zs_jg e,(select @rownum:=?) zs_ry ");
		sb.append(condition.getSql());
		sb.append("		and a.ry_id=b.ID AND a.YJG_ID=c.ID AND a.SPZT_DM=2 AND b.RY_ID=d.ID");
		sb.append("		and a.XJG_ID=e.ID");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "XMING":
				if(asc){
					sb.append("		    order by convert( d.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( d.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "yjg":
				if(asc){
					sb.append("		    order by convert( c.DWMC USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( c.DWMC USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				link.put("herf_xxzl", url+"/ryxx/zyryxx/"+id);
				link.put("herf_bgjl", url+"/ryxx/zyrybgjl/"+id);
				link.put("herf_zsjl", url+"/ryxx/zyryzsjl/"+id);
				link.put("herf_zjjl", url+"/ryxx/zyryzjjl/"+id);
				link.put("herf_zzjl", url+"/ryxx/zyryzzjl/"+id);
				link.put("herf_spzt", url+"/ryxx/zyryspzt/"+id);
				link.put("herf_njjl", url+"/ryxx/zyrynjjl/"+id);
				map.put("key", rs.getObject("key"));
				map.put("_links", link);
				map.put("XMING", rs.getObject("XMING"));
				map.put("ZYZSBH", rs.getObject("ZYZSBH"));
				map.put("yjg", rs.getObject("yjg"));
				map.put("xjg", rs.getObject("xjg"));
				map.put("DZHI", rs.getObject("DZHI"));
				map.put("DHUA", rs.getObject("DHUA"));
				return map;
			}
		});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	/**
	 * 执业管理手册打印
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 */
	public Map<String,Object> zyglscdy(int pn,int ps,Map<String, Object> qury) {
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		condition.add("b.xming", Condition.FUZZY, qury.get("xm"));
		condition.add("c.DWMC", Condition.FUZZY, qury.get("dwmc"));
		condition.add("b.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		condition.add("b.CS_DM", Condition.EQUAL, qury.get("cs"));
		condition.add("b.xb_DM", Condition.EQUAL, qury.get("xb"));
		condition.add("a.ZYZSBH", Condition.EQUAL, qury.get("zczs"));
		StringBuffer sb = new StringBuffer();
		sb.append("	select SQL_CALC_FOUND_ROWS ");
		sb.append("		@rownum:=@rownum+1 as 'key',");
		sb.append("		b.id,b.XMING,d.MC as xb,b.DHHM,b.SFZH,e.MC as xl,a.ZYZGZSBH,a.ZYZSBH,c.DWMC,a.GRHYBH,date_format(b.SRI,'%Y年%m月%d日') as sri,");
		sb.append("	case a.czr_dm when 1 then \"是\"  when 2 then \"否\" else null end as czr,");	
		sb.append("	case a.fqr_dm when 1 then \"是\"  when 2 then \"否\" else null end as fqr");
		sb.append("		FROM zs_zysws a,zs_ryjbxx b,zs_jg c,dm_xb d,dm_xl e,(select @rownum:=?) zs_ry ");
		sb.append(condition.getSql());
		sb.append("		and a.RY_ID=b.ID AND a.JG_ID=c.ID AND a.ZYZT_DM=1");
		sb.append("		and b.XB_DM=d.ID and b.XL_DM=e.ID");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "XMING":
				if(asc){
					sb.append("		    order by convert( b.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( b.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "xl":
				if(asc){
					sb.append("		    order by b.XL_DM ");
				}else{
					sb.append("		    order by b.XL_DM desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				link.put("herf_xxzl", url+"/ryxx/zyryxx/"+id);
				link.put("herf_bgjl", url+"/ryxx/zyrybgjl/"+id);
				link.put("herf_zsjl", url+"/ryxx/zyryzsjl/"+id);
				link.put("herf_zjjl", url+"/ryxx/zyryzjjl/"+id);
				link.put("herf_zzjl", url+"/ryxx/zyryzzjl/"+id);
				link.put("herf_spzt", url+"/ryxx/zyryspzt/"+id);
				link.put("herf_njjl", url+"/ryxx/zyrynjjl/"+id);
				map.put("key", rs.getObject("key"));
				map.put("_links", link);
				map.put("XMING", rs.getObject("XMING"));
				map.put("ZYZSBH", rs.getObject("ZYZSBH"));
				map.put("xb", rs.getObject("xb"));
				map.put("DHHM", rs.getObject("DHHM"));
				map.put("SFZH", rs.getObject("SFZH"));
				map.put("xl", rs.getObject("xl"));
				map.put("ZYZGZSBH", rs.getObject("ZYZGZSBH"));
				map.put("DWMC", rs.getObject("DWMC"));
				map.put("czr", rs.getObject("czr"));
				map.put("fqr", rs.getObject("fqr"));
				map.put("GRHYBH", rs.getObject("GRHYBH"));
				map.put("SRI", rs.getObject("sri"));
				return map;
			}
		});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	/**
	 * 人员调入
	 * @param rylx
	 * @param qury
	 * @return
	 */
	public List<Map<String,Object>> zyglscdy(int rylx,Map<String, Object> qury,int pJgid) {
		StringBuffer sb = new StringBuffer();
		switch(rylx){
		case 1:
			sb.append("		SELECT b.XMING as '执业税务师姓名：',b.SFZH as '身份证号：',d.MC as '性别：',e.MC as '学历：',a.ZYZSBH as '证书编号：',a.id");
			sb.append("		FROM zs_zysws a,zs_ryjbxx b,dm_xb d,dm_xl e");
			sb.append("		WHERE a.RY_ID=b.ID AND a.ZYZT_DM=1 AND a.JG_ID=-2 AND b.XB_DM=d.ID AND b.XL_DM=e.ID and a.RYSPGCZT_DM=1 and a.ZYZSBH=? and b.sfzh=?");
			break;
		case 2:
			sb.append("		SELECT b.XMING as '从业人员姓名：',b.SFZH as '身份证号：',d.MC as '性别：',e.MC as '学历：',a.id");
			sb.append("		FROM zs_cyry a,zs_ryjbxx b,dm_xb d,dm_xl e");
			sb.append("		WHERE a.RY_ID=b.ID AND a.CYRYZT_DM=1 AND a.JG_ID=-2 AND b.XB_DM=d.ID AND b.XL_DM=e.ID and b.xming=? and b.sfzh=?");
			break;
		case 3:
			sb.append("		SELECT b.XMING as '执业税务师姓名：',b.SFZH as '身份证号：',d.MC as '性别：',e.MC as '学历：',a.ZYZSBH as '证书编号：',a.id");
			sb.append("		FROM zs_zysws a,zs_ryjbxx b,dm_xb d,dm_xl e");
			sb.append("		WHERE a.RY_ID=b.ID AND a.ZYZT_DM=1 AND a.JG_ID in (select id from zs_jg where PARENTJGID='"+pJgid+"' and JGZT_DM=11)");
			sb.append("		 AND b.XB_DM=d.ID AND b.XL_DM=e.ID and a.RYSPGCZT_DM=1 and a.ZYZSBH=? and b.sfzh=?");
			break;
		case 4:
			sb.append("		SELECT b.XMING as '从业人员姓名：',b.SFZH as '身份证号：',d.MC as '性别：',e.MC as '学历：',a.id");
			sb.append("		FROM zs_cyry a,zs_ryjbxx b,dm_xb d,dm_xl e");
			sb.append("		WHERE a.RY_ID=b.ID AND a.CYRYZT_DM=1 AND a.JG_ID in (select id from zs_jg where PARENTJGID='"+pJgid+"' and JGZT_DM=11)");
			sb.append("		 AND b.XB_DM=d.ID AND b.XL_DM=e.ID and b.xming=? and b.sfzh=?");
			break;
		case 5:
			sb.append("		SELECT b.XMING as '非执业税务师姓名：',b.SFZH as '身份证号：',d.MC as '性别：',e.MC as '学历：',a.FZYZCZSBH as '非执业注册证书编号：',a.id,");
			sb.append("		a.ZZDW as '工作单位：' FROM zs_fzysws a,zs_ryjbxx b,dm_xb d,dm_xl e");
			sb.append("		WHERE a.RY_ID=b.ID AND a.FZYZT_DM=1 AND b.XB_DM=d.ID AND b.XL_DM=e.ID and a.RYSPGCZT_DM=1 and a.FZYZCZSBH=? and b.sfzh=?");
			break;
		}
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{qury.get("xming"),qury.get("sfzh")});
	}
	/**
	 * 人员相片更新
	 * @param ryid
	 * @param path
	 */
	public void ryxpgx(int ryid,String path) {
		this.jdbcTemplate.update("update zs_ryjbxx a set a.xpian=? where id=?",new Object[]{path,ryid});
	}
	/**
	 * 人员入所类别
	 * @param ryid
	 * @param path
	 */
	public Map<String, Object> swsbarslx(int ryid) {
		return this.jdbcTemplate.queryForMap("select ZYRYBALB_DM as rslb,DCS,YJGMC,YJGDH from zs_zyrybayyb where ZYSWS_ID=? limit 1",new Object[]{ryid});
	}
	/**
	 * 税务师变动统计
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 */
	public Map<String,Object> swsbdtj(int pn,int ps,Map<String, Object> qury) {
		Condition condition = new Condition();
		condition.add("DATE_FORMAT(g.SPSJ,'%Y')", Condition.EQUAL, qury.get("YEAR"));
		if(qury.containsKey("MON")){
			condition.add("DATE_FORMAT(g.SPSJ,'%m')", Condition.EQUAL, qury.get("MON"));
		}
		condition.add("g.XMING", Condition.EQUAL, qury.get("xm"));
		condition.add("g.BD_DM", Condition.EQUAL, qury.get("spsx"));
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key', g.*,DATE_FORMAT(g.SPSJ,'%Y-%m-%d %H-%i-%s') AS SJ ");
		sb.append("				FROM v_zs_rysptj g,(select @rownum:=?) zs_ry ");
		sb.append("				 "+condition.getSql()+"  ");
		sb.append("				order by g.spsj desc LIMIT ?, ?");
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

