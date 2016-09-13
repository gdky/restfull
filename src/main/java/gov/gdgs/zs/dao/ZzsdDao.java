package gov.gdgs.zs.dao;

import gov.gdgs.zs.entity.SdjlJG;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.utils.HashIdUtil;

@Repository
public class ZzsdDao extends BaseDao {

	public Map<String, Object> getJgZzsd(int page, int pagesize,
			Condition condition) {
		condition.add(" AND jl.jg_id = j.id ");
		StringBuffer sb = new StringBuffer();
		sb.append(" select @rownum:=@rownum + 1 AS 'rownum', v.*, j.dwmc ");
		sb.append(" from zs_sdjl_jg v, (select @rownum:=?) tmp, zs_jg j, ");
		
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_sdjl_jg as jl, zs_jg as j", "jl.id"));
		sb.append("    ORDER BY j.id ");
		sb.append("    LIMIT ? , ?) sub ");
		// ===> 插入查询条件集合结束
		
		sb.append(" WHERE v.id = sub.id  ");
		sb.append(" AND v.jg_id = j.id  ");
		sb.append(" ORDER BY v.sdtime desc ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String,Object>> ls = jdbcTemplate.query(sb.toString(),params.toArray(),new jgsdjl());

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("jl.id", "zs_sdjl_jg as jl, zs_jg as j");
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
		sb.append(" insert into zs_sdjl_jg (jg_id,sdyy,sdr,sdr_role,sdtime,yxbz) ");
		sb.append(" values(?,?,?,?,?,?) ");
		this.jdbcTemplate.batchUpdate(sb.toString(), batchArgs);
		
	}
	
	public class jgsdjl implements RowMapper<Map<String, Object>> {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int arg1)
				throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rownum", rs.getObject("rownum"));
			map.put("id", rs.getObject("id"));
			map.put("swsmc", rs.getObject("dwmc"));
			map.put("sdyy", rs.getObject("sdyy"));
			map.put("sdr", rs.getObject("sdr"));
			map.put("sdr_role", rs.getObject("sdr_role"));
			map.put("sdtime", rs.getDate("sdtime"));
			map.put("jsr", rs.getObject("jsr"));
			map.put("jsr_role", rs.getObject("jsr_role"));
			map.put("jstime", rs.getDate("jstime"));
			map.put("yxbz", rs.getBoolean("yxbz"));
	
			return map;
		}
	}

}
