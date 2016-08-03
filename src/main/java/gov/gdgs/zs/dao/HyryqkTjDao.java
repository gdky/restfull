package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.google.common.base.Objects;

@Repository
public class HyryqkTjDao extends BaseDao {
	
	public Map<String,Object> getHyryqkTj(HashMap<String, Object> map){
		Map<String, Object> rs=new HashMap<String, Object>();
		List<Map<String,Object>> hyryqkTj=new ArrayList<Map<String,Object>>();
		StringBuffer sql=new StringBuffer(" select '1:执业税务师' project, ");
		sql.append("        'zysws' xmmc, ");
		sql.append("        'xttjbb/hyryqktj/hyryqktjMx?type=zysws' _link, ");
		sql.append("        count(distinct zy.id) zj,  ");
		sql.append("        sum(case when jb.xb_dm = '1' then 1 else 0 end) male,  ");
		sql.append("        sum(case when jb.xb_dm = '2' then 1 else 0 end) female,  ");
		sql.append("        sum(case when jb.xl_dm = '5' then 1 else 0 end) dr, ");
		sql.append("        sum(case when jb.xl_dm = '4' then 1 else 0 end) postgraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '1' then 1 else 0 end) undergraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '2' then 1 else 0 end) juniorCollege,  ");
		sql.append("        sum(case when jb.xl_dm = '3' then 1 else 0 end) highSchool,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) below35, ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) years36_50,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) years51_60,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) over60  ");
		sql.append("   from zs_zysws zy ");
		sql.append("   left join zs_ryjbxx jb ");
		sql.append("     on zy.ry_id = jb.id ");
		sql.append("  where zy.yxbz = '1' ");
		sql.append("    and jb.yxbz = '1' ");
		sql.append(" union all ");
		sql.append(" select '其中：合伙人' project, ");
		sql.append("        'hhr' xmmc, ");
		sql.append("        'xttjbb/hyryqktj/hyryqktjMx?type=hhr' _link, ");
		sql.append("        count(distinct zy.id) zj,   ");
		sql.append("        sum(case when jb.xb_dm = '1' then 1 else 0 end) male,  ");
		sql.append("        sum(case when jb.xb_dm = '2' then 1 else 0 end) female,  ");
		sql.append("        sum(case when jb.xl_dm = '5' then 1 else 0 end) dr, ");
		sql.append("        sum(case when jb.xl_dm = '4' then 1 else 0 end) postgraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '1' then 1 else 0 end) undergraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '2' then 1 else 0 end) juniorCollege,  ");
		sql.append("        sum(case when jb.xl_dm = '3' then 1 else 0 end) highSchool,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) below35, ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) years36_50,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) years51_60,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) over60  ");
		sql.append("   from zs_zysws zy ");
		sql.append("   left join zs_ryjbxx jb ");
		sql.append("     on zy.ry_id = jb.id ");
		sql.append("  where zy.yxbz = '1' ");
		sql.append("    and jb.yxbz = '1' ");
		sql.append("    and zy.fqr_dm = '1' ");
		sql.append("  group by zy.fqr_dm ");
		sql.append(" union all ");
		sql.append(" select '其中：出资人' project, ");
		sql.append("        'czr' xmmc, ");
		sql.append("        'xttjbb/hyryqktj/hyryqktjMx?type=czr' _link, ");
		sql.append("        count(distinct zy.id) zj,   ");
		sql.append("        sum(case when jb.xb_dm = '1' then 1 else 0 end) male,  ");
		sql.append("        sum(case when jb.xb_dm = '2' then 1 else 0 end) female,  ");
		sql.append("        sum(case when jb.xl_dm = '5' then 1 else 0 end) dr, ");
		sql.append("        sum(case when jb.xl_dm = '4' then 1 else 0 end) postgraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '1' then 1 else 0 end) undergraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '2' then 1 else 0 end) juniorCollege,  ");
		sql.append("        sum(case when jb.xl_dm = '3' then 1 else 0 end) highSchool,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) below35, ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) years36_50,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) years51_60,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) over60  ");
		sql.append("   from zs_zysws zy ");
		sql.append("   left join zs_ryjbxx jb ");
		sql.append("     on zy.ry_id = jb.id ");
		sql.append("  where zy.yxbz = '1' ");
		sql.append("    and jb.yxbz = '1' ");
		sql.append("    and zy.czr_dm = '1' ");
		sql.append("  group by zy.czr_dm ");
		sql.append(" union all ");
		sql.append(" select '2:具有其它中介执业资格的从业人员' project, ");
		sql.append("        'qt' xmmc, ");
		sql.append("        'xttjbb/hyryqktj/hyryqktjMx?type=qt' _link, ");
		sql.append("        count(distinct zy.id) zj,   ");
		sql.append("        ifnull(sum(case when jb.xb_dm = '1' then 1 else 0 end), 0) male,  ");
		sql.append("        ifnull(sum(case when jb.xb_dm = '2' then 1 else 0 end), 0) female,  ");
		sql.append("        ifnull(sum(case when jb.xl_dm = '5' then 1 else 0 end), 0) dr, ");
		sql.append("        ifnull(sum(case when jb.xl_dm = '4' then 1 else 0 end), 0) postgraduate,   ");
		sql.append("        ifnull(sum(case when jb.xl_dm = '1' then 1 else 0 end), 0) undergraduate,   ");
		sql.append("        ifnull(sum(case when jb.xl_dm = '2' then 1 else 0 end), 0) juniorCollege,  ");
		sql.append("        ifnull(sum(case when jb.xl_dm = '3' then 1 else 0 end), 0) highSchool,   ");
		sql.append("        ifnull(sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end), 0) below35, ");
		sql.append("        ifnull(sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end), 0) years36_50,  ");
		sql.append("        ifnull(sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end), 0) years51_60,  ");
		sql.append("        ifnull(sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end), 0) over60  ");
		sql.append("   from zs_qtry zy ");
		sql.append("   left join zs_ryjbxx jb ");
		sql.append("     on zy.ry_id = jb.id ");
		sql.append("  where zy.qtryzt_dm = '1' ");
		sql.append("    and jb.yxbz = '1' ");
		sql.append(" union all ");
		sql.append(" select '3:从业人员' project, ");
		sql.append("        'cyry' xmmc, ");
		sql.append("        'xttjbb/hyryqktj/hyryqktjMx?type=cyry' _link, ");
		sql.append("        count(distinct zy.id) zj,   ");
		sql.append("        sum(case when jb.xb_dm = '1' then 1 else 0 end) male,  ");
		sql.append("        sum(case when jb.xb_dm = '2' then 1 else 0 end) female,  ");
		sql.append("        sum(case when jb.xl_dm = '5' then 1 else 0 end) dr, ");
		sql.append("        sum(case when jb.xl_dm = '4' then 1 else 0 end) postgraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '1' then 1 else 0 end) undergraduate,   ");
		sql.append("        sum(case when jb.xl_dm = '2' then 1 else 0 end) juniorCollege,  ");
		sql.append("        sum(case when jb.xl_dm = '3' then 1 else 0 end) highSchool,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) <= 35 then 1 else 0 end) below35, ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 then 1 else 0 end) years36_50,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 then 1 else 0 end) years51_60,  ");
		sql.append("        sum(case when (year(now()) - year(sri) - 1) > 60 then 1 else 0 end) over60  ");
		sql.append("   from zs_cyry zy ");
		sql.append("   left join zs_ryjbxx jb ");
		sql.append("     on zy.ry_id = jb.id ");
		sql.append("  where zy.yxbz = '1' ");
		sql.append("    and jb.yxbz = '1' ");
		hyryqkTj=this.jdbcTemplate.queryForList(sql.toString());
		rs.put("data", hyryqkTj);
		return rs;
	}
	
