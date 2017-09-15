package steps.Configuration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.Templates;
import mt.siteportal.pages.studyDashboard.ToolBar;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.DataHolder;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.tools.Log;
import steps.Abstract.AbstractStep;

/**
 * Created by maksym.tkachov on 4/28/2016.
 */
public class BeforeSteps extends AbstractStep {
	
	private static WebDriver driver;
	public BeforeSteps(){
		driver = Browser.getDriver();
	}
	public void loginAs(String loginName, String password) {
		loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loginAsAdmin(loginName, password);
	}

	public Dashboard loginAndOpenDashboard(String loginName, String password) {
		loginAs(loginName, password);
		
		/*if (loginName.equals(DataHolder.getHolder().getAdminAccountName())
				|| loginName.equals(DataHolder.getHolder().getMaportalUserAccountName())) {
			homePage = PageFactory.initElements(Browser.getDriver(), HomePage.class);
			homePage.openDashboard();
		}*/
		dashboard = PageFactory.initElements(driver, Dashboard.class);
		homePage = PageFactory.initElements(driver, HomePage.class);
		if (dashboard.isDashboardOpened()) {
			return dashboard;
		} else if (homePage.isHomePageOpened()) {
			dashboard = homePage.openDashboard();
		}
		return dashboard;
	}

	public void loginAndChooseStudy(String loginName, String password, String study) {

		/*
		 * @author HISHAM
		 */
		/*if (Browser.getDriver().getCurrentUrl().contains(URLsHolder.getHolder().getSiteportalURL())
				|| loginName.equals(DataHolder.getHolder().getSiteportalUserAccountName())) {
			loginAsSitePersonWithDashboard(loginName, password);
			toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
			toolbarFull.chooseStudy(study);
		} else if (Browser.getDriver().getCurrentUrl().contains(URLsHolder.getHolder().getMaportalURL())
				|| loginName.equals(DataHolder.getHolder().getMaportalUserAccountName())) {
			loginAndOpenDashboard(loginName, password);
			toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
			toolbarFull.chooseStudy(study);
		}*/
		
		if (loginName.equals(DataHolder.getHolder().getSiteportalUserAccountName())) {
			loginAsSitePersonWithDashboard(loginName, password);
			toolbarFull = PageFactory.initElements(driver, ToolBarFull.class);
			toolbarFull.chooseStudy(study);
		} else {
			loginAndOpenDashboard(loginName, password);
			toolbarFull = PageFactory.initElements(driver, ToolBarFull.class);
			toolbarFull.chooseStudy(study);
		}
	}

	public void loginAndChooseStudySite(String loginName, String password, String study, String site) {
		loginAndChooseStudy(loginName, password, study);
		toolbarFull = PageFactory.initElements(driver, ToolBarFull.class);
		toolbarFull.chooseSite(site);
	}

	public void loginAndOpenAllSubjects(String loginName, String password, String study, String site) {
		if (site.equals("")) {
			loginAndChooseStudy(loginName, password, study);
		} else {
			loginAndChooseStudySite(loginName, password, study, site);
		}
		dashboard = PageFactory.initElements(driver, Dashboard.class);
		dashboard.openAllSubjects();
	}
	
	/**
	 * 
	 * @param loginName
	 * @param password
	 * @param study
	 * @param site
	 * 
	 * @author HISHAM
	 */
	public void loginAndOpenAllVisits(String loginName, String password, String study, String site) {
		if (site.equals("")) {
			loginAndChooseStudy(loginName, password, study);
		} else {
			loginAndChooseStudySite(loginName, password, study, site);
		}
		dashboard = PageFactory.initElements(driver, Dashboard.class);
		dashboard.openAllVisits();
	}

	public void loginAndOpenSubject(String loginName, String password, String study, String site, String subject) {
		loginAndOpenAllSubjects(loginName, password, study, site);
		subjectTable = PageFactory.initElements(driver, SubjectsTable.class);
		subjectTable.openSubjectDetails(subject);
	}

