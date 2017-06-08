package gov.gdgs.zs.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.CheckingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class CheckingController {

	@Resource
	private CheckingService chService;
	@Resource
	AccountService accountService;
	/**
	 * 判断审批中
	 * @param jgid
	 * @param splx
	 * @return
	 */
	@RequestMapping(value = "/commont/checksping/{splx}/{id}", method = { RequestMethod.GET })
	public ResponseEntity<?> checkSPing(@PathVariable(value = "id") String jgid,
			@PathVariable(value = "splx") String splx) {
		return new ResponseEntity<>(chService.checkSPing(splx,jgid),HttpStatus.OK);
	}
	/**
	 * 判断能否进行合并
	 * @param jgid
	 * @param splx
	 * @return
	 */
	@RequestMapping(value = "/commont/checksping/doFix", method = { RequestMethod.GET })
	public ResponseEntity<?> checkDoFix(
			@RequestParam(value="sumbitValue", required=true) String sumbitValue,HttpServletRequest request) {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(chService.checkDoFix(sumbitValue,user.getJgId(),user.getUsername()),HttpStatus.OK);
	}
	/**
	 * 判断能否进行注销
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/commont/checksping/checkBeforeDelete", method = { RequestMethod.GET })
	public ResponseEntity<?> checkBeforeDelete(HttpServletRequest request) {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(chService.checkBeforeDelete(user.getUsername()),HttpStatus.OK);
	}
	/**
	 * 判断机构自身审批中
	 * @param jgid
	 * @param splx
	 * @return
	 */
	@RequestMapping(value = "/commont/checksping/jgSelf", method = { RequestMethod.GET })
	public ResponseEntity<?> checkJGSPing(HttpServletRequest request) {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(chService.checkJGSPingSelf(user.getJgId()),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/commont/checksping/checkSWSSP", method = { RequestMethod.GET })
	public ResponseEntity<?> checkSWSSPing(HttpServletRequest request) {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(chService.checkSWSSPing(user.getJgId()),HttpStatus.OK);
	}
	/**
	 * 判断机构是否可以设立分所
	 * @param jgid
	 * @param splx
	 * @return
	 */
	@RequestMapping(value = "/commont/checksping/jgfsssl", method = { RequestMethod.GET })
	public ResponseEntity<?> checkJGFSSL(HttpServletRequest request) {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(chService.checkJGFssl(user.getJgId()),HttpStatus.OK);
	}
	/**
	 * 判断身份证号码是否重复
	 * @param sfzh
	 * @return
	 */
	@RequestMapping(value = "/commont/checksfzh/{sfzh}", method = { RequestMethod.GET })
	public ResponseEntity<?> checkSFZH(@PathVariable(value = "sfzh") String sfzh) {
		return new ResponseEntity<>(chService.checkSFZH(sfzh),HttpStatus.OK);
	}
	/**
	 * 判断是否驳回
	 * @param spid
	 * @return
	 */
	@RequestMapping(value = "/commont/checkisbh/{spid}", method = { RequestMethod.GET })
	public ResponseEntity<?> checkIsBH(@PathVariable(value = "spid") String spid) {
		return new ResponseEntity<>(chService.checkIsBH(spid),HttpStatus.OK);
	}
	/**
	 * 检查是否可以提交报表
	 * @param obj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commont/checiftjbb/{bblx}", method = RequestMethod.GET)
	public ResponseEntity<?> addLrfpb(
			@PathVariable(value = "bblx") String bblx,
			@RequestParam(value = "checked", required = true) String checked,
			HttpServletRequest request)
			throws Exception {
			User user = accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(chService.checkIfTJBB(bblx, user.getJgId(),checked), HttpStatus.OK);
	}
}