	public Map<String, Object> getHyryqkTjMx(String type, String lx,
			HashMap<String, Object> map) {
		Map<String, Object> rs=new HashMap<String, Object>();
		List<Map<String,Object>> hyryqkTjMxs=new ArrayList<Map<String,Object>>();
		String xm=(String) map.get("xm");
		StringBuffer sql=new StringBuffer("");
		String lxCondition="";
		switch(type){
		case "zysws":
			sql.append(" SELECT jb.XMING AS xm, ");
			sql.append("        jb.SRI AS birthday, ");
			sql.append(" 		 zy.ZYZCRQ AS zcrq, ");
			sql.append(" 		 xb.MC AS sex, ");
			sql.append(" 		 xl.MC as education, ");
			sql.append(" 		 zy.ZYZGZSBH, ");
			sql.append(" 		 zy.ZYZSBH ");
			sql.append(" FROM zs_zysws zy ");
			sql.append(" LEFT JOIN zs_ryjbxx jb ON zy.ry_id = jb.id,dm_xb xb,dm_xl xl ");
			sql.append(" WHERE jb.XB_DM=xb.ID  ");
			sql.append("   and jb.XL_DM=xl.ID  ");
			sql.append("   AND zy.yxbz = '1'  ");
			sql.append("   AND jb.yxbz = '1' ");
			break;
		case "hhr":
			sql.append(" SELECT jb.XMING AS xm, ");
			sql.append("        jb.SRI AS birthday, ");
			sql.append(" 		 zy.ZYZCRQ AS zcrq, ");
			sql.append(" 		 xb.MC AS sex, ");
			sql.append(" 		 xl.MC as education, ");
			sql.append(" 		 zy.ZYZGZSBH, ");
			sql.append(" 		 zy.ZYZSBH ");
			sql.append(" FROM zs_zysws zy ");
			sql.append(" LEFT JOIN zs_ryjbxx jb ON zy.ry_id = jb.id,dm_xb xb,dm_xl xl ");
			sql.append(" WHERE jb.XB_DM=xb.ID  ");
			sql.append("   and jb.XL_DM=xl.ID  ");
			sql.append("   AND zy.yxbz = '1'  ");
			sql.append("   AND jb.yxbz = '1' ");
			sql.append("   and zy.fqr_dm = '1' ");
			break;
		case "czr":
			sql.append(" SELECT jb.XMING AS xm, ");
			sql.append("        jb.SRI AS birthday, ");
			sql.append(" 		 zy.ZYZCRQ AS zcrq, ");
			sql.append(" 		 xb.MC AS sex, ");
			sql.append(" 		 xl.MC as education, ");
			sql.append(" 		 zy.ZYZGZSBH, ");
			sql.append(" 		 zy.ZYZSBH ");
			sql.append(" FROM zs_zysws zy ");
			sql.append(" LEFT JOIN zs_ryjbxx jb ON zy.ry_id = jb.id,dm_xb xb,dm_xl xl ");
			sql.append(" WHERE jb.XB_DM=xb.ID  ");
			sql.append("   and jb.XL_DM=xl.ID  ");
			sql.append("   AND zy.yxbz = '1'  ");
			sql.append("   AND jb.yxbz = '1' ");
			sql.append("   and zy.czr_dm = '1' ");
			break;
		case "qt":
			sql.append(" SELECT jb.XMING AS xm, ");
			sql.append("        jb.SRI AS birthday, ");
			sql.append(" 		 zy.ZYZCRQ AS zcrq, ");
			sql.append(" 		 xb.MC AS sex, ");
			sql.append(" 		 xl.MC as education, ");
			sql.append(" 		 zy.ZYZGZSBH, ");
			sql.append(" 		 zy.ZYZSBH ");
			sql.append(" FROM zs_qtry qt ");
			sql.append(" left join zs_zysws zy on qt.RY_ID=zy.RY_ID ");
			sql.append(" LEFT JOIN zs_ryjbxx jb ON qt.ry_id = jb.id,dm_xb xb,dm_xl xl ");
			sql.append(" WHERE jb.XB_DM=xb.ID  ");
			sql.append("   and jb.XL_DM=xl.ID  ");
			sql.append("   AND qt.qtryzt_dm = '1' ");
			sql.append("   and jb.yxbz = '1' ");
			break;
		case "cyry":
			sql.append(" SELECT jb.XMING AS xm, ");
			sql.append("        jb.SRI AS birthday, ");
			sql.append(" 		 zy.ZYZCRQ AS zcrq, ");
			sql.append(" 		 xb.MC AS sex, ");
			sql.append(" 		 xl.MC as education, ");
			sql.append(" 		 zy.ZYZGZSBH, ");
			sql.append(" 		 zy.ZYZSBH ");
			sql.append(" FROM zs_cyry cy ");
			sql.append(" left join zs_zysws zy on cy.ID=zy.ID ");
			sql.append(" LEFT JOIN zs_ryjbxx jb ON cy.ry_id = jb.id,dm_xb xb,dm_xl xl ");
			sql.append(" WHERE jb.XB_DM=xb.ID  ");
			sql.append("   and jb.XL_DM=xl.ID  ");
			sql.append("   AND cy.yxbz = '1' ");
			sql.append("   and jb.yxbz = '1' ");
			break;
		}
		switch(lx){
		case "zj":
			break;
		case "male":
			lxCondition=" and jb.xb_dm = '1' ";
			break;
		case "female":
			lxCondition=" and jb.xb_dm = '2' ";
			break;
		case "dr":
			lxCondition=" and jb.xl_dm = '5' ";
			break;
		case "postgraduate":
			lxCondition=" and jb.xl_dm = '4' ";
			break;
		case "undergraduate":
			lxCondition=" and jb.xl_dm = '1' ";
			break;
		case "juniorCollege":
			lxCondition=" and jb.xl_dm = '2' ";
			break;
		case "highSchool":
			lxCondition=" and jb.xl_dm = '3' ";
			break;
		case "below35":
			lxCondition=" and (year(now()) - year(sri) - 1) <= 35";
			break;
		case "years36_50":
			lxCondition=" and (year(now()) - year(sri) - 1) >= 36 and (year(now()) - year(sri) - 1) <= 50 ";
			break;
		case "years51_60":
			lxCondition=" and (year(now()) - year(sri) - 1) >= 51 and (year(now()) - year(sri) - 1) <= 60 ";
			break;
		case "over60":
			lxCondition=" and (year(now()) - year(sri) - 1) > 60 ";
			break;
		}
		if(lx!=null&&!StringUtils.isEmpty(lx)){
			sql.append(lxCondition);
		}
		if(type!=null&&!StringUtils.isEmpty(type)){
			if (!Objects.equal(xm, "") && !Objects.equal(xm, null)) {
				sql.append(" and jb.xming like '%"+xm+"%'");
			}
			hyryqkTjMxs=this.jdbcTemplate.queryForList(sql.toString());			
		}
		rs.put("data", hyryqkTjMxs);
		return rs;
	}

}
