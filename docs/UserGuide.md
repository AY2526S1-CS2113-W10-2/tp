# User Guide

## Introduction

TrackStars is a financial tracker targeted towards incoming NUS exchangers to enable them to keep track of their finances not only in Singapore, but also while travelling around South East Asia. 
As such, TrackStars can operate with foreign currencies that are from popular South East Asian travel destinations. 

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
2. Download the latest JAR file of `TrackStars` from [here](http://link.to/duke).
3. Copy the JAR file into an empty folder 
4. Open the command terminal, cd into the folder you put the JAR in, then use '-java jar TrackStars.jar' command to run the application
5. Start typing commands into the terminal and execute it by pressing Enter
6. 'exit' will exit the program

## Features 

{Give detailed description of each feature}

### Adding a bank account: 'addbank'
Adds a new bank account to the user in the specified currency 

Format: 'addbank a/AMOUNT c/CURRENCY'

* The 'AMOUNT' must be a positive real number in the currency that you want the bank to be in
* The 'CURRENCY' must be the three letter form of the currency you intend. For example, MYR for Malaysian Ringgit, THB for Thai Baht. 

Example of usage: 
'addbank 5000 SGD'

'addbank 250000 IDR'

### Adding a transaction: `add`
Adds a new transaction to the list of transactions. This works only when logged into a bank, and the transaction is tied to the currency of the bank 

Format: `add c/CATEGORY a/AMOUNT d/DATE`

* The `CATEGORY` must be one of these: food, transport, entertainment, recreation 
* The `MONTH` must be the shortened three letter form of each month. For example, JAN for January, FEB for February, APR for April.
* The 'AMOUNT' must be a positive real number in the currency of the bank you are logged into.
* The 'DATE' must be in the form dd/mm/yyyy

Example of usage: 

`add transport 2.50 15/10/2025'

'add entertainment 15 25/12/2025'

## FAQ

**Q**: Do the exchange rates on this application fluctuate? 

**A**: No, the exchange rates on TrackStars are hardcoded into the programme. One of the key requirements of this project is that it must be able to run while offline. Hence, live updates of 
exchange rates are difficult to implement. We take the latest exchange rates as of 27 October 2025.

**Q** Can I attach my bank account or transactions to any currency I want?

**A** No, TrackStars only has a limited number of currencies. In the spirit of narrowing down the scope of our targeted user, we decided to implement
currencies of popular travel destinations in South East Asia. The following currencies are available: Thai Baht (BHT), Vietnamese Dong (VND), Malaysian Ringgit (MYR), Indonesian Rupiah (IDR), Singapore Dollar (SGD) and Japanese Yen (JPY).
Japan is not a South East Asian country, but is included since it is such a popular travel destination.

**Q** Can the parameters of a command be input in any order?

**A** No, the parameters of the commands must be input in the specific order that the User Guide mentions.

## Command Summary

Commands that work while logged into a bank account:

* Add transaction `add c/CATEGORY a/AMOUNT d/DATE'
* e.g, add food 25.50 10/1/2025

* Add budget 'addbudget a/AMOUNT m/MONTH'
* e.g, addbudget 125 JAN

* Deposit 'deposit a/AMOUNT'
* e.g, deposit 5000

* Withdraw 'withdraw a/AMOUNT'
* e.g, withdraw 100 

* List transactions 'list'
* Delete transaction 'delete INDEX'
* e.g, delete 3 

* Summary Page 'summary m/MONTH'
* e.g, summary JAN

* Exit programme 'exit'

Commands that work while logged out of a bank account:
* Summary Page 'summary m/MONTH'
* e.g, summary JAN 

* Login to bank 'login INDEX'
* e.g, login 0

* Summary Page for currency 'summary m/MONTH c/CURRENCY'
* e.g, summary JAN MYR

* Add bank account 'addbank i/INITIAL_DEPOSIT c/CURRENCY'
* e.g, addbank 5000 THB
* List bank accounts 'listbanks'
* Exit programme 'exit'


