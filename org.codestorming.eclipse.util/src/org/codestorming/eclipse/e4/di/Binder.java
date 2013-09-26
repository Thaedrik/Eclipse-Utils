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

import static org.eclipse.core.runtime.Assert.isNotNull;

import org.eclipse.e4.core.di.IBinding;
import org.eclipse.e4.core.di.IInjector;

/**
 * Implementation of the {@link IBinder}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
@SuppressWarnings("restriction")
public abstract class Binder implements IBinder {

	private IInjector injector;

	@Override
	public final void configure(IInjector injector) {
		isNotNull(injector);
		this.injector = injector;
		configure();
	}

	protected abstract void configure();

	@Override
	public IBinding bind(Class<?> clazz) {
		isNotNull(clazz);
		return injector.addBinding(clazz);
	}

}
