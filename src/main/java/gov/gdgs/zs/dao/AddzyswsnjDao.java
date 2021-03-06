package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

//进入页面时最先显示的数据
@Repository
public class AddzyswsnjDao extends BaseJdbcDao implements IAddzyswsnjDao {
	@Resource
	private SPDao spDao;

	// 执业税务师年检
	public Map<String, Object> getZyswsnjb(int page, int pageSize, int Jgid,
			Map<String, Object> where) {

		Condition condition = new Condition();
		condition.add("a.ND", Condition.EQUAL, where.get("nd"));
		// condition.add("b.dwmc",Condition.FUZZY,where.get("dwmc"));
		// condition.add("a.ZTDM", Condition.EQUAL, where.get("ZTDM"));
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',t.*");
		sb.append(" from  ( select a.id,a.ND,c.XMING,b.dwmc,");
		sb.append(" CASE a.ZTDM WHEN 0 THEN '退回' WHEN 1 THEN '保存' WHEN 2 THEN '自检' WHEN 3 THEN '年检' ELSE NULL END AS ZTDM,");
		sb.append(" dm.mc as NJCL");
		/*
		sb.append(" CASE a.WGCL_DM WHEN a.ZTDM=0 THEN NULL WHEN a.ZTDM=1 THEN '年检报告待提交' WHEN a.ZTDM=2 THEN '等待年检' WHEN 1 THEN  '年检予以通过' WHEN 2 THEN '年检不予通过，责令2个月整改，整改期间不得对外行使注册税务师签字权，如整改期满仍达不到要求，注销执业证书'  ");
		sb.append(" WHEN 3 THEN '年检不予通过，不得继续执业，注销执业证书' ");
		sb.append(" WHEN 4 THEN '违反《注册税务师管理暂行办法》第二十五条、第二十六条所列行为2次以上处罚记录的，年检不予通过，注销执业证书'");
		sb.append(" WHEN 5 THEN '违反《注册税务师管理暂行办法》第四十二条、第四十四条所列行政处罚记录的，在年检公告时向社会公告'");
		sb.append(" WHEN 6 THEN '违反《注册税务师管理暂行办法》第四十五条所列行政处罚情节严重，注销执业证书并向社会公告'");
		sb.append(" WHEN 7 THEN '年检不予通过'");
		sb.append(" WHEN 8 THEN '资料填写有误，请重新填写' END AS NJCL");
		*/
		sb.append(" from " + Config.PROJECT_SCHEMA
				+ " (zs_zcswsnj a,zs_jg b,zs_ryjbxx c, zs_zysws d) left join dm_rywgcl dm ");
		sb.append(" on a.WGCL_DM = dm.id "); 
		sb.append("  " + condition.getSql() + " "); 
		sb.append(" and a.ZSJG_ID=? and a.ZSJG_ID=b.ID and a.SWS_ID=d.ID and c.ID=d.RY_ID order by a.ND desc) as t, ");
		sb.append(" (SELECT @rownum:=?) temp ");
		sb.append(" LIMIT ?, ? ");
		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(Jgid);
		params.add(pageSize * (page - 1));
		params.add(startIndex);
		params.add(pageSize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		// 获取符合条件的记录数

		int total = jdbcTemplate.queryForObject("SELECT FOUND_ROWS()",
				Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);

		return obj;
	}

	// 执业税务师姓名下拉选择
	public Map<String, Object> getZyswsxm(int page, int pageSize, int Jgid,
			Map<String, Object> where) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT zy.RY_ID,r.XMING,zy.id SWS_ID");
		sb.append(" FROM zs_zysws zy,zs_ryjbxx r");
		sb.append(" where 1=1 ");
		sb.append(" and zy.RY_ID=r.ID AND zy.YXBZ='1' AND r.YXBZ='1' AND zy.JG_ID=?");
		sb.append(" order by zy.id ");

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(Jgid);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		return obj;

	}
	
	//执业税务师年度校验（校验选择年度是否已做校验）

