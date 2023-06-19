package ca.umontreal.geodes.meriem.cdeditor.editor;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public abstract class Prompt {
	public String Shot;
	public String Request;
	public String prompt;
	public String Seperator;
	public String Symbole;
	public static boolean busy = true;

	// Initialize these
	private long lastRequestTime;
	private int requestsPerSecond = 2;
	private int maxWaitTimeInMillis = 5000;

	public Prompt(String Request, String Seperator, String Symbole) {
		this.Request = Request;
		this.Seperator = Seperator;
		this.Symbole = Symbole;
		this.lastRequestTime = System.nanoTime();

	}

	public void setPrompt() {
		this.prompt = this.Shot.concat(Seperator).concat(Request).concat(Symbole);
	}

	public abstract String[] interceptResults(String Results);

	private static final int MAX_ATTEMPTS = 8;
	private static final long INITIAL_BACKOFF_MS = 1000;
	private static final double BACKOFF_MULTIPLIER = 2.0;
	private static final Random random = new Random();

	public String[] run(int maxTokens, Double temperature, String engine) {
		int attempts = 0;
		while (attempts < MAX_ATTEMPTS) {
			try {
				setPrompt();
				String Results = "";
				String[] GeneratedList = new String[25];
				// To do set env variables
				String token = Services.key;
			
				OpenAiService theService = new OpenAiService(token);

				CompletionRequest completionRequest = CompletionRequest.builder().prompt(this.prompt)
						.maxTokens(maxTokens).temperature(temperature).build();

				Results = theService.createCompletion(engine, completionRequest).getChoices().get(0).getText();
				// log this :

				GeneratedList = interceptResults(Results);

				return GeneratedList;
			} catch (Exception e) {
				System.out.println("Completion failed with exception: " + e);
			}

			// Calculate the backoff time using exponential backoff strategy
			long backoffMs = (long) (INITIAL_BACKOFF_MS * Math.pow(BACKOFF_MULTIPLIER, attempts));
			System.out.println("Backing off for " + backoffMs + " ms...");
			try {
				TimeUnit.MILLISECONDS.sleep(backoffMs);
			} catch (InterruptedException e) {
				System.out.println("Interrupted while backing off: " + e);
			}

			attempts++;
		}

		if (attempts == MAX_ATTEMPTS) {
			System.out.println("Completion failed after " + attempts + " attempts.");
		}
		return null;
	}

}
