package com.vmware.talentboost.networkofgiving.repositories;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcUserRepository implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
//    public boolean checkIfUserExist(String username) {
//        return !isEmpty(jdbcTemplate.query("SELECT * FROM user_info WHERE username = ?", this::mapUserRow, username));
//    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", this::mapUserRow);
    }

    @Override
    public User getUser(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USERNAME = ?", this::mapUserRow, username);
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
                " (SELECT charity_id FROM USERS join PARTICIPANTS on id=user_id where username=?)", this::mapCharityRow, username);
    }

    @Override
    public List<Charity> getAllDonatedCharities(String username) {

        return jdbcTemplate.query("SELECT * FROM CHARITIES WHERE id IN" +
                " (SELECT charity_id FROM USERS JOIN DONATORS ON id=user_id where username = ?)",this::mapCharityRow,username);
    }

    @Override
    public List<Charity> getAllCreatedCharities(String username) {
        return null;
    }

    private User mapUserRow(ResultSet rs, int rowNum) throws SQLException {
        User result = new User();
        result.setId(rs.getInt("id"));
        result.setName(rs.getString("name"));
        result.setUsername(rs.getString("username"));
        result.setAge(rs.getInt("age"));
        result.setGender(rs.getString("gender"));
        result.setLocation(rs.getString("location"));

        return result;
    }


    private Charity mapCharityRow(ResultSet resultSet, int rowNum) throws SQLException {
        Charity result = new Charity();
        result.setId(resultSet.getInt("id"));
        result.setCreator_id(resultSet.getInt("creator_id"));
        result.setTitle(resultSet.getString("title"));
        result.setDescription(resultSet.getString("description"));
        result.setBudget_required(resultSet.getInt("budget_required"));
        result.setAmount_collected(resultSet.getInt("amount_collected"));
        result.setVolunteers_required(resultSet.getInt("volunteers_required"));
        result.setVolunteers_signed_up(resultSet.getInt("volunteers_signed_up"));

        return result;
    }
}
