package mt.siteportal.pages;

import java.util.List;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Util;

import mt.siteportal.utils.tools.Verify;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class About extends BasePage {
	private static final String CLASSNAME = About.class.getSimpleName();
	static {
		locators.put(CLASSNAME, Util.readFromXLS(CLASSNAME));
		}
//	
/*	@FindBy(how = How.XPATH, using = "//h1[text()='About']")
    public WebElement About;
	
	@FindBy(how = How.XPATH, using = "//*[text()='About MedAvante']")
    public WebElement AboutMedAvante;
	
	@FindBy(how = How.XPATH, using = "//*[text()='Legal Notice']")
    public WebElement LegalNotice;
	
	@FindBy(how = How.XPATH, using = "//*[text()='About VIRGIL']")
    public WebElement AboutVirgil;
	
	@FindBy(how = How.XPATH, using = "//span[text() = 'MedAvante']")
    public WebElement MedAvante;
	
	@FindBy(how = How.XPATH, using = "//span[text() = 'Privacy']")
    public WebElement Privacy;
	*/
	private String aboutAllText = locators.get(CLASSNAME).get("aboutAllText");

	private By About = new By.ByXPath(locators.get(CLASSNAME).get("About"));
	//private By AboutMedAvante = new By.ByXPath("//*[text()='About MedAvante']");
	//private By LegalNotice = new By.ByXPath("//*[text()='Legal Notice']");
	private By MedAvante = new By.ByXPath(locators.get(CLASSNAME).get("MedAvante"));
	private By Privacy = new By.ByXPath(locators.get(CLASSNAME).get("Privacy"));
	//private By AboutVirgil = new By.ByXPath("//*[text()='About VIRGIL']");

	
	
	public About(WebDriver driver) {
		super(driver);
		
	}
	
	public boolean isAboutOpened(){
		Log.logInfo("Verification of displaying 'About' page");
		return UiHelper.isPresent(About);
	}
	
	public String getAboutMedAvanteContent(){
		List<String> content = getAllTextContent();
		int index = content.indexOf("About MedAvante");
		return content.get(index+1);
	}
	
	public String getLegalNoticeContent(){
		List<String> content = getAllTextContent();
		int index = content.indexOf("Legal Notice");
		return content.get(index+1);
		
	}
	
	public String getCopyrightContent(){
		List<String> content = getAllTextContent();
		int index = content.indexOf("About VIRGIL");
		return content.get(index+1);
		
	}
	
	public String getVersion(){
		List<String> content = getAllTextContent();
		int index = content.indexOf(getCopyrightContent());
		return content.get(index+1);
		
	}
	public List<String> getAllTextContent(){
		List<WebElement> elements = UiHelper.findElements(new By.ByXPath(aboutAllText));
		List<String> content = TextHelper.getElementTextContent(elements);
		return content;
	}
	
	public String openMedAvante() {
		try{
			UiHelper.click(MedAvante);
			UiHelper.switchToTab(Browser.getDriver());
		}
		catch (TimeoutException e){
			Verify.True(false,"the medavante site is opened slowly");
		}
		String url = UiHelper.getCurrentUrl();
		Browser.getDriver().close();
		return url;
		
	}
	
	public String openPrivacy() {
		try {
			UiHelper.click(Privacy);
			UiHelper.switchToTab(Browser.getDriver());
		}
		catch (TimeoutException e){
			Verify.True(false,"the privacy site is opened slowly");
		}
		String url = UiHelper.getCurrentUrl();
		Browser.getDriver().close();
		return url;
		
	}
}
