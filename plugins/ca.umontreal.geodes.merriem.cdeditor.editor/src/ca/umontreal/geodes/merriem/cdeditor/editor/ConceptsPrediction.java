package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.eclipse.emf.ecore.EObject;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class ConceptsPrediction implements IConceptsPrediction{

	@Override
	public List<HashMap<String, String>> run (EObject rootModel, Model model) {
		List<String> classNames = new ArrayList<String>();
		List<String> AllclassNames = new ArrayList<String>();
		List<Clazz> classesInModel = model.getClazz();
		String className = "";
		List<ClazzCondidate> classeCondidateInModel = model.getClazzcondidate();

		for (int i = 0; i < classesInModel.size(); i++) {
			AllclassNames.add(classesInModel.get(i).getName());
		}
		for (int i = 0; i < classeCondidateInModel.size(); i++) {
			AllclassNames.add(classeCondidateInModel.get(i).getName());
		}
		String input = "";
		if (rootModel instanceof Model) {
			for (int i = 0; i < classesInModel.size(); i++) {
				input = input.concat(",").concat(classesInModel.get(i).getName());
				classNames.add(classesInModel.get(i).getName());
			}
		} else if (rootModel instanceof Clazz) {
			System.out.print("from one class");
			Clazz inputClass = (Clazz) rootModel;
			input = inputClass.getName();
			className = input;
			classNames.add(inputClass.getName());
			// heuristic: what to send to GPT3
			Random rand = new Random();
			String randomElement = AllclassNames.get(rand.nextInt(AllclassNames.size()));
			input = input.concat(",").concat(randomElement);

		}
		return null;
		
	}

}
