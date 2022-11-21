package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class AssociationsPredictionDummy implements IAssociationsPrediction {

	@Override
	public List<HashMap<String, String>> run(EObject rootModel, Model model) {
		List< HashMap<String,String> >results = new ArrayList < HashMap<String,String> >();

		HashMap<String,String> itemAssociation = new  HashMap<String,String>();
		itemAssociation.put("Type", "association");

		itemAssociation.put("Target", "plane");
		itemAssociation.put("Source", "airport");
		results.add(itemAssociation);

		return results;
	}

}
