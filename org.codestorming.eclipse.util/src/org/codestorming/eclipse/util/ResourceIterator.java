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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * {@link Iterator} that iterates recursively over a {@link IResource} and its members.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @since 3.0
 */
public class ResourceIterator implements Iterator<IResource> {

	protected final IResource[] members;
	protected int current;
	protected ResourceIterator currentIterator;

	/**
	 * Creates a new {@code ResourceIterator}.
	 * 
	 * @param resource
	 */
	public ResourceIterator(IResource resource) {
		this(new IResource[] { resource });
	}

	protected ResourceIterator(IResource[] resources) {
		members = resources;
	}

	@Override
	public boolean hasNext() {
		return currentIterator != null && currentIterator.hasNext() || current < members.length;
	}

	@Override
	public IResource next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}// else
		IResource next = null;
		if (currentIterator != null && currentIterator.hasNext()) {
			next = currentIterator.next();
		} else {
			next = members[current];
			current++;
			if (next instanceof IContainer) {
				try {
					currentIterator = new ResourceIterator(((IContainer) next).members(IResource.FILE
							| IResource.FOLDER | IResource.PROJECT | IResource.ROOT));
				} catch (CoreException e) {
					EclipseUtilActivator.getDefault().log(e);
				}
			}
		}
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
