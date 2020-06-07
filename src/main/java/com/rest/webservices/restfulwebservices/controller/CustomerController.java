package com.rest.webservices.restfulwebservices.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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

import com.rest.webservices.restfulwebservices.domain.Customer;
import com.rest.webservices.restfulwebservices.domain.Order;
import com.rest.webservices.restfulwebservices.exception.NotFoundException;
import com.rest.webservices.restfulwebservices.repository.CustomerRepository;
import com.rest.webservices.restfulwebservices.repository.OrderRepository;

/**
 * @author SenaGuler
 */
@RestController
@RequestMapping("/api/v1")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/customers")
	public List<Customer> retrieveAllCustomers() {
		return customerRepository.findAll();
	}

	@GetMapping("/customers/{id}")
	public Resource<Customer> retrieveCustomer(@PathVariable long id) {
		Optional<Customer> customer = customerRepository.findById(id);

		if (!customer.isPresent())
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);

		// "all-customers", SERVER_PATH + "/customers"
		// retrieveAllCustomers
		Resource<Customer> resource = new Resource<Customer>(customer.get());

		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllCustomers());

		resource.add(linkTo.withRel("all-customers"));

		// HATEOAS

		return resource;
	}

	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable long id) {
		customerRepository.deleteById(id);
	}


	@PostMapping("/customers")
	public ResponseEntity<Object> createCustomer(@Valid @RequestBody Customer customer) {
		Customer savedCustomer = customerRepository.save(customer);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCustomer.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

	@PutMapping("/customers/{id}")
	public Resource<Customer> createCustomer(@PathVariable long id, @Valid @RequestBody Customer customer) {
		Optional<Customer> customerOptional = customerRepository.findById(id);

		if(!customerOptional.isPresent()) {
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);
		}

		Customer savedCustomer = customerRepository.save(customer);

		Resource<Customer> resource = new Resource<Customer>(savedCustomer);

		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllCustomers());

		resource.add(linkTo.withRel("all-customers"));

		return resource;

	}

	/**
	 * have a different order services, this added for different perspective
	 */

	@GetMapping("/customers/{id}/orders")
	public List<Order> retrieveAllCustomers(@PathVariable long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);

		if(!customerOptional.isPresent()) {
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);
		}

		return customerOptional.get().getOrders();
	}


	@PostMapping("/customers/{id}/orders")
	public ResponseEntity<Object> createOrder(@PathVariable long id, @RequestBody Order order) {

		Optional<Customer> customerOptional = customerRepository.findById(id);

		if(!customerOptional.isPresent()) {
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);
		}

		Customer customer = customerOptional.get();

		order.setCustomer(customer);

		orderRepository.save(order);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

}
