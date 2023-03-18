package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.business.api.refresh.CanonicalSynchronizer;
import org.eclipse.sirius.diagram.business.api.refresh.CanonicalSynchronizerFactory;
import org.eclipse.sirius.diagram.model.business.internal.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusLayoutDataManager;
import org.eclipse.sirius.diagram.ui.business.internal.view.RootLayoutData;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.internal.EditorReference;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The services class used by VSM. Ps need to check flag Mode if it is set or
 * not, It shouldn't be set in constructor because Services constructor is
 * called multiple times => use singleton
 */
public class Services {

	private Properties config;
	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";
	protected static final int Nan = 0;
	protected static HashMap<String, HashMap<String, String>> classAttributes;
	protected static HashMap<String, List<String>> relatedClasses;
	protected static HashMap<String, List<String>> relatedAssociations;
	protected ResourceSetListener listener;
	public static IViewPart suggestionView;
	public static IViewPart associationView;
	public static Mode mode;
	public static Logger loggerServices;

	public Services() throws Exception {
		this.config = new Properties();
		this.listener = (ResourceSetListener) new Listener();
		// PropertyConfigurator.configure("log4j.properties");

		try {
			InputStream stream = Services.class.getClassLoader().getResourceAsStream("/config.properties");
			this.config.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Services.suggestionView == null) {
			Services.suggestionView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView("ca.umontreal.geodes.merriem.cdeditor.editor.view3");

		}
		if (Services.associationView == null) {
			Services.associationView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView("ca.umontreal.geodes.merriem.cdeditor.editor.viewAssociations");
		}

		TransactionalEditingDomain domain = getSession().getTransactionalEditingDomain();

		domain.addResourceSetListener(listener);

		if (Services.loggerServices == null) {
			System.out.println("instanctiate");

			loggerServices = Logger.getLogger(Services.class.getName());
			loggerServices.setUseParentHandlers(false);
			try {

				FileHandler fileHandler = new FileHandler("/home/meriem/editorCD/class-diagram-editor" + "/logg.txt",
						true);
				fileHandler.setFormatter(new TextFormatter());
				loggerServices.setLevel(Level.INFO);
				loggerServices.addHandler(fileHandler);
				loggerServices.info("Application started : ");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Log the start of the application

	};

	public static String getCurrentTime() {
		// Return the current time in the specified format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static void refreshSuggestionsView() {
		// String id = "ca.umontreal.geodes.merriem.cdeditor.editor.view3";

		if (Services.suggestionView instanceof ViewSuggestions) {

			ViewSuggestions viewSuggestions = (ViewSuggestions) Services.suggestionView;
			viewSuggestions.createContents();
		}
	}

	public static void refreshAssociationsView() {

		if (Services.associationView instanceof ViewAssociations) {

			ViewAssociations viewAssociations = (ViewAssociations) Services.associationView;
			viewAssociations.createContents();

		}
	}

	private IGraphicalEditPart getEditPart(DDiagramElement diagramElement) {
		IEditorPart editor = EclipseUIUtil.getActiveEditor();
		if (editor instanceof DiagramEditor) {
			Session session = new EObjectQuery(diagramElement).getSession();
			View gmfView = SiriusGMFHelper.getGmfView(diagramElement, session);

			IGraphicalEditPart result = null;
			if (gmfView != null && editor instanceof DiagramEditor) {
				final Map<?, ?> editPartRegistry = ((DiagramEditor) editor).getDiagramGraphicalViewer()
						.getEditPartRegistry();
				final Object editPart = editPartRegistry.get(gmfView);
				if (editPart instanceof IGraphicalEditPart) {
					result = (IGraphicalEditPart) editPart;
					return result;
				}
			}
		}
		return null;
	}

	private void setGraphicalHintsFromExistingNode(DDiagramElement existingNode) {
		// Give hints about location and size
		IGraphicalEditPart editPart = getEditPart(existingNode);
		if (editPart instanceof ShapeEditPart) {
			ShapeEditPart part = (ShapeEditPart) editPart;
			SiriusLayoutDataManager.INSTANCE
					.addData(new RootLayoutData(existingNode.eContainer(), part.getLocation(), part.getSize()));
		}
	}

	private void setGraphicalHintsNEAR_ExistingNode(DDiagramElement existingNode, int x) {
		// Give hints about location and size
		IGraphicalEditPart editPart = getEditPart(existingNode);
		if (editPart instanceof ShapeEditPart) {
			ShapeEditPart part = (ShapeEditPart) editPart;
			Point p = part.getLocation().getTranslated(x, 1);

			SiriusLayoutDataManager.INSTANCE.addData(new RootLayoutData(existingNode.eContainer(), p, part.getSize()));
		}
	}

	public void positionRelativeToCurrent(EObject currentNode, EObject newNode) {
		if (currentNode instanceof DNode && newNode instanceof DNode) {
			DNode currentDNode = (DNode) currentNode;
			DNode newDNode = (DNode) newNode;
			// Retrieve GMF nodes associated with our graphical nodes
			Node firstNode = SiriusGMFHelper.getGmfNode(currentDNode);
			Bounds firstBounds = (Bounds) firstNode.getLayoutConstraint();
			// Launch refresh so that GMF view corresponding to the newDNode are created
			CanonicalSynchronizer canonicalSynchronizer = CanonicalSynchronizerFactory.INSTANCE
					.createCanonicalSynchronizer(firstNode.getDiagram());
			canonicalSynchronizer.synchronize();
			Node secondNode = SiriusGMFHelper.getGmfNode(newDNode);
			Bounds secondBounds = (Bounds) secondNode.getLayoutConstraint();
			// Make sure that the created view will never be arranged
			Set<View> createdViewsToArrange = SiriusLayoutDataManager.INSTANCE.getCreatedViewWithCenterLayout()
					.get(secondNode.getDiagram());
			if (createdViewsToArrange != null) {
				createdViewsToArrange.remove(secondNode);
			}
			// Set bounds for this new node
			secondBounds.setX(firstBounds.getX() + 150);
			secondBounds.setY(firstBounds.getY());
		}
	}
	// }

	@SuppressWarnings("restriction")
	public boolean containsIgnoreCase(List<String> list, String soughtFor) {

		for (String current : list) {
			if (current.replaceAll("\\s+", "").equalsIgnoreCase(soughtFor.replaceAll("\\s+", ""))) {

				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("restriction")
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

	public EObject getAttributePrediction(EObject node) {

		Services.loggerServices.info("Demand Attributes Predcion ");
		AttributesFactory attributesFactory = new AttributesFactory();
		Session session = SessionManager.INSTANCE.getSession(node);
		assert session != null;
		List<String> types = new ArrayList<>(List.of("int", "string", "float", "char", "boolean", "double", "byte",
				"array", "object", "collection", "date"));
		String NodeName = node.toString().split(":", 2)[1].replace(")", "");
		NodeName = NodeName.replaceAll("\\s+", "");
		List<String> ResultsTyped = new ArrayList<String>();

		HashMap<String, String> typeAttributes = null;
		if (Services.classAttributes == null) {
			Services.classAttributes = new HashMap<String, HashMap<String, String>>();
		}
		if (Services.classAttributes.containsKey(NodeName.toLowerCase())) {
			if (!Services.classAttributes.get(NodeName.toLowerCase()).isEmpty()) {
				// && (this.classAttributes.get(NodeName).size() > 1)) {

				typeAttributes = Services.classAttributes.get(NodeName.toLowerCase());
			}
		} else {
			System.out.println("running attributes prediction ");
			IAttributesPrediction attributesPredcition = new AttributesPrediction();
			typeAttributes = attributesPredcition.run(node, NodeName, getModel(), false);
			
			Services.classAttributes.put(NodeName.toLowerCase(), typeAttributes);
			// dummy example remove this
			// typeAttributes =new HashMap<String, String>();
		}
		if (typeAttributes == null) {
			System.out.println("type attribute is null");
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
					"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
			Services.classAttributes.remove(NodeName.toLowerCase());
			int result = dialog.open();
		} else {
			for (Map.Entry<String, String> set : typeAttributes.entrySet()) {
				if ((set.getKey().replaceAll(" ", "").equalsIgnoreCase(""))
						|| (set.getValue().replaceAll(" ", "").equalsIgnoreCase(""))) {
					typeAttributes.remove(set.getKey());
				} else if (!(set.getKey().replaceAll(" ", "").equalsIgnoreCase(""))
						&& !(set.getValue().replaceAll(" ", "").equalsIgnoreCase(""))) {

					if (containsIgnoreCase(types, set.getValue())) {

						ResultsTyped.add(set.getKey().concat(":").concat(set.getValue()));
					}
				}
			}

			String[] ArrayResultsTyped = ResultsTyped.toArray(new String[0]);

			if (ArrayResultsTyped.length > 0) {

				ElementListSelectionDialog dialog = new ElementListSelectionDialog(
						Display.getCurrent().getActiveShell(), new LabelProvider());

				// dialog.setAllowDuplicates(false);

				dialog.setElements(ArrayResultsTyped);

				dialog.setTitle("Select appropriate attributes for class " + NodeName);

				dialog.setMultipleSelection(true);

				if (dialog.open() != Window.OK) {
					// return false;
				}

				Object[] result = dialog.getResult();
				if (result != null) {
					// Services.loggerServices.info("Accept fromm Attributs list : " + result.length
					// );
					for (int i = 0; i < result.length; i++) {
						String res = (String) result[i];
						res = res.split(":")[0];
						attributesFactory.createAttribute(res, typeAttributes.get(res), NodeName, session, false);
						typeAttributes.remove(res);
					}
					Services.classAttributes.put(NodeName.toLowerCase(), typeAttributes);

				} // else {
				/**
				 * If cancel is pressed, trigger attributes prediction again. uncomment this for
				 * better performance
				 */

				/*
				 * Model model = getModel();
				 * 
				 * Job jobAttributes = new Job("Attributes prediction") {
				 * 
				 * protected IStatus run(IProgressMonitor monitor) { String NodeName =
				 * node.toString().split(":", 2)[1].replace(")", ""); NodeName =
				 * NodeName.replaceAll("\\s+", ""); System.out.
				 * println("Not satisfie with attributes, Predict other list of  attributes");
				 * HashMap<String, String> typeAttributes = new HashMap<String, String>();
				 * IAttributesPrediction attributesPredcition = new AttributesPrediction(); if
				 * (Services.classAttributes == null) { Services.classAttributes = new
				 * HashMap<String, HashMap<String, String>>(); }
				 * 
				 * typeAttributes = attributesPredcition.run(node, NodeName, model);
				 * Services.classAttributes.put(NodeName, typeAttributes);
				 * 
				 * System.out.println("job attributes  finished "); return ASYNC_FINISH; } };
				 * jobAttributes.setPriority(Job.SHORT); jobAttributes.schedule();
				 */
				// }

			} else {

				MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
						"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
				Services.classAttributes.remove(NodeName.toLowerCase());
				int result = dialog.open();
			}
		}

		return node;
	}

	public Session getSession() {
		return SessionManager.INSTANCE.getSession(getModel());
	}

	public EObject getClassPrediction(EObject rootModel) {
		Services.loggerServices.info("Demand Concepts Predcion ");

		ConceptsFactory conceptsFactory = new ConceptsFactory();
		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;

		List<String> classNames = new ArrayList<String>();
		List<String> AllclassNames = new ArrayList<String>();
		List<String> suggestedConcepts = new ArrayList<String>();
		Model model = getModel();

		// dummy example
//		conceptsFactory.createClassCondidate("nameFRom prediction", "1", session, model);
//		refreshSuggestionsView();

		List<Clazz> classesInModel = model.getClazz();
		String className = "";

		List<ClazzCandidate> classeCandidateInModel = model.getClazzcondidate();
		List<String> Results = new ArrayList<String>();

		for (int i = 0; i < classesInModel.size(); i++) {
			if (classesInModel.get(i) != null) {
				AllclassNames.add(classesInModel.get(i).getName());
				classNames.add(classesInModel.get(i).getName());
			}
		}
		for (int i = 0; i < classeCandidateInModel.size(); i++) {
			if (classeCandidateInModel.get(i) != null) {
				AllclassNames.add(classeCandidateInModel.get(i).getName());
				suggestedConcepts.add(classeCandidateInModel.get(i).getName());
			}

		}

		String input = "";
		if (rootModel instanceof Model) {
			System.out.println("from one Canvas");

			for (int i = 0; i < classesInModel.size(); i++) {
				input = input.concat(",").concat(classesInModel.get(i).getName());
			}
		} else if (rootModel instanceof Clazz) {
			System.out.print("from one class");
			Clazz inputClass = (Clazz) rootModel;
			input = inputClass.getName();
			className = input;

			/**
			 * heuristic: what to send to GPT3 concat to only one class name !
			 **/

			Random rand = new Random();
			String randomElement = AllclassNames.get(rand.nextInt(classNames.size()));
			input = input.concat(",").concat(randomElement);

		}
		if (Services.relatedClasses == null) {
			relatedClasses = new HashMap<String, List<String>>();
		}
		if (relatedClasses.containsKey(className.toLowerCase())) {
			if (!(relatedClasses.get(className.toLowerCase()).isEmpty())
					&& (relatedClasses.get(className.toLowerCase()).size() > 0)) {
				Results = relatedClasses.get(className.toLowerCase());
			}
		} else {
			JOptionPane opt = new JOptionPane("Running prediction", JOptionPane.INFORMATION_MESSAGE); // no buttons
			final JDialog dlg = opt.createDialog("Predicting relevant concepts...");
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(7000);
						dlg.dispose();
					} catch (Throwable th) {

					}
				}
			}).start();
			dlg.setVisible(true);
			IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
			List<HashMap<String, String>> Concepts = conceptsPrediction.run(className, rootModel, getModel());
			for (String key : Concepts.get(0).keySet()) {
				if (!containsIgnoreCase(classNames, key) && !(key.equals(""))) {
					Results.add(key);
				}
				if (!containsIgnoreCase(suggestedConcepts, key) && !(key.equals(""))) {
					conceptsFactory.createClassCandidate((String) key, Concepts.get(0).get(key), session, getModel());

				} else {

					conceptsFactory.updateConfidenceCandidate((String) key, session, model, 1);
				}

			}

			refreshSuggestionsView();
		}

		String[] arrayConcepts = Results.toArray(new String[0]);
		if (arrayConcepts.length > 0) {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(),
					new LabelProvider());
			dialog.setElements(arrayConcepts);
			dialog.setTitle("Select  concepts, press ctrl for multiple selection");
			dialog.setMultipleSelection(true);
			if (dialog.open() != Window.OK) {
				// return false;
			}
			Object[] result = dialog.getResult();
			if (result != null) {
				Services.loggerServices.info("Accept from list of  Concepts ");
				for (int i = 0; i < result.length; i++) {
					if (!containsIgnoreCase(classNames, (String) result[i])) {
						conceptsFactory.createClass((String) result[i], session, false);
						conceptsFactory.deleteClassCandidate((String) result[i], session);

						Results.remove((String) result[i]);

					}
				}
			} else {
				// Immediately launch a job to predict related concepts because user is not
				// satisfied.

				/*
				 * Job job = new Job("concepts  prediction") {
				 * 
				 * protected IStatus run(IProgressMonitor monitor) {
				 * 
				 * try {
				 * 
				 * String className; Clazz inputClass = (Clazz) rootModel; className =
				 * inputClass.getName();
				 * 
				 * System.out.println("concepts job started ");
				 * 
				 * List<String> Results = new ArrayList<String>(); IConceptsPrediction
				 * conceptsPrediction = new ConceptsPrediction(); // rootModel is null
				 * 
				 * List<HashMap<String, String>> Concepts = conceptsPrediction.run(className,
				 * null, model);
				 * 
				 * for (String key : Concepts.get(0).keySet()) { if
				 * ((!containsIgnoreCase(classNames, key))) { Results.add(key); } } // update
				 * hashmap in session. Services.relatedClasses.put(className, Results); for
				 * (String key : Concepts.get(0).keySet()) {
				 * 
				 * if ((!containsIgnoreCase(classNames, key))) { if
				 * (!(containsIgnoreCase(suggestedConcepts, key))) {
				 * conceptsFactory.createClassCondidate((String) key, Concepts.get(0).get(key),
				 * session, model); } else { conceptsFactory.updateConfidenceCondidate((String)
				 * key, session, model, 1); } }
				 * 
				 * }
				 * 
				 * Display.getDefault().syncExec(new Runnable() { public void run() {
				 * Services.refreshSuggestionsView();
				 * 
				 * } });
				 * 
				 * } catch (Exception e) { e.printStackTrace(); }
				 * 
				 * return ASYNC_FINISH; } }; job.setPriority(Job.SHORT); job.schedule();
				 *
				 */
			}
			if (!className.equals("")) {
				// update after removing a selected concept
				Services.relatedClasses.put(className.toLowerCase(), Results);
			}

		} else {
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
					"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
			Services.relatedClasses.remove(className.toLowerCase());

			int result = dialog.open();
		}
		refreshSuggestionsView();
		return rootModel;
	}

