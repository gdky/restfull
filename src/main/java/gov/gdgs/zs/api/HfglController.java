package gov.gdgs.zs.api;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.HfglService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class HfglController {

	@Resource
	private HfglService hfglService;
	@Resource
	AccountService accountService;
	/**
	 * 会员会费缴纳情况
	 * @param pn
	 * @param ps
	 * @param where
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhfjyqk", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> hyhfjnqk(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where) throws Exception  {
		
		return new ResponseEntity<>(hfglService.hyhfjnqk(pn, ps, where),HttpStatus.OK);

	}
	/**
	 * 费用统计
	 * @param where
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fytj", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> fytj(
			@RequestParam(value="where", required=false) String where) throws Exception  {
		
		return new ResponseEntity<>(hfglService.fytj(where),HttpStatus.OK);
		
	}
	/**
	 * 发票打印查询
	 * @param pn
	 * @param ps
	 * @param where
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fpdy", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> fpdy(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where,HttpServletRequest request) throws Exception  {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.fpdy(pn, ps, where, user.getNames()),HttpStatus.OK);
		
	}
	
	/**
	 * 发票金额分配
	 * @param jlid
	 * @param fptj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fpdy/ttgrfyfp/{jlid}", method = RequestMethod.PUT)
	public ResponseEntity<?> ttgefp(@PathVariable(value = "jlid") String jlid,
			@RequestBody Map<String, Object> fptj,HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.ttgefp(jlid, fptj, user.getNames()),HttpStatus.OK);
	}
	/**
	 * 发票金额修改
	 * @param jlid
	 * @param fptj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fpdy/fpjexg/{jlid}", method = RequestMethod.PUT)
	public ResponseEntity<?> fpjexg(@PathVariable(value = "jlid") String jlid,
			@RequestBody Map<String, Object> fptj,HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.fpjexg(jlid, fptj, user.getNames()),HttpStatus.OK);
	}
	
	
	/**
	 * 会费缴纳单据提交
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhfjn/jfsc", method = RequestMethod.POST)
	public ResponseEntity<?> upLoadJFSC(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.upLoadJFSC(file,user.getNames()),HttpStatus.CREATED);
	}
	
	/**
	 * 非执业会费缴纳查询
	 * @param pn
	 * @param ps
	 * @param where
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/fzyhfjn", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> fzyhfjn(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where
			,HttpServletRequest request) throws Exception  {
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.fzyhfjn(pn, ps, where,user.getNames()),HttpStatus.OK);
		
	}
	/**
	 * 非执业添加
	 * @param jlid
	 * @param fptj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/fzytj", method = RequestMethod.POST)
	public ResponseEntity<?> fzytj(
			@RequestBody Map<String, Object> fptj,HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.fzytj(fptj, user.getNames()),HttpStatus.OK);
	}
	/**
	 * 非执业修改
	 * @param jlid
	 * @param fptj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/fzyxg/{jlid}", method = RequestMethod.PUT)
	public ResponseEntity<?> fzyxg(@PathVariable(value = "jlid") String jlid,
			@RequestBody Map<String, Object> fptj,HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.fzyxg(jlid, fptj, user.getNames()),HttpStatus.OK);
	}
	/**
	 * 非执业删除
	 * @param jlid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/fzyDel/{jlid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> fzyDel(@PathVariable(value = "jlid") String jlid,
			HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.fzyDel(jlid, user.getNames()),HttpStatus.OK);
	}
	/**
	 * 非执业批量上传
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/fzyhyhfplsc", method = RequestMethod.POST)
	public ResponseEntity<?> upLoadfzy(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception{
		User user =  accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(hfglService.upLoadfzy(file,user.getNames()),HttpStatus.CREATED);
	}
	/**
	 * 发票打印累加
	 * @param jlid
	 * @param fptj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/fpdy/fpdylj", method = RequestMethod.PUT)
	public ResponseEntity<?> fpdylj(
			@RequestBody Object fptj) throws Exception{
		return new ResponseEntity<>(hfglService.fpdylj(fptj),HttpStatus.OK);
	}
	/**
	 * 上传管理查询
	 * @param pn
	 * @param ps
	 * @param where
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/scglcx", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> scglcx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where) throws Exception  {
		
		return new ResponseEntity<>(hfglService.scglcx(pn, ps, where),HttpStatus.OK);
		
	}
	/**
	 * 上传管理操作
	 * @param jlid
	 * @param fptj
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hyhf/scglcx/put", method = RequestMethod.PUT)
	public ResponseEntity<?> scjlglcz(
			@RequestBody Map<String,Object> fptj) throws Exception{
		return new ResponseEntity<>(hfglService.scjlglcz(fptj),HttpStatus.OK);
	}
}
