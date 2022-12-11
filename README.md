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

Coffee (only) can be ordered with extra ingredient: 
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee with extra milk, large coffee with special roast, bacon roll  
```

### Available products
* Program accepts product names defined in `src/main/resources/products.csv` file.
* Whenever product list is changed, and you run project from command line, rebuild project to refresh product list.

### Constraints
* Only coffee can be ordered with extra ingredient
* Extra ingredient cannot be ordered as primary product
* Primary product cannot be ordered as extra to another product
* Only one extra can be added to main product
* Product name and price gets updated when extra ingredient is added
* Shorter name can be used to identify product but must be unique. Otherwise, corresponding error will be printed to console




