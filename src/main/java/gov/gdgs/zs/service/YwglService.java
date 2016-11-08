package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.SWSDao;
import gov.gdgs.zs.dao.YwglDao;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.exception.YwbbException;
import com.gdky.restfull.utils.Common;
import com.gdky.restfull.utils.HashIdUtil;

@Service
@Transactional
public class YwglService {

	@Resource
	private YwglDao ywglDao;

	@Resource
	private SWSDao swsDao;
	
	@Autowired
	private ZzglService zzglService;

	public Map<String, Object> getYwbb(int page, int pageSize, String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("swsmc", "FUZZY", where.get("swsmc"));
		condition.add("wtdw", "FUZZY", where.get("wtdw"));
		condition.add("wtdwnsrsbh", "FUZZY", where.get("wtdwnsrsbh"));
		condition.add("ywlx_dm", "EQUAL", where.get("ywlx_dm"));
		condition.add("xyje", "BETWEEN", where.get("xyje"));
		condition.add("sjsqje", "BETWEEN", where.get("sjsqje"));
		condition.add("xyh", "FUZZY", where.get("xyh"));
		condition.add("bgwh", "FUZZY", where.get("bgwh"));
		condition.add("bbhm", "FUZZY", where.get("bbhm"));
		condition.add("bbrq", "DATE_BETWEEN", where.get("bbrq"));
		condition.add("bgrq", "DATE_BETWEEN", where.get("bgrq"));
		condition.add("cs_dm", "EQUAL", where.get("cs_dm"));
		condition.add("zt", "EQUAL", where.get("zt"));
		condition.add("nd", "EQUAL", where.get("nd"));
		condition.add("zsfs_dm", "EQUAL", where.get("zsfs_dm"));
		condition.add("is_yd", "EQUAL", where.get("is_yd"));
		condition.add("swbz", "EQUAL", where.get("swbz"));
		condition.add(" AND yxbz = 1 AND zt != 0 AND zt != 4 ");

		Map<String, Object> rs = ywglDao.getYwbb(page, pageSize, condition);
		return rs;
	}

	public Map<String, Object> getYwbbById(String hash) {
		Long id = HashIdUtil.decode(hash);
		Map<String, Object> obj = ywglDao.getYwbbById(id);
		return obj;
	}

	public Map<String, Object> getYwbbByJg(User user, int page,
			int pagesize, String whereparam) {
		Integer jgId = user.getJgId();
		Condition condition = new Condition();
		condition.add("jg_id", "EQUAL", jgId);
		if(!StringUtils.isEmpty(whereparam)){
			Map<String,Object> where = Common.decodeURItoMap(whereparam);
			condition.add("wtdw", "FUZZY", where.get("wtdw"));
			condition.add("ywlx_dm", "EQUAL", where.get("ywlx_dm"));
			condition.add("xyje", "BETWEEN", where.get("xyje"));
			condition.add("sjsqje", "BETWEEN", where.get("sjsqje"));
			condition.add("xyh", "FUZZY", where.get("xyh"));
			condition.add("bgwh", "FUZZY", where.get("bgwh"));
			condition.add("bbhm", "FUZZY", where.get("bbhm"));
			condition.add("bbrq", "DATE_BETWEEN", where.get("bbrq"));
			condition.add("bgrq", "DATE_BETWEEN", where.get("bgrq"));
			condition.add("cs_dm", "EQUAL", where.get("cs_dm"));
			condition.add("zt", "EQUAL", where.get("zt"));
			condition.add("nd", "EQUAL", where.get("nd"));
			condition.add("zsfs_dm", "EQUAL", where.get("zsfs_dm"));
			condition.add("is_yd", "EQUAL", where.get("is_yd"));
			condition.add("swbz", "EQUAL", where.get("swbz"));
		}
		condition.add(" AND yxbz = 1 ");		
		
		Map<String, Object> obj = ywglDao.getYwbb(page, pagesize,condition);
		return obj;
	}

