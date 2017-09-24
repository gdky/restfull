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

@Repository
public class SDSCBBDao  extends BaseDao{
	/**
	 * 
	 * @return 事务所基本情况统计表分页查询
	 * @throws Exception 
	 */
	public Map<String,Object> swsjbqktjbcx(int pn,int ps,Map<String,Object> qury) {
		Condition condition = new Condition();
		condition.add("b.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("a.nd", Condition.EQUAL, qury.get("nd"));
		condition.add("a.cs_dm", Condition.EQUAL, qury.get("cs"));
		condition.add("a.ZTBJ", Condition.EQUAL, qury.get("bbzt"));
		condition.add("a.sbrq", Condition.GREATER_EQUAL, qury.get("sbsj"));
		condition.add("a.sbrq", Condition.LESS_EQUAL, qury.get("sbsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT 	SQL_CALC_FOUND_ROWS	 ");
		sb.append("		@rownum:=@rownum+1 AS 'key',a.id,a.yysr,a.zcze,a.srze,a.lrze,");
		sb.append("	 b.dwmc,a.nd,a.dwxz,ifnull(a.czrs,0) as czrs,a.hhrs,a.ryzs,a.zyzcswsrs,a.zczj,a.jgszd,a.wths,c.mc as cs, d.mc as jgxz, ");
		sb.append("		case a.ZTBJ when 1 then '提交' when 2 then '通过' when 0 then '保存' when "
				+ "3 then '退回' else null end as bbzt,a.frdbxm,DATE_FORMAT(a.sbrq,'%Y年%m月%d日') AS sbsj,a.tianbiaoren,a.suozhang");
		sb.append("	 FROM zs_sdsb_swsjbqk a,zs_jg b,(SELECT @rownum:=?) zs_jg,dm_cs c,dm_jgxz d");
		sb.append("		"+condition.getSql()+" ");
		sb.append("	and a.JG_ID=b.ID and a.cs_dm = c.id and d.id = a. jgxz_dm and (a.ztbj = 1 or a.ztbj = 2) ");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	/**
	 * 
	 * @return 注册税务师行业人员情况统计表分页查询
	 * @throws Exception 
	 */
	public Map<String,Object> hyryqktjcx(int pn,int ps,Map<String,Object> qury) {
		Condition condition = new Condition();
		condition.add("b.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("b.cs_dm", Condition.EQUAL, qury.get("cs"));
		condition.add("a.nd", Condition.EQUAL, qury.get("nd"));
		condition.add("a.ZTBJ", Condition.EQUAL, qury.get("bbzt"));
		condition.add("a.sbrq", Condition.GREATER_EQUAL, qury.get("sbsj"));
		condition.add("a.sbrq", Condition.LESS_EQUAL, qury.get("sbsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT 	SQL_CALC_FOUND_ROWS	 ");
		sb.append("		@rownum:=@rownum+1 AS 'key',a.id,b.dwmc,a.*,c.mc as cs,");
		sb.append("		case a.ZTBJ when 1 then '提交' when 2 then '通过' when 0 then '保存' when "
				+ "3 then '退回' else null end as bbzt,DATE_FORMAT(a.sbrq,'%Y年%m月%d日') AS sbsj");
		sb.append("	 FROM zs_sdsb_hyryqktj a,zs_jg b,(SELECT @rownum:=?) zs_jg,dm_cs c");
		sb.append("		"+condition.getSql()+" ");
		sb.append("	and a.JG_ID=b.ID and c.id = b.cs_dm and (a.ztbj = 1 or a.ztbj = 2) ");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	/**
	 * 
	 * @return 注册税务师行业经营收入情况统计表分页查询
	 * @throws Exception 
	 */
	public Map<String,Object> jysrqktjcx(int pn,int ps,Map<String,Object> qury) {
		Condition condition = new Condition();
		condition.add("b.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("b.cs_dm", Condition.EQUAL, qury.get("cs"));
		condition.add("a.nd", Condition.EQUAL, qury.get("nd"));
		condition.add("a.ZTBJ", Condition.EQUAL, qury.get("bbzt"));
		condition.add("a.sbrq", Condition.GREATER_EQUAL, qury.get("sbsj"));
		condition.add("a.sbrq", Condition.LESS_EQUAL, qury.get("sbsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT 	SQL_CALC_FOUND_ROWS	 ");
		sb.append("		@rownum:=@rownum+1 AS 'key',a.id,b.dwmc,a.*,c.mc as cs,");
		sb.append("		case a.ZTBJ when 1 then '提交' when 2 then '通过' when 0 then '保存' when "
				+ "3 then '退回' else null end as bbzt,DATE_FORMAT(a.sbrq,'%Y年%m月%d日') AS sbsj");
		sb.append("	 FROM zs_sdsb_jysrqk a,zs_jg b,(SELECT @rownum:=?) zs_jg,dm_cs c");
		sb.append("		"+condition.getSql()+" ");
		sb.append("	and a.JG_ID=b.ID  and c.id = b.cs_dm and (a.ztbj = 1 or a.ztbj = 2) ");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	/**
	 * 
	 * @return 未上交手动报表事务所分页查询
	 * @throws Exception 
	 */
	public Map<String,Object> wsbbbcx(int pn,int ps,Map<String,Object> qury) {
		List<String> arr = new ArrayList<String>();
		arr.add("zs_sdsb_swsjbqk");
		arr.add("zs_sdsb_hyryqktj");
		arr.add("zs_sdsb_jysrqk");
		arr.add("zs_sdsb_jygmtjb");
		arr.add("zs_sdsb_jzywqktjb");
		Condition condition = new Condition();
		condition.add("d.dwmc", Condition.FUZZY, qury.get("dwmc"));
		StringBuffer sb = new StringBuffer();
		sb.append("		select 	sql_calc_found_rows	 @rownum:=@rownum+1 as 'key','"+qury.get("nd")
				+"' as nd,'未上报' as sbzt,d.id,d.dwmc,d.JGZCH as zsbh,c.mc as cs, d.DHUA as dhhm,d.TXYXMING as txyxm,d.XTYPHONE as txyyddh");
		sb.append("		,(select v.id from zs_sdjl_jg v where v.jg_id=d.id and v.lx=4 and v.yxbz=1 limit 1) as issd ");
		sb.append("		FROM zs_jg d,dm_cs c,(SELECT @rownum:=?) zs_jg");
		sb.append("		 "+condition.getSql()+" ");
		sb.append("		and d.ID NOT IN (");
		sb.append("		SELECT b.jg_id");
		sb.append("		FROM "+arr.get(Integer.parseInt(qury.get("bblx").toString()))+" b");
		sb.append("		where  b.nd = ? and b.ZTBJ=2) ");
		sb.append("		AND d.YXBZ = '1'");
		sb.append("		and d.CS_DM = c.ID");
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(qury.get("nd"));
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>() {
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				String id = hashids.encode(rs.getLong("id"));
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("jgid", id);
				map.put("dwmc", rs.getObject("dwmc"));
				map.put("zsbh", rs.getObject("zsbh"));
				map.put("key", rs.getObject("key"));
				map.put("cs", rs.getObject("cs"));
				map.put("dhhm", rs.getObject("dhhm"));
				map.put("txyxm", rs.getObject("txyxm"));
				map.put("txyyddh", rs.getObject("txyyddh"));
				map.put("nd", rs.getObject("nd"));
				map.put("sbzt", rs.getObject("sbzt"));
				map.put("issd", rs.getObject("issd"));
				return map;
			}
		});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	public void rjb1(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update zs_sdsb_swsjbqk set ztbj = 0 where id = ? ");
		this.jdbcTemplate.update(sb.toString(),new Object[]{id});
		
	}
	public void rjb2(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update zs_sdsb_hyryqktj set ztbj = 0 where id = ? ");
		this.jdbcTemplate.update(sb.toString(),new Object[]{id});
		
	}
	public void rjb4(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update zs_sdsb_jysrqk set ztbj = 0 where id = ? ");
		this.jdbcTemplate.update(sb.toString(),new Object[]{id});
	}
	public void rjb5(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update zs_sdsb_jygmtjb t, ");
		sb.append(" (select a.nd,a.JG_ID from zs_sdsb_jysrqk a where a. id = ?) d ");
		sb.append(" set t.ZTBJ = 0 ");
		sb.append(" where t.JG_ID = d.jg_id and t.nd = d.nd ");
		this.jdbcTemplate.update(sb.toString(),new Object[]{id});
	}
	public void rjb6(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update zs_sdsb_jzywqktjb set ztbj = 0 where id = ? ");
		this.jdbcTemplate.update(sb.toString(),new Object[]{id});
	}
}
