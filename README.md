# Currency Exchange and Discount Calculation Application

This is a Spring Boot application that integrates with a third-party currency exchange API to retrieve real-time
exchange rates. The application calculates the total payable amount for a bill in a specified currency after applying
applicable discounts. It exposes an API endpoint to submit a bill and get the payable amount in another currency.

## Table of Contents

1. [Requirements](#requirements)
2. [Setup](#setup)
3. [Running the Application](#running-the-application)
4. [API Endpoint](#api-endpoint)
5. [Testing](#testing)
6. [Code Coverage](#code-coverage)
7. [Static Code Analysis](#static-code-analysis)
8. [Caching](#caching)
9. [Assumptions](#assumptions)
10. [Follow-up Discussion](#follow-up-discussion)

## Requirements

- Java 17 or higher
- Maven 3.x
- IntelliJ IDEA (or any other IDE)
- An API key from a currency exchange service (e.g., [ExchangeRate-API](https://www.exchangerate-api.com/))

## Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/currency-exchange-discount-calculation.git

2. **Open the Project in IntelliJ IDEA:**
   ```
   Open IntelliJ IDEA.
   Select File -> Open and navigate to the cloned repository folder.
   IntelliJ will recognize the Maven project and load it.

## Running the Application
**Run the Application:**
   ```
   In IntelliJ, navigate to the CurrencyExchangeApplication.java file in src/main/java/com/example/currencyexchange.
   Right-click on the file and select Run 'CurrencyExchangeApplication'.
   
   ```

**Access the Application:**

The application will start on port 8080 by default.
You can access the API endpoint at http://localhost:8080/api/calculate.