	public Map<String, Object> getYwbbMiscByJg(User user) {
		//获取资质锁定记录
		List<Map<String,Object>> locked = ywglDao.getJgLocked(user.getJgId());
		List<Map<String, Object>> zysws = ywglDao.getZyswsByJg(user.getJgId());
		Map<String, Object> jgxx = swsDao.swsxx(user.getJgId());
		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("locked", locked);
		obj.put("zysws", zysws);
		obj.put("jgxx", jgxx);
		return obj;
	}

	public Map<String, Object> addYwbb(Map<String, Object> values, User user) {
		Map<String, Object> xy = (Map<String, Object>) values.get("dataXY");
		Map<String, Object> yw = (Map<String, Object>) values.get("dataYW");
		Map<String, Object> jg = (Map<String, Object>) values.get("dataJG");
		Map<String, Object> customer = (Map<String, Object>) values
				.get("customer");
		String type = (String) values.get("type");
		
		/* 判断是否有报备上报资质 */
		if(zzglService.isJgLocked(user)){
			throw new YwbbException("不具备业务上报资质或上报业务资质目前被锁，请联系中心解锁");
		}

		// 整理业务记录
		HashMap<String, Object> o = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		int now_y = cal.get(Calendar.YEAR);// 得到年份
		int now_m = cal.get(Calendar.MONTH) + 1;// 得到月份
		int now_d = cal.get(Calendar.DATE);// 得到月份中今天的号数
		int now_h = cal.get(Calendar.HOUR_OF_DAY);// 得到一天中现在的时间，24小时制
		int now_mm = cal.get(Calendar.MINUTE);// 得到分钟数
		int now_s = cal.get(Calendar.SECOND);// 得到秒数

		String currentTime = Common.getCurrentTime2MysqlDateTime();
		o.put("BBRQ", currentTime);
		o.put("BGWH", yw.get("BGWH"));
		o.put("BGRQ", Common.getTime2MysqlDateTime((String) yw.get("BGRQ")));
		o.put("SFJE", yw.get("SFJE"));
		o.put("JG_ID", customer.get("JG_ID"));
		o.put("SWSMC", jg.get("dwmc"));
		o.put("SWSSWDJZH", jg.get("swdjhm"));
		o.put("WTDW", customer.get("DWMC"));
		o.put("WTDWNSRSBH", customer.get("NSRSBH"));
		o.put("XYH", xy.get("XYH"));
		o.put("YJFH", yw.get("YJFH"));
		o.put("RJFH", yw.get("RJFH"));
		o.put("SJFH", yw.get("SJFH"));
		List<Map<String, Object>> qmswsList = (List<Map<String, Object>>) yw
				.get("QMSWS");
		String QMSWSID = (String) qmswsList.get(0).get("key") + ","
				+ (String) qmswsList.get(1).get("key");
		String QZSWS = (String) qmswsList.get(0).get("label") + ","
				+ (String) qmswsList.get(1).get("label");
		o.put("QZSWS", QZSWS);
		o.put("QMSWSID", QMSWSID);
		o.put("TXDZ", jg.get("dzhi"));
		o.put("SWSDZYJ", jg.get("dzyj"));
		o.put("SWSWZ", jg.get("wangzhi"));
		o.put("YWLX_DM", xy.get("YWLX_DM"));
		Integer ywlx = Integer.parseInt((String) o.get("YWLX_DM"));
		o.put("JTXM", yw.get("JTXM"));
		o.put("ZBRQ", currentTime);
		List<String> sssq = (List<String>) xy.get("SSSQ");
		o.put("SENDTIME", Common.getTime2MysqlDateTime(sssq.get(1)));
		o.put("SSTARTTIME", Common.getTime2MysqlDateTime(sssq.get(0)));
		Calendar calND = Calendar.getInstance();
		calND.setTime(Common.getTimeFromJsToJava(sssq.get(1))); // 暂按项目所属期止
		o.put("ND", calND.get(Calendar.YEAR));
		o.put("MEMO", xy.get("MEMO"));
		o.put("NSRXZ", yw.get("NSRXZ"));
		o.put("HY_ID", yw.get("HY_ID"));
		o.put("ZSFS_DM", yw.get("ZSFS_DM"));
		o.put("ISWS", yw.get("ISWS"));
		o.put("SB_DM", yw.get("SB_DM"));
		// 处理城市和地区
		if(yw.get("ISWS").equals("Y")){
			o.put("CITY", yw.get("CITY"));
			o.put("CS_DM", -2);
			o.put("QX_DM", null);
			o.put("ZGSWJG", null);
		}else{
			List<Integer> dq = (List<Integer>) yw.get("DQ");
			o.put("CS_DM", dq.get(0));
			o.put("QX_DM", dq.get(1));
			o.put("CITY", ywglDao.getCITY(dq.get(0)));
			o.put("ZGSWJG", yw.get("ZGSWJG"));
		}
		o.put("WTDWXZ_DM", yw.get("WTDWXZ_DM"));
		o.put("WTDWNSRSBHDF", customer.get("NSRSBH"));
		o.put("WTDWLXR", customer.get("LXR"));
		o.put("WTDWLXDH", customer.get("LXDH"));
		o.put("WTDXLXDZ", customer.get("LXDZ"));
		o.put("XYJE", xy.get("XYJE"));
		o.put("CUSTOMER_ID", customer.get("ID"));
		if (yw.get("TZVALUE1") != null && ywlx != 1 && ywlx != 7) {
			o.put("TZVALUE1", yw.get("TZVALUE1"));
		} else {
			o.put("TZVALUE1", null);
		}
		if (yw.get("TJVALUE2") != null && ywlx != 1 && ywlx != 2 && ywlx != 7) {
			o.put("TJVALUE2", yw.get("TJVALUE2"));
		} else {
			o.put("TJVALUE2", null);
		}
		// 判断是否异地
		if ((Integer)o.get("CS_DM")!= -2 && ((Integer) jg.get("csdm") != (Integer) o.get("CS_DM"))) {
			o.put("IS_YD", "Y");
		} else {
			o.put("IS_YD", "N");
		}

		/* 判断协议号是否唯一 */
		int xyhNum = ywglDao.getXyhNum((String) o.get("XYH"),(Integer)o.get("JG_ID"));
		if (xyhNum > 0) {
			throw new YwbbException("协议文号已存在");
		}
		/* 判断是否存在同企业同年度同类型的撤销报告，是则不允许提交 */
		if(this.isExistSameLx(o.get("ND"),o.get("YWLX_DM"),o.get("CUSTOMER_ID"))){
			throw new YwbbException("本年度已存在同委托企业同类型的撤销报告");
		}

		// 生成随机验证码
		String yzm = RandomStringUtils.randomNumeric(8);
		// 生成报备号码
		StringBuffer bbhm = new StringBuffer(String.valueOf(now_y)
				+ Common.addZero(now_m, 2));
		bbhm.append(RandomStringUtils.randomNumeric(4));
		bbhm.append(cal.getTimeInMillis());
		bbhm.delete(21, 23);
		bbhm.delete(10, 17);
		/* 判断直接提交还是保存 */
		if (type.equals("save")) { // 保存
			o.put("YZM", yzm);
			o.put("BBHM", bbhm);
			o.put("ZT", 0);
			o.put("XYZT_DM", 2);
			ywglDao.addYwbb(o);
		} else if (type.equals("commit")) { // 直接报备
			o.put("BBHM", bbhm);
			o.put("YZM", yzm);
			o.put("ZT", 1);
			o.put("XYZT_DM", 3);
			ywglDao.addYwbb(o);
		}
		Map<String,Object> resp = new HashMap<String,Object>();
		resp.put("yzm", yzm);
		resp.put("bbhm", bbhm);
		return resp;
	}


