package ca.umontreal.geodes.merriem.cdeditor.editor;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import java.util.logging.Handler;
import java.util.logging.Logger;

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
		
	String[] options = { "Automatic complete", "Complete on Request", "Complete at end" };
	
		
		MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Assistance mode ", null,
			    "Select the assistance mode you plan to use", MessageDialog.QUESTION, options, 0);
			int result = dialog.open();
			System.out.println(result);
			switch(result) {
			  case 1:
			    Services.mode=Mode.OnRequest;
			    break;
			  case 2:
				  Services.mode=Mode.assessAtEnd;
			    break;
			  case 0:
				  Services.mode=Mode.OnTrigger;
				    break;
			default: 
				Services.mode=Mode.OnRequest;
			}

				Services.refreshAssociationsView();
				Services.refreshSuggestionsView();
		return null;
	}

	
	

	


}
