package steps.Abstract;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;

import ah.siteportal.Administration.studies.Custom;
import ah.siteportal.Administration.studies.Sites;
import ah.siteportal.Administration.studies.Studies;
import hu.siteportal.pages.CentralRating.CrAppointmentDetails;
import hu.siteportal.pdfreport.PdfLog;
import hu.siteportal.pdfreport.StepVerify;
import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.RaterDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.pages.About;
import mt.siteportal.pages.CrVisitList;
import mt.siteportal.pages.ErrorContainer;
import mt.siteportal.pages.Footer;
import mt.siteportal.pages.Header;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.pages.Administration.Administration;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.EmptyDashboard;
import mt.siteportal.pages.studyDashboard.EsignDialog;
import mt.siteportal.pages.studyDashboard.HeaderFilter;
import mt.siteportal.pages.studyDashboard.NewEditSubject;
import mt.siteportal.pages.studyDashboard.StudyProfile;
import mt.siteportal.pages.studyDashboard.StudyRaters;
import mt.siteportal.pages.studyDashboard.Templates;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;

/**
 * Created by maksym.tkachov on 6/23/2016.
 */
public abstract class AbstractStep {
    protected LoginPage loginPage;
    protected HomePage homePage;
    protected Header header;
    protected HeaderFilter headerFilter;
    protected Footer footer;
    protected EmptyDashboard emptyDashboard;
    protected Dashboard dashboard;
    protected DashboardList dashboardList;
    protected Administration administration;
    protected About about;
    protected ToolBarFull toolbarFull;
    protected StudyProfile studyProfile;
    protected StudyRaters studyRaters;
    protected RaterDetails raterDetails;
    protected ITestResult result;
    protected SubjectsTable subjectTable;
    protected NewEditSubject newSubject;
    protected SubjectList subjectList;
    protected SubjectDetails subjectDetails;
    protected Templates templates;
    protected AssessmentDetails assessmentDetails;
    protected EsignDialog esignDialog;
    protected VisitTable visitTable;
    protected VisitDetails visitDetails;
    protected AssessmentsTable assessmentsTable;
    protected AssessmentList assessmentList;
    protected ErrorContainer error;
    protected QueriesSidePanel qSidePanel;
    protected CrVisitList crVisitList;
    protected CrAppointmentDetails crAppDetails;
    protected Custom custom;
    protected Sites sites;
    protected Studies studies;

    public void chooseStudy(String name){
        toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
        toolbarFull.chooseStudy(name);
    }
    
    public void chooseAllSites(){
    	dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
    	dashboard.selectAllSites();
    }

    public void openDashboard(){
        homePage = PageFactory.initElements(Browser.getDriver(),
                HomePage.class);
        homePage.openDashboard();
    }

    public void selectDuplicatedVisit(String visitName, int position){
        subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        subjectDetails.visitTable.selectVisit(visitName,position);
    }

    public void selectVisit(String visitName){
        selectVisitWithPosition(visitName,0);
    }

    public void scheduleVisit(){
        subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        subjectDetails.visitTable.scheduleVisit();
    }
    
	public void viewReScheduleAppointment() {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.viewReScheduleAppointment();
	}

	public void returnToSubjectDetails(String subjectName) {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentDetails.returnToSubject(subjectName);
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
	}

	public void returnToDashboard(String study) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		if (false == dashboard.isDashboardOpened()) {
			toolbarFull.returnToDashboard(study);
		}
	}

	public void returnToDashboard() {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		if (false == dashboard.isDashboardOpened()) {
			toolbarFull.returnToDashboard();
		}
	}
    
    public void openAllVisits(){
        dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
        dashboard.openAllVisits();
    }
    
	public void openVisitDetails(String subjectName, String visitName) {
		visitTable = PageFactory.initElements(Browser.getDriver(), VisitTable.class);
		visitTable.openVisitDetails(subjectName, visitName);
	}

    public void openAllAssessments(){
        dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
        dashboard.openAllAssesments();
    }

	public void openAssessmentDetails(String subjectName, String visitName) {
		assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
		assessmentsTable.openAssessmentDetails(subjectName, visitName);
	}

    public void openAllSubjects(){
        dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
        dashboard.openAllSubjects();
    }

	public void openSubjectDetails(String subjectName) {
		subjectTable = PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
		subjectDetails = subjectTable.openSubjectDetails(subjectName);
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
	}

    public void returnToAllSubjects(){
        toolbarFull= PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
        subjectTable = toolbarFull.returnToAllSubjects();
    }
    
    public void selectVisitInPosition(String visitName, int position) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visitName, position);
	}

    public void unScheduleVisit(){
        subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        subjectDetails.visitTable.unscheduleVisit();
    }
    
	public void unScheduleVisitInPosition(String visitName, int position) {
		selectVisitInPosition(visitName, position);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.unscheduleVisit();
	}

    //region administration

    public void openAdministration(){
        homePage = PageFactory.initElements(Browser.getDriver(),
                HomePage.class);
        administration = homePage.openAdministration();
    }

    public void selectPerson(String personName){
        administration.openPeople();
        administration.peoplePage.selectPerson(personName);
    }

    public String getPersonDetails(String detail){
        Map<String, String> personDetails = new HashMap<String,String>();
        personDetails = administration.peoplePage.getGeneralPeopleTab().getDetails();
        return personDetails.get(detail);
    }

    //endregion

    public void openHomePage(){
        header = PageFactory.initElements(Browser.getDriver(),
                Header.class);
        header.openHomePage();
    }

    public void chooseSite(String siteName){
        toolbarFull = PageFactory.initElements(Browser.getDriver(),
                ToolBarFull.class);
        toolbarFull.chooseSite(siteName);
    }

    public void elementsOnDashboardVerification(){
        Verify.True(UiHelper.isPresent(dashboard.assessments), "Assessments are not displayed on dashboard");
        Verify.True(UiHelper.isPresent(dashboard.subjects), "Subjects are not displayed on dashboard");
        Verify.True(UiHelper.isPresent(dashboard.visits), "Visits are not displayed on dashboard");
    }

    public void selectVisitWithPosition(String visitName, int position){
        selectDuplicatedVisit(visitName, position);
    }
    
    public void selectVisitWithPosition(int position){
    	subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        subjectDetails.visitTable.selectVisit(position);
    }
    
	public void subjectDetailsVerification(String subjectNumber) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		Log.logVerify("Verifying details page is opened for Subject: [" + subjectNumber + "]");
		Verify.True(subjectDetails.detailsIsOpened(subjectNumber), "Details page found not correct");
	}
	
	public void visitDetailsVerification(String visitName) {
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
		Log.logVerify("Verifying details page is opened for Visit: [" + visitName + "]");
		Verify.True(visitDetails.detailsIsOpened(visitName), "Details page found not correct");
	}
	
	public void AssessmentDetailsVerification(String AssessNumber) {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logVerify("Verifying details page is opened for Assessment: [" + AssessNumber + "]");
		Verify.True(assessmentDetails.isOpened(), "Details page found not correct");
	}
}
