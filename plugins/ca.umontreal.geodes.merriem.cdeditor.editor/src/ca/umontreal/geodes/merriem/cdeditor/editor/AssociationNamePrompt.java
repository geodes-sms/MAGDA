package ca.umontreal.geodes.merriem.cdeditor.editor;

public class AssociationNamePrompt extends Prompt {

	public AssociationNamePrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot = "predict association name:  \n  employee , company => worksIn \n person, Home =>  owns \n  car ,driver => drives  \n personalCustomer, customer => is \n  man, women=>  married \n lion, meat => eats \n manager, employee  =>  supervises \n order,  customer =>   places \n user, account =>  has \n cat, milk => drinks \n employee, department =>  worksIn \n ";

	}

	@Override
	public String[] interceptResults(String Results) {
		String[] results = new String[50];
		results[0] = Results;
		return results;
	}

}
