Feature: Data Management Tool
  1. This feature is to validate Reqres site, GET and POST API's
  2. Reading Instrument and PositionDetails CSV files and generate Output CSV with Total Price

  Scenario: Navigate to Home Page
    Given User navigates to Home Page
    Then Verify API Details
    Then Navigate to Support Page

  Scenario: GET API Test
    Given Call To GET API
    Then Verify GetResponse

  Scenario: POST API Test
    Given Call To POST API
    Then Verify PostResponse

  Scenario: Read Input1.csv and Input2.csv and create output.csv
    Given Read CSV files "InstrumentDetails.csv" and "PositionDetails.csv"
    When Calculate Price as Quantity * UnitPrice
    Then Output CSV file "PositionReport.csv"
