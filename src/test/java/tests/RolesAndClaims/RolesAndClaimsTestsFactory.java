package tests.RolesAndClaims;

import java.util.Map;

import org.testng.annotations.Factory;

import nz.siteportal.utils.dataProvider.ExcelDataMapper;

/**
 * Test Class Factory for running the same class with multiple users
 * Runs all the Roles and Claims classes with specific users
 *  
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
public class RolesAndClaimsTestsFactory {

	/**
	 * Creates the instances of the Test Classes by taking in different users
	 * from a DataProvider method
	 * 
	 * @param userType
	 *            - String - the username for which to create the Test Classes
	 * @return - Object[] of test class instances
	 */
	@Factory(dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public Object[] StudyDashboardClaimsTest(Map<String, String> userData) {
		return new Object[] { new StudyDashboardClaimsTest(userData) };
	}
	
	/*
	@Factory(dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public Object[] AccessClaimsTest(Map<String, String> userData) {
		return new Object[] { new AccessClaimsTest(userData) };
	}
*/
	/**
	 * Data Provider. Returns a list of User Types for usage in the Roles and Claims Matrix.
	 * Note that the User Type String provided here must match EXACTLY with the text/string in the ExcelSheet
	 * 
	 * @return Object[][] - Array of Objects of the format {{"STRING_1"}, {"STRING_2"},.....}
	 *//*
	@DataProvider(name = "userTypes")
	public static Object[][] userTypes() {
		Object[][] dataArray = { { "Site Rater - Type 2" } };
		return dataArray;
	}*/
}