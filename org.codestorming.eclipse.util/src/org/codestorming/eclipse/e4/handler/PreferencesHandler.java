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
package org.codestorming.eclipse.e4.handler;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.codestorming.eclipse.util.WorkbenchPreferenceNode;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for opening a {@link PreferenceDialog}.
 * <p>
 * This class is for using in Eclipse RCP applications.
 *
 * @author Thaedrik <thaedrik@gmail.com>
 */
@SuppressWarnings("restriction")
public class PreferencesHandler {

	private static final String PREF_EXTENSION_POINT = "org.eclipse.ui.preferencePages"; //$NON-NLS-1$
	private static final String PREF_PAGE_ATTR_ID = "id"; //$NON-NLS-1$
	private static final String PREF_PAGE_ATTR_NAME = "name"; //$NON-NLS-1$
	private static final String PREF_PAGE_ATTR_CLASS = "class"; //$NON-NLS-1$
	private static final String PREF_PAGE_ATTR_CATEGORY = "category"; //$NON-NLS-1$

	private static final char PM_SEPARATOR = '/';

	@Inject
	private Logger logger;

	@Inject
	private IExtensionRegistry registry;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		PreferenceManager pm = createPreferenceManager();
		PreferenceDialog dialog = new PreferenceDialog(shell, pm);
		dialog.open();
	}

	private PreferenceManager createPreferenceManager() {
		final PreferenceManager pm = new PreferenceManager(PM_SEPARATOR);
		final PreferenceNodeElement[] elements = getElements();

		for (PreferenceNodeElement element : elements) {
			PreferenceNode node = new WorkbenchPreferenceNode(element.id, element.name, null, element.bundleID,
					element.className);
			final String category = element.category;
			if (category == null || category.length() == 0) {
				pm.addToRoot(node);
			} else {
				final IPreferenceNode categoryNode = pm.find(category);
				if (categoryNode == null) {
					pm.addToRoot(node);
				} else {
					categoryNode.add(node);
				}
			}
		}

		return pm;
	}

	private PreferenceNodeElement[] getElements() {
		final IConfigurationElement[] extensions = registry.getConfigurationElementsFor(PREF_EXTENSION_POINT);
		final PreferenceNodeElement[] elements = new PreferenceNodeElement[extensions.length];
		final int length = extensions.length;
		for (int i = 0; i < length; i++) {
			final PreferenceNodeElement nodeElement = new PreferenceNodeElement();
			nodeElement.name = extensions[i].getAttribute(PREF_PAGE_ATTR_NAME);
			nodeElement.className = extensions[i].getAttribute(PREF_PAGE_ATTR_CLASS);
			nodeElement.bundleID = extensions[i].getContributor().getName();
			nodeElement.id = extensions[i].getAttribute(PREF_PAGE_ATTR_ID);
			nodeElement.category = extensions[i].getAttribute(PREF_PAGE_ATTR_CATEGORY);
			if (nodeElement.id != null && nodeElement.name != null && nodeElement.className != null
					&& nodeElement.bundleID != null) {
				elements[i] = nodeElement;
			} else {
				logger.warn(MessageFormat.format(
						"Couldn''t load the preferences extension for the page {0}, of the plugin {1}.",
						nodeElement.name, nodeElement.bundleID));
			}
		}
		Arrays.sort(elements);
		return elements;
	}

	private static class PreferenceNodeElement implements Comparable<PreferenceNodeElement> {

		String id;
		String name;
		String className;
		String bundleID;
		String category;

		@Override
		public int compareTo(PreferenceNodeElement o) {
			final String category = this.category;
			if (id.equals(o.id) && name.equals(o.name) && className.equals(o.className) && bundleID.equals(o.bundleID)
					&& (category != null && category.equals(o.category) || category == null && o.category == null)) {
				return 0;
			}// else
			int thisDepth;
			if (category == null) {
				thisDepth = 0;
			} else {
				thisDepth = categoryDepth(category);
			}
			int otherDepth;
			if (o.category == null) {
				otherDepth = 0;
			} else {
				otherDepth = categoryDepth(o.category);
			}
			final int diffDepth = thisDepth - otherDepth;
			return diffDepth != 0 ? diffDepth : name.compareTo(o.name);
		}

		private static int categoryDepth(String category) {
			final String sep = "" + PM_SEPARATOR;
			if (category.startsWith(sep)) {
				category = category.substring(1, category.length());
			}
			if (category.endsWith(sep)) {
				category = category.substring(0, category.length() - 1);
			}
			return category.split(sep).length;
		}
	}
}