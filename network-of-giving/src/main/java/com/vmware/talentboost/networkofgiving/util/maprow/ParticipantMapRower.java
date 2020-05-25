package com.vmware.talentboost.networkofgiving.util.maprow;

import com.vmware.talentboost.networkofgiving.models.Participant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantMapRower implements RowMapper<Participant> {
    @Override
    public Participant mapRow(ResultSet rs, int i) throws SQLException {
        Participant result = new Participant();
        result.setUserId(rs.getInt("user_id"));
        result.setCharityId(rs.getInt("charity_id"));

        return result;
    }
}
