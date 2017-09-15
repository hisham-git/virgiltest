package steps.Tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.Filters;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import nz.siteportal.objects.QueryPanelItem;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import steps.Abstract.AbstractStep;
import tests.Breadcrumbs.BreadcrumbsHelper;

public class BreadcrumbsNavigationSteps extends AbstractStep {
	
	public BreadcrumbsNavigationSteps(){
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
	}

	public void verifyBreadcrumbLinkTexts(String studyName, String siteName) {
		List<String> expectedLinks = BreadcrumbsHelper.generateExpectedBreadcrumbs(studyName, siteName);
		List<String> actualLinks = toolbarFull.getBreadcrumbTexts();
		Log.logInfo("Test if the Breadcrumb's generated texts are correct...");
		Assert.assertEquals(actualLinks, expectedLinks, "The breadcrumbs were not correct. The format ");
		Log.logInfo("Test Passed.");
	}

	public boolean clickStudyLinkFromToolbar() {
		try {
			toolbarFull.clickStudyLinkFromBreadcrumbs();
		} catch (NoSuchElementException e) {
			Log.logStep("Could not click the Study link from breadcrumbs...");
			Log.logInfo(e.getMessage());
			return false;
		}
		Log.logStep("Clicking the Study Link from the breadcrumbs");
		return true;
	}

	public void verifyDashboardIsOpened() {
		Log.logInfo("Test if the Dashboard is open...");
		Assert.assertTrue(dashboard.isDashboardOpened(), "The Study Dashboard was not opened. Dashboard is opened ");
		Log.logInfo("Test Passed.");
	}

	public boolean clickSecondLinkFromToolbar() {
		try {
			toolbarFull.clickSecondLinkFromBreadcrumbs();
		} catch (NoSuchElementException e) {
			return false;
		}
		Log.logStep("Clicking the Second Link from the breadcrumbs");
		return true;
	}

	/**
	 * Makes sure that All Sites is not selected
	 */
	public String selectSiteOtherThan(String siteToExclude){
		String siteName = dashboard.selectRandomSite();
		while (siteName.equals(siteToExclude))
			siteName = dashboard.selectRandomSite();
		Log.logStep("Selecting Random Site -> " + siteName);
		return siteName;
	}

