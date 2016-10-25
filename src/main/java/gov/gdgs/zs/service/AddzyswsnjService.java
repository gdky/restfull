package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import  gov.gdgs.zs.dao.AddzyswsnjDao;
import gov.gdgs.zs.dao.IAddzyswsnjDao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class AddzyswsnjService implements IAddzyswsnjService {
	//执业税务师年检表
	@Resource
	
	private AddzyswsnjDao addzyswsnjdao;
	@Resource
	private IAddzyswsnjDao iaddzyswsnjdao;
	@Override
	public Map<String, Object> addZyswsnjb (Map<String, Object> obj) throws Exception {
		
		
		
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		String rs =iaddzyswsnjdao.addZyswsnjb(obj);
		map.put("id", rs);
		return map;

	}
	@Override
	public void updateZyswsnjb(Map<String, Object> obj) throws Exception {
		iaddzyswsnjdao.updateZyswsnjb(obj);
	}
	
			public Map<String, Object> getzyswsnjb(int page, int pageSize,int Jgid, String where) {
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
				Map<String, Object> rs =addzyswsnjdao.getZyswsnjb(page, pageSize,Jgid, map);
				return rs;
			}
			
			public Map<String, Object> getzyswsnjbBySwsId(String sws_id) {
				Map<String,Object> obj = addzyswsnjdao.getzyswsnjbBySwsId(sws_id);
				return obj;
			}
			
			public Map<String, Object> getzyswsnjbById(String id) {
				Map<String,Object> obj = addzyswsnjdao.getzyswsnjbById(id);
				return obj;
			}
			
			
			public Map<String, Object> getzyswsxm(int page, int pageSize,int Jgid, String where) {
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
				Map<String, Object> rs =addzyswsnjdao.getZyswsxm(page, pageSize,Jgid, map);
				return rs;
			}
			
			public Map<String, Object> getzyswsnjBafs(String nd, String sws_id) {
				Map<String, Object> rs =addzyswsnjdao.getzyswsnjBafs(nd, sws_id);
				return rs;
			}
			
			
}


