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

import java.util.ArrayList;
import jinicommands.JCLI;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;

/**
 * JiniCommands Help
 * 
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class JiniHelp extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public JiniHelp() {
    }

    
    /**
     * In this method all the specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
        this.jcOptions.addOption(OptionBuilder.withLongOpt("search").withDescription("Search").isRequired(false).hasArg().create("s"));

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

            if (jcCmd.hasOption('s')) {
                String search = jcCmd.getOptionValue("s");

                this.searchCommand(search);

            }



            if (this.done == false) {
                this.priniCommandList();
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private void priniCommandList() {
        TablePrinter helpTableHead = new TablePrinter("", "Command Name : ", "Short Description", "Help");

        int x = 1;
        for (JCLI j : JCLI.values()) {
            helpTableHead.addRow(x + "", j.getJiniCommand(), j.getShortDesc(), j.getJiniCommand() + " --help");
            x++;
        }

        helpTableHead.print();
        
        
        this.done = true;
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "help / ?");
        helpTableHead.addRow("SYNOPSIS : ", "help [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Jini Commands Help");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "help -h", "help --help");
        helpTable.addRow("-s", "--search", "required", "Search for a string in Command Names and Description", "help -s list", "help --search list");

        helpTable.print();

        this.done = true;
    }

    private void searchCommand(String search) {
        if ((search != null) && (search.length() > 0)) {

            ArrayList<JCLI> res = new ArrayList<JCLI>();

            for (JCLI j : JCLI.values()) {
                String command = j.getJiniCommand();
               // String commandDesc = j.getShortDesc();

                if (command.contains(search)) {
                    res.add(j);
                }
            }


            if (res.size() > 0) {

                TablePrinter resultTable = new TablePrinter("Search Query", search);
                int x = 1;
                for (JCLI r : res) {
                    resultTable.addRow(r.getJiniCommand(), r.getShortDesc());
                    x++;
                }
                resultTable.print();
            }

            this.done = true;
        }
    }
}
