package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class AddzyswsnjDao extends BaseJdbcDao implements IAddzyswsnjDao{

	//执业税务师年检
    public Map<String, Object> getZyswsnjb(int page, int pageSize,int Jgid,
			Map<String, Object> where) {
			
			Condition condition = new Condition();
//			condition.add("a.nd", Condition.EQUAL, where.get("nd"));
//			condition.add("b.dwmc",Condition.FUZZY,where.get("dwmc"));
//			condition.add("a.ZTDM", Condition.EQUAL, where.get("ZTDM"));
            

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT  SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',t.*");
			sb.append(" from  ( select a.id,a.ND,c.XMING,b.dwmc,");
			sb.append(" CASE a.ZTDM WHEN 0 THEN '退回' WHEN 1 THEN '保存' WHEN 2 THEN '自检' WHEN 3 THEN '年检' ELSE NULL END AS ZTDM");
			sb.append(" from "+Config.PROJECT_SCHEMA+"zs_zcswsnj a,zs_jg b,zs_ryjbxx c, zs_zysws d,(SELECT @rownum:=?) temp");
//			sb.append(condition.getSql());//相当元 where x.xx like '%%'
			sb.append(" where a.ZSJG_ID=? and a.ZSJG_ID=b.ID and a.SWS_ID=d.ID and c.ID=d.RY_ID order by a.ND desc) as t");
			sb.append("    LIMIT ?, ? ");
			// 装嵌传值数组
			int startIndex = pageSize * (page - 1);
			ArrayList<Object> params = condition.getParams();
			params.add(pageSize * (page - 1));
			params.add(Jgid);
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
    
   /* public Map<String, Object> getzyswsnjbById(String id) {
		String sql = "select a.id,a.ND,c.XMING,"
				+ "b.dwmc, CASE a.ZTDM WHEN 0 THEN '退回' "
				+ "WHEN 1 THEN '保存' "
				+ "WHEN 2 THEN '自检' "
				+ " WHEN 3 THEN '年检' "
				+ " ELSE NULL END AS ZTDM "
				+ "  from zs_zcswsnj a,zs_jg b,zs_ryjbxx c, zs_zysws  d "
				+ "  where a.SWS_ID=d.ID and c.ID=d.RY_ID and a.ZSJG_ID=b.ID and a.id=? ";
		Map<String,Object> rs = jdbcTemplate.queryForMap(sql, id);
		return rs;
	}*/
    
    public Map<String,Object> getzyswsnjbById(String id){
		StringBuffer sb = new StringBuffer();
		sb.append("	select max( a.nd),a.nd,a.ID,c.XMING,c.DHHM,d.ZYZSBH,d.ZYZGZSBH,");
		sb.append("	b.dwmc,a.swsfzryj,e1.MC as xb,f1.mc as xl,a.CZBL,DATE_FORMAT(a.SWSFZRSJ,'%Y-%m-%d') AS SWSFZRSJ,a.SWSFZR,");
		sb.append("	DATE_FORMAT(c.SRI,'%Y-%m-%d') AS SRI,c.SFZH,a.BAFS,a.ZJWGDM,a.NJWGDM,a.ZJ,a.SWSFZRYJ,");
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
		 Map<String,Object> rs = this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{id});
		// Map<String, Object> ob = new HashMap<>();
			//ob.put("data", rs);
		 
		 return rs;
	}
    @Override
	public String addZyswsnjb( Map <String,Object> obj){
	String uuid = UUID.randomUUID().toString().replace("-", "");
	obj.put("id", uuid);
		String sql="select max(a.ID)+1 from zs_zcswsnj a";
		String id1=this.jdbcTemplate.queryForObject(sql,String.class);
	    obj.put("id1", id1);
	    
//		String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
//		String xz =this.jdbcTemplate.queryForObject(sql,new Object[]{obj.get("jg_id")},String.class);
//		obj.put("xz", xz);
		final StringBuffer sb = new StringBuffer("insert into "
				+ Config.PROJECT_SCHEMA + "zs_zcswsnj");
		sb.append(" (ZSJG_ID,ID,ND,SWS_ID,ZJWGDM,NJZJ,SWSFZRYJ,SWSFZRSJ,SWSFZR,ZDSJ,ZTDM) "
				+ "VALUES (:jg_id,:id1,:ND,:sws_id,:wg,:ZJ,:SWSFZRYJ,:SWSFZRSJ,:SWSFZR,now(),'2' ) ");
		NamedParameterJdbcTemplate named=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		int count=named.update(sb.toString(), obj);
		
		if(count==0){
		return null;
	}else {
		return uuid;
	}
		//更新执业税务师年检表
	}
    @Override
	public void updateZyswsnjb(Map <String,Object> obj) {
//		String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
//		String xz =this.jdbcTemplate.queryForObject(sql,new Object[]{obj.get("jg_id")},String.class);
//		obj.put("xz", xz);
    	String sql="select max(a.ID)+1 from zs_zcswsnj a";
		String id1=this.jdbcTemplate.queryForObject(sql,String.class);
	    obj.put("id1", id1);
		StringBuffer sb = new StringBuffer("update "
				+ Config.PROJECT_SCHEMA + "zs_zcswsnj ");
		
		sb.append(" set ZSJG_ID=:jg_id,ID=:id1,ND=:nd,SWS_ID=:sws_id,ZJWGDM=:wg,NJZJ=:ZJ,SWSFZRYJ=:SWSFZRYJ,SWSFZRSJ=:SWSFZRSJ,SWSFZR=:SWSFZR,ZDSJ=(date_format(now(),'%Y.%m.%d %h:%i:%s')),ZTDM='2' where sws_id=:sws_id ");
		
		NamedParameterJdbcTemplate named=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		named.update(sb.toString(), obj);
		
	}  
}
