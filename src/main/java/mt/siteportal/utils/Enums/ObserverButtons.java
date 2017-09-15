package mt.siteportal.utils.Enums;


public enum ObserverButtons {
    DELETE("delete"),
    EDIT("edit"),
    REMOVE("remove"),
    CANCEL("cancel"),
    SAVE("save");

    String value;

    ObserverButtons(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
