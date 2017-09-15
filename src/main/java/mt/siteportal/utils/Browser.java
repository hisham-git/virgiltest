package mt.siteportal.utils;

import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.*;

import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

/**
 * A Singlenton container for WebDriver object.</br>
 * http://en.wikipedia.org/wiki/Singleton_pattern
 */
public class Browser {
   
    private static WebDriver driver;
    //private static Browser browser;

    public Browser(String browser){
        //TODO: use .properties to specify what browser to use.
    	
    	switch (browser) {
    	/*
		// code for selenium grid
		
		System.setProperty("webdriver.chrome.driver", "D:\\Projects\\enterprisetest\\branches\\SmokeTest-1.0.1\\drivers\\chrome\\chromedriver_win.exe"); 
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		 
		try { 
			driver = new RemoteWebDriver(new URL("http://10.10.19.68:4444/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}*/

		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver", "drivers\\ie\\IEDriverServer_win32.exe");
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver(caps);
			break;
		default:
			System.setProperty("webdriver.chrome.driver", "drivers\\chrome\\chromedriver_win_v2.28.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			driver = new ChromeDriver(options);
			break;
		}

		// Set timeouts to 25 seconds. //TODO: specify timeouts in the
		// .properties file for easy deploying.
		int timeout = 30;
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
		
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double initialWidth = screenSize.getWidth();
		Double initialheight = screenSize.getHeight();
		String windowRatio = String.format("%.2f", (initialWidth.floatValue() / initialheight.floatValue()));
		Log.logSuiteInfo("Current Window resolution: " + initialWidth +" x " + initialheight);
		Log.logSuiteInfo("Current Window ratio: " + windowRatio);

		if (Double.parseDouble(windowRatio) >= 1.77d) {
//			Double newWidth = initialWidth * (0.667);
			Double newWidth = initialWidth * (0.727);
			Double newHeight = initialheight * (0.972);
			driver.manage().window().setPosition(new Point(0, 0));
			driver.manage().window().setSize(new Dimension(newWidth.intValue(), newHeight.intValue()));
		} else {
			driver.manage().window().setPosition(new Point(0, 0));
			driver.manage().window().maximize();
//			driver.manage().window().setSize(new Dimension(1280, 1050));
//			driver.manage().window().setSize(new Dimension(1920, 850));
		}
	}

   /* public static Browser getBrowser() {
        if (null == browser){
            Log.logInfo("Initializing new browser.");
            browser = new Browser();
        }
        return browser;
    }
*/
    /**
     * Gets the "driver" object. Not the best idea to expose WebDriver object here, but need this to illustrate how PageFactory pattern works.
     *
     * @return the "driver" object.
     */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Close the browser.
     *
     */
    public void quit() {
        Log.logInfo("Closing the browser.");
        getDriver().quit();
    }
    
    /*public void close() {
        Log.logInfo("Closing the browser.");
        getDriver().close();
    }*/

    /**
     * Gets URL of the current page.
     *
     * @return URL of the current page.
     */
    public static String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    /**
     * Gets title of the current page.
     *
     * @return title of the current page.
     */
    public static String getPageTitle(){
        return driver.getTitle();
    }
    
    //----------------------------------------------------------NZ TEAM-------------------------------------------------------------------------------//
    /**
     * Restores the Default Timeouts
     */
    public static void resetTimeOuts(){
    	//TODO: specify timeouts in the .properties file for easy deploying.
    	//Set timeouts to 25 seconds. 
        int timeout = 15;
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
    }
}