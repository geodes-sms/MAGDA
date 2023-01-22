package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.Comparator;
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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;

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

		Control[] children = parent.getChildren();
		for (Control child : children) {
			if (!child.isDisposed()) {
				child.dispose();
			}
		}

		List<ClazzCondidate> classCondidateInModel = this.services.getModel().getClazzcondidate();
		Table table = new Table(parent, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		parent.layout(true, true);
		// parent.pack();

		String[] titles = { "Suggestion", "score", "Add to Canvas?" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.CHECK);
			column.setText(title);
		}

		// classCondidateInModel.sort(Comparator.comparing(ClazzCondidate::getConfidence).reversed());
		List<ClazzCondidate> sortedClazzCondidate = classCondidateInModel.stream()
				.sorted(Comparator.comparing(ClazzCondidate::getConfidence).reversed()).collect(Collectors.toList());

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
					Session session = services.getSession();
					String acceptedClassName = item.getText(0);
					button.setVisible(false);
					button.dispose();
					table.remove(indexItem);
					conceptsFactory.createClass(acceptedClassName, session);
					conceptsFactory.deletetClassCondidate(acceptedClassName, session);

				}

				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});

		}

	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		createContents();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
