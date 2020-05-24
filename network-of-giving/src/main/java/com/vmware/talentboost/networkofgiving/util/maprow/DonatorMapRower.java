package com.vmware.talentboost.networkofgiving.util.maprow;

import com.vmware.talentboost.networkofgiving.models.Donator;
import com.vmware.talentboost.networkofgiving.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DonatorMapRower implements RowMapper<Donator> {

    @Override
    public Donator mapRow(ResultSet resultSet, int i) throws SQLException {
        Donator result = new Donator();
        result.setCharity_id(resultSet.getInt("charity_id"));
        result.setUser_id(resultSet.getInt("user_id"));
        result.setMoney(resultSet.getDouble("donated_money"));

        return result;
    }
}
