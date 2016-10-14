package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.Map;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.dao.CheckingDao;

import javax.annotation.Resource;

import org.hashids.Hashids;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CheckingService {

	@Resource
	private CheckingDao chDao;
	
	public boolean checkSPing(String splx,String jgid){
		Hashids hashids = new Hashids(Config.HASHID_SALT,Config.HASHID_LEN);
		switch (splx) {
		case "jgbg":
			return chDao.checkBGing((int)hashids.decode(jgid)[0]);
		case "jgzx":
			return chDao.checkZXing((int)hashids.decode(jgid)[0]);
		case "jghb":
			return chDao.checkHBing((int)hashids.decode(jgid)[0]);
		case "zysp":
			return chDao.checkZYSPing((int)hashids.decode(jgid)[0]);
		}
		return true;
	}
	
	/**
	 * 判断机构审批状态
	 * 无审批返回空字符串""
	 * @param jgid
	 * @return
	 */
	public String checkJGSPing(Integer jgid){
		if(!chDao.checkBGing(jgid)){
			return "变更审批中";
		}else if(!chDao.checkZXing(jgid)){
			return "注销审批中";
		}else if(!chDao.checkHBing(jgid)){
			return "合并审批中";
		}else if(!chDao.checkSLing(jgid)){
			return "设立审批中";
		}
		return "";
	}
	
	public boolean checkJGSPingSelf(Integer jgid){
		return this.chDao.checkJGSPing(jgid);
	}
	public boolean checkJGFssl(Integer jgid){
		return this.chDao.checkJGFssl(jgid);
	}
	public boolean checkIsBH(String spid){
		return this.chDao.checkIsBH(spid);
	}
	
	//检查身份证身份证是否已存在
	public boolean checkSFZH(String sfzh){
		return this.chDao.checkHadSFZH(sfzh);
	}
	//检查是否可以提交报表
	public Object checkIfTJBB(String bblx,Integer jgid,String checked){
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (checked != null) {
			try {
				checked = java.net.URLDecoder.decode(checked, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(checked,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}		
		switch (bblx) {
		case "zcmxb":
			return chDao.checkIfZCMXB(jgid,map);
		}
		return null;
	}
}
