package perseus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	@RequestMapping(value = "/getPolicymakerButtonFragment", method = RequestMethod.GET)
    public String getPolicymakerButtonFragment(Model model){
        return String.format("fragments/policymaker-role-fragment :: content(url='%s')", "/?lang=en");
    }
	
	@RequestMapping(value = "/getResearcherButtonFragment", method = RequestMethod.GET)
    public String getResearcherButtonFragment(Model model){
        return String.format("fragments/researcher-role-fragment :: content(url='%s')", "/?lang=en");
    }
	
	@RequestMapping(value = "/getCitizenButtonFragment", method = RequestMethod.GET)
    public String getCitizenButtonFragment(Model model){
        return String.format("fragments/citizen-role-fragment :: content(url='%s')", "/?lang=en");
    }
	
}
