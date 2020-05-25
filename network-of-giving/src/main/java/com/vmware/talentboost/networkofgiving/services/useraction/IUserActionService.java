package com.vmware.talentboost.networkofgiving.services.useraction;

import com.vmware.talentboost.networkofgiving.models.UserAction;

import java.util.List;

public interface IUserActionService {
    List<UserAction> getLatestActivities(int limit);

    List<UserAction> getLatestActivitiesForUser(int userId,int limit);

    void addUserAction(UserAction userAction);
}
