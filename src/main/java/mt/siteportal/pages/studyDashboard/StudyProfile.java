package mt.siteportal.pages.studyDashboard;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyProfile extends BasePage {

	// private WebElement Site;
	// private WebElement Study;

	@FindBy(how = How.XPATH, using = "//div[@class='row header-slide-study']/a[@title='Close']")
	public WebElement CloseButton;

	public By header = new By.ByXPath("//h1[text()='Study Profile']");
	private By language = new By.ByXPath("//span[text()='Language']/..");
	private By templateCount = new By.ByXPath("//span[text()='Templates']/following-sibling::*");
	public By partialLoadedMessage = new By.ByXPath("//div[@data-ng-show='partialLoaded']");
	public By noAssessmentDefinedMessage  = new By.ByXPath("//label[@data-ng-show='noAssessmentDefined']");

	private String visits = "//div[@data-ng-include='profileUrl']//div[@class='portal-grid']/div[not(@class='row grid-header')]";

	private By formCount = new By.ByXPath("//div[@class='section ng-scope']");
	public Map<String, String> visitNameMapCount = new HashMap<String, String>();;

	public StudyProfile(WebDriver driver) {
		super(driver);
	}

	public void selectLanguage(String languageToSelect) {
		Log.logInfo("Selecting language in dropdown");
		UiHelper.click(language);
		UiHelper.findElementByXpath("//li/label[text()='" + languageToSelect + "']").click();
		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	public void closeProfile() {
		Log.logInfo("Closing Study Profile");
		UiHelper.click(CloseButton);
	}

	public Map<String,String>  getVisitNameMapCount() {
		if(visitNameMapCount.isEmpty()){
			Log.logInfo("Getting available visits and their counts in 'StudyProfile'");
			List<WebElement> elementList = UiHelper.findElements(new By.ByXPath(visits));
			for (WebElement element : elementList) {
				String visitName = element.findElement(new By.ByXPath("div[1]")).getText();
				String templateCount = element.findElement(new By.ByXPath("div[2]")).getText();
				visitNameMapCount.put(visitName, templateCount);
			}
		}
		return visitNameMapCount;
	}

	public WebElement getElementVisitsByName(String name) {
		Log.logInfo("Getting available visits and their elements in 'StudyProfile'");
		return UiHelper.findElementByXpath("//label[text()='" + name + "']/../..");
	}

	public String getLanguage() {
		Log.logInfo("Getiing Default Language");
		return UiHelper.findElement(language).findElement(new By.ByCssSelector("h2")).getText();
	}

	public String getFirstVisit() {
		Log.logInfo("Getting first visit in list");
		return UiHelper.findElementByXpath(visits + "[1]/*[1]").getText();
	}

	public boolean isSelectedVisit(String visit) {
		Log.logInfo("Verification of visit selection");
		String isSelected = getElementVisitsByName(visit).getAttribute("class");
		return isSelected.contains("selected");
	}

	public String getTemplateCount(String visit, String language) {
		Log.logInfo("Getting templates count from brackets()");
		if (!visitNameMapCount.get(visit).equals("0")) {
			UiHelper.click(getElementVisitsByName(visit));
			selectLanguage(language);
			String cnt = UiHelper.getText(templateCount);
			if (cnt.isEmpty()) {
				return "0";
			} else {
				//return cnt.substring(1, cnt.length() - 1);
				return TextHelper.splitParentheses(cnt);
			}

		}
		return "0";

	}

	public String getFormCount() {
		Log.logInfo("Getting Form count");
		return String.valueOf(UiHelper.findElements(formCount).size());
	}

	public void openForm(int position) {
		Log.logInfo("Opening form");
		UiHelper.click(new By.ByXPath("//div[@id='virgilForms']//a[@class='thumb'][" + position + "]"));
	}

	/*-------------------------------NZ team-----------------------------------------------*/

	/*
	 * The Panel WebElement
	 */
	@FindBy(css = ".row.header-slide-study > .slide-panel-header > h1")
	private WebElement panel;

	/**
	 * Check if the Study Profile is displayed Remove this method in next
	 * versions
	 * 
	 * @return boolean
	 */
	public boolean isStudyProfileOpened() {
		return isOpen();
	}

	/**
	 * Check if the Study Profile is displayed
	 * 
	 * @return boolean
	 */
	public boolean isOpen() {
		Log.logStep("Verifying if Study Profile is opened/closed...");
		boolean isOpened = UiHelper.isClickable(panel);
		Log.logStep("The Panel is " + ((isOpened) ? "" : " not ") + "opened.");
		return isOpened;
	}
}
