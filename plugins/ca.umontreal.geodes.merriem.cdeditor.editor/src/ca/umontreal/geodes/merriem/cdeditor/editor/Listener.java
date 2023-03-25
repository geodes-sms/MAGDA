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
import org.eclipse.emf.common.notify.Adapter;
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
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class Listener extends ResourceSetListenerImpl {

	public static boolean concepstJobLaunched = false;

	public static boolean AttributesJobLaunched = false;

	public static boolean AssociationJobLaunched = false;
	public static int attributeNumber = 0;
	public static int associationsNumber = 0;
	public static int inheritanceNumber = 0;
	public static int compositionNumber = 0;
	public static int classNumbers = 0;
	public static int RemoveClassNumbers = 0;

	public void resourceSetChanged(ResourceSetChangeEvent event) {

		// for loggig and launching jobs to predict new related elements.

		try {

			for (Iterator iter = event.getNotifications().iterator(); iter.hasNext();) {
				Notification object = (Notification) iter.next();

				if (object.getFeature() instanceof EReferenceImpl) {
					EReferenceImpl x = (EReferenceImpl) object.getFeature();

					// otherwise same event will be logged 6 times.

					// check if not already launched
					// eventType ==3 ; ADD

					if (x.getName().equals("clazz") && object.getEventType() == 3) {

						 EList<Clazz> classesInModel = Services.getModel().getClazz();
						
						for(int j = 0; j<classesInModel.size(); j++  ) {
							List<Adapter> adapters = classesInModel.get(j).eAdapters();
				
						   
						    boolean hasAdapter = false;
						    for (Adapter adapter : classesInModel.get(j).eAdapters()) {
						        if (adapter instanceof ElementNameChangeNotifier) {
						            hasAdapter = true;
						            break;
						        }
						    }

						    if (!hasAdapter) {
						    	
								classesInModel.get(j).eAdapters().add(new ElementNameChangeNotifier());
						    }
						}
						
						if (classNumbers == 0) {
							Services.loggerServices.info("Create class");

						} else if (classNumbers == 6) {
							classNumbers = 0;
						}
						classNumbers++;

//						if (Services.mode != Mode.assessAtEnd && Services.mode != null) {
//							Services services;
//
//							try {
//								services = new Services();
//								Session session = services.getSession();
//								Model model = services.getModel();
//								EList<Clazz> classes = model.getClazz();
//
//								/***
//								 * First Thread - Job : Predict related concepts Predict it's attributes ?
//								 **/
//
//								if (concepstJobLaunched == false) {
//
//									concepstJobLaunched = true;
//									JobConcepts jobConcepts = new JobConcepts("Concepts prediction", services, model,
//											session, true);
//
//									jobConcepts.setPriority(Job.SHORT);
//									jobConcepts.schedule();
//									
//									// concepstJobLaunched = false;
//								}
//
//								/***
//								 * Second Thread - Job : Predict related attributes for concepts added
//								 **/
//
//								if (AttributesJobLaunched == false) {
//									AttributesJobLaunched = true;
//									JobAttributes jobAttributes = new JobAttributes("Attributes prediction", services,
//											model);
//									// I changed the place of this to out of the Job
//
//									jobAttributes.setPriority(Job.SHORT);
//									jobAttributes.schedule();
//									// jobAttributes.cancel();
//								}
//
//								/***
//								 * Third Thread - Job : Predict related associations for concepts added
//								 **/
//
//								if (AssociationJobLaunched == false && model.getClazz().size() > 1) {
//									AssociationJobLaunched = true;
//									Job jobAssociations = new JobAssociations("Associations prediction", services,
//											model, session);
//									jobAssociations.setPriority(Job.SHORT);
//									jobAssociations.schedule();
//									// jobAssociations.cancel();
//								}
//
//							} catch (Exception e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//
//						}
					} else if (x.getName().equals("attributes") && object.getEventType() == 3) {

						if (attributeNumber == 0) {
							Services.loggerServices.info("Create attribute");
						} else if (attributeNumber == 6) {
							attributeNumber = 0;
						}
						attributeNumber++;
					} else if (x.getName().equals("generalizes") && object.getEventType() == 3) {

						if (inheritanceNumber == 0) {
							Services.loggerServices.info("Create inheritance ");
						} else if (inheritanceNumber == 6) {
							inheritanceNumber = 0;
						}
						inheritanceNumber++;
					} else if (x.getName().equals("isMember") && object.getEventType() == 3) {

						if (compositionNumber == 0) {
							Services.loggerServices.info("Create composition ");
						} else if (compositionNumber == 6) {
							compositionNumber = 0;
						}
						compositionNumber++;
					} else if (x.getName().equals("association") && object.getEventType() == 3) {

						if (associationsNumber == 0) {
							Services.loggerServices.info("Create  association");
						} else if (associationsNumber == 6) {
							associationsNumber = 0;
						}
						associationsNumber++;
					} else if (x.getName().equals("clazz") && object.getEventType() == 4) {

						if (RemoveClassNumbers == 0) {
							System.out.println("++++++++");
							if (((Clazz) object.getOldValue()).getName() != null) {
								System.out.println(((Clazz) object.getOldValue()).getName());
								Services.loggerServices
										.info("remove  class {" + ((Clazz) object.getOldValue()).getName() + "}");
							} else {
								System.out.println(((Clazz) object.getOldValue()).getName());
								Services.loggerServices.info("remove  class ");
							}

						} else if (RemoveClassNumbers == 6) {
							RemoveClassNumbers = 0;
						}
						RemoveClassNumbers++;

					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
