package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class Listener extends ResourceSetListenerImpl {

	public static boolean concepstJobLaunched = false;
	public static boolean attributestJobLaunched = false;

	public void resourceSetChanged(ResourceSetChangeEvent event) {
		// if a class is created:

		String id = "ca.umontreal.geodes.merriem.cdeditor.editor.view3";
		try {
			// IViewPart view =
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(id);

			for (Iterator iter = event.getNotifications().iterator(); iter.hasNext();) {
				Notification object = (Notification) iter.next();

				// System.out.println(object.getFeature());
				if (object.getFeature() instanceof EReferenceImpl) {
					EReferenceImpl x = (EReferenceImpl) object.getFeature();
					// check if not already launched

					// eventType ==3 ; ADD
					if (x.getName().equals("clazz") && concepstJobLaunched == false && object.getEventType() == 3) {
						concepstJobLaunched = true;
						Services services;
						AssociationsFactory associationsFactory = new AssociationsFactory();

						try {
							services = new Services();
							Session session = services.getSession();
							Model model = services.getModel();
							EList<Clazz> classes = model.getClazz();

							/***
							 * First Thread - Job : Predict related concepts Predict it's attributes ?
							 **/

							Job job = new Job("concepts and attributes prediction") {

								protected IStatus run(IProgressMonitor monitor) {
									System.out.println("job will wait 10 s ");

									try {
										// wait some time, to give the user add concept name.
										TimeUnit.SECONDS.sleep(10);
										System.out.println("job started ");

										ConceptsFactory conceptsFactory = new ConceptsFactory(services);

										List<String> suggestedConcepts = new ArrayList<String>();
										List<ClazzCondidate> classeCondidateInModel = model.getClazzcondidate();

										for (int i = 0; i < model.getClazzcondidate().size(); i++) {
											suggestedConcepts.add(classeCondidateInModel.get(i).getName());
										}
										// dummy example
										// conceptsFactory.createClassCondidate("jobSuggestion", "1", session, model);

										IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
										// rootModel is null

										List<HashMap<String, String>> Concepts = conceptsPrediction.run(null, model);

										for (String key : Concepts.get(0).keySet()) {

											if (!services.containsIgnoreCase(suggestedConcepts, key)) {

												conceptsFactory.createClassCondidate((String) key,
														Concepts.get(0).get(key), session, model);

												/**
												 * One strategy: predict attributes even for potential classes or
												 * Candidates and save them to hashmaps in session
												 **/

												/*
												 * HashMap<String, String> typeAttributes = new HashMap<String,
												 * String>(); IAttributesPrediction attributesPredcition = new
												 * AttributesPrediction();
												 * 
												 * typeAttributes = attributesPredcition.run(null, (String) key, model);
												 * 
												 * // typeAttributes.put("id", "String");
												 * 
												 * if (services.classAttributes == null) { services.classAttributes =
												 * new HashMap<String, HashMap<String, String>>(); }
												 * 
												 * services.classAttributes.put((String) key, typeAttributes);
												 */

											} else {

												conceptsFactory.updateConfidenceCondidate((String) key, session, model,
														1);
											}

										}

										Display.getDefault().syncExec(new Runnable() {
											public void run() {
												Services.refreshSuggestionsView();

											}
										});

									} catch (Exception e) {
										e.printStackTrace();
									}
									System.out.println("Hello World (from a background job)");
									// reset static value to false to enable jobs running.

									return ASYNC_FINISH;
								}
							};
							job.setPriority(Job.SHORT);
							job.schedule();
							/***
							 * Second Thread - Job : Predict related attributes for concepts added ?
							 **/
							Job jobAttributes = new Job("Attributes prediction") {

								protected IStatus run(IProgressMonitor monitor) {

									System.out.println("Predict attributes for added class");
									try {
										TimeUnit.SECONDS.sleep(7);
										HashMap<String, String> typeAttributes = new HashMap<String, String>();
										IAttributesPrediction attributesPredcition = new AttributesPrediction();
										if (Services.classAttributes == null) {
											Services.classAttributes = new HashMap<String, HashMap<String, String>>();
										}
										for (int i = 0; i < classes.size(); i++) {
											if (!Services.classAttributes.containsKey(classes.get(i).getName())) {
												typeAttributes = attributesPredcition.run(null,
														classes.get(i).getName(), model);
												Services.classAttributes.put(classes.get(i).getName(), typeAttributes);
											}
										}
										System.out.println("attributes  started ");

									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return ASYNC_FINISH;
								}
							};
							jobAttributes.setPriority(Job.SHORT);
							jobAttributes.schedule();

							Job jobAssociations = new Job("Associations prediction") {

								@Override
								protected IStatus run(IProgressMonitor monitor) {
									// TODO Auto-generated method stub
									try {

										TimeUnit.SECONDS.sleep(10);
										System.out.println("Association prediction launched");
										IAssociationsPrediction associationsPrediction = new AssociationsPrediction();

										for (int i = 0; i < classes.size(); i++) {
											String className = classes.get(i).getName();
											if (Services.relatedAssociations == null) {
												Services.relatedAssociations = new HashMap<String, List<String>>();
											}
											if (!Services.relatedAssociations.containsKey(classes.get(i).getName())) {

												List<HashMap<String, String>> res = associationsPrediction
														.run(className, null, model);

												List<String> items = new ArrayList<String>();
												String item;
												for (int j = 0; j < res.size(); j++) {
													if (!(res.get(j).get("Type").replaceAll("\\s+", "").equals(""))&&(!(res.get(j).get("Type").replaceAll("\\s+", "").equals("no")))) {

														item = res.get(j).get("Type") + ":[" + res.get(j).get("Source")
																+ "," + res.get(j).get("Target") + "]; Name => "
																+ res.get(j).get("Name");
														items.add(item);

														String[] ArrayResultsTyped = items.toArray(new String[0]);
														associationsFactory.createAssociationCondidate(
																res.get(j).get("Type"), res.get(j).get("Name"),
																res.get(j).get("Target"), res.get(j).get("Source"),
																session);

														Services.relatedAssociations.put(className, items);

														Display.getDefault().syncExec(new Runnable() {
															public void run() {
																Services.refreshSuggestionsView();

															}
														});
													}
												}
											}
										}
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									concepstJobLaunched = false;

									return ASYNC_FINISH;
								}

							};
							jobAssociations.setPriority(Job.SHORT);
							jobAssociations.schedule();
							System.out.println("Association prediction finished");
							jobAssociations.cancel();

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}

			}
		} catch (

		Exception e1) {
			e1.printStackTrace();
		}
	}

}
