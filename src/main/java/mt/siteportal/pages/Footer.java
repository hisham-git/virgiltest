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

public class Footer extends BasePage {
	
	@FindBy(how = How.XPATH, using = "//footer//img")
    private WebElement MedovanteLogo;

	private By aplicationMenu = new By.ByXPath("//footer//button");
	
	private String ApplicationMenuContent = "//footer//button/following-sibling::ul/li/a";
	private String home = "//footer//a[text()='Home']";
	private String administration = "//footer//a[text()='Administration']";
	private String about = "//footer//a[text()='About']";
	public String logout = "//footer//a[text()='Log out']";
	private String privacy = "//footer//a[text()='Privacy Policy']";
	
	public List<String> getAplicationMenuContent(){
		Log.logInfo("Getting Application Content");
		UiHelper.click(aplicationMenu);
		List<WebElement> elements= UiHelper.findElements(new By.ByXPath(ApplicationMenuContent));
		List<String> content = TextHelper.getElementTextContent(elements);
		return content;
	}
	
	public HomePage openHomePage(){
		Log.logInfo("Opening Home Page");
		UiHelper.click(aplicationMenu);
		UiHelper.click(new By.ByXPath(home));
		HomePage homepage = PageFactory.initElements(Browser.getDriver(), HomePage.class);
		//UiHelper.checkPendingRequests(Browser.getDriver());
		return homepage;
	}
	
	public Dashboard openDashboard(){
		Log.logInfo("Opening Home Page");
		UiHelper.click(aplicationMenu);
		UiHelper.click(new By.ByXPath(home));
		Dashboard dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		//UiHelper.checkPendingRequests(Browser.getDriver());
		return dashboard;
	}
	
	public Administration openAdministration(){
		Log.logInfo("Opening Administration");
		UiHelper.click(aplicationMenu);
		UiHelper.click(new By.ByXPath(administration));
		Administration administration = PageFactory.initElements(Browser.getDriver(), Administration.class);
		//UiHelper.checkPendingRequests(Browser.getDriver());
		return administration;
	}
	
	public About openAbout(){
		Log.logInfo("Opening AboutPage");
		UiHelper.click(aplicationMenu);
		UiHelper.click(new By.ByXPath(about));
		About about = PageFactory.initElements(Browser.getDriver(), About.class);
		//UiHelper.checkPendingRequests(Browser.getDriver());
		return about;
	}
	
	public LoginPage openLoginPage(){
		Log.logInfo("Opening LoginPage");
		UiHelper.click(aplicationMenu);
		UiHelper.click(new By.ByXPath(logout));
		LoginPage loginPage = PageFactory.initElements(Browser.getDriver(), LoginPage.class); 
		//UiHelper.checkPendingRequests(Browser.getDriver());
		return loginPage; 
	}
	
	/**
	 * @author ubiswas
	 * 
	 * Description 	- click application menu from footer..
	 * @return
	 */
	public boolean clickApplicationMenu() {
		if (UiHelper.scrollToElementWithJavascript(aplicationMenu, Browser.getDriver())) {
			UiHelper.click(aplicationMenu);
			return true;
		} else {
			Log.logError("Couldn't found the Application menu for clicking");
			return false;
		}
	}

	public void footerPrivacyPolicyActionVerification() {
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

	public PrivacyPolicy openPrivacyPage() {
		Log.logStep("Click on Privacy link");
		Log.logVerify("Opening privacy page on new tab from application menu..");
		UiHelper.click(new By.ByXPath(privacy));
		UiHelper.sleep();
		return PageFactory.initElements(Browser.getDriver(), PrivacyPolicy.class);
	}
	
}
