package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.CustomerDao;
import gov.gdgs.zs.untils.Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.ResponseMessage.Type;
import com.gdky.restfull.utils.HashIdUtil;

@Service
@Transactional
public class CustomerService {
	
	@Resource
	private CustomerDao customerDao;

	public Map<String, Object> getCustomers(int page, int pageSize, String jgid, String where) {
		Long jid = HashIdUtil.decode(jgid);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return customerDao.getCustomers(page,pageSize,jid,map);
	}

	public void addCustomer(Map<String, Object> obj) {
		String uuid = Common.newUUID();
		obj.put("ID", uuid);
		obj.put("JG_ID", HashIdUtil.decode((String)obj.get("JG_ID")));
		obj.put("ADDDATE", Common.getCurrentTime2MysqlDateTime());
		customerDao.addCustomer(obj);
	}

	public ResponseMessage updateCustomer(String id, Map<String,Object> obj) {
		Map<String,Object> source = customerDao.getNsrsbhAndJgid(id);
		String nsrsbh = (String)source.get("nsrsbh");
		String nsrsbhdf = (String)source.get("nsrsbhdf");
		
		if(!nsrsbh.equals((String)obj.get("NSRSBH")) || !nsrsbhdf.equals((String)obj.get("NSRSBHDF"))){
			List<Map<String,Object>> ls = customerDao.getCustomerInYwbb(id);
			if (ls.size()>0){
				return new ResponseMessage(Type.warning, "500", "该委托企业已有报备记录，不能修改税务登记证号，若要修改委托企业税务登记证号，需退回业务报备后才能进行修改");
			}
		}

		customerDao.updateCustomer(id,obj);
		return new ResponseMessage(Type.warning, "200", "修改成功");
	}

	public void delCustomer(String id) {
		customerDao.delCustomer(id);
	}

	public Map<String, Object> searchCustomers(int page, int pageSize,
			String jgid, String where) {
		Long jid = HashIdUtil.decode(jgid);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return customerDao.searchCustomers(page,pageSize,jid,map);
	}

}
