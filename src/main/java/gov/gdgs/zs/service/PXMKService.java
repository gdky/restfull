package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.PXMKDao;
import gov.gdgs.zs.untils.Condition;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.Common;

@Service
@Transactional(rollbackFor=Exception.class)
public class PXMKService {
	@Resource
	private PXMKDao pxmkDao;
	
	public Map<String, Object> getpxfbList(int pn, int ps, String where) throws Exception {
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
		return pxmkDao.getpxfbList(pn, ps, map);
		
	}
	public void fspsq(Map<String,Object> ptxm,String splx)throws Exception {
		 switch (splx) {
			case "pxxxfb"://培训信息发布
				this.pxmkDao.pxxxfb(ptxm);break;
		 }
	}
	public Map<String,Object> getPxxx(User user, int page, int pagesize, String whereparam) {
		Integer jgId = user.getJgId();
		Condition condition = new Condition();
		if(!StringUtils.isEmpty(whereparam)){
			Map<String,Object> where = Common.decodeURItoMap(whereparam);
		}
		condition.add(" AND yxbz = 1 ");		
		Map<String, Object> obj = pxmkDao.getPxxxForUser(user,page, pagesize,condition);
		return obj;
	}
	public Map<String,Object> getPxxx(int page, int pagesize, String whereparam) {
		Condition condition = new Condition();
		if(!StringUtils.isEmpty(whereparam)){
			Map<String,Object> where = Common.decodeURItoMap(whereparam);
		}
		condition.add(" AND yxbz = 1 ");		
		Map<String, Object> obj = pxmkDao.getPxxx(page, pagesize,condition);
		return obj;
	}
	public Map<String,Object> getPxnr(String id) {
		return pxmkDao.getPxnr(id);
	}
	
}
