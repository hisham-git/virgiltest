package mt.siteportal.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Page Object for Confirm Pop Up
 * 
 * @author HISHAM
 */
public class ConfirmPopUp {

	/*
	 * Pop Up WebElement which serves as the base Element for all sub-elements
	 */
	private WebElement baseElement;
	// "div#queryConfirmation"

	/*
	 * Constructor, sets the base element
	 */
	public ConfirmPopUp(WebElement element) {
		this.baseElement = element;
	}
	
	private By reasonDropdown = new By.ByXPath("//div[@data-select-action='onCancelationReasonChanged']/button");
	private By reasonComments = new By.ByXPath("//textarea[@data-ng-change='okValidation()']");

	/**
	 * Check if the Pop Up is displayed on the UI
	 * 
	 * @return boolean - true if displayed, false otherwise
	 */
	public boolean isOpen() {
		return UiHelper.isPresent(baseElement.findElement(By.xpath(".//div[@class='modal-content']")));
	}

	/**
	 * Clicks the close icon on the top left corner of the upload files pop-up
	 */
	public void clickClose() {
		UiHelper.click(baseElement.findElement(By.xpath(".//button[@class='close']")));
		UiHelper.sleep();
	}
	
	/**
	 * Get the Yes Button WebElement
	 */
	public WebElement getYesButton() {
		return baseElement.findElement(By.xpath(".//div[text()='Yes']"));
	}
	
	/**
	 * Clicks the Yes button on bottom of the Confirm pop-up
	 */
	public void clickYesButton() {
//		Log.logStep("Clicking 'Yes' Button on Confirm popup...");
		UiHelper.click(getYesButton());
//		UiHelper.sleep();
	}
	
	/**
	 * Get the No Button WebElement
	 */
	public WebElement getNoButton() {
		return baseElement.findElement(By.xpath(".//div[text()='No']"));
	}
	
	/**
	 * Clicks the No button on bottom of the Confirm pop-up
	 */
	public void clickNoButton() {
//		Log.logStep("Clicking 'No' Button on Confirm popup...");
		UiHelper.click(getNoButton());
		UiHelper.sleep();
	}
	
	public String getConfirmationText(){
		return baseElement.findElement(By.cssSelector("div.modal-text label")).getText();
	}
	
	public WebElement getReasonDropdown() {
		return baseElement.findElement(reasonDropdown);

	}
	
	public void inputPredefinedReason(String dropDownvalue) {
		UiHelper.selectInDropdownBtn(getReasonDropdown(), dropDownvalue);
	}
}
