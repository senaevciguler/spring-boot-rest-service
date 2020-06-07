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

import com.rest.webservices.restfulwebservices.domain.Product;
import com.rest.webservices.restfulwebservices.exception.NotFoundException;
import com.rest.webservices.restfulwebservices.repository.ProductRepository;


/**
 * @author SenaGuler
 */
@RestController
@RequestMapping("/api/v1")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MessageSource messageSource;
	

	@GetMapping("/products")
	public List<Product> retrieveAllProducts() {
		return productRepository.findAll();
	}

	@GetMapping("/products/{id}")
	public Resource<Product> retrieveProduct(@PathVariable long id) {
		Optional<Product> product = productRepository.findById(id);

		if (!product.isPresent())
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + ": id-" + id);

		Resource<Product> resource = new Resource<Product>(product.get());

		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllProducts());

		resource.add(linkTo.withRel("all-products"));

		return resource;
	}

	@DeleteMapping("/products/{id}")
	public void deleteProduct(@PathVariable long id) {
		productRepository.deleteById(id);
	}

	@PostMapping("/products")
	public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product) {
		Product savedProduct = productRepository.save(product);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedProduct.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Object> createProduct(@PathVariable long id, @Valid @RequestBody Product product) {
		Optional<Product> productOptional = productRepository.findById(id);

		if(!productOptional.isPresent()) {
			throw new NotFoundException(messageSource.getMessage("not.found.message", null,
					LocaleContextHolder.getLocale()) + " id-" + id);
		}

		Product savedProduct = productRepository.save(product);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedProduct.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
}
