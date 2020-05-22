package com.vmware.talentboost.networkofgiving.services;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;

import java.util.List;

public interface ICharityService {
    List<Charity> getAllCharity();
    Charity getCharity(String title);

    void addCharity(Charity charity);

    void updateCharity(String title, Charity charity);

    void deleteCharity(String charity);

    List<User> getParticipantsForCharity(String title);

    List<User> getDonatorsForCharity(String title);



}
