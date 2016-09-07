package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.SWSDao;
import gov.gdgs.zs.dao.YwglDao;
import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.exception.YwbbException;
import com.gdky.restfull.utils.HashIdUtil;

@Service
@Transactional
public class YwglService {

	@Resource
	private YwglDao ywglDao;

	@Resource
	private SWSDao swsDao;

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

	public Map<String, Object> getYwbbByJg(String hashId, int page,
			int pageSize, String where) {
		Long id = HashIdUtil.decode(hashId);
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
		Map<String, Object> obj = ywglDao.getYwbbByJg(id, page, pageSize, map);
		return obj;
	}

	public Map<String, Object> getYwbbMiscByJg(String hashId) {
		Long id = HashIdUtil.decode(hashId);
		List<Map<String, Object>> zysws = ywglDao.getYwbbMiscByJg(id);
		Map<String, Object> jgxx = swsDao.swsxx(id.intValue());

		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("zysws", zysws);
		obj.put("jgxx", jgxx);
		return obj;
	}

	public Map<String, Object> addYwbb(Map<String, Object> values) {
		Map<String, Object> xy = (Map<String, Object>) values.get("dataXY");
		Map<String, Object> yw = (Map<String, Object>) values.get("dataYW");
		Map<String, Object> jg = (Map<String, Object>) values.get("dataJG");
		Map<String, Object> customer = (Map<String, Object>) values
				.get("customer");
		String type = (String) values.get("type");

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
		o.put("NSRXZ", yw.get("NSRXZ"));
		o.put("HY_ID", yw.get("HY_ID"));
		o.put("ZSFS_DM", yw.get("ZSFS_DM"));
		o.put("ISWS", yw.get("ISWS"));
		o.put("SB_DM", yw.get("SB_DM"));
		// 处理城市和地区
		List<Integer> dq = (List<Integer>) yw.get("DQ");
		o.put("CS_DM", dq.get(0));
		o.put("QX_DM", dq.get(1));
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
		if ((Integer) jg.get("cs_dm") != (Integer) o.get("CS_DM")) {
			o.put("IS_YD", "Y");
		} else {
			o.put("IS_YD", "N");
		}

		/* 判断是否有报备上报资质 */
		// TODO

		/* 判断协议号是否唯一 */
		int xyhNum = ywglDao.getXyhNum((String) o.get("XYH"));
		if (xyhNum > 0) {
			throw new YwbbException("协议文号已存在");
		}
		/* 判断是否存在同企业同年度同类型的撤销报告，是则不允许提交 */
		// TODO

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
			ywglDao.addYwbb(o);
		} else if (type.equals("commit")) { // 直接报备
			o.put("BBHM", bbhm);
			o.put("YZM", yzm);
			o.put("ZT", 1);
			ywglDao.addYwbb(o);
		}
		return null;
	}

	public Map<String, Object> getYwbbByYzmAndBbhm(String bbhm, String yzm) {
		if (bbhm == null || bbhm.isEmpty() || yzm == null || yzm.isEmpty()) {
			throw new YwbbException("报备号码或者验证码不能为空");
		}
		bbhm = bbhm.trim();
		yzm = yzm.trim();
		Map<String, Object> rs = ywglDao.getYwbbByYzmAndBbhm(bbhm, yzm);
		return rs;
	}

	/*
	 * 按类型处理业务报备修改操作
	 * 修改请求json结构为{lx:int number, data:{}}
	 * data为修改的业务具体属性信息，lx为修改操作类型
	 * 1 - 业务信息修改
	 * 2 - 退回操作，将业务状态置为0(保存)
	 * 3 - 报备操作，将业务状态置为1（报备）
	 * 4 - 收费操作，将业务状态置为3（已收费）
	 * 5 - 申请撤销，将业务状态置为7（申请撤销）
	 * 6 - 同意撤销操作，将业务状态置为5（撤销）
	 * 7 - 拒绝撤销操作，将业务状态置为1（报备）
	 * 8 - 申请退回操作，将业务状态置为6（申请退回）
	 * 9 - 拒绝退回操作，将业务状态置为1（报备）
	 * 10- 申请启用操作，将业务状态置为8（申请启用）
	 * 11- 同意启用操作，将当条业务状态置为4（作废），
	 *     同时建立一条新记录，保留原记录信息，使用新的报备号码，状态置为0（保存）
	 * 12- 拒绝启用操作，将业务状态置为5（撤销）
	 */
	public void updateYwbb(String hashid, Map<String, Object> map) {
		Long id = HashIdUtil.decode(hashid);
		Integer lx = (Integer) map.get("lx");
		Map<String, Object> data = (Map<String, Object>) map.get("data");
		if (lx != null && lx == 2) {
			this.sentBackYw(id, data);
		} else if (lx != null && lx == 6) {
			this.updateYwbbZT(id, 5);
		} else if (lx != null && lx == 7) {
			this.updateYwbbZT(id, 1);
		} else if (lx != null && lx == 9) {
			this.updateYwbbZT(id, 1);
		} else if (lx != null && lx == 11) {
			this.passQY(id);
		} else if (lx != null && lx == 12) {
			this.updateYwbbZT(id, 5);
		}
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
		this.ywglDao.updateYwbbZT(id, 4);

	}

	private void updateYwbbZT(Long id, int zt) {
		this.ywglDao.updateYwbbZT(id, zt);
	}

	/*
	 * 退回，填写退回原因
	 */
	public void sentBackYw(Long id, Map<String, Object> data) {
		data.put("zt", 0);
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
		if (yjlx!=null && yjlx.equals("1")){
			condition.add(" AND sjsqje is null ");
		} else if (yjlx != null && yjlx.equals("2")){
			condition.add(" AND sjsqje > 1000000 ");
		} else if (yjlx != null && yjlx.equals("3")){
			condition.add(" AND sjsqje < 500 ");
		} else {
			condition.add(" AND (sjsqje < 500 OR sjsqje > 1000000 OR sjsqje is null) ");
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
		
		Map<String, Object> rs = ywglDao.getYwbb(page, pagesize, condition);
		return rs;
	}

}
