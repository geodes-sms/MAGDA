package ca.umontreal.geodes.merriem.cdeditor.editor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.impl.DDiagramImpl;
import org.eclipse.sirius.diagram.model.business.internal.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.EditorReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;



public  class handler extends AbstractHandler {
	
	private Services services; 
	/**
	 * The constructor.
	 */
	public handler() {
		this.services=new Services();
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	/*public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("I am in handler "); 
		services.getRecommendation(null);
		
		return null;
	}*/
	
	protected static final String SIRIUS_DIAGRAM_EDITOR_ID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";

  

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
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	System.out.println("execute");
    	Model m = getModel();
    	List<Clazz> classes = new ArrayList<Clazz>();
    	classes=m.getClazz();
    	String input=""; 
    	for(int i=0;i<classes.size();i++){
    	    input= input.concat(",").concat(classes.get(i).getName());
    	}
    	List<String> Concepts= new ArrayList<String>();
      	Concepts.add("Cancel");
      	 
    	   Process p;
   		try {
   		
   			Process P = new ProcessBuilder("python3", "/home/meriem/editorCD/class-diagram-editor/scripts/predictConcepts.py", input).start();

   		
   	    	String line = "";
   			BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
   	        BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));
   	        
   	        String s;
   	        while ((s = stdInput.readLine()) != null) {
   	            Concepts.add(s);
   	        }

   	        while ((s = stdError.readLine()) != null) {
   	  
   	            System.out.println(s);
   	        }
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
    	
   	  	

   	 String[] arrayConcepts = Concepts.toArray(new String[0]);
   	for(int i=0;i<Concepts.size();i++){
	    System.out.println(arrayConcepts[i]);
	}
   	 IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		MessageDialog dialog = new MessageDialog(window.getShell(), "Choose relevant class", null,
			    "My message", MessageDialog.QUESTION, arrayConcepts,0);
			int result = dialog.open();
			System.out.println("chosen");
			System.out.println(arrayConcepts[result]);
		return null;
    }
}