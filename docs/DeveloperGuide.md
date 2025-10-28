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

---

#### Delete Transaction Feature
The DeleteTransaction feature allows users to remove a specific transaction from their financial record.
This command improves user flexibility by enabling correction of mistakes or removal of outdated records.
The command follows the syntax:

`delete <transaction-id>`

Example: `delete 3`
deletes the 3rd transaction listed under the user’s records.

The feature is implemented using the Command Pattern, 
where each user command is represented as a class that implements the `Command` interface.
`DeleteTransactionCommand` is the class responsible for executing the delete operation.

Given below is an example usage scenario and how the delete mechanism behaves at each step.

**Step 1: User launches the application**  
The user starts the program, and all previously saved transactions are 
loaded from `transactions.txt` into memory by the `Storage` class.

**Step 2: The user executes the delete command**  
The user enters: `delete 2`. The Parser identifies `delete` as the command word 
and passes control to the `DeleteTransactionCommand`, with `2` as the argument.

**Step 3: The command validates the input**    
`DeleteTransactionCommand`  checks that: 
- Only one argument is inputted
- The argument is a valid integer. 
- The transaction list is not empty.

If validation passes, execution proceeds.
Otherwise, a FinanceException is thrown, e.g.:  
`Error deleting transaction: Usage: delete <transaction_index>`

**Step 4: The transaction is deleted**  
DeleteTransactionCommand calls: `User.deleteTransaction(index)`  
This method removes the transaction at index 2 from the user’s list. 
It then overwrites `transactions.txt` with the updated list, ensuring persistence across sessions. 
At the same time, The user interface then prints: `"Deleted transaction: ..."`


The following sequence diagram shows how delete operation goes through the components:
![Delete_Transaction_Sequence_Diagram.png](team/CommandFeature/deleteCommand/Delete_Transaction_Sequence_Diagram.png)

The following activity diagram summarizes what happens when a user executes a new command:
![deleteCommandActivity.png](team/CommandFeature/deleteCommand/deleteCommandActivity.png)

---

#### Bank Login Feature 

#### Purpose

The bank login feature ensures that user operations (such as adding transactions, budgets, or performing account actions) are scoped to a specific bank account context. By requiring the user to log in to a bank before executing sensitive operations, the system maintains data integrity and simplifies account management. This feature allows users to interact with multiple bank accounts one at a time, with clear authorization boundaries for all financial actions. [file:3]


**Process of Logging Into a Bank**

1. **Login Command Parsing**
    - The user enters a command: `login <bankId>`.
    - The command is parsed and identified as `login` within the Parser class's switch-case structure.
2. **Login Branch Logic**
    - If the user is not already logged in (`User.isLoggedIn == false`):
        - The provided `<bankId>` is parsed from the command arguments.
        - The bank ID is validated against the available bank list (`User.banks`).
        - If the bank exists, set `User.currBank` to that bank and `User.isLoggedIn = true`.
        - Print a success message with the bank’s details.
    - If the user is already logged in, print a message requesting logout before logging in to a different bank.
    - If the bank is not found, a `FinanceException` is thrown and an error message shown. [file:3]

---

**Operations Allowed While Logged In**

The following operations may only be performed after successful bank login, with checks for `User.isLoggedIn`:

| Operation           | How It Is Performed (Command)   | Execution Logic                                                           |
|---------------------|---------------------------------|---------------------------------------------------------------------------|
| Add Transaction     | `add <args>`                    | Creates and executes `AddTransactionCommand`.                             |
| Delete Transaction  | `delete <args>`                 | Creates and Executes `DeleteTransactionCommand`.                          |
| List Budgets        | `listbudget`                    | Creates and Executes `ListBudgetsCommand` to show current bank’s budgets. |
| Deposit Funds       | `deposit <args>`                | Creates and Executes `ATM` command in deposit mode for current bank.      |
| Withdraw Funds      | `withdraw <args>`               | Creates and Executes `ATM` command in withdraw mode for current bank.     |
| Summary             | `summary`                       | Creates and Executes `SummaryCommand` displaying bank-level summary.      |

---

**Special Notes**
- Some commands are only available when not logged in (e.g., `listbanks`, which lists all banks to pick from before login). The Parser enforces this by checking login state before running those operations.
- The `logout` command sets the user's state back to not logged in and clears the current bank context.

---

**Implementation Notes**

- All commands relying on bank context include a check of `User.isLoggedIn` before execution.
- When a user is logged in, transaction and budget operations always apply to the selected bank (`User.currBank`).
- Error handling and state checks are performed for every operation in the Parser switch-case block, ensuring unauthorized actions are blocked and informative feedback is given.

The following class diagram shows the relationships between the various classes involved in the user login mechanism:
![uml_class_login.png](team/LoginFeature/uml_class_login.png)

