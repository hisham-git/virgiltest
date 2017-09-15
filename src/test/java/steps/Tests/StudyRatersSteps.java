package steps.Tests;


import mt.siteportal.details.RaterDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.objects.Rater;
import mt.siteportal.pages.Administration.Administration;
import mt.siteportal.pages.studyDashboard.StudyRaters;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.SortBy;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.support.PageFactory;

import java.util.*;

public class StudyRatersSteps extends AbstractStep{

    public StudyRatersSteps(){
        subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
        administration = PageFactory.initElements(Browser.getDriver(), Administration.class);
        studyRaters = PageFactory.initElements(Browser.getDriver(), StudyRaters.class);
    }

    public void ratersCountVerification(Integer count, String place) throws Exception {
        switch (place) {
            case "button" :
                String ratersCount = toolbarFull.getRatersCount();
                Log.logVerify("Verify that raters count(button) > 0");
                Verify.Equals(count.toString(), ratersCount, "The raters count is different");
                break;
            case "section":
                Log.logVerify("Verify that raters count(raters section) > 0");
                Verify.Equals(count.toString(), studyRaters.getRatersCount(), "The raters count is different");
                break;
            case "table":
                Log.logVerify("Verify that raters count(in the table > 0");
                Verify.Equals(count, studyRaters.getListOfRaterObjects().size(), "The raters count is different");
                break;
            default:
                throw new Exception("There is no such place like " + place);
        }
    }

    public void openRaters(){
        toolbarFull.openRaters();
    }

