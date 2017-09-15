package tests.Organization;

import mt.siteportal.objects.Organization;
import mt.siteportal.pages.Administration.organizations.GeneralOrganizationsTab;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import tests.Abstract.AbstractTest;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AddOrganizationTest extends AbstractTest{
	
	private Organization orgToCreate = new Organization("Autotest", "Auto","Vendor");
	
	/*
	@BeforeClass(alwaysRun = true)
	public void loginAsAdmin(){
		Log.logTestClassStart(this.getClass());		
		loginPage = PageFactory.initElements(Browser.getDriver(),
				LoginPage.class);
		Log.logStep("Logining to Medovante portal as admin");
		homePage = loginPage.loginAsAdmin(adminLogin, adminPasword);
		administration = homePage.openAdministration();
	}
		
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestStart(method);
	}
	
	@Test(groups = {""}, description = "SFB-TC-368")
	public void OneSite() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {	
		GeneralOrganizationsTab general = administration.orgainzationsPage.addOrganization(orgToCreate);
		HashMap<String, String> details = general.getDetails();
		Verify.Equals(orgToCreate.getName(), details.get("Name"), "The name is incorrect");
		Verify.Equals(orgToCreate.getAbbreviation(), details.get("Abbreviation"), "The Abbreviation is incorrect");
		Verify.Equals(orgToCreate.getType(), details.get("Type"), "The Type is incorrect");
		
		
		
		
		*//*toolbarFull = PageFactory.initElements(Browser.getDriver(),
				ToolBarFull.class);
		Log.logStep("Choosing Study");
		toolbarFull.chooseStudy("Auto - SubjectStatusLabel");
		if (toolbarFull.getSiteName().equals("All Sites")==false){
			Log.logStep("Verification that Study have one site");
			Verify.Equals(1, toolbarFull.getSiteCount(), "Incorrect count should be 1");
		}else 
			Verify.True(false, "The Study conatins many sites but should not");
		header = PageFactory.initElements(Browser.getDriver(),
				Header.class);
		Log.logStep("user is logout");
		header.logout();*//*
	
	}
*/
}
