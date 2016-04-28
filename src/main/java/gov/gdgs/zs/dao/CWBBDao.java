package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Pager;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;
@Repository
public class CWBBDao extends BaseJdbcDao{
	public Map<String, Object> getZcmx(int page, int pageSize, Map<String,Object> where) {
		
		Condition condition = new Condition();
		condition.add("b.DWMC", "FUZZY", where.get("DWMC"));


		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',t.*");
		sb.append(" FROM (select");
		sb.append(" a.id,");
		sb.append(" b.DWMC,");
		sb.append(" a.ZYYWCB,");
		sb.append(" a.ZYYWSJFJ,a.QTYWZC,a.GLFY,a.CWFY,");
		sb.append(" CASE a.ZTBJ WHEN 0 THEN '保存' WHEN 1 THEN '提交' ELSE NULL END AS ZTBJ,");
		sb.append(" DATE_FORMAT(a.JSSJ,'%Y-%m-%d') AS TJSJ");
		sb.append(" FROM "+Config.PROJECT_SCHEMA+"zs_cwbb_zcmx a,zs_jg b,(SELECT @rownum:=?) temp");
		sb.append(condition.getSql());//相当元 where b.DWMC like '%%'
		sb.append(" AND a.JG_ID=b.ID AND a.ZTBJ=1 ORDER  BY TJSJ) as t");
		sb.append("    LIMIT ?, ? ");
		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(0,pageSize * (page - 1));
		params.add(startIndex);
		params.add(pageSize);
		

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		// 获取符合条件的记录数
		
		int total = jdbcTemplate.queryForObject("SELECT FOUND_ROWS()",  Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);

				return obj;
	}
	public Map<String,Object> getZcmxById(String id){
		StringBuffer sb = new StringBuffer();
		sb.append("	 SELECT a.id AS 'key',a.id,b.DWMC,DATE_FORMAT(a.KSSJ,'%Y-%m-%d') as KSSJ,");
		sb.append("	DATE_FORMAT(a.JSSJ,'%Y-%m-%d') as JSSJ,a.ZYYWCB1,a.ZYYWCB,a.ZYYWSJFJ1,a.ZYYWSJFJ,");
		sb.append("	a.GZFY1,a.GZFY,a.QTYWZC1,a.QTYWZC,");
		sb.append("	a.FLF1,a.FLF,a.GLFY1,a.GLFY,a.JYF1,a.JYF,a.GLFY_GZFY1,");
		sb.append("	a.GLFY_GZFY,a.GHJF1,a.GHJF,a.GLFY_FLF1,a.GLFY_FLF,");
		sb.append("	a.SHTC1,a.SHTC,a.GLFY_YWZDF1,a.GLFY_YWZDF,a.BGF1,");
		sb.append("	a.BGF,a.GLFY_BGF1,a.GLFY_BGF,a.CLF1,a.CLF,");
		sb.append("	a.GLFY_QTSJ1,a.GLFY_QTSJ,a.HF1,a.HF,a.GLFY_QCFY1,a.GLFY_QCFY,");
		sb.append("	a.PXZLF1,a.PXZLF,a.GLFY_ZYFXJJ1,a.GLFY_ZYFXJJ,");
		sb.append("	a.HWF1,a.HWF,a.GLFY_ZYZRBX1,a.GLFY_ZYZRBX,a.ZPF1,");
		sb.append("	a.ZPF,a.GLFY_CLF1,a.GLFY_CLF,a.ZJ1,a.ZJ,a.GLFY_QTFY1,a.GLFY_QTFY,");
		sb.append("	a.ZFGJJ1,a.ZFGJJ,a.CWFY1,a.CWFY,a.GWZXF1,a.GWZXF,");
		sb.append("	a.YYWZC1,a.YYWZC,a.QT1,a.QT,a.ZCZJ1,a.ZCZJ,a.SZ,a.AGKJ,a.ZB");
		sb.append("	FROM zs_cwbb_zcmx a,zs_jg b");
		sb.append("	WHERE a.JG_ID=b.ID AND a.ZTBJ=1 and a.id=?");
		 Map<String,Object> rs = this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{id});
		 
