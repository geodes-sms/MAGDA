package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public interface IConceptsPrediction {
	List<HashMap<String, String>> run(String className, EObject rootModel, Model model);

}
