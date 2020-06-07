package com.rest.webservices.restfulwebservices.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.webservices.restfulwebservices.domain.Customer;
import com.rest.webservices.restfulwebservices.repository.CustomerRepository;

/**
 * @author SenaGuler
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository mockRepository;

    @Before
    public void init() {
        Customer customer = new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(customer));
    }

    @Test
    public void find_customerId_OK() throws Exception {

        mockMvc.perform(get("/api/v1/customers/1"))
                /*.andDo(print())*/
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("sena")))
                .andExpect(jsonPath("$.lastname", is("guler")))
                .andExpect(jsonPath("$.mail", is("senaevciguler@gmail.com")))
                .andExpect(jsonPath("$.gsm", is("111111111")))
                .andExpect(jsonPath("$.address", is("Tallinn/Estonia")));

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_allCustomer_OK() throws Exception {

        List<Customer> customers = Arrays.asList(
                new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null),
                new Customer(2L, "Lebron", "James","lebron.james@lakers.com", "111111111", "Los Angeles/USA", null));

        when(mockRepository.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("sena")))
                .andExpect(jsonPath("$[0].lastname", is("guler")))
                .andExpect(jsonPath("$[0].mail", is("senaevciguler@gmail.com")))
                .andExpect(jsonPath("$[0].gsm", is("111111111")))
                .andExpect(jsonPath("$[0].address", is("Tallinn/Estonia")));

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_customerIdNotFound_404() throws Exception {
        mockMvc.perform(get("/api/v1/customers/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_customer_OK() throws Exception {

        Customer newCustomer = new Customer(1L, "Lebron", "James","lebron.james@lakers.com", "0111111111", "Los Angeles/USA", null);
        when(mockRepository.save(any(Customer.class))).thenReturn(newCustomer);

        mockMvc.perform(post("/api/v1/customers")
                .content(om.writeValueAsString(newCustomer))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                /*.andDo(print())*/
                .andExpect(status().isCreated());

        verify(mockRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void update_customer_OK() throws Exception {

        Customer updateCustomer = new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null);
        when(mockRepository.save(any(Customer.class))).thenReturn(updateCustomer);

        mockMvc.perform(put("/api/v1/customers/1")
                .content(om.writeValueAsString(updateCustomer))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("sena")))
                .andExpect(jsonPath("$.lastname", is("guler")))
                .andExpect(jsonPath("$.mail", is("senaevciguler@gmail.com")))
                .andExpect(jsonPath("$.gsm", is("111111111")))
                .andExpect(jsonPath("$.address", is("Tallinn/Estonia")));


    }

    @Test
    public void delete_customer_OK() throws Exception {

        doNothing().when(mockRepository).deleteById(1L);

        mockMvc.perform(delete("/api/v1/customers/1"))
                /*.andDo(print())*/
                .andExpect(status().isOk());

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
