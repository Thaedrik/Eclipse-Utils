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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.codestorming.eclipse.util.EclipseUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * A {@code ColorRegistry} provides unique instances of SWT {@link Color Colors} per
 * {@link Display} and automatically disposes of them when their display is disposed.
 * <p>
 * The colors managed by a {@code ColorRegistry}, <strong>should not</strong> be disposed
 * by users, as they may be used by others.
 * <p>
 * A {@link Color} is created from the three components <strong>red, green, blue</strong>,
 * and these components can be passed
 * <ul>
 * <li><em>directly</em> with {@link ColorRegistry#getColor(int, int, int)}</li>
 * <li>through <em>{@link RGB}</em> with {@link ColorRegistry#getColor(RGB)}</li>
 * <li>in <em>hexadecimal</em> with {@link ColorRegistry#getColor(int)}</li>
 * </ul>
 * Each color component is an integer between {@code 0} and {@code 255} ({@code 0x00} to
 * {@code 0xFF} in hexadecimal).
 * 
 * @since 2.0
 * @author Thaedrik <thaedrik@gmail.com>
 */
public final class ColorRegistry {

	// Color registries for each display.
	private static Map<Display, ColorRegistry> registries = Collections
			.synchronizedMap(new HashMap<Display, ColorRegistry>());

	// Existing colors for this ColorRegistry.
	private Map<Integer, Color> colors = Collections.synchronizedMap(new TreeMap<Integer, Color>());

	// Display of this ColorRegistry colors.
	private Display display;

	/**
	 * Returns the {@link Color} corresponding to the given hexadecimal code.
	 * 
	 * @param hexCode Color hexadecimal code.
	 * @return the {@link Color} corresponding to the given hexadecimal code.
	 */
	public static Color getColor(int hexCode) {
		ColorRegistry registry = getRegistry(EclipseUtil.getDisplay());
		return registry != null ? registry.internalGetColor(hexCode) : null;
	}

	/**
	 * Returns the {@link Color} corresponding to the given {@link RGB}.
	 * 
	 * @param rgb The {@link RGB}.
	 * @return the {@link Color} corresponding to the given {@link RGB}.
	 */
	public static Color getColor(RGB rgb) {
		ColorRegistry registry = getRegistry(EclipseUtil.getDisplay());
		int hexCode = (rgb.red << 16) | (rgb.green << 8) | rgb.blue;
		return registry != null ? registry.internalGetColor(hexCode) : null;
	}

	/**
	 * Returns the {@link Color} corresponding to the given three color components.
	 * 
	 * @param red The red component.
	 * @param green The green component.
	 * @param blue The blue component.
	 * @return the {@link Color} corresponding to the given three color components.
	 */
	public static Color getColor(int red, int green, int blue) {
		return getColor(new RGB(red, green, blue));
	}

	/**
	 * Retrieve the registry for the given {@link Display} or create a new one.
	 * 
	 * @param display The display.
	 * @return the {@link ColorRegistry} for the given {@link Display}.
	 */
	private static ColorRegistry getRegistry(Display display) {
		if (display.isDisposed())
			return null;
		ColorRegistry registry = registries.get(display);
		if (registry == null) {
			registry = new ColorRegistry(display);
			registries.put(display, registry);
			installDisposeListener(display);
		}
		return registry;
	}

	private static void installDisposeListener(final Display display) {
		// Listener for cleaning up the color registry when the display is disposed
		final Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				final ColorRegistry registry = registries.get(display);
				registries.remove(registry);
				synchronized (registry.colors) {
					for (final Color color : registry.colors.values()) {
						color.dispose();
					}
					registry.colors.clear();
				}
			}
		};
		// Running in the given Display UI thread.
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				display.addListener(SWT.Dispose, listener);
			}
		});
	}

	/**
	 * Creates a new {@code ColorRegistry}.
	 * 
	 * @param display The display for which the colors will be created.
	 */
	private ColorRegistry(Display display) {
		this.display = display;
	}

	private Color internalGetColor(int hexCode) {
		checkDispose();
		Color color = colors.get(hexCode);
		if (color == null) {
			color = createColor(hexCode);
			colors.put(hexCode, color);
		}
		return color;
	}

	/**
	 * Create a new {@link Color} by decomposing the given {@code hexCode} into the three
	 * color components.
	 * 
	 * @param hexCode The color hexadecimal code.
	 * @return the created color.
	 */
	private Color createColor(int hexCode) {
		int red = (0xFF0000 & hexCode) >> 16;
		int green = (0x00FF00 & hexCode) >> 8;
		int blue = (0x0000FF & hexCode);
		return new Color(display, red, green, blue);
	}

	/**
	 * Check if the {@link Display} is disposed.
	 * 
	 * @throws SWTError ERROR_DEVICE_DISPOSED if the {@link Display} of this
	 *         {@code ColorRegistry} is disposed.
	 */
	private void checkDispose() {
		if (display.isDisposed()) {
			throw new SWTError(SWT.ERROR_DEVICE_DISPOSED);
		}
	}
}
