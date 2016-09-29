package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.Map;




import gov.gdgs.zs.dao.YhdljlbDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/*
功能：用于显示中心端用户登录信息


*/
@Service
public class YhdljlbService {
	@Resource 
	private YhdljlbDao yhdljlDao;
	
	public Map<String, Object> getYhdljlb(int pn, int ps, String where){
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
		  Map<String, Object> rs=yhdljlDao.yhdljlb(pn,ps,map);
		  return rs;
	}
	
}
