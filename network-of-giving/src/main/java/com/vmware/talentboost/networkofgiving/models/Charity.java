package com.vmware.talentboost.networkofgiving.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class Charity {
    private int id;
    private int creator_id;

    @NotBlank
    @Length(max = 128)
    private String title;

    private String description;
    private double budget_required;
    private int volunteers_signed_up;
    private double amount_collected;
    private int volunteers_required;

    public Charity() {
    }

    public Charity(int id, int creator_id, @NotBlank @Length(max = 128) String title,
                   String description, double budget_required, int volunteers_signed_up,
                   double amount_collected, int volunteers_required) {
        this.id = id;
        this.creator_id = creator_id;
        this.title = title;
        this.description = description;
        this.budget_required = budget_required;
        this.volunteers_signed_up = volunteers_signed_up;
        this.amount_collected = amount_collected;
        this.volunteers_required = volunteers_required;
    }

    public int getVolunteers_signed_up() {
        return volunteers_signed_up;
    }

    public void setVolunteers_signed_up(int volunteers_signed_up) {
        this.volunteers_signed_up = volunteers_signed_up;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
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

    public double getBudget_required() {
        return budget_required;
    }

    public void setBudget_required(double budget_required) {
        this.budget_required = budget_required;
    }

    public double getAmount_collected() {
        return amount_collected;
    }

    public void setAmount_collected(double amount_collected) {
        this.amount_collected = amount_collected;
    }

    public int getVolunteers_required() {
        return volunteers_required;
    }

    public void setVolunteers_required(int volunteers_required) {
        this.volunteers_required = volunteers_required;
    }

    public void addVoluteer() {
        volunteers_signed_up +=1;
    }
}
