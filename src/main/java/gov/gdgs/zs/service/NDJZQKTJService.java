/*
 * 年度鉴证情况统计
 */
package gov.gdgs.zs.service;
import java.util.HashMap;
import java.util.Map;

import gov.gdgs.zs.dao.NDJZQKTJDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 系统业务报表年度鉴证情况统计表
 * @author lenovo
 *
 */
@Service
public class NDJZQKTJService {
@Resource 
private NDJZQKTJDao ndjzqkTjDao;


  
public Map<String, Object> getNdjzqktjb(int page, int pageSize, String where) {
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
	return ndjzqkTjDao.ndjzqktj(page, pageSize, map);
}



}


