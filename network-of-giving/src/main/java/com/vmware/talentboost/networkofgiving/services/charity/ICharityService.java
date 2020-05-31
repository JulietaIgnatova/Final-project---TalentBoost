package com.vmware.talentboost.networkofgiving.services.charity;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;

import java.util.List;

public interface ICharityService {
    List<Charity> getAllCharities();

    Charity getCharity(String title);

    void addCharity(Charity charity);

    void updateCharity(String title, Charity charity);

    void deleteCharity(String title);

    List<User> getParticipantsForCharity(String title);

    List<User> getDonationsForCharity(String title);

    User getCreatorForCharity(String title);

    void donateMoneyForCharity(Charity charity, int userId, double money);

    void participateInCharity(Charity charity, int userId);

    List<Charity> getFilteredCharitiesByTitle(String filter);

    Double getSuggestionForDonation(Charity charity, int userId);
}
