package perseus.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import perseus.Application;
import perseus.utils.ParameterStringBuilder;

@Controller
//@SessionAttributes({"access_token"})
public class Cop26AnalysisController {
	
	@RequestMapping(value = "/cop26dashboard", method = RequestMethod.GET)
	public String getCop26Dashboard(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") == null) {
//			URL url = new URL("http://134.122.102.92:8000/authenticate");
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			con.setRequestMethod("POST");
//			
//			con.setConnectTimeout(5000);
//			con.setDoOutput(true);
//			
//			Properties prop = new Properties();
//			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("access.properties");
//			prop.load(inputStream);
//			
//			Map<String, String> parameters = new HashMap<>();
//			parameters.put("username", prop.getProperty("username"));
//			parameters.put("password", prop.getProperty("password"));
//			
//			DataOutputStream out = new DataOutputStream(con.getOutputStream());
//			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//			out.flush();
//			out.close();
//			
//			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer content = new StringBuffer();
//			
//			while ((inputLine = in.readLine()) != null) {
//			    content.append(inputLine);
//			}
//			
//			JSONObject json = new JSONObject(content.toString());
//			
//			in.close();
//			
//			if(json != null) {
//				model.addAttribute("access_token", json.get("access_token"));
//			}
//		}
//		
		model.addAttribute("authenticated", true);
		
		return "cop26_direct";
	}
	
	@RequestMapping(value = "/getCop26VizAuth", method = RequestMethod.GET)
	public String getCop26VizAuthorizations(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") == null) {
//			URL url = new URL("http://134.122.102.92:8000/authenticate");
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			con.setRequestMethod("POST");
//			
//			con.setConnectTimeout(5000);
//			con.setDoOutput(true);
//			
//			Properties prop = new Properties();
//			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("access.properties");
//			prop.load(inputStream);
//			
//			Map<String, String> parameters = new HashMap<>();
//			parameters.put("username", prop.getProperty("username"));
//			parameters.put("password", prop.getProperty("password"));
//			
//			DataOutputStream out = new DataOutputStream(con.getOutputStream());
//			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//			out.flush();
//			out.close();
//			
//			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer content = new StringBuffer();
//			
//			while ((inputLine = in.readLine()) != null) {
//			    content.append(inputLine);
//			}
//			
//			JSONObject json = new JSONObject(content.toString());
//			
//			in.close();
//			
//			if(json != null) {
//				model.addAttribute("access_token", json.get("access_token"));
//			}
//		}
//		
		model.addAttribute("authenticated", true);
		
		return String.format("fragments/cop26-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getEUMentions", method = RequestMethod.GET)
	public String getCop26Mentions(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_eu_mentions", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getEUMentions.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getClimatePolicySentiment", method = RequestMethod.GET)
	public String getClimatePolicySentiment(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_climate_policy_sentiment", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getClimatePolicySentiment.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getClimateChangeSentiment", method = RequestMethod.GET)
	public String getClimateChangeSentiment(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_climate_change_sentiment", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getClimateChangeSentiment.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDataVolumes", method = RequestMethod.GET)
	public String getDataVolumes(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_data_volumes", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getDataVolumes.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getTopicsData", method = RequestMethod.GET)
	public String getTopicData(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_topics_data", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getTopicsData.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getEuMentionsOverTime", method = RequestMethod.GET)
	public String getEuMentionsOverTime(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_eu_mentions_over_time", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getEuMentionsOverTime.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getClimatePolicySentimentOverTime", method = RequestMethod.GET)
	public String getClimatePolicySentimentOverTime(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_climate_policy_sentiment_over_time", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getClimatePolicySentimentOverTime.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getClimateChangeSentimentOverTime", method = RequestMethod.GET)
	public String getClimateChangeSentimentOverTime(Model model) throws IOException, ParseException {
		
//		if(model.asMap().get("access_token") != null) {
//			
//			return postCall("http://134.122.102.92:8000/get_climate_change_sentiment_over_time", new JSONObject("{access_token: " + model.asMap().get("access_token").toString() + "}")).toString();
//		}
		
		JSONArray obj = null;
		try (InputStream input = new FileInputStream(Application.DATA + "/getClimateChangeSentimentOverTime.json")) {
		    obj = new JSONArray(new JSONTokener(input));
		}
		
		return obj.toString();
	}
	
//	private JSONArray postCall(String urlString, JSONObject jsonInput) throws IOException {
//		
//		URL url = new URL(urlString);
//		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		
//		con.setRequestMethod("POST");
//		con.setRequestProperty("Content-Type", "application/json; utf-8");
//		con.setDoOutput(true);
//		
//		Properties prop = new Properties();
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("access.properties");
//		prop.load(inputStream);
//		
//		try(OutputStream os = con.getOutputStream()) {
//		    byte[] input = jsonInput.toString().getBytes("utf-8");
//		    os.write(input, 0, input.length);
//		}
//		
//		JSONArray json = null;
//		
//		try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
//		    StringBuilder response = new StringBuilder();
//		    String responseLine = null;
//		    
//		    while ((responseLine = br.readLine()) != null) {
//		        response.append(responseLine.trim());
//		    }
//		    
//		    //System.out.println(response.toString());
//		    json = new JSONArray(response.toString());
//		}
//		
//		return json;
//	}

}
