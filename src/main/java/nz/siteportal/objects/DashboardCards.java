package nz.siteportal.objects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Creates a Dashboard Card object with attributes Title, Count and Description
 * 
 * @author anoor
 *
 */
public class DashboardCards {
	private WebElement cardItem;
	private String cardCategory;
	private String cardTitle;
	private String cardDescription;
	private int filterCount = 0;
	private String cardType;

	/*
	 * Constructor
	 */
	public DashboardCards(WebElement item) {
		cardItem = item;
		cardTitle = cardItem.findElement(By.cssSelector(".dashboard-tile-name.ng-binding")).getText().trim();
		String count = cardItem.findElement(By.cssSelector(".dashboard-tile-rows-count>span.ng-binding")).getText();
		if (!count.equals(""))
			filterCount = Integer.parseInt(count);
		if (item.getAttribute("class").contains("root-filter"))
			setCardType("Root Filter");
		else if (getDeleteButton() != null)
			setCardType("Custom Filter");
		else {
			setCardType("Default Filter");
		}
		setCardDescription();
	}

	/**
	 * Returns the WebElement reference to the Delete Button
	 * 
	 * @return WebElement, or null if no Delete Button is found
	 */
	private WebElement getDeleteButton() {
		try {
			Browser.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement temp = cardItem.findElement(By.xpath("child::a[contains(@class, 'remove-custom-filter')]"));
			Browser.resetTimeOuts();
			return temp;
		} catch (org.openqa.selenium.NoSuchElementException any) {
			Browser.resetTimeOuts();
			return null;
		}
	}

	/**
	 * Clicks on the Delete Button for Custom Filter Cards
	 */
	public void clickDelete() {
		WebElement deleteButton = getDeleteButton();
		if (deleteButton != null) {
			UiHelper.click(deleteButton);
		} else {
			Log.logInfo("Could not click the Delete Button since it could not be found...");
		}
	}

	/**
	 * 
	 * @return Card Title
	 */
	public String getCardTitle() {
		return cardTitle;
	}

	/**
	 * 
	 * @return Card Count
	 */
	public int getCardCount() {
		return filterCount;
	}

	/**
	 * 
	 * @return Card Description
	 */
	public void setCardDescription() {
		this.cardDescription = "";
		try {
			Browser.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			this.cardDescription = cardItem
					.findElement(By.cssSelector(".dashboard-tile-descriptor.hidden-xs.ng-binding")).getText().trim();
			Browser.resetTimeOuts();
		} catch (org.openqa.selenium.NoSuchElementException any) {
			Browser.resetTimeOuts();
		}
	}

	/**
	 * Set Card Category to that of it's parent
	 * 
	 */

	public void setCardCategory(String cat) {
		cardCategory = cat;
	}

	/**
	 * Returns a Card category
	 * 
	 * @return Card Category
	 */
	public String getCardCategory() {
		return cardCategory;
	}

	/**
	 * Returns WebElment of label TOTAL
	 * 
	 * @return WebElement LabelTotal
	 */
	public WebElement getLabelTotal() {
		try {
			Browser.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement temp = cardItem.findElement(By.cssSelector(".label-total"));
			Browser.resetTimeOuts();
			return temp;
		} catch (org.openqa.selenium.NoSuchElementException any) {
			Browser.resetTimeOuts();
			return null;
		}
	}

	/**
	 * Clicks on a Card
	 */
	public void clicksOnACard() {
		UiHelper.click(this.cardItem);
	}

	/**
	 * Sets the Dashboard Card's filter type. "Root Filter" is for Card headers.
	 * "Custom Filter" are for Cards which can be deleted. "Default Filter" are
	 * for all other types
	 * 
	 * @param cardType
	 *            String - the type of filter this card is
	 */
	private void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * Returns the Type of Card Filter
	 * 
	 * @return String - Card Filter type
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * Returns the Card's Description if it has any, returns an empty String
	 * otherwise
	 * 
	 * @return String - Card's Description
	 */
	public String getCardDescription() {
		return this.cardDescription;
	}

	/**
	 * Returns the String formatted version of this object, especially for
	 * printing. Format =
	 * "Name : {NAME}, Count : {COUNT}, Category : {CATEGORY}"
	 * 
	 * @return String - The name, count and category values of the DashboardCard's Object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name : ");
		sb.append(getCardTitle());
		sb.append(", Count : ");
		sb.append(getCardCount());
		sb.append(", Category : ");
		sb.append(getCardCategory());
		return sb.toString();
	}
}
