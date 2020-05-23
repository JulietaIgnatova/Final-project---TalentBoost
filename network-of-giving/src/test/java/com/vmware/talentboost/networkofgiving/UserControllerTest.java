package com.vmware.talentboost.networkofgiving;

import com.vmware.talentboost.networkofgiving.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

//    @Test
//    public void testGetAllUsers() {
//        int expectedNumberOfUsers = 2;
//
//        ResponseEntity<List<User>> responseEntity = restTemplate.exchange("/api/v1/users",
//                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
//                });
//        HttpStatus responseStatus = responseEntity.getStatusCode();
//        final List<User> userList = responseEntity.getBody();
//
//
//        assertEquals(HttpStatus.OK, responseStatus);
//        assertNotNull(userList);
//        assertFalse(userList.isEmpty());
//        assertEquals(expectedNumberOfUsers,userList.size());
//    }



}
