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
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class Listener extends ResourceSetListenerImpl {

	public static boolean concepstJobLaunched = false;

	public static boolean AttributesJobLaunched = false;

	public static boolean AssociationJobLaunched = false;

	public void resourceSetChanged(ResourceSetChangeEvent event) {

		if (Services.mode != Mode.assessAtEnd && Services.mode != null) {
			// if a class is created:

			try {

				for (Iterator iter = event.getNotifications().iterator(); iter.hasNext();) {
					Notification object = (Notification) iter.next();

					if (object.getFeature() instanceof EReferenceImpl) {
						EReferenceImpl x = (EReferenceImpl) object.getFeature();
						// check if not already launched

						// eventType ==3 ; ADD
						if (x.getName().equals("clazz") && object.getEventType() == 3) {

							Services services;

							try {
								services = new Services();
								Session session = services.getSession();
								Model model = services.getModel();
								EList<Clazz> classes = model.getClazz();

								/***
								 * First Thread - Job : Predict related concepts Predict it's attributes ?
								 **/

								if (concepstJobLaunched == false) {
									
									concepstJobLaunched = true;
									JobConcepts jobConcepts = new JobConcepts("Concepts prediction", services, model,
											session,true);
									
									jobConcepts.setPriority(Job.SHORT);
									jobConcepts.schedule();
									//jobConcepts.cancel();
									//concepstJobLaunched = false;
								}

								/***
								 * Second Thread - Job : Predict related attributes for concepts added
								 **/

								if (AttributesJobLaunched == false) {
									AttributesJobLaunched = true;
									JobAttributes jobAttributes = new JobAttributes("Attributes prediction", services,
											model);
									// I changed the place of this to out of the Job
									
									jobAttributes.setPriority(Job.SHORT);
									jobAttributes.schedule();
									//jobAttributes.cancel();
								}

								/***
								 * Third Thread - Job : Predict related associations for concepts added
								 **/

								if (AssociationJobLaunched == false) {
									AssociationJobLaunched = true;
									Job jobAssociations = new JobAssociations("Associations prediction", services,
											model, session);
									jobAssociations.setPriority(Job.SHORT);
									jobAssociations.schedule();
									// jobAssociations.cancel();
								}

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					}

				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
