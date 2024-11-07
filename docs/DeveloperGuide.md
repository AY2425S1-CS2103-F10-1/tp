---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* [`OpenCSV`](https://mvnrepository.com/artifact/com.opencsv/opencsv/5.7.1) is used in this project for Goods related features.
* This project is based on the [Address-Book-Level3 (AB3)](https://github.com/se-edu/addressbook-level3) project.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete Alice")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

### SupplyCentral: Goods Classes ###

<img src="images/GoodsClassDiagram.png" width="550">

Goods classes are specific for SupplyCentral, where many convenience functions will be built in the application to cater to specific suppliers.
Person (Suppliers) will,
* be able to supply multiple Goods to the user
* will be tracked with a unique Goods Receipt, which contains more useful information related to the transaction

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: A Business owner managing a small business who frequently procures goods from suppliers.

**Persona**: Ms. Balakrishnan is a business owner of a small convenience store (mama shop) located in tampines. She finds it difficult to track her procurements, and is bad with numbers.

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* needs to associate goods with contacts

**Value proposition**: To be able to manage contacts faster than a typical mouse/GUI driven app, and able to associate goods to contacts and suppliers for easier and more comprehensive business management.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                              | I want to …​                                                                 | So that I can…​                                                                               |
| -------- |--------------------------------------|------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| `* * *`  | business owner                       | add suppliers and clients                                                    | a. view them later<br/>b. view information related to the contact                             |
| `* * *`  | business owner                       | list contacts of whom I added                                                | see the full list of suppliers I am in contact with                                           |
| `* * *`  | business owner                       | see the contacts which I added even after I exit and restart the application | access supplier information without needing to re-enter information each time I open the app. |
| `* * *`  | business owner                       | delete a contact                                                             | remove suppliers that are no longer relevant to me                                            |
| `* *`    | business owner                       | add extra relevant information to the contact                                | view more details of the supplier                                                             |
| `*`      | business owner who has many contacts | search for contacts by name and category of goods and services               | locate the relevant supplier easily                                                           |
| `* * *`  | business owner                       | add procured goods under my suppliers                                        | keep track of my transacted goods                                                             |
| `* * *`  | business owner                       | view a list of procured goods                                                | look at their details                                                                         |
| `* *`    | business owner                       | delete erroneous goods data                                                  | ensure the data integrity of my goods information                                             |
| `*`      | business owner                       | view my pending deliveries                                                   | see what goods I should be expecting to arrive                                                |
| `*`      | business owner who is bad at numbers | get statistical data for my goods                                            | do my cost and profit calculations                                                            |

### Use cases

(For all use cases below, the **System** is `SupplyCentral` and the **Actor** is the `user`, unless specified otherwise)

#### UC1: Delete a contact

- Precondition: -
- Postcondition: Specified contact is deleted

##### MSS

1. User submits contact information to be deleted.
2. System checks if contact information is properly formatted.
3. System checks if contact exists.
4. System deletes the specified contact.

##### Extensions

- 2a. Contact information is not properly formatted.

    - 2ai. System displays an error message.

      Use case ends.

- 3a. Contact does not exist.

    - 3ai. System displays an error message.

  Use case ends.

#### UC2: Add a contact

- Precondition: -
- Postcondition: Contact is added to system.

##### MSS

1. User submits contact information.
2. System checks contact information is properly formatted.
3. System checks if it doesn't exist already.
4. System adds contact information.

##### Extensions

- 2a. The contact information is not properly formatted.

  - 2a1. System shows an error message
  
  Use case ends.

- 3a. The contact information already exists (i.e. duplicated contact names)

    - 3ai. System shows an error message

    Use case ends.

#### UC3: View contacts

- Precondition: -
- Postcondition: Contacts stored (if any) are displayed to the user.
- Actors: User

#### MSS

1. User chooses to view contacts.
2. System displays details of the contact.

   Use case ends.

#### Extensions

- 2a. There are no recorded contacts
  - 2a1. System displays an error message.

    Use case ends.

#### UC4: Add goods to a supplier

- Precondition: At least one contact exists.
- Postcondition: Goods are added to the contact.
- Actors: User

#### MSS

1. User chooses to add goods to a supplier.
2. User inputs the goods to add to the supplier.
3. System adds goods to the supplier.

   Use case ends.

### Extensions

-2a. Goods information is incomplete.
   -2ai. System displays an error message.

   Use case ends.

-2b. The provided goods information exists (i.e. duplicated goods information)
   -2bi. System displays an error message.

   Use case ends.

#### UC5: Delete goods from supplier

- Precondition: The supplier has goods tagged to it.
- Postcondition: The specified goods is removed from the supplier.
- Actors: User

#### MSS

1. User chooses a contact
2. User chooses to delete goods from the supplier.
3. The specified goods is deleted from the contact.

   Use case ends.

### UC6: View Goods

- Precondition: -
- Postcondition: Goods information is displayed.
- Actors: User

### MSS

1. User chooses to view goods.
2. System displays goods information.

   Use case ends.

#### UC7: Edit contact

- Precondition: At least one contact exists.
- Postcondition: Selected contact is edited to the user's specifications.
- Actors: User

#### MSS

1. User chooses a contact.
2. User chooses to edit the contact.
3. System prompts user for their edits.
4. User submits contact information.
5. System checks if the contact information is properly formatted.
6. System checks if the contact already exists in the system.
7. System updates contact information.

#### Extensions

- 5a. The contact information is not properly formatted.
  - 2a1. System displays an error message.

    Use case ends.

- 6a. The contact information already exists in the system (i.e. duplicated contact names)
  - 6a1. System displays an error message.

    Use case ends.

#### UC8: Export Csv
- Precondition:
- Postcondition: csv is generated based on goods displayed to the user
- Actors: User

MSS
1. User chooses to export 
2. System generates a csv based on goods displayed to the user
3. Use case ends 

#### UC9: Clear suppliers and goods

MSS
1. User chooses to clear 
2. System clear all supplier contacts as well as goods
3. Use case ends

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to store up to 1000 contacts persistently.
3.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
4.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
5.  Should be able to store up to 10000 goods persistently.
6.  Updating persistent storage should not result in unnecessary data modifications.
7.  Startup time for the application should be less than 3 seconds on all _mainstream OS_.
8.  The system should perform commands within 3 seconds on all _mainstream_OS.
9.  The system should be not crash unexpectedly on all _mainstream OS_ during the performing of commands.
10. The _help system_ must be easily accessible for users.
11. All error messages should be readable (in layman's terms) and provide corrective actions whenever possible.
12. Naming and functions of commands should be similar to it's meaning in natural english language, and should not be too abstract.
13. The application must be able to export data in _commonly used formats_.
14. The application must not require any internet connection to function.
15. The codebase should be well documented with comments or JavaDoc to explain key modules and functions.
16. The project is not required to handle any messaging functions between users and contacts.
17. Key functions in the project should be tested with JUnit, with a minimal coverage of 75%.
18. Price of goods should not be negative.
19. Quantity of goods should not be negative.
20. Arrival date of goods should not be earlier than the current date.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Commonly Used Formats**: Text Files, CSV Files
* **Help System**: The built-in documentation or command that provides guidance on using the application, including descriptions of commands and examples.
* **Goods**: A commodity that is sold to the user.
* **Supplier**: Someone who is selling goods to the user. For this application, they are the contacts.
* **Procurement Date**: The date in which the order for the goods have been made.
* **Arrival Date**: The expected date of delivery for the goods.
* **Tag**: A note for the contact written by the user.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a person

1. Adding a person to SupplyCentral.

   1. Prerequisites: -

   1. Test case: `add n/John Doe p/98765432 a/John street, Block 123, #01-01`<br>
      Expected: Contact `John Doe` is added to the list and shown, with the relevant details.

   1. Incorrect add commands to try: `add n/John Doe`, `add n/1001` (where n is provided as a number)
      Expected: System will provide an error message indicating correct usage. 

### Deleting a person

1. Deleting a person from SupplyCentral.

   1. Prerequisites: -

   1. Test case: `delete Amy`<br>
      Expected: Contact `Amy` is deleted from the list. Details of the deleted contact shown in the status message.

   1. Incorrect delete commands to try: `delete`, `delete x`, `...` (where x is a number or a name that is not registered)<br>
      Expected: System will provide an error message indicating correct usage.

### Adding Goods to the system

1. Adding goods to SupplyCentral.

   1. Prerequisites: At least one supplier (e.g. Alex Yeoh) must be registered.

   1. Test case: `addgoods gn/Gardenia Milk Bread q/2 p/5 c/CONSUMABLES pd/2024-08-08 11:00 ad/2024-11-11 11:00 n/Alex Yeoh`
      Expected: Goods `Gardenia Milk Bread` is added under the supplier `Alex Yeoh`.

   1. Incorrect add goods commands to try: `addgoods`, `addgoods gn/Gardenia Bread q/-1 p/5 c/CONSUMABLES pd/2024-08-08 11:00 ad/2024-11-11 11:00 n/Alex Yeoh`
      Expected: System will provide an error message indicating correct usage.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

# Effort Summary
Difficulty level for the project was high as we increased the complexity of our use case by handling another entity `Goods` in addition to the suppliers. As we strived to tackle the pain points faced by our persona, we had to add goods tracking and other important features for our persona. We have been able to build a comprehensive application that we believe can help to alleviate the pain points of our persona. This app handles two different entities in two different data types, while ensuring smooth integration between both.

# Goods Storage
OpenCSV was used to handle working with CSV files. Hence, we are handling another file type besides the JSON that AB3 uses. Integrating this library made it easier to implement CSV-related functions. However, we did have to learn to use this library effectively and in a way that could make the application bug-free even wtih unintended incorrect usage. This resulted in quite some time and effort being used to ensure the testability of the integration of the project with OpenCSV.

# Main Challenges Faced
Besides ensuring that any bugs with OpenCSV are handled correctly, we also had to:
* Ensure duplication checks are done according to real-life scenarios (*e.g.* where it is possible for two transactions for the same goods to be present with the same supplier)
* Ensure that proper business rules are enforced throughout the application (*e.g.* quantites and prices of the goods cannot be negative)
* Consider the needs of our persona, and develop features accordingly (*e.g.* addition of statistical data when viewing the desired list of goods)
* Adding minor GUI fixes for better display formats

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

Team Size: 5

1. Make the error message for adding of goods more detailed. The current error message is too general and can be improved upon by mentioning which field is failing the sanity check and reflecting that to the user.
