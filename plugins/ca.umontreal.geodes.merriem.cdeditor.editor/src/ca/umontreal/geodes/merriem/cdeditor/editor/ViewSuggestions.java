package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

public class ViewSuggestions extends ViewPart {
	private Services services;
	Composite parent;
	// Table table;

	public ViewSuggestions() {
		try {
			this.services = new Services();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.table = new Table(parent, SWT.BORDER);

		// TODO Auto-generated constructor stub
	}

	public void createContents() {

		//URI sessionResourceURI = URI	.createFileURI("platform:/resource/test/representations.aird");

		parent.pack();
		parent.layout(true);

		List<ClazzCondidate> classeCondidateInModel = this.services.getModel().getClazzcondidate();
		Table table = new Table(parent, SWT.BORDER);
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
			button.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// System.out.println("accepted clicked");

				}

				@Override
				public void mouseDown(MouseEvent e) {
					//Session createdSession = SessionManager.INSTANCE.getSession(sessionResourceURI, new NullProgressMonitor());
					Session session = services.getSession(); 
		            //SessionManager.INSTANCE.add(session);
					services.createClass(item.getText(0), session);
					//services.deletetClassCondidate(item.getText(0),session);
					table.remove(indexItem);
					button.setVisible(false);

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
	public void createPartControl(Composite parent) {
		this.parent = parent;

		createContents();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
