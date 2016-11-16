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
		sb.append("		SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',a.id as pxid,a.BT,a.PXNR,a.PXKSSJ,a.PXJSSJ,a.BMJZSJ,a.PXLXR,"); 
		sb.append("	case a.FBZT when 0 then '进行中'  when 1 then '活动结束' else null end as fbzt,"); 
		sb.append("	 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.YXBZ=1 )as bmrs,"); 
		sb.append("	   concat((select cast(count(b.id) as char) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=1 and b.YXBZ=1 ),'/',"); 
		sb.append("	  (select cast(ceil (count(b.id) / 2 ) as char) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.FJLX=2 and b.YXBZ=1 ))as dfs,");
		sb.append("	CONCAT((SELECT cast(COUNT(b.id) as char) FROM zs_pxqkbmb b");
		sb.append("	WHERE a.ID=b.PXID AND b.ZAOC=1 AND b.YXBZ=1),'/',	 (SELECT cast(COUNT(b.id) as char) FROM zs_pxqkbmb b");
		sb.append("	WHERE a.ID=b.PXID AND b.WUC=1 AND b.YXBZ=1),'/',(SELECT cast(COUNT(b.id) as char) FROM zs_pxqkbmb b");
		sb.append("	WHERE a.ID=b.PXID AND b.WANC=1 AND b.YXBZ=1)) AS dcs");
		sb.append("	FROM zs_pxqkb a,(select @rownum:=?) zs_ry "+condition.getSql()+" and a.YXBZ=1 LIMIT ?, ?"); 
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
				sqxx.get("QS"),sqxx.get("PXKSSJ"),sqxx.get("PXJSSJ"),sqxx.get("BMJSSJ"),
				sqxx.get("PXDDDH"),sqxx.get("PXLXR"),sqxx.get("PXNR"),sqxx.get("ZYSX"),
				sqxx.get("SRJ"),sqxx.get("DRJ"),sqxx.get("ZAOC"),sqxx.get("WUC"),
				sqxx.get("WANC"),sqxx.get("BGZJ"),sqxx.get("HWZDHHM")});
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
		List<Map<String,Object>> ls =  this.jdbcTemplate.queryForList(sql);
		if (ls.size()>0){
			return ls.get(0);
		}
		return null;
	}

	public Map<String, Object> getPxxxForUser(int page, int pagesize,
			Condition condition) {
		// TODO Auto-generated method stub
		return null;
	}
}
