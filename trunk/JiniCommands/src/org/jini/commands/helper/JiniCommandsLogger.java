package org.jini.commands.helper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JiniCommandsLogger {

    static boolean append = true;
    static int limit = 104857600; // 100 MB Per log file
    static int numLogFiles = 5;
    String fileName = this.getJiniLogDir() + File.separator + "JiniLogs.log"; //Default name of the log file
    static Level logLevel = Level.INFO;
    static final Logger logger = Logger.getLogger("org.jini.commands.helper.JiniCommandsLogger");

    public static int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        JiniCommandsLogger.limit = limit;
    }

    public static boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        JiniCommandsLogger.append = append;
    }

    private String getJiniLogDir() {
        return this.getOsSpecificTempDirectory() + "JiniCommandsLog";
    }

    private String getOsSpecificTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    public JiniCommandsLogger() {
        if (this.checkJiniLogDir() == false) {
            this.createJiniLogDir();
        }

        Formatter formatter = this.getCustomFormatter();

        try {
            FileHandler handler;
            handler = new FileHandler(this.fileName, JiniCommandsLogger.getLimit(), numLogFiles, JiniCommandsLogger.isAppend());
            if (formatter != null) {
                handler.setFormatter(formatter);
            }
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
        }
    }

    public void writeLog(String msg) {
        logger.log(new LogRecord(logLevel, msg));
        //logger.finest("This is a finest level of message");
    }

    private boolean checkJiniLogDir() {
        String directoryName = this.getJiniLogDir();

        if ((directoryName != null) && (directoryName.length() > 0)) {
            File dirTest = new File(directoryName);
            if (dirTest.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void createJiniLogDir() {
        File dirName = new File(this.getJiniLogDir());
        if (!dirName.exists()) {
            dirName.mkdir();
        }
    }

    private Formatter getCustomFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                String recordStr;
                recordStr = "" + getTimeStamp() + " : "
                       // + record.getLevel() + "  "
                        //+ record.getSourceClassName() + " {Method} "
                        //+ record.getSourceMethodName() + " {Message} "
                        + record.getMessage() + "\n";
                return recordStr;
            }
        };
    }

    private String getTimeStamp() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");

        return df.format(new Date());
        //return Calendar.getInstance().getTime().toString();
    }

    public static void main(String args[]) {
        JiniCommandsLogger jcl = new JiniCommandsLogger();

        for (int x = 0; x < 10; x++) {
            jcl.writeLog("Test in " + x);
        }
    }
}
