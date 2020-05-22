package com.vmware.talentboost.networkofgiving.repositories;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;

import java.util.List;

public interface IUserRepository {
    List<User> getAllUsers();

    User getUser(String username);

    public boolean checkUser(String username);

    void addUser(User user);

    void updateUser(String username, User user);

    void deleteUser(String username);

    List<Charity> getAllParticipatedCharities(String username);

    List<Charity> getAllDonatedCharities(String username);

    List<Charity> getAllCreatedCharities(String username);
}
