package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.SDSCBBDao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class SDSCBBService {
	@Resource
	private SDSCBBDao sdscbbDao;
	public Map<String, Object> swsjbqktjbcx(int pn, int ps, String where) {
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
		return sdscbbDao.swsjbqktjbcx(pn, ps, map);
	}
	public Map<String, Object> hyryqktjcx(int pn, int ps, String where) {
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
		return sdscbbDao.hyryqktjcx(pn, ps, map);
	}
	public Map<String, Object> jysrqktjcx(int pn, int ps, String where) {
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
		return sdscbbDao.jysrqktjcx(pn, ps, map);
	}
	public Map<String, Object> wsbbbcx(int pn, int ps, String where) {
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
		return sdscbbDao.wsbbbcx(pn, ps, map);
	}
	public void rjb1(String id) {
		sdscbbDao.rjb1(id);
		
	}
	public void rjb2(String id) {
		sdscbbDao.rjb2(id);
		
	}
	public void rjb4(String id) {
		sdscbbDao.rjb4(id);
		sdscbbDao.rjb5(id);
		
	}
	public void rjb6(String id) {
		sdscbbDao.rjb6(id);
		
	}
}
