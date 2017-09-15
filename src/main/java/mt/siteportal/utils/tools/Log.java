package mt.siteportal.utils.tools;

import java.io.IOException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.TestResult;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import mt.siteportal.utils.Browser;

/**
 * An extension for TestNG's Reporter.
 * Adds more convenient logging interface.
 *
 * TODO: Add screenshots capturing.
 * TODO: Add "multy errors" support, see http://seleniumexamples.com/blog/guide/using-soft-assertions-in-testng/
 */
public class Log extends Reporter {
    private static final Logger logger = LoggerFactory.getLogger(Log.class);

	private static String newLine = "\r\n </br>";
	private static ExtentReports extentReport;
	private static ExtentTest extntTest;
	private static ThreadLocal<ExtentTest> testNode;
	private static ThreadLocal<ExtentTest> classNode;
    private static ThreadLocal<ExtentTest> methodNode;
	
	/**
	 * 'extentreports', version:'2.41.1'
	 * Documentation: http://extentreports.com/docs/versions/2/java/
	 * Examples: http://www.techbeamers.com/generate-reports-selenium-webdriver/#h1
	 */

	/**
	 * Logs verification error commented by the message specified.
	 * @param errorMessage - error's comment.
	 */
	public static void logVerificationError(String errorMessage) {
		logVerificationError(errorMessage, null);
		extntTest.error(errorMessage);
	}

	/**
	 * Logs verification error commented by the message specified.
	 * @param errorMessage - error's comment.
	 * @param throwable - The throwable to log.
	 */
	public static void logVerificationError(String errorMessage, Throwable throwable) {
		log(String.format("%s %s [VERIFICATION ERROR] %s", newLine, Util.getTimeStampForLog(), errorMessage), true);
		extntTest.error(errorMessage);
		reportFailure(throwable);
	}

