# Fahad Mohaideen's Project Portfolio Page

## Overview
TrackStars is a financial tracker app aimed at NUS exchange students travelling to the South East Asia region.
It makes tracking savings and spendings across a number of bank accounts in a variety of currencies easy.
This allows them to gain insights on their spending habits on different categories, such as transport, food etc.

## Code Contributed
Linked below is the reposense report.

https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=fahadmohaideen&tabRepo=AY2526S1-CS2113-W10-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false

## Features added

### Adding Budget feature

**What it Does**: It allows each user to add budgets to the list of budgets for expenditure related to the various categories
attributed to them

**Rationale**: Users would find it easier to track their expenditure for the various categories, while also comparing them
to their respective budgets, ensuring they do not exceed any of them.

**Highlights**: All the user's budgets will be displayed when either the listBudget or Summary command is executed. In the 
case of Summary command, budgets will be displayed according to each category alongside with the total monthly expenditure 
for that month.

### Linking of transactions to specific banks

Before this was implemented, the user has two separate lists for transactions and banks, which was quite disorganised.

**What it Does**: When a user adds a transaction, the transaction is added to a specific bank, which has an arrayList of 
transactions

**Rationale**: In order to allow users to track their expenditure more easily across banks, especially if they have many.

**Highlights**: This feature is integrated with the login to bank feature, which will be explained after this. 

### Login to Bank feature

**What it Does**: Users can login into any of their existing banks to view bank specific information or to add transactions

**Rationale**: It is now easier for users to view transactions as well as the monthly expenditure summary organised
according to banks

**Highlights**: When the user is logged in they can only view monthly summary ans transactions of that specific bank, and
they MUST be logged in to do so. When the user is logged out, they can view all transactions and banks, and they MUST be
logged out to do so.

### ATM Feature

**What it Does**: It allows users to deposit and withdraw money to and from the bank

**Rationale**: This enables users to keep track of cash that is being deposited and withdrawn, simulating an ATM, which 
exists for most banks in real life

**Highlights**: When users deposit money, the bank balance increases and when they withdraw money, the bank balance
decreases accordingly. When balance is a non-positive number, there will be an exception thrown if the user tries to
withdraw money in that situation.

### Adding tags to transactions

**What it Does**: When users add transactions, they are not only able to associate it with a specific category, but also
give them specific labels such as 'buying bread' or 'Grab to work'.

**Rationale**: To allow users to keep track of specific transactions per category, as in reality, most people would have
multiple purchases under the same category

**Highlights**: This was inspired by the tagging functionality of github commits, and it is an optional argument. If there
are no tags assigned to a specific transaction, it will just appear as 'unnamed', when listing transactions or viewing summary.

## Documentation

### Developer Guide

* Added design and implementation details about the features I have mentioned above
* Added and designed corresponding UML diagrams for those features
* Described and explained the execution flow step by step detailing all possible branches, as well as exceptions that
could be thrown.

### User Guide
* Looked and check through the user guide to ensure that the information about the execution of various commands matches
that of the Developer Guide, for consistency

## Project Management

### Submission of deliverables

* Released v2.0 together with tG and uG for testing during PE-D



