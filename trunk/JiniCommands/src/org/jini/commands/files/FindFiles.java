/*
New BSD License
Copyright (c) 2012, Norman David <normandavid67@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Norman David BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
  
 For a copy of the License type 'license'
 */
package org.jini.commands.files;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.jini.commands.helper.TablePrinter;
import org.jini.commands.utils.PrintChar;

/**
 * File Search
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class FindFiles extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    ArrayList<File> jcList = new ArrayList<File>();
    private boolean done;
    private boolean recursive = true;
    private String fromLocation = getWorkingDirectory();
    private boolean withDetails;

    public FindFiles() {
    }

    /**
     * In this method all the Command specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Path").hasArg(true).isRequired(false).create("p"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("withdetails").withDescription("With Details").isRequired(false).create("wd"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("notrecursive").withDescription("Non Recursive").hasArg(false).isRequired(false).create("nr"));

        OptionGroup jcGroup = new OptionGroup();
        jcGroup.addOption(OptionBuilder.withLongOpt("extension").withDescription("Search with extensions").hasArg(true).isRequired(false).create("ex"));
        jcGroup.addOption(OptionBuilder.withLongOpt("wildcard").withDescription("Wild Card Option").hasArg().create("wc"));
        this.jcOptions.addOptionGroup(jcGroup);


    }

    /**
     * In this method all the execution of the specific Command takes place
     */
    @Override
    public void executeCommand() {

        this.setJCLIOptions();
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                printHelp();
            }

            if (jcCmd.hasOption("nr")) {
                this.recursive = false;
            }

            if (jcCmd.hasOption("wd")) {
                this.withDetails = true;
            }



            if (jcCmd.hasOption("p")) {


                /* If no option is supplied then we take the directory where the user's working directory */
                if ((jcCmd.getOptionValue("p").isEmpty() == false) && (jcCmd.getOptionValue("p").length() == 0)) {
                    this.fromLocation = getWorkingDirectory();
                } else {
                    this.fromLocation = jcCmd.getOptionValue("p");
                    if (this.isDir(this.fromLocation) == false) {
                        this.fromLocation = getWorkingDirectory() + File.separator + this.fromLocation;
                    }

                    if (this.isDir(this.fromLocation) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : " + this.fromLocation + " Is not a valid Directory");
                    }
                }
            }

            if (jcCmd.hasOption("ex")) {
                if ((this.done == false) && (this.getJcError() == false)) {
                    String ex = jcCmd.getOptionValue("ex");
                    String exarg[] = ex.split(",");

                    this.printTable("Starting search in Directory ", this.fromLocation);
                    this.findFileWithExtension(this.fromLocation, exarg, this.recursive);
                }
            }

            if (jcCmd.hasOption("wc")) {
                if ((this.done == false) && (this.getJcError() == false)) {
                    String wcStr = jcCmd.getOptionValue("wc");
                    this.printTable("Starting search in Directory ", this.fromLocation);
                    this.findFilesWithWildCard(this.fromLocation, wcStr, this.recursive);
                }
            }



            if (jcCmd.hasOption("ex")) {
                if ((this.done == false) && (this.getJcError() == false)) {
                    String ex = jcCmd.getOptionValue("ex");
                    String exarg[] = ex.split(",");
                    this.printTable("Starting search in Directory ", this.fromLocation);
                    this.findFileWithExtension(this.fromLocation, exarg, this.recursive);
                }
            }



        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error : " + ex.getMessage());
        }
    }

    /**
     * Directory tester
     *
     * @param directoryName
     * @return
     */
    private boolean isDir(String directoryName) {
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

    /**
     * Getter method of the location of the user.
     *
     * @return
     */
    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * Find Files with a wild card pattern
     *
     * @param dirName
     * @param wildCardPattern
     * @param recursive
     */
    void findFilesWithWildCard(String dirName, String wildCardPattern, boolean recursive) {

        PrintChar printChar = new PrintChar();
        printChar.setOutPutChar(">");

        if ((this.getJcError() == false) && (this.isDir(dirName) == false)) {
            this.setJcError(true);
            this.addErrorMessages("Error : Directory [" + dirName + "] does not exsist.");
        }

        if (this.getJcError() == false) {
            printChar.start();


            try {
                File root = new File(dirName);

                Collection files = FileUtils.listFiles(root, null, recursive);

                TablePrinter tableF = new TablePrinter("Name", "Type", "Readable", "Writable", "Executable", "Size KB", "Size MB", "Last Modified");

                for (Iterator iterator = files.iterator(); iterator.hasNext();) {

                    File file = (File) iterator.next();

                    if (file.getName().matches(wildCardPattern)) {

                        if (this.withDetails == false) {
                            System.out.println(file.getAbsolutePath());
                        } else {

                            java.util.Date lastModified = new java.util.Date(file.lastModified());
                            long filesizeInKB = file.length() / 1024;
                            double bytes = file.length();
                            double kilobytes = (bytes / 1024);
                            double megabytes = (kilobytes / 1024);

                            String type = "";

                            DecimalFormat df = new DecimalFormat("####.####");

                            if (file.isDirectory()) {
                                type = "Dir";
                            }
                            if (file.isFile()) {
                                type = "File";
                            }

                            if (file.isFile() && file.isHidden()) {
                                type = "File (Hidden)";
                            }
                            if (file.isDirectory() && (file.isHidden())) {
                                type = "Dir (Hidden)";
                            }

                            String canExec = "" + file.canExecute();
                            String canRead = "" + file.canRead();
                            String canWrite = "" + file.canWrite();
                            String filesizeInKBStr = Long.toString(filesizeInKB);
                            String filesizeInMBStr = df.format(megabytes);

                            tableF.addRow(file.getAbsolutePath(), type, canRead, canWrite, canExec, filesizeInKBStr, filesizeInMBStr, lastModified.toString());
                        }
                    }
                }

                if (this.withDetails == true) {
                    if (files.isEmpty() == false) {
                        tableF.print();
                    }
                }

                printChar.setStopPrinting(true);
            } catch (Exception e) {

                this.setJcError(true);
                this.addErrorMessages("Error : " + e.toString());
            }

        }

        printChar.setStopPrinting(true);
        this.done = true;
    }

    /**
     * Search for files with specific extensions
     *
     * @param dirName
     * @param extensions
     * @param recursive
     */
    void findFileWithExtension(String dirName, String[] extensions, boolean recursive) {
        PrintChar printChar = new PrintChar();
        printChar.setOutPutChar(">");

        if ((this.getJcError() == false) && (this.isDir(dirName) == false)) {
            this.setJcError(true);
            this.addErrorMessages("Error : Directory [" + dirName + "] does not exsist.");
        }


        if (this.getJcError() == false) {
            printChar.start();
            File root = new File(dirName);

            try {
                Collection files = FileUtils.listFiles(root, extensions, recursive);

                TablePrinter tableF = new TablePrinter("Name", "Type", "Readable", "Writable", "Executable", "Size KB", "Size MB", "Last Modified");



                for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                    File file = (File) iterator.next();

                    if (this.withDetails == false) {
                        System.out.println(file.getAbsolutePath());
                    } else {

                        java.util.Date lastModified = new java.util.Date(file.lastModified());
                        long filesizeInKB = file.length() / 1024;
                        double bytes = file.length();
                        double kilobytes = (bytes / 1024);
                        double megabytes = (kilobytes / 1024);

                        String type = "";

                        DecimalFormat df = new DecimalFormat("####.####");

                        if (file.isDirectory()) {
                            type = "Dir";
                        }
                        if (file.isFile()) {
                            type = "File";
                        }

                        if (file.isFile() && file.isHidden()) {
                            type = "File (Hidden)";
                        }
                        if (file.isDirectory() && (file.isHidden())) {
                            type = "Dir (Hidden)";
                        }

                        String canExec = "" + file.canExecute();
                        String canRead = "" + file.canRead();
                        String canWrite = "" + file.canWrite();
                        String filesizeInKBStr = Long.toString(filesizeInKB);
                        String filesizeInMBStr = df.format(megabytes);

                        tableF.addRow(file.getAbsolutePath(), type, canRead, canWrite, canExec, filesizeInKBStr, filesizeInMBStr, lastModified.toString());
                    }
                }

                if (this.withDetails == true) {
                    if (files.isEmpty() == false) {
                        tableF.print();
                    }
                }

                printChar.setStopPrinting(true);
            } catch (Exception e) {
                this.setJcError(true);
                this.addErrorMessages("Error : " + e.toString());
            }
        }

        this.done = true;
    }

    /**
     * Print a short info table
     *
     * @param infDesc
     * @param inf
     */
    private void printTable(String infDesc, String inf) {

        if (((infDesc != null) && (infDesc.length() > 0)) && ((inf != null) && (inf.length() > 0))) {
            TablePrinter helpTableHead = new TablePrinter("Description", "Info");
            helpTableHead.addRow(infDesc, inf);

            helpTableHead.print();
        }

    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {

        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "findfiles / findfile");
        helpTableHead.addRow("SYNOPSIS : ", "findfiles [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Make a Recursive/Non Recursive search for file/s. Search options extensions, ");

        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "findfiles -h", "findfiles --help");
        helpTable.addRow("-p", "--path", "not required", "Define the path from where to start the search.", "findfiles -p", "findfiles --path");
        helpTable.addRow(" ", " ", " ", "File Search using Relative Path", "findfiles -p DirName", "findfiles --path DirName");
        helpTable.addRow("-ex", "--extension", "required", "Define File extension/s to search for. ", "findfiles -p c:\\ -ex txt,java", "findfiles --path c:\\  --extension txt,java");
        helpTable.addRow("-wc", "--wildcard", "required", "Filename search with Wild Card Characters.", "findfiles -p c:\\ -wc [a-zA-Z]*.txt  -wd", "findfiles --path c:\\ --wildcard [a-zA-Z]*.txt --withdetails ");
        helpTable.addRow("-wd", "--withdetails", "not required", "Show all details of the file.", "findfiles -p c:\\ -ex txt,java -wd", "findfiles --path c:\\  --extension txt,java --withdetails");
        helpTable.print();

        this.done = true;
    }
}
