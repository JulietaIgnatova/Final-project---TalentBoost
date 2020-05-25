package com.vmware.talentboost.networkofgiving.repositories.useraction;

import com.vmware.talentboost.networkofgiving.models.UserAction;

import java.util.List;

public interface IUserActionRepository {

    List<UserAction> getLatestActivities(int limit);

    List<UserAction> getLatestActivitiesForUser(int userId, int limit);

    void addUserAction(UserAction userAction);
}
