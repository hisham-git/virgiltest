package tests.People;

import mt.siteportal.pages.Administration.people.GeneralPeopleTab;
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

public class AddPersonTest extends AbstractTest{
		
		/*private String firstName = "MaxAuto";
		private String lastName = "Tkachov";
		
		
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
			administration.openPeople();
			GeneralPeopleTab generalPeople = administration.peoplePage.addPerson(firstName, lastName);
			Verify.Equals(firstName+" "+lastName, generalPeople.getDetails().get("Name"), "the name is inoccrect");
			
}
		*/
}

