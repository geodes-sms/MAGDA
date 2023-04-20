package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.util.*;
import java.lang.*;
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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
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
						"byte", "array", "object", "collection", "Date"));
				services = new Services();
				AttributesFactory attributesFactory = new AttributesFactory();
				Session session = services.getSession();
				Model model = services.getModel();
				EList<Clazz> classes = model.getClazz();

				System.out.println("Complete missing elements...... ");

				ConceptsFactory conceptsFactory = new ConceptsFactory(services);
				AssociationsFactory associationsFactory = new AssociationsFactory();

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

				List<String> topConcepts = new ArrayList<String>();
				int l = 0;

				for (Entry<String, Integer> set : nlist) {
					if (l < 5) {
						l++;
						topConcepts.add(set.getKey().toLowerCase());
					} else {
						break;
					}
				}

				boolean cancelPressed = false;
				ArrayList<String> addedClass = new ArrayList<String>();
				while (topConcepts.size() > 0 && cancelPressed == false) {
					String[] arrayConcepts = topConcepts.toArray(new String[0]);
					ElementListSelectionDialog dialog = new ElementListSelectionDialog(
							Display.getCurrent().getActiveShell(), new LabelProvider());
					dialog.setElements(arrayConcepts);
					dialog.setTitle("Select  missing class names");
					dialog.setMultipleSelection(true);
					if (dialog.open() != Window.OK) {
						// return false;
					}
					Object[] result = dialog.getResult();

					if (result != null) {
						for (int i = 0; i < result.length; i++) {
							if (!services.containsIgnoreCase(classesInModel, (String) result[i])) {
								conceptsFactory.createClass((String) result[i], session, false);
								topConcepts.remove((String) result[i]);
								addedClass.add((String) result[i]);
							}
						}
					} else {
						cancelPressed = true;
					}

				}

				for (int i = 0; i < classes.size(); i++) {
					HashMap<String, String> typeAttributes = new HashMap<String, String>();
					IAttributesPrediction attributesPredcition = new AttributesPrediction();

					typeAttributes = attributesPredcition.run(classes.get(i), (String) classes.get(i).getName(), model,
							true);
					cancelPressed = false;

					while (typeAttributes.size() != 0 && cancelPressed == false) {
						List<String> ResultsTyped = new ArrayList<String>();

						Iterator<Map.Entry<String, String>> iter = typeAttributes.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry<String, String> entry = iter.next();
							String key = entry.getKey();
							String value = entry.getValue();
							if (key.trim().isEmpty() || value.trim().isEmpty()) {
								iter.remove();
							} else if (!(key.replaceAll(" ", "").equalsIgnoreCase(""))
									&& !(value.replaceAll(" ", "").equalsIgnoreCase(""))) {

								if (services.containsIgnoreCase(types, value.replaceAll("\\s+", ""))) {

									ResultsTyped.add(key.concat(":").concat(value.replaceAll("\\s+", "")));
								}
							}
						}
						String[] ArrayResultsTyped = ResultsTyped.toArray(new String[0]);

						ElementListSelectionDialog dialog2 = new ElementListSelectionDialog(
								Display.getCurrent().getActiveShell(), new LabelProvider());
						dialog2.setElements(ArrayResultsTyped);
						dialog2.setTitle("Select  attributes for  Class \" " + classes.get(i).getName() + "\"");
						dialog2.setMultipleSelection(true);
						if (dialog2.open() != Window.OK) {
							// return false;
						}
						Object[] result = dialog2.getResult();
						if (result != null) {
							for (int i1 = 0; i1 < result.length; i1++) {
								String res = (String) result[i1];
								res = res.split(":")[0];
								attributesFactory.createAttribute(res, typeAttributes.get(res),
										classes.get(i).getName(), session, true);
								typeAttributes.remove(res);
							}
							if (typeAttributes.size() == 0) {
								cancelPressed = true;
							}
						} else {
							cancelPressed = true;
						}

					}

				}

				/**
				 * One strategy: predict associations even for potential classes how many
				 * associations to be predicted ?
				 **/
				IAssociationsPrediction associationsPrediction = new AssociationsPrediction();
				String[] ArrayResultsTyped = new String[20];
				String item;
				for (int i = 0; i < classes.size(); i++) {
					if (classes.get(i).getName() != null) {
						String className = classes.get(i).getName();

						List<HashMap<String, String>> res = associationsPrediction.run(className, null, model);

						List<String> items = new ArrayList<String>();

						if (res != null) {

							for (int j = 0; j < res.size(); j++) {
								if (!res.get(j).get("Type").replaceAll("\\s+", "").equals("")
										&& (!(res.get(j).get("Type").replaceAll("\\s+", "").equalsIgnoreCase("no")))) {

									if (!(res.get(j).get("Name").replaceAll("\\s+", "").equals(""))) {
										if (res.get(j).get("Type").equalsIgnoreCase("association")) {
											item = res.get(j).get("Type") + " ' " + res.get(j).get("Name")
													+ " ' between " + res.get(j).get("Source") + " => "
													+ res.get(j).get("Target");
										}else {
											item = res.get(j).get("Type")  + " between " + res.get(j).get("Source") + " => "
													+ res.get(j).get("Target");
										}

									} else {

										item = res.get(j).get("Type") + " between " + res.get(j).get("Source") + " => "
												+ res.get(j).get("Target");
									}
									items.add(item);

								}

							}
							ArrayResultsTyped = items.toArray(new String[0]);
						}

					}
				}
				cancelPressed = false;
				
				while (ArrayResultsTyped.length > 0 && (ArrayResultsTyped[0] != null) && cancelPressed == false) {
					ElementListSelectionDialog dialog = new ElementListSelectionDialog(
							Display.getCurrent().getActiveShell(), new LabelProvider());

					dialog.setElements(ArrayResultsTyped);
					dialog.setTitle("Select association to add to canvas");
					dialog.setMultipleSelection(true);

					if (dialog.open() != Window.OK) {
						// return false;
					}
					Object[] result = dialog.getResult();

					if (result != null) {
						List<String> items = new ArrayList<String>(Arrays.asList(ArrayResultsTyped));

						for (int j = 0; j < result.length; j++) {
							item = (String) result[j];
							String Type = item.split(" ")[0];
							String Name = item.split("'")[1];
							String Target = item.split(" ")[5];
							String Source = item.split(" ")[7];
							associationsFactory.createAssociation(Type, Name, Target, Source, session, true);
							items.remove(item);

						}
					} else {
						cancelPressed = true;
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
