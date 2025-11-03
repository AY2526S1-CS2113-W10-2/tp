# Xu Huanghao's Project Portfolio Page

## Overview
TrackStars is a finance tracker that helps NUS Exchange Students manage their expenses, 
monitor transactions, and gain insights into their spending habits as they are in SouthEast Asia Region. 
It provides a user-friendly command-line interface for managing multiple banks, categorizing transactions, 
and generating summaries efficiently.

#### Summary of Contributions

* **Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=xuhh03&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)


* **New Feature**: Added delete transaction command from banks
  * **What it does:** Allows users to delete a specific transaction from their selected bank account using its index.
  * **Justification:** This feature is essential for users in correcting entry mistakes and maintaining accurate transaction records.
  * **Highlights:** Involves integration with storage, updating bank balance dynamically, and ensuring synchronization across different commands. 
  Implemented detailed exception handling and logging support for reliability.
  * **Credits**: Adapted delete concept from Individual Project


* **New Feature**: Added filter command to sort transactions
  * **What it does:** Enables users to filter transactions based on category, cost range, or date range.
  * **Justification:** Improves user experience by helping users focus on specific transaction sets without scrolling through long lists.
  * **Highlights:** Required coordination with transaction model and UI output formatting. Implemented modular logic to support future expansion (e.g., multi-criteria filters).


* **New Feature**: Added search command to allow users to search transactions by keywords
  * **What it does:** Allows users to search transactions by keywords or tags (e.g., “food”, “transport”).
  * **Justification:** Provides fast lookup capability, improving usability for large transaction lists.
  * **Highlights:** Integrated case-insensitive search, result formatting. Implemented logging to track command execution and user query results.
  * **Credits**: Adapted find concept from Individual Project


* **New Feature** Added help command to guide user on the usage of the program
  * **What it does:** Displays a comprehensive list of all available commands, along with a brief description of their functionality and expected input format.
  * **Justification:** This allows users to quickly learn how to interact with the system without referring to external documentation. It reduces the learning curve and minimizes user frustration due to command errors.
  * **Highlights:** It supports clean, readable output formatting suitable for the Command Line Interface (CLI).


* **Project Management**:
  * Assisted in coordinating task distribution and enforcing coding standards across team members.
  * Reviewed PRs for command implementations and exception handling.
  * Refactored classes for better code readability, maintainability, and adherence to coding standards.


* **Enhancements to existing features**
  * Added more robust **JUnit tests** for core components such as `Parser` and `Command` classes, improving code reliability and test coverage. (Pull Request #60, #75)
  * Integrated centralized `AppLogger` to unify logging and suppress console output. (Pull Request #158)


* **Documentation**
  * UserGuide
    * Added sections for `filter`, and `search` commands and fix cosmetics for existing feature documentations. #77
    * Added Table of Content for better navigability. #151
  * DeveloperGuide
    * Added implementation for the `deleteTransactionCommand`, `searchCommand`, `filterCommand`,`helpCommand`.
    * Added **class diagram for the `UserInterface` and `User` API** to illustrate command flow and component interactions.
    * Assisted in editing, formatting and enforcing **coding and diagram standards** across the DG for consistency and readability.
    * Added User Stories
    * Added Non-Functional Requirements
* **Team Contribution**
  * Assisted in correcting coding standard and styling errors
  * Review pull requests and commits
