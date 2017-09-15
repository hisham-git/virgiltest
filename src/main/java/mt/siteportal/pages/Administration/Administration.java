package mt.siteportal.pages.Administration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import mt.siteportal.pages.BasePage;
import mt.siteportal.pages.Administration.organizations.Organizations;
import mt.siteportal.pages.Administration.people.People;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class Administration extends BasePage{

	@FindBy(how = How.XPATH, using = "//span[text()='People']/..")
    private WebElement PeopleTab;
	//@FindBy(how = How.XPATH, using = "//label[text()='Deactivated']")
 /*   public WebElement Home;
    public WebElement Organization;
    public WebElement People;
    public WebElement Studies;
    public WebElement FormsLibrary;
    public WebElement SystemNotifcations;*/
    public Organizations orgainzationsPage;
    public People peoplePage;    
    public Studies studiesPage;
    public SystemNotifcations SystemNotifcations;
    
	
	public Administration(WebDriver driver) {
		super(driver);
		orgainzationsPage = PageFactory.initElements(Browser.getDriver(), Organizations.class);
	}
	
	

	public void openOprganizations(){
		//Click
		orgainzationsPage = PageFactory.initElements(Browser.getDriver(), Organizations.class);
	}
	
	public void openPeople(){
		Log.logInfo("Opening the People tab");
		UiHelper.click(PeopleTab);
		peoplePage = PageFactory.initElements(Browser.getDriver(), People.class);
	}
	
	public Studies openStudies(){
		//Click
		return PageFactory.initElements(Browser.getDriver(), Studies.class);
	}
	
	public FormsLibrary openFormsLibrary(){
		//Click
		return PageFactory.initElements(Browser.getDriver(), FormsLibrary.class);
	}
	
	public SystemNotifcations openSystemNotifcations(){
		//Click
		return PageFactory.initElements(Browser.getDriver(), SystemNotifcations.class);
	}
	
	public boolean isAdministrationOpened(){
		Log.logInfo("Verification of displaying 'Administration' page");
		return UiHelper.getCurrentUrl().contains("admin#/manage");
        
	}
	
	/*
	 * -----------------------------NZ--------------------------------------------------------
	 */
	
	/**
	 * Checks is the Administration Page is opened by looking for the admin
	 * class in its div and its title
	 * 
	 * @return boolean
	 */
	public boolean isOpen() {
		WebElement page_content = UiHelper.findElement(By.id("page-content"));
		try{
			page_content.findElement(By.xpath("div[contains(@class, 'admin-page')]"));
		}catch(NoSuchElementException nse){
			return false;
		}
		return driver.getTitle().contains("Administration");
	}
	
	/*
	 * -----------------------------------------------------------------------------------------
	 */
}
