package tests.Observer;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Created by maksym.tkachov on 1/27/2016.
 *
 * Test Objective(s):    
 * To add observers for subject. Check that if subject has only one active observer, he is automatically assigned to all ObsRO forms in visit
 *  Prerequisite(s):
 *  1. At least one subject exists with the folowing:
 *  1.1. Subject doesn't have any observers
 *  1.2. Subject has at least one visit with configured ObsRO forms
 *  2. At least one subject exists with the folowing:
 *  2.1. Subject already has at least one active observer
 *  2.2. Subject has at least one visit with configured ObsRO forms
 *
 * Steps
 * 1	Navigate to subject grid and select the subject identified in the prerequisite #1
 *      -Subject Details are displayed. Control to Add new Observer is available
 * 2	Click on Add new observer
 *      -Required fields are highlighted. Save (disabled) and Cancel options are available
 * 3	Fill in all required fields
 *      -Save and Cancel controls are enabled
 * 4	Click on Cancel
 *      -New observer record is not saved and not displayed.
 * 5	Click on Add new observer again. Fill all required fields and click on Save
 *      -Added observer is saved and displayed
 * 6	On visit list click on Add visit identified in prerequisite #1.2
 *      -ObsRO forms in visit are automatically assigned to observer that was added in step #5
 * 7	Navigate to subject grid and select the subject identified in the prerequisite #2
 *      -Subject Details are displayed. Existing observers are displayed. Control to Add new Observer is available
 * 8	Click on Add new observer
 *      -Required fields are highlighted.<br> Save (disabled) and Cancel options are available
 * 9	 Fill all required fields and click on Save
 *      -Added observer is saved and displayed in the list of observers
 * 10	On visit list click on Add visit identified in prerequisite #2.2
 *      -ObsRO forms in visit are not automatically assigned to observer that was added in step #9.
 */
@Test(groups = "Observer")
public class AddObserverTest extends AbstractObserver {
    
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        Log.logTestClassStart(this.getClass());
        beforeSteps = new BeforeSteps();
        //performing pre-condition actions with db
     /*
        dbSteps.deleteObserver(subjectName, relation, alias);
        dbSteps.deleteObserver(subjectName, relation + " up", alias + " up");
        dbSteps.deleteObserver(subjectName2, relation, alias);
        dbSteps.deleteObserver(subjectName2, relation + " up", alias + " up");
        dbSteps.deleteSubjectVisit(subjectName, visitName);
        dbSteps.deleteSubjectVisit(subjectName2, visitName);
    */
        Nav.toURL(sitePortalUrl);
        beforeSteps.loginAndOpenAllSubjects(siteportalUserAccountName, siteportalUserAccountPassword, study, site);
      
        steps.deleteObserver(subjectName, site, alias);       
        steps.deleteObserver(subjectName2, site, alias);
        steps.deleteSubjectVisit(subjectName, visitName);
        steps.deleteSubjectVisit(subjectName2, visitName);
    
    }

	@Test(groups = { "AddObserver",
			"SFB-TC-775" }, description = "Check subject with one active observer automatically assigned to all ObsRO forms in visit")
	public void addObserverTest() {
		
		steps.addButtonVerification(subjectName);
		steps.observerFieldsVerification();
		steps.fieldsFillingVerification(relation, alias);
		steps.cancelObserver();
		
		steps.observerCreationVerification(false, alias);
		steps.addObserver(relation, alias);
		steps.observerCreationVerification(true, alias);
	
		steps.selectVisit(visitName);
		steps.scheduleVisit();
		steps.observerAssignmentVerification(true, alias, relation);
		steps.returnToAllSubjects();
		
		steps.openSubjectDetails(subjectName2);
		subjectSteps.subjectDetailsVerification(subjectName2);
		steps.observerCreationVerification(true, relation1);
		steps.observerCreationVerification(true, alias1);
		steps.observerFieldsVerification();
		steps.cancelObserver();
		steps.observerCreationVerification(false, alias);
		steps.addObserver(relation, alias);
		steps.observerCreationVerification(true, alias);
		steps.selectVisit(visitName);
		steps.scheduleVisit();
		steps.observerAssignmentVerification(false, alias, relation);
		
	}
}
