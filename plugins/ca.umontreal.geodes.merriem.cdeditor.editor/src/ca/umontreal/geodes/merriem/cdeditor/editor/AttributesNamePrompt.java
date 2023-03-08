package ca.umontreal.geodes.merriem.cdeditor.editor;

public class AttributesNamePrompt extends Prompt {

	public AttributesNamePrompt(String Request, String Seperator, String Symbole) {

		super(Request, Seperator, Symbole);
		this.Shot = "generate attributes for class names: \n Medication: ['id', 'label', 'hours', 'minutes', 'dose', 'units'] \n	Response: ['id', 'questionId', 'responseKey', 'value', 'timeStamp'] \n ClockState: ['mToggledDayTime', 'mChanged', 'mFirstTouch', 'mIsDayTime'] \n Medication: ['id', 'label', 'hours', 'minutes', 'dose', 'units']. \n Response: ['id', 'questionId', 'responseKey', 'value', 'timeStamp']. \n PongPanel: ['WIDTH', 'HEIGHT', 'scoreP1', 'divider', 'scoreP2', 'scorePanel', 'timer', 'DELAY']. \n patient: ['id', 'name', 'surname', 'history','gender','contact','homeAddress'].	\n Bicycle: ['costPass', 'costVehicle', 'pMax', 'vSize']. \n Bus: ['costPass', 'costVehicle', 'pMax', 'vSize'].	\n Car: ['costPassenger', 'costVehicle', 'pMax', 'vSize'].	\n Truck: ['costPass', 'costVehicle', 'pMax', 'vSize'].	\n CatchCreature: ['rnd', 'delay', 'timer', 'score']. \n nurse: ['id', 'name', 'department', 'yearsOfExperience','speciality','contact'].";

		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] interceptResults(String Results) {
		String[] Attributes = new String[20];
		try {
			Results = Results.replaceAll("\n", "");
			Results = Results.replaceAll("'", "");
			String attributesStr = Results;

			if (Results.contains("[")) {
				attributesStr = Results.split("\\[")[1];
			}
			if (Results.contains("]")) {
				attributesStr.replace("\\]", "");
			}
			Attributes = attributesStr.split(",");
			for (int j = 0; j < Attributes.length; j++) {
				Attributes[j].replaceAll("'", "");
				System.out.println(Attributes[j]);
			}
		} catch (Exception e) {

		}

		return Attributes;

	}

}
