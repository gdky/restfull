package gov.gdgs.zs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class SettingDao extends BaseDao{

	public List<Map<String, Object>> getYwSetting() {
		String sql = "select * from zs_setting ";
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql);
		return ls;
	}

	public void updateSetting(String id, Map<String, Object> map) {
		String sql = "update zs_setting set value = ? where id = ?";
		Object value = (Object) map.get("value");
		this.jdbcTemplate.update(sql, new Object[]{value,id});		
	}

}
