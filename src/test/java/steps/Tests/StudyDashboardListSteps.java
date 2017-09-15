package steps.Tests;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.StudyProfile;
import mt.siteportal.pages.studyDashboard.StudyRaters;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.DetailPages.AddSubject;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;
import nz.siteportal.pages.studydashboard.ListPages.VisitList;
import steps.Configuration.CommonSteps;

public class StudyDashboardListSteps extends CommonSteps{
	
	private DashboardList dashboardList;
	private SubjectList subjectList;
	private VisitList visitList;		
	private AddSubject addSubject;
	
	public boolean getToAllSubjectsList(){
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		if(subjectList.isOpened())
			return true;
		getToStudyDashboard();
		openAllSubjects();
		return subjectList.isOpened();
	}

	public void verifyColumnExists(String columnName) {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logInfo("Test if the column [" + columnName + "] exists...");
		Assert.assertNotNull(dashboardList.getColumnHeaderFor(columnName),
				"The Column Header called " + columnName + " was not found.");
		Log.logInfo("Test Passed.");
	}

	public boolean sort(String column, boolean increasingDownwards) {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logStep(String.format("Sorting column [%s] to be %s", column,
				(increasingDownwards) ? "ascending" : "descending"));
		try {
			dashboardList.sortColumn(column, increasingDownwards);
		} catch (Exception e) {
			Log.logInfo("There was a problem trying to sort the column [" + column + "]...");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void verifySort(String column, boolean increasingDownwards){
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logInfo("Test if the column [" + column + "] is correctly sorted...");
		Assert.assertTrue(dashboardList.isSorted(column, increasingDownwards), "The Sorting was not correct.");
		Log.logInfo("Test Passed.");
	}

	public boolean clickOnRandomRow() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		dashboardList.clickRandomRow();
		return !dashboardList.isListOpen();
	}

	public void verifySubjectDetailsPageIsOpen() {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		Log.logInfo("Test if the Subject Details Page was correctly opened...");
		Assert.assertTrue(subjectDetails.isOpened(), "The Subject Details page was not opened. Subject Details is opened? ");
		Log.logInfo("Test Passed.");
	}

	public void verifySubjectAddButtonExists() {
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		addSubject = PageFactory.initElements(Browser.getDriver(), AddSubject.class);
		Log.logInfo("Test if the Add New Subject button is present...");
		Assert.assertNotNull(subjectList.getAddButton());
		Log.logInfo("Test Passed");
	}

	public void verifyAddSubjectButtonsSiteList(List<String> sites) {
		Log.logInfo("Test if the Add New Subject button shows the list of Sites to add the Subject to...");
		if (sites.size() > 1) {
			subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
			List<String> siteOptions = subjectList.getAddToSiteOptions();
			Assert.assertEquals(siteOptions.size(), sites.size()-1,
					"The sites available in the Add Subject Dropdown were not correct.");
		}
		Log.logInfo("Test Passed");
	}

	public List<String> storeSitesAvailable() {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		return dashboard.getListOfSites();
	}
	
	public List<String> storeSitesAvailable(String studyName) {
		chooseStudy(studyName);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		return dashboard.getListOfSites();
	}
	
	public boolean addSubjectToSite(String site) {
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		subjectList.addSubjectToSite(site);
		return !subjectList.isOpened();
	}

	public boolean addSubjectToRandomSite() {
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		List<String> siteOptions = subjectList.getAddToSiteOptions();
		Random r = new Random();
		int randSite = r.nextInt((siteOptions.size() - 0) + 1) + 0;
		// subjectList.addSubjectToSite("");
		subjectList.addSubjectToSite(siteOptions.get(randSite));
		return !subjectList.isOpened();
	}

	public void verifyAddNewSubjectPageIsOpened() {
		Log.logInfo("Test if clicking the Add New Subject Page is opened...");
		addSubject = PageFactory.initElements(Browser.getDriver(), AddSubject.class);
		Assert.assertTrue(addSubject.isOpen(), "Add New Subject Page was not opened.");
		Log.logInfo("Test Passed.");
	}

	public void verifyGenderIcon() {
		Log.logInfo("Test if the Gender Icon is present in the Subject List page...");
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		WebElement firstRow = subjectList.getFirstItemFromList();
		String imageSource = firstRow.findElement(By.xpath(".//img")).getAttribute("src");
		Assert.assertTrue(imageSource.contains("icon_gender"), "The Gender Icon image was not found.");
		Log.logInfo("Test Passed");
	}

	public boolean getToAllVisitsList() {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		visitList = PageFactory.initElements(Browser.getDriver(), VisitList.class);
		if(visitList.isVisitListOpened())
			return true;
		getToStudyDashboard();
		openAllVisits();
		return visitList.isVisitListOpened();
	}

	public void verifyRefreshFunctionality() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logStep("Clicking the refresh button...");
		dashboardList.clickRefresh();
		Log.logInfo("Test if the Refresh Button opens new connections...");
		Assert.assertTrue(UiHelper.didAjaxConnectionsOpen(Browser.getDriver()),
				"The Page did not request a new connection.");
		Log.logInfo("Test Passed.");
	}

