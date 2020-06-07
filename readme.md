
# RESTful Web Services

## Order Application Resource Mappings

### Customer -> Orders

- Retrieve all Customers       - GET  api/v1/customers
- Create a Customers           - POST api/v1/customers
- Retrieve one Customers       - GET  api/v1/customers/{id} -> api/v1/customers/1   
- Delete a Customers           - DELETE api/v1/customers/{id} -> api/v1/customers/1

- Retrieve all Orders for a Customer - GET api/v1/customers/{id}/orders 
- Create a Orders for a Customer - POST api/v1/customers/{id}/orders
- Retrieve details of a Order - GET api/v1/customers/{id}/orders/{order_id}


### Example Requests

#### GET http://localhost:8080/api/v1/customers
```json
[
    {
        "id": 1,
        "name": "sena",
        "lastname": "guler",
        "mail": "senaevciguler@gmail.com",
        "gsm": "111111111",
        "address": "Tallinn/Estonia"
    },
    {
        "id": 1,
        "name": "test",
        "lastname": "test",
        "mail": "senaevciguler@gmail.com",
        "gsm": "111111111",
        "address": "Tallinn/Estonia"
    }
]
```
#### GET http://localhost:8080/api/v1/customers/1
```json
{
    "name": "sena",
    "lastname": "guler",
    "mail": "senaevciguler@gmail.com",
    "gsm": "111111111",
    "address": "Tallinn/Estonia",
    "_links": {
        "all-customers": {
          "href": "http://localhost:8080/api/v1/customers"
        }
    }
}
```
#### POST http://localhost:8080/api/v1/customers
```json
{
    "name": "lebron",
    "lastname": "james",
    "mail": "lebronjames@lakers.com",
    "gsm": "111111111",
    "address": "Los Angeles/USA"
}
```

#### GET http://localhost:8080/api/v1/customers/1000
- Get request to a non existing resource. 
- The response shows default error message structure auto configured by Spring Boot.

```json
{
    "timestamp": "2020-01-08T13:23:25.813+0000",
    "message": "Not Found id-1000",
    "details": "uri=/api/v1/customers/1000"
}
```

#### POST http://localhost:8080/api/v1/customers with Validation Errors

##### Request
```json
{
    "name": "l",
    "lastname": "james",
    "mail": "lebronjames@lakers.com",
    "gsm": "111111111",
    "address": "Los Angeles/USA"
}
```
##### Response - 400 Bad Request
```json
{
    "timestamp": "2020-01-08T13:39:37.535+0000",
    "message": "Validation Failed",
    "details": "org.springframework.validation.BeanPropertyBindingResult: 1 errors\nField error in object 'customer' on field 'name': rejected value [l]; codes [Size.customer.name,Size.name,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [customer.name,name]; arguments []; default message [name],2147483647,2]; default message [Name should have at least 2 characters]"
}
```

#### Internationalization

##### Configuration 
- LocaleResolver
   - Default Locale - Locale.US
- ResourceBundleMessageSource

##### Usage
- Autowire MessageSource
- @RequestHeader(value = "Accept-Language", required = false) Locale locale
- messageSource.getMessage("helloWorld.message", null, locale)

