package com.rest.webservices.restfulwebservices.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SenaGuler
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description="All details about the order.")
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @DecimalMin(value = "0.0")
    @ApiModelProperty(notes="Quantity should be minumum 0.0")
    private BigDecimal quantity;

    @DecimalMin(value = "0.0")
    @ApiModelProperty(notes="Subtotal should be minumum 0.0")
    private BigDecimal subtotal;

    private String note;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Customer customer;

}
