package mt.siteportal.pages;

import mt.siteportal.pages.Administration.Administration;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class Header extends BasePage{

	private By virgilLogo = new By.ByCssSelector("img[alt='Virgil']");
	private By portalVersion = new By.ByCssSelector("#logo>span");
	private By aplicationMenu = new By.ByXPath("//header//button");
	private By userInfo = new By.ByCssSelector("div.user-info > div.pull-left.name > label");
	
    private String ApplicationMenuContent = "//header//button/following-sibling::ul/li/a";
    private String home = "//header//a[text()='Home']";
    private String administration = "//header//a[text()='Administration']";
    private String about = "//header//a[text()='About']";
    private String logout = "//header//a[text()='Log out']";
    private String privacy = "//header//a[text()='Privacy Policy']";
    
	@FindBy(how = How.CSS, using = ".btn.logout")
    private WebElement LogOutButton;
	
	/**
	 * @author HISHAM
	 * 
	 * @return String - Application version info
	 */
	public String getVersion() {
		Log.logInfo("Getting Version of Portal...");
		WebElement versionElement = UiHelper.fluentWaitForElementVisibility(portalVersion, 30); 
		if (null != versionElement) {
			String version = versionElement.getText().trim();
			Log.logInfo("Portal running on version: [" + version + "]");
			return version;
		}
		Log.logInfo("Portal Version found null.");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * @return String - Logged in User name
	 */
	public String getuserName() {
		Log.logInfo("Getting Logged in User name...");
		String userName = null;
		WebElement userInfoElement = UiHelper.fluentWaitForElementVisibility(userInfo, 30);
		if (null != userInfoElement) {
			userName = userInfoElement.getText();
			Log.logInfo("User is logged in as: [" + userName + "]");
			return userName;
		}
		Log.logInfo("Logged in User name found: [" + userName + "]");
		return null;
	}
	
	public boolean clickApplicationMenu() {
		if (UiHelper.scrollToElementWithJavascript(aplicationMenu, Browser.getDriver())) {
			UiHelper.click(aplicationMenu);
			return true;
		} else {
			Log.logError("Couldn't found the Application menu for clicking");
			return false;
		}
	}
	
	public List<String> getAplicationMenuContent() {
		Log.logInfo("Getting Application Content");
		if (clickApplicationMenu()) {
			List<WebElement> elements = UiHelper.findElements(new By.ByXPath(ApplicationMenuContent));
			List<String> content = TextHelper.getElementTextContent(elements);
			return content;
		} else {
			Log.logInfo("Couldn't get 'Application Content' from Application menu...");
			return null;
		}
	}
	
	public HomePage openHomePageWithLogo(){
		Log.logInfo("Opening Home Page with Logo");
		UiHelper.click(virgilLogo);
		return PageFactory.initElements(Browser.getDriver(), HomePage.class);
	}
	
	public Dashboard openDashboardWithLogo(){
		Log.logInfo("Opening Dashboard with Logo");
		UiHelper.click(virgilLogo);
		return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	/**
	 * @author HISHAM
	 * 
	 * @return
	 */
	public HomePage openHomePage() {
		Log.logInfo("Opening Home Page from Application menu...");
		if (clickApplicationMenu()) {
			UiHelper.click(new By.ByXPath(home));
			return PageFactory.initElements(Browser.getDriver(), HomePage.class);
		} else {
			Log.logInfo("Couldn't open Home page from Application menu...");
			return null;
		}
	}

	public Dashboard openDashboard() {
		Log.logInfo("Opening Dashboard from Application menu...");
		if (clickApplicationMenu()) {
			UiHelper.click(new By.ByXPath(home));
			return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		} else {
			Log.logInfo("Couldn't open Dashboard from Application menu...");
			return null;
		}
	}
	
	public Administration openAdministration() {
		Log.logInfo("Opening Administration from Application menu...");
		if (clickApplicationMenu()) {
			UiHelper.click(new By.ByXPath(administration));
			return PageFactory.initElements(Browser.getDriver(), Administration.class);
		} else {
			Log.logInfo("Couldn't found Administration menu item from Application menu...");
			return null;
		}
	}
	
	public About openAbout() {
		Log.logInfo("Opening AboutPage from Application menu...");
		if (clickApplicationMenu()) {
			UiHelper.click(new By.ByXPath(about));
			return PageFactory.initElements(Browser.getDriver(), About.class);
		} else {
			Log.logInfo("Couldn't found About menu item from Application menu...");
			return null;
		}
	}
	
	public LoginPage openLoginPage() {
		Log.logInfo("Opening LoginPage from Application menu...");
		if (clickApplicationMenu()) {
			UiHelper.click(new By.ByXPath(logout));
			return PageFactory.initElements(Browser.getDriver(), LoginPage.class);
		} else {
			Log.logInfo("Couldn't found LogOut menu item from Application menu...");
			return null;
		}
	}
	
	public LoginPage logout() {
		Log.logInfo("Logging out...");
		if (UiHelper.scrollToElementWithJavascript(LogOutButton, Browser.getDriver())) {
			UiHelper.click(LogOutButton);
			return PageFactory.initElements(Browser.getDriver(), LoginPage.class);
		} else {
			Log.logError("Couldn't found the LogOut button for clicking");
			return null;
		}
	}

	/**
	 * @author ubiswas
	 * 
	 *         Description open privacy policy page from header
	 */
	public void headerPrivacyPolicyActionVerification() {
		// TODO Auto-generated method stub
		Log.logStep("Click on Application Menu");
		if (this.clickApplicationMenu()) {
			List<WebElement> elements = UiHelper.findElements(new By.ByXPath(ApplicationMenuContent));
			for (WebElement el : elements) {
				if (el.getText().toString().equalsIgnoreCase("Privacy Policy")) {
					Log.logInfo("Privacy Policy lik is found");
					this.openPrivacyPage();
				}
			}
		}
	}

	/**
	 * @author ubiswas
	 * 
	 *         Description open privacy policy page from header
	 */
	public PrivacyPolicy openPrivacyPage() {
		Log.logStep("Click on Privacy link");
		Log.logVerify("Opening privacy page on new tab from application menu..");
		UiHelper.click(new By.ByXPath(privacy));
		UiHelper.sleep();
		return PageFactory.initElements(Browser.getDriver(), PrivacyPolicy.class);
	}
}
