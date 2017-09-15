package mt.siteportal.details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.db.DbUtil;
import mt.siteportal.utils.helpers.RandomStringGeneratorHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.Score;

/**
 * Sub-page Page Object for the Scores Tab under the Assessment Details Web Page
 * 
 * @author Syed A. Zawad
 *
 */
public class ScoresTab extends BasePage {

	/*
	 * WebElements
	 */
	@FindBy(id = "scores")
	private WebElement baseElement;

	/*
	 * Locators
	 */
	private By checkboxesLocator = By.xpath(".//div[@class='checkbox']"),
			sectionsLocator = By.cssSelector(" div.section"),
			sectionHeaderLocator = By.cssSelector("div:not(.ng-scope)"),
			sectionQuestionsLocator = By.cssSelector("div.ng-scope"),
			questionRowsLocator = By.cssSelector(" .caption.ng-scope");

	/*
	 * Constructor
	 */
	public ScoresTab(WebDriver driver) {
		super(driver);
	}

	/**
	 * Returns a HashMap<String, WebElement> of Checkboxes mapped to their
	 * corresponding labels
	 * 
	 * @return HashMap<String, WebElement>
	 */
	public HashMap<String, WebElement> getCheckboxes() {
		List<WebElement> checkBoxes = baseElement.findElements(checkboxesLocator);
		HashMap<String, WebElement> checkboxMap = new HashMap<String, WebElement>();
		for (WebElement elem : checkBoxes) {
			checkboxMap.put(elem.getText(), elem);
		}
		return checkboxMap;
	}

	/**
	 * Get the list of Scores by reading it from the UI. Score Objects generated
	 * like this can only have Group Text, Score Text and Answer
	 * 
	 * @return List<Score> scores
	 */
	public List<Score> getScoresFromTab() {
		List<Score> scoresFromUI = new ArrayList<Score>();
		List<WebElement> sections = baseElement.findElements(sectionsLocator);
		for (WebElement section : sections) {
			WebElement sectionHeader = section.findElement(sectionHeaderLocator);
			String header = sectionHeader.getText();
			List<WebElement> sectionQuestions = section.findElements(sectionQuestionsLocator);
			for (WebElement sectionQuestion : sectionQuestions) {
				List<WebElement> questionAndAnswer = sectionQuestion.findElements(By.cssSelector("div.small"));
				String question = questionAndAnswer.get(0).getText();
				Score temp = new Score();
				temp.setGroup_text(header);
				temp.setScore_text(question);
				temp.setAnswer(questionAndAnswer.get(1).getText());
				scoresFromUI.add(temp);
			}
		}
		return scoresFromUI;
	}

	/**
	 * Gets the possible answers as specified in the FDEF xml file. Depends on
	 * the scoreType on the xml. Ignores the Scores which doesn't have
	 * ScoreTypes
	 * 
	 * @param possibleAnswers
	 *            HashMap<String, List<String>> The keys are the scoreType. The
	 *            values are Strings of Answer Options
	 * @return boolean true if the particular Score's Answer is one of the
	 *         possible answers. for every Score false if even one is not an
	 *         answer option
	 */
	public boolean checkPossibleAnswers(HashMap<String, List<String>> possibleAnswers) {
		List<Score> scoresFromUI = getScoresFromTab();
		for (Score score : scoresFromUI) {
			if (score.getScore_type() != null) {
				if (!possibleAnswers.get(score.getScore_type()).contains(score.getAnswer()))
					return false;
			}
		}
		return true;
	}