		 return rs;
	}
	
	public Map<String,Object> lrfp1(int pn,int ps){
	  	   StringBuffer sb= new StringBuffer();
	  	 sb.append("	SELECT  b.ID AS 'key',a.id,a.JG_ID,b.DWMC,DATE_FORMAT(a.JSSJ,'%Y-%m-%d') AS TJSJ,a.DWFZR,");
			sb.append(" a.CKFZR,CASE a.ZTBJ WHEN 0 THEN \"保存\"  WHEN 1 THEN \"提交\" ELSE NULL END AS ZTBJ");
			sb.append(" FROM "+Config.PROJECT_SCHEMA+"zs_cwbb_lrfp a,zs_jg b");
			sb.append(" WHERE a.JG_ID=b.ID AND a.ZTBJ=1  ORDER BY a.JSSJ");	 
	  	   List<Map<String,Object>> ls= this.jdbcTemplate.queryForList(sb.toString());
	  	   Pager<Map<String,Object>> pager =Pager.create(ls,ps);
	         Map<String,Object> ob =new HashMap<>();
	         ob.put("data", pager.getPagedList(pn));
	         ob.put("total_number1", ls.size());
	         ob.put("pagetotal" , (ls.size()+ps-1)/ ps);
	         return ob;      

	    }
	 public Map<String,Object> lrfp(){
	   		StringBuffer sb = new StringBuffer();
	   		sb.append("	SELECT  b.ID AS 'key',a.id,a.JG_ID,b.DWMC,DATE_FORMAT(a.JSSJ,'%Y-%m-%d') AS TJSJ,a.DWFZR,");
			sb.append(" a.CKFZR,CASE a.ZTBJ WHEN 0 THEN \"保存\"  WHEN 1 THEN \"提交\" ELSE NULL END AS ZTBJ");
			sb.append(" FROM "+Config.PROJECT_SCHEMA+"zs_cwbb_lrfp a,zs_jg b");
			sb.append(" WHERE a.JG_ID=b.ID AND a.ZTBJ=1  ORDER BY a.JSSJ");
	  	List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		List<Map<String,Object>> fl = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> rec : ls){
			Map<String,Object> link = new HashMap<>();
			link.put("herf_xx", "http://localhost:8080/api/lrfp/xx/"+rec.get("id").toString());
			rec.put("_links", link);
			fl.add(rec);
		}
		   Map<String,Object> ob = new HashMap<>();
	   		ob.put("data", ls);
	   		return ob;
	 }
	 public Map<String,Object> xx(String id){
	   StringBuffer sb = new StringBuffer();
	 sb.append(" SELECT b.id AS 'key',a.id,a.JG_ID,b.DWMC,DATE_FORMAT(a.JSSJ,'%Y-%m-%d')AS SJ,a.JLR,a.JLRUPYEAR,a.NCWFPLR,a.NCWFPLRUPYEAR,a.QTZR,a.QTZRUPYEAR,");
	  sb.append("  a.KFPLR,a.KFPLRUPYEAR,a.YYGJ,a.YYGJUPYEAR,a.JLFLJJ,a.JLFLJJUPYEAR,a.CBJJ,a.CBJJUPYEAR,a.QYFZJJ,a.QYFZJJUPYEAR,");	 
	  sb.append(" a.LRGHTZ,a.LRGHTZUPYEAR,a.TZZFPLR,a.TZZFPLRUPYEAR,a.YXGL,a.YXGLUPYEAR,a.PTGL,a.PTGLUPYEAR,a.ZHPTGL,a.ZHPTGLUPYEAR,");
	  sb.append(" a.WFPLR,a.WFPLRUPYEAR,a.DWFZR,a.CKFZR,a.FHR,a.ZBR");	
	  sb.append(" FROM "+Config.PROJECT_SCHEMA+"zs_cwbb_lrfp a,zs_jg b");	
	  sb.append(" WHERE a.JG_ID=b.ID AND a.ZTBJ=1 AND a.id=? ORDER BY a.JSSJ");	
	 Map<String,Object> bg = this.jdbcTemplate.queryForMap(sb.toString(),new Object[]{id});
	   Map<String,Object> ob = new HashMap<>();
		    ob.put("data", bg);
		return ob;
		 
  }

}


