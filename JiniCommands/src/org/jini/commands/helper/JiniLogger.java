package org.jini.commands.helper;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JiniLogger {

    /**
     * Define whether the log should be appended to the existing one If set to
     * false, the log file will be overwritten, not appended
     */
    boolean append = true;
    /**
     * Defining the limit of file size in Bytes. This is tentatively the maximum
     * data that will be written to one log file.
     */
    int limit = 104857600; // 100 MB Per log file
    /**
     * This is the maximum number of log files to be created. If this number is
     * reached, the last file will be truncated and a new one will be created.
     */
    int numLogFiles = 50;
    /**
     * If a file is full (as defined by 'limit' value above), files will be
     * renamed and a new log file will be created.
     *
     * Pattern defines how the log file should be renamed.
     *
     * In this example, we are using the same fileName as the pattern. So the
     * files will be renamed as: JiniLogs.log.0 JiniLogs.log.1 JiniLogs.log.2
     * ..and so on.
     */
    String fileName = this.getOsSpecificTempDirectory()+File.pathSeparator+"JiniLogs.log";

    /**
     * Getter method for append variable
     *
     * @return
     */
    public boolean isAppend() {
        return append;
    }

    /**
     * Setter method for append variable
     *
     * @param append
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Logger getFileLogger() {
        Formatter formatter = this.getCustomFormatter();

        /**
         * Find or create a logger for a named subsystem. If a logger has
         * already been created with the given name it is returned. Otherwise a
         * new logger is created.
         *
         * If a new logger is created its log level will be configured based on
         * the LogManager configuration and it will configured to also send
         * logging output to its parent's handlers. It will be registered in the
         * LogManager global name space.
         */
        Logger logger = Logger.getLogger("org.jini.commands.helper.JiniLogger");
        try {

 

            /**
             * Creating a file handler based on the above parameters
             */
            FileHandler handler = new FileHandler(this.getFileName(), this.getLimit(), numLogFiles, this.isAppend());
            if (formatter != null) {
                handler.setFormatter(formatter);
            }
            logger.addHandler(handler);
        } catch (IOException e) {
        }
        return logger;
    }

    /**
     * This method will define a custom formatter and return it. Formatter is
     * used to format the way the output content is logged to the system, in
     * this case a file.
     */
    public Formatter getCustomFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                String recordStr = "{Date} " + new Date() + " {Log Level} "
                        + record.getLevel() + " {Class} "
                        + record.getSourceClassName() + " {Method} "
                        + record.getSourceMethodName() + " {Message} "
                        + record.getMessage() + "\n";
                return recordStr;
            }
        };
    }

    /**
     * Getter method for OS specific temporary file
     *
     * @return
     */
    private String getOsSpecificTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }
}