package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.AddcwbbService;
import gov.gdgs.zs.service.SwsService;
import gov.gdgs.zs.service.SwsztService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;


@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)

public class SWSZTTJController {

	@Autowired
	private SwsztService swsztservice;
	@RequestMapping(value = "/swszt1", method = { RequestMethod.GET })
	public ResponseEntity<?> getSwszttjb(
			@RequestParam(value = "year", required = true) int year) {
		Map<String, Object> ls = swsztservice.getSwstjb(year);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}

	
}
