package mt.siteportal.utils.Enums;

/**
 * Created by Kirill.Brener on 7/11/2016.
 */
public enum SubjectDetailsFields {
    SCREENING("Screening#"),
    TEMPID("TemporaryID"),
    STATUS("Status"),
    LANGUAGE("Language"),
    CONSENT("Consent To Record"),
    DISABLEPRO("Disable PRO"),
    DISABLEOBSRO("Disable ObsRO"),
    REASONPRO("Reason for disabling PRO"),
    REASONOBSRO("Reason for disabling ObsRO");


    String value;

    SubjectDetailsFields(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
