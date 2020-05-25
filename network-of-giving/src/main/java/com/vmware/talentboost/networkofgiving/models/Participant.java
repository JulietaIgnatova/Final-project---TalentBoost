package com.vmware.talentboost.networkofgiving.models;

public class Participant {
    private int userId;
    private int charityId;

    public int getCharityId() {
        return charityId;
    }

    public void setCharityId(int charityId) {
        this.charityId = charityId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
