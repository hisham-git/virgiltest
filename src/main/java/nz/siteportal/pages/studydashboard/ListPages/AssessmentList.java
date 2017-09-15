package nz.siteportal.pages.studydashboard.ListPages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Page Object for the Assessment List that contains functions and elements
 * specific to this page.
 * 
 * @author Syed A. Zawad
 */
public class AssessmentList extends DashboardList {
	
	/*
	 * Constructor
	 */
	public AssessmentList(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * Checks if the Page Object opened is for the Assessment List. It does so by first checking for the presence of the Table, and then checks if the 
	 * Header says it is Assessment. returns true if both the conditions are met, false otherwise
	 * 
	 * @return boolean
	 */
	public boolean isOpened() {
		return isOpened("Assessments");
	}
	
	/**
	 * Iterates over the full List until a row with the Assessment Name and SVID as given in the parameters can be found.
	 * If found, click the row and return the AssessmentDetails Page.
	 * If not found, return null
	 * 
	 * @param subjectName String - the name of the subject to click
	 * @return AssessmentDetails if the desired row is found
	 * 		   null otherwise
	 */
	public AssessmentDetails clickRow(String assessmentName, String svid) {
		Log.logInfo("Trying to click an Assessment with Form Name : " + assessmentName + " and SVID : " + svid);
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			String currentRowsAssessmentName = assessment.findElement(By.xpath("div[" + getIndexOf("Assessment") + "]"))
					.getText();
			System.out.println("currentRowsAssessmentName: " + currentRowsAssessmentName);
			String currentRowsSVID = assessment.findElement(By.xpath("div[" + getIndexOf("SVID") + "]")).getText();
			if (currentRowsAssessmentName.equalsIgnoreCase(assessmentName) && currentRowsSVID.equalsIgnoreCase(svid)) {
				Log.logStep("Found the row with the matching Assessment, clicking it....");
				UiHelper.click(assessment);
				Log.logStep("Redirecting to Assessment Details page...");				
				return PageFactory.initElements(driver, AssessmentDetails.class);
			}
			assessment = nextRow(assessment);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("Could not find any Assessment with these values.");
		return null;
	}
	
	/**
	 * clicks on the Assessment that matches the colValue
	 * 
	 * @param colName
	 * @param colValue
	 * 
	 */
	public WebElement getRowForAnAssessment(String formName, String svid) {
		Log.logInfo("Trying to click an Assessment with Form Name : " + formName + " and SVID : " + svid);
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			String currentRowsAssessmentName = assessment.findElement(By.xpath("div[" + getIndexOf("Assessment") + "]"))
					.getText();
			String currentRowsSVID = assessment.findElement(By.xpath("div[" + getIndexOf("SVID") + "]")).getText();
			if (currentRowsAssessmentName.equalsIgnoreCase(formName) && currentRowsSVID.equalsIgnoreCase(svid)) {
				return assessment;
			}
			assessment = nextRow(assessment);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("No such Assessment found.");
		return null;
	}
	
	/**
	 * 
	 * @param assessmentRow
	 * @return the Visit Details page
	 */
	public AssessmentDetails clickAnAssessment(WebElement assessmentRow) {
		Log.logStep("Clicking on the assessment... ");
		UiHelper.click(assessmentRow);
		Log.logInfo("Clicked on the Assessment.");
		Log.logStep("Redirecting to Assessment Details page...");
		return PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
	}
	
	/**
	 * clicks on the Assessment that matches the colValue
	 * Clicks on the 1ft matching row if multiple exist
	 * 
	 * @param colName
	 * @param colValue
	 * 
	 */
	public WebElement getRowForAssessment(String colName, String colValue) {
		Log.logStep("Searching the assessment with:"+colName+" ="+colValue+"...");
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			String currentRowAssessType = assessment.findElement(By.xpath("div[" + getIndexOf(colName) + "]"))
					.getText();
			if (currentRowAssessType.equalsIgnoreCase(colValue)) {
				Log.logInfo("Found an Assessment with:"+colName+" ="+colValue);
				return assessment;
			}
			assessment = nextRow(assessment);
			while (UiHelper.getNumberOfOpenAjaxConnections(driver) != 0) {
				UiHelper.sleep(100);
			}
		}
		Log.logInfo("No such Assessment found.");
		return null;
	}

