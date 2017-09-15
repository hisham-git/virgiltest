package mt.siteportal.utils.helpers;

import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.DataHolder;

/**
 * A helper class, contains methods like goToSpecificPage().
 */
public class Nav {
    private static final Logger logger = LoggerFactory.getLogger(Nav.class);

    /**
     * Navigates to the URL specified.
     *
     * @param url - URL to navigate to.
     */
    public static void toURL(String url){
        logger.debug("Navigating to " + url);
        Browser.getDriver().get(url);
    }

    /**
     * Performs logging in as an admin.
     * Note: it is supposed that current page is Login(Index) page.
     *
     * TODO: Add check for currentPage=LoginPage, Nav to Login if not.
     * @return HomePage object.
     */
    public static HomePage loginAsAdmin() {
        logger.debug("Logging in as admin.");
        LoginPage loginPage = PageFactory.initElements(Browser.getDriver(), LoginPage.class);

        return loginPage.loginAsAdmin(DataHolder.getHolder().getAdminName(), DataHolder.getHolder().getAdminAccountPassword());
    }    
    
}