package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class JobAssociations extends Job {

	private Services services;
	private Model model;

	private Session session;
	public JobAssociations(String name, Services services, Model model, Session session) {
		super(name);
		try {
			this.services = services;
			this.model = model;
			this.session = session;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-ge nerated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		try {
			List<Clazz> classes = model.getClazz();
			TimeUnit.SECONDS.sleep(13);
			System.out.println("Association prediction launched");
			IAssociationsPrediction associationsPrediction = new AssociationsPrediction();

			for (int i = 0; i < classes.size(); i++) {
				String className = classes.get(i).getName();
				if (Services.relatedAssociations == null) {
					Services.relatedAssociations = new HashMap<String, List<String>>();
				}
				if (!Services.relatedAssociations.containsKey(classes.get(i).getName())) {

					List<HashMap<String, String>> res = associationsPrediction.run(className, null, model);

					List<String> items = new ArrayList<String>();
					String item;
					for (int j = 0; j < res.size(); j++) {
						if (!(res.get(j).get("Type").replaceAll("\\s+", "").equals(""))
								&& (!(res.get(j).get("Type").replaceAll("\\s+", "").equals("no")))) {

							item = res.get(j).get("Type") + ":[" + res.get(j).get("Source") + ","
									+ res.get(j).get("Target") + "]; Name => " + res.get(j).get("Name");
							items.add(item);
							AssociationsFactory associationsFactory = new AssociationsFactory();

							associationsFactory.createAssociationcandidate(res.get(j).get("Type"),
									res.get(j).get("Name"), res.get(j).get("Target"), res.get(j).get("Source"), session,
									model);

							Services.relatedAssociations.put(className, items);

						}
					}
				}
			}
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					Services.refreshAssociationsView();

				}
			});
			Listener.AssociationJobLaunched = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ASYNC_FINISH;
	}

}
