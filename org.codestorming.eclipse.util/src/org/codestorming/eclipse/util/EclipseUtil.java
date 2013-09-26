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

import static org.eclipse.core.runtime.Assert.isNotNull;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * Utility and convenient methods for the Eclipse platform.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class EclipseUtil {

	// Suppressing default constructor, ensuring non-instantiability
	private EclipseUtil() {}

	/**
	 * Add/Remove the specified nature on the given project.
	 * 
	 * @param project The project.
	 * @param natureId The id of the nature to add or remove.
	 */
	public static void toggleProjectNature(IProject project, String natureId) {
		isNotNull(project);
		isNotNull(natureId);
		if (project.exists() && project.isOpen()) {
			try {
				if (project.hasNature(natureId)) {
					internalRemoveProjectNature(project, natureId);
				} else {
					internalAddProjectNature(project, natureId);
				}
			} catch (CoreException e) {
				EclipseUtilActivator.log(e);
			}
		}
	}

	/**
	 * Remove the specified nature from the given {@link IProject}.
	 * 
	 * @param project The {@link IProject project}.
	 * @param natureId The ID of the {@link IProjectNature nature} to remove.
	 */
	public static void removeProjectNature(IProject project, String natureId) {
		isNotNull(project);
		isNotNull(natureId);
		if (project.exists() && project.isOpen()) {
			try {
				internalRemoveProjectNature(project, natureId);
			} catch (CoreException e) {
				EclipseUtilActivator.log(e);
			}
		}
	}

	/**
	 * Add the specified nature to the given {@link IProject}.
	 * 
	 * @param project The {@link IProject project}.
	 * @param natureId The ID of the {@link IProjectNature nature} to add.
	 */
	public static void addProjectNature(IProject project, String natureId) {
		isNotNull(project);
		isNotNull(natureId);
		if (project.exists() && project.isOpen()) {
			try {
				internalAddProjectNature(project, natureId);
			} catch (CoreException e) {
				EclipseUtilActivator.log(e);
			}
		}
	}

	private static void internalRemoveProjectNature(IProject project, String natureId) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		if (natures.length > 0) {
			String[] newNatures = new String[natures.length - 1];
			int i = 0;
			for (String nature : natures) {
				if (!natureId.equals(nature)) {
					newNatures[i++] = nature;
				}
			}
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}
	}

	private static void internalAddProjectNature(IProject project, String natureId) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = natureId;
		description.setNatureIds(newNatures);
		project.setDescription(description, null);
	}

	/**
	 * Transforms the given path into an absolute path.
	 * <p>
	 * Does nothing if the given path is relative to the current workspace.
	 * 
	 * @param path The the path to transform.
	 * @return the absolute path of the given one.
	 */
	public static String getAbsolutePath(String path) {
		final Location location = Platform.getInstanceLocation();
		if (location != null) {
			File file = new File(location.getURL().getFile());
			final String workspacePath = file.toString();
			if (!path.startsWith(workspacePath) && path.startsWith("/")) {
				path = workspacePath + path;
			}
		}
		return path;
	}

	/**
	 * Returns the current {@link IWorkspace workspace}.
	 * <p>
	 * Convenient method for {@link ResourcesPlugin#getWorkspace()}.
	 * 
	 * @return the current {@link IWorkspace workspace}.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
}
