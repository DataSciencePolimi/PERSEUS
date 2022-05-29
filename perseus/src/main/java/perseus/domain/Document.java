package perseus.domain;
import java.util.Date;

public class Document {
	
	private String id;
	
	private String title;

	private String content;

	private String docAbstract;

	private Date publishDate;
	
	private String authors;
	
	private String keywords;
	
	private String categories;

	private String policySectors;
	
	private String institution;
	
	private String license;
	
	private String resourceType;
	
	private String linkToOriginal;
	
	private String linkToExternal;
	
	private String highlightedString;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDocAbstract() {
		return docAbstract;
	}

	public void setDocAbstract(String docAbstract) {
		this.docAbstract = docAbstract;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getInstitution() {
		return institution;
	}

	public void setPolicySectors(String policySectors){
		this.policySectors = policySectors;
	}

	public String getPolicySectors(){
		return policySectors;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getLinkToOriginal() {
		return linkToOriginal;
	}

	public void setLinkToOriginal(String linkToOriginal) {
		this.linkToOriginal = linkToOriginal;
	}

	public String getLinkToExternal() {
		return linkToExternal;
	}

	public void setLinkToExternal(String linkToExternal) {
		this.linkToExternal = linkToExternal;
	}

	public String getHighlightedString() {
		return highlightedString;
	}

	public void setHighlightedString(String highlightedString) {
		this.highlightedString = highlightedString;
	}
}