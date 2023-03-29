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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
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
	public static ProgressBar progressBar;

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
			while (classCondidateInModel.remove(null));
				

//			Table table = new Table(parent, SWT.BORDER);
//			table.setLinesVisible(true);
//			table.setHeaderVisible(true);
//			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
//			data.heightHint = 200;
//			table.setLayoutData(data);
//			parent.layout(true, true);
			

			 
		
			
			
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setVisible(true);
		    composite.setLayout(new GridLayout());
		    composite.layout(true, true);
		    GridData compositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		    composite.setLayoutData(compositeData);
		    // Create the table control
		    Table table = new Table(composite, SWT.BORDER);
			
			
			  //Table table = new Table(parent, SWT.BORDER);
			    table.setLinesVisible(true);
			    table.setHeaderVisible(true);
			    table.setLayout(new GridLayout());
			    
			    GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true); 
			    tableData.heightHint = 600;
			    table.setLayoutData(tableData);
		    
			    // Create a progress bar control
			   
//			
			   
				
				Composite progressBarComposite = new Composite(composite, SWT.NONE);
				progressBarComposite.setLayout(new GridLayout());
				GridData progressBarCompositeData = new GridData(SWT.FILL, SWT.TOP, true, false);
				progressBarComposite.setLayoutData(progressBarCompositeData);
				progressBarComposite.layout(true,true);
				this.progressBar = new ProgressBar(progressBarComposite, SWT.HORIZONTAL | SWT.INDETERMINATE);
				GridData progressBarData = new GridData(SWT.FILL, SWT.TOP, true, false);
				this.progressBar.setLayoutData(progressBarData);
				this.progressBar.setVisible(true);
			

			    parent.layout(true, true);


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
				String CandidateNameUppercase = sortedClazzCondidate.get(i).getName().substring(0, 1).toUpperCase()
						+ sortedClazzCondidate.get(i).getName().substring(1);
				item.setText(0, CandidateNameUppercase);
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
				if (sortedClazzCondidate.get(i).getConfidence() < 2) {
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
							Services.loggerServices.info("Accept Concept From view ");
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
									services.getModel(), session, false, progressBar);

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
							if (Services.classAttributes.containsKey(acceptedClassName.toLowerCase())) {
								if (!Services.classAttributes.get(acceptedClassName.toLowerCase()).isEmpty()) {
									// && (this.classAttributes.get(NodeName).size() > 1)) {

									typeAttributes = Services.classAttributes.get(acceptedClassName.toLowerCase());
								}

							} else {

								IAttributesPrediction attributesPredcition = new AttributesPrediction();
								typeAttributes = attributesPredcition.run(null, acceptedClassName, services.getModel(),
										false);
								Services.classAttributes.put(acceptedClassName.toLowerCase(), typeAttributes);
							}
							if (typeAttributes == null) {
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
									dialog.setTitle("Potential attributes for class \" " + acceptedClassName + "\"");
									dialog.setMultipleSelection(true);

									if (dialog.open() != Window.OK) {
										// return false;
									}

								}
								;
							}
							;

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

	public static ProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public void createPartControl(Composite parent) {
		if (Services.mode != Mode.assessAtEnd && Services.mode != Mode.OnRequest) {

			this.parent = parent;

			createContents();

		}

	}

	@Override
	public void setFocus() {

	}

}