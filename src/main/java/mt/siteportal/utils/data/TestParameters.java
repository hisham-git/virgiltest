package mt.siteportal.utils.data;

import mt.siteportal.utils.helpers.ExcelDataReader;

public class TestParameters {
	private static TestParameters testParameters;
	private ExcelDataReader parametersFile;

	private TestParameters() {
		String filename = "TestParameters.xlsx";
		parametersFile = new ExcelDataReader(filename);
	}

	public static TestParameters get() {
		if (testParameters == null)
			testParameters = new TestParameters();
		return testParameters;
	}

	private ExcelDataReader forTest(String testName){
		return parametersFile.fromSheet(testName);
	}
	
	public String getStringParameter(String testName, String parameter){
		return forTest(testName).getDataAsStringFor(parameter, DataHolder.getHolder().getEnvironmentName());
	}
	
}
