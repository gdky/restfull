package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.gdky.restfull.dao.BaseJdbcDao;
import com.gdky.restfull.utils.HashIdUtil;

@Repository
public class YwglDao extends BaseJdbcDao {

	public Map<String, Object> getYwbb(int page, int pagesize,
			Condition condition) {

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT y.*,z.mc AS ywzt,l.mc AS ywlx,hy.mc AS hy,cs.mc AS cs,qx.mc AS qx ");
		sb.append(" FROM (zs_ywbb y,dm_ywbb_zt z,dm_ywlx l,  ");

		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql("zs_ywbb", "id"));
		sb.append("    ORDER BY bbrq desc  ");
		sb.append("    LIMIT ? , ?) sub) ");
		// ===> 插入查询条件集合结束
		sb.append(" left join dm_hy AS hy ");
		sb.append(" on y.hy_id = hy.id ");
		sb.append(" left join dm_cs AS cs ");
		sb.append(" on y.cs_dm = cs.id ");
		sb.append(" left join dm_cs AS qx ");
		sb.append(" on y.qx_dm = qx.id ");

		sb.append(" WHERE y.zt = z.id  ");
		sb.append(" AND y.ywlx_dm = l.id  ");
		sb.append(" AND sub.id = y.id ");
		sb.append(" ORDER BY y.bbrq desc ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				params.toArray(), new YwbbRowMapper());

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("id", "zs_ywbb");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public Map<String, Object> getYwbbById(long id) {
		String sql = "select * from " + Config.PROJECT_SCHEMA
				+ "zs_ywbb_old where id = ?";
		Map<String, Object> rs = jdbcTemplate.queryForMap(sql, id);
		return rs;
	}

	public List<Map<String, Object>> getYwbbMiscByJg(Long id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.XMING,z.ID as ZYSWS_ID ");
		sb.append(" from zs_jg j,zs_ryjbxx r,zs_zysws z ");
		sb.append(" where j.id = z.JG_ID ");
		sb.append(" and z.RY_ID = r.ID ");
		sb.append(" and j.ID=? ");
		sb.append(" and z.RYSPGCZT_DM = 1 ");
		sb.append(" and z.YXBZ=1 ");
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sb.toString(), new Object[] { id });
		return ls;
	}

	public void addYwbb(HashMap<String, Object> o) {
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into zs_ywbb ");
		sb.append(" (ND,BBRQ,BGWH,BGRQ,SFJE,JG_ID,SWSMC,SWSSWDJZH,WTDW,WTDWNSRSBH,XYH,YJFH,RJFH,SJFH, ");
		sb.append(" QZSWS,QMSWSID,TXDZ,SWSDZYJ,SWSWZ,YWLX_DM,JTXM,ZBRQ,ZGSWJG, ");
		sb.append(" SENDTIME,SSTARTTIME,MEMO,NSRXZ,HY_ID,ZSFS_DM,ISWS,SB_DM,CS_DM,QX_DM,CITY, ");
		sb.append(" WTDWXZ_DM,WTDWNSRSBHDF,WTDWLXR,WTDWLXDH,WTDXLXDZ,XYJE,CUSTOMER_ID,TZVALUE1,TJVALUE2, ");
		sb.append(" YZM,BBHM,IS_YD,ZT) ");
		sb.append(" values(:ND,:BBRQ,:BGWH,:BGRQ,:SFJE,:JG_ID,:SWSMC,:SWSSWDJZH,:WTDW,:WTDWNSRSBH,:XYH,:YJFH,:RJFH,:SJFH, ");
		sb.append(" :QZSWS,:QMSWSID,:TXDZ,:SWSDZYJ,:SWSWZ,:YWLX_DM,:JTXM,:ZBRQ,:ZGSWJG, ");
		sb.append(" :SENDTIME,:SSTARTTIME,:MEMO,:NSRXZ,:HY_ID,:ZSFS_DM,:ISWS,:SB_DM,:CS_DM,:QX_DM,:CITY, ");
		sb.append(" :WTDWXZ_DM,:WTDWNSRSBHDF,:WTDWLXR,:WTDWLXDH,:WTDXLXDZ,:XYJE,:CUSTOMER_ID,:TZVALUE1,:TJVALUE2, ");
		sb.append(" :YZM,:BBHM,:IS_YD,:ZT) ");
		this.namedParameterJdbcTemplate.update(sb.toString(), o);
	}