	public boolean getToFirstDetailsPageInList() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		if(dashboardList.getFirstItemFromList() == null)
			throw new SkipException("The randomly selected list has no items. Skipping this test.");
		Log.logStep("Clicking first item from list...");
		dashboardList.clickFirstRow();
		return true;
	}

	public void verifyBreadcrumbLinkTexts(String studyName, String siteName, String randomDashboardCard) {
		String context = randomDashboardCard.split("-")[0];
		String filter = randomDashboardCard.split("-")[1];
		filter = filter.equals(context) ? "All" : filter;
		Log.logVerify("Verifying if the breadcrumb is generated correctly for a details page...");
		Verify.Equals(BreadcrumbsHelper.generateExpectedBreadcrumbs(studyName, siteName, context, filter),
				toolbarFull.getBreadcrumbTexts(), "Generated Breadcrumbs were not correct. [FAILED]");
		Log.logInfo("[PASSED]");
	}

	public boolean clickLastLinkFromToolbar() {
		try {
			toolbarFull.clickLastLinkFromBreadcrumbs();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public void verifyDashboardListIsOpened(String randomDashboardCard) {
		String context = randomDashboardCard.split("-")[0];
		String filter = randomDashboardCard.split("-")[1];
		filter = filter.equals(context) ? "All" : filter;
		Log.logInfo("Test if the Dashboard list page of the correct type is opened...");
		Assert.assertTrue(dashboardList.isOpened(context), "The redirection was not correct. Dashboard List of type ");
		Log.logInfo("Test Passed.");
		Log.logInfo("Test if the Dashboard list page with the correct filter is opened...");
//		Assert.assertEquals(dashboardList.getFilter(), filter,
//				"The redirection was not correct. Dashboard List with Filter ");
		Assert.assertTrue(dashboardList.getFilter().equalsIgnoreCase(filter),
				"The redirection was not correct. Dashboard List with Filter ");
		Log.logInfo("Test Passed.");
	}
	
	public void clickRandomCard(String randomDashboardCard) {
		dashboardList = dashboard.clicksOnCard(randomDashboardCard);
		if (dashboardList.getFirstItemFromList() == null)
			throw new SkipException("The randomly selected card [" + randomDashboardCard
					+ "] did not have any data for the filter, so tests could not be run on this configuration.");
	}
	
	private String visitOrSubjectNameFromDetailsGrid;
	public void verifyExpectedBreadcrumbsForForms(String studyName, String siteName, String context, String filter) {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		dashboardList.clickFirstRow();
		visitOrSubjectNameFromDetailsGrid = BreadcrumbsHelper.navigateAndgetValueFromAssessmentDetails(context);
		if (null != visitOrSubjectNameFromDetailsGrid) {
			toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
			HardVerify.Equals(
					BreadcrumbsHelper.generateExpectedBreadcrumbsForForms(studyName, siteName, context, filter,
							visitOrSubjectNameFromDetailsGrid),
					toolbarFull.getBreadcrumbTexts(), "Test if the breadcrumbs have been correctly generated...",
					"[Test Passed]",
					"The Breadcrumbs were not correct. Expected ["
							+ BreadcrumbsHelper.generateExpectedBreadcrumbsForForms(studyName, siteName, context,
									filter, visitOrSubjectNameFromDetailsGrid).toString()
							+ "] but was [" + toolbarFull.getBreadcrumbTexts().toString() + "]");
		} else {
			throw new SkipException(
					"No Subject/Visit found with completed/submitted assessments for breadcrumbs generation. Skipping tests...");
		}
	}
	
	public void clickLastLinkFromBreadcrumbs() {
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		toolbarFull.clickLastLinkFromBreadcrumbs();
	}
	
	public void verifyCorrectContextRedirection(String context){
		String actualHeader = BreadcrumbsHelper.getHeaderFromAppropriatePage(context);
		String expectedheader = context.substring(0, context.length() - 1) + ": " + visitOrSubjectNameFromDetailsGrid;
		HardVerify.Equals(expectedheader, actualHeader,
				"Test if the breadcrumbs correctly navigates to approprirate page...", "[Test Passed]",
				"The Breadcrumbs did not redirect correctly. Expected [" + expectedheader
						+ "] as the Details Page, but was [" + actualHeader + "] [Test Failed]");
	}
	
	public void openListPageFor(String type) {
		StudyDashboardListSteps listSteps = new StudyDashboardListSteps();
		switch (type) {
		case "Subject":
			openAllSubjects();
			break;
		case "Visit":
			openAllVisits();
			break;
		case "Assessment":
			openAllAssessments();
			break;
		case "Score":
			openAllAssessments();
			break;
		default:
			Log.logError("List type doesn't match");
			break;
		}
	}
	
	private QueriesSidePanel panel;
	
	public void openQuerySidePanel() {
	panel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		if (panel == null || !panel.isOpened())
			panel = dashboard.openQueriesPanel();
	}
	
	public void closeQuerySidePanel() {
		if (panel.isOpened())
			panel.clickClose();
	}
	
	private String context;
	public void findAndClickRandomQuery() {
		int queryIndex = panel.getRandomQueryIndex();
		WebElement query = panel.getQueryAtIndex(queryIndex);
		context = (new QueryPanelItem(query)).getQueryCategory();
		UiHelper.click(query);
	}

	/**
	 * @author HISHAM
	 * 
	 * Description:
	 * This method selects query according to increasing index until
	 * the given type (Subject/Visit/Assessment/Score) is matched.
	 * Once type matched it clicks the query
	 * 
	 * @param String type 
	 * 			- Type of Query to search for(Subject/Visit/Assessment/Score)
	 */
	public void findAndClickQueryWithType(String type) {
		WebElement query;
		int count = panel.getNumberOfQueriesInList();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				query = panel.getQueryAtIndex(i);
				context = (new QueryPanelItem(query)).getQueryCategory();
				if (context.equalsIgnoreCase(type)) {
					Log.logInfo(type + " Query found at index: " + i);
					UiHelper.click(query);
					break;
				}
			}
		} else {
			throw new SkipException("Query panel doesn't contain any query for searching. Skipping tests...");
		}
	}
	
	public void verifyExpectedBreadcrumbsForQueries(String studyName, String siteName) {
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		List<String> expected = BreadcrumbsHelper.generateExpectedBreadcrumbsForQueries(studyName, siteName, context);
		List<String> actual = toolbarFull.getBreadcrumbTexts();
		HardVerify.Equals(expected, actual, "Test if the Breadcrumbs have been correctly generated...", "[Test Passed]",
				"The Breadcrumbs were not correct. Expected " + expected + " but was " + actual + " [Test Failed]");
	}
	
	public void verifyCorrectContextRedirection() {
		DashboardList list = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		HardVerify.Equals((context.equalsIgnoreCase("Score") ? "Assessment" : context) + "s: All",	//To verify score Assessments redirection
				list.getListTypeName() + ": " + list.getFilter(), "Test if the Breadcrumbs correctly redirect...",
				"[Test Passed]", "The Breadcrumbs do not correctly redirect. Expected List header to be [" + context
						+ "s: All" + "] but was [" + list.getListTypeName() + ": " + list.getFilter() + "]");
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Description: 
	 * Gets list of filters for given Category(Subjects/Visits/Assessments) 
	 * for selected Site
	 * 
	 * @param category - Category to find 
	 * @return List filtersName - Filter names
	 */
	public List<String> getAvailableFiltersFor(String category) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);

		List<String> filtersName = new ArrayList<String>();
		Map<String, String> filterNameValues = dashboard.getCategoryFiltersNameValueFor(category);
		Iterator<String> itr = filterNameValues.keySet().iterator();
		while (itr.hasNext()) {
			String name = itr.next();
			Log.logInfo("Filters found for " + category + ": " + name);
			filtersName.add(name);
		}
		return filtersName;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Description: 
	 * This method returns predefined list of filters for given Category(Subjects/Visits/Assessments) 
	 * 
	 * @param String category 
	 * 			- Category of filters
	 * @return List filtersName 
	 * 			- Filter names as list
	 */
	public List<String> getAllFiltersFor(String category) {
		List<String> filters = null;
		switch (category) {
		case "Subjects":
			filters = Filters.subjectFilters();
			break;
		case "Visits":
			filters = Filters.visitFilters();
			break;
		case "Assessments":
			filters = Filters.assesmentFilters();
			break;
		default:
			Log.logError("Category name doesn't matched. Should be (Subjects/Visits/Assessments)");
			break;
		}
		return filters;
	}
}
