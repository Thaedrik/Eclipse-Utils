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

/**
 * Activator of the Eclipse Util plug-in.
 */
public class EclipseUtilActivator extends BundleActivatorWithLog {
	
	public static final String PLUGIN_ID = "org.codestorming.eclipse.util";

	@Override
	public String getPluginID() {
		return PLUGIN_ID;
	}

}
