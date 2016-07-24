package gov.gdgs.zs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class HyryqkTjDao extends BaseDao {

	public Map<String, Object> getHyryqkTjMx(String type, String lx,
			HashMap<String, Object> map) {
		Map<String, Object> rs=new HashMap<String, Object>();
		List<Map<String,Object>> hyryqkTjMxs=new ArrayList<Map<String,Object>>();
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
		if(type!=null&&!StringUtils.isEmpty(type)){
			sql.append(lxCondition);
			hyryqkTjMxs=this.jdbcTemplate.queryForList(sql.toString());
		}
		rs.put("data", hyryqkTjMxs);
		return rs;
	}

}
