package ca.umontreal.geodes.merriem.cdeditor.editor;

public class ConceptsPrompt extends Prompt {

	public ConceptsPrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot= "[['Customer', 'ContactMethod'], ['Bank', 'Customer'], ['Bank', 'Party'], ['Bank', 'account']].\n [['Saving', 'Customer'], ['Loan', 'Customer'], ['Customer', 'Financial_Representative']].\n [['Customer', 'Person'], ['Customer', 'PaymentDetails'], ['Customer', 'Person']].\n [['ActualAmountEntry', 'Category'], ['ActualTransactionEntry', 'Category'], ['BudgetFactorEntry', 'Category']].\n [['Company', 'Visitable'], ['Employee', 'Visitable'], ['Company', 'Department']].\n [['Family', 'Person'], ['mother', 'father'], ['daughter', 'Family']].\n [['Member', 'Family'], ['Member', 'Family'], ['Family', 'Member']].\n [['Family', 'NamedElement'],['father','mother']].\n [['Nurse', 'Staff'], ['Department', 'Room'], ['Nurse', 'patient'],['Nurse','department']].\n [['Addon', 'Costable'], ['visitor', 'Costable'], ['Room', 'Costable']].\n [['CirculatingItem', 'Borrower'], ['AudioVisualItem', 'Borrower'], ['Library', 'Borrower']].\n [['Male', 'Person'], ['Female', 'Person'],['Female','Male']].\n [['Airport', 'City'], ['SpecificFlight', 'GeneralFlight'], ['Airport', 'City'],['passenger', 'plane'],['trip','passenger']].\n [['Family', 'FamilyMember'], ['FamilyRegister', 'Family'], ['Family', 'FamilyMember']].\n [['university', 'department'], ['professor', 'department'], ['student', 'course'],['professor', 'course']]. "; 
	}

	@Override
	public String[] interceptResults(String Results) {
		String[] results = new String[50];
		results[0]=Results;
		return results;
	}
	

}
