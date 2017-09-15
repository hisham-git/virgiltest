package steps.Tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.DashboardCards;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import steps.Abstract.AbstractStep;
import steps.Configuration.CommonSteps;

public class ChartsViewSteps extends CommonSteps {
	private List<DashboardCards> dsCards;
	private List<String> sitesToCycleThrough;
	private Map<String, List<DashboardCards>> siteToCardMap;
	private String category, filter;
	private int count;
	private DashboardList dashboardList;
	
	public ChartsViewSteps() {
		dashboard = PageFactory.initElements(Browser.getDriver(),
                Dashboard.class);
		toolbarFull = PageFactory.initElements(Browser.getDriver(),
				ToolBarFull.class);
	}
	
	public void refreshDashboard() {
		dashboard.clickRefresh();
	}
	
	public void changeView(String view) {
		if ( false == dashboard.getCurrentViewMode().equalsIgnoreCase(view) ) {
			dashboard.toggleViewModeTo(view);
		}
	}
	
	public void getCardViewItems() {
		dsCards = dashboard.getCardList();
	}
	
	public void verifyChartsViewData() {
		HardVerify.True(dashboard.validateChartsData(dsCards),
				"Test if the Data on the Chart Cards are consistent with the Data on the Dashboard Card's View Mode...",
				"[TEST PASSED]", "Data was not consistent.");
		HardVerify.True(dashboard.validatePercentages(),
				"Test if the % values on the Chart Cards are correctly calculated...", "[TEST PASSED]",
				"Values were not correct");
	}
		
	public void collectCardViewDataForEverysite() {
		sitesToCycleThrough = toolbarFull.getSites();
		siteToCardMap = new HashMap<String, List<DashboardCards>>();
		Log.logStep("Generating Cards List for the sites -> " + sitesToCycleThrough.toString());
		for (String site : sitesToCycleThrough) {
			dashboard.selectSite(site);
			siteToCardMap.put(site, dashboard.getCardList());
			Log.logStep("Generated Cards List for Site : " + site);
		}
	}
	
	public void verifyChartsViewDataForEverySite() {
		Log.logStep("Looping through every site...");
		for (String site : sitesToCycleThrough) {
			dashboard.selectSite(site);
			HardVerify.True(dashboard.isChartsMode(),
					"Test if the Chart View Mode is active even after changing Sites...", "[TEST PASSED]",
					"The Dashboard View Mode was changed to " + dashboard.getCurrentViewMode());
			Log.logStep("Testing charts for Site : " + site);
			HardVerify.True(dashboard.validateChartsData(siteToCardMap.get(site)),
					"Test if the Chart's Data are consistent after changing Sites (for site -> " + site + ")...",
					"[TEST PASSED]", "The Chart's Data was not correct.");
		}
	}
	
	public void selectRandomCategoryFilter() {
		category = dashboard.getRandomCategory();
		filter = dashboard.getRandomFilterForCategory(category);
		count = dashboard.getCountFor(category, filter);
	}

	public void clickOnRandomFilter() {
		Log.logStep("Clicking on the filter : " + category + " - " + filter);
		dashboardList = dashboard.clickOnChartCardFilter(category, filter);
	}

	public void verifyDashboardListOpenedWithCorrectType() {
		HardVerify.Equals(category, dashboardList.getListTypeName(),
				"Test if the Dashboard List with of the correct type is opened...", "[TEST PASSED]",
				"The correct List was not opened. Expected [" + category + "] but was ["
						+ dashboardList.getListTypeName() + "]");
	}

	public void verifyDashboardListOpenedWithCorrectFiter() {
		HardVerify.EqualsIgnoreCase(filter, dashboardList.getFilter(),
				"Test if the Dashboard List with the correct filter is opened...", "[TEST PASSED]",
				"The correct Filter was not opened. Expected [" + filter + "] but was [" + dashboardList.getFilter()
						+ "]");
	}

	public void verifyDashboardListHasCorrectItems() {
		HardVerify.Equals(count, dashboardList.getListCount(),
				"Test if the Dashboard List has the expected number of items...", "[TEST PASSED]",
				"The correct number was not given. Expected [" + count + "] but was [" + dashboardList.getListCount()
						+ "]");
	}
		
}
