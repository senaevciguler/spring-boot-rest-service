package com.rest.webservices.restfulwebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.webservices.restfulwebservices.domain.Customer;

/**
 * @author SenaGuler
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
