package tests.Breadcrumbs;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;

/**
 * Helper Class containing functions ONLY USEFUL for the Breadcrumbs Test
 * Classes. Not available for other classes tests
 * 
 * @author Syed A. Zawad
 *
 */
public class BreadcrumbsHelper {
	
	private static SubjectDetails subjectDetails;
	private static AssessmentDetails assesment;
	private static VisitDetails visitDetails;

	/**
	 * Navigates to the Assessment Details Page by clicking on a Submitted Form
	 * from either a Subject Details or Visit Details Page. The context is taken
	 * as input, and then used to create the appropriate Page Object, which is
	 * in turn used to navigate to the Assessment Details Page by clicking on
	 * the Form From the Assessments Detail page, gets the appropriate value
	 * (either Subject or Visit) from the details grid and returns it
	 * 
	 * @param context
	 *            - either "Subjects" or "Visits"
	 * @return String value of either the Subject or Visit from the Details Grid
	 *         of the Assessment Details, depending on the context
	 */
	public static String navigateAndgetValueFromAssessmentDetails(String context) {
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
		if (context.equals("Subjects")) {
			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			int visits = subjectDetails.visitTable.getVisitCount();
			if (visits > 0) {
				for (int i = 0; i < visits; i++) {
					subjectDetails.visitTable.selectVisit(i);
					int forms = subjectDetails.getFormCount();
					if (forms > 0) {
						for (int j = 0; j < forms; j++) {
							assesment = visitDetails.clickSubmittedOrAssignedFormThumbnail(j);
							if (null != assesment)
								break;
						}
						return assesment.getAssessmentDetailsItemValue("Subject");
					}
				}
			}

		} else if (context.equals("Visits")) {
			int forms = visitDetails.getFormCount();
			if (forms > 0) {
				for (int i = 0; i < forms; i++) {
					assesment = visitDetails.clickSubmittedOrAssignedFormThumbnail(i);
					if (null != assesment)
						break;
				}
				return assesment.getAssessmentDetailsItemValue("Visit");
			}
		}
		Log.logStep("Could not get any details for Context : " + context);
		return null;
	}

	/**
	 * Gets the header as String from the appropriate Page as indicated by the
	 * context variable
	 * 
	 * @param context,
	 *            must be either Subjects or Visits
	 * @return String, the header
	 */
	public static String getHeaderFromAppropriatePage(String context) {
		if (context.equals("Subjects")) {
			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			return subjectDetails.getHeader();
		} else if (context.equals("Visits")) {
			visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
			return visitDetails.getHeader();
		}
		Log.logWarning("Could not get header for context : " + context);
		return null;
	}

	/**
	 * Generates an array of how the breadcrumbs should be like depending on the
	 * number of arguments
	 * 
	 * @return List<String> of texts that should be displayed depending on the
	 *         number of arguments
	 */
	public static List<String> generateExpectedBreadcrumbs(String studyName, String siteName, String... args) {
		List<String> answer = new ArrayList<String>();
		answer.add(studyName);
		if (!siteName.equalsIgnoreCase("All Sites"))
			answer.add(siteName);
		if (args.length >= 2)
			answer.add(args[0] + ": " + args[1]);
		if (args.length == 3)
			answer.add(args[0].substring(0, args[0].length() - 1) + " : " + args[2]);
		return answer;
	}

	/**
	 * Generates an array of how the breadcrumbs should be like when navigating
	 * through a Query click
	 * 
	 * @return List<String> of texts that should be displayed
	 */
	public static List<String> generateExpectedBreadcrumbsForQueries(String studyName, String siteName,
			String context) {
		List<String> answer = generateExpectedBreadcrumbs(studyName, siteName);
		if (context.equalsIgnoreCase("Score")) {
			answer.add("Assessments: All");
			return answer;
		}
		answer.add(context + "s: All");
		return answer;
	}

	/**
	 * Generates an array of how the breadcrumbs should be like when navigating
	 * through a Form click
	 * 
	 * @return List<String> of texts that should be displayed
	 */
	public static List<String> generateExpectedBreadcrumbsForForms(String studyName, String siteName, String context,
			String filter, String visitOrSubjectNameFromDetailsGrid) {
		List<String> answer = generateExpectedBreadcrumbs(studyName, siteName, context, filter);
		if(context.equalsIgnoreCase("Subjects")) {
			answer.add("Subject : " + visitOrSubjectNameFromDetailsGrid);
		} else {
			answer.add(visitOrSubjectNameFromDetailsGrid);
		}
		return answer;
	}
	
}
