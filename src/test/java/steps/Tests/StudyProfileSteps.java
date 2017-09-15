package steps.Tests;

import mt.siteportal.pages.studyDashboard.StudyProfile;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.BrowserPDFViewerHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.support.PageFactory;

/**
 * Created by maksym.tkachov on 7/7/2016.
 */
public class StudyProfileSteps extends AbstractStep {

    public StudyProfileSteps (){
        toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
        studyProfile = PageFactory.initElements(Browser.getDriver(),StudyProfile.class);
    }
    public void openStudyProfile(){
        toolbarFull.openStudyProfile();
    }

    public void isOpenedStudyProfilePageVerification(){
        Log.logVerify("Study Profile is opened");
        Verify.True(UiHelper.isPresent(studyProfile.header), "Study Profile is not opened");
    }

    public void languageVerification(String languageToVerify){
        Log.logVerify("Correspond language "+languageToVerify+"is displayed");
        Verify.Equals(languageToVerify, studyProfile.getLanguage(),
                "Language dropDown doesn't contain default language");
    }

    public void isFirstVisitSelectedVerification(){
        Log.logVerify("First visit selected by default");
        String firstVisit = studyProfile.getFirstVisit();
        Verify.True(studyProfile.isSelectedVisit(firstVisit), "The First visit is not selected by default");
    }

    public void selectLanguageInDropdown(String language){
        studyProfile.selectLanguage(language);
    }

    public void redMessageContentVerification(String visit, String language, String messageToVerify){
        String countVisit = studyProfile.getVisitNameMapCount().get(visit);
        String countTemplate = studyProfile.getTemplateCount(visit, language);
        if (countTemplate.equals("0")) {
            Log.logVerify("Selected visit doesn't have any templates");
            Verify.Equals(messageToVerify, UiHelper.getText(studyProfile.noAssessmentDefinedMessage),
                    "The red info message is incorrect");
        } else if (!countVisit.equals(countTemplate)) {
            Log.logStep("Selected visit doesn't have all templates in one language");
            Verify.Equals(messageToVerify, UiHelper.getText(studyProfile.partialLoadedMessage),
                    "The red info message is incorrect");
        }
    }

    public void visitTemplateFormCountsCompareVerification(String visit, String language){
        String countVisit = studyProfile.getVisitNameMapCount().get(visit);
        String countTemplate = studyProfile.getTemplateCount(visit, language);
        String countForm = studyProfile.getFormCount();
        Log.logVerify("countVisit and countTemplate are equal");
        Verify.Equals(countVisit, countTemplate, "The counts in visit and template sections are not equal");
        Log.logVerify("countVisit and countForm are equal");
        Verify.Equals(countVisit, countForm, "The counts in visit and form sections are not equal");
    }

    public void openForm(int position){
        studyProfile.openForm(position);
    }
    public void openFormActionVerification(){
        int activeWindows = UiHelper.getActiveWindows(Browser.getDriver());
        Log.logVerify("new Browser Tab is opened");
        Verify.True(activeWindows == 2, "The form is not opened");
        BrowserPDFViewerHelper.close();
    }
}
