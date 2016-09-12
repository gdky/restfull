package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.ZzsdDao;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.entity.User;

@Service
@Transactional
public class ZzsdService {

	@Resource
	private ZzsdDao zzsdDao;


	public Map<String, Object> getJgZzsd(int page, int pagesize, String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("sdyy", "FUZZY", where.get("sdyy"));
		condition.add("sdr_role", "EQUAL", where.get("sdr_role"));
		condition.add("jsr_role", "EQUAL", where.get("jsr_role"));
		condition.add("sdtime", "DATE_BETWEEN", where.get("sdtime"));
		condition.add("jstime", "DATE_BETWEEN", where.get("jstime"));
		
		Map<String, Object> rs = zzsdDao.getJgZzsd(page, pagesize, condition);
		return rs;
	}


	public void addJgZzsd(User user, String sdyy, Integer[] jgId) {
		List<Object[]> batchArgs  = new ArrayList<Object[]>();
		
		for (int i = 0 ; i < jgId.length; i++){
			
		}
		zzsdDao.addJgZzsd(user.getId(),)
		
	}
}
