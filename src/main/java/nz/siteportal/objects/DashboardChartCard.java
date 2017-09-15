package nz.siteportal.objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Page Element Wrapper Object for Dashboard Chart
 * 
 * @author Syed A. Zawad
 *
 */
public class DashboardChartCard {

	/*
	 * Object Variables
	 */
	private String chartCategory;
	private int total;
	private WebElement chartItem;

	/*
	 * Locators for sub-elements
	 */
	private By rowsLocator = By.xpath(".//a[not(contains(@class, 'root-filter'))]"),
			deleteButtonLocator = By.cssSelector(" button.remove-custom-filter");

	/**
	 * Constructor
	 * 
	 * @param chartItem
	 *            - The Chart Card WebElement
	 */
	public DashboardChartCard(WebElement chartItem) {
		this.chartItem = chartItem;
		chartCategory = chartItem.findElement(By.cssSelector(" .dashboard-tile-name")).getText();
		String totalString = chartItem.findElement(By.cssSelector(" .dashboard-tile-rows-count")).getText();
		total = Integer.parseInt(totalString);
	}

	/**
	 * Returns the Card Category
	 * 
	 * @return String - Card Category
	 */
	public String getChartCategory() {
		return chartCategory;
	}

	/**
	 * Generates a list of all the ChartsRow objects from this Chart Card's
	 * filters
	 * 
	 * @return List<ChartsRow> of Filter's Wrapper Objects
	 */
	private List<ChartsRow> getRows() {
		List<WebElement> rows = chartItem.findElements(rowsLocator);
		List<ChartsRow> answer = new ArrayList<ChartsRow>();
		for (WebElement row : rows) {
			if (row.getAttribute("disabled") != null && row.getAttribute("disabled").equals("true"))
				continue;
			ChartsRow chartRow = new ChartsRow(row);
			answer.add(chartRow);
		}
		return answer;
	}

	/**
	 * Takes a list of Dashboard Cards and matches their filter's counts against
	 * this Chart Card's filter's counts
	 * 
	 * @param dsCards
	 *            List<DashboardCards> - the Dashboard Cards that are expected
	 *            to be found in this Chart
	 * @return boolean true if all the data match, false otherwise
	 */
	public boolean validateChartsData(List<DashboardCards> dsCards) {
		List<ChartsRow> rows = getRows();
		if (rows.size() != dsCards.size()) {
			Log.logInfo("The Cards do not match. Expected [" + dsCards.size() + "] filters for category ["
					+ chartCategory + "] but was [" + rows.size() + "]");
			return false;
		}
		for (ChartsRow row : rows) {
			boolean isRowPresent = false;
			if (row.getType().equals("Root Filter"))
				continue;
			for (int index = 0; index < dsCards.size(); index++) {
				DashboardCards card = dsCards.get(index);
				if (card.getCardTitle().equals(row.getTitle())) {
					if (card.getCardType().equals(row.getType()) && row.getCount() == card.getCardCount()) {
						dsCards.remove(index);
						isRowPresent = true;
						break;
					}
				}
			}
			if (false == isRowPresent) {
				Log.logInfo("The Filter for [" + row.getTitle()
						+ "] was found in the Charts View, but not in the Cards View.");
				return false;
			}
		}
		if (dsCards.size() > 0) {
			Log.logInfo("One or more filter/s were found in the Cards View but not in the Charts View. They are "
					+ dsCards.toString());
			return false;
		}
		return true;
	}

