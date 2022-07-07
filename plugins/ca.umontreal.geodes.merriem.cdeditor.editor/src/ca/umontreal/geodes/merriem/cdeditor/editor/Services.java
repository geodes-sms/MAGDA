package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.model.business.internal.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.diagram.ui.tools.internal.part.SiriusDiagramGraphicalViewer;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorReference;
import org.osgi.framework.ServiceException;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.sound.midi.SysexMessage;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;
/**
 * The services class used by VSM.
 */
public class Services {
	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";

	public Services() {	};
    /**
    * See http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24 for documentation on how to write service methods.
    */
    public EObject myService(EObject self, String arg) {
       // TODO Auto-generated code
      return self;
    }
    
    protected Model getModel() {
        IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();

        IEditorReference editor = null;

        for (IEditorReference iEditorReference : editorReferences) {
            EditorReference editRef = (EditorReference) iEditorReference;
            if (editRef.getDescriptor().getId().equalsIgnoreCase(SIRIUS_DIAGRAM_EDITOR_ID)) {
                editor = editRef;
                break;
            }
        }

        if (editor == null) {
            System.out.println("No process found.");
            return null;
        }

        EObject input = null;

        try {
            IEditorInput editorInput = editor.getEditorInput();
            if (editorInput instanceof SessionEditorInput) {
                input = ((SessionEditorInput) editorInput).getInput();
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EObject model = null;
 
        if (input instanceof Diagram) {
            EObject element = ((Diagram) input).getElement();
            if (element instanceof DSemanticDiagramSpec) {
                model = ((DSemanticDiagramSpec) element).getTarget();
            }
        }
        
        return (Model) model;
    }
  
    public void createAttribute(String Name,String Type,String containerName) {
		try {
			URI sessionResourceURI = URI.createFileURI("/home/meriem//editorCD/class-diagram-editor/testFolder/representations.aird");

			
			Session createdSession = SessionManager.INSTANCE.getSession(sessionResourceURI, new NullProgressMonitor());
			createdSession.open(new NullProgressMonitor());

			DAnalysis root = (DAnalysis) createdSession.getSessionResource().getContents().get(0);
			DView dView = root.getOwnedViews().get(0);
			
			
			TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			
		    CommandStack stack = domain.getCommandStack();
			
			RecordingCommand cmd =  new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					Model model = getModel();
					MetamodelFactory metamodelFactory = ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory.eINSTANCE;
					
					
					 
				    AttributeImpl newAttribute=(AttributeImpl) metamodelFactory.createAttribute();
						   newAttribute.setName(Name); 
						   List<Clazz> classes = model.getClazz(); 
						   for(int i=0 ; i<classes.size(); i++){
							   if (classes.get(i).getName()==containerName) {
								   model.getClazz().get(i).getAttributes().add(newAttribute);
								   break; 
							   }
						   }
						   
				
						    
						
						
					
					//model.eContents().add(newClazz);
					
					//refresh Model
					DRepresentation represnt = null;
					for(DRepresentationDescriptor descrp : dView.getOwnedRepresentationDescriptors()) {
					represnt = descrp.getRepresentation();
					
					}
					DialectManager.INSTANCE.refresh(represnt, new NullProgressMonitor());

				}						
				
			};
			
			stack.execute(cmd);
			}
			catch (ServiceException e) {
				e.printStackTrace();
			}	
	

	}

    
    public EObject getAttributePrediction(EObject m) {
    	String NodeName = m.toString().split(" ", 2)[1];
    	System.out.println("DOuble clicked , predictAttibutes ");
    	System.out.println(NodeName);
    	System.out.println("attributes : " );
    	List<String> attributes = new ArrayList<String>() ;
    	for (int i =0 ; i< m.eContents().size(); i++) {
    		
    		attributes.add(m.eContents().get(i).toString().split(" ", 3)[2].split(":", 3)[0]);
    		
    		
    	};
    	for (int i =0 ; i< attributes.size(); i++) {
    		
    		System.out.println(attributes.get(i));
    		
    		
    	};
    	return m;
    }
    
}
