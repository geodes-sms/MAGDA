package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class PredictionMode implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Services services = new Services();
			Session session = services.getSession();
			Model model = services.getModel();
			EList<Clazz> classes = model.getClazz();

			/***
			 * First Thread - Job : Predict related concepts Predict it's attributes ?
			 **/

			JobConcepts jobConcepts = new JobConcepts("Attributes prediction", services, model, session,false);

			jobConcepts.setPriority(Job.SHORT);
			jobConcepts.schedule();

			/***
			 * Second Thread - Job : Predict related attributes for concepts added
			 **/

			JobAttributes jobAttributes = new JobAttributes("Attributes prediction", services, model);
			// I changed the place of this to out of the Job

			jobAttributes.setPriority(Job.SHORT);
			jobAttributes.schedule();

			/***
			 * Third Thread - Job : Predict related associations for concepts added
			 **/

			Job jobAssociations = new JobAssociations("Associations prediction", services, model, session);

			jobAssociations.setPriority(Job.SHORT);
			jobAssociations.schedule();
			System.out.println("Association prediction finished");
			// jobAssociations.cancel();

		} catch (

		Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
