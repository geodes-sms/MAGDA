package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.NamedElementImpl;

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

				System.out.print("Element name changed from " + oldName + " to " + newName);
				Services.loggerServices.info("update class name");
				if (Services.mode != Mode.assessAtEnd && Services.mode != null) {
					/***
					 * First Thread - Job : Predict related concepts Predict it's attributes ?
					 **/

					JobConcepts jobConcepts = new JobConcepts("Concepts prediction", services, model, session, false);

					jobConcepts.setPriority(Job.SHORT);
					jobConcepts.schedule();


					/***
					 * Second Thread - Job : Predict related attributes for concepts added
					 **/

					JobAttributes jobAttributes = new JobAttributes("Attributes prediction", services, model);
					// I changed the place of this to out of the Job

					jobAttributes.setPriority(Job.SHORT);
					jobAttributes.schedule();

					/***
					 * Third Thread - Job : Predict related associations for concepts added
					 **/

					if (model.getClazz().size() > 1) {

						JobAssociations jobAssociations = new JobAssociations("Associations prediction", services,
								model, session);
						jobAssociations.setPriority(Job.SHORT);
						jobAssociations.schedule();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}