package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobConceptsDummy extends Job {

	private Services services;
	private Model model;
	private Session session;
	private Boolean wait;
	private ProgressBar progressBar;

	public JobConceptsDummy(String name, Services services, Model model, Session session, Boolean wait,
			ProgressBar progressBar) {
		super(name);
		this.progressBar = progressBar;

		try {
			this.services = services;
			this.model = model;
			this.session = session;
			this.wait = wait;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (progressBar != null && !progressBar.isDisposed()) {
					progressBar.setMaximum(100);
					progressBar.setMinimum(0);
				}
			}
		});
		// wait some time, to give the user add concept name.
		try {
			
				TimeUnit.SECONDS.sleep(3);
			
			ConceptsFactory conceptsFactory = new ConceptsFactory(services);
			List<HashMap<String, String>> Concepts ; 
			conceptsFactory.createClassCandidate("classA", "2", session, model);
			conceptsFactory.createClassCandidate("classB", "1", session, model);
			conceptsFactory.createClassCandidate("classC", "1", session, model);
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
					Services.refreshSuggestionsView();

				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.cancel();
		return ASYNC_FINISH;
	}

}
