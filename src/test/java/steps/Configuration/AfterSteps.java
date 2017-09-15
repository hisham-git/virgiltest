package steps.Configuration;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.Header;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.pages.studyDashboard.StudyProfile;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import steps.Abstract.AbstractStep;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;

/**
 * Created by maksym.tkachov on 6/23/2016.
 */
public class AfterSteps extends AbstractStep {
	private LoginPage loginPage;

	public void logout() {
		header = PageFactory.initElements(Browser.getDriver(), Header.class);
		header.logout();
	}

	public void logoutByClearingCookie() {
		Log.logInfo("Clearing all browser cookies for logout...");
		Browser.getDriver().manage().deleteAllCookies();
		Log.logInfo("Refreshing browser window for redirecting to Login Page...");
		Browser.getDriver().navigate().refresh();
		loginPage = PageFactory.initElements(Browser.getDriver(), LoginPage.class);
		if (loginPage.isLoginPageOpened()) {
			Log.logInfo("Redirected to Login Page...");
		} else {
			Log.logError("Clearing browser cookies & refreshing not redirected to Login page");
		}
	}

	public void closeStudyProfileAndLogout() {
		studyProfile = PageFactory.initElements(Browser.getDriver(), StudyProfile.class);
		studyProfile.closeProfile();
		logout();
	}
	
	/**
	 * @author HISHAM
	 * 
	 * This function first unschedules list of visits & then deletes the subject
	 * 
	 * @param List<String, Integer> visits
	 * 			- visit names with position in visit table
	 */
	
	
	/**
	 * </p>
	 * Description:This function first unschedule given visit in position & then
	 * deletes the subject
	 * </p>
	 * 
	 * @param visitName
	 *            - String, name of the visit in visit table
	 * @param visitPos
	 *            - int, position of the visit in visit table
	 * 
	 * @author HISHAM
	 */
	public void unScheduleVisitAndDeleteSubject(String visitName, int visitPos) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visitName, visitPos);
		subjectDetails.visitTable.unscheduleVisit();
		subjectDetails.deleteSubject();
	}
}
