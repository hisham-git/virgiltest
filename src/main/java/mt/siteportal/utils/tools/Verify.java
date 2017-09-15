package mt.siteportal.utils.tools;

import mt.siteportal.utils.Browser;

import org.testng.Assert;

/**
 * A set of "soft" asserts - wrappers around TestNG's Assert methods.
 * Verify method will not stop test execution on verification failure.
 * Also, need this layer to separate specific UnitTest framework from tests themselves.
 * TODO: Add Asserts, Verify.False etc.
 */
public class Verify {

	/**
	 * Compares the expected and actual. Logs the messageOnFail on verification failure.
	 * @param expected - expected condition;
	 * @param actual - actual condition;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static void Equals(Object expected, Object actual, String messageOnFail){
        try {
			Assert.assertEquals(
					actual,
					expected,
					messageOnFail);
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
		}		
	}
	
	/**
	 * Check the specified condition to be True. Logs the messageOnFail on verification failure.
	 * @param condition - condition to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static boolean True(boolean condition, String messageOnFail){
        try {
			Assert.assertTrue(
					condition,
					messageOnFail);
			return true;
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			return false;
		}		
	}
	
	/**
	 * Check the specified condition to be False. Logs the messageOnFail on verification failure.
	 * @param condition - condition to check;
	 * @param messageOnFail - the message to be logged on verification failure.
	 */
	public static boolean False(boolean condition, String messageOnFail){
        try {
			Assert.assertFalse(
					condition,
					messageOnFail);
			return true;
		} catch (AssertionError err) {
			Log.logVerificationError(messageOnFail, err);
			return false;
		}		
	}

    /**
     * Verifies whether current URL contains the expected ones.
     * "Contains" is used since current URL may contain additional entries like session ID etc.
     *
     * @param expectedURL - expected sub-URL.
     * @param messageOnFail - message to be logged on verification failure.
     */
    public static void isCurrentUrlContainsExpected(String expectedURL, String messageOnFail){
        String actualURL = Browser.getCurrentUrl();
        String msgOnFail = String.format("Unexpected URL. Current URL(%s) was expected to contain (%s) | %s", actualURL, expectedURL, messageOnFail);

        True(actualURL.contains(expectedURL), msgOnFail);
    }
    
    /**
   	 * Check the specified condition to be True. Logs the messageOnFail on verification failure.
   	 * @param condition - condition to check;
   	 * @param messageOnFail - the message to be logged on verification failure.
   	 */
       
       public static boolean True(boolean condition, String preAssertMessage, String messageOnPass, String messageOnFail){
           try {
           	Log.logStep(preAssertMessage);
   			Assert.assertTrue(
   					condition,
   					messageOnFail);
   			Log.logInfo(messageOnPass);
   			return true;
   		} catch (AssertionError err) {
   			Log.logVerificationError(messageOnFail, err);
   			return false;
   		}		
   	}
}