package ca.umontreal.geodes.meriem.cdeditor.editor;

public class AssociationTypePrompt extends Prompt {

	public AssociationTypePrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot = "choose type of associations these concepts, inheritance or association or composition or no :  \n  \n student , person => inheritance \n computer, cpu => composition \n plane , passenger => no  \n person , address => association  \n account , saving => inheritance \n Bank, Atm => composition \n nurse , bank => no  \n professor, student => association  \n professor , person => inheritance \n contact , address => composition \n circle , bank => no  \n account , checking => inheritance  \n customer , account => association \n book , paper => composition \n book , doctor => no  \n lion , meat => association  \n lion , salad => no  \n mammal , dog => inheritance \n customer, order => association \n addressBook, contact   => composition \n Bank, Atm => composition \n manager, employee =>  association \n plane, manager =>  no";
	}

	@Override
	public String[] interceptResults(String Results) {

		if ("inheritance".contains(Results.toLowerCase().replaceAll("\\s+", ""))) {
			Results = "inheritance";
		} else if ("association".contains(Results.toLowerCase().replaceAll("\\s+", ""))) {
			Results = "association";
		} else if ("composition".contains(Results.toLowerCase().replaceAll("\\s+", ""))) {
			Results = "composition";
		}
		String[] results = new String[5];
		results[0] = Results;

		return results;
	}

}
