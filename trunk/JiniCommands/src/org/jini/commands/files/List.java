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
import java.io.FileFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import jinicommands.JiniCmd;
import org.apache.commons.cli.*;
import org.apache.commons.io.comparator.*;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jini.commands.files.filters.DirFilter;
import org.jini.commands.files.filters.FileDirFilter;
import org.jini.commands.files.filters.SimpleFileFilter;
import org.jini.commands.helper.TablePrinter;

/**
 * Lists all files/Directories in a specific location
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class List extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    ArrayList<File> jcList = new ArrayList<File>();
    String directory = getWorkingDirectory();
    boolean withDetails = false;
    String resultType = "string";
    boolean showAll = false;
    String resultFormat = "string";
    boolean orderByDate = false;
    String orderByDateOpt = "asc";
    boolean orderByName = false;
    String orderByNameOpt = "asc";
    boolean doneFlag = false;

    public List() {
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
        this.jcOptions.addOption(OptionBuilder.withLongOpt("result_type").withDescription("Result Type").isRequired(false).hasArg().create("rt"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("showall").withDescription("Show All Files").isRequired(false).create("all"));

        OptionGroup jcGroup = new OptionGroup();
        jcGroup.addOption(OptionBuilder.withLongOpt("filesonly").withDescription("Show Files Only").create("fo"));
        jcGroup.addOption(OptionBuilder.withLongOpt("dirsonly").withDescription("Show Dirs Only").create("do"));
        jcGroup.addOption(OptionBuilder.withLongOpt("hiddenfiles").withDescription("Hidden Files").create("hf"));
        jcGroup.addOption(OptionBuilder.withLongOpt("hiddendirs").withDescription("Hidden Directories").create("hd"));
        jcGroup.addOption(OptionBuilder.withLongOpt("wildcard").withDescription("Wild Card Option").hasArg().create("wc"));
        this.jcOptions.addOptionGroup(jcGroup);

        OptionGroup jcGroup2 = new OptionGroup();
        jcGroup2.addOption(OptionBuilder.withLongOpt("orderbydate").withDescription("Order By Date").isRequired(false).hasArg().create("obd"));
        jcGroup2.addOption(OptionBuilder.withLongOpt("orderbyname").withDescription("Order By Name").isRequired(false).hasArg().create("obn"));
        this.jcOptions.addOptionGroup(jcGroup2);
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
                this.doneFlag = true;
            }


            if (jcCmd.hasOption("wd")) {
                this.withDetails = true;
            }

            if (jcCmd.hasOption("all")) {
                this.showAll = true;
            }

            if (jcCmd.hasOption("obd")) {
                this.orderByDate = true;

                String obdOpt = jcCmd.getOptionValue("obd");
                if (obdOpt.matches("[aA][sS][cC]")) {
                    this.orderByDateOpt = "asc";
                } else if (obdOpt.matches("[dD][eE][sS][cC]")) {
                    this.orderByDateOpt = "desc";
                }
            }

            if (jcCmd.hasOption("obn")) {
                this.orderByName = true;

                String obnOpt = jcCmd.getOptionValue("obn");
                if (obnOpt.matches("[aA][sS][cC]")) {
                    this.orderByNameOpt = "asc";
                } else if (obnOpt.matches("[dD][eE][sS][cC]")) {
                    this.orderByNameOpt = "desc";
                }
            }

            if (jcCmd.hasOption('p')) {
                /* If no option is supplied then we take the directory where the user's working directory */
                if (jcCmd.getOptionValue("p").length() == 0) {
                    this.directory = getWorkingDirectory();
                } else {
                    this.directory = jcCmd.getOptionValue("p");
                }

                File dirTest = new File(this.directory);
                if (dirTest.isDirectory() == false) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Directory [" + this.directory + "] does not exsist.");
                }
            }


            if ((this.doneFlag == false) && (this.getJcError() == false)) {
                if ((jcCmd.hasOption("fo") == false)
                        && (jcCmd.hasOption("do") == false)
                        && (jcCmd.hasOption("hf") == false)
                        && (jcCmd.hasOption("hd") == false)
                        && (jcCmd.hasOption("wc") == false)) {

                    try {
                        this.getFileDirList(this.showAll);

                        if (this.printResults(this.withDetails, this.jcList) == true) {
                            this.doneFlag = true;
                        }
                    } catch (IOException ex) {
                        this.setJcError(true);
                        this.addErrorMessages("Error :" + ex.getMessage());
                    }
                }
            }

            if ((this.doneFlag == false) && (this.getJcError() == false)) {

                if ((jcCmd.hasOption("fo") == true)
                        && (jcCmd.hasOption("do") == false)
                        && (jcCmd.hasOption("hf") == false)
                        && (jcCmd.hasOption("hd") == false)
                        && (jcCmd.hasOption("wc") == false)) {

                    try {
                        this.getFileList(this.showAll);

                        if (this.printResults(this.withDetails, this.jcList) == true) {
                            // Set the doneFlag
                            this.doneFlag = true;
                        }
                    } catch (IOException ex) {
                        this.setJcError(true);
                        this.addErrorMessages("Error :" + ex.getMessage());
                    }
                }

                if ((jcCmd.hasOption("fo") == false)
                        && (jcCmd.hasOption("do") == true)
                        && (jcCmd.hasOption("hf") == false)
                        && (jcCmd.hasOption("hd") == false)
                        && (jcCmd.hasOption("wc") == false)) {
                    try {
                        this.getDirList(this.showAll);

                        if (this.printResults(this.withDetails, this.jcList) == true) {
                            // Set the doneFlag
                            this.doneFlag = true;
                        }
                    } catch (IOException ex) {
                        this.setJcError(true);
                        this.addErrorMessages("Error :" + ex.getMessage());
                    }
                }
            }

            if (jcCmd.hasOption("wc") == true) {
                //System.out.println("WildCard Set " + (jcCmd.hasOption("wc")));

                this.getListWildCard(jcCmd.getOptionValue("wc"));

                if (this.printResults(this.withDetails, this.jcList) == true) {
                    // Set the doneFlag
                    this.doneFlag = true;
                }
            }
        } catch (org.apache.commons.cli.ParseException ex) {
            this.jcError = true;
            this.addErrorMessages("Error : " + ex.getMessage());
        }
    }

    /**
     * Getter method for all the Directories
     *
     * @param showAll
     * @throws IOException
     */
    private void getDirList(boolean showAll) throws IOException {
        if (this.getJcError() == false) {
            File[] files = this.getFileArray(new DirFilter(this.showAll));
            for (File f : files) {
                this.addFileList(f);
            }

        }
    }

    /**
     * Getter method for all normal files
     *
     * @param showAll
     * @throws IOException
     */
    private void getFileList(boolean showAll) throws IOException {
        if (this.getJcError() == false) {
            File[] files = this.getFileArray(new SimpleFileFilter(showAll));

            for (File f : files) {
                this.addFileList(f);
            }
        }
    }

    /**
     * Getter method for all Directories
     *
     * @param showAll
     * @throws IOException
     */
    private void getFileDirList(boolean showAll) throws IOException {
        if (this.getJcError() == false) {
            File[] files = this.getFileArray(new FileDirFilter(showAll));

            for (File f : files) {
                this.addFileList(f);
            }
        }
    }

    /**
     * Getter method for search with wildcard options
     *
     * @param wildCardOpt
     */
    private void getListWildCard(String wildCardOpt) {
        if (this.getJcError() == false) {
            File dir = new File(this.directory);
            FileFilter fileFilter = new WildcardFileFilter(wildCardOpt);
            File[] files = dir.listFiles(fileFilter);

            for (File f : files) {
                this.addFileList(f);
            }
        }
    }

    /**
     * Print the results
     *
     * @param withDetails
     * @param fileList
     * @return
     */
    private boolean printResults(boolean withDetails, ArrayList<File> fileList) {

        if (this.getJcError() == false) {
            if (withDetails == true) {
                TablePrinter tableF = new TablePrinter("Name", "Type", "Readable", "Writable", "Executable", "Size KB", "Size MB", "Last Modified");
                for (File v : fileList) {

                    java.util.Date lastModified = new java.util.Date(v.lastModified());
                    long filesizeInKB = v.length() / 1024;
                    double bytes = v.length();
                    double kilobytes = (bytes / 1024);
                    double megabytes = (kilobytes / 1024);

                    String type = "";

                    DecimalFormat df = new DecimalFormat("####.####");

                    if (v.isDirectory()) {
                        type = "Dir";
                    }
                    if (v.isFile()) {
                        type = "File";
                    }

                    if (v.isFile() && v.isHidden()) {
                        type = "File (Hidden)";
                    }
                    if (v.isDirectory() && (v.isHidden())) {
                        type = "Dir (Hidden)";
                    }

                    String canExec = "" + v.canExecute();
                    String canRead = "" + v.canRead();
                    String canWrite = "" + v.canWrite();
                    String filesizeInKBStr = Long.toString(filesizeInKB);
                    String filesizeInMBStr = df.format(megabytes);

                    tableF.addRow(v.getName(), type, canRead, canWrite, canExec, filesizeInKBStr, filesizeInMBStr, lastModified.toString());
                }

                tableF.print();
            } else {
                for (File v : fileList) {
                    System.out.println(v.getName());
                }
            }
        }

        return true;
    }

    /**
     * Get all the files from a array
     *
     * @param fileFilter
     * @return
     */
    private File[] getFileArray(FileFilter fileFilter) {
        File dir = new File(this.directory);
        File[] files = dir.listFiles(fileFilter);

        if (this.orderByDate == true) {
            if (this.orderByDateOpt.equals("desc")) {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            }
            if (this.orderByDateOpt.equals("asc")) {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
            }
        }

        if (this.orderByName == true) {
            if (this.orderByNameOpt.equals("asc")) {
                Arrays.sort(files, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
            }

            if (this.orderByNameOpt.equals("desc")) {
                Arrays.sort(files, NameFileComparator.NAME_INSENSITIVE_REVERSE);
            }
        }

        return files;
    }

    void addFileList(File file) {
        this.jcList.add(file);
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
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "list");
        helpTableHead.addRow("SYNOPSIS : ", " list [OPTION]... [DIRECTORY]..");
        helpTableHead.addRow("DESCRIPTION : ", " Print a list of files/directories in a Directory.");
        helpTableHead.print();

        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "list -h", "list --help");

        helpTable.addRow("-p", "--path", "required", "Path to the directory. (Default is current Directory)", "list -p c:\\", "list --path c:\\");

        helpTable.addRow("-wd", "--withdetails", "not required", "Display table of files/directories with details", "list -p c:\\ -wd ", "list --path c:\\ --withdetails ");
        helpTable.addRow("-all", "--showall", "not required", "Include files/directories whose name starts with .", "list -p c:\\ -wd -all", "list --path c:\\ --withdetails --showall");

        helpTable.addRow("-fo", "--filesonly", "not required", "List only Files.", "list -p c:\\ -wd -fo ", "list --path c:\\ --withdetails --filesonly ");
        helpTable.addRow("-do", "--dirsonly", "not required", "List only Directories.", "list -p c:\\ -wd -do ", "list --path c:\\ --withdetails --dirsonly ");
        helpTable.addRow("-wc", "--wildcard", "required", "Wild Card Search.", "list -p c:\\ -wd -wc *.html", "list --path c:\\ --withdetails --wildcard *.sys ");

        helpTable.addRow("-obd", "--orderbydate", "required [asc | desc]", "Order by Date", "list -p c:\\ -wd -obd asc", "list --path c:\\ --withdetails --orderbydate asc");
        helpTable.addRow("-obn", "--orderbyname", "required [asc | desc]", "Order by Name", "list -p c:\\ -wd -obn asc", "list --path c:\\ --withdetails --orderbyname asc");

        helpTable.print();

        TablePrinter helpTableFoot = new TablePrinter("Option Groups : ", "");
        helpTableFoot.addRow("--filesonly --dirsonly --wildcard", " These options are grouped. i.e only one can be defined at a given time.");
        helpTableFoot.addRow("--orderbydate --orderbyname", " These options are grouped. i.e only one can be defined at a given time.");
        helpTableFoot.print();

    }
}
