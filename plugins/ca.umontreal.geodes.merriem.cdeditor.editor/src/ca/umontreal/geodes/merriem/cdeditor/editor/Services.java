package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;

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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorReference;
import org.osgi.framework.ServiceException;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


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
						   newAttribute.setType(Type);
						   List<Clazz> classes = model.getClazz(); 
						   
						   for(int i=0 ; i<classes.size(); i++){
							  
							   String Cname=classes.get(i).getName().replaceAll("\\s+","");
						
							   if (containerName.equals(Cname) ) {
								   EList<Attribute> attributesName= model.getClazz().get(i).getAttributes();
								boolean attributeExist = false; 
								   for (int j=0 ; j<attributesName.size();j++) {
									  
										if ( attributesName.get(j).getName().replaceAll("\\s+","").equals(Name.replaceAll("\\s+",""))) {
											attributeExist=true; 
											System.out.println("found attribute skip !"); 
											System.out.println(Name);
											break ;
											}
										}	   
								  if (! attributeExist) {
								 
								
								   model.getClazz().get(i).getAttributes().add(newAttribute);
								   
								   break; 
								   }
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

    
    public EObject getAttributePrediction(EObject node) {
    	String NodeName = node.toString().split(" ", 2)[1];
    	NodeName=NodeName.replaceAll("\\s+","");
    	System.out.print("DOuble clicked , predictAttibutes for :  ");
    	System.out.println(NodeName);
    	List<String> attributes = new ArrayList<String>() ;
    	
    	for (int i =1 ; i< node.eContents().size(); i++) {
    		
    		attributes.add(node.eContents().get(i).toString().split(" ", 3)[2].split(":", 3)[0]);
    		
    	};
    	String input;
    	if (node.eContents().size()>1) {
	    	input=attributes.get(0);
	    	
	    	for (int i =1 ; i< attributes.size(); i++) {
	    		
	    	    input= input.concat(",").concat(attributes.get(i));
	
	    		
	    	};
	    }else
	    	input="";
    
    	List<String> Results= new ArrayList<String>();
    	
      	 
    	   Process p;
   		try {
   		
   			Process P = new ProcessBuilder("python3", "/home/meriem/editorCD/class-diagram-editor/scripts/predictAttributes.py", NodeName,input).start();

   			BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
   	        BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));
   	        
   	        String s;
   	        while ((s = stdInput.readLine()) != null) {
   	        	Results.add(s);
   	        }

   	        while ((s = stdError.readLine()) != null) {
   	        	//add logger ! 
   	            System.out.println(s);
   	        }
   		} catch (IOException e) {
   			e.printStackTrace();
   		}

   	String[] arrayAttributes = Results.toArray(new String[0]);
   	
   	//print recieved attributes from python script
   	for(int i=0;i<Results.size();i++){
	    System.out.println(arrayAttributes[i]);
	   
	    	createAttribute(arrayAttributes[i], "", NodeName);
	    
	    
	    
	}
   	
    	return node;
    }
    
}
