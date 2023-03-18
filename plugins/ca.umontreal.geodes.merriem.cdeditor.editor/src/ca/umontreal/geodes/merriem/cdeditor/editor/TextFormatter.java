package ca.umontreal.geodes.merriem.cdeditor.editor;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class TextFormatter extends Formatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        sb.append(dateFormat.format(new Date(record.getMillis()))).append(",");
        sb.append(record.getLevel()).append(",");
        sb.append(record.getMessage()).append(System.lineSeparator()).append(",");
        sb.append(String.valueOf(Services.mode));
        System.out.println("Log record: " + sb.toString());
        return sb.toString();
    }
}
