/***************************************************************************
 * Copyright (c) 2013 Codestorming.org.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Codestorming - initial API and implementation
 ****************************************************************************/
package org.codestorming.eclipse.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

import org.codestorming.eclipse.util.EclipseUtilActivator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * A {@code TemporaryFile} is a file created in the user workspace and deleted when the
 * JVM (properly) terminates.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @since 1.2
 */
public class TemporaryFile {

	private File tempFile;

	private static String generateFileName() {
		return UUID.randomUUID().toString().concat(".temp"); //$NON-NLS-1$
	}

	/**
	 * Creates a new {@code TemporaryFile} from the given {@link InputStream}.
	 * 
	 * @param inputStream
	 * @throws IOException if an error happens during the copy of the {@code inputStream}
	 *         content.
	 */
	public TemporaryFile(InputStream inputStream) throws IOException {
		Bundle bundle = Platform.getBundle(EclipseUtilActivator.PLUGIN_ID);
		tempFile = bundle.getDataFile(generateFileName());
		if (tempFile == null) {
			throw new IllegalStateException("This system does not support user data storage.");
		}
		tempFile.deleteOnExit();
		copyContent(inputStream);
	}

	/**
	 * Copy the {@code inputStream} content into the temporary file.
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	private void copyContent(InputStream inputStream) throws IOException {
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
		try {
			final byte[] buffer = new byte[2048];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} finally {
			out.close();
		}
	}

	/**
	 * @see java.io.File#getName()
	 */
	public String getName() {
		return tempFile.getName();
	}

	/**
	 * @see java.io.File#getParent()
	 */
	public String getParent() {
		return tempFile.getParent();
	}

	/**
	 * @see java.io.File#getParentFile()
	 */
	public File getParentFile() {
		return tempFile.getParentFile();
	}

	/**
	 * @see java.io.File#getPath()
	 */
	public String getPath() {
		return tempFile.getPath();
	}

	/**
	 * @see java.io.File#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return tempFile.getAbsolutePath();
	}

	/**
	 * @see java.io.File#getAbsoluteFile()
	 */
	public File getAbsoluteFile() {
		return tempFile.getAbsoluteFile();
	}

	/**
	 * @see java.io.File#toURI()
	 */
	public URI toURI() {
		return tempFile.toURI();
	}

	/**
	 * @see java.io.File#lastModified()
	 */
	public long lastModified() {
		return tempFile.lastModified();
	}

	/**
	 * @see java.io.File#length()
	 */
	public long length() {
		return tempFile.length();
	}

	/**
	 * @see java.io.File#compareTo(java.io.File)
	 */
	public int compareTo(File pathname) {
		return tempFile.compareTo(pathname);
	}

	/**
	 * @see java.io.File#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return tempFile.equals(obj);
	}

	/**
	 * @see java.io.File#hashCode()
	 */
	public int hashCode() {
		return tempFile.hashCode();
	}

	/**
	 * @see java.io.File#toString()
	 */
	public String toString() {
		return tempFile.toString();
	}

}
