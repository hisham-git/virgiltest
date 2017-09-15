package ah.siteportal.Administration.studies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class Sites extends Studies {

//	@FindBy(how = How.CSS, using = "input[name='ctrl1513']")
//	private WebElement screeningInput;
	
	@FindBy(how = How.CSS, using = "div[data-value='site.number'] label")
	private WebElement siteLabel;
	
	@FindBy(how = How.CSS, using = "div[data-value='site.number'] input")
	private WebElement siteInput;

	@FindBy(how = How.CSS, using = "div[data-can-save='canSaveSite()'] a[title='Save']")
	private WebElement saveButton;

	@FindBy(how = How.CSS, using = "div[data-can-save='canSaveSite()'] a[title='Cancel']")
	private WebElement cancelButton;

	public Sites(WebDriver driver) {
		super(driver);
	}

	public WebElement getSiteLabel() {
		return siteLabel;
	}

	public WebElement getSiteInput() {
		return siteInput;
	}

	public WebElement getSaveButton() {
		return saveButton;
	}

	public WebElement getCancelButton() {
		return cancelButton;
	}
}