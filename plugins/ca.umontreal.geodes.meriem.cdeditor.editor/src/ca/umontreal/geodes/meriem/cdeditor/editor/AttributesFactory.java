package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.osgi.framework.ServiceException;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;

public class AttributesFactory {
	private Services services;

	public AttributesFactory() {
		try {
			this.services = new Services();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createAttribute(String Name, String Type, String containerName, Session session, Boolean refreshFlag) {
		try {
			
			
			
			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(dView);

			CommandStack stack = editingDomain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(editingDomain) {

				@Override
				protected void doExecute() {
					Model model = services.getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					AttributeImpl newAttribute = (AttributeImpl) metamodelFactory.createAttribute();
					newAttribute.setName(Name);
					newAttribute.setType(Type);
					List<Clazz> classes = model.getClazz();
					for (int i = 0; i < classes.size(); i++) {
						if (classes.get(i).getName() != null) {
							String className = classes.get(i).getName().replaceAll("\\s+", "");
							if (containerName.equalsIgnoreCase(className)) {
								EList<Attribute> attributesName = model.getClazz().get(i).getAttributes();
								boolean attributeExist = false;
								for (int j = 0; j < attributesName.size(); j++) {
									if (attributesName.get(j).getName()!=null) {
										if (attributesName.get(j).getName().replaceAll("\\s+", "")
												.equalsIgnoreCase(Name.replaceAll("\\s+", ""))) {
											attributeExist = true;
											break;
										}
									}
								}
								if (!attributeExist) {
									model.getClazz().get(i).getAttributes().add(newAttribute);
									break;
								}
							}
						}
					}
					// refresh Model
					SessionManager.INSTANCE.notifyRepresentationCreated(session);
					DRepresentation represnt = null;

					for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
						represnt = descrp.getRepresentation();
						DialectEditor editor = (DialectEditor) org.eclipse.sirius.ui.business.api.dialect.DialectUIManager.INSTANCE
								.openEditor(session, represnt, new NullProgressMonitor());
						/// DialectUIManager.INSTANCE.refreshEditor(editor, new NullProgressMonitor());

					}
					DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
				}
			};
			stack.execute(cmd);

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
