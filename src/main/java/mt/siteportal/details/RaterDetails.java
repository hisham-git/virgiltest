package mt.siteportal.details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mt.siteportal.pages.BasePage;

import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class RaterDetails extends BasePage {
	//private static final Logger LOGGER = LoggerFactory.getLogger(RaterDetails.class);
	
	private Map<String, String> details = new HashMap<String, String>();
	private List<String> languages = new ArrayList<String>();
	/*private List<QualificationsTable> qualificationsTable;
	private List<AssessmentsTable> assessmentsTable;*/
	


	public RaterDetails(WebDriver driver) {
		super(driver);
		List<WebElement> keys = UiHelper.findElements(new By.ByXPath("//div[@class='caption col-xs-10']"));
		List<WebElement> values = UiHelper.findElements(new By.ByXPath("//div[@class='value col-xs-14']"));
		for (int i=0; i<keys.size();i++){
			details.put(TextHelper.getElementTextContent(keys).get(i), TextHelper.getElementTextContent(values).get(i));
			}
		languages = TextHelper.getElementTextContent(UiHelper.findElements(new By.ByXPath("//div[@class='col-sm-24'][parent::div[not(contains(@class,'ng-hide'))]]//label")));
	}
	
	public Map<String, String> getDetails() {
		return details;
	}

	public List<String> getLanguages() {
		return languages;
	}
	
	
/*--------------------------------------NZ Team-------------------------------------------------*/
	private By detailsGrid = By.cssSelector("div.details-grid");
	
	@FindBy(how = How.CSS, using = "#page-title>h1")
	public WebElement raterName;

	
	/**
	 * Verify if the Rater's Details page is opened
	 * @return
	 */
	public boolean isOpened(){
	    UiHelper.waitForVisibility(driver.findElement(detailsGrid));
		Log.logStep("Checking if the current page is Rater's Details...");
		String raterStr = TextHelper.getPartsFromDetails(raterName.getText())[0];
		if (raterStr.equals("Rater") && UiHelper.isPresentAndVisible(detailsGrid)) {
			Log.logInfo("Rater's Details page is open.");
			return true;
		}
		Log.logInfo("Current Page is Not Rater's Details.");
		return false;
	}

	/**
	 * Verify the Rater's Details page with respect to an specific Rater's name
	 * @param Rater's name
	 * @return
	 */
	public boolean isOpened(String paramName){
		String raterNameHeader = TextHelper.getPartsFromHeader(raterName.getText())[1];
		if(paramName.equals(raterNameHeader) && isOpened()){
			Log.logInfo("Current Page is found as the details page of Rater:"+paramName);
			return true;
		}
		return false;
	}

}

