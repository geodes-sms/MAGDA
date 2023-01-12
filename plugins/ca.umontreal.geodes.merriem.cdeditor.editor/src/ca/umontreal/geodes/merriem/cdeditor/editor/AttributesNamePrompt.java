package ca.umontreal.geodes.merriem.cdeditor.editor;

public class AttributesNamePrompt extends Prompt {

	public AttributesNamePrompt(String Request, String Seperator, String Symbole) {

		super(Request, Seperator, Symbole);
		this.Shot = "generate attributes for class names: \n Medication: ['id', 'label', 'hours', 'minutes', 'dose', 'units'] \n	Response: ['id', 'questionId', 'responseKey', 'value', 'timeStamp'] \n AnalogClockTimePicker: ['mCalendar', 'mHourHand', 'mMinuteHand', 'mClockHinge', 'mDial', 'mMinutes', 'mHour', 'mLastX', 'mLastY', 'mTimeFormatter', 'mTimeTextSize', 'mDayTimeTextDelta', 'mHourAngle']. \n ClockState: ['mToggledDayTime', 'mChanged', 'mFirstTouch', 'mIsDayTime'] \n Medication: ['id', 'label', 'hours', 'minutes', 'dose', 'units']. \n Response: ['id', 'questionId', 'responseKey', 'value', 'timeStamp'].	\n Ball: ['RADIUS', 'rnd', 'ballX', 'ballY', 'ballSpeedX', 'ballSpeedY', 'ballStartSpeedP1', 'ballStartSpeedP2', 'ballStartX', 'ballStartY']. \n PongPanel: ['WIDTH', 'HEIGHT', 'scoreP1', 'divider', 'scoreP2', 'scorePanel', 'timer', 'DELAY']. \n patient: ['id', 'name', 'surname', 'history','gender','contact','homeAddress'].	\n Bicycle: ['costPass', 'costVehicle', 'pMax', 'vSize']. \n Bus: ['costPass', 'costVehicle', 'pMax', 'vSize'].	\n Car: ['costPassenger', 'costVehicle', 'pMax', 'vSize'].	\n Truck: ['costPass', 'costVehicle', 'pMax', 'vSize'].	\n CatchCreature: ['rnd', 'delay', 'timer', 'score'].	\n Creature: ['img', 'rnd', 'imgX', 'imgY']. \n BallPanel: ['WIDTH', 'HEIGHT', 'DELAY', 'bollKnapp', 'ballSpeedX', 'ballSpeedY', 'ballX', 'ballY', 'rnd', 'timer', 'balls']. \n nurse: ['id', 'name', 'department', 'yearsOfExperience','speciality','contact'].";

		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] interceptResults(String Results) {
		Results = Results.replaceAll("\n", "");
		Results = Results.replaceAll("'", "");
		String attributesStr=Results;
		String[] Attributes = new String[20];
		if (Results.contains("[")) {
			attributesStr = Results.split("[")[1];
			}
		if (Results.contains("]")) {
			attributesStr.replace("]", "");
		}
			Attributes = attributesStr.split(",");
			for (int j = 0; j < Attributes.length; j++) {
				Attributes[j].replaceAll("'", "");
				System.out.println(Attributes[j]);
			}
		

	return Attributes;

}

}