	public EObject getAssociationPrediction(EObject rootModel) {
		Services.loggerServices.info("Demand Association Predcion ");
		AssociationsFactory associationsFactory = new AssociationsFactory();
		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className = rootModel.toString().split(":")[1];
		className = className.substring(1, className.length() - 1);
		List<String> Results = new ArrayList<String>();
		String[] ArrayResultsTyped = new String[20];
		String item;

		if (Services.relatedAssociations == null) {
			Services.relatedAssociations = new HashMap<String, List<String>>();
		}
		if (Services.relatedAssociations.containsKey(className.toLowerCase())) {
			if (!Services.relatedAssociations.get(className.toLowerCase()).isEmpty()) {
				Results = Services.relatedAssociations.get(className.toLowerCase());
			}
			ArrayResultsTyped = Results.toArray(new String[0]);

		} else {
			IAssociationsPrediction associationsPrediction = new AssociationsPrediction();
			List<HashMap<String, String>> res = associationsPrediction.run(className, rootModel, getModel());

			List<String> items = new ArrayList<String>();
			for (int j = 0; j < res.size(); j++) {
				if (!res.get(j).get("Type").replaceAll("\\s+", "").equals("")
						&& (!(res.get(j).get("Type").replaceAll("\\s+", "").equals("no")))) {

					if (!(res.get(j).get("Name").replaceAll("\\s+", "").equals(""))) {
						item = res.get(j).get("Type") + ":[" + res.get(j).get("Source") + "," + res.get(j).get("Target")
								+ "]; Name => " + res.get(j).get("Name");
					} else {
						item = res.get(j).get("Type") + ":[" + res.get(j).get("Source") + "," + res.get(j).get("Target")
								+ "]";
					}
					items.add(item);

					ArrayResultsTyped = items.toArray(new String[0]);
					associationsFactory.createAssociationcandidate(res.get(j).get("Type"), res.get(j).get("Name"),
							res.get(j).get("Target"), res.get(j).get("Source"), session, getModel());
					Services.relatedAssociations.put(className.toLowerCase(), items);
				}

			}
			refreshAssociationsView();

		}
		if (ArrayResultsTyped.length > 0 && (ArrayResultsTyped[0] != null)) {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(),
					new LabelProvider());

			dialog.setElements(ArrayResultsTyped);
			// dialog.s
			dialog.setTitle("Select association to add to canvas");
			dialog.setMultipleSelection(true);

			if (dialog.open() != Window.OK) {
				// return false;
			}
			Object[] result = dialog.getResult();

			if (result != null) {
				List<String> items = new ArrayList<String>(Arrays.asList(ArrayResultsTyped));
				Services.loggerServices.info("Accept from list of  Concepts : " + result.length);
				for (int j = 0; j < result.length; j++) {
					item = (String) result[j];
					String Type = item.split(":")[0];
					System.out.println(Type);
					String Name = item.split("=>")[1];
					String Target = (item.split(",")[1]).split("]")[0];
					String Source = (item.split("\\[")[1]).split(",")[0];
					associationsFactory.createAssociation(Type, Name, Target, Source, session, true);
					associationsFactory.removecandidate(Type, Name, Target, Source, session);
					refreshAssociationsView();

					items.remove(item);
					Services.relatedAssociations.put(className.toLowerCase(), items);

					// this is to enable future predictions and not always show "try later"
					if (!(Services.relatedAssociations.get(className.toLowerCase()).size() > 0)) {
						Services.relatedAssociations.remove(className.toLowerCase());
					}
				}
			}
		} else {
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
					"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
			this.relatedAssociations.remove(className.toLowerCase());
			int result = dialog.open();
			refreshAssociationsView();
		}

