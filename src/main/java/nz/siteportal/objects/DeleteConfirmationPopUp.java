package nz.siteportal.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;


/**
 * Wrapper Class for the Delete Confirmation Pop Up WebElement
 * @author Syed A. Zawad
 *
 */
public class DeleteConfirmationPopUp{
	
	WebElement pop_up_box;	
//	By yes_button = By.cssSelector(".btn-orange");
	By yes_button = By.cssSelector("div[data-ng-click='okClick()']");
//	By no_button = By.cssSelector(".btn-default");
	By no_button = By.cssSelector("div[data-ng-click='cancelClick()']");
	By pop_up_header = By.cssSelector("#queryConfirmationLabel");
	By pop_up_close = By.cssSelector(".close");
	By pop_up_text = By.xpath("label");
	
	public DeleteConfirmationPopUp(WebElement element){
		pop_up_box = element;
	}
	
	public boolean isOpen(){
		Log.logInfo("Getting Pop Up Box's Display Status");
		return pop_up_box.isDisplayed();
	}
	
	public String getBodyText(){
		Log.logInfo("Getting Pop Up's Body Text");
		return pop_up_box.findElement(pop_up_text).getText();
	}
	
	public void clickYes(){
		Log.logStep("Clicking Popup's Yes button.");
		UiHelper.click(pop_up_box.findElement(yes_button));
	}
	
	public void clickNo(){
		Log.logStep("Clicking Popup's NO button.");
		UiHelper.click(pop_up_box.findElement(no_button));
	}
	
	public void clickClose(){
		Log.logStep("Clicking Popup close.");
		UiHelper.click(pop_up_box.findElement(pop_up_close));
	}
}
