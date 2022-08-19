package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.model.business.internal.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.internal.EditorReference;
import org.osgi.framework.ServiceException;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

/**
 * The services class used by VSM.
 */
public class Services {

	private Properties config;
	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";
	protected static final int Nan = 0;
	protected HashMap<String, HashMap<String, String>> classAttributes;

	public Services() throws Exception {
		this.config = new Properties();
		try {
			InputStream stream = Services.class.getClassLoader().getResourceAsStream("/config.properties");
			this.config.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	};

	//call threads  for caching begining
	public EObject setPredictionMode(EObject rootModel) throws InterruptedException {
		System.out.println("in set predictionMode ....");

		Model m = getModel();
		cachingThread T = new cachingThread(m); // creating thread
		T.start();
		T.join();
		this.classAttributes = T.classAttributes;
		System.out.println("hello");
		System.out.println(this.classAttributes);
		return rootModel; 
	}
	
	private void getCoorddinatesmodelObject(EObject modelObject) {
		NodeImpl node = (NodeImpl) modelObject;
		LayoutConstraint nodeLayoutConstraint = node.getLayoutConstraint();
		if (nodeLayoutConstraint instanceof Bounds) {
			Bounds bounds = (Bounds) nodeLayoutConstraint;
			int x = bounds.getX();
			int y = bounds.getY();
		}
	}

	private boolean containsIgnoreCase(List<String> list, String soughtFor) {

		for (String current : list) {
			if (current.replaceAll("\\s+", "").equalsIgnoreCase(soughtFor.replaceAll("\\s+", ""))) {
				return true;
			}
		}
		return false;
	}

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

	public void createClass(String Name, Session session) {
		try {
			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

					ClazzImpl newClazz = (ClazzImpl) metamodelFactory.createClazz();
					newClazz.setName(Name);
					model.getClazz().add(newClazz);

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

	public void deletetClassCondidate(String classToRemove, Session session) {
		try {
			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

					List<ClazzCondidate> classesCondidate = model.getClazzcondidate();
					int index = Nan;
					for (int i = 0; i < classesCondidate.size(); i++) {
						if (classesCondidate.get(i).getName().replaceAll("\\s+", "")
								.equals(classToRemove.replaceAll("\\s+", ""))) {
							index = i;
							break;
						}

					}
					model.getClazzcondidate().remove(index);

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

	public void createClassCondidate(String Name, Session session) {
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					ClazzCondidateImpl newClazzCondidate = (ClazzCondidateImpl) metamodelFactory.createClazzCondidate();
					newClazzCondidate.setName(Name);
					model.getClazzcondidate().add(newClazzCondidate);

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

	public void createAttribute(String Name, String Type, String containerName, Session session) {
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
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

	public void createAssociation(String AssociationName, String Type, String Target, String Source, Session session) {
		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
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
					System.out.print("type : ");

					System.out.println(Type);

					switch (Type) {
					case "inheritance":
						ClassSource.setSpecializes(ClassTarget);
						System.out.println("inheritance");

						break;
					case "composition":
						ClassSource.setIsMember(ClassTarget);
						System.out.println("composition");

						break;
					case "association":
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

		String NodeName = node.toString().split(":", 2)[1].replace(")", "");

		NodeName = NodeName.replaceAll("\\s+", "");
		System.out.print("PredictAttibutes for :  ");
		System.out.println(NodeName);
		String[] arrayAttributes  ; 
		HashMap<String, String> typeAttributes = new HashMap<String, String>();; 
		if (!classAttributes.containsKey(NodeName)) {
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
			arrayAttributes = Results.toArray(new String[0]);
			

			// print recieved attributes from python script
			for (int i = 0; i < arrayAttributes.length; i++) {
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
				typeAttributes.put(arrayAttributes[i], Type);

				// createAttribute(arrayAttributes[i], Type, NodeName, session);

			}
			
		} else {
			typeAttributes = classAttributes.get(NodeName); 
			arrayAttributes = typeAttributes.keySet().toArray(new String[typeAttributes.keySet().size()]);; 					
			
		}
		List<String> ResultsTyped = new ArrayList<String>();

		for (int i = 0; i < arrayAttributes.length; i++) {
			ResultsTyped.add(arrayAttributes[i].concat(":").concat(typeAttributes.get(arrayAttributes[i])));
		}

		String[] ArrayResultsTyped = ResultsTyped.toArray(new String[0]);

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(),
				new LabelProvider());

		dialog.setElements(ArrayResultsTyped);
		dialog.setTitle("select appropriate attributes, press ctrl for multiple selection");
		// user pressed cancel
		dialog.setMultipleSelection(true);

		if (dialog.open() != Window.OK) {
			// return false;
			System.out.println("not ok ");
		}
		Object[] result = dialog.getResult();
		for (int i = 0; i < result.length; i++) {
			String res = (String) result[i];
			res = res.split(":")[0];
			createAttribute(res, typeAttributes.get(res), NodeName, session);
		}

			return node;
	}

	public EObject getClassPrediction(EObject rootModel) {
		// Node theNode =
		// org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper.getGmfNode((DDiagramElement)
		// rootModel) ;
		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		List<String> classNames = new ArrayList<String>();
		List<String> AllclassNames = new ArrayList<String>();
		List<String> Concepts = new ArrayList<String>();
		Model model = getModel();
		List<Clazz> classesInModel = model.getClazz();

		List<ClazzCondidate> classeCondidateInModel = model.getClazzcondidate();

		for (int i = 0; i < classesInModel.size(); i++) {
			AllclassNames.add(classesInModel.get(i).getName());

		}
		for (int i = 0; i < classeCondidateInModel.size(); i++) {
			AllclassNames.add(classeCondidateInModel.get(i).getName());

		}
		String input = "";
		if (rootModel instanceof Model) {
			System.out.println("from one Canvas");

			for (int i = 0; i < classesInModel.size(); i++) {

				input = input.concat(",").concat(classesInModel.get(i).getName());
				classNames.add(classesInModel.get(i).getName());
			}
		} else if (rootModel instanceof Clazz) {
			System.out.println("from one class");
			Clazz inputClass = (Clazz) rootModel;
			input = inputClass.getName();
			classNames.add(inputClass.getName());
			// heuristic: what to send to GPT3
			Random rand = new Random();
			String randomElement = AllclassNames.get(rand.nextInt(AllclassNames.size()));
			input = input.concat(",").concat(randomElement);

		}

		Process p;

		for (int i = 0; i < AllclassNames.size(); i++) {
			System.out.println(AllclassNames.get(i));

		}
		String scriptLocation = this.config.getProperty("scriptlocation");
		String pythonCommand = this.config.getProperty("pythoncommand");
		if (input != "") {
			try {
				Process P = new ProcessBuilder(pythonCommand, scriptLocation + "predictConcepts.py", input).start();

				String line = "";
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));

				String s;
				while ((s = stdInput.readLine()) != null) {
					if (!AllclassNames.contains(s)) {
						Concepts.add(s);
					}
				}

				while ((s = stdError.readLine()) != null) { // add logger !
					System.out.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String[] arrayConcepts = Concepts.toArray(new String[0]);

			// create class condidate
			for (int i = 0; i < Concepts.size(); i++) {

				if (!containsIgnoreCase(AllclassNames, arrayConcepts[i].toLowerCase())) {
					System.out.println(arrayConcepts[i]);

					createClassCondidate(arrayConcepts[i], session);

				}
			}
		}
		// For prototype: window to select from

		// Create clazz (container) in editor if a concept is chosen.

		return null;
	}

	public EObject getAssociationPrediction(EObject rootModel) {

		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className;

		Model model = getModel();
		List<Clazz> classesInModel = model.getClazz();
		String scriptLocation = this.config.getProperty("scriptlocation");
		String pythonCommand = this.config.getProperty("pythoncommand");
		if (rootModel instanceof Clazz) {

			className = rootModel.toString().split(":")[1];

			className = className.substring(1, className.length() - 1);
			System.out.println(className);

			List<String> classesAssociatedTo = new ArrayList<String>();
			for (int i = 0; i < classesInModel.size(); i++) {
				if (classesInModel.get(i).getName().equals(className)) {
					System.out.println("found the class : ");

					for (int j = 0; j < classesInModel.get(i).getAssociatedTo().size(); j++) {
						System.out.println("is associated");

						System.out.println(classesInModel.get(i).getAssociatedTo().get(j).getName());
						classesAssociatedTo.add(classesInModel.get(i).getAssociatedTo().get(j).getName());
					}

					for (int j = 0; j < classesInModel.get(i).getAssociatedFrom().size(); j++) {
						System.out.println("is associatedfrom");

						classesAssociatedTo.add(classesInModel.get(i).getAssociatedFrom().get(j).getName());
					}
					if (classesInModel.get(i).getIsMember().getName() != null) {
						System.out.println("is memebr");
						System.out.println(classesInModel.get(i).getIsMember().getName());

						classesAssociatedTo.add(classesInModel.get(i).getIsMember().getName());
					}
					/*
					 * if (classesInModel.get(i).getSpecializes().getName() != null) {
					 * System.out.println("is special");
					 * 
					 * classesAssociatedTo.add(classesInModel.get(i).getSpecializes().getName());
					 * System.out.println(classesInModel.get(i).getSpecializes().getName()); }
					 */

				}
			}
			System.out.println("classesAssociatedTo");

			System.out.println(classesAssociatedTo);
			for (int i = 0; i < classesInModel.size(); i++) {
				if (!className.replaceAll("\\s+", "").equals(classesInModel.get(i).getName())) {

					if (!classesAssociatedTo.contains(classesInModel.get(i).getName().replaceAll("\\s+", ""))) {
						System.out.println("found possibility");

						System.out.println(classesInModel.get(i).getName());
						System.out.println(className);

						String input = classesInModel.get(i).getName().concat(" , ").concat(className);
						try {
							Process P = new ProcessBuilder(pythonCommand, scriptLocation + "predictAssociation.py",
									input).start();

							String line = "";
							BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
							BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));

							String s, res = null;
							while ((s = stdInput.readLine()) != null) {

								res = s;
								System.out.println(res);
							}

							while ((s = stdError.readLine()) != null) { // add logger !
								System.out.println(s);
							}
							if (res != null) {
								createAssociation(res, res.replaceAll("\\s+", ""), classesInModel.get(i).getName(),
										className, session);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}

		}

		return null;
	}

	public EObject approveClassCondidate(EObject rootModel) {
		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className = "";
		if (rootModel instanceof ClazzCondidate) {
			className = rootModel.toString().split(":", 0)[1].replaceAll(")", "");
			System.out.println(className);

		} else {

			className = rootModel.toString().split(" ", 2)[1];
			System.out.println(className);

		}
		if (className.contains(":")) {
			className = className.split(":", 0)[1].replaceAll(")", "");
		}
		createClass(className, session);
		deletetClassCondidate(className, session);

		return rootModel;
	}

}
