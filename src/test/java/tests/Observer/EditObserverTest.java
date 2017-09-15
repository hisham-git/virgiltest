package tests.Observer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Enums.ObserverButtons;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Created by maksym.tkachov on 2/8/2016.
 *
 * Test Objective(s):  To edit data for observer and save changes
 * Prerequisite(s):  At least one subject having at least one associated observer exists for a Study Site.
 *
 * Steps:
 * 1: Select observer defined in prerequisites on Subject details
 *      -Observer general information is displayed. Edit and Delete icons are enabled
 * 2: Click on Edit.
 *      -Observer attributes are editable. Options to "Save" and "Cancel" are displayed.
 * 3: Make changes in Relation or Alias  for selected observer and click on Cancel.
 *      -Changes are not saved
 * 4: Activate edit-mode again, make changes and click on Save.
 *      -Changes are saved
 */
@Test(groups = "Observer")
public class EditObserverTest extends AbstractObserver {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(){
        Log.logTestClassStart(this.getClass());
        beforeSteps = new BeforeSteps();
        Nav.toURL(sitePortalUrl);
        beforeSteps.loginAndOpenSubject(siteportalUserAccountName, siteportalUserAccountPassword, study, site, subjectName);
    }

	@Test(groups = { "EditObserver", "SFB-TC-777" }, dependsOnGroups = {
			"AddObserver" }, description = "Check editing data for observer and save")
	public void editObserverTest() throws Exception {
		steps.observerButtonPresenceVerification(false, ObserverButtons.EDIT);
		steps.observerButtonPresenceVerification(false, ObserverButtons.DELETE);
		steps.selectObserver(relation);
		steps.observerButtonPresenceVerification(true, ObserverButtons.EDIT);
		steps.observerButtonPresenceVerification(true, ObserverButtons.DELETE);

		steps.observerButtonPresenceVerification(false, ObserverButtons.CANCEL);
		steps.observerButtonPresenceVerification(false, ObserverButtons.SAVE);
		steps.clickNeededObserverButton(ObserverButtons.EDIT);
		steps.observerButtonPresenceVerification(true, ObserverButtons.CANCEL);
		steps.observerButtonPresenceVerification(true, ObserverButtons.SAVE);

		steps.editRequiredFields(relation + " up", alias + " up");
		steps.cancelObserver();
		steps.observerCreationVerification(false, relation + " up");

		steps.editObserver(relation, relation + " up", alias + " up");
		steps.observerCreationVerification(true, relation + " up");
	}
}
