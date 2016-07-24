package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.HyryqkTjDao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HyryqkTjService {

	@Resource
	private HyryqkTjDao hyryqkTjDao;
	
	public Map<String, Object> getHyryqkTjMx(String type, String lx,
			String where) {
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
		Map<String, Object> rs=hyryqkTjDao.getHyryqkTjMx(type,lx,map);
		return rs;
	}

}
