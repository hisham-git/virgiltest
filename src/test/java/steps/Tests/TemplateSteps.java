package steps.Tests;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.studyDashboard.Templates;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import hu.siteportal.pdfreport.PdfLog;
import hu.siteportal.pdfreport.StepVerify;

/**
 * Created by maksym.tkachov on 6/29/2016.
 */
public class TemplateSteps extends AbstractStep {

	public TemplateSteps() {
		templates = PageFactory.initElements(Browser.getDriver(), Templates.class);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}

	public void assignFirstTemplateTo(String raterName) {
		assignTemplateTo(1, raterName);
	}

	public void assignTemplateTo(int position, String raterName) {
		templates.assignTemplateTo(position, raterName);
	}

	public void unAssignTempalte() {
		templates.unAssignFirstTemplate();
	}

	public void makeTemplateAsNotCompleted() {
		templates.asNotCompleted(1);
	}

	public boolean notAssignedOptionVerification() {
		Log.logVerify("Verifying dropdown with not assigned option");
		String actualValue = templates.getNameOfAssignee();
		return Verify.True(actualValue.equalsIgnoreCase("Not Assigned"), "The assessment is assigned");
	}
	
	public boolean notAssignedOptionVerification(boolean shouldBeNotAssigned) {
		Log.logVerify("Verifying dropdown with not assigned option");
		String actualValue = templates.getNameOfAssignee();
		if (shouldBeNotAssigned) {
			return Verify.True(actualValue.equalsIgnoreCase("Not Assigned"), "The assessment is assigned");
		}
		return Verify.False(actualValue.equalsIgnoreCase("Not Assigned"), "The assessment is not assigned");
	}

	public void assignedToRaterVerification(String raterName) {
		Log.logVerify("Assessment assigned to " + raterName);
		Verify.Equals(raterName, templates.getNameOfAssignee(), "The assessment is not assigned to " + raterName);
	}

	public void templateStatusVerification(String status) {
		Log.logVerify("The Template status");
		Verify.Equals(status, templates.getTemplateStatus(), "the status of assessment is incorrect");
	}

	public void assignTemplateTo(String aliasAssigned, String relationAssigned) {
		templates.assignTemplateTo(1, aliasAssigned + " (" + relationAssigned + ")");
	}

	public void unAssignTemplate(int position) {
		templates.unAssignTemplate(position);
	}

	public void assignTemplateToMe(String visitName, int position, boolean isScheduled, Boolean isOpen) {
		templates = PageFactory.initElements(Browser.getDriver(), Templates.class);
		if (!isScheduled) {
			Log.logStep("Scheduling the " + visitName);
			subjectDetails.visitTable.scheduleVisit();
		}
		assignTemplateTo(position, "Me");
		if (isOpen) {
			templates.openTemplate();
		}
	}
	
	public void assignTemplateToMeAndVerify(String visitName, int position, boolean isScheduled, Boolean isOpen) {
		PdfLog.setStep("Assigning visit: [" + visitName + "] template to Me");
		templates = PageFactory.initElements(Browser.getDriver(), Templates.class);
		if (!isScheduled) {
			Log.logStep("Scheduling the " + visitName);
			subjectDetails.visitTable.scheduleVisit();
		}
		assignTemplateTo(position, "Me");
		if (isOpen) {
			templates.openTemplate();
		}
		String assignedValue = templates.getNameOfAssignee();
		StepVerify.True(assignedValue.equalsIgnoreCase("Me"), "Visit: [" + visitName + "] template assigned to 'Me'",
				"Visit: [" + visitName + "] template found not assigned to 'Me'");
	}

	public void templateCountVerification(int countExpected) {
		Log.logVerify("Count of templates");
		int countActual = Integer.valueOf(templates.getTemplateCount());
		Verify.Equals(countExpected, countActual,
				"Template count is incorrect. Expected: [" + countExpected + "] but Found [" + countActual + "]");
	}

	/**
	 * Author: Uttam Description: Unschedule a Subject visit
	 * 
	 * @param subjectName
	 * @param visitName
	 */

	public void deleteSubjectVisit(String subjectName, String visitName) {
		SubjectSteps subjectSteps = new SubjectSteps();
//		subjectStep.selectVisit(visitName);
		subjectSteps.selectVisitByName("Scheduled", visitName);
		if (null != subjectDetails.visitTable.getRemoveVisitButton())
			subjectSteps.unScheduleVisit();
		else {
			Log.logWarning("Couldn't get delete button for visit: [" + visitName + "]!");
		}
	}

	/*
	 * public String getCurrentDateTime() { // TODO Auto-generated method stub
	 * return new
	 * SimpleDateFormat("yyyyMMddss").format(Calendar.getInstance().getTime());
	 * }
	 */
}
