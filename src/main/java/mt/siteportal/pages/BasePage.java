package mt.siteportal.pages;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;


/**
 * Base class for all <a href="http://code.google.com/p/selenium/wiki/PageObjects">PageObjects</a>.  
 */
public class BasePage {
	protected WebDriver driver;

	/*protected By saveButton = new By.ByCssSelector(".icon-small.icon-save");
	protected By editButton = new By.ByCssSelector(".icon-small.icon-edit");
	protected By deleteButton = new By.ByCssSelector(".icon-small.icon-delete");
	protected By removeButton = new By.ByXPath("//button[@title='Remove']");
	protected By cancelButton = new By.ByCssSelector(".icon-small.icon-cancel");
	protected By addButton = new By.ByCssSelector(".icon-small.icon-add");*/


	/**
	 * Default constructor. Need it to use <a href="http://selenium.googlecode.com/svn/trunk/docs/api/java/index.html">PageFactory</a>
	 * @param driver - current WebDriver(browser) object.
	 */
	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

    /**
     * An alternative constructor, allows not to use PageFactory.
     */
    public BasePage(){
    }
    
    protected static Map<String, Map<String, String>> locators = new HashMap<String, Map<String,String>>();
    

    
}