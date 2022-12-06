package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class ConceptsPrediction implements IConceptsPrediction {

	@Override
	public List<HashMap<String, String>> run(EObject rootModel, Model model) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		List<String[]> predictionLists = new ArrayList<String[]>();
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
			Clazz inputClass = (Clazz) rootModel;
			input = inputClass.getName();
			className = input;
			classNames.add(inputClass.getName());
			/**
			 * heuristic/strategy : what to send to GPT3, use random , TO DO: update use
			 * loop
			 **/

			for (int z = 0; z < classesInModel.size(); z++) {
				input = className;
				if (!classesInModel.get(z).getName().equals(className)) {
					if (input != "") {
						input = input.concat(",").concat(classesInModel.get(z).getName());

					}
				}
				Prompt concpetsPrompt = new ConceptsPrompt(input, "\n", ",");
				concpetsPrompt.setPrompt();
				String[] arrayAssociationName = concpetsPrompt.run(20, 0.7, "text-davinci-002");
				// HashMap<String, String> item = new HashMap<String, String>();
				results.add(new HashMap<String, String>());
				for (int i = 0; i < arrayAssociationName.length; i++) {

					// key = value (because it's interface ...)
					results.get(0).put(arrayAssociationName[i], arrayAssociationName[i]);
					System.out.print(arrayAssociationName[i]);

				}
				System.out.println("--------");
				predictionLists.add(arrayAssociationName);

			}

		}

		// add a loop concat with all the rest of classes in canvas !
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i = 0; i < predictionLists.size(); i++) {
			for (int j = 0; j < predictionLists.get(i).length; j++) {
				if (!result.keySet().contains(predictionLists.get(i)[j])) {
					result.put(predictionLists.get(i)[j], 0);
				}
				result.merge(predictionLists.get(i)[j], 1, Integer::sum);
			}
		}
		List<HashMap<String, String>> convertedResults = new ArrayList<HashMap<String, String>>();

		Map copy = result.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString()));
		HashMap<String, String> temp = new HashMap<String, String>(copy);
		convertedResults.add(temp);

		return convertedResults;

	}

}
