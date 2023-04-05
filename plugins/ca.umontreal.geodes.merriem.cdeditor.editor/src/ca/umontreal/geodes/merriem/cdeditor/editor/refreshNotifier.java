package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.NullProgressMonitor;
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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class refreshNotifier extends AdapterImpl {

	public static boolean lock = false;

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		Services services;
		try {
			System.out.println(notification.getEventType());
			if ((notification.getEventType() == Notification.ADD) && this.lock== false ) {
			
//				Display.getDefault().syncExec(new Runnable() {
//					public void run() {
//						System.out.println("refresh ? ");
//						refreshNotifier.lock=true; 				
////						try {
////							refreshNotifier.lock=true; 
////							TimeUnit.MILLISECONDS.sleep(500);
////							refreshNotifier.lock=false; 
////						} catch (InterruptedException e1) {
////							// TODO Auto-generated catch block
////							e1.printStackTrace();
////							
////						}
//						System.out.println("refresh done ");
//						refreshNotifier.lock=false; 
//						try {
//						Services services = new Services();
//						DAnalysis root = (DAnalysis) services.getSession().getSessionResource().getContents().get(0);
//						DView dView = root.getOwnedViews().get(0);
//						DRepresentation represnt = null;
//						for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
//							represnt = descrp.getRepresentation();
//							DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
//
//
//						}
//
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//					}
//				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
