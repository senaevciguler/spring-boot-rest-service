package com.rest.webservices.restfulwebservices.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rest.webservices.restfulwebservices.domain.Customer;

/**
 * @author SenaGuler
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @MockBean
    private CustomerRepository customerRespository;


    @Before
    public void init() {
        Customer customer = new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null);

        when(customerRespository.findById(1L)).thenReturn(Optional.of(customer));
    }

    @Test
    public void whenFindById_thenReturnCustomer() {
        Customer customer = customerRespository.findById(1L).get();

        assertEquals(customer.getName(),"sena");
    }

    @Test
    public void whenFindAll_thenReturnCustomerList() {
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "sena", "guler","senaevciguler@gmail.com", "111111111", "Tallinn/Estonia", null),
                new Customer(2L, "Lebron", "James","lebron.james@lakers.com", "111111111", "Los Angeles/USA", null));

        when(customerRespository.findAll()).thenReturn(customers);

        assertEquals(customers.size(),2);
    }
}