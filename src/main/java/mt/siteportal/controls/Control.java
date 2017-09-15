package mt.siteportal.controls;

import org.openqa.selenium.WebElement;

public class Control {
	
	protected WebElement element;
	public static final String DROPDOWNXPATH = ".//button[@class='btn dropdown-toggle btn-default']";
	public static final String INPUTXPATH = "./input[@type='text']";
	public static final String TEXTAREAXPATH = ".//textarea";
	public static final String CALENDARXPATH = ".//div[@class='value']";
	
	public Control(WebElement element){
		this.element = element;
	}
}
