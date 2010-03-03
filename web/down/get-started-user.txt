Cliche Command-Line Shell
=========================

Getting Started for Users
-------------------------

Using Cliche is easy. I'll explain you some important aspects of using the program explained in [Getting Started for Developers](/?f=get-started-dev).

Here's the sample session:

~~~~~~
Enter '?list' to list all commands
cliche> ?list
Name         Abbr     ArgNum   Descr
hello        h        0        hello() : String
add-numbers  an       2        Add two integers
exit                  0        exit() : void
cat                   1        concatenate(String[]) : String
!about       !a       0        Info about special command handler
!exit                 0        Instantly breaks execution
?list        ?l       0        Lists all available commands
?list        ?l       1        Lists all available commands starting with given prefix
?help        ?h       1        Shows detailed info on all commands with given name
cliche> add-numbers 23 -40 
-17
cliche> an 10 4
14
cliche> ?list !
Name        Abbr      ArgNum   Descr
!about      !a        0        Info about special command handler
!exit                 0        Instantly breaks execution
cliche> ?help cat
COMMAND: cat
ABBREV: null
This command accepts 1 arguments:


cliche> ?help an
COMMAND: add-numbers
ABBREV: an
This command accepts 2 arguments:
        augmend       int        What a fancy word :)
        addend        int        


cliche> cat 8 ' cats and one ''dog''' " in here"
The concatenation is:
8 cats and one 'dog' in here
cliche> h
Hello!
cliche> exit
~~~~~~

You enter commands in response to the prompt, `cliche> `. To list all avaliable commands enter `?list` or `?l`. You will see a table showing name, abbreviation, argument count and short description of each command. You can enter `list str` to list only commands whose name starts with "str".

To get detailed help on the command, enter `?help cmdname`. Yes, the system isn't perfect :)

To pass strings with spaces you can use quotes. To include quote or double-quote simply double it.

That's all! If you're interested in how it works, read [the guide for developers](/?f=get-started-dev).
