# Cliche Command-Line Shell #

## Manual ##

### About the document ###

This is the most detailed instruction on Cliche. Here I'll describe major aspects of using Cliche
library from the developer's viewpoint.

**Note:** I don't worry much about backward compatibility, but I'll try
to emphasize things that will likely be changed.

### Why is Cliche worth considering? ###

Cliche is a small (under 100 kilobytes, **no** dependencies),  Java library enabling **really** simple creation
of CLIs (CLI stands for "command-line user interface" throughout all Cliche docs.)

**How simple?** _So simple:_

```
package asg.cliche.sample;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import java.io.IOException;

public class HelloWorld {

    @Command // One,
    public String hello() {
        return "Hello, World!";
    }

    @Command // two,
    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("hello", "", new HelloWorld())
            .commandLoop(); // and three.
    }
}
```

_Three additional lines of code!_ More precisely, one per each method to be available to user,
and one "magic phrase" to run the Shell. Well, that should produce really crappy UI...

```
hello> ?list
abbrev	name	params
a	add	(p1, p2)
h	hello	()
hello> hello
Hello, World!
hello> ?help add
Command: add
Abbrev:  a
Params:  (p1, p2)
Description: add(p1:int, p2:int) : int
Number of parameters: 2
p1	int	
p2	int	


hello> add 45 34
79
hello> a 45 34
79
hello> exit
```

Not so bad, I suppose. As you should have noticed, there are some "builtin" commands, such as `?list`, they
are prefixed with some special symbol, e.g. `?` or `!`.

This library proved to be very helpful in experimenting with new code. E.g. you've spent an hour writing
new algorithm and want to quickly test it. Instead of spending another hour on writing any kind of UI,
you just add a couple of annotation and that magic line and hit Shift+F6, and quickly learn how that code works.
Or you've got a third-party library and want to test it "hands on".

Personally I am very happy that I have Cliche, because now I'm experimenting with algorithms a lot,
and I spend **no** time constructing the UI.

Or there may be "real" applications, since the very core functionality of it, i.e. type
conversion and object output, is easily extensible. And it supports subshells, that give us the important
ability to navigate through tree-like structures.

One more use case is when you have rather complicated system, say, MVC-style webapp, and you want to give the
sysadmin full control over the model, but it's too expensive to build a web admin panel for only one man...
Then you'll probably just have to write a simple Main class and maybe some type converters in the worst case.

In many cases we prefer simplicity, and I don't think there's simpler UI framework for Java.

### Simplest usage ###

As in previous example, sometimes the only thing you need is to mark methods with `@Command` and run
the `commandLoop()`.

Now let's be precise. Parameter type conversion is performed automatically only for primitive types,
their boxed equivalents (i.e. `Integer`) and strings. Output is supported for primitives, arrays,
collections and anything with meaningful `toString()`. Of course you can override conversion algorithm
for any type. More on this later.

Varargs functions are supported. Method overloading generally doesn't work, except in the case when
number of parameters is different: command is characterized by its name and arity (number of parameters).

That "magic phrase" should be explained in detail.

```
public static Shell ShellFactory.createConsoleShell(
    String pathElement, String hint, Object... handlers) {
```

<dl>
<blockquote><dt><pre><code>pathElement</code></pre></dt>
<dd>Think of it as of text displayed in the command prompt. This parameter makes a lot more sense when you use subshells.</dd>
<dt><pre><code>hint</code></pre></dt>
<dd>Shell's hint is some text that is displayed upon startup and every time user enters <code>?</code> command. It may be used instead of 'about' command.</dd>
<dt><pre><code>handlers</code></pre></dt>
<dd>Handlers are objects whose annotated methods are to be turned into commands. All commands resulted from this parameter are in the same namespace (are placed as-is.)</dd>
</dl></blockquote>

### Documenting commands ###

Descriptions like `add(p1:int, p2:int) : int` are all right if you're the only person working with the
program, but in other cases (and sometimes in this case, too) it's better to document the UI.

This is extremely easy when you have already written javadoc comments. Suppose you have:

```
/**
 * Command description
 * @param param1 Description of param1
 * @param param2 Description of param2
 */
public int someCommand(int param1, int param2) {
    . . .
}
```

To make it well-documented command you transform these comments into annotation parameters:

```
@Command(description="Command description")
public int someCommand(
    @Param(name="param1", description="Description of param1")
        int param1,
    @Param(name="param2", description="Description of param2")
        int param2) {
    . . .
}
```

And this command appears in the list as follows:

```
hello> ?l
abbrev	name	params
a	add	(p1, p2)
sc	some-command	(param1, param2)
h	hello	()
hello> ?help some-command
Command: some-command
Abbrev:  sc
Command description
This command has 2 parameter(s):
param1	int	Description of param1
param2	int	Description of param2


hello> exit
```

