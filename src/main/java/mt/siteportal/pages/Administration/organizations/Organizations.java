package mt.siteportal.pages.Administration.organizations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import mt.siteportal.controls.Control;
import mt.siteportal.controls.Dropdown;
import mt.siteportal.controls.Input;
import mt.siteportal.objects.Organization;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;


public class Organizations extends BasePage{
	
	@FindBy(how = How.XPATH, using = "//a[@title='Add']")
    public WebElement AddOrganizationButton;
	
	@FindBy(how = How.XPATH, using = "//a[@title='Remove']")
    public WebElement Remove;
	
	public HashMap<String, WebElement> organizationsMap;
	public GeneralOrganizationsTab generalOrganizationTab;
	public AddressesTab addresses;
	public PeopleTab people;

	public Organizations(WebDriver driver) {
		super(driver);
		generalOrganizationTab = PageFactory.initElements(Browser.getDriver(), GeneralOrganizationsTab.class);
	}

	public void removeOrganization(String name){
		}
	
	public void filterOrganizationsBy(String name){
		}
	
	public void filterOrganizationsByName(String name){
		}
	
	public GeneralOrganizationsTab addOrganization(Organization orgainzation) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Log.logInfo("Adding new Organization: "+orgainzation.getName());
		UiHelper.click(AddOrganizationButton);				
		HashMap<String, WebElement> details = generalOrganizationTab.addEditDetails();
		Set<String> keys = details.keySet();
		for (String key : keys) {
			System.out.println("Spliting");
			String splitedKey = TextHelper.toCamelCase(key);
			Field field = orgainzation.getClass().getDeclaredField(splitedKey);
			System.out.println();
			field.setAccessible(true);
			String value = (String) field.get(orgainzation);
			if(value!=null){
				Control control = UiHelper.getControl(details.get(key));
				if (control instanceof Input){
					((Input) control).sendKeys(value);
				}
				else if (control instanceof Dropdown){
					((Dropdown) control).selectValue(value);
				}
				/*else if (control instanceof TextArea){
					//do something
				}
				else if (control instanceof Calendar){
					//do something
				}*/
				
			}
		}
		UiHelper.click(generalOrganizationTab.Save);
		return generalOrganizationTab;
	}

	public void openTab(String name){
		Log.logInfo("Openning tab "+ name);
		//String locatorTab = ""+name+"";
	
		if(name.equals("General")){
			//UiHelper.click();
			generalOrganizationTab = PageFactory.initElements(Browser.getDriver(), GeneralOrganizationsTab.class);
		}
		else if(name.equals("Addresses")){
			//UiHelper.click();
			addresses = PageFactory.initElements(Browser.getDriver(), AddressesTab.class);
		}
		else if(name.equals("People")){
			//UiHelper.click();
			people =  PageFactory.initElements(Browser.getDriver(), PeopleTab.class);
		}
		 
	}
}
