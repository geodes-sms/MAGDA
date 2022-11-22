package ca.umontreal.geodes.merriem.cdeditor.editor;


import java.util.List;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.engine.Engine;

public class main {

	public static void main(String[] args) {
		String prompt= "generate attributes for class names:  \n Medication: ['id', 'label', 'hours', 'minutes', 'dose', 'units']  \n	Response: ['id', 'questionId', 'responseKey', 'value', 'timeStamp']  \n AnalogClockTimePicker: ['mCalendar', 'mHourHand', 'mMinuteHand', 'mClockHinge', 'mDial', 'mMinutes', 'mHour', 'mLastX', 'mLastY', 'mTimeFormatter', 'mTimeTextSize', 'mDayTimeTextDelta', 'mHourAngle'].  \n ClockState: ['mToggledDayTime', 'mChanged', 'mFirstTouch', 'mIsDayTime']  \n Medication: ['id', 'label', 'hours', 'minutes', 'dose', 'units']. \n Response: ['id', 'questionId', 'responseKey', 'value', 'timeStamp'].	 \n Ball: ['RADIUS', 'rnd', 'ballX', 'ballY', 'ballSpeedX', 'ballSpeedY', 'ballStartSpeedP1', 'ballStartSpeedP2', 'ballStartX', 'ballStartY']. \n PongPanel: ['WIDTH', 'HEIGHT', 'scoreP1', 'divider', 'scoreP2', 'scorePanel', 'timer', 'DELAY']. \n patient: ['id', 'name', 'surname', 'history','gender','contact','homeAddress'].	 \n Bicycle: ['costPass', 'costVehicle', 'pMax', 'vSize'].  \n Bus: ['costPass', 'costVehicle', 'pMax', 'vSize'].	 \n Car: ['costPassenger', 'costVehicle', 'pMax', 'vSize'].	 \n Truck: ['costPass', 'costVehicle', 'pMax', 'vSize'].	 \n CatchCreature: ['rnd', 'delay', 'timer', 'score'].	 \n Creature: ['img', 'rnd', 'imgX', 'imgY']. \n BallPanel: ['WIDTH', 'HEIGHT', 'DELAY', 'bollKnapp', 'ballSpeedX', 'ballSpeedY', 'ballX', 'ballY', 'rnd', 'timer', 'balls']. \n nurse: ['id', 'name', 'department', 'yearsOfExperience','speciality','contact']. \n plane: ["; 

		
		//to do: use environment variables. (export token) 
		String token = "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";
		OpenAiService service = new OpenAiService(token);


		System.out.println("\n Creating completion...");
		CompletionRequest completionRequest = CompletionRequest.builder()
				.prompt(prompt).maxTokens(10).temperature(0.7).build();
		String result; 
		result = service.createCompletion("davinci", completionRequest).getChoices().get(0).getText();
		System.out.println(result);
		

	}
}