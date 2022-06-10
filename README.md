# cli
Weird but useful Command line tools

These are designed to use a shebang line, first supported in Java 11, to make the source files executable directly from the command prompt. The shebang line doesn't appear in these files, but gets added in the installation process.

See the TrimTwo class for installation instructions.

## Shebang Rules:
A Java Shebang line at the start of the file allows you execute a java source file from the command line. In general, shebang lines specify the application that is capable of executing the script, so the Java shebang line will include a path to the java executable command. The rules for a Shebang line aren't spelled out on Oracle.com, but they're very simple:

1. A shebang line looks like this: `#!/usr/bin/java --source 11` The `/usr/bin/java` is the path to the Java executable, and the `--source 11` specifies the version of the java source, which must be 11 or higher.
2. The shebang line must be the first line of the java source code.
3. Other java options may be specified.
4. The entire tool must be contained in a single source file. No outside classes will be linked in.
5. The source file should not have the standard .java extension. It shouldn't have any extension.
6. No third-party libraries may be used. Only libraries that ship with the standard Java library will work.

When you launch a java source file from the command line, the java executable will quickly compile the file and execute the `main(String[] args)` method. It can compile quickly because it knows ahead of time that it doesn't link in any external libraries.
