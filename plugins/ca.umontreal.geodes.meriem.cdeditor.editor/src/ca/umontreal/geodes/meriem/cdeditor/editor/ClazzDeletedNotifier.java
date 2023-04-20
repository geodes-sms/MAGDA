package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Association;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl;

public class ClazzDeletedNotifier extends AdapterImpl {

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		Services services;
		try {

			if ((notification.getEventType() == Notification.REMOVE)) {

				if (notification.getFeature() instanceof EReferenceImpl) {
					if (((EReferenceImpl) notification.getFeature()).getName() == "clazz") {

						services = new Services();
						Session session = services.getSession();
						Model model = services.getModel();
						EList<Clazz> classes = model.getClazz();

						String oldName = (String) ((Clazz) notification.getOldValue()).getName();
						AssociationsFactory.cleanOperationCandidate(session);

						Services.loggerServices.info("remove  class {" + oldName + "}");
						if (Services.relatedAssociations != null && oldName != null && oldName.toLowerCase() != "") {
							if (Services.relatedAssociations.containsKey(oldName.toLowerCase())) {
								Services.relatedAssociations.remove(oldName.toLowerCase());
							}
						}
						if ((Services.relatedClasses != null) && oldName != null
								&& (Services.relatedClasses.containsKey(oldName.toLowerCase()))) {
							for (int j = 0; j < Services.relatedClasses.get(oldName.toLowerCase()).size(); j++) {

								ConceptsFactory.deleteClassCandidate(
										Services.relatedClasses.get(oldName.toLowerCase()).get(j), session);

							}
						}

						AssociationsFactory.cleanOperations(session);
						Services.refreshAssociationsView();
						Services.refreshSuggestionsView();
					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
