package mt.siteportal.details;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import mt.siteportal.objects.ConfirmPopUp;
import mt.siteportal.objects.UploadFilesPopUp;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Page Object for the Attachments Tab
 * 
 * @author Syed A. Zawad
 *
 */
public class AttachmentsTab extends BasePage {

	/*
	 * Locators
	 */
	private By rowsSelector = By.cssSelector(".attachments-row"), attachmentsPanel = By.cssSelector("#attachments"),
			uploadAttachmentsButton = By.cssSelector("a[title='Upload Attachments']");

	/*
	 * Locators for attachments tab, delete attachment button & file links
	 */
//	@FindBy(how = How.CSS, using = "li[ng-click='currentTab = 1']")
	@FindBy(how = How.CSS, using = "ul.nav-tabs li:nth-child(2):not(.ng-hide)")
	public WebElement attachmentsTab;

	@FindBy(how = How.CSS, using = "a[href='#attachments']")
	private WebElement attachmentsTabLink;

	@FindBy(how = How.CSS, using = "div.attachments-row")
	private List<WebElement> attachmentsRow;

	@FindBy(how = How.CSS, using = "[data-ng-click='deleteAttachment(attachment)']")
	private List<WebElement> deleteAttachmentsButton;

	@FindBy(how = How.CSS, using = "[data-ng-click='previewAttachment(attachment)']")
	private List<WebElement> attachmentsFileLinks;
	
	/*
	 * Constructor
	 */
	public AttachmentsTab(WebDriver driver) {
		super(driver);
	}

	/**
	 * Get the number of attachments by counting the number of links from the
	 * attachments tab
	 * 
	 * @return int - count of the number of attachments
	 */
	public int getNumberOfFilesFromAttachmentTab() {
		List<WebElement> attachments = getRows();
		return attachments.size();
	}

	/**
	 * Get the filenames of all attachments from the attachments tab
	 * 
	 * @return List<String> filenames
	 */
	public List<String> getFileNamesFromAttachmentTab() {
		List<WebElement> attachments = getRows();
		return TextHelper.getElementTextContent(attachments);
	}

	/**
	 * Gets a List of WebElements which houses the Attachment Link and the
	 * Delete Button
	 * 
	 * @return List<WebElement> of Files
	 */
	private List<WebElement> getRows() {
		return driver.findElements(rowsSelector);
	}

	/**
	 * Clicks the delete button of the attachment at the given index, starting
	 * index at 0
	 * 
	 * @param index
	 */
	public void clickDeleteAttachmentAtIndex(int index) {
		try {
			WebElement attachmentsRow = getRows().get(index);
			WebElement deleteButton = attachmentsRow.findElement(By.cssSelector(" a[title='Delete Attachment']"));
			UiHelper.click(deleteButton);
		} catch (IndexOutOfBoundsException iobe) {
			Log.logInfo("An Attachment at index [" + (index + 1)
					+ "] could not be found for the Assessment Details Page. Number of Attachments : "
					+ getNumberOfFilesFromAttachmentTab() + ", skipping test.");
			throw new SkipException(
					"There was no attachment at the given index. This is an issue with the test parameters.");
		}
	}

	/**
	 * Check if the Attachment Panel exists and is visible
	 * 
	 * @return boolean - true if exists and is visible, false otherwise
	 */
	public boolean isAttachmentsPanelPresent() {
		return UiHelper.isPresentAndVisible(attachmentsPanel);
	}

	/**
	 * Check if the Upload Attachment exists and is visible
	 * 
	 * @return boolean - true if exists and is visible, false otherwise
	 */
	public boolean isUploadAttachmentsButtonPresent() {
		return UiHelper.isPresentAndVisible(uploadAttachmentsButton);
	}

	/**
	 * Clicks on the Upload Attachments Button
	 * 
	 * @return The UploadFilesPopUp element
	 */
	public UploadFilesPopUp clickUploadAttachmentsButton() {
		Log.logStep("Clicking the Upload Attachments button...");
		UiHelper.click(UiHelper.findElement(uploadAttachmentsButton));
		if (!UiHelper.isPresentAndVisible(By.cssSelector("#filesSource"))) {
			Log.logInfo("The Upload Modal did not appear. Could not create a new Pop Up object. Returning null...");
			return null;
		}
		Log.logInfo("The Upload Modal Pop Up appeared...");
		return new UploadFilesPopUp(UiHelper.findElement(By.cssSelector("#filesSource")));
	}

