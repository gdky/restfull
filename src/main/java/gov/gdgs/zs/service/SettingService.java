package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.SettingDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettingService {

	@Resource
	private SettingDao settingDao;

	public Map<String, Object> getSettings(String lx, int page, int pagesize) {
		Map<String, Object> rs = new HashMap<String, Object>();
		if (lx != null && lx.equals("yw")) {
			rs = this.getYwSetting(page, pagesize);
		}
		return rs;
	}

	private Map<String, Object> getYwSetting(int page, int pagesize) {
		List<Map<String,Object>> ls = settingDao.getYwSetting();
		List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
		for (int i = 0 ; i < ls.size(); i++){
			Map<String,Object> map = ls.get(i);
			if (((String)map.get("param")).equals("zysws_alert_fs_peryear")){
				l.add(map);
			}
			if (((String)map.get("param")).equals("zysws_lock_fs_peryear")){
				l.add(map);
			}
		}
		Map<String,Object> rs = new HashMap<String,Object>();
		rs.put("data",l);
		return rs;
	}

	public void updateSetting(String id, Map<String, Object> map) {
		settingDao.updateSetting(id,map);
	}

}
