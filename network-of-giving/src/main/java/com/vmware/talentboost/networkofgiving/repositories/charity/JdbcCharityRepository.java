package com.vmware.talentboost.networkofgiving.repositories.charity;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import com.vmware.talentboost.networkofgiving.util.maprow.CharityMapRower;
import com.vmware.talentboost.networkofgiving.util.maprow.DonatorMapRower;
import com.vmware.talentboost.networkofgiving.util.maprow.ParticipantMapRower;
import com.vmware.talentboost.networkofgiving.util.maprow.UserMapRower;
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
        jdbcTemplate.update("INSERT INTO CHARITIES(creator_id, title, description, budget_required, amount_collected, volunteers_required, volunteers_signed_up,image)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ", charity.getCreatorId(), charity.getTitle(), charity.getDescription(),
                charity.getBudgetRequired(), charity.getAmountCollected(), charity.getVolunteersRequired(), charity.getVolunteersSignedUp(),charity.getImage());

    }

    @Override
    public void updateCharity(String title, Charity charity) {
        jdbcTemplate.update("UPDATE CHARITIES SET creator_id=?, title=?, description=?, budget_required=?, " +
                        "amount_collected=?, volunteers_required=?, volunteers_signed_up=? WHERE title =?",
                charity.getCreatorId(), charity.getTitle(), charity.getDescription(),
                charity.getBudgetRequired(), charity.getAmountCollected(), charity.getVolunteersRequired(),
                charity.getVolunteersSignedUp(), title);

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
    public List<User> getDonationsForCharity(String title) {

        return jdbcTemplate.query("SELECT * FROM USERS WHERE id IN " +
                "(SELECT user_id FROM CHARITIES JOIN DONATORS ON id = charity_id WHERE TITLE = ?)", new UserMapRower(), title);
    }

    @Override
    public User getCreatorForCharity(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE id = " +
                "(SELECT creator_id FROM CHARITIES WHERE title = ?)", new UserMapRower(), title);
    }

    @Override
    public void donateMoneyForCharity(Charity charity, int userId, double money) {
        if (checkIfAlreadyDonated(userId, charity.getId())) {
            updateDonation(userId, charity.getId(), money);
        } else {
            addDonater(userId, charity.getId(), money);
        }
        charity.setAmountCollected(money + charity.getAmountCollected());
        updateCharity(charity.getTitle(), charity);
    }

    @Override
    public void participateInCharity(Charity charity, int userId) {
        if(checkIfAlreadyParticipate(userId,charity.getId())) {
            throw new IllegalArgumentException("Already participated in this charity.");
        }
        addParticipant(userId,charity.getId());
        charity.addVoluteer();
        System.out.println("sssssa");
        updateCharity(charity.getTitle(),charity);
    }

    @Override
    public Double getSuggestionForDonation(int userId) {
        return jdbcTemplate.queryForObject("SELECT AVG(donated_money) FROM DONATORS WHERE user_id = ?", Double.class,userId);
    }

    private void addDonater(int userId, int chariryId, double money) {
        jdbcTemplate.update("INSERT INTO DONATORS(user_id, charity_id, donated_money) VALUES(?, ?, ?)", userId, chariryId, money);
    }

    private boolean checkIfAlreadyDonated(int userId, int charityId) {
        return !isEmpty(jdbcTemplate.query("SELECT * FROM DONATORS WHERE user_id = ? AND charity_id =?", new DonatorMapRower(), userId, charityId));
    }

    private void updateDonation(int userId, int charityId, double money) {
        jdbcTemplate.update("UPDATE DONATORS SET donated_money = donated_money + ?", money);
    }

    private boolean checkIfAlreadyParticipate(int userId, int charityId) {
        return !isEmpty(jdbcTemplate.query("SELECT * FROM PARTICIPANTS WHERE user_id = ? AND charity_id =?", new ParticipantMapRower(), userId, charityId));
    }

    private void addParticipant(int userId, int chariryId){
        jdbcTemplate.update("INSERT INTO PARTICIPANTS(user_id, charity_id) VALUES(?,?)",userId,chariryId);
    }
}
