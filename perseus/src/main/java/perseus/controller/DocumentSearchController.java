package perseus.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import perseus.domain.Document;
import perseus.utils.ElasticSearchConnector;
import perseus.utils.ElasticSearchResultParser;
import perseus.utils.ReadPropertyFiles;

@Controller
@SessionAttributes("logged")
public class DocumentSearchController {
	
	private final ElasticSearchConnector elasticSearchConnector;
	private final ApplicationContext context;
	private ReadPropertyFiles readPropertyFiles;
	
	@Autowired
	public DocumentSearchController(ElasticSearchConnector elasticSearchConnector, ApplicationContext context, ReadPropertyFiles readPropertyFiles) {
		this.elasticSearchConnector = elasticSearchConnector;
		this.context = context;
		this.readPropertyFiles = readPropertyFiles;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(Model model, @RequestParam("username") String username, @RequestParam("password") String password) throws IOException {
		
		boolean logged = elasticSearchConnector.checkCredentials(username, password, context);
		
		model.addAttribute("logged", logged);
		
		if(logged) {
			return "redirect:/getDocumentUploadFragment";
		}

		model.addAttribute("loginError", "Incorrect Login Credentials");
		return String.format("fragments/login-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getDocumentSearchFragment", method = RequestMethod.GET)
	public String getDocumentSearchFragment(Model model) throws IOException {
		if(!model.containsAttribute("logged")) {
			model.addAttribute("logged", false);
		}

		Properties prop = readPropertyFiles.readPropFile("properties/document_features.properties");
        
        model.addAttribute("categories", prop.getProperty("categories").split(", "));
		model.addAttribute("sectors", prop.getProperty("sectors").split(", "));
        model.addAttribute("documents", prop.getProperty("doc-resources").split(", "));
        model.addAttribute("webpages", prop.getProperty("web-resources").split(", "));
        model.addAttribute("any", prop.getProperty("any-resources").split(", ")[0]);
        model.addAttribute("licenses", prop.getProperty("licenses").split(", "));

		return String.format("fragments/document-search-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/getDocumentUploadFragment", method = RequestMethod.GET)
	public String getDocumentUploadFragment(Model model, @ModelAttribute("logged") boolean logged) throws IOException {
		
		if(!logged) {
			return String.format("fragments/login-fragment :: content(url='%s')", "/?lang=en");
		}
		
		Properties prop = readPropertyFiles.readPropFile("properties/document_features.properties");
            
        model.addAttribute("categories", prop.getProperty("categories").split(", "));
		model.addAttribute("sectors", prop.getProperty("sectors").split(","));
        model.addAttribute("documents", prop.getProperty("doc-resources").split(", "));
        model.addAttribute("webpages", prop.getProperty("web-resources").split(", "));
        model.addAttribute("licenses", prop.getProperty("licenses").split(", "));
		
		return String.format("fragments/document-upload-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/simpleSearchQuery", method = {RequestMethod.POST, RequestMethod.GET})
	public String simpleSearchQuery(Model model, @RequestParam("queryText") String queryText) throws IOException, ParseException {
		
		SearchHit[] results = elasticSearchConnector.simpleSearch(queryText);
		
		ArrayList<Document> parsedResults = ElasticSearchResultParser.parseElasticResult(results);
		
		model.addAttribute("queryResults", parsedResults);
		model.addAttribute("totalResultsCount", parsedResults.size());
		
		return String.format("fragments/document-search-result-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/documentSearchQuery", method = {RequestMethod.POST, RequestMethod.GET})
	public String documentSearchQuery(Model model, @RequestParam(name = "documentTitle", required = false, defaultValue = "") String documentTitle,
			@RequestParam(name = "documentAuthor", required = false, defaultValue = "") String documentAuthor,
		    @RequestParam(name = "documentKeywords", required = false, defaultValue = "") String documentKeywords,
			@RequestParam(name = "documentText", required = false, defaultValue = "") String documentText,
		    @RequestParam(name = "documentLicense", required = false, defaultValue = "") String documentLicense,
			@RequestParam(name = "documentResource", required = false, defaultValue = "") String documentResource,
		    @RequestParam(name = "category", required = false, defaultValue = "") String categories,
			@RequestParam(name = "sector", required = false, defaultValue = "") String policySector) throws IOException, ParseException {

		SearchHit[] results = elasticSearchConnector.advancedSearch(documentTitle,
				documentAuthor, documentKeywords, documentText, documentLicense, documentResource, categories, policySector);
		
		ArrayList<Document> parsedResults = ElasticSearchResultParser.parseElasticResult(results);
		
		model.addAttribute("queryResults", parsedResults);
		model.addAttribute("totalResultsCount", parsedResults.size());
		
		return String.format("fragments/document-search-result-fragment :: content(url='%s')", "/?lang=en");
	}
	
	@RequestMapping(value = "/documentUpload", method = {RequestMethod.POST, RequestMethod.GET})
	public String documentUploadQuery(Model model, @RequestParam("documentTitle") String documentTitle, @RequestParam("documentAuthors") String documentAuthors,
			@RequestParam("publicationDate") String publicationDate, @RequestParam("documentInstitution") String documentOwner, @RequestParam("documentKeywords") String documentKeywords,
			@RequestParam("documentLicense") String documentLicense, @RequestParam("documentResource") String documentResource, @RequestParam(name = "downloadLink", required = false, defaultValue = "NULL") String downloadLink, 
			@RequestParam("documentFile") MultipartFile documentFile, @RequestParam(name = "websiteLink", required = false, defaultValue = "NULL") String websiteLink,
			@RequestParam("abstract") String document_abstract, @RequestParam("confirmUpload") boolean checkedUpload,
		    @RequestParam(value = "category", required = false, defaultValue = "NULL") String categories, @RequestParam(value = "sector", required = false, defaultValue = "NULL") String sectors,
			@RequestParam("documentExternalReference") String externalReference) throws IOException {
		
		if(checkedUpload) {
		
			JSONObject documentInfo = new JSONObject();
			documentInfo.put("title", documentTitle);
			documentInfo.put("abstract", document_abstract);
			documentInfo.put("publish_date", publicationDate);
			
			JSONArray authorsArray = removeWhiteSpaces(documentAuthors.split(","));
			documentInfo.put("authors", authorsArray);
			
			JSONArray keywordArray = removeWhiteSpaces(documentKeywords.split(","));
			documentInfo.put("keywords", keywordArray);

			if(!categories.equals("NULL")) {
				JSONArray categoriesArray = removeWhiteSpaces(categories.split(","));
				documentInfo.put("categories", categoriesArray);
				System.out.println(categoriesArray.toString());
			}

			if(!sectors.equals("NULL")){
				String[] selectedSectors = sectors.split(",");
				documentInfo.put("policy_sectors", selectedSectors);
			}

			documentInfo.put("institution", documentOwner);
			documentInfo.put("license", documentLicense);
			documentInfo.put("resource_type", documentResource);
			documentInfo.put("link_to_external", externalReference);
			
			if(!documentFile.isEmpty() && !downloadLink.equals("NULL") && websiteLink.equals("NULL")) {
				//Document
				documentInfo.put("link_to_original", downloadLink);
				
				elasticSearchConnector.uploadDocument(documentFile, documentInfo);
				
			} else if(!websiteLink.equals("NULL") && documentFile.isEmpty() && downloadLink.equals("NULL")) {
				//Website
				documentInfo.put("link_to_original", websiteLink);
				
				elasticSearchConnector.uploadWebsite(documentInfo);
			}
		}
		
		return String.format("fragments/document-search-fragment :: content(url='%s')", "/?lang=en");
	}

	private JSONArray removeWhiteSpaces(String[] tokens){
		JSONArray tokensArray = new JSONArray();
		for(String token : tokens) {
			String tokenTmp = token;

			if(tokenTmp.indexOf(" ") == 0) {
				tokenTmp = tokenTmp.substring(1);
			}

			tokensArray.put(tokenTmp);
		}
		return tokensArray;
	}

	@RequestMapping(value = "/getDeepDivesDocs", method = {RequestMethod.POST, RequestMethod.GET})
	public String getDeepDivesDocs(Model model) throws IOException, ParseException {
		String keywords = "deep dives, eu";
		String resourceType = "document";
		String category = "foresight";
		String sectors = "data protection, climate, eu, international governance, health";

		SearchHit[] results = elasticSearchConnector.advancedSearch("",
				"", keywords, "", "", resourceType, category, sectors);

		ArrayList<Document> parsedResults = ElasticSearchResultParser.parseElasticResult(results);

		model.addAttribute("queryResults", parsedResults);
		model.addAttribute("totalResultsCount", parsedResults.size());

		return String.format("fragments/document-search-result-fragment :: content(url='%s')", "/?lang=en");
	}


	@RequestMapping(value = "/getTechnologyDocs", method = {RequestMethod.POST, RequestMethod.GET})
	public String getTechnologyDocs(Model model) throws IOException, ParseException {
		String keywords = "eu";
		String resourceType = "document";
		String category = "foresight";
		String sectors = "technology";

		SearchHit[] results = elasticSearchConnector.advancedSearch("",
				"", keywords, "", "", resourceType, category, sectors);

		ArrayList<Document> parsedResults = ElasticSearchResultParser.parseElasticResult(results);

		model.addAttribute("queryResults", parsedResults);
		model.addAttribute("totalResultsCount", parsedResults.size());

		return String.format("fragments/document-search-result-fragment :: content(url='%s')", "/?lang=en");
	}
}
