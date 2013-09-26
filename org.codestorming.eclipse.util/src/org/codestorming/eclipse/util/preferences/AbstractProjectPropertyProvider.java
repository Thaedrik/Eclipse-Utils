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
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Base implementation for providing a plugin properties from a {@link IProject project}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public abstract class AbstractProjectPropertyProvider {

	/**
	 * The project on which the properties are registered.
	 */
	protected final IProject project;

	private ScopedPreferenceStore preferenceStore;

	/**
	 * Creates a new {@code AbstractProjectPropertyProvider}.
	 * 
	 * @param project The project (must <strong>not</strong> be {@code null}).
	 */
	public AbstractProjectPropertyProvider(IProject project) {
		Assert.isNotNull(project, "The project cannot be null");
		this.project = project;
	}

	/**
	 * Returns the {@link IPreferenceStore} for the project specified on this
	 * {@link AbstractProjectPropertyProvider}.
	 * <p>
	 * Uses the specified {@link IProject} and {@link #getId()} to create a
	 * {@link ScopedPreferenceStore}.
	 * 
	 * @return the {@link IPreferenceStore} for the project specified on this
	 *         {@link AbstractProjectPropertyProvider}.
	 */
	protected IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			ProjectScope scope = new ProjectScope(project);
			preferenceStore = new ScopedPreferenceStore(scope, getId());
		}
		return preferenceStore;
	}

	/**
	 * Returns the identifier of the preference store.
	 * 
	 * @return the identifier of the preference store.
	 */
	protected abstract String getId();
}
