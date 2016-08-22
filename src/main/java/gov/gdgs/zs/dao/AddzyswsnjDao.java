package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class AddzyswsnjDao extends BaseDao {

	//执业税务师年检
    public Map<String, Object> getZyswsnjb(int page, int pageSize,int Jgid,
			Map<String, Object> where) {
			
			Condition condition = new Condition();
			condition.add("a.nd", Condition.EQUAL, where.get("nd"));
			condition.add("b.dwmc",Condition.FUZZY,where.get("dwmc"));
			condition.add("a.ZTDM", Condition.EQUAL, where.get("ZTDM"));
            

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT  SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',t.*");
			sb.append(" from  ( select a.id,a.ND,c.XMING,b.dwmc,");
			sb.append(" CASE a.ZTDM WHEN 0 THEN '退回' WHEN 1 THEN '保存' WHEN 2 THEN '自检' WHEN 3 THEN '年检' ELSE NULL END AS ZTDM");
			sb.append(" from "+Config.PROJECT_SCHEMA+"zs_zcswsnj a,zs_jg b,zs_ryjbxx c, zs_zysws d,(SELECT @rownum:=?) temp");
			sb.append(condition.getSql());//相当元 where x.xx like '%%'
			sb.append(" and a.ZSJG_ID=? and  a.SWS_ID=d.ID and c.ID=d.RY_ID   order by a.ND desc) as t");
			sb.append("    LIMIT ?, ? ");
			// 装嵌传值数组
			int startIndex = pageSize * (page - 1);
			ArrayList<Object> params = condition.getParams();
			params.add(0, pageSize * (page - 1));
			params.add(Jgid);
			params.add(startIndex);
			params.add(pageSize);

			// 获取符合条件的记录
			List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
					params.toArray());

			// 获取符合条件的记录数

			int total = jdbcTemplate.queryForObject("SELECT FOUND_ROWS()",
					Integer.class);

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("data", ls);
			obj.put("total", total);
			obj.put("pageSize", pageSize);
			obj.put("current", page);

			return obj;
		}
	
	
}