	// 前台点击"查看"时显示数据
	public Map<String, Object> getzyswsnjbById(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("	select max( a.nd),a.nd,a.ID,c.XMING,c.DHHM,d.ZYZSBH,d.ZYZGZSBH,a.sws_id,a.NJZJ ,");
		sb.append("	b.dwmc,a.swsfzryj,e1.MC as xb,f1.mc as xl,a.CZBL,DATE_FORMAT(a.SWSFZRSJ,'%Y-%m-%d') AS SWSFZRSJ,a.SWSFZR,");
		sb.append("	DATE_FORMAT(c.SRI,'%Y-%m-%d') AS SRI,c.SFZH,d.ZYZCRQ,ifnull(a.BAFS,0) as BAFS,a.ZJWGDM,a.NJWGDM,a.ZJ,a.SWSFZRYJ,");
		sb.append("	CASE a.ZTDM WHEN 1 THEN '保存'  WHEN 2 THEN '未审批' WHEN 0 THEN '退回' WHEN 3 THEN '已年检' ELSE NULL END AS njzt,");
		sb.append("	DATE_FORMAT( f.SPSJ,'%Y-%m-%d') AS spsj,f.SPYJ,f.SPRNAME");
		sb.append("	FROM  ( zs_zcswsnj a left join(zs_jg b,zs_ryjbxx c,zs_zysws d,dm_xb e1,dm_xl f1) on (");
		sb.append("	a.ZSJG_ID=b.ID AND c.id=d.RY_ID");
		sb.append("	and c.XL_DM=f1.id and c.XB_DM=e1.ID");
		sb.append("	AND d.id = a.sws_id ) ) left join(zs_spzx e,zs_spxx f,zs_splcbz g,zs_splc h) on (");
		sb.append("	f.SPID=e.ID AND g.ID=f.LCBZID and a.ZTDM <> 1");
		sb.append("	AND h.ID=g.LCID AND h.LCLXID='12'");
		sb.append("	AND a.ID=e.SJID )");
		sb.append("	WHERE  a.id=? group by nd");

		Map<String, Object> rs = this.jdbcTemplate.queryForMap(sb.toString(),
				new Object[] { id });
		// Map<String, Object> ob = new HashMap<>();
		// ob.put("data", rs);

		return rs;
	}

	// 姓名选择下拉框显示数据
	public Map<String, Object> getzyswsnjbBySwsId(String sws_id) {
		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT xb.MC as xb,r.SRI,xl.MC as xl,r.SFZH,jg.DWMC,r.DHHM,z.ZYZSBH,r.XPIAN,z.ZYZCRQ,z.ZYZGZSBH,round(if(ifnull(jg.ZCZJ,0)=0,0,ifnull(z.CZE,0)/ifnull(jg.ZCZJ,0))*100,2) as czbl ");
		sb.append(" FROM zs_zysws z,zs_ryjbxx r,zs_jg jg,dm_xb xb,dm_xl xl ");
		sb.append(" WHERE z.RY_ID=r.ID AND z.JG_ID=jg.ID AND z.ID=? AND r.XB_DM=xb.ID and r.XL_DM=xl.ID ");

		Map<String, Object> rs = this.jdbcTemplate.queryForMap(sb.toString(),
				new Object[] { sws_id });
		// Map<String, Object> ob = new HashMap<>();
		// ob.put("data", rs);

		return rs;
	}

	//
	public Map<String, Object> getzyswsnjBafs(String nd, String sws_id) {
		StringBuffer sb = new StringBuffer();

		sb.append(" select count(b.ID) bndbafs  ");
		sb.append(" from zs_zysws z, zs_ywbb b  ");
		sb.append(" 		  where (locate(concat(',', z.ry_id, ','), b.qmswsid) > 0 or  ");
		sb.append(" 		        locate(concat(z.ry_id, ','), b.qmswsid) = 1 or  ");
		sb.append(" 		        (locate(concat(',', z.ry_id), b.qmswsid) > 0 and  ");
		sb.append(" 		        locate(concat(',', z.ry_id), b.qmswsid) =  ");
		sb.append(" 		        length(b.qmswsid) - length(z.ry_id)))  ");
		sb.append(" 		    and year(b.BBRQ) = ?  ");
		sb.append(" 		    and z.ID=? ");

		Map<String, Object> rs = this.jdbcTemplate.queryForMap(sb.toString(),
				new Object[] { nd, sws_id });
		// Map<String, Object> ob = new HashMap<>();
		// ob.put("data", rs);

		return rs;
	}