	/**
	 * @author HISHAM
	 * 
	 * Improved version for 3 Assessment Types: ObsRO, PRO & ClinRO
	 * 
	 * @return True/False
	 */
	
	public boolean diffTypesOfAssessmentExists() {
		WebElement currentRowAssessmentLink = getFirstItemFromList();
		
		if (null != currentRowAssessmentLink) {			
			boolean ClinRO = false;
			boolean ObsRO = false;
			boolean PRO = false;
			
			String columnXpathLocator = "div[" + getIndexOf("Type") + "]";
			String currentRowAssessmentType = null;
			WebElement nextRowAssessmentLink = null;
						
			Actions actions = new Actions(Browser.getDriver());
			actions.moveToElement(currentRowAssessmentLink).perform();
			
			do {
				try {
					currentRowAssessmentType = currentRowAssessmentLink.findElement(By.xpath(columnXpathLocator)).getText();
				} catch (StaleElementReferenceException staleEx) {
					Log.logInfo("Found StaleElementReferenceException. Resolving it by initialing 'currentRowAssessmentType'...");
					currentRowAssessmentLink = getFirstItemFromList();
					currentRowAssessmentType = currentRowAssessmentLink.findElement(By.xpath(columnXpathLocator)).getText();
				}
				
				switch (currentRowAssessmentType) {
				case "ClinRO":
					Log.logInfo(currentRowAssessmentType + " : Type found");
					ClinRO = true;
					break;
				case "ObsRO":
					Log.logInfo(currentRowAssessmentType + " : Type found");
					ObsRO = true;
					break;
				case "PRO":
					Log.logInfo(currentRowAssessmentType + " : Type found");
					PRO = true;
					break;
				default:
					Log.logInfo( "ClinRO/ObsRO/PRO type Assessments not found");
					break;
				}

				if ( ClinRO == true && ObsRO == true && PRO == true ) {
					Log.logInfo("Found all three possible types of Assessments. Skippping search...");
					break;
				}
				
				nextRowAssessmentLink = this.nextRow(currentRowAssessmentLink);
				if (null != nextRowAssessmentLink)
					actions.moveToElement(nextRowAssessmentLink).perform();
				UiHelper.fastWait(Browser.getDriver());
				currentRowAssessmentLink = nextRowAssessmentLink;
			} while ( null != nextRowAssessmentLink );
			
			if ( ClinRO == true && ObsRO == true && PRO == true ) {
				return true;
			} else {
				Log.logInfo("All three possible types of Assessments not found...");
				return false;
			}
		}
		Log.logInfo("No Assessment(s) found in the Assessment List...");
		return false;
	}
	
	/**
	 * Clicks a row from the Assessments List by matching the Column's values using the Map
	 * Checks every row's column - value pairs based on the Map's key - value pairs. If every key - value pair matches then
	 * clicks that row. Returns null if no rows match.
	 * 
	 * @param columnValues Map<String, String> key-value pairs of the column name and its expected value which can be used to identify a row
	 * @return AssesmentDetails object, or null if no rows are clicked.
	 */
	public AssessmentDetails clickRow(Map<String, String> columnValues){
		WebElement row = getRow(columnValues);
		if(row != null){
			Log.logInfo("Clicking row...");
			UiHelper.click(row);
			return PageFactory.initElements(driver, AssessmentDetails.class);
		}
		Log.logInfo("Did not click any rows...");
		return null;
	}
	
