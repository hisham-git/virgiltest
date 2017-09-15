package mt.siteportal.pages.Administration.people;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

public class People extends BasePage{

	@FindBy(how = How.XPATH, using = "//a[@title='Add']")
    public WebElement AddPersonButton;
	
	@FindBy(how = How.XPATH, using = "//a[@title='Remove']")
    public WebElement Remove;
	
	public HashMap<String, WebElement> personMap;
	private GeneralPeopleTab generalPeopleTab;
	private LanguagesTab languages;
	private ContactInformationTab contactInformation;
	private OrganizationsTab organizationsTab;

	public People(WebDriver driver) {
		super(driver);
		generalPeopleTab = PageFactory.initElements(Browser.getDriver(), GeneralPeopleTab.class);
	}

	public void removePerson(String name){
		}
	
	public void filterPersonBy(String name){
		}
	
	public void filterPersonByName(String name){
		}
	
	public GeneralPeopleTab addPerson(String firstName, String lastName) {
		UiHelper.click(AddPersonButton);
		generalPeopleTab = PageFactory.initElements(Browser.getDriver(), GeneralPeopleTab.class);
		generalPeopleTab.fillRequiredFields(firstName, lastName);
		UiHelper.click(generalPeopleTab.Save);
		return generalPeopleTab;
	}

	public GeneralPeopleTab selectPerson(String name) {
		Log.logInfo("Selecting Person="+name);
		UiHelper.click(new By.ByXPath("//span[text()='"+name+"']"));
		return PageFactory.initElements(Browser.getDriver(), GeneralPeopleTab.class);
	}

	public void openTab(String name){
		Log.logInfo("Openning tab "+ name);
		UiHelper.click(new By.ByXPath("//ul[@class='nav nav-tabs']//a[contains(text(), '"+name+"')]"));

		if(name.equals("General")){
			generalPeopleTab = PageFactory.initElements(Browser.getDriver(), GeneralPeopleTab.class);
		}
		else if(name.equals("Languages")){
			languages = PageFactory.initElements(Browser.getDriver(), LanguagesTab.class);
		}
		else if(name.equals("Contact Information")){
			contactInformation =  PageFactory.initElements(Browser.getDriver(), ContactInformationTab.class);
		}
		else if(name.equals("Organizations")){
			organizationsTab =  PageFactory.initElements(Browser.getDriver(), OrganizationsTab.class);
		}
		 
	}
	
	public GeneralPeopleTab getGeneralPeopleTab() {
		return generalPeopleTab;
	}

	public LanguagesTab getLanguagesTab() {
		return languages;
	}

	public ContactInformationTab getContactInformation() {
		return contactInformation;
	}
	
	public OrganizationsTab getOrganizationsTab() {
		return organizationsTab;
	}

}
