package com.vmware.talentboost.networkofgiving.util.maprow;

import com.vmware.talentboost.networkofgiving.models.Charity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharityMapRower implements RowMapper<Charity> {
    @Override
    public Charity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Charity result = new Charity();
        result.setId(resultSet.getInt("id"));
        result.setCreatorId(resultSet.getInt("creator_id"));
        result.setTitle(resultSet.getString("title"));
        result.setDescription(resultSet.getString("description"));
        result.setBudgetRequired(resultSet.getInt("budget_required"));
        result.setAmountCollected(resultSet.getInt("amount_collected"));
        result.setVolunteersRequired(resultSet.getInt("volunteers_required"));
        result.setVolunteersSignedUp(resultSet.getInt("volunteers_signed_up"));
        return result;
    }

}
