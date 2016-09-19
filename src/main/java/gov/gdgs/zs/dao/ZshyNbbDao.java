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

	/**
	 * 事务所基本情况统计表1
	 * @param page
	 * @param pageSize
	 * @param map
	 * @return
	 */
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

	/**
	 * 行业人员情况统计表2
	 * @param map
	 * @return
	 */
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

	/**
	 * 事务所机构情况统计表3
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSwsjgqktjb(HashMap<String, Object> map) {
		ArrayList<Object> params=new ArrayList<>();
		String nd= map.get("ND").toString();
		if(nd==null){
			int year=Calendar.getInstance().get(Calendar.YEAR)-1;
			nd=year+"";
		}
		StringBuffer sql=new StringBuffer(" SELECT COUNT(DISTINCT jg.id) zjgs,   ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN jg.jgxz_dm = '1' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) hh_jgs, ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN jg.jgxz_dm = '2' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) yx_jgs, ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN YEAR(jg.sbrq) = ? THEN ");
		params.add(nd);
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) xz_jgs,  ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN YEAR(jg.sbrq) = ? AND jg.jgxz_dm = '1' THEN ");
		params.add(nd);
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) xz_hh_jgs, ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN YEAR(jg.sbrq) = ?  AND jg.jgxz_dm = '2' THEN ");
		params.add(nd);
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) xz_yx_jgs,  ");
		sql.append("        COUNT(DISTINCT bg.jg_id) bg_jgs,  ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN bg.mc = '性质' AND bg.jzhi = '有限公司' AND bg.xzhi = '合伙事务所' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) bg_yxhh_jgs,  ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN bg.mc = '性质' AND bg.xzhi = '有限公司' AND bg.jzhi = '合伙事务所' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) bg_hhyx_jgs,   ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN bg.mc LIKE '%法人%' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) bg_fr_jgs, ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN bg.mc LIKE '%单位名称%' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) bg_sm_jgs, ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN bg.mc LIKE '%注册资金%' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) bg_zczj_jgs,  ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN bg.mc LIKE '%办公地址%' THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) bg_bgdz_jgs, ");
		sql.append("        COUNT(DISTINCT zx.id) zx_jgs,  ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN jg.jgxz_dm = '1' AND zx.id IS NOT NULL THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) zx_hh_jgs,  ");
		sql.append("        ifnull(SUM(CASE ");
		sql.append("              WHEN jg.jgxz_dm = '2' AND zx.id IS NOT NULL THEN ");
		sql.append("               1 ");
		sql.append("              ELSE ");
		sql.append("               0 ");
		sql.append("            END),0) zx_yx_jgs,  ");
		sql.append("        COUNT(DISTINCT hb.id) hb_jgs  ");
		sql.append("   FROM zs_sdsb_swsjbqk jg ");
		sql.append("   LEFT JOIN (SELECT x.id, b.jg_id, x.mc, x.jzhi, x.xzhi ");
		sql.append("                FROM zs_jgbgspb b, zs_jgbgxxb x ");
		sql.append("               WHERE x.jgbgspb_id = b.id ");
		sql.append("                 AND spzt_dm = '8' ");
		sql.append("                 AND YEAR(bgrq) = ? ) bg ");
		params.add(nd);
		sql.append("     ON jg.id = bg.jg_id ");
		sql.append("   LEFT JOIN (SELECT * ");
		sql.append("                FROM zs_jgzx ");
		sql.append("               WHERE spzt = '2' ");
		sql.append("                 AND YEAR(zxrq) = ? ) zx ");
		params.add(nd);
		sql.append("     ON jg.id = zx.jg_id ");
		sql.append("   LEFT JOIN (SELECT * ");
		sql.append("                FROM zs_jghb hb ");
		sql.append("               WHERE hbzt = '6' ");
		sql.append("                 AND YEAR(hbsj) = ? ) hb ");
		params.add(nd);
		sql.append("     ON jg.id = hb.jg_id ");
		sql.append("  WHERE jg.ND= ?  AND jg.ZTBJ <> '0' ");
		params.add(nd);
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob; 
	}

	/**
	 * 行业经营收入汇总表4
	 * @param map
	 * @return
	 */
	public Map<String, Object> getHyjysrqkhzb(HashMap<String, Object> map) {
		ArrayList<Object> params=new ArrayList<>();
		List<Map<String,Object>> xmls=new ArrayList<>();
		List<Map<String,Object>> ls=new ArrayList<>();
		StringBuffer xmSql=new StringBuffer();
		StringBuffer sql=new StringBuffer();
		String nd= map.get("ND").toString();
		if(nd==null){
			int year=Calendar.getInstance().get(Calendar.YEAR)-1;
			nd=year+"";
		}
		
		xmSql=new StringBuffer(" SELECT '1' xh,'一、收入总额' xmmc,null xmzd1,'SRZE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '2' xh,'（一）主营业务收入合计' xmmc,'ZYYWSRHJ_HS' xmzd1,'ZYYWSRHJ_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '3' xh,'1、代理税务登记' xmmc,'DLSWDJ_HS' xmzd1,'DLSWDJ_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '4' xh,'2、代理纳税申报' xmmc,'DLNSSB_HS' xmzd1,'DLNSSB_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '5' xh,'3、代理建账记账' xmmc,'DLJZJZ_HS' xmzd1,'DLJZJZ_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '6' xh,'4、代理申请减、免、退税' xmmc,'DLSQJMTS_HS' xmzd1,'DLSQJMTS_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '7' xh,'5、代理申请增值税一般纳税人资格认定' xmmc,'DLZGRD_HS' xmzd1,'DLZGRD_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '8' xh,'6、代购普通发票' xmmc,null xmzd1,null xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '9' xh,'7、代理税务行政复议' xmmc,null xmzd1,null xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '10' xh,'8、代理制作涉税文书' xmmc,'DLZZSSWS_HS' xmzd1,'DLZZSSWS_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '11' xh,'9、受聘税务顾问咨询' xmmc,'SPSWGWZX_HS' xmzd1,'SPSWGWZX_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '12' xh,'10、代理税收筹划' xmmc,'DLSSCH_HS' xmzd1,'DLSSCH_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '13' xh,'11、涉税培训业务' xmmc,'SSPX_HS' xmzd1,'SSPX_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '14' xh,'12、代开增值税专用发票' xmmc,null xmzd1,null xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '15' xh,'13、企业所得税汇算清缴鉴证' xmmc,'SDSHSQJ_HS' xmzd1,'SDSHSQJ_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '16' xh,'14、企业财产损失税前扣除鉴证' xmmc,'CCSSSQKC_HS' xmzd1,'CCSSSQKC_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '17' xh,'15、其他涉税鉴证业务' xmmc,'QTSSJZ_HS' xmzd1,'QTSSJZ_JE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '18' xh,'16、其他涉税业务收入小计' xmmc,'QTSSYWSRXJHS' xmzd1,'QTSSYWSRXJ' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '19' xh,'（二）其他业务收入合计' xmmc,null xmzd1,'QTYWSRHJ' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '20' xh,'1、投资收益' xmmc,null xmzd1,'TZSY' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '21' xh,'2、补贴收入' xmmc,null xmzd1,'BTSR' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '22' xh,'3、营业外收入' xmmc,null xmzd1,'YYWSR' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '23' xh,'4、其他收入' xmmc,null xmzd1,'QTSR' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '24' xh,'二、支出总额' xmmc,null xmzd1,'ZCZE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '25' xh,'（一）主营业务成本' xmmc,null xmzd1,'ZYYWCB' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '26' xh,'（二）主营业务税金及附加' xmmc,null xmzd1,'ZYYWSJFJ' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '27' xh,'（三）营业费用' xmmc,null xmzd1,'YYFY' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '28' xh,'（四）管理费用' xmmc,null xmzd1,'GLFY' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '29' xh,'（五）财务费用' xmmc,null xmzd1,'CWFY' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '30' xh,'（六）营业外支出' xmmc,null xmzd1,'YYWZC' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '31' xh,'三、利润总额' xmmc,null xmzd1,'LRZE' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '32' xh,'（一）主营业务利润' xmmc,null xmzd1,'ZYYWLR' xmzd2 FROM DUAL  ");
		xmSql.append(" UNION ");
		xmSql.append(" SELECT '33' xh,'（二）其他业务利润' xmmc,null xmzd1,'QTYWLR' xmzd2 FROM DUAL  ");
		
		xmls=this.jdbcTemplate.queryForList(xmSql.toString());
		for(Map xm : xmls){
			String xh=xm.get("xh").toString();
			String xmmc=xm.get("xmmc").toString();
			if(null==xm.get("xmzd1")&&null==xm.get("xmzd2")){
				sql=new StringBuffer(" SELECT xm.nd, ");
				sql.append("        xm.xh, ");
				sql.append("        xm.xmmc, ");
				sql.append("        IFNULL(xj.sn_hs,0) sn_hs, ");
				sql.append("        IFNULL(xj.sn_je,0) sn_je, ");
				sql.append("        IFNULL(xj.jn_hs,0) jn_hs, ");
				sql.append("        IFNULL(xj.jn_je,0) jn_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0) end zj_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0) end zj_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_hs,0) when 0 then 0 else ROUND((IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0))/IFNULL(xj.sn_hs,0)*100,2) end,'%')) end zjl_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_je,0) when 0 then 0 else ROUND((IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0))/IFNULL(xj.sn_je,0)*100,2) end,'%')) end zjl_je ");
				sql.append("   FROM (SELECT '"+nd+"' nd, '"+xh+"' xh, '"+xmmc+"' xmmc FROM DUAL) xm ");
				sql.append("   LEFT JOIN (SELECT t.nd, ");
				sql.append("                     '--' jn_hs, ");
				sql.append("                     '--' jn_je, ");
				sql.append("                     '--' sn_hs, ");
				sql.append("                     '--' sn_je ");
				sql.append("                FROM zs_sdsb_jysrqk t ");
				sql.append("               GROUP BY t.ND) xj ");
				sql.append("     on xm.nd = xj.nd ");
				Map data=this.jdbcTemplate.queryForMap(sql.toString());
				ls.add(data);
			}else if(null==xm.get("xmzd1")){
				String xmzd2=xm.get("xmzd2").toString();
				sql=new StringBuffer(" SELECT xm.nd, ");
				sql.append("        xm.xh, ");
				sql.append("        xm.xmmc, ");
				sql.append("        IFNULL(xj.sn_hs,0) sn_hs, ");
				sql.append("        IFNULL(xj.sn_je,0) sn_je, ");
				sql.append("        IFNULL(xj.jn_hs,0) jn_hs, ");
				sql.append("        IFNULL(xj.jn_je,0) jn_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0) end zj_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0) end zj_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_hs,0) when 0 then 0 else ROUND((IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0))/IFNULL(xj.sn_hs,0)*100,2) end,'%')) end zjl_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_je,0) when 0 then 0 else ROUND((IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0))/IFNULL(xj.sn_je,0)*100,2) end,'%')) end zjl_je ");
				sql.append("   FROM (SELECT '"+nd+"' nd, '"+xh+"' xh, '"+xmmc+"' xmmc FROM DUAL) xm ");
				sql.append("   LEFT JOIN (SELECT t.nd, ");
				sql.append("                     '--' jn_hs, ");
				sql.append("                     SUM(IFNULL(t."+xmzd2+",0)) jn_je, ");
				sql.append("                     '--' sn_hs, ");
				sql.append("                     SUM(IFNULL(t."+xmzd2+"0,0)) sn_je ");
				sql.append("                FROM zs_sdsb_jysrqk t ");
				sql.append("               GROUP BY t.ND) xj ");
				sql.append("     on xm.nd = xj.nd ");
				Map data=this.jdbcTemplate.queryForMap(sql.toString());
				ls.add(data);
			}else{
				String xmzd1=xm.get("xmzd1").toString();
				String xmzd2=xm.get("xmzd2").toString();
				sql=new StringBuffer(" SELECT xm.nd, ");
				sql.append("        xm.xh, ");
				sql.append("        xm.xmmc, ");
				sql.append("        IFNULL(xj.sn_hs,0) sn_hs, ");
				sql.append("        IFNULL(xj.sn_je,0) sn_je, ");
				sql.append("        IFNULL(xj.jn_hs,0) jn_hs, ");
				sql.append("        IFNULL(xj.jn_je,0) jn_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0) end zj_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0) end zj_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_hs,0) when 0 then 0 else ROUND((IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0))/IFNULL(xj.sn_hs,0)*100,2) end,'%')) end zjl_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_je,0) when 0 then 0 else ROUND((IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0))/IFNULL(xj.sn_je,0)*100,2) end,'%')) end zjl_je ");
				sql.append("   FROM (SELECT '"+nd+"' nd, '"+xh+"' xh, '"+xmmc+"' xmmc FROM DUAL) xm ");
				sql.append("   LEFT JOIN (SELECT t.nd, ");
				sql.append("                     SUM(IFNULL(t."+xmzd1+",0)) jn_hs, ");
				sql.append("                     SUM(IFNULL(t."+xmzd2+",0)) jn_je, ");
				sql.append("                     SUM(IFNULL(t."+xmzd1+"0,0)) sn_hs, ");
				sql.append("                     SUM(IFNULL(t."+xmzd2+"0,0)) sn_je ");
				sql.append("                FROM zs_sdsb_jysrqk t ");
				sql.append("               GROUP BY t.ND) xj ");
				sql.append("     on xm.nd = xj.nd ");
				Map data=this.jdbcTemplate.queryForMap(sql.toString());
				ls.add(data);
			}
			
		}		
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob; 
	}

	/**
	 * 行业经营规模情况统计表5
	 * @param map
	 * @return
	 */
	public Map<String, Object> getHyjygmqktjb(HashMap<String, Object> map) {
		ArrayList<Object> params=new ArrayList<>();
		String nd= map.get("ND").toString();
		if(nd==null){
			int year=Calendar.getInstance().get(Calendar.YEAR)-1;
			nd=year+"";
		}
		StringBuffer sql=new StringBuffer(" select jg.dwmc, ");
		sql.append("        ifnull(t.bnsrze_hj,0) bnsrze_hj, ");
		sql.append("        ifnull(t.bnsrze_ssfw,0) bnsrze_ssfw, ");
		sql.append("        ifnull(t.bnsrze_ssjz,0) bnsrze_ssjz, ");
		sql.append("        ifnull(t.bnsrze_qtyw,0) bnsrze_qtyw, ");
		sql.append("        ifnull(t.snsrze,0) snsrze, ");
		sql.append("        case ifnull(t.snsrze,0) when 0 then 0 else (ifnull(t.bnsrze_hj,0)-ifnull(t.snsrze,0))/ifnull(t.snsrze,0) end zzl, ");
		sql.append("        t.bz ");
		sql.append("   from zs_sdsb_jygmtjb t, zs_jg jg ");
		sql.append("  where t.jg_id = jg.id ");
		sql.append("    and t.nd = ? ");
		sql.append("    and t.ztbj <> '0' ");
		sql.append("  order by jg.id ");
		params.add(nd);
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob; 
	}

	/**
	 * 行业鉴证业务情况统计表6
	 * @param map
	 * @return
	 */
	public Map<String, Object> getHyjzywqktjb(HashMap<String, Object> map) {
		ArrayList<Object> params=new ArrayList<>();
		List<Map<String,Object>> xmls=new ArrayList<>();
		List<Map<String,Object>> ls=new ArrayList<>();
		StringBuffer xmSql=new StringBuffer();
		StringBuffer sql=new StringBuffer();
		String nd= map.get("ND").toString();
		if(nd==null){
			int year=Calendar.getInstance().get(Calendar.YEAR)-1;
			nd=year+"";
		}
		
		xmSql=new StringBuffer(" select '1' xh,'企业所得税汇算清缴总户数' xmmc,'HSQJHS' xmzd1,null xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '2' xh,'企业所得税汇算清缴纳税申报鉴证业务' xmmc,'HSQJJE_HS' xmzd1,'HSQJJE_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '3' xh,'其中：（1）调增应纳所得税税额' xmmc,'TZYNSDSE_HS' xmzd1,'TZYNSDSE_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '4' xh,'（2）调减应纳所得税税额' xmmc,'TJYNSDSE_HS' xmzd1,'TJYNSDSE_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '5' xh,'企业税前弥补亏损鉴证业务' xmmc,'MBKSJE_HS' xmzd1,'MBKSJE_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '6' xh,'企业财产损失税前扣除鉴证业务' xmmc,'CCSSKC_HS' xmzd1,'CCSSKC_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '7' xh,'土地增值税清算签证业务' xmmc,'TDZZSQSJZ_HS' xmzd1,'TDZZSQSJZ_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '8' xh,'其他鉴证业务小计' xmmc,null xmzd1,'JZYWHJ' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '9' xh,'其中：（1）高新技术企业认定签证业务' xmmc,'GXJSQYRDQZYW_HS' xmzd1,'GXJSQYRDQZYW_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '10' xh,'（2）企业注销税务登记税款清算签证业务' xmmc,'QYZXSWDESKJSJZYW_HS' xmzd1,'QYZXSWDESKJSJZYW_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '11' xh,'（3）研发费加计扣除签证业务' xmmc,'YFFJJKCJZYW_HS' xmzd1,'YFFJJKCJZYW_JE' xmzd2 from dual ");
		xmSql.append(" union ");
		xmSql.append(" select '12' xh,'（4）其他' xmmc,'QT_HS' xmzd1,'QT_JE' xmzd2 from dual ");
		
		xmls=this.jdbcTemplate.queryForList(xmSql.toString());
		for(Map xm : xmls){
			String xh=xm.get("xh").toString();
			String xmmc=xm.get("xmmc").toString();
			if(null==xm.get("xmzd1")&&null!=xm.get("xmzd2")){
				String xmzd2=xm.get("xmzd2").toString();
				sql=new StringBuffer(" SELECT xm.nd, ");
				sql.append("        xm.xh, ");
				sql.append("        xm.xmmc, ");
				sql.append("        ifnull(xj.sn_hs,0) sn_hs, ");
				sql.append("        ifnull(xj.sn_je,0) sn_je, ");
				sql.append("        ifnull(xj.jn_hs,0) jn_hs, ");
				sql.append("        ifnull(xj.jn_je,0) jn_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0) end zj_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0) end zj_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_hs,0) when 0 then 0 else ROUND((IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0))/IFNULL(xj.sn_hs,0)*100,2) end,'%')) end zjl_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_je,0) when 0 then 0 else ROUND((IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0))/IFNULL(xj.sn_je,0)*100,2) end,'%')) end zjl_je ");
				sql.append("   FROM (SELECT '"+nd+"' nd, '"+xh+"' xh, '"+xmmc+"' xmmc FROM DUAL) xm ");
				sql.append("   LEFT JOIN (SELECT t.nd, ");
				sql.append("                     '--' jn_hs, ");
				sql.append("                     round(SUM(ifnull(t."+xmzd2+",0))/10000,2) jn_je, ");
				sql.append("                     '--' sn_hs, ");
				sql.append("                     round(SUM(ifnull(t."+xmzd2+"0,0))/10000,2) sn_je ");
				sql.append("                FROM zs_sdsb_jzywqktjb t where t.ztbj<>'0' ");
				sql.append("               GROUP BY t.ND) xj ");
				sql.append("     on xm.nd = xj.nd ");
				Map data=this.jdbcTemplate.queryForMap(sql.toString());
				ls.add(data);
			}else if(null!=xm.get("xmzd1")&&null==xm.get("xmzd2")){
				String xmzd1=xm.get("xmzd1").toString();
				sql=new StringBuffer(" SELECT xm.nd, ");
				sql.append("        xm.xh, ");
				sql.append("        xm.xmmc, ");
				sql.append("        ifnull(xj.sn_hs,0) sn_hs, ");
				sql.append("        ifnull(xj.sn_je,0) sn_je, ");
				sql.append("        ifnull(xj.jn_hs,0) jn_hs, ");
				sql.append("        ifnull(xj.jn_je,0) jn_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0) end zj_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0) end zj_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_hs,0) when 0 then 0 else ROUND((IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0))/IFNULL(xj.sn_hs,0)*100,2) end,'%')) end zjl_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_je,0) when 0 then 0 else ROUND((IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0))/IFNULL(xj.sn_je,0)*100,2) end,'%')) end zjl_je ");
				sql.append("   FROM (SELECT '"+nd+"' nd, '"+xh+"' xh, '"+xmmc+"' xmmc FROM DUAL) xm ");
				sql.append("   LEFT JOIN (SELECT t.nd, ");
				sql.append("                     SUM(ifnull(t."+xmzd1+",0)) jn_hs, ");
				sql.append("                     '--' jn_je, ");
				sql.append("                     SUM(ifnull(t."+xmzd1+"0,0)) sn_hs, ");
				sql.append("                     '--' sn_je ");
				sql.append("                FROM zs_sdsb_jzywqktjb t where t.ztbj<>'0' ");
				sql.append("               GROUP BY t.ND) xj ");
				sql.append("     on xm.nd = xj.nd ");
				Map data=this.jdbcTemplate.queryForMap(sql.toString());
				ls.add(data);
			}else{
				String xmzd1=xm.get("xmzd1").toString();
				String xmzd2=xm.get("xmzd2").toString();
				sql=new StringBuffer(" SELECT xm.nd, ");
				sql.append("        xm.xh, ");
				sql.append("        xm.xmmc, ");
				sql.append("        ifnull(xj.sn_hs,0) sn_hs, ");
				sql.append("        ifnull(xj.sn_je,0) sn_je, ");
				sql.append("        ifnull(xj.jn_hs,0) jn_hs, ");
				sql.append("        ifnull(xj.jn_je,0) jn_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0) end zj_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0) end zj_je, ");
				sql.append("        case concat(xj.jn_hs,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_hs,0) when 0 then 0 else ROUND((IFNULL(xj.jn_hs,0)-IFNULL(xj.sn_hs,0))/IFNULL(xj.sn_hs,0)*100,2) end,'%')) end zjl_hs, ");
				sql.append("        case concat(xj.jn_je,'') when '--' then '--' else ( ");
				sql.append(" 		 concat(case IFNULL(xj.sn_je,0) when 0 then 0 else ROUND((IFNULL(xj.jn_je,0)-IFNULL(xj.sn_je,0))/IFNULL(xj.sn_je,0)*100,2) end,'%')) end zjl_je ");
				sql.append("   FROM (SELECT '"+nd+"' nd, '"+xh+"' xh, '"+xmmc+"' xmmc FROM DUAL) xm ");
				sql.append("   LEFT JOIN (SELECT t.nd, ");
				sql.append("                     SUM(ifnull(t."+xmzd1+",0)) jn_hs, ");
				sql.append("                     round(SUM(ifnull(t."+xmzd2+",0))/10000,2) jn_je, ");
				sql.append("                     SUM(ifnull(t."+xmzd1+"0,0)) sn_hs, ");
				sql.append("                     round(SUM(ifnull(t."+xmzd2+"0,0))/10000,2) sn_je ");
				sql.append("                FROM zs_sdsb_jzywqktjb t where t.ztbj<>'0' ");
				sql.append("               GROUP BY t.ND) xj ");
				sql.append("     on xm.nd = xj.nd ");
				Map data=this.jdbcTemplate.queryForMap(sql.toString());
				ls.add(data);
			}
			
		}		
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob;
	}

}
