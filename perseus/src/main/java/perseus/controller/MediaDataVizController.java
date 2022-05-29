package perseus.controller;

import java.sql.Blob;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import perseus.data.DataCollection;
import perseus.utils.ImageFinder;

@Controller
public class MediaDataVizController {
	
	@Autowired
	private DataCollection dataCollection;
	
	@RequestMapping(value = "/getPerseusHomeNavbar", method = RequestMethod.GET)
	public String getPerseusHomeNavbar(Model model) {
		return String.format("fragments/index-fragment :: navbar-fragment(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getPerseusHomeTopNavbar", method = RequestMethod.GET)
	public String getPerseusHomeTopNavbar(Model model) {
		return String.format("fragments/index-fragment :: navbar-switch-fragment(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getMediaDataVizFragment", method = RequestMethod.GET)
	public String getMediaDataVizFragment(Model model) {
		return String.format("fragments/media-dataviz-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getEurLexDataVizFragment", method = RequestMethod.GET)
	public String getEurLexDataVizFragment(Model model) {
		return String.format("fragments/eurlex-dataviz-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getCaseStudiesFragment", method = RequestMethod.GET)
	public String getCaseStudiesFragment(Model model) {
		
		List<Object[]> narratives = dataCollection.getNarrative();
		
		for(Object[] narrative : narratives) {
			ImageFinder.findNarrativeImage((String)narrative[0], (Blob)narrative[2]);
		}
		
		model.addAttribute("narratives", narratives);
		
		return String.format("fragments/case-studies-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getPerseusHome", method = RequestMethod.GET)
	public String getPerseusHome(Model model) {
		return String.format("fragments/index-fragment :: default-index-fragment(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getCop26Fragment", method = RequestMethod.GET)
	public String getCop26Fragment(Model model) {
		return String.format("fragments/cop26-fragment :: content(url='%s')", "/?lang=en");
	}
}
