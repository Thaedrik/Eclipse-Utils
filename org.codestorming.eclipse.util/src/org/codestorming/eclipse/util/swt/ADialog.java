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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Abstract implementation of an SWT {@link Dialog} returning a value of the specified
 * type when closed.
 * 
 * @param <T> The return type of the {@link #open()} method.
 * @since 2.0
 * @author Thaedrik <thaedrik@gmail.com>
 */
public abstract class ADialog<T> extends Dialog {

	private Shell shell;

	/**
	 * Result to return when the dialog is closed.
	 */
	protected T result;

	/**
	 * The style of the shell.<br>
	 * Default value : {@code SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL}
	 */
	protected int shellStyle = SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL;

	private int minWidth;
	private int minHeight;

	/**
	 * Constructs a new instance of this class given only its
	 * parent.
	 * 
	 * @param parent a shell which will be the parent of the new instance
	 * 
	 * @exception IllegalArgumentException <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *            </ul>
	 * @exception SWTException <ul>
	 *            <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that
	 *            created the parent</li>
	 *            </ul>
	 */
	public ADialog(Shell parent) {
		super(parent);
	}

	/**
	 * Constructs a new instance of this class given its parent
	 * and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be built
	 * by <em>bitwise OR</em>'ing together (that is, using the <code>int</code> "|"
	 * operator) two or more of those <code>SWT</code> style constants. The class
	 * description lists the style constants that are applicable to the class. Style bits
	 * are also inherited from superclasses.
	 * 
	 * @param parent a shell which will be the parent of the new instance
	 * @param style the style of dialog to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *            </ul>
	 * @exception SWTException <ul>
	 *            <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that
	 *            created the parent</li>
	 *            </ul>
	 * 
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 */
	public ADialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Set the style of the shell created when the dialog is opening.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of {@link Shell}, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the <code>int</code> "|"
	 * operator) two or more of those <code>SWT</code> style constants.
	 * 
	 * @param style The shell's style.
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#NO_TRIM
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#ON_TOP
	 * @see SWT#TOOL
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 * @see SWT#SHEET
	 */
	public void setShellStyle(int style) {
		shellStyle = style;
	}

	/**
	 * Set the minimum height of this dialog.
	 * 
	 * @param height The minimum height.
	 */
	public void setMinimumHeight(int height) {
		minHeight = height;
	}

	/**
	 * Set the minimum width of this dialog.
	 * 
	 * @param width The minimum width.
	 */
	public void setMinimumWidth(int width) {
		minWidth = width;
	}

	/**
	 * Open this dialog and return the result when closed.
	 * <p>
	 * The shell's style default value :<br>
	 * {@code SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL}
	 * 
	 * @return the result.
	 */
	public T open() {
		Shell parent = getParent();
		shell = new Shell(parent, shellStyle);
		shell.setText(getText());

		// Dialog content creation
		createControl(shell);

		shell.setMinimumSize(minWidth, minHeight);
		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create the dialog content.
	 * <p>
	 * The given {@code parent} is a {@link Shell} without any layout defined.
	 * 
	 * @param parent The parent SWT composite.
	 */
	protected abstract void createControl(Composite parent);

	/**
	 * Called before the dialog closes.<br>
	 * This method is <strong>not called</strong> if the dialog is already closed.
	 * <p>
	 * <em>Returns {@code true} by default.</em>
	 * 
	 * @return {@code true} if the dialog can close;<br>
	 *         {@code false} otherwise.
	 */
	protected boolean performClose() {
		return true;
	}

	/**
	 * Close this dialog.
	 * <p>
	 * The dialog will be closed only if {@link #performClose()} returns {@code true}.
	 * 
	 * @throws SWTException ERROR_THREAD_INVALID_ACCESS if not called from the thread that
	 *         created the receiver.
	 */
	protected final void close() {
		if (!shell.isDisposed() && performClose()) {
			shell.close();
		}
	}

}
