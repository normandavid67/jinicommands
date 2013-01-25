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
import org.apache.commons.cli.*;
import org.jini.commands.helper.TablePrinter;

/**
 * Change Location command
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class ChangeDirectory extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public ChangeDirectory() {
    }

    /**
     * In this method all the Command specific Command Line options are defined.
     *
     */

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        // option -h --help : Help
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        // option -p --path
        this.jcOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Path where the Directory will be changed").hasArg().isRequired(false).create("p"));
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

            // Check for -h option
            if (jcCmd.hasOption("h")) {
                // Print the help
                this.printHelp();
            }


            // Check for -p option : Path
            if (jcCmd.hasOption("p")) {
                String dirLocation = jcCmd.getOptionValue("p");

                if (this.isDir(dirLocation) == false) {
                    dirLocation = getWorkingDirectory() + File.separator + dirLocation;
                }
                this.changeDirectory(dirLocation);
            }

            if ((this.done == false) && (this.getJcError() == false)) {
                System.out.println("Error : Incorrect arguements passed. Please type cd --help");
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    /**
     * Change location
     *
     * @param dirLocation
     */
    private void changeDirectory(String dirLocation) {
        if ((dirLocation == null) || (dirLocation.length() == 0)) {
            this.setJcError(true);
            this.addErrorMessages("Error : Please define a path.");
        }

        if ((this.getJcError() == false) && (this.isDir(dirLocation) == false)) {
            this.setJcError(true);
            this.addErrorMessages("Error : Directory [" + dirLocation + "] does not exsist.");
        }

        if (this.getJcError() == false) {
            File file = new File(dirLocation);
            String absolutePathOfDir = file.getAbsolutePath();


            // Set the System property
            System.setProperty("user.dir", absolutePathOfDir);
            System.out.println("Changed Directory to : " + System.getProperty("user.dir"));

            this.done = true;
        }
    }

    /**
     * Directory tester
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
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "cd");
        helpTableHead.addRow("SYNOPSIS : ", "cd [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Change Directory.");

        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "help -h", "help --help");
        helpTable.addRow("-p", "--path", "required", "Full/Relative path to a directory ", "cd -p c:\\", "cd --path c:\\");

        helpTable.print();

        this.done = true;
    }
}
