package com.rest.webservices.restfulwebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.webservices.restfulwebservices.domain.Order;

/**
 * @author SenaGuler
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
