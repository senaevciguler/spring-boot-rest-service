package com.rest.webservices.restfulwebservices.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.webservices.restfulwebservices.domain.Customer;
import com.rest.webservices.restfulwebservices.repository.CustomerRepository;

/**
 * @author SenaGuler
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class CustomerControllerRestTemplateTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CustomerRepository mockRepository;

    @Before
    public void init() {
        Customer customer = new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(customer));
    }

    @Test
    public void find_customerId_OK() throws JSONException {

        String expected = " {\n"
                + "        \"name\": \"sena\",\n"
                + "        \"lastname\": \"guler\",\n"
                + "        \"mail\": \"senaevciguler@gmail.com\",\n"
                + "        \"gsm\": \"111111111\",\n"
                + "        \"address\": \"Tallinn/Estonia\"\n"
                + "    }";

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/customers/1", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());

        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_allCustomer_OK() throws Exception {

        List<Customer> customers = Arrays.asList(
                new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null),
                new Customer(2L, "nena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null));

        when(mockRepository.findAll()).thenReturn(customers);

        String expected = om.writeValueAsString(customers);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/customers", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_customerIdNotFound_404() throws Exception {

        String expected = "{\n"
                + "    \"timestamp\": \"2020-01-11T10:22:15.032+0000\",\n"
                + "    \"message\": \"Not Found id-5\",\n"
                + "    \"details\": \"uri=/api/v1/customers/5\"\n"
                + "}";

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/customers/5", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        //JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    public void save_customer_OK() throws Exception {

        Customer newCustomer = new Customer(1L, "Lebron", "James","lebron.james@lakers.com", "0111111111", "Los Angeles/USA", null);
        when(mockRepository.save(any(Customer.class))).thenReturn(newCustomer);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/customers", newCustomer, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(mockRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void update_customer_OK() throws Exception {

        Customer updateCustomer =  new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null);
        when(mockRepository.save(any(Customer.class))).thenReturn(updateCustomer);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateCustomer), headers);

        ResponseEntity<Customer> response = restTemplate.exchange("/api/v1/customers/1", HttpMethod.PUT, entity, Customer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        response.getBody().setId(1L);
        JSONAssert.assertEquals(om.writeValueAsString(updateCustomer), om.writeValueAsString(response.getBody()), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Customer.class));

    }


    @Test
    public void delete_customer_OK() {

        doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mockRepository, times(1)).deleteById(1L);
    }

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
