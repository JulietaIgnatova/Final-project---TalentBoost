package com.vmware.talentboost.networkofgiving.models;

public class Participant {
    int user_id;
    int charity_id;

    public int getCharity_id() {
        return charity_id;
    }

    public void setCharity_id(int charity_id) {
        this.charity_id = charity_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
