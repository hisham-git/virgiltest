package steps.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.support.PageFactory;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.QueryPanelItem;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import steps.Configuration.CommonSteps;

public class StudyDashboardSteps extends CommonSteps{
	
	/**
	 * Selects a random Study from the list of Studies dropdown
	 * 
	 * @return String - name of the study selected
	 */
	public String selectRandomStudy(){
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		return dashboard.selectRandomStudy();
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least 1 query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the study selected
	 */
	public String selectStudyWithQueries() {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int count;
		int studiesAvailable = tbf.StudyNumberList.size();
		String selectedStudy;
		if (studiesAvailable > 1) {
			do {
				selectedStudy = tbf.chooseRandomStudy();
				count = Integer.parseInt(tbf.getQueriesCount());
			} while (count < 1);
			Log.logInfo("Study: " + selectedStudy + " found with Queries: " + count);
			return selectedStudy;
		} else if (studiesAvailable == 1) {
			selectedStudy = tbf.chooseRandomStudy();
			count = Integer.parseInt(tbf.getQueriesCount());
			if (count > 0) {
				Log.logInfo("Study: " + selectedStudy + " found with Queries: " + count);
				return selectedStudy;
			}
		}
		Log.logInfo("No Study found with Queries: ");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least max query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the study selected
	 */
	public String selectStudyWithMaxQueriesWithin(int minCount, int maxCount) {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int tempCount = 0;
		int finalCount = 0;
		int loc = 0;
		int studiesAvailable = tbf.StudyNumberList.size();
		String[] selectedStudy = new String[studiesAvailable];
		if (studiesAvailable > 0) {
			for (int i = 0; i < studiesAvailable; i++) {
				selectedStudy[i] = tbf.chooseStudyNo(i);
				tempCount = Integer.parseInt(tbf.getQueriesCount());
				if ((tempCount >= minCount) && (tempCount <= maxCount)) {
					finalCount = tempCount;
					loc = i;
					break; // Stop searching any more studies for queries
				}
			}
			if (finalCount > 0) {
				Log.logInfo("Study: " + selectedStudy[loc] + " found with Queries: " + finalCount);
				return selectedStudy[loc];
			}
			Log.logInfo("No Study found with Queries!");
			return null;
		}
		Log.logInfo("No Study found");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least max query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @param String type 
	 * 			- Query type(All/Subject/Visit/Assessment) to search for
	 * 
	 * @return String 
	 * 			- Name of the selected study
	 */
	public String selectStudyWithQueryType(String type, boolean searchAddQuery) {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);

		List<Boolean> found = new ArrayList<Boolean>(Arrays.asList(new Boolean[4]));
		Collections.fill(found, false);

		tbf.clickStudyDropdown();
		List<String> studiesAvailable = TextHelper.getElementTextContent(tbf.StudyNumberList);
		tbf.clickStudyDropdown();
		if (studiesAvailable.size() > 0) {
			for (String selectedStudy : studiesAvailable) {
				tbf.chooseStudy(selectedStudy);
				if (studiesAvailable.get(0).equalsIgnoreCase(selectedStudy)) {
					qSidePanel = tbf.openQueries();
					qSidePanel.checkPrimaryCheckBoxesTo(true);
				}

				if (searchAddQuery && false == qSidePanel.isAddQueryVisibleAndDisabled()) {
					continue; // go to next study
				}
				qSidePanel.refreshAndWait();
				List<QueryPanelItem> queries = qSidePanel.getQueriesFromList();

				if (queries.size() == 0) {
					Log.logInfo("Study: [" + selectedStudy + "] contains 0 query. Searching next...");
					continue;
				}

				for (QueryPanelItem query : queries) {
					switch (query.getQueryCategory()) {
					case "Subject":
						found.set(0, true);
						break;
					case "Visit":
						found.set(1, true);
						break;
					case "Assessment":
						found.set(2, true);
						break;
					case "Score":
						found.set(3, true);
						break;
					default:
						Log.logInfo("Not found all four possible types of queries.");
						break;
					}
				}

				switch (type) {
				case "All":
					if (!found.contains(false)) {
						Log.logInfo("Study: " + selectedStudy + " found with all 4 possible types of queries.");
						qSidePanel.clickClose();
						return selectedStudy;
					}
					break;
				case "Subject":
					if (found.get(0).equals(true)) {
						Log.logInfo("Study: " + selectedStudy + " found with Subject query.");
						qSidePanel.clickClose();
						return selectedStudy;
					}
					break;
				case "Visit":
					if (found.get(1).equals(true)) {
						Log.logInfo("Study: " + selectedStudy + " found with Visit query.");
						qSidePanel.clickClose();
						return selectedStudy;
					}
					break;
				case "Assessment":
					if (found.get(2).equals(true)) {
						Log.logInfo("Study: " + selectedStudy + " found with Assessment query.");
						qSidePanel.clickClose();
						return selectedStudy;
					}
					break;
				case "Score":
					if (found.get(3).equals(true)) {
						Log.logInfo("Study: " + selectedStudy + " found with Score query.");
						qSidePanel.clickClose();
						return selectedStudy;
					}
					break;
				default:
					Log.logInfo("Not found any possible types of queries.");
					break;
				}
			}
			qSidePanel.clickClose();
			Log.logInfo("No Study found with all four possible types of queries!!!");
			return null;
		}
		Log.logInfo("No Study found!!!");
		return null;
	}
	/*public String selectStudyWithQueryType(String type, boolean searchAddQuery) {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);

		List<Boolean> found = new ArrayList<Boolean>(Arrays.asList(new Boolean[4]));
		Collections.fill(found, false);
		
		tbf.clickStudyDropdown();
		List<String> studiesAvailable = TextHelper.getElementTextContent(tbf.StudyNumberList);
		tbf.clickStudyDropdown();
		if (studiesAvailable.size() > 0) {
			for (String selectedStudy : studiesAvailable) {
				tbf.chooseStudy(selectedStudy);
				qSidePanel = tbf.openQueries();
				
				if (searchAddQuery && false == qSidePanel.isAddQueryVisibleAndDisabled()) {
					qSidePanel.clickClose();
					continue; // go to next study
				}
				
				qSidePanel.checkPrimaryCheckBoxesTo(true);
				List<QueryPanelItem> queries = qSidePanel.getQueriesFromList();
				if (queries.size() > 0) {
					for (QueryPanelItem query : queries) {
						switch (query.getQueryCategory()) {
						case "Subject":
							found.set(0, true);
							break;
						case "Visit":
							found.set(1, true);
							break;
						case "Assessment":
							found.set(2, true);
							break;
						case "Score":
							found.set(3, true);
							break;
						default:
							Log.logInfo("Not found all four possible types of queries.");
							break;
						}
					}

					switch (type) {
					case "All":
						if (!found.contains(false)) {
							Log.logInfo(
									"Study: " + selectedStudy + " found with all four possible types of queries.");
							qSidePanel.clickClose();
							return selectedStudy;
						}
						break;
					case "Subject":
						if (found.get(0).equals(true)) {
							Log.logInfo("Study: " + selectedStudy + " found with Subject query.");
							qSidePanel.clickClose();
							return selectedStudy;
						}
						break;
					case "Visit":
						if (found.get(1).equals(true)) {
							Log.logInfo("Study: " + selectedStudy + " found with Visit query.");
							qSidePanel.clickClose();
							return selectedStudy;
						}
						break;
					case "Assessment":
						if (found.get(2).equals(true)) {							
							Log.logInfo("Study: " + selectedStudy + " found with Assessment query.");
							qSidePanel.clickClose();
							return selectedStudy;
						}
						break;
					case "Score":
						if (found.get(3).equals(true)) {							
							Log.logInfo("Study: " + selectedStudy + " found with Score query.");
							qSidePanel.clickClose();
							return selectedStudy;
						}
						break;
					default:
						Log.logInfo("Not found any possible types of queries.");
						break;
					}
				}
				qSidePanel.clickClose();
			}
			qSidePanel.clickClose();
			Log.logInfo("No Study found with all four possible types of queries!!!");
			return null;
		}
		Log.logInfo("No Study found!!!");
		return null;
	}*/
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least max query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the study selected
	 */
	public String selectStudyWithMaxForms() {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int tempCount = 0;
		int finalCount = 0;
		int loc = 0;
		tbf.clickStudyDropdown();
		int studiesAvailable = tbf.StudyNumberList.size();
		tbf.clickStudyDropdown();
		String[] selectedStudy = new String[studiesAvailable];
		if (studiesAvailable > 0) {
			for (int i = 0; i < studiesAvailable; i++) {
				selectedStudy[i] = tbf.chooseStudyNo(i);
				Map<String, String> nameCount = dashboard.getCategoryNameValueFor("Assessments");
				tempCount = Integer.parseInt(nameCount.get("Assessments"));
				if ((tempCount > finalCount)) {
					finalCount = tempCount;
					loc = i;
				}
			}
			if (finalCount > 0) {
				Log.logInfo("Study: [" + selectedStudy[loc] + "] found with Assessments: " + finalCount);
				return selectedStudy[loc];
			}
			Log.logInfo("No Study found with Assessments!");
			return null;
		}
		Log.logInfo("No Study found");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * Description:
	 * This method selects a study which has value greater than zero for all filters
	 * for a given category(i.e. Subjects/Visits/Assessments) 
	 * 
	 * 1. Selects a Study from the list of Studies dropdown
	 * 2. Checks for all filter values not equal to zero for given category for the selected Study
	 * 3. This procedure continues for all studies available
	 * 4. Returns name of the study if all filters for given category has at least one item, null otherwise
	 * 
	 * @param String category 
	 * 			- Name of the category(i.e. Subjects/Visits/Assessments) to calculate values
	 * @return String 
	 * 			- Name of the study which has all filter values for given category
	 */
	public String selectStudyWithAllFilterValuesFor(String category) {
		Log.logInfo("Trying to select a study with all filter values for category: " + category);

		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);

		tbf.clickStudyDropdown();
		List<String> studiesAvailable = TextHelper.getElementTextContent(tbf.StudyNumberList);
		tbf.clickStudyDropdown();
		if (studiesAvailable.size() > 0) {
			for (String selectedStudy : studiesAvailable) {
				boolean allFilterHasValue = true;
				tbf.chooseStudy(selectedStudy);
				Map<String, String> filterNameValues = dashboard.getCategoryFiltersNameValueFor(category);
				Iterator<String> itr = filterNameValues.keySet().iterator();
				while (itr.hasNext()) {
					int value = Integer.parseInt(filterNameValues.get(itr.next()));
					if (value < 1) {
						allFilterHasValue = false;
						break;
					}
				}
				if (allFilterHasValue) {
					Log.logInfo("Study: [" + selectedStudy + "] found with all filter values for : " + category);
					return selectedStudy;
				}
			}
			Log.logInfo("No study found with all filter values for : " + category);
			return null;
		}
		Log.logInfo("No Study found");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * Description:
	 * This method selects a study which has maximum value for each filters
	 * for a given category(i.e. Subjects/Visits/Assessments) 
	 * 
	 * 1. Selects a Study from the list of Studies dropdown
	 * 2. Checks for each filter maximum value for given category for the selected Study
	 * 3. This procedure continues for all studies available
	 * 4. Returns name of the study if each filter has maximum value for given category, null otherwise
	 * 
	 * @param String category 
	 * 			- Name of the category(i.e. Subjects/Visits/Assessments) to calculate values
	 * @return String 
	 * 			- Name of the study which has maximum value for each filters for given category
	 */
	public String selectStudyWithMaxFilterValuesForAllCategories() {
		Log.logInfo("Trying to select a study with maximum filter values for all categories");
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		String selectedStudy = null;

		tbf.clickStudyDropdown();
		List<String> studiesAvailable = TextHelper.getElementTextContent(tbf.StudyNumberList);
		tbf.clickStudyDropdown();
		if (studiesAvailable.size() > 0) {
			Map<String, Integer> studyFilterMean = new HashMap<String, Integer>();
			for (String study : studiesAvailable) {
				tbf.chooseStudy(study);
				Map<String, String> subjectFiltersValue = dashboard.getCategoryFiltersNameValueFor("Subjects");
				Map<String, String> visitFiltersValue = dashboard.getCategoryFiltersNameValueFor("Visits");
				Map<String, String> formFiltersValue = dashboard.getCategoryFiltersNameValueFor("Assessments");
				int subjectFiltersFound = subjectFiltersValue.size();
				int visitFiltersFound = visitFiltersValue.size();
				int formFiltersFound = formFiltersValue.size();
				
				if (subjectFiltersFound < 5 || visitFiltersFound < 3 || formFiltersFound < 5) {
					Log .logInfo("Study [" + study + "] doesn't have all filters for all 3 categories. Searching on next study...");
					continue;
				}

				Iterator<String> sbjItr = subjectFiltersValue.keySet().iterator();
				Iterator<String> visitItr = visitFiltersValue.keySet().iterator();
				Iterator<String> formItr = formFiltersValue.keySet().iterator();
				int sbjFilterTotalValue = 0;
				int visitFilterTotalValue = 0;
				int formFilterTotalValue = 0;

				while (sbjItr.hasNext()) {
					sbjFilterTotalValue = sbjFilterTotalValue
							+ Integer.parseInt(subjectFiltersValue.get(sbjItr.next()));
				}
				int sbjFilterMeanValue = sbjFilterTotalValue / subjectFiltersFound;
//				int sbjFilterMeanValue = sbjFilterTotalValue / 6;

				while (visitItr.hasNext()) {
					visitFilterTotalValue = visitFilterTotalValue
							+ Integer.parseInt(visitFiltersValue.get(visitItr.next()));
				}
				int visitFilterMeanValue = visitFilterTotalValue / visitFiltersFound;
//				int visitFilterMeanValue = visitFilterTotalValue / 4;

				while (formItr.hasNext()) {
					formFilterTotalValue = formFilterTotalValue
							+ Integer.parseInt(formFiltersValue.get(formItr.next()));
				}
				int formFilterMeanValue = formFilterTotalValue / formFiltersFound;
//				int formFilterMeanValue = formFilterTotalValue / 6;
				int totalMeanValue = (sbjFilterMeanValue + visitFilterMeanValue + formFilterMeanValue) / 3;
				Log.logInfo("Study: [" + study + "] has mean filter value for all 3 categories as: " + totalMeanValue);
				studyFilterMean.put(study, totalMeanValue);
			}

			Iterator<String> itr = studyFilterMean.keySet().iterator();
			int maxMeanValue = 0;
			while (itr.hasNext()) {
				String study = itr.next();
				int meanValue = studyFilterMean.get(study);
				if (meanValue > maxMeanValue) {
					maxMeanValue = meanValue;
					selectedStudy = study;
				}
			}
			if (null != selectedStudy) {
				Log.logInfo("Study: [" + selectedStudy + "] found with maximum filter values for all categories");
				return selectedStudy;
			}
		}
		Log.logInfo("No Study found");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least max query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the study selected
	 */
	public String selectStudyWithItemsForCategoryFilter(String category, String filter) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int tempCount = 0;
		int finalCount = 0;
		int loc = 0;
		int studiesAvailable = tbf.StudyNumberList.size();
		String[] selectedStudy = new String[studiesAvailable];
		if (studiesAvailable > 0) {
			for (int i = 0; i < studiesAvailable; i++) {
				selectedStudy[i] = tbf.chooseStudyNo(i);
				tempCount = dashboard.getCountFor(category, filter);
				if ((tempCount > finalCount)) {
					finalCount = tempCount;
					loc = i;
				}
			}
			if (finalCount >= 3) {
				Log.logInfo("Study: " + selectedStudy[loc] + " found containing: " + finalCount + " items for "
						+ category + " - " + filter);
				return selectedStudy[loc];
			}
			Log.logInfo("No Study found with items!");
			return null;
		}
		Log.logInfo("No Study found");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Site from the list of Sites dropdown
	 * 2. Checks for at least 1 query for the selected Site
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the site selected
	 */
	public String selectSiteWithQueries() {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int count;
		int sitesAvailable = tbf.getSiteCount();
		String selectedSite;
		if (sitesAvailable > 1) {
			do {
				selectedSite = tbf.clickRandomSite();
				count = Integer.parseInt(tbf.getQueriesCount());
			} while (count < 1);
			Log.logInfo("Site: " + selectedSite + " found with Queries: " + count);
			return selectedSite;
		} else if (sitesAvailable == 1) {
			selectedSite = tbf.clickRandomSite();
			count = Integer.parseInt(tbf.getQueriesCount());
			if (count > 0) {
				Log.logInfo("Site: " + selectedSite + " found with Queries: " + count);
				return selectedSite;
			}
		}
		Log.logInfo("No Site found with Queries: ");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least max query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the study selected
	 */
	public String selectSiteWithMaxQueries() {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int count = 0;
		int loc = 0;
		int sitesAvailable = tbf.getSiteCount();
		String[] selectedSite = new String[sitesAvailable];
		if (sitesAvailable > 0) {
			for (int i = 0; i < sitesAvailable; i++) {
				selectedSite[i] = tbf.clickSiteNo(i);
				if (Integer.parseInt(tbf.getQueriesCount()) > count) {
					count = Integer.parseInt(tbf.getQueriesCount());
					loc = i;
				}
			}
			if (count > 0) {
				Log.logInfo("Site: " + selectedSite[loc] + " found with Queries: " + count);
				return selectedSite[loc];
			}
			Log.logInfo("No Site found with Queries!");
			return null;
		}
		Log.logInfo("No Site found!");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * 1. Selects a random Study from the list of Studies dropdown
	 * 2. Checks for at least max query for the selected Study
	 * 3. Returns true if found, false otherwise
	 * 
	 * @return String - name of the study selected
	 */
	public String selectSiteWithMaxForms() {
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int count = 0;
		int loc = 0;
		int sitesAvailable = tbf.getSiteCount();
		String[] selectedSite = new String[sitesAvailable];
		if (sitesAvailable > 0) {
			for (int i = 0; i < sitesAvailable; i++) {
				selectedSite[i] = tbf.clickSiteNo(i);
				Map<String, String> nameCount = dashboard.nameCountHolder();
				int tempCount = Integer.parseInt(nameCount.get("Assessments"));
				if (tempCount > count) {
					count = tempCount;
					loc = i;
				}
			}
			if (count > 0) {
				Log.logInfo("Site: " + selectedSite[loc] + " found with Assessments: " + count);
				return selectedSite[loc];
			}
			Log.logInfo("No Site found with Assessments!");
			return null;
		}
		Log.logInfo("No Site found!");
		return null;
	}
	
	/**
	 * Description: 
	 * The following method selects a site which has maximum items for a given category filter
	 * 
	 * @author HISHAM
	 * 
	 * @param String category 
	 * 			- category(Subjects/Visits/Assessments) for dashboard filters 
	 * @param String selectedFilter
	 * 			- filter(new/pending/complete etc.) for the category
	 * 
	 * @return String selectedSite
	 * 			- Name of the found site with maximum items for a given category filter
	 */
	public String selectSiteForFilterWithValues(String category, String selectedFilter) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		ToolBarFull tbf = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		int count = 0;
		int loc = 0;
		int sitesAvailable = tbf.getSiteCount();
		String[] selectedSite = new String[sitesAvailable];
		Map<String, String> filtersNameCount;

		if (sitesAvailable > 0) {
			for (int i = 0; i < sitesAvailable; i++) {
				selectedSite[i] = tbf.clickSiteNo(i);
				if (selectedSite[i].equalsIgnoreCase("All Sites")) {
					continue;
				} else {
					filtersNameCount = dashboard.getCategoryFiltersNameValueFor(category);
					int tempCount = Integer.parseInt(filtersNameCount.get(selectedFilter));
					if (tempCount > count) {
						count = tempCount;
						loc = i;
						break;
					}
				}
			}
			if (count > 0) {
				Log.logInfo("Site: [" + selectedSite[loc] + "] found with Filter: [" + selectedFilter + "] with value: "
						+ count);
				return selectedSite[loc];
			}
			Log.logInfo("No Site found with Filter value!");
			return null;
		}
		Log.logInfo("No Site found!");
		return null;
	}
	
	/**
	 * Selects a random Site from the list of Sites dropdown
	 * 
	 * @return String - name of the site selected
	 */
	public String selectRandomSite(){
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		return dashboard.selectRandomSite();
	}
	
	/**
	 * Selects a random card from all the available cards in the Study Dashboard
	 * 
	 * @return String - the name of the card selected
	 */
	public String selectRandomCard(){
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		String card = dashboard.getRandomCard();
		Log.logStep("Clicking on a random card -> [" + card + "]");
		dashboard.clicksOnCard(card);
		return card;
	}
	
	/**
	 * @author HISHAM
	 * 
	 */
	public String selectRandomCardForCategory(String category) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		String card = dashboard.getRandomFilterForCategory(category);
		Log.logStep("Clicking on a random card for Category: [" + category + "]");
		dashboard.clicksOnCard(card);
		Log.logStep("Clicked on a random card " + "[" + card + "]");
		return card;
	}
	
	/**
	 * Clicks on the given card from the dashboard cards
	 * 
	 * @param type
	 * @param filter
	 * @return boolean - true if correctly redirected, false otherwise
	 */
	public boolean selectDashboardCard(String type, String filter){
		return goToList(type+"-"+filter);
	}

	/**
	 * Clicks on the given card from the dashboard cards
	 * 
	 * @param Parameters
	 *            - String - Should be of the format {LIST_TYPE}-{FILTER}
	 * @return boolean - true if correctly redirected, false otherwise
	 */
	public boolean goToList(String paramCard) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		dashboard.clicksOnCard(paramCard);
		return !dashboard.isDashboardOpened();
	}
	
	
}
