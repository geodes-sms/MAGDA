package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobAssociationsDummy extends Job {

	private Services services;
	private Model model;
	private ProgressBar progressBar;
	private Session session;

	public JobAssociationsDummy(String name, Services services, Model model, Session session, ProgressBar progressBar) {
		super(name);
		try {
			this.services = services;
			this.model = model;
			this.session = session;
			this.progressBar = progressBar;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-ge nerated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (progressBar != null && !progressBar.isDisposed()) {
					progressBar.setMaximum(100);
					progressBar.setMinimum(0);
				}
			}
		});

		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(2);
			AssociationsFactory associationsFactory = new AssociationsFactory(services);
			associationsFactory.createAssociationcandidate("Association", "name", "classA", "classB",  session, model);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// reset static value to false to enable jobs running.
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(15);
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
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Services.refreshAssociationsView();

			}
		});
		this.cancel();
		return ASYNC_FINISH;
	}

}