	/**
	 * Reads the number of Attachments as found on the Attachments Tab between
	 * the brackets. If not found, return 0
	 * 
	 * @return int - the number of files attached as found on the UI in the
	 *         Attachments Tab header
	 */
	public int getNumberOfFilesFromAttachmentsTabHeader() {
		String attachmentsTabHeader = driver.findElement(attachmentsPanel).findElement(By.cssSelector(" h2")).getText();
		String[] parts = TextHelper.getPartsFromDetails(attachmentsTabHeader);
		String number = parts[parts.length - 1];
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException nfe) {
			Log.logStep("The String [" + attachmentsTabHeader
					+ "] could not be parsed to find an integer, specifically this part -> " + number
					+ " and so returning 0.");
			return 0;
		}
	}

	/**
	 * Clicks on the Attachment at the given index
	 * 
	 * @param attachmentsIndex
	 *            int - the index of the File to click, starting at 0
	 */
	public void clickAttachmentLinkAtIndex(int index) {
		try {
			WebElement attachmentsRow = getRows().get(index);
			WebElement link = attachmentsRow.findElement(By.cssSelector("div.text-nowrap>a"));
			UiHelper.click(link);
		} catch (IndexOutOfBoundsException iobe) {
			Log.logInfo("An Attachment at index [" + (index + 1)
					+ "] could not be found for the Assessment Details Page. Number of Attachments : "
					+ getNumberOfFilesFromAttachmentTab() + ", skipping test.");
			throw new SkipException(
					"There was no attachment at the given index. This is an issue with the test parameters.");
		}
	}
	
	/**
	 * Clicks on the Attachment at the given file name
	 * 
	 * @param fileName
	 *            String - the name of the File to click
	 * 
	 * @author HISHAM
	 */
	public void clickAttachmentLinkFor(String fileName) {
		for (WebElement row : attachmentsRow) {
			WebElement link = row.findElement(new By.ByCssSelector("a[data-ng-click='previewAttachment(attachment)']"));
			if (link.getText().equalsIgnoreCase(fileName)) {
				UiHelper.click(link);
			}
		}
	}
	
	/**
	 * Clicks on the delete Attachment at the given file name
	 * 
	 * @param fileName
	 *            String - the name of the File to delete
	 * 
	 * @author HISHAM
	 */
	public ConfirmPopUp clickDeleteAttachmentFor(String fileName) {
		WebElement deleteButton = getDeleteAttachmentsButton(fileName);
		if (null != deleteButton) {
			UiHelper.click(deleteButton);
			return new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#queryConfirmation")));
		} else {
			throw new SkipException("Delete button not found for attachment: [" + fileName + "]. Skipping test...");
		}
	}

	/**
	 * Get the attachment tab locator
	 * 
	 * @return WebElement - locator for attachment tab
	 */
	public WebElement getAttachmentTab() {
		return attachmentsTabLink;
	}
	
	/**
	 * Get the attachment tab locator
	 * 
	 * @return WebElement - locator for attachment tab
	 */
	public List<WebElement> getDeleteAttachmentsButton() {
		return deleteAttachmentsButton;
	}
	
	/**
	 * Returns delete attachment button for a given attachment file
	 * 
	 * @param fileName
	 * @return
	 * 
	 * @author HISHAM
	 */
	public WebElement getDeleteAttachmentsButton(String fileName) {
		for (WebElement row : attachmentsRow) {
			String fileNameFound = row
					.findElement(new By.ByCssSelector("a[data-ng-click='previewAttachment(attachment)']")).getText();
			if (fileNameFound.equalsIgnoreCase(fileName)) {
				WebElement deleteButton = row.findElement(new By.ByCssSelector("a[title='Delete Attachment']"));
				if (UiHelper.isVisible(deleteButton))
					return deleteButton;
				return null;
			}
		}
		return null;
	}
	
	/**
	 * to get any random attachment link webelement
	 * 
	 * @return WebElement - any random element
	 */
	public WebElement getAttachmentWebelement() {
		WebElement returnElement = null;
		returnElement = UiHelper.getRandomObjectFromList(attachmentsFileLinks);
		return returnElement;
	}
	
	/**
	 * Check if the Upload Attachment exists and is visible
	 * 
	 * @return boolean - true if exists and is visible, false otherwise
	 */
	public boolean isDeleteAttachmentsButtonPresent() {
		boolean isPresent = false;
		for (WebElement deleteAttachmentButton : deleteAttachmentsButton) {
			isPresent = UiHelper.isPresentAndVisible(deleteAttachmentButton);
		}
		return isPresent;
	}
	
	/*
	 * Attachments Tab and its controls----------------------------------------------------------------------------------
	 */
	/**
	 * Checks if the Attachments Tab Link exists and is visible
	 * @return boolean - true if exists and is visible, false otherwise
	 */
	public boolean isAttachmentsTabLinkPresent(){
		return UiHelper.isPresentAndVisible(attachmentsTabLink);
	}
	
	/**
	 * Checks if the Attachments Tab is visible and selected
	 * @return boolean - true if visible & selected, false otherwise
	 * 
	 * @author HISHAM
	 */
	public boolean isAttachmentsTabSelected(){
		return UiHelper.isSelected(attachmentsTab);
	}
	
	/**
	 * Returns attachment count from link on Attachments tab
	 * 
	 * @param value 
	 * @return integer value in parentheses
	 * 
	 * @author HISHAM
	 */
	public int getAttachmentCount() {
		String attachmentCount;
		try {
			attachmentCount = attachmentsTabLink.findElement(new By.ByCssSelector("span"))
					.getText();
			return Integer.parseInt(TextHelper.splitParentheses(attachmentCount));
		} catch (NoSuchElementException e) {
			Log.logInfo("Attachment count on tab header found: 0");
			return 0;
		}
	}
	
	/**
	 * Clicks on the Attachments Tab Link
	 * @return AttachementsTab PageObject
	 */
	public AttachmentsTab clickAttachmentsTabLink(){
		UiHelper.waitForVisibility(attachmentsTabLink);
		UiHelper.click(attachmentsTabLink);
		return PageFactory.initElements(driver, AttachmentsTab.class);
	}
}