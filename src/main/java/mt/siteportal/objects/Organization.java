package mt.siteportal.objects;

public class Organization {

	private String name;
	private String abbreviation;
	private String type;
	private String subType;
	private String activated;
	private String deactivated;
	private String deactivateReason;
	private String webSite;
	private String comments;
	
	public Organization (String name, String abbreviation, String type){
		this.name=name;
		this.abbreviation=abbreviation;
		this.type=type;
	}
	
	public Organization (){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getActivated() {
		return activated;
	}
	public void setActivated(String activated) {
		this.activated = activated;
	}
	public String getDeactivated() {
		return deactivated;
	}
	public void setDeactivated(String deactivated) {
		this.deactivated = deactivated;
	}
	public String getDeactivateReason() {
		return deactivateReason;
	}
	public void setDeactivateReason(String deactivateReason) {
		this.deactivateReason = deactivateReason;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "Organization [name=" + name + ", abbreviation=" + abbreviation
				+ ", type=" + type + ", subType=" + subType + ", activated="
				+ activated + ", deactivated=" + deactivated
				+ ", deactivateReason=" + deactivateReason + ", webSite="
				+ webSite + ", comments=" + comments + "]";
	}
	
	
}
