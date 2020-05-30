package com.vmware.talentboost.networkofgiving.services.user;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.LoginForm;
import com.vmware.talentboost.networkofgiving.models.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();

    User getUser(String username);

    void addUser(User user);

    void updateUser(String username, User user);

    void deleteUser(String username);

    List<Charity> getAllParticipatedCharities(String username);

    List<Charity> getAllDonatedCharities(String username);

    List<Charity> getAllCreatedCharities(String username);

    User login(LoginForm loginForm);
}

