Cliche Command-Line Shell
=========================

Release 3 (Apr 23, 2009)
------------------------

### Summary

This release includes some bug fixes and small improvements (e.g. newlines in auto-generated help), as well as new feature -- Auxiliary Command handlers support.

AuxHandlers allow you to specify several extension objects that act like ordinary command handlers (with optional prefixes), except that they all are inherited by subshells. You can use this functionality, for instance, to make some commands global, or to collect all I/O converters in one place.

### Backward compatibility

`ShellFactory.createConsoleShell(String prompt, String hint, Object... handlers)` method is kept to provide compatibility with cliche-2.

### What's inside

This release includes Cliche jar (built against JRE 1.6), full source code and some documentation (javadocs included), that you can also find at [Cliche home page](http://cliche.sourceforge.net/).