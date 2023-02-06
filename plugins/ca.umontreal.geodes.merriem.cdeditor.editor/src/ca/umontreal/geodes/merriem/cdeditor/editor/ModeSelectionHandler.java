package ca.umontreal.geodes.merriem.cdeditor.editor;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.core.commands.AbstractHandler;
/**
 * mode selection:
 * 
 **/
public class ModeSelectionHandler extends AbstractHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
	String[] options = { "OnTrigger", "OnRequest", "acessAtEnd" };
	
		
		MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Assistance mode ", null,
			    "Select the assistance mode you plan to use", MessageDialog.ERROR, options, 0);
			int result = dialog.open();
			System.out.println(result);
			switch(result) {
			  case 1:
			    Services.mode=Mode.OnRequest;
			    break;
			  case 2:
				  Services.mode=Mode.acessAtEnd;
			    break;
			  case 0:
				  Services.mode=Mode.OnTrigger;
				    break;
			default: 
				Services.mode=Mode.OnRequest;
			}

				
		return null;
	}

	
	

	


}
