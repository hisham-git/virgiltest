package mt.siteportal.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Visit {
	
	private Map<String, String> visits = new HashMap<String, String>();
	
	public Visit(List<String> headers, List<String> values) {
		for (int i=0;i<headers.size(); i++){
			visits.put(headers.get(i), values.get(i));
		}
	}
	
	public Visit(){
		
	}

	public String getVisit() {
		return visits.get("Order by Visit");
	}

	public String getComplete() {
		return visits.get("Order by Complete");
	}

	public String getStatus() {
		return visits.get("Order by Status");
	}
	
	public String getSvid() {
		return visits.get("Order by SVID");
	}
	
	public String getSubject() {
		return visits.get("Order by Subject");
	}
	
	public String getLanguage() {
		return visits.get("Order by Language");
	}
	
	public String getSite() {
		return visits.get("Order by Site");
	}

	public String getSubjectStatus() {
		return visits.get("Order by Subject Status");
	}

	public String getOrderByFeedback() {
		return visits.get("Order by Feedback");
	}
	
	public String getinitials() {
		return visits.get("Order by Initials");
	}
}
