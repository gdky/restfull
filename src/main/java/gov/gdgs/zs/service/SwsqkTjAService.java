package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.SwsqkTjADao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SwsqkTjAService {

	@Resource
	private SwsqkTjADao swsqkTjADao;
	
	public Map<String, Object> getSwsqkTjAs(int page, int pageSize, String where) {
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
		Map<String, Object> rs=swsqkTjADao.getSwsqkTjAs(page,pageSize,map);
		return rs;
	}

}
