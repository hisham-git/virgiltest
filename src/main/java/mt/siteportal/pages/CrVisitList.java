package mt.siteportal.pages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.controls.DayPickerWidget;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class CrVisitList extends BasePage {
	
	
	@FindBy(how = How.CSS, using = "*[data-ma-filter-drop-down-change='subjectStatus'] .btn.dropdown-toggle.btn-default")
    private By crSubjectStatusBtn;
	
	@FindBy(how = How.CSS, using = "*[data-ma-filter-drop-down-change='subjectVisitStatus'] .btn.dropdown-toggle.btn-default")
    private By crSubjectVisitStatusBtn;
	
	@FindBy(css=".picker-open .datepicker>div.datepicker-days>table")
	private WebElement datePickerElement;	
	
	private By crAssessmentLabel = new By.ByCssSelector("h1.ng-binding");
	private By crRefreshButton = new By.ByCssSelector(".icon-small.icon-refresh-b");
	private By crResetBtn = new By.ByCssSelector("div.pull-right > button.btn.btn-default");
	
	private String  scheduleDate = ".col-md-4.col-sm-4.col-xs-4 .ellipsis.ng-binding";
	private String crVisitCommentTooltip = ".col-md-1.col-sm-1.col-xs-1 .comment-container.text-nowrap .icon-query.ng-scope";
	private String crVisitComment = ".col-md-1.col-sm-1.col-xs-1 div.hover-block label.ng-binding";
	private String crScheduledVisitStatus = ".row.grid-row.ng-scope div:nth-child(8) label";
	private String crScheduledVisitCommentTooltip = ".col-md-4.col-sm-4.col-xs-4 .icon-query.ng-scope";
	private String crScheduledVisitComment = ".col-md-4.col-sm-4.col-xs-4 div.hover-block label.ng-binding";
	private String crSubjectStatus = ".row.grid-row.ng-scope div:nth-child(2) label";
	private String crStudyName = ".row.grid-row.ng-scope div:nth-child(4) label";
	
	@FindBy(css = ".row.grid-row.ng-scope")
	protected List<WebElement> listItems;
	
	
	
	public void setStartedDate(String date, String month, String year, String datePickerFor) {
		By crScheduleDateFromBtn = new By.ByCssSelector("*[data-value='"+datePickerFor+"'] .add-on.icon-calendar.datepickerbutton");
		if ( null != UiHelper.fluentWaitForElementVisibility(crScheduleDateFromBtn, 5))
			UiHelper.click(crScheduleDateFromBtn);
		DayPickerWidget datePicker = new DayPickerWidget(datePickerElement);
		datePicker.setYear(year);
		datePicker.setMonth(month.toUpperCase());
		datePicker.setDate(date);
		
		UiHelper.sleep();
	}
	
	
	public CrVisitList chooseStatus(String status, String nameOfSubjectStatus) {
		By subjectStatusBtn = new By.ByCssSelector("*[data-ma-filter-drop-down-change='"+status+"'] .btn.dropdown-toggle.btn-default");
		if (null != UiHelper.fluentWaitForElementClickability(subjectStatusBtn, 30)) {
			UiHelper.scrollToElementWithJavascript(subjectStatusBtn, Browser.getDriver());
			UiHelper.selectInDropdownBtn(subjectStatusBtn, nameOfSubjectStatus);
			UiHelper.sleep();
			return PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
		} else {
			Log.logError("Couldn't choose a status because of dropdown not available within timeout");
			return null;
		}
	}
	
	public CrVisitList chooseDatePicker(String date, String datePicker){
		String[] dateParts = date.split("-");
		setStartedDate(dateParts[0], dateParts[1], dateParts[2], datePicker);
		return PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
	}



	public boolean isCentralRatingOpened() {
		String txt = UiHelper.findElement(crAssessmentLabel).getText().toString();		
		if(txt.startsWith("CR Assessments")){
			return true;
		}
		return false;
	}



	public boolean crAssessmentLabelisDispalyed() {
		return isCentralRatingOpened();
		
	}



	public boolean crRefreshIconIsdisplayed() {
		if(UiHelper.isPresent(crRefreshButton)){
			return true;
		}
		return false;
	}



	public boolean crScheduleDatePickerIsDisplayed(String dateFrom) {
		if (UiHelper.isPresent(
				new By.ByCssSelector("*[data-value='" + dateFrom + "'] .add-on.icon-calendar.datepickerbutton"))) {
			return true;
		}
		return false;
	}



	public String crScheduleDatePickerGetSelectedDate(String dateFrom) {
		return UiHelper
				.findElement(new By.ByCssSelector(
						"*[data-value='" + dateFrom + "'] >div.date-wrapper >div.value>label.ng-binding"))
				.getText().toString();
	}



	public boolean crExpectedColHeaderIsPresent(String colName) {
		if(UiHelper.isPresent(new By.ByCssSelector("*[title='Order by "+colName+"']"))){
			return true;
		}
		return false;
	}
	

	public boolean getVisitScheduleDate(String currentDate) {
		String getCurrentVisitSchedleDate;
		boolean bol = true;
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		while (visit != null) {
			getCurrentVisitSchedleDate = null;
			actions.moveToElement(visit).perform();
			WebElement e = visit.findElement(new By.ByCssSelector(scheduleDate));
			getCurrentVisitSchedleDate = e.getText().toString();
			if (!getCurrentVisitSchedleDate.contains(currentDate)) {
				if (!getCurrentVisitSchedleDate.isEmpty())
					bol = false;
			}
			visit = nextRow(visit);
			UiHelper.fastWait(Browser.getDriver());
		}
		return bol;
	}

	public boolean crVisitsAreForCurrentDateDispalyed(String currentDate) {
		return getVisitScheduleDate(currentDate);
		
	}


	public boolean crResetButtonIsdisplayed() {
		if(UiHelper.isPresent(crResetBtn)){
			UiHelper.click(crResetBtn);
			return true;			
		}
		return false;
	}
	
	public CrVisitList crClikResetButton(){
		UiHelper.click(crResetBtn);
		return PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
	}

	public boolean crExpectedSearchBoxIsPresent(String colName) {
		if(UiHelper.isPresent(new By.ByXPath("//input[@data-ma-filter-text-change ='"+colName+"']"))){
			return true;
		}
		return false;
	}



	public boolean crExpectedStatusDropdownIsPresent(String colName) {
		if(UiHelper.isPresent(new By.ByXPath("//div[@data-ma-filter-drop-down-change='"+colName+"']"))){
			return true;
		}
		return false;
	}
	

	public WebElement getFirstItemFromList() {
		if (listItems.size() > 0) {
			return listItems.get(0);
		}
		return null;
	}

	public WebElement nextRow(WebElement pointer) {
		try {
			WebElement next = pointer.findElement(By.xpath("following-sibling::div"));
			return next;
		} catch (NoSuchElementException ne) {
			return null;
		}
	}


	public boolean crGetExpectedVisitList(String currentDate, String selectedDate) {
		String getCurrentVisitSchedleDate;
		boolean bol = true;
		
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		while (visit != null) {
			getCurrentVisitSchedleDate = null;
			actions.moveToElement(visit).perform();
			WebElement e = visit.findElement(new By.ByCssSelector(scheduleDate));
			getCurrentVisitSchedleDate = e.getText().toString();
			if (!getCurrentVisitSchedleDate.contains(currentDate)){
					if(!getCurrentVisitSchedleDate.contains(selectedDate)) {
						if (!getCurrentVisitSchedleDate.isEmpty())
							bol = false;
					}
			}
			visit = nextRow(visit);
			UiHelper.fastWait(Browser.getDriver());
		}
		return bol;
		
	}


	public CrVisitList crScheduleDateFromAndToSetTocurrentDateAfterClickOnResetButton() {
		if(UiHelper.isClickable(crResetBtn)){
			UiHelper.click(crResetBtn);			
		}
		return PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
	}


	public boolean crRVisitListIsRefreshedAfterClickOnRefreshIcon() {
		UiHelper.click(crRefreshButton);
		return true;
	}


	public boolean crVisitCommentsAreDispalyedAsTooltip() {
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		boolean isCommentFound = true;
		while (visit != null) {
			actions.moveToElement(visit).perform();
			if (checkToolTipIsPresent(visit, crVisitCommentTooltip)) {
				if (checkToolTipContainsComments(visit, crVisitComment)) {
					isCommentFound = false;
				}
			}
			visit = nextRow(visit);
//			UiHelper.fastWait(Browser.getDriver());
		}

		return isCommentFound;
	}


	private boolean checkToolTipContainsComments(WebElement visit, String comments) {
		WebElement el = visit.findElement(new By.ByCssSelector(comments));
		if (!el.getText().toString().isEmpty()) {
			return true;
		}
		return false;
	}


	private boolean checkToolTipIsPresent(WebElement visit, String crTooltip) {
		
		if(findExpectedElement(visit,crTooltip )){
			return true;
		}
		return false;
	}
	
	
	private boolean findExpectedElement(WebElement visit, String element){
		try{
			visit.findElement(new  By.ByCssSelector(element));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}


	public boolean crScheuledTimeSiteTimeZonesAreDispalyedForScheduledVisit() {
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		boolean isCommentFound = true, scheduled = false;
		while (visit != null) {
			actions.moveToElement(visit).perform();
			if(IsSelectedVisitScheduled(visit,crScheduledVisitStatus )){
				scheduled = true;
				if (checkToolTipIsPresent(visit, crScheduledVisitCommentTooltip)) {
					if (checkToolTipContainsComments(visit, crScheduledVisitComment)) {
						isCommentFound = false;
					}
				}
			}
			
			visit = nextRow(visit);
//			UiHelper.fastWait(Browser.getDriver());
		}

		return isCommentFound && scheduled;
	}


	private boolean IsSelectedVisitScheduled(WebElement visit, String scheduledVisit) {
		WebElement e = visit.findElement(new By.ByCssSelector(scheduledVisit));
		if (e.getText().contains("Scheduled")) {
			return true;
		} else {
			return false;
		}
	}


	public boolean crVisitListAreFilteredBasedOnSubjectStatus(int currentListSize, String subjectStatus) {
		
		boolean bol = false;
		if(currentListSize>0){
			if(checkVisitStatusForCurrentList(crSubjectStatus, subjectStatus)){
				bol = true;
			}
			
		}
		return bol;
	}


	private boolean checkVisitStatusForCurrentList(String statusElement, String subjectStatus) {
		
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		boolean isStatusFound = true;
		while (visit != null) {
			actions.moveToElement(visit).perform();
			if(!visit.findElement(new By.ByCssSelector(statusElement)).getText().toString().equalsIgnoreCase(subjectStatus)){
				isStatusFound = false;
			}
			
			visit = nextRow(visit);
			UiHelper.fastWait(Browser.getDriver());
		}

		return isStatusFound;
		
		
	}
	
	public int getCurrentListSize(){
		List<WebElement> listItem = UiHelper.findElements(new By.ByCssSelector(".row.grid-row.ng-scope"));
		return listItem.size();
	}


	public boolean crVisitListAreFilteredBasedOnVisitStatus(int currentListSize, String visitStatus) {
		boolean bol = false;
		if(currentListSize>0){
			if(checkVisitStatusForCurrentList(crScheduledVisitStatus, visitStatus)){
				bol = true;
			}
			
		}
		return bol;
	}
	
	
	public boolean studyFormatChecking(String studyName){
		//TODO:
		
		Pattern str = Pattern.compile("\\w*-*");
		Matcher m = str.matcher(studyName);
		return m.find();
	}


	public boolean crStudyNameFormatIsCorrect() {
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		boolean isStatusFound = true;
		while (visit != null) {
			actions.moveToElement(visit).perform();
			String studyName = visit.findElement(new By.ByCssSelector(crStudyName)).getText().toString();
			if(!studyFormatChecking(studyName)){
				isStatusFound = false;
			}
			
			visit = nextRow(visit);
			UiHelper.fastWait(Browser.getDriver());
		}

		return isStatusFound;
	}


	public String getSearchItem(int itemNum) {
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		if(visit!=null){
			return visit.findElement(new By.ByCssSelector(".row.grid-row.ng-scope div:nth-child("+itemNum+") label")).getText().toString();
		}
		return null;
	}


	public CrVisitList filteredVisitListBySearch(String getSearchOption, String searchbox) {
		WebElement el = UiHelper.findElement(new By.ByXPath("//input[@data-ma-filter-text-change ='"+searchbox+"']"));
		if(UiHelper.isPresent(el)){
			el.clear();
			el.sendKeys(getSearchOption);
			UiHelper.sleep();
			return PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
		}
		return null;
		
	}


	public boolean checkingFilteringListBySearchOption(String getSearchOption, int i) {
		boolean bol = false;
		int currentListSize = getCurrentListSize();
		if(currentListSize>0){
			if(checkVisitForCurrentSearch(getSearchOption,i)){
				bol = true;
			}
			
		}
		return bol;
	}


	private boolean checkVisitForCurrentSearch(String getSearchOption,int itemNum) {
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		actions.moveToElement(visit);
		boolean bol = false;
		while(visit!=null){
			WebElement e =  visit.findElement(new By.ByCssSelector(".row.grid-row.ng-scope div:nth-child("+itemNum+") label"));
			if(e.getText().toString().equalsIgnoreCase(getSearchOption)){
				bol = true;
			}
			visit = nextRow(visit);
			UiHelper.fastWait(Browser.getDriver());
		}
	
		return bol;
	}
}
