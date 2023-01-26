package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.HashMap;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;



class MyListener extends ResourceSetListenerImpl {
	
	
	
    public MyListener(HashMap<String, HashMap<String, String>> classAttributes) {
		// TODO Auto-generated constructor stub
	}
	public void resourceSetChanged(ResourceSetChangeEvent event) {
 
            System.out.println("Domain " + event.getEditingDomain().getID() +
             " changed " + event.getNotifications().size() + " times");
           

    }
}