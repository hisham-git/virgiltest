package tests.Observer;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Enums.ObserverButtons;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.TemplateSteps;

/**
 * Created by maksym.tkachov on 2/8/2016.
 * Test Objective(s):
 * To check possibility to delete observer
 *
 * Prerequisite(s):
 * 1. At least one subject with at least two observers exists
 * 2. At least one observer is assigned to at least one assessment
 * 3. At least one observer is not assigned to any assessment
 * Steps
 * 1: Navigate to Subject Details of the subject identified in prerequisite #1 and select observer identified in prerequisites #2
 *      -Observer general information is displayed. Edit control are enabled. Delete control is hidden
 * 2: Select observer identified in prerequisites #3.
 *      -Observer general information is displayed. Edit and Delete icons are enabled
 * 3: Click on Delete
 *      -Delete control replaced with Remove button. Cancel control is available
 * 4: Click on Cancel
 *      -Observer is not deleted and displayed in the list
 * 5: Select observer identified in prerequisites #3 again and click on Delete.
 *      -Delete control replaced with Remove button. Cancel control is available
 * 6: Click on Remove.
 *      -Observer is deleted. The list of observers is updated. The deleted observer is not displayed on observers list
 */

@Test(groups = "Observer")
public class DeleteObserverTest extends AbstractObserver {
    private String subjectName = "WithObservers";
    private String subjectName2 = "WithoutObservers";
    private String relationAssigned = "Existing";
    private String aliasAssigned = "Observer";
    TemplateSteps templateSteps = new TemplateSteps();


    @BeforeClass(alwaysRun = true)
    public void beforeClass(){
        Log.logTestClassStart(this.getClass());
        beforeSteps = new BeforeSteps();
        Nav.toURL(sitePortalUrl);
        beforeSteps.loginAndOpenSubject(siteportalUserAccountName, siteportalUserAccountPassword, study, site, subjectName);
        steps.selectVisit(visitName);
        templateSteps.assignTemplateTo(aliasAssigned, relationAssigned);
    }

	@Test(groups = { "DeleteObserver", "SFB-TC-778" }, dependsOnGroups = {
			"EditObserver" }, description = "Check delete observer with assigned/unassigned assessments")
	public void deleteObserverTest() throws Exception {
		steps.selectObserver(aliasAssigned);
		steps.observerButtonPresenceVerification(true, ObserverButtons.EDIT);
		steps.observerButtonPresenceVerification(false, ObserverButtons.DELETE);

		steps.selectObserver(alias);
		steps.observerButtonPresenceVerification(true, ObserverButtons.EDIT);
		steps.observerButtonPresenceVerification(true, ObserverButtons.DELETE);

		steps.clickNeededObserverButton(ObserverButtons.DELETE);
		steps.observerButtonPresenceVerification(true, ObserverButtons.REMOVE);
		steps.observerButtonPresenceVerification(true, ObserverButtons.CANCEL);

		steps.clickNeededObserverButton(ObserverButtons.CANCEL);
		steps.observerCreationVerification(true, alias);

		steps.deleteObserver(alias);
		steps.observerCreationVerification(false, alias);
		steps.returnToAllSubjects();
		steps.openSubjectDetails(subjectName2);
		steps.deleteObserver(alias + " up");
//		steps.selectVisit(visitName);
//		steps.unScheduleVisit();
		steps.returnToAllSubjects();
		steps.openSubjectDetails(subjectName);
		steps.selectVisit(visitName);
		templateSteps.unAssignTemplate(1);
//		steps.unScheduleVisit();	
	}
}
