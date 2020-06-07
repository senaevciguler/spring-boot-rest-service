package com.rest.webservices.restfulwebservices.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author SenaGuler
 */
@Data
@ApiModel(description="All details about the product.")
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min=2, message="Name should have atleast 2 characters")
    @ApiModelProperty(notes="Name should have atleast 2 characters")
    private String name;

    private String description;

    @DecimalMin(value = "0.0")
    @ApiModelProperty(notes="Price should be minumum 0.0")
    private BigDecimal price;

    @OneToMany(mappedBy="product", fetch= FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;


}
