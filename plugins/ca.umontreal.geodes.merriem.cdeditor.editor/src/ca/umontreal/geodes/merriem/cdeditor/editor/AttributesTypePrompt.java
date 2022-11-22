package ca.umontreal.geodes.merriem.cdeditor.editor;

public class AttributesTypePrompt extends Prompt {

	public AttributesTypePrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot= "Address => String \n age => int \n name => String \n isCanceled => boolean \n salary => int \n sold => float \n surname => String \n birthDate => Date \n isValidated => boolean \n staffNumber => int \n width=> double \n phoneNumber => float \n city => String \n state => String \n zipCode => int" ;
	}

	@Override
	public String[] interceptResults(String Results) {
		System.out.println("here is the prblm? " + Results); 
		String[] results = new String[50];
		results[0]=Results;
		return results; 
	}

}
