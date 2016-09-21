package gov.gdgs.zs.dao;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hashids.Hashids;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class HfglDao extends BaseDao{
	/**
	 * 会员会费缴纳情况
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> hyhfjnqk(int pn,int ps,Map<String, Object> qury) throws Exception{
		 Calendar ca = Calendar.getInstance();
	     Object lyear =new Object();
	     DateFormat df = new SimpleDateFormat("MM-dd");
	     if(qury.containsKey("nd")){
	    	 lyear=qury.get("nd");
	     }else if(df.parse(ca.get(Calendar.MONTH)+1+"-"+ca.get(Calendar.DATE)).after(df.parse("07-15"))){
	    	 lyear=ca.get(Calendar.YEAR);
	     }else{
	    	 lyear=ca.get(Calendar.YEAR)-1;
	     }
	     Condition condition = new Condition();
		 condition.add("b.dwmc", Condition.FUZZY, qury.get("dwmc"));
		 ArrayList<Object> params = condition.getParams();
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select  SQL_CALC_FOUND_ROWS g.* from (select   @rownum:=@rownum+1 as 'key',b.dwmc,'"+lyear+"' as nd,b.id,");
		sb.append("		(select a.ZGYWSR from zs_cwbb_lrgd a where a.jg_id=b.id and a.nd='"+lyear+"' and a.ztbj=1 order by a.TIMEVALUE desc limit 1) as jyzsr,");
		sb.append("		f_yjtt(b.id, '"+lyear+"')+");
		sb.append("		(select count(c.RY_ID)*800 as yjgr from zs_zysws c where c.JG_ID=b.id  and c.YXBZ=1  and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') ) as yjz,");
		sb.append("		f_yjtt(b.id, '"+lyear+"') as yjtt,");
		sb.append("		(select sum(e.YJTTHF) from zs_hyhfjnqk e where e.JG_ID=b.id and e.ND='"+lyear+"' and e.yxbz=1) as yftt,");
		sb.append("		f_qjtt(f_yjtt(b.id, '"+lyear+"'),b.id,'"+lyear+"') as qjtt,");
		sb.append("		(select count(c.RY_ID)*800 as yjgr from zs_zysws c where c.JG_ID=b.id  and c.YXBZ=1  and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') )as yjgr,");
		sb.append("		(select sum(e.YJGRHF) from zs_hyhfjnqk e where e.JG_ID=b.id and e.ND='"+lyear+"' and e.yxbz=1) as yfgr,");
		sb.append("		f_qjgr((select count(c.RY_ID)*800 as yjgr from zs_zysws c where c.JG_ID=b.id  and c.YXBZ=1  and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') ),b.id,'"+lyear+"') as qjgr");
		sb.append("		,(select v.id from zs_sdjl_jg v where v.jg_id=b.id and v.lx=2 and v.yxbz=1 limit 1) as issd ");
		sb.append("		from zs_jg b,(select @rownum:=0) zs_ry ");
		sb.append("	"+condition.getSql()+"	and b.yxbz=1 ) g ");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "dwmc":
				if(asc){
					sb.append("		    order by convert( g.dwmc USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( g.dwmc USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "jyzsr":
				if(asc){
					sb.append("		    order by g.jyzsr ");
				}else{
					sb.append("		    order by g.jyzsr desc");
				}
				break;
			case "qjtt":
				if(asc){
					sb.append("		    order by g.qjtt ");
				}else{
					sb.append("		    order by g.qjtt desc");
				}
				break;
			case "qjgr":
				if(asc){
					sb.append("		    order by g.qjgr ");
				}else{
					sb.append("		    order by g.qjgr desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
		List<Map<String,Object>> ls = this.jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String,Object>>() {
			public Map<String,Object> mapRow(ResultSet rs, int arg1) throws SQLException{
				Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
				String id = hashids.encode(rs.getLong("id"));
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("jgid", id);
				map.put("dwmc", rs.getObject("dwmc"));
				map.put("jyzsr", rs.getObject("jyzsr"));
				map.put("key", rs.getObject("key"));
				map.put("nd", rs.getObject("nd"));
				map.put("qjgr", rs.getObject("qjgr"));
				map.put("qjtt", rs.getObject("qjtt"));
				map.put("yfgr", rs.getObject("yfgr"));
				map.put("yftt", rs.getObject("yftt"));
				map.put("yjgr", rs.getObject("yjgr"));
				map.put("yjtt", rs.getObject("yjtt"));
				map.put("yjz", rs.getObject("yjz"));
				map.put("issd", rs.getObject("issd"));
				return map;
			}
		});
	     int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		StringBuffer ub = new StringBuffer();
		ub.append("		select  'dqytjkey' as 'jgid','当前页统计：' as dwmc,h.nd,'0' as id,sum(h.jyzsr) as jyzsr,sum(h.yjz) as yjz,sum(h.yjtt) as yjtt,");
		ub.append("		sum(h.yftt) as yftt,sum(h.qjtt) as qjtt,sum(h.yjgr) as yjgr,sum(h.yfgr) as yfgr,");
		ub.append("		sum(h.qjgr) as qjgr,'1' as issd from (select g.* from (select   b.dwmc,'"+lyear+"' as nd,");
		ub.append("			(select a.ZGYWSR from zs_cwbb_lrgd a where a.jg_id=b.id and a.nd='"+lyear+"' and a.ztbj=1 order by a.TIMEVALUE desc limit 1) as jyzsr,");
		ub.append("			f_yjtt(b.id, '"+lyear+"')+");
		ub.append("			(select count(c.RY_ID)*800 as yjgr from zs_zysws c where c.JG_ID=b.id  and c.YXBZ=1 ");
		ub.append("			and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') ) as yjz,");
		ub.append("			f_yjtt(b.id, '"+lyear+"') as yjtt,");
		ub.append("			(select sum(e.YJTTHF) from zs_hyhfjnqk e where e.JG_ID=b.id and e.ND='"+lyear+"' and e.yxbz=1) as yftt,");
		ub.append("			f_qjtt(f_yjtt(b.id, '"+lyear+"'),b.id,'"+lyear+"') as qjtt,");
		ub.append("			(select count(c.RY_ID)*800 as yjgr from zs_zysws c where c.JG_ID=b.id  and c.YXBZ=1 ");
		ub.append("			and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') )as yjgr,");
		ub.append("			(select sum(e.YJGRHF) from zs_hyhfjnqk e where e.JG_ID=b.id and e.ND='"+lyear+"' and e.yxbz=1) as yfgr,");
		ub.append("			f_qjgr((select count(c.RY_ID)*800 as yjgr from zs_zysws c where");
		ub.append("					c.JG_ID=b.id  and c.YXBZ=1  and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') ),b.id,'"+lyear+"') as qjgr ");
		ub.append("			from zs_jg b");
		ub.append("		"+condition.getSql()+"	and b.yxbz=1) as g  ");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "dwmc":
				if(asc){
					ub.append("		    order by convert( g.dwmc USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					ub.append("		    order by convert( g.dwmc USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "jyzsr":
				if(asc){
					ub.append("		    order by g.jyzsr ");
				}else{
					ub.append("		    order by g.jyzsr desc");
				}
				break;
			case "qjtt":
				if(asc){
					ub.append("		    order by g.qjtt ");
				}else{
					ub.append("		    order by g.qjtt desc");
				}
				break;
			case "qjgr":
				if(asc){
					ub.append("		    order by g.qjgr ");
				}else{
					ub.append("		    order by g.qjgr desc");
				}
				break;
			}
		}
		ub.append("		    LIMIT ?, ? ) h");
		Map<String, Object> tj = this.jdbcTemplate.queryForMap(ub.toString(),params.toArray());
		ls.add(tj);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		meta.put("total",new BigDecimal(total).divide(new BigDecimal(ps),5,BigDecimal.ROUND_HALF_DOWN).doubleValue()*(ps+1));
		ob.put("page", meta);
		return ob;
	}
	/**
	 * 发票打印查询
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> fpdy(int pn,int ps,Map<String, Object> qury,String uname) throws Exception{
		Condition condition = new Condition();
		condition.add("a.dwmc", Condition.FUZZY, qury.get("dwmc"));
		condition.add("a.nd", Condition.EQUAL, qury.get("nd"));
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',a.ID as jlid,a.JG_ID as jgid,a.SCJLID as scid,a.ND,a.DWMC,"); 
		sb.append("		a.JFZE,a.YJTTHF,a.YJGRHF,a.JFRQ,a.DYCS,a.BZ,a.YJE,a.GGR,a.XGR,'"+uname+"' as KPR from zs_hyhfjnqk a,(select @rownum:=?) zs_ry");
		sb.append("		"+condition.getSql()+" and a.yxbz=1 order by a.LRRQ desc LIMIT ?, ?");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),params.toArray());
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()", int.class);
		Map<String,Object> ob = new HashMap<>();
		ob.put("data", ls);
		Calendar ca = Calendar.getInstance();
		String nd = ca.get(Calendar.YEAR)+"";
		if(qury.containsKey("nd")){
			nd=qury.get("nd").toString();
		}
		ob.put("jftj", this.jdbcTemplate.queryForMap("select sum(JFZE*DYCS) as dyze,'"+nd+"' as dynd,sum(DYCS) as cs,count(id) as ts  from zs_hyhfjnqk where nd=?",nd));
		Map<String, Object> meta = new HashMap<>();
		meta.put("pageNum", pn);
		meta.put("pageSize", ps);
		meta.put("pageTotal",total);
		ob.put("page", meta);
		return ob;
	}
	
	/**
	 * 发票打印分配
	 * @param jlid
	 * @param fptj
	 * @param name
	 * @return
	 */
	public boolean ttgefp(String jlid,Map<String, Object> fptj,String name){
		if((new BigDecimal(fptj.get("YJTTHF").toString()).add(new BigDecimal(fptj.get("YJGRHF").toString())).compareTo(new BigDecimal(fptj.get("jfje").toString()))==0)){
			String sql="update zs_hyhfjnqk set YJTTHF=?,YJGRHF=?,GGR=? where id =?";
			this.jdbcTemplate.update(sql,new Object[]{fptj.get("YJTTHF"),fptj.get("YJGRHF"),name,jlid});
			String yfgr =this.jdbcTemplate.queryForObject("select sum(e.YJGRHF) from zs_hyhfjnqk e where e.JG_ID=? and e.ND=? and e.yxbz=1",
						  new Object[]{fptj.get("jgid"),fptj.get("nd")}, String.class);
			int zys = (int)new BigDecimal(yfgr).divide(new BigDecimal("800"),1,BigDecimal.ROUND_HALF_EVEN).doubleValue();
			int yzys = this.jdbcTemplate.queryForObject("select count(b.ry_id) from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1",
						new Object[]{fptj.get("nd"),fptj.get("jgid")},int.class);
			if(yzys>0){
				if(zys>yzys){
					for(Map<String, Object> k:this.jdbcTemplate.queryForList("select ry_id from zs_zysws a where a.jg_id=? and a.yxbz=1 and a.ry_id not in(select b.ry_id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1) limit ?",
							new Object[]{fptj.get("jgid"),fptj.get("nd"),fptj.get("jgid"),zys-yzys})){
						this.jdbcTemplate.update("insert into zs_hyhfjfryls (ID,ND,RY_ID,JG_ID,JF_ID,SCJLID,LRRQ,YXBZ) values(?,?,?,?,?,?,sysdate(),1)",new Object[]{new Common().newUUID(),fptj.get("nd"),k.get("ry_id"),fptj.get("jgid"),jlid,fptj.get("scid")});
					}
				}else if(zys<yzys){
					for(Map<String, Object> k:this.jdbcTemplate.queryForList("select b.id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1 limit ?",
							new Object[]{fptj.get("nd"),fptj.get("jgid"),yzys-zys})){
						this.jdbcTemplate.update("update zs_hyhfjfryls set YXBZ=0 where id=?",new Object[]{k.get("id")});
					}
				}
			}else if(zys>0){
				for(Map<String, Object> k:this.jdbcTemplate.queryForList("select ry_id from zs_zysws a where a.jg_id=? and a.yxbz=1 and a.ry_id not in(select b.ry_id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1) limit ?",
						new Object[]{fptj.get("jgid"),fptj.get("nd"),fptj.get("jgid"),zys})){
					this.jdbcTemplate.update("insert into zs_hyhfjfryls (ID,ND,RY_ID,JG_ID,JF_ID,SCJLID,LRRQ,YXBZ) values(?,?,?,?,?,?,sysdate(),1)",new Object[]{new Common().newUUID(),fptj.get("nd"),k.get("ry_id"),fptj.get("jgid"),jlid,fptj.get("scid")});
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 发票打印修改
	 * @param jlid
	 * @param fptj
	 * @param name
	 * @return
	 */
	public boolean fpjexg(String jlid,Map<String, Object> fptj,String name){
		if((new BigDecimal(fptj.get("XGYJTTHF").toString()).add(new BigDecimal(fptj.get("XGYJGRHF").toString())).compareTo(new BigDecimal(fptj.get("JFZE").toString()))==0)){
			if(fptj.containsKey("yje")){
				 this.jdbcTemplate.update("update zs_hyhfjnqk set JFZE=?,YJTTHF=?,YJGRHF=?,XGR=?,XGRQ=sysdate() where id =?",
						 new Object[]{fptj.get("JFZE"),fptj.get("XGYJTTHF"),fptj.get("XGYJGRHF"),name,jlid});
			}else{
				this.jdbcTemplate.update("update zs_hyhfjnqk set JFZE=?,YJTTHF=?,YJGRHF=?,XGR=?,YJE=?,XGRQ=sysdate() where id =?",
						new Object[]{fptj.get("JFZE"),fptj.get("XGYJTTHF"),fptj.get("XGYJGRHF"),name,fptj.get("jfje"),jlid});
			}
			String yfgr =this.jdbcTemplate.queryForObject("select sum(e.YJGRHF) from zs_hyhfjnqk e where e.JG_ID=? and e.ND=? and e.yxbz=1",
					new Object[]{fptj.get("jgid"),fptj.get("nd")}, String.class);
			int zys = (int)new BigDecimal(yfgr).divide(new BigDecimal("800"),1,BigDecimal.ROUND_HALF_EVEN).doubleValue();
			int yzys = this.jdbcTemplate.queryForObject("select count(b.ry_id) from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1",
					new Object[]{fptj.get("nd"),fptj.get("jgid")},int.class);
			if(yzys>0){
				if(zys>yzys){
					for(Map<String, Object> k:this.jdbcTemplate.queryForList("select ry_id from zs_zysws a where a.jg_id=? and a.yxbz=1 and a.ry_id not in(select b.ry_id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1) limit ?",
							new Object[]{fptj.get("jgid"),fptj.get("nd"),fptj.get("jgid"),zys-yzys})){
						this.jdbcTemplate.update("insert into zs_hyhfjfryls (ID,ND,RY_ID,JG_ID,JF_ID,SCJLID,LRRQ,YXBZ) values(?,?,?,?,?,?,sysdate(),1)",new Object[]{new Common().newUUID(),fptj.get("nd"),k.get("ry_id"),fptj.get("jgid"),jlid,fptj.get("scid")});
					}
				}else if(zys<yzys){
					for(Map<String, Object> k:this.jdbcTemplate.queryForList("select b.id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1 limit ?",
							new Object[]{fptj.get("nd"),fptj.get("jgid"),yzys-zys})){
						this.jdbcTemplate.update("update zs_hyhfjfryls set YXBZ=0 where id=?",new Object[]{k.get("id")});
					}
				}
			}else if(zys>0){
				for(Map<String, Object> k:this.jdbcTemplate.queryForList("select ry_id from zs_zysws a where a.jg_id=? and a.yxbz=1 and a.ry_id not in(select b.ry_id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1) limit ?",
						new Object[]{fptj.get("jgid"),fptj.get("nd"),fptj.get("jgid"),zys})){
					this.jdbcTemplate.update("insert into zs_hyhfjfryls (ID,ND,RY_ID,JG_ID,JF_ID,SCJLID,LRRQ,YXBZ) values(?,?,?,?,?,?,sysdate(),1)",new Object[]{new Common().newUUID(),fptj.get("nd"),k.get("ry_id"),fptj.get("jgid"),jlid,fptj.get("scid")});
				}
			}
			return true;
		}
		return false;
	}
	//费用统计
	public Map<String,Object> fytj(Map<String, Object> qury) throws Exception{
		 Calendar ca = Calendar.getInstance();
	     Object lyear =new Object();
	     DateFormat df = new SimpleDateFormat("MM-dd");
	     if(qury.containsKey("nd")){
	    	 lyear=qury.get("nd");
	     }else if(df.parse(ca.get(Calendar.MONTH)+1+"-"+ca.get(Calendar.DATE)).after(df.parse("07-15"))){
	    	 lyear=ca.get(Calendar.YEAR);
	     }else{
	    	 lyear=ca.get(Calendar.YEAR)-1;
	     }
	     StringBuffer sb = new StringBuffer();
	     sb.append("			select '"+lyear+"' as nd, sum(if(g.jyzsr>0,g.jyzsr,0)) as yyz,sum(if(g.yftt>0,g.yftt,0))+sum(if(g.yfgr>0,g.yfgr,0)) as yfz,sum(if(g.yftt>0,g.yftt,0)) as yft,sum(if(g.yfgr>0,g.yfgr,0)) as yfg,");
	     sb.append("		sum(if(g.yjtt>0,g.yjtt,0))+sum(if(g.yjgr>0,g.yjgr,0))-(sum(if(g.yftt>0,g.yftt,0))+sum(if(g.yfgr>0,g.yfgr,0))) as qjz,sum(if(g.yjtt>0,g.yjtt,0))-sum(if(g.yftt>0,g.yftt,0)) as qjt,");
	     sb.append("	  sum(if(g.yjgr>0,g.yjgr,0))-sum(if(g.yfgr>0,g.yfgr,0)) as qjg from(	select  ");
	     sb.append("		(select a.ZGYWSR from zs_cwbb_lrgd a where a.jg_id=b.id and a.nd='"+lyear+"' and a.ztbj=1 order by a.TIMEVALUE desc limit 1) as jyzsr,");
	     sb.append("		f_yjtt(b.id, '"+lyear+"') as yjtt,");
	     sb.append("		(select sum(e.YJTTHF) from zs_hyhfjnqk e where e.JG_ID=b.id and e.ND='"+lyear+"' and e.yxbz=1) as yftt,");
	     sb.append("		(select count(c.RY_ID)*800 as yjgr from zs_zysws c where c.JG_ID=b.id  and c.YXBZ=1  and c.ry_id not in (select d.RY_ID from zs_hyhfjfryls d where d.nd='"+lyear+"') )as yjgr,");
	     sb.append("		(select sum(e.YJGRHF) from zs_hyhfjnqk e where e.JG_ID=b.id and e.ND='"+lyear+"' and e.yxbz=1) as yfgr");
	     sb.append("		from zs_jg b where b.yxbz=1 ) g ");
	     return this.jdbcTemplate.queryForMap(sb.toString());
	  }
	/**
	 * 缴费单据上传
	 * @param file
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> upLoadJFSC(MultipartFile file,String uname) throws Exception{  
		InputStream stream = file.getInputStream();
		String fileType =file.getOriginalFilename().split("\\u002E")[1];
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook(stream);  
        }  
        else if (fileType.equals("xlsx")) {  
            wb = new XSSFWorkbook(stream);  
        }  
        else {  
        	throw new Exception("您输入的excel格式不正确");
        }  
        Sheet sheet1 = wb.getSheetAt(0);
        sheet1.removeRow(sheet1.getRow(0));
        Map<String,Object> rs=new HashMap<>();
        List<Object> fls=new ArrayList<Object>();
        String uuid2 = new Common().newUUID();
        int i=0,j=0;
        for (Row row : sheet1) {  
        	ArrayList<Object> params =new ArrayList<Object>();  
            for (Cell cell : row) {
            	switch (cell.getCellType()) {  
            	case XSSFCell.CELL_TYPE_NUMERIC :
            		if(DateUtil.isCellDateFormatted(cell)){
                		Date date = cell.getDateCellValue();
                		params.add(Common.getDate2MysqlDateTime(date));
                	}else {
                		params.add(cell);  
                	}
            		break;
            	case XSSFCell.CELL_TYPE_STRING:
            		params.add(cell.getStringCellValue());  
            		break;
            	case XSSFCell.CELL_TYPE_BLANK:
            		params.add("");
            		break;
            	}
            }
            String nd = (params.get(1)+"").substring(0,4);
            List<Map<String, Object>> jg = this.jdbcTemplate.queryForList("select a.id,f_yjtt(a.id,'"+nd+"') as yjtt,(select count(b.id) from zs_zysws b where b.JG_ID=a.ID and yxbz=1) as yjrs from zs_jg a where a.dwmc=? and a.yxbz=1",
            		params.get(3)+"");
            if(jg.size()==0){
            	j+=1;
            	fls.add("第 "+row.getRowNum()+"行："+params.get(3)+""+"（请检查事务所状态及名称）");
            	continue;
            }else{
            	Map<String, Object> jgs = jg.get(0);
            	String uuid = new Common().newUUID();
            	String yftt = params.get(5)+"";
            	String yfgr = params.get(6)+"";
            	if((params.get(5)+"").equals("")&&(params.get(6)+"").equals("")){
            		if((jgs.get("yjtt")+"").equals("null")){
            			yftt=params.get(4)+"";
            			yfgr="0";
                	}else{
	            		double syz = new BigDecimal(params.get(4)+"").subtract(new BigDecimal(jgs.get("yjtt")+"")).doubleValue();
	            		if(syz>0){
	            			int zys = (int)new BigDecimal(syz).divide(new BigDecimal("800"),1,BigDecimal.ROUND_HALF_EVEN).doubleValue();
	            			for(Map<String, Object> k:this.jdbcTemplate.queryForList("select ry_id from zs_zysws a where a.jg_id=? and a.yxbz=1 and a.ry_id not in(select b.ry_id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1) limit ?",
	            					new Object[]{jgs.get("id"),nd,jgs.get("id"),zys})){
	            				this.jdbcTemplate.update("insert into zs_hyhfjfryls (ID,ND,RY_ID,JG_ID,JF_ID,SCJLID,LRRQ,YXBZ) values(?,?,?,?,?,?,sysdate(),1)",new Object[]{new Common().newUUID(),nd,k.get("ry_id"),jgs.get("id"),uuid,uuid2});
	            			}
	            			yftt=(String) jgs.get("yjtt");
	            			yfgr=syz+"";
	            		}else{
	            			yftt=params.get(4)+"";
	            			yfgr="0";
	            		}
                	}
            	}else{
            		if(yftt.equals("")){
            			yftt="0";
            			int zys = (int)new BigDecimal(yfgr).divide(new BigDecimal("800"),1,BigDecimal.ROUND_HALF_EVEN).doubleValue();
            			for(Map<String, Object> k:this.jdbcTemplate.queryForList("select ry_id from zs_zysws a where a.jg_id=? and a.yxbz=1 and a.ry_id not in(select b.ry_id from zs_hyhfjfryls b where b.nd=? and b.JG_ID=? and b.yxbz=1) limit ?",
            					new Object[]{jgs.get("id"),nd,jgs.get("id"),zys})){
            				this.jdbcTemplate.update("insert into zs_hyhfjfryls (ID,ND,RY_ID,JG_ID,JF_ID,SCJLID,LRRQ,YXBZ) values(?,?,?,?,?,?,sysdate(),1)",new Object[]{new Common().newUUID(),nd,k.get("ry_id"),jgs.get("id"),uuid,uuid2});
            			}
            		}
            		if(yfgr.equals("")){
            			yfgr="0";
            		}
            	}
            	this.jdbcTemplate.update("insert into zs_hyhfjnqk (ID,JG_ID,DWMC,ND,JFZE,YJTTHF,YJGRHF,JFRQ,YJRS,SCJLID,YXBZ,BZ,LRRQ) values(?,?,?,?,?,?,?,?,?,?,'1',?,sysdate())",
            			new Object[]{uuid,jgs.get("id"),params.get(3)+"",nd,params.get(4)+"",yftt,yfgr,params.get(0)+"",jgs.get("yjrs"),uuid2,params.get(2)+""});
            	i+=1;
            }
        }
        this.jdbcTemplate.update("insert into zs_hyhfscjlb (ID,SUCESS,FAIL,SCRQ,SCR,YXBZ) values(?,?,?,sysdate(),?,1)",new Object[]{uuid2,i,j,uname});
        rs.put("success", i);
        rs.put("fail", j);
        rs.put("fls", fls);
        return rs;
    } 
	/**
	 * 非执业会费缴纳查询
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> fzyhfjn(int pn,int ps,Map<String, Object> qury,String uname) throws Exception{
		Calendar ca = Calendar.getInstance();
	     Object lyear =new Object();
	     DateFormat df = new SimpleDateFormat("MM-dd");
	     if(qury.containsKey("nd")){
	    	 lyear=qury.get("nd");
	     }else if(df.parse(ca.get(Calendar.MONTH)+1+"-"+ca.get(Calendar.DATE)).after(df.parse("07-15"))){
	    	 lyear=ca.get(Calendar.YEAR);
	     }else{
	    	 lyear=ca.get(Calendar.YEAR)-1;
	     }
		Condition condition = new Condition();
		condition.add("c.xming", Condition.FUZZY, qury.get("ximng"));
		condition.add("c.sfzh", Condition.FUZZY_LEFT, qury.get("sfzh"));
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add(1,lyear);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key','"+lyear+"' as nd,c.XMING,d.mc as XB,c.SFZH,e.mc as CS,DATE_FORMAT(a.FZYZCRQ,'%Y-%m-%d') AS FZYZCRQ, ");
		sb.append("a.ZYZGZSBH,a.FZYZCZSBH,a.ZZDW,b.JE,b.BZ,f.MC as ryzt,b.id as jlid,b.YJE,b.LRR,b.XGR,b.YJE,b.JFRQ,'"+uname+"' as KPR ");
		sb.append("	from zs_ryjbxx c,dm_xb d,dm_cs e,dm_ryspgczt f,(select @rownum:=?) zs_ry,zs_fzysws a left join zs_hyhffzyjfb b on a.RY_ID=b.RY_ID and b.ND=? and b.yxbz=1");
		sb.append("	"+condition.getSql()+" and a.FZYZT_DM=1 and c.ID=a.RY_ID and c.XB_DM=d.ID and c.CS_DM=e.ID and a.RYSPGCZT_DM=f.ID ");
		if(qury.containsKey("sorder")){
			Boolean asc = qury.get("sorder").toString().equals("ascend");
			switch (qury.get("sfield").toString()) {
			case "XMING":
				if(asc){
					sb.append("		    order by convert( c.xming USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( c.xming USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "CS":
				if(asc){
					sb.append("		    order by convert( e.mc USING gbk) COLLATE gbk_chinese_ci ");
				}else{
					sb.append("		    order by convert( e.mc USING gbk) COLLATE gbk_chinese_ci desc");
				}
				break;
			case "XB":
				if(asc){
					sb.append("		    order by c.xb_dm ");
				}else{
					sb.append("		    order by c.xb_dm desc");
				}
				break;
			case "JE":
				if(asc){
					sb.append("		    order by b.je ");
				}else{
					sb.append("		    order by b.je desc");
				}
				break;
			}
		}
		sb.append("		    LIMIT ?, ? ");
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
	 * 非执业添加
	 * @param fptj
	 * @param name
	 * @return
	 */
	public boolean fzytj(Map<String, Object> fptj,String name){
		if(!fptj.containsKey("nd")||!fptj.containsKey("sfzh")||!fptj.containsKey("je")||!fptj.containsKey("jfrq")){
			return false;
		}
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList("select a.ry_id from zs_fzysws a,zs_ryjbxx b where a.ry_id=b.id and b.sfzh=?  and a.fzyzt_dm=1",
				fptj.get("sfzh"));
		if(ls.size()==0){
			return false;
		}
		if(this.jdbcTemplate.queryForList("select id from zs_hyhffzyjfb where ry_id=? and nd=? and yxbz=1",new Object[]{ls.get(0).get("ry_id"),fptj.get("nd")}).size()!=0){
			return false;
		}
		String sql="insert into zs_hyhffzyjfb (ID,ND,RY_ID,JE,JFRQ,BZ,LRR,LRRQ,YXBZ) values(?,?,?,?,?,?,?,sysdate(),1)";
		this.jdbcTemplate.update(sql,
				new Object[]{new Common().newUUID(),fptj.get("nd"),ls.get(0).get("ry_id"),
				fptj.get("je"),new Common().getTime2MysqlDateTime((String)fptj.get("jfrq")),
				fptj.get("bz"),name});
		return true;
	}
	/**
	 * 非执业修改
	 * @param jlid
	 * @param fptj
	 * @param name
	 * @return
	 */
	public boolean fzyxg(String jlid,Map<String, Object> fptj,String name){
		if(fptj.containsKey("yje")){
			this.jdbcTemplate.update("update zs_hyhffzyjfb set JE=?,BZ=?,XGR=?,XGRQ=sysdate() where id=?",new Object[]{fptj.get("JE"),fptj.get("BZ"),name,jlid});
		}else{
			this.jdbcTemplate.update("update zs_hyhffzyjfb set JE=?,BZ=?,XGR=?,YJE=?,XGRQ=sysdate() where id=?",new Object[]{fptj.get("JE"),fptj.get("BZ"),name,fptj.get("BZ"),jlid});
			
		}
		return true;
	}
	/**
	 * 非执业删除
	 * @param jlid
	 * @param fptj
	 * @param name
	 * @return
	 */
	public boolean fzyDel(String jlid,String name){
			this.jdbcTemplate.update("update zs_hyhffzyjfb set XGR=?,XGRQ=sysdate(),YXBZ=0 where id=?",new Object[]{name+"(删除)",jlid});
		return true;
	}
	/**
	 * 非执业批量上传
	 * @param file
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> upLoadfzy(MultipartFile file,String uname) throws Exception{  
		InputStream stream = file.getInputStream();
		String fileType =file.getOriginalFilename().split("\\u002E")[1];
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook(stream);  
        }  
        else if (fileType.equals("xlsx")) {  
            wb = new XSSFWorkbook(stream);  
        }  
        else {  
        	throw new Exception("您输入的excel格式不正确");
        }  
        Sheet sheet1 = wb.getSheetAt(0);
        sheet1.removeRow(sheet1.getRow(0));
        Map<String,Object> rs=new HashMap<>();
        List<Object> fls=new ArrayList<Object>();
        String uuid2 = new Common().newUUID();
        int i=0,j=0;
        for (Row row : sheet1) {  
        	ArrayList<Object> params =new ArrayList<Object>();  
            for (Cell cell : row) {  
            	params.add(cell);  
            }
            String nd ="";
            String sfzh=params.get(2)+"";
            try {
            	 nd = (params.get(0)+"").substring(0,4);
			} catch (Exception e) {
				j+=1;
            	fls.add(params.get(1)+""+"   "+sfzh+"（请检查年份格式）");
				continue;
			}
            List<Map<String, Object>> ls = this.jdbcTemplate.queryForList("select a.ry_id from zs_fzysws a,zs_ryjbxx b where a.ry_id=b.id and b.sfzh=?  and a.fzyzt_dm=1",sfzh);
            if(ls.size()==0){
            	j+=1;
            	fls.add(params.get(1)+""+"   "+sfzh+"（请检查身份证号码）");
            	continue;
    		}
            if(this.jdbcTemplate.queryForList("select id from zs_hyhffzyjfb where ry_id=? and nd=? and yxbz=1",new Object[]{ls.get(0).get("ry_id"),nd}).size()!=0){
            	j+=1;
            	fls.add(params.get(1)+""+"   "+sfzh+"（该年度缴费记录已存在，请使用修改操作修改）");
            	continue;
    		}
            String sql="insert into zs_hyhffzyjfb (ID,ND,RY_ID,JE,BZ,LRR,SCJLID,JFRQ,LRRQ,YXBZ) values(?,?,?,?,?,?,?,?,sysdate(),1)";
    		this.jdbcTemplate.update(sql,new Object[]{new Common().newUUID(),nd,ls.get(0).get("ry_id"),params.get(3)+"",params.get(5)+"",uname,uuid2,params.get(4)+""});
    		i+=1;
        }
        this.jdbcTemplate.update("insert into zs_hyhfscjlb (ID,SUCESS,FAIL,SCRQ,SCR,YXBZ) values(?,?,?,sysdate(),?,3)",new Object[]{uuid2,i,j,uname});
        rs.put("success", i);
        rs.put("fail", j);
        rs.put("fls", fls);
        return rs;
    }
	/**
	 * 发票打印累加
	 * @param jlid
	 * @param name
	 * @return
	 */
	public boolean fpdylj(Object qury){
		this.jdbcTemplate.update("update zs_hyhfjnqk set DYCS=? where id=?",
				new Object[]{this.jdbcTemplate.queryForObject("select DYCS from zs_hyhfjnqk where id=?",new Object[]{qury}, int.class)+1,
				qury});
	return true;
	}
	/**
	 * 上传管理查询
	 * @param pn
	 * @param ps
	 * @param qury
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> scglcx(int pn,int ps,Map<String, Object> qury) throws Exception{
		Condition condition = new Condition();
		condition.add("a.SCRQ", Condition.EQUAL, qury.get("nd"));
		ArrayList<Object> params = condition.getParams();
		params.add(0,(pn-1)*ps);
		params.add((pn-1)*ps);
		params.add(ps);
		StringBuffer sb = new StringBuffer();
		sb.append("		select SQL_CALC_FOUND_ROWS @rownum:=@rownum+1 as 'key',a.*,date_format(a.SCRQ,'%Y-%m-%d') as scrq,"); 
		sb.append("		case a.YXBZ when 0 then '已撤销' when 1 then '有效' when 2 then '已撤销'"); 
		sb.append("		when 3 then '有效' else null end as jlzt from zs_hyhfscjlb a,(select @rownum:=?) zs_ry "+condition.getSql()+" order by a.SCRQ desc LIMIT ?, ?"); 
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
	 * 上传记录管理操作
	 * @param jlid
	 * @param name
	 * @return
	 */
	public boolean scjlglcz(Map<String,Object> qury){
		int lx = (int)qury.get("lx");
		if(lx==0||lx==1){
			this.jdbcTemplate.update("update zs_hyhfjnqk a,zs_hyhfscjlb b"
					+ " set b.YXBZ=?,a.YXBZ=?,b.CZRQ=sysdate() where b.id=?"
					+ " and a.SCJLID=b.id",
					new Object[]{lx,qury.get("bj"),qury.get("jlid")});
			if(this.jdbcTemplate.queryForList("select id from zs_hyhfjfryls where SCJLID=?",qury.get("jlid")).size()!=0){
				this.jdbcTemplate.update("update zs_hyhfjfryls d set d.YXBZ=? where d.SCJLID=?",
						new Object[]{qury.get("bj"),qury.get("jlid")});
			}
		}else{
			this.jdbcTemplate.update("update zs_hyhfscjlb b,zs_hyhffzyjfb c"
					+ " set b.YXBZ=?,c.YXBZ=?,b.CZRQ=sysdate() where b.id=?"
					+ " and c.SCJLID=b.id ",
					new Object[]{lx,qury.get("bj"),qury.get("jlid")});
		}
	return true;
	}
}
