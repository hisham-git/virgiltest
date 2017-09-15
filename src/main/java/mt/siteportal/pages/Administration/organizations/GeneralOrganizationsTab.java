package mt.siteportal.pages.Administration.organizations;

import java.util.HashMap;
import java.util.List;

import mt.siteportal.pages.Administration.GeneralTab;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GeneralOrganizationsTab extends GeneralTab{
	
	private String keysLocator = "//div[@class='details-grid portal-grid row']//div[@class='caption col-xs-9 ng-scope']//label";
	private String valuesLocator = "//div[@class='value col-xs-15'or @class='date-wrapper col-xs-15']";
	
	@Override
	//TODO check the method again
	public HashMap<String, String> getDetails() {
		Log.logInfo("Getting details name-data from general tab");
    	List<WebElement> keys = UiHelper.findElements(new By.ByXPath(keysLocator));
    	List<WebElement> values =  UiHelper.findElements(new By.ByXPath(valuesLocator+"//label"));  
    	List<String> stringKeys = TextHelper.getElementTextContent(keys);
    	List<String> stringValues = TextHelper.getElementTextContent(values);
    	HashMap<String, String> details = new HashMap<String, String>();  
    	for (int i = 0; i<keys.size();i++){
    		details.put(stringKeys.get(i), stringValues.get(i));
    	}
    	return details;
	}

	@Override
	public HashMap<String, WebElement> addEditDetails() {
		Log.logInfo("Getting details name-object from general tab");
		List<String> keys = TextHelper.getElementTextContent(UiHelper.findElements(new By.ByXPath(keysLocator)));
    	List<WebElement> values = UiHelper.findElements(new By.ByXPath(valuesLocator));
    	HashMap<String, WebElement> details = new HashMap<String, WebElement>();
    	for (int i = 0; i<keys.size();i++){
    		System.out.println("addEditDetails");
    		details.put(keys.get(i), values.get(i));
    	}
		return details;
	}

}
