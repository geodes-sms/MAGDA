package ca.umontreal.geodes.meriem.cdeditor.editor;

public class InheritancePrompt extends Prompt{

	public InheritancePrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot = "select the  super class  in this UML inheritance relationship: admin, user  => user \n  Account , SavingAccount => Account \n Room , doubleRoom  =>Room \n vehicle , car => vehicle \n dog , animal => animal";

	}

	@Override
	public String[] interceptResults(String Results) {
		
		
		String[] results = new String[10];
		results[0]=Results;
		
		return results; 
	}

}
