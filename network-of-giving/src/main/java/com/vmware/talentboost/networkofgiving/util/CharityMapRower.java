package com.vmware.talentboost.networkofgiving.util;

import com.vmware.talentboost.networkofgiving.models.Charity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharityMapRower implements RowMapper<Charity> {
    @Override
    public Charity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
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
