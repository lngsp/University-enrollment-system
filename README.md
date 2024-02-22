Simplified system that models a university student enrollment system and uses two databases to store the data. 
All data pertaining to students whose first name with A through M is recorded in database_1 and data for those students whose first name starts with N through Z is in database_2.

The proposed solution is a web server developed with the SpringBoot framework using the Java programming language. To meet the requirement of the problem, I used two MariaDB databases, which were created using SpringBoot JPA.

![image](https://github.com/lungu-stefania-paraschiva/University-enrollment-system/assets/102326882/87688e8e-2fdd-46f7-b1f4-838596489aee)

To optimize memory usage, I have the same entities for both database 1 and database 2, so the tables are the same in both databases. I used two different repositories for each entity, one connected to the first database and the second connected to the second.

Postman Tests: https://www.postman.com/stefania-lungu/workspace/university-enrollment-backend
