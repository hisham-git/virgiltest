package mt.siteportal.utils.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A container for constants.  
 *
 */
public class Constants {	
	
	/**
	 * A date formatter for logging sub-system.
	 */
	public static final DateFormat DATE_FORMAT_LOG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * A date formatter for file/directory name timestamps.
	 */
	public static final DateFormat DATE_FORMAT_FILE = new SimpleDateFormat("yyyy_MM_dd-HHmmss");

    /**
     * A date formatter for form date fields.
     */
    public static final DateFormat DATE_FORMAT_FORM_DATES = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

	public static final String USER = "user";
	public static final String ADMIN = "admin";
	public static final String ME = "Me";
	public static final String NOTASSIGNED = "Not Assigned";
	public static final int PROCCESINGTIME = 30000;
	public static final String ASSESSMENTSTATUSPENDING = "PENDING";
	public static final String ASSESSMENTSTATUSPROCESSING = "PROCESSING...";
	public static final String ASSESSMENTSTATUSCOMPLETE = "COMPLETE";
	public static final String NOTADMINISTERED = "Not Administered";
	public static final String NOTCOMPLETED = "Not Completed";
	public static final String PROCESSING = "Processing...";
	public static final String[] ALIASES= {"NewUp","ScreenedUp","EnrolledUp","CompletedUp","DiscontinuedUp"};
	public static final String[] SUBJECTSTASUSES= {"New","Screened","Enrolled","Completed","Discontinued"};
	public static final String[] SUBJECTVISITSTATASUSES = {"Requested","Scheduled","Initiated","In Progress","Complete","Editing","Cancelled"};
	public static final String SUBJECTS = "Subjects";
	public static final String ASSESSMENTS = "Assessments";
	public static final String VISITS = "Visits";
	public static final String SUBJECTSTATUS = "Subject Status";
	public static final String ENGLISH = "English (US)";
	public static final String SPANISH = "Spanish (US)";
	public static final String VISIT_STATUS_PENDING = "Pending";
	public static final String VISIT_STATUS_SKIPPED = "Skipped";
}