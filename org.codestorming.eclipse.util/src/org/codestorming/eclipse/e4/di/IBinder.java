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
package org.codestorming.eclipse.e4.di;

import org.eclipse.e4.core.di.IBinding;
import org.eclipse.e4.core.di.IInjector;

/**
 * Classes implementing {@code IBinder} are responsible for registering bindings into the
 * Eclipse {@link IInjector}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
@SuppressWarnings("restriction")
public interface IBinder {

	/**
	 * Id of the extension point that allows to register {@code IBinders}.
	 */
	public static final String EXTENSION_POINT_ID = "org.codestorming.eclipse.util.binder";

	/**
	 * Configures this {@code IBinder} for the given {@link IInjector injector}.
	 * 
	 * @param injector
	 */
	public void configure(IInjector injector);

	/**
	 * Creates a binding for the given {@code class} into this binder's injector.
	 * 
	 * @param clazz The {@code class} for which to create a {@link IBinding binding}.
	 * @return the created {@link IBinding binding}.
	 */
	public IBinding bind(Class<?> clazz);
}
