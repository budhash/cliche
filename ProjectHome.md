# Cliche Command-Line Shell #

Cliche is a small Java library enabling _really_ simple creation of interactive command-line user interfaces.

It uses metadata and Java Reflection to determine which class methods should be exposed to end user and to provide info for user. Therefore all information related to specific command is kept in only one place: in annotations in method's header. User don't have to organize command loop, write complicated parsers/converters for primitive types, though he can implement custom converters when needed.

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

## When to Use ##

Though interactive command-line UIs aren't spread wide, they can be extremely useful for their simplicity when program needs some kind of user interaction.

Of course you may use full-blown shell like BeanShell, but for small utilities it's a huge overkill. Cliche's jar is under 100 kilobytes, and there isn't much code to read, so you can easily fix any unacceptable behavior. It's perfectly suited for simple administrative utilities (I've made one for small web application when I was too lazy to make administrative JSPs — to remove users, view statistics, etc.). And it proved helpful in experimenting with computational algoritms to quickly adjust parameters. Actually, we construct lots of very simple Swing UIs with couple of buttons and textboxes just to test some idea, and with Cliche you **just write methods** performing your task: much of IO handling and conversion is done for you.