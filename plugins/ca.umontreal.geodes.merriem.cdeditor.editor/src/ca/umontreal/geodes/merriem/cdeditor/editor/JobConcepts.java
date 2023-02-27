package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobConcepts extends Job {

	private Services services;
	private Model model;
	private Session session;

	public JobConcepts(String name, Services services, Model model, Session session) {
		super(name);
		try {
			this.services = services;
			this.model = model;
			this.session = session;
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
			TimeUnit.SECONDS.sleep(13);

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
			for (int i = 0; i < model.getClazzcondidate().size(); i++) {
				suggestedConcepts.add(classeCondidateInModel.get(i).getName());
			}

			if (Services.relatedClasses == null) {
				Services.relatedClasses = new HashMap<String, List<String>>();
			}
			for (int i = 0; i < classes.size(); i++) {
				List<String> Results = new ArrayList<String>();
				if (!Services.relatedClasses.containsKey(classes.get(i).getName())) {
					IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
					// rootModel is null

					List<HashMap<String, String>> Concepts = conceptsPrediction.run(classes.get(i).getName(), null,
							model);
					for (String key : Concepts.get(0).keySet()) {
						if (!services.containsIgnoreCase(classNames, key)) {
							Results.add(key);

						}
					}

					Services.relatedClasses.put(classes.get(i).getName(), Results);
					for (String key : Concepts.get(0).keySet()) {

						if (!services.containsIgnoreCase(suggestedConcepts, key)) {

							conceptsFactory.createClassCondidate((String) key, Concepts.get(0).get(key), session,
									model);

							/**
							 * One strategy: predict attributes even for potential classes or Candidates and
							 * save them to hashmaps in session
							 **/

							/*
							 * HashMap<String, String> typeAttributes = new HashMap<String, String>();
							 * IAttributesPrediction attributesPredcition = new AttributesPrediction();
							 * 
							 * typeAttributes = attributesPredcition.run(null, (String) key, model);
							 * 
							 * // typeAttributes.put("id", "String");
							 * 
							 * if (services.classAttributes == null) { services.classAttributes = new
							 * HashMap<String, HashMap<String, String>>(); }
							 * 
							 * services.classAttributes.put((String) key, typeAttributes);
							 */

						} else {

							conceptsFactory.updateConfidenceCondidate((String) key, session, model, 1);
						}

					}
				}
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
		System.out.println("Hello World (from a background job concepts)");
		// reset static value to false to enable jobs running.

		return ASYNC_FINISH;

	}

}
