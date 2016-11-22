package gov.gdgs.zs.dao;

import gov.gdgs.zs.dao.YwglDao.YwbbRowMapper;
import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.entity.User;

@Repository
public class PXMKDao extends BaseDao{

	/**
	 * 中心端培训信息列表
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 */
	public Map<String,Object> getpxfbList(int pn,int ps,Map<String, Object> qury){
		Condition condition = new Condition();
		condition.add("a.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("a.nd", Condition.EQUAL, qury.get("nd"));
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',a.id as pxid,a.BT,a.PXDZ as PXDD,a.QS,a.PXDDDH,a.PXNR,a.PXKSSJ,a.PXJSSJ,a.BMJZSJ,a.PXLXR,"); 
		sb.append("	a.ZYSX,a.ZAOC,a.WUC,a.WANC,a.DRJ,a.SRJ,a.BGDH as BGZJ,a.HWWZFJH as HWZDHHM,"); 
		sb.append("	 a.FBZT as fbzt,"); 
		sb.append("	 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.YXBZ=1 )as bmrs,");
		sb.append("		(select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX in (1,2) and b.YXBZ=1 )as zszrs,"); 
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=1 and b.YXBZ=1 )as drzs,");
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=2 and b.XB='男' and b.YXBZ=1 )as srnan,");
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=2 and b.XB='女' and b.YXBZ=1 )as srnv, ");
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and (b.ZAOC=1 or b.WUC=1 or b.WANC=1) and b.YXBZ=1 )as bczrs,");
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.ZAOC=1 and b.YXBZ=1 )as zcrs,");
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.WUC=1 and b.YXBZ=1 )as wucrs,");
		sb.append("		 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.WANC=1 and b.YXBZ=1 )as wcrs,");
		sb.append("	   concat((select cast(count(b.id) as char) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=1 and b.YXBZ=1 ),'/',"); 
		sb.append("	  (select cast(ceil (count(b.id) / 2 ) as char) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=2 and b.YXBZ=1 ))as dfs,");
		sb.append("	CONCAT((SELECT cast(COUNT(b.id) as char) FROM zs_pxqkbmb b");
		sb.append("	WHERE a.ID=b.PXID AND b.ZAOC=1 AND b.YXBZ=1),'/',	 (SELECT cast(COUNT(b.id) as char) FROM zs_pxqkbmb b");
		sb.append("	WHERE a.ID=b.PXID AND b.WUC=1 AND b.YXBZ=1),'/',(SELECT cast(COUNT(b.id) as char) FROM zs_pxqkbmb b");
		sb.append("	WHERE a.ID=b.PXID AND b.WANC=1 AND b.YXBZ=1)) AS dcs");
		sb.append("	FROM (select * from zs_pxqkb order by lrrq desc)  a,(select @rownum:=?) zs_ry "+condition.getSql()+" and a.YXBZ=1 LIMIT ?, ?"); 
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
	
	public void pxxxfb(Map<String, Object> sqxx) throws Exception {
		String sql ="insert into zs_pxqkb (ID,BT,PXDZ,QS,PXKSSJ,PXJSSJ,BMJZSJ,PXDDDH,PXLXR,PXNR,ZYSX,SRJ,DRJ,ZAOC,WUC,WANC,BGDH,HWWZFJH,FBZT,LRRQ,YXBZ) values(replace(uuid(),'-',''),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'0',sysdate(),'1')";
		this.jdbcTemplate.update(sql, new Object[]{sqxx.get("BT"),sqxx.get("PXDD"),
				sqxx.get("QS"),sqxx.get("PXKSSJ"),sqxx.get("PXJSSJ"),sqxx.get("BMJZSJ"),
				sqxx.get("PXDDDH"),sqxx.get("PXLXR"),sqxx.get("PXNR"),sqxx.get("ZYSX"),
				sqxx.get("SRJ"),sqxx.get("DRJ"),sqxx.get("ZAOC"),sqxx.get("WUC"),
				sqxx.get("WANC"),sqxx.get("BGZJ"),sqxx.get("HWZDHHM")});
	}
	
	public void pxxxxg(Map<String, Object> sqxx) throws Exception {
		String sql ="update zs_pxqkb set BT=?,PXDZ=?,QS=?,PXKSSJ=?,PXJSSJ=?,BMJZSJ=?,PXDDDH=?,PXLXR=?,PXNR=?,ZYSX=?,SRJ=?,DRJ=?,ZAOC=?,WUC=?,WANC=?,BGDH=?,HWWZFJH=?,FBZT='0' where ID=?";
		this.jdbcTemplate.update(sql, new Object[]{sqxx.get("BT"),sqxx.get("PXDD"),
				sqxx.get("QS"),sqxx.get("PXKSSJ"),sqxx.get("PXJSSJ"),sqxx.get("BMJZSJ"),
				sqxx.get("PXDDDH"),sqxx.get("PXLXR"),sqxx.get("PXNR"),sqxx.get("ZYSX"),
				sqxx.get("SRJ"),sqxx.get("DRJ"),sqxx.get("ZAOC"),sqxx.get("WUC"),
				sqxx.get("WANC"),sqxx.get("BGZJ"),sqxx.get("HWZDHHM"),sqxx.get("pxid")});
	}
	
	public void pxxxsc(Map<String, Object> sqxx) throws Exception {
		String sql ="update zs_pxqkb set YXBZ='0' where ID=?";
		this.jdbcTemplate.update(sql, new Object[]{sqxx.get("pxid")});
	}
	
	public void pxxxtz(Map<String, Object> sqxx) throws Exception {
		String sql ="update zs_pxqkb set FBZT='1' where ID=?";
		this.jdbcTemplate.update(sql, new Object[]{sqxx.get("pxid")});
	}
	public List<Map<String,Object>> pxtjbmList(String pxid){
		StringBuffer sb = new StringBuffer();
		sb.append("	select b.DWMC,a.XMING,a.XB,");
		sb.append("	a.ZW,a.YDDH,");
		sb.append("	case a.FJLX when 1 then '单' when 2 then '双' else null end as DF,");
		sb.append("	a.RZSJ,a.LKSJ,");
		sb.append("	concat((case a.ZAOC when 1 then '早餐' else '' end),");
		sb.append("	(case a.WUC when a.ZAOC=1 and a.WUC=1 and a.WANC=1 then '，午餐，' ");
		sb.append("	when a.ZAOC=0 and a.WUC=1 and a.WANC=1 then '午餐，'");
		sb.append("	when a.ZAOC=0 and a.WUC=1 and a.WANC=0 then '午餐' ");
		sb.append("	when a.ZAOC=1 and a.WUC=1 and a.WANC=0 then '，午餐' ");
		sb.append("	when a.ZAOC=1 and a.WUC=0 and a.WANC=1 then '，'");
		sb.append("	 else '' end),");
		sb.append("	(case a.WANC when 1 then '晚餐' else ''end)) as DCQK,a.BZ");
		sb.append("	 from zs_pxqkbmb a,zs_jg b where a.JG_ID=b.ID and a.pxid=?");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{pxid});
	}
	public Map<String, Object> getPxxx(int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.bt,t.pxkssj,t.pxjssj,t.pxlxr,t.bmjzsj,t.fbzt,@rownum := @rownum + 1 AS xh ");
		sb.append(" FROM zs_pxqkb t, ");
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_pxqkb", "id"));
		sb.append("    ORDER BY lrrq desc  ");
		sb.append("    LIMIT ? , ?) sub, ");
		// ===> 插入查询条件集合结束
		sb.append(" (SELECT @rownum:=?) temp ");
		sb.append(" where t.id = sub.id ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);
		params.add(startIndex);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("id", "zs_pxqkb");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public Map<String, Object> getPxnr(String id) {
		String sql = " select bt,pxkssj,pxjssj,pxlxr,pxnr,zysx from zs_pxqkb where id = ? ";
		List<Map<String,Object>> ls =  this.jdbcTemplate.queryForList(sql,new Object[]{id});
		if (ls.size()>0){
			return ls.get(0);
		}
		return null;
	}

	public Map<String, Object> getPxxxForUser(User user , int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT t.id,t.bt,t.pxkssj,t.pxjssj,t.pxlxr,t.bmjzsj,t.fbzt,t.lrrq, ");
		sb.append(" t.srj,t.drj,t.zaoc,t.wuc,t.wanc,t.bgdh,t.hwwzfjh,t.pxdz,t.pxdddh, ");
		sb.append(" @rownum := @rownum + 1 as xh, ");
		sb.append(" if(b.jg_id is not null,1,0) as isbm ");
		sb.append(" FROM (zs_pxqkb t, ");
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_pxqkb", "id"));
		sb.append("    ORDER BY lrrq desc  ");
		sb.append("    LIMIT ? , ?) sub, ");
		// ===> 插入查询条件集合结束
		sb.append(" (SELECT @rownum:=?) temp) ");
		sb.append(" left join ");
		sb.append(" (select distinct jg_id,pxid from zs_pxqkbmb) b ");
		sb.append(" on (b.PXID = t.id and b.jg_id = ?) ");
		sb.append(" where sub.id = t.id ");
		sb.append(" order by t.lrrq desc ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);
		params.add(startIndex);
		params.add(user.getJgId());

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("id", "zs_pxqkb");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);
		return obj;
	}

	public List<Map<String, Object>> getPxbmRy(User user, String id) {
		String sql = " select xming,xb,zw,yddh,dhhm,nl,email,fjlx,zaoc,wuc,wanc,rzsj,lksj,bz "
				+ "from zs_pxqkbmb where pxid = ? and jg_id= ? and yxbz =1 ";
		return   this.jdbcTemplate.queryForList(sql,new Object[]{id,user.getJgId()});
	}

	public Map<String, Object> getPxxxMx(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select fbzt,bt,pxdz,qs,pxkssj,pxjssj,bmjzsj,pxdddh,pxlxr,pxnr,zysx,");
		sb.append(" srj,drj,zaoc,wuc,wanc,bgdh,hwwzfjh from zs_pxqkb where id = ? and yxbz = 1 ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
		if(ls.size()>0){
			return ls.get(0);
		}
		return null;
	}
	
	public int[] addPxbm (Map<String, Object>[] values){
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into zs_pxqkbmb (ID,PXID,JG_ID,XMING,XB,ZW,YDDH,DHHM,NL,EMAIL,FJLX,ZAOC,WUC,WANC,RZSJ,LKSJ,BMSJ,BZ,YXBZ) ");
		sb.append(" values(:id,:pxid,:jg_id,:xming,:xb,:zw,:yddh,:dhhm,:nl,:email,:fjlx,:zaoc,:wuc,:wanc,:rzsj,:lksj,:bmsj,:bz,1) ");
		return this.namedParameterJdbcTemplate.batchUpdate(sb.toString(), values);
	}
	public int delRyByPxidAndUser(User user, String pxid){
		String sql = " delete from zs_pxqkbmb where pxid=? and jg_id=? ";
		return this.jdbcTemplate.update(sql, new Object[]{pxid,user.getJgId()});
	}
}
