# SpringBoot-Ignite
This is a small example that deploy Ignite DB on Spring Boot. I use this project to learn Ignite DB @ Spring Boot.

This is a RESTful Web Service with api:
./register          : for register with Json { "username":"XXX" , "password":"XXX"}
./login             : for login with Json { "username":"XXX" , "password":"XXX"}
./secure/users/user : for JWT authorization testing. It is invalid for accessing unless Http Request carries valid JWT token

From now on, the project can:
1. Turn on a ignite node and a cache named "PersonCache" is added
2. Register with "username" and "password"
3. Login with "username" and "password"
4. Jwt authorization testing

