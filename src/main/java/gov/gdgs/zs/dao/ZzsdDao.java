package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class ZzsdDao extends BaseDao {

	public Map<String, Object> getJgZzsd(int page, int pagesize,
			Condition condition) {
		condition.add(" AND jl.jg_id = j.id ");
		condition.add(" AND jl.yxbz = 1 ");
		StringBuffer sb = new StringBuffer();
		sb.append(" select @rownum:=@rownum + 1 AS 'rownum', t.* from ( ");
		sb.append(" select v.*, j.dwmc ");
		sb.append(" from zs_sdjl_jg v, zs_jg j, ");
		
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_sdjl_jg as jl, zs_jg as j", "jl.id"));
		sb.append("    ORDER BY j.id ");
		sb.append("    LIMIT ? , ?) sub ");
		// ===> 插入查询条件集合结束
		
		sb.append(" WHERE v.id = sub.id  ");
		sb.append(" AND v.jg_id = j.id  ");
		sb.append(" ORDER BY v.sdtime desc) as t, ");
		sb.append(" (SELECT @rownum:=?) tmp ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);
		params.add(startIndex);

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
		sb.append(" insert into zs_sdjl_jg (jg_id,sdyy,sdr,sdr_role,sdtime,lx,yxbz) ");
		sb.append(" values(?,?,?,?,?,?,?) ");
		this.jdbcTemplate.batchUpdate(sb.toString(), batchArgs);
		
	}
	


	public List<Integer> getSdJGByLx(Integer lx) {
		String sql = "select jg_id as jgId from zs_sdjl_jg where yxbz = 1 and lx = ? ";
		List<Integer>  ls  = this.jdbcTemplate.query(sql, new Object[]{lx},new sdJGIDRowMapper());
		return ls;
	}
	public List<Integer> getSdsws() {
		String sql = "select zysws_id as swsid from zs_sdjl_zysws where yxbz = 1 ";
		List<Integer>  ls  = this.jdbcTemplate.query(sql,new sdSWSIDRowMapper());
		return ls;
	}
	
	public void unlockJgZzsd(List<Object[]> batchArgs) {
		String sql = "update zs_sdjl_jg set jsr = ?, jsr_role = ?, jstime = ?,yxbz = ? where id = ? ";
		this.jdbcTemplate.batchUpdate(sql, batchArgs);
	}
	
	public void lockJgZzsd(List<Object[]> batchArgs) {
		String sql = "update zs_sdjl_jg  set yxbz = ? where id = ? ";
		this.jdbcTemplate.batchUpdate(sql, batchArgs);
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
	public class swssdjl implements RowMapper<Map<String, Object>> {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int arg1)
				throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", rs.getObject("id"));
			map.put("xming", rs.getObject("xming"));
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
	
	public class sdJGIDRowMapper implements RowMapper<Integer> {

		@Override
		public Integer mapRow(ResultSet rs, int arg1)
				throws SQLException {
			return rs.getInt("jgId");

		}
	}
	public class sdSWSIDRowMapper implements RowMapper<Integer> {

		@Override
		public Integer mapRow(ResultSet rs, int arg1)
				throws SQLException {
			return rs.getInt("swsid");

		}
	}

	public Map<String, Object> getJgZzsdwx(int page, int pagesize,
			Condition condition) {
		condition.add(" AND jl.jg_id = j.id ");
		condition.add(" AND jl.yxbz = 0 ");
		StringBuffer sb = new StringBuffer();
		sb.append(" select @rownum:=@rownum + 1 AS 'rownum', t.* from ( ");
		sb.append(" select v.*, j.dwmc ");
		sb.append(" from zs_sdjl_jg v, zs_jg j, ");
		
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_sdjl_jg as jl, zs_jg as j", "jl.id"));
		sb.append("    ORDER BY j.id ");
		sb.append("    LIMIT ? , ?) sub ");
		// ===> 插入查询条件集合结束
		
		sb.append(" WHERE v.id = sub.id  ");
		sb.append(" AND v.jg_id = j.id  ");
		sb.append(" ORDER BY v.sdtime desc) as t, ");
		sb.append(" (SELECT @rownum:=?) tmp ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);
		params.add(startIndex);

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

	public Map<String, Object> getSWSzzzt(int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select SQL_CALC_FOUND_ROWS z.id,r.xming,z.zyzgzsbh, j.dwmc as swsmc ,!isnull(s.ID) as islock ");
		sb.append(" from (zs_zysws z, zs_ryjbxx r , zs_jg j) ");
		sb.append(" left join (select id,zysws_id from zs_sdjl_zysws where yxbz = 1) as s ");
		sb.append(" on z.id = s.ZYSWS_ID ");
		sb.append(condition.getSql());
		sb.append(" and z.RY_ID = r.ID ");
		sb.append(" and z.JG_ID = j.ID ");
		sb.append(" and z.YXBZ =1  ");
		sb.append(" and z.ZYZT_DM = 1 ");
		sb.append(" order by islock desc, id desc ");
		sb.append(" limit ?,? ");
		
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);
		
		List<Map<String,Object>> ls = jdbcTemplate.queryForList(sb.toString(),params.toArray());
		String sql = "select FOUND_ROWS()";
		Integer total = jdbcTemplate.queryForObject(sql,  Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public void addSwszzsd(List<Object[]> batchArgs) {
		StringBuffer sb =  new StringBuffer();
		sb.append(" insert into zs_sdjl_zysws (zysws_id,sdyy,sdr,sdr_role,sdtime,yxbz) ");
		sb.append(" values(?,?,?,?,?,?) ");
		this.jdbcTemplate.batchUpdate(sb.toString(), batchArgs);
	}

	public Map<String, Object> getSWSsdjl(int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select SQL_CALC_FOUND_ROWS s.id,s.sdyy,s.sdr,s.sdr_role,s.sdtime,s.jsr,s.jsr_role,s.jstime,s.yxbz, ");
		sb.append(" r.XMING from zs_sdjl_zysws s,zs_zysws z,zs_ryjbxx r ");
		sb.append(condition.getSql());
		sb.append(" and s.ZYSWS_ID = z.ID ");
		sb.append(" and z.RY_ID = r.ID ");
		sb.append(" order by s.yxbz desc, s.sdtime desc ");
		sb.append(" limit ?,? ");
		
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);
		
		List<Map<String,Object>> ls = jdbcTemplate.query(sb.toString(),params.toArray(),new swssdjl());
		
		String sql = "select FOUND_ROWS()";
		Integer total = jdbcTemplate.queryForObject(sql,  Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);
		
		return obj;
	}

	public void unlockSWSZzsd(List<Object[]> batchArgs) {
		String sql = "update zs_sdjl_zysws set jsr = ?, jsr_role = ?, jstime = ?,yxbz = ? where id = ? ";
		this.jdbcTemplate.batchUpdate(sql, batchArgs);
	}



	
}
