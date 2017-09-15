package mt.siteportal.utils.helpers;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.tools.Log;

/**
 * A helper class for handling the Elements of the Audio Player
 * 
 * @author Syed A. Zawad
 */
public class AudioPlayerHelper{
	
	/*
	 * Locators for the Audio Player controls
	 */
	private By play_audio_button_locator = By.cssSelector(".fp-play"),
			audio_player_container_locator = By.id("audio-player-container");
	private WebDriver driver;

	/*
	 * Constructor
	 */
	public AudioPlayerHelper(WebDriver driver) {
		this.driver = driver;
		UiHelper.waitForSpinnerEnd(driver.findElement(By.cssSelector("div.spinner")), 30);
	}

	/**
	 * Wait until the Audio Player is not playing anymore
	 */
	public void waitForPlayerToEnd() {
		while(hasStatus("is-playing")){
			UiHelper.sleep();
		}
	}
	
	/**
	 * Wait until the Audio Player has finished downloading and is ready to be played 
	 */
	public void waitForPlayerToBeReady() {
		while(!hasStatus("is-ready")){
			UiHelper.sleep();
		}
		UiHelper.sleep();
	}
	
	/**
	 * Click the Audio Player's Play/Pause button
	 */
	public void clickPlayAudioButton() {
		UiHelper.waitFor(play_audio_button_locator);
		UiHelper.findElement(play_audio_button_locator).click();
	}
	
	/**
	 * Check if the Audio Player's class attribute contains a particular text.
	 * This text is used to represent the class to be checked for
	 * 
	 * @param class_name String - the class to be checked for
	 * @return boolean - true if the class name exists, false otherwise
	 */
	public boolean hasStatus(String class_name){
		String class_attributes = getStatuses();
		List<String> all_attributes = Arrays.asList(class_attributes.split(" "));
		return all_attributes.contains(class_name);
	}
	
	/**
	 * Waits for a defined period of time (in milliseconds) for the desired
	 * status to appear. If it doesn't then simply return the
	 * hasStatus(class_name)
	 * 
	 * @param class_name
	 *            String - the class to be checked for
	 * @param timeout_in_milliseconds
	 *            int - the amount of millisecond to wait (this function polls
	 *            at 250ms)
	 * @return boolean - true if the class name exists, false otherwise
	 */
	public boolean waitForStatus(String class_name, int timeout_in_milliseconds) {
		int time_waited = 0;
		while (time_waited < timeout_in_milliseconds && !hasStatus(class_name)) {
			UiHelper.sleep(250);
			time_waited += 250;
		}
		return hasStatus(class_name);
	}

	/**
	 * Get the String from the Audio Player container's class attribute
	 * 
	 * @return String, the class attribute value
	 */
	public String getStatuses(){
		return driver.findElement(audio_player_container_locator).getAttribute("class");
	}

	/**
	 * Check that the Audio Player exists and is visible
	 * @return boolean - true if exists and is Visible, false otherwise
	 */
	public boolean isEnabled() {
		return UiHelper.isPresentAndVisible(audio_player_container_locator);
	}
	
	/**
	 * Play the audio for given seconds
	 * Clicks play button, waits given seconds, then clicks the play button again
	 */
	public void playForSomeTime(int timeInSecond){
		clickPlayAudioButton();
		UiHelper.sleep(1000 * timeInSecond);
		clickPlayAudioButton();
	}
	
	/**
	 * Gets the percentage of the audio that has been played from the Audio Player's
	 * UI Timeline
	 * @return
	 */
	public Double getTimeline(){
		WebElement buffer = driver.findElement(audio_player_container_locator)
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
}