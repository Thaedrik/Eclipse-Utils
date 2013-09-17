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
package org.codestorming.eclipse.util;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;

/**
 * {@link PreferenceNode} using the bundle ID for retrieving a page's class.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class WorkbenchPreferenceNode extends PreferenceNode {

	private String className;
	private String label;
	private ImageDescriptor imageDescriptor;
	private String bundleID;

	/**
	 * Creates a preference node with the given id, label, and image, bundleID and the
	 * preference page class name. The preference node assumes (sole) responsibility for
	 * disposing of the image; this will happen when the node is disposed.
	 * 
	 * @param id the node id
	 * @param label the label used to display the node in the preference dialog's tree
	 * @param image the image displayed left of the label in the preference dialog's tree,
	 *        or <code>null</code> if none
	 * @param bundleID ID of the bundle containing the page's class.
	 * @param className the class name of the preference page; this class must implement
	 *        <code>IPreferencePage</code>
	 */
	public WorkbenchPreferenceNode(String id, String label, ImageDescriptor image, String bundleID, String className) {
		super(id, label, image, className);
		this.label = label;
		this.imageDescriptor = image;
		this.className = className;
		this.bundleID = bundleID;
	}

	/**
	 * Creates a new instance of the given class <code>className</code>.
	 * 
	 * @param className Name of the class to create.
	 * @return new Object or <code>null</code> in case of failures.
	 */
	private Object createObject(String className) {
		Assert.isNotNull(bundleID);
		Assert.isNotNull(className);
		try {
			final Bundle bundle = Platform.getBundle(bundleID);
			if (bundle == null) {
				return null;
			}// else
			Class<?> cl = bundle.loadClass(className);
			if (cl != null) {
				return cl.newInstance();
			}
		} catch (ClassNotFoundException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (NoSuchMethodError e) {
			return null;
		}
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on IPreferenceNode.
	 */
	public void createPage() {
		IPreferencePage page = (IPreferencePage) createObject(className);
		if (getLabelImage() != null) {
			page.setImageDescriptor(imageDescriptor);
		}
		page.setTitle(label);
		setPage(page);
	}

}
