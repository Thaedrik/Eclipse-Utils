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
 * Notified when a job state changes.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @since 1.1
 */
public interface IJobEventListener {

	/**
	 * Notify this listener, the given job is beginning.
	 * 
	 * @param job The beginning job.
	 * @param taskname The name of the task.
	 * @param totalWork The total work of the job.
	 */
	public void begin(Job job, String taskname, int totalWork);

	/**
	 * Notify this listener a certain amount of work have been done on the given job.
	 * 
	 * @param job The job for which some work has been done.
	 * @param work The amount of work done.
	 */
	public void worked(Job job, int work);

	/**
	 * Notify this listener the given job has been canceled.
	 * 
	 * @param job The canceled job.
	 */
	public void canceled(Job job);

	/**
	 * Notify this listener the given sub-task {@code taskname} has begun.
	 * 
	 * @param job The job for which a sub-task began.
	 * @param taskname The name of the sub-task.
	 */
	public void subtask(Job job, String taskname);

	/**
	 * Notify this listener the given sub-task {@code taskname} is done.
	 * 
	 * @param job The job for which a sub-task is done.
	 * @param taskname The name of the sub-task.
	 */
	public void subtaskDone(Job job, String taskname);

	/**
	 * Notify this listener the given job is done.
	 * 
	 * @param job The done job.
	 */
	public void done(Job job);
}
