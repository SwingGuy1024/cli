package com.neptunedreams.tools.cli;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
//import org.jetbrains.annotations.Nullable;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 12/12/21
 * <p>Time: 1:26 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"HardCodedStringLiteral", "MagicNumber"})
public enum FindFile {
  ;

  private static final File[] EMPTY_FILE_ARRAY = new File[0];
  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.printf("Usage: FindFile <name> [, <name>...]%n"); // NON-NLS
    } else {
      
      
//      // temp:
//      File[] files = emptyIfNull(new File("/").listFiles());
//      Arrays.sort(files, Comparator.comparing(File::getName));
//      
//      // Is there a more succinct way to write this?
//      Arrays.sort(files, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName())); 
//      for (File f: files) {
//        System.out.printf("Dir: %5b  SL: %5b  %s%n", f.isDirectory(), Files.isSymbolicLink(Path.of(f.toURI())), f.getName()); // NON-NLS
//      }
      
      find(args);
    }
  }
  
  private static void find(String[] names) {
    long start = System.currentTimeMillis();
    StringBuilder builder = new StringBuilder();
    for (String name: names) {
      builder.append(name);
      builder.append(' ');
    }
    String target = builder.toString().trim();
    File root = new File("/");
    Deque<File> dirSource = new LinkedList<>();
    dirSource.add(root);
    while (!dirSource.isEmpty()) {
      File dir = dirSource.pop();
      File [] files = dir.listFiles(); // returns null if none are found
      for (File file: emptyIfNull(files)) {
        if (file.isDirectory() && !isApp(file) && !isSymbolicLink(file)) {
          dirSource.add(file);
        } else if (file.getName().contains(target)) {
          System.out.println(file.getAbsolutePath()); // NON-NLS
        }
      }
    }
    long end = System.currentTimeMillis();
    System.out.printf("Search took %d seconds%n", (end - start)/1000L); // NON-NLS
  }
  
  private static File[] emptyIfNull(File /*@Nullable*/ [] array) {
    return (array == null) ? EMPTY_FILE_ARRAY : array;
  }

  private static String[] emptyIfNull(String /*@Nullable*/ [] strings) {
    return (strings == null) ? EMPTY_STRING_ARRAY : strings;
  }

  private static boolean isApp(File file) {
//    return false;
    return (file.getName().endsWith(".app")) && hasContents(file);
  }
  
  private static boolean hasContents(File dir) {
    String[] names = dir.list();
    for (String name: emptyIfNull(names)) {
      if ("Contents".equals(name)) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isSymbolicLink(File file) {
    return Files.isSymbolicLink(Path.of(file.toURI()));
  }
}
