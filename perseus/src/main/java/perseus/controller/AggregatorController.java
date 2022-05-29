package perseus.controller;

import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AggregatorController {

    @RequestMapping(value = "/getRepositoryFragment", method = RequestMethod.GET)
    public String getRepositoryFragment(Model model){
        return String.format("fragments/aggregator-repo-fragment :: content(url='%s')", "/?lang=en");
    }
    
    @RequestMapping(value = "/getDeepDivesFragment", method = RequestMethod.GET)
    public String getDeepDivesFragment(Model model){
        return String.format("fragments/deep-dives-fragment :: content(url='%s')", "/?lang=en");
    }
    
    @RequestMapping(value = "/getTechnologyFragment", method = RequestMethod.GET)
    public String getTechnologyFragment(Model model){
        return String.format("fragments/technology-fragment :: content(url='%s')", "/?lang=en");
    }
}