	private boolean isExistSameLx(Object nd, Object ywlx, Object customer) {
		List<Map<String,Object>> ls = ywglDao.isExistSameLx(nd,ywlx,customer);
		if(ls.size()>0){
			return true;
		}
		return false;
	}

	public Map<String, Object> getYwbbByYzmAndBbhm(String bbhm, String yzm) {
		if (StringUtils.isBlank(bbhm)|| StringUtils.isBlank(yzm)) {
			throw new YwbbException("报备号码或者验证码不能为空");
		}
		bbhm = bbhm.trim();
		yzm = yzm.trim();
		Map<String, Object> rs = ywglDao.getYwbbByYzmAndBbhm(bbhm, yzm);
		if(rs == null) {
			throw new YwbbException("无法查询到相应的业务信息，请提供有效的报备号码和验证码。");
		}
		return rs;
	}

	/*
	 * 按类型处理业务报备修改操作 修改请求json结构为{lx:int number, data:{}}
	 * data为修改的业务具体属性信息，lx为修改操作类型 
	 * 1 - 业务信息修改 
	 * 2 - 退回操作，将业务状态置为0(保存)，协议状态置为1
	 * 3 - 报备操作，将业务状态置为1（报备），协议状态置为3  客户端
	 * 4 - 收费操作，将业务状态置为3（已收费），协议状态置为4 客户端
	 * 5 - 申请撤销，将业务状态置为7（申请撤销） 客户端
	 * 6 - 同意撤销操作，将业务状态置为5（撤销），协议状态置为0
	 * 7 - 拒绝撤销操作，将业务状态置为1（报备） ，协议状态置为3
	 * 8 - 申请退回操作，将业务状态置为6（申请退回） 客户端
	 * 9 - 拒绝退回操作，将业务状态置为1（报备），协议状态置为3 
	 * 10- 申请启用操作，将业务状态置为8（申请启用） 客户端
	 * 11- 同意启用操作，将当条业务状态置为4（作废），协议状态置为0
	 *     同时建立一条新记录，保留原记录信息，使用新的报备号码，业务状态置为0，协议状态置为1（保存） 
	 * 12- 拒绝启用操作，将业务状态置为5，协议状态置为0（撤销）
	 */
	public void updateYwbb(String hashid, Map<String, Object> map) {
		Long id = HashIdUtil.decode(hashid);
		Integer lx = (Integer) map.get("lx");
		Map<String, Object> data = (Map<String, Object>) map.get("data");
		if (lx != null && lx == 2) {
			this.sentBackYw(id, data);
		} else if (lx != null && lx == 6) {
			this.ywglDao.updateYwbbZT(id, 5, 0);
		} else if (lx != null && lx == 7) {
			this.ywglDao.updateYwbbZT(id, 1, 3);
		} else if (lx != null && lx == 9) {
			this.ywglDao.updateYwbbZT(id, 1, 3);
		} else if (lx != null && lx == 11) {
			this.passQY(id);
		} else if (lx != null && lx == 12) {
			this.ywglDao.updateYwbbZT(id, 5, 0);
		} else if (lx != null && lx == 3) {
			this.handleYwBB(id, data);
		} else if (lx != null && lx == 4) {
			this.handleYwSF(id,data);
		} else if (lx != null && lx == 8) {
			this.handleYwTH(id,data);
		} else if (lx != null && lx == 5) {
			this.ywglDao.updateYwbbZT(id, 7);
		} else if (lx != null && lx == 10 ){
			this.handleYwQY(id,data);
		}
	}
	
