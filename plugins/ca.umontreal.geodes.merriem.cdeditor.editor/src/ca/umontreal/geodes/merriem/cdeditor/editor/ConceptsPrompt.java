package ca.umontreal.geodes.merriem.cdeditor.editor;

public class ConceptsPrompt extends Prompt {

	public ConceptsPrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot= "[['Customer', 'ContactMethod'], ['Bank', 'Customer'], ['Bank', 'Party'], ['Bank', 'account']].\n [['Saving', 'Customer'], ['Loan', 'Customer'], ['Customer', 'Financial_Representative']].\n [['Customer', 'Person'], ['Customer', 'PaymentDetails'], ['Customer', 'Person']].\n [['ActualAmountEntry', 'Category'], ['ActualTransactionEntry', 'Category'], ['BudgetFactorEntry', 'Category']].\n [['Company', 'Visitable'], ['Employee', 'Visitable'], ['Company', 'Department']].\n [['Family', 'Person'], ['mother', 'father'], ['daughter', 'Family']].\n [['Member', 'Family'], ['Member', 'Family'], ['Family', 'Member']].\n [['Family', 'NamedElement'],['father','mother']].\n [['Nurse', 'Staff'], ['Department', 'Room'], ['Nurse', 'patient'],['Nurse','department']].\n [['Addon', 'Costable'], ['visitor', 'Costable'], ['Room', 'Costable']].\n [['CirculatingItem', 'Borrower'], ['AudioVisualItem', 'Borrower'], ['Library', 'Borrower']].\n [['Male', 'Person'], ['Female', 'Person'],['Female','Male']].\n [['Airport', 'City'], ['SpecificFlight', 'GeneralFlight'], ['Airport', 'City'],['passenger', 'plane'],['trip','passenger']].\n [['Family', 'FamilyMember'], ['FamilyRegister', 'Family'], ['Family', 'FamilyMember']].\n [['university', 'department'], ['professor', 'department'], ['student', 'course'],['professor', 'course']]. "; 
	}

	
	public String[] removeDuplicated(String[] Concepts ) {
		String[] results =  new String[100] ;
		results[0]= Concepts[0]; 
		for(int i =0 ; i< Concepts.length; i++) {
			boolean exist=false; 
			for(int j =0 ; j<results.length; j++) {
				
				if (Concepts[i].toLowerCase().equals(Concepts[j].toLowerCase())){
					exist=true; 
					break;
				}
				
			}
			if (! exist) {
				results[results.length]=Concepts[i].toLowerCase(); 
			}
			
			
		}
		return results; 
	}
	@Override
	public String[] interceptResults(String Results) {
	    		/*S = re.sub(r'\W+', ' ', res)
	    	    FirstSet = S.split(' ')
	    	    NewConcepts = wordninja.split(S)
	    	    NewConcepts.extend(FirstSet)
	    	    NewConcepts = ' '.join(NewConcepts).split()
	    	    concepts = set(NewConcepts)
	    	    concepts = list(concepts)
	    	    concepts = removeLetter(concepts)
	    	    concepts = removeDuplicated(concepts)*/
		return null; 
	}
	

}
