package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.model.business.internal.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorReference;
import org.osgi.framework.ServiceException;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;

/**
 * The services class used by VSM.
 */
public class Services {

	private Properties config;
	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";

	public Services() {
		this.config = new Properties();
		try {
			InputStream stream = Services.class.getClassLoader().getResourceAsStream("/config.properties");
			this.config.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	protected Model getModel() {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();

		IEditorReference editor = null;

		for (IEditorReference iEditorReference : editorReferences) {
			EditorReference editRef = (EditorReference) iEditorReference;
			if (editRef.getDescriptor().getId().equalsIgnoreCase(SIRIUS_DIAGRAM_EDITOR_ID)) {
				editor = editRef;
				break;
			}
		}

		if (editor == null) {
			System.out.println("No process found.");
			return null;
		}

		EObject input = null;

		try {
			IEditorInput editorInput = editor.getEditorInput();
			if (editorInput instanceof SessionEditorInput) {
				input = ((SessionEditorInput) editorInput).getInput();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		EObject model = null;

		if (input instanceof Diagram) {
			EObject element = ((Diagram) input).getElement();
			if (element instanceof DSemanticDiagramSpec) {
				model = ((DSemanticDiagramSpec) element).getTarget();
			}
		}

		return (Model) model;
	}

	
	/**
	 * Comment from Istvan:
	 * Use Session session = SessionManager.INSTANCE.getSession(node) in the {@link #getAttributePrediction(EObject)} method to
	 * get a reference to the singleton Session object from the node, and then pass that session object to this method as a parameter.
	 * This will allow you to get rid of the hard-coded aird file location.
	 */
	public void createAttribute(String Name, String Type, String containerName) {
		try {
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
					Model model = getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					AttributeImpl newAttribute = (AttributeImpl) metamodelFactory.createAttribute();
					newAttribute.setName(Name);
					newAttribute.setType(Type);
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

	/**
	 * Comment from Istvan:
	 * Similarly to the other comment above, pass a session object to this method as a parameter
	 * to eliminate the hard-coded aird file location.
	 */
	public void createAssociation(String AssociationName, String Type, String Target, String Source) {
		try {
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
					Model model = getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					List<Clazz> classes = new ArrayList<Clazz>();
					classes = model.getClazz();
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
					case "simple":
						ClassSource.getAssociatedFrom().add(ClassTarget);
						ClassTarget.getAssociatedTo().add(ClassSource);
						break;
					default:
						// code block
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

	public EObject getAttributePrediction(EObject node) {
		
		Session session = SessionManager.INSTANCE.getSession(node);
		assert session != null;
		
		String NodeName = node.toString().split(" ", 2)[1];
		NodeName = NodeName.replaceAll("\\s+", "");
		System.out.print("DOuble clicked , predictAttibutes for :  ");
		System.out.println(NodeName);
		List<String> attributes = new ArrayList<String>();
		for (int i = 1; i < node.eContents().size(); i++) {
			attributes.add(node.eContents().get(i).toString().split(" ", 3)[2].split(":", 3)[0]);
		}
		String input;
		if (node.eContents().size() > 1) {
			input = attributes.get(0);
			for (int i = 1; i < attributes.size(); i++) {
				input = input.concat(",").concat(attributes.get(i));
			}
		} else
			input = "";
		List<String> Results = new ArrayList<String>();

		String scriptLocation = this.config.getProperty("scriptlocation");
		String pythonCommand = this.config.getProperty("pythoncommand");

		try {
			Process P1 = new ProcessBuilder(pythonCommand, scriptLocation + "predictAttributes.py", NodeName, input,
					"Attribute").start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(P1.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(P1.getErrorStream()));

			/*
			 * Runtime runtime = Runtime.getRuntime(); Process process =
			 * runtime.exec("python 3");
			 */

			String s;

			while ((s = stdInput.readLine()) != null) {

				Results.add(s);
			}

			while ((s = stdError.readLine()) != null) {
				// add logger !
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] arrayAttributes = Results.toArray(new String[0]);

		// print recieved attributes from python script
		for (int i = 0; i < arrayAttributes.length; i++) {
			System.out.println(arrayAttributes[i]);
			String Type = "";

			try {
				Process P2 = new ProcessBuilder(pythonCommand, scriptLocation + "predictAttributes.py",
						arrayAttributes[i], input, "Type").start();
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(P2.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(P2.getErrorStream()));
				String s;
				while ((s = stdInput.readLine()) != null) {

					Type = s;
				}
				while ((s = stdError.readLine()) != null) {
					// add logger !
					System.out.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			createAttribute(arrayAttributes[i], Type, NodeName);

		}

		return node;
	}

	/**
	 * Comment from Istvan:
	 * Here, the assumption was that the class prediction menu is shown when clicked on the canvas. This may or may not be the case.
	 * Change the code accordingly. Acquire a reference to the session object as shown above.
	 */
	public EObject getClassPrediction(EObject rootModel) {
		assert rootModel instanceof Model;
		Model model = (Model) rootModel;

		// TODO: Meriem, try to solve the class generation here. "Model model" is a
		// reference to the working model.

		return null;
	}

}
