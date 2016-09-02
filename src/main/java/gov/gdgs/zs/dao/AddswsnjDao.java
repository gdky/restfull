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
		//condition.add("c.dwmc", Condition.FUZZY, where.get("dwmc"));
		//condition.add("c.cs_dm", Condition.EQUAL, where.get("cs"));
		condition.add("a.nd", "FUZZY", where.get("nd"));
		//condition.add("a.ztdm", Condition.EQUAL, where.get("bbzt"));
		//condition.add("a.zjrq", Condition.GREATER_EQUAL, where.get("sbsj"));
		//condition.add("a.zjrq", Condition.LESS_EQUAL, where.get("sbsj2"));
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
		sb.append("	and a.ZSJG_ID=c.ID and a.ZSJG_ID=? and a.ztdm in (1,2,3)  and d.ID = c.JGXZ_DM");
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
				+ "where 1=1 and d.ID = c.JGXZ_DM and a.ZSJG_ID=c.ID and a.id=?";
		Map<String,Object> rs = jdbcTemplate.queryForMap(sql, id);
		return rs;
	}
	
	@Override
	public String addSwsnjb( Map <String,Object> obj){
		String uuid = UUID.randomUUID().toString().replace("-", "");
		obj.put("id", uuid);
		String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
		String xz =this.jdbcTemplate.queryForObject(sql,new Object[]{obj.get("jg_id")},String.class);
		obj.put("xz", xz);
		final StringBuffer sb = new StringBuffer("insert into "
				+ Config.PROJECT_SCHEMA + "zs_jg_njb");
		sb.append(" ( ZSJG_ID, ND,ZSJGXZ_ID,ZJWGDM, NJZJ, SZ, ZCZJ, ZRS, ZYRS, YJYRS, SJJYRS, WJYRS,  ZJ, FZR, ZCSWSBZJ, ZCSWSBJS,BAFS, FSS,ZDSJ,ZJSJ,ztdm) "
				+ "VALUES (:jg_id,:nd,:xz,:wg,:NJZJ, :sz, :zczj, :zrs, :zyrs, :yjyrs, :sjjyrs, :wjyrs, :ZJ, :FZR, :ZCSWSBZJ,:ZCSWSBJS,:BAFS,:FSS,now(),now(),'1') ");
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
		String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
		String xz =this.jdbcTemplate.queryForObject(sql,new Object[]{obj.get("jg_id")},String.class);
		obj.put("xz", xz);
		StringBuffer sb = new StringBuffer("update "
				+ Config.PROJECT_SCHEMA + "zs_jg_njb ");
		
		sb.append(" set ZSJG_ID=:jg_id,ZSJGXZ_ID=:xz,ND =:nd,ZJWGDM=:wg,NJZJ=:NJZJ,GDBDQKZJ=:GDBDQKZJ,"
				+ "GDBDQKJS=:GDBDQKJS,ZRS=:ZRS,ZYRS=:zyrs,YJYRS=:yjyrs,SJJYRS=:sjjyrs, "
				+ "WJYRS=:wjyrs,ZJ=:ZJ,FZR=:FZR,ZCSWSBZJ=:ZCSWSBZJ, "
				+ "ZCSWSBJS=:ZCSWSBJS,FSS=:FSS,"
				+ "ztdm='2'where id=:id ");
		
		NamedParameterJdbcTemplate named=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		named.update(sb.toString(), obj);
		
	}
	
	
	
}
