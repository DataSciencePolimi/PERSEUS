package perseus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import perseus.domain.Document;

@Component
public class ElasticSearchResultParser {

	public static ArrayList<Document> parseElasticResult(SearchHit[] hits) throws ParseException {
		
		ArrayList<Document> results = new ArrayList<Document>();
		
		for(SearchHit hit : hits) {
			
			Map<String, Object> hitMap = hit.getSourceAsMap();
			
			Document doc = new Document();
			
			String resource_type = hitMap.get("resource_type").toString();
			
			doc.setInstitution(hitMap.get("institution").toString());
			doc.setResourceType(resource_type);
			doc.setLicense(hitMap.get("license").toString());
			doc.setLinkToOriginal(hitMap.get("link_to_original").toString());
			doc.setLinkToExternal(hitMap.get("link_to_external").toString());
			doc.setTitle(hitMap.get("title").toString());
			doc.setPublishDate(new SimpleDateFormat("yyyy-MM-dd").parse(hitMap.get("publish_date").toString()));

			// Retrieve keywords
			String keywords = hitMap.get("keywords").toString();
			String kws = "";
			String[] keys = keywords.split(",");
			
			for(String k : keys) {
				kws += k.substring(1, k.length()) + ", ";
			}

			doc.setKeywords(kws.substring(0, kws.length()-3));

			// Retrieve categories
			if(hitMap.get("categories") != null){
				String categories = hitMap.get("categories").toString();
				String cs = "";
				String[] cats = categories.split(",");

				for(String c : cats) {
					cs += c.substring(1, c.length()) + ", ";
				}

				doc.setCategories(cs.substring(0, cs.length()-3));
			}

			// Retrieve authors
			if(hitMap.get("authors") != null) {
				String authors = hitMap.get("authors").toString();
				String as = "";
				String[] auts = authors.split(",");

				for(String a : auts) {
					as += a.substring(1, a.length()) + ", ";
				}

				doc.setAuthors(as.substring(0, as.length()-3));
			}

			// Retrieve policy sectors
			if(hitMap.get("policy_sectors") != null) {
				String policySectors = hitMap.get("policy_sectors").toString();
				String sectorsString = "";
				String [] sectorName = policySectors.split(",");

				for(String s : sectorName) {
					sectorsString += s.substring(1, s.length()) + ", ";
				}

				doc.setPolicySectors(sectorsString.substring(0, sectorsString.length()-3));
			}

			// Retrieve content and abstract if not a website
			if(!resource_type.equals("web")) {
				doc.setDocAbstract(hitMap.get("abstract").toString());
				
				if(hitMap.get("content") != null) {
					doc.setContent(hitMap.get("content").toString());
				}
				
				if(hit.getHighlightFields().get("content") != null) {
					doc.setHighlightedString(hit.getHighlightFields().get("content").getFragments()[0].toString());
				}
			}
			
			results.add(doc);
		}
		
		return results;
		
	}
}
