package steps.Tests;


import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.studyDashboard.Templates;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.ObserverButtons;
import mt.siteportal.utils.helpers.Required;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.support.PageFactory;

public class ObserverSteps extends AbstractStep{

    public ObserverSteps(){
        subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        templates = PageFactory.initElements(Browser.getDriver(),
                Templates.class);
        toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
    }

    public void addButtonVerification(String subjectName){
        openSubjectDetails(subjectName);
        Log.logVerify("Verifying that 'Add observer' button is displayed");
        Verify.True(UiHelper.isPresent(subjectDetails.observersTable.addObserverButton),
                "The 'add observer' button is not displayed");
    }

    public void observerFieldsVerification(){
        //clicking on 'add observer' button
        UiHelper.click(subjectDetails.observersTable.addObserverButton);
        //main verification
        Log.logVerify("the fields are required");
        Verify.True(Required.isInputRequired(subjectDetails.observersTable.newObserverRelationInput),
                "The relation is not required");
        Verify.True(Required.isInputRequired(subjectDetails.observersTable.newObserverAliasInput),
                "The alias is not required");
        Log.logVerify("the save button is disabled");
        Verify.True(subjectDetails.observersTable.isButtonDisabled("Save"),"The saveButton should be disabled");
        Log.logVerify("the cancel button is enabled");
        Verify.False(subjectDetails.observersTable.isButtonDisabled("Cancel"),"The cancelButton should be enabled");
    }

    public void fieldsFillingVerification(String relation, String allias){
        subjectDetails.observersTable.fillRequiredFields(relation, allias);
        Log.logVerify("the save button is enabled");
        Verify.False(subjectDetails.observersTable.isButtonDisabled("Save"),"The saveButton should be enabled");
        Log.logVerify("the cancel button is enabled");
        Verify.False(subjectDetails.observersTable.isButtonDisabled("Cancel"),"The cancelButton should be enabled");
    }

    public void observerCreationVerification(Boolean isCreated, String alias){
        if(isCreated){
            Log.logVerify("Observer is created");
            Verify.True(subjectDetails.observersTable.isObserverInTable(alias),"the observer is not created");
        } else {
            Log.logVerify("Observer is not created");
            Verify.False(subjectDetails.observersTable.isObserverInTable(alias),"the observer is in table, but should not");
        }
    }

    public void addObserver(String relation, String alias){
        subjectDetails.observersTable.addObserver(relation,alias);
    }

    public void cancelObserver(){
        subjectDetails.observersTable.cancelObserver();
    }

    public void observerAssignmentVerification(Boolean isAssigned, String alias, String relation){
        if (isAssigned) {
            Log.logVerify("Observer is automatically assigned to the visit");
            Verify.Equals(alias + " (" + relation + ")", templates.getNameOfAssignee(), "The name is not correct");
            templates.unAssignTemplate(1);
        } else {
            Log.logVerify("Observer is not automatically assigned to the visit");
            Verify.Equals("Not Assigned", templates.getNameOfAssignee(),
                    "Observer is automatically assigned but should not");
        }
    }

    public void selectObserver(String aliasAssigned){
        subjectDetails.observersTable.selectObserver(aliasAssigned);
    }