    public void sortingVerification(SortBy sortBy) throws Exception {
        switch (sortBy) {
            case SCHEDULED:
                Log.logStep("Getting ScheduledCount before Sorting");
                List<Integer> countsBefore = new ArrayList<Integer>();
                for (Rater rater: studyRaters.getListOfRaterObjects()){
                    countsBefore.add(Integer.parseInt(rater.getScheduledCount()));
                }
                Log.logStep("Sorting ScheduledCount before Sorting");
                Collections.sort(countsBefore);

                Log.logStep("Sorting raters by ScheduledCount on portal");
                studyRaters.sortBy(SortBy.SCHEDULED);
                Log.logStep("Getting ScheduledCount after Sorting");
                List<Integer> countsAfter = TextHelper.convertToInt(studyRaters.getDataAfterSotring(SortBy.SCHEDULED));
                Log.logStep("Verification that Sorting(Ascending) works correctly");
                Verify.Equals(countsBefore, countsAfter, "The Sorting(Ascending) works incorrectly");

                Log.logStep("Sorting raters by ScheduledCount Descending");
                studyRaters.sortBy(SortBy.SCHEDULED);
                Log.logStep("Getting ScheduledCount after Sorting");
                countsAfter = TextHelper.convertToInt(studyRaters.getDataAfterSotring(SortBy.SCHEDULED));
                Log.logStep("Verification that Sorting(Descending) works correctly");
                Collections.reverse(countsBefore);
                Verify.Equals(countsBefore, countsAfter, "The Sorting(Descending) works incorrectly");
                break;
            case COMPLETED:
                Log.logStep("Getting CompletedCount before Sorting");
                List<Integer> countsBeforeCompleted = new ArrayList<Integer>();
                for (Rater rater: studyRaters.getListOfRaterObjects()){
                    countsBeforeCompleted.add(Integer.parseInt(rater.getCompletedCount()));
                }
                Log.logStep("Sorting ScheduledCount before Sorting");
                Collections.sort(countsBeforeCompleted);

                Log.logStep("Sorting raters by CompletedCount on portal");
                studyRaters.sortBy(SortBy.COMPLETED);
                Log.logStep("Getting CompletedCount after Sorting");
                List<Integer> countsAfterCompleted =
                        TextHelper.convertToInt(studyRaters.getDataAfterSotring(SortBy.COMPLETED));
                Log.logStep("Verification that Sorting(Ascending) works correctly");
                Verify.Equals(countsBeforeCompleted, countsAfterCompleted, "The Sorting(Ascending) works incorrectly");

                Log.logStep("Sorting raters by CompletedCount Descending");
                studyRaters.sortBy(SortBy.COMPLETED);
                Log.logStep("Getting CompletedCount after Sorting");
                countsAfterCompleted = TextHelper.convertToInt(studyRaters.getDataAfterSotring(SortBy.COMPLETED));
                Log.logStep("Verification that Sorting(Descending) works correctly");
                Collections.reverse(countsBeforeCompleted);
                Verify.Equals(countsBeforeCompleted, countsAfterCompleted, "The Sorting(Descending) works incorrectly");
                break;
            case EDITED:
                Log.logStep("Getting EditedCount before Sorting");
                List<Integer> countsBeforeEdited = new ArrayList<Integer>();
                for (Rater rater: studyRaters.getListOfRaterObjects()){
                    countsBeforeEdited.add(Integer.parseInt(rater.getEditedCount()));
                }
                Log.logStep("Sorting EditedCount before Sorting");
                Collections.sort(countsBeforeEdited);

                Log.logStep("Sorting raters by EditedCount on portal");
                studyRaters.sortBy(SortBy.EDITED);
                Log.logStep("Getting EditedCount after Sorting");
                List<Integer> countsAfterEdited =
                        TextHelper.convertToInt(studyRaters.getDataAfterSotring(SortBy.EDITED));
                Log.logStep("Verification that Sorting(Ascending) works correctly");
                Verify.Equals(countsBeforeEdited, countsAfterEdited, "The Sorting(Ascending) works incorrectly");

                Log.logStep("Sorting raters by EditedCount Descending");
                studyRaters.sortBy(SortBy.EDITED);
                Log.logStep("Getting EditedCount after Sorting");
                countsAfterEdited = TextHelper.convertToInt(studyRaters.getDataAfterSotring(SortBy.EDITED));
                Log.logStep("Verification that Sorting(Descending) works correctly");
                Collections.reverse(countsBeforeEdited);
                Verify.Equals(countsBeforeEdited, countsAfterEdited, "The Sorting(Descending) works incorrectly");
                break;
            case RATER:
                Log.logStep("Getting Name before Sorting");
                List<String> countsBeforeRater = new ArrayList<String>();
                for (Rater rater: studyRaters.getListOfRaterObjects()){
                    countsBeforeRater.add(rater.getRaterName());
                }
                Log.logStep("Sorting Name before Sorting");
                Collections.sort(countsBeforeRater);

                Log.logStep("Sorting raters by Name on portal");
                studyRaters.sortBy(SortBy.RATER);
                Log.logStep("Getting Name after Sorting");
                List<String> countsAfterRater = studyRaters.getDataAfterSotring(SortBy.RATER);
                Log.logStep("Verification that Sorting(Ascending) works correctly");
                Verify.Equals(countsBeforeRater, countsAfterRater, "The Sorting(Ascending) works incorrectly");

                Log.logStep("Sorting raters by name Descending");
                studyRaters.sortBy(SortBy.RATER);
                Log.logStep("Getting name after Sorting");
                countsAfterRater = studyRaters.getDataAfterSotring(SortBy.RATER);
                Log.logStep("Verification that Sorting(Descending) works correctly");
                Collections.reverse(countsBeforeRater);
                Verify.Equals(countsBeforeRater, countsAfterRater, "The Sorting(Descending) works incorrectly");
                break;
            default:
                throw new Exception("There is no such sorting method like " + sortBy);
        }
    }

    public RaterDetails openRaterDetails(String name){
        Log.logStep("Opening the rater " + name);
        return raterDetails =  studyRaters.openRaterDetails(name);
    }

	public void raterDetailsVerification(String name, Map<String, String> personDetails) {
		raterDetails = openRaterDetails(name);
		String languages = personDetails.get("Language");
		String raterName = raterDetails.getDetails().get("Name");
		String personName = personDetails.get("Name");

		Log.logStep("Verifying rater details verification");
		// Verify.Equals(raterDetails.getDetails().get("Name"),
		// personDetails.get("Name") + " " +
		// personDetails.get("Degrees"), "The names are different");

		Verify.True(raterName.startsWith(personName.substring(0, personName.lastIndexOf(" "))),
				"The names are different"); // @author: Hisham
		Log.logVerify("Languages of rater are the same as on admin page");
		List<String> lst = raterDetails.getLanguages();
		for (String language : lst) {
			Verify.True(languages.contains(language), "The list of languages are not the same");
		}
	}

    public Map<String, String> getPersonDetails(){
        Map<String, String> personDetails = administration.peoplePage.getGeneralPeopleTab().getDetails();
        return personDetails;
    }
}