		return rootModel;
	}

	public EObject approveClassCondidate(EObject rootModel) throws InterruptedException {
		Services.loggerServices.info("Approve Class Candidate  ");
		ConceptsFactory conceptsFactory = new ConceptsFactory();

		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className = "";
		if (rootModel instanceof ClazzCandidate) {
			className = rootModel.toString().split(":", 2)[1];
			className = className.replace(")", " ");

		} else {
			className = rootModel.toString().split(" ", 2)[1];

		}
		if (className.contains(":")) {
			className = className.split(":", 0)[1].replace(")", "");
		}
		Collection<EObject> objects;
		Collection<DRepresentation> allRepresentations = DialectManager.INSTANCE.getAllRepresentations(session);
		for (DRepresentation representation : allRepresentations) {
			DialectManager.INSTANCE.refresh(representation, new NullProgressMonitor());
		}
		objects = new EObjectQuery(rootModel)
				.getInverseReferences(ViewpointPackage.Literals.DSEMANTIC_DECORATOR__TARGET);
		EObject eob = objects.iterator().next();
		// the new class should should have the same coordinates as the candidate class

		setGraphicalHintsFromExistingNode((DDiagramElement) eob);
		conceptsFactory.createClass(className, session, false);
		conceptsFactory.deleteClassCandidate(className, session);

		return rootModel;
	}

}
