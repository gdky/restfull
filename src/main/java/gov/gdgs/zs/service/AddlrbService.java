package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.ILrbDao;
import gov.gdgs.zs.dao.LrbDao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;




@Service
public class AddlrbService implements IAddlrbService{
	
	@Resource
	private ILrbDao lrbDao;
	@Resource
	private LrbDao lrDao;
	@Override
	public Map<String, Object> addLrb (Map<String, Object> obj) {
		
		
		
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		
		String rs =  lrbDao.addLrb(obj);
		map.put("id", rs);
		return map;

	}
	 //利润表
		public Map<String, Object> getlrb(int page, int pageSize, String where) {
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
			Map<String, Object> rs = lrDao.getlrb(page, pageSize, map);
			return rs;
		}
	

}
