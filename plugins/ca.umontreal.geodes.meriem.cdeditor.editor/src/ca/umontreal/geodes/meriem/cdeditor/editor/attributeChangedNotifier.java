package ca.umontreal.geodes.meriem.cdeditor.editor;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl;

public class attributeChangedNotifier extends AdapterImpl {

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		Services services;
		try {
			services = new Services();

			if ((notification.getEventType() == Notification.SET)
					&& ((notification.getFeatureID(Attribute.class) == MetamodelPackageImpl.ATTRIBUTE__NAME)
							|| (notification.getFeatureID(Attribute.class) == MetamodelPackageImpl.ATTRIBUTE__TYPE))) {

				System.out.println("Attributes");
				Display.getDefault().syncExec(new Runnable() {
					public void run() {

						try {
							Services services = new Services();
							DAnalysis root = (DAnalysis) services.getSession().getSessionResource().getContents()
									.get(0);
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