The following sequence diagram shows how various classes and methods interact together throughout the user login mechanism:
![uml_sequence_login.png](team/LoginFeature/uml_sequence_login.png)

---

#### Deposit & Withdrawal Feature Developer Documentation

**Purpose**

The deposit and withdrawal feature allows users to modify the balance of their selected bank account securely and accurately. All operations are strictly performed in the context of the logged-in user and their active bank account, ensuring correct tracking of account transactions and maintaining financial integrity [file:3][file:12].

---

**Process: How Deposit & Withdrawal Work**

1. **User Input Detection**
    - The user enters a command: `deposit <amount>` or `withdraw <amount>`.
    - The command is parsed by the `Parser` class and matched in a switch-case structure.

2. **Command Validation**
    - The parser checks `User.isLoggedIn`. If not, an error message is shown and the command is aborted.
    - If logged in, the `ATM` command is instantiated:
        - For deposit: `ATM(arguments, User.currBank, true, false)`
        - For withdraw: `ATM(arguments, User.currBank, false, true)`

3. **Execution Flow**
    - `ATM.execute()` checks the type (deposit/withdrawal), validates the amount, and updates the bank account balance.
    - The transaction may be recorded in the user's transaction history.
    - Feedback is provided to the user (success, updated balance, or error message).
    - Error conditions (invalid amount, insufficient funds) are caught, and a `FinanceException` may be thrown.

---

**Operations Carried out**

| Operation        | Command                      | Execution Logic                                                                 |
|------------------|-----------------------------|----------------------------------------------------------------------------------|
| Deposit Funds    | `deposit <amount>`          | Creates and executes `ATM` in deposit mode for the logged-in bank. Updates balance if amount is valid. [file:3][file:12] |
| Withdraw Funds   | `withdraw <amount>`         | Creates and executes `ATM` in withdrawal mode. Deducts funds if available; throws error if insufficient. [file:3][file:12] |

---

**Implementation Notes**

- All operations must pass `User.isLoggedIn` check.
- Only the bank currently referenced by `User.currBank` is affected.
- The `ATM` class centrally handles both features and manages error handling for account balances.

---

The following class diagram shows the relationships between the various classes involved in the adding of budgets:
![uml_class_atm.png](team/ATMFeature/uml_class_atm.png)

The following sequence diagram shows how various classes and methods interact together throughout the adding of budget mechanism:
![uml_sequence_atm.png](team/ATMFeature/uml_sequence_atm.png)

---

#### Adding of budgets feature
**What it does**

The add budget mechanism enables users to create a budget entry that specifies a spending limit for a given category in 
a particular month within the context of a selected bank account. This helps users track, control, and manage their 
finances by planning expenses ahead of time with category-specific monetary limits.

**How it works**

**1. Command Parsing**:

When a user issues the command `addBudget <category> <amount> <month>`, in the application interface, the `Parser` class 
detects this command, by its first argument `addBudget` in its switch-case structure.

The command's arguments (expected: category, amount, and month) are extracted from the user input.

**2.Command Initialization**:

An instance of the `AddBudgetCommand` class is created with the parsed arguments.

**3.Execution Flow in AddBudgetCommand**:

**Argument Validation**: It checks that all required arguments are provided. If fewer than 3 arguments are present, an error (exception `FinanceException` will be thrown) with the correct usage instructions is returned.

**Category Processing**: The first argument is converted into a valid Category enum. Invalid categories cause an error (exception `FinanceException` will be thrown) .

**Amount Processing**: The second argument is parsed as a float representing the budget amount. It must be a non-negative valid number, or else an error (exception `FinanceException`) will be thrown

**Month Processing**: The third argument is interpreted as a Month enum. Invalid month inputs are rejected with an error (exception `FinanceException` will be thrown)

**Budget Object Creation**: Using the validated inputs and the currency from the currently logged-in bank, a new Budget object is initialized.

**Budget Addition**: The new budget is added to the user's budget list through User.addBudget(), which also triggers persistent storage saving.

**User Feedback**: Upon success, the user is notified with a message specifying the budget amount, currency, category, and month set.

**4. Data Persistence and State Update**:

The budget is stored in an internal list as `budgets` attribute in the `User` class and saved to persistent storage (text file named `budgets.txt`) to maintain state across sessions.

***This modular, error-checked approach ensures that budget entries are valid, tied to a bank context, and immediately available for the user to manage their finances effectively***.

The following class diagram shows the relationships between the various classes involved in the adding of budgets:
![uml_class_add_budget.png](team/BudgetFeature/uml_class_add_budget.png)

The following sequence diagram shows how various classes and methods interact together throughout the adding of budget mechanism:
![uml_sequence_add_budget.png](team/BudgetFeature/uml_sequence_add_budget.png)

---

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
