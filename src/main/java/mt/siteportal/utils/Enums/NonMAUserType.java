package mt.siteportal.utils.Enums;

/**
 * Created by Abdullah Al Hisham on 08/23/2016.
 */
public enum NonMAUserType {
	SITEUSERTYPE0("Site Coordinator"),
	SITEUSERTYPE1("Site Rater - Type 1"),
	SITEUSERTYPE2("Site Rater - Type 2"),
	SITEUSERTYPE3("Site Rater - Type 3"),
	SITEUSERTYPE4("Site Rater - Type 4"),
	SITEUSERTYPE5("Site Rater - Type 5"),
	SPONSORUSERTYPE1("Sponsor User Type 1"),
	SPONSORUSERTYPE2("Sponsor User Type 2"),
	SPONSORUSERTYPE3("Sponsor User Type 3");

    private String userTypevalue;

    NonMAUserType(String value) {
        this.userTypevalue = value;
    }

    public String getValue(){
        return userTypevalue;
    }
}
