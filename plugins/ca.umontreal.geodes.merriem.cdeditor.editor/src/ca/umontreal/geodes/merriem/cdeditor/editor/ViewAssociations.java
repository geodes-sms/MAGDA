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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationCandidateImpl;
import kotlin.Pair;

public class ViewAssociations extends ViewPart {

	private Services services;
	private AssociationsFactory associationsFactory;
	public Composite parent;
	public static ProgressBar progressBar;

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

			// AssociationsFactory.cleanOperationCandidate(services.getSession());
			List<AssociationCandidate> operationsCandidate = this.services.getModel().getOperation();

			Composite composite = new Composite(parent, SWT.NONE);
			composite.setVisible(true);
			composite.setLayout(new GridLayout());
			composite.layout(true, true);
			GridData compositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
			composite.setLayoutData(compositeData);
			// Create the table control
			Table table = new Table(composite, SWT.BORDER);

			// Table table = new Table(parent, SWT.BORDER);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setLayout(new GridLayout());

			GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
			tableData.heightHint = 600;
			table.setLayoutData(tableData);

			Composite progressBarComposite = new Composite(composite, SWT.NONE);
			progressBarComposite.setLayout(new GridLayout());
			GridData progressBarCompositeData = new GridData(SWT.FILL, SWT.TOP, true, false);
			progressBarComposite.setLayoutData(progressBarCompositeData);
			progressBarComposite.layout(true, true);
			this.progressBar = new ProgressBar(progressBarComposite, SWT.HORIZONTAL | SWT.INDETERMINATE);
			GridData progressBarData = new GridData(SWT.FILL, SWT.TOP, true, false);
			this.progressBar.setLayoutData(progressBarData);
			this.progressBar.setVisible(true);

			parent.layout(true, true);

			String[] titles = { "Name", "Source", "Target", "Type of association", "Draw in Canvas?", "score" };
			for (String title : titles) {
				TableColumn column = new TableColumn(table, SWT.CHECK);
				column.setText(title);
			}

			HashMap<AssociationCandidate, Integer> selectedOperations = new HashMap<>();

			for (int i = 0; i < operationsCandidate.size(); i++) {
				Boolean find = false;
				for (AssociationCandidate associationCandidateKey : selectedOperations.keySet()) {
					if (operationsCandidate.get(i).getSource() == null
							|| operationsCandidate.get(i).getTarget() == null) {
						continue;
					}
					if (associationCandidateKey.getSource() != null && associationCandidateKey.getTarget() != null) {
						if (operationsCandidate.get(i).getSource().getName()
								.equals(associationCandidateKey.getSource().getName())
								&& operationsCandidate.get(i).getTarget().getName()
										.equals(associationCandidateKey.getTarget().getName())) {
							find = true;
							int score = selectedOperations.get(associationCandidateKey);
							selectedOperations.put(associationCandidateKey, score + 1);

						}
					}

				}
				if (!find) {
					selectedOperations.put(operationsCandidate.get(i), 1);
				}
			}

			for (AssociationCandidate associationCandidateKey : selectedOperations.keySet()) {
				String type = associationCandidateKey.getType();
				if (type != "") {
					type = type.substring(0, 1).toUpperCase() + type.substring(1);
				}
				if (associationCandidateKey.getSource() == null) {
					continue;
				}
				if (associationCandidateKey.getTarget() == null) {
					continue;
				}
				String source = associationCandidateKey.getSource().getName();
				if (source != "") {
					source = source.substring(0, 1).toUpperCase() + source.substring(1);
				}

				String target = associationCandidateKey.getTarget().getName();
				if (target == null) {
					continue;
				}

				if (target != "") {
					target = target.substring(0, 1).toUpperCase() + target.substring(1);
				}
				TableItem item = new TableItem(table, SWT.CHECK);
				item.setText(0, associationCandidateKey.getName());
				item.setText(1, source);
				item.setText(2, target);

				item.setText(3, type);
				item.setText(5, selectedOperations.get(associationCandidateKey).toString());

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
				button.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, true));
				editor.grabHorizontal = true;
				editor.minimumWidth = 50;
				button.setText("Accept");
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
							Services.loggerServices.info("Accept Association From view");
							Session session = services.getSession();
							String type = item.getText(3);
							String name = item.getText(0);
							String target = item.getText(2);
							String source = item.getText(1);
							button.setVisible(false);
							button.dispose();
							table.remove(indexItem);
							associationsFactory.removecandidate(type, name, target, source, session);
							associationsFactory.createAssociation(type, name, target, source, session, false);

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

	public static ProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public void setFocus() {

	}

}
