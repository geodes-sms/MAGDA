package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.sirius.diagram.ui.tools.internal.part.SiriusDiagramGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.util.Timer;

import javax.sound.midi.SysexMessage;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;
/**
 * The services class used by VSM.
 */
public class Services {
	public Services() {	};
    /**
    * See http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24 for documentation on how to write service methods.
    */
    public EObject myService(EObject self, String arg) {
       // TODO Auto-generated code
      return self;
    }
    


    public EObject getRecommendationConcepts(EObject m) {
    	System.out.print("am I in services ? ");

    	/*Timer timer = new Timer();
    
		
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if (editorPart != null) {		
			if (editorPart instanceof DiagramEditor) {
				EditPartViewer viewer = ((DiagramEditor) editorPart).getDiagramEditPart().getViewer();
				if (viewer instanceof SiriusDiagramGraphicalViewer) {
					org.eclipse.gef.KeyHandler keyHandler = viewer.getKeyHandler();
					if (keyHandler == null) {
						keyHandler = new org.eclipse.gef.KeyHandler();
					}
					keyHandler.put(KeyStroke.getKeyStroke(SWT.TAB),  print());
					viewer.setKeyHandler(keyHandler);
					
					viewer.setKeyHandler(keyHandler);
				}								
			}
		}*/
		
    	return m;
    }
    
}
