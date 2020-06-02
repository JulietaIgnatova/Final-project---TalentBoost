package com.vmware.talentboost.networkofgiving.models;

import java.time.OffsetDateTime;

public class UserAction {
    private int userId;
    private String description;
    private OffsetDateTime actionDate;
    private String formattedActionDate;
    private String charityTitle;

    public UserAction() {
    }

    public UserAction(int userId, String description, String charityTitle) {
        this.userId = userId;
        this.description = description;
        this.charityTitle = charityTitle;
    }

    public String getCharityTitle() {
        return charityTitle;
    }

    public void setCharitTitle(String charityTitle) {
        this.charityTitle = charityTitle;
    }

    public String getFormattedActionDate() {
        return formattedActionDate;
    }

    public void setFormattedActionDate(String formattedActionDate) {
        this.formattedActionDate = formattedActionDate;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(OffsetDateTime actionDate) {
        this.actionDate = actionDate;
    }


}
