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

	public void resourceSetChanged(ResourceSetChangeEvent event) {
		// if a class is created:

		String id = "ca.umontreal.geodes.merriem.cdeditor.editor.view3";
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(id);

		for (Iterator iter = event.getNotifications().iterator(); iter.hasNext();) {
			Notification object = (Notification) iter.next();

			// System.out.println(object.getFeature());
			if (object.getFeature() instanceof EReferenceImpl) {
				EReferenceImpl x = (EReferenceImpl) object.getFeature();
				// check if not already launched
				
				// eventType ==3 ; ADD
				if (x.getName().equals("clazz") && concepstJobLaunched == false && object.getEventType()==3) {
					concepstJobLaunched = true;
					Services services;
				

					
					try {
						services = new Services();
						Session session = services.getSession();
						Model model = services.getModel();
						System.out.println("class is created or removed !!!! since it's ref impl !");
						Job job = new Job("My First Job") {

							protected IStatus run(IProgressMonitor monitor) {
								System.out.println("job will wait 15 s ");

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
									//dummy example 
									//conceptsFactory.createClassCondidate("from job suggestion","1", session, model);
									

									IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
									// root model is null
									List<HashMap<String, String>> Concepts = conceptsPrediction.run(null, model);

									for (String key : Concepts.get(0).keySet()) {
										// Results.add(key);
										if (!services.containsIgnoreCase(suggestedConcepts, key)) {
											conceptsFactory.createClassCondidate((String) key, Concepts.get(0).get(key),
													session, model);

										} else {

											conceptsFactory.updateConfidenceCondidate((String) key, session, model, 1);
										}

									}
									 
									Display.getDefault().syncExec(new Runnable() {
										public void run() {
											if (view instanceof ViewSuggestions) {
												ViewSuggestions viewSuggestions = (ViewSuggestions) view;
												{
													viewSuggestions.createContents();
												}
											}

										}
									});

								} catch (Exception e) {
									e.printStackTrace();
								}
								System.out.println("Hello World (from a background job)");
								// reset static value to false to enable jobs running.
								concepstJobLaunched = false;
								return Status.OK_STATUS;
							}
						};
						job.setPriority(Job.SHORT);
						job.schedule();
						break; // I added break because every event is considered twice !
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else if (x.getName().equals("attributes")) {
					System.out.println("attribute is created !");
					break;
				}

			}
			if (object instanceof Model) {
				System.out.println("it is a model");

			}

		}
	}

}
