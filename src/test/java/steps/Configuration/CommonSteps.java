package steps.Configuration;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.About;
import mt.siteportal.pages.Footer;
import mt.siteportal.pages.Header;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Random;

/**
 * Created by maksym.tkachov on 6/23/2016.
 */
public class CommonSteps extends AbstractStep {

    public CommonSteps(){
        about = PageFactory.initElements(Browser.getDriver(), About.class);
        toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
        footer = PageFactory.initElements(Browser.getDriver(), Footer.class);
        header = PageFactory.initElements(Browser.getDriver(), Header.class);
        homePage = PageFactory.initElements(Browser.getDriver(),
                HomePage.class);
        dashboard = PageFactory.initElements(Browser.getDriver(),
                Dashboard.class);
    }
    
	public static String generateRandomNames(int len) {
		final char[] idchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		char[] id = new char[len];
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < len; i++) {
			id[i] = idchars[r.nextInt(idchars.length)];
		}
		return new String(id);
	}
    
    public void openAboutPage(){
        Nav.toURL(URLsHolder.getHolder().getPageAbout());
        Log.logStep("Verification of About page opened");
        Verify.True(about.isAboutOpened(), "The current Page is not about");
    }
    public void aboutMedAvanteContentVerification(String expectedContent) {
        Log.logStep("Verification of content on AboutMedavante page");
        Verify.Equals(expectedContent, about.getAboutMedAvanteContent(), "The about medavante text is incorrect");
    }

    public void legalNoticeContentVerification(String expectedContent) {
        Log.logStep("Verification of content on LegalNotice page");
        Verify.Equals(expectedContent, about.getLegalNoticeContent(), "The legal notice text is incorrect");
    }

    public void copyrightVerification(String expectedCopyright) {
        Log.logStep("Verification of content on LegalNotice page");
        Verify.Equals(expectedCopyright, about.getCopyrightContent(), "The copyright text is incorrect");
    }

    public void navigationToMedAvanteVerification(String expectedUrl) {
        Log.logStep("Verification navigation to Medavante");
        Verify.Equals(expectedUrl, about.openMedAvante(), "The current page is not medavante.com");;
    }

    public void navigationToPrivacyVerification(String expectedUrl) {
        Log.logStep("Verification navigation to Privacy");
        Verify.Equals(expectedUrl, about.openPrivacy(), "The current page is not medavante.com/privacy");;
    }

    public void oneEntityInSiteMenuVerification(){
        if (toolbarFull.getSiteName().equals("All Sites")==false){
            Log.logVerify("Verification that Study have one site");
            Verify.Equals(1, toolbarFull.getSiteCount(), "Incorrect count should be 1");
        }else
            Verify.True(false, "The Study contains many sites but should not");
    }

    public void overOneEntityInSiteMenuVerification(){
        Log.logVerify("Verification that All sites is displayed");
        Verify.Equals("All Sites", toolbarFull.getSiteName(), "the default Sites should be 'All Sites' for current Study");
        Log.logVerify("Verification that Study have more then site");
        Verify.True(toolbarFull.getSiteCount()>1, "the default Sites should be 'All Sites' for current Study");
    }

    public void footerHomeButtonActionVerification(String userType){
        if (userType.equals(Constants.ADMIN)){
            homePage = footer.openHomePage();
            Log.logVerify("Home button opens HomePage");
            Verify.True(homePage.isHomePageOpened(),
                    "The Home button is not return the home page");
        }
        else if(userType.equals(Constants.USER)){
            dashboard = footer.openDashboard();
            Log.logVerify("Home button opens Dashboard");
            Verify.True(dashboard.isDashboardOpened(),
                    "The Home button is not return the Dashboard");
        }
        else {
            Log.logError("The incorrect parameter inserted");
        }
    }

    public void footerAboutButtonActionVerification(){
        Log.logStep("Opening About with application menu");
        about = footer.openAbout();
        Log.logVerify("About button opens About Page");
        Verify.True(about.isAboutOpened(),
                "The About button is not return the About page");
    }
    public void footerApplicationMenuAdministrationButton() {
        Log.logStep("Opening Administration with application menu");
        administration = footer.openAdministration();
        Log.logVerify("Administration button opens AdministrationPage");
        Verify.True(administration.isAdministrationOpened(),
                "The Adminstration button is not return the Admin page");
    }
    public void footerLogoutButtonActionVerification(){
        loginPage = footer.openLoginPage();
        Log.logVerify("Login Page is displayed");
        Verify.True(loginPage.isLoginPageOpened(),
                "The LogOut button is not return the Login page");
    }

    public void headerVirgilLogoActionVerification(String userType){
        if (userType.equals(Constants.ADMIN)){
            homePage = header.openHomePageWithLogo();
            Log.logVerify("Logo button opens HomePage");
            Verify.True(homePage.isHomePageOpened(),
                    "The Logo is not return the home page");
        }
        else if(userType.equals(Constants.USER)){
            dashboard = header.openDashboardWithLogo();
            Log.logVerify("Logo button opens Dashboard");
            Verify.True(dashboard.isDashboardOpened(),
                    "The Logo is not return the Dashboard");
        }
        else {
            Log.logError("The incorrect parameter inserted");
        }
    }

    public void headerHomeButtonActionVerification(String userType){
        if (userType.equals(Constants.ADMIN)){
            homePage = header.openHomePage();
            Log.logVerify("Home button opens HomePage");
            Verify.True(homePage.isHomePageOpened(),
                    "The Home button is not return the home page");
        }
        else if(userType.equals(Constants.USER)){
            dashboard = header.openDashboard();
            Log.logVerify("Home button opens Dashboard");
            Verify.True(dashboard.isDashboardOpened(),
                    "The Home button is not return the Dashboard");
        }
        else {
            Log.logError("The incorrect parameter inserted");
        }
    }

    public void headerAboutButtonActionVerification(){
        about = header.openAbout();
        Log.logVerify("About button opens About Page");
        Verify.True(about.isAboutOpened(),
                "The About button is not return the About page");
    }
    public void headerAdministrationButtonActionVerification() {
        administration = header.openAdministration();
        Log.logVerify("Administration button opens AdministrationPage");
        Verify.True(administration.isAdministrationOpened(),
                "The Adminstration button is not return the Admin page");
    }
    public void headerLogoutButtonActionVerification(){
        loginPage = header.openLoginPage();
        Log.logVerify("Login Page is displayed");
        Verify.True(loginPage.isLoginPageOpened(),
                "The LogOut button is not return the Login page");
    }

    public void homepageContentAdminVerification(){
        Log.logVerify("Admin is logged in");
        Verify.True(homePage.isHomePageOpened(),
                "There is no WelcomeMessage , probably current page is not Home");
        Log.logVerify("Study dashboard button is displayed on Home page");
        Verify.True(UiHelper.isPresent(homePage.studyDashboard),
                "The Study Dashboard is not displayed");
        Log.logVerify("Administration button is displayed on Home page");
        Verify.True(UiHelper.isPresent(homePage.administration),
                "The Administration is not displayed");
    }

    public void homepageContentUserVerification(){
        Log.logVerify("User is logged in");
        Verify.True(dashboard.isDashboardOpened(),
                "There is no Dashboard message , probably current page is not Dashboard");
        List<String> headerMenuContent = header.getAplicationMenuContent();
        Log.logVerify("User doesn't have access to administration from header");
        Verify.False(headerMenuContent.contains("ADMINISTRATION"),
                "The  Administration is displayed in the header menu, but should not");
        List<String> footerMenuContent = footer.getAplicationMenuContent();
        Log.logVerify("User doesn't have access to administration from footer");
        Verify.False(footerMenuContent.contains("ADMINISTRATION"),
                "The  Administration is displayed in the footer menu, but should not");
    }

    /**
     * Navigates to the Study Dashboard
     * 
     * @return boolean
     */
	public boolean getToStudyDashboard() {
		if (dashboard.isDashboardOpened())
			return true;
		if (!homePage.isHomePageOpened())
			openHomePage();
		try {
			if (!dashboard.isDashboardOpened())
				homePage.openDashboard();
		} catch (NoSuchElementException nse) {
			// DO NOTHING
		}
		return dashboard.isDashboardOpened();
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Overloaded method for quick navigation to dashboard via study link from breadcrumbs
	 * 
	 * @return boolan
	 * 		- true if Dashboard opened, false otherwise
	 */
	public boolean getToStudyDashboardViaStudyLink() {
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		if (null != toolbarFull.returnToDashboard()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Navigates to the given url
	 * 
	 * @param url - String - must contain https://
	 */
	public void navigateToURL(String url) {
		Nav.toURL(url);
	}
	
	/**
	 * @author HISHAM
	 * @param Filter
	 * @return
	 */
	public boolean getAssessmentsListFor(String filter) {
		/*
		 * Disabled due to slowness
		 * assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		 * if (assessmentList.isOpened()) {
		 * return true;
		 * }*/
		
		if (getToStudyDashboard()) {
			dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
			dashboardList = dashboard.clicksOnCard("Assessments-" + filter);
		}

		if (dashboardList.isListOpen()) {
			String headerName = dashboardList.getListTypeNameWithFilter();
			if ( (headerName.equals("Assessments: " + filter)) || (headerName.equals("Assessments: All")) ) {
				Log.logInfo("The Header text is " + dashboardList.getListTypeNameWithFilter()
						+ ", indicating that this is Assessments: " + filter + " page.");
				return true;
			} else {
				Log.logInfo("The Header text is " + dashboardList.getListTypeNameWithFilter()
				+ ", indicating that this is not Assessments: " + filter + " page.");
				return false;
			}
		}
		Log.logInfo("The Assessments: " + filter + " List is not Opened");
		return false;
	}
	
	public boolean clickStudyLinkFromBreadcrumbs() {
		toolbarFull.clickStudyLinkFromBreadcrumbs();
		return dashboard.isDashboardOpened();
	}
	
	/**
	 * @author ubiswas
	 * 
	 * Description: Privacy policy action from header application menu.
	 */
	public void headerPrivacyPolicyActionVerification() {
		header.headerPrivacyPolicyActionVerification();
	}
	
	/**
	 * @author ubiswas
	 * 
	 * Description: Privacy policy action from footer application menu.
	 */
	public void footerPrivacyPolicyActionVerification() {
		footer.footerPrivacyPolicyActionVerification();
	}
}
