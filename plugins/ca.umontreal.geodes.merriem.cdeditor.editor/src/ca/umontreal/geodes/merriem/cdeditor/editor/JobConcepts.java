package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobConcepts extends Job {

	private Services services;
	private Model model;
	private Session session;
	private Boolean wait; 

	public JobConcepts(String name, Services services, Model model, Session session, Boolean wait) {
		super(name);
		try {
			this.services = services;
			this.model = model;
			this.session = session;
			this.wait=wait;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		// wait some time, to give the user add concept name.
		try {
			if (wait == true) {
				TimeUnit.SECONDS.sleep(13);
			}

			System.out.println("job started ");
			List<Clazz> classes = model.getClazz();

			ConceptsFactory conceptsFactory = new ConceptsFactory(services);
			List<String> classNames = new ArrayList<String>();

			List<String> suggestedConcepts = new ArrayList<String>();
			List<ClazzCandidate> classeCondidateInModel = model.getClazzcondidate();
			List<Clazz> classesInModel = model.getClazz();
			for (int i = 0; i < classesInModel.size(); i++) {

				classNames.add(classesInModel.get(i).getName());
			}
			for (int i = 0; i < classeCondidateInModel.size(); i++) {
				suggestedConcepts.add(classeCondidateInModel.get(i).getName());
			}

			if (Services.relatedClasses == null) {
				Services.relatedClasses = new HashMap<String, List<String>>();
			}
			for (int i = 0; i < classes.size(); i++) {
				List<String> Results = new ArrayList<String>();
				if (!Services.relatedClasses.containsKey(classes.get(i).getName().toLowerCase())) {
					IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
					// rootModel is null

					List<HashMap<String, String>> Concepts = conceptsPrediction.run(classes.get(i).getName(), null,
							model);
					for (String key : Concepts.get(0).keySet()) {
						if (!services.containsIgnoreCase(classNames, key)) {
							Results.add(key);

						}
					}

					Services.relatedClasses.put(classes.get(i).getName().toLowerCase(), Results);
					for (String key : Concepts.get(0).keySet()) {

						if (!services.containsIgnoreCase(suggestedConcepts, key)) {

							conceptsFactory.createClassCandidate((String) key.toLowerCase(), Concepts.get(0).get(key),
									session, model);
							suggestedConcepts.add(key.toLowerCase());

						} else {
							System.out.println("found it in candidates" + key);

							conceptsFactory.updateConfidenceCandidate((String) key.toLowerCase(), session, model, 1);
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
						/*
						 * IAttributesPrediction attributesPredcition = new AttributesPrediction();
						 * 
						 * typeAttributes = attributesPredcition.run(null, (String)
						 * Candidates.get(k).getName(), model, false);
						 */
						typeAttributes.put("id; dummy ", "String");

						if (services.classAttributes == null) {
							services.classAttributes = new HashMap<String, HashMap<String, String>>();
						}

						services.classAttributes.put((String) Candidates.get(k).getName().toLowerCase(),
								typeAttributes);
					}
				}
			}

			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					Services.refreshSuggestionsView();

				}
			});
		} catch (

		InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hello World (from a background job concepts)");
		System.out.println("Hello World (from a background job concepts)");

		Listener.concepstJobLaunched = false;

		// reset static value to false to enable jobs running.

		return ASYNC_FINISH;

	}

}
