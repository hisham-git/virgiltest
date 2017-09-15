package mt.siteportal.pages.studyDashboard;

import java.util.ArrayList;
import java.util.List;

import mt.siteportal.details.RaterDetails;
import mt.siteportal.objects.Rater;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.SortBy;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class StudyRaters extends BasePage {
	private String tableLocator = "//div[@id='portal-grid-page-content']//div[count(div)=4][@class!='row']/*";
	private static final String RATER = "rater";
	private static final String SCHEDULED = "scheduled";
	private static final String COMPLETED = "completed";
	private static final String EDITED = "edited";
	
	
	@FindBy(how = How.XPATH, using = "//div[@class='row']/a[@title='Close']")
	public WebElement CloseButton;
	
	/*@FindBy(how = How.XPATH, using = "//h1[starts-with(text(),'Raters')]")
	public WebElement Header;*/
	
	@FindBy(how = How.XPATH, using = "//div[@class='slide-panel-header']//*[@title='Refresh']")
	public WebElement RefreshButton;
	//
	public By scheduled = new By.ByXPath("//img[@title='Scheduled']");

	public By completed = new By.ByXPath("//img[@title='Completed']");

	public By edited = new By.ByXPath("//img[@title='Editing']");

	public By rater = new By.ByXPath("//label[text()='Rater']");

	private List<Rater> table;
	private String ratersCount;
	public StudyRaters(WebDriver driver) {
		super(driver);
//		if (ratersCount != "0"){
//			setTable(readRatersFromWeb());
//		}
//		else{
//			setTable(null);
//	}
}
	
	public void sortBy(SortBy sortBy){
		Log.logInfo(String.format("Clicking on the %s to sort", sortBy.getValue()));
	        switch (sortBy) {
	            case RATER:  UiHelper.click(rater);
	                     break;
	            case SCHEDULED : UiHelper.click(scheduled);
	                     break;
	            case COMPLETED:  UiHelper.click(completed);
                		 break;
	            case EDITED:  UiHelper.click(edited);
                		 break;
	        } 
	}
	
	public List<String> getDataAfterSotring(SortBy byName) {
		List<String> data = new ArrayList<String>();
		Log.logInfo(String.format("Clicking on the %s to sort", byName));
		switch (byName) {
		case RATER:
			data = TextHelper.getElementTextContent(UiHelper
					.findElements(new By.ByXPath(tableLocator + "[1]")));
			break;
		case SCHEDULED:
			data = TextHelper.getElementTextContent(UiHelper
					.findElements(new By.ByXPath(tableLocator + "[2]")));
			break;
		case COMPLETED:
			data = TextHelper.getElementTextContent(UiHelper
					.findElements(new By.ByXPath(tableLocator + "[3]")));
			break;
		case EDITED:
			data = TextHelper.getElementTextContent(UiHelper
					.findElements(new By.ByXPath(tableLocator + "[4]")));
			break;
		}
		return data;
	}
	
	public void closeRaters(){
		Log.logInfo("Closing Raters");
		UiHelper.click(CloseButton);
		//UiHelper.checkPendingRequests(Browser.getDriver());
	}
	
	
	public String getRatersCount(){
		ratersCount =
				TextHelper.splitParentheses(UiHelper.getText(new By.ByXPath("//h1[starts-with(text(),'Raters')]")));
		return ratersCount;
	}
	private  List<Rater> readRatersFromWeb(){
		List<Rater> table = new ArrayList<Rater>();
		List<WebElement> elements = UiHelper.findElements(new By.ByXPath(tableLocator));
		List<String> textFromElements = TextHelper.getElementTextContent(elements);
		
		for (int i = 0; i<textFromElements.size();i=i+4){
			table.add(new Rater(textFromElements.get(i), textFromElements.get(i+1), textFromElements.get(i+2), textFromElements.get(i+3)));
		}
		return table;
	}

	
	
	public RaterDetails openRaterDetails(String raterName){
		Log.logInfo("Opening details of "+raterName);
		UiHelper.click(new By.ByXPath("//label[text()='"+raterName+"']"));

		return PageFactory.initElements(Browser.getDriver(), RaterDetails.class);
	}

	public List<Rater> getListOfRaterObjects() {
		setTable(readRatersFromWeb());
		return table;
	}

	public void setTable(List<Rater> table) {
		this.table = table;
	}
	/*-------------------------------NZ team-----------------------------------------------*/
	@FindBy(css=".sliding-container.raters-panel.ng-isolate-scope.opened")
	private WebElement panel;

	public boolean isRaterPanelOpened() {
		Log.logInfo("Verification if Raters Panel is opened.");
		try{
			if(panel.isDisplayed() && panel.isEnabled()) {
				Log.logInfo("Raters Panel is Open.");
				return true;
			}
			Log.logInfo("Raters Panel is NOT Open.");
			return false;
		}catch(NoSuchElementException ne){
			return false;
		}
	}
}

