/*
 * 年度鉴证情况统计
 */
package gov.gdgs.zs.service;
import java.util.Map;

import gov.gdgs.zs.dao.NDJZQKTJDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 系统业务报表年度鉴证情况统计表
 * @author lenovo
 *
 */
@Service
public class NDJZQKTJService {
@Resource 
private NDJZQKTJDao ndjzqkTjDao;


  
      public Map<String, Object> getNdjzqktjb(int year){
    	  Map<String, Object> rs=ndjzqkTjDao.ndjzqktj(year);
    	  return rs;
      }



}


