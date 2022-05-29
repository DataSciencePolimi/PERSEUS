package perseus.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.json.*;

import perseus.data.DataCollection;

@Controller
public class CaseDataVizController {
	
	private final DataCollection dataCollection;
	
	@Autowired
	public CaseDataVizController(DataCollection dataCollection) {
		this.dataCollection = dataCollection;
	}
	
	@RequestMapping(value = "/getCaseViz", method = RequestMethod.GET)
	public String getCovidVizFragment(Model model, @RequestParam(name = "narrative") int narrative,
			@RequestParam(name = "currentScenario", required = false, defaultValue = "0") String currentScenario) {
		
		List<String[]> data = dataCollection.getNarrativeScenarios(narrative);
		
		model.addAttribute("scenarios", data);
		if(currentScenario.equals("0")) {
			model.addAttribute("active", data.get(0)[0]);
		} else {
			model.addAttribute("active",currentScenario);
		}
		model.addAttribute("narrative", narrative);
		model.addAttribute("title", dataCollection.getNarrative(narrative).get(0)[1]);
		
		return String.format("fragments/case-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMostUsedKeywords", method = RequestMethod.GET)
	public String getMostUsedKeywords(Model model, @RequestParam(name = "idScenario") String idScenario) {
		List<Object[]> data = dataCollection.getTopKeywords(Integer.parseInt(idScenario));
		JSONArray results = new JSONArray();

		for(Object[] d : data){
			JSONObject obj = new JSONObject();
			obj.put("size", Integer.parseInt(d[2].toString()));
			obj.put("text", d[1].toString());
			results.put(obj);
			obj = new JSONObject();
		}

		return results.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/getMostUsedDescriptionWords", method = RequestMethod.GET)
	public String getMostUsedDescriptionWords(Model model, @RequestParam(name = "idScenario")String idScenario) throws NumberFormatException, IOException {
		List<Object[]> data = dataCollection.getDescriptionWords(Integer.parseInt(idScenario));
		JSONArray results = new JSONArray();

		for(Object[] d : data){
			JSONObject obj = new JSONObject();
			obj.put("size", Integer.parseInt(d[1].toString()));
			obj.put("text", d[0].toString());
			results.put(obj);
			obj = new JSONObject();
		}

		return results.toString();
	}
}
