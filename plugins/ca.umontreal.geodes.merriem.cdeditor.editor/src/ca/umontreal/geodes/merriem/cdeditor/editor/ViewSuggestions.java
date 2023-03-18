package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class ViewSuggestions extends ViewPart {
	private Services services;
	private ConceptsFactory conceptsFactory;
	public Composite parent;
	// Table table;

	public ViewSuggestions() {
		try {
			this.services = new Services();
			this.conceptsFactory = new ConceptsFactory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("restriction")
	public void createContents() {
		System.out.println("the service mode is : " + Services.mode);

		Control[] children = parent.getChildren();
		for (Control child : children) {
			if (!child.isDisposed()) {
				child.dispose();
			}
		}
		if (Services.mode != Mode.assessAtEnd && Services.mode != Mode.OnRequest) {
			// mergeCandidates(this.services.getModel());
			List<ClazzCandidate> classCondidateInModel = this.services.getModel().getClazzcondidate();
			while (classCondidateInModel.remove(null))
				;

			Table table = new Table(parent, SWT.BORDER);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);

			data.heightHint = 600;
			/*
			 * Button refreshButton = new Button(parent, SWT.PUSH);
			 * refreshButton.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, false));
			 * refreshButton.setText("Show Suggestions"); refreshButton.setSize(80, 16);
			 * 
			 * refreshButton.pack(); refreshButton.addMouseListener(new MouseListener() {
			 * 
			 * @Override public void mouseDoubleClick(MouseEvent e) { //
			 * System.out.println("accepted clicked"); }
			 * 
			 * @Override public void mouseDown(MouseEvent e) {
			 * System.out.println("put to on trigger"); Services.mode = Mode.OnTrigger;
			 * Services.refreshSuggestionsView(); }
			 * 
			 * @Override public void mouseUp(MouseEvent e) { // TODO Auto-generated method
			 * stub
			 * 
			 * } });
			 */
			table.setLayoutData(data);
			parent.layout(true, true);
			// parent.pack();

			String[] titles = { "Suggestion", "score", "Add class to Canvas?", "Attributes" };
			for (String title : titles) {
				TableColumn column = new TableColumn(table, SWT.CHECK);
				column.setText(title);
			}

			List<ClazzCandidate> sortedClazzCondidate = classCondidateInModel.stream()
					.sorted(Comparator.comparing(ClazzCandidate::getConfidence).reversed())
					.collect(Collectors.toList());

			for (int i = 0; i < sortedClazzCondidate.size(); i++) {
				TableItem item = new TableItem(table, SWT.CHECK);
				item.setText(0, sortedClazzCondidate.get(i).getName());
				item.setText(1, Integer.toString(sortedClazzCondidate.get(i).getConfidence()));

			}



			for (int i = 0; i < titles.length; i++) {
				table.getColumn(i).pack();
			}

			TableItem[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {
				int indexItem = i;
				TableEditor editor = new TableEditor(table);
				TableEditor editorAttributes = new TableEditor(table);
				TableItem item = items[i];
				Button button = new Button(table, SWT.PUSH);

				
				Button buttonAttributes = new Button(table, SWT.PUSH);
				editor.grabHorizontal = true;
				editor.minimumWidth = 50;
				editorAttributes.grabHorizontal = true;
				editorAttributes.minimumWidth = 50;

				button.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, true));
				buttonAttributes.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, true));

				button.setText("Accept");
				button.pack();

				buttonAttributes.setText("Show Attributes");

				buttonAttributes.pack();
				// editor.minimumWidth = button.getSize().x;
				editor.horizontalAlignment = SWT.LEFT;
				editorAttributes.horizontalAlignment = SWT.LEFT;

				editor.setEditor(buttonAttributes, item, 3);
				editorAttributes.setEditor(button, item, 2);
				if(sortedClazzCondidate.get(i).getConfidence()>1) {
					System.out.println("confidence is high");
					buttonAttributes.setVisible(false);
				}
				// editor.layout();
				button.addMouseListener(new MouseListener() {

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// System.out.println("accepted clicked");
					}

					@Override
					public void mouseDown(MouseEvent e) {
						try {
							Session session = services.getSession();
							Services.loggerServices.info("Accept Concept From view of  Suggestions" );
							String acceptedClassName = item.getText(0);
							button.setVisible(false);
							button.dispose();
							buttonAttributes.setVisible(false);
							buttonAttributes.dispose();
							table.remove(indexItem);
							if (Services.relatedClasses != null) {
								for (String l : Services.relatedClasses.keySet()) {
									if (Services.relatedClasses.get(l).contains(acceptedClassName)) {
										Services.relatedClasses.get(l).remove(acceptedClassName);

									}
								}
							}
							conceptsFactory.deleteClassCandidate(acceptedClassName, session);
							conceptsFactory.createClass(acceptedClassName, session, true);
							JobConcepts jobConcepts = new JobConcepts("Concepts prediction", services,
									services.getModel(), session, false);

							jobConcepts.setPriority(Job.SHORT);
							jobConcepts.schedule();

						} catch (Exception e1) {
							System.out.println(e1);
						}
					}

					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
				buttonAttributes.addMouseListener(new MouseListener() {

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// System.out.println("accepted clicked");
					}

					@Override
					public void mouseDown(MouseEvent e) {
						try {
							Session session = services.getSession();
							HashMap<String, String> typeAttributes = null;
							AttributesFactory attributesFactory = new AttributesFactory();

							List<String> types = new ArrayList<>(List.of("int", "string", "float", "char", "boolean",
									"double", "byte", "array", "object", "collection"));

							System.out.println("show attributes");
							String acceptedClassName = item.getText(0);
							List<String> ResultsTyped = new ArrayList<String>();

							if (Services.classAttributes == null) {
								Services.classAttributes = new HashMap<String, HashMap<String, String>>();
							}
							if (Services.classAttributes.containsKey(acceptedClassName)) {
								if (!Services.classAttributes.get(acceptedClassName).isEmpty()) {
									// && (this.classAttributes.get(NodeName).size() > 1)) {

									typeAttributes = Services.classAttributes.get(acceptedClassName);
								}
								if (typeAttributes == null) {
									System.out.println("type attribute is null");
									MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
											"Try again later", null, "We have no suggestion for you now",
											MessageDialog.ERROR, new String[] { "Cancel" }, 0);
									int result = dialog.open();
								} else {
									for (Map.Entry<String, String> set : typeAttributes.entrySet()) {
										if ((set.getKey().replaceAll(" ", "").equalsIgnoreCase(""))
												|| (set.getValue().replaceAll(" ", "").equalsIgnoreCase(""))) {
											typeAttributes.remove(set.getKey());
										} else if (!(set.getKey().replaceAll(" ", "").equalsIgnoreCase(""))
												&& !(set.getValue().replaceAll(" ", "").equalsIgnoreCase(""))) {
											if (services.containsIgnoreCase(types, set.getValue())) {

												ResultsTyped.add(set.getKey().concat(":").concat(set.getValue()));
											}
										}
									}

									String[] ArrayResultsTyped = ResultsTyped.toArray(new String[0]);

									if (ArrayResultsTyped.length > 0) {

										ElementListSelectionDialog dialog = new ElementListSelectionDialog(
												Display.getCurrent().getActiveShell(), new LabelProvider());

										dialog.setElements(ArrayResultsTyped);
										dialog.setTitle("Select appropriate attributes" + acceptedClassName);
										dialog.setMultipleSelection(true);

										if (dialog.open() != Window.OK) {
											// return false;
										}

										Object[] result = dialog.getResult();
										if (result != null) {
											for (int i = 0; i < result.length; i++) {
												String res = (String) result[i];
												res = res.split(":")[0];
												attributesFactory.createAttribute(res, typeAttributes.get(res),
														acceptedClassName, session, false);
												typeAttributes.remove(res);
											}
											Services.classAttributes.put(acceptedClassName, typeAttributes);

										}

									}
									;
								}
								;
							} else {

								MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
										"Try again later", null, "We have no suggestion for you now",
										MessageDialog.ERROR, new String[] { "Cancel" }, 0);
								int result = dialog.open();
							}
						}

						catch (Exception e1) {
							System.out.println(e1);
						}
					}

					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub

					}

				});

			}
		}

	}

	@Override
	public void createPartControl(Composite parent) {
		if (Services.mode != Mode.assessAtEnd && Services.mode != Mode.OnRequest) {

			this.parent = parent;

			createContents();
		}
	}

	/**
	 * public void mergeCandidates(Model model) {
	 * 
	 * List<ClazzCandidate> candidatesInModel = model.getClazzcondidate();
	 * 
	 * Map<String, Integer> nameCounts = new HashMap<>(); for (ClazzCandidate
	 * candidate : candidatesInModel) { if (candidate != null) { String name =
	 * candidate.getName(); int count = nameCounts.getOrDefault(name, 0);
	 * nameCounts.put(name, count + candidate.getConfidence()); }
	 * 
	 * }
	 * 
	 * Session session = services.getSession(); for (Map.Entry<String, Integer>
	 * entry : nameCounts.entrySet()) { String name = entry.getKey(); int count =
	 * entry.getValue(); conceptsFactory.deleteClassCandidate(name, session);
	 * 
	 * conceptsFactory.createClassCandidate(name, Integer.toString(count), session,
	 * model);
	 * 
	 * }
	 * 
	 * }
	 **/

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}