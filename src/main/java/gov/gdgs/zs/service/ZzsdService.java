package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.ZzsdDao;
import gov.gdgs.zs.entity.SdjlJG;
import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.dao.AuthDao;
import com.gdky.restfull.entity.Role;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.HashIdUtil;

@Service
@Transactional
public class ZzsdService {

	@Resource
	private ZzsdDao zzsdDao;

	@Autowired
	private AuthDao authDao;

	public Map<String, Object> getJgZzsd(int page, int pagesize,
			String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("jl.sdyy", "FUZZY", where.get("sdyy"));
		condition.add("jl.sdr_role", "FUZZY", where.get("sdr_role"));
		condition.add("jl.jsr_role", "FUZZY", where.get("jsr_role"));
		condition.add("jl.sdr", "FUZZY", where.get("sdr"));
		condition.add("jl.jsr", "FUZZY", where.get("jsr"));
		condition.add("jl.sdtime", "DATE_BETWEEN", where.get("sdtime"));
		condition.add("jl.jstime", "DATE_BETWEEN", where.get("jstime"));
		condition.add("j.dwmc", "FUZZY", where.get("swsmc"));

		Map<String, Object> rs = zzsdDao.getJgZzsd(page, pagesize, condition);
		return rs;
	}

	/**
	 * 添加一组机构锁定记录
	 * @param user 锁定人
	 * @param sdyy 锁定原因
	 * @param jgId 一组机构id
	 */
	public void addJgZzsd(User user, String sdyy, List<String> jgId,Integer lx) {
		String sdtime = Common.getCurrentTime2MysqlDateTime();
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		String roleName = role.get(0).getDescription();

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		List<Integer> usedJgId = getSdJGByLx(lx);

		for (int i = 0; i < jgId.size(); i++) {
			Long jid =  HashIdUtil.decode(jgId.get(i));
			if(!usedJgId.contains(jid.intValue())){
				Object[] arg = new Object[] { jid, sdyy, user.getUsername(),
						roleName, sdtime,lx, 1 };
				batchArgs.add(arg);
			}
		}
		if(batchArgs.size()>0){
			zzsdDao.addJgZzsd(batchArgs);
		}
	}

	/**
	 * 添加一条机构锁定记录
	 * @param user 锁定人
	 * @param sdyy 锁定原因
	 * @param jgId 机构id
	 */
	public void addJgZzsd(User user, String sdyy, Integer jgId, Integer lx) {
		String sdtime = Common.getCurrentTime2MysqlDateTime();
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		String roleName = role.get(0).getDescription();
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		List<Integer> usedJgId = getSdJGByLx(lx);
		if(!usedJgId.contains(jgId)){
			Object[] arg = new Object[] { jgId, sdyy, user.getUsername(),
					roleName, sdtime, lx, 1 };
			batchArgs.add(arg);
		}
		if(batchArgs.size()>0){
			zzsdDao.addJgZzsd(batchArgs);
		}
	}
	
	public List<Integer> getSdJGByLx(Integer lx){
		List<Integer> ls = zzsdDao.getSdJGByLx(lx);
		return ls;
	}
	
	public List<Integer> getSdsws(){
		List<Integer> ls = zzsdDao.getSdsws();
		return ls;
	}

	public void unlockJgZzsd(User user, List<Integer> id) {
		String jstime = Common.getCurrentTime2MysqlDateTime();
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		String roleName = role.get(0).getDescription();
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Integer sdId : id){
			Object[] arg = new Object[] { user.getUsername(),roleName, jstime, 0, sdId };
			batchArgs.add(arg);
		}
		if(batchArgs.size()>0){
			zzsdDao.unlockJgZzsd(batchArgs);
		}
	}
	public void lockJgZzsd(User user, List<Integer> id) {
		String jstime = Common.getCurrentTime2MysqlDateTime();
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		String roleName = role.get(0).getDescription();
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Integer sdId : id){
			Object[] arg = new Object[] { 1, sdId };
			batchArgs.add(arg);
		}
		if(batchArgs.size()>0){
			zzsdDao.lockJgZzsd(batchArgs);
		}
	}

	public Map<String, Object> getJgZzsdwx(int page, int pagesize, String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("jl.sdyy", "FUZZY", where.get("sdyy"));
		condition.add("jl.sdr_role", "FUZZY", where.get("sdr_role"));
		condition.add("jl.jsr_role", "FUZZY", where.get("jsr_role"));
		condition.add("jl.sdr", "FUZZY", where.get("sdr"));
		condition.add("jl.jsr", "FUZZY", where.get("jsr"));
		condition.add("jl.sdtime", "DATE_BETWEEN", where.get("sdtime"));
		condition.add("jl.jstime", "DATE_BETWEEN", where.get("jstime"));
		condition.add("j.dwmc", "FUZZY", where.get("swsmc"));

		Map<String, Object> rs = zzsdDao.getJgZzsdwx(page, pagesize, condition);
		return rs;
	}

	public Map<String, Object> getSWSzzzt(int page, int pagesize, String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("j.dwmc", "FUZZY", where.get("swsmc"));
		condition.add("r.xming", "FUZZY", where.get("xming"));

		Map<String, Object> rs = zzsdDao.getSWSzzzt(page, pagesize, condition);
		return rs;
	}

	public void addSwszzsd(User user, String sdyy, List<Integer> swsId) {
		String sdtime = Common.getCurrentTime2MysqlDateTime();
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		String roleName = role.get(0).getDescription();

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		List<Integer> usedSwsId = getSdsws();

		for (int i = 0; i < swsId.size(); i++) {
			if(!usedSwsId.contains(swsId.get(i).intValue())){
				Object[] arg = new Object[] {swsId.get(i) , sdyy, user.getUsername(),
						roleName, sdtime, 1 };
				batchArgs.add(arg);
			}
		}
		if(batchArgs.size()>0){
			zzsdDao.addSwszzsd(batchArgs);
		}
		
	}

	public Map<String, Object> getSWSsdjl(int page, int pagesize, String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("s.sdyy", "FUZZY", where.get("sdyy"));
		condition.add("s.sdr_role", "FUZZY", where.get("sdr_role"));
		condition.add("s.jsr_role", "FUZZY", where.get("jsr_role"));
		condition.add("s.sdr", "FUZZY", where.get("sdr"));
		condition.add("s.jsr", "FUZZY", where.get("jsr"));
		condition.add("s.sdtime", "DATE_BETWEEN", where.get("sdtime"));
		condition.add("s.jstime", "DATE_BETWEEN", where.get("jstime"));
		condition.add("r.xming", "FUZZY", where.get("xming"));

		Map<String, Object> rs = zzsdDao.getSWSsdjl(page, pagesize, condition);
		return rs;
	}

	public void unlockSWSZzsd(User user, List<Integer> id) {
		String jstime = Common.getCurrentTime2MysqlDateTime();
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		String roleName = role.get(0).getDescription();
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Integer sdId : id){
			Object[] arg = new Object[] { user.getUsername(),roleName, jstime, 0, sdId };
			batchArgs.add(arg);
		}
		if(batchArgs.size()>0){
			zzsdDao.unlockSWSZzsd(batchArgs);
		}
		
	}

}
