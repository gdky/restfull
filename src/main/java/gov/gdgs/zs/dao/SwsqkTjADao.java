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
import org.springframework.util.StringUtils;



@Repository
public class SwsqkTjADao extends BaseDao{

	public Map<String, Object> getSwsqkTjAs(int page, int pageSize,
			HashMap<String, Object> map) {
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		StringBuffer sql=new StringBuffer(" select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key', ");
		sql.append("        qk.ID as id,");
		sql.append("        jg.DWMC as jgmc, ");
		sql.append("        jg.JGXZ_DM as xz, ");
		sql.append("        qk.FRDBXM as frdb, ");
		sql.append("        qk.CZRS as czrs, ");
		sql.append("        qk.HHRS as fqrs, ");
		sql.append("        qk.RYZS as zrs, ");
		sql.append("        qk.ZYZCSWSRS as swsrs, ");
		sql.append("        qk.RYZS - qk.ZYZCSWSRS as cyrs, ");
		sql.append("        qk.ZCZJ as zczj, ");
		sql.append("        qk.YYSR as yyzj, ");
		sql.append("        qk.ZCZE as zcze, ");
		sql.append("        qk.SRZE as srze, ");
		sql.append("        qk.LRZE as lrze, ");
		sql.append("        cs.MC as cs");
		sql.append("   from zs_sdsb_swsjbqk qk, ");
		sql.append("        zs_jg jg ");
		sql.append("   left join dm_cs cs ");
		sql.append("     on jg.CS_DM = cs.ID, ");
		sql.append("   (select @rownum:=?) jg_xh ");
		sql.append(""+condition.getSql());
		sql.append("    and jg.ID = qk.JG_ID");
		sql.append(" order by jg.CS_DM,jg.ID ");
		sql.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(page-1)*pageSize);
		params.add((page-1)*pageSize);
		params.add(pageSize);
		List<Map<String,Object>> ls=this.jdbcTemplate.query(sql.toString(),params.toArray(),new RowMapper<Map<String,Object>>() {

			@Override
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException {
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encodeHex(rs.getString("id"));
				link.put("herf_xxzl", url+"/swsqktjA/swsxxzl/"+id);
				link.put("herf_bgjl", url+"/swsqktjA/swsbgjl/"+id);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("key", rs.getObject("key"));
				map.put("xh", rs.getObject("key"));
				map.put("_links", link);
				map.put("jgmc", rs.getObject("jgmc"));
				map.put("xz", rs.getObject("xz"));
				map.put("frdb", rs.getObject("frdb"));
				map.put("czrs", rs.getObject("czrs"));
				map.put("fqrs", rs.getObject("fqrs"));
				map.put("zrs", rs.getObject("zrs"));
				map.put("swsrs", rs.getObject("swsrs"));
				map.put("cyrs", rs.getObject("cyrs"));
				map.put("zczj", rs.getObject("zczj"));
				map.put("yyzj", rs.getObject("yyzj"));
				map.put("zcze", rs.getObject("zcze"));
				map.put("srze", rs.getObject("srze"));
				map.put("lrze", rs.getObject("lrze"));
				map.put("cs", rs.getObject("cs"));
				return map;
			}
		});
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", page);
		meta.put("pageSize", pageSize);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + pageSize - 1) / pageSize);
		ob.put("page", meta);
		
		return ob;
	}

	public Map<String, Object> getSwsXxzl(String id) {
		Map<String, Object> rs=null;
		StringBuffer sqlJgID=new StringBuffer(" SELECT t.JG_ID ");
		sqlJgID.append(" FROM zs_sdsb_swsjbqk t ");
		sqlJgID.append(" WHERE t.ID=? ");
		List<String> jgIds=this.jdbcTemplate.queryForList(sqlJgID.toString(),new Object[]{id},String.class);
		String jgId="";
		if(jgIds.size()>0){
			jgId=jgIds.get(0);
			if(jgId!=null&&!StringUtils.isEmpty(jgId)){
				
			}
		}
		return rs;
	}

	public List<Map<String,Object>> getSwsBgjl(String id) {
		List<Map<String,Object>> rs=null;
		StringBuffer sqlJgID=new StringBuffer(" SELECT t.JG_ID ");
		sqlJgID.append(" FROM zs_sdsb_swsjbqk t ");
		sqlJgID.append(" WHERE t.ID=? ");
		List<String> jgIds=this.jdbcTemplate.queryForList(sqlJgID.toString(),new Object[]{id},String.class);
		String jgId="";
		if(jgIds.size()>0){
			jgId=jgIds.get(0);
			if(jgId!=null&&!StringUtils.isEmpty(jgId)){
				StringBuffer sql=new StringBuffer(" SELECT bg.ID,bg.MC as bgmc,bg.JZHI as jzhi,bg.XZHI as xzhi,DATE_FORMAT(bg.GXSJ,'%Y-%m-%d') as gxsj ");
				sql.append(" FROM zs_jglsbgxxb bg ");
				sql.append(" WHERE bg.JGB_ID=? ");
				rs=this.jdbcTemplate.queryForList(sql.toString(),new Object[]{jgId});
			}
		}
		return rs;
	}

}
