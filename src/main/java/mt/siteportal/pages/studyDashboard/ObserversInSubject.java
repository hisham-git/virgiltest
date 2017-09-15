package mt.siteportal.pages.studyDashboard;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by maksym.tkachov on 1/27/2016.
 */
public class ObserversInSubject extends BasePage {

    public By addObserverButton = new By.ByXPath("//a[contains(@data-ng-click, 'addObserver()')]/span");

    public By newObserverAliasInput = new By.ByXPath("//input[@data-ng-model='newObserver.alias']");

    public By newObserverRelationInput = new By.ByXPath("//input[@data-ng-model='newObserver.relation']");

    public By editedItemAliasInput = new By.ByXPath("//input[@data-ng-model='editedItem.alias']");

    public By editedItemRelationInput = new By.ByXPath("//input[@data-ng-model='editedItem.relation']");

 /*   @FindBy(how= How.XPATH, using= "//div[@data-items='viewModel.observers.items']//div[@class='row']//span[@class='icon-small icon-edit']")
    public WebElement EditButton;

    @FindBy(how= How.XPATH, using= "//div[@data-items='viewModel.observers.items']//div[@class='row']//span[@class='icon-small icon-delete']")
    public WebElement DeleteButton;

    @FindBy(how= How.XPATH, using= "//div[@data-items='viewModel.observers.items']//div[@class='row']//span[@class='icon-small icon-save']")
    public WebElement SaveButton;

    @FindBy(how= How.XPATH, using= "//div[@data-items='viewModel.observers.items']//div[@class='row']//span[@class='icon-small icon-cancel']")
    public WebElement CancelButton;*/
    private String observerTableLocator = "//div[@data-items='viewModel.observers.items']";
    private String disabledLocator = "//div[@data-items='viewModel.observers.items']//div[@class='row']//div[@class='editing-controls']/*[@disabled][@title='%s']";
    public By editButton = new By.ByXPath(observerTableLocator+"//div[@class='editing-controls']//span[@class='icon-small icon-edit']");
    public By deleteButton = new By.ByXPath(observerTableLocator+"//div[@class='editing-controls']/a[not(contains(@class, 'ng-hide'))]/span[@class='icon-small icon-delete']");
    public By saveButton = new By.ByXPath(observerTableLocator+"//div[@class='row']//div[@class='editing-controls']//span[@class='icon-small icon-save']");
    public By cancelButton = new By.ByXPath(observerTableLocator+"//div[@class='row']//div[@class='editing-controls']/*[not(contains(@class,'ng-hide'))]//span[@class='icon-small icon-cancel']");
    public By removeButton = new By.ByXPath(observerTableLocator+"//div[@class='editing-controls']//button[@title='Remove']");




    public ObserversInSubject(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks on the Cancel button in selected Observer and cancel all changes
     */
    public void cancelObserver(){
        Log.logInfo("Cancel observer values");
        //WebElement elem = ObserversTable.findElement(new By.ByXPath(".//div[@class='row']"));
        UiHelper.click(cancelButton);
    }

    /**
     * Fills required fields for Observer
     * @param relation required input "Relation"
     * @param alias required input "Alias"
     */
    public void fillRequiredFields (String relation, String alias){
        Log.logInfo("Filling Required fields");
        UiHelper.sendKeys(newObserverRelationInput, relation);
        UiHelper.sendKeys(newObserverAliasInput, alias);
    }

    /**
     * Clears already filled data and fills another data
     * @param relation required input "Relation"
     * @param alias required input "Alias"
     */
    public void editRequiredFields (String relation, String alias){
        Log.logInfo("Editing Required fields");
        UiHelper.clear(editedItemRelationInput);
        UiHelper.sendKeys(editedItemRelationInput,relation);
        UiHelper.clear(editedItemAliasInput);
        UiHelper.sendKeys(editedItemAliasInput, alias);
    }

    /**
     * Select the existing Observer in the observer list by relation or alias
     * @param relationOrAlias relation or alias string to select
     */
    public void selectObserver(String relationOrAlias){
        Log.logInfo("Selecting Observer "+ relationOrAlias);
        UiHelper.click(new By.ByXPath("//label[@title='"+relationOrAlias+"']"));
    }

    /**
     * Saves the new Observer to database
     * @param relation required input "Relation"
     * @param alias required input "Alias"
     */
    public void addObserver(String relation, String alias){
        Log.logInfo("Adding Observer with relation="+ relation+" and alias="+alias);
        UiHelper.click(addObserverButton);
        fillRequiredFields(relation,alias);
        UiHelper.click(saveButton);
    }

    /**
     * Changes old data in existing Observer to New data
     * @param oldAliasOrRelation relation or alias string to select existing observer
     * @param relation new relation string
     * @param alias new alias string
     */
    public void editObserver(String oldAliasOrRelation, String relation, String alias){
        Log.logInfo("Editing Observer from "+ oldAliasOrRelation+ "to "+ alias);
        selectObserver(oldAliasOrRelation);
        UiHelper.click(editButton);
        editRequiredFields(relation,alias);
        UiHelper.click(saveButton);
    }

    /**
     * Deletes existing Observer
     * @param aliasOrRelation relation or alias string to delete existing observer
     */
    public void deleteObserver(String aliasOrRelation){
        Log.logInfo("Deleteing Observer with alias "+ aliasOrRelation);
        selectObserver(aliasOrRelation);
        UiHelper.click(deleteButton);
        UiHelper.click(removeButton);
    }

    /**
     * Verifies that save button for new or existing Observer is disabled
     * @return true if button is disabled otherwise false
     */
    public boolean isButtonDisabled(String button){
        return UiHelper.isPresent(new By.ByXPath(String.format(disabledLocator,button)));
    }

    /**
     * Verifies that existing Observer in the list
     * @param relationOrAlias relation or alias string to verify existing observer
     * @return true if Observer in the list otherwise false
     */
    public boolean isObserverInTable(String relationOrAlias){
        return UiHelper.isPresent(new By.ByXPath(".//*[text()='"+relationOrAlias+"']"));
    }

}
