package com.vmware.talentboost.networkofgiving.services.useraction;

import com.vmware.talentboost.networkofgiving.models.UserAction;
import com.vmware.talentboost.networkofgiving.repositories.user.IUserRepository;
import com.vmware.talentboost.networkofgiving.repositories.useraction.IUserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserActionService implements IUserActionService {
    private final IUserActionRepository repository;

    @Autowired
    public UserActionService(IUserActionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserAction> getLatestActivities(int limit) {
        return repository.getLatestActivities(limit);
    }

    @Override
    public List<UserAction> getLatestActivitiesForUser(int userId,int limit) {
        return repository.getLatestActivitiesForUser(userId,limit);
    }

    @Override
    public void addUserAction(UserAction userAction) {
        repository.addUserAction(userAction);

    }
}
