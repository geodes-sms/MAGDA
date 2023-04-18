package ca.umontreal.geodes.merriem.cdeditor.editor;

public class AttributesTypePrompt extends Prompt {

	public AttributesTypePrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot = "Address => String \n age => int \n name => String \n isCanceled => boolean \n  sold => float \n surname => String \n birthDate => Date \n isValidated => boolean \n staffNumber => int \n width=> double \n phoneNumber => float \n city => String \n state => String";
	}

	@Override
	public String[] interceptResults(String Results) {
		String[] results = new String[3];
		results[0] = Results.replaceAll("\\s+", "");
		return results;
	}

}
