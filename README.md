
# Selenium Hybrid Framework using Java+TestNG+PageObjectModal

The Selenium Hybrid Framework is designed to provide a scalable and maintainable solution for automated web testing. It combines the power of Java programming language, TestNG testing framework, Maven for build management, Page Object Model design pattern for improved code organization, Allure Reports for enhanced reporting, and Jenkins for seamless CI/CD integration.


## Prerequisites
- Java jdk-1.8 or higher
- Apache Maven 3 or higher
- Ecplise IDE
- Allure Report latest version

## Setup and Installation

- Clone this repository to your local machine.

```bash
  git clone repositoryURL
```
- Download the Ecplise IDE, Configure JDK, Maven in your local system.

- Open the project in Eclipse IDE
- Download the TestNG plugin from the Eclipse MarketPlace
- Set the Path of the Java JDK and Maven to System Enviornment variable
- Check the Java and Maven version

```bash
  java --version
  maven --version
```
## Running Tests

- Navigate to the project root directory.
- To run tests, run the following maven comman in CMD

```bash
  mvn clean test -Dbrowser=chrome -Denv=local -Dtestng.file=add_path_to_suite_xml_file
```


## Technology Stack
- Selenium
- TestNG
- Maven
- Page Object Design Pattern
- Allure Reports
- Integration with Jenkins

## About the Project
I have used an Ecommerce Application to automate the following scenarios:
- Login and Register a user
- Purchase the Product using Member user
- Purchase the Product as Guest user
- Search product functionality
- Error message validations on Login form

## Page Object Modal Design
The Page Object Model (POM) design pattern is used to create a structured representation of web pages.

## Read Data from property File
Created a generic method for Json and property files to read data using TestNG getData.

## Apache POI 
Created a generic method and used a Apache POI depencency to read the data from the Excel files.





## Allure Report

Used an Allure Report to provide detailed and interactive test execution reports.
After running the tests, you can generate Allure Reports using the following command:

- First clean the allure report before Test Execution.

```bash
  allure generate --clean -o allure-result
```
- After the tests are executed then add below command to open 

```bash
  allure serve allure-results

```

## Jenkins CI 
This framework is integrated with Jenkins to automate the testing process.