	private void handleYwBB(Long id, Map<String,Object> data){
		//TODO 首先要检测报备资质
		ywglDao.updateYwbbZT(id, 1, 3);
	}

	private void handleYwQY(Long id, Map<String, Object> data) {
		data.put("id", id);
		data.put("zt", 8);
		ywglDao.handleYwQY(id, data);
		
	}

	private void handleYwTH(Long id, Map<String, Object> data) {
		data.put("id", id);
		data.put("zt", 6);
		ywglDao.handleYwTH(id, data);
	}

	private void handleYwSF(Long id, Map<String, Object> data) {
		data.put("id", id);
		data.put("zt", 3);
		data.put("xyzt_dm", 4);
		ywglDao.handleYwSF(id, data);		
	}

	private void passQY(Long id) {
		// 生成随机验证码
		String yzm = RandomStringUtils.randomNumeric(8);
		// 生成报备号码
		Calendar cal = Calendar.getInstance();
		int now_y = cal.get(Calendar.YEAR);// 得到年份
		int now_m = cal.get(Calendar.MONTH) + 1;// 得到月份
		StringBuffer bbhm = new StringBuffer(String.valueOf(now_y)
				+ Common.addZero(now_m, 2));
		bbhm.append(RandomStringUtils.randomNumeric(4));
		bbhm.append(cal.getTimeInMillis());
		bbhm.delete(21, 23);
		bbhm.delete(10, 17);
		// 生成一条新记录
		Number newId = this.ywglDao.newRecordFromId(id, bbhm.toString(), yzm);
		// 将原记录置为作废
		this.ywglDao.updateYwbbZT(id, 4, 0);

	}

