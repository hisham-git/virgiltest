package mt.siteportal.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assessment {
	private Map<String, String> assesments = new HashMap<String, String>();
	
	public Assessment(List<String> headers, List<String> values) {
		for (int i=0;i<headers.size(); i++){
			assesments.put(headers.get(i), values.get(i));
		}
	}
	
	public Assessment(){
		
	}

	public String getAssesment() {
		return assesments.get("Order by Assessment");
	}

/*	public String getBlinding() {
		return assesments.get("Order by Assessment");
	}
*/
	public String getStatus() {
		return assesments.get("Order by Status");
	}

	public String getVersion() {
		return assesments.get("Order by Version");
	}

	public String getRater() {
		return assesments.get("Order by Rater");
	}

	public String getVisit() {
		return assesments.get("Order by Visit");
	}

	public String getComplete() {
		return assesments.get("Order by Complete");
	}

	public String getSvid() {
		return assesments.get("Order by SVID");
	}

	public String getSubject() {
		return assesments.get("Order by Subject");
	}
	
	public String getSite() {
		return assesments.get("Order by Site");
	}

	public String getSubjectStatus() {
		return assesments.get("Order by Subject Status");
	}

	public String getFeedback() {
		return assesments.get("Order by Feedback");
	}
}
