package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class ViewAssociations extends ViewPart {

	public ViewAssociations() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		//Canvas clazzSource = new Canvas(parent, SWT.BORDER);

		//GridData gridData = new GridData(GridData.FILL_BOTH);

		//clazzSource.setLayoutData(gridData);
		/*Display display = Display.getCurrent();

		Image image = new Image(display,
				"/home/meriem/editorCD/class-diagram-editor/plugins/ca.umontreal.geodes.merriem.cdeditor.editor/src/ca/umontreal/geodes/merriem/cdeditor/editor/pattern.png");
		GC gc = new GC(image);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawText("I've been drawn on", 0, 0, true);
		gc.dispose();*/

		// item.set

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
