package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class AddswsnjDao extends BaseJdbcDao implements IAddswsnjDao {

	@Resource
	private SPDao spDao;
	
	public Map<String, Object> getswsnjb(int page, int pageSize, int Jgid,
			Map<String, Object> where) {
		Condition condition = new Condition();
		condition.add("a.nd", "FUZZY", where.get("nd"));
		StringBuffer sql = new StringBuffer(
				" SELECT SQL_CALC_FOUND_ROWS @rownum := @rownum + 1 AS 'key', v.* ");
		sql.append("   FROM (SELECT c.dwmc, ");
		sql.append("                c.JGZCH AS zsbh, ");
		sql.append("                d.mc AS jgxz, ");
		sql.append("                c.yzbm, ");
		sql.append("                c.DZHI AS bgdz, ");
		sql.append("                c.DHUA AS dhhm, ");
		sql.append("                a.*, ");
		sql.append("                CASE a.ztdm ");
		sql.append("                  WHEN 0 THEN ");
		sql.append("                   '退回' ");
		sql.append("                  WHEN 3 THEN ");
		sql.append("                   '已年检' ");
		sql.append("                  WHEN 2 THEN ");
		sql.append("                   '已自检' ");
		sql.append("                  else null");
		sql.append("                end as njzt, ");
		sql.append("                CASE a.WGCL_DM ");
		sql.append("                  WHEN 1 THEN ");
		sql.append("                   '年检予以通过' ");
		sql.append("                  WHEN 2 THEN ");
		sql.append("                   '年检不予通过,责令2个月整改' ");
		sql.append("                  WHEN 6 THEN ");
		sql.append("                   '年检不予以通过' ");
		sql.append("                  WHEN 7 THEN ");
		sql.append("                   '资料填写有误，请重新填写' ");
		sql.append("                  ELSE ");
		sql.append("                   NULL ");
		sql.append("                END AS njcl, ");
		sql.append("                DATE_FORMAT(a.zjsj, '%Y-%m-%d') AS zjrq, ");
		sql.append("                DATE_FORMAT(c.SWSZSCLSJ, '%Y-%m-%d') AS clsj, ");
		sql.append("                DATE_FORMAT(a.fzrsj, '%Y-%m-%d') AS qzrq, ");
		sql.append("                sl.zyrs dqzyrs, ");
		sql.append("                sl.zrs dqzrs, ");
		sql.append("                sl.fss dqfss ");
		sql.append("           FROM zs_jg_njb a, ");
		sql.append("                zs_jg c, ");
		sql.append("                dm_jgxz d, ");
		sql.append("                (SELECT jg.ID, ");
		sql.append("                        count(distinct z.ID) zyrs, ");
		sql.append("                        count(distinct z.ID) + count(distinct f.ID) + ");
		sql.append("                        count(distinct c.ID) + count(distinct q.ID) zrs, ");
		sql.append("                        count(distinct zjg.ID) fss ");
		sql.append("                   FROM zs_jg jg ");
		sql.append("                   LEFT JOIN zs_zysws z ");
		sql.append("                     ON jg.ID = z.JG_ID ");
		sql.append("                    AND z.YXBZ = '1' ");
		sql.append("                   left join zs_fzysws f ");
		sql.append("                     on jg.ID = f.ZSJGID ");
		sql.append("                    and f.YXBZ = '1' ");
		sql.append("                   left join zs_cyry c ");
		sql.append("                     on jg.ID = c.JG_ID ");
		sql.append("                    and c.YXBZ = '1' ");
		sql.append("                   left join zs_qtry q ");
		sql.append("                     on jg.ID = q.JG_ID ");
		sql.append("                   left join zs_jg zjg ");
		sql.append("                     on jg.ID = zjg.PARENTJGID ");
		sql.append("                    and zjg.YXBZ = '1' ");
		sql.append("                  group by jg.ID) sl ");
		sql.append(condition.getSql());
		sql.append("            and a.ZSJG_ID = c.ID ");
		sql.append("            and c.ID = sl.id ");
		sql.append("            and a.ZSJG_ID = ? ");
		// sql.append("            and a.ztdm in (2, 3) ");
		sql.append("            and d.ID = c.JGXZ_DM ");
		// sql.append("          group by a.zsjg_id, nd ");
		sql.append("          order by a.ND desc) as v, ");
		sql.append("        (SELECT @rownum := ?) zs_jg  ");
		sql.append("        LIMIT ?,? ");

		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();

		params.add(Jgid);
		params.add(pageSize * (page - 1));
		params.add(startIndex);
		params.add(pageSize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(
				sql.toString(), params.toArray());

		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()",
				int.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);
		obj.put("jg_id", Jgid);
		return obj;

	}

	// 获取事务所报备份数方法
	public Map<String, Object> getswsbafs(int Jgid, Map<String, Object> where) {
		Condition condition = new Condition();
		condition.add("b.nd", "FUZZY", where.get("nd"));
		StringBuffer sb = new StringBuffer();
		sb.append("select count(b.ID) as bndbafs  from zs_ywbb b ");
		sb.append(condition.getSql());
		sb.append(" and b.JG_ID=? ");

		// 装嵌传值数组

		ArrayList<Object> params = condition.getParams();

		params.add(Jgid);

		// 获取符合条件的记录
		String ls = jdbcTemplate.queryForObject(sb.toString(),
				params.toArray(), String.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("jg_id", Jgid);
		return obj;

	}

	// 获取机构已年检年度（用于校验）
	public Map<String, Object> getswsnjnd(int Jgid, Map<String, Object> where) {
		String nd = "0";
		Condition condition = new Condition();
		condition.add("a.nd", "FUZZY", where.get("nd"));
		StringBuffer sb = new StringBuffer();
		sb.append("select a.ND,a.ID from zs_jg_njb a ");
		sb.append(condition.getSql());
		sb.append(" and a.ZSJG_ID=? ");

		// 装嵌传值数组

		ArrayList<Object> params = condition.getParams();

		params.add(Jgid);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());
		if (ls.size() > 0) {
			if (null == where.get("njid")) {
				nd = ((Integer) ls.get(0).get("nd")).toString();
			} else {
				Integer njid = (Integer) where.get("njid");
				Integer dbid = (Integer) ls.get(0).get("ID");
				if (njid.toString().equals(dbid.toString())) {
					nd = "0";
				} else {
					nd = ((Integer) ls.get(0).get("nd")).toString();
				}
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", nd);
		obj.put("jg_id", Jgid);
		return obj;

	}

	public Map<String, Object> getswsnjbById(String id) {
		StringBuffer sql = new StringBuffer(" SELECT c.dwmc, ");
		sql.append("        c.JGZCH AS zsbh, ");
		sql.append("        d.mc AS jgxz, ");
		sql.append("        c.yzbm, ");
		sql.append("        c.DZHI AS bgdz, ");
		sql.append("        c.DHUA AS dhhm, ");
		sql.append("        a.ID,");
		sql.append("        ifnull(a.ZCSWSB,0) ZCSWSB ,");
		sql.append("        ifnull(a.GDBDQK,0) GDBDQK,");
		//sql.append("       ifnull() ");
		sql.append(" ifnull(a.ZCZJ,0) ZCZJ,");
		sql.append("  a.ZJ,");
		sql.append("  a.SZ,");
		sql.append("  a.ND,");
		sql.append("  a.FZR,");
		sql.append("  a.NJZJ,");
		sql.append("  a.ZJWGDM,   "     );
		sql.append(" ifnull( a.ZRS,0) ZRS,");
		sql.append("  ifnull(a.BAFS,0) BAFS,");
		sql.append(" ifnull (a.ZCZJ,0) ZCZJ,");
		sql.append(" ifnull( a.ZYRS,0) ZYRS,");
		sql.append(" ifnull( a.YJYRS,0) YJYRS,");
		sql.append(" ifnull( a.SJJYRS,0) SJJYRS,");
		sql.append(" ifnull( a.WJYRS,0) WJYRS,");
		sql.append(" ifnull( a.FSS,0) FSS,");
		sql.append(" ifnull( a.ZCZJ,0) ZCZJ,");
		sql.append(" ifnull( a.ZCSWSBZJ,0) ZCSWSBZJ,");
		sql.append("  ifnull(a.ZCSWSBJS,0) ZCSWSBJS,");
		sql.append("  ifnull(a.GDBDQKZJ,0) GDBDQKZJ,");
		sql.append("  ifnull(a.GDBDQKJS,0) GDBDQKJS,");
		
		
		sql.append("        CASE a.ztdm ");
		sql.append("          WHEN 3 THEN ");
		sql.append("           '已年检' ");
		sql.append("          WHEN 2 THEN ");
		sql.append("           '已自检' ");
		sql.append("          ELSE ");
		sql.append("           NULL ");
		sql.append("        END AS njzt, ");
		sql.append("        CASE a.WGCL_DM ");
		sql.append("          WHEN 1 THEN ");
		sql.append("           '年检予以通过' ");
		sql.append("          WHEN 2 THEN ");
		sql.append("           '年检不予通过，责令2个月整改' ");
		sql.append("          WHEN 6 THEN ");
		sql.append("           '年检不予以通过' ");
		sql.append("          WHEN 7 THEN ");
		sql.append("           '资料填写有误，请重新填写' ");
		sql.append("          ELSE ");
		sql.append("           NULL ");
		sql.append("        END AS njcl, ");
		sql.append("        DATE_FORMAT(a.zjsj, '%Y-%m-%d') AS zjrq, ");
		sql.append("        DATE_FORMAT(c.SWSZSCLSJ, '%Y-%m-%d') AS clsj, ");
		sql.append("        DATE_FORMAT(a.fzrsj, '%Y-%m-%d') AS qzrq, ");
		sql.append("        sl.zyrs dqzyrs, ");
		sql.append("        sl.zrs dqzrs, ");
		sql.append("        sl.fss dqfss ");
		sql.append("   FROM zs_jg_njb a, ");
		sql.append("        zs_jg c, ");
		sql.append("        dm_jgxz d, ");
		sql.append("        (SELECT jg.ID, ");
		sql.append("                count(distinct z.ID) zyrs, ");
		sql.append("                count(distinct z.ID) + count(distinct f.ID) + ");
		sql.append("                count(distinct c.ID) + count(distinct q.ID) zrs, ");
		sql.append("                count(distinct zjg.ID) fss ");
		sql.append("           FROM zs_jg jg ");
		sql.append("           LEFT JOIN zs_zysws z ");
		sql.append("             ON z.JG_ID = jg.ID ");
		sql.append("            AND z.YXBZ = '1' ");
		sql.append("           LEFT JOIN zs_fzysws f ");
		sql.append("             on f.ZSJGID = jg.ID ");
		sql.append("            and f.YXBZ = '1' ");
		sql.append("           left join zs_cyry c ");
		sql.append("             on c.JG_ID = jg.ID ");
		sql.append("            and c.YXBZ = '1' ");
		sql.append("           left join zs_qtry q ");
		sql.append("             on q.JG_ID = jg.ID ");
		sql.append("            and q.qtryzt_dm = '1' ");
		sql.append("           left join zs_jg zjg ");
		sql.append("             on zjg.PARENTJGID = jg.ID ");
		sql.append("            and zjg.YZBM = '1' ");
		sql.append("          group by jg.ID) sl ");
		sql.append("  WHERE 1 = 1 ");
		sql.append("    AND d.ID = c.JGXZ_DM ");
		sql.append("    AND a.ZSJG_ID = c.ID ");
		sql.append("    and c.ID = sl.ID ");
		sql.append("    AND a.id = ? ");
		Map<String, Object> rs = jdbcTemplate.queryForMap(sql.toString(), id);
		return rs;
	}

	@Override
	public String addSwsnjb(Map<String, Object> obj) throws Exception {
		Integer jgid = (Integer) obj.get("jg_id");
		String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
		String xz = this.jdbcTemplate.queryForObject(sql,
				new Object[] { obj.get("jg_id") }, String.class);
		obj.put("xz", xz);
		final StringBuffer sb = new StringBuffer("insert into "
				+ Config.PROJECT_SCHEMA + "zs_jg_njb");
		sb.append(" ( ZSJG_ID, ND,ZSJGXZ_ID,ZJWGDM, NJZJ, SZ, ZCZJ, ZRS, ZYRS, YJYRS, SJJYRS, WJYRS,ZJ, FZR, ZCSWSBZJ, ZCSWSBJS,BAFS, FSS,ZDSJ,ZJSJ,ztdm,GDBDQKZJ,GDBDQKJS,FZRSJ) "
				+ "VALUES (:jg_id,:nd,:xz,:wg,:NJZJ, :sz, :zczj, :zrs, :zyrs, :yjyrs, :sjjyrs, :wjyrs, :ZJ, :FZR, :ZCSWSBZJ,:ZCSWSBJS,:bndbafs,:FSS,now(),now(),:ztdm,:GDBDQKZJ,:GDBDQKJS,:qzrq ) ");
		Number njid = this.insertAndGetKeyByNamedJdbc(sb.toString(), obj,
				new String[] { "id" });
		if (null == njid) {
			return null;
		} else {
			if ("2".equals(obj.get("ztbj"))) {
				Map<String, Object> spsq = new HashMap<>();// 设置生成审批表方法参数
				spsq.put("sid", njid);
				if (this.jdbcTemplate
						.queryForList(
								"select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",
								new Object[] { jgid }).size() == 0) {
					spsq.put("lclx", "40288087233c611801234b6fac3c01b3");
				} else {
					spsq.put("lclx", "40288087233c611801234b71cfc801b6");
				}
				spsq.put("jgid", jgid);
				spDao.swsSPqq(spsq);// 生成审批表记录
			}
			return njid.toString();
		}
	}

	@Override
	public void updateSwsnjb(Map<String, Object> obj) throws Exception  {
		Integer njid=(Integer) obj.get("id");
		Integer jgid=(Integer) obj.get("jg_id");
		String sql = "select jg.JGXZ_DM from zs_jg jg where jg.ID=? ";
		String xz = this.jdbcTemplate.queryForObject(sql,
				new Object[] { obj.get("jg_id") }, String.class);
		obj.put("xz", xz);
		StringBuffer sb = new StringBuffer("update " + Config.PROJECT_SCHEMA
				+ "zs_jg_njb ");

		sb.append(" set ZSJG_ID=:jg_id,ZSJGXZ_ID=:xz,ND =:nd,ZJWGDM=:wg,NJZJ=:NJZJ,GDBDQKZJ=:GDBDQKZJ,"
				+ "GDBDQKJS=:GDBDQKJS,ZRS=:ZRS,ZYRS=:zyrs,YJYRS=:yjyrs,SJJYRS=:sjjyrs, "
				+ "WJYRS=:wjyrs,ZJ=:ZJ,FZR=:FZR,ZCSWSBZJ=:ZCSWSBZJ, "
				+ "ZCSWSBJS=:ZCSWSBJS,FSS=:FSS," + "ztdm=:ztdm where id=:id ");

		NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		int count=named.update(sb.toString(), obj);
		if(count==1){
		if ("2".equals(obj.get("ztbj"))) {
			Map<String, Object> spsq = new HashMap<>();// 设置生成审批表方法参数
			spsq.put("sid", njid);
			if (this.jdbcTemplate
					.queryForList(
							"select id from zs_jg where parentjgid is not null and parentjgid>0 and id=?",
							new Object[] { jgid }).size() == 0) {
				spsq.put("lclx", "40288087233c611801234b6fac3c01b3");
			} else {
				spsq.put("lclx", "40288087233c611801234b71cfc801b6");
			}
			spsq.put("jgid", jgid);
			spDao.swsSPqq(spsq);// 生成审批表记录
		}
		}
	}

}
