package mt.siteportal.utils.Enums;

/**
 * Created by Abdullah Al Hisham on 08/23/2016.
 */
public enum MAUserType {
	MAUSERTYPE1("MedAvante User Type 1"),
	MAUSERTYPE2("MedAvante User Type 2"),
	MAUSERTYPE3("MedAvante User Type 3"),
	MAUSERTYPE4("MedAvante User Type 4");

    private String userTypevalue;

    MAUserType(String value) {
        this.userTypevalue = value;
    }

    public String getValue(){
        return userTypevalue;
    }
}
