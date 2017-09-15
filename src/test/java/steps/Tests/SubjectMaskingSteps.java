package steps.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import ah.siteportal.Administration.studies.Custom;
import ah.siteportal.Administration.studies.Sites;
import ah.siteportal.Administration.studies.Studies;
import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.NewEditSubject;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Required;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import steps.Abstract.AbstractStep;

public class SubjectMaskingSteps extends AbstractStep {

	public SubjectMaskingSteps() {
		subjectTable = PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		visitTable = PageFactory.initElements(Browser.getDriver(), VisitTable.class);
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
		assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		newSubject = PageFactory.initElements(Browser.getDriver(), NewEditSubject.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		studies = PageFactory.initElements(Browser.getDriver(), Studies.class);
		
	}
	
	
	List<String> validStringList = new ArrayList<String>();

	public Custom getToCustomTab() {
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		studies = PageFactory.initElements(Browser.getDriver(), Studies.class);
		UiHelper.fluentWaitForElementClickability(studies.getCustomTab(), 60).click();
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(), Custom.class);
	}
	
	public Sites getToSitesTab() {
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		studies = PageFactory.initElements(Browser.getDriver(), Studies.class);
		UiHelper.fluentWaitForElementClickability(studies.getSitesTab(), 60).click();
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(), Sites.class);
	}

	public int checkString(String[] dataList, WebElement input) {
		int validCount = 0;
		Log.logStep("Checking valid characters from total list of: [" + dataList.length + "]");
		Actions builder = new Actions(Browser.getDriver());
		custom = PageFactory.initElements(Browser.getDriver(), Custom.class);
		if (custom.getScreeningLabel().isDisplayed()) {
			builder.doubleClick(custom.getScreeningLabel()).perform();
			for (String data : dataList) {
				Log.logStep("Checking character: " + data);
				input.click();
				input.sendKeys(data);
				if (!Required.isInputInvalid(input)) {
					validStringList.add(data);
					validCount++;
				}
				input.clear();
			}
		}
		Log.logDebugMessage("Total valid characters found: " + validCount);
		Log.logDebugMessage("Total valid character list: " + validStringList.toString());
		validStringList.clear();
		return validCount;
	}
	
	public void saveMask(String mask) {
		Log.logStep("Saving generated Mask: [" + mask + "]");
		Actions builder = new Actions(Browser.getDriver());
		custom = PageFactory.initElements(Browser.getDriver(), Custom.class);
		WebElement screeningLabel = custom.getScreeningLabel();
		WebElement scrreningInput = custom.getScreeningInput();
		if (screeningLabel.isDisplayed()) {
			builder.doubleClick(screeningLabel).perform();
			if (UiHelper.isClickable(scrreningInput)) {
				scrreningInput.click();
				scrreningInput.clear();
				scrreningInput.sendKeys(mask);
				UiHelper.click(custom.getSaveButton());
				UiHelper.waitForSpinnerEnd(Browser.getDriver());
			} else {
				throw new SkipException("Couldn't click Screening# field. Skipping tests...");
			}
		} else {
			throw new SkipException("Couldn't click Screening# field. Skipping tests...");
		}
	}
	
	private static final String regex = "(L(?<!\\\\L)|#(?<!\\\\#)|\\?)";
	private Pattern p = Pattern.compile(regex);
	private Matcher m;
	
	public void verifyMaskWithPartialCoverage(String mask, String site) {
		String input = "";
		m = p.matcher(mask);
		while (m.find()) {
			input += m.group();
		}
		Log.logDebugMessage("Original input: " + input);
		input = input.replaceAll("L", "A");
		input = input.replaceAll("#", "9");
		input = input.replace("?", "z");
		Log.logDebugMessage("Modified input: [" + input + "]");

		mask = mask.replaceAll("L(?<!\\\\L)", "A");
		mask = mask.replaceAll("#(?<!\\\\#)", "9");
		mask = mask.replaceAll("SN", site);
		mask = mask.replace("?", "z");
		mask = mask.replace("\\", "");
		Log.logDebugMessage("Replaced Mask as: [" + mask + "]");
		
		WebElement screeningField = newSubject.getInputByName("Screening#");
		if (null != UiHelper.fluentWaitForElementClickability(screeningField, 30)) {
			screeningField.click();
			screeningField.sendKeys(input);
			String dataEntered = screeningField.getAttribute("value");
			HardVerify.True(Required.isMaskValid(screeningField),
					"Verifyng screening# field is not required after entering data: [" + input + "]", "[PASSED]",
					"[FAILED]");
			HardVerify.Equals(dataEntered, mask, "Verifyng screening# field matches with entered data", "[PASSED]",
					"Expected: [" + mask + "]. Found: [" + dataEntered + "] [FAILED]");
		} else {
			throw new SkipException("Screening field not clickable within timeout. Skipping tests...");
		}
		Log.logDebugMessage("Verified masks: [" + ++currentMaskNo + "/" + results.size() + "]");
	}
	
	public void verifyMaskWithFullCoverage(String mask, String site) {
		String input = "";
		Map<String, String> maskInput = new HashMap<String, String>();
		m = p.matcher(mask);
		while (m.find()) {
			input += m.group();
		}
		if (mask.contains("#")) {
			for (int i = 0; i < 10; i++) {
				if (mask.contains("L") || mask.contains("?")) {
					for (int j = 0; j < 26; j++) {
						char upper = (char) ('A' + j);
						char lower = (char) ('a' + j);

						String key = mask.replaceAll("SN", site);
						key = key.replaceAll("#(?<!\\\\#)", String.valueOf(i));
						key = key.replaceAll("L(?<!\\\\L)", String.valueOf(upper));
						key = key.replace("?", String.valueOf(lower));
						key = key.replace("\\", "");
						Log.logDebugMessage("Orginal Mask: " + mask);
						Log.logDebugMessage("Modified Mask: [" + key + "]");

						String value = input.replaceAll("#", String.valueOf(i));
						value = value.replaceAll("L", String.valueOf(upper));
						value = value.replace("?", String.valueOf(lower));
						Log.logDebugMessage("Original input: " + input);
						Log.logDebugMessage("Modified input: [" + value + "]");

						maskInput.put(key, value);
					}
				} else {
					String key = mask.replaceAll("SN", site);
					key = key.replaceAll("#(?<!\\\\#)", String.valueOf(i));
					key = key.replace("\\", "");
					Log.logDebugMessage("Orginal Mask: " + mask);
					Log.logDebugMessage("Modified Mask: [" + key + "]");

					String value = input.replaceAll("#", String.valueOf(i));
					Log.logDebugMessage("Original input: " + input);
					Log.logDebugMessage("Modified input: [" + value + "]");

					maskInput.put(key, value);
				}
			}
		} else if (mask.contains("L") || mask.contains("?")) {
			for (int j = 0; j < 26; j++) {
				char upper = (char) ('A' + j);
				char lower = (char) ('a' + j);

				String key = mask.replaceAll("SN", site);
				key = key.replaceAll("L(?<!\\\\L)", String.valueOf(upper));
				key = key.replace("?", String.valueOf(lower));
				key = key.replace("\\", "");

				Log.logDebugMessage("Orginal Mask: " + mask);
				Log.logDebugMessage("Modified Mask: [" + key + "]");

				String value = input.replaceAll("L", String.valueOf(upper));
				value = value.replace("?", String.valueOf(lower));
				Log.logDebugMessage("Original input: " + input);
				Log.logDebugMessage("Modified input: [" + value + "]");

				maskInput.put(key, value);
			}
		}

		WebElement screeningField = newSubject.getInputByName("Screening#");
		if (null != UiHelper.fluentWaitForElementClickability(screeningField, 30)) {

			for (String modifiedMask : maskInput.keySet()) {
				String modifiedInput = maskInput.get(modifiedMask);
				screeningField.clear();
				screeningField.click();
				screeningField.sendKeys(modifiedInput);
				UiHelper.fluentWaitForCssValue(screeningField, "ng-valid-mask-pattern", 10);
				String dataEntered = screeningField.getAttribute("value");
				HardVerify.True(Required.isMaskValid(screeningField),
						"Verifyng screening# field is not required after entering data: [" + modifiedInput + "]",
						"[PASSED]", "[FAILED]");
				HardVerify.Equals(dataEntered, modifiedMask, "Verifyng screening# field matches with entered data",
						"[PASSED]", "Expected: [" + modifiedMask + "]. Found: [" + dataEntered + "] [FAILED]");
			}
		} else {
			throw new SkipException("Screening field not clickable within timeout. Skipping tests...");
		}
		Log.logDebugMessage("Verified masks: [" + ++currentMaskNo + "/" + results.size() + "]");
	}
	
	private List<Object[]> results = new ArrayList<Object[]>();
	private List<String> dataList = new ArrayList<String>();
	int currentMaskNo = 0;

	public List<Object[]> generateMask(List<String> characterList, int len) {
		generatePermutations(new ArrayList<String>(), characterList, len);
		for (String data : dataList) {
			m = p.matcher(data);
			if (m.find()) {
				Log.logDebugMessage("Added mask for dataprovider: [" + data + "]");
				results.add(new Object[] { data });
			}
		}
		Log.logDebugMessage("Generated total masks: [" + results.size() + "]");
		return results;
	}
	
	public List<Object[]> generateSiteData(List<String> characterList, int len) {
		final String sitePattern = "^[a-zA-Z0-9]*$";
		Pattern p = Pattern.compile(sitePattern);
		generatePermutations(new ArrayList<String>(), characterList, len);
		for (String data : dataList) {
			m = p.matcher(data);
			if (m.find()) {
				Log.logDebugMessage("Added site number for dataprovider: [" + data + "]");
				results.add(new Object[] { data });
			}
		}
		Log.logDebugMessage("Generated total site numbers: [" + results.size() + "]");
		return results;
	}

	/*private void generatePermutations(List<String> stringList, String result, int len) {
		if (result.length() == len) {
			Log.logDebugMessage("Data generated: [" + result + "]");
			dataList.add(result);
			return;
		}
		for (int i = 0; i < stringList.size(); i++) {
			String perm = new StringBuilder().append(result).append(stringList.get(i)).toString();
			generatePermutations(stringList, perm, len);
		}
	}*/
	
	/**
	 * New improved version
	 * 
	 * @param characterList
	 * @param len
	 * @return
	 */
	private void generatePermutations(List<String> prefixList, List<String> postfixList, int len) {
		if (prefixList.size() == len) {
			StringBuilder perm = new StringBuilder();
			for (String prefix : prefixList) {
				perm.append(prefix);
			}
			Log.logDebugMessage("Data generated: [" + perm.toString() + "]");
			dataList.add(perm.toString());
			return;
		}

		for (int i = 0; i < postfixList.size(); i++) {
			List<String> prefix = new ArrayList<String>(prefixList);
			prefix.add(postfixList.get(i));
			List<String> postfix = new ArrayList<String>(postfixList);
			postfix.remove(i);
			generatePermutations(prefix, postfix, len);
		}
	}

	public void openNewSubject(String site ) {
		newSubject = subjectTable.openNewSubjectForm(site);
	}
	
	public void verifySubjectFieldValidCharacters(String[] dataList, int validStudyCharCount) {
		int validSubjetCharCount = checkString(dataList, newSubject.getInputByName("Screening#"));
		HardVerify.Equals(validStudyCharCount, validSubjetCharCount,
				"Verifying valid character count matches on both Study custom & Subject details page", "[PASSED]",
				"Expected: [" + validStudyCharCount + "] Actual: [" + validSubjetCharCount + "] [FAILED]");
	}

	public void verifySitedata(String siteNumber) {
		Actions builder = new Actions(Browser.getDriver());
		sites = PageFactory.initElements(Browser.getDriver(), Sites.class);
		WebElement siteLabel = sites.getSiteLabel();
		WebElement siteInput = sites.getSiteInput();
		if (siteLabel.isDisplayed()) {
			builder.click(siteLabel).perform();
			siteInput.click();
			siteInput.clear();
			siteInput.sendKeys(siteNumber);
			HardVerify.True(Required.isInputValid(siteInput), "Verifying site number: [" + siteNumber + "] is valid",
					"[PASSED]", "[FAILED]");
//			siteInput.clear();
//			sites.getCancelButton().click();
		} else {
			throw new SkipException("Couldn't click site input field. Skipping tests...");
		}
	}
	
	public void saveSiteId(String id) {
		UiHelper.click(sites.getSaveButton());
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
	}
}
