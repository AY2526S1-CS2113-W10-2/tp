# User Guide

## Introduction

TrackStars is a financial tracker targeted towards outgoing NUS exchangers to enable them to keep track of their finances not only in Singapore, but also while travelling around South East Asia. 
As such, TrackStars can operate with foreign currencies that are from popular South East Asian travel destinations. 

---
#### Table of Contents

- [How To Get Started](#how-to-get-started)
- [Features](#features)
  - [Adding a Bank Account: `addbank`](#adding-a-bank-account-addbank)
  - [Logging Into a Bank Account: `login`](#logging-into-a-bank-account-login)
  - [Logging Out of a Bank Account: `logout`](#logging-out-of-a-bank-account-logout)
  - [Listing Recent Transactions: `list`](#listing-recent-transaction-list)
  - [Listing Owned Bank Accounts: `listBanks`](#listing-owned-bank-accounts-listbanks)
  - [Adding a Transaction: `add`](#adding-a-transaction-add)
  - [Deleting a Transaction: `delete`](#deleting-a-transaction-delete)
  - [Adding a Budget: `addBudget`](#adding-a-budget-addbudget)
  - [Listing Budgets: `listBudget`](#listing-budgets-listbudget)
  - [Depositing Money: `deposit`](#depositing-money-into-current-bank-account-deposit)
  - [Withdrawing Money: `withdraw`](#withdrawing-money-from-current-bank-account-withdraw)
  - [Searching Transactions: `search`](#searching-transactions-search)
  - [Filtering Transactions: `filter`](#filtering-transactions-filter)
  - [Viewing Summary of Recent Transactions: `summary`](#view-summary-of-recent-transactions-and-usage-summary)
  - [Exiting the Programme: `exit`](#exiting-the-programme-exit)
  - 
- [Saving the Data](#saving-the-data)
- [FAQ](#faq)
- [Command Summary](#command-summary)


#### How To Get Started

---
1. Ensure that you have Java 17 or above installed.
2. Download the latest JAR file of `TrackStars` from [here](http://link.to/duke).
3. Copy the JAR file into an empty folder 
4. Open the command terminal, cd into the folder you put the JAR in.
5. Use '-java jar TrackStars.jar' command to run the application.
6. A warning may appear depending on your terminal formatting. If the symbols do not display correctly
consider running "chcp 65001" in cmd, then repeat step 5.
7. Start typing commands into the terminal and execute it by pressing Enter
8. 'exit' will exit the program

## Features

---

### Adding a bank account: `addbank`
Adds a new bank account to the user in the specified currency 

Format: `addbank AMOUNT CURRENCY`

* The `AMOUNT` must be a positive real number in the currency that you want the bank to be in
* The `CURRENCY` must be the three letter form of the currency you intend. 
For example, MYR for Malaysian Ringgit, THB for Thai Baht. Please see the Glossary within the DeveloperGuide for the full list of supported currencies. 

Example of usage:  
`addbank 5000 SGD`  
`addbank 250000 IDR`

---

### Logging into a bank account: `login`
Logs into a pre-existing bank account. If you do not have a bank account yet, you must create one by using 'addbank'.
Logging into a bank account allows the user to do bank specific commands like adding transactions and setting budgets. Once logged in, there is no need to specify the currency of the amounts as the 
currency is tied to that specific bank account you are logged into.

Format: `login INDEX`
* The `INDEX` refers to the index number of that bank account in `listbanks`


Example of usage:

`listbanks` to see current bank accounts  
`login INDEX` to log into that specific bank account

---

### Logging out of a bank account: `logout`
Logs out of the currently logged-in account. Does nothing of not currently logged into an account.


Format: `logout`

Example of usage:  

`logout` to see log out of the currently logged-in bank

---

### Listing recent transaction: `list`
Lists the 10 most recent transactions, across all accounts.

Format: `list`

Example of usage:

`list` to see recent transactions

---

### Listing owned Bank Accounts: `listBanks`
Lists all registered bank accounts, with their balance and exchange rate

Format: `listBanks`

Example of usage:

`listBanks` to see recent all bank accounts

---

### Adding a transaction: `add`
Adds a new transaction to the list of transactions. This works only when logged into a bank, 
and the transaction is tied to the currency of the bank 

Format: `add TAG(optional) CATEGORY AMOUNT DATE`

* The TAG is optional and can be used to describe the transaction (e.g., "Milo", "Lunch"). 
If omitted, "unnamed" will be assigned as the default tag.
* The `CATEGORY` must be one of these: food, transport, entertainment, recreation 
* The `AMOUNT` must be a positive real number in the currency of the bank you are logged into.
* The `DATE` must be in the form DD/MM

Example of usage: 

`add transport 2.50 15/10`  
`add entertainment 15 25/12`  
`add 'Milo' food 1.2 01/11`

---

### Deleting a transaction: `delete`
Deletes a previously keyed in transaction from the list. The 'delete' feature deletes the transaction at the specified index. This index refers to the index number shown in the transactions list.
You can view this by using the command 'list'. You must be logged into a bank account to delete transactions from that bank. 

Format: `delete INDEX`
* The `INDEX` refers to the index of that transaction in 'list'

Example of usage:  
`list` followed by `delete 2` to delete the 2nd transaction in that list of that bank

---

### Adding a Budget: `addBudget`
Adds a new budget for a specific category of spending. Used to keep track of total amounts spent on different categories over time.

Format: `addBudget CATEGORY AMOUNT MONTH`

* The `CATEGORY` must be one of these: food, transport, entertainment, recreation
* The `AMOUNT` must be a positive real number in the currency of the bank you are logged into.
* The `MONTH` either the full name or the shortened three letter form of each month. For example, JAN or January, FEB or February, APR or April, etc.

Example of usage:

`addBudget transport 40 JAN` to indicate a target spend of 40 on transport in January.  
`addBudget entertainment 75 MAR` to indicate a target spend of 75 on transport in March.

---

### Listing Budgets: `listBudget`
Lists all budgets, and their current utilisation. Used as brief summary of spending by category.

Format: `listBudget MONTH`

* The `MONTH` either the full name or the shortened three letter form of each month. For example, JAN or January, FEB or February, APR or April, etc.
* 
Example of usage:

`listBudget JAN` to indicate show budget utilisation for January

---

### Depositing money into current bank account: `deposit`
Adds to the balance of a current signed in account. Used to track income.

Format: `deposit AMOUNT`

* The `AMOUNT` must be a positive real number in the currency of the bank you are logged into.

Example of usage:

`deposit 1500` deposits 1500 into the account, in whatever currency that account trades in

---

### Withdrawing money from current bank account: `withdraw`
Subtracts from the balance of a current signed in account. Used to track outgoing payments not assigned to a specific category.

Format: `withdraw AMOUNT`

* The `AMOUNT` must be a positive real number in the currency of the bank you are logged into.

Example of usage:

`withdraw 1500` withdraws 1500 from the account, in whatever currency that account trades in

---

### Searching Transactions: `search`
Searches for transactions containing a specific keyword in the category or tag/description.
Works only when logged into a bank account.

Format: `search KEYWORD`
* The `search` command is **case-insensitive**.

Example of usage:

`search milo` to search for transactions with tag milo.  

---

### Filtering Transactions: `filter`
Filters transactions by category, cost, or date range. Works only when logged into a bank account.

Format: `filter FILTER_TYPE ...`
* `FILTER_TYPE` includes `category`, `cost`, `date`
* `category` filters and displays transactions based on selected category.
  * Valid categories: `FOOD`, `TRANSPORT`, `ENTERTAINMENT`, `RECREATION`.
  * Syntax: `filter category <CATEGORY>`.
* `cost` filters and displays transactions where the value is between `MIN` and `MAX` (inclusive).
  * Both `MIN` and `MAX` must be positive numbers. 
  * `MAX` must not be smaller than `MIN`.
  * Syntax `filter cost <MIN> <MAX>`.
* `date` filters and displays transactions between `START_DATE` and `END_DATE` (inclusive).
  * Dates must be in the format `DD/MM`.
  * Syntax `filter date <start(DD/MM)> <end(DD/MM)>`

Example of usage:

`filter category food` to filter by category  
`filter cost 10 50` to filter by cost range  
`filter date 01/01 31/01` to filter by date range  

---

### View Summary of Recent Transactions and Usage: `summary`
Prints a summary of individual transactions and total values spent on different categories in the selected month of the current year.

Format: `summary MONTH`
* The `MONTH` either the full name or the shortened three letter form of each month. For example, JAN or January, FEB or February, APR or April, etc.  

Example of usage:

`summary FEB` to see summary from the most recent February

---

### Exiting the programme: `exit`
Exits the programme

Format: `exit`

---

### Displaying list of commands: `help`
Shows all the commands and formats that users can input.

Format: `help`

---

### Saving the data
TrackStars automatically stores the previously listed transactions, banks and budgets into the hard disk automatically. There is no need to manually key in a command to store data. 

---

## FAQ

**Q**: Do the exchange rates on this application fluctuate? 

**A**: No, the exchange rates on TrackStars are hardcoded into the programme. One of the key requirements of this project is that it must be able to run  offline. Hence, live updates of 
exchange rates are difficult to implement. We have taken the latest exchange rates as of 27 October 2025.

**Q** Can I attach my bank account or transactions to any currency I want?

**A** No, TrackStars only has a limited number of currencies. In the spirit of narrowing down the scope of our targeted user, we decided to implement
currencies of popular travel destinations in South East Asia. The following currencies are available: Thai Baht (BHT), Vietnamese Dong (VND), Malaysian Ringgit (MYR), Indonesian Rupiah (IDR), Singapore Dollar (SGD) and Japanese Yen (JPY).
Japan is not a South East Asian country, but is included since it is such a popular travel destination.

**Q** Can the parameters of a command be input in any order?

**A** No, the parameters of the commands must be input in the specific order that the User Guide mentions.

**Q** When keying in transactions, must the dates be in the format dd/mm? 

**A** Yes, the dates must be in that specific format (dd/mm). The programme is not able to convert 10 January to 10/1/.

**Q** Why can't I input the year in my transactions? Why am I relegated to only transactions in the current year?

**A** This is a reasonable question, but consider the targeted user of TrackStars. TrackStars aims to serve NUS Exchangers to aggregate financial data into a single application. The NUS academic semester is from either August - December (Sem 1) or January - May (sem 2). As such, it seemed redundant to the developers to include a year, as NUS Exchangers stay at NUS for at most, 6 months, and that stint never crosses into a new year. Meaning, it is expected that if an NUS exchanger partakes in an exchange programme in 2025, they will leave by 2025.

## Command Summary

Commands that work while **logged into** a bank account:

* Add transaction `add CATEGORY AMOUNT DATE'
  * e.g, add food 25.50 10/1

* Add budget 'addbudget AMOUNT MONTH'
  * e.g, addbudget 125 JAN

* Deposit 'deposit AMOUNT'
  * e.g, deposit 5000

* Withdraw 'withdraw AMOUNT'
  * e.g, withdraw 100 

* List transactions 'list'
* Delete transaction 'delete INDEX'
  * e.g, delete 3 

* Search transactions 'search KEYWORD'
  * e.g., search milo

* Filter transactions by category, cost, or date 'filter <filter_type> ...'
  * e.g., filter category food
  * e.g., filter cost 10 50
  * e.g., filter date 01/01/2025 31/01/2025

* Summary Page 'summary MONTH'
  * e.g, summary JAN

* Exit programme 'exit'

Commands that work while **logged out** of a bank account:
* Summary Page 'summary MONTH'
  * e.g, summary JAN 

* Login to bank 'login INDEX'
  * e.g, login 0

* Summary Page for currency 'summary MONTH CURRENCY'
  * e.g, summary JAN MYR

* Add bank account 'addbank INITIAL_DEPOSIT CURRENCY'
  * e.g, addbank 5000 THB
* List bank accounts 'listbanks'
* Exit programme 'exit'


