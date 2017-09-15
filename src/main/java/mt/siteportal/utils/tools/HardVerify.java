package mt.siteportal.utils.tools;

import mt.siteportal.utils.Browser;

import org.testng.Assert;

/**
 * A set of "hard" asserts - wrappers around TestNG's Assert methods used mainly for logging.
 * HardVerify method WILL stop test execution on verification failure.
 * Also, need this layer to separate specific UnitTest framework from tests themselves.
 * TODO: Add Asserts, Verify.False etc.
 */
public class HardVerify {

	/**
	 * Compares the expected and actual. Logs the messageOnFail on verification failure.
	 * @param expected - expected condition;
	 * @param actual - actual condition;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void Equals(Object expected, Object actual, String preAssertMessage, String messageOnPass,
			String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertEquals(actual, expected, messageOnFail);
			Log.logPass(messageOnPass);
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Compares the expected and actual. Logs the messageOnFail on verification failure.
	 * @param expected - expected condition;
	 * @param actual - actual condition;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void Equals(Object expected, Object actual, String messageOnFail) {
		try {
			Assert.assertEquals(actual, expected, messageOnFail);
			Log.logPass("Passed");
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be True. Logs the messageOnFail on verification failure.
	 * @param condition - condition to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void True(boolean condition, String preAssertMessage, String messageOnPass, String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertTrue(condition, messageOnFail);
			Log.logPass(messageOnPass);
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be True. Logs the messageOnFail on verification failure.
	 * @param condition - condition to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void True(boolean condition, String messageOnFail) {
		try {
			Assert.assertTrue(condition, messageOnFail);
			Log.logPass("Passed");
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be False. Logs the messageOnFail on verification failure.
	 * @param condition - condition to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void False(boolean condition, String preAssertMessage, String messageOnPass, String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertFalse(condition, messageOnFail);
			Log.logPass(messageOnPass);
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be False. Logs the messageOnFail on verification failure.
	 * @param condition - condition to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void False(boolean condition, String messageOnFail) {
		try {
			Assert.assertFalse(condition, messageOnFail);
			Log.logPass("Passed");
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be Not Null. Logs the messageOnFail on verification failure.
	 * @param obj - object to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static boolean NotNull(Object obj, String preAssertMessage, String messageOnPass, String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertNotNull(obj, messageOnFail);
			Log.logPass(messageOnPass);
			return true;
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	

	/**
	 * Check the specified condition to be Not Null. Logs the messageOnFail on verification failure.
	 * @param obj - object to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void NotNull(Object obj, String messageOnFail) {
		try {
			Assert.assertNotNull(obj, messageOnFail);
			Log.logPass("Passed");
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be Null. Logs the messageOnFail on verification failure.
	 * @param obj - object to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static boolean Null(Object obj, String preAssertMessage, String messageOnPass, String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertNull(obj, messageOnFail);
			Log.logPass(messageOnPass);
			return true;
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified condition to be Null. Logs the messageOnFail on verification failure.
	 * @param obj - object to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void Null(Object obj, String messageOnFail) {
		try {
			Assert.assertNull(obj, messageOnFail);
			Log.logPass("Passed");
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
    /**
     * Verifies whether current URL contains the expected ones.
     * "Contains" is used since current URL may contain additional entries like session ID etc.
     *
     * @param expectedURL - expected sub-URL.
     * @param messageOnFail - message to be logged on verification failure.
     */
    public static void isCurrentUrlContainsExpected(String expectedURL, String preAssertMessage, String messageOnPass, String messageOnFail){
        String actualURL = Browser.getCurrentUrl();
        String msgOnFail = String.format("Unexpected URL. Current URL(%s) was expected to contain (%s) | %s", actualURL, expectedURL, messageOnFail);

        True(actualURL.contains(expectedURL), preAssertMessage, messageOnPass, msgOnFail);
    }

    /**
	 * Check the specified Objects are not Equal. Logs the messageOnFail on verification failure.
	 * @param expected - expected condition;
	 * @param actual - actual condition;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void NotEquals(Object expected, Object actual, String preAssertMessage, String messageOnPass,
			String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertNotEquals(actual, expected, messageOnFail);
			Log.logPass(messageOnPass);
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}

	/**
	 * Check the specified Objects are not Equal. Logs the messageOnFail on
	 * verification failure.
	 * 
	 * @param expected
	 *            - expected condition;
	 * @param actual
	 *            - actual condition;
	 * @param messageOnFail
	 *            - the message to be logged on verification failure.
	 */
	public static void NotEquals(Object expected, Object actual, String messageOnFail) {
		try {
			Assert.assertNotEquals(actual, expected, messageOnFail);
			Log.logPass("Passed");
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
	
	/**
	 * Check the specified Strings are Equal by ignoring case. Logs the messageOnFail on
	 * verification failure.
	 * 
	 * @param expected
	 *            - expected condition;
	 * @param actual
	 *            - actual condition;
	 * @param messageOnFail
	 *            - the message to be logged on verification failure.
	 */
	public static void EqualsIgnoreCase(String actual, String expected, String preAssertMessage, String messageOnPass,
			String messageOnFail) {
		try {
			Log.logVerify(preAssertMessage);
			Assert.assertTrue(actual.equalsIgnoreCase(expected), messageOnFail);
			Log.logPass(messageOnPass);
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			Log.logFail(messageOnFail);
			throw new AssertionError(messageOnFail, err);
		}
	}
}