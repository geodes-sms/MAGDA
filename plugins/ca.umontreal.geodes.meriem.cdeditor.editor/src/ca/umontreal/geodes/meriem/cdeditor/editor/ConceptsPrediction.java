package ca.umontreal.geodes.meriem.cdeditor.editor;

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
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class ConceptsPrediction implements IConceptsPrediction {

	@Override
	public List<HashMap<String, String>> run(String className, EObject rootModel, Model model) {
		try {
			List<String[]> predictionLists = new ArrayList<String[]>();
			List<String> AllclassNames = new ArrayList<String>();
			List<Clazz> classesInModel = model.getClazz();
			// String className = "";
			List<ClazzCandidate> classeCandidateInModel = model.getClazzcondidate();

			for (int i = 0; i < classesInModel.size(); i++) {
				AllclassNames.add(classesInModel.get(i).getName());
			}
			for (int i = 0; i < classeCandidateInModel.size(); i++) {
				AllclassNames.add(classeCandidateInModel.get(i).getName());
			}
			String input = "";
			// rootModel could be null (in cashing system)
			if (rootModel instanceof Model || rootModel == null) {
				System.out.println("In concepts prediction from all canvas");
				for (int i = 0; i < classesInModel.size(); i++) {
					if (!(classesInModel.get(i).getName() == null)) {
						input = input.concat(",").concat(classesInModel.get(i).getName());
					}
				}

				Prompt concpetsPrompt = new ConceptsPrompt(input, "\n", ",");
				concpetsPrompt.setPrompt();
				String[] arrayConceptsName = concpetsPrompt.run(8, 0.7, "text-davinci-002");

				predictionLists.add(arrayConceptsName);

			} else if (rootModel instanceof Clazz) {
				System.out.println("in concepts prediction from one class");

				Clazz inputClass = (Clazz) rootModel;
				input = inputClass.getName();
				className = input;
				/**
				 * heuristic/strategy : what to send to GPT3, use random , TO DO: update use
				 * loop, here we send all possible couples. then based on frequency we select
				 * top 3
				 **/

				for (int z = 0; z < classesInModel.size(); z++) {
					input = className;
					if (!classesInModel.get(z).getName().equals(className)) {
						if (input != "") {
							input = input.concat(",").concat(classesInModel.get(z).getName());
							Prompt concpetsPrompt = new ConceptsPrompt(input, "\n", ",");
							concpetsPrompt.setPrompt();

							String[] arrayConceptsName = concpetsPrompt.run(8, 0.7, Services.usedModel);
							predictionLists.add(arrayConceptsName);
							System.out.println(arrayConceptsName);

						}
					}
				}
			}

			// combine results and calculate frequecy

			Map<String, Integer> result = new HashMap<String, Integer>();
			for (int i = 0; i < predictionLists.size(); i++) {
				for (int j = 0; j < predictionLists.get(i).length; j++) {
					if (!result.keySet().contains(predictionLists.get(i)[j])) {
						result.put(predictionLists.get(i)[j], 0);
					}
					result.merge(predictionLists.get(i)[j], 1, Integer::sum);
				}
			}
			// we have this format because it is an interface.

			List<HashMap<String, String>> convertedResults = new ArrayList<HashMap<String, String>>();

			Map copy = result.entrySet().stream().sorted(Map.Entry.comparingByValue())
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString()));
			HashMap<String, String> temp = new HashMap<String, String>(copy);

			// first element only will be considered...
			convertedResults.add(temp);

			return convertedResults;
		} catch (Exception e) {
			
			System.out.println("Exception happened while  predicting Concepts ! ");
			System.out.println(e);
			return null;
		}
	}

}
