# Charlene's Coffee Corner

## Overview
The following repo contains example of program whose output is formatted like a receipt received at a supermarket.

## Guidelines

### Requirements
* java version 17
* Apache Maven version 3.x

### Build project

Build project by running maven command in project root directory
```
mvn clean package
```

### Run project
Run project by running java command in project root directory
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar <comma-separated product list> 
```
Example usage:
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee, large coffee, bacon roll  
```

### Available products
* Program accepts product names defined in `src/main/resources/products.csv` file.
* Whenever product list is changed and you run project from command line, rebuild project to refresh product list.




