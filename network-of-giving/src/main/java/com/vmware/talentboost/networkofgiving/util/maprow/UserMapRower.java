package com.vmware.talentboost.networkofgiving.util.maprow;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapRower implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User result = new User();
        result.setId(rs.getInt("id"));
        result.setName(rs.getString("name"));
        result.setUsername(rs.getString("username"));
        result.setAge(rs.getInt("age"));
        result.setGender(rs.getString("gender"));
        result.setLocation(rs.getString("location"));

        return result;
    }
}
