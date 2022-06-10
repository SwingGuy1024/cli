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
### TrimTwo must be installed locally for this to work, but need not be in ~/bin:
### This will install and update TrimTwo 
rm TrimTwo
echo '#!/usr/bin/java --source 11' > TrimTwo
cat TrimTwo.java >> TrimTwo
chmod a+x TrimTwo

for f in *.java
do
#  echo $f
  ./TrimTwo $f ~/bin/;
done;


### The following bash script does most of what TrimTwo does, but it doesn't change the name of the output when
### the "use" directive is specified.
#for f in *.java
#do
#  fn=`basename $f .java`
#  if [ -f "$fn" ];
#  then
#      "rm ~/binx/$fn"
#  fi
#  echo $fn
#  echo '#!/usr/bin/java --source 11' > ~/binx/$fn
#  cat $f >> ~/binx/$fn
#  chmod a+x ~/binx/$fn
#done;