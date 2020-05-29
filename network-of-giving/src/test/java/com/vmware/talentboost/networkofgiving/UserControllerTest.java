package com.vmware.talentboost.networkofgiving;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.talentboost.networkofgiving.models.Charity;
import com.vmware.talentboost.networkofgiving.models.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
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
    private  HttpHeaders headers;
    @Before
    public void setHeaders(){
        String plainCreds = "maria:123456";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        //HttpEntity<String> request = new HttpEntity<String>(headers);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    String url = "/api/v1/users";

    @Test
    public void testGetAllUsers() {
        int expectedNumberOfUsers = 2;

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<User>>() {
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

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(getUrl,HttpMethod.GET, request, User.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final User user = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertEquals(username, user.getUsername());
    }


    @Test
    public void testGetNonExistingUser() {
        final String username = "martinn";
        final String getUrl = url + "/" + username;

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(getUrl,HttpMethod.GET, request, User.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
    }


    @Test
    public void testAddUserWithExistingUsername() throws JsonProcessingException {
        final User user = new User(1, "Maria", "maria", 21, "F", "Plovdiv");

        ObjectMapper mapper = new ObjectMapper();
        String userAsJson = mapper.writeValueAsString(user);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(userAsJson,headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testAddUserWithUniqueUsername() throws JsonProcessingException {
        final User expected = new User(3, "Karina", "karina", 21, "F", "Burgas");
        expected.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        String userAsJson = mapper.writeValueAsString(expected);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(userAsJson,headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

        HttpStatus responseStatus = responseEntity.getStatusCode();
        assertEquals(HttpStatus.CREATED, responseStatus);

        final String username = "karina";
        final String getUrl = url + "/" + username;

        ResponseEntity<User> responseUserEntity = restTemplate.exchange(getUrl,HttpMethod.GET, request, User.class);

        final User user = responseUserEntity.getBody();

        assertEquals(expected.getUsername(), user.getUsername());
        assertEquals(expected.getAge(), user.getAge());
    }

    @Test
    public void testUpdateExistingUser() throws JsonProcessingException {
        final String username = "maria";
        final String putUrl = url + "/" + username;
        final User expected = new User(1, "Maria", username, 25, "F", "Plovdiv");
        expected.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        String userAsJson = mapper.writeValueAsString(expected);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestUpdate = new HttpEntity<>(expected, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(putUrl, HttpMethod.PUT, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);

        ResponseEntity<User> responseUserEntity = restTemplate.exchange(putUrl, HttpMethod.GET, requestUpdate, User.class);
        final User user = responseUserEntity.getBody();

        assertEquals(expected.getAge(), user.getAge());
    }

    @Test
    public void testUpdateNonExistingUser() {
        final User user = new User(1, "Julieta", "monika", 21, "F", "Chirpan");
        final String username = user.getUsername();
        final String putUrl = url + "/" + username;

        HttpEntity<User> requestUpdate = new HttpEntity<>(user, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(putUrl, HttpMethod.PUT, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testDeleteNonExistingUser() {
        final String username = "mariana";
        final String deleteUrl = url + "/" + username;

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testDeleteExistingUser() {
        final String username = "martin";
        final String deleteUrl = url + "/" + username;

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);
    }

    @Test
    public void testGetAllParticipatedCharitiesOfExistingUser() {
        int sizeOfList = 2;
        String getUrl = url+"/maria/charities/participated";

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
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

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testGetAllDonatedCharitiesOfExistingUser() {
        String getUrl = url+"/maria/charities/donated";
        int sizeOfList = 2;

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
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

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    public void testGetAllCreatedCharitiesOfExistingUser() {
        String getUrl = url+"/maria/charities/created";

        int sizeOfList = 1;

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
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


        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }
}
