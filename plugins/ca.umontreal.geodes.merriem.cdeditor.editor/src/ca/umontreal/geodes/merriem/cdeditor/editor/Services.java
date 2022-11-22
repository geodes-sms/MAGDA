package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.internal.EditorReference;
import org.osgi.framework.ServiceException;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Association;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.AttributeCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeCondidateImpl;
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
	protected HashMap<String, List<String>> relatedClasses;

	public Services() throws Exception {
		this.config = new Properties();
		this.relatedClasses = new HashMap<String, List<String>>();
		this.classAttributes = new HashMap<String, HashMap<String, String>>();
		try {
			InputStream stream = Services.class.getClassLoader().getResourceAsStream("/config.properties");
			this.config.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResourceSetListener listener = new MyListener();
		TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

		domain.addResourceSetListener(listener);

	};

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

	public boolean containsIgnoreCase(List<String> list, String soughtFor) {

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
					// MetamodelFactory metamodelFactory =
					// ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

					List<ClazzCondidate> classesCondidate = model.getClazzcondidate();
					int index = Nan;
					for (int i = 0; i < classesCondidate.size(); i++) {
						if (classesCondidate.get(i).getName().replaceAll("\\s+", "")
								.equals(classToRemove.replaceAll("\\s+", ""))) {
							index = i;
							// removedClazz= classesCondidate.get(i);

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

		// return removedClazz;
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

	public void createAssociation(String Type, String Name, String Target, String Source, Session session) {

		try {

			DAnalysis root = (DAnalysis) session.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);

			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

			CommandStack stack = domain.getCommandStack();

			RecordingCommand cmd = new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					System.out.println("the type: " + Type);
					Model model = getModel();
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

	public EObject getAttributePrediction(EObject node) {

		Session session = SessionManager.INSTANCE.getSession(node);
		assert session != null;
		String NodeName = node.toString().split(":", 2)[1].replace(")", "");
		NodeName = NodeName.replaceAll("\\s+", "");
		List<String> ResultsTyped = new ArrayList<String>();
		HashMap<String, String> typeAttributes;

		if (this.classAttributes.containsKey(NodeName) && !this.classAttributes.get(NodeName).isEmpty()) {
			typeAttributes = this.classAttributes.get(NodeName);
		} else {
			IAttributesPrediction attributesPredcition = new AttributesPrediction();
			typeAttributes = attributesPredcition.run(node, getModel());
			for (Map.Entry<String, String> set : typeAttributes.entrySet()) {
				System.out.println(set.getKey().concat(":").concat(set.getValue()));
				ResultsTyped.add(set.getKey().concat(":").concat(set.getValue()));
			}
		
		}

		String[] ArrayResultsTyped = ResultsTyped.toArray(new String[0]);
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(),
				new LabelProvider());

		dialog.setElements(ArrayResultsTyped);
		dialog.setTitle("select appropriate attributes, press ctrl for multiple selection");
		dialog.setMultipleSelection(true);

		if (dialog.open() != Window.OK) {
			// return false;
		}
		Object[] result = dialog.getResult();
		for (int i = 0; i < result.length; i++) {
			String res = (String) result[i];
			res = res.split(":")[0];
			createAttribute(res, typeAttributes.get(res), NodeName, session);
			typeAttributes.remove(res);
		}
		this.classAttributes.put(NodeName, typeAttributes);
		return node;
	}

	public EObject getClassPrediction(EObject rootModel) {

		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String[] arrayConcepts = new String[50];

		List<String> classNames = new ArrayList<String>();
		List<String> AllclassNames = new ArrayList<String>();
		Model model = getModel();
		List<Clazz> classesInModel = model.getClazz();
		String className = "";
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
			System.out.print("from one class");
			Clazz inputClass = (Clazz) rootModel;
			input = inputClass.getName();
			className = input;
			classNames.add(inputClass.getName());
			// heuristic: what to send to GPT3
			Random rand = new Random();
			String randomElement = AllclassNames.get(rand.nextInt(AllclassNames.size()));
			input = input.concat(",").concat(randomElement);
			System.out.println(className);

		}
		if (relatedClasses.containsKey(className) && !relatedClasses.get(className).isEmpty()) {
			arrayConcepts = relatedClasses.get(className).toArray(new String[0]);
		} else {
			System.out.println("not found in Cash! , start predicting ... ");

			IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
			List<HashMap<String, String>> Concepts = conceptsPrediction.run(rootModel, getModel());

			for (String key : Concepts.get(0).keySet()) {

				arrayConcepts[arrayConcepts.length] = Concepts.get(0).get(key);
			}

		}

		return null;
	}

	public EObject getAssociationPrediction(EObject rootModel) {

		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		// AssociationsPrediction(); AssociationsPredictionDummy()
		IAssociationsPrediction associationsPrediction = new AssociationsPrediction();
		List<HashMap<String, String>> res = associationsPrediction.run(rootModel, getModel());
		for (int j = 0; j < res.size(); j++) {
			System.out.println(res.get(j).get("Type"));
			createAssociation(res.get(j).get("Type"), res.get(j).get("Name"), res.get(j).get("Target"),
					res.get(j).get("Source"), session);
		}

		return null;
	}

	public EObject approveClassCondidate(EObject rootModel) throws InterruptedException {
		Session session = SessionManager.INSTANCE.getSession(rootModel);
		assert session != null;
		String className = "";
		if (rootModel instanceof ClazzCondidate) {
			className = rootModel.toString().split(":", 2)[1];
			className = className.replace(")", " ");

		} else {
			className = rootModel.toString().split(" ", 2)[1];
			System.out.println(className);

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
		createClass(className, session);
		deletetClassCondidate(className, session);

		return rootModel;
	}

}
