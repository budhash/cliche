Cliche Command-Line Shell
=========================

Getting Started for Developers
------------------------------

### Warning
This is applicable to the *first release* (cliche-1; [release notes](?f=release-1));
several changes are required to use the code in *cliche-2* ([release notes](?f=release-2))

### What are we going to do?
We are going to create simple application consisting of just one class, `Sample`. We'll document our methods and params.

### Simpliest example
Here's what we need in the very beginning:

~~~~~~
package cliche.sample;
import asg.cliche.*;
import java.io.IOException;
public class Sample {
    @Command
    public String hello() {
        return "Hello!";
    }
    @Command
    public void exit() { }

    public static void main(String[] args) throws IOException {
        Shell.createConsoleShell("cliche> ", "Enter '?list' to list all commands",
                new Sample()).commandLoop();
    }
}
~~~~~~

Now let's go into details. As you may have guessed, we have two commands for the user: *hello* and *exit*. *Hello* prints a string, and *exit* does nothing. Well, not true. Cliche will terminate the command loop after *exit* command is invoked (it doesn't care neither for method params nor for the result).

In the `main` method we create new Shell with factory method `createConsoleShell` which allows us to specify the prompt and the hint, which will be displayed upon startup and every time user enters '?' command.

Now let's run our program:
~~~~~~
Enter '?list' to list all commands
cliche> ?list
Name        Abbr        ArgNum        Descr
hello        h        0        hello() : String
add-numbers        an        2        Add two integers
exit                0        exit() : void
cat                1        concatenate(String[]) : String
!about        !a        0        Info about special command handler
!exit                0        Instantly breaks execution
?list        ?l        0        Lists all available commands
?list        ?l        1        Lists all available commands starting with given prefix
?help        ?h        1        Shows detailed info on all commands with given name
cliche> hello
Hello!
cliche> h
Hello!
cliche> exit
~~~~~~

That's interesting: there's some commands beyond those two defined by us. Those prefixed with '!' are system commands, affecting operation of the Cliche itself. Prefixed with '?' are help system commands, including *?list*, which was also invoked.

You see that commands are given abbreviations — command's first letters. More on that later.

### Complicating the example
Let's add some `@Command` methods:
~~~~~~
    @Command(description="Add two integers")
    public int addNumbers(
            @Param(name="augmend", description="What a fancy word :)") int a,
            @Param(name="addend") int b) {
        return a + b;
    }
    @Command(name="cat", abbrev="", header="The concatenation is:")
    public String concatenate(String... strings) {
        String result = "";
        for (String s : strings) {
            result += s;
        }
        return result;
    }
~~~~~~
And run it:
~~~~~~
Enter '?list' to list all commands
cliche> ?l
Name        Abbr        ArgNum        Descr
hello        h        0        hello() : String
add-numbers        an        2        Add two integers
exit                0        exit() : void
cat                1        concatenate(String[]) : String
!about        !a        0        Info about special command handler
!exit                0        Instantly breaks execution
?list        ?l        0        Lists all available commands
?list        ?l        1        Lists all available commands starting with given prefix
?help        ?h        1        Shows detailed info on all commands with given name
cliche> cat a bc cde efg
The concatenation is:
abccdeefg
cliche> cat 123 '   ' 456 " ..."
The concatenation is:
123   456 ...
cliche> an 39 -25
14
cliche> ?help an
Command: add-numbers
Abbrev:  an
This command has 2 argument(s):
augmend        int        What a fancy word :)
addend        int        


cliche> exit
~~~~~~

You see, `@Command` annotation have a lot of optional parameters. Consequences of setting them are obvious. There's also a `@Param` annotation, which is used to assign names and descriptions to method params (the Reflection doesn't allow us to extract param names from code).

Other important aspect is command naming: `addNumbers` become `add-numbers`. Abbreviation is formed from first letters of each word.

And, you see, Java 5 varargs and spaces in strings are supported.

### Further directions

Now you know everything to use Cliche. Read [javadocs](/javadoc/) and experiment freely! Oh, and show your users [this guide](/?f=get-started-user).
