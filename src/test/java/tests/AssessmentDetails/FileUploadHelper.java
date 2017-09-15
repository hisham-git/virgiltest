package tests.AssessmentDetails;

import mt.siteportal.utils.tools.Log;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * Class to help in uploading files to the VIRGIL Platform through Java Robots
 * IS NOT MEANT TO BE USED IN OTHER CLASSES This class is essentially a fix to
 * the hidden file inputs that Selenium cannot access
 * 
 * @author Syed A. Zawad
 *
 */
public class FileUploadHelper {

	/**
	 * A bunch of sequences that uses a OS robot to upload files to the browser
	 * @param absoluteFilePath
	 */
	public static void inputFilePath(String absoluteFilePath) {
		StringSelection ss = new StringSelection(absoluteFilePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		try {
			Robot robot = new Robot();
			robot.delay(500);
			
			// to emulate alt+tab for activating windows open menu
			/*robot.keyPress(KeyEvent.VK_ALT);
		    robot.keyPress(KeyEvent.VK_TAB);
		    robot.delay(1000);
		    robot.keyRelease(KeyEvent.VK_TAB);
		    robot.delay(3000);
		    robot.keyRelease(KeyEvent.VK_ALT);*/
		    
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(500);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.delay(1500);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException awte) {
			awte.printStackTrace();
			Log.logInfo("Could not enter the path in the File Browser correctly...");
			return;
		}
		Log.logStep("Entered [" + absoluteFilePath + "] to the File Browser...");
	}

}
