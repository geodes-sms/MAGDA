package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.Transaction;
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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl;
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

	public void updateConfidenceCandidate(String key, Session session, Model model, int value) {
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(dView);

			CommandStack stack = editingDomain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(editingDomain) {

				@Override
				protected void doExecute() {

					List<ClazzCandidate> listSuggestions = model.getClazzcondidate();
					for (int j = 0; j < listSuggestions.size(); j++) {
						if (listSuggestions.get(j).getName().replaceAll("\\s+", "")
								.equalsIgnoreCase(key.replaceAll("\\s+", ""))) {
							int previousConfidence = listSuggestions.get(j).getConfidence();
							listSuggestions.get(j).setConfidence(previousConfidence + value);
							// break;
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

	public void createClass(String name, Session session, Boolean refreshFlag) {
		try {
			if (name != null) {
				DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
				DView dView = root.getOwnedViews().get(0);

				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(dView);

				CommandStack stack = editingDomain.getCommandStack();

				RecordingCommand cmd = new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						Model model = services.getModel();
						MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

						ClazzImpl newClazz = (ClazzImpl) metamodelFactory.createClazz();
						String processedName = name.substring(0, 1).toUpperCase() + name.substring(1);
						newClazz.setName(processedName);
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
				RecordingCommand cmd2 = new RecordingCommand(editingDomain) {

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
				// stack.execute(cmd2);

				// SessionManager.INSTANCE.notifyRepresentationCreated(session);

			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	public void createClassCandidate(String name, String confidence, Session session, Model model) {
		if (!name.equals("")) {
			try {

				EList<Clazz> classesINCanvas = model.getClazz();
				List<String> namesOfClassesInCanvas = new ArrayList<String>();
				for (int j = 0; j < classesINCanvas.size(); j++) {
					namesOfClassesInCanvas.add(classesINCanvas.get(j).getName());
				}
				if (!services.containsIgnoreCase(namesOfClassesInCanvas, name)) {
					DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
					DView dView = root.getOwnedViews().get(0);

					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(dView);

					CommandStack stack = editingDomain.getCommandStack();

					RecordingCommand cmd = new RecordingCommand(editingDomain) {

						@Override
						protected void doExecute() {

							MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

							ClazzCandidateImpl newClazzCandidate = (ClazzCandidateImpl) metamodelFactory
									.createClazzCandidate();
							newClazzCandidate.setName(name);

							newClazzCandidate.setConfidence(Integer.parseInt(confidence));

							model.getClazzcondidate().add(newClazzCandidate);

						}

					};

					stack.execute(cmd);
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteClassCandidate(String classToRemove, Session session) {

		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(dView);
			CommandStack stack = editingDomain.getCommandStack();

			stack.execute(new AbstractCommand() {

				@Override
				public boolean canExecute() {
					// Return true if the command can be executed, false otherwise.
					return true;
				}

				@Override
				public void execute() {
					Model model = Services.getModel();
					List<ClazzCandidate> classesCondidate = model.getClazzcondidate();
					int index = 0;
					for (int i = 0; i < classesCondidate.size(); i++) {
						if (classesCondidate.get(i).getName().replaceAll("\\s+", "")
								.equals(classToRemove.replaceAll("\\s+", ""))) {
							index = i;
						}
					}
					System.out.println("removing class candidate..");
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

				@Override
				public void redo() {
					// This method is not needed in this case, since we are not using undo/redo.
				}

				@Override
				public void undo() {
					// This method is not needed in this case, since we are not using undo/redo.
				}

				@Override
				public boolean canUndo() {
					// This method is not needed in this case, since we are not using undo/redo.
					return false;
				}
			});

		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

}
