package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class YhdljlbDao extends BaseDao {
	
	public Map<String, Object> yhdljlb(int pn,int ps,Map<String,Object> where){
		Condition condition=new Condition();
	    condition.add("r.NAME",Condition.EQUAL,where.get("name"));
	    condition.add("l.ACCESS_IP",Condition.EQUAL,where.get("ACCESS_IP"));
		condition.add("r.DESCRIPTION", Condition.EQUAL, where.get("DESCRIPTION"));
		condition.add("l.ACCESS_TIME", Condition.GREATER_EQUAL, where.get("dlsj"));
		condition.add("l.ACCESS_TIME", Condition.LESS_EQUAL, where.get("dlsj2"));
		
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',l.USER_ID,l.ACCESS_IP,date_format(l.ACCESS_TIME,'%Y-%m-%d') as ACCESS_TIME,l.ACTION,r.DESCRIPTION,r.NAME ");
		sb.append(" FROM fw_users u, fw_user_log l, fw_user_role ur , fw_role r ");
		sb.append( condition.getSql());
		sb.append(" and r.ID=ur.ROLE_ID AND l.USER_ID=u.ID AND ur.USER_ID=u.ID ");
		

		ArrayList<Object> params = condition.getParams();
		
		// 获取符合条件的记录
				List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
						params.toArray());
		
		
		int total = jdbcTemplate.queryForObject("SELECT FOUND_ROWS()",
				Integer.class);
		

		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
     	obj.put("total", total);
		obj.put("pageSize", 1);
		obj.put("current", 1);
        System.out.print(obj);
		return obj;
		
		
	}
}
