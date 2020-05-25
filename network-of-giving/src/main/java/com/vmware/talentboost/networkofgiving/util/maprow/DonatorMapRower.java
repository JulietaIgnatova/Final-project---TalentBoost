package com.vmware.talentboost.networkofgiving.util.maprow;

import com.vmware.talentboost.networkofgiving.models.Donation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DonatorMapRower implements RowMapper<Donation> {

    @Override
    public Donation mapRow(ResultSet resultSet, int i) throws SQLException {
        Donation result = new Donation();
        result.setCharityId(resultSet.getInt("charity_id"));
        result.setUserId(resultSet.getInt("user_id"));
        result.setMoney(resultSet.getDouble("donated_money"));

        return result;
    }
}
