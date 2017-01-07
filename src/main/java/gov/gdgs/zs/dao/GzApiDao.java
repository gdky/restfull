package gov.gdgs.zs.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class GzApiDao extends BaseJdbcDao {

	public List<Map<String, Object>> getSws(String year, String month, String day,
			String hour) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select ID,XM,XB,SFZH,SWSSWSXLH,CS,MZ,XL,ZZMM,ZYZGZH,ZYZCHM,YZBM,DH,YDDH,ZW,SFSC,SFCZR,SFFQR,SJZT, ");
		sb.append(" DATE_FORMAT(CSNY,'%Y-%m-%d') AS CSNY, ");
		sb.append(" DATE_FORMAT(ZYZGZQFRQ,'%Y-%m-%d') AS ZYZGZQFRQ, ");
		sb.append(" DATE_FORMAT(ZYZCRQ,'%Y-%m-%d') AS ZYZCRQ ");
		sb.append(" FROM gzapi_data_sws where addtime between  ? and ?");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),
				new Object[]{year+"-"+month+"-"+day+" "+hour+":00:00",
			year+"-"+month+"-"+day+" "+hour+":59:59"});
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
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{start , end});
		return ls;
	}

	public List<Map<String, Object>> getSwsjg(String year, String month, String day,
			String hour) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,YZBM,DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXYY,SJZT,PARENTJGID, ");
		sb.append(" DATE_FORMAT(ZJPZSJ,'%Y-%m-%d') AS ZJPZSJ, ");
		sb.append(" DATE_FORMAT(ZXSJ,'%Y-%m-%d') AS ZXSJ ");
		sb.append(" from gzapi_data_swsjg  where addtime between  ? and ?");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),
				new Object[]{year+"-"+month+"-"+day+" "+hour+":00:00",
			year+"-"+month+"-"+day+" "+hour+":59:59"});
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
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{start,end});
		return ls;
	}
	
	public void addJG(int jgid){//机构
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_swsjg (SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,ZJPZSJ,YZBM,");
		sb.append("DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXSJ,ZXYY,PARENTJGID,SJZT,ADDTIME) select a.id,a.SWDJHM,a.DWMC,");
		sb.append(" (select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ) as cs,");
		sb.append("a.FDDBR,a.DZHI,");
		sb.append(" concat((select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ),'国税局') as swjg_dm,");
		sb.append("a.ZJPZWH,a.ZJPZSJ,a.YZBM,a.DHUA,a.CZHEN,a.JGXZ_DM,a.JGZCH,a.ZCZJ,a.JYFW,a.DZYJ,a.KHH,a.KHHZH,null as ZXSJ,null as ZXYY,a.PARENTJGID,'1' as SJZT,sysdate()  from zs_jg a where a.id=?");
		this.jdbcTemplate.update(sb.toString(),new Object[]{jgid});
	}
	public void changedJG(int jgid){
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_swsjg (SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,ZJPZSJ,YZBM,");
		sb.append("DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXSJ,ZXYY,PARENTJGID,SJZT,ADDTIME) select a.id,a.SWDJHM,a.DWMC,");
		sb.append(" (select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ) as cs,");
		sb.append("a.FDDBR,a.DZHI,");
		sb.append(" concat((select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ),'国税局') as swjg_dm,");
		sb.append("a.ZJPZWH,a.ZJPZSJ,a.YZBM,a.DHUA,a.CZHEN,a.JGXZ_DM,a.JGZCH,a.ZCZJ,a.JYFW,a.DZYJ,a.KHH,a.KHHZH,null as ZXSJ,null as ZXYY,a.PARENTJGID,'2' as SJZT,sysdate()  from zs_jg a where a.id=?");
		this.jdbcTemplate.update(sb.toString(),new Object[]{jgid});
	}
	public void delJG(int jgid){
		StringBuffer sb = new StringBuffer();
		sb.append("insert into gzapi_data_swsjg (SWSSWSXLH,SWDJZHM,DWMC,SZCS,FRDB,DZ,SWJG_DM,ZJPZWH,ZJPZSJ,YZBM,");
		sb.append("DH,CZ,JGXZ,ZSBH,ZCZJ,JYFW,DZYJ,KHH,KHHZH,ZXSJ,ZXYY,PARENTJGID,SJZT,ADDTIME) select a.id,a.SWDJHM,a.DWMC,");
		sb.append(" (select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ) as cs,");
		sb.append("a.FDDBR,a.DZHI,");
		sb.append(" concat((select (case  parent_id when 0  then  z.mc else  (select t.mc from dm_cs t where t.id=z.parent_id) end) from dm_cs z where z.id=a.cs_dm ),'国税局') as swjg_dm,");
		sb.append("a.ZJPZWH,a.ZJPZSJ,a.YZBM,a.DHUA,a.CZHEN,a.JGXZ_DM,a.JGZCH,a.ZCZJ,a.JYFW,a.DZYJ,a.KHH,a.KHHZH,b.ZXRQ,");
		sb.append("(select l.mc from dm_jgzxyy l where l.id=b.ZXYY_ID ) as ZXYY,a.PARENTJGID,'0' as SJZT,sysdate()  from zs_jg a,zs_jgzx b where a.id=? and a.id=b.jg_id");
		this.jdbcTemplate.update(sb.toString(),new Object[]{jgid});
	}

	public void insertSWS(int zyid,int way){//税务师
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
		sb.append("'"+way+"' as SJZT,sysdate()  from zs_zysws a,zs_ryjbxx b where a.id=? and a.ry_id=b.id");
		this.jdbcTemplate.update(sb.toString(),new Object[]{zyid});
	}
	
}
