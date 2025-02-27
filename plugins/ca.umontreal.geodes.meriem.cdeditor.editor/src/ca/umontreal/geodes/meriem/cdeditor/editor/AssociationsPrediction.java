package ca.umontreal.geodes.meriem.cdeditor.editor;

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
	public List<HashMap<String, String>> run(String className, EObject rootModel, Model model) {
		try {

			List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
			// String className;

			List<Clazz> classesInModel = model.getClazz();

			if (rootModel instanceof Clazz || rootModel == null) {

				List<Association> Associations = new ArrayList<Association>();
				Associations = model.getAssociation();
				List<String> classesAssociatedTo = new ArrayList<String>();
				for (int i = 0; i < classesInModel.size(); i++) {
					if (classesInModel.get(i).getName().equals(className)) {

						if (classesInModel.get(i).getIsMember() != null) {
							classesAssociatedTo
									.add(classesInModel.get(i).getIsMember().getName().replaceAll("\\s+", ""));
						}
						if (classesInModel.get(i).getSpecializes() != null) {
							classesAssociatedTo
									.add(classesInModel.get(i).getSpecializes().getName().replaceAll("\\s+", ""));
						}
					}
				}

				for (int j = 0; j < Associations.size(); j++) {
					if (Associations.get(j).getSource() != null && Associations.get(j).getTarget() != null) {
						if (Associations.get(j).getSource().getName().equals(className)) {
							classesAssociatedTo.add(Associations.get(j).getTarget().getName().replaceAll("\\s+", ""));
						}

						if (Associations.get(j).getTarget().getName().equals(className)) {
							classesAssociatedTo.add(Associations.get(j).getSource().getName().replaceAll("\\s+", ""));
						}
					}
				}

				for (int i = 0; i < classesInModel.size(); i++) {
					if (classesInModel.get(i).getName() != null) {
						if (!className.replaceAll("\\s+", "").equals(classesInModel.get(i).getName())) {

							HashMap<String, String> itemAssociation = new HashMap<String, String>();
							if (!classesAssociatedTo.contains(classesInModel.get(i).getName().replaceAll("\\s+", ""))) {
								String input = classesInModel.get(i).getName().concat(" , ").concat(className);

								Prompt associtaionsNamePrompt = new AssociationNamePrompt(input, "\n", "=>");
								associtaionsNamePrompt.setPrompt();
								String[] arrayAssociationName = associtaionsNamePrompt.run(1, 0.7, Services.usedModel);
								String type = "";

								Prompt associtaionsTypePrompt = new AssociationTypePrompt(input, "\n", "=>");
								associtaionsNamePrompt.setPrompt();
								String[] arrayAssociationType = associtaionsTypePrompt.run(1, 0.7, Services.usedModel);
								type = arrayAssociationType[0];
								System.out.println(type);
								String target = classesInModel.get(i).getName();
								String source = className;

								if (type == "inheritance"
										|| arrayAssociationName[0].replaceAll("\\s+", "").equalsIgnoreCase("is")) {
									Prompt inheritancePrompt = new InheritancePrompt(input, "\n", "=> ");
									String[] resultInheritance = inheritancePrompt.run(1, 0.7, Services.usedModel);
									
									if (!(target.replaceAll("\\s+", "")
											.equalsIgnoreCase(resultInheritance[0].replaceAll("\\s+", "")))) {
										String temp = source;
										source = target;
										
										target = temp;

									}
									System.out.println("new target : super  : " + target);
								}

								itemAssociation.put("Name", arrayAssociationName[0].replaceAll("\\s+", ""));
								itemAssociation.put("Type", type.replaceAll("\\s+", ""));
								itemAssociation.put("Target", target);
								itemAssociation.put("Source", source);
								results.add(itemAssociation);

							}
						}
					}

				}

			}
			return results;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

}
