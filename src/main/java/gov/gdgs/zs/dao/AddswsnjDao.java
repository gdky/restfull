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
public class AddswsnjDao extends BaseJdbcDao implements IAddswsnjDao{
	
	public Map<String,Object> getswsnjb(int page, int pageSize,int Jgid,
			Map<String, Object> where) {
		Condition condition = new Condition();
		condition.add("c.dwmc", Condition.FUZZY, where.get("dwmc"));
		condition.add("c.cs_dm", Condition.EQUAL, where.get("cs"));
		condition.add("a.nd", Condition.EQUAL, where.get("nd"));
		condition.add("a.ztdm", Condition.EQUAL, where.get("bbzt"));
		condition.add("a.zjrq", Condition.GREATER_EQUAL, where.get("sbsj"));
		condition.add("a.zjrq", Condition.LESS_EQUAL, where.get("sbsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		select  SQL_CALC_FOUND_ROWS  @rownum:=@rownum+1 AS 'key',v.* from ( SELECT ");
		sb.append("		c.dwmc,c.JGZCH as zsbh,d.mc as jgxz,c.yzbm,c.DZHI as bgdz,c.DHUA as dhhm,a.*,");
		sb.append("		case a.ztdm when 3 then '已年检' when 2 then '已自检'  "
				+ " else null end as njzt, CASE a.WGCL_DM WHEN 1 THEN '年检予以通过' WHEN 2 THEN '年检不予通过，"
				+ "责令2个月整改' WHEN 6 THEN '年检不予以通过' WHEN 7 THEN '资料填写有误，请重新填写' ELSE NULL END AS njcl,"
				+ "DATE_FORMAT(a.zjsj,'%Y-%m-%d') AS zjrq,DATE_FORMAT(c.SWSZSCLSJ ,'%Y-%m-%d') AS clsj,"
				+ "DATE_FORMAT(a.fzrsj,'%Y-%m-%d') AS qzrq");
		sb.append("	 FROM  zs_jg_njb a,zs_jg c,dm_jgxz d");
		sb.append("		"+condition.getSql()+" ");
		sb.append("	and a.ZSJG_ID=c.ID and a.ZSJG_ID=? and a.ztdm in (2,3)  and d.ID = c.JGXZ_DM");
		sb.append("	group by a.zsjg_id,nd order by a.ND desc");
		sb.append("	 ) as v ,(SELECT @rownum:=?) zs_jg");
		sb.append("		LIMIT ? ,?");
		
		
		// 装嵌传值数组
				int startIndex = pageSize * (page - 1);
				ArrayList<Object> params = condition.getParams();
				
				params.add(0,Jgid);
				params.add( pageSize * (page - 1));
		        params.add(startIndex);
				params.add(pageSize);
				

				// 获取符合条件的记录
				List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
						params.toArray());
		
				int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);
		obj.put("jg_id", Jgid);
		return obj;
		
		
}
	public Map<String, Object> getswsnjbById(String id) {
		String sql="select c.dwmc,c.JGZCH as zsbh,d.mc as jgxz,c.yzbm,c.DZHI as bgdz,c.DHUA as dhhm,a.*,"
				+ "case a.ztdm when 3 then '已年检' when 2 then '已自检'else null end as njzt,"
				+ " CASE a.WGCL_DM WHEN 1 THEN '年检予以通过' "
				+ "WHEN 2 THEN '年检不予通过，责令2个月整改' "
				+ "WHEN 6 THEN '年检不予以通过' "
				+ "WHEN 7 THEN '资料填写有误，请重新填写' ELSE NULL END AS njcl,"
				+ "DATE_FORMAT(a.zjsj,'%Y-%m-%d') AS zjrq,"
				+ "DATE_FORMAT(c.SWSZSCLSJ ,'%Y-%m-%d') AS clsj,"
				+ "DATE_FORMAT(a.fzrsj,'%Y-%m-%d') AS qzrq "
				+ "FROM  zs_jg_njb a,zs_jg c,dm_jgxz d "
				+ "where a.ztdm in (2,3)  and d.ID = c.JGXZ_DM and a.ZSJG_ID=c.ID and a.id=?";
		Map<String,Object> rs = jdbcTemplate.queryForMap(sql, id);
		return rs;
	}
	
	@Override
	public String addSwsnjb( Map <String,Object> obj){
		String uuid = UUID.randomUUID().toString().replace("-", "");
		obj.put("id", uuid);
		final StringBuffer sb = new StringBuffer("insert into "
				+ Config.PROJECT_SCHEMA + "zs_jg_njb");
		sb.append(" ( ND, NJZJ, SZ, ZCZJ, ZRS, ZYRS, YJYRS, SJJYRS, WJYRS,  ZJ, FZR, ZCSWSBZJ, ZCSWSBJS,BAFS, FSS,ZDSJ,ztdm) "
				+ "VALUES (:nd,:NJZJ, :sz, :zczj, :zrs, :zyrs, :yjyrs, :sjjyrs, :wjyrs, :ZJ, :FZR, :ZCSWSBZJ,:ZCSWSBJS,:BAFS,:FSS,now(),'1') ");
		NamedParameterJdbcTemplate named=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		int count=named.update(sb.toString(), obj);
		if(count==0){
		return null;
	}else {
		return uuid;
	}
		//更新事务所年检表
	}
	@Override
	public void updateSwsnjb(Map <String,Object> obj) {
		StringBuffer sb = new StringBuffer("update "
				+ Config.PROJECT_SCHEMA + "zs_cwbb_zcmx ");
		sb.append(" set jg_id=:jg_id,use_id=:use_id,ztbj=:ztbj,kssj=:kssj,jssj=:jssj,tjrq=sysdate(),nd=:nd,zyywcb1=:zyywcb1,zyywcb=:zyywcb,zyywsjfj1=:zyywsjfj1,zyywsjfj=:zyywsjfj,");		
		sb.append(" gzfy1=:gzfy1,gzfy=:gzfy,qtywzc1=:qtywzc1,qtywzc=:qtywzc,flf1=:flf1,flf=:flf,glfy1=:glfy1,glfy=:glfy,jyf1=:jyf1,jyf=:jyf,glfy_gzfy1=:glfy_gzfy1,");
		sb.append(" glfy_gzfy=:glfy_gzfy,ghjf1=:ghjf1,ghjf=:ghjf,glfy_flf1=:glfy_flf1,glfy_flf=:glfy_flf,shtc1=:shtc1,shtc=:shtc,glfy_ywzdf1=:glfy_ywzdf1,glfy_ywzdf=:glfy_ywzdf,");
		sb.append(" bgf1=:bgf1,bgf=:bgf,glfy_bgf1=:glfy_bgf1,glfy_bgf=:glfy_bgf,clf1=:clf1,clf=:clf,glfy_qtsj1=:glfy_qtsj1,glfy_qtsj=:glfy_qtsj,hf1=:hf1,hf=:hf,");
		sb.append(" glfy_qcfy1=:glfy_qcfy1,glfy_qcfy=:glfy_qcfy,pxzlf1=:pxzlf1,pxzlf=:pxzlf,glfy_zyfxjj1=:glfy_zyfxjj1,glfy_zyfxjj=:glfy_zyfxjj,");
		sb.append(" hwf1=:hwf1,hwf=:hwf,glfy_zyzrbx1=:glfy_zyzrbx1,glfy_zyzrbx=:glfy_zyzrbx,zpf1=:zpf1,zpf=:zpf,glfy_clf1=:glfy_clf1,glfy_clf=:glfy_clf,zj1=:zj1,zj=:zj,");
		sb.append(" glfy_qtfy1=:glfy_qtfy1,glfy_qtfy=:glfy_qtfy,zfgjj1=:zfgjj1,zfgjj=:zfgjj,cwfy1=:cwfy1,cwfy=:cwfy,gwzxf1=:gwzxf1,gwzxf=:gwzxf,");
		sb.append(" yywzc1=:yywzc1,yywzc=:yywzc,qt1=:qt1,qt=:qt,zczj1=:zczj1,zczj=:zczj,sz=:sz,agkj=:agkj,zb=:zb where id=:id");
		NamedParameterJdbcTemplate named=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		named.update(sb.toString(), obj);
		
	}
	
	
	
}
