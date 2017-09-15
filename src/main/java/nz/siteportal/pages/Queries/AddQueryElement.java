package nz.siteportal.pages.Queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import mt.siteportal.controls.Dropdown;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class AddQueryElement extends BasePage {

	/*
	 * The base WebElement that contains all the other Add Query Controls. All
	 * selectors in this class MUST use this WebElement as the base reference,
	 * E.g. status_box_element = base_element.findElement(By.xpath)
	 */
	@FindBy(css = ".query-new-item")
	private WebElement base_element;

	private By status_indicator = By.xpath("div[1]/span"); // The status
															// indicator color
															// box

	/*
	 * The Age and Context elements
	 */
	private By age = By.xpath("div[2]/label"), contexts = By.xpath("div[2]/div");

	/*
	 * The dropdowns, text area and Save/Cancel buttons
	 */
	private By context_selector_dropdown = By.xpath("div[3]//div[@data-items='contextLevels']"), // The
																									// Context
																									// Dropdown
																									// Options
			score_selector_dropdown = By.xpath("div[3]//div[@data-items='scoreItems']"), // The
																							// Score
																							// Items
																							// Options
			text_area = By.xpath("div[3]//textarea"), create_button = By.xpath("div[3]/ul//a[@title='Create']"),
			cancel_button = By.xpath("div[3]/ul//a[@title='Cancel']");

	public AddQueryElement(WebDriver driver) {
		super(driver);
	}

	public String getStatus() {
		String statusColorClass = base_element.findElement(status_indicator).getAttribute("class");
		if (statusColorClass.contains("numgreen"))
			return "Closed";
		else if (statusColorClass.contains("numyellow"))
			return "Responded";
		else if (statusColorClass.contains("numred"))
			return "Open";
		else
			return "Unknown";
	}

	public String getStatusColor() {
		String statusColorClass = base_element.findElement(status_indicator).getAttribute("class");
		if (statusColorClass.contains("numgreen"))
			return "Green";
		else if (statusColorClass.contains("numyellow"))
			return "Yellow";
		else if (statusColorClass.contains("numred"))
			return "Red";
		else
			return "White";
	}

	public String getAge() {
		return base_element.findElement(age).getText().trim();
	}

	public String getValueOfContext(String context) {
		return getAllContexts().get(context);
	}

	public HashMap<String, String> getAllContexts() {
		List<WebElement> meta_data = base_element.findElements(contexts);
		HashMap<String, String> context_map = new HashMap<String, String>();
		for (WebElement data : meta_data) {
			if (data.isDisplayed()) {
				String key = data.findElement(By.cssSelector(".caption")).getText().trim();
				String value = data.findElement(By.cssSelector(".ng-binding")).getText().trim();
				context_map.put(key, value);
			}
		}
		return context_map;
	}

	public String getLastLevelContext() {
		Log.logInfo("Getting the lowest level visible context for the current Add Query page...");
		HashMap<String, String>	context_map = getAllContexts();
		if (context_map.containsKey("Score"))
			return "Score";
		if (context_map.containsKey("Assessment"))
			return "Assessment";
		if (context_map.containsKey("Visit"))
			return "Visit";
		if (context_map.containsKey("Subject"))
			return "Subject";
		else
			return "";
	}

	public void selectFromDropdownContext(String context) {
		Dropdown dropdown_list = new Dropdown(base_element.findElement(context_selector_dropdown));
		dropdown_list.selectValue(context);
		UiHelper.sleep(400);
	}

	public void selectFromDropdownScoreItem(String scoreItem) {
		Dropdown dropdown_list = new Dropdown(base_element.findElement(score_selector_dropdown));
		dropdown_list.selectValue(scoreItem);
	}

	public void setTextareaText(String text) {
		base_element.findElement(text_area).clear();
		Log.logStep("Entering [" + text + "] in the Add Query Text area");
		UiHelper.sendKeys(base_element.findElement(text_area), text);
	}

	public void clickCreate() {
		Log.logStep("Clicking Add Query's Create button");
		UiHelper.click(base_element.findElement(create_button));
	}

	public void clickCancel() {
		Log.logStep("Clicking Add Query's Cancel button");
		UiHelper.click(base_element.findElement(cancel_button));
	}

	public WebElement getCreateButton() {
		return base_element.findElement(create_button);
	}

	public WebElement getCancelButton() {
		return base_element.findElement(create_button);
	}

	public boolean isScoreItemsDropdownPresent() {
		Log.logInfo("Getting the Score Items Dropdown...");
		try {
			return UiHelper.isPresent(base_element.findElement(score_selector_dropdown));
		} catch (NoSuchElementException ne) {
			Log.logInfo("The Score Items dropdown was not found.");
			return false;
		}
	}

	public boolean isContextDropdownPresent() {
		Log.logInfo("Getting the Context Dropdown...");
		try {
			return UiHelper.isPresent(base_element.findElement(context_selector_dropdown));
		} catch (NoSuchElementException ne) {
			Log.logInfo("The Context dropdown was not found.");
			return false;
		}
	}

	public WebElement getTextArea() {
		return base_element.findElement(text_area);
	}

	public boolean commonChecks() {
		try {
			/*
			 * Log.logInfo("Checking Status Color..."); if
			 * (!getStatusColor().equals("White")) { Log.logInfo(
			 * "The Color was " + getStatusColor() +
			 * " and not white. Test Failure."); return false; }
			 */
			Log.logInfo("Checking Age...");
			if (!getAge().equals("NEW")) {
				Log.logInfo("The Age was " + getAge() + " and not NEW. Test Failure.");
				return false;
			}
			Log.logInfo("Checking Textbox...");
			if (!getTextArea().getAttribute("value").equals("")) {
				Log.logInfo("The Textbox value was '" + getTextArea().getAttribute("value")
						+ "' and not empty. Test Failure.");
				return false;
			}
			Log.logInfo("Checking Create button...");
			if (isCreateButtonEnabled()) {
				Log.logInfo("The Create button was enabled by default. Test Failure.");
				return false;
			}
			Log.logInfo("Checking Cancel button...");
			if (!getCancelButton().isDisplayed()) {
				Log.logInfo("The Cancel button was not displayed. Test Failure.");
				return false;
			}
			Log.logInfo("Checking Contexts...");
			HashMap<String, String> context_map = getAllContexts();
			if (context_map.size() == 0) {
				Log.logInfo("The Contexts were not displayed. Test Failure");
				return false;
			}
		} catch (NoSuchElementException ne) {
			Log.logInfo(
					"An Element was not found during performing common checks. Message :::::> \n" + ne.getMessage());
			return false;
		} catch (NullPointerException ne) {
			Log.logInfo("An Element was null during performing common checks. Message :::::> \n" + ne.getMessage());
			return false;
		}
		return true;
	}

	public boolean isCreateButtonEnabled() {
		try {
			if (getCreateButton().isDisplayed() && getCreateButton().isEnabled()) {
				if (getCreateButton().getAttribute("disabled") != null
						&& getCreateButton().getAttribute("disabled").equals("true")) {
					Log.logInfo("The Create Query button is disabled");
					return false;
				}
				Log.logInfo("The Create button is enabled");
				return true;
			}
			Log.logInfo("The Create button is disabled");
			return false;
		} catch (NoSuchElementException ne) {
			Log.logInfo("Could not find the Create button");
			return false;
		}
	}

	public List<String> getContextDropdownOptions() {
		WebElement dropdown = base_element.findElement(context_selector_dropdown);
		dropdown.findElement(By.xpath("button")).click();
		List<String> texts = new ArrayList<String>();
		for(WebElement link : dropdown.findElements(By.cssSelector(".dropdown-menu li"))){
			texts.add(link.getText());
		}
		dropdown.findElement(By.xpath("button")).click();
		return texts;
	}

	public boolean contextDropdownOptionsEqual(String... options) {
		List<String> dropdown_options = getContextDropdownOptions();
		if(options.length != dropdown_options.size()){
			Log.logInfo("Expected " + options.length + " arrays but found " + dropdown_options.size());
		}
		for(String option : options){
			if(!dropdown_options.contains(option)){
				Log.logInfo("Found " + option + " in expected options, but was not present in list "+ dropdown_options.toString());
				return false;
			}
		}
		return true;
	}

	public boolean scoreItemDropdownOptionsEqual(String... options) {
		List<String> dropdown_options = getScoreItemDropdownOptions();
		if(options.length != dropdown_options.size()){
			Log.logInfo("Expected " + options.length + " arrays but found " + dropdown_options.size());
		}
		for(String option : options){
			if(!dropdown_options.contains(option)){
				Log.logInfo("Found " + option + " in expected options, but was not present in list "+ dropdown_options.toString());
				return false;
			}
		}
		return true;
	}

	private List<String> getScoreItemDropdownOptions() {
		WebElement dropdown = base_element.findElement(score_selector_dropdown);
		dropdown.findElement(By.xpath("button")).click();
		List<String> texts = new ArrayList<String>();
		for(WebElement link : dropdown.findElements(By.cssSelector(".dropdown-menu li"))){
			texts.add(link.getText());
		}
		dropdown.findElement(By.xpath("button")).click();
		return texts;
	}

	public boolean isOpened() {
		try{
			return base_element.isDisplayed();
		}catch(NoSuchElementException ne){
			Log.logDebugMessage("The Add Query Panel was not found...");
			return false;
		}
	}

}
