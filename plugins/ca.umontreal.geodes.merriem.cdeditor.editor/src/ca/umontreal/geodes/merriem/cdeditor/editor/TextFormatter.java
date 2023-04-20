package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

public class TextFormatter extends Formatter {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Override
	public String format(LogRecord record) {
		List<String> classNames = new ArrayList<String>();
		List<String> Attributes = new ArrayList<String>();
		for (int i = 0; i < Services.getModel().getClazz().size(); i++) {
			
			if (Services.getModel().getClazz().get(i).getName() != null) {

				classNames.add(Services.getModel().getClazz().get(i).getName());
			}
			String className= Services.getModel().getClazz().get(i).getName() + ": ["; 
			for(int k=0; k< Services.getModel().getClazz().get(i).getAttributes().size(); k++) {
				className=className+Services.getModel().getClazz().get(i).getAttributes().get(k).getName()+ ","; 
			}
			className=className + "]"; 
			Attributes.add(className); 
			
		}
	
		StringBuilder sb = new StringBuilder();
		sb.append(dateFormat.format(new Date(record.getMillis()))).append(" , ");
		sb.append(record.getLevel()).append(" , ");
		sb.append(String.valueOf(Services.mode)).append(" , ");
		sb.append(record.getMessage()).append(" , ");

		sb.append(String.valueOf(classNames)).append(" , ");
		if (Services.relatedClasses != null) {

			List<String> flattenedList = Services.relatedClasses.values().stream().flatMap(List::stream)
					.collect(Collectors.toList());
			// remove duplicated
			sb.append(String.valueOf(flattenedList)).append(" , ");
		} else {
			sb.append(" - ").append(" , ");
		}
		
		sb.append(String.valueOf(Attributes)).append(" , ");
		
		if (Services.classAttributes != null) {
			List<String> flattenedListAttributes = new ArrayList<String>();
			  for (Map.Entry<String, HashMap<String, String>> entry : Services.classAttributes.entrySet()) {
		            String key = entry.getKey();
		            HashMap<String, String> innerMap = entry.getValue();
		            String innerKeys = String.join(", ", innerMap.keySet());
		            flattenedListAttributes.add(key + ": [" + innerKeys + "]"); 
		            //System.out.println(key + ": [" + innerKeys + "]");
		        }
			sb.append(String.valueOf(flattenedListAttributes)).append(System.lineSeparator());
		} else {
			sb.append(" - ").append(System.lineSeparator());
		}
		System.out.println("Log record: " + sb.toString());
		return sb.toString();
	}
}
