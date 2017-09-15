package mt.siteportal.pages.studyDashboard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class EmptyDashboard extends BasePage{
	
	@FindBy(how = How.XPATH, using = "//span[text()='Choose Study']")
    public WebElement DashboardMessage;
	
	 @FindBy(how = How.CSS, using = "h1")
	    private WebElement PageName;
	
	public EmptyDashboard(WebDriver driver) {
		super(driver);
	}

	
	public boolean isEmptyDashboardOpened(){
		Log.logInfo("Verification of displaying 'Dashboard'");
		return UiHelper.isPresent(DashboardMessage);
	}
}
