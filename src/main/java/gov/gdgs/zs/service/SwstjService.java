package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.gdgs.zs.dao.SWSTJDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 系统报表事务所情况统计B
 * @author lenovo
 *
 */
@Service
public class SwstjService {
@Resource 
private SWSTJDao swsTjDao;


  
      public Map<String, Object> getSwstjb(int year){
    	  Map<String, Object> rs=swsTjDao.swstj(year);
    	  return rs;
      }



}
