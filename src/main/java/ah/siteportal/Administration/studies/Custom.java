package ah.siteportal.Administration.studies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class Custom extends Studies {

//	@FindBy(how = How.CSS, using = "input[name='ctrl1513']")
//	private WebElement screeningInput;
	
	@FindBy(how = How.XPATH, using = "//label[contains(text(),'Screening#')]")
	private WebElement screeningLabel;
	
	@FindBy(how = How.XPATH, using = "//label[contains(text(),'Screening#')]/parent::div/following-sibling::div/input")
	private WebElement screeningInput;

	@FindBy(how = How.CSS, using = "input[name='ctrl1514']")
	private WebElement randomizationInput;

	@FindBy(how = How.CSS, using = "input[name='ctrl1515']")
	private WebElement temporaryIDInput;

	@FindBy(how = How.CSS, using = "a[title='Save']")
	private WebElement saveButton;

	@FindBy(how = How.CSS, using = "a[title='Cancel']")
	private WebElement cancelButton;

	public Custom(WebDriver driver) {
		super(driver);
	}

	public WebElement getScreeningLabel() {
		return screeningLabel;
	}

	public WebElement getScreeningInput() {
		return screeningInput;
	}

	public WebElement getRandomizationInput() {
		return randomizationInput;
	}

	public WebElement getTemporaryIDInput() {
		return temporaryIDInput;
	}

	public WebElement getSaveButton() {
		return saveButton;
	}

	public WebElement getCancelButton() {
		return cancelButton;
	}
}
