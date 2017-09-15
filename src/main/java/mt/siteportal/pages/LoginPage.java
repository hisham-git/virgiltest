package mt.siteportal.pages;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * PageObject class for "Login" page.<br/>
 * Optimized for <a href="http://code.google.com/p/selenium/wiki/PageFactory">PageFactory</a> usage.
 */
public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

    // using = "loginName" - this is OK till that ID is only needed here, at 1 place.

    private By login = new By.ById("WebLogin_UserName");
    private By pass = new By.ById("Password");
    private By loginButton = new By.ById("WebLogin_Login");

    @FindBy(how = How.ID, using = "WebLogin_PasswordRecovery")
    private WebElement PasswordRecovery;
    
    @FindBy(how = How.XPATH, using = "//img[@class='logo']")
    public WebElement Logo;
    
    @FindBy(how = How.CSS, using = "li.failure-text.row > div")
    public WebElement failureMessage;
    
    // PageObject dependencies
    private Dashboard dashboard;
    private HomePage homePage;
    private Header header;
    
	/**
	 * Types in loginName and password and submits the login form.
	 * 
	 * @param loginName
	 *            - name to use for login.
	 * @param password
	 *            - password to use.
	 * @return - HomePage PageObject.
	 */
	public HomePage loginAsAdmin(String loginName, String password) {
		if (!isLoginPageOpened()) {
			Log.logInfo("The login page is not opened. User is going to logs out");
			UiHelper.click(new By.ByCssSelector(".btn.logout"));
		} else {
			Log.logInfo(String.format("Logging in as admin [%s/%s]", loginName, password));
			UiHelper.clear(login);
			UiHelper.sendKeys(login, loginName);
			UiHelper.clear(pass);
			UiHelper.sendKeys(pass, password);
			UiHelper.click(loginButton);
			header = PageFactory.initElements(Browser.getDriver(), Header.class);
			header.getuserName();
			header.getVersion();
		}
		homePage = PageFactory.initElements(Browser.getDriver(), HomePage.class);
		if (homePage.isHomePageOpened()) {
			return homePage;
		}
		return null;
	}
    
	/**
	 * Types in as Site person loginName and password and submits the login form.
	 * 
	 * @param loginName
	 *            - name to use for login.
	 * @param password
	 *            - password to use.
	 * @return - Dashboard PageObject.
	 */
	public Dashboard loginAsSitePerson(String loginName, String password) {
		if (!isLoginPageOpened()) {
			Log.logInfo("The login page is not opened. User is going to logs out");
			UiHelper.click(new By.ByCssSelector(".btn.logout"));
		} else {
			Log.logInfo(String.format("Logging as %s/%s", loginName, password));
			UiHelper.clear(login);
			UiHelper.sendKeys(login, loginName);
			UiHelper.clear(pass);
			UiHelper.sendKeys(pass, password);
			UiHelper.click(loginButton);
			header = PageFactory.initElements(Browser.getDriver(), Header.class);
			header.getuserName();
			header.getVersion();
			dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
			homePage = PageFactory.initElements(Browser.getDriver(), HomePage.class);
			if (dashboard.isDashboardOpened()) {
				return dashboard;
			} else if (homePage.isHomePageOpened()) {
				homePage.openDashboard();
				return dashboard;
			}
		}
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Types in as Sponsor person loginName and password and submits the login form.
	 * 
	 * @param loginName
	 *            - name to use for login.
	 * @param password
	 *            - password to use.
	 * @return - Dashboard PageObject.
	 */
	public Dashboard loginAsSponsorPerson(String loginName, String password) {
		if (!isLoginPageOpened()) {
			Log.logInfo("The login page is not opened. User is going to logs out");
			UiHelper.click(new By.ByCssSelector(".btn.logout"));
		} else {
			Log.logInfo(String.format("Logging as %s/%s", loginName, password));
			UiHelper.clear(login);
			UiHelper.sendKeys(login, loginName);
			UiHelper.clear(pass);
			UiHelper.sendKeys(pass, password);
			UiHelper.click(loginButton);
			header = PageFactory.initElements(Browser.getDriver(), Header.class);
			header.getuserName();
			header.getVersion();
			dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
			homePage = PageFactory.initElements(Browser.getDriver(), HomePage.class);
			if (dashboard.isDashboardOpened()) {
				return dashboard;
			} else if (homePage.isHomePageOpened()) {
				homePage.openDashboard();
				return dashboard;
			}
		}
		return null;
	}

    /**
     * Checks whether Login Name field present on current page.
     *
     * @return True if it is.
     */
    /*public boolean isLoginPageOpened(){
    	Log.logInfo("Verification of displaying 'Virgil logo' on page");
    	WebDriverWait wait = new WebDriverWait(Browser.getDriver(), 30);
		try {
			wait.until(ExpectedConditions.visibilityOf(Logo));
		} catch (TimeoutException te) {
			return false;
		}
		return true;
//    	return UiHelper.isPresent(Logo);
    }*/
    
	public boolean isLoginPageOpened() {
		Log.logInfo("Verification of displaying Login fields on page...");
		if (UiHelper.isClickable(login) && UiHelper.isClickable(pass) && UiHelper.isClickable(loginButton))
			return true;
		return false;
	}
    
    /**
     * Get failure messages for authentication/authorization
     *  
     * @return String 
     * 				- Failure message describing the reason
     */
    public String getFailureMessage() {
    	return failureMessage.getText().trim();
    }
    
    
 
   

}