
package nz.siteportal.pages.studydashboard.ListPages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.DetailPages.AddSubject;


/**
 * Page Object for the Subject List that contains functions and elements
 * specific to this page.
 * 
 * @author Syed A. Zawad
 */
public class SubjectList extends DashboardList {

	/*
	 * The Add Subject Button's parent div, which is needed to select the list of Sites as well
	 */
	@FindBy(css = ".add-subject")
	private WebElement addSubject;

	/*
	 * Constructor
	 */
	public SubjectList(WebDriver driver) {
		super(driver);
	}

	/**
	 * Checks if the Page Object opened is for the Subject List. It does so by first checking for the presence of the Table, and then checks if the 
	 * Header says it is Subject. returns true if both the conditions are met, false otherwise
	 * 
	 * @return boolean
	 */
	public boolean isOpened() {
		return isOpened("Subjects");
	}

	/**
	 * Function to click Add Subject and then Select the Site from the Add Subject's Site Options
	 * Causes a redirect to the SubjectDetails page if done correctly, otherwise returns a null element
	 * 	
	 * @param site_name String - The site to be Selected from the Add Button dropdown menu
	 * @return SubjectDetails - if successfully clicked all elements
	 * 		   null - otherwise
	 */
	public AddSubject addSubjectToSite(String site_name) {
		try {
			WebElement add_button = getAddButton();
			if (!getSiteName().equals("") || site_name.equals("")) {
				Log.logStep("Clicking Add Subject button for Site : + " + getSiteName() + "...");
				UiHelper.click(add_button);
				return PageFactory.initElements(driver, AddSubject.class);
			}
			WebElement sites_list = addSubject.findElement(By.cssSelector("ul"));
			if (!sites_list.isDisplayed()) {
				Log.logStep("Clicking Add Subject button...");
				UiHelper.click(add_button);
			}
			Log.logStep("Clicking Site " + site_name);
			UiHelper.click(sites_list.findElement(By.xpath("li/a[text()='" + site_name + "']")));
			return PageFactory.initElements(driver, AddSubject.class);
		} catch (NoSuchElementException ne) {
			Log.logInfo("One of the Add New Subject Elements was not found. Cannot redirect to Subject Details Page..");
			return null;
		}
	}

	/**
	 * Iterates over the full List until a row with the Subject Name as given in the parameter can be found.
	 * If found, click the row and return the SubjectDetails Page.
	 * If not found, return null
	 * 
	 * @param subjectName String - the name of the subject to click
	 * @return SubjectDetails if the desired row is found
	 * 		   null otherwise
	 */
	public SubjectDetails clickRowForSubject(String subjectName) {
		Log.logStep("Looking for Subject " + subjectName);
		Actions actions = new Actions(Browser.getDriver());
		WebElement subject = getFirstItemFromList();
		actions.moveToElement(subject);
		while (subject != null) {
			if (subject.findElement(By.xpath("div[" + getIndexOf("Subject") + "]")).getText()
					.equalsIgnoreCase(subjectName)) {
				Log.logStep("Found it. Clicking on subject row....");
				UiHelper.click(subject);
				Log.logStep("Redirecting to Subject Details page....");
				return PageFactory.initElements(driver, SubjectDetails.class);
			}
			subject = nextRow(subject);
		}
		Log.logStep("Could not find Subject called " + subjectName + ". Returning null...");
		return null;
	}

	/**
	 * Locates and returns the Add Subject Button. Returns null if not found
	 * @return WebElement or null
	 */
	public WebElement getAddButton() {
		WebElement sbjAddBtn = addSubject.findElement(By.cssSelector("a"));
		if (UiHelper.isClickable(sbjAddBtn))
			return sbjAddBtn;
		Log.logStep("Add Button in Subject List Page not Found. Returning null");
		return null;
	}

	/**
	 * Clicks on the Add Button, records a List of all the Sites present in the list as a String
	 * 
	 * @return List<String>
	 */
	public List<String> getAddToSiteOptions() {
		WebElement sites;
		try{
			sites = addSubject.findElement(By.cssSelector("ul"));
		}catch(NoSuchElementException any){
			Log.logInfo("No site found via add  button dropdown");
			return null;
		}
		
		if (!sites.isDisplayed()) {
			UiHelper.click(addSubject.findElement(By.cssSelector("a")));
		}
		
		List<WebElement> sites_list = addSubject.findElements(By.cssSelector("ul>li"));
		List<String> listTexts = TextHelper.getElementTextContent(sites_list);
		if (sites.isDisplayed()) {
			UiHelper.click(addSubject.findElement(By.cssSelector("a")));
		}
		return listTexts;
	}
}
