package ca.umontreal.geodes.meriem.cdeditor.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.runtime.jobs.Job;

public class JobWithProgressBar extends Job {

	private ProgressBar progressBar;

	public JobWithProgressBar(String name, ProgressBar progressBar) {
		super(name);
		this.progressBar = progressBar;
	}

	protected IStatus run(IProgressMonitor monitor) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (progressBar != null && !progressBar.isDisposed()) {
					progressBar.setMaximum(100);
					progressBar.setMinimum(0);
				}
			}
		});

		// Your job's code here
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			final int progress = i;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (progressBar != null && !progressBar.isDisposed()) {
						progressBar.setSelection(progress);
					}
				}
			});
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (progressBar != null && !progressBar.isDisposed()) {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Job Completed", "The job with progress bar has completed.");
				}
			}
		});

		return Status.OK_STATUS;
	}
}
