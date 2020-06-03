package com.vmware.talentboost.networkofgiving.services;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.Donation;
import com.vmware.talentboost.networkofgiving.models.Participant;
import com.vmware.talentboost.networkofgiving.models.UserAction;
import com.vmware.talentboost.networkofgiving.repositories.charity.ICharityRepository;
import com.vmware.talentboost.networkofgiving.repositories.participant.IParticipantRepository;
import com.vmware.talentboost.networkofgiving.repositories.useraction.IUserActionRepository;
import com.vmware.talentboost.networkofgiving.util.format.UserActivityFormat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityManager {
    private final ICharityRepository charityRepository;
    private final IParticipantRepository participantRepository;
    private final IUserActionRepository actionRepository;

    public ActivityManager(ICharityRepository charityRepository, IParticipantRepository participantRepository,
                           IUserActionRepository actionRepository) {
        this.charityRepository = charityRepository;
        this.participantRepository = participantRepository;
        this.actionRepository = actionRepository;
    }

    public void updateParticipantsIfNeeded(Charity oldCharity, Charity newCharity) {
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

    public void updateCurrentBudgedIfNeeded(Charity oldCharity, Charity newCharity) {
        Double diffBudget = oldCharity.getBudgetRequired() - newCharity.getBudgetRequired();
        Double targetMoney = oldCharity.getAmountCollected() - newCharity.getBudgetRequired();
        if ((diffBudget > 0) && (targetMoney > 0)) {
            List<Donation> donationsForCharity = charityRepository.getDonationsForCharity(oldCharity.getTitle());
            for (Donation donation : donationsForCharity) {
                Double moneyToReturn = donation.getMoney();
                if ((targetMoney - moneyToReturn) < 0) {
                    //update donation
                    charityRepository.setMoneyToDonation(donation.getUserId(), donation.getCharityId(), moneyToReturn - targetMoney);
                    newCharity.setAmountCollected(newCharity.getAmountCollected() - targetMoney); // set new amount
                    String describeAction = String.format(UserActivityFormat.NOTIFY_ON_UPDATE_MONEY, targetMoney, newCharity.getTitle());
                    actionRepository.addUserAction(new UserAction(donation.getUserId(), describeAction, newCharity.getTitle()));
                    return;
                }
                newCharity.setAmountCollected(newCharity.getAmountCollected() - moneyToReturn); // set new amount
                //delete donation
                charityRepository.deleteDonation(donation.getUserId(), donation.getCharityId());
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
