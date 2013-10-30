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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.codestorming.eclipse.util.EclipseUtilActivator;
import org.codestorming.util.collection.Arrays2;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * Implementation of the abstract {@link ProgressProvider}, usable in a custom Eclipse RCP
 * application.
 * <p>
 * The {@code SimpleProgressProvider} should be set in the RCP application's
 * {@link IJobManager}.
 * <p>
 * Then, add new {@link IJobEventListener IJobEventListeners} to the
 * {@code SimpleProgressProvider} instance to listen to jobs events.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @since 1.1
 * @see IJobEventListener
 */
public class SimpleProgressProvider extends ProgressProvider {

	private static SimpleProgressProvider instance;

	protected final Map<Job, Set<IJobEventListener>> jobListeners = Collections
			.synchronizedMap(new HashMap<Job, Set<IJobEventListener>>());

	protected final Set<IJobEventListener> listeners = Collections.synchronizedSet(new HashSet<IJobEventListener>());

	/**
	 * Creates a new {@code SimpleProgressProvider}.
	 */
	private SimpleProgressProvider() {}

	/**
	 * Returns the singleton {@link SimpleProgressProvider} instance.
	 * 
	 * @return the singleton {@link SimpleProgressProvider} instance.
	 */
	public static SimpleProgressProvider getInstance() {
		if (instance == null) {
			instance = new SimpleProgressProvider();
		}
		return instance;
	}

	@Override
	public IProgressMonitor createMonitor(Job job) {
		Assert.isNotNull(job);
		return new ProgressMonitor(job, this);
	}

	/**
	 * Add to this {@link ProgressProvider} a {@link IJobEventListener} for listening to
	 * all job events.
	 * 
	 * @param listener The {@link IJobEventListener} to add.
	 */
	public void addJobListener(IJobEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Add to this {@link ProgressProvider} a {@link IJobEventListener} for listening to
	 * the given job's events.
	 * 
	 * @param job
	 * @param listener The {@link IJobEventListener} to add.
	 */
	public void addJobListener(Job job, IJobEventListener listener) {
		synchronized (jobListeners) {
			Set<IJobEventListener> listeners = jobListeners.get(job);
			if (listeners == null) {
				listeners = new HashSet<IJobEventListener>();
				jobListeners.put(job, listeners);
			}
			listeners.add(listener);
		}
	}

	/**
	 * Remove the given {@link IJobEventListener} from this {@link ProgressProvider}.
	 * <p>
	 * Remove the listener only if it has been registered with
	 * {@link #addJobListener(IJobEventListener)}.
	 * 
	 * @param listener The {@link IJobEventListener} to remove.
	 */
	public void removeJobListener(IJobEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Remove the given {@link IJobEventListener} from this {@link ProgressProvider}.
	 * <p>
	 * Remove the listener only if it has been registered with
	 * {@link #addJobListener(Job, IJobEventListener)}.
	 * 
	 * @param job
	 * @param listener The {@link IJobEventListener} to remove.
	 */
	public void removeJobListener(Job job, IJobEventListener listener) {
		synchronized (jobListeners) {
			final Set<IJobEventListener> listeners = jobListeners.get(job);
			if (listeners != null) {
				listeners.remove(listener);
			}
		}
	}

	protected void begin(Job job, String taskname, int totalWork) {
		for (IJobEventListener listener : getListeners(job)) {
			listener.begin(job, taskname, totalWork);
		}
	}

	protected void worked(Job job, int work) {
		for (IJobEventListener listener : getListeners(job)) {
			listener.worked(job, work);
		}
	}

	protected void canceled(Job job) {
		for (IJobEventListener listener : getListeners(job)) {
			listener.canceled(job);
		}
	}

	protected void subtask(Job job, String taskname) {
		for (IJobEventListener listener : getListeners(job)) {
			listener.subtask(job, taskname);
		}
	}

	protected void subtaskDone(Job job, String taskname) {
		for (IJobEventListener listener : getListeners(job)) {
			listener.subtaskDone(job, taskname);
		}
	}

	protected void done(Job job) {
		for (IJobEventListener listener : getListeners(job)) {
			listener.done(job);
		}
	}

	/**
	 * Returns the {@link IJobEventListener listeners} of the specified job.
	 * 
	 * @param job
	 * @return the {@link IJobEventListener listeners} of the specified job.
	 */
	protected IJobEventListener[] getListeners(Job job) {
		IJobEventListener[] l = listeners.toArray(new IJobEventListener[listeners.size()]);
		synchronized (jobListeners) {
			Set<IJobEventListener> jobListeners = this.jobListeners.get(job);
			if (jobListeners != null && jobListeners.size() > 0) {
				IJobEventListener[] l1 = jobListeners.toArray(new IJobEventListener[jobListeners.size()]);
				l = Arrays2.insert(l, l.length, l1, 0, l1.length);
			}
		}
		return l;
	}

	static class ProgressMonitor implements IProgressMonitor {

		/**
		 * Monitored job.
		 */
		final Job job;

		/**
		 * This monitor progress provider.
		 */
		final SimpleProgressProvider progressProvider;

		/**
		 * Operation cancel flag.
		 */
		private boolean canceled;

		/**
		 * Work done.
		 */
		// private int worked;

		/**
		 * Current running task of the job.
		 */
		final Stack<String> currentTask = new Stack<String>();

		/**
		 * Creates a new {@code ProgressMonitor}.
		 * 
		 * @param job
		 * @param progressProvider
		 */
		public ProgressMonitor(Job job, SimpleProgressProvider progressProvider) {
			this.job = job;
			this.progressProvider = progressProvider;
		}

		@Override
		public void beginTask(String name, int totalWork) {
			currentTask.push(name);
			progressProvider.begin(job, name, totalWork);
		}

		@Override
		public void done() {
			currentTask.clear();
			progressProvider.done(job);
		}

		@Override
		public void internalWorked(double work) {}

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean value) {
			canceled = value;
			if (canceled) {
				progressProvider.canceled(job);
			}
		}

		@Override
		public void setTaskName(String name) {
			if (!currentTask.isEmpty()) {
				final String taskname = currentTask.pop();
				if (!currentTask.isEmpty()) {
					final String task = currentTask.peek();
					if (!task.equals(name)) {
						EclipseUtilActivator.log("Expected task " + name + " but was " + task, IStatus.WARNING);
					}
				}
				progressProvider.subtaskDone(job, taskname);
			}
		}

		@Override
		public void subTask(String name) {
			currentTask.push(name);
			progressProvider.subtask(job, name);
		}

		@Override
		public void worked(int work) {
			// worked += work;
			progressProvider.worked(job, work);
		}
	}
}
