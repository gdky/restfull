package gov.gdgs.zs.dao;

import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;
@Repository
public class CheckingDao extends BaseJdbcDao{
	/**
	 * 判断事务所设立审批中
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkJGSPing(int jgid){
		if(this.jdbcTemplate.queryForList("select id from zs_jg where tgzt_dm = 5 and id =?",new Object[]{jgid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断事务所是否可以设立分所
	 * @param jgid
	 * @return false--不满足设立条件
	 */
	public boolean checkJGFssl(int jgid){
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				"	select if((select count(b.id) from zs_zysws b where b.JG_ID=a.id and b.yxbz=1)-"
				+ "(select count(id)*3+10 from zs_jg c where c.PARENTJGID=a.id and c.JGZT_DM not in (9,10))<0,"
				+ "'false','true') as rypd from zs_jg a where a.TGZT_DM<>5 and a.YXBZ=1 and a.id =?",new Object[]{jgid});
		String rypd=ls.get(0).get("rypd")+"";
		if(ls.size()!=0&&rypd.equals("true")){
			return true;
		}
		return false;
	}
	/**
	 * 判断事务所变更审批中
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkBGing(int jgid){
		if(this.jdbcTemplate.queryForList("select * from zs_jgbgspb where spzt_dm = 1 and jg_id =?",new Object[]{jgid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断事务所注销审批中
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkZXing(int jgid){
		if(this.jdbcTemplate.queryForList("select id from zs_jgzx where spzt = 1 and jg_id =?",new Object[]{jgid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断事务所合并审批中
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkHBing(int jgid){
		if(this.jdbcTemplate.queryForList("select id from zs_jghb where HBZT = 1 and jg_id =?",new Object[]{jgid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断事务所能否进行审批事项申请
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkSWSSPing(int jgid){
		StringBuffer sb = new StringBuffer();
		sb.append("		select 1 from zs_jg a where a.JGZT_DM='5' and a.PARENTJGID=?");
		sb.append("			union ");
		sb.append("			select 1 from zs_jgbgspb b where b.SPZT_DM='1' and b.JG_ID=?");
		sb.append("			union");
		sb.append("			select 1 from zs_jgzx c where c.SPZT='1' and c.JG_ID=?");
		sb.append("			union ");
		sb.append("			select 1 from zs_jghb_jgxx d where d.HBZT='1' and d.JG_ID=?");
		if(this.jdbcTemplate.queryForList(sb.toString(),new Object[]{jgid,jgid,jgid,jgid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断事务所能否进行审批事项申请
	 * @param jgmc
	 * @return false--审批中
	 */
	public boolean checkSWSSPing(String jgmc){
		StringBuffer sb = new StringBuffer();
		sb.append("		select 1 from zs_jg a,zs_jg e where a.JGZT_DM='5' and a.PARENTJGID=e.id and e.dwmc=? ");
		sb.append("			union ");
		sb.append("			select 1 from zs_jgbgspb b,zs_jg e where b.SPZT_DM='1' and b.JG_ID=e.id  and e.dwmc=?");
		sb.append("			union");
		sb.append("			select 1 from zs_jgzx c,zs_jg e where c.SPZT='1' and c.JG_ID=e.id and e.dwmc=?");
		sb.append("			union ");
		sb.append("			select 1 from zs_jghb_jgxx d,zs_jg e where d.HBZT='1' and d.JG_ID=e.id and e.dwmc=?");
		if(this.jdbcTemplate.queryForList(sb.toString(),new Object[]{jgmc,jgmc,jgmc,jgmc}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断是否存在税务师审批中事项
	 * @param jgmc
	 * @return false--存在
	 */
	public boolean checkZYSWSSPing(String jgmc){
		StringBuffer sb = new StringBuffer();
		sb.append("		select 1 from zs_zysws a,zs_jg b where a.RYSPGCZT_DM in(2,4,6,9,12,11,7,8) and b.ID=a.JG_ID and b.DWMC=?");
		sb.append("				union ");
		sb.append("				select 2 from zs_fzyzzy c,zs_jg d where c.XORGID=d.ID and c.RYSPZT='0' and d.DWMC=?");
		if(this.jdbcTemplate.queryForList(sb.toString(),new Object[]{jgmc,jgmc}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断事务所设立审批中
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkSLing(int jgid){
		if(this.jdbcTemplate.queryForList("select id from zs_jg where JGZT_DM in(5,8,12) and jg_id =?",new Object[]{jgid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断执业税务师审批中
	 * @param jgid
	 * @return false--审批中
	 */
	public boolean checkZYSPing(int zyid){
		if(this.jdbcTemplate.queryForList("select id from zs_zysws where zyzt_dm=1 and ryspgczt_dm not in (1,3) and id=?",zyid).size()!=0){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断审批是否为上级驳回
	 * @param sfzh
	 * @return false--上级驳回
	 */
	public boolean checkIsBH(String spid){
		if(this.jdbcTemplate.queryForList("select id from zs_spzx where id = ? and qrbj is not null",new Object[]{spid}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断是否存在重复身份证号
	 * @param sfzh
	 * @return false--身份证号重复
	 */
	public boolean checkHadSFZH(String sfzh){
		if(this.jdbcTemplate.queryForList("select id from zs_ryjbxx where SFZH = ? and ryzt_dm not in(2,5,6)",new Object[]{sfzh}).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断是否可创建支出明细表
	 * @param sfzh
	 * @return false--存在同时期表单
	 */
	public boolean checkIfZCMXB(Integer jgid,Map<String,Object> map){
		Condition condition = new Condition();
		condition.add("JG_ID", Condition.EQUAL, jgid);
		condition.add("ND", Condition.EQUAL, map.get("nd"));
		condition.add("id", Condition.NOT_EQUAL, map.get("id"));
		if(map.get("timevalue").equals("0")){
			condition.add("jssj", Condition.EQUAL, map.get("nd")+"-06-30");
		}else if(map.get("timevalue").equals("1")){
			condition.add("jssj", Condition.EQUAL, map.get("nd")+"-12-31");
		};
		ArrayList<Object> params = condition.getParams();
		if(this.jdbcTemplate.queryForList("select id from zs_cwbb_zcmx "+condition.getSql().toString(),params.toArray()).size()!=0){
			return false;
		}
		return true;
	}
	/**
	 * 判断是否存在正常事务所
	 * @param jgmc
	 * @param jgzsbh
	 * @return false--存在
	 */
	public boolean haveJG(String jgmc,String jgzsbh){
		if(this.jdbcTemplate.queryForList("select 1 from zs_jg where JGZT_DM = '11' and yxbz='1' and dwmc=? and JGZCH=?",new Object[]{jgmc,jgzsbh}).size()!=0){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否已存在机构名称
	 * @param xjgmc
	 * @return false--已存在
	 */
	public boolean haveJGDWMC(String xjgmc){
		if(this.jdbcTemplate.queryForList("select 1 from zs_jg where JGZT_DM = '11' and yxbz='1' and dwmc=? ",new Object[]{xjgmc}).size()!=0){
			return false;
		}
		return true;
	}
	
}
