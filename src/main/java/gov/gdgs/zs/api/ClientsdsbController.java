package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.AddsdsbService;
import gov.gdgs.zs.service.ClientsdsbService;
import gov.gdgs.zs.service.IAddsdsbService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;

@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class ClientsdsbController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private IAddsdsbService iaddsdsbService;

	@Autowired
	private AddsdsbService addsdsbService;
	@Autowired
	private ClientsdsbService clientsdsbService;

	/*
	 * 客户端获取行业人员统计表
	 */
	@RequestMapping(value = "/client/hyryqktjb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getHyryqktjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) throws Exception {
		User user = accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(clientsdsbService.getHyryqktjb(page,
				pageSize, user.getJgId(), where), HttpStatus.OK);
	}

	/*
	 * 客户端获取行业人员情况统计表明细
	 */
	@RequestMapping(value = "/client/hyryqktjb/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getHyryqktjbById(
			@PathVariable("id") String id) {
		Map<String, Object> obj = clientsdsbService.getHyryqktjbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端添加行业人员情况统计表
	 */
	@RequestMapping(value = "/addhyryqktjb", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addHyryqktjb(
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}
		return new ResponseEntity<>(clientsdsbService.AddHyryqktjb(obj),
				HttpStatus.CREATED);
	}

	/*
	 * 客户端修改行业人员情况统计表
	 */
	@RequestMapping(value = "/addhyryqktjb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateHyryqktjb(
			@PathVariable("id") String id,
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}
		clientsdsbService.UpdateHyryqktjb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),
				HttpStatus.OK);

	}

	/*
	 * 客户端判断是否可添加行业人员情况统计表
	 */
	@RequestMapping(value = "/add/hyryqktjbok/{jg_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getOk(
			@PathVariable("jg_id") String jgid) {

		Map<String, Object> obj = clientsdsbService.getOK(jgid);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端获取经营收入统计表
	 */
	@RequestMapping(value = "/add/jysrqkb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJysrqkb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where,
			HttpServletRequest request) throws Exception {
		User user = accountService.getUserFromHeaderToken(request);

		return new ResponseEntity<>(clientsdsbService.getJysrqkb(page,
				pageSize, user.getJgId(), where), HttpStatus.OK);
	}

	/*
	 * 客户端获取经营收入情况表明细
	 */
	@RequestMapping(value = "/add/jysrqkb/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJysrqkbById(
			@PathVariable("id") String id) {

		Map<String, Object> obj = clientsdsbService.getJysrqkbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端添加经营收入情况表
	 */
	@RequestMapping(value = "/addjysrqkb", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addJysrqkb(
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}

		return new ResponseEntity<>(clientsdsbService.AddJysrqkb(obj),
				HttpStatus.CREATED);
	}

	/*
	 * 客户端修改经营收入情况表
	 */
	@RequestMapping(value = "/addjysrqkb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateJysrqkb(
			@PathVariable("id") String id,
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}

		clientsdsbService.UpdateJysrqkb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),
				HttpStatus.OK);

	}

	/*
	 * 客户端获取上年经营收入表数据
	 */
	@RequestMapping(value = "/add/upyear1/{jg_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUpyear1(
			@PathVariable("jg_id") String jgid) {

		Map<String, Object> obj = clientsdsbService.getUpyear(jgid);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端获取鉴证业务表上年数据
	 */
	@RequestMapping(value = "/add/upyear/{jg_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUpyear(
			@PathVariable("jg_id") String jgid) {

		Map<String, Object> obj = addsdsbService.getUpyear(jgid);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	

	/*
	 * 客户端获取事务所基本情况表
	 */
	@RequestMapping(value = "/client /swsjbqk", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getSwsjbqk(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where)
			throws Exception {
		User user = accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(addsdsbService.getSwsjbqkb(page, pageSize,
				user.getJgId(), user.getId(), where), HttpStatus.OK);
	}

	/*
	 * 客户端获取事务所基本情况表明细
	 */
	@RequestMapping(value = "/client/swsjbqk/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getSwsjbqkById(
			@PathVariable("id") String id) {

		Map<String, Object> obj = addsdsbService.getSwsjbqkbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	/*
	 * 客户端添加事务所基本情况表
	 */
	@RequestMapping(value = "/client/swsjbqk", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addSwsjbb(
			@RequestBody Map<String, Object> obj){
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		return new ResponseEntity<>(addsdsbService.AddSwsjbqkb(obj),
				HttpStatus.CREATED);
	}

	/*
	 * 客户端获取填报基本情况表所需基本信息
	 */
	@RequestMapping(value = "/client/swsjbqkinit", method = RequestMethod.GET)
	public ResponseEntity<?> getSwsjbqkInit() {

		User user = accountService.getUserFromHeaderToken(request);
		Map<String, Object> obj = addsdsbService.getSwsjbqkInit(user);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端修改事务所基本情况表
	 */
	@RequestMapping(value = "/client/swsjbqk/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateSwsjbb(
			@PathVariable("id") String id,
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}

		addsdsbService.UpdateSwsjbqkb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),
				HttpStatus.OK);

	}

	/*
	 * 客户端获取经营规模表
	 */
	@RequestMapping(value = "/add/jygmtjb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJygmtjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where,
			HttpServletRequest request) throws Exception {
		User user = accountService.getUserFromHeaderToken(request);
		Map<String, Object> obj = addsdsbService.getJygmtjb(page, pageSize,
				user.getId(), user.getJgId(), where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端获取经营规模表明细
	 */
	@RequestMapping(value = "/add/jygmtjb/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJygmtjbById(
			@PathVariable("id") String id) {

		Map<String, Object> obj = addsdsbService.getJygmtjbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端获取经营规模表上年数据
	 */
	@RequestMapping(value = "/add/jygmtjbok/{jg_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getOk1(
			@PathVariable("jg_id") String jgid) {

		Map<String, Object> obj = addsdsbService.getOK1(jgid);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端添加经营规模表
	 */
	@RequestMapping(value = "/addjygmtjb", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addJygmtjb(
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}
		Map<String, Object> rs = iaddsdsbService.AddJygmtjb(obj);
		return new ResponseEntity<>(rs, HttpStatus.CREATED);
	}

	/*
	 * 客户端修改经营规模表
	 */
	@RequestMapping(value = "/addjygmtjb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateJygmtjb(
			@PathVariable("id") String id,
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}

		addsdsbService.UpdateJygmtjb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),
				HttpStatus.OK);

	}

	/*
	 * 客户端获取鉴证业务情况统计表
	 */
	@RequestMapping(value = "/add/jzywqktjb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJzywqktjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where,
			HttpServletRequest request) throws Exception {
		User user = accountService.getUserFromHeaderToken(request);
		Map<String, Object> obj = addsdsbService.getJzywqktjb(page, pageSize,
				user.getId(), user.getJgId(), where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端获取鉴证业务表的明细
	 */
	@RequestMapping(value = "/add/jzywqktjb/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJzywqktjbById(
			@PathVariable("id") String id) {

		Map<String, Object> obj = addsdsbService.getJzywqktjbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端添加鉴证业务表
	 */
	@RequestMapping(value = "/addjzywqktjb", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addJzywqktjb(
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}
		Map<String, Object> rs = iaddsdsbService.AddJzywqktjb(obj);
		return new ResponseEntity<>(rs, HttpStatus.CREATED);
	}

	/*
	 * 客户端修改鉴证业务表
	 */
	@RequestMapping(value = "/addjzywqktjb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateJzywqktjb(
			@PathVariable("id") String id,
			@RequestBody Map<String, Object> obj, HttpServletRequest request)
			throws Exception {
		try {
			User user = accountService.getUserFromHeaderToken(request);
			obj.put("use_id", user.getId());
			obj.put("jg_id", user.getJgId());
		} catch (Exception e) {
		}

		addsdsbService.UpdateJzywqktjb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),
				HttpStatus.OK);

	}

}
