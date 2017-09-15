package ah.siteportal.Administration.studies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import mt.siteportal.pages.BasePage;

public class Studies extends BasePage{
	@FindBy(how = How.CSS, using = "a[title='Add']")
    private WebElement addStudyButton;
	
	@FindBy(how = How.CSS, using = "a[title='Remove']")
	private WebElement removeStudyButton;
	
	@FindBy(how = How.CSS, using = ".input-group>input[name='search']")
	private WebElement searchInput;
	
	@FindBy(how = How.CSS, using = ".input-group button")
	private WebElement searchIcon;
	
//	@FindBy(how = How.LINK_TEXT, using = "Custom")
//	private WebElement customTab;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Custom')]")
	private WebElement customTab;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Sites')]")
	private WebElement sitesTab;
	
	public Studies(WebDriver driver) {
		super(driver);
	}
	
	public WebElement getCustomTab(){
		return customTab;
	}
	
	public WebElement getSitesTab(){
		return sitesTab;
	}

}
