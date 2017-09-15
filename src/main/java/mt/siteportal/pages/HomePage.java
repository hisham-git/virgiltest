package mt.siteportal.pages;


import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.EmptyDashboard;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.pages.Administration.Administration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

//import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public class HomePage extends BasePage {

	public By welcomeMessage = new By.ByXPath("//h1[text()='Welcome to MedAvante Portal']");
	public By studyDashboard = new By.ByXPath("//a[@href = '/study']");
    public By administration = new By.ByXPath("//div/a[@href = '/admin']");
    public By centralRating = new By.ByXPath("//div/a[@href = '/centralrating']");

    @FindBy(how = How.ID, using = "")
    private WebElement Settings;
    
    @FindBy(how = How.XPATH, using = "//a[@class='btn logout']")
    private WebElement Logout;
	
	public HomePage(WebDriver driver) {
	    super(driver);
	    }
		
	public void waitForFullLoadingPage(){
		UiHelper.waitForElementDisappeared(new By.ByCssSelector(".loading-overlay"));
	}
	
	public boolean isHomePageOpened(){
		Log.logInfo("Verification of displaying 'HomePage' field on page");
		return UiHelper.isPresent(welcomeMessage);
				
	}
	
	public EmptyDashboard openEmptyDashboard(){
		Log.logInfo("Opening StudyDashboard");
		UiHelper.click(studyDashboard);
		return PageFactory.initElements(Browser.getDriver(), EmptyDashboard.class);
		}
		
	public Dashboard openDashboard(){
		Log.logInfo("Opening StudyDashboard");
//		UiHelper.sleep(500);
		UiHelper.click(studyDashboard);
		return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		}
	
	public LoginPage logout(){
		Log.logInfo("Logging out..");
		UiHelper.click(Logout);
		
		return PageFactory.initElements(Browser.getDriver(), LoginPage.class);
	}
			
	public Administration openAdministration(){
		Log.logInfo("Opening Administration");
		UiHelper.click(administration);
		return PageFactory.initElements(Browser.getDriver(), Administration.class);
	}
	
	public Settings openSettings(){
		return new Settings();
	}
	
	public CrVisitList openCentralRating() {			
		UiHelper.click(centralRating);
		UiHelper.sleep();
		return PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
	}
}