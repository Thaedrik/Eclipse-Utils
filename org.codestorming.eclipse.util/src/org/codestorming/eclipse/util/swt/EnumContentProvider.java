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
package org.codestorming.eclipse.util.swt;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * {@link IStructuredContentProvider} using an enumeration java class for providing
 * enumeration literals.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class EnumContentProvider implements IStructuredContentProvider {

	private static EnumContentProvider INSTANCE;
	private static int nb;

	/**
	 * Returns the instance of {@link EnumContentProvider}.
	 * 
	 * @return the instance of {@link EnumContentProvider}.
	 */
	public static EnumContentProvider getInstance() {
		nb++;
		if (INSTANCE == null) {
			INSTANCE = new EnumContentProvider();
		}
		return INSTANCE;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] enumConstants = {};
		if (inputElement instanceof Class) {
			final Class<?> c = (Class<?>) inputElement;
			if (c.isEnum()) {
				enumConstants = c.getEnumConstants();
			}
		}
		return enumConstants;
	}

	@Override
	public void dispose() {
		nb--;
		if (nb <= 0) {
			nb = 0;
			INSTANCE = null;
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();
	}
}
