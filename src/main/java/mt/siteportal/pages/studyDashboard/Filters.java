package mt.siteportal.pages.studyDashboard;

import java.util.ArrayList;
import java.util.List;

public class Filters {
	//TODO review this class
	public static final String NEW = "New"; 
	public static final String SCREENED = "Screened";
	public static final String SCREENEDFAILED = "Screen Failed";
	public static final String ENROLLED = "Enrolled";
	public static final String COMPLETED = "Completed";
	public static final String DISCONTINUED = "Discontinued";
	public static final String PENDING = "Pending";
	public static final String INPROGRESS = "In Progress";
	public static final String COMPLETE = "Complete";
	public static final String EDITING = "Editing";
	public static final String PAPERTRANSCRIPTION = "Paper Transcription";
	public static final String PAPERTRANSCRIPTIONWOSOURCE = "Paper Transcription w/o Source";
	public static final String VALIDATIONRULEESOVERRIDDEN = "With Overrides";
	public static final String UNREADCENTRALREVIEWFEEDBACK = "Unread Feedback";
	public static final String QUERIES = "With Queries";

	
	private static String[] subjectFilters = {NEW,SCREENED,SCREENEDFAILED,ENROLLED,COMPLETED,DISCONTINUED};
	private static String[] visitFilters = {PENDING,INPROGRESS,COMPLETE,EDITING};
	private static String[] assesmentFilters = {COMPLETE,VALIDATIONRULEESOVERRIDDEN,PAPERTRANSCRIPTION,PAPERTRANSCRIPTIONWOSOURCE,QUERIES,UNREADCENTRALREVIEWFEEDBACK};
	
	public static List<String> subjectFilters(){
		List<String> lst = new ArrayList<String>();
		for (int i = 0;i<subjectFilters.length; i++)
		lst.add(subjectFilters[i]);
		return lst;
	}
	
	public static List<String> visitFilters(){
		List<String> lst = new ArrayList<String>();
		for (int i = 0;i<visitFilters.length; i++)
		lst.add(visitFilters[i]);
		return lst;
	}
	
	public static List<String> assesmentFilters(){
		List<String> lst = new ArrayList<String>();
		for (int i = 0;i<assesmentFilters.length; i++)
		lst.add(assesmentFilters[i]);
		return lst;
	}
}
