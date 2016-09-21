package gov.gdgs.zs.service;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.dao.SWSDao;

import java.util.HashMap;
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
	public Map<String, Object> getSzxx(Long jid) {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String, Object> getFqrxx(Long jid) {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String, Object> getCzrxx(Long jid) {
		// TODO Auto-generated method stub
		return null;
	}
}
