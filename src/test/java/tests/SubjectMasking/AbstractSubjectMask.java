package tests.SubjectMasking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import ah.siteportal.Administration.studies.Custom;
import hu.siteportal.pdfreport.PdfLog;
import mt.siteportal.utils.Browser;
import steps.Configuration.CommonSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectMaskingSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "SubjectMask", "MultiEnvironment", "Sanity" })
public abstract class AbstractSubjectMask extends AbstractTest {
	
	// ------------------------------------------------------------------------------------------------------------------------
	protected final static String study = "Test Sponsor - SubjectMasking";

	protected final static DateFormat subjectFormat = new SimpleDateFormat("MMddyy");
	protected final static String formattedDate = subjectFormat.format(new Date());
	protected final static String suffix = "-" + CommonSteps.generateRandomNames(2);
	protected final static String subjectName = "AddSubjectTest#" + formattedDate + suffix;
	protected final static String subjectNameUpdated = subjectName + "-UPD";
	
	protected final static String adminLink = sitePortalUrl + "/admin";
	
	////////////////////////Test Environment data ///////////////////////////////
	protected final static String studyLink = sitePortalUrl
			+ "/admin#/manage/studies/40d306d5-24d6-43cd-838b-7a673eede285/general";
	protected final static String newSubjectLink = sitePortalUrl
			+ "/site/540bbcaa-07e2-4d87-a4cb-5c17dd7afffb/filter/55b92f9a-8dfd-40d7-8b05-0404c78f8290/subject/00000000-0000-0000-0000-000000000000";
	
	//////////////////////// STG Environment data ///////////////////////////////
	/*protected final static String studyLink = sitePortalUrl
			+ "/admin#/manage/studies/014ed2ce-d282-48f9-87b2-3ba4ba2c10a5/general";
	protected final static String newSubjectLink = sitePortalUrl
			+ "/site/db3b8ab6-278d-49e2-87a7-c76c5ac8babf/filter/55b92f9a-8dfd-40d7-8b05-0404c78f8290/subject/00000000-0000-0000-0000-000000000000";*/
	
	/*protected final static String[] dataList = 
		{" ", "0","1","2", "3", "4", "5", "6","7","8","9",
			"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
			"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"`", "~", "!",  "@", "#", "$", "%", "^", "&", "*", "(",")", "-", "_", "=", "+", "[", "]", "{", "}", "\\", "|", ":", ";","'","\"",",",".","<",">","/","?"
		};*/
	
	protected final static String[] dataList = { " ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-",
			".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", "A", "B",
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z", "[", "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~" };
	
	protected Custom custom;
	protected SubjectSteps steps;
	protected ErrorSteps errorSteps;
	protected SubjectMaskingSteps maskingSteps;
	protected PdfLog pdfLog = new PdfLog();
	protected int validStudyCharCount;
	
	// ------------------------------------------------------------------------------------------------------------------------
}