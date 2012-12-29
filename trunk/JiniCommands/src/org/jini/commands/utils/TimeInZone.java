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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;

/**
 * Print out time in a TimeZone
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class TimeInZone extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    int lines = 90;
    private boolean done;

    public TimeInZone() {
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
        this.jcOptions.addOption(OptionBuilder.withLongOpt("search").withDescription("Search for a TimeZone").isRequired(false).hasArg().create("s"));

        OptionGroup jcGroup = new OptionGroup();
        jcGroup.addOption(OptionBuilder.withLongOpt("zone").withDescription("Zone Name").hasArg(true).isRequired(false).create("z"));
        jcGroup.addOption(OptionBuilder.withLongOpt("list").withDescription("List").hasArg(false).isRequired(false).create("l"));
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

            if (jcCmd.hasOption('z')) {
                String timeZoneName = jcCmd.getOptionValue("z");

                if ((timeZoneName != null) && (timeZoneName.length() > 0)) {
                    TablePrinter resultTable1 = new TablePrinter("Time Zone", "Date Time");
                    ArrayList<String> res = this.searchTimeZone(timeZoneName);
                    for (String r : res) {
                        if (this.getZoneDetails(r).length() > 0) {
                            resultTable1.addRow(r, this.getZoneDetails(r));
                        }
                    }
                    resultTable1.print();
                    this.done = true;
                }
            }
            if (jcCmd.hasOption('s')) {
                String search = jcCmd.getOptionValue("s");
                ArrayList<String> res = this.searchTimeZone(search);

                if (res.size() > 0) {
                    TablePrinter resultTable = new TablePrinter("", "Time Zone");

                    int x = 1;
                    for (String r : res) {
                        resultTable.addRow("(" + x + ")", r);
                        x++;
                    }
                    resultTable.print();
                }
            }

            if (jcCmd.hasOption('l')) {
                this.listZones();
            }


        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private ArrayList<String> searchTimeZone(String search) {
        ArrayList<String> res = new ArrayList<String>();
        if (this.jcError == false) {
            String[] timeZones = TimeZone.getAvailableIDs();
            for (int x = 0; x < timeZones.length; x++) {
                if (timeZones[x].toLowerCase().contains(search.toLowerCase())) {
                    res.add(timeZones[x]);
                }
            }
        }

        return res;
    }

    private String getZoneDetails(String zoneName) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(zoneName));

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);      // 0 to 11
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        return formatter.format("%2d/%02d/%04d %02d:%02d:%02d", day, month + 1, year, hour, minute, second).toString();
    }

    private void listZones() {

        TablePrinter zoneTableHead = new TablePrinter("", "Time Zone");

        String[] timeZones = TimeZone.getAvailableIDs();

        for (int x = 0; x < timeZones.length; x++) {
            zoneTableHead.addRow(x + "", timeZones[x]);
        }
        zoneTableHead.print();

        this.done = true;
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "timeinzone");
        helpTableHead.addRow("SYNOPSIS : ", "timeinzone [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Print time & Date in a Time Zone");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "timeinzone -h", "timeinzone --help");
        helpTable.addRow("-z", "--zone", "required", "Define the Time Zone", "timeinzone -z Hongkong ", "timeinzone --zone Hongkong");
        helpTable.addRow("-l", "--list", "not required", "Print list of all avaliable Time Zone", "timeinzone - l", "timeinzone --list");
        helpTable.addRow("-s", "--search", "required", "Search for a Time Zone", "timeinzone -s Australia", "timeinzone --search Australia");

        helpTable.print();
        this.done = true;
    }
}