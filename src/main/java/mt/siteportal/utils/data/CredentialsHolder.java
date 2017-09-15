package mt.siteportal.utils.data;

import mt.siteportal.utils.helpers.ExcelDataReader;

public class CredentialsHolder {
	private static CredentialsHolder userCredentials;
	private ExcelDataReader credentialsFile;

	private CredentialsHolder() {
		String filename = "Credentials.xlsx";
		credentialsFile = new ExcelDataReader(filename);
	}

	public static CredentialsHolder get() {
		if (userCredentials == null)
			userCredentials = new CredentialsHolder();
		return userCredentials;
	}

	public String usernameForUserType(String userType) {
		return credentialsFile.fromSheet("Credentials_" + DataHolder.getHolder().getEnvironmentName())
				.getDataAsStringFor(userType, "Username");
	}

	public String passwordForUserType(String userType) {
		return credentialsFile.fromSheet("Credentials_" + DataHolder.getHolder().getEnvironmentName())
				.getDataAsStringFor(userType, "Password");
	}
}
