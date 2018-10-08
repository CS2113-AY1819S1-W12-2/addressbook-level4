Pillars of OOP
===

## Abstraction
Hiding the implementation from the end user. That way the user will know what something does
but not how it does it.


## Encapsulation
Wrapping methods and attributes into a single unit.
- Declare an attribute as private
- Create methods that specifically get or set these attributes

## Inheritance
Child class can do everything parent class can do. Child class can only access public attributes
and methods of the parent class.

Biggest plus for implements is that a child class can implement from multiple interface classes
but cannot inherit from multiple classes/ abstract classes

- java interfaces do not allow you to create private methods/ attributes unlike classes

- Both interface and abstract classes **cannot be instantiated**

### Abstract vs concrete classes
#### Abstract class
- A class can be an abstract class without having any methods inside it. But if it has any methods inside it,
it must have at least one abstract method. This rule does not apply to static methods.

- As abstract classes can have both abstract and non abstract methods, hence the abstract modifier is necessary here ( unlike in interface where only abstract methods are allowed ).

- Static members are allowed.
- Abstract classes can extend other at most one abstract or concrete class and implement several interfaces.
- Any class that does not implement all the abstract methods of it’s super class has to be an abstract class itself.

#### Concrete class
A concrete class is complete in itself and can extend and can be extended by any class.

- Static members are also allowed

- Does not allow **method signatures** unlike abstract classes

- The only condition is that all the methods have to be implemented (if it extends or implements others)
in order for it to qualify as a concrete class.

## Polymorphism
Relies heavily on overriding methods. Mainly used to store abstract types but we want a
specific/ more refined method for each of them.

- We can use implements or inheritance for this

### Example:
```Java
Person p = new Student();
p.read(); // Custom read overidden from Student class
p.compare(); // Not overidden, obtained from the Person class
```
