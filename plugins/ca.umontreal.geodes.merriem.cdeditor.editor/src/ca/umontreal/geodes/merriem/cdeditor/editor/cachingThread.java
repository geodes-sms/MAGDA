package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;

public class cachingThread extends Thread {
	
	protected Model model  ; 
	protected volatile HashMap<String, HashMap<String, String>> classAttributes ; 
	protected Properties config;

	cachingThread(Model model){
		this.model = model; 
		this.config = new Properties();
		try {
			InputStream stream = Services.class.getClassLoader().getResourceAsStream("/config.properties");
			this.config.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void run()
    {
        System.out.print("prediction is proceeding 1 ... ");

		String scriptLocation = this.config.getProperty("scriptlocation");
		String pythonCommand = this.config.getProperty("pythoncommand");
        System.out.print("prediction is proceeding ... ");
        List<Clazz> classesInModel = model.getClazz();
        List<String> AllclassNames = new ArrayList<String>();
        for (int i = 0; i < classesInModel.size(); i++) {
			AllclassNames.add(classesInModel.get(i).getName());
			


		}
		HashMap<String, HashMap<String, String>> classAttributes = new HashMap<String, HashMap<String, String>>();
		List<String> Results = new ArrayList<String>();
		String input="";
		for(int i= 0; i<AllclassNames.size(); i++) {
        try {
    		

        	
        	//pythonCommand= 
        	//scriptLocation= load from config !
        	
			Process P1 = new ProcessBuilder(pythonCommand, scriptLocation + "predictAttributes.py", AllclassNames.get(i), input,"Attribute").start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(P1.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(P1.getErrorStream()));

			String s;

			while ((s = stdInput.readLine()) != null) {

				Results.add(s);
			}

			while ((s = stdError.readLine()) != null) {
				// add logger !
				System.out.println(s);
			}
			

			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        String[] arrayAttributes = Results.toArray(new String[0]);
		HashMap<String, String> typeAttributes = new HashMap<String, String>();

		// Add keys and values (Country, City)

		// print recieved attributes from python script
		for (int j = 0; j< arrayAttributes.length;j++) {
			String Type = "";

			try {
				Process P2 = new ProcessBuilder(pythonCommand, scriptLocation + "predictAttributes.py", 		arrayAttributes[j], input, "Type").start();
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(P2.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(P2.getErrorStream()));
				String s;
				while ((s = stdInput.readLine()) != null) {

					Type = s;
				}
				while ((s = stdError.readLine()) != null) {
					// add logger !
					System.out.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			typeAttributes.put(arrayAttributes[j], Type);



		}
       
		classAttributes.put(AllclassNames.get(i), typeAttributes);
		}
        this.classAttributes=classAttributes; 
    

    }
}
