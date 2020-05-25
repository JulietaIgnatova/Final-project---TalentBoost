package com.vmware.talentboost.networkofgiving.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class Charity {
    private int id;
    private int creatorId;

    @NotBlank
    @Length(max = 128)
    private String title;

    private String description;
    private double budgetRequired;
    private int volunteersSignedUp;
    private double amountCollected;
    private int volunteersRequired;

    public Charity() {
    }

    public Charity(int id, int creatorId, @NotBlank @Length(max = 128) String title,
                   String description, double budgetRequired, int volunteersSignedUp,
                   double amountCollected, int volunteersRequired) {
        this.id = id;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.budgetRequired = budgetRequired;
        this.volunteersSignedUp = volunteersSignedUp;
        this.amountCollected = amountCollected;
        this.volunteersRequired = volunteersRequired;
    }

    public int getVolunteersSignedUp() {
        return volunteersSignedUp;
    }

    public void setVolunteersSignedUp(int volunteersSignedUp) {
        this.volunteersSignedUp = volunteersSignedUp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudgetRequired() {
        return budgetRequired;
    }

    public void setBudgetRequired(double budgetRequired) {
        this.budgetRequired = budgetRequired;
    }

    public double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(double amountCollected) {
        this.amountCollected = amountCollected;
    }

    public int getVolunteersRequired() {
        return volunteersRequired;
    }

    public void setVolunteersRequired(int volunteersRequired) {
        this.volunteersRequired = volunteersRequired;
    }

    public void addVoluteer() {
        volunteersSignedUp +=1;
    }
}
