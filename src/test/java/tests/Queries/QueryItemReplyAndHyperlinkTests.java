package tests.Queries;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * QueryItemControlsTests
 * Automation Scope -> Queries Side Bar -> Query Entry, Expand/Collapse button, Textbox
 * 
 * Status : 100% COMPLETE
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueryItemReplyAndHyperlink" })
public class QueryItemReplyAndHyperlinkTests extends AbstractQueries {
	
	//The String to use as the Default Text Keyboard Entry for the Reply Text Box
	private final String textarea_text="Automated Query Reply : " + UiHelper.generateRandonUUIDString();
	
	/**
	 * Before every test method: 
	 * 1. Open Query Panel
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		queriesSteps.openQuerySidePanel();
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Query Entry -> Accessibility and HyperLink
	 * 
	 * Objective - Checks a query's accessibility and onClick actions
	 * Steps : 
	 * 	1. Select TRUE for every Filter
	 * 	2. For every query in the panel
	 * 		-> Check if Displayed
	 * 		-> Check if Enabled
	 * 		-> Check if Clicking fires a Javascript Event (Substitute for a hyperlink, since AngularJS is being used to actually navigate) 
	 */
	@Test(groups = { "QueryItemAccessability",
			"JamaNA" }, description = "Checks if a random query in the Query Panel List is Accessible/Viewable, and Hyperlinked")
	public void queryItemAccessabilityTest() {
		queriesSteps.selectQueriesCheckBox(true, "All");	
		queriesSteps.getRandomQueryFromPanel();
		queriesSteps.verifyQueryItemVisibility();
		queriesSteps.verifyQueryItemClickability();
		queriesSteps.verifyClickEventContainsJavaScriptCall();
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Query Entry -> Age and Context Checks
	 * 
	 * Objective - Checks a query's Age and Context
	 * Steps : 
	 * 	1. Select TRUE for every Filter
	 * 	2. For every query in the panel
	 * 		-> Check if Age shows correctly
	 * 		-> Check if Subject is displayed
	 * 		-> Check if Site is displayed
	 */
	@Test(groups = { "QueryItemAgeAndContext",
			"JamaNA" }, description = "Checks if a random query in the Query Panel List has the minimum context and the correct age.")
	public void queryItemAgeAndContextTest() {
		queriesSteps.selectQueriesCheckBox(true, "All");
		queriesSteps.getRandomQueryFromPanel();
		queriesSteps.verifyQueryItemMetaData();
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Expand/Collapse Button, Text Box and Reply Button
	 * 
	 * Objective - Checks a random query's Expand/Collapse Button, Text Box and Reply Button functionalities
	 * Steps : 
	 * 	 1. Select TRUE for every Filter
	 * 	 2. Select FALSE for Closed Filter
	 * 	 3. Select a random query in the panel
	 * 	 4. Check if the Expand Button is available
	 * 	 5. Click the Expand Button
	 * 	 6. Check if the Reply Textbox exists, is visible and enabled
	 * 	 7. Check if the Reply Textbox is empty by default
	 * 	 8. Check if the Reply button exists, is visible and disabled
	 * 	 9. Enter some text in the Reply Textbox
	 * 	10. Check if the Reply Button is enabled
	 * 	11. Check if the Collapse Button exists
	 * 	12. Click the Collapse Button
	 * 	13. Check if the Text Area or Reply button are invisible.
	 */
	@Test(groups = { "QueryItemExpandReplyButtonAndTextBox",
			"JamaNA" }, description = "Checks a random Query in the Query Panel List for the correct Expand Query Button, Reply Button and Text Box functionality")
	public void queryItemExpandReplyButtonAndTextBoxTest() {
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
		queriesSteps.selectQueriesCheckBox(true, "Open", "Responded", "Mine");
		queriesSteps.createNewQueryIfUnavailable();
		queriesSteps.getRandomQueryFromPanel();
		queriesSteps.verifyQueryItemExpandButton();
		queriesSteps.expandRandomQueryItem();
		queriesSteps.verifyQueryItemReplyTextBox();
		queriesSteps.verifyQueryItemReplyButton();
		queriesSteps.fillupReplyTextBox(textarea_text);
		queriesSteps.verifyEnabledReplyButton();
		queriesSteps.verifyQueryItemCollapseButton();
		queriesSteps.collapseRandomQueryItem();
		queriesSteps.verifyQueryItemHiddenReplyTextBox();
		queriesSteps.verifyQueryItemHiddenReplyButton();
		queriesSteps.verifyQueryItemExpandButton();
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Text Box and Reply Button
	 * 
	 * Objective - Checks a random query's presence of the Text Box and Reply Button for Closed Queries
	 * Steps : 
	 * 	 1. Select FALSE for every Filter
	 * 	 2. Select TRUE for Closed Filter
	 * 	 3. Select a random query in the panel
	 * 	 5. Click the Expand Button
	 * 	 6. Check if the Reply Textbox exists
	 * 	 8. Check if the Reply button exists
	 */
	@Test(groups = { "ReplyOnClosedQueries",
			"JamaNA" }, description = "Checks a random Closed Query in the Query Panel List for the correct Reply Button and Text Box functionalities")
	public void replyOnClosedQueriesTest() {
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
		queriesSteps.selectQueriesCheckBox(true, "Closed");
		queriesSteps.createClosedQueryIfUnavailable();
		queriesSteps.getRandomQueryFromPanel();
		queriesSteps.expandRandomQueryItem();
		queriesSteps.verifyInvisibleReplyTextBox();
		queriesSteps.verifyInvisibleReplyButton();
	}
	
	/*
	 * Need a different user for this
	 */
	/**
	 * Test Method
	 * Queries Side Bar -> Text Box and Reply Button
	 * 
	 * Objective - Checks a random Assessment or Score category Query in the Query Panel List for the presence of the Reply Button and Text Box using an unqualified User
	 * Steps : 
	 * 	 1. Select TRUE for every Filter
	 * 	 2. Make a shortlist of all the Assessment and Score Category Queries
	 * 	 3. Select a random query in the shortlist
	 * 	 5. Click the Expand Button
	 * 	 6. Check if the Reply Textbox exists
	 * 	 8. Check if the Reply button exists
	 */
	/*@Test(groups = { "ReplyButtonAndTextBoxForUnqualifiedUser",
			"JamaNA" }, description = "Checks a random Assessment or Score category Query in the Query Panel List for the presence of the Reply Button and Text Box using an unqualified User", dependsOnGroups = {
					"QueryItemExpandReplyButtonAndTextBox" })
	public void replyButtonAndTextBoxForUnqualifiedUserTest() {
		//TODO : RELOG and navigate to the Query Panel with an unqualified user
		queriesSteps.reLoginWithUnqualifiedUser();
		afterSteps.logout();
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
		beforeSteps.chooseStudy(studyName);
		beforeSteps.chooseSite(siteName);
			
			
		panel.checkAllCheckBoxesTo(true);
		Log.logStep("Creating a short-list of Assessment and Score Level queries");
		List<WebElement> scoreAndAssessmentQueriesOnly = new ArrayList<WebElement>();
		List<WebElement> queries = panel.getAllQueries();
		for (WebElement query : queries) {
			QueryPanelItem q = new QueryPanelItem(query);
			if (q.getQueryCategory().equals("steps/Assessment") || q.getQueryCategory().equals("Score"))
				scoreAndAssessmentQueriesOnly.add(query);
		}
		Log.logInfo("Found " + scoreAndAssessmentQueriesOnly.size() + " Assessment and Score Category Queries.");
		int random_index = (int) Math.floor(Math.random() * (scoreAndAssessmentQueriesOnly.size()));
		Log.logInfo("Getting a random number for query. Got : " + random_index);
		QueryPanelItem item = new QueryPanelItem(scoreAndAssessmentQueriesOnly.get(random_index));
		item.expand();
		Log.logStep("Test that the random query does not have a Reply Text Box...");
		HardVerify.Null(item.getReplyTextBox(), "A Reply Text Area was found for query : "
				+ (random_index + 1) + " after the Expand Button was clicked for a Closed Query.");
		Log.logStep("Test Passed.");
		Log.logStep("Test that the random query does not have a Reply Button...");
		HardVerify.Null(item.getReplyButton(), "A Reply Button was found for query : "
				+ (random_index + 1) + " after the Expand Button was clicked for a Closed Query.");
		Log.logStep("Test Passed.");
	}*/
	
	/**
	 * Test Method
	 * Queries Side Bar -> Text Box and Reply Button
	 * 
	 * Objective - Checks if a Reply correctly gets posted on a random Open or Responded Query in the Query Panel List
	 * Steps : 
	 * 	 1. Select TRUE for every Filter
	 * 	 2. Select FALSE for Closed Filter
	 * 	 3. Select a random Query on which to post a reply to
	 * 	 4. Expand the Query
	 * 	 5. Enter the predefined text in the Reply Textbox
	 * 	 6. Click Reply Button
	 * 	 7. Check the Reply Thread for the Latest Query's Poster Name
	 * 	 8. Check the Reply Thread for the Latest Query's Text
	 */
	@Test(groups = { "PostedQueryReply",
			"JamaNA" }, description = "Checks if a Reply correctly gets posted on a random Open or Responded Query in the Query Panel List")
	public void postedQueryReplyTest() {
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
		queriesSteps.selectQueriesCheckBox(false, "Closed");
		queriesSteps.createNewQueryIfUnavailable();
		queriesSteps.getRandomQueryFromPanel();
		queriesSteps.expandRandomQueryItem();
		queriesSteps.fillupReplyTextBox(textarea_text);
		queriesSteps.clickReplyForRandomQueryItem();
//		queriesSteps.expandRandomQueryItem();
		queriesSteps.verifyLatestReplyUserName();
		queriesSteps.verifyLatestReplyText(textarea_text);
	}
}
