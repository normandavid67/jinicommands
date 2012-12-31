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
package org.jini.commands.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;

/**
 * Prints out Todays Date in different formats
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class JiniDate extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public JiniDate() {
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
        this.jcOptions.addOption(OptionBuilder.withLongOpt("format").withDescription("Format").hasArg().hasOptionalArg().isRequired(false).create("f"));

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

            if (jcCmd.hasOption('f')) {
                String format = jcCmd.getOptionValue("f").trim();

                System.out.println(this.getSimpleDate(format));
                this.done = true;
            }

            if (this.done == false) {
                System.out.println(this.getSimpleDate());
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private String getSimpleDate(String format) {

        try {

            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(now.getTime());
        } catch (Exception e) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + e.getMessage());
        }

        return "";

    }

    private String getSimpleDate() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
        String d = formatter.format(now.getTime());

        return d;

    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "date");
        helpTableHead.addRow("SYNOPSIS : ", " date [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", " Print today's date.");
        helpTableHead.addRow("Example 1 : ", "date --format dd-MMMM-YYYY will print '26-March-2012'");
        helpTableHead.addRow("Example 2 : ", "date --format dd-MMM-yy will print '29-Jan-02'");
        helpTableHead.addRow("Example 3 : ", "date --format dd-MMMM-YYYY will print '26-März-2012'");
        helpTableHead.addRow("Example 4 : ", "date --format dd_MMM_yyyy_HH:mm:ss will print '26_Mrz_2012_10:25:36'");

        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "date -h", "date --help");
        helpTable.addRow("-f", "--format", "required", "Define the Format of the output.", "date -f dd-MMMM-YYYY", "date --format dd-MMMM-YYYY");


        helpTable.print();

        this.done = true;
    }
}
