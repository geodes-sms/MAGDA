package ca.umontreal.geodes.merriem.cdeditor.editor;

import org.osgi.framework.ServiceException;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public abstract class Prompt {
	public String Shot;
	public String Request;
	public String prompt; 
	public String Seperator; 
	public String Symbole; 


 
		public Prompt(String Request,String Seperator, String Symbole) {
			this.Request=Request; 
			this.Seperator=Seperator; 
			this.Symbole=Symbole; 
			
		}
		
		public void setPrompt() {
			this.prompt= this.Shot.concat(Seperator).concat(Request).concat(Symbole); 
		}
		
		public abstract String[] interceptResults(String Results); 
		
		public String[] run(int maxTokens, Double temperature, String engine) {
			setPrompt(); 
			String Results= "";

			String token = "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";
			try {
				System.out.println("what's wrong ? ");

			OpenAiService theService = new OpenAiService(token);
			theService.getEngines().forEach(System.out::println);
			System.out.println("  Creating completion...");
			CompletionRequest completionRequest = CompletionRequest.builder()
					.prompt(this.prompt).maxTokens(10).temperature(0.7).build();
			
			/*
			System.out.println(" \n Creating completion...");
			CompletionRequest completionRequest = CompletionRequest.builder()
					.prompt(this.prompt).maxTokens(maxTokens).temperature(temperature).build();
			*/
			Results=  theService.createCompletion(engine, completionRequest).getChoices().get(0).getText();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return  interceptResults(Results);
			
		}
}
