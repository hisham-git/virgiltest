package mt.siteportal.details;



import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;


public class CommonDetails extends BasePage {
	
	@FindBy(how = How.CSS, using = "li[ng-click='currentTab = 1']")
	public WebElement attachmentsTab;
	
	@FindBy(how = How.CSS, using = "a[href='#attachments']")
	public WebElement attachmentsTabLink;
	
	/*private By attachmentsTab = By.cssSelector("li[ng-click='currentTab = 1']");
	private By attachmentsTabLink = By.cssSelector("a[href='#attachments']");*/

	public CommonDetails(WebDriver driver) {
		super(driver);
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