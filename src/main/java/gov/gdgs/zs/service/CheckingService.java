package gov.gdgs.zs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	/**
	 * 合并前机构检查
	 * @param sumbitValue
	 * @param jgid
	 * @return
	 */
	public Map<String,Object> checkDoFix(String sumbitValue,Integer jgid,String userName){
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (sumbitValue != null) {
			try {
				sumbitValue = java.net.URLDecoder.decode(sumbitValue, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(sumbitValue,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map<String,Object> re=new HashMap<String,Object>();
		ArrayList<String>mcList=(ArrayList<String>) map.get("SFMCLIST");
		ArrayList<String>zsbhList=(ArrayList<String>) map.get("SFZSBHLIST");
		if(!chDao.haveJGDWMC(map.get("XSWSMC").toString())){
			re.put("re", "E");//已存在新机构名称
			return re;
		}
		if(!mcList.contains(userName)){
			re.put("re", "C");//需包含自身事务所
			return re;
		}
		if(mcList.get(0).equals(mcList.get(mcList.size()-1))){
			re.put("re", "D");//不能自己和自己合并
			return re;
		}
		for(int i = 0;i<mcList.size();i++){
			if(chDao.haveJG(mcList.get(i),zsbhList.get(i))){
				re.put("re", "A");//不存在该事务所
				return re;
			}
			if(!chDao.checkSWSSPing(mcList.get(i))){
				re.put("re", "B");//事务所存在审批事项
				return re;
			}
			if(!chDao.checkZYSWSSPing(mcList.get(i))){
				re.put("re", "F");//执业税务师存在审批事项
				return re;
			}
		}
		return re;
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
	
	public boolean checkBeforeDelete(String jgmc){
		return (this.chDao.checkSWSSPing(jgmc)&&chDao.checkZYSWSSPing(jgmc));
	}
	public boolean checkSWSSPing(Integer jgid){
		return this.chDao.checkSWSSPing(jgid);
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