All builtin commands are documented exactly in this manner.
```
hello> ?help ?help
Command: ?help
Abbrev:  ?h
Shows detailed info on all commands with given name
This command has 1 parameter(s):
command-name	String	Command name you want help on

Command: ?help
Abbrev:  ?h
Shows info on using the UI
This command has no arguments.

hello> exit
```

### Naming commands ###

Sometimes you don't like auto-generated name or abbreviation or want to avoid conflicts due to
method overloading. In such cases use `name` and/or `abbrev` parameters of `@Command`:
```
@Command(name="list", abbreviation="ls")
public List listFiles() { . . . }
```

Or, maybe, you don't like the way the method names are transformed into commands? Say, you want `someMethod`
to become not `some-method`, but `Some_Method` (you've coded a lot in Ada, right?) Then it probably
won't be a problem for you to create new `CommandNamer` implementation and then assemble your shell like
`ShellFactory` does. In fact, this is rather simple, there's even `Strings` class that has a method for
breaking javaCase identifiers into words. `svn checkout`.

Also note that by default `cmdSomeCommand` and `cliSomeCommand` are becoming `some-command`. I recommend that
you use this naming convention for purely UI methods, existing solely for `@Command`'s sake. As always,
if you don't like this behavior, set `@Command(name=...)`, which is always accepted as-is (you may even use
spaces in that name, though that command would become inaccessible.)


### The Help System ###

There are three help commands: `?list`, `?list-all` and `?help`. Let Shell explain them.
```
hello> ?h ?list
Command: ?list
Abbrev:  ?l
Lists all commands with no prefix
This command has no arguments.
Command: ?list
Abbrev:  ?l
Lists all available commands starting with given string
This command has 1 parameter(s):
startsWith	String	Pattern to show commands starting with


hello> ?h ?list-all
Command: ?list-all
Abbrev:  ?la
Lists all commands
This command has no arguments.

hello> ?h ?help
Command: ?help
Abbrev:  ?h
Shows detailed info on all commands with given name
This command has 1 parameter(s):
command-name	String	Command name you want help on

Command: ?help
Abbrev:  ?h
Shows info on using the UI
This command has no arguments.

hello> ?l
abbrev	name	params
!el	!enable-logging	(fileName)
!dl	!disable-logging	()
!rs	!run-script	(filename)
!gle	!get-last-exception	()
!sdt	!set-display-time	(do-display-time)
?l	?list	()
?l	?list	(startsWith)
?h	?help	()
?h	?help	(command-name)
?ghh	?generate-HTML-help	(file-name, include-prefixed)
?la	?list-all	()
a	add	(p1, p2)
h	hello	()
sc	some-command	(param1, param2)
hello> ?h
This is Cliche shell (http://cliche.sourceforge.net/).
To list all available commands enter ?list or ?list-all, the latter will also show you
system commands. To get detailed info on a command enter ?help command-name.
hello> exit
```

Sorry, I don't know any good way to print big table to stdout, so it's difficult to read.

Now let **me** explain them in short.

You enter `?l part-of-command-name` or `?l` or `?la`, find interesting name and enter `?h that-name`.
That's all you really need to know about the help system.

### `ShellDependent` and `ShellManageable` ###

These two interfaces are to be implemented when you want to be aware of some system CLI events.

An object implementing `ShellDependent` gains the ability to obtain a reference for Shell running it
through a method `void cliSetShell(Shell theShell)`:
```
public class Tree implements ShellDependent {
    . . .

    private Shell theShell;

    public void cliSetShell(Shell theShell) {
        this.theShell = theShell;
    }

    . . .
}
```

In some cases you might want to know when Shell enters or exits the command loop. Then you might
be interested in implementing `ShellManageable`:
```
public interface ShellManageable {

    // This method is called when it is about to enter the command loop.
    void cliEnterLoop();

    // This method is called when Shell is leaving the command loop.
    void cliLeaveLoop();
}
```

In such cases you are probably `ShellDependent` as well.

### Subshells ###

Subshells offer a way to navigate through tree-like structures. Here's the example:
```
public class Tree implements ShellDependent {
    
    private String name;
    
    public Tree(String name) {
        this.name = name;
    }

    private Tree left;
    private Tree right;
    
    @Command
    public void setName(String name) {
        this.name = name;
    }
    
    @Command
    public String getName() {
        return name;
    }
    
    @Command
    public void left() throws IOException {
        if (left == null) {
            left = new Tree("unnamed");
        }
        ShellFactory.createSubshell(left.name, theShell, "Left", left)
                .commandLoop();
    }

    @Command
    public void right() throws IOException {
        if (right == null) {
            right = new Tree("unnamed");
        }

        // And then a miracle occurs...
        ShellFactory.createSubshell(right.name, theShell, "Right", right)
                .commandLoop();
    }

    private Shell theShell;

    public void cliSetShell(Shell theShell) {
        this.theShell = theShell;
    }

    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("root", "", new Tree("root"))
                .commandLoop();
    }

}
```