	/**
	 * Checks that the Scores and Groupings in the XML are represented perfectly
	 * in the UI.
	 * 
	 * @param scoresFromTester
	 *            List<Score> array of Scores from the tester to use against the
	 *            UI
	 * @return boolean true, if all the Questions group together correctly,
	 *         false if even one is incorrect
	 */
	public boolean checkQuestionsAndGrouping(List<Score> scoresFromTester) {
		List<WebElement> sections = baseElement.findElements(By.cssSelector(" div.section>div:not(.ng-scope)"));
		List<Score> scoresFromUI = getScoresFromTab();
		for (WebElement section : sections) {
			List<Score> filteredScoresFromUI = filterInGroup(section.getText(), scoresFromUI);
			List<Score> filteredScoresFromTester = filterInGroup(section.getText(), scoresFromTester);
			for (Score scoreItemFromTester : filteredScoresFromTester) {
				List<Score> filteredByQuestionsScoresFromUI = filterInQuestion(scoreItemFromTester.getScore_text(),
						filteredScoresFromUI);
				List<Score> filteredByQuestionsScoresFromTester = filterInQuestion(scoreItemFromTester.getScore_text(),
						filteredScoresFromTester);
				if (filteredByQuestionsScoresFromTester.size() != filteredByQuestionsScoresFromUI.size()) {
					System.out.println("Tester Scores : " + filteredByQuestionsScoresFromTester + " UI Scores : "
							+ filteredByQuestionsScoresFromUI);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Filters out all Questions that are not from the specified group
	 * 
	 * @param groupToFilterIn
	 *            - String - the name of the group to filter in
	 * @param scores
	 *            - List<Score> - the list of Scores from which to filter in
	 * @return List<Score> - list of scores containing only those scores from
	 *         the specified group
	 */
	private List<Score> filterInGroup(String groupToFilterIn, List<Score> scores) {
		List<Score> answer = new ArrayList<Score>();
		for (Score score : scores) {
			String group_text = score.getGroup_text();
			if (group_text != null && group_text.equals(groupToFilterIn)) {
				answer.add(score);
			}
		}
		return answer;
	}

	/**
	 * Takes in a list of scores, and filters out all scores other than the one
	 * specified
	 * 
	 * @param questionToFilterIn
	 *            - String - the Text of the Question Score
	 * @param scores
	 *            - List<Score> - the list from which to filter in from
	 * @return List<Score> - the List only containing the Scores with the
	 *         specified text
	 */
	private List<Score> filterInQuestion(String questionToFilterIn, List<Score> scores) {
		List<Score> answer = new ArrayList<Score>();
		for (Score score : scores) {
			String question_text = score.getScore_text();
			if (question_text != null && question_text.equals(questionToFilterIn)) {
				answer.add(score);
			}
		}
		return answer;
	}

	/**
	 * Checks the data in the Scores Tab to the ones from the database
	 * 
	 * @return - boolean - true if all the questions and answers match, false
	 *         otherwise
	 */
	public boolean checkAgainstDatabase(String formName, int SVID) {
		List<Score> scoresFromUI = getScoresFromTab();
		List<Score> scoresFromDB = DbUtil.getCompletedScoresFromDBFor(formName, SVID);
		boolean answerMatches = false;
		for (Score score : scoresFromDB) {
			List<Score> filteredByQuestionsFromUI = filterInQuestion(score.getScore_text(), scoresFromUI);
			for (Score scoreFiltered : filteredByQuestionsFromUI) {
				if (score.getAnswer().equals(scoreFiltered.getAnswer())) {
					answerMatches = true;
				}
			}
		}
		return answerMatches;
	}

	/**
	 * Creates a Map<String, String> of the Questions and Answers from the
	 * scores tab
	 * 
	 * @return - Map<String, String>
	 */
	public Map<String, String> getScoresAndAnswersFromTab() {
		Map<String, String> scoresAndAnswers = new HashMap<String, String>();
		List<WebElement> rows = baseElement.findElements(questionRowsLocator);
		for (WebElement row : rows){
			scoresAndAnswers.put(row.findElement(By.xpath("./div[1]/label")).getText(),
					row.findElement(By.xpath("./div[2]/label")).getText());
		}
		return scoresAndAnswers;
	}

	/*
	 * SECTION START ---------------------------------------------------------
	 * Edit, Save and Cancel buttons for Paper Transcription Scores
	 */
	public WebElement getEditButton() {
		return baseElement.findElement(By.xpath(".//a[@title='Edit']"));
	}

	public WebElement getSaveButton() {
		return baseElement.findElement(By.xpath(".//div[@title='Save']/a"));
	}

	public WebElement getCancelButton() {
		return baseElement.findElement(By.xpath(".//a[@title='Cancel']"));
	}
	/*
	 * SECTION END ----------------------------------------------------------
	 */

	/*
	 * Random Score funtions
	 */
	/**
	 * Selects a random question from the Scores Tab and returns it
	 * 
	 * @return - WebElement - the random element to be returned
	 */
	public WebElement getRandomScore() {
		List<WebElement> rows = baseElement.findElements(questionRowsLocator);
		return rows.get((new Random()).nextInt(rows.size()));
	}

	/**
	 * Takes a score item and changes its value to an appropriate random value
	 * For dropdown selections, selects a value from the options. For inputs -
	 * enters a random 4 digit number
	 * 
	 * @param scoreItem - WebElement - the score to be changed
	 * @return - String - the random text that was chosen to be the value
	 */
	public String makeRandomChangeTo(WebElement scoreItem) {
		WebElement inputBox = scoreItem.findElement(By.xpath(".//input[@type='text']"));
		WebElement dropdownSelection;
		try{
			dropdownSelection = scoreItem.findElement(By.cssSelector(" button.btn.dropdown-toggle.btn-default"));
		}catch(NoSuchElementException nse){
			dropdownSelection = null;
		}
		if (dropdownSelection != null && dropdownSelection.isDisplayed()) {
			UiHelper.click(dropdownSelection);
			List<WebElement> options = scoreItem.findElements(By.cssSelector(" div.dropdown-menu>ul>li"));
			int randomlySelectedOption = (new Random()).nextInt(options.size());
			UiHelper.click(options.get(randomlySelectedOption));
			return dropdownSelection.getText();
		}
		if (inputBox.isDisplayed()) {
			inputBox.clear();
			String randomFourDigitNumber = RandomStringGeneratorHelper.getRandomYear();
			inputBox.sendKeys(randomFourDigitNumber);
			return randomFourDigitNumber;
		}
		Log.logInfo("Could not edit the value for Score : [" + scoreItem.getText() + "]");
		return null;
	}

	public String getVersion() {
		return baseElement.findElement(By.xpath(".//div[@class='row scores-header']/div[2]")).getText();
	}
}
