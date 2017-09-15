package tests.CrVisitList;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;

import steps.Tests.CrVisitListSteps;
import tests.Abstract.AbstractTest;

@Test(groups={"CrVisitList"})
public abstract class AbstractCrVisitList extends AbstractTest{
	
	protected String subjectStatus = "subjectStatus";
	protected String subjectVisitStatus = "subjectVisitStatus";
	DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");	
	Date date = new Date();
	
	

	protected String scheduledDateFrom = "scheduledDateFrom";
	protected String scheduledDateTo = "scheduledDateTo";
	protected String[] expectedColHeaders = { "Subject Number", "Subject Status", "SVID", "Study Name", "Site",
			"Site Country", "Visit Name", "Visit Status", "CR Clinician Name", "Scheduled Date (User timezone)",
			"Consent to Record" };
	protected String[] expectedTextSearchBoxes = { "subjectNumber", "svid","studyName", "site","siteCountry" ,"visitName", 
			"clinicianName", };
	protected int[] searchboxIndex = {1,3,4,5,6,7,9};
	protected String[] expectedStatusDropdowns = { "subjectStatus", "subjectVisitStatus" };

	protected CrVisitListSteps crVisitStep = new CrVisitListSteps();
	
	
	protected String getYesterdayDate() {
//		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	
	protected String getNextdayDate() {
//		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		return dateFormat.format(cal.getTime());
	}
	protected String getCurrentDate(){
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
		
	}
	
}
