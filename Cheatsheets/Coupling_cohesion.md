# Coupling

Measure of number of dependencies between modules. A module is coupled to another module when it depends on it.
##Types of Coupling
1. **Content coupling** - one module modifies or relies on the internal workings of another module eg.
accessing local data of another module
2. **Common/ global coupling** - two modules share the same global data
3. **Control coupling** - one module passing a flag to another module
4. **Data coupling** - one module sharing data with another module eg. passing params
5. **External coupling** - two modules share an externally imposed convention eg.
communication protocols
6. **Subclass coupling** - a class inherits from another class.
Note that a child class is coupled to the parent class but not the other way around.
7. **Temporal coupling** - two actions are bundled together just because they happen to occur at the same time

# Cohesion
Cohesion is a measure of how strongly-related and focused the various responsibilities of a component are.
A highly-cohesive component keeps related functionalities together while keeping out all other unrelated things.

## Cons of low cohesion
1. Impedes the understandability of modules
2. Lowers maintainability, many modules need to be modified to achieve a small change in behaviour
3. Lowers reusability of modules because they do not represent logical units of functionality

Eg. of less cohesive
```Java
class EmailMessage {
    private String sendTo;
    private String subject;
    private String message;
    private String username;

    public EmailMessage(String sendTo, String subject, String message) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.message = message;
    }

    public void sendMessage() {
        // sends message using sendTo, subject and message
    }

    public void login(String username, String password) {
        this.username = username;
        // code to login
    }
}
```
**Question**: Why is login being handled by EmailMessage??


# Separation of Concerns Principle
To achieve better modularity, separate the code into distinct sections, such that each section addresses a separate concern.

- Applying SoC reduces functional overlaps among code sections and also limits the
ripple effect when changes are introduced to a specific part of the system.

## SOC vs SRP
SOC is higher level. Applies to component, class.

# Open-closed principle
A module should be open for extension but closed for modification. That is,
modules should be written so that they can be extended, without requiring them to be modified.

- ArrayList class can be reused for different templates/ types without modifying its code base.
- example: AddressbookParser, where we need to add more switch cases when we wish to add more commands


# Association classes
Example:
```Java
class Transaction{

    //all fields are compulsory
    Person seller;
    Person buyer;
    Date date;
    String receiptNumber;

    Marriage (Person seller, Person buyer, Date date, String receiptNumber){
        //set fields
    }
}
```
An association class represents additional information about an association.
It is a normal class but plays a special role from a design point of view.
