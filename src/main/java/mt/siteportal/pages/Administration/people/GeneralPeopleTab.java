package mt.siteportal.pages.Administration.people;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mt.siteportal.pages.Administration.GeneralTab;
import mt.siteportal.utils.helpers.UiHelper;

import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class GeneralPeopleTab extends GeneralTab {

	
	@FindBy(how = How.XPATH, using = "//div[@data-value='model.firstName']//input")
    private WebElement FirstNameInput;
	
	@FindBy(how = How.XPATH, using = "//div[@data-value='model.lastName']//input")
    private WebElement LastNameInput;

	private String detailsKeys = "//div[contains(@class, 'col-xs-6 caption ng-binding') or contains(@class, 'caption col-xs-9')][not(ancestor::div[@class='ng-hide'])]|//div[contains(@class, 'col-xs-6 caption ng-binding') or contains(@class, 'caption col-xs-9')]/following-sibling::div[not(contains(@class,'ng-hide'))][not(ancestor::div[@class='ng-hide'])]";

	@Override
	public Map<String, String> getDetails() {
		Log.logInfo("Getting details of current person");
		Map<String,String> detailsData = new HashMap<String, String>();
		List<WebElement> elementKeys = UiHelper.findElements(new By.ByXPath(detailsKeys));
		String key;
		String value;
		for(int i = 0; i<elementKeys.size();i=i+2){
			key = elementKeys.get(i).getText();
			value = elementKeys.get(i+1).getText();
			detailsData.put(key, value);
		}
		return detailsData;
	}

	@Override
	public HashMap<String, WebElement> addEditDetails() {
		return null;
		// TODO Auto-generated method stub
	
	}
	
	public void fillRequiredFields(String firstName, String lastName){
		UiHelper.sendKeys(FirstNameInput, firstName);
		UiHelper.sendKeys(LastNameInput, lastName);	
	}
	
	
	
	

}
