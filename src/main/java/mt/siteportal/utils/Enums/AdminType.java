package mt.siteportal.utils.Enums;

/**
 * Created by Abdullah Al Hisham on 08/23/2016.
 */
public enum AdminType {
	MAADMINTYPE2("MedAvante Administrator - Type 2"),
	MAADMINTYPE3("MedAvante Administrator - Type 3"),
	MAADMINTYPE4("MedAvante Administrator - Type 4"),
	SYSADMINTYPE1("System Administrator - Type 1"),
	SYSADMINTYPE2("System Administrator - Type 2");

    private String adminTypevalue;

    AdminType(String value) {
        this.adminTypevalue = value;
    }

    public String getValue(){
        return adminTypevalue;
    }
}
