package nz.siteportal.pages.studydashboard.DetailPages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.tools.Log;
/**
 * The PageObject for the Add New Subject page
 * TODO: Everything....
 * @author Syed A. Zawad
 *
 */
public class AddSubject extends BasePage {

	/*
	 * The header WebElement
	 */
	@FindBy(xpath = "//div[@id='page-title']/h1[2]")
	private WebElement header;

	/*
	 * Constructor
	 */
	public AddSubject(WebDriver driver) {
		super(driver);
	}

	/**
	 * Checks if the header text is present and whether the header text shows "Subject: New" which
	 * should be the case for any Add New Subject Page
	 * @return boolean, true if both the above conditions are met
	 * 					false otherwise
	 */
	public boolean isOpen() {
		try {
			String headerText = header.getText();
			if (headerText.equalsIgnoreCase("Subject: New"))
				return true;
			Log.logStep("The Header Text was not correct. It shows [" + headerText
					+ "] but should have been [Subject: New], indicating this is not the Add New Subject Page");
			return false;
		} catch (NoSuchElementException ne) {
			Log.logStep("The Header could not be found, indicating this is not the Add New Subject Page");
			return false;
		}
	}

}
