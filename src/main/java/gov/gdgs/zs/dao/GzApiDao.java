package gov.gdgs.zs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class GzApiDao extends BaseJdbcDao {

	public List<Map<String, Object>> getSws(String year, String month,
			String day, String hour) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select ID,XM,XB,SFZH,SWSSWSXLH,CS,MZ,XL,ZZMM,ZYZGZH,ZYZCHM,YZBM,DH,YDDH,ZW,SFSC,SFCZR,SFFQR,SJZT, ");
		sb.append(" DATE_FORMAT(CSNY,'%Y-%m-%d') AS CSNY, ");
		sb.append(" DATE_FORMAT(ZYZGZQFRQ,'%Y-%m-%d') AS ZYZGZQFRQ, ");
		sb.append(" DATE_FORMAT(ZYZCRQ,'%Y-%m-%d') AS ZYZCRQ ");
		sb.append(" FROM gzapi_data_sws where addtime between  ? and ?");
		List<Map<String, Object>> ls = this.jdbcTemplate
				.queryForList(sb.toString(),
						new Object[] {
								year + "-" + month + "-" + day + " " + hour
										+ ":00:00",
								year + "-" + month + "-" + day + " " + hour
										+ ":59:59" });
		return ls;
	}

	public List<Map<String, Object>> getYwba(String start, String end) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select ID,BBHM,ND,BGWH,YZM,SFJE,SWSSWSXLH,SWSMC,SWSSWDJZH,WTDW,WTDWSWDJZH,XYH,YJFH,RJFH,SJFH,QZSWS, ");
		sb.append(" TXDZ,SWSDZYJ,SWSWZ,YWLX,JTXM,VALUE1,VALUE2,ZSXYID,NSRXZ,HYLX,ZSFS,ISWS,SB,CITY,QX,WTDWXZ,ZTBJ, ");
		sb.append(" DATE_FORMAT(BBRQ,'%Y-%m-%d') AS  BBRQ, ");
		sb.append(" DATE_FORMAT(BGRQ,'%Y-%m-%d') AS  BGRQ, ");
		sb.append(" DATE_FORMAT(ZBRQ,'%Y-%m-%d') AS  ZBRQ, ");
		sb.append(" DATE_FORMAT(SSTARTTIME,'%Y-%m-%d') AS  SSTARTTIME, ");
		sb.append(" DATE_FORMAT(SENDTIME,'%Y-%m-%d') AS  SENDTIME ");
		sb.append(" from gzapi_data_ywba ");
		sb.append(" where addtime between ? and ? ");
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sb.toString(), new Object[] { start, end });
		return ls;
	}

	public List<Map<String, Object>> getSwsjg(String year, String month,
			String day, String hour) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,YZBM,DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXYY,SJZT,PARENTJGID, ");
		sb.append(" DATE_FORMAT(ZJPZSJ,'%Y-%m-%d') AS ZJPZSJ, ");
		sb.append(" DATE_FORMAT(ZXSJ,'%Y-%m-%d') AS ZXSJ ");
		sb.append(" from gzapi_data_swsjg  where addtime between  ? and ?");
		List<Map<String, Object>> ls = this.jdbcTemplate
				.queryForList(sb.toString(),
						new Object[] {
								year + "-" + month + "-" + day + " " + hour
										+ ":00:00",
								year + "-" + month + "-" + day + " " + hour
										+ ":59:59" });
		return ls;
	}

	public List<Map<String, Object>> getZsxy(String start, String end) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select ID,XYH,YWLXNAME,WTFMC,DJHM_GS,DJHM_DS,JFLXR,JFTELEPHONE,JFADDRESS,JG_ID,SWSMC, ");
		sb.append(" FPHM,XYJE,SSJE,XYZT,MEMO, ");
		sb.append(" DATE_FORMAT(XYKSSJ,'%Y-%m-%d') AS  XYKSSJ, ");
		sb.append(" DATE_FORMAT(XYJSSJ,'%Y-%m-%d') AS  XYJSSJ, ");
		sb.append(" DATE_FORMAT(EDITDATE,'%Y-%m-%d') AS  EDITDATE ");
		sb.append(" FROM gzapi_data_zsxy ");
		sb.append(" where addtime between ? and ? ");
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sb.toString(), new Object[] { start, end });
		return ls;
	}

	public void addJG(int jgid) {// 机构
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_swsjg (SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,ZJPZSJ,YZBM,");
		sb.append("DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXSJ,ZXYY,PARENTJGID,SJZT,ADDTIME) select a.id,a.SWDJHM,a.DWMC,");
		sb.append(" (select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ) as cs,");
		sb.append("a.FDDBR,a.DZHI,");
		sb.append(" concat((select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ),'国税局') as swjg_dm,");
		sb.append("a.ZJPZWH,a.ZJPZSJ,a.YZBM,a.DHUA,a.CZHEN,a.JGXZ_DM,a.JGZCH,a.ZCZJ,a.JYFW,a.DZYJ,a.KHH,a.KHHZH,null as ZXSJ,null as ZXYY,a.PARENTJGID,'1' as SJZT,sysdate()  from zs_jg a where a.id=?");
		this.jdbcTemplate.update(sb.toString(), new Object[] { jgid });
	}

	public void changedJG(int jgid) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_swsjg (SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,ZJPZSJ,YZBM,");
		sb.append("DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXSJ,ZXYY,PARENTJGID,SJZT,ADDTIME) select a.id,a.SWDJHM,a.DWMC,");
		sb.append(" (select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ) as cs,");
		sb.append("a.FDDBR,a.DZHI,");
		sb.append(" concat((select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ),'国税局') as swjg_dm,");
		sb.append("a.ZJPZWH,a.ZJPZSJ,a.YZBM,a.DHUA,a.CZHEN,a.JGXZ_DM,a.JGZCH,a.ZCZJ,a.JYFW,a.DZYJ,a.KHH,a.KHHZH,null as ZXSJ,null as ZXYY,a.PARENTJGID,'2' as SJZT,sysdate()  from zs_jg a where a.id=?");
		this.jdbcTemplate.update(sb.toString(), new Object[] { jgid });
	}

	public void delJG(int jgid) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_swsjg (SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,ZJPZSJ,YZBM,");
		sb.append("DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXSJ,ZXYY,PARENTJGID,SJZT,ADDTIME) select a.id,a.SWDJHM,a.DWMC,");
		sb.append(" (select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ) as cs,");
		sb.append("a.FDDBR,a.DZHI,");
		sb.append(" concat((select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ),'国税局') as swjg_dm,");
		sb.append("a.ZJPZWH,a.ZJPZSJ,a.YZBM,a.DHUA,a.CZHEN,a.JGXZ_DM,a.JGZCH,a.ZCZJ,a.JYFW,a.DZYJ,a.KHH,a.KHHZH,b.ZXRQ,");
		sb.append("(select l.mc from dm_jgzxyy l where l.id=b.ZXYY_ID ) as ZXYY,a.PARENTJGID,'0' as SJZT,sysdate()  from zs_jg a,zs_jgzx b where a.id=? and a.id=b.jg_id");
		this.jdbcTemplate.update(sb.toString(), new Object[] { jgid });
	}

	public void insertSWS(int zyid, int way) {// 税务师
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_sws (ID,XM,XB,CSNY,SFZH,SWSSWSXLH,CS,MZ,XL,ZZMM,");
		sb.append("ZYZGZH,ZYZGZQFRQ,ZYZCHM,ZYZCRQ,YZBM,DH,YDDH,ZW,SFSC,SFCZR,SFFQR,SJZT,ADDTIME) select a.id,b.XMING,");
		sb.append(" (select z.mc from dm_xb z where z.id=b.xb_dm ) as xb,b.SRI,b.SFZH,a.JG_ID,");
		sb.append("(select j.mc from dm_cs j where j.id=b.CS_DM ) as cs,");
		sb.append("(select k.mc from dm_mz k where k.id=b.MZ_DM ) as mz,");
		sb.append("(select l.mc from dm_xl l where l.id=b.xl_DM ) as xl,");
		sb.append("(select h.mc from dm_zzmm h where h.id=b.zzmm_DM ) as zzmm,");
		sb.append("a.ZYZGZSBH,a.ZGZSQFRQ,a.ZYZSBH,a.ZYZCRQ,b.YZBM,b.DHHM,b.YDDH,");
		sb.append("(select g.mc from dm_zw g where g.id=a.zw_DM ) as zw,");
		sb.append("case a.SZ_DM when 1 then \"是\"  when 2 then \"否\" else null end as sz,");
		sb.append("case a.CZR_DM when 1 then \"是\"  when 2 then \"否\" else null end as czr,");
		sb.append("case a.FQR_DM when 1 then \"是\"  when 2 then \"否\" else null end as fqr,");
		sb.append("'"
				+ way
				+ "' as SJZT,sysdate()  from zs_zysws a,zs_ryjbxx b where a.id=? and a.ry_id=b.id");
		this.jdbcTemplate.update(sb.toString(), new Object[] { zyid });
	}

	public Map<String, Object> getYwbbSource(Number ywId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT y.*,l.mc AS YWLX,hy.mc AS HYLX,cs.mc AS CS,qx.mc AS QX,xy.MC as XYZT, j.cs_dm as JGCITY ");
		sb.append(" FROM (zs_ywbb y,zs_jg j) ");
		sb.append(" left join dm_hy AS hy  ");
		sb.append(" on y.hy_id = hy.id ");
		sb.append(" left join dm_cs AS cs ");
		sb.append(" on y.cs_dm = cs.id ");
		sb.append(" left join dm_cs AS qx ");
		sb.append(" on y.qx_dm = qx.id ");
		sb.append(" left join dm_ywlx as l ");
		sb.append(" on y.ywlx_dm = l.id ");
		sb.append(" left join dm_xyzt as xy ");
		sb.append(" on y.xyzt_dm = xy.ID ");
		sb.append(" where y.ID = ? ");
		sb.append(" and y.jg_id = j.id ");
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(), new Object[] { ywId },
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int arg1)
							throws SQLException {
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ID", rs.getObject("ID"));
						map.put("ZSXYID", rs.getObject("ID"));
						map.put("BBHM", rs.getObject("BBHM"));
						map.put("BBRQ", rs.getObject("BBRQ"));
						map.put("ND", rs.getObject("ND"));
						map.put("BGWH", rs.getObject("BGWH"));
						map.put("BGRQ", rs.getObject("BGRQ"));
						map.put("YZM", rs.getObject("YZM"));
						map.put("SFJE", rs.getObject("SFJE"));
						map.put("SWSSWSXLH", rs.getObject("JG_ID"));
						map.put("SWSMC", rs.getObject("SWSMC"));
						map.put("SWSSWDJZH", rs.getObject("SWSSWDJZH"));
						map.put("WTDW", rs.getObject("WTDW"));
						map.put("WTDWSWDJZH", rs.getObject("WTDWNSRSBH"));
						map.put("XYH", rs.getObject("XYH"));
						map.put("YJFH", rs.getObject("YJFH"));
						map.put("RJFH", rs.getObject("RJFH"));
						map.put("SJFH", rs.getObject("SJFH"));
						map.put("QZSWS", rs.getObject("QZSWS"));
						map.put("TXDZ", rs.getObject("TXDZ"));
						map.put("SWSDZYJ", rs.getObject("SWSDZYJ"));
						map.put("SWSWZ", rs.getObject("SWSWZ"));
						map.put("YWLX", rs.getObject("YWLX"));
						map.put("JTXM", rs.getObject("JTXM"));
						map.put("ZBRQ", rs.getObject("ZBRQ"));
						map.put("VALUE1", rs.getObject("TZVALUE1"));
						map.put("VALUE2", rs.getObject("TJVALUE2"));
						map.put("SSTARTTIME", rs.getObject("SSTARTTIME"));
						map.put("SENDTIME", rs.getObject("SENDTIME"));
						map.put("HYLX", rs.getObject("HYLX"));
						if (rs.getInt("NSRXZ") == 0) {
							map.put("NSRXZ", "一般纳税人");
						} else if (rs.getInt("nsrxz") == 1) {
							map.put("NSRXZ", "小规模纳税人");
						} else {
							map.put("NSRXZ", "非增值税纳税人");
						}
						if (rs.getInt("ZSFS_DM") == 0) {
							map.put("ZSFS", "查账征收");
						} else {
							map.put("ZSFS", "核定征收");
						}
						if (rs.getString("ISWS") == null
								|| rs.getString("ISWS").equals("N")) {
							map.put("ISWS", "广东省");
						} else {
							map.put("ISWS", "外省");
						}
						if (rs.getInt("SB_DM") == 1) {
							map.put("SB", "国税");
						} else {
							map.put("SB", "地税");
						}
						if (rs.getInt("WTDWXZ_DM") == 0) {
							map.put("WTDWXZ", "居民企业");
						} else {
							map.put("WTDWXZ", "非居民企业税");
						}
						map.put("CITY", rs.getObject("CITY"));
						map.put("QX", rs.getObject("QX"));
						map.put("YWLXNAME", rs.getObject("YWLX"));
						map.put("XYKSSJ", rs.getObject("SSTARTTIME"));
						map.put("XYJSSJ", rs.getObject("SENDTIME"));
						map.put("WTFMC", rs.getObject("WTDW"));
						map.put("DJHM_GS", rs.getObject("WTDWNSRSBH"));
						map.put("DJHM_DS", rs.getObject("WTDWNSRSBHDF"));
						map.put("JFLXR", rs.getObject("WTDWLXR"));
						map.put("JFTELEPHONE", rs.getObject("WTDWLXDH"));
						map.put("JFADDRESS", rs.getObject("WTDXLXDZ"));
						map.put("EDITDATE", rs.getObject("ZBRQ"));
						map.put("FPHM", rs.getObject("FPHM"));
						map.put("XYJE", rs.getObject("XYJE"));
						map.put("SSJE", rs.getObject("SJSQJE"));
						map.put("MEMO", rs.getObject("MEMO"));
						map.put("SSJE", rs.getObject("SJSQJE"));
						map.put("XYZT", rs.getObject("XYZT"));
						map.put("JGCITY", rs.getInt("JGCITY"));
						map.put("JG_ID", rs.getInt("JG_ID"));
						return map;
					}
				});
		if(ls.size()>0){
			return ls.get(0);
		}
		return null;
	}

	public void insertZSXY(Map<String, Object> yw) {
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into gzapi_data_zsxy ");
		sb.append(" (ID,XYH,YWLXNAME,XYKSSJ,XYJSSJ,WTFMC,DJHM_GS,DJHM_DS,JFLXR,JFTELEPHONE, ");
		sb.append(" JFADDRESS,JG_ID,SWSMC,EDITDATE,FPHM,XYJE,SSJE,XYZT,MEMO,ZTBJ,ADDTIME) ");
		sb.append(" values(:ID,:XYH,:YWLXNAME,:XYKSSJ,:XYJSSJ,:WTFMC,:DJHM_GS,:DJHM_DS,:JFLXR,:JFTELEPHONE, ");
		sb.append(" :JFADDRESS,:JG_ID,:SWSMC,:EDITDATE,:FPHM,:XYJE,:SSJE,:XYZT,:MEMO,:ZTBJ,:ADDTIME) ");
		this.namedParameterJdbcTemplate.update(sb.toString(), yw);
		

	}

	public void insertYWBA(Map<String, Object> yw) {
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into gzapi_data_ywba ");
		sb.append(" (ID,BBHM,BBRQ,ND,BGWH,BGRQ,YZM,SFJE,SWSSWSXLH,SWSMC,SWSSWDJZH,WTDW,WTDWSWDJZH,XYH, ");
		sb.append(" YJFH,RJFH,SJFH,QZSWS,TXDZ,SWSDZYJ,SWSWZ,YWLX,JTXM,ZBRQ,VALUE1,VALUE2,ZSXYID, ");
		sb.append(" SSTARTTIME,SENDTIME,NSRXZ,HYLX,ZSFS,ISWS,SB,CITY,QX,WTDWXZ,ZTBJ,ADDTIME) ");
		sb.append(" values (:ID,:BBHM,:BBRQ,:ND,:BGWH,:BGRQ,:YZM,:SFJE,:SWSSWSXLH,:SWSMC,:SWSSWDJZH,:WTDW,:WTDWSWDJZH,:XYH, ");
		sb.append(" :YJFH,:RJFH,:SJFH,:QZSWS,:TXDZ,:SWSDZYJ,:SWSWZ,:YWLX,:JTXM,:ZBRQ,:VALUE1,:VALUE2,:ZSXYID, ");
		sb.append(" :SSTARTTIME,:SENDTIME,:NSRXZ,:HYLX,:ZSFS,:ISWS,:SB,:CITY,:QX,:WTDWXZ,:ZTBJ,:ADDTIME) ");
		this.namedParameterJdbcTemplate.update(sb.toString(), yw);
	}

}
