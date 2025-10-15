# XML Manipulator Console Application

A Spring Boot console application for manipulating XML files with various operations including reading, validation, transformation, formatting, and XPath extraction.

## Features

- **Read XML**: Parse and display XML file contents
- **Validate XML**: Check if XML files are well-formed
- **Transform XML**: Copy and modify XML files
- **Prettify XML**: Format XML files with proper indentation
- **XPath Extraction**: Extract specific data using XPath expressions
- **Demo Mode**: Run a complete demonstration of all features

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Building the Application

```bash
mvn clean package
```

This will create an executable JAR file in the `target` directory.

## Usage

### Running the Application

```bash
java -jar target/xml-manipulator-1.0.0.jar <command> [options]
```

### Commands

#### 1. Read XML File
```bash
java -jar target/xml-manipulator-1.0.0.jar read <file-path>
```
Example:
```bash
java -jar target/xml-manipulator-1.0.0.jar read data/sample.xml
```

#### 2. Validate XML File
```bash
java -jar target/xml-manipulator-1.0.0.jar validate <file-path>
```
Example:
```bash
java -jar target/xml-manipulator-1.0.0.jar validate data/sample.xml
```

#### 3. Transform XML File
```bash
java -jar target/xml-manipulator-1.0.0.jar transform <input-file> <output-file>
```
Example:
```bash
java -jar target/xml-manipulator-1.0.0.jar transform data/input.xml data/output.xml
```

#### 4. Prettify XML File
```bash
java -jar target/xml-manipulator-1.0.0.jar prettify <input-file> [output-file]
```
Example:
```bash
java -jar target/xml-manipulator-1.0.0.jar prettify data/sample.xml data/formatted.xml
```

If output file is not specified, it will create a new file with `_pretty.xml` suffix.

#### 5. Extract Data with XPath
```bash
java -jar target/xml-manipulator-1.0.0.jar extract <file-path> <xpath-expression>
```
Example:
```bash
java -jar target/xml-manipulator-1.0.0.jar extract data/sample.xml "//book[@id='1']"
```

#### 6. Run Demo
```bash
java -jar target/xml-manipulator-1.0.0.jar demo
```

This will create sample XML files and demonstrate all features.

## Running Tests

```bash
mvn test
```

## Project Structure

```
milxly2nvg/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/milxly2nvg/xmlmanipulator/
│   │   │       ├── XmlManipulatorApplication.java
│   │   │       ├── XmlConsoleRunner.java
│   │   │       └── service/
│   │   │           └── XmlManipulatorService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/milxly2nvg/xmlmanipulator/
│               └── XmlManipulatorServiceTest.java
├── data/                      # Directory for XML files
├── pom.xml
└── README.md
```

## Technologies Used

- **Spring Boot 3.1.5**: Application framework
- **Java 17**: Programming language
- **Maven**: Build and dependency management
- **DOM Parser**: XML parsing
- **XPath**: XML querying
- **JAXB**: XML binding
- **Jackson XML**: XML serialization/deserialization
- **Lombok**: Reducing boilerplate code

## Example XML File

```xml
<?xml version="1.0" encoding="UTF-8"?>
<bookstore>
    <book id="1" category="fiction">
        <title>The Great Adventure</title>
        <author>John Doe</author>
        <year>2023</year>
        <price>29.99</price>
    </book>
</bookstore>
```

## License

This project is open source and available under the MIT License.