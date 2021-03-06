package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.PXMKDao;
import gov.gdgs.zs.dao.SWSDao;
import gov.gdgs.zs.untils.Condition;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.dao.UploadDao;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.Common;
import com.google.common.base.Objects;

@Service
@Transactional
public class PXMKService {
	@Resource
	private PXMKDao pxmkDao;
	
	@Autowired
	private UploadDao uploadDao;
	
	@Resource
	private SWSDao swsDao;
	
	public Map<String, Object> getpxfbList(int pn, int ps, String where) throws Exception {
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
		return pxmkDao.getpxfbList(pn, ps, map);
		
	}
	public void fspsq(Map<String,Object> ptxm,String splx)throws Exception {
		Object fjurl=null; 
		if(ptxm.containsKey("FJ")&&(splx.equals("pxxxfb")||splx.equals("pxxxxg"))){
			Map<String,Object> fj=(Map<String, Object>) ptxm.get("FJ");
			//有记录且记录相同
			if(ptxm.containsKey("pxid")&&!Objects.equal(ptxm.get("pxid"), "")&&(this.pxmkDao.getpxqkbFJURL(ptxm.get("pxid")).equals(fj.get("uploadUrl").toString()))){
				fjurl=fj.get("uploadUrl");
			//无记录或发布
			}else if(splx.equals("pxxxfb")||this.pxmkDao.getpxqkbFJURL(ptxm.get("pxid")).equals("")){
				fjurl=fj.get("uploadUrl");
				this.uploadDao.insertfile(fj);
			}else if(ptxm.containsKey("pxid")){
				fjurl=fj.get("uploadUrl");
				this.uploadDao.insertfile(fj);
				this.uploadDao.delfile(this.pxmkDao.getpxqkbFJURL(ptxm.get("pxid")));
			}
		}
		ptxm.put("fjurl", fjurl);
		 switch (splx) {
			case "pxxxfb"://培训信息发布
				this.pxmkDao.pxxxfb(ptxm);break;
			case "pxxxxg"://培训信息修改
				this.pxmkDao.pxxxxg(ptxm);break;
			case "pxxxsc"://培训信息删除
				this.pxmkDao.pxxxsc(ptxm);break;
			case "pxxxtz"://培训信息停止报名
				this.pxmkDao.pxxxtz(ptxm);break;
		 }
	}
	public Map<String,Object> getPxxx(User user, int page, int pagesize, String whereparam) {
		Integer jgId = user.getJgId();
		Condition condition = new Condition();
		if(!StringUtils.isEmpty(whereparam)){
			Map<String,Object> where = Common.decodeURItoMap(whereparam);
		}
		condition.add(" AND yxbz = 1 ");		
		Map<String, Object> obj = pxmkDao.getPxxxForUser(user,page, pagesize,condition);
		return obj;
	}
	public Map<String,Object> getPxxx(int page, int pagesize, String whereparam) {
		Condition condition = new Condition();
		if(!StringUtils.isEmpty(whereparam)){
			Map<String,Object> where = Common.decodeURItoMap(whereparam);
		}
		condition.add(" AND yxbz = 1 ");		
		Map<String, Object> obj = pxmkDao.getPxxx(page, pagesize,condition);
		return obj;
	}
	public Map<String,Object> getPxxxMx(String id){
		return pxmkDao.getPxxxMx(id);
	}
	public Map<String,Object> getPxnr(String id) {
		return pxmkDao.getPxnr(id);
	}
	public List<Map<String,Object>> pxtjbmList(String id) {
		return pxmkDao.pxtjbmList(id);
	}
	public Map<String, Object> getPxbmInit(User user,String id ) {
		Map<String,Object> px = this.getPxxxMx(id);
		Map<String,Object> jg = swsDao.swsxx(user.getJgId());
		List<Map<String,Object>> ry = this.pxmkDao.getPxbmRy(user , id);
		Map<String,Object> rs = new HashMap<String,Object>();
		px.put("swsmc", jg.get("dwmc"));
		px.put("swsdh", jg.get("dhua"));
		px.put("swscz", jg.get("czhen"));
		px.put("fddbr", jg.get("fddbr"));
		px.put("swssj", jg.get("szphone"));
		px.put("swsdz", jg.get("dzhi"));
		px.put("swsdzyj", jg.get("dzyj"));
		rs.put("base", px);
		rs.put("ry",ry);
		return rs;
	}
	public void addPxbm(User user, String pxid, List<Map<String, Object>> values) {
		List<Integer> ls = pxmkDao.makeBHs(values.size());
		Calendar calendar = Calendar.getInstance();
		int year =  (calendar.get(Calendar.YEAR)%100);
		String month = Common.addZero(calendar.get(Calendar.MONTH)+1, 2);
		int i = 0;
		for (Map<String,Object> item : values){
			item.put("id", Common.newUUID());
			item.put("pxid", pxid);
			item.put("jg_id", user.getJgId());
			item.put("bmsj", Common.getCurrentTime2MysqlDateTime());
			item.put("bh", "PX"+year+month+Common.addZero((int)ls.get(i), 4));
			i++;
		}
		pxmkDao.addPxbm(values.toArray(new HashMap[values.size()]));
		
	}
	public void updatePxbm(User user, String pxid,
			List<Map<String, Object>> values) {
		List<Integer> ls = pxmkDao.makeBHs(values.size());
		Calendar calendar = Calendar.getInstance();
		int year =  (calendar.get(Calendar.YEAR)%100);
		String month = Common.addZero(calendar.get(Calendar.MONTH)+1, 2);
		int i = 0;
		for (Map<String,Object> item : values){
			item.put("id", Common.newUUID());
			item.put("pxid", pxid);
			item.put("jg_id", user.getJgId());
			item.put("bmsj", Common.getCurrentTime2MysqlDateTime());
			item.put("bh", "PX"+year+month+Common.addZero((int)ls.get(i), 4));
			i++;
		}
		pxmkDao.delRyByPxidAndUser(user, pxid);
		pxmkDao.addPxbm(values.toArray(new HashMap[values.size()]));
	}

	
}
