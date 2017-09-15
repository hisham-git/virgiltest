package mt.siteportal.pages.Administration.people;

import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class LanguagesTab {

    @FindBy(how = How.XPATH, using = "//a[@title='Save']")
    public WebElement Save;

    @FindBy(how = How.XPATH, using = "//a[@title='Cancel']")
    public WebElement Cancel;

    private String languagesXpath = "//div[@class='col-xs-24'][parent::div[not(contains(@class,'ng-hide'))]]//label[@class='ng-binding']";


    public List<String> getlanguages(){
        Log.logInfo("Getting the languages of person");
        return TextHelper.getElementTextContent(UiHelper.findElements(new By.ByXPath(languagesXpath)));
    }
}
