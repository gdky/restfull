/**
 * dao是用来连接数据库的；
 */

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
public class SWSTJDao extends BaseDao {

	/*public List<Map<String,Object>> swstj(int year){
	StringBuffer sb=new StringBuffer();
    sb.append(" select count(distinct jg.id) zjgs, ");
	sb.append("        sum(case ");
	sb.append("              when jg.jgxz_dm = '1' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) hh_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when jg.jgxz_dm = '2' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) yx_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when year(jg.sbclsj) = ? then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) xz_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when year(jg.sbclsj) = ? and jg.jgxz_dm = '1' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) xz_hh_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when year(jg.sbclsj) = ? and jg.jgxz_dm = '2' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) xz_yx_jgs,  ");
	sb.append("         ");
	sb.append("        count(distinct bg.jg_id) bg_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when bg.mc = '性质' and bg.jzhi = '有限公司' and bg.xzhi = '合伙事务所' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) bg_yxhh_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when bg.mc = '性质' and bg.xzhi = '有限公司' and bg.jzhi = '合伙事务所' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) bg_yxhh_jgs,  ");
	sb.append("        sum(case ");
	sb.append("              when bg.mc like '%法人%' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) bg_fr_jgs,  ");
	sb.append("             ");
	sb.append("        sum(case ");
	sb.append("              when bg.mc like '%单位名称%' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) bg_sm_jgs,  ");
	sb.append("             ");
	sb.append("         sum(case ");
	sb.append("              when bg.mc like '%注册资金%' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) bg_zczj_jgs,  ");
	sb.append("             ");
	sb.append("             sum(case ");
	sb.append("              when bg.mc like '%办公地址%' then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) bg_bgdz_jgs,  ");
	sb.append("             ");
	sb.append("         count(distinct zx.id) zx_jgs , ");
	sb.append("          ");
	sb.append("           sum(case ");
	sb.append("              when jg.jgxz_dm = '1' and zx.id is not null  then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) zx_hh_jgs,  ");
	sb.append("         ");
	sb.append("        sum(case ");
	sb.append("              when jg.jgxz_dm = '2' and zx.id is not null then ");
	sb.append("               1 ");
	sb.append("              else ");
	sb.append("               0 ");
	sb.append("            end) zx_yx_jgs,  ");
	sb.append("             ");
	sb.append("        count(distinct hb.id) hb_jgs  ");
	sb.append("   from zs_jg jg ");
	sb.append("   left join (select x.id, b.jg_id, x.mc, x.jzhi, x.xzhi ");
	sb.append("                From zs_jgbgspb b, zs_jgbgxxb x ");
	sb.append("               where x.JGBGSPB_ID = b.id ");
	sb.append("                 and spzt_dm = '8' ");
	sb.append("                 and year(bgrq = ?)) bg ");
	sb.append("     on jg.id = bg.jg_id ");
	sb.append("   left join (select * ");
	sb.append("                from zs_jgzx ");
	sb.append("               where spzt = '2' ");
	sb.append("                 and year(zxrq) = ?) zx ");
	sb.append("     on jg.id = zx.jg_id ");
	sb.append("   left join (select * ");
	sb.append("                from zs_jghb hb ");
	sb.append("               where hbzt = '6' ");
	sb.append("                 and year(hbsj) = ?) hb ");
	sb.append("     on jg.id = hb.jg_id ");
	sb.append("  where jg.yxbz = '1' ");
	sb.append("   ");
		
  return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{year,year,year,year,year,year});
		
	
	}*/
	
	
	public Map<String, Object> swstj(int year){
		
	
		StringBuffer sb=new StringBuffer();
	    sb.append(" select count(distinct jg.id) zjgs, ");
		sb.append("        sum(case ");
		sb.append("              when jg.jgxz_dm = '1' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) hh_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when jg.jgxz_dm = '2' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) yx_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when year(jg.sbclsj) = ? then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) xz_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when year(jg.sbclsj) = ? and jg.jgxz_dm = '1' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) xz_hh_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when year(jg.sbclsj) = ? and jg.jgxz_dm = '2' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) xz_yx_jgs,  ");
		sb.append("         ");
		sb.append("        count(distinct bg.jg_id) bg_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when bg.mc = '性质' and bg.jzhi = '有限公司' and bg.xzhi = '合伙事务所' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) bg_yxhh_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when bg.mc = '性质' and bg.xzhi = '有限公司' and bg.jzhi = '合伙事务所' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) bg_yxhh_jgs,  ");
		sb.append("        sum(case ");
		sb.append("              when bg.mc like '%法人%' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) bg_fr_jgs,  ");
		sb.append("             ");
		sb.append("        sum(case ");
		sb.append("              when bg.mc like '%单位名称%' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) bg_sm_jgs,  ");
		sb.append("             ");
		sb.append("         sum(case ");
		sb.append("              when bg.mc like '%注册资金%' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) bg_zczj_jgs,  ");
		sb.append("             ");
		sb.append("             sum(case ");
		sb.append("              when bg.mc like '%办公地址%' then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) bg_bgdz_jgs,  ");
		sb.append("             ");
		sb.append("         count(distinct zx.id) zx_jgs , ");
		sb.append("          ");
		sb.append("           sum(case ");
		sb.append("              when jg.jgxz_dm = '1' and zx.id is not null  then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) zx_hh_jgs,  ");
		sb.append("         ");
		sb.append("        sum(case ");
		sb.append("              when jg.jgxz_dm = '2' and zx.id is not null then ");
		sb.append("               1 ");
		sb.append("              else ");
		sb.append("               0 ");
		sb.append("            end) zx_yx_jgs,  ");
		sb.append("             ");
		sb.append("        count(distinct hb.id) hb_jgs  ");
		sb.append("   from zs_jg jg ");
		sb.append("   left join (select x.id, b.jg_id, x.mc, x.jzhi, x.xzhi ");
		sb.append("                From zs_jgbgspb b, zs_jgbgxxb x ");
		sb.append("               where x.JGBGSPB_ID = b.id ");
		sb.append("                 and spzt_dm = '8' ");
		sb.append("                 and year(bgrq = ?)) bg ");
		sb.append("     on jg.id = bg.jg_id ");
		sb.append("   left join (select * ");
		sb.append("                from zs_jgzx ");
		sb.append("               where spzt = '2' ");
		sb.append("                 and year(zxrq) = ?) zx ");
		sb.append("     on jg.id = zx.jg_id ");
		sb.append("   left join (select * ");
		sb.append("                from zs_jghb hb ");
		sb.append("               where hbzt = '6' ");
		sb.append("                 and year(hbsj) = ?) hb ");
		sb.append("     on jg.id = hb.jg_id ");
		sb.append("  where jg.yxbz = '1' ");
		sb.append("   ");
	
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				new Object[]{year,year,year,year,year,year});
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", 1);
		obj.put("pageSize", 1);
		obj.put("current", 1);

		return obj;
		
	}	
}
