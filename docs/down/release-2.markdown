Cliche Command-Line Shell
=========================

Release 2 (Mar 08, 2009)
------------------------

### Summary

The second release incorporates a lot of small changes, mostly in source code (major refactoring).

New features include some interesting builtin commands:

* **!run-script**,
* !enable-logging, !disable-logging,
* !set-display-time,

**subshells**, which allow you to traverse hierarchical structures, and last but not the least, complete yet very short **manual**!

### Backward compatibility

This release is **incompatible** with previous one: `Shell.createConsoleShell` should be replaced by `ShellFactory.createConsoleShell`.

Now you should specify the prompt (ehm, now it's called "path element") as `"cliche"`, and the Shell will display it as `"cliche> "`.

### What's inside

This release includes Cliche jar (built against JRE 1.6), full source code and some documentation (javadocs included), that you can also find at [Cliche home page](http://cliche.sourceforge.net/) (you've been there, aren't you?).