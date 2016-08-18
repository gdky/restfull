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
public class YwglDao extends BaseJdbcDao {

	public Map<String, Object> getYwbb(int page, int pageSize,
			Map<String, Object> where) {

		// 子查询，用于拼接查询条件和返回起止区间
		Condition condition = new Condition();
		condition.add("swsmc", "FUZZY", where.get("swsmc"));
		condition.add("cs_dm", "EQUAL", where.get("cs"));
		condition.add("bbhm", "FUZZY", where.get("bbhm"));
		condition.add("bbrq", "FUZZY", where.get("bbrq"));

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  ");
		sb.append("    @rownum:=@rownum + 1 AS 'key', v . * ");
		sb.append("FROM ");
		sb.append("    (SELECT  ");
		sb.append("        t.id, ");
		sb.append("        t.nd, ");
		sb.append("            t.swsmc, ");
		sb.append("            ds.MC AS cs, ");
		sb.append("            dl.MC AS ywlx, ");
		sb.append("            t.bgwh, ");
		sb.append("            t.xyje, ");
		sb.append("            t.sjsqje, ");
		sb.append("            t.bbhm, ");
		sb.append("            t.bbrq, ");
		sb.append("            t.yzm ");
		sb.append("    FROM ");
		sb.append("        zs_ywbb_old t, dm_cs ds, dm_ywlx dl, ");
		// <=== 查询条件集合
		sb.append(" ( "
				+ condition.getSelectSql(Config.PROJECT_SCHEMA
						+ "zs_ywbb_old", "id"));
		sb.append("    ORDER BY bbrq DESC ");
		sb.append("    LIMIT ? , ?) sub ");
		// ===> 插入查询条件集合结束
		sb.append("    WHERE ");
		sb.append("        t.CS_DM = ds.ID AND t.YWLX_DM = dl.ID ");
		sb.append("            AND sub.id = t.id) v, ");
		sb.append("    (SELECT @rownum:=?) tmp ");
		sb.append(" ");

		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pageSize);
		params.add(pageSize * (page - 1));

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(
				sb.toString(),
				params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("key", rs.getObject("key"));
				map.put("id", hashids.encode(rs.getLong("id")));
				map.put("nd", rs.getObject("nd"));
				map.put("swsmc", rs.getObject("swsmc"));
				map.put("cs", rs.getObject("cs"));
				map.put("ywlx", rs.getObject("ywlx"));
				map.put("bgwh", rs.getObject("bgwh"));
				map.put("xyje", rs.getObject("xyje"));
				map.put("sjsqje", rs.getObject("sjsqje"));
				map.put("bbhm", rs.getObject("bbhm"));
				map.put("bbrq", rs.getObject("bbrq"));
				map.put("yzm", rs.getObject("yzm"));
				return map;
			}
		});

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("id", "zs_ywbb_old");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);

		return obj;
	}

	public Map<String, Object> getYwbbById(long id) {
		String sql = "select * from "+Config.PROJECT_SCHEMA+"zs_ywbb_old where id = ?";
		Map<String,Object> rs = jdbcTemplate.queryForMap(sql, id);
		return rs;
	}

	public Map<String, Object> getYwbbByJg(Long id, int page, int pageSize,
			Map<String,Object> where) {
		
		String  sql = "select * from zs_ywbb where jg_id=? and yxbz = 1 order by zbrq desc";

		List<Map<String,Object>> ls = jdbcTemplate.queryForList(sql,
				new Object[]{id});
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		return obj;
	}

	public List<Map<String,Object>> getYwbbMiscByJg(Long id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.XMING,z.ID as ZYSWS_ID ");
		sb.append(" from zs_jg j,zs_ryjbxx r,zs_zysws z ");
		sb.append(" where j.id = z.JG_ID ");
		sb.append(" and z.RY_ID = r.ID ");
		sb.append(" and j.ID=? ");
		sb.append(" and z.RYSPGCZT_DM = 1 ");
		sb.append(" and z.YXBZ=1 ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(), new Object[]{id});
		return ls;
	}

	public void addYwbb(HashMap<String, Object> o) {
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into zs_ywbb ");
		sb.append(" (ND,BBRQ,BGWH,BGRQ,SFJE,JG_ID,SWSMC,SWSSWDJZH,WTDW,WTDWNSRSBH,XYH,YJFH,RJFH,SJFH, ");
		sb.append(" QZSWS,QMSWSID,TXDZ,SWSDZYJ,SWSWZ,YWLX_DM,JTXM,ZBRQ, ");
		sb.append(" SENDTIME,SSTARTTIME,NSRXZ,HY_ID,ZSFS_DM,ISWS,SB_DM,CS_DM,QX_DM, ");
		sb.append(" WTDWXZ_DM,WTDWNSRSBHDF,WTDWLXR,WTDWLXDH,WTDXLXDZ,XYJE,CUSTOMER_ID,TZVALUE1,TJVALUE2, ");
		sb.append(" YZM,BBHM,IS_YD,ZT) ");
		sb.append(" values(:ND,:BBRQ,:BGWH,:BGRQ,:SFJE,:JG_ID,:SWSMC,:SWSSWDJZH,:WTDW,:WTDWNSRSBH,:XYH,:YJFH,:RJFH,:SJFH, ");
		sb.append(" :QZSWS,:QMSWSID,:TXDZ,:SWSDZYJ,:SWSWZ,:YWLX_DM,:JTXM,:ZBRQ, ");
		sb.append(" :SENDTIME,:SSTARTTIME,:NSRXZ,:HY_ID,:ZSFS_DM,:ISWS,:SB_DM,:CS_DM,:QX_DM, ");
		sb.append(" :WTDWXZ_DM,:WTDWNSRSBHDF,:WTDWLXR,:WTDWLXDH,:WTDXLXDZ,:XYJE,:CUSTOMER_ID,:TZVALUE1,:TJVALUE2, ");
		sb.append(" :YZM,:BBHM,:IS_YD,:ZT) ");
		this.namedParameterJdbcTemplate.update(sb.toString(), o);		
	}

	public int getXyhNum(String xyh) {
		String sql = "select id from zs_ywbb where xyh = ?";
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql,new Object[]{xyh});
		return ls.size();
	}

	public Map<String, Object> getYwbbByYzmAndBbhm(String bbhm, String yzm) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select y.*,z.mc as ywzt,l.mc as ywlx,hy.mc as hy,cs.mc as cs ");
		sb.append(" from zs_ywbb y,dm_ywbb_zt z,dm_ywlx l,dm_hy hy,dm_cs cs ");
		sb.append(" where bbhm=? and yzm = ? ");
		sb.append(" and y.zt = z.id ");
		sb.append(" and y.ywlx_dm = l.id ");
		sb.append(" and y.hy_id = hy.id ");
		sb.append(" and y.cs_dm = cs.id ");
		List<Map<String, Object>> ls = jdbcTemplate.query(
				sb.toString(),
				new Object[]{bbhm,yzm},
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String,Object> map = new HashMap<String,Object>();
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
				map.put("ywlx", rs.getString("ywlx"));
				map.put("jtxm", rs.getString("jtxm"));
				map.put("tzvalue1", rs.getBigDecimal("tzvalue1"));
				map.put("tjvalue2", rs.getBigDecimal("tjvalue2"));
				map.put("sstarttime", rs.getDate("sstarttime"));
				map.put("sendtime", rs.getDate("sendtime"));
				if(rs.getInt("nsrxz")==0){
					map.put("nsrxz", "一般纳税人");
				}else if (rs.getInt("nsrxz") ==1){
					map.put("nsrxz", "小规模纳税人");
				}else{
					map.put("nsrxz", "非增值税纳税人");
				}
				if(rs.getInt("zsfs_dm")==0){
					map.put("zsfs", "查账征收");
				}else {
					map.put("zsfs", "核定征收");
				}
				if(rs.getString("ISWS").equals("Y")){
					map.put("zsfs", "外省");
				}else {
					map.put("zsfs", "广东省");
				}
				if(rs.getInt("SB_DM")==1){
					map.put("sb_dm", "国税");
				}else {
					map.put("sb_dm", "地税");
				}
				
				
				return map;
			}
		});
		
		return ls.get(0);
	}
	

}
