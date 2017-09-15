package tests.SubjectVisitAttachment;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.SubjectSteps;
import steps.Tests.SubjectVisitAttachmentSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "SubjectVisitAttachment", "MultiEnvironment", "Sanity" })
public abstract class AbstractSubjectVisitAttachment extends AbstractTest {
	
	// ------------------------------------------------------------------------------------------------------------------------
	protected static final String study = "Auto - SubjectVisitAttachment";
	protected static final String site = "Site_1 - Francis Gibbes";
	protected static final String subjectName = "SubjectDetailsAttachmentTest";
	protected static final String visitName = "VisitDetailsAttachmentTest";
	protected static final int visitPos = 0;
	
	protected static final String[] fileName = { "MMSE_en-us_v2.0.pdf", "CDR_es-us_v2.0.pdf", "ECogPro_en-us_v1.0.pdf" };
	protected static final String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\";
	
	protected SubjectSteps subjectSteps = new SubjectSteps();
	protected SubjectVisitAttachmentSteps steps = new SubjectVisitAttachmentSteps();
	
	// ------------------------------------------------------------------------------------------------------------------------
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
	}
}