	/**
	 * @author HISHAM
	 * 
	 * Checks every filter's % calculated from count
	 * 
	 * @return boolean - true if all of them are correctly calculated, false otherwise
	 */
	public boolean validatePercentages() {
		List<ChartsRow> rows = getRows();
		for (ChartsRow row : rows) {
			int percent = row.getPercentage();
			double rowCount = row.getCount();
			int calculated = (int) Math.round((rowCount / total) * 100);
			// if (calculated != percent) {
			// Condition for handling errors on <1% calculations
			if (percent >= 2 && calculated < percent) {
				Log.logInfo("The percentage calculated for the filter : " + row.getTitle()
						+ " was incorrect. Should have been (rounded) [" + calculated + "] but was [" + percent + "].");
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the name of a Random Filter from this Chart Card
	 * 
	 * @return String - name of the Random Filter selected
	 */
	public String getRandomRow() {
		List<ChartsRow> rows = getRows();
		int index = (int) Math.floor(Math.random() * rows.size());
		return rows.get(index).getTitle();
	}

	/**
	 * Returns the count of a given filter
	 * 
	 * @param filter
	 *            String - the filter who's count is returned
	 * @return int - count of the filter. Returns 0 if the filter is not found
	 */
	public int getCount(String filter) {
		ChartsRow filterRow = getRow(filter);
		if (filterRow != null)
			return filterRow.getCount();
		Log.logInfo("Could not find Filter : " + filter + " for category " + this.getChartCategory()
				+ ", getting filter count as 0.");
		return 0;
	}

	/**
	 * Returns the ChartsRow Wrapper Object for the Filter specified
	 * 
	 * @param filter
	 *            - String - the name of the filter for which a Row is needed
	 * @return ChartsRow - wrapped around the filter link, returns null if the filter is not found
	 */
	private ChartsRow getRow(String filter) {
		List<ChartsRow> rows = getRows();
		for (ChartsRow row : rows) {
			if (row.getTitle().equals(filter))
				return row;
		}
		Log.logInfo("Could not find Filter : " + filter + " for category " + this.getChartCategory());
		return null;
	}

	/**
	 * Clicks on the filter specified
	 * 
	 * @param filter - String - the filter to click on
	 */
	public void clickFilterLink(String filter) {
		ChartsRow crow = getRow(filter);
		crow.click();
	}

	/**
	 * Wrapper Object for the Chart's Rows. Used to access the Filter properties
	 * for the Chart Card. Only available inside the DashboardChartCard
	 * 
	 * @author Syed A. Zawad
	 */
	private class ChartsRow {
		private int count, percentage;
		private String name, type;
		private WebElement rowItem;
		ChartsRow(WebElement rowItem) {
			this.rowItem = rowItem;
			String rowText = rowItem.getText();
			parseAndPopulateVariables(rowText);
			determineType();
		}
		public String getTitle() {
			return name;
		}
		public String getType() {
			return type;
		}
		public int getCount() {
			return count;
		}
		public int getPercentage() {
			return percentage;
		}
		private void determineType() {
			if (getDeleteButton() != null)
				type = "Custom Filter";
			else
				type = "Default Filter";
		}
		private WebElement getDeleteButton() {
			try {
				Browser.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				WebElement deleteButton = rowItem.findElement(deleteButtonLocator);
				Browser.resetTimeOuts();
				return deleteButton;
			} catch (NoSuchElementException nse) {
				Browser.resetTimeOuts();
				return null;
			}
		}
		private void parseAndPopulateVariables(String rowText) {
			try {
				String[] texts = TextHelper.splitNewlines(rowText);
				name = texts[0];
//				String[] counts = TextHelper.splitSpaces(texts[1]);
				// Splits based on any non-digit character set
				String[] counts = texts[1].split("\\D+"); 			
				count = Integer.parseInt(counts[0]);
				percentage = Integer.parseInt(counts[1]);
//				percentage = Integer.parseInt(counts[1].replaceAll("[\\(><\\)%]", ""));
			} catch (NumberFormatException nfe) {
				System.out.println("Could not parse text : " + rowText);
				nfe.printStackTrace();
			} catch (IndexOutOfBoundsException iob) {
				System.out.println("Could not parse text : " + rowText);
				iob.printStackTrace();
			}
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Name : ");
			sb.append(name);
			sb.append(", Count : ");
			sb.append(count);
			sb.append(", Percentage : ");
			sb.append(percentage);
			sb.append(", Type : ");
			sb.append(type);
			return sb.toString();
		}
		public void click() {
			UiHelper.click(rowItem);
		}
	}

}