    public void observerButtonPresenceVerification(Boolean isDisplayed, ObserverButtons button) throws Exception {
        switch (button) {
            case DELETE:
                if(isDisplayed){
                    Log.logVerify("Delete button is displayed");
                    Verify.True(UiHelper.isPresent(subjectDetails.observersTable.deleteButton),"The delete button should be displayed");
                } else {
                    Log.logVerify("Delete button is hidden");
                    Verify.False(UiHelper.isPresent(subjectDetails.observersTable.deleteButton), "The Delete button is displayed");
                }
                break;
            case REMOVE:
                if(isDisplayed) {
                    Log.logVerify("Remove button is displayed");
                    Verify.True(UiHelper.isPresent(subjectDetails.observersTable.removeButton),
                            "The Remove button is not displayed");
                } else {
                    Log.logVerify("Remove button is hidden");
                    Verify.False(UiHelper.isPresent(subjectDetails.observersTable.removeButton),
                            "The Remove button is displayed");
                }
                break;
            case EDIT:
                if(isDisplayed) {
                    Log.logVerify("Edit button is displayed");
                    Verify.True(UiHelper.isPresent(subjectDetails.observersTable.editButton),
                            "The Edit button is not displayed");
                } else {
                    Log.logVerify("Edit button is hidden");
                    Verify.False(UiHelper.isPresent(subjectDetails.observersTable.editButton),
                            "The Edit button is displayed");
                }
                break;
            case CANCEL:
                if(isDisplayed) {
                    Log.logVerify("Cancel button is displayed");
                    Verify.True(UiHelper.isPresent(subjectDetails.observersTable.cancelButton),
                            "The Cancel button is not displayed");
                } else {
                    Log.logVerify("Cancel button is hidden");
                    Verify.False(UiHelper.isPresent(subjectDetails.observersTable.cancelButton),
                            "The Cancel button is displayed");
                }
                break;
            case SAVE:
                if(isDisplayed) {
                    Log.logVerify("Save button is displayed");
                    Verify.True(UiHelper.isPresent(subjectDetails.observersTable.saveButton),
                            "The Cancel button is not displayed");
                } else {
                    Log.logVerify("Save button is hidden");
                    Verify.False(UiHelper.isPresent(subjectDetails.observersTable.saveButton),
                            "The Save button is displayed");
                }
                break;
            default:
                throw new Exception("There is no such button as " + button);
        }

    }

    public void clickNeededObserverButton(ObserverButtons button) throws Exception {
        switch (button) {
            case DELETE:
                UiHelper.click(subjectDetails.observersTable.deleteButton);
                break;
            case REMOVE:
                UiHelper.click(subjectDetails.observersTable.removeButton);
                break;
            case EDIT:
                UiHelper.click(subjectDetails.observersTable.editButton);
                break;
            case CANCEL:
                UiHelper.click(subjectDetails.observersTable.cancelButton);
                break;
            default:
                throw new Exception("There is no such button as " + button);
        }
    }


    public void deleteObserver(String alias){
        subjectDetails.observersTable.deleteObserver(alias);
    }

    public void editRequiredFields(String relation, String alias){
        subjectDetails.observersTable.editRequiredFields(relation, alias);
    }

    public void editObserver(String oldAliasOrRelation, String relation, String alias){
        subjectDetails.observersTable.editObserver(oldAliasOrRelation,relation, alias);
    }
/**
 * @author ubiswas
 * Description: Creating two new subjects (with/without observer), deleting observer for existing subjects
 * @param subjectName
 * 			- WithObservers, WithoutObservers
 * @param site
 * @param alias
 */
	public void deleteObserver(String subjectName, String site, String alias) {
		SubjectsTable subjectTable = new SubjectsTable(Browser.getDriver());
		SubjectSteps subjectStep = new SubjectSteps();
		ObserverSteps observerStep = new ObserverSteps();

		if (subjectTable.isSubjectPresent(subjectName)) {
			openSubjectDetails(subjectName);
			if(subjectDetails.observersTable.isObserverInTable(alias)){
				subjectDetails.observersTable.deleteObserver(alias);
			}else{
				Log.logVerify("Selected Alias is not present at this sbuject");
			}
			
			subjectStep.returnToAllSubjects();

		} else {
			Log.logVerify("Subject is not present on the list");
			subjectStep.addSubjectToSite(subjectName, site);
			if (subjectName.equalsIgnoreCase("WithObservers")) {
				observerStep.addObserver("Existing", "Observer");
			}
			subjectStep.returnToAllSubjects();
		}
	}
	

	/**
	 * Description: Unscheduled visit for subjects.
	 * @param subjectName
	 * @param visitName
	 */
	public void deleteSubjectVisit(String subjectName, String visitName) {
		SubjectSteps subjectStep = new SubjectSteps();

		openSubjectDetails(subjectName);
		subjectStep.unScheduleVisit();
		subjectStep.returnToAllSubjects();

	}
}
