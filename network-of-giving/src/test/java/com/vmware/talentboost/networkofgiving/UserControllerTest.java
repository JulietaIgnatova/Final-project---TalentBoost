package com.vmware.talentboost.networkofgiving;

import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:database/seed.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:database/purge.sql")})
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    String url = "/api/v1/users";

    @Test
    public void testGetAllUsers() {
        int expectedNumberOfUsers = 2;

        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<User> userList = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(expectedNumberOfUsers, userList.size());
    }

    @Test
    public void testGetExistingUser() {

        final String username = "martin";
        final String getUrl = url + "/" + username;

        ResponseEntity<User> responseEntity = restTemplate.getForEntity(getUrl, User.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final User user = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertEquals(username, user.getUsername());
    }


    @Test
    public void testGetNonExistingUser() {
        final String username = "martinn";
        final String getUrl = url + "/" + username;
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(getUrl, User.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
    }


    @Test
    public void testAddUserWithExistingUsername() {
        final User user = new User(1, "Maria", "maria", 21, "F", "Plovdiv");

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, user, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testAddUserWithUniqueUsername() {
        final User expected = new User(3, "Karina", "karina", 21, "F", "Burgas");

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, expected, Void.class);

        HttpStatus responseStatus = responseEntity.getStatusCode();
        assertEquals(HttpStatus.CREATED, responseStatus);

        final String username = "karina";
        final String getUrl = url + "/" + username;

        final User actual = restTemplate.getForObject(getUrl, User.class);

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getAge(), actual.getAge());
    }

    @Test
    public void testUpdateExistingUser() {
        final String username = "maria";
        final String putUrl = url + "/" + username;
        final User expected = new User(1, "Maria", username, 25, "F", "Plovdiv");

        HttpEntity<User> requestUpdate = new HttpEntity<>(expected, new HttpHeaders());
        ResponseEntity<Void> responseEntity = restTemplate.exchange(putUrl, HttpMethod.PUT, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);

        final User actual = restTemplate.getForObject(putUrl, User.class);

        assertEquals(expected.getAge(), actual.getAge());
    }

    @Test
    public void testUpdateNonExistingUser() {
        final User user = new User(1, "Julieta", "monika", 21, "F", "Chirpan");
        final String username = user.getUsername();
        final String putUrl = url + "/" + username;

        HttpEntity<User> requestUpdate = new HttpEntity<>(user, new HttpHeaders());
        ResponseEntity<Void> responseEntity = restTemplate.exchange(putUrl, HttpMethod.PUT, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testDeleteNonExistingUser() {
        final String username = "mariana";
        final String deleteUrl = url + "/" + username;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testDeleteExistingUser() {
        final String username = "martin";
        final String deleteUrl = url + "/" + username;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);
    }

    @Test
    public void testGetAllParticipatedCharitiesOfExistingUser() {
        int sizeOfList = 2;
        String getUrl = url+"/maria/charities/participated";

        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<Charity> actual = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        assertEquals(sizeOfList, actual.size());

    }

    @Test
    public void testGetAllParticipatedCharitiesOfNonExistingUser() {
        String getUrl = url+"/mariana/charities/participated";

        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

//
    @Test
    public void testGetAllDonatedCharitiesOfExistingUser() {
        String getUrl = url+"/maria/charities/donated";
        int sizeOfList = 2;

        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<Charity> actual = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(sizeOfList, actual.size());
    }

    @Test
    public void testGetAllDonatedCharitiesOfNonExistingUser() {
        String getUrl = url+"/mariana/charities/donated";

        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    public void testGetAllCreatedCharitiesOfExistingUser() {
        String getUrl = url+"/maria/charities/created";

        int sizeOfList = 1;

        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<Charity> actual = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(sizeOfList, actual.size());
    }

    @Test
    public void testGetAllCreatedCharitiesOfNonExistingUser() {
        String getUrl = url+"/mariana/charities/created";


        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }
}