	/**
	 * Returns a WebElement reference to the row from the Assessments List by matching the Column's values using the Map
	 * Checks every row's column - value pairs based on the Map's key - value pairs. If every key - value pair matches then
	 * clicks that row. Returns null if no rows match.
	 * 
	 * @param columnValues Map<String, String> key-value pairs of the column name and its expected value which can be used to identify a row
	 * @return WebElement object, or null if no rows match.
	 */
	public WebElement getRow(Map<String, String> columnValues){
		Log.logInfo("Getting Assessment from list with values : " + columnValues.toString());
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			boolean columnValuesMatched = true;
			for(String column : columnValues.keySet()){
				int columnIndex = getIndexOf(column);
				if(columnIndex < 0){
					Log.logInfo("Column called [" + column + "] was not found, indicating that there is a problem with the Test Configuration Parameters.");
					continue;
				}
				String currentRowsColumnValue = assessment.findElement(By.xpath("div[" + columnIndex + "]"))
						.getText();
				if(!currentRowsColumnValue.equals(columnValues.get(column))){
					columnValuesMatched = false;
					break;
				}
			}
			if(columnValuesMatched){
				Log.logStep("Found the row with the matching Assessment....");
				return assessment;
			}
			assessment = nextRow(assessment);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("Could not find any Assessment with these values.");
		return null;
	}
	
	/**
	 * @author ubiswas
	 * description: find attachment row based on subject, visit and form name
	 * @param subjectName
	 * @param visitName
	 * @param assessmentName
	 * @return
	 */
	public AssessmentDetails clickRow(String subjectName, String visitName, String assessmentName) {
		Log.logInfo("Trying to click an Assessment with Form Name : " + assessmentName + " and Visit : " + visitName
				+ " of Sub#: " + subjectName);
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			String currentRowsAssessmentName = assessment.findElement(By.xpath("div[" + getIndexOf("Assessment") + "]"))
					.getText();

			String currentRowsVisitName = assessment.findElement(By.xpath("div[" + getIndexOf("Visit") + "]"))
					.getText();

			String currentRowsSubjectName = assessment.findElement(By.xpath("div[" + getIndexOf("Subject") + "]"))
					.getText();

			if (currentRowsAssessmentName.equalsIgnoreCase(assessmentName)
					&& currentRowsVisitName.equalsIgnoreCase(visitName)
					&& currentRowsSubjectName.equalsIgnoreCase(subjectName)) {
				Log.logStep("Found the row with the matching Assessment, clicking it....");
				UiHelper.click(assessment);
				UiHelper.fastWait(driver);
				Log.logStep("Redirecting to Assessment Details page...");
				return PageFactory.initElements(driver, AssessmentDetails.class);
			}
			assessment = nextRow(assessment);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("Could not find any Assessment with these values.");
		return null;
	}
/**
 * @author ubiswas
 * Description: Return selected assessment row..
 * @param subjectName
 * @param assessmentName
 * @param visitName
 * @return
 */

	public WebElement getRowSelectedAssessment(String subjectName, String assessmentName, String visitName) {
		Log.logInfo("Trying to click an Assessment with Form Name : " + assessmentName + " and Visit : " + visitName);
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			String currentRowSubjectName = assessment.findElement(By.xpath("div[" + getIndexOf("Subject") + "]"))
					.getText();
			String currentRowsAssessmentName = assessment.findElement(By.xpath("div[" + getIndexOf("Assessment") + "]"))
					.getText();
			String currentRowsVisitName = assessment.findElement(By.xpath("div[" + getIndexOf("Visit") + "]")).getText();
			if (currentRowSubjectName.equalsIgnoreCase(subjectName) && currentRowsAssessmentName.equalsIgnoreCase(assessmentName) && currentRowsVisitName.equalsIgnoreCase(visitName)) {
				return assessment;
			}
			assessment = nextRow(assessment);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("No such Assessment found.");
		return null;
	}
	
}