    /**
	 * Logs Exception commented by the message specified.
	 * @param message - the comment.
	 * @param throwable - The throwable to log.
	 */
	public static void logException(String message, Throwable throwable) {
		logMessage(String.format("[Exception] %s", message));
		try {
			extntTest.fatal(message, MediaEntityBuilder
					.createScreenCaptureFromPath(ExtentManager.createScreenshot(Browser.getDriver())).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: logger.error(message);
		reportFailure(throwable);
	}

    /**
     * Logs Error commented by the message specified.
     * @param message - the comment.
     * @param throwable - The throwable to log.
     */
	public static void logError(String message, Throwable throwable) {
		logMessage(String.format("[ERROR] %s", message));
		try {
			extntTest.error(message, MediaEntityBuilder
					.createScreenCaptureFromPath(ExtentManager.createScreenshot(Browser.getDriver())).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: logger.error(message);

		reportFailure(throwable);
	}

    /**
     * Logs Error commented by the message specified.
     */
	public static void logError(String message) {
		logError(message, null);
		try {
			extntTest.error(message, MediaEntityBuilder
					.createScreenCaptureFromPath(ExtentManager.createScreenshot(Browser.getDriver())).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Logs test step message.
	 * @param stepMessage - test step's comment.
	 */
	public static void logStep(String stepMessage){
		logMessage(String.format("[Step] %s", stepMessage));
		extntTest.info(stepMessage);
	}
	
	/**
	 * Logs test verify message.
	 * @param vefifyMessage - test verify's comment.
	 */
	public static void logVerify(String verifyMessage){
		logMessage(String.format("[Verify] %s", verifyMessage));
		extntTest.info(verifyMessage);
	}
	
	/**
	 * Logs suite Info message.
	 * @param message - Info message to log.
	 */
	public static void logSuiteInfo(String message){
		logMessage(String.format("[Info] %s", message));
	}
	
	/**
	 * Logs test Info message.
	 * @param message - Info message to log.
	 */
	public static void logTestInfo(String message){
		logMessage(String.format("[Info] %s", message));
		extntTest.info(message);
	}
	
	/**
	 * Logs test Info message.
	 * @param message - Info message to log.
	 */
	public static void logInfo(String message){
		logMessage(String.format("[Info] %s", message));
//		childMethod.info(message);
	}
	
	/**
	 * Logs test pass message.
	 * @param message - Info message to log.
	 */
	public static void logPass(String message){
		logMessage(String.format("[Pass] %s", message));
		extntTest.pass(message);
	}
	
	/**
	 * Logs test fail message.
	 * @param message - Info message to log.
	 */
	public static void logFail(String message) {
		logMessage(String.format("[Fail] %s", message));
		try {
			extntTest.fail(message, MediaEntityBuilder
					.createScreenCaptureFromPath(ExtentManager.createScreenshot(Browser.getDriver())).build());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Logs test Warning message.
	 * @param message - Warning message to log.
	 */
	public static void logWarning(String message){
		logMessage(String.format("[WARNING] %s", message));
		extntTest.warning(message);
	}
    
    /**
     * Logs test Warning message.
     * @param message - Warning message to log.
     */
    public static void logDebugMessage(String message){
        logMessage(String.format("---> [DEBUG] %s", message));
    }
	
	/**
	 * Logs test message.
	 * @param message - message to log.
	 */
	public static void logMessage(String message){
		log(String.format("%s %s %s", newLine, Util.getTimeStampForLog(), message), true);
//TODO:		logger.info(message);
	}

	/**
	 * Captures screenshot and saves it to the specified location.
	 * @param saveToLocation - path to save the image.
	 */
	public void captureScreenshot(String saveToLocation){
        //TODO: implement screenshots capturing.
        throw new UnsupportedOperationException("Not implemented yet");
	}

    private static void reportFailure(Throwable throwable) {
        TestResult tr = (TestResult) Reporter.getCurrentTestResult();

        tr.setStatus(ITestResult.FAILURE);
        if (null != throwable){
            tr.setThrowable(throwable);
        }

        Reporter.setCurrentTestResult(tr);
    }
    
    //---------------------------------------------------------- Log methods for test execution status ----------------------------------------------------------
    
	/**
	 * Logs at the start of TestNG test suite
	 * 
	 * @param testContext
	 */
	public static void logTestSuiteStart(ITestContext testContext) {
		logMessage("[+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [Starting new Test Suite '"
				+ testContext.getSuite().getName() + "'] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+] [+]");
		// Start the test using the ExtentManager static class.
		extentReport = ExtentManager.GetExtent();	
	}
	
	/**
	 * Logs at the end of TestNG test suite
	 * 
	 * @param testContext
	 */
	public static void logTestSuiteEnd(ITestContext testContext) {
		logMessage("[-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [Ending Test Suite '"
				+ testContext.getSuite().getName() + "'] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-] [-]");
		// This method will clear all resources
		// extentReport.close();
	}
	
	/**
	 * Logs at the start of TestNG test tag
	 * 
	 * @param testContext
	 */
	public static void logTestStart(ITestContext testContext) {
		logMessage("<+> <+> <+> <+> <+> <+> <+> <+> <+> <+> <Starting new Test '" + testContext.getName()
				+ "'> <+> <+> <+> <+> <+> <+> <+> <+> <+> <+>");
		testNode = new ThreadLocal<ExtentTest>();
		extntTest = extentReport.createTest(testContext.getCurrentXmlTest().getName());
		testNode.set(extntTest);
	}
	
	/**
	 * Logs at the end of TestNG test tag
	 * 
	 * @param testContext
	 */
	public static void logTestEnd(ITestContext testContext) {
		logMessage("<-> <-> <-> <-> <-> <-> <-> <-> <-> <-> <Ending Test '" + testContext.getName()
				+ "'> <-> <-> <-> <-> <-> <-> <-> <-> <-> <->");
	}
	
	
	/**
     * Logs at the start of TestNG test class
     */
	public static void logTestClassStart(Class<?> cls) {
		logMessage("{+} {+} {+} {+} {+} {+} {+} {+} {+} {+} {Starting new Test Class '" + cls.getSimpleName()
				+ "'} {+} {+} {+} {+} {+} {+} {+} {+} {+} {+}");
		classNode = new ThreadLocal<ExtentTest>();
		try {
			extntTest = testNode.get().createNode(cls.getSimpleName());	
		} catch (NullPointerException e) {
			System.out.println("===> testNode found null: "+ (null == testNode));
		}
		classNode.set(extntTest);
	}
	
    /**
     * Logs at the end of TestNG test class
     */
	public static void logTestClassEnd(Class<?> cls) {
		logMessage("{-} {-} {-} {-} {-} {-} {-} {-} {-} {-} {Ending Test Class '" + cls.getSimpleName()
				+ "'} {-} {-} {-} {-} {-} {-} {-} {-} {-} {-}");
	}
    
    /**
     * Logs at the start of TestNG test class
     */
    public static void logTestGroupStart(String groupName){
    	logMessage("# # # # # # # # # # {Starting new Test Group '"+groupName+"'} # # # # # # # # # #");
    }
	
    /**
     * Logs at the end of TestNG test class
     */
    public static void logTestGroupEnd(String groupName){
    	logMessage("# # # # # # # # # # {Ending Test Group '"+groupName+"'} # # # # # # # # # #");
    }

    /**
     * Logs at the start of TestNG test method
     */
    public static void logTestMethodStart(Method method){
    	logMessage("(+) (+) (+) (+) (+) (Starting new Test Method '"+ method.getName() +"') (+) (+) (+) (+) (+)");
    	methodNode = new ThreadLocal<ExtentTest>();
    	extntTest = classNode.get().createNode(method.getName());
		methodNode.set(extntTest);
    }
    
    /**
     * Logs at the end of TestNG test method
     */
    public static void logTestMethodEnd(Method method, ITestResult result){
    	logMessage("(-) (-) (-) (-) (-) (Ending Test Method '"+ method.getName() +"') (-) (-) (-) (-) (-)");
    	if (result.getStatus() == ITestResult.FAILURE)
    		methodNode.get().fail(result.getThrowable());
        else if (result.getStatus() == ITestResult.SKIP)
        	methodNode.get().skip(result.getThrowable());
        else
        	methodNode.get().pass("[TEST PASSED]");

    	extentReport.flush();
    }
}