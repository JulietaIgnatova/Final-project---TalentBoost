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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:database/seed.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:database/purge.sql")})
public class CharityControllerTest {

    private HttpHeaders headers;

    @Before
    public void setHeaders() {
        String plainCreds = "maria:123456";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    String url = "/api/v1/charities";

    @Test
    public void testGetAllCharities() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        int expectedNumberOfUsers = 2;

        ResponseEntity<List<Charity>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Charity>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<Charity> charityListList = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(charityListList);
        assertFalse(charityListList.isEmpty());
        assertEquals(expectedNumberOfUsers, charityListList.size());
    }

    @Test
    public void testGetExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        final String title = "save the world";
        final String getUrl = url + "/" + title;

        ResponseEntity<Charity> responseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, request, Charity.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final Charity charity = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertEquals(title, charity.getTitle());
    }

    @Test
    public void testGetNonExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        final String title = "save world";
        final String getUrl = url + "/" + title;

        ResponseEntity<Charity> responseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, request, Charity.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
    }

    @Test
    public void testAddCharityWithUniqueTitle() throws JsonProcessingException {
        final Charity expected = new Charity(1, 1, "clean the world", "we are going to clean the world", 10000, 200, 20, 10);

        ObjectMapper mapper = new ObjectMapper();
        String charityAsJson = mapper.writeValueAsString(expected);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", null);
        body.add("body", charityAsJson);


        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseStatus);

        final String title = "clean the world";
        final String getUrl = url + "/" + title;
        final Charity actual = restTemplate.getForObject(getUrl, Charity.class);

        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    public void testAddCharityWithExistingTitle() throws JsonProcessingException {
        final Charity expected = new Charity(1, 1, "save the world", "we are going to clean the world", 10000, 200, 20, 10);

        ObjectMapper mapper = new ObjectMapper();
        String charityAsJson = mapper.writeValueAsString(expected);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", null);
        body.add("body", charityAsJson);

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseStatus);
    }

    @Test
    public void testUpdateExistingCharity() {
        final String title = "save the world";
        final String putUrl = url + "/" + title;
        final Charity expected = new Charity(1, 1, title, "clean the world", 10000, 200, 20, 10);

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(expected, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(putUrl, HttpMethod.PUT, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);

        final Charity actual = restTemplate.getForObject(putUrl, Charity.class);

        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    public void testUpdateNonExistingCharity() {
        final String title = "save the world today";
        final String putUrl = url + "/" + title;
        final Charity expected = new Charity(1, 1, title, "clean the world", 10000, 200, 20, 10);

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(expected, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(putUrl, HttpMethod.PUT, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    public void testDeleteExistingCharity() {
        final Charity expected = new Charity(1, 1, "save the world", "we are going to clean the world", 10000, 200, 20, 10);

        HttpEntity<Charity> request = new HttpEntity<>(expected,headers);
        final String title = "save the world";
        final String postUrl = url + "/" + title;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, request, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);
    }

    @Test
    public void testDeleteNonExistingCharity() {
        final Charity expected = new Charity(1, 1, "save the world today", "we are going to clean the world", 10000, 200, 20, 10);

        HttpEntity<Charity> request = new HttpEntity<>(expected,headers);
        final String title = "save the world today";
        final String postUrl = url + "/" + title;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, request, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    public void testGetParticipantsOfExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        int sizeOfList = 2;
        final String title = "better world";
        String getUrl = url + "/" + title + "/participants";

        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<User>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<User> actual = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(sizeOfList, actual.size());
    }

    @Test
    public void testGetParticipantsOfNonExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        final String title = "save the planet";
        String getUrl = url + "/" + title + "/participants";

        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<User>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    public void testGetDonationsOfExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        final String title = "save the world";
        String getUrl = url + "/" + title + "/donations";

        int sizeOfList = 2;

        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<User>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final List<User> actual = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(sizeOfList, actual.size());
    }


    @Test
    public void testGetDonationsOfNonExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        final String title = "save the world today";
        String getUrl = url + "/" + title + "/donations";

        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(getUrl,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<User>>() {
                });
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);

    }

    @Test
    public void testGetCreatorOfExistingCharity() {
        HttpEntity<String> request = new HttpEntity<String>(headers);
        final String title = "save the world";
        String getUrl = url + "/" + title + "/creator";

        ResponseEntity<User> responseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, request, User.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();
        final User user = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseStatus);
        assertNotNull(user);
    }

    @Test
    public void testDonateValidMoneyForExistingCharity() {
        final int userId = 1;
        final int donatedMoney = 200;
        final String postUrl = url + "/donate/" + userId + "?money=" + donatedMoney;
        final Charity expected = new Charity(1, 1, "save the world", "we are going to clean the world", 10000, 200, 200, 10);

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(expected, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);


        final String getUrl = "/api/v1/charities/save the world";
        final Charity actual = restTemplate.getForObject(getUrl, Charity.class);
        assertEquals(expected.getAmountCollected() + donatedMoney, actual.getAmountCollected(), 0.01);
    }


    @Test
    public void testDonateValidMoneyForNonExistingCharity() {
        final int userId = 1;
        final int donatedMoney = 200;
        final String postUrl = url + "/donate/" + userId + "?money=" + donatedMoney;
        final String title = "save the planet";
        final Charity body = new Charity(1, 1, title, "we are going to clean the world", 10000, 200, 200, 10);

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    public void testDonateTooMuchMoneyForExistingCharity() {
        final int userId = 1;
        final int donatedMoney = 20000;
        final String postUrl = url + "/donate/" + userId + "?money=" + donatedMoney;
        final Charity body = new Charity(1, 1, "save the world", "we are going to clean the world", 10000, 200, 200, 10);

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }


    @Test
    public void testParticipateInExistingCharityForTheFirstTime() {
        final int userId = 2;
        final int donatedMoney = 200;
        final String postUrl = url + "/participate/" + userId;

        final Charity body = new Charity(1, 1, "save the world", "we are going to clean the world", 10000, 10, 200, 20);

        int expectedVolunteers = body.getVolunteersSignedUp() + 1;

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, responseStatus);

        final String getUrl = "/api/v1/charities/save the world";
        final Charity actual = restTemplate.getForObject(getUrl, Charity.class);
        assertEquals(expectedVolunteers, actual.getVolunteersSignedUp());
    }

    @Test
    public void testParticipateInExistingCharityNotForTheFirstTime() {
        final int userId = 1;
        final int donatedMoney = 200;
        final String postUrl = url + "/participate/" + userId;

        final Charity body = new Charity(1, 1, "save the world", "we are going to clean the world", 10000, 10, 200, 20);

        int expectedVolunteers = body.getVolunteersSignedUp() + 1;

        HttpEntity<Charity> requestUpdate = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);

    }

    @Test
    public void testParticipateInNonExistingCharity() {
        final int userId = 2;
        final int donatedMoney = 200;
        final String postUrl = url + "/participate/" + userId;

        final Charity body = new Charity(1, 1, "today", "we are going to clean the world", 10000, 10, 200, 20);

        int expectedVolunteers = body.getVolunteersSignedUp() + 1;
        HttpEntity<Charity> requestUpdate = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(postUrl, HttpMethod.POST, requestUpdate, Void.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);

    }

}

