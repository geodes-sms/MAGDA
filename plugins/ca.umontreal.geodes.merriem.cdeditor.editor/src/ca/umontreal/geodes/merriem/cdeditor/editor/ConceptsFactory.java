package ca.umontreal.geodes.merriem.cdeditor.editor;

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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

public class ConceptsFactory {
	private Services services;

	public ConceptsFactory() {
		try {
			this.services = new Services();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ConceptsFactory(Services services) {
		try {
			this.services = services;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateConfidenceCondidate(String key, Session session, Model model, int value) {
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = services.getModel();
					List<ClazzCondidate> listSuggestions = model.getClazzcondidate();
					for (int j = 0; j < listSuggestions.size(); j++) {
						if (listSuggestions.get(j).getName().replaceAll("\\s+", "")
								.equalsIgnoreCase(key.replaceAll("\\s+", ""))) {
							int previousConfidence = listSuggestions.get(j).getConfidence();
							listSuggestions.get(j).setConfidence(previousConfidence + value);
							break;
						}
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

	public void createClass(String Name, Session session, Boolean refreshFlag) {
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			// domain = session.getTransactionalEditingDomain();

			CommandStack stack = domain.getCommandStack();
			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = services.getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

					ClazzImpl newClazz = (ClazzImpl) metamodelFactory.createClazz();
					newClazz.setName(Name);
					model.getClazz().add(newClazz);

					SessionManager.INSTANCE.notifyRepresentationCreated(session);
					// refresh Model
					DRepresentation represnt = null;
					for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
						represnt = descrp.getRepresentation();

					}
					DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
				}

			};
			RecordingCommand cmd2 = new RecordingCommand(session.getTransactionalEditingDomain()) {
				@Override
				protected void doExecute() {

					DRepresentation represnt = null;
					for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
						represnt = descrp.getRepresentation();
						DialectEditor editor = (DialectEditor) org.eclipse.sirius.ui.business.api.dialect.DialectUIManager.INSTANCE
								.openEditor(session, represnt, new NullProgressMonitor());

						/**
						 * this suggestions to canvas
						 **/
						if (refreshFlag) {
							DialectUIManager.INSTANCE.refreshEditor(editor, new NullProgressMonitor());

						}
					}
					/**
					 * this list to canvas
					 **/

					if (!refreshFlag) {
						DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
					}
				}
			};
			stack.execute(cmd);
			stack.execute(cmd2);

			// SessionManager.INSTANCE.notifyRepresentationCreated(session);

		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	public void createClassCondidate(String Name, String confidence, Session session, Model model) {

		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {

					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					ClazzCondidateImpl newClazzCondidate = (ClazzCondidateImpl) metamodelFactory.createClazzCondidate();
					newClazzCondidate.setName(Name);

					newClazzCondidate.setConfidence(Integer.parseInt(confidence));

					model.getClazzcondidate().add(newClazzCondidate);

					// refresh Model
					/*
					 * DRepresentation represnt = null; for (DRepresentationDescriptor descrp :
					 * dView.getOwnedRepresentationDescriptors()) { represnt =
					 * descrp.getRepresentation();
					 * 
					 * } DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
					 */
				}

			};

			stack.execute(cmd);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	public void deletetClassCondidate(String classToRemove, Session session) {

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

					List<ClazzCondidate> classesCondidate = model.getClazzcondidate();
					int index = 0;
					for (int i = 0; i < classesCondidate.size(); i++) {
						if (classesCondidate.get(i).getName().replaceAll("\\s+", "")
								.equals(classToRemove.replaceAll("\\s+", ""))) {
							index = i;

							break;
						}
					}
					model.getClazzcondidate().remove(index);
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

			RecordingCommand cmd2 = new RecordingCommand(session.getTransactionalEditingDomain()) {
				@Override
				protected void doExecute() {

					DRepresentation represnt = null;
					for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
						represnt = descrp.getRepresentation();
						DialectEditor editor = (DialectEditor) org.eclipse.sirius.ui.business.api.dialect.DialectUIManager.INSTANCE
								.openEditor(session, represnt, new NullProgressMonitor());

						// DialectUIManager.INSTANCE.refreshEditor(editor, new NullProgressMonitor());
					}

					DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
				}
			};
			stack.execute(cmd);
			// stack.execute(cmd2);

		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

}
