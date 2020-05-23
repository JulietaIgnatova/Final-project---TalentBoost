package com.vmware.talentboost.networkofgiving.repositories;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.util.CharityMapRower;
import com.vmware.talentboost.networkofgiving.util.UserMapRower;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcUserRepository implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean checkUser(String username) {
        return !isEmpty(jdbcTemplate.query("SELECT * FROM USERS WHERE username = ?", new UserMapRower(), username));
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UserMapRower());
    }

    @Override
    public User getUser(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USERNAME = ?", new UserMapRower(), username);
    }

    @Override
    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS(name,username,age,gender, location) VALUES (?, ?, ?, ?, ?) ",user.getName(),
                user.getUsername(),user.getAge(), user.getGender(), user.getLocation());

    }

    @Override
    public void updateUser(String username, User user) {
        jdbcTemplate.update("UPDATE USERS SET name = ?, age = ?, gender = ?, location = ? WHERE username = ?",
                user.getName(),user.getAge(), user.getGender(), user.getLocation(),username );
    }

    @Override
    public void deleteUser(String username) {
        jdbcTemplate.update("DELETE FROM USERS WHERE username = ?", username);
    }



    @Override
    public List<Charity> getAllParticipatedCharities(String username) {
        return jdbcTemplate.query("SELECT * FROM CHARITIES WHERE id IN" +
                " (SELECT charity_id FROM USERS join PARTICIPANTS on id=user_id where username=?)", new CharityMapRower(), username);
    }

    @Override
    public List<Charity> getAllDonatedCharities(String username) {

        return jdbcTemplate.query("SELECT * FROM CHARITIES WHERE id IN" +
                " (SELECT charity_id FROM USERS JOIN DONATORS ON id=user_id where username = ?)",new CharityMapRower(),username);
    }

    @Override
    public List<Charity> getAllCreatedCharities(String username) {
        return null;
    }


}
