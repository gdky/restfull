package gov.gdgs.zs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SWSZTTJDao extends BaseDao {
	
	
	public Map<String, Object> swszttjb(int year){
	StringBuffer sb=new StringBuffer();
	sb.append(" select ? as nd, 0, ");
	sb.append("    (a.zy_zrs+b.fzy_zrs) ba_zrs,  ");
	sb.append("    a.zy_zrs, ");
	sb.append("    b.fzy_zrs,");
	sb.append("    c.fzz_zrs ,");
	sb.append("    d.zzf_zrs,  ");
	sb.append("    e.swzx ,");
	sb.append("    e.wgzx, ");
	sb.append("    e.lzzx, ");
	sb.append("    e.njzx, ");
	sb.append("    e.qtzx, ");
	sb.append("    zx_zrs ");
	sb.append("   from (select count(ZYSWS_ID) zy_zrs ");
	sb.append("           from zs_zyswsbasp zyba ");
	sb.append("          where zyba.spzt_dm = '2' ");
	sb.append("          ) a, ");
	sb.append("        (select count(FZYSWS_ID) fzy_zrs ");
	sb.append("           from zs_fzybasp zyba ");
	sb.append("          where zyba.spzt_dm = '2') ");
	sb.append("            b, ");
	sb.append("        (select count(FZY_ID) fzz_zrs ");
	sb.append("           from zs_fzyzzy fzz ");
	sb.append("          where ryspzt = '2' ");
	sb.append("            and year(BDRQ) = ?) c, ");
	sb.append("        (select count(ZYSWS_ID) zzf_zrs ");
	sb.append("           from zs_zyswszfzy zzf ");
	sb.append("          where spzt_dm = '2' ");
	sb.append("            and year(SGLZXYJRQ) = ?) d, ");
	sb.append("        (select count(ZYSWS_ID) zx_zrs, ");
	sb.append("                sum(case ");
	sb.append("                      when ZYSWSZXYY_DM = '4' then ");
	sb.append("                       1 ");
	sb.append("                      else ");
	sb.append("                       0 ");
	sb.append("                    end) swzx,  ");
	sb.append("                sum(case ");
	sb.append("                      when ZYSWSZXYY_DM = '1' then ");
	sb.append("                       1 ");
	sb.append("                      else ");
	sb.append("                       0 ");
	sb.append("                    end) wgzx, ");
	sb.append("                sum(case ");
	sb.append("                      when ZYSWSZXYY_DM = '3' then ");
	sb.append("                       1 ");
	sb.append("                      else ");
	sb.append("                       0 ");
	sb.append("                    end) lzzx,  ");
	sb.append("                sum(case ");
	sb.append("                      when ZYSWSZXYY_DM = '2' then ");
	sb.append("                       1 ");
	sb.append("                      else ");
	sb.append("                       0 ");
	sb.append("                    end) njzx, ");
	sb.append("                sum(case ");
	sb.append("                      when ZYSWSZXYY_DM = '5' then ");
	sb.append("                       1 ");
	sb.append("                      else ");
	sb.append("                       0 ");
	sb.append("                    end) qtzx  ");
	sb.append("           from zs_zyswszx ");
	sb.append("          where spzt_dm = '2' ");
	sb.append("            and year(zxrq) = ?) e ");
	sb.append("  ");
	
	List<Map<String, Object>> ls=jdbcTemplate.queryForList(sb.toString(),new Object[]{year,year,year,year});
	Map<String, Object> obj = new HashMap<String, Object>();
	obj.put("data", ls);
	obj.put("total", 1);
	obj.put("pageSize", 1);
	obj.put("current", 1);

	return obj;
	
	}

	
}
