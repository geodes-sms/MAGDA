package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class AttributesPrediction implements IAttributesPrediction {

	@Override
	public HashMap<String, String> run(EObject node, Model model) {

		String NodeName = node.toString().split(":", 2)[1].replace(")", "");
		NodeName = NodeName.replaceAll("\\s+", "");
		HashMap<String, String> typeAttributes = new HashMap<String, String>();
		List<String> attributes = new ArrayList<String>();
		for (int i = 1; i < node.eContents().size(); i++) {
			attributes.add(node.eContents().get(i).toString().split(" ", 3)[2].split(":", 3)[0]);
		}
		/** if class already have attributes, ... **/
		String input;
		if (node.eContents().size() > 1) {
			input = attributes.get(0);
			for (int i = 1; i < attributes.size(); i++) {
				input = input.concat(",").concat(attributes.get(i));
			}
		} else
			input = "";
		Prompt attributesNamePrompt = new AttributesNamePrompt(NodeName.concat(input), "\n", " : [ ");
		attributesNamePrompt.setPrompt();
		System.out.println("predicting attributes...");

		String[] arrayAttributes = attributesNamePrompt.run(20,0.7, "text-davinci-002");
		
		// type of predicted attribute
		for (int i = 0; i < arrayAttributes.length; i++) {
			Prompt attributesTypePrompt = new AttributesTypePrompt(arrayAttributes[i], "\n", " => ");
			attributesTypePrompt.setPrompt();
			String[] Type = attributesTypePrompt.run(1, 0.7, "text-davinci-002");
			System.out.println(arrayAttributes[i] +"::::"+ Type[0]);
			typeAttributes.put(arrayAttributes[i], Type[0]);

		}
		return typeAttributes;
	}
}
