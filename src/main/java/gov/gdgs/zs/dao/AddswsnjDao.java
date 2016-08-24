package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
@Repository
public class AddswsnjDao extends BaseDao{
	
	public Map<String,Object> getswsnjb(int page, int pageSize,int Jgid,
			Map<String, Object> where) {
		Condition condition = new Condition();
		condition.add("c.dwmc", Condition.FUZZY, where.get("dwmc"));
		condition.add("c.cs_dm", Condition.EQUAL, where.get("cs"));
		condition.add("a.nd", Condition.EQUAL, where.get("nd"));
		condition.add("a.ztdm", Condition.EQUAL, where.get("bbzt"));
		condition.add("a.zjrq", Condition.GREATER_EQUAL, where.get("sbsj"));
		condition.add("a.zjrq", Condition.LESS_EQUAL, where.get("sbsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		select  SQL_CALC_FOUND_ROWS  @rownum:=@rownum+1 AS 'key',v.* from ( SELECT ");
		sb.append("		c.dwmc,c.JGZCH as zsbh,d.mc as jgxz,c.yzbm,c.DZHI as bgdz,c.DHUA as dhhm,a.*,");
		sb.append("		case a.ztdm when 3 then '已年检' when 2 then '已自检'  "
				+ " else null end as njzt, CASE a.WGCL_DM WHEN 1 THEN '年检予以通过' WHEN 2 THEN '年检不予通过，"
				+ "责令2个月整改' WHEN 6 THEN '年检不予以通过' WHEN 7 THEN '资料填写有误，请重新填写' ELSE NULL END AS njcl,"
				+ "DATE_FORMAT(a.zjsj,'%Y-%m-%d') AS zjrq,DATE_FORMAT(c.SWSZSCLSJ ,'%Y-%m-%d') AS clsj,"
				+ "DATE_FORMAT(a.fzrsj,'%Y-%m-%d') AS qzrq");
		sb.append("	 FROM  zs_jg_njb a,zs_jg c,dm_jgxz d");
		sb.append("		"+condition.getSql()+" ");
		//sb.append("	and a.ZSJG_ID = c.ID and a.ztdm in (2,3)  and d.ID = c.JGXZ_DM");
		sb.append("	and a.ZSJG_ID=c.ID and a.ZSJG_ID=? and a.ztdm in (2,3)  and d.ID = c.JGXZ_DM");
		sb.append("	group by a.zsjg_id,nd order by a.ND desc");
		sb.append("	 ) as v ,(SELECT @rownum:=?) zs_jg");
		//sb.append("	 ) as v ");
		//sb.append("	 ) as v");
		sb.append("		LIMIT ? ,?");
		//ArrayList<Object> params = condition.getParams();
		//params.add((page-1)*pageSize);
		//params.add((page-1)*pageSize);
		//params.add(pageSize);
		
		// 装嵌传值数组
				int startIndex = pageSize * (page - 1);
				ArrayList<Object> params = condition.getParams();
				
				params.add(0,Jgid);
				params.add( pageSize * (page - 1));
		        params.add(startIndex);
				params.add(pageSize);
				
//				params.add(0, pageSize * (page - 1));
//		        params.add(Jgid);	
//				params.add(startIndex);
//				params.add(pageSize);

				// 获取符合条件的记录
				List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
						params.toArray());
		
		//List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);
		obj.put("jg_id", Jgid);
		return obj;
		
		
//		Map<String,Object> ob = new HashMap<>();
//		ob.put("data", ls);
//		ob.put("total",total);
//		Map<String, Object> meta = new HashMap<>();
//		meta.put("pageNum", page);
//		meta.put("pageSize", pageSize);
		//meta.put("pageTotal",total);
//		meta.put("pageAll",(total + pageSize - 1) / pageSize);
//		ob.put("page", meta);
		
//		return ob;
}
	public Map<String, Object> getswsnjbById(String id) {
		String sql="select c.dwmc,c.JGZCH as zsbh,d.mc as jgxz,c.yzbm,c.DZHI as bgdz,c.DHUA as dhhm,a.*,"
				+ "case a.ztdm when 3 then '已年检' when 2 then '已自检'else null end as njzt,"
				+ " CASE a.WGCL_DM WHEN 1 THEN '年检予以通过' "
				+ "WHEN 2 THEN '年检不予通过，责令2个月整改' "
				+ "WHEN 6 THEN '年检不予以通过' "
				+ "WHEN 7 THEN '资料填写有误，请重新填写' ELSE NULL END AS njcl,"
				+ "DATE_FORMAT(a.zjsj,'%Y-%m-%d') AS zjrq,"
				+ "DATE_FORMAT(c.SWSZSCLSJ ,'%Y-%m-%d') AS clsj,"
				+ "DATE_FORMAT(a.fzrsj,'%Y-%m-%d') AS qzrq "
				+ "FROM  zs_jg_njb a,zs_jg c,dm_jgxz d "
				+ "where a.ztdm in (2,3)  and d.ID = c.JGXZ_DM and a.ZSJG_ID=c.ID and a.id=?";
		//String sql = "select b.DWMC,CASE a.TIMEVALUE WHEN 0 THEN '半年' WHEN 1 THEN '全年' ELSE NULL END AS TIMEVALUE,a.* from "+Config.PROJECT_SCHEMA+"zs_cwbb_lrgd a, zs_jg b where a.jg_id = b.id and a.id = ?";
		Map<String,Object> rs = jdbcTemplate.queryForMap(sql, id);
		return rs;
	}
}
