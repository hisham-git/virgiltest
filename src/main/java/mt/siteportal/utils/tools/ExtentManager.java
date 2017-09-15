package mt.siteportal.utils.tools;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Examples: http://qavalidation.com/2016/10/selenium-html-result-reporting-using-extentreports-3-x.html/
 * Official documentation: http://extentreports.com/docs/versions/3/java/#htmlreporter-configuration
 * 
 * @author ahisham
 */
public class ExtentManager {
	private static ExtentReports extent;
	private static ExtentTest test;
	private static ExtentHtmlReporter htmlReporter;
	private static final String extentReportDir = System.getProperty("user.dir") + "\\build\\reports\\extentReports\\";
	private static final String extentImageDir = "screenshots\\";
	
	private static void checkDirectory(String folder) {
		File extentDir = new File(folder);
		if (!extentDir.exists()) {
			try {
				if (extentDir.mkdirs())
					Log.logInfo("Successfully created Extent Report directory structure: [" + extentDir + "]");
			} catch (Exception e) {
				e.getMessage();
			}
		}
	}
	
	public static ExtentReports GetExtent() {
		if (extent != null)
			return extent; // avoid creating new instance of html file
		// initialize ExtentReports
		extent = new ExtentReports();
		// attach the HtmlReporter
		extent.attachReporter(getHtmlReporter());
		return extent;
	}

	private static ExtentHtmlReporter getHtmlReporter() {
		// check if save directory exists, if not then create
		checkDirectory(extentReportDir);
		htmlReporter = new ExtentHtmlReporter(extentReportDir + "extentReportFile.html");

		// make the charts visible on report open
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK);

		htmlReporter.config().setDocumentTitle("Virgil Automation Report");
		htmlReporter.config().setReportName("Regression cycle");
		htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a");
		return htmlReporter;
	}
	
	public static ExtentTest createTest(String name, String description){
		test = extent.createTest(name, description);
		return test;
	}
	
	public static String createScreenshot(WebDriver driver) {
	    UUID uuid = UUID.randomUUID();
	 
	    // generate screenshot as a file object
	    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    try {
	        // copy file object to designated location
			FileUtils.copyFile(scrFile, new File(extentReportDir + extentImageDir + uuid + ".png"));
	    } catch (IOException e) {
	        Log.logError("Error while generating screenshot:\n" + e.toString());
	    }
	    return extentImageDir + uuid + ".png";
	}
}
