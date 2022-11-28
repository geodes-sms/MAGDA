package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConceptsPrompt extends Prompt {

	public ConceptsPrompt(String Request, String Seperator, String Symbole) {
		super(Request, Seperator, Symbole);
		this.Shot = "[['Customer', 'ContactMethod'], ['Bank', 'Customer'], ['Bank', 'Party'], ['Bank', 'account']].\n [['Saving', 'Customer'], ['Loan', 'Customer'], ['Customer', 'Financial_Representative']].\n [['Customer', 'Person'], ['Customer', 'PaymentDetails'], ['Customer', 'Person']].\n [['ActualAmountEntry', 'Category'], ['ActualTransactionEntry', 'Category'], ['BudgetFactorEntry', 'Category']].\n [['Company', 'Visitable'], ['Employee', 'Visitable'], ['Company', 'Department']].\n [['Family', 'Person'], ['mother', 'father'], ['daughter', 'Family']].\n [['Member', 'Family'], ['Member', 'Family'], ['Family', 'Member']].\n [['Family', 'NamedElement'],['father','mother']].\n [['Nurse', 'Staff'], ['Department', 'Room'], ['Nurse', 'patient'],['Nurse','department']].\n [['Addon', 'Costable'], ['visitor', 'Costable'], ['Room', 'Costable']].\n [['CirculatingItem', 'Borrower'], ['AudioVisualItem', 'Borrower'], ['Library', 'Borrower']].\n [['Male', 'Person'], ['Female', 'Person'],['Female','Male']].\n [['Airport', 'City'], ['SpecificFlight', 'GeneralFlight'], ['Airport', 'City'],['passenger', 'plane'],['trip','passenger']].\n [['Family', 'FamilyMember'], ['FamilyRegister', 'Family'], ['Family', 'FamilyMember']].\n [['university', 'department'], ['professor', 'department'], ['student', 'course'],['professor', 'course']]. ";
	}
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
  
                ArrayList<T> newList = new ArrayList<T>();
  
        for (T element : list) {
  
            if (!newList.contains(element)) {
  
                newList.add(element);
            }
        }
  
        return newList;
    }

	
	public List<String> removeSymbols(List<String> Concepts) {
		ArrayList<String> Results = new ArrayList<String>();
		for (int i = 0; i < Concepts.size(); i++) {
			Results.add(Concepts.get(i).replaceAll("[^a-zA-Z]", "").replaceAll("  ", ""));
		}
		return Results;
	}

	@Override
	public String[] interceptResults(String Results) {
		ArrayList<String> concepts;
		
		String[] second = Results.split(",");
		String[] words = Results.split(" ");
		ArrayList<String> both = new ArrayList<String>(words.length + second.length);
		Collections.addAll(both, words);
		Collections.addAll(both, second);
		// concepts = removeLetter(concepts)
		both = (ArrayList<String>) removeSymbols(both);
		
		concepts = removeDuplicates(both);
		return concepts.toArray(String[]::new);
	}

}
