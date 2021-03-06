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

import  gov.gdgs.zs.dao.AddswsnjDao;
import gov.gdgs.zs.dao.IAddswsnjDao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class AddswsnjService implements IAddswsnjService{
	//事务所年检表
	@Resource
	private AddswsnjDao addswsnjdao;
	@Resource
	private IAddswsnjDao iaddswsnjdao;
	@Override
	public Map<String, Object> addSwsnjb (Map<String, Object> obj) throws Exception {
		
		
		
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		String rs =iaddswsnjdao.addSwsnjb(obj);
		map.put("id", rs);
		return map;

	}
	@Override
	public void updateSwsnjb(Map<String, Object> obj) throws Exception {
		iaddswsnjdao.updateSwsnjb(obj);
        
	}
	

			public Map<String, Object> getswsnjb(int page, int pageSize,int Jgid, String where) {
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
				Map<String, Object> rs =addswsnjdao.getswsnjb(page, pageSize,Jgid, map);
				return rs;
			}
			
			public Map<String, Object> getswsbafs(int Jgid, String where) {
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
				Map<String, Object> rs =addswsnjdao.getswsbafs(Jgid, map);
				return rs;
			}
			
			public Map<String, Object> getswsnjnd(int Jgid, String where) {
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
				Map<String, Object> rs =addswsnjdao.getswsnjnd(Jgid, map);
				return rs;
			}
			
			
			
			
			public Map<String, Object> getSwsnjById(String id) {
				Map<String,Object> obj = addswsnjdao.getswsnjbById(id);
				return obj;
			}
			
			

			
			
			
}
