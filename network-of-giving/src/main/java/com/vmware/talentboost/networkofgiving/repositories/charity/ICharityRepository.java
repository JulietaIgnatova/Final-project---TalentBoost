package com.vmware.talentboost.networkofgiving.repositories.charity;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;

import java.util.List;

public interface ICharityRepository {
    List<Charity> getAllCharities();

    Charity getCharity(String title);

    public boolean checkCharity(String title);

    void addCharity(Charity charity);

    void updateCharity(String title, Charity charity);

    void deleteCharity(String charity);

    List<User> getParticipantsForCharity(String title);

    List<User> getDonatorsForCharity(String title);

    User getCreatorForCharity(String title);
}
