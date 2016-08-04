/*
 * 机构数据分析
 */
package gov.gdgs.zs.service;

import java.util.Map;

import gov.gdgs.zs.dao.JGSJFXDao;



import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 机构数据分析/行业学历数据分析
 * @author lenovo
 *
 */
@Service
public class JGSJFXService {
@Resource 
private JGSJFXDao jgsjfxDao;
      public Map<String, Object> getHyxlsjfxb(int nd){
    	  Map<String, Object> rs=jgsjfxDao.getHyxlsjfxb(nd);
    	  return rs;
      }
	



}