	/*
	 * 退回，填写退回原因
	 */
	public void sentBackYw(Long id, Map<String, Object> data) {
		data.put("zt", 0);
		data.put("xyzt_dm", 1);
		this.ywglDao.sentBack(id, data);
	}

	public Map<String, Object> getYwbbSFJEYJ(int page, int pagesize,
			String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("swsmc", "FUZZY", where.get("swsmc"));
		condition.add("wtdw", "FUZZY", where.get("wtdw"));
		condition.add("wtdwnsrsbh", "FUZZY", where.get("wtdwnsrsbh"));
		condition.add("ywlx_dm", "EQUAL", where.get("ywlx_dm"));
		condition.add("xyje", "BETWEEN", where.get("xyje"));
		condition.add("sjsqje", "BETWEEN", where.get("sjsqje"));
		condition.add("xyh", "FUZZY", where.get("xyh"));
		condition.add("bgwh", "FUZZY", where.get("bgwh"));
		condition.add("bbhm", "FUZZY", where.get("bbhm"));
		condition.add("bbrq", "DATE_BETWEEN", where.get("bbrq"));
		condition.add("bgrq", "DATE_BETWEEN", where.get("bgrq"));
		condition.add("cs_dm", "EQUAL", where.get("cs_dm"));
		condition.add("zt", "EQUAL", where.get("zt"));
		condition.add("nd", "EQUAL", where.get("nd"));
		condition.add("zsfs_dm", "EQUAL", where.get("zsfs_dm"));
		condition.add("is_yd", "EQUAL", where.get("is_yd"));
		condition.add("swbz", "EQUAL", where.get("swbz"));
		condition.add(" AND yxbz = 1  ");
		condition.add(" AND (zt = 1  OR zt = 3)  ");
		String yjlx = (String) where.get("yjlx");
		if (yjlx != null && yjlx.equals("1")) {
			condition.add(" AND sjsqje is null ");
		} else if (yjlx != null && yjlx.equals("2")) {
			condition.add(" AND sjsqje > 1000000 ");
		} else if (yjlx != null && yjlx.equals("3")) {
			condition.add(" AND sjsqje < 500 ");
		} else {
			condition
					.add(" AND (sjsqje < 500 OR sjsqje > 1000000 OR sjsqje is null) ");
		}
		Map<String, Object> rs = ywglDao.getYwbb(page, pagesize, condition);
		return rs;
	}

