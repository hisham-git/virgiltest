package mt.siteportal.utils.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Helper Class for maanging HTML5s PDF Viewer
 * 
 * @author Syed A. Zawad
 *
 */
public class PDFViewerHelper {

	@FindBy(css = "iframe.embedPdf")
	private WebElement pdfContainer;

	private WebDriver driver;

	public PDFViewerHelper(WebDriver driver) {
		this.driver = driver;
		UiHelper.waitForSpinnerEnd(driver.findElement(By.cssSelector("div.spinner")), 30);
	}

	public void printFrames() {
		driver.switchTo().frame(0);
		WebElement input = driver.findElement(By.id("pageNumber"));
		System.out.println("Page Number " + input.getAttribute("value"));
		driver.switchTo().parentFrame();
	}

	public void goToPageNumber(String pageNumber) {
		driver.switchTo().frame(0);
		WebElement input = driver.findElement(By.id("pageNumber"));
		input.sendKeys(Keys.BACK_SPACE);
		input.sendKeys(pageNumber);
		input.sendKeys(Keys.ENTER);
		driver.switchTo().parentFrame();
	}

	public String getCurrentPageNumber() {
		driver.switchTo().frame(0);
		WebElement input = driver.findElement(By.id("pageNumber"));
		String value = input.getAttribute("value");
		driver.switchTo().parentFrame();
		return value;
	}

}
