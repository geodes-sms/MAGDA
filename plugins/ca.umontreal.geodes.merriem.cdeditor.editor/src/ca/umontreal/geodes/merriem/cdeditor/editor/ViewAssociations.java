package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.List;

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

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.OperationCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.OperationCondidateImpl;

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
		this.parent = parent;

		createContents();

	}

	public void createContents() {
		// TODO Auto-generated method stub

		Control[] children = parent.getChildren();
		for (Control child : children) {
			if (!child.isDisposed()) {
				child.dispose();
			}
		}

		List<OperationCondidate> operationsCondidate = this.services.getModel().getOperation();
		Table table = new Table(parent, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		parent.layout(true, true);
		// parent.pack();
		String[] titles = { "Source", "Target", "Name", "Type of association", "Draw in Canvas?" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.CHECK);
			column.setText(title);
		}
		for (int i = 0; i < operationsCondidate.size(); i++) {
			TableItem item = new TableItem(table, SWT.CHECK);
			item.setText(0, operationsCondidate.get(i).getSource().getName());
			item.setText(1, operationsCondidate.get(i).getTarget().getName());
			item.setText(2, operationsCondidate.get(i).getName());
			item.setText(3, operationsCondidate.get(i).getType());
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
			button.setText("Draw");
			button.setSize(80, 16);
			button.pack();
			editor.minimumWidth = button.getSize().x;
			editor.horizontalAlignment = SWT.LEFT;
			editor.setEditor(button, item, 4);
			editor.layout();
			button.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// System.out.println("accepted clicked");
				}

				@Override
				public void mouseDown(MouseEvent e) {
					Session session = services.getSession();
					button.setVisible(false);
					button.dispose();
					table.remove(indexItem);
					associationsFactory.createAssociation(item.getText(3), item.getText(2), item.getText(1),
							item.getText(0), session);
					associationsFactory.removeCondidate(item.getText(3), item.getText(2), item.getText(1),
							item.getText(0), session);

				}

				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});

		}

	}

	@Override
	public void setFocus() {

	}

}
