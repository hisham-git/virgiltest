package mt.siteportal.utils.Enums;


public enum SortBy {
    RATER("rater"),
    SCHEDULED("scheduled"),
    COMPLETED("completed"),
    EDITED("edited");

    String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
