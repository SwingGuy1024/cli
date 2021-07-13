package com.mm.util;

/*
 * @(#)ExtensionFileFilter.java	1.16 04/07/26
 * 
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * @(#)ExtensionFileFilter.java	1.16 04/07/26
 */


import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.filechooser.FileFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A convenience implementation of FileFilter that filters out all files except
 * for those type extensions that it knows about.
 * <p>
 * Extensions are of the type ".foo", which is typically found on Windows and
 * Unix boxes, but not on Macinthosh. Case is ignored.
 * <p>
 * Example - create a new filter that filters out all files but gif and jpg
 * image files:
 * <p>
 * <pre>
 * JFileChooser chooser = new JFileChooser();
 * ExtensionFileFilter filter = new ExtensionFileFilter( new String{"gif", "jpg"}, "JPEG & GIF Images")
 * chooser.addChoosableFileFilter(filter); chooser.showOpenDialog(this);
 * </pre>
 * <p>Adapted from ExampleFileFilter by Jeff Dinkins</p>
 * @author Jeff Dinkins
 * @author Miguel Mu\u00f1oz
 * @version 1.16 07/26/04
 */
@SuppressWarnings({"MagicCharacter", "StringConcatenation"})
public class ExtensionFileFilter
    extends FileFilter
{
	@NotNull
	private final Set<String> extensions;
	private String description=null;
	private @Nullable String fullDescription=null;
	private boolean useExtensionsInDescription=true;

	/**
	 * Creates a file filter. If no filters are added, then all files are
	 * accepted.
	 *
	 * @see #addExtension
	 */
	public ExtensionFileFilter() {
		super();
		this.extensions = new HashSet<>();
	}

	/**
	 * Creates a file filter that accepts files with the given extension. Example:
	 * new ExtensionFileFilter("jpg");
	 *
	 * @see #addExtension
	 */
	public ExtensionFileFilter(String extension)
	{
		this(null, extension);
	}

	/**
	 * Creates a file filter that accepts the given file type. Example: new
	 * ExtensionFileFilter("jpg", "JPEG Image Images");
	 * <p>
	 * Note that the "." before the extension is not needed. If provided, it will
	 * be ignored.
	 *
	 * @see #addExtension
	 */
	public ExtensionFileFilter(@Nullable String description, String extension)
	{
		this();
		if (extension!=null) {
			addExtension(extension);
		}
		if (description!=null) {
			setDescription(description);
		}
	}

	/**
	 * Creates a file filter from the given string array. Example: new
	 * ExtensionFileFilter(String {"gif", "jpg"});
	 * <p>
	 * Note that the "." before the extension is not needed adn will be ignored.
	 *
	 * @see #addExtension
	 */
	public ExtensionFileFilter(String... filters)
	{
		this(null, filters);
	}

	/**
	 * Creates a file filter from the given string array and description. Example:
	 * new ExtensionFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
	 * <p>
	 * Note that the "." before the extension is not needed and will be ignored.
	 *
	 * @see #addExtension
	 */
	public ExtensionFileFilter(@Nullable String description, String... filters)
	{
		this();
		for (final String filter : filters) {
			// add filters one by one
			addExtension(filter);
		}
		if (description!=null) {
			setDescription(description);
		}
	}

	/**
	 * Return true if this file should be shown in the directory pane, false if it
	 * shouldn't.
	 * <p>
	 * Files that begin with "." are ignored.
	 *
	 * @see #getExtension
	 * @see FileFilter#accept
	 */
	@Override
	public boolean accept(File f)
	{
		if (f!=null)
		{
			if (f.isDirectory())
			{
				return true;
			}
			String extension=getExtension(f);
			return (extension != null) && extensions.contains(extension);
		}
		return false;
	}

	/**
	 * Return the extension portion of the file's name .
	 *
	 * @see #getExtension
	 * @see FileFilter#accept
	 */
	@Nullable
	public String getExtension(File f)
	{
		if (f!=null)
		{
			String filename=f.getName();
			int i=filename.lastIndexOf('.');
			if ((i > 0) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	/**
	 * Adds a filetype "dot" extension to filter against.
	 * <p>
	 * For example: the following code will create a filter that filters out all
	 * files except those that end in ".jpg" and ".tif":
	 * <p>
	 * ExtensionFileFilter filter = new ExtensionFileFilter();
	 * filter.addExtension("jpg"); filter.addExtension("tif");
	 * <p>
	 * Note that the "." before the extension is not needed and will be ignored.
	 */
	public void addExtension(String extension)
	{
		extensions.add(extension.toLowerCase());
		fullDescription=null;
	}


	/**
	 * Returns the human readable description of this filter. For example: "JPEG
	 * and GIF Image Files (*.jpg, *.gif)"
	 *
	 * @see #setDescription
	 * @see #setExtensionListInDescription
	 * @see #isExtensionListInDescription
	 * @see FileFilter#getDescription
	 */
	@Override
	public String getDescription()
	{
		if (fullDescription==null)
		{
			if ((description == null) || isExtensionListInDescription())
			{
				fullDescription = (description == null) ? "(" : (description + " (");
				// build the description from the extension list
				Iterator<String>  iterator = extensions.iterator();
				fullDescription += '.' + iterator.next();
				while (iterator.hasNext()) {
					//noinspection StringConcatenationInLoop
					fullDescription += ", ." + iterator.next();
				}
				fullDescription+=")";
			}
			else
			{
				fullDescription=description;
			}
		}
		return fullDescription;
	}

	/**
	 * Sets the human readable description of this filter. For example:
	 * filter.setDescription("Gif and JPG Images");
	 *
	 * @see #getDescription
	 * @see #setExtensionListInDescription
	 * @see #isExtensionListInDescription
	 */
	public void setDescription(String description)
	{
		this.description=description;
		fullDescription=null;
	}

	/**
	 * Determines whether the extension list (.jpg, .gif, etc) should show up in
	 * the human readable description.
	 * <p>
	 * Only relevent if a description was provided in the constructor or using
	 * setDescription();
	 *
	 * @see #getDescription
	 * @see #setDescription
	 * @see #isExtensionListInDescription
	 */
	public void setExtensionListInDescription(boolean b)
	{
		useExtensionsInDescription=b;
		fullDescription=null;
	}

	/**
	 * Returns whether the extension list (.jpg, .gif, etc) should show up in the
	 * human readable description.
	 * <p>
	 * Only relevent if a description was provided in the constructor or using
	 * setDescription();
	 *
	 * @see #getDescription
	 * @see #setDescription
	 * @see #setExtensionListInDescription
	 */
	public boolean isExtensionListInDescription()
	{
		return useExtensionsInDescription;
	}
}
