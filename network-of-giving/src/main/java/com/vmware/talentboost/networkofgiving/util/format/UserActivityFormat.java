package com.vmware.talentboost.networkofgiving.util.format;

public class UserActivityFormat {

    public static final String DONATE_ACTION = "Donate $%.2f to a charity '%s'.";
    public static final String PARTICIPATE_ACTION = "Signed up for a charity '%s'.";
    public static final String CREATED_ACTION = "Created a charity '%s'.";
    public static final String DELETED_ACTION = "Deleted a charity '%s'.";
    public static final String GIVE_MONEY_BACK = "Deleted Charity: $%.2f have been returned to you from '%s'.";
    public static final String NOTIFY_PARTICIPANTS = "Deleted Charity: You are no longer participant in '%s'.";
    public static final String COLLECTED_ALL_MONEY = "Charity '%s' collected all the money required.";
    public static final String GET_ALL_VOLUNTEERS = "Charity '%s' gathered all volunteers needed.";

    private UserActivityFormat() {
    }
}
