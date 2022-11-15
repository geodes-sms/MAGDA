package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public interface AssociationPrediction {
	

	List< HashMap<String,String>> run(EObject rootModel, Model model, Properties config);
}
