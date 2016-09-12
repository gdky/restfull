package gov.gdgs.zs.dao;

import gov.gdgs.zs.entity.SdjlJG;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ZzsdDao extends BaseDao {

	public Map<String, Object> getJgZzsd(int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT y.*,z.mc AS ywzt,l.mc AS ywlx,hy.mc AS hy,cs.mc AS cs,qx.mc AS qx ");
		sb.append(" FROM (zs_ywbb y,dm_ywbb_zt z,dm_ywlx l,  ");
		
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_ywbb", "id"));
		sb.append("    ORDER BY id ");
		sb.append("    LIMIT ? , ?) sub) ");
		// ===> 插入查询条件集合结束
		sb.append(" left join dm_hy AS hy ");
		sb.append(" on y.hy_id = hy.id ");
		sb.append(" left join dm_cs AS cs ");
		sb.append(" on y.cs_dm = cs.id ");
		sb.append(" left join dm_cs AS qx ");
		sb.append(" on y.qx_dm = qx.id ");
		
		sb.append(" WHERE y.zt = z.id  ");
		sb.append(" AND y.ywlx_dm = l.id  ");
		sb.append(" AND sub.id = y.id ");
		sb.append(" ORDER BY y.bbrq desc ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<SdjlJG> ls = jdbcTemplate.query(
				sb.toString(),params.toArray(),	new BeanPropertyRowMapper<SdjlJG>(SdjlJG.class));

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("id", "zs_sdjl_jg");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public void addJgZzsd(List<Object[]>batchArgs) {
		StringBuffer sb =  new StringBuffer();
		sb.append(" insert into zs_sdjl_jg (jg_id,sdyy,sdr_id,sdr_role,sdtime,yxbz) ");
		sb.append(" values( ) ");
		this.jdbcTemplate.batchUpdate(sb.toString(), batchArgs);
		
	}

}
