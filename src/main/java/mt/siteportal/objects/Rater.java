package mt.siteportal.objects;

public class Rater {
		
	private String raterName;
	private String sceduledCount;
	private String completedCount;
	private String editedCount;
	
	public Rater(String raterName, String sceduledCount, String completedCount, String editedCount){
		this.raterName=raterName;
		this.sceduledCount = sceduledCount;
		this.completedCount=completedCount;
		this.editedCount=editedCount;
	}
	public Rater(){
		
	}
	
	public String getEditedCount() {
		return editedCount;
	}
	public void setEditedCount(String editedCount) {
		this.editedCount = editedCount;
	}
	public String getCompletedCount() {
		return completedCount;
	}
	public void setCompletedCount(String completedCount) {
		this.completedCount = completedCount;
	}
	public String getScheduledCount() {
		return sceduledCount;
	}
	public void setSceduledCount(String sceduledCount) {
		this.sceduledCount = sceduledCount;
	}
	public String getRaterName() {
		return raterName;
	}
	public void setRaterName(String raterName) {
		this.raterName = raterName;
	}
}
