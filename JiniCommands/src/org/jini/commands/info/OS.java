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
package org.jini.commands.info;

import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;

/**
 * Prints out OS related information
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class OS extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public OS() {
    }

    /**
     * In this method all the specific Command Line options are defined.
     *
     */
    @Override
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
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

            if (jcCmd.hasOption("h")) {
                this.printHelp();
            }


            if (this.done == false) {

                String osName = System.getProperty("os.name");
                String osArch = System.getProperty("os.arch");
                String osVersion = System.getProperty("os.version");


                TablePrinter helpTableHead = new TablePrinter("OS", "Details");
                if ((osName != null) && (osName.isEmpty() == false)) {
                    helpTableHead.addRow("Operating system name", osName);
                }
                if ((osArch != null) && (osArch.isEmpty() == false)) {
                    helpTableHead.addRow("Operating system architecture", osArch);
                }
                if ((osVersion != null) && (osVersion.isEmpty() == false)) {
                    helpTableHead.addRow("Operating system version", osVersion);
                }
                helpTableHead.print();


            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "os");
        helpTableHead.addRow("SYNOPSIS : ", "os [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Prints OS Info.");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "help -h", "help --help");
        helpTable.print();

        this.done = true;
    }
}
