package com.vmware.talentboost.networkofgiving.services;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;

import java.util.List;

public class CharityService implements ICharityService {
    @Override
    public List<Charity> getAllCharity() {
        return null;
    }

    @Override
    public Charity getCharity(String title) {
        return null;
    }

    @Override
    public void addCharity(Charity charity) {

    }

    @Override
    public void updateCharity(String title, Charity charity) {

    }

    @Override
    public void deleteCharity(String charity) {

    }

    @Override
    public List<User> getParticipantsForCharity(String title) {
        return null;
    }

    @Override
    public List<User> getDonatorsForCharity(String title) {
        return null;
    }
}
