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

import org.codestorming.eclipse.util.EclipseUtilActivator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Utility class for Standard Widget Toolkit (SWT).
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class SWTUtil {

	public static final int BUTTON_STD_WIDTH = 100;

	// Suppressing default constructor, ensuring non-instantiability.
	private SWTUtil() {}

	/**
	 * Returns the current {@link Display} if this method is called in an UI thread,
	 * otherwise returns the default display.
	 * 
	 * @return the current {@link Display} if this method is called in an UI thread,
	 *         otherwise returns the default display.
	 */
	public static Display getDisplay() {
		Display display = Display.getCurrent();
		return display != null ? display : Display.getDefault();
	}

	/**
	 * Returns the system default {@link Font font}.
	 * <p>
	 * Convenient method for <code>Display.getSystemFont()</code>.
	 * 
	 * @return the system default {@link Font font}.
	 */
	public static Font getDefaultFont() {
		return getDisplay().getSystemFont();
	}

	/**
	 * Create a {@link Text} field in which only integers can be inserted.
	 * 
	 * @param parent The SWT parent composite.
	 * @param style The style of the text field.
	 * @return the created {@link Text} field.
	 */
	public static Text createTextNumberWidget(Composite parent, int style) {
		final Text txt = new Text(parent, style);
		addKeyListenerForTextNumber(txt);
		return txt;
	}

	/**
	 * Create a {@link Text} field in which only integers can be inserted.
	 * 
	 * @param toolkit The {@link FormToolkit} to use to create the text field.
	 * @param parent The SWT parent composite.
	 * @param value The initial value (or {@code null})
	 * @return the created {@link Text} field.
	 */
	public static Text createTextNumberWidget(FormToolkit toolkit, Composite parent, Long value) {
		final Text txt = toolkit.createText(parent, value == null ? null : String.valueOf(value));
		addKeyListenerForTextNumber(txt);
		return txt;
	}

	/**
	 * Create a {@link Text} field in which only integers can be inserted.
	 * 
	 * @param toolkit The {@link FormToolkit} to use to create the text field.
	 * @param parent The SWT parent composite.
	 * @param style The text field style.
	 * @param value The initial value (or {@code null})
	 * @return the created {@link Text} field.
	 */
	public static Text createTextNumberWidget(FormToolkit toolkit, Composite parent, int style, Long value) {
		final Text txt = toolkit.createText(parent, value == null ? null : String.valueOf(value), style);
		addKeyListenerForTextNumber(txt);
		return txt;
	}

	/**
	 * Create a {@link Text} field in which only a double can be inserted.
	 * 
	 * @param parent The SWT parent composite.
	 * @param style The style of the text field.
	 * @return the created {@link Text} field.
	 */
	public static Text createTextDoubleWidget(Composite parent, int style) {
		final Text txt = new Text(parent, style);
		addKeyListenerForTextDouble(txt);
		return txt;
	}

	/**
	 * Create a {@link Text} field in which only a double can be inserted.
	 * 
	 * @param toolkit The {@link FormToolkit} to use to create the text field.
	 * @param parent The SWT parent composite.
	 * @param value The initial value (or {@code null})
	 * @return the created {@link Text} field.
	 */
	public static Text createTextDoubleWidget(FormToolkit toolkit, Composite parent, Double value) {
		final Text txt = toolkit.createText(parent, value == null ? null : String.valueOf(value));
		addKeyListenerForTextDouble(txt);
		return txt;
	}

	/**
	 * Create a {@link Text} field in which only a double can be inserted.
	 * 
	 * @param toolkit The {@link FormToolkit} to use to create the text field.
	 * @param parent The SWT parent composite.
	 * @param style The text field style.
	 * @param value The initial value (or {@code null})
	 * @return the created {@link Text} field.
	 */
	public static Text createTextDoubleWidget(FormToolkit toolkit, Composite parent, int style, Double value) {
		final Text txt = toolkit.createText(parent, value == null ? null : String.valueOf(value), style);
		addKeyListenerForTextDouble(txt);
		return txt;
	}

	private static void addKeyListenerForTextNumber(Text txt) {
		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				final int keyCode = e.keyCode;
				// Code 8 : Key "Backspace"
				// Code 16777223 : Key "begin"
				if (keyCode != 8 && keyCode != 16777223 && keyCode != SWT.ARROW_LEFT && keyCode != SWT.ARROW_RIGHT
						&& keyCode != (int) SWT.DEL && keyCode != SWT.CAPS_LOCK && keyCode != SWT.END
						&& (e.stateMask & SWT.CTRL) == 0 && (e.stateMask & SWT.ALT) == 0) {
					e.doit = e.character == '0' || e.character == '1' || e.character == '2' || e.character == '3'
							|| e.character == '4' || e.character == '5' || e.character == '6' || e.character == '7'
							|| e.character == '8' || e.character == '9';
				}
			}
		});
	}

	private static void addKeyListenerForTextDouble(final Text txt) {
		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				final int keyCode = e.keyCode;
				// Code 8 : Key "Backspace"
				// Code 16777223 : Key "begin"
				if (keyCode != 8 && keyCode != 16777223 && keyCode != SWT.ARROW_LEFT && keyCode != SWT.ARROW_RIGHT
						&& keyCode != (int) SWT.DEL && keyCode != SWT.CAPS_LOCK && keyCode != SWT.END
						&& (e.stateMask & SWT.CTRL) == 0 && (e.stateMask & SWT.ALT) == 0) {
					final char c = e.character;
					e.doit = c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6'
							|| c == '7' || c == '8' || c == '9' || c == '.' && !txt.getText().contains(".");
				}
			}
		});
	}

	/**
	 * Sets the minimum width of the given {@link Button button} to
	 * {@link #BUTTON_STD_WIDTH}.
	 * 
	 * @param button
	 * @param data
	 */
	public static void computeButton(Button button, GridData data) {
		int width = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		if (width < BUTTON_STD_WIDTH) {
			width = BUTTON_STD_WIDTH;
		}
		data.minimumWidth = width;
		data.widthHint = width;
		button.setLayoutData(data);
	}

	/**
	 * Create and open an error {@link MessageBox} with the given parent {@code shell},
	 * {@code title} and {@code message}.
	 * 
	 * @param shell The parent shell of the message box.
	 * @param title The title of the message box or {@code null}.
	 * @param message The message of the message box.
	 */
	public static void errorMessageBox(Shell shell, String title, String message) {
		if (!shell.isDisposed()) {
			final MessageBox msgBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
			if (title != null) {
				msgBox.setText(title);
			}
			msgBox.setMessage(message);
			msgBox.open();
		} else {
			EclipseUtilActivator.log(message, IStatus.ERROR);
		}
	}

	/**
	 * Create and open an error {@link MessageBox} with the given parent {@code shell} and
	 * {@code message}.
	 * 
	 * @param shell The parent shell of the message box.
	 * @param message The message of the message box.
	 */
	public static void errorMessageBox(Shell shell, String message) {
		errorMessageBox(shell, null, message);
	}
}
