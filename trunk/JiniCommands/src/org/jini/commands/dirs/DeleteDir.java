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
package org.jini.commands.dirs;

import java.io.File;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;

/**
 * Command to delete a Directory
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class DeleteDir extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    /**
     * In this method all the specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        // option -h --help : Help
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        // option -p --path 
        this.jcOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Path where the Directory will be created").hasArg().isRequired(false).create("p"));
    }

    /**
     * In this method all the execution of the specific Command takes place
     */
    @Override
    public void executeCommand() {
        this.setJCLIOptions();




        try {
            CommandLine jcCmd = null;
            String args[] = this.convertToArray();
            jcCmd = this.jcParser.parse(this.jcOptions, args);

            // Check for -h option
            if (jcCmd.hasOption('h')) {
                // Print the help
                printHelp();
            }

            // Check for -p option : Path
            if (jcCmd.hasOption('p')) {

                if (jcCmd.getOptionValue("p").length() == 0) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a directory to delete.");
                } else {

                    String delDirectory = jcCmd.getOptionValue("p");

                    if (this.isDir(delDirectory) == false) {
                        delDirectory = getWorkingDirectory() + File.separator + delDirectory;
                    }

                    if (this.isDir(delDirectory) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Directory [" + delDirectory + "] does not exsist.");
                    }

                    if ((this.getJcError() == false) && (this.done == false)) {
                        this.deleteDir(delDirectory);
                    }
                }
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    /**
     * Remove Directory
     *
     * @param delDir
     */
    private void deleteDir(String delDir) {
        File dDir = new File(delDir);

        if (removeDirectory(dDir) == true) {
            this.done = true;
            this.addResultMessages("Sucessfully Deleted Directory [" + delDir + "].");
        }

        if (dDir.exists()) {
            this.setJcError(true);
            this.addErrorMessages("Error : Unable to Delete Directory [" + delDir + "].");
        }

        this.done = true;
    }

    /**
     * Remove a directory and all of its contents.
     *
     * The results of executing File.delete() on a File object that represents a
     * directory seems to be platform dependent. This method removes the
     * directory and all of its contents.
     *
     * @return true if the complete directory was removed, false if it could not
     * be. If false is returned then some of the files in the directory may have
     * been removed.
     *
     */
    public boolean removeDirectory(File directory) {

        if (directory == null) {
            return false;
        }
        if (!directory.exists()) {
            return true;
        }
        if (!directory.isDirectory()) {
            return false;
        }

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                System.out.println("\t Deleted : " + entry);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry)) {
                        return false;
                    }
                } else {
                    if (!entry.delete()) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Unable to Delete [" + entry + "].");
                        return false;
                    }
                }
            }
        }

        return directory.delete();
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
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {

        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "deldir");
        helpTableHead.addRow("SYNOPSIS : ", "deldir [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Deletes a Directory");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "makedir -h", "makedir --help");
        helpTable.addRow("-p", "--path", "required", "Absolute Path to the Directory you want deleted", "deldir -p c:\\MyDir", "deldir --path c:\\MyDir");
        helpTable.addRow("-p", "--path", "required", "Relative Path to the Directory you want deleted", "deldir -p MyDir", "deldir --path MyDir");

        helpTable.print();

        this.done = true;
    }
}