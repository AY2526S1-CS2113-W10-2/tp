## Kevin Loke's Portfolio Project Page

### Project: TrackStars

TrackStars is a desktop finance tracker application that intends to serve as an integrated application for NUS Exchange Students to track their finances across multiple currencies. The user interacts with the programme through the CLI.

Given below are my contributions to the project: 

#### New Feature: Monthly Summary

What it does: This allows the user to see a monthly summary of their spending. 
Justification: This feature improves significantly the convenience of managing finances across many currencies, because spending data can be aggregated into an easily understood format
Highlights: The same Monthly Summary feature can function in 3 ways, depending on the user input. If the user is logged out, they have the choice to see total spending across all bank accounts, or spending tied to a specific currency. If the user is logged in, the summary page will show only the spending tied to that bank account. 

#### New Feature: Storage 
What it does: Storage allows the user to save the budgeting, transactions and bank data that they have keyed in prior to exiting the programme 
Justification: This feature helps to make the application less inconvenient and a smoother experience of the user. 
Highlights:
Code contributed: https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=kevinlokewy&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=

#### Project Management:
 * Managed release v1.0 on GitHub 

#### Enhancements to existing features
 * Wrote additional and more robust tests for SummaryCommandTest and StorageTest respectively. (Pull Requests #84, #26)
 * UI quality of life updates to declutter the output and display useful financial information to the user. (Pull Request #67)

#### Documentation: 
 * User Guide:
    1. Added documentation for the introduction
    2. Filled up the features section of the UserGuide (addBank, login, logout, list, listBanks, delete, addBudget...)
    3. Did the FAQ section 
    4. Contributed to the Command Summary section 

 * DeveloperGuide: 
    1. Added design and implementation details for storage and high-level architecture
    2. Write up and diagrams for Monthly Summary Feature 
    3. Documented product scope, both target user profile and value proposition 
    4. User Stories
    5. Manual testing section 

#### Community:
 * Contributed to forum discussions (Issue #24, #23)