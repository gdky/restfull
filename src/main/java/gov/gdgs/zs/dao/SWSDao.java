package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SWSDao extends BaseDao{

	/**
	 * 
	 * @return 事务所分页查询
	 * @throws Exception 
	 */
	public Map<String,Object> swscx(int pn,int ps,Map<String,Object> qury) {
		final String url=Config.URL_PROJECT+"/swsxx";
		Condition condition = new Condition();
		condition.add("a.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("a.JGZCH", Condition.EQUAL, qury.get("zsbh"));
		condition.add("a.zczj", Condition.GREATER_EQUAL, qury.get("zczj"));
		condition.add("a.zczj", Condition.LESS_EQUAL, qury.get("zczj2"));
		condition.add("a.cs_dm", Condition.EQUAL, qury.get("cs"));
		condition.add("a.JGXZ_DM", Condition.EQUAL, qury.get("swsxz"));
		condition.add("a.swszsclsj", Condition.GREATER_EQUAL, qury.get("clsj"));
		condition.add("a.swszsclsj", Condition.LESS_EQUAL, qury.get("clsj2"));
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT 	SQL_CALC_FOUND_ROWS	 ");
		sb.append("		@rownum:=@rownum+1 AS 'key',a.ZJPZWH,a.DZHI,");
		sb.append("		a.id,	a.dwmc,a.zczj,a.YYZZHM,a.JYFW,");
		sb.append("		a.fddbr,a.JGZCH as zsbh,");
		sb.append("		d.mc AS swsxz,c.mc AS cs,");
		sb.append("		(select count(id) from zs_zysws where jg_id=a.id and ZYZT_DM=1) as zyrs,");
		sb.append("		(select count(id) from zs_zysws where jg_id=a.id AND ZYZT_DM=1)+");
		sb.append("(select count(id) from zs_cyry where jg_id=a.id and CYRYZT_DM=1) as zrs,");
		sb.append("		DATE_FORMAT(a.swszsclsj,'%Y-%m-%d') AS clsj");
		sb.append("		,(select v.id from zs_sdjl_jg v where v.jg_id=a.id and v.lx=1 and v.yxbz=1 limit 1) as issd");
		sb.append("		FROM	zs_jg a,	dm_cs c,dm_jgxz d,(SELECT @rownum:=?) zs_jg ");
		sb.append(condition.getSql());
		sb.append("		and a.jgxz_dm = d.id ");
		sb.append("		AND a.cs_dm = c.id ");
		sb.append("		AND a.YXBZ = '1'");
		if(qury.containsKey("zyrs")){
			sb.append("		and a.id in (SELECT jg_id");
			sb.append("				FROM zs_zysws");
			sb.append("				WHERE  ZYZT_DM=1 group by jg_id having count(jg_id)>="+qury.get("zyrs")+")");
		}
		if(qury.containsKey("zyrs2")){
			sb.append("		and a.id in (SELECT jg_id");
			sb.append("				FROM zs_zysws");
			sb.append("				WHERE  ZYZT_DM=1 group by jg_id having count(jg_id)<="+qury.get("zyrs2")+")");
		}
		if(qury.containsKey("zrs")){
			sb.append("		and a.id in (select f.id from (SELECT ID,ifnull(b.b,0)+ifnull(e.c,0) zrs");
			sb.append("				FROM zs_jg left join (");
			sb.append("				SELECT jg_id, COUNT(jg_id) b");
			sb.append("				FROM zs_zysws where ZYZT_DM=1");
			sb.append("				GROUP BY jg_id) b on(zs_jg.ID=b.jg_id)");
			sb.append("				left join (");
			sb.append("				SELECT jg_id, COUNT(jg_id) c");
			sb.append("				FROM zs_cyry where CYRYZT_DM=1");
			sb.append("				GROUP BY jg_id) e on zs_jg.ID=e.jg_id) f where f.zrs>="+qury.get("zrs")+")");
		}
		if(qury.containsKey("zrs2")){
			sb.append("		and a.id in (select f.id from (SELECT ID,ifnull(b.b,0)+ifnull(e.c,0) zrs");
			sb.append("				FROM zs_jg left join (");
			sb.append("				SELECT jg_id, COUNT(jg_id) b");
			sb.append("				FROM zs_zysws where ZYZT_DM=1");
			sb.append("				GROUP BY jg_id) b on(zs_jg.ID=b.jg_id)");
			sb.append("				left join (");
			sb.append("				SELECT jg_id, COUNT(jg_id) c");
			sb.append("				FROM zs_cyry where CYRYZT_DM=1");
			sb.append("				GROUP BY jg_id) e on zs_jg.ID=e.jg_id) f where f.zrs<="+qury.get("zrs2")+")");
		}
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "dwmc":
				if(asc){
					sb.append("		    order by convert( a.dwmc USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( a.dwmc USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "zczj":
				if(asc){
					sb.append("		    order by a.zczj ");
				}else{
					sb.append("		    order by a.zczj desc");
				}
				break;
			case "fddbr":
				if(asc){
					sb.append("		    order by convert( a.fddbr USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( a.fddbr USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "clsj":
				if(asc){
					sb.append("		    order by a.swszsclsj ");
				}else{
					sb.append("		    order by a.swszsclsj desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>(){
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				Map<String,Object> map = new HashMap<String,Object>();
				Map<String,Object> link = new HashMap<>();
				String id = hashids.encode(rs.getLong("id"));
				link.put("herf_sws", url+"/swsxx/"+id);
				link.put("herf_zyry", url+"/zyryxx/"+id);
				link.put("herf_cyry", url+"/cyryxx/"+id);
				link.put("herf_czrylb", url+"/czrylb/"+id);
				link.put("herf_swsbgxx", url+"/swsbgxx/"+id);
				link.put("herf_njjl", url+"/njjl/"+id);
				map.put("id", id);
				map.put("key", rs.getObject("key"));
				map.put("xh", rs.getObject("key"));
				map.put("_links", link);
				map.put("dwmc", rs.getObject("dwmc"));
				map.put("zczj", rs.getObject("zczj"));
				map.put("cs", rs.getObject("cs"));
				map.put("fddbr", rs.getObject("fddbr"));
				map.put("zsbh", rs.getObject("zsbh"));
				map.put("swsxz", rs.getObject("swsxz"));
				map.put("zrs", rs.getObject("zrs"));
				map.put("zyrs", rs.getObject("zyrs"));
				map.put("clsj", rs.getObject("clsj"));
				map.put("ZJPZWH", rs.getObject("ZJPZWH"));
				map.put("DZHI", rs.getObject("DZHI"));
				map.put("YYZZHM", rs.getObject("YYZZHM"));
				map.put("JYFW", rs.getObject("JYFW"));
				map.put("issd", rs.getObject("issd"));
				return map;
				}
			});
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
	
	/**
	 * 
	 * 
	 * @param id
	 * @return 事务所详细信息
	 */
	public Map<String,Object> swsxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select a.dwmc,if(a.CS_DM is not null,(select c.mc from dm_cs c where c.ID=a.CS_DM),null) as cs,	a.fddbr,a.dzhi,a.sjlzxsbwh,a.zcdz, ");
		sb.append("		date_format(a.sglzxsbsj,'%Y-%m-%d') as sglzxsbsj,date_format(a.zjpzsj,'%Y-%m-%d')");
		sb.append("		as zjpzsj,a.yzbm,a.zjpzwh,a.czhen,a.dhua,a.szyx, ");
		sb.append("		a.txyxming,a.xtyyx,a.xtyphone,a.JGZCH as zsbh,	a.zczj,a.jyfw,a.GZ_DM as isgz,");
		sb.append("		(select count(id) from zs_zysws where jg_id=a.id)+");
		sb.append("		(select count(id) from zs_cyry where jg_id=a.id and CYRYZT_DM=1) as zrs, ");
		sb.append("		if(a.JGXZ_DM is not null,(select b.mc from dm_jgxz b where b.ID=a.JGXZ_DM),null) as swsxz,a.szphone,a.gsyhmcbh,a.dzyj,a.yhdw,date_format(a.yhsj,'%Y-%m-%d') as yhsj, ");
		sb.append("		a.gzbh,a.gzdw,a.gzry,date_format(a.gzsj,'%Y-%m-%d') as gzsj,a.yzbh,a.yzdw,");
		sb.append("		a.yzry,date_format(a.yzsj,'%Y-%m-%d') as yzsj, a.tgzt_dm as tgzt,");
		sb.append("		a.tthybh,date_format(a.rhsj,'%Y-%m-%d') as rhsj,a.khh,a.khhzh,a.fj,a.swdjhm,a.jbqk, ");
		sb.append("		a.glzd,a.gddh,a.bgcszczm,a.yyzzhm,DATE_FORMAT(a.swszsclsj,'%Y-%m-%d') AS clsj,");
		sb.append("		a.jgdmzh,a.wangzhi,a.CS_DM as csdm,a.JGXZ_DM as jgxzdm from		"); 
		sb.append("		 zs_jg a ");
		sb.append("		WHERE a.id =?");
		String sql = "SELECT @rownum:=@rownum+1 AS 'key',b.* FROM zs_nbjgsz b,(SELECT @rownum:=0) zs_jg WHERE  b.jg_id=? ";
		List<Map<String, Object>> tl = this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
		Map<String,Object> ll =tl.get(0);
		ll.put("nbjgsz", this.jdbcTemplate.queryForList(sql,new Object[]{id}));
		return ll;
	}
	/**
	 * 
	 * @param id
	 * @return 执业人员信息
	 */
	public List<Map<String,Object>> zyryxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key',@rownum AS xh,c.xming,c.id, ");
		sb.append("	case a.czr_dm when 1 then '是'  when 2 then '否' else null end as czr,");
		sb.append("	case a.fqr_dm when 1 then '是'  when 2 then '否' else null end as fqr,");
		sb.append("	case a.sz_dm when 1 then '是'  when 2 then '否' else null end as sz ");
		sb.append("	from zs_zysws a,zs_ryjbxx c,(select @rownum:=0) zs_jg ");
		sb.append("	where a.jg_id=?");
		sb.append("	 and a.ry_id = c.id");
		sb.append("	 and a.ZYZT_DM=1");
		return this.jdbcTemplate.query(sb.toString(),new Object[]{id},
				new RowMapper<Map<String,Object>>() {
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				String id = hashids.encode(rs.getLong("id"));
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("zyid", id);
				map.put("key", rs.getObject("key"));
				map.put("xh", rs.getObject("xh"));
				map.put("xming", rs.getObject("xming"));
				map.put("czr", rs.getObject("czr"));
				map.put("fqr", rs.getObject("fqr"));
				map.put("sz", rs.getObject("sz"));
				return map;
			}
		});
	}
	/**
	 * 
	 * @param id
	 * @return 从业人员信息
	 */
	public List<Map<String,Object>> cyryxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key', c.xming,c.id, ");
		sb.append("			 d.mc as xl, c.sfzh,");
		sb.append("			e.mc as zc from zs_cyry a,");
		sb.append("			zs_ryjbxx c,dm_xl d,");
		sb.append("			dm_zw e,(select @rownum:=0) zs_jg");
		sb.append("			where a.jg_id=? ");
		sb.append("			and a.ry_id = c.id ");
		sb.append("			and c.xl_dm = d.id ");
		sb.append("			and a.zw_dm = e.id");
		sb.append("			and a.CYRYZT_DM=1");
		return this.jdbcTemplate.query(sb.toString(),new Object[]{id},
				new RowMapper<Map<String,Object>>() {
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				String id = hashids.encode(rs.getLong("id"));
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("cyid", id);
				map.put("key", rs.getObject("key"));
				map.put("xl", rs.getObject("xl"));
				map.put("xming", rs.getObject("xming"));
				map.put("sfzh", rs.getObject("sfzh"));
				map.put("zc", rs.getObject("zc"));
				return map;
			}
		});
	}
	/**
	 * 
	 * @param id
	 * @return 出资人列表
	 */
	public List<Map<String,Object>> czrylb(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key',c.xming,");
		sb.append("		a.cze,");
		sb.append("		b.dwmc,");
		sb.append("		c.sfzh");
		sb.append("		from ");
		sb.append("		zs_zysws a,zs_jg b ,");
		sb.append("			zs_ryjbxx c,(select @rownum:=0) zs_jg ");
		sb.append("			where");
		sb.append("			 b.id =? ");
		sb.append("		and b.id=a.jg_id");
		sb.append("		and a.czr_dm = 1");
		sb.append("		 and a.ry_id = c.id");
		sb.append("		 and c.yxbz = '1'");
		sb.append("		 and c.rysf_dm = '1'");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * @param id
	 * @return 事务所变更信息
	 */
	public List<Map<String,Object>> swsbgxx(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("		select @rownum:=@rownum+1 as 'key',date_format(b.gxsj,'%Y-%m-%d') as xgxsj,b.* from zs_jglsbgxxb b,(select @rownum:=0) zs_jg where b.JGB_ID = ? ");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 
	 * @param id
	 * @return 年检记录
	 */
	public List<Map<String,Object>> njjl(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("	select 		 ");
		sb.append("		@rownum:=@rownum+1 as 'key',");
		sb.append("		f.nd,	 ");
		sb.append("		a.dwmc,");
		sb.append("		c.spyj,");
		sb.append("		case f.ztdm when 1 then \"保存\"  when 2 then \"自检\" when 0 then \"退回\" when 3 then \"年检\" else null end as njzt,	");	 
		sb.append("		date_format( c.spsj,'%Y-%m-%d') as spsj ");
		sb.append("		from		");
		sb.append("		 zs_jg a,");
		sb.append("		 zs_spzx b,");
		sb.append("		 zs_spxx c,");
		sb.append("		 zs_splcbz d,");
		sb.append("		 zs_splc e,");
		sb.append("		zs_jg_njb f,(select @rownum:=0) zs_jg");
		sb.append("		where		 ");
		sb.append("		a.id = ?");
		sb.append("			and b.zsjg_id = a.id ");
		sb.append("		and c.spid = b.id ");
		sb.append("		and c.ispass = 'y' ");
		sb.append("		 and d.id = c.lcbzid ");
		sb.append("		 and e.id = d.lcid");
		sb.append("		  and e.lclxid ='11'");
		sb.append("		and f.ztdm = 3");
		sb.append("		and f.id = b.sjid");
		sb.append("		and a.id = f.ZSJG_ID");
		sb.append("		order by f.nd desc ");
		return this.jdbcTemplate.queryForList(sb.toString(),new Object[]{id});
	}
	/**
	 * 事务所设立审批查询
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> swsslspcx(int pn,int ps,Map<String, Object> qury) {
		Condition condition = new Condition();
		condition.add("b.dwmc", Condition.FUZZY, qury.get("dwmc"));
		if(qury.containsKey("sbsj")){
			String sbsj = new Common().getTime2MysqlDateTime((String)qury.get("sbsj")).substring(0,10);
			condition.add("b.SBCLSJ", Condition.GREATER_EQUAL, sbsj);
		}
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key', b.dwmc,c.NAMES ");
		sb.append(" as lxr,d.MC as jgzt,date_format(b.SBCLSJ,'%Y-%m-%d') as sbclsj from zs_jg b,fw_users c,dm_jgzt d,(select @rownum:=?) zs_ry "
					+condition.getSql()+" and b.jgzt_dm =5");
		sb.append(" and c.JG_ID=b.id and d.ID=b.jgzt_dm LIMIT ?, ?"); 
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		ob.put("page", meta);
		return ob;
	}
	/**
	 * 添加机构
	 * @param jgtj
	 * @return
	 */
	public Object insertjg(Map<String, Object> jgtj){
		if(this.jdbcTemplate.queryForList("select id from zs_jg where dwmc=? ",jgtj.get("dwmc")).size()!=0){
			return null;
		}
		boolean is=false;
		if(jgtj.containsKey("iswdfs")){
			if(jgtj.get("iswdfs").toString().equals("true")){
				is=true;
			}
		}
		if(is){
			return this.insertAndGetKeyByJdbc("insert into zs_jg (DWMC,CS_DM,jgzt_dm,PARENTJGID,SBCLSJ,YXBZ) values(?,?,5,0,sysdate(),0)",
					new Object[]{jgtj.get("dwmc"),jgtj.get("cs")},new String[] {"ID"});
		}
		String sql ="insert into zs_jg (DWMC,CS_DM,jgzt_dm,SBCLSJ,YXBZ) values(?,?,5,sysdate(),0)";
		return this.insertAndGetKeyByJdbc(sql,new Object[]{jgtj.get("dwmc"),jgtj.get("cs")},new String[] {"ID"});
	}
	/**
	 * 当前用户分所/判断是否主所
	 * @param pid
	 * @return
	 */
	public Object chilchenJG(Object pid){
		List<Map<String, Object>> fs = this.jdbcTemplate.query("select ID,DWMC from zs_jg where PARENTJGID=? and jgzt_dm=11",new Object[]{pid},
				new RowMapper<Map<String,Object>>() {
					public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
						Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
						String id = hashids.encode(rs.getLong("id"));
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("ID", id);
						map.put("DWMC", rs.getObject("DWMC"));
						return map;
					}
				});
		if(fs.size()>0){
			return fs;
		}else{
			List<Map<String, Object>> isfs = this.jdbcTemplate.queryForList("select id from zs_jg a where (a.PARENTJGID >0) and a.YXBZ=1 and a.id=?",new Object[]{pid});
			if(isfs.size()>0){
				return 1;
			}
		};
		return false;
		
	}

	public List<Map<String, Object>> getCzrxx(Long jgId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select @rownum:=@rownum+1 as id, r.xming,if(r.XB_DM=1 ,'男','女')as xb,z.zyzgzsbh,z.zyzsbh,xl.MC as xl,zw.MC as zw ");
		sb.append(" from zs_zysws z , zs_jg j , zs_ryjbxx r,dm_xl xl,dm_zw zw,(SELECT @rownum:=0) tmp ");
		sb.append(" where z.JG_ID = j.ID ");
		sb.append(" and r.ID = z.RY_ID ");
		sb.append(" and r.XL_DM = xl.ID ");
		sb.append(" and z.ZW_DM = zw.ID ");
		sb.append(" and j.id = ? ");
		sb.append(" and z.CZR_DM = 1 ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(), new Object[]{jgId});
		return ls;
	}

	public List<Map<String, Object>> getFqrxx(Long jgId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select @rownum:=@rownum+1 as id, r.xming,if(r.XB_DM=1 ,'男','女')as xb,z.zyzgzsbh,z.zyzsbh,xl.MC as xl,zw.MC as zw ");
		sb.append(" from zs_zysws z , zs_jg j , zs_ryjbxx r,dm_xl xl,dm_zw zw,(SELECT @rownum:=0) tmp ");
		sb.append(" where z.JG_ID = j.ID ");
		sb.append(" and r.ID = z.RY_ID ");
		sb.append(" and r.XL_DM = xl.ID ");
		sb.append(" and z.ZW_DM = zw.ID ");
		sb.append(" and j.id = ? ");
		sb.append(" and z.FQR_DM = 1 ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(), new Object[]{jgId});
		return ls;
	}

	public Map<String, Object> getSumZysws(Long jgId, int page, int pagesize) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT sql_calc_found_rows  @rownum:=@rownum+1 as id ,r.xming,xb.MC AS xb, YEAR(FROM_DAYS(DATEDIFF(NOW(),r.SRI))) AS age, xl.mc AS xl,  ");
		sb.append(" z.cze , concat(round((z.cze/j.zczj)*100,2),'%')  as bl ");
		sb.append(" FROM zs_zysws z, zs_ryjbxx r, dm_xb xb, dm_xl xl, zs_jg j,(SELECT @rownum:=0) tmp ");
		sb.append(" WHERE z.JG_ID = ?  ");
		sb.append(" AND z.JG_ID = j.ID  ");
		sb.append(" AND z.RY_ID = r.ID  ");
		sb.append(" AND r.XB_DM = xb.ID  ");
		sb.append(" AND r.XL_DM = xl.ID  ");
		sb.append(" AND z.yxbz = 1  ");
		sb.append(" AND z.ZYZT_DM = 1 ");
		sb.append(" limit ?,? ");
		
		int startIndex = pagesize * (page - 1);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(), new Object[]{jgId,startIndex,pagesize});
		
		String sql = "select FOUND_ROWS()";
		Integer total = jdbcTemplate.queryForObject(sql,  Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);
		
		return obj;
	}
	
	public Map<String, Object> getSumCyry(Long jgId, int page, int pagesize) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT sql_calc_found_rows  @rownum:=@rownum+1 as id , r.xming,xb.MC AS xb, YEAR(FROM_DAYS(DATEDIFF(NOW(),r.SRI))) AS age, xl.mc AS xl,  ");
		sb.append(" z.zgxlzymc,z.xzsngzgw ");
		sb.append(" FROM zs_cyry z, zs_ryjbxx r, dm_xb xb, dm_xl xl, zs_jg j,(SELECT @rownum:=0) tmp ");
		sb.append(" WHERE z.JG_ID = ? ");
		sb.append(" AND z.JG_ID = j.ID  ");
		sb.append(" AND z.RY_ID = r.ID  ");
		sb.append(" AND r.XB_DM = xb.ID  ");
		sb.append(" AND r.XL_DM = xl.ID  ");
		sb.append(" AND z.yxbz = 1  ");
		sb.append(" and z.CYRYZT_DM = 1 ");
		sb.append(" limit ?,? ");
		
		int startIndex = pagesize * (page - 1);
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(), new Object[]{jgId,startIndex,pagesize});
		
		String sql = "select FOUND_ROWS()";
		Integer total = jdbcTemplate.queryForObject(sql,  Integer.class);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);
		
		return obj;
	}
	
	public Map<String,Object> swsbgqktj(int pn,int ps,Map<String, Object> qury) {
		Condition condition = new Condition();
		condition.add("DATE_FORMAT(g.SPSJ,'%Y')", Condition.EQUAL, qury.get("YEAR"));
		if(qury.containsKey("MON")){
			condition.add("DATE_FORMAT(g.SPSJ,'%m')", Condition.EQUAL, qury.get("MON"));
		}
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key', g.*,DATE_FORMAT(g.SPSJ,'%Y-%m-%d') AS SJ from (SELECT b.SPSJ,c.JGZCH,c.DWMC, CONCAT(c.DZHI,' 邮编',c.YZBM) AS YBDZ, DATE_FORMAT(c.SBCLSJ,'%Y-%m-%d') AS SLSJ,c.FDDBR,c.DHUA,'事务所设立' AS BGSZ,'' AS BGNR");
		sb.append("				FROM ");
		sb.append("				zs_spzx a,zs_spxx b,zs_jg c");
		sb.append("				WHERE a.ID=b.SPID AND a.SJID=c.ID AND a.LCBZID='402882891f4b1acc011f546cf7d10091'");
		sb.append("				 AND a.ZTBJ='N' AND b.ISPASS='Y' AND c.TGZT_DM IN (6,7) ");
		sb.append("				union ");
		sb.append("				SELECT b.SPSJ,c.JGZCH,c.DWMC, CONCAT(c.DZHI,' 邮编',c.YZBM) AS YBDZ, DATE_FORMAT(c.SBCLSJ,'%Y-%m-%d') AS SLSJ,c.FDDBR,c.DHUA,'事务所分所设立' AS BGSZ,'' AS BGNR");
		sb.append("				FROM ");
		sb.append("				zs_spzx a,zs_spxx b,zs_jg c");
		sb.append("				WHERE a.ID=b.SPID AND a.SJID=c.ID AND a.LCBZID='4028808722837891012283900818002e' AND b.LCBZID='4028808722837891012283900818002e'");
		sb.append("				 AND a.ZTBJ='N' AND b.ISPASS='Y' AND c.TGZT_DM IN (6,7) ");
		sb.append("				union");
		sb.append("				SELECT b.SPSJ,c.JGZCH,c.DWMC, CONCAT(c.DZHI,' 邮编',c.YZBM) AS YBDZ, DATE_FORMAT(c.SBCLSJ,'%Y-%m-%d') AS SLSJ,c.FDDBR,c.DHUA,'此所已注销' AS BGSZ,'' AS BGNR");
		sb.append("				FROM ");
		sb.append("				zs_spzx a,zs_spxx b,zs_jg c,zs_jgzx d");
		sb.append("				WHERE a.ID=b.SPID AND a.SJID=d.ID AND a.LCBZID='402881831be2e6af011be3adc72c0013'");
		sb.append("				 AND a.ZTBJ='N' AND b.ISPASS='Y' and d.JG_ID=c.ID and d.SPZT=2");
		sb.append("				union");
		sb.append("				SELECT b.SPSJ,c.JGZCH,c.DWMC, CONCAT(c.DZHI,' 邮编',c.YZBM) AS YBDZ, DATE_FORMAT(c.SBCLSJ,'%Y-%m-%d') AS SLSJ,c.FDDBR,c.DHUA,e.MC AS BGSZ,");
		sb.append("				concat(e.MC,' 由 ',ifnull(e.JZHI,'无内容'),' 变为 ',e.XZHI) AS BGNR");
		sb.append("				FROM ");
		sb.append("				zs_spzx a,zs_spxx b,zs_jg c,zs_jgbgspb d,zs_jgbgxxb e");
		sb.append("				WHERE a.ID=b.SPID AND a.SJID=d.ID AND a.LCBZID='402881831be2e6af011be3ab8b84000d'");
		sb.append("				 AND a.ZTBJ='N' AND b.ISPASS='Y' and d.JG_ID=c.ID and d.SPZT_DM=8 and e.JGBGSPB_ID=d.ID");
		sb.append("				union");
		sb.append("				SELECT b.SPSJ,c.JGZCH,c.DWMC, CONCAT(c.DZHI,' 邮编',c.YZBM) AS YBDZ, DATE_FORMAT(c.SBCLSJ,'%Y-%m-%d') AS SLSJ,c.FDDBR,c.DHUA,e.MC AS BGSZ,");
		sb.append("				concat(e.MC,' 由 ',ifnull(e.JZHI,'无内容'),' 变为 ',e.XZHI) AS BGNR");
		sb.append("				FROM ");
		sb.append("				zs_spzx a,zs_spxx b,zs_jg c,zs_jgbgspb d,zs_jgbgxxb e");
		sb.append("				WHERE a.ID=b.SPID AND a.SJID=d.ID AND a.LCBZID='40288087228378910122838ecac50023' AND b.LCBZID='40288087228378910122838ecac50023'");
		sb.append("				 AND a.ZTBJ='N' AND b.ISPASS='Y' and d.JG_ID=c.ID and d.SPZT_DM=8 and e.JGBGSPB_ID=d.ID ) as g,(select @rownum:=?) zs_ry ");
		sb.append("				 "+condition.getSql()+"  ");
		sb.append("				order by g.jgzch,g.spsj desc LIMIT ?, ?");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		ob.put("page", meta);
		return ob;
	}
}
