package tests.AssessmentDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.StudyDashboardSteps;

/**
 * Test Case : SFB-TC-1124
 * 			   Assessment details - Breadcrumb navigation
 *  
 * Test Objective(s):
 * Information about study, site and assessment filter in breadcrumbs
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "BreadcrumbNavigation" })
public class BreadcrumbNavigationTests extends AbstractAssessmentDetails {
	
	private String studyName;
	List<String> availableFilters;
	List<Object[]> filterList = new ArrayList<Object[]>();
	 
	StudyDashboardSteps studyDashboardSteps = new StudyDashboardSteps();
	BreadcrumbsNavigationSteps breadcrumbsSteps = new BreadcrumbsNavigationSteps();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
		studyName = studyDashboardSteps.selectStudyWithAllFilterValuesFor("Assessments");
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
			availableFilters = breadcrumbsSteps.getAvailableFiltersFor("Assessments");
			
			for (String filter : availableFilters) {
				Log.logInfo("Adding filter: " + filter);
				filterList.add(new Object[] { filter });
			}
		} else {
			throw new SkipException("No study found to meet the test criteria. Skipping tests...");
		}
	}
	
	/*
	 * Data Provider
	 *
	 */
	
	@DataProvider
	public Iterator<Object[]> filters() {
		return filterList.iterator();
	}
	
	/**
	 * Test the Breadcrumb Links as in SFB-TC-1124
	 * Steps : 
	 * 	1. Click on a Card from the Study Dashboard
	 *  2. Check if the number of Rows are correct
	 *  3. Click on the first item on the List
	 *  4. Check if the Study name is correct
	 *  5. Check if the Site Name is correct
	 *  6. Check if the filter Name is correct
	 * 
	 * @param filter
	 */
	@Test(groups = { "BreadcrumbLinks",
			"SFB-TC-1124" }, description = "Checks the Breadcrumb Links for the Assessment Details Page", dataProvider = "filters")
	public void breadcrumbLinksTest(String filter) {
		detailsSteps.getAssessmentsListFor(filter);
		if (detailsSteps.checkRowNumbers()) {
			detailsSteps.getSiteNameForFirstRow();
			detailsSteps.clickFirstItemFromAssessmetList();
			detailsSteps.verifyStudyName(studyName);
			detailsSteps.verifySiteName();
			detailsSteps.verifyFilterName(filter);
		}
	}
}
