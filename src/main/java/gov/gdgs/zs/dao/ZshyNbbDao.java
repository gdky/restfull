package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class ZshyNbbDao extends BaseJdbcDao{

	public Map<String, Object> getSwsjbqktj(int page, int pageSize,
			HashMap<String, Object> map) {
		Condition condition = new Condition();
		condition.add("jg.cs_dm", Condition.EQUAL, map.get("CS_DM"));
		condition.add("qk.nd", Condition.EQUAL, map.get("ND"));
		StringBuffer sql=new StringBuffer(" select sql_calc_found_rows @rownum := @rownum + 1 as 'key', xj.* ");
		sql.append("   from (select qk.id, ");
		sql.append("                qk.jg_id, ");
		sql.append("                jg.dwmc, ");
		sql.append("                xz.mc        jgxz, ");
		sql.append("                qk.frdbxm    fddbr, ");
		sql.append("                qk.czrs      gdrs, ");
		sql.append("                qk.hhrs, ");
		sql.append("                qk.ryzs      zrs, ");
		sql.append("                qk.zyzcswsrs zyrs, ");
		sql.append("                qk.zczj, ");
		sql.append("                qk.yysr      yyzj, ");
		sql.append("                qk.zcze, ");
		sql.append("                qk.srze, ");
		sql.append("                qk.lrze, ");
		sql.append("                cs.mc        szd, ");
		sql.append("                qk.wths      wtrhs ");
		sql.append("           from zs_sdsb_swsjbqk qk, zs_jg jg ");
		sql.append("           left join dm_jgxz xz ");
		sql.append("             on jg.jgxz_dm = xz.id ");
		sql.append("           left join dm_cs cs ");
		sql.append("             on jg.cs_dm = cs.id ");
		sql.append("          "+condition.getSql());
		sql.append("            and jg.id = qk.jg_id ");
		sql.append("            and jg.yxbz = '1' ");
		sql.append("          order by jg.id) xj, ");
		sql.append("        (select @rownum := ?) xh ");
		sql.append("  LIMIT ?, ? ");
		
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

	public Map<String, Object> getHyryqktjb(HashMap<String, Object> map) {
		ArrayList<Object> params=new ArrayList<>();
		String nd= map.get("ND").toString();
		if(nd==null){
			nd=Calendar.getInstance().get(Calendar.YEAR)-1+"";
		}
		StringBuffer sql=new StringBuffer(" select xj.xh, ");
		sql.append("        case xj.xh when '1' then '1:执业税务师' ");
		sql.append("                   when '2' then '其中：股东或合伙人' ");
		sql.append("                   when '3' then '2:具有其它中介执业资格的从业人员' ");
		sql.append("                   when '4' then '3.从业人员' ");
		sql.append("                   else '总数' end xmmc, ");
		sql.append("        '' bz, ");
		sql.append("        SUM(IFNULL(xj.zrs,0)) zrs, ");
		sql.append("        SUM(IFNULL(xj.v_zrs,0)) v_zrs, ");
		sql.append("        SUM(IFNULL(xj.n_zrs,0)) n_zrs, ");
		sql.append("        SUM(IFNULL(xj.yjs_zrs,0)) yjs_zrs, ");
		sql.append("        SUM(IFNULL(xj.bk_zrs,0)) bk_zrs, ");
		sql.append("        SUM(IFNULL(xj.dz_zrs,0)) dz_zrs, ");
		sql.append("        SUM(IFNULL(xj.zz_zrs,0)) zz_zrs, ");
		sql.append("        SUM(IFNULL(xj.35_zrs,0)) 35_zrs, ");
		sql.append("        SUM(IFNULL(xj.50_zrs,0)) 50_zrs, ");
		sql.append("        SUM(IFNULL(xj.60_zrs,0)) 60_zrs, ");
		sql.append("        SUM(IFNULL(xj.61_zrs,0)) 61_zrs ");
		sql.append(" from( ");
		sql.append(" SELECT '1' xh,  ");
		sql.append("        SUM(t.ZYSWS_RY_ZJ) zrs, ");
		sql.append("        SUM(t.ZYSWS_RY_NV) v_zrs, ");
		sql.append("        SUM(t.ZYSWS_RY_ZJ - t.ZYSWS_RY_NV) n_zrs, ");
		sql.append("        SUM(t.ZYSWS_XL_YJS) yjs_zrs, ");
		sql.append("        SUM(t.ZYSWS_XL_BK) bk_zrs, ");
		sql.append("        SUM(t.ZYSWS_XL_DZ) dz_zrs, ");
		sql.append("        SUM(t.ZYSWS_XL_ZZ) zz_zrs, ");
		sql.append("        SUM(t.ZYSWS_NL_35) 35_zrs, ");
		sql.append("        SUM(t.ZYSWS_NL_50) 50_zrs, ");
		sql.append("        SUM(t.ZYSWS_NL_60L) 60_zrs, ");
		sql.append("        SUM(t.ZYSWS_NL_60U) 61_zrs ");
		sql.append("   FROM zs_sdsb_hyryqktj t ");
		sql.append("  WHERE t.nd = ? ");
		params.add(nd);
		sql.append(" UNION ");
		sql.append(" SELECT '2' xh,  ");
		sql.append("        SUM(t.HHCZR_RY_ZJ) zrs, ");
		sql.append("        SUM(t.HHCZR_RY_NV) v_zrs, ");
		sql.append("        SUM(t.HHCZR_RY_ZJ - t.HHCZR_RY_NV) n_zrs, ");
		sql.append("        SUM(t.HHCZR_XL_YJS) yjs_zrs, ");
		sql.append("        SUM(t.HHCZR_XL_BK) bk_zrs, ");
		sql.append("        SUM(t.HHCZR_XL_DZ) dz_zrs, ");
		sql.append("        SUM(t.HHCZR_XL_ZZ) zz_zrs, ");
		sql.append("        SUM(t.HHCZR_NL_35) 35_zrs, ");
		sql.append("        SUM(t.HHCZR_NL_50) 50_zrs, ");
		sql.append("        SUM(t.HHCZR_NL_60L) 60_zrs, ");
		sql.append("        SUM(t.HHCZR_NL_60U) 61_zrs ");
		sql.append("   FROM zs_sdsb_hyryqktj t ");
		sql.append("  WHERE t.nd = ? ");
		params.add(nd);
		sql.append(" UNION ");
		sql.append(" SELECT '3' xh,  ");
		sql.append("        SUM(t.ZCKJS_RY_ZJ + t.ZCPGS_RY_ZJ + t.LS_RY_ZJ) zrs, ");
		sql.append("        SUM(t.ZCKJS_RY_NV + t.ZCPGS_RY_NV + t.LS_RY_NV) v_zrs, ");
		sql.append("        SUM((t.ZCKJS_RY_ZJ + t.ZCPGS_RY_ZJ + t.LS_RY_ZJ) - (t.ZCKJS_RY_NV + t.ZCPGS_RY_NV + t.LS_RY_NV)) n_zrs, ");
		sql.append("        SUM(t.ZCKJS_XL_YJS + t.ZCPGS_XL_YJS + t.LS_XL_YJS) yjs_zrs, ");
		sql.append("        SUM(t.ZCKJS_XL_BK + t.ZCPGS_XL_BK + t.LS_XL_BK) bk_zrs, ");
		sql.append("        SUM(t.ZCKJS_XL_DZ + t.ZCPGS_XL_DZ + t.LS_XL_DZ) dz_zrs, ");
		sql.append("        SUM(t.ZCKJS_XL_ZZ + t.ZCPGS_XL_ZZ + t.LS_XL_ZZ) zz_zrs, ");
		sql.append("        SUM(t.ZCKJS_NL_35 + t.ZCPGS_NL_35 + t.LS_NL_35) 35_zrs, ");
		sql.append("        SUM(t.ZCKJS_NL_50 + t.ZCPGS_NL_50 + t.LS_NL_50) 50_zrs, ");
		sql.append("        SUM(t.ZCKJS_NL_60L + t.ZCPGS_NL_60L + t.LS_NL_60L) 60_zrs, ");
		sql.append("        SUM(t.ZCKJS_NL_60U + t.ZCPGS_NL_60U + t.LS_NL_60U) 61_zrs ");
		sql.append("   FROM zs_sdsb_hyryqktj t ");
		sql.append("  WHERE t.nd = ? ");
		params.add(nd);
		sql.append(" UNION ");
		sql.append(" SELECT '4' xh,  ");
		sql.append("        SUM(t.QTCYRY_RY_ZJ) zrs, ");
		sql.append("        SUM(t.QTCYRY_RY_NV) v_zrs, ");
		sql.append("        SUM(t.QTCYRY_RY_ZJ - t.QTCYRY_RY_NV) n_zrs, ");
		sql.append("        SUM(t.QTCYRY_XL_YJS) yjs_zrs, ");
		sql.append("        SUM(t.QTCYRY_XL_BK) bk_zrs, ");
		sql.append("        SUM(t.QTCYRY_XL_DZ) dz_zrs, ");
		sql.append("        SUM(t.QTCYRY_XL_ZZ) zz_zrs, ");
		sql.append("        SUM(t.QTCYRY_NL_35) 35_zrs, ");
		sql.append("        SUM(t.QTCYRY_NL_50) 50_zrs, ");
		sql.append("        SUM(t.QTCYRY_NL_60L) 60_zrs, ");
		sql.append("        SUM(t.QTCYRY_NL_60U) 61_zrs ");
		sql.append("   FROM zs_sdsb_hyryqktj t ");
		sql.append("  WHERE t.nd = ?)xj ");
		params.add(nd);
		sql.append("  GROUP BY xh WITH ROLLUP ");
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob; 
	}

}
