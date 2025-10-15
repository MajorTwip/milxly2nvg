# Quick Start Guide

This guide will help you get started with the XML Manipulator console application quickly.

## 1. Build the Application

```bash
mvn clean package
```

This creates an executable JAR file: `target/xml-manipulator-1.0.0.jar`

## 2. Run the Demo

The easiest way to see all features is to run the demo:

```bash
java -jar target/xml-manipulator-1.0.0.jar demo
```

This will:
- Create sample XML files
- Demonstrate reading, validation, transformation, prettification, and XPath extraction
- Show you how each feature works

## 3. Try It Yourself

A sample XML file is provided in `data/sample.xml`. Try these commands:

### Read the XML file
```bash
java -jar target/xml-manipulator-1.0.0.jar read data/sample.xml
```

### Validate the XML structure
```bash
java -jar target/xml-manipulator-1.0.0.jar validate data/sample.xml
```

### Prettify (format) the XML
```bash
java -jar target/xml-manipulator-1.0.0.jar prettify data/sample.xml data/formatted.xml
```

### Extract specific data with XPath
```bash
# Get all books
java -jar target/xml-manipulator-1.0.0.jar extract data/sample.xml "//book"

# Get a specific book by ID
java -jar target/xml-manipulator-1.0.0.jar extract data/sample.xml "//book[@id='2']"

# Get all titles
java -jar target/xml-manipulator-1.0.0.jar extract data/sample.xml "//title"

# Get books in a specific category
java -jar target/xml-manipulator-1.0.0.jar extract data/sample.xml "//book[@category='fiction']"
```

### Transform an XML file
```bash
java -jar target/xml-manipulator-1.0.0.jar transform data/sample.xml data/output.xml
```

## 4. Use Your Own XML Files

Place your XML files in the `data/` directory or specify the full path:

```bash
java -jar target/xml-manipulator-1.0.0.jar read /path/to/your/file.xml
```

## Common XPath Expressions

- `//elementName` - Find all elements with that name
- `//element[@attribute='value']` - Find elements with specific attribute value
- `//parent/child` - Find child elements under parent
- `/root/element[1]` - Find the first element
- `//element[text()='value']` - Find elements with specific text content

## Need Help?

Run without arguments to see usage information:

```bash
java -jar target/xml-manipulator-1.0.0.jar
```

Or check the main [README.md](README.md) for detailed documentation.
