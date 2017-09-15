package mt.siteportal.objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Page Object for Upload Pop Up
 * 
 * @author Syed A. Zawad
 */
public class UploadFilesPopUp {

	/*
	 * Pop Up WebElement which serves as the base Element for all sub-elements
	 */
	private WebElement baseElement;

	/*
	 * Constructor, sets the base element
	 */
	public UploadFilesPopUp(WebElement element) {
		this.baseElement = element;
	}

	/**
	 * Click the Add File Button
	 */
	public void clickAddFileButton() {
		Log.logStep("Clicking the Add File Button...");
		UiHelper.clickAndWait(baseElement.findElement(By.xpath(".//button[@title='Add File(s)']")));
	}
	
	/**
	 * Get the Add File Button WebElement
	 */
	public WebElement getAddFileButton() {
		return baseElement.findElement(By.xpath(".//button[@title='Add File(s)']"));
	}

	/**
	 * Check if the Upload Button is enabled
	 * 
	 * @return boolean - true if enabled, false otherwise
	 */
	public boolean isUploadButtonEnabled() {
		return baseElement.findElement(By.xpath(".//div[text()='Upload']")).isEnabled();
	}

	/**
	 * Click the Upload Button
	 */
	public void clickUploadButton() {
		Log.logStep("Clicking the Upload Button...");
		UiHelper.clickAndWait(baseElement.findElement(By.xpath(".//div[text()='Upload']")));
	}
	
	/**
	 * Get the Upload Button WebElement
	 */
	public WebElement getUploadButton() {
		return baseElement.findElement(By.xpath(".//div[text()='Upload']"));
	}

	/**
	 * Get a List of all the Files that have been added and are ready to be
	 * uploaded.
	 * 
	 * @return List<String>
	 */
	public List<String> getUploadedFilenames() {
		List<WebElement> nameLabels = baseElement.findElements(By.cssSelector("label.ellipsis.ng-binding"));
		return TextHelper.getElementTextContent(nameLabels);
	}

	/**
	 * Waits for the File Uploads to finish, after clicking that the Upload
	 * Button
	 */
	public void waitForUploadToFinish() {
		Log.logStep("Waiting for the Upload to Finish...");
		UiHelper.sleep();
		int sec = 0;
		while (UiHelper.isPresentAndVisible(By.cssSelector(".spinner.spinner-small")) && sec < 30) {
			UiHelper.sleep();
			sec++;
		}
	}

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
	 * Get the Cancel Button WebElement
	 */
	public WebElement getCancelButton() {
		return baseElement.findElement(By.xpath(".//div[text()='Cancel']"));
	}
	
	/**
	 * Clicks the cancel button on the bottom left corner of the upload files pop-up
	 */
	public void clickCancelButton() {
		Log.logStep("Clicking the Cancel Button...");
		UiHelper.click(baseElement.findElement(By.xpath(".//div[text()='Cancel']")));
		UiHelper.sleep();
	}

}
