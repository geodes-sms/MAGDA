package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobAttributes extends Job {

	private Services services;
	private Model model;

	public JobAttributes(String name, Services services, Model model) {
		super(name);
		try {
			this.services = services;
			this.model = model;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-ge nerated constructor stub
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {

			// Model model = this.services.getModel();

			TimeUnit.SECONDS.sleep(10);
			EList<Clazz> classes = model.getClazz();
			HashMap<String, String> typeAttributes = new HashMap<String, String>();
			IAttributesPrediction attributesPredcition = new AttributesPrediction();
			if (Services.classAttributes == null) {
				Services.classAttributes = new HashMap<String, HashMap<String, String>>();
			}
			for (int i = 0; i < classes.size(); i++) {
				if (!Services.classAttributes
						.containsKey(classes.get(i).getName().replaceAll("\\s+", "").toLowerCase())) {
					typeAttributes = attributesPredcition.run(null, classes.get(i).getName().toLowerCase(), model,
							false);
					Services.classAttributes.put(classes.get(i).getName().toLowerCase(), typeAttributes);
				}
			}
			System.out.println("attributes  started ");
			Listener.AttributesJobLaunched = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ASYNC_FINISH;
	}

}