package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.PXMKDao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
