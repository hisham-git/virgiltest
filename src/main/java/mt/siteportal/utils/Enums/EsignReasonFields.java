package mt.siteportal.utils.Enums;

/**
 * Created by Abdullah Al Hisham on 10/25/2016.
 */
public enum EsignReasonFields {
	STATUS("Subject status change"),
	PROTOCOL("Protocol requirement"),
	ERROR("Data entry error"),
	REQUEST("Subject Request"),
	
	/*SPONSOR("Confirmed with Sponsor"),
    CONVENTION("Data Convention"),
    GLOBAL("Global Impression of Subject"),
    SITE("Per Conversation with Site"),
	UNAVAILABLE("Subject unavailable"),*/
	
	REFUSED("Participant refused"),
	FAILURE("Screen failure"),
	IMPAIRED("Participant too impaired"),
	TECHNICAL("Technical difficulties"),
	
	ASSESSMENT("Incorrect assessment assigned"),
	TABLET("Tablet not available"),
	VIRGILFORM("Virgil form unavailable");

    String value;

    EsignReasonFields(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
