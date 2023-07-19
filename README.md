# restapi-person-app
Simple rest Api with PostgreSQL with two endpoints, add person and match person by personal id or and birthdate. Includes unit, integration tests and logging that logs service class actions to log file.
Project was built using these technologies:
+ **Java Spring Boot**
+ **PostgreSQL**
## Installation
You will need Docker for this installation 
1. Download repository `https://github.com/janisruduks/restapi-person-app`
2. Create a Docker container for the PostgreSQL database: `docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=password -d postgres` Note: You can modify the provided fields but remember to update the corresponding Spring application properties.
3. Finally, run the Spring Boot application using your preferred method (e.g., IDE or command line).
## Key features
- **Save Person:** Add a new person to the database.
- **Delete Person:** Remove an Person from the database.
- **Query Persosn by ID or BirthDate:** Retrieve and display detailed information about a specific person.
- **Log Saving, Deleting and Querying:** Update the details of an existing employee.
