package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.NDJYSRTJDao;
import gov.gdgs.zs.dao.SWSTJDao;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class NDJYSRTJService {
	@Resource
	private NDJYSRTJDao ndjysrtjDao;
	
	public Map<String, Object> getNdjysrtjb(int year){
	  Map<String, Object> rs=ndjysrtjDao.ndjysrtjb(year);
	  return rs;
}

	
	
}