You must be ShellDependent to create subshells, because `createSubshell()`, which is very similar
to `createConsoleShell()`, wants you to give it the parent shell.

Here's how it works from user's perspective:
```
root> ?l
abbrev	name	params
gn	get-name	()
sn	set-name	(p1)
l	left	()
r	right	()
root> gn
root
root> left
Left
root/unnamed> sn A  # prompt won't change immediately
root/A/unnamed>     # as it's set upon creation.
root/unnamed> gn    # Although the name did change.
A
root/unnamed> exit  # Use exit to navigate up.
root> right
Right
root/unnamed> exit
root> left
Left
root/A> right       # Now we see correct name at the prompt.
Right
root/A/unnamed> set-name B
root/A/unnamed> exit
root/A> exit
root> left
Left
root/A> left
Left
root/A/unnamed> exit
root/A> right
Right
root/A/B> exit  # Wouldn't it be nice if there were
root/A> exit    # "exit-all" command?
root> exit      # Unfortunately, that's not so easy.
```

### Converters ###

**Input converters** are very important, because how do you support all the variety of types?
For example, I've written a method:
```
@Command
public void computeFrequencies(StringBuilder text, int eqClass) {
    for (int i = 0; i <= text.length() - frameSize; i++) {
        frequencies.put(eqClass, text.substring(i, i+frameSize));
    }
}
```

But I can't call it since the Shell doesn't know what is that strange `StringBuilder` thing and says
it doesn't know how to convert string to it. But StringBuilder itself is kind of string!

Let's explain it to Shell:
```
public static final InputConverter[] CLI_INPUT_CONVERTERS = {
    new InputConverter() {
        public Object convertInput(String original, Class toClass)
                throws Exception {

            if (toClass.equals(StringBuilder.class)) {
                return new StringBuilder(original);
            } else {
                return null;
            }
        }
    },
};
```

We declare a public field named `CLI_INPUT_CONVERTERS` which contains all necessary converters.
As you can see, there are few rules to obey:

  1. Type of the field must be asg.cliche.InputConverter[.md](.md);
  1. name of the field must start with reserved string `CLI_INPUT_CONVERTERS` (yes, we could name it
`CLI_INPUT_CONVERTERS_FOR_StringBuilder`);
  1. the field must be `public`, other modifiers aren't important;
  1. Converter returns `null` when required conversion is not his business.

It is possible to perform all necessary custom conversions in one big `InputConverter` but I
recommend that you place them in separate converter instances.

**Output conversions** are useful if you don't like object's default `toString()` behavior.
Or, alternatively, you might want those integers be printed not as `13`, but rather as `Thirteen`...

The way of declaring them is similar to input converters:
```
public static final OutputConverter[] CLI_OUTPUT_CONVERTERS = {
    new OutputConverter() { // to make a text from strings
        public Object convertOutput(Object toBeFormatted) {
            if (toBeFormatted instanceof String[]) {
                return StringUtils.join((String[])toBeFormatted, " ");
            } else {
                return null;
            }
        }
    }
};
```

Here you have the same rules, but you may return not necessary `String` but any other object
which will be formatted according to usual rules. For example, if you need to output some
weird custom collection you may convert it to an array and Shell (in fact, `OutputConversionEngine`)
will format it well.

One thing to remember is that all custom output converters are applied to all objects to be output,
_the order of application being the reverse of that of the array._

### Special commands ###

There are some quite useful builtin commands.

  * `!run-script filename` reads and executes commands from given file.
  * `!set-display-time true/false` toggles displaying of command execution time. Time is shown in
milliseconds and includes only your method's physical time.
  * `!enable-logging filename` and `!disable-logging` control logging, i.e. duplication of all
Shell's input and output in a file.

You already know how to use the builtin help system to get more details, aren't you?

### Aux handlers ###

As of cliche-3 release, the Shell allows so-called 'aux handlers' to be specified in construction. These are extension objects that act like ordinary command handlers (with optional prefixes), except that they all are inherited by subshells. You can use this functionality, for instance, to make some commands global, or to collect all I/O converters in one place:

```
// MainEnvironment.java

private MultiMap<String, Object> cliAuxHandlers = new ArrayHashMultiMap<String, Object>();

// instance initializer
{
	cliAuxHandlers.put("MainEnvironment-", new CliUtils()); // CliUtils contains a bunch of converters and no commands
	cliAuxHandlers.put("gv-", new GraphVisualizer());       // GV allows to adjust some global options
}

public static void main(String[] args) throws IOException {
	MainEnvironment env = new MainEnvironment();
	ShellFactory.createConsoleShell("LCA", "", env, env.cliAuxHandlers)
		.commandLoop();
}	
```

### Conclusion ###

There're certainly some undisclosed aspects of this piece of software, but even this detalization is
more then necessary. The "Simplest usage" section is enough very frequently. And nobody likes long
manuals. Although almost everybody writes :)