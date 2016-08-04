package gov.gdgs.zs.service;
import gov.gdgs.zs.dao.ZYZZSJFXDao;
import gov.gdgs.zs.dao.SWSTJDao;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class ZYZZSJFXService  {
	@Resource
	private ZYZZSJFXDao zyzzsjfxDao;
	
	public Map<String, Object> getZyzzsjfxb(int nd){
	  Map<String, Object> rs=zyzzsjfxDao.zyzzsjfxb(nd);
	  return rs;
}

	
	
}

