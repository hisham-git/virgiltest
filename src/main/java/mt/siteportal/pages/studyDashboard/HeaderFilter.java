package mt.siteportal.pages.studyDashboard;

import mt.siteportal.pages.Header;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.Table;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.PageFactory;

public class HeaderFilter extends Header {

	private By filterType = new By.ByXPath("//div[@class='input-group-btn']/a");
	private By filterInput = new By.ByCssSelector(".tags-list-viewport");
	private By clear = new By.ByCssSelector(".clear-filter");
	
	
	public Table filterBy(String byName, String field, String value){
		Log.logInfo(String.format("Filtering records by %s=%s", field, value));
		UiHelper.click(filterType);
		UiHelper.click(new By.ByXPath("//a[text()='"+byName+"']"));
		UiHelper.click(filterInput);
		UiHelper.click(new By.ByXPath("//a[text()='"+field+"']"));
		UiHelper.click(new By.ByXPath("//a[text()='"+value+"']"));
		UiHelper.sendKeys(new By.ById("input-holder"), Keys.RETURN);
		switch(byName){
			case "Subjects": return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class); 
			case "Visits": return PageFactory.initElements(Browser.getDriver(), VisitTable.class); 
			case "Assessments": return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
		}
		return null;
	}
	
	public Table filterByFieldOnly(String byName, String field, String value){
		Log.logInfo(String.format("Filtering records by %s=%s", field, value));
		UiHelper.click(filterInput);
		UiHelper.click(new By.ByXPath("//a[text()='"+field+"']"));
		UiHelper.click(new By.ByXPath("//a[text()='"+value+"']"));
		UiHelper.sendKeys(new By.ById("input-holder"), Keys.RETURN);
		switch(byName){
			case "Subjects": return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class); 
			case "Visits": return PageFactory.initElements(Browser.getDriver(), VisitTable.class); 
			case "Assesments": return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
		}
		return null;
	}
	
	private String getFilterType(){
		Log.logInfo("Getting Filter Type");
		return UiHelper.getText(filterType);
	}
	
	public Table clearFilter(){
		Log.logInfo("Clearing filter");
		String filterName = getFilterType();
		UiHelper.scrollToElementWithJavascript(clear, Browser.getDriver());
		UiHelper.click(clear);
		switch(filterName){
			case "Subject": return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class); 
			case "Visit": return PageFactory.initElements(Browser.getDriver(), VisitTable.class); 
			case "Assesment": return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
	}
		return null;
	}
}
