/*
 * 机构数据分析
 */
package gov.gdgs.zs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class JGSJFXDao extends BaseDao {
	
	/*
	 * 行业学历数据分析
	 */
	public Map<String, Object> getHyxlsjfxb(int page, int pageSize,
			HashMap<String, Object> map){
		List<Map<String, Object>> ls=this.getHyxlsj(null);
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		for(int i=0;i<ls.size();i++){
			this.getHyxlsjfxbTree(ls.get(i));
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		return obj;
	}
	
	/**
	 * 递归树
	 * @param parent
	 */
	private void getHyxlsjfxbTree(Map parent){
		Integer id=((Number) parent.get("cs_dm")).intValue();
		String idStr=id.toString();
		List<Map<String,Object>> children=this.getHyxlsj(idStr);
		for(int i=0;i<children.size();i++){
			this.getHyxlsjfxbTree(children.get(i));
		}
		if(children.size()>0)
		parent.put("children", children);
	}
	
	/**
	 * 数据查询
	 * @param pid
	 * @return
	 */
	private List<Map<String,Object>> getHyxlsj(String pid){
		List<Map<String, Object>> ls=new ArrayList<>();
		if(null==pid){
			StringBuffer sql=new StringBuffer(" select c.mc, ");
			sql.append("        t.nd, ");
			sql.append("        t.cs_dm, ");
			sql.append("        t.cs_dm ID, ");
			sql.append("        t.yjs_zrs, ");
			sql.append("        t.bk_zrs, ");
			sql.append("        t.dz_zrs, ");
			sql.append("        t.gz_zrs, ");
			sql.append("        t.zrs, ");
			sql.append("        t.zy_yjs_zrs, ");
			sql.append("        t.zy_bk_zrs, ");
			sql.append("        t.zy_dz_zrs, ");
			sql.append("        t.zy_gz_zrs, ");
			sql.append("        t.zy_zrs, ");
			sql.append("        t.fzy_yjs_zrs, ");
			sql.append("        t.fzy_bk_zrs, ");
			sql.append("        t.fzy_dz_zrs, ");
			sql.append("        t.fzy_gz_zrs, ");
			sql.append("        t.fzy_zrs, ");
			sql.append("        t.cy_yjs_zrs, ");
			sql.append("        t.cy_bk_zrs, ");
			sql.append("        t.cy_dz_zrs, ");
			sql.append("        t.cy_gz_zrs, ");
			sql.append("        t.cy_zrs, ");
			sql.append("        t.gxsj ");
			sql.append("   from zs_hyxlsjfxb_jg t, dm_cs c ");
			sql.append("  where t.cs_dm = c.ID ");
			sql.append("    and c.PARENT_ID is null ");
			ls=jdbcTemplate.queryForList(sql.toString());
		}else{
			StringBuffer sql=new StringBuffer(" select c.mc, ");
			sql.append("        t.nd, ");
			sql.append("        t.cs_dm, ");
			sql.append("        t.cs_dm ID, ");
			sql.append("        t.yjs_zrs, ");
			sql.append("        t.bk_zrs, ");
			sql.append("        t.dz_zrs, ");
			sql.append("        t.gz_zrs, ");
			sql.append("        t.zrs, ");
			sql.append("        t.zy_yjs_zrs, ");
			sql.append("        t.zy_bk_zrs, ");
			sql.append("        t.zy_dz_zrs, ");
			sql.append("        t.zy_gz_zrs, ");
			sql.append("        t.zy_zrs, ");
			sql.append("        t.fzy_yjs_zrs, ");
			sql.append("        t.fzy_bk_zrs, ");
			sql.append("        t.fzy_dz_zrs, ");
			sql.append("        t.fzy_gz_zrs, ");
			sql.append("        t.fzy_zrs, ");
			sql.append("        t.cy_yjs_zrs, ");
			sql.append("        t.cy_bk_zrs, ");
			sql.append("        t.cy_dz_zrs, ");
			sql.append("        t.cy_gz_zrs, ");
			sql.append("        t.cy_zrs, ");
			sql.append("        t.gxsj ");
			sql.append("   from zs_hyxlsjfxb_jg t, dm_cs c ");
			sql.append("  where t.cs_dm = c.ID ");
			sql.append("    and c.PARENT_ID = ? ");
			sql.append(" 	order by c.ID ");
			ls=jdbcTemplate.queryForList(sql.toString(),pid);
		}
		return ls;
	}
	
	
/*
 * 资金规模数据分析
 */
	
	public Map<String, Object> getZjgmsjfxb(int page, int pageSize,
			HashMap<String, Object> map){
		StringBuffer sb = new StringBuffer();
		sb.append(" select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1,t2.*,date_format(now(),'%Y' ) as nd");
		sb.append("   from( select *, ");
		sb.append("        round(fhzcgd_yx * 100 / zjgs, 2) fhzcgd_yx_bl, ");
		sb.append("        round(fhzcgd_hh * 100 / zjgs, 2) fhzcgd__hh_bl, ");
		sb.append("        round(fhzcgd_fs * 100 / zjgs, 2) fhzcgd_fs_bl, ");
		sb.append("        round(bfhzcgd_yx * 100 / zjgs, 2) bfhzcgd_yx_bl, ");
		sb.append("        round(bfhzcgd_hh * 100 / zjgs, 2) bfhzcgd__hh_bl, ");
		sb.append("        round(bfhzcgd_fs * 100 / zjgs, 2) bfhzcgd_fs_bl ");
		sb.append("   from (select cs.ID, ");
		sb.append("                cs.PARENT_ID, ");
		sb.append("                cs.mc, ");
		sb.append("                ifnull(sum(case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(zczj) from zs_jg where yxbz = '1') ");
		sb.append("                  when parent_id = '0' then ");
		sb.append("                  zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null then ");
		sb.append("                  zczj ");
		sb.append("                end),0) zjgs,                ");
		sb.append("               ifnull( sum(case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(jg.zczj) ");
		sb.append("                      from zs_jg jg ");
		sb.append("                      left join zs_jgyjxxb yj ");
		sb.append("                        on jg.id = yj.id ");
		sb.append("                     where yxbz = '1' ");
		sb.append("                       and flag = '1' ");
		sb.append("                       and jgxz_dm = '1') ");
		sb.append("                  when parent_id = '0' and flag = '1' and jgxz_dm = '1' then ");
		sb.append("                   zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null and ");
		sb.append("                       flag = '1' and jgxz_dm = '1' then ");
		sb.append("                   zczj ");
		sb.append("                end),0) fhzcgd_yx,                 ");
		sb.append("               ifnull( sum(case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(jg.zczj) ");
		sb.append("                      from zs_jg jg ");
		sb.append("                      left join zs_jgyjxxb yj ");
		sb.append("                        on jg.id = yj.id ");
		sb.append("                     where yxbz = '1' ");
		sb.append("                       and flag = '1' ");
		sb.append("                       and jgxz_dm = '2') ");
		sb.append("                  when parent_id = '0' and flag = '1' and jgxz_dm = '2' then ");
		sb.append("                   zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null and ");
		sb.append("                       flag = '1' and jgxz_dm = '2' then ");
		sb.append("                   zczj ");
		sb.append("                end) ,0)fhzcgd_hh,                ");
		sb.append("               ifnull( sum(case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(jg.zczj) ");
		sb.append("                      from zs_jg jg ");
		sb.append("                      left join zs_jgyjxxb yj ");
		sb.append("                        on jg.id = yj.id ");
		sb.append("                     where yxbz = '1' ");
		sb.append("                       and flag = '1' ");
		sb.append("                       and jgxz_dm = '3') ");
		sb.append("                  when parent_id = '0' and flag = '1' and jgxz_dm = '3' then ");
		sb.append("                   zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null and ");
		sb.append("                       flag = '1' and jgxz_dm = '3' then ");
		sb.append("                   zczj ");
		sb.append("                end) ,0)fhzcgd_fs,                 ");
		sb.append("               ifnull(sum( case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(jg.zczj) ");
		sb.append("                      from zs_jg jg ");
		sb.append("                      left join zs_jgyjxxb yj ");
		sb.append("                        on jg.id = yj.id ");
		sb.append("                     where yxbz = '1' ");
		sb.append("                       and flag = '0' ");
		sb.append("                       and jgxz_dm = '1') ");
		sb.append("                  when parent_id = '0' and flag = '0' and jgxz_dm = '1' then ");
		sb.append("                   zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null and ");
		sb.append("                       flag = '0' and jgxz_dm = '1' then ");
		sb.append("                   zczj ");
		sb.append("                end),0) bfhzcgd_yx,                 ");
		sb.append("               ifnull( sum(case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(jg.zczj) ");
		sb.append("                      from zs_jg jg ");
		sb.append("                      left join zs_jgyjxxb yj ");
		sb.append("                        on jg.id = yj.id ");
		sb.append("                     where yxbz = '1' ");
		sb.append("                       and flag = '0' ");
		sb.append("                       and jgxz_dm = '2') ");
		sb.append("                  when parent_id = '0' and flag = '0' and jgxz_dm = '2' then ");
		sb.append("                   zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null and ");
		sb.append("                       flag = '0' and jgxz_dm = '2' then ");
		sb.append("                   zczj ");
		sb.append("                end),0) bfhzcgd_hh, ");
		sb.append("                 ");
		sb.append("               ifnull( sum(case ");
		sb.append("                  when parent_id is null then ");
		sb.append("                   (select sum(jg.zczj) ");
		sb.append("                      from zs_jg jg ");
		sb.append("                      left join zs_jgyjxxb yj ");
		sb.append("                        on jg.id = yj.id ");
		sb.append("                     where yxbz = '1' ");
		sb.append("                       and flag = '0' ");
		sb.append("                       and jgxz_dm = '3') ");
		sb.append("                  when parent_id = '0' and flag = '0' and jgxz_dm = '3' then ");
		sb.append("                   zczj ");
		sb.append("                  when parent_id <> '0' and parent_id is not null and ");
		sb.append("                       flag = '0' and jgxz_dm = '3' then ");
		sb.append("                   zczj ");
		sb.append("                end),0) bfhzcgd_fs  ");
		sb.append("           from (select jg.ID, jg.JGXZ_DM, yj.FLAG, jg.cs_dm, jg.zczj ");
		sb.append("                   from zs_jg jg ");
		sb.append("                   left join zs_jgyjxxb yj ");
		sb.append("                     on jg.id = yj.id ");
		sb.append("                  where jg.yxbz = '1') jg ");
		sb.append("          right join dm_cs cs ");
		sb.append("             on jg.CS_DM = cs.id ");
		sb.append("            where cs.PARENT_ID=0 ");
		sb.append("          group by cs.ID, cs.PARENT_ID, cs.mc) as t) as t2");
		sb.append("    ");
	//StringBuffer sqlCount = new StringBuffer(" select count(*) from( ");
	//sqlCount.append(sb).append(" )t ");
	List<Map<String, Object>> ls=jdbcTemplate.queryForList(sb.toString());
	int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
	
   //int total = this.jdbcTemplate.queryForObject(sqlCount.toString(), Integer.class);
	Map<String, Object> obj = new HashMap<String, Object>();
	
	
	obj.put("data", ls);
	obj.put("total", total);
	//obj.put("pageSize", 1);
	//obj.put("current", 1);

	return obj;
	
	}
	
	
	
}

