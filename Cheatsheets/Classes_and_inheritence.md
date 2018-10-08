# Single Responsibility Principle
If a class has only one responsibility, it needs to change only when there is a change to that responsibility.
Not all classes have to follow this principle


# Dependencies
Dependencies are objects that are not directly linked in the object network can still interact with each other.
These are a weaker form of associations we call dependencies.

```java
class Foo{
    // Foo has a dependency on Bar class
    int calculate(Bar bar){
        return bar.getValue(); // Note that Bar class is not stored as a parameter in class Foo
    }
}

class Bar{
    int value;

    int getValue(){
        return value;
    }
}
```
Dependencies are represented by a dashed arrow. [Foo] ---> [Bar]

# Composition

1. Strong *whole-part* relationship
2. Cannot have cyclical links
3. Ideally, the ‘part’ object may not even be visible to clients of the ‘whole’ object.

```java
// When email is destroyed, subject will be destroyed too
class Email {
    private Subject subject;
  ...
}
```
UML uses a solid diamond pointing at the *whole* class


# Aggregation
Aggregation represents a container-contained relationship. It is a weaker relationship than composition.


## Aggregation vs Composition
Aggregation association is when the containee (subject) can still exist even after the container is destroyed.

UML uses the same symbol as *Composition*

# Inheritence
```
eg. Animal <-- Bird <-- Eagle

```
Triangle points to the parent class

Multiple Inheritance (Multiple parent classes) is when a class inherits directly from multiple classes. - *Not allowed in Java*



1. Child must always be able to do what the parent can do
2. Arrow points to the parent
3. Inheritence is extending
4. Can form an inheritence tree.
5. In Java all classes inherit from the Object class
6. Java disallows multiple inheritance in a class
7. Abstraction and encapsulation
8. Child class will override parent class metohd as long as same method name is declared in child
9. Child class can also access public attribs of parent class




# Practices for good code quality
1. Class names should start with upper case letter
2. Class names should be meaningful
3. Avoid magic numbers (Even if numbers are fixed, declare them as macros or global variables)
