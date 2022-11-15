package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Association;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class AssociationPredictionImpl implements AssociationPrediction {

	@Override
	public List<HashMap<String, String>> run(EObject rootModel, Model model, Properties config) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		String className;
		String res = "";

		List<Clazz> classesInModel = model.getClazz();
		for (int j = 0; j < classesInModel.size(); j++) {
			System.out.println(classesInModel.get(j).getName());
		}
		String scriptLocation = config.getProperty("scriptlocation");
		String pythonCommand = config.getProperty("pythoncommand");
		if (rootModel instanceof Clazz) {
			className = rootModel.toString().split(":")[1];
			className = className.substring(1, className.length() - 1);
			List<Association> Associations = new ArrayList<Association>();
			Associations = model.getAssociation();
			List<String> classesAssociatedTo = new ArrayList<String>();
			for (int i = 0; i < classesInModel.size(); i++) {
				if (classesInModel.get(i).getName().equals(className)) {

					if (classesInModel.get(i).getIsMember() != null) {
						classesAssociatedTo.add(classesInModel.get(i).getIsMember().getName());
					}
					if (classesInModel.get(i).getSpecializes() != null) {
						classesAssociatedTo.add(classesInModel.get(i).getSpecializes().getName());
					}
				}
			}
			for (int j = 0; j < Associations.size(); j++) {
				if (Associations.get(j).getSource() != null) {
					if (Associations.get(j).getSource().getName().equals(className)) {

						classesAssociatedTo.add(Associations.get(j).getTarget().getName().replaceAll("\\s+", ""));
					}
				}
				if (Associations.get(j).getTarget() != null) {

					if (Associations.get(j).getTarget().getName().equals(className)) {
						classesAssociatedTo.add(Associations.get(j).getSource().getName());
					}
				}
			}

			for (int i = 0; i < classesInModel.size(); i++) {

				if (!className.replaceAll("\\s+", "").equals(classesInModel.get(i).getName())) {
					HashMap<String, String> itemAssociation = new HashMap<String, String>();

					if (!classesAssociatedTo.contains(classesInModel.get(i).getName().replaceAll("\\s+", ""))) {

						String input = classesInModel.get(i).getName().concat(" , ").concat(className);
						try {
							Process P = new ProcessBuilder(pythonCommand, scriptLocation + "predictAssociation.py",
									input).start();

							BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
							BufferedReader stdError = new BufferedReader(new InputStreamReader(P.getErrorStream()));

							String s;

							while ((s = stdInput.readLine()) != null) {

								res = s;
								itemAssociation.put("Type", res.replaceAll("\\s+", ""));
							}

							while ((s = stdError.readLine()) != null) { // add logger !
								System.out.println(s);
							}
							itemAssociation.put("Target", classesInModel.get(i).getName());
							itemAssociation.put("Source", className);
							results.add(itemAssociation);
						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}

			}
		}

		return results;

	}

}
