package com.vmware.talentboost.networkofgiving.services.charity;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.repositories.charity.ICharityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharityService implements ICharityService {
    private final ICharityRepository repository;

    @Autowired
    public CharityService(ICharityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Charity> getAllCharities() {

        return repository.getAllCharities();
    }

    @Override
    public Charity getCharity(String title) {

        return repository.getCharity(title);
    }

    @Override
    public void addCharity(Charity charity) {

        repository.addCharity(charity);

    }

    @Override
    public void updateCharity(String title, Charity charity) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        repository.updateCharity(title, charity);
    }

    @Override
    public void deleteCharity(String title) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        repository.deleteCharity(title);

    }

    @Override
    public List<User> getParticipantsForCharity(String title) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return repository.getParticipantsForCharity(title);
    }

    @Override
    public List<User> getDonatorsForCharity(String title) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return repository.getDonatorsForCharity(title);
    }

    @Override
    public User getCreatorForCharity(String title) {

        return repository.getCreatorForCharity(title);
    }

    @Override
    public void donateMoneyForCharity(Charity charity, int userId, double money) {
        double newCollectedMoney = charity.getAmount_collected() + money;
        if (!repository.checkCharity(charity.getTitle())) {
            throw new IllegalArgumentException("Chariry doesn't exists.");
        }

        if (newCollectedMoney > charity.getBudget_required()) {
            throw new IllegalArgumentException("Too much money.Change the donation");
            //think for better exception
        }

        repository.donateMoneyForCharity(charity, userId, money);
    }

    @Override
    public void participateInCharity(Charity charity, int userId) {
        int newVolunteersCount = charity.getVolunteers_signed_up() + 1;

        if (!repository.checkCharity(charity.getTitle())) {
            throw new IllegalArgumentException("Chariry doesn't exists.");
        }

        if (newVolunteersCount > charity.getVolunteers_required()) {
            throw new IllegalArgumentException("Too much volunteers");
            //think for better exception
        }
        repository.participateInCharity(charity, userId);
    }
}
