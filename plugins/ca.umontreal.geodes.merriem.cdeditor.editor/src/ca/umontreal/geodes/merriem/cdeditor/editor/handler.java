package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.ServiceException;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeCondidateImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

public class handler extends AbstractHandler {

	private Services services;
	private Properties config;

	/**
	 * The constructor.
	 * 
	 * @throws Exception
	 */
	public handler() throws Exception {
		this.services = new Services();

		this.config = new Properties();
		try {
			InputStream stream = Services.class.getClassLoader().getResourceAsStream("/config.properties");
			this.config.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * the command has been executed, so extract extract the needed information from
	 * the application context.
	 */

	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";

	public void createInstance(String instanceType, String Name, String containerName) {
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
					// Implement your write operations here
					// for example: set a new name

					Model m = services.getModel();
					List<Clazz> classes = new ArrayList<Clazz>();
					classes = m.getClazz();
					List<String> classNames = new ArrayList<String>();
					String input = "";

					for (int i = 0; i < classes.size(); i++) {
						input = input.concat(",").concat(classes.get(i).getName());
						classNames.add(classes.get(i).getName().toLowerCase());
					}
					List<String> Concepts = new ArrayList<String>();


					String scriptLocation = config.getProperty("scriptlocation");
					String pythonCommand = config.getProperty("pythoncommand");

					// **********************"predicting concepts condidates"********************//
					// ****************************************************************************//
					try {
						Process P = new ProcessBuilder(pythonCommand, scriptLocation + "predictConcepts.py", input)
								.start();

						BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
						BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));

						String s;
						while ((s = stdInput.readLine()) != null) {
							if (!classNames.contains(s)) {
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

					for (int i = 0; i < arrayConcepts.length; i++) {

						// ***************"creaating class condidate"**************//

						if (!services.containsIgnoreCase(classNames, arrayConcepts[i].toLowerCase())) {

							// **********************"predicting attributes condidates for
							// arrayConcepts[i]********************//
							// ***********************************************************************************************//

							String[] arrayAttributes;
							HashMap<String, String> typeAttributes = new HashMap<String, String>();
							input = "";
							List<String> Results = new ArrayList<String>();

							try {
								Process P1 = new ProcessBuilder(pythonCommand, scriptLocation + "predictAttributes.py",
										arrayConcepts[i], input, "Attribute").start();

								BufferedReader stdInput = new BufferedReader(
										new InputStreamReader(P1.getInputStream()));
								BufferedReader stdError = new BufferedReader(
										new InputStreamReader(P1.getErrorStream()));
								String s;
								while ((s = stdInput.readLine()) != null) {
									if (s != "") {
										Results.add(s);
									}
								}

								while ((s = stdError.readLine()) != null) {
									// add logger !
									System.out.println(s);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							arrayAttributes = Results.toArray(new String[0]);

							// print received attributes from python script
							for (int k = 0; k < arrayAttributes.length; k++) {
								String Type = "";
								if (arrayAttributes[k] != "") {
									try {
										Process P2 = new ProcessBuilder(pythonCommand,
												scriptLocation + "predictAttributes.py", arrayAttributes[i], input,
												"Type").start();
										BufferedReader stdInput = new BufferedReader(
												new InputStreamReader(P2.getInputStream()));
										BufferedReader stdError = new BufferedReader(
												new InputStreamReader(P2.getErrorStream()));
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
								}
								typeAttributes.put(arrayAttributes[i], Type);

							}

							List<String> ResultsTyped = new ArrayList<String>();

							// ***********************create clazz condidate **************************//
							MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
							ClazzCondidateImpl newClazzCondidate = (ClazzCondidateImpl) metamodelFactory
									.createClazzCondidate();
							newClazzCondidate.setName(arrayConcepts[i]);
							m.getClazzcondidate().add(newClazzCondidate);
							classNames.add(arrayConcepts[i]);
							// refresh Model
							DRepresentation represnt = null;
							for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
								represnt = descrp.getRepresentation();

							}
							DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());

							// ***************Concat results from both predictions***********//
							for (int j = 0; j < arrayAttributes.length; j++) {
								if (arrayAttributes[j] != "" && typeAttributes.get(arrayAttributes[j]) != "") {
									ResultsTyped.add(arrayAttributes[i].concat(":")
											.concat(typeAttributes.get(arrayAttributes[i])));
									MetamodelFactory metamodelFactory_i = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;

									AttributeCondidateImpl newAttribute = (AttributeCondidateImpl) metamodelFactory_i
											.createAttributeCondidate();
									newAttribute.setName(arrayAttributes[i]);
									newAttribute.setType(typeAttributes.get(arrayAttributes[i]));
									newClazzCondidate.getAttributecondidate().add(newAttribute);
									// refresh Model
									for (DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
										represnt = descrp.getRepresentation();

									}
									DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());
								}
							}
						}
					}

				}
			};

			
			stack.execute(cmd);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return null;
	}

}