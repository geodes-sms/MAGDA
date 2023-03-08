package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationCandidateImpl;
import kotlin.Pair;

public class ViewAssociations extends ViewPart {

	private Services services;
	private AssociationsFactory associationsFactory;
	public Composite parent;

	public ViewAssociations() {
		try {
			this.services = new Services();
			this.associationsFactory = new AssociationsFactory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		if (Services.mode != Mode.assessAtEnd && Services.mode != Mode.OnRequest) {
			this.parent = parent;

			createContents();
		}

	}

	public void createContents() {
		// TODO Auto-generated method stub

		Control[] children = parent.getChildren();
		for (Control child : children) {
			if (!child.isDisposed()) {
				child.dispose();
			}
		}
		if (Services.mode != Mode.assessAtEnd && Services.mode != Mode.OnRequest) {

			List<AssociationCandidate> operationsCandidate = this.services.getModel().getOperation();
			Table table = new Table(parent, SWT.BORDER);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.heightHint = 200;
			table.setLayoutData(data);
			parent.layout(true, true);
			// parent.pack();
			String[] titles = { "Source", "Target", "Name", "Type of association", "Draw in Canvas?", "score" };
			for (String title : titles) {
				TableColumn column = new TableColumn(table, SWT.CHECK);
				column.setText(title);
			}

			// trying to have score with associations;
//			HashMap<Pair<String, String>, Integer> AssociationsFrequency = null;
//
//			for (int i = 0; i < operationsCandidate.size(); i++) {
//
//				// exact matching
//				Pair itemOperation1 = new Pair<>(operationsCandidate.get(i).getSource(),
//						operationsCandidate.get(i).getTarget());
//				Pair itemOperation2 = new Pair<>(operationsCandidate.get(i).getTarget(),
//						operationsCandidate.get(i).getSource());
//
//				if (AssociationsFrequency.keySet().contains(itemOperation2)) {
//					Integer score = AssociationsFrequency.get(operationsCandidate.get(i));
//					AssociationsFrequency.put(itemOperation2, score + 1);
//				} else if (AssociationsFrequency.keySet().contains(itemOperation1)) {
//					Integer score = AssociationsFrequency.get(operationsCandidate.get(i));
//					AssociationsFrequency.put(itemOperation1, score + 1);
//				}
//
//				else {
//					AssociationsFrequency.put(itemOperation2, 1);
//				}
//			}

			HashMap<AssociationCandidate, Integer> selectedOPerations = new HashMap<>();

			for (int i = 0; i < operationsCandidate.size(); i++) {
				Boolean find = false;
				for (AssociationCandidate associationCandidateKey : selectedOPerations.keySet()) {
					if (operationsCandidate.get(i).getSource().getName()
							.equals(associationCandidateKey.getSource().getName())
							&& operationsCandidate.get(i).getTarget().getName()
									.equals(associationCandidateKey.getTarget().getName())) {
						find = true;
						int score = selectedOPerations.get(associationCandidateKey);
						selectedOPerations.put(associationCandidateKey, score + 1);

					}
				}
				if (!find) {
					selectedOPerations.put(operationsCandidate.get(i), 1);
				}
			}

			for (AssociationCandidate associationCandidateKey : selectedOPerations.keySet()) {
				TableItem item = new TableItem(table, SWT.CHECK);
				item.setText(0, associationCandidateKey.getSource().getName());
				item.setText(1, associationCandidateKey.getTarget().getName());
				item.setText(2, associationCandidateKey.getName());
				item.setText(3, associationCandidateKey.getType());
				item.setText(5, selectedOPerations.get(associationCandidateKey).toString());

			}

//			for (int i = 0; i < operationsCandidate.size(); i++) {
//				TableItem item = new TableItem(table, SWT.CHECK);
//				item.setText(0, operationsCandidate.get(i).getSource().getName());
//				item.setText(1, operationsCandidate.get(i).getTarget().getName());
//				item.setText(2, operationsCandidate.get(i).getName());
//				item.setText(3, operationsCandidate.get(i).getType());
//			}
			for (int i = 0; i < titles.length; i++) {
				table.getColumn(i).pack();
			}
			TableItem[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {
				int indexItem = i;
				TableEditor editor = new TableEditor(table);
				TableItem item = items[i];
				Button button = new Button(table, SWT.PUSH);
				button.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, true));
				editor.grabHorizontal = true;
				editor.minimumWidth = 50;
				button.setText("Draw");
				button.pack();
				
	
				editor.setEditor(button, item, 4);
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
							

							// need to figure it out, what is acceptedAssociation
//							if (Services.relatedAssociations != null) {
//								for (String l : Services.relatedAssociations.keySet()) {
//
//									if (Services.relatedAssociations.get(l).contains(acceptedAssociation)) {
//										Services.relatedAssociations.get(l).remove(acceptedAssociation);
//
//									}
//								}
//							}
							associationsFactory.removecandidate(item.getText(3), item.getText(2), item.getText(1),
									item.getText(0), session);
							associationsFactory.createAssociation(item.getText(3), item.getText(2), item.getText(1),
									item.getText(0), session, false);
							button.setVisible(false);
							button.dispose();
							table.remove(indexItem);
							// TO DO: remove the association from hashmap in session.

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
	public void setFocus() {

	}

}
