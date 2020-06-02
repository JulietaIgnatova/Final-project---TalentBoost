package com.vmware.talentboost.networkofgiving.services.charity;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.Donation;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.models.UserAction;
import com.vmware.talentboost.networkofgiving.repositories.charity.ICharityRepository;
import com.vmware.talentboost.networkofgiving.repositories.useraction.IUserActionRepository;
import com.vmware.talentboost.networkofgiving.util.format.UserActivityFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CharityService implements ICharityService {
    private final ICharityRepository repository;
    private final IUserActionRepository actionRepository;

    @Autowired
    public CharityService(ICharityRepository repository, IUserActionRepository actionRepository) {
        this.repository = repository;
        this.actionRepository = actionRepository;
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

        String describeAction = String.format(UserActivityFormat.CREATED_ACTION, charity.getTitle());
        actionRepository.addUserAction(new UserAction(charity.getCreatorId(), describeAction, charity.getTitle()));

    }

    @Override
    public void updateCharity(String title, Charity charity) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        repository.updateCharity(title, charity);
    }

    @Override
    public void deleteCharity(Charity charity, String title) {
        System.out.println("helloo");
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        List<Donation> donationsForCharity = repository.getDonationsForCharity(title);
        List<User> participantsForCharity = repository.getParticipantsForCharity(title);

        repository.deleteCharity(title);

        String describeAction = String.format(UserActivityFormat.DELETED_ACTION, charity.getTitle());
        actionRepository.addUserAction(new UserAction(charity.getCreatorId(), describeAction, charity.getTitle()));

        giveMoneyBackAfterDeletion(donationsForCharity, charity);
        notifyParticipantsAfterDeletion(participantsForCharity, charity);
    }

    @Override
    public List<User> getParticipantsForCharity(String title) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return repository.getParticipantsForCharity(title);
    }

    @Override
    public List<User> getDonationsForCharity(String title) {
        if (!repository.checkCharity(title)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return repository.getAllUserDonationsForCharity(title);
    }

    @Override
    public User getCreatorForCharity(String title) {

        return repository.getCreatorForCharity(title);
    }

    @Override
    public void donateMoneyForCharity(Charity charity, int userId, double money) {
        double newCollectedMoney = charity.getAmountCollected() + money;
        if (!repository.checkCharity(charity.getTitle())) {
            throw new IllegalArgumentException("Chariry doesn't exists.");
        }

        if (newCollectedMoney > charity.getBudgetRequired()) {
            throw new IllegalArgumentException("Too much money.Change the donation");
            //think for better exception
        }

        repository.donateMoneyForCharity(charity, userId, money);

        String describeAction = String.format(UserActivityFormat.DONATE_ACTION, money, charity.getTitle());
        actionRepository.addUserAction(new UserAction(userId, describeAction, charity.getTitle()));

        checkIfMoneyAreCollected(charity, newCollectedMoney);
    }


    @Override
    public void participateInCharity(Charity charity, int userId) {
        int newVolunteersCount = charity.getVolunteersSignedUp() + 1;

        if (!repository.checkCharity(charity.getTitle())) {
            throw new IllegalArgumentException("Chariry doesn't exists.");
        }

        if (newVolunteersCount > charity.getVolunteersRequired()) {
            throw new IllegalArgumentException("Too much volunteers");
            //think for better exception
        }
        repository.participateInCharity(charity, userId);

        String describeAction = String.format(UserActivityFormat.PARTICIPATE_ACTION, charity.getTitle());
        actionRepository.addUserAction(new UserAction(userId, describeAction, charity.getTitle()));

        checkIfAllVolunteersAreGathered(charity, newVolunteersCount);
    }


    @Override
    public List<Charity> getFilteredCharitiesByTitle(String filter) {
        return getAllCharities().stream().filter(
                charity -> {
                    return charity.getTitle().toLowerCase().contains(filter.toLowerCase());
                }).collect(Collectors.toList());
    }

    @Override
    public Double getSuggestionForDonation(Charity charity, int userId) {
        double suggestion = repository.getSuggestionForDonation(userId);
        double neededMoney = charity.getBudgetRequired() - charity.getAmountCollected();
        if (suggestion > neededMoney) {
            return new BigDecimal(neededMoney).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return new BigDecimal(suggestion).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    private void notifyParticipantsAfterDeletion(List<User> participantsForCharity, Charity charity) {
        for (User user : participantsForCharity) {
            String describeAction = String.format(UserActivityFormat.NOTIFY_PARTICIPANTS, charity.getTitle());
            actionRepository.addUserAction(new UserAction(user.getId(), describeAction, charity.getTitle()));
        }
    }

    private void giveMoneyBackAfterDeletion(List<Donation> donationsForCharity, Charity charity) {
        for (Donation donation : donationsForCharity) {
            String describeAction = String.format(UserActivityFormat.GIVE_MONEY_BACK, donation.getMoney(), charity.getTitle());
            actionRepository.addUserAction(new UserAction(donation.getUserId(), describeAction, charity.getTitle()));
        }
    }

    private void checkIfMoneyAreCollected(Charity charity, double newCollectedMoney) {
        if (newCollectedMoney == charity.getBudgetRequired()) {
            List<Donation> donationsForCharity = repository.getDonationsForCharity(charity.getTitle());
            for (Donation donation : donationsForCharity) {
                String describeAction = String.format(UserActivityFormat.COLLECTED_ALL_MONEY, charity.getTitle());
                actionRepository.addUserAction(new UserAction(donation.getUserId(), describeAction, charity.getTitle()));
            }
        }
    }

    private void checkIfAllVolunteersAreGathered(Charity charity, int newVolunteersCount) {
        if(charity.getVolunteersRequired() == newVolunteersCount){
            List<User> participantsForCharity = repository.getParticipantsForCharity(charity.getTitle());
            for(User user: participantsForCharity) {
                String describeAction = String.format(UserActivityFormat.GET_ALL_VOLUNTEERS, charity.getTitle());
                actionRepository.addUserAction(new UserAction(user.getId(), describeAction, charity.getTitle()));
            }
        }
    }
}
