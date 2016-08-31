package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.dao.ZshyNbbDao;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ZshyNbbService {
	
	@Resource
	private ZshyNbbDao zshyNbbDao;
	
	/**
	 * 事务所基本情况统计表1
	 * @param page
	 * @param pageSize
	 * @param where
	 * @return
	 */
	public Map<String, Object> getSwsjbqktj(int page, int pageSize,
			String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(where != null){
			try{
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return zshyNbbDao.getSwsjbqktj(page, pageSize, map);
	}

	/**
	 * 行业人员情况统计表2
	 * @param where
	 * @return
	 */
	public Map<String, Object> getHyryqktjb(String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(where != null){
			try{
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return zshyNbbDao.getHyryqktjb(map);
	}
}
