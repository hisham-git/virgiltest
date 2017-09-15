package mt.siteportal.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subject {

	private Map<String, String> subjects = new HashMap<String, String>();
	
	public Subject(List<String> headers, List<String> values) {
		for (int i=0;i<headers.size(); i++){
			subjects.put(headers.get(i), values.get(i));
		}
	}

	public Subject(){
		
	}
	public String getInitials() {
		return subjects.get("Order by Initials");
	}

	public String getDateOfBirth() {
		return subjects.get("Order by Date of Birth");
	}

	public String getAge() {
		return subjects.get("Order by Age");
	}
	
	public String getSubjectName() {
		return subjects.get("Order by Subject");
	}
	
	public String getLanguage() {
		return subjects.get("Order by Language");
	}
	
	public String getSite() {
		return subjects.get("Order by Site");
	}

	public String getStatus() {
		return subjects.get("Order by Status");
	}
	
	public String getActiveVisits() {
		return subjects.get("Order by Active Visits");
	}

	public String getCompletedVisits() {
		return subjects.get("Order by Completed Visits");
	}
	
	public String getVisitInEdit() {
		return subjects.get("Order by Visits in Edit");
	}

}
