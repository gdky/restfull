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

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class XtsjfxDao extends BaseJdbcDao{
	/*
	 * 机构年检数据分析
	 */
	
	public Map<String, Object> getJgnjsjfxb(int page, int pageSize,
			Map<String, Object> where) {

		Condition condition = new Condition();
		condition.add("nd", "EQUAL", where.get("nd"));
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select SQL_CALC_FOUND_ROWS	@rownum:=@rownum+1 AS 'key',t.id,t.parent_id,t.mc,ifnull(t.zjgs,0) zjgs,ifnull(t.cjnj_jgs,0)cjnj_jgs,");
		sb.append(" round(t.cjnj_jgs*100/t.zjgs,2) cjnj_bl,ifnull((t.zjgs-t.cjnj_jgs),0) wcjnj_jgs,100-round(t.cjnj_jgs*100/t.zjgs,2) wcjnj_bl,");	
		sb.append(" ifnull(t.tgnj_jgs,0) tgnj_jgs,round(t.tgnj_jgs*100/t.cjnj_jgs,2) tgnj_bl,ifnull(t.njz_jgs,0) njz_jgs,round(t.njz_jgs*100/t.cjnj_jgs,2) njz_bl,");	
		sb.append(" ifnull(t.wtgnj_jgs,0) wtgnj_gjs,round(t.wtgnj_jgs*100/t.cjnj_jgs,2)wtgnj_bl ");		
		sb.append(" FROM (select cs.ID,cs.PARENT_ID,cs.mc,(select count(id) from zs_jg where yxbz = '1') zjgs,(select count(nj.id)");
		sb.append("    from zs_jg jg, zs_jg_njb nj where jg.yxbz = '1'   and nj.yxbz = '1' and jg.id = nj.zsjg_id and nj.nd = ?) cjnj_jgs,");
		sb.append("  (select count(nj.id) from zs_jg jg, zs_jg_njb nj where jg.yxbz = '1' and nj.yxbz = '1' and nj.ztdm = '3' ");
		sb.append("  and jg.id = nj.zsjg_id and nj.nd = ?) tgnj_jgs,(select count(nj.id) from zs_jg jg, zs_jg_njb nj");
		sb.append("  where jg.yxbz = '1'  and nj.yxbz = '1'  and nj.ztdm in ('1', '2') and jg.id = nj.zsjg_id and nj.nd = ?) njz_jgs,");
		sb.append("  (select count(nj.id) from zs_jg jg, zs_jg_njb nj where jg.yxbz = '1' and nj.yxbz = '1' and nj.ztdm = '0' ");
		sb.append("  and jg.id = nj.zsjg_id and nj.nd = ?) wtgnj_jgs from dm_cs cs,(SELECT @rownum:=?) temp  where cs.parent_id is null");
		sb.append("  union all select b.id, b.parent_id, b.mc, a.zrs, b.zrs, b.tgnj_jgs, b.njz_jgs, b.btgnj_jgs  from (select cs.ID,");
		sb.append("  cs.PARENT_ID, cs.mc, sum(case when parent_id = '0'  then 1 when parent_id <> '0'   and parent_id is not null then");
		sb.append("   1 else 0 end )zrs from zs_jg jb right join dm_cs cs  on jb.CS_DM = cs.id where cs.parent_id is not null  and jb.yxbz = '1' ");
		sb.append("  group by cs.ID, cs.PARENT_ID, cs.mc) a,(select cs.ID, cs.PARENT_ID, cs.mc, sum( case when parent_id = '0' and id_2 is not null then ");
		sb.append("  1 when parent_id <> '0'  and id_2 is not null and parent_id is not null then 1 else 0 end ) zrs, sum(case ");
		sb.append("  when parent_id = '0' and ztdm = '3'  and id_2 is not null then 1 when parent_id <> '0' and ztdm = '3' and id_2 is not null  and ");
		sb.append("  parent_id is not null then 1 else 0 end ) tgnj_jgs, sum( case when parent_id = '0' and ztdm = '0'  and id_2 is not null then ");
		sb.append("  1  when parent_id <> '0' and ztdm = '0'  and id_2 is not null and  parent_id is not null then 1 else 0 end  )btgnj_jgs,");
		sb.append("  sum( case when parent_id = '0' and ztdm in ('1', '2')  and id_2 is not null then 1 when parent_id <> '0' and ztdm in ('1', '2')  and id_2 is not null and ");
		sb.append("  parent_id is not null then 1 else 0 end  )njz_jgs from (select jb.id    id_1, zy.id    id_2, zy.nd, zy.ztdm, ");
		sb.append("  jb.cs_dm from zs_jg jb left join zs_jg_njb zy on jb.id = zy.zsjg_id where jb.yxbz = '1' and zy.yxbz = '1' and (zy.nd = ? or zy.nd is null)) jg ");
		sb.append("  right join dm_cs cs on jg.CS_DM = cs.id where cs.parent_id is not null group by cs.ID, cs.PARENT_ID, cs.mc) b where a.id = b.id)t");
		sb.append("    LIMIT ?, ? ");
		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = (condition.getParams());
		params.add(condition.getParams().get(0));
		params.add(condition.getParams().get(0));
		params.add(condition.getParams().get(0));
		params.add(pageSize * (page - 1));
		params.add(condition.getParams().get(0));
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

	public Map<String, Object> getRynjsjfxb(int page, int pageSize,
			HashMap<String, Object> map) {
		StringBuffer sql = new StringBuffer(" select ")
			.append(" t1.id,t1.parent_id,date_format(now(),'%Y') as nd,t1.mc,t1.zrs ycjzy_zrs,t3.scjzy_rs, ")
			.append(" round(t3.scjzy_rs * 100 / t1.zrs, 2) scjzy_bl, ")
			.append(" t3.scjzy_tg_rs,t3.scjzy_wtg_rs,t3.scjzy_clz_rs,(t1.zrs - t3.scjzy_rs) wcjzy_rs, ")
			.append(" 100 - round(t3.scjzy_rs * 100 / t1.zrs, 2) wcjzy_bl,t2.zrs ycjfzy_zrs, ")
			.append(" t4.scjfzy_rs,round(t4.scjfzy_rs * 100 / t2.zrs, 2) scjzy_bl2,t4.scjfzy_tg_rs, ")
			.append(" t4.scjfzy_wtg_rs,t4.scjfzy_clz_rs,(t2.zrs - t4.scjfzy_rs) wcjzy_rs2, ")
			.append(" 100 - round(t4.scjfzy_rs * 100 / t2.zrs, 2) wcjzy_bl2 ")
		;
		//StringBuffer sqlCount = new StringBuffer(" select count(t1.id) ");
		StringBuffer sqlTableWhere = new StringBuffer(" from ")
			.append(" ( ")
			.append(" select cs.id,cs.parent_id,cs.mc, ")
			.append(" case when parent_id is null then  ")
			.append(" (select count(id) from zs_zysws where yxbz = '1') ")
			.append(" when parent_id = '0' then ")
			.append(" count(t.id)  ")
			.append(" when parent_id <> '0' and parent_id is not null then ")
			.append(" count(t.id) end zrs ")
			.append(" from dm_cs cs left join ")
			.append(" (select ry.cs_dm, ry.id from zs_zysws zs, zs_ryjbxx ry where zs.ry_id = ry.id and ry.yxbz = '1' and zs.yxbz = '1') t ")
			.append(" on cs.id = t.cs_dm group by cs.id, cs.parent_id, cs.mc ")
			.append(" ) t1, ")
			.append(" ( ")
			.append(" select cs.id,cs.parent_id,cs.mc, ")
			.append(" case when parent_id is null then ")
			.append(" (select count(id) from zs_fzysws where yxbz = '1') ")
			.append(" when parent_id = '0' then count(t.id) ")
			.append(" when parent_id <> '0' and parent_id is not null then count(t.id) end zrs ")
			.append(" from dm_cs cs left join ")
			.append(" (select ry.cs_dm, ry.id from zs_fzysws zs, zs_ryjbxx ry where zs.ry_id = ry.id and ry.yxbz = '1' and zs.yxbz = '1') t ")
			.append(" on cs.id = t.cs_dm group by cs.id, cs.parent_id, cs.mc ")
			.append(" ) t2, ")
			.append(" ( ")
			.append(" select cs.id,cs.parent_id,cs.mc, ")
			.append(" sum( ")
			.append(" case when parent_id is null then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id and zs.yxbz = '1' and ry.nd = '2015') ")
			.append(" when parent_id = '0' and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null then 1 else 0 end ")
			.append(" ) scjzy_rs, ")
			.append(" sum( ")
			.append(" case when parent_id is null and ztdm = '3' then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id and zs.yxbz = '1' and ry.ztdm = '3' and ry.nd = '2015') ")
			.append(" when parent_id = '0' and ztdm = '3' and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null and ztdm = '3' then 1 else 0 end ")
			.append(" ) scjzy_tg_rs, ")
			.append(" sum( ")
			.append(" case when parent_id is null and ztdm in ('1', '2') then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id and zs.yxbz = '1' and ry.ztdm in ('1', '2') and ry.nd = '2015') ")
			.append(" when parent_id = '0' and ztdm in ('1', '2') and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null and ztdm in ('1', '2') then 1 else 0 end ")
			.append(" ) scjzy_clz_rs, ")
			.append(" sum( ")
			.append(" case when parent_id is null and ztdm = '0' then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id and zs.yxbz = '1' and ry.ztdm = '0' and ry.nd = '2015') ")
			.append(" when parent_id = '0' and ztdm = '0' and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null and ztdm = '0' then 1 else 0 end ")
			.append(" ) scjzy_wtg_rs ")
			.append(" from dm_cs cs left join ")
			.append(" (select jb.cs_dm, ry.id, ry.ztdm from zs_zysws zs, zs_zcswsnj ry, zs_ryjbxx jb where zs.ry_id = ry.sws_id and zs.ry_id = jb.id and jb.yxbz = '1' and zs.yxbz = '1' and ry.nd = '2015') t ")
			.append(" on cs.id = t.cs_dm group by cs.id, cs.parent_id, cs.mc ")
			.append(" ) t3, ")
			.append(" ( ")
			.append(" select cs.id,cs.parent_id,cs.mc, ")
			.append(" sum( ")
			.append(" case when parent_id is null then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id  and zs.yxbz = '1' and ry.nd = '2015') ")
			.append(" when parent_id = '0' and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null then 1 else 0 end ")
			.append(" ) scjfzy_rs, ")
			.append(" sum( ")
			.append(" case when parent_id is null and ztdm = '3' then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id and zs.yxbz = '1' and ry.ztdm = '3' and ry.nd = '2015') ")
			.append(" when parent_id = '0' and ztdm = '3' and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null and ztdm = '3' then 1 else 0 end ")
			.append(" ) scjfzy_tg_rs, ")
			.append(" sum( ")
			.append(" case when parent_id is null and ztdm in ('1', '2') then ")
			.append(" (select count(zs.id) from zs_zysws zs, zs_zcswsnj ry where zs.ry_id = ry.sws_id and zs.yxbz = '1' and ry.ztdm in ('1', '2') and ry.nd = '2015') ")
			.append(" when parent_id = '0' and ztdm in ('1', '2') and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and t.id is not null and ztdm in ('1', '2') then 1 end ")
			.append(" ) scjfzy_clz_rs, ")
			.append(" sum( ")
			.append(" case when parent_id is null and ztdm = '0' then ")
			.append(" (select count(zs.id) from zs_fzysws zs, zs_zcswsnj ry where zs.ry_id = ry.qtsws_id and zs.yxbz = '1' and ry.ztdm = '0' and ry.nd = '2015') ")
			.append(" when parent_id = '0' and ztdm = '0' and t.id is not null then 1 ")
			.append(" when parent_id <> '0' and parent_id is not null and ztdm = '0' and t.id is not null then 1 else 0 end ")
			.append(" ) scjfzy_wtg_rs ")
			.append(" from dm_cs cs left join ")
			.append(" (select jb.cs_dm, ry.id, ry.ztdm from zs_fzysws zs, zs_zcswsnj ry, zs_ryjbxx jb where zs.ry_id = ry.qtsws_id and zs.ry_id = jb.id and jb.yxbz = '1' and zs.yxbz = '1' and ry.nd = '2015') t ")
			.append(" on cs.id = t.cs_dm group by cs.id, cs.parent_id, cs.mc ")
			.append(" ) t4 ")
			.append(" where t1.id = t2.id and t2.id = t3.id and t3.id = t4.id ")
			.append(" and t1.id in (select id from dm_cs where parent_id is null or parent_id < 1) ");
		if(map != null && map.get("year") != null && !"".equals(map.get("year"))){
			//Object year = map.get("year");
			sqlTableWhere.append(" and date_format(now(),'%Y') = "+map.get("year"));
		}
		//int total = this.jdbcTemplate.queryForObject(sqlCount.append(sqlTableWhere).toString(), Integer.class);
		//List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql.append(sqlTableWhere.append(" order by t1.parent_id,t1.id ").append(" limit "+pageSize * (page - 1)+","+pageSize)).toString());
		//List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql.append(sqlTableWhere).append(" llimit "+pageSize * (page - 1)+","+pageSize+" ").toString());
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql.append(sqlTableWhere).toString());
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("data", ls);
		//obj.put("total", total);
		//obj.put("pageSize", pageSize);
		//obj.put("current", page);
		return obj;
	}

	public Map<String, Object> getZyzshSjfxb(int page, int pageSize,
			HashMap<String, Object> map) {
		final String url=Config.URL_PROJECT;
		Condition condition = new Condition();
		condition.add("r.XMING", Condition.FUZZY, map.get("XMING"));
		condition.add("z.ZYZSBH", Condition.EQUAL, map.get("ZYZSBH"));
		condition.add("z.JG_ID", Condition.EQUAL, map.get("ID"));
		StringBuffer sql=new StringBuffer(" SELECT SQL_CALC_FOUND_ROWS @rownum := @rownum + 1 AS 'key', T.* ");
		sql.append("   FROM (SELECT z.ID, ");
		sql.append("                z.RY_ID, ");
		sql.append("                r.XMING, ");
		sql.append("                xb.MC XB, ");
		sql.append("                xl.MC XL, ");
		sql.append("                z.ZYZGZSBH, ");
		sql.append("                z.ZYZSBH, ");
		sql.append("                IF(z.CZR_DM = '0', '是', '否') isCzr, ");
		sql.append("                IF(z.FQR_DM = '0', '是', '否') isFqr, ");
		sql.append("                jg.DWMC ");
		sql.append("           FROM zs_zysws z, zs_ryjbxx r, dm_xb xb, dm_xl xl, zs_jg jg ");
		sql.append("  "+condition.getSql());
		sql.append("            AND z.RY_ID = r.ID ");
		sql.append("            AND r.XB_DM = xb.ID ");
		sql.append("            AND r.XL_DM = xl.ID ");
		sql.append("            AND z.JG_ID = jg.ID ");
		sql.append("            and z.YXBZ = '1' ");
		sql.append("            AND r.YXBZ = '1' ");
		sql.append("            AND z.ZYZSBH IS NOT NULL ");
		sql.append("            AND z.ZYZSBH IN (SELECT t.ZYZSBH ");
		sql.append("                               FROM zs_zysws t ");
		sql.append("                              where t.YXBZ = '1' ");
		sql.append("                              GROUP BY t.ZYZSBH ");
		sql.append("                             HAVING COUNT(t.ZYZSBH) > 1) ");
		sql.append("          ORDER BY z.ZYZSBH, z.RY_ID) T, ");
		sql.append("        (SELECT @rownum := ?) xh ");
		sql.append("  LIMIT ?, ? ");
		
		ArrayList<Object> params = condition.getParams();
		params.add((page-1)*pageSize);
		params.add((page-1)*pageSize);
		params.add(pageSize);
		List<Map<String,Object>> ls=this.jdbcTemplate.query(sql.toString(),params.toArray(),new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("RY_ID"));
				link.put("herf_xxzl", url+"/ryxx/zyryxx/"+id);
				link.put("herf_bgjl", url+"/ryxx/zyrybgjl/"+id);
				link.put("herf_zsjl", url+"/ryxx/zyryzsjl/"+id);
				link.put("herf_zjjl", url+"/ryxx/zyryzjjl/"+id);
				link.put("herf_zzjl", url+"/ryxx/zyryzzjl/"+id);
				map.put("key", rs.getObject("key"));
				map.put("ID", rs.getObject("ID"));
				map.put("_links", link);
				map.put("XMING", rs.getObject("XMING"));
				map.put("XB", rs.getObject("XB"));
				map.put("XL", rs.getObject("XL"));
				map.put("ZYZGZSBH", rs.getObject("ZYZGZSBH"));
				map.put("ZYZSBH", rs.getObject("ZYZSBH"));
				map.put("isCzr", rs.getObject("isCzr"));
				map.put("isFqr", rs.getObject("isFqr"));
				map.put("DWMC", rs.getObject("DWMC"));
				return map;
			}
		});
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

}
