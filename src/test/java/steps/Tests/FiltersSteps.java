package steps.Tests;

import mt.siteportal.objects.Assessment;
import mt.siteportal.objects.Subject;
import mt.siteportal.objects.Visit;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.Filters;
import mt.siteportal.pages.studyDashboard.HeaderFilter;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by maksym.tkachov on 7/1/2016.
 */
public class FiltersSteps extends AbstractStep {

    public FiltersSteps(){
        headerFilter = PageFactory.initElements(Browser.getDriver(), HeaderFilter.class);
        assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
        visitTable = PageFactory.initElements(Browser.getDriver(),VisitTable.class);
        dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
    }

    public void resetFilter(){
        headerFilter.clearFilter();
    }

    public void filterByAssessmentsSubjectStatus(String status){
        assessmentsTable = (AssessmentsTable) headerFilter.filterBy(Constants.ASSESSMENTS,Constants.SUBJECTSTATUS , status);
    }

    public void filterByVisitsSubjectStatus(String status){
        visitTable = (VisitTable) headerFilter.filterBy(Constants.VISITS,Constants.SUBJECTSTATUS , status);
    }

    public void filterBySubjectsSubjectStatus(String status){
        subjectTable = (SubjectsTable) headerFilter.filterByFieldOnly(Constants.SUBJECTS,Constants.SUBJECTSTATUS , status);
    }

    public void openSubjectsByStatusFromDashboard(String status) {
        subjectTable = (SubjectsTable) dashboard.openFilter(status);
    }



    public void filtersByAssessmentSubjectStatusVerification(String statusToVerify, List<String> expectedStatuses){
        List <Assessment> table = assessmentsTable.getListOfAssessmentObjects();
        int numberOfAssessments = table.size();
        if (numberOfAssessments>0&&expectedStatuses.size()==1){
            for (int i =0; i<numberOfAssessments; i++){
                String actualStatus =table.get(i).getSubjectStatus();
                Log.logVerify(actualStatus+" is displayed on the page");
                Verify.True(actualStatus.equals(statusToVerify), "The subjects contain unnecessary status "+actualStatus);
            }
        }
        else if (numberOfAssessments>0&&expectedStatuses.size()>1){
            for (int i =0; i<numberOfAssessments; i++){
                String actualStatus =table.get(i).getSubjectStatus();
                Log.logVerify(actualStatus+" is displayed on the page");
                Verify.True(expectedStatuses.contains(actualStatus), "The subjects contain unnecessary status "+actualStatus);
            }
        }else {
            Verify.True(false, "There is not any assessments on the page");
        }
    }

    public void filterByVisitSubjectStatusVerification(String statusToVerify, List<String> expectedStatuses){
        List <Visit> table = visitTable.getListOfVisitObjects();
        int numberOfVisits = table.size();
        if (numberOfVisits>0&&expectedStatuses.size()==1){
            for (int i =0; i<numberOfVisits; i++){
                String actualStatus =table.get(i).getSubjectStatus();
                Log.logVerify(actualStatus+" is displayed on the page");
                Verify.True(actualStatus.equals(statusToVerify), "The subjects contain unnecessary status "+actualStatus);
            }
        }
        else if (numberOfVisits>0&&expectedStatuses.size()>1){
            for (int i =0; i<numberOfVisits; i++){
                String actualStatus =table.get(i).getSubjectStatus();
                Log.logVerify(actualStatus+" is displayed on the page");
                Verify.True(expectedStatuses.contains(actualStatus), "The subjects contain unnecessary status "+actualStatus);
            }
        }else {
            Verify.True(false, "There is not any visits on the page");
        }
    }

    public void filterBySubjectSubjectStatusVerification(String statusToVerify, List<String> expectedStatuses) {
        List<Subject> table = subjectTable.getListOfSubjectObjects();
        int numberOfSubjects = table.size();
        if (numberOfSubjects > 0 && expectedStatuses.size() == 1) {
            for (int i = 0; i < numberOfSubjects; i++) {
                String actualStatus = table.get(i).getStatus();
                Log.logVerify(actualStatus + " is displayed on the page");
                Verify.True(actualStatus.equals(statusToVerify), "The subjects contain unnecessary status " + actualStatus);
            }
        } else if (numberOfSubjects > 0 && expectedStatuses.size() > 1) {
            for (int i = 0; i < numberOfSubjects; i++) {
                String actualStatus = table.get(i).getStatus();
                Log.logVerify(actualStatus + " is displayed on the page");
                Verify.True(expectedStatuses.contains(actualStatus), "The subjects contain unnecessary status " + actualStatus);
            }
        } else {
            Verify.True(false, "There is not any subjects on the page");
        }
    }

        public void dashboardSubjectFiltersVerification(){
            Log.logVerify("Verification correct filters on dashboard");
            Map<String,String> filters = dashboard.nameCountHolder();
            for (int i = 0; i< Filters.subjectFilters().size(); i++){
                String filterName = Filters.subjectFilters().get(i);
                Verify.True(filters.containsKey(filterName), String.format("The dashboard doesn't have %s status", filterName));
            }
        }
    }

