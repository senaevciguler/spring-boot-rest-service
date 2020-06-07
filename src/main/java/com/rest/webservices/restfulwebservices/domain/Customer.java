package com.rest.webservices.restfulwebservices.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SenaGuler
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description="All details about the customer.")
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min=2, message="Name should have at least 2 characters")
    @ApiModelProperty(notes="Name should have at least 2 characters")
    private String name;

    @Size(min=2, message="Lastname should have at least 2 characters")
    @ApiModelProperty(notes="Lastname should have at least 2 characters")
    private String lastname;

    //@Email
    @ApiModelProperty(notes="Mail should be correct format")
    private String mail;

    @Digits(integer = 10, fraction = 0)
    @ApiModelProperty(notes="Gsm should be number")
    private String gsm;

    @Size(min=2, message="Address should have at least 2 characters")
    @ApiModelProperty(notes="Address should have at least 2 characters")
    private String address;

    @OneToMany(mappedBy="customer", fetch= FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

}
