package com.vmware.talentboost.networkofgiving.services;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.repositories.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService implements IUserService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {

        return repository.getAllUsers();
    }

    @Override
    public User getUser(String username) {
        if (repository.checkUser(username)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return repository.getUser(username);
    }

    @Override
    public void addUser(final User user) {
        if (repository.checkUser(user.getUsername())) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        repository.addUser(user);
    }

    @Override
    public void updateUser(String username, User user) {
        if (!repository.checkUser(username)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        repository.updateUser(username, user);
    }

    @Override
    public void deleteUser(String username) {
        if (!repository.checkUser(username)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        repository.deleteUser(username);
    }

    @Override
    public List<Charity> getAllParticipatedCharities(String username) {
        if (!repository.checkUser(username)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return repository.getAllParticipatedCharities(username);
    }

    @Override
    public List<Charity> getAllDonatedCharities(String username) {
        if (!repository.checkUser(username)) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return repository.getAllDonatedCharities(username);
    }

    @Override
    public List<Charity> getAllCreatedCharities(String username) {
        if (!repository.checkUser(username)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return repository.getAllCreatedCharities(username);
    }
}
