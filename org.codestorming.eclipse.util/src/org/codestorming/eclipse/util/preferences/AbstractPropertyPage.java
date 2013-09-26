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
package org.codestorming.eclipse.util.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Abstract implementation of a property page.
 * <p>
 * This implementation provides the {@link IPreferenceStore}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public abstract class AbstractPropertyPage extends PropertyPage {

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		ProjectScope scope = new ProjectScope(getProject());
		return new ScopedPreferenceStore(scope, getId());
	}

	/**
	 * Returns the {@link IPreferenceStore} identifier.
	 * <p>
	 * By convention the ID is the ID of the plugin that declares this page.
	 * 
	 * @return the {@link IPreferenceStore} identifier.
	 */
	protected abstract String getId();

	/**
	 * Returns the project of this page.
	 * 
	 * @return the project of this page.
	 */
	protected IProject getProject() {
		Object project;
		IAdaptable element = getElement();
		if (element instanceof IProject) {
			project = element;
		} else {
			project = element.getAdapter(IProject.class);
		}
		return (IProject) project;
	}

}
