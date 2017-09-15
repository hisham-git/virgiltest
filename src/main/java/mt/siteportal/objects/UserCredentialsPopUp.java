package mt.siteportal.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class UserCredentialsPopUp {

	private WebElement baseElement;
	private WebDriver driver;
	
	private By usernameInputLocator = By.id("loginInput"), passwordInputLocator = By.xpath(".//*[@id='passwordInput']"),
			okButtonLocator = By.xpath(".//div[text()='OK']"),
			cancelButtonLocator = By.xpath(".//div[text()='Cancel']");

	public UserCredentialsPopUp(WebElement baseElement, WebDriver driver) {
		this.baseElement = baseElement;
		this.driver = driver;
	}

	public boolean isOpen() {
		boolean username = UiHelper.isPresent(baseElement.findElement(usernameInputLocator));
		boolean password = UiHelper.isPresent(baseElement.findElement(passwordInputLocator));
		boolean ok = UiHelper.isPresent(baseElement.findElement(okButtonLocator));
		boolean cancel = UiHelper.isPresent(baseElement.findElement(cancelButtonLocator));
		if (username && password && ok)
			return true;
		Log.log("Is Present? : Username Field : " + username + ", password field : " + password + ", OK button : " + ok
				+ ", Cancel : " + cancel);
		return false;
	}

	public void enterCredentials(String username, String password) {
		UiHelper.sendKeys(baseElement.findElement(usernameInputLocator), username);
		UiHelper.sendKeys(baseElement.findElement(passwordInputLocator), password);
	}
	
	public void ok(){
		UiHelper.click(baseElement.findElement(okButtonLocator));
		UiHelper.waitForSpinnerEnd(driver);
	}

}
