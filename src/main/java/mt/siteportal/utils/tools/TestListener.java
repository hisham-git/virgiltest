package mt.siteportal.utils.tools;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Primitive test listener - an illustration of how TestNG reporting/logging sub-system could be extended.
 * See http://testng.org/doc/documentation-main.html#test-results for more detail
 */
public class TestListener extends TestListenerAdapter {
	  private int m_count = 0;
	 
	  @Override
	  public void onTestFailure(ITestResult tr) {
	    log("FAILED");
	  }
	 
	  @Override
	  public void onTestSkipped(ITestResult tr) {
	    log("SKIPPED");
	  }
	 
	  @Override
	  public void onTestSuccess(ITestResult tr) {
	    log("PASSED!!!");
	  }	  	  
	 
	  private void log(String string) {
	    System.out.print(string);
	    if (++m_count % 40 == 0) {
	      System.out.println("");
	    }
	  }
}