	public int getXyhNum(String xyh, Integer jgId) {
		String sql = "select id from zs_ywbb where xyh = ? and yxbz = 1 and jg_id = ?";
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,
				new Object[] { xyh,jgId });
		return ls.size();
	}

	public Map<String, Object> getYwbbByYzmAndBbhm(String bbhm, String yzm) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select y.*,z.mc as ywzt,l.mc as ywlx,hy.mc as hy,cs.mc as cs, ");
		sb.append(" qx.mc as qx ");
		sb.append(" from zs_ywbb y,dm_ywbb_zt z,dm_ywlx l,dm_hy hy,dm_cs cs,dm_cs qx ");
		sb.append(" where bbhm=? and yzm = ? ");
		sb.append(" and y.zt = z.id ");
		sb.append(" and y.ywlx_dm = l.id ");
		sb.append(" and y.hy_id = hy.id ");
		sb.append(" and y.cs_dm = cs.id ");
		sb.append(" and y.qx_dm = qx.id ");
		sb.append(" and y.yxbz = 1 ");
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				new Object[] { bbhm, yzm },
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("nd", rs.getObject("nd"));
						map.put("bbhm", rs.getObject("bbhm"));
						map.put("bbrq", rs.getDate("bbrq"));
						map.put("bgwh", rs.getString("bgwh"));
						map.put("zbrq", rs.getDate("zbrq"));
						map.put("yzm", rs.getString("yzm"));
						map.put("sfje", rs.getBigDecimal("sfje"));
						map.put("swsmc", rs.getString("swsmc"));
						map.put("swsswdjzh", rs.getString("swsswdjzh"));
						map.put("wtdw", rs.getString("wtdw"));
						map.put("wtdwnsrsbh", rs.getString("wtdwnsrsbh"));
						map.put("xyh", rs.getString("xyh"));
						map.put("yjfh", rs.getString("yjfh"));
						map.put("rjfh", rs.getString("rjfh"));
						map.put("sjfh", rs.getString("sjfh"));
						map.put("qzsws", rs.getString("qzsws"));
						map.put("txdz", rs.getString("txdz"));
						map.put("swsdzyj", rs.getString("swsdzyj"));
						map.put("swswz", rs.getString("swswz"));
						map.put("ywlx_dm", rs.getInt("ywlx_dm"));
						map.put("ywlx", rs.getString("ywlx"));
						map.put("jtxm", rs.getString("jtxm"));
						map.put("tzvalue1", rs.getBigDecimal("tzvalue1"));
						map.put("tjvalue2", rs.getBigDecimal("tjvalue2"));
						map.put("sstarttime", rs.getDate("sstarttime"));
						map.put("sendtime", rs.getDate("sendtime"));
						if (rs.getInt("nsrxz") == 0) {
							map.put("nsrxz", "一般纳税人");
						} else if (rs.getInt("nsrxz") == 1) {
							map.put("nsrxz", "小规模纳税人");
						} else {
							map.put("nsrxz", "非增值税纳税人");
						}
						if (rs.getInt("zsfs_dm") == 0) {
							map.put("zsfs", "查账征收");
						} else {
							map.put("zsfs", "核定征收");
						}
						if (rs.getString("ISWS") == null
								|| rs.getString("ISWS").equals("N")) {
							map.put("zsfs", "广东省");
						} else {
							map.put("zsfs", "外省");
						}
						if (rs.getInt("SB_DM") == 1) {
							map.put("sb", "国税");
						} else {
							map.put("sb", "地税");
						}
						map.put("hy", rs.getString("hy"));
						map.put("cs", rs.getString("cs"));
						map.put("qx", rs.getString("qx"));
						if (rs.getInt("WTDWXZ_DM") == 0) {
							map.put("wtdwxz", "居民企业");
						} else {
							map.put("wtdwxz", "非居民企业税");
						}
						map.put("wtdwnsrsbhdf", rs.getString("WTDWNSRSBHDF"));
						map.put("wtdwlxr", rs.getString("WTDWLXR"));
						map.put("wtdwlxdh", rs.getString("WTDWLXDH"));
						map.put("wtdxlxdz", rs.getString("WTDXLXDZ"));
						map.put("fphm", rs.getString("FPHM"));
						map.put("xyje", rs.getBigDecimal("XYJE"));
						map.put("sjsqje", rs.getBigDecimal("SJSQJE"));
						map.put("memo", rs.getString("MEMO"));
						map.put("zgswjg", rs.getString("ZGSWJG"));
						map.put("swsdh", rs.getString("SWSDH"));
						map.put("swscz", rs.getString("SWSCZ"));
						map.put("ywzt", rs.getString("ywzt"));

						return map;
					}
				});
		if (ls.size() > 0) {
			return ls.get(0);
		} else {
			return null;
		}

	}

	public void sentBack(Long id, Map<String, Object> data) {
		String sql = "update zs_ywbb set thyy = ?,zt=? where id = ? ";
		this.jdbcTemplate.update(sql,
				new Object[] { data.get("thyy"), data.get("zt"), id });
	}

	public void updateYwbbZT(Long id, int zt) {
		String sql = "update zs_ywbb set zt=? where id = ? ";
		this.jdbcTemplate.update(sql, new Object[] { zt, id });

	}

	public Number newRecordFromId(Long id, String bbhm, String yzm) {
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into zs_ywbb (BBHM,BBRQ,ND,BGWH,BGRQ,ZBRQ,YZM,SFJE,JG_ID,SWSMC, ");
		sb.append(" SWSSWDJZH,WTDW,WTDWNSRSBH,XYH,YJFH,RJFH,SJFH,QZSWS,TXDZ,SWSDZYJ,SWSWZ, ");
		sb.append(" YWLX_DM,JTXM,TZVALUE1,TJVALUE2,SSTARTTIME,SENDTIME,NSRXZ,HY_ID,ZSFS_DM, ");
		sb.append(" ISWS,SB_DM,CS_DM,QX_DM,WTDWXZ_DM,WTDWNSRSBHDF,WTDWLXR,WTDWLXDH,WTDXLXDZ, ");
		sb.append(" FPHM,XYJE,SJSQJE,XYZT_DM,MEMO,USER_ID,ZGSWJG,QMSWSID,SWSDH,SWSCZ,IS_YD, ");
		sb.append(" CUSTOMER_ID,SQTHYY,SQQYLY,THYY,ZT,YDSPZT,SWBZ,YXBZ) ");
		sb.append(" select ?,BBRQ,ND,BGWH,BGRQ,ZBRQ,?,SFJE,JG_ID,SWSMC, ");
		sb.append(" SWSSWDJZH,WTDW,WTDWNSRSBH,XYH,YJFH,RJFH,SJFH,QZSWS,TXDZ,SWSDZYJ,SWSWZ, ");
		sb.append(" YWLX_DM,JTXM,TZVALUE1,TJVALUE2,SSTARTTIME,SENDTIME,NSRXZ,HY_ID,ZSFS_DM, ");
		sb.append(" ISWS,SB_DM,CS_DM,QX_DM,WTDWXZ_DM,WTDWNSRSBHDF,WTDWLXR,WTDWLXDH,WTDXLXDZ, ");
		sb.append(" FPHM,XYJE,SJSQJE,XYZT_DM,MEMO,USER_ID,ZGSWJG,QMSWSID,SWSDH,SWSCZ,IS_YD, ");
		sb.append(" CUSTOMER_ID,SQTHYY,SQQYLY,THYY,?,YDSPZT,SWBZ,YXBZ  ");
		sb.append(" from zs_ywbb where id = ? ");
		Number idNum = this.insertAndGetKeyByJdbc(sb.toString(), new Object[] {
				bbhm, yzm, 0, id }, new String[] { "id" });
		return idNum;
	}

	public Map<String, Object> getYwbbNDBTYJ(int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT SQL_CALC_FOUND_ROWS  @rownum := IF(@rowtype = customer_id, @rownum + 1, 1) AS row_number, @rowtype := customer_id AS dummy, t .* ");
		sb.append(" FROM ( ");
		sb.append(" select a.*,z.mc AS ywzt,l.mc AS ywlx,hy.mc AS hy,cs.mc AS cs,qx.mc AS qx ");
		sb.append(" from (zs_ywbb a,dm_ywbb_zt z,dm_ywlx l, ");
		// <=== 查询条件集合
		sb.append(" ( " + condition.getSelectSql("zs_ywbb", "id"));
		sb.append("    ORDER BY id) sub) ");
		// ===> 插入查询条件集合结束
		sb.append(" LEFT JOIN zs_ywbb b ON (a.customer_id = b.customer_id AND a.YWLX_DM = b.YWLX_DM) ");
		sb.append(" LEFT JOIN dm_hy AS hy ON a.hy_id = hy.id ");
		sb.append(" LEFT JOIN dm_cs AS cs ON a.cs_dm = cs.id ");
		sb.append(" LEFT JOIN dm_cs AS qx ON a.qx_dm = qx.id ");
		sb.append(" WHERE a.id = sub.id AND a.ND != b. nd AND a.JG_ID != b. JG_ID AND a.zt = z. id AND a.ywlx_dm = l.id AND (a.zt= 1 OR a.zt = 3) ");
		sb.append(" GROUP BY a.id ");
		sb.append(" ORDER BY a.customer_id, a.nd DESC) t,( ");
		sb.append(" SELECT @rownum:= 1) tmp_row, ( ");
		sb.append(" SELECT @rowtype := '') tmp_type ");
		sb.append(" HAVING row_number <= 2  ");
		sb.append(" limit ?,? ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				params.toArray(), new YwbbRowMapper());

		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public Map<String, Object> getYwbbWTFYJ(int page, int pagesize,
			Condition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT   SQL_CALC_FOUND_ROWS a.*, z.mc AS ywzt,l.mc AS ywlx,hy.mc AS hy, cs.mc AS cs,qx.mc AS qx ");
		sb.append(" from (zs_ywbb a,dm_ywbb_zt z,dm_ywlx l, ");
		// <=== 查询条件集合
		sb.append(" ( " + condition.getSelectSql("zs_ywbb", "id"));
		sb.append("    ORDER BY id) sub) ");
		// ===> 插入查询条件集合结束
		sb.append(" LEFT JOIN zs_ywbb b ON (a.customer_id = b.customer_id AND a.nd = b.nd) ");
		sb.append(" LEFT JOIN dm_hy AS hy ON a.hy_id = hy.id ");
		sb.append(" LEFT JOIN dm_cs AS cs ON a.cs_dm = cs.id ");
		sb.append(" LEFT JOIN dm_cs AS qx ON a.qx_dm = qx.id ");
		sb.append(" WHERE a.id = sub.id AND a.ywlx_dm != b.ywlx_dm AND a.JG_ID != b. JG_ID AND a.zt = z. id AND a.ywlx_dm = l.id AND (a.zt= 1 OR a.zt = 3) ");
		sb.append(" GROUP BY a.id ");
		sb.append(" ORDER BY a.customer_id, a.nd DESC ");
		sb.append(" limit ?,? ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				params.toArray(), new YwbbRowMapper());

		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public class YwbbRowMapper implements RowMapper<Map<String, Object>> {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int arg1)
				throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", HashIdUtil.encode(rs.getLong("id")));
			map.put("nd", rs.getObject("nd"));
			map.put("bbhm", rs.getObject("bbhm"));
			map.put("bbrq", rs.getDate("bbrq"));
			map.put("bgwh", rs.getString("bgwh"));
			map.put("bgrq",rs.getDate("bgrq"));
			map.put("zbrq", rs.getDate("zbrq"));
			map.put("yzm", rs.getString("yzm"));
			map.put("sfje", rs.getBigDecimal("sfje"));
			map.put("swsmc", rs.getString("swsmc"));
			map.put("swsswdjzh", rs.getString("swsswdjzh"));
			map.put("wtdw", rs.getString("wtdw"));
			map.put("wtdwnsrsbh", rs.getString("wtdwnsrsbh"));
			map.put("xyh", rs.getString("xyh"));
			map.put("yjfh", rs.getString("yjfh"));
			map.put("rjfh", rs.getString("rjfh"));
			map.put("sjfh", rs.getString("sjfh"));
			map.put("qzsws", rs.getString("qzsws"));
			map.put("txdz", rs.getString("txdz"));
			map.put("swsdzyj", rs.getString("swsdzyj"));
			map.put("swswz", rs.getString("swswz"));
			map.put("ywlx_dm", rs.getInt("ywlx_dm"));
			map.put("ywlx", rs.getString("ywlx"));
			map.put("jtxm", rs.getString("jtxm"));
			map.put("tzvalue1", rs.getBigDecimal("tzvalue1"));
			map.put("tjvalue2", rs.getBigDecimal("tjvalue2"));
			map.put("sstarttime", rs.getDate("sstarttime"));
			map.put("sendtime", rs.getDate("sendtime"));
			if (rs.getInt("nsrxz") == 0) {
				map.put("nsrxz", "一般纳税人");
			} else if (rs.getInt("nsrxz") == 1) {
				map.put("nsrxz", "小规模纳税人");
			} else {
				map.put("nsrxz", "非增值税纳税人");
			}
			if (rs.getInt("zsfs_dm") == 0) {
				map.put("zsfs", "查账征收");
			} else {
				map.put("zsfs", "核定征收");
			}
			if (rs.getString("ISWS") == null
					|| rs.getString("ISWS").equals("N")) {
				map.put("dq", "广东省");
			} else {
				map.put("dq", "外省");
			}
			if (rs.getInt("SB_DM") == 1) {
				map.put("sb", "国税");
			} else {
				map.put("sb", "地税");
			}
			map.put("hy", rs.getString("hy"));
			map.put("cs", rs.getString("cs"));
			map.put("qx", rs.getString("qx"));
			map.put("city", rs.getString("city"));
			if (rs.getInt("WTDWXZ_DM") == 0) {
				map.put("wtdwxz", "居民企业");
			} else {
				map.put("wtdwxz", "非居民企业税");
			}
			if (rs.getString("is_yd") == null
					|| rs.getString("is_yd").equals("N")) {
				map.put("is_yd", "非异地报备");
			} else {
				map.put("is_yd", "异地报备");
			}
			map.put("wtdwnsrsbhdf", rs.getString("WTDWNSRSBHDF"));
			map.put("wtdwlxr", rs.getString("WTDWLXR"));
			map.put("wtdwlxdh", rs.getString("WTDWLXDH"));
			map.put("wtdxlxdz", rs.getString("WTDXLXDZ"));
			map.put("fphm", rs.getString("FPHM"));
			map.put("xyje", rs.getBigDecimal("XYJE"));
			map.put("sjsqje", rs.getBigDecimal("SJSQJE"));
			map.put("memo", rs.getString("MEMO"));
			map.put("zgswjg", rs.getString("ZGSWJG"));
			map.put("swsdh", rs.getString("SWSDH"));
			map.put("swscz", rs.getString("SWSCZ"));
			map.put("ywzt", rs.getString("ywzt"));
			map.put("ywzt_dm", rs.getInt("zt"));
			map.put("sqthyy", rs.getString("sqthyy"));
			map.put("sqqyly", rs.getString("sqqyly"));

			return map;
		}
	}

	/**
	 * 业务报备数据分析
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjfx(HashMap<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> params = new ArrayList<>();
		String fsnd = null;
		String bbnd = null;
		String ywlx = null;
		String type = "yw";
		if (null != map.get("fsnd")) {
			fsnd = map.get("fsnd").toString();
		}
		if (null != map.get("bbnd")) {
			bbnd = map.get("bbnd").toString();
		}
		if (null != map.get("ywlx")) {
			ywlx = map.get("ywlx").toString();
		}
		if (null != map.get("type")) {
			type = map.get("type").toString();
		}
		if ("yw".equals(type)) {
			sql = new StringBuffer(
					"select sql_calc_found_rows @rownum := @rownum + 1 as 'xh',xj.* from (");
			sql.append(" SELECT cs.cs_dm, ");
			sql.append("        cs.mc dq, ");
			sql.append("        count(distinct y.JG_ID) swshs, ");
			sql.append("        count(y.WTDWNSRSBH) wtdwhs, ");
			sql.append("        sum(ifnull(y.XYJE,0)) xysfzje, ");
			sql.append("        sum(ifnull(y.SJSQJE,0)) sjsfzje, ");
			sql.append("        count(distinct(if(y.YWLX_DM='1',y.JG_ID,null))) swshs_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.XYJE,0),0)) xysfzje_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.SJSQJE,0),0)) sjsfzje_jskf, ");
			sql.append("        count(distinct(if(y.YWLX_DM='2',y.JG_ID,null))) swshs_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.XYJE,0),0)) xysfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.SJSQJE,0),0)) sjsfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.TZVALUE1,0),0)) sqkcxmje_sqkc, ");
			sql.append("        count(distinct(if(y.YWLX_DM='3',y.JG_ID,null))) swshs_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.XYJE,0),0)) xysfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.SJSQJE,0),0)) sjsfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TZVALUE1,0),0)) nstzzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TJVALUE2,0),0)) nstzjse_sdshsqj, ");
			sql.append("        count(distinct(if(y.YWLX_DM='4',y.JG_ID,null))) swshs_td, ");
			sql.append("        sum(if(y.YWLX_DM='4' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.XYJE,0),0)) xysfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.SJSQJE,0),0)) sjsfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TZVALUE1,0),0)) ybse_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TJVALUE2,0),0)) ytse_td, ");
			sql.append("        count(distinct(if(y.YWLX_DM='5',y.JG_ID,null))) swshs_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.XYJE,0),0)) xysfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.SJSQJE,0),0)) sjsfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TZVALUE1,0),0)) ybse_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TJVALUE2,0),0)) ytse_fdc, ");
			sql.append("        count(distinct(if(y.YWLX_DM='6',y.JG_ID,null))) swshs_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.XYJE,0),0)) xysfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.SJSQJE,0),0)) sjsfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TZVALUE1,0),0)) ybse_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TJVALUE2,0),0)) ytse_qt, ");
			sql.append("        count(distinct(if(y.YWLX_DM='7',y.JG_ID,null))) swshs_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.XYJE,0),0)) xysfzje_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.SJSQJE,0),0)) sjsfzje_gxjs, ");
			sql.append("        count(distinct(if(y.YWLX_DM='8',y.JG_ID,null))) swshs_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.XYJE,0),0)) xysfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.SJSQJE,0),0)) sjsfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TZVALUE1,0),0)) ybse_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TJVALUE2,0),0)) ytse_zx, ");
			sql.append("        count(distinct(if(y.YWLX_DM='9',y.JG_ID,null))) swshs_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.XYJE,0),0)) xysfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.SJSQJE,0),0)) sjsfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TZVALUE1,0),0)) ybse_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TJVALUE2,0),0)) ytse_bg, ");
			sql.append("        count(distinct(if(y.YWLX_DM='10',y.JG_ID,null))) swshs_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.XYJE,0),0)) xysfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.SJSQJE,0),0)) sjsfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TZVALUE1,0),0)) nstzzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TJVALUE2,0),0)) nstzjse_grhsqj ");
			sql.append("   FROM zs_ywbb y, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  WHERE y.CS_DM = cs.id ");
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("  group by cs.cs_dm, cs.mc ");
			sql.append("  order by cs.cs_dm");
			sql.append("  )xj,(select @rownum := 0) xh");
		} else if ("sws".equals(type)) {
			sql = new StringBuffer(
					"select sql_calc_found_rows @rownum := @rownum + 1 as 'xh',xj.* from (");
			sql.append(" SELECT cs.cs_dm, ");
			sql.append("        cs.mc dq, ");
			sql.append("        count(distinct y.JG_ID) swshs, ");
			sql.append("        count(y.WTDWNSRSBH) wtdwhs, ");
			sql.append("        sum(ifnull(y.XYJE,0)) xysfzje, ");
			sql.append("        sum(ifnull(y.SJSQJE,0)) sjsfzje, ");
			sql.append("        count(distinct(if(y.YWLX_DM='1',y.JG_ID,null))) swshs_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.XYJE,0),0)) xysfzje_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.SJSQJE,0),0)) sjsfzje_jskf, ");
			sql.append("        count(distinct(if(y.YWLX_DM='2',y.JG_ID,null))) swshs_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.XYJE,0),0)) xysfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.SJSQJE,0),0)) sjsfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.TZVALUE1,0),0)) sqkcxmje_sqkc, ");
			sql.append("        count(distinct(if(y.YWLX_DM='3',y.JG_ID,null))) swshs_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.XYJE,0),0)) xysfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.SJSQJE,0),0)) sjsfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TZVALUE1,0),0)) nstzzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TJVALUE2,0),0)) nstzjse_sdshsqj, ");
			sql.append("        count(distinct(if(y.YWLX_DM='4',y.JG_ID,null))) swshs_td, ");
			sql.append("        sum(if(y.YWLX_DM='4' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.XYJE,0),0)) xysfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.SJSQJE,0),0)) sjsfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TZVALUE1,0),0)) ybse_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TJVALUE2,0),0)) ytse_td, ");
			sql.append("        count(distinct(if(y.YWLX_DM='5',y.JG_ID,null))) swshs_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.XYJE,0),0)) xysfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.SJSQJE,0),0)) sjsfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TZVALUE1,0),0)) ybse_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TJVALUE2,0),0)) ytse_fdc, ");
			sql.append("        count(distinct(if(y.YWLX_DM='6',y.JG_ID,null))) swshs_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.XYJE,0),0)) xysfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.SJSQJE,0),0)) sjsfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TZVALUE1,0),0)) ybse_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TJVALUE2,0),0)) ytse_qt, ");
			sql.append("        count(distinct(if(y.YWLX_DM='7',y.JG_ID,null))) swshs_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.XYJE,0),0)) xysfzje_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.SJSQJE,0),0)) sjsfzje_gxjs, ");
			sql.append("        count(distinct(if(y.YWLX_DM='8',y.JG_ID,null))) swshs_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.XYJE,0),0)) xysfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.SJSQJE,0),0)) sjsfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TZVALUE1,0),0)) ybse_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TJVALUE2,0),0)) ytse_zx, ");
			sql.append("        count(distinct(if(y.YWLX_DM='9',y.JG_ID,null))) swshs_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.XYJE,0),0)) xysfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.SJSQJE,0),0)) sjsfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TZVALUE1,0),0)) ybse_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TJVALUE2,0),0)) ytse_bg, ");
			sql.append("        count(distinct(if(y.YWLX_DM='10',y.JG_ID,null))) swshs_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.XYJE,0),0)) xysfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.SJSQJE,0),0)) sjsfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TZVALUE1,0),0)) nstzzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TJVALUE2,0),0)) nstzjse_grhsqj ");
			sql.append("   FROM zs_ywbb y,zs_jg jg, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  WHERE y.jg_id=jg.ID ");
			sql.append("    and jg.CS_DM=cs.id");
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("  group by cs.cs_dm, cs.mc ");
			sql.append("  order by cs.cs_dm");
			sql.append("  )xj,(select @rownum := 0) xh");
		}
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob;
	}

	/**
	 * 业务报备数据分析
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjfxDq(HashMap<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> params = new ArrayList<>();
		String fsnd = null;
		String bbnd = null;
		String ywlx = null;
		String type = "yw";
		String cxdq = "1";
		if (null != map.get("fsnd")) {
			fsnd = map.get("fsnd").toString();
		}
		if (null != map.get("bbnd")) {
			bbnd = map.get("bbnd").toString();
		}
		if (null != map.get("ywlx")) {
			ywlx = map.get("ywlx").toString();
		}
		if (null != map.get("type")) {
			type = map.get("type").toString();
		}
		if (null != map.get("cxdq")) {
			cxdq = map.get("cxdq").toString();
		}
		if ("yw".equals(type)) {
			sql = new StringBuffer(
					" select sql_calc_found_rows @rownum := @rownum + 1 as 'xh',xj.* from( ");
			sql.append(" SELECT y.jg_id, ");
			sql.append("        jg.dwmc,  ");
			sql.append("        count(y.WTDWNSRSBH) wtdwhs, ");
			sql.append("        sum(ifnull(y.XYJE,0)) xysfzje, ");
			sql.append("        sum(ifnull(y.SJSQJE,0)) sjsfzje,  ");
			sql.append("        sum(if(y.YWLX_DM='1' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.XYJE,0),0)) xysfzje_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.SJSQJE,0),0)) sjsfzje_jskf,  ");
			sql.append("        sum(if(y.YWLX_DM='2' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.XYJE,0),0)) xysfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.SJSQJE,0),0)) sjsfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.TZVALUE1,0),0)) sqkcxmje_sqkc,  ");
			sql.append("        sum(if(y.YWLX_DM='3' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.XYJE,0),0)) xysfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.SJSQJE,0),0)) sjsfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TZVALUE1,0),0)) nstzzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TJVALUE2,0),0)) nstzjse_sdshsqj,  ");
			sql.append("        sum(if(y.YWLX_DM='4' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.XYJE,0),0)) xysfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.SJSQJE,0),0)) sjsfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TZVALUE1,0),0)) ybse_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TJVALUE2,0),0)) ytse_td,  ");
			sql.append("        sum(if(y.YWLX_DM='5' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.XYJE,0),0)) xysfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.SJSQJE,0),0)) sjsfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TZVALUE1,0),0)) ybse_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TJVALUE2,0),0)) ytse_fdc,  ");
			sql.append("        sum(if(y.YWLX_DM='6' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.XYJE,0),0)) xysfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.SJSQJE,0),0)) sjsfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TZVALUE1,0),0)) ybse_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TJVALUE2,0),0)) ytse_qt,  ");
			sql.append("        sum(if(y.YWLX_DM='7' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.XYJE,0),0)) xysfzje_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.SJSQJE,0),0)) sjsfzje_gxjs,  ");
			sql.append("        sum(if(y.YWLX_DM='8' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.XYJE,0),0)) xysfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.SJSQJE,0),0)) sjsfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TZVALUE1,0),0)) ybse_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TJVALUE2,0),0)) ytse_zx,  ");
			sql.append("        sum(if(y.YWLX_DM='9' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.XYJE,0),0)) xysfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.SJSQJE,0),0)) sjsfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TZVALUE1,0),0)) ybse_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TJVALUE2,0),0)) ytse_bg,  ");
			sql.append("        sum(if(y.YWLX_DM='10' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.XYJE,0),0)) xysfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.SJSQJE,0),0)) sjsfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TZVALUE1,0),0)) nstzzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TJVALUE2,0),0)) nstzjse_grhsqj ");
			sql.append("   FROM zs_ywbb y,zs_jg jg, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  WHERE y.CS_DM = cs.id ");
			sql.append("    and y.JG_ID = jg.ID ");
			sql.append("    and cs.cs_dm= ? ");
			params.add(cxdq);
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("  group by y.JG_ID,jg.DWMC ");
			sql.append("  order by y.JG_ID ");
			sql.append(" )xj,(select @rownum := 0) xh ");
		} else if ("sws".equals(type)) {
			sql = new StringBuffer(
					" select sql_calc_found_rows @rownum := @rownum + 1 as 'xh',xj.* from( ");
			sql.append(" SELECT y.jg_id, ");
			sql.append("        jg.dwmc,  ");
			sql.append("        count(y.WTDWNSRSBH) wtdwhs, ");
			sql.append("        sum(ifnull(y.XYJE,0)) xysfzje, ");
			sql.append("        sum(ifnull(y.SJSQJE,0)) sjsfzje,  ");
			sql.append("        sum(if(y.YWLX_DM='1' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.XYJE,0),0)) xysfzje_jskf, ");
			sql.append("        sum(if(y.YWLX_DM='1',ifnull(y.SJSQJE,0),0)) sjsfzje_jskf,  ");
			sql.append("        sum(if(y.YWLX_DM='2' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.XYJE,0),0)) xysfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.SJSQJE,0),0)) sjsfzje_sqkc, ");
			sql.append("        sum(if(y.YWLX_DM='2',ifnull(y.TZVALUE1,0),0)) sqkcxmje_sqkc,  ");
			sql.append("        sum(if(y.YWLX_DM='3' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.XYJE,0),0)) xysfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.SJSQJE,0),0)) sjsfzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TZVALUE1,0),0)) nstzzje_sdshsqj, ");
			sql.append("        sum(if(y.YWLX_DM='3',ifnull(y.TJVALUE2,0),0)) nstzjse_sdshsqj,  ");
			sql.append("        sum(if(y.YWLX_DM='4' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.XYJE,0),0)) xysfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.SJSQJE,0),0)) sjsfzje_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TZVALUE1,0),0)) ybse_td, ");
			sql.append("        sum(if(y.YWLX_DM='4',ifnull(y.TJVALUE2,0),0)) ytse_td,  ");
			sql.append("        sum(if(y.YWLX_DM='5' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.XYJE,0),0)) xysfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.SJSQJE,0),0)) sjsfzje_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TZVALUE1,0),0)) ybse_fdc, ");
			sql.append("        sum(if(y.YWLX_DM='5',ifnull(y.TJVALUE2,0),0)) ytse_fdc,  ");
			sql.append("        sum(if(y.YWLX_DM='6' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.XYJE,0),0)) xysfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.SJSQJE,0),0)) sjsfzje_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TZVALUE1,0),0)) ybse_qt, ");
			sql.append("        sum(if(y.YWLX_DM='6',ifnull(y.TJVALUE2,0),0)) ytse_qt,  ");
			sql.append("        sum(if(y.YWLX_DM='7' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.XYJE,0),0)) xysfzje_gxjs, ");
			sql.append("        sum(if(y.YWLX_DM='7',ifnull(y.SJSQJE,0),0)) sjsfzje_gxjs,  ");
			sql.append("        sum(if(y.YWLX_DM='8' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.XYJE,0),0)) xysfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.SJSQJE,0),0)) sjsfzje_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TZVALUE1,0),0)) ybse_zx, ");
			sql.append("        sum(if(y.YWLX_DM='8',ifnull(y.TJVALUE2,0),0)) ytse_zx,  ");
			sql.append("        sum(if(y.YWLX_DM='9' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.XYJE,0),0)) xysfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.SJSQJE,0),0)) sjsfzje_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TZVALUE1,0),0)) ybse_bg, ");
			sql.append("        sum(if(y.YWLX_DM='9',ifnull(y.TJVALUE2,0),0)) ytse_bg,  ");
			sql.append("        sum(if(y.YWLX_DM='10' and y.WTDWNSRSBH is not null,1,0)) wtdwhs_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.XYJE,0),0)) xysfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.SJSQJE,0),0)) sjsfzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TZVALUE1,0),0)) nstzzje_grhsqj, ");
			sql.append("        sum(if(y.YWLX_DM='10',ifnull(y.TJVALUE2,0),0)) nstzjse_grhsqj ");
			sql.append("   FROM zs_ywbb y,zs_jg jg, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  WHERE y.JG_ID = jg.ID ");
			sql.append("    and jg.CS_DM = cs.id ");
			sql.append("    and cs.cs_dm = ? ");
			params.add(cxdq);
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("  group by y.JG_ID,jg.DWMC ");
			sql.append("  order by y.JG_ID ");
			sql.append(" )xj,(select @rownum := 0) xh ");
		}
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob;
	}

	public Map<String, Object> getYwbbsjfxSws(HashMap<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> params = new ArrayList<>();
		String fsnd = null;
		String bbnd = null;
		String ywlx = null;
		String type = "yw";
		String cxdq = "1";
		String cxsws = null;
		String cxswsmc = "";
		String cxnd = "统计年度：";
		if (null != map.get("fsnd")) {
			fsnd = map.get("fsnd").toString();
		}
		if (null != map.get("bbnd")) {
			bbnd = map.get("bbnd").toString();
		}
		if (null != map.get("ywlx")) {
			ywlx = map.get("ywlx").toString();
		}
		if (null != map.get("type")) {
			type = map.get("type").toString();
		}
		if (null != map.get("cxdq")) {
			cxdq = map.get("cxdq").toString();
		}
		if (null != map.get("cxsws")) {
			cxsws = map.get("cxsws").toString();
		}
		String swsSql = "select jg.DWMC from zs_jg jg where jg.ID=? ";
		cxswsmc = this.jdbcTemplate.queryForObject(swsSql, String.class, cxsws);
		if (null != fsnd) {
			cxnd += fsnd;
		} else if (null != bbnd) {
			cxnd += bbnd;
		}
		if ("yw".equals(type)) {
			sql = new StringBuffer(" SELECT y.jg_id, ");
			sql.append("        jg.dwmc sws, ");
			sql.append("        y.YWLX_DM ywlx_dm, ");
			sql.append("        lx.MC ywlx ");
			sql.append("   FROM zs_ywbb y, ");
			sql.append("        zs_jg jg, ");
			sql.append("        dm_ywlx lx, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  WHERE y.CS_DM = cs.id ");
			sql.append("    AND y.JG_ID = jg.ID ");
			sql.append("    and y.YWLX_DM=lx.ID ");
			sql.append("    AND cs.cs_dm = ?  ");
			sql.append("    AND y.JG_ID = ? ");
			params.add(cxdq);
			params.add(cxsws);
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("    group by y.JG_ID,jg.DWMC,y.YWLX_DM,lx.MC ");
		} else if ("sws".equals(type)) {
			sql = new StringBuffer(" SELECT y.jg_id, ");
			sql.append("        jg.dwmc sws, ");
			sql.append("        y.YWLX_DM ywlx_dm, ");
			sql.append("        lx.MC ywlx ");
			sql.append("   FROM zs_ywbb y, ");
			sql.append("        zs_jg jg, ");
			sql.append("        dm_ywlx lx, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  WHERE jg.CS_DM = cs.id ");
			sql.append("    AND y.JG_ID = jg.ID ");
			sql.append("    and y.YWLX_DM=lx.ID ");
			sql.append("    AND cs.cs_dm = ?  ");
			sql.append("    AND y.JG_ID = ? ");
			params.add(cxdq);
			params.add(cxsws);
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("    group by y.JG_ID,jg.DWMC,y.YWLX_DM,lx.MC ");
		}
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		Map<String, Object> ob = new HashMap<>();
		ob.put("sws", cxswsmc);
		ob.put("nd", cxnd);
		ob.put("list", ls);
		return ob;
	}

	public Map<String, Object> getYwbbsjfxYwlx(HashMap<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> params = new ArrayList<>();
		String fsnd = null;
		String bbnd = null;
		String ywlx = null;
		String type = "yw";
		String cxdq = "1";
		String cxsws = null;
		String cxywlx = null;
		if (null != map.get("fsnd")) {
			fsnd = map.get("fsnd").toString();
		}
		if (null != map.get("bbnd")) {
			bbnd = map.get("bbnd").toString();
		}
		if (null != map.get("ywlx")) {
			ywlx = map.get("ywlx").toString();
		}
		if (null != map.get("type")) {
			type = map.get("type").toString();
		}
		if (null != map.get("cxdq")) {
			cxdq = map.get("cxdq").toString();
		}
		if (null != map.get("cxsws")) {
			cxsws = map.get("cxsws").toString();
		}
		if (null != map.get("cxywlx")) {
			cxywlx = map.get("cxywlx").toString();
		}
		if ("yw".equals(type)) {
			sql = new StringBuffer(
					" select sql_calc_found_rows @rownum := @rownum + 1 as 'xh',xj.* from( ");
			sql.append(" select y.jg_id, ");
			sql.append("        jg.dwmc, ");
			sql.append("        y.wtdw qymc, ");
			sql.append("        y.swsswdjzh, ");
			sql.append("        c.mc ywfsd, ");
			sql.append("        y.bgwh, ");
			sql.append("        y.bbhm, ");
			sql.append("        y.yjfh, ");
			sql.append("        y.rjfh, ");
			sql.append("        y.sjfh, ");
			sql.append("        y.qzsws, ");
			sql.append("        ifnull(y.xyje,0) xyje, ");
			sql.append("        ifnull(y.sjsqje,0) sjsqje, ");
			sql.append("        ifnull(y.tzvalue1,0) ybse, ");
			sql.append("        ifnull(y.tjvalue2,0) ytse, ");
			sql.append("        date_format(y.bbrq,'%Y-%m-%d') bbrq ");
			sql.append("   from zs_ywbb y, ");
			sql.append("        zs_jg jg, ");
			sql.append("        dm_cs c, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  where y.jg_id = jg.id ");
			sql.append("    and y.cs_dm = c.id ");
			sql.append("    and y.CS_DM = cs.id ");
			sql.append("    AND cs.cs_dm =  ? ");
			sql.append("    AND y.JG_ID =   ? ");
			sql.append("    and y.ywlx_dm = ? ");
			params.add(cxdq);
			params.add(cxsws);
			params.add(cxywlx);
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("  order by y.BBRQ ");
			sql.append(" )xj,(select @rownum := 0) xh  ");
		} else if ("sws".equals(type)) {
			sql = new StringBuffer(
					" select sql_calc_found_rows @rownum := @rownum + 1 as 'xh',xj.* from( ");
			sql.append(" select y.jg_id, ");
			sql.append("        jg.dwmc, ");
			sql.append("        y.wtdw qymc, ");
			sql.append("        y.swsswdjzh, ");
			sql.append("        c.mc ywfsd, ");
			sql.append("        y.bgwh, ");
			sql.append("        y.bbhm, ");
			sql.append("        y.yjfh, ");
			sql.append("        y.rjfh, ");
			sql.append("        y.sjfh, ");
			sql.append("        y.qzsws, ");
			sql.append("        ifnull(y.xyje,0) xyje, ");
			sql.append("        ifnull(y.sjsqje,0) sjsqje, ");
			sql.append("        ifnull(y.tzvalue1,0) ybse, ");
			sql.append("        ifnull(y.tjvalue2,0) ytse, ");
			sql.append("        date_format(y.bbrq,'%Y-%m-%d') bbrq ");
			sql.append("   from zs_ywbb y, ");
			sql.append("        zs_jg jg, ");
			sql.append("        dm_cs c, ");
			sql.append("        (SELECT c.ID, c.MC, c.ID cs_dm ");
			sql.append("           FROM dm_cs c ");
			sql.append("          WHERE c.parent_id = '0' ");
			sql.append("         UNION ");
			sql.append("         SELECT c.ID, pc.MC, pc.ID cs_dm ");
			sql.append("           FROM dm_cs c, dm_cs pc ");
			sql.append("          WHERE c.PARENT_ID = pc.ID ");
			sql.append("            AND pc.PARENT_ID = '0') cs ");
			sql.append("  where y.jg_id = jg.id ");
			sql.append("    and y.cs_dm = c.id ");
			sql.append("    and jg.CS_DM = cs.id ");
			sql.append("    AND cs.cs_dm =  ? ");
			sql.append("    AND y.JG_ID =   ? ");
			sql.append("    and y.ywlx_dm = ? ");
			params.add(cxdq);
			params.add(cxsws);
			params.add(cxywlx);
			if (null != fsnd) {
				sql.append(" and y.nd = ? ");
				params.add(fsnd);
			}
			if (null != bbnd) {
				sql.append(" and year(y.BBRQ) = ? ");
				params.add(bbnd);
			}
			if (null != ywlx) {
				sql.append(" and y.YWLX_DM = ? ");
				params.add(ywlx);
			}
			sql.append("  order by y.BBRQ ");
			sql.append(" )xj,(select @rownum := 0) xh  ");
		}
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		return ob;
	}

	/**
	 * 业务报备数据汇总-机构人员报备数据分析
	 * 
	 * @param page
	 * @param pagesize
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzRy(int page, int pagesize,
			HashMap<String, Object> map) {
		StringBuffer hzSql = new StringBuffer();
		StringBuffer sjSql = new StringBuffer();
		ArrayList<Object> params = new ArrayList<>();
		String bbnd = "";
		String jgId = "";
		if (null != map.get("bbnd")) {
			bbnd = (map.get("bbnd")).toString();
		}
		if (null != map.get("jgId")) {
			jgId = ((Integer) map.get("jgId")).toString();
		}
		List<Map<String, Object>> hzls = this.getJgRyYwbbsjhz(bbnd, jgId);

		sjSql = new StringBuffer(" select SQL_CALC_FOUND_ROWS b.id, ");
		sjSql.append("        b.qzsws, ");
		sjSql.append("        b.wtdw, ");
		sjSql.append("        b.bgwh, ");
		sjSql.append("        lx.mc ywlx, ");
		sjSql.append("        ifnull(b.xyje,0) xyje, ");
		sjSql.append("        ifnull(b.sjsqje,0) sjsqje, ");
		sjSql.append("        date_format(b.bbrq,'%Y-%m-%d') bbrq  ");
		sjSql.append("   from dm_ywlx lx, zs_ywbb b ");
		sjSql.append("  where lx.id = b.ywlx_dm ");
		sjSql.append("    and year(b.bbrq) = ? ");
		sjSql.append("    and b.jg_id = ? ");
		sjSql.append("  order by b.bbrq desc ");
		sjSql.append("     limit ?,? ");
		params.add(bbnd);
		params.add(jgId);
		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> sjls = this.jdbcTemplate.queryForList(
				sjSql.toString(), params.toArray());

		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, Integer.class);

		Map<String, Object> ob = new HashMap<>();
		ob.put("dataHz", hzls);
		ob.put("dataSj", sjls);
		ob.put("total", total);
		ob.put("pagesize", pagesize);
		ob.put("current", page);
		return ob;
	}

	/**
	 * 根据提交报备的年度和机构id对机构人员各个业务类型的报备数据进行统计
	 * 
	 * @param bbnd
	 * @param jgId
	 * @return
	 */
	private List<Map<String, Object>> getJgRyYwbbsjhz(String bbnd, String jgId) {
		ArrayList<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql = new StringBuffer(" select r.id, ");
		sql.append("        r.xming ry, ");
		sql.append("        sum(if(b.ywlx_dm='1', 1, 0)) jskf, ");
		sql.append("        sum(if(b.ywlx_dm='2', 1, 0)) sqkc, ");
		sql.append("        sum(if(b.ywlx_dm='3', 1, 0)) qyhsqj, ");
		sql.append("        sum(if(b.ywlx_dm='4', 1, 0)) td, ");
		sql.append("        sum(if(b.ywlx_dm='5', 1, 0)) fdc, ");
		sql.append("        sum(if(b.ywlx_dm='6', 1, 0)) qt, ");
		sql.append("        sum(if(b.ywlx_dm='7', 1, 0)) gxjs, ");
		sql.append("        sum(if(b.ywlx_dm='8', 1, 0)) zx, ");
		sql.append("        sum(if(b.ywlx_dm='9', 1, 0)) bg, ");
		sql.append("        sum(if(b.ywlx_dm='10', 1, 0)) grhsqj ");
		sql.append("   from zs_ryjbxx r, zs_zysws z, zs_ywbb b ");
		sql.append("  where r.id = z.ry_id ");
		sql.append("    and (locate(concat(',', z.id, ','), b.qmswsid) > 0 or ");
		sql.append("        locate(concat(z.id, ','), b.qmswsid) = 1 or ");
		sql.append("        (locate(concat(',', z.id), b.qmswsid) > 0 and ");
		sql.append("        locate(concat(',', z.id), b.qmswsid) = ");
		sql.append("        length(b.qmswsid) - length(z.id))) ");
		sql.append("    and year(b.bbrq) = ? ");
		sql.append("    and b.jg_id = ? ");
		sql.append("  group by r.id, r.xming ");
		params.add(bbnd);
		params.add(jgId);
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		return ls;
	}

	/**
	 * 业务报备数据汇总-机构人员报备数据分析-根据业务报备表的id查找报备数据明细
	 * @param bbid
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzRyMx(String bbid) {
		Map<String, Object> ob = new HashMap<>();
		StringBuffer sql=new StringBuffer(" SELECT b.xyh, ");
		sql.append("        b.bbhm, ");
		sql.append("        b.wtdw, ");
		sql.append("        b.wtdwnsrsbh, ");
		sql.append("        b.wtdwnsrsbhdf, ");
		sql.append("        b.wtdwlxr, ");
		sql.append("        b.wtdwlxdh, ");
		sql.append("        b.wtdxlxdz, ");
		sql.append("        b.swsmc, ");
		sql.append("        b.xyje, ");
		sql.append("        lx.MC ywlx, ");
		sql.append("        date_format(b.sstarttime,'%Y年%m月%d日') sstarttime, ");
		sql.append("        date_format(b.sendtime,'%Y年%m月%d日') sendtime, ");
		sql.append("        b.sjsqje, ");
		sql.append("        b.fphm, ");
		sql.append("        b.memo, ");
		sql.append("        date_format(b.bbrq,'%Y年%m月%d日') bbrq, ");
		sql.append("        b.zgswjg, ");
		sql.append("        hy.MC hy, ");
		sql.append("        case b.NSRXZ ");
		sql.append("          when '0' then '一般纳税人' ");
		sql.append("          when '1' then '小规模纳税人' ");
		sql.append("          when '2' then '非增值税纳税人' ");
		sql.append("        end nsrxz, ");
		sql.append("        b.bgwh, ");
		sql.append("        date_format(b.bgrq,'%Y年%m月%d日') bgrq, ");
		sql.append("        b.yzm, ");
		sql.append("        b.yjfh, ");
		sql.append("        b.rjfh, ");
		sql.append("        b.sjfh, ");
		sql.append("        b.qzsws, ");
		sql.append("        b.sfje, ");
		sql.append("        b.jtxm, ");
		sql.append("        b.ywlx_dm, ");
		sql.append("        b.swsswdjzh, ");
		sql.append("        b.swsdh, ");
		sql.append("        b.swscz, ");
		sql.append("        b.swsdzyj, ");
		sql.append("        b.swswz, ");
		sql.append("        b.txdz ");
		sql.append("   FROM dm_hy hy, dm_cs q, dm_cs c, dm_ywlx lx, zs_ywbb b ");
		sql.append("  WHERE hy.ID = b.HY_ID ");
		sql.append("    and q.ID = b.QX_DM ");
		sql.append("    and c.ID = b.CS_DM ");
		sql.append("    and lx.ID = b.YWLX_DM ");
		sql.append("    AND b.ID = ? ");
		Map<String, Object> data=this.jdbcTemplate.queryForMap(sql.toString(), bbid);
		ob.put("data", data);
		return ob;
	}

	/**
	 * 业务报备数据汇总-机构人员报备数据分析-报备机构
	 * @param page
	 * @param pagesize
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzYwbbJg(int page, int pagesize,
			HashMap<String, Object> map) {
		ArrayList<Object> params = new ArrayList<>();
		StringBuffer sql=new StringBuffer("SELECT SQL_CALC_FOUND_ROWS @rownum := @rownum + 1 AS 'KEY', sj.* ");
		sql.append(" from( ");
		sql.append(" SELECT jg.id, jg.dwmc, jg.fddbr, zt.mc zt ");
		sql.append("   FROM zs_jg jg, dm_jgzt zt ");
		sql.append("  WHERE jg.JGZT_DM = zt.ID ");
		sql.append("    and jg.ID in (select b.JG_ID from zs_ywbb b where b.YXBZ = '1') ");
		sql.append("    and jg.YXBZ='1' ");
		if(null!=map.get("dwmc")){
			sql.append("    and jg.DWMC like ? ");
			params.add("%" + map.get("dwmc") + "%");
		}
		sql.append("  order by jg.ID ");
		sql.append(" )sj, ");
		sql.append(" (SELECT @rownum := ?) xh ");
		sql.append("     LIMIT ?, ? ");
		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		params.add(startIndex);
		params.add(startIndex);
		params.add(pagesize);
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		
		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, int.class);

		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		ob.put("total", total);
		ob.put("pagesize", pagesize);
		ob.put("current", page);
		return ob;
	}

	/**
	 * 业务报备数据汇总-会计所报备数据分析
	 * @param page
	 * @param pagesize
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzSws(int page, int pagesize,
			HashMap<String, Object> map) {
		ArrayList<Object> params = new ArrayList<>();
		String bbnd="";
		String ywlx="";
		if(null!=map.get("bbnd")){
			bbnd=map.get("bbnd").toString();
		}
		if(null!=map.get("ywlx")){
			ywlx=map.get("ywlx").toString();
		}
		StringBuffer sql=new StringBuffer(" select SQL_CALC_FOUND_ROWS @rownum := @rownum + 1 as 'xh',sj.* ");
		sql.append(" from( ");
		sql.append(" select b.swsmc, ");
		sql.append("        b.wtdw, ");
		sql.append("        b.swsswdjzh, ");
		sql.append("        c.mc fsd, ");
		sql.append("        b.bgwh, ");
		sql.append("        b.bbhm, ");
		sql.append("        b.yjfh, ");
		sql.append("        b.rjfh, ");
		sql.append("        b.sjfh, ");
		sql.append("        b.qzsws, ");
		sql.append("        ifnull(b.xyje, 0) xyje, ");
		sql.append("        ifnull(b.sjsqje, 0) sjsqje, ");
		sql.append("        date_format(b.bbrq, '%Y-%m-%d') bbrq, ");
		sql.append("        ifnull(b.tzvalue1, 0) tzvalue1, ");
		sql.append("        ifnull(b.tjvalue2, 0) tjvalue2 ");
		sql.append("   from zs_ywbb b, dm_cs c ");
		sql.append("  where c.id = b.cs_dm ");
		sql.append("    and year(b.bbrq) = ? ");
		sql.append("    and b.ywlx_dm = ? ");
		params.add(bbnd);
		params.add(ywlx);
		sql.append("    order by b.id ");
		sql.append(" )sj, ");
		sql.append(" (select @rownum := ? )xh ");
		sql.append("   limit ?,? ");
		
		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		params.add(startIndex);
		params.add(startIndex);
		params.add(pagesize);
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		
		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, int.class);

		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		ob.put("total", total);
		ob.put("pagesize", pagesize);
		ob.put("current", page);
		return ob;
	}

	/**
	 * 业务报备数据汇总-外省报备数据分析
	 * @param page
	 * @param pagesize
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzWs(int page, int pagesize,
			HashMap<String, Object> map) {
		ArrayList<Object> params = new ArrayList<>();
		String bbnd="";
		String ywlx="";
		if(null!=map.get("bbnd")){
			bbnd=map.get("bbnd").toString();
		}
		if(null!=map.get("ywlx")){
			ywlx=map.get("ywlx").toString();
		}
		StringBuffer sql=new StringBuffer(" select SQL_CALC_FOUND_ROWS @rownum := @rownum + 1 as 'xh',sj.* ");
		sql.append(" from( ");
		sql.append(" select b.swsmc, ");
		sql.append("        b.wtdw, ");
		sql.append("        b.swsswdjzh, ");
		sql.append("        b.city fsd, ");
		sql.append("        b.bgwh, ");
		sql.append("        b.bbhm, ");
		sql.append("        b.yjfh, ");
		sql.append("        b.rjfh, ");
		sql.append("        b.sjfh, ");
		sql.append("        b.qzsws, ");
		sql.append("        ifnull(b.xyje, 0) xyje, ");
		sql.append("        ifnull(b.sjsqje, 0) sjsqje, ");
		sql.append("        date_format(b.bbrq, '%Y-%m-%d') bbrq, ");
		sql.append("        ifnull(b.tzvalue1, 0) tzvalue1, ");
		sql.append("        ifnull(b.tjvalue2, 0) tjvalue2 ");
		sql.append("   from zs_ywbb b ");
		sql.append("  where b.isws='Y' ");
		sql.append("    and year(b.bbrq) = ? ");
		sql.append("    and b.ywlx_dm = ? ");
		params.add(bbnd);
		params.add(ywlx);
		sql.append("    order by b.id ");
		sql.append(" )sj, ");
		sql.append(" (select @rownum := ? )xh ");
		sql.append("   limit ?,? ");
		
		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		params.add(startIndex);
		params.add(startIndex);
		params.add(pagesize);
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		
		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, int.class);

		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		ob.put("total", total);
		ob.put("pagesize", pagesize);
		ob.put("current", page);
		return ob;
	}

	/**
	 * 业务报备数据汇总-业务报备总收费金额数据分析
	 * @param page
	 * @param pagesize
	 * @param map
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzJe(int page, int pagesize,
			HashMap<String, Object> map) {
		ArrayList<Object> params = new ArrayList<>();
		String bbnd="";
		String ywlx="";
		String fsd="";
		if(null!=map.get("bbnd")){
			bbnd=map.get("bbnd").toString();
		}
		if(null!=map.get("ywlx")){
			ywlx=map.get("ywlx").toString();
		}
		if(null!=map.get("fsd")){
			fsd=map.get("fsd").toString();
		}
		StringBuffer sql=new StringBuffer(" select sql_calc_found_rows @rownum := @rownum + 1 as xh, sj.* ");
		sql.append("   from (select b.swsmc, sum(ifnull(b.xyje,0)) xyje, sum(ifnull(b.sjsqje,0)) sjsqje ");
		sql.append("           from zs_ywbb b, dm_cs c ");
		sql.append("          where b.cs_dm = c.id ");
		if(!StringUtils.isEmpty(bbnd)){
			sql.append("        and year(b.bbrq) = ? ");
			params.add(bbnd);
		}
		if(!StringUtils.isEmpty(ywlx)){
			sql.append("        and b.ywlx_dm = ? ");
			params.add(ywlx);
		}
		if(!StringUtils.isEmpty(fsd)){
			sql.append("        and b.cs_dm= ? ");
			params.add(fsd);
		}
		sql.append("          group by b.JG_ID,b.SWSMC) sj, ");
		sql.append("        (select @rownum := ?) xh ");
		sql.append("        order by sj.xyje desc,sj.sjsqje desc ");
		sql.append("   limit ?,? ");
		
		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		params.add(startIndex);
		params.add(startIndex);
		params.add(pagesize);
		
		List<Map<String,Object>> ls=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		
		// 获取符合条件的记录数
		String countSql = " SELECT FOUND_ROWS()";
		int total = jdbcTemplate.queryForObject(countSql, int.class);

		Map<String, Object> ob = new HashMap<>();
		ob.put("data", ls);
		ob.put("total", total);
		ob.put("pagesize", pagesize);
		ob.put("current", page);
		return ob;
	}
	
	public Object getCITY(Object CS_DM) {
		String sql = "select mc from dm_cs where id = ?";
		String mc = this.jdbcTemplate.queryForObject(sql, new Object[]{CS_DM}, String.class);
		return mc;
	}
}
