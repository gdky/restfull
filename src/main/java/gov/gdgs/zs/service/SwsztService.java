
/**
 * @author lsz
 * 功能：税务师状态统计
 */

package gov.gdgs.zs.service;

import java.util.Map;

import gov.gdgs.zs.dao.SWSZTTJDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class SwsztService {
@Resource
	private  SWSZTTJDao swsztTjDao;

public Map<String, Object> getSwstjb(int year){
	  Map<String, Object> rs=swsztTjDao.swszttjb(year);
	  return rs;
}



}
