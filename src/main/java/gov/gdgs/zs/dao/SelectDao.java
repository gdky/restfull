package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class SelectDao extends BaseJdbcDao{

	public Map<String, Object> getJgSelect(HashMap<String, Object> map) {
		Condition condition = new Condition();
		StringBuffer sql=new StringBuffer("SELECT jg.ID  id, jg.DWMC mc FROM zs_jg jg where jg.yxbz=1");
		sql.append(" ORDER BY jg.ID");
		ArrayList<Object> params = condition.getParams();
		List<Map<String,Object>> ls=this.jdbcTemplate.query(sql.toString(),params.toArray(),new RowMapper<Map<String,Object>>() {

			@Override
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", rs.getString("id"));
				map.put("mc", rs.getString("mc"));
				return map;
			}
		});
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob;
	}

    public List<Map<String,Object>> getYwlxSelector(String isqy) {
		String sql = "select id,mc from dm_ywlx where yxbz =1 ";
		if(isqy != null && isqy.equals("Y")){
			sql += " and isqy = 1 ";
		}
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql);
		return ls;
    }
}
