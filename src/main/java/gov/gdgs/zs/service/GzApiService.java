package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.GzApiDao;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GzApiService {
	
	@Resource
	private GzApiDao gzApiDao;

	public Map<String, Object> getSws(String year, String month, String day,
			String hour) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		List<Map<String,Object>> ls = gzApiDao.getSws(year,month,day,hour);
		obj.put("code", 200);
		obj.put("total", ls.size());
		obj.put("data", ls);	
		
		return obj;
	}

	public Map<String, Object> getSwsjg(String year, String month, String day,
			String hour) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		List<Map<String,Object>> ls = gzApiDao.getSwsjg(year,month,day,hour);
		obj.put("code", 200);
		obj.put("total", ls.size());
		obj.put("data", ls);	
		
		return obj;
	}

	public Map<String, Object> getYwba(String year, String month, String day,
			String hour) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		String start = this.getQueryStartTime(year, month, day, hour);
		String end = this.getQueryEndtime(year, month, day, hour);
		List<Map<String,Object>> ls = gzApiDao.getYwba(start,end);
		obj.put("code", 200);
		obj.put("total", ls.size());
		obj.put("data", ls);	
		
		return obj;
	}


	public Map<String, Object> getZsxy(String year, String month, String day,
			String hour) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		String start = this.getQueryStartTime(year, month, day, hour);
		String end = this.getQueryEndtime(year, month, day, hour);
		List<Map<String,Object>> ls = gzApiDao.getZsxy(start,end);
		obj.put("code", 200);
		obj.put("total", ls.size());
		obj.put("data", ls);	
		
		return obj;
	}
	
	private String getQueryEndtime(String year, String month, String day,
			String hour) {
		StringBuffer time = new StringBuffer(year);
		time.append("-").append(month).append("-").append(day);
		time.append(" ");
		time.append(hour).append(":").append("59").append(":").append("59");
		return time.toString();
	}

	private String getQueryStartTime(String year, String month, String day,
			String hour) {
		StringBuffer time = new StringBuffer(year);
		time.append("-").append(month).append("-").append(day);
		time.append(" ");
		time.append(hour).append(":").append("00").append(":").append("00");
		return time.toString();
	}
	
	public void insertSWSJG(Integer jgid,int way){
		if(jgid==null){
			return;
		}
		switch(way){
		case 1:gzApiDao.addJG(jgid);break;//增加
		case 2:gzApiDao.changedJG(jgid);break;//修改
		case 0:gzApiDao.delJG(jgid);break;//删除
		}
	}
	public void insertSWS(Integer zyid,int way){
		if(zyid==null){
			return;
		}
		gzApiDao.insertSWS(zyid, way);
	}
	
	/**
	 *  为广州接口数据池添加业务和协议变更的记录
	 * @param ywId 业务报备Id
	 * @param type 1 - 增加 ；2 - 修改；0 - 删除
	 */
	public void insertYWBB(Number ywId,Integer type){
		
	}

}
