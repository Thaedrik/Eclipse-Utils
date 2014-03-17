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

import org.codestorming.eclipse.util.pde.BundleActivatorWithLog;
import org.osgi.framework.BundleContext;

/**
 * Activator of the Eclipse Util plug-in.
 */
public class EclipseUtilActivator extends BundleActivatorWithLog {

	public static final String PLUGIN_ID = "org.codestorming.eclipse.util";

	// The shared instance
	private static EclipseUtilActivator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared {@link EclipseUtilActivator} instance.
	 * 
	 * @return the shared {@link EclipseUtilActivator} instance.
	 * @since 3.0
	 */
	public static EclipseUtilActivator getDefault() {
		return plugin;
	}

	@Override
	public String getPluginID() {
		return PLUGIN_ID;
	}

}
