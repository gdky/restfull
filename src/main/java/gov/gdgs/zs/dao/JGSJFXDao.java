/*
 * 机构数据分析
 */
package gov.gdgs.zs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class JGSJFXDao extends BaseDao {
	
	/*
	 * 行业学历数据分析
	 */
	public Map<String, Object> getHyxlsjfxb(int nd){
		StringBuffer sb = new StringBuffer();
		sb.append(" select ? as nd, ");
		sb.append("  cs.ID, ");
		sb.append("        cs.PARENT_ID, ");
		sb.append("        cs.mc, ");
		sb.append("        ifnull((select count(id) from zs_ryjbxx where yxbz = '1'),0) zrs,  ");
		sb.append("        ifnull((select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and xl_dm in ('4', '5')),0) yjs_zrs,  ");
		sb.append("       ifnull( (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and xl_dm = '1') ,0)bk_zrs,  ");
		sb.append("       ifnull( (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and xl_dm = '2'),0) dz_zrs,  ");
		sb.append("       ifnull( (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and xl_dm in ('6', '3')),0) gz_zrs,  ");
		sb.append("         ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1'),0) zy_zrs,  ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm in ('4', '5')),0) zy_yjs_zrs,  ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm = '1'),0) zy_bk_zrs, ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm = '2'),0) zy_dz_zrs, ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm in ('6', '3')),0) zy_gz_zrs,  ");
		sb.append("         ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1'),0) fzy_zrs, ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm in ('4', '5')) ,0)fzy_yjs_zrs,  ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm = '1'),0) fzy_bk_zrs,");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm = '2'),0) fzy_dz_zrs, ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm in ('6', '3')) ,0)fzy_gz_zrs,");
		sb.append("         ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1') ,0)cy_zrs,");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm in ('4', '5')),0) cy_yjs_zrs,  ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm = '1'),0) cy_bk_zrs, ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm = '2'),0) cy_dz_zrs,    ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xl_dm in ('6', '3')),0) cy_gz_zrs      ");
		sb.append("   from dm_cs cs ");
		sb.append("  where cs.parent_id is null ");
		sb.append(" union all ");
		sb.append("  ");
		sb.append(" select ? as nd, ");
		sb.append("  cs.ID, ");
		
		sb.append("        cs.PARENT_ID, ");
		sb.append("        cs.mc, ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0'  is not null then ");
		sb.append("            1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null then ");
		sb.append("           1 ");
		sb.append("          else 0 ");
		sb.append("        end),0) zrs, ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm in ('4', '5') then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               xl_dm in ('4', '5') then ");
		sb.append("            1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '1' then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '1' then ");
		sb.append("           1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '2' then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '2' then ");
		sb.append("           1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and xl_dm in ('6', '3') then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               xl_dm in ('6', '3') then ");
		sb.append("           1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("         ");
		
		sb.append("       ifnull(sum( case ");
		sb.append("          when parent_id = '0' and id_2 is not null then ");
		sb.append("          1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("        end),0) zrs, ");
		sb.append("       ifnull(sum( case ");
		sb.append("          when parent_id = '0' and id_2 is not null and xl_dm in ('4', '5') then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               xl_dm in ('4', '5') and id_2 is not null then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull(sum( case ");
		sb.append("          when parent_id = '0' and xl_dm = '1' and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '1' and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("         else 0 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '2' and id_2 is not null then ");
		sb.append("          1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '2' and id_2 is not null then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and xl_dm in ('6', '3') and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null  and id_2 is not null and ");
		sb.append("               xl_dm in ('6', '3') then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("         ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_3 is not null then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0) zrs, ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and xl_dm in ('4', '5') and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_3 is not null and ");
		sb.append("               xl_dm in ('4', '5') then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '1' and id_3 is not null then ");
		sb.append("          1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '1' and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '2' and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '2'  and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm in ('6', '3') and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_3 is not null and ");
		sb.append("               xl_dm in ('6', '3') then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("         ");
		sb.append("        ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and  id_4 is not null then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end) ,0)zrs, ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm in ('4', '5') and  id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_4 is not null and ");
		sb.append("               xl_dm in ('4', '5') then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '1'and  id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and  id_4 is not null and xl_dm = '1' then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("        ifnull(sum(case ");
		sb.append("          when parent_id = '0' and xl_dm = '2' and  id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and xl_dm = '2' and  id_4 is not null then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull(sum( case ");
		sb.append("          when parent_id = '0' and xl_dm in ('6', '3') and  id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and  id_4 is not null and ");
		sb.append("               xl_dm in ('6', '3') then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0) ");
		sb.append("  ");
		sb.append("   from (select jb.id    id_1, ");
		sb.append("                zy.id    id_2, ");
		sb.append("                fz.id    id_3, ");
		sb.append("                cy.id    id_4, ");
		sb.append("                jb.xl_dm, ");
		sb.append("                jb.cs_dm ");
		sb.append("           from zs_ryjbxx jb ");
		sb.append("           left join zs_zysws zy ");
		sb.append("             on jb.id = zy.ry_id ");
		sb.append("           left join zs_fzysws fz ");
		sb.append("             on jb.id = fz.ry_id ");
		sb.append("           left join zs_cyry cy ");
		sb.append("             on jb.id = cy.ry_id ");
		sb.append("          where jb.yxbz = '1') jg ");
		sb.append("  right join dm_cs cs ");
		sb.append("     on jg.CS_DM = cs.id ");
		sb.append("  where cs.parent_id is not null ");
		sb.append("  group by cs.ID, cs.PARENT_ID, cs.mc; ");
		sb.append("  ");
	
	List<Map<String, Object>> ls=jdbcTemplate.queryForList(sb.toString(),new Object[]{nd,nd});
	Map<String, Object> obj = new HashMap<String, Object>();
	obj.put("data", ls);
	obj.put("total", 1);
	//obj.put("pageSize", 1);
	//obj.put("current", 1);

	return obj;
	
	}

	
}

