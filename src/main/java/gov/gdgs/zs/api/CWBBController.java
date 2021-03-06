package gov.gdgs.zs.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.dao.CWBBDao;
import gov.gdgs.zs.service.CWBBService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdky.restfull.entity.ResponseMessage;

@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class CWBBController {

	@Resource
	private CWBBService cwbbService;

	// 支出明细表
	@RequestMapping(value = "/cwbb/zcmxb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getZcmxb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getZcmxb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/cwbb/zcmxb/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getZcmxbById(
			@PathVariable("Id") String id) {
		Map<String, Object> obj = cwbbService.getZcmxbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// 利润分配表
	@RequestMapping(value = "/cwbb/lrfpb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLrfpb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getLrfpb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/cwbb/lrfpb/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLrfpbById(
			@PathVariable("Id") String id) {
		Map<String, Object> obj = cwbbService.getLrfpbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// 利润表
	@RequestMapping(value = "/cwbb/lrb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLrb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getLrb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}

	@RequestMapping(value = "/cwbb/lrb/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLrbById(
			@PathVariable("id") String id) {

		Map<String, Object> obj = cwbbService.getLrbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// 现金流量表
	@RequestMapping(value = "/cwbb/xjllb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getXjllb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getXjllb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/cwbb/xjllb/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getXjllbById(
			@PathVariable("Id") String id) {
		Map<String, Object> obj = cwbbService.getXjllbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// 资产负债表
	@RequestMapping(value = "/cwbb/zcfzb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getZcfzb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getZcfzb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}

	@RequestMapping(value = "/cwbb/zcfzb/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getZcfzbById(
			@PathVariable("id") String id) {

		Map<String, Object> obj = cwbbService.getZcfzbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// 鉴证业务统计表
	@RequestMapping(value = "/sdsb/jzywtjb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJzywtjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getJzywtjb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/sdsb/jzywtjb/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJzywtjbById(
			@PathVariable("Id") String id) {
		Map<String, Object> obj = cwbbService.getJzywtjbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// 经营规模统计表
	@RequestMapping(value = "/sdsb/jygmtjb", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJygmtjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where) {
		Map<String, Object> obj = cwbbService.getJygmtjb(page, pageSize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/sdsb/jygmtjb/{Id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getJygmtjbById(
			@PathVariable("Id") String id) {
		Map<String, Object> obj = cwbbService.getJygmtjbById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rjlrb/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjLrb(@PathVariable("id") String id){
		cwbbService.rjLrb(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjlrfpb/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjlrfpb(@PathVariable("id") String id){
		cwbbService.rjlrfpb(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjxjllb/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjxjllb(@PathVariable("id") String id){
		cwbbService.rjxjllb(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjzcfzb/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjzcfzb(@PathVariable("id") String id){
		cwbbService.rjzcfzb(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjzcmxb/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjzcmxb(@PathVariable("id") String id){
		cwbbService.rjzcmxb(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}

}
