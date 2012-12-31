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
 * Create a new Directory
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class MakeDirectory extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    /**
     * In this method all the Command specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Path where the Directory will be created").hasArg().isRequired(false).create("p"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("name").withDescription("Name of the Directory to be created").hasArg().isRequired(false).create("n"));


    }

    /**
     * In this method all the execution of the specific Command takes place
     */
    @Override
    public void executeCommand() {
        this.setJCLIOptions();
        String dirLocation = getWorkingDirectory();
        String dirName = "";

        CommandLine jcCmd = null;
        String args[] = this.convertToArray();
        try {
            jcCmd = this.jcParser.parse(this.jcOptions, args);

            // Check for -h option
            if (jcCmd.hasOption('h')) {
                printHelp();
            }

            if (jcCmd.hasOption('p')) {
                dirLocation = jcCmd.getOptionValue("p").trim();

                if ((dirLocation != null) && (dirLocation.length() > 0)) {
                    if (this.isDir(dirLocation) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Directory [" + dirLocation + "] does not exsist.");
                    }
                }
            }

            if (jcCmd.hasOption('n')) {
                dirName = jcCmd.getOptionValue("n").trim();
            }

            if ((this.done == false) && ((dirName == null) || (dirName.length() == 0))) {
                this.setJcError(true);
                this.addErrorMessages("Error : Please define a directory name to create. Help type 'makedir -h' ");
            }


            if ((this.getJcError() == false) && (this.done == false)) {
                String newDirName = dirLocation + File.separator + dirName;
                this.createDirectory(newDirName);
            }


        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    /**
     * Create the Directory
     *
     * @param destinationDir
     */
    private void createDirectory(String destinationDir) {
        File dstDir = new File(destinationDir);

        if (!dstDir.exists()) {
            dstDir.mkdir();

            if (dstDir.exists()) {
                this.addResultMessages("Sucessfully created dir " + destinationDir);
            }
        } else {
            this.setJcError(true);
            this.addErrorMessages("Error : Directory exsists [" + destinationDir + "].");
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
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {

        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "makedir");
        helpTableHead.addRow("SYNOPSIS : ", "makedir [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Creates a new Directory");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "makedir -h", "makedir --help");
        helpTable.addRow("-p", "--path", "not required", "Path where the Directory will be created", "makedir -p c:\\", "makedir --path");
        helpTable.addRow("-n", "--name", "required", "Name of the Directory to be created", "makedir -p c:\\ -n NewDirectory", "makedir --path c:\\ --name NewDirectory");
        helpTable.print();

        this.done = true;
    }
}