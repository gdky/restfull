package gov.gdgs.zs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;
@Repository
public class XtywbbDao extends BaseJdbcDao {

	public Map<String, Object> getNdjysrtj(int page, int pageSize,
			HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select '一、收入总额' xmlx, ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl,  ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl  ");
		sb.append("   from (select ifnull(sum(srze), 0) je_bn, ");
		sb.append("                ifnull(null, 0) hs_bn, ");
		sb.append("                ifnull(sum(SRZE0),0)je_qn, ");
		sb.append("                ifnull(null, 0) hs_qn, ");
		sb.append("                ifnull(sum(srze),0)-ifnull(sum(srze0),0) je_zj, ");
		sb.append("                ifnull(null,0)-ifnull(null,0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append("  union all ");
		sb.append("  select '(一)、主营业务合计', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(ZYYWSRHJ_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(ZYYWSRHJ_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(ZYYWSRHJ_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(ZYYWSRHJ_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(ZYYWSRHJ_JE),0)-ifnull(sum(ZYYWSRHJ_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(ZYYWSRHJ_HS),0)-ifnull(sum(ZYYWSRHJ_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append("  union all ");
		sb.append("  select '1、涉税服务业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(SSFWYW_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(SSFWYW_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(SSFWYW_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(SSFWYW_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(SSFWYW_JE),0)-ifnull(sum(SSFWYW_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(SSFWYW_HS),0)-ifnull(sum(SSFWYW_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(1)代理税务登记', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLSWDJ_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLSWDJ_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLSWDJ_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLSWDJ_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLSWDJ_JE),0)-ifnull(sum(DLSWDJ_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLSWDJ_HS),0)-ifnull(sum(DLSWDJ_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(2)代理纳税申报', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLNSSB_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLNSSB_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLNSSB_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLNSSB_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLNSSB_JE),0)-ifnull(sum(DLNSSB_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLNSSB_HS),0)-ifnull(sum(DLNSSB_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(3)代理建帐记账', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLJZJZ_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLJZJZ_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLJZJZ_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLJZJZ_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLJZJZ_JE),0)-ifnull(sum(DLJZJZ_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLJZJZ_HS),0)-ifnull(sum(DLJZJZ_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(4)代理申请减、免、退税', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLSQJMTS_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLSQJMTS_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLSQJMTS_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLSQJMTS_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLSQJMTS_JE),0)-ifnull(sum(DLSQJMTS_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLSQJMTS_HS),0)-ifnull(sum(DLSQJMTS_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all          ");
		sb.append(" select '(5)代理申请增值税一般纳税人资格认定', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLZGRD_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLZGRD_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLZGRD_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLZGRD_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLZGRD_JE),0)-ifnull(sum(DLZGRD_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLZGRD_HS),0)-ifnull(sum(DLZGRD_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(6)代理制作涉税文书', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLZZSSWS_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLZZSSWS_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLZZSSWS_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLZZSSWS_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLZZSSWS_JE),0)-ifnull(sum(DLZZSSWS_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLZZSSWS_HS),0)-ifnull(sum(DLZZSSWS_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(7)代理一机多卡业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLYJDK_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLYJDK_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLYJDK_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLYJDK_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLYJDK_JE),0)-ifnull(sum(DLYJDK_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLYJDK_HS),0)-ifnull(sum(DLYJDK_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(8)受聘税务顾问咨询', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(SPSWGWZX_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(SPSWGWZX_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(SPSWGWZX_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(SPSWGWZX_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(SPSWGWZX_JE),0)-ifnull(sum(SPSWGWZX_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(SPSWGWZX_HS),0)-ifnull(sum(SPSWGWZX_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(9)代理税收筹划', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(DLSSCH_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(DLSSCH_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(DLSSCH_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(DLSSCH_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(DLSSCH_JE),0)-ifnull(sum(DLSSCH_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(DLSSCH_HS),0)-ifnull(sum(DLSSCH_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(10)涉税培训业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(SSPX_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(SSPX_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(SSPX_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(SSPX_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(SSPX_JE),0)-ifnull(sum(SSPX_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(SSPX_HS),0)-ifnull(sum(SSPX_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(11)其他涉税服务业务小计', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(QTSSFWYWXJ_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(QTSSFWYWXJ_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(QTSSFWYWXJ_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(QTSSFWYWXJ_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(QTSSFWYWXJ_JE),0)-ifnull(sum(QTSSFWYWXJ_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(QTSSFWYWXJ_HS),0)-ifnull(sum(QTSSFWYWXJ_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '2、涉税鉴证业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(SSJZYW_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(SSJZYW_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(SSJZYW_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(SSJZYW_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(SSJZYW_JE),0)-ifnull(sum(SSJZYW_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(SSJZYW_HS),0)-ifnull(sum(SSJZYW_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(1)企业所得税汇算清缴纳税申报鉴证业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(SDSHSQJ_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(SDSHSQJ_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(SDSHSQJ_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(SDSHSQJ_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(SDSHSQJ_JE),0)-ifnull(sum(SDSHSQJ_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(SDSHSQJ_HS),0)-ifnull(sum(SDSHSQJ_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(2)企业税前弥补亏损鉴证业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(MBKS_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(MBKS_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(MBKS_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(MBKS_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(MBKS_JE),0)-ifnull(sum(MBKS_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(MBKS_HS),0)-ifnull(sum(MBKS_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(3)企业资产损失税前扣除鉴证业务', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(CCSSSQKC_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(CCSSSQKC_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(CCSSSQKC_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(CCSSSQKC_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(CCSSSQKC_JE),0)-ifnull(sum(CCSSSQKC_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(CCSSSQKC_HS),0)-ifnull(sum(CCSSSQKC_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(4)土地增值税清算鉴证', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(TT_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(TT_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(TT_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(TT_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(TT_JE),0)-ifnull(sum(TT_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(TT_HS),0)-ifnull(sum(TT_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(5)其他涉税鉴证业务小计', ");
		sb.append("        hs_qn, ");
		sb.append("        je_qn, ");
		sb.append("        hs_bn, ");
		sb.append("        je_bn, ");
		sb.append("         hs_zj, ");
		sb.append("        je_zj, ");
		sb.append("        round(hs_zj* 100 / (case ");
		sb.append("                when hs_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 hs_bn ");
		sb.append("              end), ");
		sb.append("              2) hs_bl, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(QTSSJZ_JE), 0) je_bn, ");
		sb.append("                ifnull(sum(QTSSJZ_HS), 0) hs_bn, ");
		sb.append("                ifnull(sum(QTSSJZ_JE0),0) je_qn, ");
		sb.append("                ifnull(sum(QTSSJZ_HS0), 0) hs_qn, ");
		sb.append("                ifnull(sum(QTSSJZ_JE),0)-ifnull(sum(QTSSJZ_JE0),0) je_zj, ");
		sb.append("                ifnull(sum(QTSSJZ_HS),0)-ifnull(sum(QTSSJZ_HS0),0) hs_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(二)其他收入合计', ");
		sb.append(" 			 null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(QTYWSRHJ), 0) je_bn, ");
		sb.append("                ifnull(sum(QTYWSRHJ0),0) je_qn, ");
		sb.append("                ifnull(sum(QTYWSRHJ),0)-ifnull(sum(QTYWSRHJ0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '二、支出总额', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(ZCZE), 0) je_bn, ");
		sb.append("                ifnull(sum(ZCZE0),0) je_qn, ");
		sb.append("                ifnull(sum(ZCZE),0)-ifnull(sum(ZCZE0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(一)主营业务成本', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(ZYYWCB), 0) je_bn, ");
		sb.append("                ifnull(sum(ZYYWCB0),0) je_qn, ");
		sb.append("                ifnull(sum(ZYYWCB),0)-ifnull(sum(ZYYWCB0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(二)主营业务税金及附加', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(ZYYWSJFJ), 0) je_bn, ");
		sb.append("                ifnull(sum(ZYYWSJFJ0),0) je_qn, ");
		sb.append("                ifnull(sum(ZYYWSJFJ),0)-ifnull(sum(ZYYWSJFJ0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(三)营业费用', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(YYFY), 0) je_bn, ");
		sb.append("                ifnull(sum(YYFY0),0) je_qn, ");
		sb.append("                ifnull(sum(YYFY),0)-ifnull(sum(YYFY0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(四)管理费用', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(GLFY), 0) je_bn, ");
		sb.append("                ifnull(sum(GLFY0),0) je_qn, ");
		sb.append("                ifnull(sum(GLFY),0)-ifnull(sum(GLFY0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(五)财务费用', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(CWFY), 0) je_bn, ");
		sb.append("                ifnull(sum(CWFY0),0) je_qn, ");
		sb.append("                ifnull(sum(CWFY),0)-ifnull(sum(CWFY0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(六)营业外支出', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(YYWZC), 0) je_bn, ");
		sb.append("                ifnull(sum(YYWZC0),0) je_qn, ");
		sb.append("                ifnull(sum(YYWZC),0)-ifnull(sum(YYWZC0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		sb.append(" union all ");
		sb.append(" select '(七)其他支出', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(QTZC), 0) je_bn, ");
		sb.append("                ifnull(sum(QTZC0),0) je_qn, ");
		sb.append("                ifnull(sum(QTZC),0)-ifnull(sum(QTZC0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a          ");
		sb.append(" union all ");
		sb.append(" select '三、利润总额', ");
		sb.append("        null, ");
		sb.append("        je_qn, ");
		sb.append("        null, ");
		sb.append("        je_bn, ");
		sb.append("        null, ");
		sb.append("        je_zj, ");
		sb.append("        null, ");
		sb.append("        round(je_zj * 100 / (case ");
		sb.append("                when je_bn = 0 then ");
		sb.append("                 null ");
		sb.append("                else ");
		sb.append("                 je_bn  ");
		sb.append("              end), ");
		sb.append("              2) je_bl ");
		sb.append("   from (select ifnull(sum(LRZE), 0) je_bn, ");
		sb.append("                ifnull(sum(LRZE0),0) je_qn, ");
		sb.append("                ifnull(sum(LRZE),0)-ifnull(sum(LRZE0),0) je_zj ");
		sb.append("           from zs_sdsb_jysrqk ");
		sb.append("          where nd = '2014') a ");
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		return obj;
	}

	public Map<String, Object> getHyjygmqktj(int page, int pageSize,
			HashMap<String, Object> map) {
		StringBuffer where = new StringBuffer();
			where.append(" from (select jg.dwmc,jb.bz, ")
				.append(" ifnull(sum(BNSRZE_HJ), 0) bnhj, ")
				.append(" ifnull(sum(BNSRZE_SSFW), 0) ssfw, ")
				.append(" ifnull(sum(BNSRZE_SSJZ), 0) ssjz, ")
				.append(" ifnull(sum(BNSRZE_QTYW), 0) qtyw, ")
				.append(" ifnull(sum(SNSRZE), 0) qnhj, ")
				.append(" ifnull(sum(BNSRZE_HJ), 0) - ifnull(sum(SNSRZE), 0) zz ")
				.append(" from zs_sdsb_jygmtjb jb, zs_jg jg ")
				.append(" where jb.JG_ID = jg.ID and nd = '2014' group by jg.dwmc ,jb.bz) t ");
		StringBuffer sql = new StringBuffer(" select t.*,round(zz * 100 / (case when qnhj = 0 then null else qnhj end),2) zz_bl ");
		StringBuffer sqlCount = new StringBuffer(" select count(*) ");
		int total = this.jdbcTemplate.queryForObject(sqlCount.append(where).toString(), Integer.class);
		where.append("limit "+pageSize * (page - 1)+","+pageSize);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql.append(where).toString());
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);
		return obj;
	}
	
}
