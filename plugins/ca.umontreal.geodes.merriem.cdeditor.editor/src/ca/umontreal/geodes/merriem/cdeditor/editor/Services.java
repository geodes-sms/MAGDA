package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
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
import org.eclipse.ui.internal.EditorReference;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

/**
 * The services class used by VSM.
 */
public class Services {

	private Properties config;
	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";
	protected static final int Nan = 0;
	protected static HashMap<String, HashMap<String, String>> classAttributes;
	protected HashMap<String, List<String>> relatedClasses;
	protected static HashMap<String, List<String>> relatedAssociations;
	protected ResourceSetListener listener;
	public static IViewPart suggestionView;
	public static IViewPart associationView;

	public Services() throws Exception {
		this.config = new Properties();
		this.relatedClasses = new HashMap<String, List<String>>();
		// this.relatedAssociations = new HashMap<String, List<String>>();
		// this.classAttributes = new HashMap<String, HashMap<String, String>>();
		this.listener = (ResourceSetListener) new Listener();
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

		// domain.addResourceSetListener(new MyListener(this.classAttributes));

	};

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
		AttributesFactory attributesFactory = new AttributesFactory();
		Session session = SessionManager.INSTANCE.getSession(node);
		assert session != null;
		List<String> types = new ArrayList<>(
				List.of("int", "string", "float", "char", "boolean", "double", "byte", "array", "object"));
		String NodeName = node.toString().split(":", 2)[1].replace(")", "");
		NodeName = NodeName.replaceAll("\\s+", "");
		List<String> ResultsTyped = new ArrayList<String>();

