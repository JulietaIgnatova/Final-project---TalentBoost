package com.vmware.talentboost.networkofgiving.services.charity;

import com.vmware.talentboost.networkofgiving.models.*;
import com.vmware.talentboost.networkofgiving.repositories.charity.ICharityRepository;
import com.vmware.talentboost.networkofgiving.repositories.participant.IParticipantRepository;
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
    private final IParticipantRepository participantRepository;

    @Autowired
    public CharityService(ICharityRepository repository, IUserActionRepository actionRepository, IParticipantRepository participantRepository) {
        this.repository = repository;
        this.actionRepository = actionRepository;
        this.participantRepository = participantRepository;
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

        Charity oldCharity = repository.getCharity(title);

        updateCurrentBudgedIfNeeded(oldCharity, charity);

        updateParticipantsIfNeeded(oldCharity, charity);

        repository.updateCharity(title, charity);
    }


    @Override
    public void deleteCharity(Charity charity, String title) {
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
        if (charity.getVolunteersRequired() == newVolunteersCount) {
            List<User> participantsForCharity = repository.getParticipantsForCharity(charity.getTitle());
            for (User user : participantsForCharity) {
                String describeAction = String.format(UserActivityFormat.GET_ALL_VOLUNTEERS, charity.getTitle());
                actionRepository.addUserAction(new UserAction(user.getId(), describeAction, charity.getTitle()));
            }
        }
    }

    private void updateParticipantsIfNeeded(Charity oldCharity, Charity newCharity) {
        int participantsDiff = oldCharity.getVolunteersRequired() - newCharity.getVolunteersRequired();
        int participantsToRemove = oldCharity.getVolunteersSignedUp() - newCharity.getVolunteersRequired();

        if ((participantsDiff > 0) && (participantsToRemove > 0)) {
            List<Participant> participantsForCharity = participantRepository.getAllParticipants(oldCharity.getTitle());

            for (int i = 0; i < participantsToRemove; i++) {
                participantRepository.deleteParticipant(participantsForCharity.get(i).getUserId(), participantsForCharity.get(i).getCharityId());
                newCharity.setVolunteersSignedUp(newCharity.getVolunteersSignedUp() - 1);
                String describeAction = String.format(UserActivityFormat.NOTIFY_ON_UPDATE_PARTICIPANTS, newCharity.getTitle());
                actionRepository.addUserAction(new UserAction(participantsForCharity.get(i).getUserId(), describeAction, newCharity.getTitle()));
            }
        }
    }

    private void updateCurrentBudgedIfNeeded(Charity oldCharity, Charity newCharity) {
        Double diffBudget = oldCharity.getBudgetRequired() - newCharity.getBudgetRequired();
        Double targetMoney = oldCharity.getAmountCollected() - newCharity.getBudgetRequired();
        if ((diffBudget > 0) && (targetMoney > 0)) {
            List<Donation> donationsForCharity = repository.getDonationsForCharity(oldCharity.getTitle());
            for (Donation donation : donationsForCharity) {
                Double moneyToReturn = donation.getMoney();
                if ((targetMoney - moneyToReturn) < 0) {
                    //update donation
                    repository.setMoneyToDonation(donation.getUserId(), donation.getCharityId(), moneyToReturn - targetMoney);
                    newCharity.setAmountCollected(newCharity.getAmountCollected() - targetMoney); // set new amount
                    String describeAction = String.format(UserActivityFormat.NOTIFY_ON_UPDATE_MONEY, targetMoney, newCharity.getTitle());
                    actionRepository.addUserAction(new UserAction(donation.getUserId(), describeAction, newCharity.getTitle()));
                    return;
                }
                newCharity.setAmountCollected(newCharity.getAmountCollected() - moneyToReturn); // set new amount
                //delete donation
                repository.deleteDonation(donation.getUserId(), donation.getCharityId());
                String describeAction = String.format(UserActivityFormat.NOTIFY_ON_UPDATE_MONEY, donation.getMoney(), newCharity.getTitle());
                actionRepository.addUserAction(new UserAction(donation.getUserId(), describeAction, newCharity.getTitle()));
                targetMoney = newCharity.getAmountCollected() - newCharity.getBudgetRequired();

                if (targetMoney <= 0) {
                    //all money are returned
                    return;
                }
            }
        }
    }

}
