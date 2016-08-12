package gov.gdgs.zs.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HtmlController {

	@RequestMapping(value = "/html/jzbgfm", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("name","gdzs");
		model.addAttribute("age","25");
		return "index.html";
	}
}
