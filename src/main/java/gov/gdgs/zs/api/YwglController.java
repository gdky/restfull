package gov.gdgs.zs.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.YwglService;

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

/**
 * 业务管理API controller
 * 
 * @author ming
 *
 */

@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class YwglController {

	@Resource
	private YwglService ywglService;
	
	@Autowired
	private AccountService accountService;


	/**
	 * 获取有效的业务报备
	 */
	@RequestMapping(value = "/ywbb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getYwbb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {

		Map<String, Object> obj = ywglService.getYwbb(page, pagesize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/**
	 * 获取收费金额预警的业务报备列表
	 */
	@RequestMapping(value = "/ywbbsfjeyj", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getYwbbSFJEYJ(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {

		Map<String, Object> obj = ywglService.getYwbbSFJEYJ(page, pagesize,
				where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/**
	 * 获取年度不同预警的业务报备列表
	 */
	@RequestMapping(value = "/ywbbndbtyj", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbNDBTYJ(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {

		Map<String, Object> obj = ywglService.getYwbbNDBTYJ(page, pagesize,
				where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/**
	 * 获取业务委托方变更预警列表
	 */
	@RequestMapping(value = "/ywbbwtfyj", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getYwbbWTFYJ(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {

		Map<String, Object> obj = ywglService.getYwbbWTFYJ(page, pagesize,
				where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 修改业务报备信息
	 */
	@RequestMapping(value = "/ywbb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> getYwbb(@RequestBody Map<String, Object> map,
			@PathVariable String id) {
		ywglService.updateYwbb(id, map);
		ResponseMessage rm = new ResponseMessage(ResponseMessage.Type.success,
				"200", "更新成功");
		return new ResponseEntity<>(rm, HttpStatus.OK);
	}

	/*
	 * 获取业务报备细节信息
	 */
	@RequestMapping(value = "/ywbb/{hash}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getYwbbById(
			@PathVariable("hash") String hash) {

		Map<String, Object> obj = ywglService.getYwbbById(hash);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端用业务报备查询
	 */
	@RequestMapping(value = "/jgyw/{hashId}", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbByJg(@PathVariable("hashId") String hashId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = ywglService.getYwbbByJg(hashId, page,
				pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端用机构关联执业税务师和机构信息
	 */
	@RequestMapping(value = "/ywbbmisc/{jgHashid}", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbMiscByJg(
			@PathVariable("jgHashid") String jgHashid) {
		Map<String, Object> obj = ywglService.getYwbbMiscByJg(jgHashid);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	/*
	 * 客户端提交业务报备信息
	 */
	@RequestMapping(value = "/ywbb", method = RequestMethod.POST)
	public  ResponseEntity<Map<String,Object>> addYwbb(
			@RequestBody Map<String,Object> values,
			HttpServletRequest request){ 
		
		User user =  accountService.getUserFromHeaderToken(request);
		Map<String,Object> obj = ywglService.addYwbb(values,user);
		return new ResponseEntity<>(obj,HttpStatus.CREATED);
	}
	
	/**
	 * 客户端-个人业务统计
	 * @param where
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ywgl/grywtj", method = RequestMethod.GET)
	public ResponseEntity<?> getGrywtj(
			@RequestParam(value = "where", required = false) String where,
			HttpServletRequest request) {
	    String jgid=accountService.getUserFromHeaderToken(request).getJgId().toString();
		Map<String, Object> map = ywglService.getGrywtj(jgid,where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}


	/**
	 * 客户端-事务所业务统计
	 * @param page
	 * @param pagesize
	 * @param where
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ywgl/swsywtj", method = RequestMethod.GET)
	public ResponseEntity<?> getSwsywtj(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where,
			HttpServletRequest request) {
	    String jgid=accountService.getUserFromHeaderToken(request).getJgId().toString();
		Map<String, Object> map = ywglService.getSwsywtj(jgid,page,pagesize,where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
    /**
     * 客户端-事务所业务统计-明细
     * @param ywlx
     * @param bbnd
     * @param where
     * @param request
     * @return
     */
	@RequestMapping(value = "/ywgl/swsywtj/{ywlx}", method = RequestMethod.GET)
	public ResponseEntity<?> getSwsywtjMx(
			@PathVariable("ywlx") String ywlx,
			@RequestParam(value = "bbnd", required = true) String bbnd,
			@RequestParam(value="where",required=false) String where,
			HttpServletRequest request) {
		String jgid=accountService.getUserFromHeaderToken(request).getJgId().toString();
		Map<String, Object> map = ywglService.getSwsywtjMx(ywlx,bbnd,jgid,where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjfx", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjfx(
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjfx(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjfx/dq", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjfxDq(
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjfxDq(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjfx/sws", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjfxSws(
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjfxSws(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjfx/ywlx", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjfxYwlx(
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjfxYwlx(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据汇总-机构人员报备数据分析
	 * 
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjhz/ry", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjhzRy(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjhzRy(page, pagesize,
				where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据汇总-机构人员报备数据分析-根据业务报备表的id查找报备数据明细
	 * 
	 * @param bbid
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjhz/ry/{bbid}", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjhzRyMx(@PathVariable("bbid") String bbid) {
		Map<String, Object> map = ywglService.getYwbbsjhzRyMx(bbid);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据汇总-机构人员报备数据分析-报备机构
	 * 
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjhz/ry/ywbbjg", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjhzYwbbJg(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjhzYwbbJg(page, pagesize,
				where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据汇总-会计所报备数据分析
	 * 
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjhz/sws", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjhzSws(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjhzSws(page, pagesize,
				where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据汇总-外省报备数据分析
	 * 
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjhz/ws", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjhzWs(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjhzWs(page, pagesize,
				where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 中心端-业务报备数据汇总-业务报备总收费金额数据分析
	 * 
	 * @param page
	 * @param pagesize
	 * @param where
	 * @return
	 */
	@RequestMapping(value = "/ywgl/ywbbsjhz/je", method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbsjhzJe(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> map = ywglService.getYwbbsjhzJe(page, pagesize,
				where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
