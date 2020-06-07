package com.rest.webservices.restfulwebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.webservices.restfulwebservices.domain.Product;

/**
 * @author SenaGuler
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