	@Override
	public String addZyswsnjb(Map<String, Object> obj) throws Exception {
		Integer jgid= (Integer) obj.get("jg_id");


		// String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
		// String xz =this.jdbcTemplate.queryForObject(sql,new
		// Object[]{obj.get("jg_id")},String.class);
		// obj.put("xz", xz);
		final StringBuffer sb = new StringBuffer("insert into "
				+ Config.PROJECT_SCHEMA + "zs_zcswsnj");
		sb.append(" (ZSJG_ID,ND,SWS_ID,ZJWGDM,ZJ,SWSFZRYJ,SWSFZRSJ,SWSFZR,ZDSJ,ZTDM,CZBL,BAFS) "
				+ "VALUES (:jg_id,:ND,:sws_id,:wg,:ZJ,:SWSFZRYJ,:SWSFZRSJ,:SWSFZR,now(),:ztdm,:czbl,:bndbafs) ");
		Number njid = this.insertAndGetKeyByNamedJdbc(sb.toString(), obj, new String[]{"id"});
		if(((Integer)obj.get("ztdm"))!=1){
			Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
			spsq.put("sid", njid);
			if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{jgid}).size()==0){
				spsq.put("lclx", "40288087233c611801234b748bac01bb");
			}else{
				spsq.put("lclx", "40288087233c611801234b758fed01be");
			}
			
			spsq.put("jgid", jgid);
			spDao.swsSPqq(spsq);//生成审批表记录
			}
		// 更新执业税务师年检表
		return njid+"";
	}
	
	@Override
	public void updateZyswsnjb(Map<String, Object> obj) throws Exception {
		Integer njid=(Integer) obj.get("id");
		Integer jgid=(Integer) obj.get("jg_id");
		// String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
		// String xz =this.jdbcTemplate.queryForObject(sql,new
		// Object[]{obj.get("jg_id")},String.class);
		// obj.put("xz", xz);
//		String sql = "select max(a.ID)+1 from zs_zcswsnj a";
//		String id1 = this.jdbcTemplate.queryForObject(sql, String.class);
//		obj.put("id1", id1);
		StringBuffer sb = new StringBuffer("update " + Config.PROJECT_SCHEMA
				+ "zs_zcswsnj ");

		sb.append(" set ZSJG_ID=:jg_id,ND=:nd,SWS_ID=:sws_id,ZJWGDM=:wg,ZJ=:ZJ,SWSFZRYJ=:SWSFZRYJ,SWSFZRSJ=:SWSFZRSJ,SWSFZR=:SWSFZR,ZDSJ=(date_format(now(),'%Y.%m.%d %h:%i:%s')),ZTDM=:ztdm where id=:id ");

		this.namedParameterJdbcTemplate.update(sb.toString(), obj);
		if(((Integer)obj.get("ztdm"))==2){
			Map<String,Object> spsq=new HashMap<>();//设置生成审批表方法参数
			spsq.put("sid", njid);
			if(this.jdbcTemplate.queryForList("select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",new Object[]{jgid}).size()==0){
				spsq.put("lclx", "40288087233c611801234b748bac01bb");
			}else{
				spsq.put("lclx", "40288087233c611801234b758fed01be");
			}
			
			spsq.put("jgid", jgid);
			spDao.swsSPqq(spsq);//生成审批表记录
			}
	}

	

	public Map<String, Object> checkzyswsnjnd(Integer jgId,
			Map<String, Object> where) {
		
		Condition condition = new Condition();
		condition.add("a.nd", "FUZZY", where.get("nd"));
		condition.add("a.sws_id","FUZZY",where.get("sws_id"));
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.nd FROM zs_zcswsnj a,zs_jg b,zs_ryjbxx c, zs_zysws d ");
		sb.append(condition.getSql());
		sb.append(" and a.ZSJG_ID=? ");
		sb.append(" AND a.ZSJG_ID=b.ID AND a.SWS_ID=d.ID AND c.ID=d.RY_ID ");
		sb.append("ORDER BY a.ND DESC");
		
		
		// 装嵌传值数组

		ArrayList<Object> params = condition.getParams();

		params.add(jgId);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());
		

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
	    obj.put("jg_id", jgId);
		return obj;

	}
	
}
