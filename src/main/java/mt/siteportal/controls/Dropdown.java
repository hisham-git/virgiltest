package mt.siteportal.controls;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mt.siteportal.utils.helpers.UiHelper;

public class Dropdown extends Control {

	private static final Logger LOGGER = LoggerFactory.getLogger(Dropdown.class);

	public Dropdown(WebElement element) {
		super(element);
	}

	public void selectValue(String value) {
		LOGGER.debug("Selecting in dropdown " + value);
		element.findElement(new By.ByXPath(DROPDOWNXPATH)).click();
		element.findElement(new By.ByXPath(".//span[text()='  " + value + "']/..")).click();
	}
	
	public void selectIndex(int value) {
		LOGGER.debug("Selecting in dropdown index " + value);
		element.findElement(new By.ByXPath(DROPDOWNXPATH)).click();
		element.findElement(new By.ByXPath(".//li[" + value + "]")).click();
	}
	
	public List<WebElement> getAllOptions(){
		LOGGER.debug("Getting all dropdown selection options.");
		List<WebElement> elems = element.findElements(By.xpath(".//li/span"));
		return elems;
	}
	
	public String getSelectedValue(){
		LOGGER.debug("Getting the current selected option in dropdown");
		return element.findElement(new By.ByXPath(DROPDOWNXPATH)).getText();
	}

	public boolean isEnabled() {
		LOGGER.debug("Checking if dropdown is enabled.");
		WebElement button = element.findElement(By.xpath(".//button[contains(@class, 'dropdown-toggle')]"));
		return UiHelper.isEnabled(button);
	}
}
