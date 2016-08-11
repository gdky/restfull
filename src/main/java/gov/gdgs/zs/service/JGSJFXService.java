/*
 * 机构数据分析
 */
package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.Map;

import gov.gdgs.zs.dao.JGSJFXDao;




import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 机构数据分析/行业学历数据分析
 * @author lenovo
 *
 */
@Service
public class JGSJFXService {
@Resource 
private JGSJFXDao jgsjfxDao;
public Map<String, Object> getHyxlsjfxb(int page, int pageSize, String where) {
	HashMap<String, Object> map = new HashMap<String, Object>();
	if(where != null){
		try{
			where = java.net.URLDecoder.decode(where, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(where,
					new TypeReference<Map<String, Object>>() {
					});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	return jgsjfxDao.getHyxlsjfxb(page, pageSize, map);
}
public Map<String, Object> getZjgmsjfxb(int page, int pageSize, String where) {
	HashMap<String, Object> map = new HashMap<String, Object>();
	if(where != null){
		try{
			where = java.net.URLDecoder.decode(where, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(where,
					new TypeReference<Map<String, Object>>() {
					});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	return jgsjfxDao.getZjgmsjfxb(page, pageSize, map);
}
     /* public Map<String, Object> getZjgmsjfxb(int nd){
    	  Map<String, Object> rs=jgsjfxDao.getZjgmsjfxb(nd);
    	  return rs;
      }*/


}


