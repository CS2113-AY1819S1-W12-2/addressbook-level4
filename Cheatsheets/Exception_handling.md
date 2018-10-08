# Assertions
- Developer mishaps
- Prevent misunderstandings

Use assertions when you wish to verify a variable or method that could go wrong
eg. assert(isValid(age)) - if this fks up, developer is at fault

```Java
// Sample syntax
assert (var >= 0 && var < 2); // Ensures that the variable is within this, if not it will throw assertion error
```
# Exception handling
Use this for methods that could go wrong due to external uncontrollable factors.

- Write throwables for methods that could go wrong
- Look at most specific first for catch blocks

Syntax:
```Java
// Sample throwable
public void getJunk() throws InterruptedException {
    Thread.sleep(1000);
}

// Try catch
public static void main(String a[]){
    try{
        for(int i=0; i<5; i++){
            getJunk();
        }
    } catch (InterruptedException iex){
        iex.printStackTrace();
    } catch (Exception e) {
        // Always write the generic (less specific exception after the more specific one)
        e.printStackTrace();
    }
}
```

# Logger
Logger types:
- SEVERE (highest)
- WARNING
- INFO
- CONFIG
- FINE
- FINER
- FINEST

Syntax:
```Java
logger.info("test");
logger.severe("severe report");
```