		HashMap<String, String> typeAttributes;
		if (Services.classAttributes == null) {
			Services.classAttributes = new HashMap<String, HashMap<String, String>>();
		}
		if (Services.classAttributes.containsKey(NodeName) && !Services.classAttributes.get(NodeName).isEmpty()) {
			// && (this.classAttributes.get(NodeName).size() > 1)) {

			typeAttributes = Services.classAttributes.get(NodeName);
		} else {

			IAttributesPrediction attributesPredcition = new AttributesPrediction();
			typeAttributes = attributesPredcition.run(node, NodeName, getModel());

			// dummy example remove this
			// typeAttributes =new HashMap<String, String>();
		}
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

			ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(),
					new LabelProvider());

			dialog.setElements(ArrayResultsTyped);

			dialog.setTitle("Select appropriate attributes");
			dialog.setMultipleSelection(true);

			if (dialog.open() != Window.OK) {
				// return false;
			}

			Object[] result = dialog.getResult();
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
					String res = (String) result[i];
					res = res.split(":")[0];
					attributesFactory.createAttribute(res, typeAttributes.get(res), NodeName, session);
					typeAttributes.remove(res);
				}
			}

			Services.classAttributes.put(NodeName, typeAttributes);

		} else {
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
					"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
			Services.classAttributes.remove(NodeName);
			int result = dialog.open();
		}

		return node;
	}

	public Session getSession() {
		return SessionManager.INSTANCE.getSession(getModel());
	}

	public EObject getClassPrediction(EObject rootModel) {

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
		List<ClazzCondidate> classeCondidateInModel = model.getClazzcondidate();
		List<String> Results = new ArrayList<String>();
		for (int i = 0; i < classesInModel.size(); i++) {
			AllclassNames.add(classesInModel.get(i).getName());
			classNames.add(classesInModel.get(i).getName());
		}
		for (int i = 0; i < classeCondidateInModel.size(); i++) {
			AllclassNames.add(classeCondidateInModel.get(i).getName());
		}
		for (int i = 0; i < classeCondidateInModel.size(); i++) {
			suggestedConcepts.add(classeCondidateInModel.get(i).getName());
		}
		String input = "";
		if (rootModel instanceof Model) {
			System.out.println("from one Canvas");

			for (int i = 0; i < classesInModel.size(); i++) {
				input = input.concat(",").concat(classesInModel.get(i).getName());
				classNames.add(classesInModel.get(i).getName());
			}
		} else if (rootModel instanceof Clazz) {
			System.out.print("from one class");
			Clazz inputClass = (Clazz) rootModel;
			input = inputClass.getName();
			className = input;
			// heuristic: what to send to GPT3
			Random rand = new Random();
			String randomElement = AllclassNames.get(rand.nextInt(AllclassNames.size()));
			input = input.concat(",").concat(randomElement);

		}
		if (relatedClasses.containsKey(className)) {
			if (!(relatedClasses.get(className).isEmpty()) && (relatedClasses.get(className).size() > 1)) {
				Results = relatedClasses.get(className);
			}
		} else {
			JOptionPane opt = new JOptionPane("Running prediction", JOptionPane.NO_OPTION); // no buttons
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
			List<HashMap<String, String>> Concepts = conceptsPrediction.run(rootModel, getModel());
			for (String key : Concepts.get(0).keySet()) {
				Results.add(key);
				if (!containsIgnoreCase(suggestedConcepts, key)) {
					conceptsFactory.createClassCondidate((String) key, Concepts.get(0).get(key), session, getModel());

				} else {

					conceptsFactory.updateConfidenceCondidate((String) key, session, model, 1);
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
				for (int i = 0; i < result.length; i++) {
					if (!containsIgnoreCase(classNames, (String) result[i])) {
						conceptsFactory.createClass((String) result[i], session);
						Results.remove((String) result[i]);
					}
				}
			}
			if (!className.equals("")) {
				this.relatedClasses.put(className, Results);
			}

		} else {
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
					"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
			this.relatedClasses.remove(className);
			int result = dialog.open();
		}
		return rootModel;
	}

	public EObject getAssociationPrediction(EObject rootModel) {
		AssociationsFactory associationsFactory = new AssociationsFactory();
		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className = rootModel.toString().split(":")[1];
		className = className.substring(1, className.length() - 1);
		List<String> Results = new ArrayList<String>();
		String[] ArrayResultsTyped = new String[20];
		// AssociationsPrediction(); AssociationsPredictionDummy()
		String item;

		if (Services.relatedAssociations == null) {
			Services.relatedAssociations = new HashMap<String, List<String>>();
		}
		if (Services.relatedAssociations.containsKey(className)) {
			if (!Services.relatedAssociations.get(className).isEmpty()) {
				Results = Services.relatedAssociations.get(className);
			}
			ArrayResultsTyped = Results.toArray(new String[0]);

		} else {
			IAssociationsPrediction associationsPrediction = new AssociationsPrediction();
			List<HashMap<String, String>> res = associationsPrediction.run(className, rootModel, getModel());

			List<String> items = new ArrayList<String>();
			for (int j = 0; j < res.size(); j++) {
				if (!res.get(j).get("Type").replaceAll("\\s+", "").equals("")
						&& (!(res.get(j).get("Type").replaceAll("\\s+", "").equals("no")))) {
					item = res.get(j).get("Type") + ":[" + res.get(j).get("Source") + "," + res.get(j).get("Target")
							+ "]; Name => " + res.get(j).get("Name");
					items.add(item);

					ArrayResultsTyped = items.toArray(new String[0]);
					associationsFactory.createAssociationCondidate(res.get(j).get("Type"), res.get(j).get("Name"),
							res.get(j).get("Target"), res.get(j).get("Source"), session);
					refreshAssociationsView();
					Services.relatedAssociations.put(className, items);
				}

			}

		}
		if (ArrayResultsTyped.length > 0 && (ArrayResultsTyped[0]!=null)) {
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

				for (int j = 0; j < result.length; j++) {
					item = (String) result[j];
					String Type = item.split(":")[0];
					System.out.println(Type);
					String Name = item.split("=>")[1];
					String Target = (item.split(",")[1]).split("]")[0];
					String Source = (item.split("\\[")[1]).split(",")[0];
					associationsFactory.createAssociation(Type, Name, Target, Source, session);
					associationsFactory.removeCondidate(Type, Name, Target, Source, session);
					refreshAssociationsView();

					items.remove(item);
					Services.relatedAssociations.put(className, items);
				}
			}
		} else {
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Try again later", null,
					"We have no suggestion for you now", MessageDialog.ERROR, new String[] { "Cancel" }, 0);
			this.relatedClasses.remove(className);
			int result = dialog.open();
		}

		return rootModel;
	}

	public EObject approveClassCondidate(EObject rootModel) throws InterruptedException {
		ConceptsFactory conceptsFactory = new ConceptsFactory();

		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className = "";
		if (rootModel instanceof ClazzCondidate) {
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
		conceptsFactory.createClass(className, session);
		conceptsFactory.deletetClassCondidate(className, session);

		return rootModel;
	}

}
