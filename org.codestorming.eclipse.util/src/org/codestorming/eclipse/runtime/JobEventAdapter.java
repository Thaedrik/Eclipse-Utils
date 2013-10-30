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
package org.codestorming.eclipse.runtime;

import org.eclipse.core.runtime.jobs.Job;

/**
 * Implementation of the {@link IJobEventListener} with empty methods.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @since 1.1
 */
public class JobEventAdapter implements IJobEventListener {

	@Override
	public void begin(Job job, String taskname, int totalWork) {}

	@Override
	public void worked(Job job, int work) {}

	@Override
	public void canceled(Job job) {}

	@Override
	public void subtask(Job job, String taskname) {}

	@Override
	public void subtaskDone(Job job, String taskname) {}

	@Override
	public void done(Job job) {}

}
