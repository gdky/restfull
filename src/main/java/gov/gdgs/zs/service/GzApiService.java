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

}
