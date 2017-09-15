package hu.siteportal.pdfreport;

import java.util.Iterator;
import java.util.List;

import org.testng.Assert;

import mt.siteportal.utils.tools.Log;

public class StepVerify extends PdfLog {

	public static void True(boolean condition, String stepMsg, String successMsg, String failMsg) {
		Log.logVerify(stepMsg);
		PdfLog.setStep(stepMsg);
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertTrue(condition, successMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus("Passed");
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus("Failed");
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void False(boolean condition, String stepMsg, String successMsg, String failMsg) {
		PdfLog.setStep(stepMsg);
		PdfLog.setExpected(successMsg);
		try {
			Log.logVerify(stepMsg);
			Assert.assertFalse(condition, successMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus("Passed");
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus("Failed");
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void True(boolean condition, String successMsg, String failMsg) {
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertTrue(condition, successMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus("Passed");
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus("Failed");
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void False(boolean condition, String successMsg, String failMsg) {
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertFalse(condition, successMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus("Passed");
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus("Failed");
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void True(List<Boolean> condition, String stepMsg, List<String> successMsg, List<String> failMsg) {
		boolean passed = false;
		Iterator<Boolean> conditionItr = condition.iterator();
		Iterator<String> successMsgItr = successMsg.iterator();
		Iterator<String> failMsgItr = failMsg.iterator();

		PdfLog.setStep(stepMsg);
		while (conditionItr.hasNext() && successMsgItr.hasNext() && failMsgItr.hasNext()) {
			Boolean conditionFound = conditionItr.next();
			String successMsgFound = successMsgItr.next();
			String failMsgFound = failMsgItr.next();

			PdfLog.setExpected(successMsgFound);
			try {
				Assert.assertTrue(conditionFound, successMsgFound);
				Log.logPass(successMsgFound);
				PdfLog.setActual("- " + successMsgFound);
				passed = true;
//				PdfLog.setStepStatus("Passed");
			} catch (AssertionError err) {
				Log.logFail(failMsgFound);
				PdfLog.setActual("- " + failMsgFound);
//				PdfLog.setStepStatus("Failed");
				throw new AssertionError(failMsgFound, err);
			}
		}
		
		if (passed) {
			PdfLog.setStepStatus("Passed");
		} else {
			PdfLog.setStepStatus("Failed");
		}
	}
	
	/*public static void True(List<Boolean> condition, List<String> successMsg, List<String> failMsg) {
		String success = "", failure = "";
		boolean status = true;

		Iterator<Boolean> conditionItr = condition.iterator();
		Iterator<String> successMsgItr = successMsg.iterator();
		Iterator<String> failMsgItr = failMsg.iterator();

		while (conditionItr.hasNext() && successMsgItr.hasNext() && failMsgItr.hasNext()) {
			Boolean conditionFound = conditionItr.next();
			String successMsgFound = successMsgItr.next();
			String failMsgFound = failMsgItr.next();

			try {
				Assert.assertTrue(conditionFound, successMsgFound);
				Log.logPass(successMsgFound);
				success += "- " + successMsgFound + "\n\n";
			} catch (AssertionError err) {
				Log.logFail(failMsgFound);
				failure += "- " + failMsgFound + "\n\n";
				status = false;
			}
		}
		True(status, success, failure);
	}*/
	
	public static void True(List<Boolean> condition, List<String> successMsg, List<String> failMsg) {
		Iterator<Boolean> conditionItr = condition.iterator();
		Iterator<String> successMsgItr = successMsg.iterator();
		Iterator<String> failMsgItr = failMsg.iterator();
		boolean passed = false;

		while (conditionItr.hasNext() && successMsgItr.hasNext() && failMsgItr.hasNext()) {
			Boolean conditionFound = conditionItr.next();
			String successMsgFound = successMsgItr.next();
			String failMsgFound = failMsgItr.next();

			PdfLog.setExpected(successMsgFound);
			try {
				Assert.assertTrue(conditionFound, successMsgFound);
				Log.logPass(successMsgFound);
				PdfLog.setActual("- " + successMsgFound);
				passed = true;
//				PdfLog.setStepStatus("Passed");
			} catch (AssertionError err) {
				Log.logFail(failMsgFound);
				PdfLog.setActual("- " + failMsgFound);
//				PdfLog.setStepStatus("Failed");
				throw new AssertionError(failMsgFound, err);
			}
		}
		
		if (passed) {
			PdfLog.setStepStatus("Passed");
		} else {
			PdfLog.setStepStatus("Failed");
		}
	}
	
	public static void EqualsIgnoreCase(String actual, String expected, String stepMsg, String successMsg,
			String successStatus, String failMsg, String failStatus) {
		Log.logVerify(stepMsg);
		PdfLog.setStep(stepMsg);
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertTrue(actual.equalsIgnoreCase(expected), successMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus(successStatus);
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus(failStatus);
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void EqualsIgnoreCase(String actual, String expected, String successMsg, String successStatus,
			String failMsg, String failStatus) {
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertTrue(actual.equalsIgnoreCase(expected), successMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus(successStatus);
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus(failStatus);
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void Equals(Object actual, Object expected, String stepMsg, String successMsg, String failMsg) {
		Log.logVerify(stepMsg);
		PdfLog.setStep(stepMsg);
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertEquals(actual, expected, failMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus("Passed");
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus("Failed");
			throw new AssertionError(failMsg, err);
		}
	}
	
	public static void Equals(Object actual, Object expected, String successMsg, String failMsg) {	
		PdfLog.setExpected(successMsg);
		try {
			Assert.assertEquals(actual, expected, failMsg);
			Log.logPass(successMsg);
			PdfLog.setActual(successMsg);
			PdfLog.setStepStatus("Passed");
		} catch (AssertionError err) {
			Log.logFail(failMsg);
			PdfLog.setActual(failMsg);
			PdfLog.setStepStatus("Failed");
			throw new AssertionError(failMsg, err);
		}
	}
}
