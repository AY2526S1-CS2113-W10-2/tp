# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}
### Design 

### Architecture 

![img.png](team/Architecture/img.png)

The Architecture Diagram given above explains the high-level design of the App. Here is a quick overview of how these components interact with each other: 

'UserInterface' is the main class through which the app runs.

The bulk of the work done by the app is from these four components: 


'Storage': Reads data from, and writes data to the hard disk.

#### UserInterface component
API: UserInterface.java
#### Storage component
API: Storage.java 

#### User component
API: User.java

### Implementation 

#### Monthly Summary feature
The Monthly Summary feature enables users to view their spending and budget for each category within a specific month. It is facilitated by the Summary class in the summary package and works closely with the User, Transaction, Category, Month, and OutputManager classes.
When the user enters a command such in the form of summary <month>, the Parser class identifies the command and delegates it to the Summary class via the static method Summary.handleSummary().
This method validates user input, initializes a Summary instance with the user’s current Storage, and invokes Summary.showMonthlySummary(month).
The showMonthlySummary() method performs the following:
Retrieves all transactions from User.getTransactions().
Filters them by the month specified.
For each Category, calculates:
Total spending – the sum of all transactions under that category.
Budget – the user’s allocated budget for that category and month, retrieved via User#getBudgetAmount(cat, monthEnum).
Formats the summarized data through OutputManager.printSummary(). Displays the formatted summary to the user using OutputManager.printMessage().

Given below is an example usage scenario and how the Monthly Summary feature behaves at each step.

Step 1. The user launches the application. Arraylist<> banks, transactions and budgets are initialised via the following:

transactions = storage.loadTransactions();
banks = storage.loadBanks();
budgets = storage.loadBudgets();

![img.png](team/SummaryFeature/img.png)

Step 2. The user executes the summary <month> command to retrieve a spending summary of the month. For example, summary JAN retrieves the spending summary for the month of January.
The Parser recognises the "summary" keyword and delegates the command handling to Summary.handleSummary(commandlist). Inside handleSummary(), the method validates that the month arguemnt ("JAN") is valid. If valid, it instantiates a new Summary object using the current user's stored data.
Summary summary = new Summary(User.getStorage());

![img_1.png](team/SummaryFeature/img_1.png)

The showMonthlySummary("JAN") method is then called, which filters transactions from User.getTransactions() by month and category, retrieves the relevant budget allocations through User.getBudgetAmount(cat, Month.JAN), and aggregates the results.
The computed totals are passed to OutputManager.printSummary() to format the output, followed by OutputManager.printMessage() to display the results to the user.


The following sequence diagram shows how the summary command goes through the relevant components to display the output to the user.
![img_2.png](team/SummaryFeature/img_2.png)

The following activity diagram summarises what happens when the user executes summary JAN
![img_3.png](team/SummaryFeature/img_3.png)

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