	public Map<String, Object> getYwbbNDBTYJ(int page, int pagesize,
			String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("swsmc", "FUZZY", where.get("swsmc"));
		condition.add("wtdw", "FUZZY", where.get("wtdw"));
		condition.add("wtdwnsrsbh", "FUZZY", where.get("wtdwnsrsbh"));
		condition.add("ywlx_dm", "EQUAL", where.get("ywlx_dm"));
		condition.add("xyje", "BETWEEN", where.get("xyje"));
		condition.add("sjsqje", "BETWEEN", where.get("sjsqje"));
		condition.add("bbhm", "FUZZY", where.get("bbhm"));
		condition.add("bbrq", "DATE_BETWEEN", where.get("bbrq"));
		condition.add("cs_dm", "EQUAL", where.get("cs_dm"));
		condition.add("zt", "EQUAL", where.get("zt"));
		condition.add("nd", "EQUAL", where.get("nd"));
		condition.add("zsfs_dm", "EQUAL", where.get("zsfs_dm"));
		condition.add("is_yd", "EQUAL", where.get("is_yd"));
		condition.add("swbz", "EQUAL", where.get("swbz"));
		condition.add(" AND yxbz = 1 AND zt != 0 AND zt != 4 ");

		Map<String, Object> rs = ywglDao.getYwbbNDBTYJ(page, pagesize,
				condition);
		return rs;
	}

