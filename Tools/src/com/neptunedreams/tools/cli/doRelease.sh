# Shebang Notes
# Java file must start with a Shebang line, which looks like this: #!/usr/bin/java --source 11
# # and ! must not have a space between them
# Java 11 or later must be set
# files must be executable
# Shebang files must be on the path. You can't execute shebang by typing . ./shebang
# TrimTwo does the same as cp, but adds a shebang line to the top of the file. It also looks for an optional
# line starting with this:
# // use: xxx
# It takes xxx as the name of the output file. It only looks at the first line. Consequently, the second parameter
# to TrimTwo should be a directory, not a file.

# Notes on releasing
# Path should include ~/bin
# ~/bin directory should exist.
### TrimTwo must be installed for this to work. To install, execute TrimTwo from inside your IDE with a single parameter of "install". 


for f in $(ls *.java);
do
  TrimTwo $f ~/bin/;
done;