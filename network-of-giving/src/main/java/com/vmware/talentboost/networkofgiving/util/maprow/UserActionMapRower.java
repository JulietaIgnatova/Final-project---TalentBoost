package com.vmware.talentboost.networkofgiving.util.maprow;

import com.vmware.talentboost.networkofgiving.models.UserAction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class UserActionMapRower implements RowMapper<UserAction> {
    private final String zoneOffset = "+03:00";
    private String pattern = "yyyy-MM-dd hh:mm:ss E";

    @Override
    public UserAction mapRow(ResultSet rs, int i) throws SQLException {
        UserAction result = new UserAction();

        result.setDescription((rs.getString("description")));
        result.setUserId(rs.getInt("user_id"));
        result.setCharitTitle(rs.getString("charity_title"));
        OffsetDateTime actionDate = rs.getObject("action_date", OffsetDateTime.class);
        actionDate = actionDate.toInstant().atOffset(ZoneOffset.of(zoneOffset));
        result.setActionDate(actionDate);
        result.setFormattedActionDate(actionDate.format(DateTimeFormatter.ofPattern(pattern)));
        return result;
    }
}
