package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Association;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.OperationCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.OperationCondidateImpl;

public class AssociationsFactory {

	private Services services;

	public AssociationsFactory() {
		try {
			this.services = new Services();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createAssociation(String type, String Name, String Target, String Source, Session session) {
		System.out.println("creating association  " + type + " " + Name + " from " + Source + " To " + Target);
		try {
			String Type=type.replaceAll("\\s+", " ").toLowerCase();
			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {

					Model model = services.getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					List<Clazz> classes = new ArrayList<Clazz>();
					List<Association> Associations = new ArrayList<Association>();
					classes = model.getClazz();
					Associations = model.getAssociation();

					// find both classes target and source:
					Clazz ClassSource = null;
					Clazz ClassTarget = null;
					for (int i = 0; i < classes.size(); i++) {

						if (classes.get(i).getName().replaceAll("\\s+", "").equals(Source.replaceAll("\\s+", ""))) {
							ClassSource = classes.get(i);
						}
						if (classes.get(i).getName().replaceAll("\\s+", "").equals(Target.replaceAll("\\s+", ""))) {
							ClassTarget = classes.get(i);
						}
					}

					switch (Type) {
					case "inheritance":
						ClassSource.setSpecializes(ClassTarget);

						break;
					case "composition":
						ClassSource.setIsMember(ClassTarget);

						break;
					case "association":

						if (Name.equals("is")) {
							ClassSource.setSpecializes(ClassTarget);

						} else {
							AssociationImpl newAssociation = (AssociationImpl) metamodelFactory.createAssociation();
							newAssociation.setName(Name);
							newAssociation.setTarget(ClassTarget);
							newAssociation.setSource(ClassSource);
							Associations.add(newAssociation);
						}

						break;
					default:
						break;

					}
					// refresh Model
					DRepresentation represnt = null;
					for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
						represnt = descrp.getRepresentation();
					}
					DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());

				}
			};
			stack.execute(cmd);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void createAssociationCondidate(String Type, String Name, String Target, String Source, Session session) {

		System.out.println("creating association condidates " + Type + " " + Name + " from " + Source + " To " + Target);
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {

					Model model = services.getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					List<Clazz> classes = new ArrayList<Clazz>();
					List<OperationCondidate> Associations = new ArrayList<OperationCondidate>();
					classes = model.getClazz();
					Associations = model.getOperation();

					// find both classes target and source:
					Clazz ClassSource = null;
					Clazz ClassTarget = null;
					for (int i = 0; i < classes.size(); i++) {

						if (classes.get(i).getName().replaceAll("\\s+", "").equals(Source.replaceAll("\\s+", ""))) {
							ClassSource = classes.get(i);
						}
						if (classes.get(i).getName().replaceAll("\\s+", "").equals(Target.replaceAll("\\s+", ""))) {
							ClassTarget = classes.get(i);
						}
					}
					OperationCondidateImpl newAssociationCondidate = (OperationCondidateImpl) metamodelFactory
							.createOperationCondidate();

					if (Name.equals("is")) {
						ClassSource.setSpecializes(ClassTarget);

					} else {
						newAssociationCondidate.setName(Name);
						newAssociationCondidate.setTarget(ClassTarget);
						newAssociationCondidate.setSource(ClassSource);
						newAssociationCondidate.setType(Type);
						Associations.add(newAssociationCondidate);
					}

					// refresh Model
					DRepresentation represnt = null;
					for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
						represnt = descrp.getRepresentation();
					}
					DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());

				}
			};
			stack.execute(cmd);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void removeCondidate(String type, String name, String target, String source, Session session) {

			try {
				DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
				DView dView = root.getOwnedViews().get(0);

				TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

				CommandStack stack = domain.getCommandStack();
				RecordingCommand cmd = new RecordingCommand(domain) {

					@Override
					protected void doExecute() {
						Model model = services.getModel();
						// MetamodelFactory metamodelFactory =
						// ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

						List<OperationCondidate> operationCondidates = model.getOperation();
						int index = 0;
						for (int i = 0; i < operationCondidates.size(); i++) {
							if (operationCondidates.get(i).getName().replaceAll("\\s+", "")
									.equals(name.replaceAll("\\s+", "")) &&(operationCondidates.get(i).getTarget().getName().replaceAll("\\s+", "")
									.equals(target.replaceAll("\\s+", "")))&& (operationCondidates.get(i).getSource().getName().replaceAll("\\s+", "")
								
									.equals(source.replaceAll("\\s+", ""))))  {
								System.out.println("found the operation" );
								index = i;

								break;
							}
						}
						model.getOperation().remove(index);
						SessionManager.INSTANCE.notifyRepresentationCreated(session);

					}

				};

				/*RecordingCommand cmd2 = new RecordingCommand(session.getTransactionalEditingDomain()) {
					@Override
					protected void doExecute() {

						DRepresentation represnt = null;
						for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
							represnt = descrp.getRepresentation();
							DialectEditor editor = (DialectEditor) org.eclipse.sirius.ui.business.api.dialect.DialectUIManager.INSTANCE
									.openEditor(session, represnt, new NullProgressMonitor());

							DialectUIManager.INSTANCE.refreshEditor(editor, new NullProgressMonitor());
						}

						// DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
					}
				};*/
				stack.execute(cmd);
				//stack.execute(cmd2);

			} catch (ServiceException e) {
				e.printStackTrace();
			}

			// return removedClazz;
		
		
	}

}
