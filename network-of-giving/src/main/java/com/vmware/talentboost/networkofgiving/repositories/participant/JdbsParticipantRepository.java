package com.vmware.talentboost.networkofgiving.repositories.participant;

import com.vmware.talentboost.networkofgiving.models.Participant;
import com.vmware.talentboost.networkofgiving.util.maprow.ParticipantMapRower;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbsParticipantRepository implements IParticipantRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbsParticipantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Participant> getAllParticipants(String title) {
        return jdbcTemplate.query("SELECT user_id, charity_id FROM PARTICIPANTS JOIN charities" +
                " ON charity_id = id where title=? ORDER BY participant_date DESC", new ParticipantMapRower(), title);
    }

    @Override
    public void deleteParticipant(int userId, int charityId) {
        jdbcTemplate.update("DELETE FROM PARTICIPANTS WHERE user_id=? and charity_id=?", userId, charityId);
    }

}