	public Map<String, Object> getYwbbWTFYJ(int page, int pagesize,
			String whereParam) {
		HashMap<String, Object> where = new HashMap<String, Object>();
		if (whereParam != null) {
			try {
				whereParam = java.net.URLDecoder.decode(whereParam, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				where = mapper.readValue(whereParam,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		// 拼接查询条件
		Condition condition = new Condition();
		condition.add("swsmc", "FUZZY", where.get("swsmc"));
		condition.add("wtdw", "FUZZY", where.get("wtdw"));
		condition.add("wtdwnsrsbh", "FUZZY", where.get("wtdwnsrsbh"));
		condition.add("ywlx_dm", "EQUAL", where.get("ywlx_dm"));
		condition.add("xyje", "BETWEEN", where.get("xyje"));
		condition.add("sjsqje", "BETWEEN", where.get("sjsqje"));
		condition.add("bbhm", "FUZZY", where.get("bbhm"));
		condition.add("bbrq", "DATE_BETWEEN", where.get("bbrq"));
		condition.add("cs_dm", "EQUAL", where.get("cs_dm"));
		condition.add("zt", "EQUAL", where.get("zt"));
		condition.add("nd", "EQUAL", where.get("nd"));
		condition.add("zsfs_dm", "EQUAL", where.get("zsfs_dm"));
		condition.add("is_yd", "EQUAL", where.get("is_yd"));
		condition.add("swbz", "EQUAL", where.get("swbz"));
		condition.add(" AND yxbz = 1 AND zt != 0 AND zt != 4 ");

		Map<String, Object> rs = ywglDao
				.getYwbbWTFYJ(page, pagesize, condition);
		return rs;
	}

	/**
	 * 业务报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjfx(String where) {
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
		return ywglDao.getYwbbsjfx(map);
	}

	/**
	 * 业务报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjfxDq(String where) {
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
		return ywglDao.getYwbbsjfxDq(map);
	}

	public Map<String, Object> getYwbbsjfxSws(String where) {
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
		return ywglDao.getYwbbsjfxSws(map);
	}

	public Map<String, Object> getYwbbsjfxYwlx(String where) {
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
		return ywglDao.getYwbbsjfxYwlx(map);
	}

	/**
	 * 业务报备数据汇总-机构人员报备数据分析
	 * 
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzRy(int page, int pagesize,
			String where) {
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
		return ywglDao.getYwbbsjhzRy(page, pagesize, map);
	}

	/**
	 * 业务报备数据汇总-机构人员报备数据分析-根据业务报备表的id查找报备数据明细
	 * 
	 * @param bbid
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzRyMx(String bbid) {
		return ywglDao.getYwbbsjhzRyMx(bbid);
	}

	/**
	 * 业务报备数据汇总-机构人员报备数据分析-报备机构
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzYwbbJg(int page, int pagesize,
			String where) {
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
		return ywglDao.getYwbbsjhzYwbbJg(page, pagesize, map);
	}

	/**
	 * 业务报备数据汇总-会计所报备数据分析
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzSws(int page, int pagesize,
			String where) {
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
		return ywglDao.getYwbbsjhzSws(page, pagesize, map);
	}

	/**
	 * 业务报备数据汇总-外省报备数据分析
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzWs(int page, int pagesize,
			String where) {
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
		return ywglDao.getYwbbsjhzWs(page, pagesize, map);
	}

	/**
	 * 业务报备数据汇总-业务报备总收费金额数据分析
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	public Map<String, Object> getYwbbsjhzJe(int page, int pagesize,
			String where) {
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
		return ywglDao.getYwbbsjhzJe(page, pagesize, map);
	}

	/**
	 * 个人业务统计
	 * @param jgid
	 * @param where
	 * @return
	 */
	public Map<String, Object> getGrywtj(String jgid, String where) {
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
		map.put("jgid", jgid);
		return ywglDao.getGrywtj(map);
	}

	/**
	 * 客户端-事务所业务统计
	 * @param jgid
	 * @param pagesize 
	 * @param page 
	 * @param where
	 * @return
	 */
	public Map<String, Object> getSwsywtj(String jgid, int page, int pagesize, String where) {
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
		map.put("jgid", jgid);
		return ywglDao.getSwsywtj(page, pagesize, map);
	}

	/**
	 * 事务所业务统计-明细
	 * @param ywlx
	 * @param bbnd
	 * @param jgid
	 * @param where
	 * @return
	 */
	public Map<String, Object> getSwsywtjMx(String ywlx, String bbnd,
			String jgid, String where) {
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
		return ywglDao.getSwsywtjMx(ywlx, bbnd, jgid, map);
	}

	public Map<String, Object> addSaveYwbb(Map<String, Object> value, User user) {
		Map<String, Object> formValue = (Map<String, Object>) value.get("formValue");
		Map<String, Object> jg = (Map<String, Object>) value.get("dataJG");
		
		// 整理业务记录
		HashMap<String, Object> o = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		int now_y = cal.get(Calendar.YEAR);// 得到年份
		int now_m = cal.get(Calendar.MONTH) + 1;// 得到月份
		int now_d = cal.get(Calendar.DATE);// 得到月份中今天的号数
		int now_h = cal.get(Calendar.HOUR_OF_DAY);// 得到一天中现在的时间，24小时制
		int now_mm = cal.get(Calendar.MINUTE);// 得到分钟数
		int now_s = cal.get(Calendar.SECOND);// 得到秒数

		String currentTime = Common.getCurrentTime2MysqlDateTime();
		o.put("BBRQ", currentTime);
		o.put("BGWH", formValue.get("BGWH"));
		if(formValue.get("BGRQ")!=null){
			o.put("BGRQ", Common.getTime2MysqlDateTime((String) formValue.get("BGRQ")));
		}else{
			o.put("BGRQ",null);
		}
		o.put("SFJE", formValue.get("SFJE"));
		o.put("JG_ID", user.getJgId());
		o.put("SWSMC", jg.get("dwmc"));
		o.put("SWSSWDJZH", jg.get("swdjhm"));
		o.put("WTDW", formValue.get("DWMC"));
		o.put("WTDWNSRSBH", formValue.get("NSRSBH"));
		o.put("XYH", formValue.get("XYH"));
		o.put("YJFH", formValue.get("YJFH"));
		o.put("RJFH", formValue.get("RJFH"));
		o.put("SJFH", formValue.get("SJFH"));
		if(formValue.get("QMSWS") != null){
			List<Map<String, Object>> qmswsList = (List<Map<String, Object>>) formValue
					.get("QMSWS");
			String QMSWSID = (String) qmswsList.get(0).get("key") + ","
					+ (String) qmswsList.get(1).get("key");
			String QZSWS = (String) qmswsList.get(0).get("label") + ","
					+ (String) qmswsList.get(1).get("label");
			o.put("QZSWS", QZSWS);
			o.put("QMSWSID", QMSWSID);
		}else {
			o.put("QZSWS", null);
			o.put("QMSWSID", null);
		}
		o.put("TXDZ", jg.get("dzhi"));
		o.put("SWSDZYJ", jg.get("dzyj"));
		o.put("SWSWZ", jg.get("wangzhi"));
		o.put("YWLX_DM", formValue.get("YWLX_DM"));
		Integer ywlx = null;
		if (formValue.get("YWLX_DM")!=null){
			ywlx = Integer.parseInt((String) o.get("YWLX_DM"));
		}
		if (formValue.get("TZVALUE1") != null && ywlx != 1 && ywlx != 7) {
			o.put("TZVALUE1", formValue.get("TZVALUE1"));
		} else {
			o.put("TZVALUE1", null);
		}
		if (formValue.get("TJVALUE2") != null && ywlx != 1 && ywlx != 2 && ywlx != 7) {
			o.put("TJVALUE2", formValue.get("TJVALUE2"));
		} else {
			o.put("TJVALUE2", null);
		}
		o.put("JTXM", formValue.get("JTXM"));
		o.put("ZBRQ", currentTime);
		if(formValue.get("SSSQ")!=null){
			List<String> sssq = (List<String>) formValue.get("SSSQ");
			o.put("SENDTIME", Common.getTime2MysqlDateTime(sssq.get(1)));
			o.put("SSTARTTIME", Common.getTime2MysqlDateTime(sssq.get(0)));
		}else{
			o.put("SENDTIME",null);
			o.put("SSTARTTIME",null);
		}
		o.put("ND",null);
		o.put("MEMO", formValue.get("MEMO"));
		o.put("NSRXZ", formValue.get("NSRXZ"));
		o.put("HY_ID", formValue.get("HY_ID"));
		o.put("ZSFS_DM", formValue.get("ZSFS_DM"));
		o.put("ISWS", formValue.get("ISWS"));
		o.put("SB_DM", formValue.get("SB_DM"));
		o.put("CITY", formValue.get("CITY"));
		if(formValue.get("ISWS").equals("Y")){
			o.put("CS_DM", -2);
			o.put("QX_DM", null);
		}else if (formValue.get("DQ")!=null){
			List<Integer> dq = (List<Integer>) formValue.get("DQ");
			o.put("CS_DM", dq.get(0));
			o.put("QX_DM", dq.get(1));			
		}else {
			o.put("CS_DM",null);
			o.put("QX_DM",null);
		}
		o.put("ZGSWJG", formValue.get("ZGSWJG"));
		o.put("WTDWXZ_DM", formValue.get("WTDWXZ_DM"));
		o.put("WTDWNSRSBHDF", formValue.get("NSRSBH"));
		o.put("WTDWLXR", formValue.get("LXR"));
		o.put("WTDWLXDH", formValue.get("LXDH"));
		o.put("WTDXLXDZ", formValue.get("LXDZ"));
		o.put("XYJE", formValue.get("XYJE"));
		o.put("CUSTOMER_ID", formValue.get("ID"));
		o.put("ZT", 0);
		o.put("XYZT_DM", 1);
		ywglDao.addSaveYwbb(o);

		Map<String,Object> resp = new HashMap<String,Object>();
		return resp;
	}

	public Integer delYwbb(String hash, User user) {
		Long id = HashIdUtil.decode(hash);
		return ywglDao.delYwbb(id,user);
	}

}
