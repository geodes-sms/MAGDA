package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl;

public class AttributesPrediction implements IAttributesPrediction {

	public static String[] removeNulls(String[] arr) {
		return Arrays.stream(arr).filter(s -> s != null && !s.isEmpty()).toArray(String[]::new);
	}

	@Override
	public HashMap<String, String> run(EObject node, String NodeName, Model model, boolean isAtEndCompletion) {
		try {

			HashMap<String, String> typeAttributes = new HashMap<String, String>();
			List<String> attributes = new ArrayList<String>();
			String input;
			String shot = "";
			String Symbol = "";
			if (node != null) {
				if (!(node instanceof ClazzImpl)) {
					for (int i = 0; i < node.eContents().size(); i++) {
						attributes.add(node.eContents().get(i).toString().split(" ")[2].split("\\)")[0]);

					}
					if (attributes.size() != 0) {
						input = attributes.get(0);
						for (int i = 1; i < attributes.size(); i++) {
							input = input.concat(",").concat(attributes.get(i));
						}
					
						shot = NodeName.concat(": [' ").concat(input);
						Symbol = " , ";
					} else {
						input = "";
						shot = NodeName;
						Symbol = ": [ ";
					}
				} else if (node instanceof ClazzImpl && isAtEndCompletion == true) {

					List<Attribute> attributesNodes = ((Clazz) node).getAttributes();
					if (attributesNodes.size() != 0) {
						input = attributesNodes.get(0).getName();
						for (int i = 1; i < attributesNodes.size(); i++) {
							input = input.concat(",").concat(attributesNodes.get(i).getName());
						}
						shot = NodeName.concat(": [' ").concat(input);
						Symbol = " , ";
					} else {
						input = "";
						shot = NodeName;
						Symbol = ": [ ";
					}

				}

			} else {
				input = "";
				shot = NodeName;
				Symbol = ": [ ";
			}

			
			System.out.println("predicting attributes...for class :  " + NodeName);
			Prompt attributesNamePrompt = new AttributesNamePrompt(shot, "\n", Symbol);
			attributesNamePrompt.setPrompt();

			String[] arrayAttributes = attributesNamePrompt.run(40, 0.7, Services.usedModel);

			// type of predicted attribute
			String[] arrayAttributesTemp = new String[10];

			/**
			 * Strategy: Consider three attributes or less to be added to canvas when it's
			 * global prediction; pre
			 **/

			if (isAtEndCompletion) {
				List<String> arrayAttributesList = new ArrayList<String>(Arrays.asList(arrayAttributes));
				for (int k = 0; k < 3; k++) {
					if (arrayAttributesList.size() != 0) {
						Random random = new Random();
						int index = random.nextInt(arrayAttributesList.size());
						arrayAttributesTemp[k] = arrayAttributesList.get(index);
						arrayAttributesList.remove(index);
					}
				}

				arrayAttributes = removeNulls(arrayAttributesTemp);
				// arrayAttributes=arrayAttributesTemp;
			}

			for (int i = 0; i < arrayAttributes.length; i++) {

				Prompt attributesTypePrompt = new AttributesTypePrompt(arrayAttributes[i], "\n", " => ");
				attributesTypePrompt.setPrompt();
				String[] Type = attributesTypePrompt.run(2, 0.7, "text-davinci-002");
				typeAttributes.put(arrayAttributes[i], Type[0]);
				System.out.println(arrayAttributes[i] + " :" + Type[0]);
			}
			return typeAttributes;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
