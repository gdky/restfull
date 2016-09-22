package gov.gdgs.zs.service;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.dao.SWSDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hashids.Hashids;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.exception.UserException;

@Service
public class SwsService {
	@Resource
	private SWSDao swsDao;
	
	public Map<String, Object> swscx(int pn, int ps, String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return swsDao.swscx(pn, ps, map);
	}
	public Map<String, Object> swsslspcx(int pn, int ps, String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return swsDao.swsslspcx(pn, ps, map);
	}
	
	public Map<String, Object> swsxx(String xqTab,String gid) {
		Map<String, Object> sb = new HashMap<>();
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		int jgid = (int)hashids.decode(gid)[0];
		switch (xqTab) {
		case "swsxx":
			sb.put("data", swsDao.swsxx(jgid));
			break;
		case "zyryxx":
			sb.put("data", swsDao.zyryxx(jgid));
			break;
		case "cyryxx":
			sb.put("data", swsDao.cyryxx(jgid));
			break;
		case "czrylb":
			sb.put("data", swsDao.czrylb(jgid));
			break;
		case "swsbgxx":
			sb.put("data", swsDao.swsbgxx(jgid));
			break;
		case "njjl":
			sb.put("data", swsDao.njjl(jgid));
			break;
		}
		return sb;
	}
	
	public Map<String, Object> swsxxcx(String jgid) {
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		return this.swsDao.swsxx((int)hashids.decode(jgid)[0]);
	}
	public Object insertjg(Map<String, Object> jgtj){
		Object obj = swsDao.insertjg(jgtj);
		if(obj==null){
			throw new UserException("单位名称重复");
		}
		return obj;
	}
	public Object chilchenJG(Object pid){
		return swsDao.chilchenJG(pid);
	}
	public Map<String, Object> getSzxx(Map<String,Object> swsxx) {
		Map<String,Object> obj = new HashMap<String,Object>();
		Map<String,Object> data = (Map<String,Object>)swsxx.get("data");
		obj.put("xming", (String)data.get("fddbr"));
		obj.put("szphone", (String)data.get("szphone"));
		obj.put("szyx", (String)data.get("szyx"));
		return obj;
	}
	
	public List<Map<String, Object>> getFqrxx(Long jgId) {
		return swsDao.getFqrxx(jgId);
	}
	
	public List<Map<String, Object>> getCzrxx(Long jgId) {
		return swsDao.getCzrxx(jgId);
	}
	
	public Map<String, Object> getSumZysws(Long jgId, int page, int pagesize) {
		return swsDao.getSumZysws(jgId,page,pagesize);
	}
	
	public Map<String, Object> getSumCyry(Long jgId, int page, int pagesize) {
		return swsDao.getSumCyry(jgId,page,pagesize);
	}
}
