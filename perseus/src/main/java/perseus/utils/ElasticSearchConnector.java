package perseus.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ElasticSearchConnector {
	
	private final RestHighLevelClient client;
	private final String[] simpleFields = {"title", "authors", "content", "keywords", "institution"};
	
	
	@Autowired
	public ElasticSearchConnector(RestHighLevelClient client) {
		this.client = client;
	}
	
	public void uploadDocument(MultipartFile documentFile, JSONObject obj) throws IOException {
		
	    String encodedfile = new String(Base64.getEncoder().encodeToString(documentFile.getBytes()));
		
		IndexRequest request = new IndexRequest("perseus_doc_search", "_doc");
		
		request.setPipeline("custom_pdf_processor");
		
		/**
		 * TESTARE BENE
		 */
		request.timeout(TimeValue.timeValueSeconds(20));
		
		request.source("{\n" + "  \"data\": " +"\""+ encodedfile +"\""+ "}", XContentType.JSON);

		IndexResponse response = this.client.index(request, RequestOptions.DEFAULT);
		
		String documentId = response.getId();
		
		UpdateRequest updateRequest = new UpdateRequest("perseus_doc_search", "_doc", documentId);
		
		updateRequest.doc(obj.toString(), XContentType.JSON);
		
		@SuppressWarnings("unused")
		UpdateResponse updateResponse = this.client.update(updateRequest, RequestOptions.DEFAULT);
		
	}
	
	public void uploadWebsite(JSONObject obj) throws IOException {
		IndexRequest request = new IndexRequest("perseus_doc_search", "_doc");
		request.source(obj.toString(), XContentType.JSON);
		@SuppressWarnings("unused")
		IndexResponse response = this.client.index(request, RequestOptions.DEFAULT);
	}
	
	public SearchHit[] simpleSearch(String queryText) throws IOException {
		String[] rawTokens = queryText.split(" ");
		List<String> tokens = new ArrayList<String>();
		
		for(String rt : rawTokens) {
			if(rt.length() > 1) {
				tokens.add(rt);
			}
		}
		
		SearchRequest searchRequest = new SearchRequest("perseus_doc_search");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		
		HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
		highlightContent.highlighterType("unified");
		highlightBuilder.field(highlightContent);
		highlightBuilder.order("score");
		
		searchSourceBuilder.highlighter(highlightBuilder);
		
		BoolQueryBuilder query = new BoolQueryBuilder();
		
		for(String t : tokens) {
			for(String field : simpleFields) {
				query.should(QueryBuilders.matchQuery(field, t));
			}
		}
		
		searchSourceBuilder.query(query);
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = this.client.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits hits = searchResponse.getHits();
		
		return hits.getHits();
	}
	
	public SearchHit[] advancedSearch(String documentTitle, String documentAuthor, String documentKeywords, String documentText, 
			String documentLicense, String documentResource, String documentCategories, String documentSectors) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest("perseus_doc_search");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		
		HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
		highlightContent.highlighterType("unified");
		highlightBuilder.field(highlightContent);
		highlightBuilder.order("score");
		
		searchSourceBuilder.highlighter(highlightBuilder);
		
		BoolQueryBuilder query = new BoolQueryBuilder();
		
		if(!documentTitle.equals("")) {
			query.should(QueryBuilders.matchQuery("title", documentTitle));
		}

		if(!documentAuthor.equals("")) {
			String[] authors = documentAuthor.split(",");
			
			for(String author : authors) {
				String authorTmp;
				if(author.indexOf(" ") == 0) {
					authorTmp = author.substring(1);
				} else {
					authorTmp = author;
				}
				
				query.should(QueryBuilders.matchQuery("keywords", authorTmp));
				
			}
		}
		
		if(!documentKeywords.equals("")) {
			
			String[] keywords = documentKeywords.split(",");
			
			for(String keyword : keywords) {
				String keywordTmp;
				if(keyword.indexOf(" ") == 0) {
					keywordTmp = keyword.substring(1);
				} else {
					keywordTmp = keyword;
				}
				
				query.should(QueryBuilders.matchQuery("keywords", keywordTmp));
				
			}
		}

		if(!documentText.equals("")) {
			query.should(QueryBuilders.matchQuery("content", documentText));
		}
		
		if(!documentLicense.equals("")) {
			query.must(QueryBuilders.matchQuery("license", documentLicense));
		}
		
		if(!documentResource.equals("")) {
			query.must(QueryBuilders.matchQuery("resource_type", documentResource));
		}

		if(!documentCategories.equals("")) {
			String[] categories = documentCategories.split(",");
			
			for(String category : categories) {
				String categoryTmp;
				if(category.indexOf(" ") == 0) {
					categoryTmp = category.substring(1);
				} else {
					categoryTmp = category;
				}
				
				query.should(QueryBuilders.matchQuery("keywords", categoryTmp));
				
			}
		}

		if(!documentSectors.equals("")) {
			String[] sectors = documentSectors.split(",");

			for(String sector : sectors) {
				String sectorTmp;
				if(sector.indexOf(" ") == 0) {
					sectorTmp = sector.substring(1);
				} else {
					sectorTmp = sector;
				}

				query.should(QueryBuilders.matchQuery("policy_sectors", sectorTmp));
			}
		}

		searchSourceBuilder.query(query);
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits hits = searchResponse.getHits();
		
		return hits.getHits();
	}

	public boolean checkCredentials(String username, String password, ApplicationContext context) throws IOException {
		
		BCryptPasswordEncoder encoder = context.getBean(BCryptPasswordEncoder.class);
		
		SearchRequest searchRequest = new SearchRequest("perseus_users");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder usernameQuery = new BoolQueryBuilder();
		usernameQuery.must(QueryBuilders.matchQuery("username", username));
		
		searchSourceBuilder.query(usernameQuery);
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = this.client.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHit[] hits = searchResponse.getHits().getHits();
		
		if(hits.length > 0) {
			String elasticPassword = hits[0].getSourceAsMap().get("password").toString();
			
			if(encoder.matches(password, elasticPassword)) {
				return true;
			}
		}
		
		return false;
	}
}
