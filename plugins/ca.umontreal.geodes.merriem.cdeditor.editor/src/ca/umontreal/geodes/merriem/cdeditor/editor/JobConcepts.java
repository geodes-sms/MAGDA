package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.PlatformUI;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobConcepts extends Job {

	private Services services;
	private Model model;
	private Session session;
	private Boolean wait;
	private ProgressBar progressBar;

	public JobConcepts(String name, Services services, Model model, Session session, Boolean wait,
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
			if (wait == true) {
				TimeUnit.SECONDS.sleep(15);
			}

			List<Clazz> classes = model.getClazz();

			ConceptsFactory conceptsFactory = new ConceptsFactory(services);
			List<String> classNames = new ArrayList<String>();

			List<String> suggestedConcepts = new ArrayList<String>();
			List<ClazzCandidate> classeCondidateInModel = model.getClazzcondidate();
			List<Clazz> classesInModel = model.getClazz();
			for (int i = 0; i < classesInModel.size(); i++) {
				if (classesInModel.get(i).getName() != null) {
					classNames.add(classesInModel.get(i).getName());
				}
			}
			for (int i = 0; i < classeCondidateInModel.size(); i++) {
				if (classeCondidateInModel.get(i).getName() != null) {
					suggestedConcepts.add(classeCondidateInModel.get(i).getName());
				}
			}

			if (Services.relatedClasses == null) {
				Services.relatedClasses = new HashMap<String, List<String>>();
			}
			for (int i = 0; i < classes.size(); i++) {
				List<String> Results = new ArrayList<String>();
				if (classes.get(i).getName() != null) {
					if (!Services.relatedClasses.containsKey(classes.get(i).getName().toLowerCase())) {
						IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
						// rootModel is null

						List<HashMap<String, String>> Concepts = conceptsPrediction.run(classes.get(i).getName(), null,
								model);
						if (Concepts != null) {

							for (String key : Concepts.get(0).keySet()) {
								if (!services.containsIgnoreCase(classNames, key)) {
									Results.add(key);

								}
							}

							Services.relatedClasses.put(classes.get(i).getName().toLowerCase(), Results);
							for (String key : Concepts.get(0).keySet()) {

								if (!services.containsIgnoreCase(suggestedConcepts, key)) {

									conceptsFactory.createClassCandidate((String) key.toLowerCase(),
											Concepts.get(0).get(key), session, model);
									suggestedConcepts.add(key.toLowerCase());

								} else {
									System.out.println("found it in candidates" + key);

									conceptsFactory.updateConfidenceCandidate((String) key.toLowerCase(), session,
											model, 1);
								}

							}

						}
					}
				}
			}
			EList<ClazzCandidate> Candidates = model.getClazzcondidate();

			for (int k = 0; k < Candidates.size(); k++) {
				/**
				 * One strategy: predict attributes even for potential classes or Candidates and
				 * save them to hashmaps in session, only consider the candidates that have a
				 * confidence value superior than 1
				 **/
				if (Services.classAttributes == null) {
					Services.classAttributes = new HashMap<String, HashMap<String, String>>();
				}
				if (Candidates.get(k).getConfidence() > 1) {
					if (Services.classAttributes != null && (!Services.classAttributes
							.containsKey(Candidates.get(k).getName().replaceAll("\\s+", "").toLowerCase()))) {

						System.out.println("predicting attributes for potentail class , with good confidence : "
								+ Candidates.get(k).getName());

						HashMap<String, String> typeAttributes = new HashMap<String, String>();

//						typeAttributes = attributesPredcition.run(null, (String) Candidates.get(k).getName(), model,
//								false);

						if (services.classAttributes == null) {
							services.classAttributes = new HashMap<String, HashMap<String, String>>();
						}

						services.classAttributes.put((String) Candidates.get(k).getName().toLowerCase(),
								typeAttributes);
					}
				}
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				Services.refreshSuggestionsView();

			}
		});
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		this.cancel();
		return ASYNC_FINISH;

	}

}
