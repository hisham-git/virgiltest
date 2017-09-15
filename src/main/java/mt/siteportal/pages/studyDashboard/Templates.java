package mt.siteportal.pages.studyDashboard;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class Templates extends BasePage{

    private String asignDropDown = "//div[@id='virgilForms']/div[%s]//button[@class='btn btn-default dropdown-toggle']";
//	private String notCompetedLink = "//div[@data-ng-repeat='form in forms'][%s]//span[contains(text(),'Not completed')]";
	private String notCompetedLink = "//div[@class='row ng-scope']/a[contains(text(),'Not completed')]";

	@FindBy(how = How.XPATH, using = "//div[@class='form-cover-mask']/a/img[2]")
    private WebElement ImageOfTemplate;

	public By templateCount = new By.ByXPath("//span[text()='Templates']/following-sibling::span");

	public By templateStatus = new By.ByXPath("//div[@id='virgilForms']//div[@class='small']/label");
	
	public Templates(WebDriver driver) {
	    super(driver);	    
	    }
	
	public void assignTemplateTo(int position, String raterName) {
		Log.logInfo("Asigning template to: [" + raterName + "]");
		UiHelper.findElementByXpath(String.format(asignDropDown, Integer.toString(position))).click();
		// UiHelper.findElementByXpath("//span[text()='
		// "+raterName+"']").click();
		UiHelper.click(new ByXPath("//span[text()=' " + raterName + "']"));
		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	/**
	 * Selects in the dropdown Not Assigned value
	 */
	public void unAssignTemplate(int position){
		assignTemplateTo(position, "Not Assigned");
	}

	/**
	 * Selects in the dropdown Not Assigned value for first template
	 */
	public void unAssignFirstTemplate(){
		assignTemplateTo(1, "Not Assigned");
	}

	public AssessmentDetails openTemplate(){
		Log.logInfo("Opening template");
		//UiHelper.waitFor(ImageOfTemplate);
		UiHelper.click(ImageOfTemplate);
		AssessmentDetails assesmentDetails =  PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		UiHelper.waitFor(assesmentDetails.notAdministered);
		return assesmentDetails;
	}

	public String getNameOfAssignee(){
		Log.logInfo("Getting Name of Assignee");
		return UiHelper.getText(new By.ByXPath("//span[@class='text-ellipsis-dd ng-binding']"));
	}

	public String getTemplateCount() {
		Log.logInfo("Getting the count of tempaltes for correspond visit");
		// Introduced due to no support for getting text from a hidden element
		// in WebDriver getText() method
		String count = Browser.getDriver().findElement(templateCount).getAttribute("innerHTML");
		if (count.equals("")) {
			return "0";
		} else {
			return TextHelper.splitParentheses(count);
		}
	}

	public EsignDialog asNotCompleted(int position){
		Log.logInfo("Marking template as not completed");
		UiHelper.findElementByXpath(String.format(notCompetedLink, Integer.toString(position))).click();
		UiHelper.checkPendingRequests(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(),EsignDialog.class);
	}

	public String getTemplateStatus(){
		Log.logInfo("Getting status of template for correspond visit");
		return UiHelper.getText(templateStatus);
	}
}
