package steps.Tests;

import org.openqa.selenium.support.PageFactory;

import hu.siteportal.pages.CentralRating.CrAppointmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.CrVisitList;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import steps.Abstract.AbstractStep;

public class PrimaryAddressSteps extends AbstractStep{
	ErrorSteps errorSteps;
	public PrimaryAddressSteps() {
		crVisitList = PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
		crAppDetails = PageFactory.initElements(Browser.getDriver(), CrAppointmentDetails.class);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);

		errorSteps = new ErrorSteps();
	}

}
