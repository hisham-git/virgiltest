package mt.siteportal.pages;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


/**
 * Created by maksym.tkachov on 1/19/2016.
 */
public class ErrorContainer extends BasePage {
    private static final String CLASSNAME = ErrorContainer.class.getSimpleName();

    static {
        locators.put(CLASSNAME, Util.readFromXLS(CLASSNAME));
    }

    private By CloseButton = new By.ByCssSelector(locators.get(CLASSNAME).get("CloseButton"));
    private By Container = new By.ByCssSelector(locators.get(CLASSNAME).get("Container"));
    private By HiddenContainer = new By.ByCssSelector(locators.get(CLASSNAME).get("HiddenContainer"));
    private By ErrorMessage = new By.ByCssSelector(locators.get(CLASSNAME).get("ErrorMessage"));


    public ErrorContainer (WebDriver driver) {
        super(driver);
    }

	public boolean isContainerDisplayed() {
		if (UiHelper.isPresentAndVisible((Container))) {
			return true;
		}
		return false;
		/*
		 * if (UiHelper.isPresent(HiddenContainer)){ return false; } else{
		 * return true; }
		 */
	}

    public void close(){
        Log.logStep("Closing the error container");
        UiHelper.click(CloseButton);
    }
    public String getErrorMessage(){
        Log.logStep("Getting error message");
        return UiHelper.getText(ErrorMessage);
    }
}
