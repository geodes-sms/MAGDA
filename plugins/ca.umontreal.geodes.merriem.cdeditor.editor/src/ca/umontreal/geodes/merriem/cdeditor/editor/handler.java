package ca.umontreal.geodes.merriem.cdeditor.editor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorReference;

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

        if (input instanceof Model) {
        	model = input;
            
        }

        Assert.isTrue(model instanceof Model, "Fatal error: model not instance of Model.");

        return (Model) model;
    }
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	System.out.println("execute");
    	Model m = getModel();
    	System.out.println("execute");
		System.out.println(m.getClazz());

		return null;
    }
}