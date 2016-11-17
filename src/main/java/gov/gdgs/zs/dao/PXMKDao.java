package gov.gdgs.zs.dao;

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
		sb.append("		SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',a.id as pxid,a.BT,a.PXDZ as PXDD,a.QS,a.PXDDDH,a.PXNR,a.PXKSSJ,a.PXJSSJ,a.BMJZSJ,a.PXLXR,"); 
		sb.append("	a.ZYSX,a.ZAOC,a.WUC,a.WANC,a.DRJ,a.SRJ,a.BGDH as BGZJ,a.HWWZFJH as HWZDHHM,"); 
		sb.append("	 a.FBZT as fbzt,"); 
		sb.append("	 (select count(b.id) from zs_pxqkbmb b WHERE a.ID=b.PXID and b.YXBZ=1 )as bmrs,"); 
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
}