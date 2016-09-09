package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.SettingDao;

import java.util.HashMap;
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
		Map<String, Object> rs = settingDao.getYwSetting(page, pagesize);
		return rs;
	}

	public void updateSetting(String id, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

}
