package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class XttjbbDao extends BaseJdbcDao {

	public Map<String, Object> getXttjbb(int page, int pageSize, HashMap<String, Object> where) {
		// TODO Auto-generated method stub
		Condition condition = new Condition();
		condition.add("jg.DWMC", Condition.EQUAL, where.get("dwmc")); 
		StringBuffer sql=new StringBuffer(" SELECT SQL_CALC_FOUND_ROWS @rownum := @rownum + 1 AS 'KEY', xj.* ");
		sql.append("   FROM (SELECT jg.ID id, ");
		sql.append("                jg.DWMC DWMC, ");
		sql.append("                xz.MC DWXZ, ");
		sql.append("                jg.FDDBR FRDBXM, ");
		sql.append("                (IFNULL(cy.gdrs, 0) + IFNULL(zy.gdrs, 0) + IFNULL(f.gdrs, 0) + IFNULL(q.gdrs, 0)) CZRS, ");
		sql.append("                (IFNULL(cy.hhrs, 0) + IFNULL(zy.hhrs, 0) + IFNULL(f.hhrs, 0) + IFNULL(q.hhrs, 0)) HHRS, ");
		sql.append("                (IFNULL(cy.rs, 0) + IFNULL(zy.rs, 0) + IFNULL(f.rs, 0) + IFNULL(q.rs, 0)) RYZS, ");
		sql.append("                IFNULL(zy.rs, 0) ZYZCSWSRS, ");
		sql.append("                IFNULL(cy.rs, 0) CYRS, ");
		sql.append("                IFNULL(jg.ZCZJ, 0) ZCZJ, ");
		sql.append("                0 YYSR, ");
		sql.append("                NULL ZCZE, ");
		sql.append("                NULL SRZE, ");
		sql.append("                NULL LRZE, ");
		sql.append("                cs.MC JGSZD ");
		sql.append("           FROM zs_jg jg ");
		sql.append("           LEFT JOIN dm_jgxz xz ");
		sql.append("             ON jg.JGXZ_DM = xz.ID ");
		sql.append("           LEFT JOIN dm_cs cs ");
		sql.append("             ON jg.CS_DM = cs.ID ");
		sql.append("           LEFT JOIN (SELECT cy.JG_ID, ");
		sql.append("                            COUNT(cy.JG_ID) rs, ");
		sql.append("                            SUM(IF(cy.CZR_DM = '1', 1, 0)) gdrs, ");
		sql.append("                            SUM(IF(cy.FQR_DM = '1', 1, 0)) hhrs ");
		sql.append("                       FROM zs_cyry cy ");
		sql.append("                      WHERE cy.yxbz = '1' ");
		sql.append("                      GROUP BY cy.JG_ID) cy ");
		sql.append("             ON jg.ID = cy.JG_ID ");
		sql.append("           LEFT JOIN (SELECT zy.JG_ID, ");
		sql.append("                            COUNT(zy.JG_ID) rs, ");
		sql.append("                            SUM(IF(zy.CZR_DM = '1', 1, 0)) gdrs, ");
		sql.append("                            SUM(IF(zy.FQR_DM = '1', 1, 0)) hhrs ");
		sql.append("                       FROM zs_zysws zy ");
		sql.append("                      WHERE zy.yxbz = '1' ");
		sql.append("                      GROUP BY zy.JG_ID) zy ");
		sql.append("             ON jg.ID = zy.JG_ID ");
		sql.append("           LEFT JOIN (SELECT f.ZSJGID, ");
		sql.append("                            COUNT(f.ZSJGID) rs, ");
		sql.append("                            SUM(IF(f.CZR_DM = '1', 1, 0)) gdrs, ");
		sql.append("                            SUM(IF(f.FQR_DM = '1', 1, 0)) hhrs ");
		sql.append("                       FROM zs_fzysws f ");
		sql.append("                      WHERE f.yxbz = '1' ");
		sql.append("                      GROUP BY f.ZSJGID) f ");
		sql.append("             ON jg.ID = f.ZSJGID ");
		sql.append("           LEFT JOIN (SELECT q.JG_ID, ");
		sql.append("                            COUNT(q.JG_ID) rs, ");
		sql.append("                            SUM(IF(q.CZR_DM = '1', 1, 0)) gdrs, ");
		sql.append("                            SUM(IF(q.FQR_DM = '1', 1, 0)) hhrs ");
		sql.append("                       FROM zs_qtry q ");
		sql.append("                      WHERE q.qtryzt_dm = '1' ");
		sql.append("                      GROUP BY q.JG_ID) q ");
		sql.append("             ON jg.ID = q.JG_ID ");
		sql.append("           "+condition.getSql());
		sql.append("            AND jg.YXBZ = '1' ");
		sql.append("          ORDER BY jg.ID) xj, ");
		sql.append("        (SELECT @rownum := ?) xh  ");
		sql.append("        LIMIT ?,? ");
		
		ArrayList<Object> params = condition.getParams();
		params.add((page-1)*pageSize);
		params.add((page-1)*pageSize);
		params.add(pageSize);
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
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

	public Map<String, Object> getHyryqktj(int page, int pageSize,
			HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" select '1:执业税务师' as xmmc, count(distinct zy.id) zrs, ")
			.append(" sum(case when jb.xb_dm = '1' then 1 else 0 end) n_zrs, ")
			.append(" sum(case when jb.xb_dm = '2' then 1 else 0 end) v_zrs, ")
			.append(" sum(case when jb.xl_dm = '5' then 1 else 0 end) bs_zrs, ")
			.append(" sum(case when jb.xl_dm = '4' then 1 else 0 end) yjs_zrs, ")
			.append(" sum(case when jb.xl_dm = '1' then 1 else 0 end) bk_zrs, ")
			.append(" sum(case when jb.xl_dm = '2' then 1 else 0 end) dz_zrs, ")
			.append(" sum(case when jb.xl_dm = '3' then 1 else 0 end) gz_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) 35_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) 50_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) 60_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) 61_zrs ")
			.append(" from zs_zysws zy left join zs_ryjbxx jb on zy.ry_id = jb.id ")
			.append(" where zy.yxbz = '1' and jb.yxbz = '1' ")
			.append(" union all ")
			.append(" select '其中：合伙人', count(distinct zy.id) zrs, ")
			.append(" sum(case when jb.xb_dm = '1' then 1 else 0 end) n_zrs, ")
			.append(" sum(case when jb.xb_dm = '2' then 1 else 0 end) v_zrs, ")
			.append(" sum(case when jb.xl_dm = '5' then 1 else 0 end) bs_zrs, ")
			.append(" sum(case when jb.xl_dm = '4' then 1 else 0 end) yjs_zrs, ")
			.append(" sum(case when jb.xl_dm = '1' then 1 else 0 end) bk_zrs, ")
			.append(" sum(case when jb.xl_dm = '2' then 1 else 0 end) dz_zrs, ")
			.append(" sum(case when jb.xl_dm = '3' then 1 else 0 end) gz_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) 35_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) 50_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) 60_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) 61_zrs ")
			.append(" from zs_zysws zy left join zs_ryjbxx jb on zy.ry_id = jb.id ")
			.append(" where zy.yxbz = '1' and jb.yxbz = '1' and zy.fqr_dm = '1' ")
			.append(" group by zy.fqr_dm ")
			.append(" union all ")
			.append(" select '其中：出资人', count(distinct zy.id) zrs, ")
			.append(" sum(case when jb.xb_dm = '1' then 1 else 0 end) n_zrs, ")
			.append(" sum(case when jb.xb_dm = '2' then 1 else 0 end) v_zrs, ")
			.append(" sum(case when jb.xl_dm = '5' then 1 else 0 end) bs_zrs, ")
			.append(" sum(case when jb.xl_dm = '4' then 1 else 0 end) yjs_zrs, ")
			.append(" sum(case when jb.xl_dm = '1' then 1 else 0 end) bk_zrs, ")
			.append(" sum(case when jb.xl_dm = '2' then 1 else 0 end) dz_zrs, ")
			.append(" sum(case when jb.xl_dm = '3' then 1 else 0 end) gz_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) 35_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) 50_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) 60_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) 61_zrs ")
			.append(" from zs_zysws zy left join zs_ryjbxx jb on zy.ry_id = jb.id ")
			.append(" where zy.yxbz = '1' and jb.yxbz = '1' and zy.czr_dm = '1' ")
			.append(" group by zy.czr_dm ")
			.append(" union all ")
			.append(" select '2:具有其它中介执业资格的从业人员', count(distinct zy.id) zrs, ")
			.append(" ifnull(sum(case when jb.xb_dm = '1' then 1 else 0 end),0) n_zrs, ")
			.append(" ifnull( sum(case when jb.xb_dm = '2' then 1 else 0 end),0) v_zrs, ")
			.append(" ifnull( sum(case when jb.xl_dm = '5' then 1 else 0 end),0) bs_zrs, ")
			.append(" ifnull(sum(case when jb.xl_dm = '4' then 1 else 0 end),0) yjs_zrs, ")
			.append(" ifnull(sum(case when jb.xl_dm = '1' then 1 else 0 end),0) bk_zrs, ")
			.append(" ifnull( sum(case when jb.xl_dm = '2' then 1 else 0 end),0) dz_zrs, ")
			.append(" ifnull(sum(case when jb.xl_dm = '3' then 1 else 0 end),0) gz_zrs, ")
			.append(" ifnull(sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) ,0)35_zrs, ")
			.append(" ifnull( sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end),0) 50_zrs, ")
			.append(" ifnull(sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end),0) 60_zrs, ")
			.append(" ifnull(sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end),0) 61_zrs ")
			.append(" from zs_qtry zy left join zs_ryjbxx jb on zy.ry_id = jb.id  ")
			.append(" where zy.qtryzt_dm = '1' and jb.yxbz = '1' ")
			.append(" union all ")
			.append(" select '3:从业人员',count(distinct zy.id) zrs, ")
			.append(" sum(case when jb.xb_dm = '1' then 1 else 0 end) n_zrs, ")
			.append(" sum(case when jb.xb_dm = '2' then 1 else 0 end) v_zrs, ")
			.append(" sum(case when jb.xl_dm = '5' then 1 else 0 end) bs_zrs, ")
			.append(" sum(case when jb.xl_dm = '4' then 1 else 0 end) yjs_zrs, ")
			.append(" sum(case when jb.xl_dm = '1' then 1 else 0 end) bk_zrs, ")
			.append(" sum(case when jb.xl_dm = '2' then 1 else 0 end) dz_zrs, ")
			.append(" sum(case when jb.xl_dm = '3' then 1 else 0 end) gz_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) 35_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) 50_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) 60_zrs, ")
			.append(" sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) 61_zrs ")
			.append(" from zs_cyry zy left join zs_ryjbxx jb on zy.ry_id = jb.id ")
			.append(" where zy.yxbz = '1' and jb.yxbz = '1' ")
			;
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql.toString());
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("data", ls);
		return obj;
	}

	public Map<String, Object> getHynlsjfx(int page, int pageSize,
			HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select cs.ID, ");
		sb.append("        cs.PARENT_ID, ");
		sb.append("        cs.mc, ");
		sb.append("        date_format(now(),'%Y') as nd, ");
		sb.append("       ifnull( (select count(id) from zs_ryjbxx where yxbz = '1' and (year(now())-year(sri)-1) > 60),0) zrs_60,  ");
		sb.append("       ifnull(  (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60),0) zrs_50,  ");
		sb.append("       ifnull(  (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("           and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50),0) zrs_36,  ");
		sb.append("      ifnull(   (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("           and  (year(now())-year(sri)-1) <=35),0) zrs_35,  ");
		sb.append("     ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) > 60),0) zy_zrs_60,  ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60),0) zy_zrs_50,  ");
		sb.append("        ifnull( (select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50),0) zy_zrs_36,  ");
		sb.append("        ifnull( (select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) <=35),0) zy_zrs_35,  ");
		sb.append("         ");
		sb.append("       ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) > 60),0) fzy_zrs_60,  ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60),0) fzy_zrs_50,  ");
		sb.append("      ifnull(   (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50),0) fzy_zrs_36,  ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) <=35),0) fzy_zrs_35,  ");
		sb.append("         ");
		sb.append("        ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("             and (year(now())-year(sri)-1) > 60),0) cy_zrs_60,  ");
		sb.append("      ifnull(   (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60),0) cy_zrs_50,   ");
		sb.append("       ifnull(  (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50),0) cy_zrs_36,  ");
		sb.append("      ifnull(   (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and (year(now())-year(sri)-1) <=35),0) cy_zry_35  ");
		sb.append("   from dm_cs cs ");
		sb.append("  where cs.parent_id is null ");
		sb.append(" union all ");
		sb.append("  ");
		sb.append(" select cs.ID, ");
		sb.append("        cs.PARENT_ID, ");
		sb.append("        cs.mc, ");
		sb.append("        date_format(now(),'%Y') as nd, ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) > 60 then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               (year(now())-year(sri)-1) > 60 then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("      ifnull( sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50 then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50 then ");
		sb.append("           1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull(sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) <=35 then ");
		sb.append("          1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               (year(now())-year(sri)-1) <=35 then ");
		sb.append("          1 ");
		sb.append("          else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) > 60 and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_2 is not null  and ");
		sb.append("               (year(now())-year(sri)-1) > 60 then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60  and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null  and id_2 is not null  and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 then ");
		sb.append("           1 ");
		sb.append("           else 0 ");
		sb.append("        end),0), ");
		sb.append("      ifnull(  sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50  and id_2 is not null  then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null  and id_2 is not null and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50 then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0), ");
		sb.append("      ifnull( sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) <=35  and id_2 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null  and id_2 is not null and ");
		sb.append("               (year(now())-year(sri)-1) <=35 then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0), ");
		sb.append("       ifnull( sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) > 60 and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_3 is not null and ");
		sb.append("               (year(now())-year(sri)-1) > 60 then ");
		sb.append("           1 ");
		sb.append("         else 0 ");
		sb.append("        end),0), ");
		sb.append("      ifnull( sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 and id_3 is not null then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0), ");
		sb.append("     ifnull(  sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50  and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50 and id_3 is not null then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0), ");
		sb.append("     ifnull(   sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) <=35  and id_3 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and id_3 is not null and ");
		sb.append("               (year(now())-year(sri)-1) <=35 then ");
		sb.append("           1 else 0 ");
		sb.append("        end),0), ");
		sb.append("         ");
		sb.append("         ");
		sb.append("        ");
		sb.append("     ifnull(  sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) > 60  and id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               (year(now())-year(sri)-1) > 60  and id_4 is not null then ");
		sb.append("           1 else 0 ");
		sb.append("        end),0), ");
		sb.append("     ifnull( sum(  case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 and id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and (year(now())-year(sri)-1) >= 51 and (year(now())-year(sri)-1) <=60 and id_4 is not null then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0), ");
		sb.append("     ifnull(  sum( case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50 and id_4 is not null then ");
		sb.append("           1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and (year(now())-year(sri)-1) >= 36 and (year(now())-year(sri)-1) <=50 and id_4 is not null then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0), ");
		sb.append("     ifnull(   sum(case ");
		sb.append("          when parent_id = '0' and (year(now())-year(sri)-1) <=35  and id_4 is not null then ");
		sb.append("          1 ");
		sb.append("          when parent_id <> '0' and parent_id is not null and ");
		sb.append("               (year(now())-year(sri)-1) <=35 and id_4 is not null then ");
		sb.append("          1 else 0 ");
		sb.append("        end),0) ");
		sb.append("  ");
		sb.append("   from (select jb.id    id_1, ");
		sb.append("                zy.id    id_2, ");
		sb.append("                fz.id    id_3, ");
		sb.append("                cy.id    id_4, ");
		sb.append("                jb.sri, ");
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
		sb.append("  group by cs.ID, cs.PARENT_ID, cs.mc ");
		/*StringBuffer sbCount = new StringBuffer(" select count(*) from ( ");
		sbCount.append(sb).append(" )t ");
		int total = this.jdbcTemplate.queryForObject(sbCount.toString(), Integer.class);*/
		sb.append("limit "+pageSize * (page - 1)+","+pageSize);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		StringBuffer sbCount = new StringBuffer(" select count(*) from ( ");
			sbCount.append(" select cs.ID from dm_cs cs where cs.parent_id is null ")
				.append(" union all ")
				.append(" select cs.ID from ")
				.append(" ( ")
				.append(" select distinct jb.cs_dm ")
				.append(" from zs_ryjbxx jb ")
				.append(" left join zs_zysws zy ")
				.append(" on jb.id = zy.ry_id ")
				.append(" left join zs_fzysws fz ")
				.append(" on jb.id = fz.ry_id ")
				.append(" left join zs_cyry cy ")
				.append(" on jb.id = cy.ry_id ")
				.append(" where jb.yxbz = '1' ")
				.append(" ) jg ")
				.append("  right join dm_cs cs ")
				.append(" on jg.CS_DM = cs.id ")
				.append(" where cs.parent_id is not null ")
				.append(" )t ")
			;
		int total = this.jdbcTemplate.queryForObject(sbCount.toString(), Integer.class);
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);
		return obj;
	}

	public Map<String, Object> getRyztsjfx(int page, int pageSize,
			HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select cs.ID, ");
		sb.append("        cs.PARENT_ID, ");
		sb.append("        cs.mc, ");
		sb.append("        date_format(now(),'%Y') nd, ");
		sb.append("        ifnull((select count(id) from zs_ryjbxx where yxbz = '1'),0) zrs,  ");
		sb.append("       ifnull( (select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and xb_dm = '1') ,0)zrs_n,  ");
		sb.append("        ifnull((select count(id) ");
		sb.append("           from zs_ryjbxx ");
		sb.append("          where yxbz = '1' ");
		sb.append("            and xb_dm = '2'),0) zrs_v,  ");
		sb.append("         ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1'),0) zy_zrs, ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xb_dm = '1'),0) zy_zrs_n,  ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_zysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xb_dm = '2'),0) zy_zrs_v,  ");
		sb.append("         ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1'),0) fzy_zrs,  ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xb_dm = '1'),0) fzy_zrs_n,  ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_fzysws zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xb_dm = '2'),0) fzy_zrs_v,  ");
		sb.append("         ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1'),0) cy_zrs,  ");
		sb.append("        ifnull((select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xb_dm = '1'),0) cy_zrs_n,  ");
		sb.append("       ifnull( (select count(zy.id) ");
		sb.append("           from zs_cyry zy, zs_ryjbxx jb ");
		sb.append("          where zy.ry_id = jb.id ");
		sb.append("            and jb.yxbz = '1' ");
		sb.append("            and jb.xb_dm = '2'),0) cy_zrs_v  ");
		sb.append("  ");
		sb.append("   from dm_cs cs ");
		sb.append("  where cs.parent_id is null ");
		//sb.append("limit "+pageSize * (page - 1)+","+pageSize);
		//StringBuffer sqlCount = new StringBuffer("select count(*) from dm_cs cs where cs.parent_id is null");
		//int total = this.jdbcTemplate.queryForObject(sqlCount.toString(), Integer.class);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("data", ls);
		//obj.put("total", total);
		//obj.put("pageSize", pageSize);
		//obj.put("current", page);
		return obj;
	}

}
