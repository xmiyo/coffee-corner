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

Coffee (only) can be ordered with max 2 extra ingredients:
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee with extra milk and special roast  
```

### Available products
* Program accepts product names defined in `src/main/resources/products.csv` file.
* Whenever product list is changed, and you run project from command line, rebuild project to refresh product list.

### Constraints
* Only coffee can be ordered with extra ingredients
* Max extra ingredients size is 2.
* Extra ingredient cannot be ordered as primary product
* Primary product cannot be ordered as extra to another product
* Product name and price gets updated when extra ingredient is added
* Shorter name can be used to identify product but must be unique. Otherwise, corresponding error will be printed to console

### Bonus program

1. Any beverage can be collected for free when product name is prefixed with 'bonus' string
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee with extra milk, bonus large coffee with special roast  
```
2. When ordering drink and snack, extra ingredient (if any) in drink, is for free
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee with extra milk, bacon roll  
```
3. When ordering drinks, every 5th drink is free
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee with extra milk, small coffee, small coffee, small coffee, small coffee  
```
4. When ordering drinks, drinks not used for bonus are exchanged to bonus points. Bonus points can be exchanged to free dring (see first rule of bonus program)
```
java -jar target/coffee-corner-1.0-SNAPSHOT.jar small coffee with extra milk, small coffee, small coffee, small coffee, small coffee, orange juice, small coffee
```