	public void verifyHomeLink() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		// dashboard = dashboardList.clickHomeForSiteUser();
		homePage = dashboardList.clickHomeLink();
		Log.logInfo("Test if the Home Page link on the toolbar is working...");
		Assert.assertTrue(homePage.isHomePageOpened(),
				"Home page link is not working since dashboard page or the Home page should have been opened. ");
		Log.logInfo("Test Passed.");
	}

	public void verifyStudyProfileButton() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		StudyProfile studyProfile = dashboardList.clickStudyProfile();
		Log.logInfo("Test if the Study Profile Panel was opened...");
		Assert.assertTrue(studyProfile.isOpen(), "The Study Profile was not opened.");
		Log.logInfo("Test Passed.");
		studyProfile.closeProfile();
		Log.logInfo("Test if the Study Profile Panel was closed...");
		Assert.assertFalse(studyProfile.isOpen(), "The Study Profile was not closed.");
		Log.logInfo("Test Passed.");
	}

	public void verifyRatersButton() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		StudyRaters ratersPanel = dashboardList.clickRaters();
		Log.logInfo("Test if the Raters Panel was opened...");
		Assert.assertTrue(ratersPanel.isRaterPanelOpened(), "The Raters Panel was not opened.");
		Log.logInfo("Test Passed.");
		ratersPanel.closeRaters();
		Log.logInfo("Test if the Raters Panel was closed...");
		Assert.assertFalse(ratersPanel.isRaterPanelOpened(), "The Raters Panel was not closed.");
		Log.logInfo("Test Passed.");
	}

	public void verifyQueriesButton() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		QueriesSidePanel queriesPanel = dashboardList.clickQueries();
		Log.logInfo("Test if the Queries Panel was opened...");
		Assert.assertTrue(queriesPanel.isOpened(), "The Raters Panel was not opened.");
		Log.logInfo("Test Passed.");
		queriesPanel.clickClose();
		Log.logInfo("Test if the Queries Panel was closed...");
		Assert.assertFalse(queriesPanel.isOpened(), "The Queries Panel was not closed.");
		Log.logInfo("Test Passed.");
	}

	public void verifyItemCount() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		int visitCountFromList = dashboardList.getItemCountFromList();
		int visitCountFromHeader = dashboardList.getListCount();
		Log.logInfo("Test if the count displayed and the number of items in the list match...");
		Assert.assertEquals(visitCountFromHeader, visitCountFromList, "Item count didn't match. Count from list:"
				+ visitCountFromList + ", Count from header:" + visitCountFromHeader);
		Log.logInfo("Test Passed.");
	}

	public boolean clickRow(Map<String, String> criteria) {
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		assessmentDetails = assessmentList.clickRow(criteria);
		return (assessmentDetails != null);
	}

	public boolean getToAllAssessmentsList() {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		if(assessmentList.isOpened())
			return true;
		getToStudyDashboard();
		openAllAssessments();
		return assessmentList.isOpened();
	}
	
}
