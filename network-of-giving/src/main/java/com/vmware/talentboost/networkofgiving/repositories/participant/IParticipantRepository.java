package com.vmware.talentboost.networkofgiving.repositories.participant;

import com.vmware.talentboost.networkofgiving.models.Participant;

import java.util.List;

public interface IParticipantRepository {
    void deleteParticipant(int userId, int charityId);

    public List<Participant> getAllParticipants(String title);
}
