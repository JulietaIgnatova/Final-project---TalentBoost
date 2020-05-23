package com.vmware.talentboost.networkofgiving.repositories.charity;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.util.CharityMapRower;
import com.vmware.talentboost.networkofgiving.util.UserMapRower;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class JdbcCharityRepository implements ICharityRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcCharityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Charity> getAllCharities() {

        return jdbcTemplate.query("SELECT * FROM CHARITIES", new CharityMapRower());
    }

    @Override
    public Charity getCharity(String title) {

        return jdbcTemplate.queryForObject("SELECT * FROM CHARITIES WHERE TITLE = ?", new CharityMapRower(), title);
    }

    @Override
    public boolean checkCharity(String title) {

        return !isEmpty(jdbcTemplate.query("SELECT * FROM CHARITIES WHERE title = ?", new CharityMapRower(), title));
    }

    @Override
    public void addCharity(Charity charity) {
        jdbcTemplate.update("INSERT INTO CHARITIES(creator_id, title, description, budget_required, amount_collected, volunteers_required, volunteers_signed_up)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?) ", charity.getCreator_id(), charity.getTitle(), charity.getDescription(),
                charity.getBudget_required(), charity.getAmount_collected(), charity.getVolunteers_required(), charity.getVolunteers_signed_up());

    }

    @Override
    public void updateCharity(String title, Charity charity) {
        jdbcTemplate.update("UPDATE CHARITIES SET creator_id=?, title=?, description=?, budget_required=?, " +
                        "amount_collected=?, volunteers_required=?, volunteers_signed_up=? WHERE title =?",
                charity.getCreator_id(), charity.getTitle(), charity.getDescription(),
                charity.getBudget_required(), charity.getAmount_collected(), charity.getVolunteers_required(),
                charity.getVolunteers_signed_up(), title);

    }

    @Override
    public void deleteCharity(String title) {
        jdbcTemplate.update("DELETE FROM CHARITIES WHERE title = ?", title);

    }

    @Override
    public List<User> getParticipantsForCharity(String title) {

        return jdbcTemplate.query("SELECT * FROM USERS WHERE id IN " +
                "(SELECT user_id FROM CHARITIES JOIN PARTICIPANTS ON id = charity_id WHERE TITLE = ?)", new UserMapRower(), title);
    }

    @Override
    public List<User> getDonatorsForCharity(String title) {

        return jdbcTemplate.query("SELECT * FROM USERS WHERE id IN " +
                "(SELECT user_id FROM CHARITIES JOIN DONATORS ON id = charity_id WHERE TITLE = ?)", new UserMapRower(), title);
    }

    @Override
    public User getCreatorForCharity(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE id = " +
                "(SELECT creator_id FROM CHARITIES WHERE title = ?)", new UserMapRower(), title);
    }
}
