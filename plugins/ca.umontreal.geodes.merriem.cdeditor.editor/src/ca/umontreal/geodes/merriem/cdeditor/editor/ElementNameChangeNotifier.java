package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.NamedElementImpl;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;

public class ElementNameChangeNotifier extends AdapterImpl {

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		Services services;
		try {
			services = new Services();

			Session session = services.getSession();
			Model model = services.getModel();
			EList<Clazz> classes = model.getClazz();
			if ((notification.getEventType() == Notification.SET)
					&& notification.getFeatureID(Clazz.class) == MetamodelPackageImpl.CLAZZ__NAME) {

				Clazz element = (Clazz) notification.getNotifier();
				String oldName = (String) notification.getOldValue();
				String newName = (String) notification.getNewValue();

				System.out.println("Element name changed from " + oldName + " to " + newName);
				Services.loggerServices.info("update class name");
				if (Services.mode != Mode.assessAtEnd && Services.mode != null) {
					/***
					 * First Thread - Job : Predict related concepts Predict it's attributes ?
					 **/
					
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							System.out.println("refresh ? ");
							refreshNotifier.lock=true; 				
//							try {
//								refreshNotifier.lock=true; 
//								TimeUnit.MILLISECONDS.sleep(500);
//								refreshNotifier.lock=false; 
//							} catch (InterruptedException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//								
//							}
							System.out.println("refresh done ");
							refreshNotifier.lock=false; 
							try {
							Services services = new Services();
							DAnalysis root = (DAnalysis) services.getSession().getSessionResource().getContents().get(0);
							DView dView = root.getOwnedViews().get(0);
							DRepresentation represnt = null;
							for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
								represnt = descrp.getRepresentation();
								DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());


							}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
					
					ProgressBar progressBar = ((ViewSuggestions) Services.suggestionView).getProgressBar();
					//JobConceptsDummy jobConcepts = new JobConceptsDummy("Concepts prediction", services, model, session, false,progressBar);
					
					JobConcepts jobConcepts = new JobConcepts("Concepts prediction", services, model, session, false,
							progressBar);
					jobConcepts.setPriority(Job.SHORT);
					jobConcepts.schedule();

					/***
					 * Second Thread - Job : Predict related attributes for concepts added
					 **/

//					JobAttributes jobAttributes = new JobAttributes("Attributes prediction", services, model);
//					jobAttributes.setPriority(Job.SHORT);
//					jobAttributes.schedule();

					/***
					 * Third Thread - Job : Predict related associations for concepts added
					 **/

					if (model.getClazz().size() > 1) {
						ProgressBar progressBarAssociations = ((ViewAssociations) Services.associationView).getProgressBar();
						JobAssociations jobAssociations = new JobAssociations("Associations prediction", services,
								model, session,progressBarAssociations);
//						JobAssociationsDummy jobAssociations = new JobAssociationsDummy("Associations prediction", services,
//								model, session,progressBarAssociations);
						
						jobAssociations.setPriority(Job.SHORT);
						jobAssociations.schedule();
					}
				}
			}
			else if ((notification.getEventType() == Notification.SET)
					&& (notification.getFeatureID(Clazz.class) == MetamodelPackageImpl.CLAZZ__HAS )|| (notification.getFeatureID(Clazz.class) == MetamodelPackageImpl.CLAZZ__IS_MEMBER)) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						System.out.println("composition refresh  ? ");
						refreshNotifier.lock=true; 				
//						try {
//							refreshNotifier.lock=true; 
//							TimeUnit.MILLISECONDS.sleep(500);
//							refreshNotifier.lock=false; 
//						} catch (InterruptedException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//							
//						}
						System.out.println("refresh done ");
						refreshNotifier.lock=false; 
						try {
						Services services = new Services();
						DAnalysis root = (DAnalysis) services.getSession().getSessionResource().getContents().get(0);
						DView dView = root.getOwnedViews().get(0);
						DRepresentation represnt = null;
						for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
							represnt = descrp.getRepresentation();
							DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());


						}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}