package ca.umontreal.geodes.merriem.cdeditor.editor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
public class handler extends AbstractHandler {


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
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Test",
				"Hello, Eclipse world");

		services.getRecommendation(null);
		
		return null;
	}
}