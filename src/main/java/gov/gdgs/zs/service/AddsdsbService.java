package gov.gdgs.zs.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gov.gdgs.zs.dao.AddsdsbDao;
import gov.gdgs.zs.dao.ClientsdsbDao;
import gov.gdgs.zs.dao.IAddsdsbDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.exception.BbtbException;
import com.gdky.restfull.utils.Common;

@Service
public class AddsdsbService implements IAddsdsbService {

	@Resource
	private ClientsdsbDao clientSdsbDao;
	@Resource
	private IAddsdsbDao iaddsdsbDao;

	@Resource
	private AddsdsbDao addsdsbDao;

	@Override
	public Map<String, Object> AddSwsjbqkb(Map<String, Object> obj) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 判断是否已存在同年的基本情况表
		if (clientSdsbDao.isExists(obj.get("nd"), obj.get("jg_id"),
				"zs_sdsb_swsjbqk")) {
			throw new BbtbException("该年度报表已存在，请勿重复添加");
		}
		// 将提交上来的城市信息拆分成代码及城市名
		Map<String, Object> cs = (Map<String, Object>) obj.get("cs_dm");
		obj.put("cs_dm", cs.get("key"));
		obj.put("jgszd", cs.get("label"));
		String rs = addsdsbDao.AddSwsjbqkb(obj);
		map.put("id", rs);
		return map;

	}

	@Override
	public void UpdateSwsjbqkb(Map<String, Object> obj) {
		// 将提交上来的城市信息拆分成代码及城市名
		Map<String, Object> cs = (Map<String, Object>) obj.get("cs_dm");
		obj.put("cs_dm", cs.get("key"));
		obj.put("jgszd", cs.get("label"));
		addsdsbDao.UpdateSwsjbqkb(obj);
	}

	public Map<String, Object> getSwsjbqkb(int page, int pageSize, int Jgid,
			int Id, String whereParam) {
		Map<String, Object> where = Common.decodeURItoMap(whereParam);
		Map<String, Object> rs = addsdsbDao.getSwsjbqkb(page, pageSize, Jgid,
				Id, where);
		return rs;
	}

	public Map<String, Object> getSwsjbqkbById(String id) {
		Map<String, Object> obj = addsdsbDao.getSwsjbqkbById(id);
		return obj;
	}

	/*
	 * 新建基本情况表取初始数据
	 */
	public Map<String, Object> getSwsjbqkInit(User user) {
		// 获取去年年度
		Calendar cal = Calendar.getInstance();
		int last_y = cal.get(Calendar.YEAR) - 1;
		int now_y = cal.get(Calendar.YEAR);
		// 获取事务所名称
		// 获取事务所执业税务师人数
		// 获取事务所性质
		List<Map<String, Object>> ls = clientSdsbDao.getSwsTj(user.getJgId(),
				last_y);
		if (ls.size() == 0) {
			ls = clientSdsbDao.getSwsTj(user.getJgId(), now_y);
			if (ls.size() == 0) {
				throw new BbtbException("无法获得基本数据");
			}
		}
		Map<String, Object> swstj = ls.get(0);
		// 获取去年全年营业收入
		BigDecimal srze = clientSdsbDao.getSrze(last_y, user.getJgId());
		if (srze == null) {
			throw new BbtbException("没法获取" + last_y + "年主营收入总额，请先提交该年度利润表");
		}
		BigDecimal b1 = new BigDecimal(10000);
		srze = srze.divide(b1, 2, BigDecimal.ROUND_HALF_UP);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("nd", last_y);
		obj.put("dwmc", swstj.get("dwmc"));
		obj.put("jgxz_dm", String.valueOf(swstj.get("jgxz_dm")));
		obj.put("zyzcswsrs", swstj.get("zysws_sfnum"));
		obj.put("srze", srze);
		return obj;
	}
	
	/*
	 * 修改基本情况表取初始数据
	 */
	public Map<String, Object> getSwsjbqkInit(User user, String id) {
		//获取本记录的年度和执业人数
		Map<String,Object> rs = clientSdsbDao.getJbqkNd(id);
		//获取税务师数量
		List<Map<String, Object>> ls = clientSdsbDao.getSwsTj(user.getJgId(),
				(Integer)rs.get("nd"));
		Map<String, Object> swstj = new HashMap<String,Object>();
		if (ls.size() == 0) {
			swstj.put("zysws_sfnum",rs.get("zyzcswsrs"));
		}else {
			swstj = ls.get(0);
		}
		
		// 获取去年全年营业收入
		BigDecimal srze = clientSdsbDao.getSrze((Integer)rs.get("nd"), user.getJgId());
		if (srze == null) {
			throw new BbtbException("没法获取" + rs.get("nd").toString() + "年主营收入总额，请先提交该年度利润表");
		}
		BigDecimal b1 = new BigDecimal(10000);
		srze = srze.divide(b1, 2, BigDecimal.ROUND_HALF_UP);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("zyzcswsrs", swstj.get("zysws_sfnum"));
		obj.put("srze", srze);
		return obj;
	}
	
	

	public Map<String, Object> AddJygmtjb(Map<String, Object> obj) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String rs = iaddsdsbDao.AddJygmtjb(obj);
		map.put("id", rs);
		return map;

	}

	@Override
	public void UpdateJygmtjb(Map<String, Object> obj) {
		iaddsdsbDao.UpdateJygmtjb(obj);
	}

	// 经营规模统计表
	public Map<String, Object> getJygmtjb(int page, int pageSize, int Id,
			int Jgid, String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		Map<String, Object> rs = addsdsbDao.getJygmtjb(page, pageSize, Id,
				Jgid, map);
		return rs;
	}

	public Map<String, Object> getJygmtjbById(String id) {
		Map<String, Object> obj = addsdsbDao.getJygmtjbById(id);
		return obj;
	}

	public Map<String, Object> getOK1(String jgid) {
		Map<String, Object> obj = addsdsbDao.getOk(jgid);
		return obj;
	}

	// 鉴证业务情况统计表

	@Override
	public Map<String, Object> AddJzywqktjb(Map<String, Object> obj) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String rs = iaddsdsbDao.AddJzywqktjb(obj);
		map.put("id", rs);
		return map;

	}

	@Override
	public void UpdateJzywqktjb(Map<String, Object> obj) {
		iaddsdsbDao.UpdateJzywqktjb(obj);
	}

	public Map<String, Object> getJzywqktjb(int page, int pageSize, int Id,
			int Jgid, String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		Map<String, Object> rs = addsdsbDao.getJzywqktjb(page, pageSize, Id,
				Jgid, map);
		return rs;
	}

	public Map<String, Object> getJzywqktjbById(String id) {
		Map<String, Object> obj = addsdsbDao.getJzywqktjbById(id);
		return obj;
	}

	public Map<String, Object> getUpyear(String jgid) {
		Map<String, Object> obj = addsdsbDao.getUpyear(jgid);
		return obj;
	}

	
	
	public Map<String, Object> getEditJygmtjInit(String jgid, String where) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		Map<String, Object> rs=clientSdsbDao.getEditJygmtjInit(jgid,map);
		return rs;
	}

}
