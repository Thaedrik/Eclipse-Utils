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

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.layout.GridLayout;

/**
 * Helper for creating SWT {@link GridLayout GridLayouts}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class GridLayoutHelper {

	// Suppressing the default constructor, ensuring non-instanttiability
	private GridLayoutHelper() {}

	/**
	 * Create a {@link GridLayout} with one column and no margin and default spacing (5).
	 * 
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout() {
		return newGridLayout(1, false, 0, 0, 5, 5);
	}

	/**
	 * Create a {@link GridLayout} with the given number of columns (of non-equal width)
	 * and no margin and default spacing (5).
	 * 
	 * @param columns Number of columns of the {@code GridLayout} to create.
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout(int columns) {
		return newGridLayout(columns, false, 0, 0, 5, 5);
	}

	/**
	 * Create a {@link GridLayout} with the given number of columns, the
	 * {@code equalWidth} and no margin and default spacing (5).
	 * 
	 * @param columns Number of columns of the {@code GridLayout} to create.
	 * @param equalWidth Indicates if the columns of the layout should be of equal width.
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout(int columns, boolean equalWidth) {
		return newGridLayout(columns, equalWidth, 0, 0, 5, 5);
	}

	/**
	 * Create a {@link GridLayout} with the given {@code marginHeight} and
	 * {@code marginWidth} and with only one column and default spacing (5).
	 * 
	 * @param marginHeight The layout marginHeight
	 * @param marginWidthThe layout marginWidth
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout(int marginHeight, int marginWidth) {
		return newGridLayout(1, false, marginHeight, marginWidth, 5, 5);
	}

	/**
	 * Create a {@link GridLayout} with the given {@code marginHeight} and
	 * {@code marginWidth} and with the specified number of columns (of non-equal width)
	 * and default spacing (5).
	 * 
	 * @param columns The number of columns
	 * @param marginHeight The layout marginHeight
	 * @param marginWidth The layout marginWidth
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout(int columns, int marginHeight, int marginWidth) {
		return newGridLayout(columns, false, marginHeight, marginWidth, 5, 5);
	}

	/**
	 * Create a {@link GridLayout} with the given {@code marginHeight},
	 * {@code marginWidth} and with the specified number of columns (of non-equal width)
	 * and the specified spacing.
	 * 
	 * @param columns The number of columns
	 * @param marginHeight The layout marginHeight
	 * @param marginWidth The layout marginWidth
	 * @param hSpacing The vertical spacing
	 * @param vSpacing The vertical spacing
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout(int columns, int marginHeight, int marginWidth, int hSpacing, int vSpacing) {
		return newGridLayout(columns, false, marginHeight, marginWidth, hSpacing, vSpacing);
	}

	/**
	 * Create a {@link GridLayout} with the given {@code marginHeight},
	 * {@code marginWidth} and with the specified number of columns and the specified
	 * spacing.
	 * 
	 * @param columns The number of columns
	 * @param equalWidth Indicates if the columns of the layout should be of equal width.
	 * @param marginHeight The layout marginHeight
	 * @param marginWidth The layout marginWidth
	 * @param hSpacing The vertical spacing
	 * @param vSpacing The vertical spacing
	 * @return the created {@link GridLayout}.
	 */
	public static GridLayout newGridLayout(int columns, boolean equalWidth, int marginHeight, int marginWidth,
			int hSpacing, int vSpacing) {
		Assert.isTrue(columns >= 1);
		Assert.isTrue(marginHeight >= 0 && marginWidth >= 0);
		Assert.isTrue(hSpacing >= 0 && vSpacing >= 0);
		GridLayout gl = new GridLayout(columns, equalWidth);
		gl.marginHeight = marginHeight;
		gl.marginWidth = marginWidth;
		gl.horizontalSpacing = hSpacing;
		gl.verticalSpacing = vSpacing;
		return gl;
	}
}
