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

public class AssociationsPrediction implements IAssociationsPrediction {

	@Override
	public List<HashMap<String, String>> run(EObject rootModel, Model model) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		String className;

		List<Clazz> classesInModel = model.getClazz();
	
		if (rootModel instanceof Clazz) {
			className = rootModel.toString().split(":")[1];
			className = className.substring(1, className.length() - 1);
			List<Association> Associations = new ArrayList<Association>();
			Associations = model.getAssociation();
			List<String> classesAssociatedTo = new ArrayList<String>();
			for (int i = 0; i < classesInModel.size(); i++) {
				if (classesInModel.get(i).getName().equals(className)) {

					if (classesInModel.get(i).getIsMember() != null) {
						classesAssociatedTo.add(classesInModel.get(i).getIsMember().getName().replaceAll("\\s+", ""));
					}
					if (classesInModel.get(i).getSpecializes() != null) {
						classesAssociatedTo.add(classesInModel.get(i).getSpecializes().getName().replaceAll("\\s+", ""));
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
						classesAssociatedTo.add(Associations.get(j).getSource().getName().replaceAll("\\s+", ""));
					}
				}
			}

			for (int i = 0; i < classesInModel.size(); i++) {

				if (!className.replaceAll("\\s+", "").equals(classesInModel.get(i).getName())) {

					HashMap<String, String> itemAssociation = new HashMap<String, String>();
					if (!classesAssociatedTo.contains(classesInModel.get(i).getName().replaceAll("\\s+", ""))) {
						String input = classesInModel.get(i).getName().concat(" , ").concat(className);

						Prompt associtaionsNamePrompt = new AssociationNamePrompt(input, "\n", "=>");
						associtaionsNamePrompt.setPrompt();
						String[] arrayAssociationName = associtaionsNamePrompt.run(1, 0.7, "text-davinci-002");
						String Type = "";

						Prompt associtaionsTypePrompt = new AssociationTypePrompt(input, "\n", "=>");
						associtaionsNamePrompt.setPrompt();
						String[] arrayAssociationType = associtaionsTypePrompt.run(1, 0.7, "text-davinci-002");
						Type = arrayAssociationType[0];
						System.out.println(Type);

						itemAssociation.put("Name", arrayAssociationName[0].replaceAll("\\s+", ""));
						itemAssociation.put("Type", Type.replaceAll("\\s+", ""));
						itemAssociation.put("Target", classesInModel.get(i).getName());
						itemAssociation.put("Source", className);
						results.add(itemAssociation);

					}
				}

			}
		}

		return results;

	}

}
