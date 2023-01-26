package ca.umontreal.geodes.merriem.cdeditor.editor;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public abstract class Prompt {
	public String Shot;
	public String Request;
	public String prompt;
	public String Seperator;
	public String Symbole;

	public Prompt(String Request, String Seperator, String Symbole) {
		this.Request = Request;
		this.Seperator = Seperator;
		this.Symbole = Symbole;

	}

	public void setPrompt() {
		this.prompt = this.Shot.concat(Seperator).concat(Request).concat(Symbole);
	}

	public abstract String[] interceptResults(String Results);

	public String[] run(int maxTokens, Double temperature, String engine) {
		setPrompt();
		String Results = "";
		String[] GeneratedList = new String[25] ; 
		// To do set env variables
		String token = "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";
		
		try {

			OpenAiService theService = new OpenAiService(token);
			CompletionRequest completionRequest = CompletionRequest.builder().prompt(this.prompt).maxTokens(maxTokens)
					.temperature(temperature).build();

			Results = theService.createCompletion(engine, completionRequest).getChoices().get(0).getText();
			// log this : 
		
			GeneratedList = interceptResults(Results);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GeneratedList ;
 
	}
}
