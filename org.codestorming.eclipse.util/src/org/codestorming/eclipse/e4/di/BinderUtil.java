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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Utility class for {@link IBinder binders}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class BinderUtil {

	// Suppressing default constructor, ensuring non-instantiability
	private BinderUtil() {}

	/**
	 * Retrieve the registered {@link IBinder binders} and load the necessary instances of
	 * these binders.
	 * 
	 * @param registry Extension registry (must not be {@code null}).
	 * @return new instances of the registered {@link IBinder binders}.
	 */
	public static List<IBinder> getBinders(IExtensionRegistry registry) {
		Assert.isNotNull(registry);
		final List<BinderElement> binderElements = internalGetAllBinders(registry);
		// Removing unecessary binders
		for (final BinderElement binderElement : binderElements.toArray(new BinderElement[0])) {
			if (binderElement.superBinderClass != null
					&& binderElement.superBinderClass.isAssignableFrom(binderElement.binderClass)) {
				final ListIterator<BinderElement> iter = binderElements.listIterator();
				while (iter.hasNext()) {
					if (iter.next().binderClass == binderElement.superBinderClass) {
						iter.remove();
						break;
					}
				}
			}
		}
		// Loading binders
		final List<IBinder> binders = new ArrayList<IBinder>();
		for (final BinderElement binderElement : binderElements) {
			try {
				final Object binder = binderElement.binderClass.newInstance();
				binders.add((IBinder) binder);
			} catch (InstantiationException e) {
				System.err.println("Couldn't instantiate " + binderElement.binderClass.getName() //$NON-NLS-1$
						+ " : has it a public default constructor ?");//$NON-NLS-1$
			} catch (IllegalAccessException e) {
				System.err.println("Couldn't instantiate " + binderElement.binderClass.getName() //$NON-NLS-1$
						+ " : constructor is it public ?");//$NON-NLS-1$
			}
		}
		return binders;
	}

	@SuppressWarnings("unchecked")
	private static List<BinderElement> internalGetAllBinders(IExtensionRegistry registry) {
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(IBinder.EXTENSION_POINT_ID);
		final List<BinderElement> binderElements = new ArrayList<BinderElement>();
		for (final IConfigurationElement element : elements) {
			final String binderName = element.getAttribute("class");//$NON-NLS-1$
			final IConfigurationElement[] inheritance = element.getChildren("inheritance");//$NON-NLS-1$
			final Class<?> binderClass = findBinderClass(element, binderName);
			Class<?> superBinderClass = null;
			if (binderClass != null) {
				if (inheritance.length > 0) {
					final String superBinderBundle = inheritance[0].getAttribute("bundleId");//$NON-NLS-1$
					final String superBinderName = inheritance[0].getAttribute("class");//$NON-NLS-1$
					try {
						superBinderClass = findBinderClass(superBinderBundle, superBinderName);
					} catch (ClassNotFoundException e) {
						System.err.println("Couldn't find IBinder " + superBinderName //$NON-NLS-1$
								+ " in bundle " + superBinderBundle);//$NON-NLS-1$
					}
				}
				if (IBinder.class.isAssignableFrom(binderClass)
						&& (superBinderClass == null || IBinder.class.isAssignableFrom(superBinderClass))) {
					binderElements.add(new BinderElement((Class<? extends IBinder>) binderClass,
							(Class<? extends IBinder>) superBinderClass));
				}
			}
		}
		return binderElements;
	}

	private static Class<?> findBinderClass(IConfigurationElement element, String name) {
		Class<?> binderClass = null;
		try {
			binderClass = Class.forName(name);
		} catch (ClassNotFoundException e) {
			// Trying another way
			final String bundleId = element.getContributor().getName();
			try {
				binderClass = findBinderClass(bundleId, name);
			} catch (ClassNotFoundException e1) {
				System.err.println("Couldn't find IBinder " + name);//$NON-NLS-1$
			}
		}
		return binderClass;
	}

	private static Class<?> findBinderClass(String bundleId, String name) throws ClassNotFoundException {
		Class<?> binderClass = null;
		final Bundle bundle = Platform.getBundle(bundleId);
		if (bundle != null) {
			binderClass = bundle.loadClass(name);
		}
		return binderClass;
	}

	private static class BinderElement {

		final Class<? extends IBinder> binderClass;
		final Class<? extends IBinder> superBinderClass;

		public BinderElement(Class<? extends IBinder> binderClass, Class<? extends IBinder> superBinderClass) {
			this.binderClass = binderClass;
			this.superBinderClass = superBinderClass;
		}
	}
}
