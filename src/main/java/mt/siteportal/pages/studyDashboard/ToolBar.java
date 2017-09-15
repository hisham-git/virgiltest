package mt.siteportal.pages.studyDashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public abstract class ToolBar extends BasePage {

	@FindBy(how = How.CSS, using = ".btn.dropdown-toggle.btn-active")
	public WebElement studyDropdown;
	
//	private By studyDropdown2 = By.cssSelector(".btn.dropdown-toggle.btn-active");

	@FindBy(how = How.CSS, using = ".home")
	private WebElement homeButton;

	public Dashboard chooseStudy(String nameOfStudy) {
		Log.logInfo("Selecting Study: " + "[" + nameOfStudy + "]");
		UiHelper.waitFor(studyDropdown);
		try {
			UiHelper.fluentWaitForElementClickability(studyDropdown, 30);
			UiHelper.selectInDropdownBtn(studyDropdown, nameOfStudy);
			return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		} catch (NullPointerException npe) {
			throw new SkipException("Couldn't choose a study because of dropdown not available within timeout", npe);
		}
	}

	public boolean clickStudyDropdown() {
		Log.logStep("Clicking study dropdown...");
		try {
			UiHelper.fluentWaitForElementVisibility(studyDropdown, 30).click();
			return true;
		} catch (NullPointerException npe) {
			Log.logException("Study dropdown not available within timeout", npe);
			return false;
		}
	}
}
