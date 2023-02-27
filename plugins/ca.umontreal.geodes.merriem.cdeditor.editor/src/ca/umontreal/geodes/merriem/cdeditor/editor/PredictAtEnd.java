package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.*;
import java.lang.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.swt.widgets.Display;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class PredictAtEnd extends AbstractHandler {
	public Map<String, Integer> countFrequencies(List<String> list) {
		// hashmap to store the frequency of element
		Map<String, Integer> hm = new HashMap<String, Integer>();

		for (String i : list) {
			Integer j = hm.get(i);
			hm.put(i, (j == null) ? 1 : j + 1);
		}

		return hm;
	}

	public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

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

		/***
		 * Predict related concepts and their attributes; Create classes with top score
		 * and a percentage 25% of the total suggested. Put all suggested attributes in
		 * that class predict association and create them only 30% and try to avoid
		 * antipatterns !
		 * 
		 **/
		if (Services.mode == Mode.assessAtEnd) {
			try {
				Services services;
				List<String> types = new ArrayList<>(List.of("int", "string", "float", "char", "boolean", "double",
						"byte", "array", "object", "collection"));
				services = new Services();
				AttributesFactory attributesFactory = new AttributesFactory();
				Session session = services.getSession();
				Model model = services.getModel();
				EList<Clazz> classes = model.getClazz();

				System.out.println("Complete missing elements...... ");

				ConceptsFactory conceptsFactory = new ConceptsFactory(services);

				List<String> classesInModel = new ArrayList<String>();

				for (int i = 0; i < classes.size(); i++) {
					classesInModel.add(classes.get(i).getName());
				}
				/**
				 * Strategy: predict relevant classes for each concept in the canvas, then
				 * consider top 25% with higher frequency.
				 **/
				List<String> Results = new ArrayList<String>();

				for (int i = 0; i < classes.size(); i++) {
					IConceptsPrediction conceptsPrediction = new ConceptsPrediction();
					List<HashMap<String, String>> Concepts = conceptsPrediction.run(classes.get(i).getName(), null,
							model);
					for (String key : Concepts.get(0).keySet()) {
						Results.add(key);
					}
				}

				// Map<String, Integer> hm = countFrequencies(Results);
				HashMap<String, Integer> sortedMap = new HashMap<String, Integer>(countFrequencies(Results));
				List<Entry<String, Integer>> nlist = new ArrayList<>(sortedMap.entrySet());
				nlist.sort(Entry.comparingByValue(Comparator.reverseOrder()));

				System.out.print("alll concepts ");
				System.out.print(sortedMap);
				List<String> topConcepts = new ArrayList<String>();
				int l = 0;

				for (Entry<String, Integer> set : nlist) {
					if (l < 4) {
						l++;
						topConcepts.add(set.getKey().toLowerCase());
					} else {
						break;
					}
				}
				System.out.print("chosen ones ");
				System.out.print(topConcepts);

				for (String key : topConcepts) {
					System.out.print(key);
					if (!services.containsIgnoreCase(classesInModel, key)) {
						conceptsFactory.createClass((String) key, session, true);
					}
				}
//
//				/**
//				 * One strategy: predict attributes even for potential classes
//				 **/
//				for (String key : topConcepts) {
//					HashMap<String, String> typeAttributes = new HashMap<String, String>();
//					IAttributesPrediction attributesPredcition = new AttributesPrediction();
//
//					typeAttributes = attributesPredcition.run(null, (String) key, model);
//					if (typeAttributes != null) {
//						System.out.println("full of attributes");
//						for (Map.Entry<String, String> set : typeAttributes.entrySet()) {
//							if (!(set.getKey().replaceAll(" ", "").equalsIgnoreCase(""))
//									&& !(set.getValue().replaceAll(" ", "").equalsIgnoreCase(""))) {
//								if (services.containsIgnoreCase(types, set.getValue().replaceAll(" ", ""))) {
//									attributesFactory.createAttribute(set.getKey(), set.getValue(), key, session, true);
//								}
//							}
//						}
//					} else {
//						System.out.println("attributes list is empty");
//					}
//				}

				IAssociationsPrediction associationsPrediction = new AssociationsPrediction();
				List<HashMap<String, String>> allResults = new ArrayList<HashMap<String, String>>();
				EList<Clazz> Allclasses = model.getClazz();
				for (int i = 0; i < Allclasses.size(); i++) {
					String className = Allclasses.get(i).getName();

					List<HashMap<String, String>> res = associationsPrediction.run(className, null, model);
					if (res != null) {
						allResults.addAll(res);
					}

				}
//					HashMap<HashMap<String, String>, Integer> frequencyAssociations = new HashMap<HashMap<String, String>, Integer>();
//					int freq;
//					for (int j = 0; j < allResults.size(); j++) {
//						for (Entry<HashMap<String, String>, Integer> set : frequencyAssociations.entrySet()) {
//							if ((allResults.get(j).get("Source").equals(set.getKey().get("Source"))
//									&& allResults.get(j).get("Target").equals(set.getKey().get("Target"))) || allResults.get(j).get("Source").equals(set.getKey().get("Target"))
//									&& allResults.get(j).get("Target").equals(set.getKey().get("Source"))) {
//
//								freq = set.getValue() + 1;
//								
//								if
//								
//							} else {
//								freq = 0;
//							}
//
//							frequencyAssociations.put(allResults.get(j), freq);
//
//						}
//
//					}
				
				System.out.println("Associatios ******************************");
				for (int j = 0; j < allResults.size(); j++) {
					System.out.println("Associatios ******************************");

					if (!(allResults.get(j).get("Type").replaceAll("\\s+", "").equals(""))
							&& (!(allResults.get(j).get("Type").replaceAll("\\s+", "").equals("no")))) {

						AssociationsFactory associationsFactory = new AssociationsFactory();

						// Rule: should not create if already exist association no cycles...

						if (!associationsFactory.checkAssociationExist(allResults.get(j).get("Target"),
								allResults.get(j).get("Source"), services.getModel())) {
							associationsFactory.createAssociation(allResults.get(j).get("Type"),
									allResults.get(j).get("Name"), allResults.get(j).get("Target"),
									allResults.get(j).get("Source"), session, false);
						}

					}
				}

				// reset static value to false to enable jobs running.

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
