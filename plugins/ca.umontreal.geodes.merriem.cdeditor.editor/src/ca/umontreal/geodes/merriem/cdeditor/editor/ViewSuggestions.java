package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;

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
		System.out.println("the service mode is : " +  Services.mode);

			Control[] children = parent.getChildren();
			for (Control child : children) {
				if (!child.isDisposed()) {
					child.dispose();
				}
			}
			if (Services.mode != Mode.assessAtEnd && Services.mode != Mode.OnRequest) {

				List<ClazzCandidate> classCondidateInModel = this.services.getModel().getClazzcondidate();
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

				String[] titles = { "Suggestion", "score", "Add to Canvas?" };
				for (String title : titles) {
					TableColumn column = new TableColumn(table, SWT.CHECK);
					column.setText(title);
				}

				// classCondidateInModel.sort(Comparator.comparing(ClazzCondidate::getConfidence).reversed());
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
					TableItem item = items[i];
					Button button = new Button(table, SWT.PUSH);
					button.setText("Accept");
					button.setSize(80, 16);
					button.pack();
					editor.minimumWidth = button.getSize().x;
					editor.horizontalAlignment = SWT.LEFT;
					editor.setEditor(button, item, 2);
					editor.layout();
					button.addMouseListener(new MouseListener() {

						@Override
						public void mouseDoubleClick(MouseEvent e) {
							// System.out.println("accepted clicked");
						}

						@Override
						public void mouseDown(MouseEvent e) {
							try {
								Session session = services.getSession();
								String acceptedClassName = item.getText(0);
								button.setVisible(false);
								button.dispose();
								table.remove(indexItem);
								if (Services.relatedClasses != null) {
									for (String l : Services.relatedClasses.keySet()) {

										if (Services.relatedClasses.get(l).contains(acceptedClassName)) {
											Services.relatedClasses.get(l).remove(acceptedClassName);

										}
									}
								}
								conceptsFactory.deletetClassCondidate(acceptedClassName, session);

								conceptsFactory.createClass(acceptedClassName, session, true);

							} catch (Exception e1) {
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

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}