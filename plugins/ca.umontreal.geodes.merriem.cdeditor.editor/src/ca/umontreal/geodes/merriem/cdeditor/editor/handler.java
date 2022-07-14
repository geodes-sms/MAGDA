package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.impl.DDiagramImpl;
import org.eclipse.sirius.diagram.model.business.internal.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.EditorReference;
import org.osgi.framework.ServiceException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelFactoryImpl;

public class handler extends AbstractHandler {

	private Services services;

	/**
	 * The constructor.
	 */
	public handler() {
		this.services = new Services();
	}

	/**
	 * the command has been executed, so extract extract the needed information from
	 * the application context.
	 */

	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";

	public void createInstance(String instanceType, String Name, String containerName) {
		try {
			// URI sessionResourceURI =
			// URI.createFileURI("/home/meriem/editorCD/class-diagram-editor/plugins/ca.umontreal.geodes.meriem.cdeditor.metamodel/model/metamodel.aird");
			URI sessionResourceURI = URI
					.createFileURI("/home/meriem//editorCD/class-diagram-editor/testFolder/representations.aird");

			Session createdSession = SessionManager.INSTANCE.getSession(sessionResourceURI, new NullProgressMonitor());
			createdSession.open(new NullProgressMonitor());

			DAnalysis root = (DAnalysis) createdSession.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = services.getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

					switch (instanceType) {
					case "class":
						ClazzImpl newClazz = (ClazzImpl) metamodelFactory.createClazz();
						newClazz.setName(Name);
						model.getClazz().add(newClazz);

						break;
					case "attribute":
						AttributeImpl newAttribute = (AttributeImpl) metamodelFactory.createAttribute();
						newAttribute.setName(Name);
						List<Clazz> classes = model.getClazz();
						for (int i = 0; i < classes.size(); i++) {

							String Cname = classes.get(i).getName().replaceAll("\\s+", "");

							if (containerName.equals(Cname)) {
								EList<Attribute> attributesName = model.getClazz().get(i).getAttributes();
								boolean attributeExist = false;
								for (int j = 0; j < attributesName.size(); j++) {

									if (attributesName.get(j).getName().replaceAll("\\s+", "")
											.equals(Name.replaceAll("\\s+", ""))) {
										attributeExist = true;
										System.out.println("found attribute skip !");
										System.out.println(Name);
										break;
									}
								}
								if (!attributeExist) {

									model.getClazz().get(i).getAttributes().add(newAttribute);

									break;
								}
							}
						}

						break;
					default:

					}

					// model.eContents().add(newClazz);

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

	public Object execute(ExecutionEvent event) throws ExecutionException {

		Model m = services.getModel();
		List<Clazz> classes = new ArrayList<Clazz>();
		classes = m.getClazz();
		List<String> classNames=new ArrayList<String>();
		String input = "";
		for (int i = 0; i < classes.size(); i++) {
			input = input.concat(",").concat(classes.get(i).getName());
			classNames.add(classes.get(i).getName());
		}
		System.out.println(input); 
		List<String> Concepts = new ArrayList<String>();
		Concepts.add("Cancel");

		Process p;
		try {

			Process P = new ProcessBuilder("python3",
					"/home/meriem/editorCD/class-diagram-editor/scripts/predictConcepts.py", input).start();

			String line = "";
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));

			String s;
			while ((s = stdInput.readLine()) != null) {
				if(! classNames.contains(s)) {
					Concepts.add(s);
				}
				
			}

			while ((s = stdError.readLine()) != null) {
				// add logger !
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] arrayConcepts = Concepts.toArray(new String[0]);

		// print recieved concepts from python script
		for (int i = 0; i < Concepts.size(); i++) {
			System.out.println(arrayConcepts[i]);
		}

		// For prototype: window to select from
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		MessageDialog dialog = new MessageDialog(window.getShell(), "Choose relevant class", null, "Choose relevant class",
				MessageDialog.QUESTION, arrayConcepts, 0);
		int result = dialog.open();
		System.out.println("chosen");
		String inputSelected = arrayConcepts[result];
		System.out.println(inputSelected);

		// Create clazz (container) in editor if a concept is chosen.
		if (arrayConcepts[result] != "Cancel") {
			System.out.println(input);
			createInstance("class", inputSelected, null);
		}

		return null;
	}

}