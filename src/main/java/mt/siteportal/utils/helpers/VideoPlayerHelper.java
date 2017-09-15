package mt.siteportal.utils.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;

/**
 * Helper class to assist with the Video Player Controls
 * 
 * @author Syed A. Zawad
 */
public class VideoPlayerHelper {
	
	/*
	 * Locator for the Video Player's Container
	 */
	private By videoPlayerContainerLocator = By.id("video-player-container");

	private WebDriver driver;
	/*
	 * Constructor
	 */
	public VideoPlayerHelper(WebDriver driver) {
		this.driver = driver;
		UiHelper.waitFor(videoPlayerContainerLocator);
		UiHelper.waitForVisibility(videoPlayerContainerLocator);
		UiHelper.waitForSpinnerEnd(driver.findElement(By.cssSelector("div.spinner")), 30);
	}

	/**
	 * Press the SPACE key after focusing on the Video player's screen
	 */
	public void keypressSpaceOnScreen() {
		Actions action = new Actions(driver);
		WebElement screen = driver.findElement(By.className("fp-ui"));
		action.moveToElement(screen).sendKeys(Keys.SPACE).perform();
		UiHelper.sleep(3000);
	}

	/**
	 * Gets the values of the Video Player Container's class attribute as a
	 * single String
	 * 
	 * @return String - the value of the Video Player Container's Class
	 *         attribute
	 */
	public String getStatusFor(String attr) {
		UiHelper.fluentWaitForCssValue(videoPlayerContainerLocator, attr, 15);
		WebElement playerStatus = driver.findElement(videoPlayerContainerLocator);
		return playerStatus.getAttribute("class");
	}
	
	/**
	 * Click the play or pause button for the Video Player
	 */
	public void clickPlayOrPause() {
		UiHelper.click(driver.findElement(By.className("fp-play")));
		UiHelper.sleep(3000);
	}

	/**
	 * Checks the Video Player Container's class attributes for the specific
	 * String
	 * 
	 * @param status
	 *            - String - the status that is checked for in the class
	 *            values
	 * @return boolean - true if the String status is found, false otherwise
	 */
	public boolean isStatus(String status) {
//		String statuses = getStatuses();
		String statuses = getStatusFor(status);
		if (statuses.contains(status))
			return true;
		return false;
	}

	/**
	 * Performs a series of actions and checks to ensure that the volume
	 * button is working correctly First, check that the volume indicator is
	 * at 100%. Then click mute and check that the indicator is at 0%.
	 * Again, click un-mute and check that the indicator is at 100%
	 * 
	 * @return booleam - true if all the above conditions are met, false
	 *         otherwise
	 */
	public boolean checkVolumeControl() {
		String volumeLevel = driver.findElement(By.cssSelector(".fp-volumelevel"))
				.getAttribute("style");
		if (!volumeLevel.contains("width: 100%")) {
			Log.logInfo(
					"The volume control does not show an initial volume level of 100%. The style attribute shows : "
							+ volumeLevel);
			return false;
		}
		UiHelper.click(By.cssSelector("a.fp-mute"));
		volumeLevel = driver.findElement(By.cssSelector(".fp-volumelevel")).getAttribute("style");
		if (!volumeLevel.contains("width: 0%")) {
			Log.logInfo(
					"The volume control does not show a volume level of 0% after clicking mute. The style attribute shows : "
							+ volumeLevel);
			return false;
		}
		UiHelper.click(By.cssSelector("a.fp-mute"));
		volumeLevel = driver.findElement(By.cssSelector(".fp-volumelevel")).getAttribute("style");
		if (!volumeLevel.contains("width: 100%")) {
			Log.logInfo(
					"The volume control does not show a volume level of 100% after unclicking mute. The style attribute shows : "
							+ volumeLevel);
			return false;
		}
		return true;
	}

	/**
	 * Checks if the toggle full-screen icon is present, visible and enabled
	 * 
	 * @return boolean - true if all the above conditions are met, false
	 *         otherwise
	 */
	public boolean checkFullscreenToggleLink() {
		WebElement toggle_fullscreen = driver.findElement(By.className("fp-fullscreen"));
		return (toggle_fullscreen.isDisplayed() && toggle_fullscreen.isEnabled());
	}

	/**
	 * Click the Video Players Screen
	 */
	public void clickScreen() {
		UiHelper.click(By.cssSelector("div.fp-ui"));
		UiHelper.sleep(3000);
	}

	/**
	 * Waits until the buffer is 100% or the timeout is reached for the
	 * Video Player to Download the video
	 * 
	 * @param timeout_in_seconds
	 *            - int - maximum time until which the function waits for
	 *            the buffer to complete
	 */
	public void waitForVideoToFullyLoad(int timeout_in_seconds) {
		Double percent_downloaded = getBufferStatus();
		int time_in_seconds = 0;
		while (percent_downloaded < 99.5 && time_in_seconds <= timeout_in_seconds) {
			UiHelper.sleep();
			percent_downloaded = getBufferStatus();
			time_in_seconds++;
		}
		if (time_in_seconds > timeout_in_seconds) {
			Log.logInfo("The Video buffer timed out at " + percent_downloaded + "% downloaded for timeout : "
					+ timeout_in_seconds);
		}
	}

	/**
	 * returns the percentage of the full video buffered as double
	 * 
	 * @return Double - percent of the video file buffered
	 */
	private Double getBufferStatus() {
		WebElement buffer = driver.findElement(videoPlayerContainerLocator)
				.findElement(By.cssSelector(" .fp-buffer"));
		String styles = buffer.getAttribute("style");
		styles = styles.replaceAll("[^0-9\\.]", "");
		try {
			return Double.valueOf(styles);
		} catch (NumberFormatException nfe) {
			Log.logInfo("Could not parse style's attribute : " + styles + " to Double. Returning 0.");
			return 0.0;
		}
	}

	/**
	 * Gets the percentage of the video that has been played from the Video Player's
	 * UI Timeline
	 * 
	 * @return Double - percent of Video played.
	 */
	public Double getVideoTimeline() {
		WebElement buffer = driver.findElement(videoPlayerContainerLocator)
				.findElement(By.cssSelector(" .fp-progress"));
		String [] styles = buffer.getAttribute("style").split("; *");
		String style = "";
		for(String eq : styles){
			if(eq.contains("width")){
				style = eq.replaceAll("[^0-9\\.]", "");
				break;
			}	
		}
		try {
			return Double.valueOf(style);
		} catch (NumberFormatException nfe) {
			Log.logInfo("Could not parse style's attribute : " + style + " to Double. Returning 0.");
			return 0.0;
		}
		
	}

	/**
	 * Play Video for a few seconds
	 * Clicks the play video button, waits for given seconds and clicks the pause video button
	 */
	public void playForSomeTime(int timeInSecond) {
		clickPlayOrPause();
		UiHelper.sleep(1000 * timeInSecond);
		clickPlayOrPause();
	}
}