package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;

public class ViewSuggestions extends ViewPart {
	private Services services;

	public ViewSuggestions() throws Exception {
		this.services = new Services();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// Text text = new Text(parent, SWT.BORDER);
		// text.setText("Imagine a fantastic user interface here");
		List<ClazzCondidate> classeCondidateInModel = this.services.getModel().getClazzcondidate();
		final Table table = new Table(parent, SWT.BORDER);
		// table.setItemCount(5);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = { "Suggestion", "score", "Add to Canvas?" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.CHECK);
			column.setText(title);
		}
		for (int i = 0; i < classeCondidateInModel.size(); i++) {
			TableItem item = new TableItem(table, SWT.CHECK);
			item.setText(0, classeCondidateInModel.get(i).getName());
			item.setText(1, Integer.toString(classeCondidateInModel.get(i).getConfidence()));

		}
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		TableItem[] items = table.getItems();

		for (int i = 0; i < items.length; i++) {
			int indexItem = i ; 
			TableEditor editor = new TableEditor(table);

			TableItem item = items[i];

			Button button = new Button(table, SWT.PUSH);

			button.setText("Accept");

			button.setSize(80, 16);
			button.pack();

			editor.minimumWidth = button.getSize().x;

			editor.horizontalAlignment = SWT.LEFT;

			editor.setEditor(button, item, 2);
			button.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					//System.out.println("accepted clicked");

				}

				@Override
				public void mouseDown(MouseEvent e) {
					table.remove(indexItem);

				}

				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});

			// button.setToolTipText("Fix this session");

		

		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
