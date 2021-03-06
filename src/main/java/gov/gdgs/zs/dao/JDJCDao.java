package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JDJCDao extends BaseDao{
	/**
	 * 
	 * @return 监督检查→→事物所年检分页查询
	 * @throws Exception 
	 */
	public Map<String,Object> swsnjcx(int pn,int ps,Map<String,Object> qury) {
		Condition condition = new Condition();
		condition.add("c.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("c.cs_dm", Condition.EQUAL, qury.get("cs"));
		condition.add("a.nd", Condition.EQUAL, qury.get("nd"));
		condition.add("a.ztdm", Condition.EQUAL, qury.get("bbzt"));
		condition.add("a.zjsj", Condition.GREATER_EQUAL, qury.get("sbsj"));
		condition.add("a.zjsj", Condition.LESS_EQUAL, qury.get("sbsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',v.*,");
		sb.append("		case v.ztdm when 3 then '已年检' when 2 then '已自检' else null end as njzt,");
		sb.append("		 if(v.WGCL_DM is not null,(select b.CLMC from dm_jgwgcl b where b.ID=v.WGCL_DM),null)  as njcl,");
		sb.append("		if((SELECT e.ID FROM zs_spzx e,zs_spxx f WHERE e.sjid=v.id AND e.ID=f.SPID limit 1),");
		sb.append("		(SELECT CONCAT_WS(',', IF(f.SPSJ IS NULL,'',DATE_FORMAT(f.SPSJ,'%Y-%m-%d')), IF(f.SPYJ IS NULL,'',f.SPYJ), ");
		sb.append("		IF(f.SPRNAME IS NULL,'',f.SPRNAME)) AS njcl");
		sb.append("		FROM zs_spzx e,zs_spxx f");
		sb.append("		WHERE e.sjid=v.id AND e.ID=f.SPID");
		sb.append("		ORDER BY e.TJSJ DESC");
		sb.append("		LIMIT 1),null");
		sb.append("		) as spcl");
		sb.append("		FROM (");
		sb.append("		SELECT 		c.dwmc,c.JGZCH AS zsbh,d.mc AS jgxz,c.yzbm,c.DZHI AS bgdz,c.DHUA AS dhhm,a.*,");
		sb.append("		  DATE_FORMAT(a.zjsj,'%Y-%m-%d') AS zjrq,");
		sb.append("		DATE_FORMAT(c.SWSZSCLSJ,'%Y-%m-%d') AS clsj, ");
		sb.append("		DATE_FORMAT(a.fzrsj,'%Y-%m-%d') AS qzrq");
		sb.append("		FROM zs_jg_njb a,zs_jg c,dm_jgxz d");
		sb.append(condition.getSql());
		sb.append("		 AND a.ZSJG_ID = c.ID AND a.ztdm IN (2,3) AND d.ID = c.JGXZ_DM");
		sb.append("		GROUP BY a.zsjg_id,nd");
		sb.append("		ORDER BY a.ND DESC	");
		sb.append("		    LIMIT ?, ? ");
		sb.append("		) AS v,(");
		sb.append("		SELECT @rownum:=?) zs_jg");
		ArrayList<Object> params = condition.getParams();
		params.add((pn-1)*ps);
		params.add(ps);
		params.add((pn-1)*ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("pageAll",(total + ps - 1) / ps);
		ob.put("page", meta);
		
		return ob;
	}
	//执业税务师年检
    public Map<String, Object> getZyswsnjb(int page, int pageSize, Map<String,Object> where) {
			
			Condition condition = new Condition();
			condition.add("a.nd", Condition.EQUAL, where.get("nd"));
			condition.add("b.dwmc",Condition.FUZZY,where.get("dwmc"));
			condition.add("a.ZTDM", Condition.EQUAL, where.get("ZTDM"));
            

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT  SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 AS 'key',t.*");
			sb.append(" from  ( select a.id,a.ND,c.XMING,b.dwmc,");
			sb.append(" CASE a.ZTDM WHEN 0 THEN '退回' WHEN 1 THEN '保存' WHEN 2 THEN '自检' WHEN 3 THEN '年检' ELSE NULL END AS ZTDM");
			sb.append(" from "+Config.PROJECT_SCHEMA+"zs_zcswsnj a,zs_jg b,zs_ryjbxx c, zs_zysws d,(SELECT @rownum:=?) temp");
			sb.append(condition.getSql());//相当元 where x.xx like '%%'
			sb.append(" and a.ZSJG_ID=b.ID and  a.SWS_ID=d.ID and c.ID=d.RY_ID   order by a.ND desc) as t");
			sb.append("    LIMIT  ?, ? ");
			// 装嵌传值数组
			int startIndex = pageSize * (page - 1);
			ArrayList<Object> params = condition.getParams();
			params.add(0, pageSize * (page - 1));
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
    
    public Map<String,Object> getZyswsnjbById(long id){
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
			 Map<String, Object> ob = new HashMap<>();
				ob.put("data", rs);
			 
			 return ob;
		}
    /**
     * 未交报表
     * @param page
     * @param pageSize
     * @param where
     * @return
     */
    public Map<String, Object> getWsbbb(int page, int pageSize, Map<String,Object> where) {    	
    	List<String> arr = new ArrayList<String>();
    	arr.add("zs_cwbb_lrgd");
		arr.add("zs_cwbb_zcfzgd");
		arr.add("zs_cwbb_lrfp");
		arr.add("zs_cwbb_xjll");
		arr.add("zs_cwbb_zcmx");
		Condition condition = new Condition();
		Condition condition2 = new Condition();
		condition.add("d.dwmc",Condition.FUZZY,where.get("dwmc"));
		condition2.add("nd",Condition.EQUAL,where.get("nd"));
		condition2.add("timevalue",Condition.EQUAL,where.get("timevalue"));
		StringBuffer sb = new StringBuffer();
		sb.append(" 	SELECT sql_calc_found_rows	 @rownum:=@rownum+1 as 'key','"+where.get("nd")+"' as nd,'未上报' as sbzt,d.id,d.dwmc,");
		sb.append(" 	d.JGZCH as zsbh,c.mc as cs, d.DHUA as dhhm,d.TXYXMING as txyxm,d.XTYPHONE as txyyddh");
		sb.append("		,(select v.id from zs_sdjl_jg v where v.jg_id=d.id and v.lx=3 and v.yxbz=1 limit 1) as issd ");
		sb.append(" 	FROM zs_jg d,dm_cs c,(SELECT @rownum:=?) temp");
		sb.append(condition.getSql());//相当元 where x.xx like '%%'
		sb.append("     AND d.CS_DM=c.ID  ");
		sb.append(" 	AND d.ID not in(select JG_ID from "+arr.get(Integer.parseInt(where.get("bblx").toString()))+condition2.getSql()+" AND ZTBJ=1)");
		sb.append(" 	and d.YXBZ=1");
		sb.append("    LIMIT ?, ? ");
		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(0, pageSize * (page - 1));
		params.addAll(condition2.getParams());
		params.add(startIndex);
		params.add(pageSize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				params.toArray(),
				new RowMapper<Map<String,Object>>() {
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				String id = hashids.encode((int)rs.getObject("id"));
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("jgid", id);
				map.put("dwmc", rs.getObject("dwmc"));
				map.put("zsbh", rs.getObject("zsbh"));
				map.put("key", rs.getObject("key"));
				map.put("cs", rs.getObject("cs"));
				map.put("dhhm", rs.getObject("dhhm"));
				map.put("txyxm", rs.getObject("txyxm"));
				map.put("txyyddh", rs.getObject("txyyddh"));
				map.put("nd", rs.getObject("nd"));
				map.put("sbzt", rs.getObject("sbzt"));
				map.put("issd", rs.getObject("issd"));
				return map;
			}
		});

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
	
}
