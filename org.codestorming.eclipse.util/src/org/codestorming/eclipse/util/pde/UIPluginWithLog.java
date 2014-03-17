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
package org.codestorming.eclipse.util.pde;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Singleton {@link AbstractUIPlugin} with log methods.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public abstract class UIPluginWithLog extends AbstractUIPlugin {

	/**
	 * Logs the given status. The status is distributed to the log listeners installed on
	 * this log and then to the log listeners installed on the platform.
	 * 
	 * @param status the status to log.
	 * @since 3.0
	 */
	public void log(IStatus status) {
		getLog().log(status);
	}

	/**
	 * Logs the given {@link Exception exception}.
	 * 
	 * @param exception The {@code exception} to log.
	 * @since 3.0
	 */
	public void log(Exception exception) {
		log(exception.getMessage(), IStatus.ERROR, exception);
	}

	/**
	 * Logs the given message with the specified {@code severity}.
	 * <p>
	 * The {@code severity} is one of :
	 * <ul>
	 * <li>{@link IStatus#INFO}</li>
	 * <li>{@link IStatus#WARNING}</li>
	 * <li>{@link IStatus#ERROR}</li>
	 * </ul>
	 * 
	 * @param message The message to log.
	 * @param severity The severity of the message.
	 * @since 3.0
	 */
	public void log(String message, int severity) {
		log(message, severity, null);
	}

	/**
	 * Logs the given message with the specified {@code severity} and {@link Exception
	 * exception}.
	 * <p>
	 * The {@code severity} is one of :
	 * <ul>
	 * <li>{@link IStatus#INFO}</li>
	 * <li>{@link IStatus#WARNING}</li>
	 * <li>{@link IStatus#ERROR}</li>
	 * </ul>
	 * The {@code exception} can be {@code null}.
	 * 
	 * @param message The message to log.
	 * @param severity The severity of the message.
	 * @param exception The originating exception.
	 * @since 3.0
	 */
	public void log(String message, int severity, Exception exception) {
		IStatus status = new Status(severity, getPluginID(), message, exception);
		log(status);
	}

	/**
	 * Returns this plugin's ID.
	 * 
	 * @return this plugin's ID.
	 */
	public abstract String getPluginID();
}
