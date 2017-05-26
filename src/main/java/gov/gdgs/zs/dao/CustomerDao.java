package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;
import com.gdky.restfull.dao.AuthDao.UserRowMapper;

@Repository
public class CustomerDao extends BaseJdbcDao{

	public Map<String, Object> getCustomers(int page, int pageSize,
			Long jid, HashMap<String, Object> where) {
		Condition condition = new Condition();
		condition.add("c.jg_id",Condition.EQUAL,jid);
		condition.add("c.lxr",Condition.FUZZY,where.get("lxr"));
		condition.add("c.nsrsbh", Condition.FUZZY, where.get("nsrsbh"));
		condition.add("c.nsrsbhdf", Condition.FUZZY, where.get("nsrsbhdf"));
		condition.add("c.dwmc", Condition.FUZZY, where.get("dwmc"));

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  ");
		sb.append("     @rownum:=@rownum + 1 AS 'key', v.* ");
		sb.append(" FROM ");
		sb.append("     (SELECT  ");
		sb.append("         t.* ");
		sb.append("     FROM ");
		sb.append("         zs_customer t, ");
		sb.append(" ( ");
		sb.append(condition.getSelectSql("zs_customer c", "c.id"));
		sb.append("     ORDER BY c.ADDDATE DESC ");
		sb.append("     LIMIT ? , ?) sub ");
		sb.append("     WHERE ");
		sb.append("         sub.id = t.id) v, ");
		sb.append("     (SELECT @rownum:=?) tmp ");

		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pageSize);
		params.add(startIndex);
		

		// 获取符合条件的记录
		List<Map<String,Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());
		String countSql = condition.getCountSql("c.id",
				"zs_customer c");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);

		return obj;
		
	}

	public void addCustomer(Map<String, Object> obj) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into zs_customer ");
		sb.append(" (id,jg_id,dwmc,dwdz,lxr,lxdh,nsrsbh,nsrsbhdf,adddate) ");
		sb.append(" values (:ID,:JG_ID,:DWMC,:DWDZ,:LXR,:LXDH,:NSRSBH,:NSRSBHDF,:ADDDATE)");
		
		this.namedParameterJdbcTemplate.update(sb.toString(), obj);
		
	}

	public void updateCustomer(String id, Map<String, Object> obj) {
		obj.put("ID", id);
		StringBuffer sb = new StringBuffer();
		sb.append(" update zs_customer set dwmc=:DWMC,dwdz=:DWDZ,LXR=:LXR,NSRSBH=:NSRSBH, ");
		sb.append(" NSRSBHDF=:NSRSBHDF ");
		sb.append(" where id=:ID");
		this.namedParameterJdbcTemplate.update(sb.toString(), obj);
		
		
	}

	public void delCustomer(String id) {
		String sql = "delete from zs_customer where id = ?";
		this.jdbcTemplate.update(sql, new Object[] { id });		
	}

	public Map<String, Object> searchCustomers(int page, int pageSize,
			Long jid, HashMap<String, Object> where) {
		
		
		String keyword = (String) where.get("keyword");

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum + 1 AS 'key', c.* ");
		sb.append(" FROM zs_customer c,(SELECT @rownum:=?) tmp ");
		sb.append(" WHERE c.jg_id = ? and (c.DWMC like ? or c.NSRSBH like ?) ");
		sb.append(" ORDER BY c.ADDDATE DESC ");
		sb.append(" LIMIT ?, ? ");
		// 装嵌传值数组
		ArrayList<Object> params = new ArrayList<Object>();
		int startIndex = pageSize * (page - 1);
		params.add(startIndex);
		params.add(jid);
		params.add("%"+keyword+"%");
		params.add("%"+keyword+"%");
		params.add(startIndex);
		params.add(pageSize);

		// 获取符合条件的记录
		List<Map<String,Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		int total = jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);

		return obj;
		
	}

	public Map<String, Object> getNsrsbhAndJgid(String id) {
		String sql = "select jg_id as jgid ,nsrsbh,nsrsbhdf from zs_customer where id = ?";
		Map<String,Object> rs = this.jdbcTemplate.queryForMap(sql, id);
		return rs;
	}

	public List<Map<String, Object>> getCustomerInYwbb(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select c.id  ");
		sb.append(" from zs_customer c, zs_ywbb y  ");
		sb.append(" where c.ID = y.CUSTOMER_ID  ");
		sb.append(" and c.ID = ? ");
		sb.append(" and (y.ZT != 0 and y.zt != 5 and y.zt != 4) ");
		
		return this.jdbcTemplate.queryForList(sb.toString(), id);
	}

}
