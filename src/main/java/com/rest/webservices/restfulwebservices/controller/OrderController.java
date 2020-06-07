package com.rest.webservices.restfulwebservices.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rest.webservices.restfulwebservices.domain.Order;
import com.rest.webservices.restfulwebservices.exception.NotFoundException;
import com.rest.webservices.restfulwebservices.repository.OrderRepository;

/**
 * @author SenaGuler
 */
@RestController
@RequestMapping("/api/v1")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/orders")
	public List<Order> retrieveAllOrders() {
		return orderRepository.findAll();
	}

	@GetMapping("/orders/{id}")
	public Order retrieveOrder(@PathVariable long id) {
		Optional<Order> order = orderRepository.findById(id);

		if (!order.isPresent())
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);

		return order.get();
	}

	@DeleteMapping("/orders/{id}")
	public void deleteOrder(@PathVariable long id) {
		orderRepository.deleteById(id);
	}


	@PostMapping("/orders")
	public ResponseEntity<Object> createOrder(@Valid @RequestBody Order order) {
		Order savedOrder = orderRepository.save(order);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedOrder.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

	@PutMapping("/orders/{id}")
	public ResponseEntity<Object> createOrder(@PathVariable long id, @Valid @RequestBody Order order) {
		Optional<Order> orderOptional = orderRepository.findById(id);

		if(!orderOptional.isPresent()) {
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);
		}

		Order savedOrder = orderRepository.save(order);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedOrder.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
}
