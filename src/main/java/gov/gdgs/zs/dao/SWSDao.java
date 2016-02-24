package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Pager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SWSDao {
	@Resource(name ="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String,Object>> testJDBC (){
		String sql = "select * from zs_jg";
		return this.jdbcTemplate.queryForList(sql);
		
	}
	/**
	 * 
	 * @param pn
	 * @param ps
	 * @return 事务所分页查询
	 */
	public Map<String,Object> swscx(int pn,int ps){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("    a.ID,");
		sb.append("   a.dwmc,");
		sb.append("   a.ZCZJ,");
		sb.append("   a.fddbr,");
		sb.append("   a.jgzch,");
		sb.append("   b.mc,");
		sb.append("   c.mc,");
		sb.append("    d.zrs,");
		sb.append("   d.zyrs,");
		sb.append("    date_format(a.swszsclsj,'%Y-%m-%d') as clsj");
		sb.append("		FROM");
		sb.append("    zs_jg a,");
		sb.append("    dm_jgxz b,");
		sb.append("    dm_cs c,");
		sb.append("   v_zsjgry d");
		sb.append("		WHERE");
		sb.append("   a.JGXZ_DM = b.ID");
		sb.append("  AND a.CS_DM = c.ID");
		sb.append("	AND a.ID = d.ZSJG_ID");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		Pager<Map<String, Object>> pager = Pager.create(ls, ps);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", pager.getPagedList(pn));
		ob.put("pagesize", ls.size());
		return ob;
	}

}
