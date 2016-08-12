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
public class ZYZZSJFXDao extends BaseDao{
	
	public Map<String, Object> getZyzzsjfxb(int page, int pageSize,
			HashMap<String, Object> map){
	StringBuffer sb = new StringBuffer();	
	sb.append(" select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1,t.*,date_format(now(),'%Y') as nd");
	sb.append(" from( select cs.ID, ");
	sb.append("        cs.PARENT_ID, ");
	sb.append("        cs.mc, ");
	sb.append("        case ");
	sb.append("          when parent_id is null then ");
	sb.append("           ifnull((select count(*) from zs_jg where yxbz = '1'),0) ");
	sb.append("          when parent_id = '0' then ");
	sb.append("           ifnull(count(jg.id),0) ");
	sb.append("          when parent_id <> '0' and parent_id is not null then ");
	sb.append("          ifnull( count(jg.id),0) ");
	sb.append("        end zjgs,  ");
	sb.append("         ");
	sb.append("        ifnull(sum(case ");
	sb.append("          when parent_id is null then ");
	sb.append("           (select count(*) ");
	sb.append("              from zs_jg jg ");
	sb.append("              left join zs_jgyjxxb yj ");
	sb.append("                on jg.id = yj.id ");
	sb.append("             where yxbz = '1' ");
	sb.append("               and flag = '1' ");
	sb.append("               and jgxz_dm = '2') ");
	sb.append("          when parent_id = '0' and flag = '1' and jgxz_dm = '2' then ");
	sb.append("           1 ");
	sb.append("          when parent_id <> '0' and parent_id is not null and flag = '1' and ");
	sb.append("               jgxz_dm = '2' then ");
	sb.append("          1 ");
	sb.append("          else 0 ");
	sb.append("        end),0) fhzcgd_yx, ");
	sb.append("         ");
	sb.append("       ifnull( sum(case ");
	sb.append("          when parent_id is null then ");
	sb.append("           (select count(*) ");
	sb.append("              from zs_jg jg ");
	sb.append("              left join zs_jgyjxxb yj ");
	sb.append("                on jg.id = yj.id ");
	sb.append("             where yxbz = '1' ");
	sb.append("               and flag = '1' ");
	sb.append("               and jgxz_dm = '1') ");
	sb.append("          when parent_id = '0' and flag = '1' and jgxz_dm = '1' then ");
	sb.append("           1 ");
	sb.append("          when parent_id <> '0' and parent_id is not null and flag = '1' and ");
	sb.append("               jgxz_dm = '1' then ");
	sb.append("           1 ");
	sb.append("           else 0 ");
	sb.append("        end),0) fhzcgd_hh,  ");
	sb.append("         ");
	sb.append("       ifnull( sum(case ");
	sb.append("          when parent_id is null then ");
	sb.append("           (select count(*) ");
	sb.append("              from zs_jg jg ");
	sb.append("              left join zs_jgyjxxb yj ");
	sb.append("                on jg.id = yj.id ");
	sb.append("             where yxbz = '1' ");
	sb.append("               and flag = '1' ");
	sb.append("               and jgxz_dm = '3') ");
	sb.append("          when parent_id = '0' and flag = '1' and jgxz_dm = '3' then ");
	sb.append("           1 ");
	sb.append("          when parent_id <> '0' and parent_id is not null and flag = '1' and ");
	sb.append("               jgxz_dm = '3' then ");
	sb.append("           1 else 0 ");
	sb.append("        end),0) fhzcgd_fs, ");
	sb.append("         ");
	sb.append("       ifnull(sum( case ");
	sb.append("          when parent_id is null then ");
	sb.append("           (select count(*) ");
	sb.append("              from zs_jg jg ");
	sb.append("              left join zs_jgyjxxb yj ");
	sb.append("                on jg.id = yj.id ");
	sb.append("             where yxbz = '1' ");
	sb.append("               and flag = '0' ");
	sb.append("               and jgxz_dm = '2') ");
	sb.append("          when parent_id = '0' and flag = '0' and jgxz_dm = '2' then ");
	sb.append("          1 ");
	sb.append("          when parent_id <> '0' and parent_id is not null and flag = '0' and ");
	sb.append("               jgxz_dm = '2' then ");
	sb.append("           1 else 0 ");
	sb.append("        end),0) bfhzcgd_yx, ");
	sb.append("         ");
	sb.append("       ifnull(sum( case ");
	sb.append("          when parent_id is null then ");
	sb.append("           (select count(*) ");
	sb.append("              from zs_jg jg ");
	sb.append("              left join zs_jgyjxxb yj ");
	sb.append("                on jg.id = yj.id ");
	sb.append("             where yxbz = '1' ");
	sb.append("               and flag = '0' ");
	sb.append("               and jgxz_dm = '1') ");
	sb.append("          when parent_id = '0' and flag = '0' and jgxz_dm = '1' then ");
	sb.append("           1 ");
	sb.append("          when parent_id <> '0' and parent_id is not null and flag = '0' and ");
	sb.append("               jgxz_dm = '1' then ");
	sb.append("           1 else 0 ");
	sb.append("        end),0) bfhzcgd_hh, ");
	sb.append("         ");
	sb.append("        ifnull(sum(case ");
	sb.append("          when parent_id is null then ");
	sb.append("           (select count(*) ");
	sb.append("              from zs_jg jg ");
	sb.append("              left join zs_jgyjxxb yj ");
	sb.append("                on jg.id = yj.id ");
	sb.append("             where yxbz = '1' ");
	sb.append("               and flag = '0' ");
	sb.append("               and jgxz_dm = '3') ");
	sb.append("          when parent_id = '0' and flag = '0' and jgxz_dm = '3' then ");
	sb.append("           1 ");
	sb.append("          when parent_id <> '0' and parent_id is not null and flag = '0' and ");
	sb.append("               jgxz_dm = '3' then ");
	sb.append("           1 else 0 ");
	sb.append("        end),0) bfhzcgd_fs  ");
	sb.append("   from (select jg.ID, jg.JGXZ_DM, yj.FLAG, jg.cs_dm ");
	sb.append("           from zs_jg jg ");
	sb.append("           left join zs_jgyjxxb yj ");
	sb.append("             on jg.id = yj.id ");
	sb.append("          where jg.yxbz = '1') jg ");
	sb.append("  right join dm_cs cs ");
	sb.append("     on jg.CS_DM = cs.id ");
	sb.append("        where cs.PARENT_ID =0 ");
	sb.append("  group by cs.ID, cs.PARENT_ID, cs.mc) as t");
	sb.append("  ");
	
	
	List<Map<String, Object>> ls=jdbcTemplate.queryForList(sb.toString());
	int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
	
	Map<String, Object> obj = new HashMap<String, Object>();
	obj.put("data", ls);
	obj.put("total", total);
	return obj;
	
	}	
}

