package com.vmware.talentboost.networkofgiving.repositories.useraction;

import com.vmware.talentboost.networkofgiving.models.UserAction;
import com.vmware.talentboost.networkofgiving.util.maprow.UserActionMapRower;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcUserActionRepository implements IUserActionRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserActionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserAction> getLatestActivities(int limit) {
        return jdbcTemplate.query("SELECT * FROM USERACTIONS ORDER BY action_date desc limit ?", new UserActionMapRower(), limit);
    }

    @Override
    public List<UserAction> getLatestActivitiesForUser(int userId, int limit) {
        return jdbcTemplate.query("SELECT * FROM USERACTIONS  WHERE user_id = ? ORDER BY action_date desc limit ?", new UserActionMapRower(), userId, limit);
    }

    @Override
    public void addUserAction(UserAction userAction) {
        jdbcTemplate.update("INSERT INTO USERACTIONS(user_id,description,charity_title) VALUES(?,?,?)", userAction.getUserId(), userAction.getDescription(), userAction.getCharityTitle());
    }
}
