# jdbc

## Prerequisites

- Java 21 or later
- H2 Database Engine
- Maven
- Database script run in `src/main/resources/database.sql`

## Database commands

```sh
java -cp h2-2.3.232.jar org.h2.tools.Shell
java -cp h2-2.3.232.jar org.h2.tools.Server
```

## Run from command line

- Database MUST be initialized first

```sh
mvn clean package exec:java -Dexec.mainClass="com.github.alsaghir.App"
```