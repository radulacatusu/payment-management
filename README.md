# A financial institution’s account transactional system

This is a A Spring Boot Application for Securing a REST API with JSON Web Token (JWT)
* Sign up an user
* Login the user
* Create new account
* Get an account
* Load an account
* Transfer between accounts

## About

The application is developed with Spring Boot 2.1.0.RELEASE and maven as a building tool.
The database used is postgres.

## To run the application
Use one of the several ways of running a Spring Boot application. Below are just three options:

1. Build using maven goal: `mvn clean package` and execute the resulting artifact as follows `java -jar payment-management-1.0-SNAPSHOT.jar` or
2. On Unix/Linux based systems: run `mvn clean package` then run the resulting jar as any other executable `./payment-management-1.0-SNAPSHOT.jar`

## API Documentation

Swagger can be accessed at http://localhost:8080/swagger-ui.html

## Testing the API

1. Integration tests with @SpringBootTest over a in memory database
2. Mocking with @MockBean for the service layer
3. Integration Testing with @DataJpaTest for the persistence layer

## To manually test the application

I used POSTMAN and cURL to test the application. 

### Sign-up
```
POST - http://localhost:8080/sign-up
```
##### Body
```
{
  "name": "digipay",
  "password": "6LmexwZ723"
}
```
Response: 201 CREATED
```
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaWdpcGF5IiwiaWF0IjoxNTQ0MDM3MzgzLCJleHAiOjE1NDQwNDMzODN9.A8R8O717IkEDlKlgLMHT1aTxMJuB53RqP4pJmhsQ4WcpYKtM6v0JKw5yzETzqRTiJfrwjLzM1k-KBMXGi3yPaw"
}
```
### Login
```
POST - http://localhost:8080/access-tokens
```
##### Body
```
{
  "name": "digipay",
  "password": "6LmexwZ723"
}
```
Response: 200 OK
```
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaWdpcGF5IiwiaWF0IjoxNTQ0MDM3Mzk3LCJleHAiOjE1NDQwNDMzOTd9.4wbL3y-1YsO9b647-DZoNMWKlZIgXE4fBDz2hRYtKg0WZQN2iQNet-knUuvrVhTvKg4pdGmzi5xAscHEiGEdNg"
}
```
### Create new account
```
POST - http://localhost:8080/accounts
```
##### Body
```
{
	"nameOnAccount": "Albert Einstein Third",
	"currencyId": "EUR"
}
```
Response: 201 CREATED
```
{
    "id": "3"
}
```
### Get an account
```
GET - http://localhost:8080/accounts/1
```
Response: 200 OK
```
{
    "id": 1,
    "currencyId": "EUR",
    "balance": "1000000.00",
    "balanceStatus": "DR",
    "balanceTimestamp": "2018-12-05T19:16:03.344+0000",
    "dateOpened": "2018-12-05T19:16:03.344+0000",
    "accountType": "LEDGER",
    "nameOnAccount": "Ledger"
}
```
### Load an account
```
POST - http://localhost:8080/accounts/2/load
```
##### Body
```
{
	"amount": 50.00,
	"currencyId": "EUR"
}
```
Response: 200 OK
```
{
    "accountId": 2,
    "balance": "290.00",
    "balanceStatus": "CR"
}
```
### Transfer between accounts
```
POST - http://localhost:8080/transfers
```
##### Body
```
{
    "debitAccountId": 2,
    "creditAccountId": 3,
    "amount": 50.00,
    "currencyId": "EUR"
}
```
Response: 200 OK

## Authentication for the API

TODO - I want to add refresh token in the response of sign-up and login


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Radu Stefan Lacatusu** - [radulacatusu](https://github.com/radulacatusu/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details