	public void loginAndScheduleVisit(String loginName, String password, String study, String site, String subject,
			String visit, int visitPos) {
		loginAndOpenSubject(loginName, password, study, site, subject);
		subjectDetails = PageFactory.initElements(driver, SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visit, visitPos);
		subjectDetails.visitTable.scheduleVisit();
	}

	public void loginAndOpenTemplate(String loginName, String password, String study, String site, String subject,
			String visit, int visitPos, int formPos, String raterName) {
		loginAndScheduleVisit(loginName, password, study, site, subject, visit, visitPos);
		templates = PageFactory.initElements(driver, Templates.class);
		templates.assignTemplateTo(formPos, raterName);
		templates.openTemplate();
	}

	public void loginAndOpenEsignDialog(String loginName, String password, String study, String site, String subject,
			String visit, int visitPos, int formPos, String raterName) {
		loginAndOpenTemplate(loginName, password, study, site, subject, visit, visitPos, formPos, raterName);
		assessmentDetails = PageFactory.initElements(driver, AssessmentDetails.class);
		assessmentDetails.asNotAdministered();
		esignDialog = assessmentDetails.confirmAssesment();
	}
    
	/**
	 * @author HISHAM
	 */
	public void loginAsSitePerson(String loginName, String password) {
		loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loginAsSitePerson(loginName, password);
	}

	/**
	 * @author HISHAM
	 */
	public void loginAsSitePersonWithDashboard(String loginName, String password) {
		loginAsSitePerson(loginName, password);
		PageFactory.initElements(driver, Dashboard.class);
	}

	/**
	 * @author HISHAM
	 */
	public boolean loginAndChooseStudySiteWithAssessmnetCard(String loginName, String password, String study,
			String site, String assessmentCard) {
		loginAndChooseStudySite(loginName, password, study, site);
		Log.logStep("Clicking on card: " + assessmentCard);
		dashboard = PageFactory.initElements(driver, Dashboard.class);
		;
		dashboardList = dashboard.clicksOnCard("Assessments-" + assessmentCard);
		if (dashboardList.isOpened(assessmentCard)) {
			Log.logStep("Returning true if Assessment card page is opened, false otherwise. Found : "
					+ dashboardList.isOpened(assessmentCard));
			return true;
		} else {
			Log.logStep("Returning true if Assessment card page is opened, false otherwise. Found : "
					+ dashboardList.isOpened(assessmentCard));
			return false;
		}
	}

	/**
	 * @author HISHAM
	 */
	public boolean loginAndChooseStudySiteWithAssessmnetNameAndSvid(String loginName, String password, String study,
			String site, String assessmentCard, String assessmentName, String svid) {
		loginAndChooseStudySiteWithAssessmnetCard(loginName, password, study, site, assessmentCard);
		Log.logStep("Clicking on Assessment: " + assessmentName + " with svid: " + svid);
		assessmentDetails = assessmentList.clickRow(assessmentName, svid);
		if (assessmentDetails.isOpened()) {
			Log.logStep("Returning true if Assessment details page is opened, false otherwise. Found : "
					+ assessmentDetails.isOpened());
			return true;
		} else {
			Log.logStep("Returning true if Assessment details page is opened, false otherwise. Found : "
					+ assessmentDetails.isOpened());
			return false;
		}
	}
	
	public void focusBrowserWindow() {
		// Store the current window handle
		String currentWindowHandle = driver.getWindowHandle();

		// run javascript and alert code
		((JavascriptExecutor) driver).executeScript("alert('Click yes to focus window')");
		// Log.logInfo("Trying to focus browser window...");
		// ((JavascriptExecutor) driver).executeScript("window.focus()");

		Log.logInfo("Clicking 'Yes' from alert window to focus browser window...");
		driver.switchTo().alert().accept();

		// Switch back to to the window using the handle saved earlier
		driver.switchTo().window(currentWindowHandle);
	}
}

