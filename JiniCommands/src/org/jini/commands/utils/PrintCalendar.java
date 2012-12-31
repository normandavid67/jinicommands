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

import java.util.Calendar;
import java.util.GregorianCalendar;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;

/**
 * Print Calendar
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class PrintCalendar extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    Calendar cal = new GregorianCalendar();

    public PrintCalendar() {
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

        this.jcOptions.addOption(OptionBuilder.withLongOpt("year").withDescription("From Location").isRequired(false).hasArg().create("y"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("month").withDescription("To Location").isRequired(false).hasArg().create("m"));

    }

    /**
     * In this method all the execution of the specific Command takes place
     */
    @Override
    public void executeCommand() {
        this.setJCLIOptions();
        CommandLine jcCmd = null;
        String args[] = this.convertToArray();
        int year = 0;
        int month = 0;

        try {
            jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                printHelp();
            }

            if (jcCmd.hasOption("y")) {
                try {
                    year = Integer.parseInt(jcCmd.getOptionValue("y"));
                } catch (Exception e) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a valid year");
                }
            }

            if (jcCmd.hasOption("m")) {

                try {
                    month = Integer.parseInt(jcCmd.getOptionValue("m"));
                } catch (Exception e) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a valid month");
                }

                if ((month < 0) || (month > 12)) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a valid month");
                }
            }

            if (this.done == false) {

                if ((year == 0) && (month == 0)) {

                    year = this.cal.get(Calendar.YEAR);
                    switch (this.cal.get(Calendar.MONTH)) {
                        case (0):
                            month = 1;
                            break;
                        case (1):
                            month = 2;
                            break;

                        case (2):
                            month = 3;
                            break;
                        case (3):
                            month = 4;
                            break;

                        case (4):
                            month = 5;
                            break;
                        case (5):
                            month = 6;
                            break;
                        case (6):
                            month = 7;
                            break;
                        case (7):
                            month = 8;
                            break;
                        case (8):
                            month = 9;
                            break;
                        case (9):
                            month = 10;
                            break;
                        case (10):
                            month = 11;
                            break;
                        case (11):
                            month = 12;
                            break;
                    }

                    printMonth(year, month);
                    //this.done = true;
                }

                if ((this.jcError == false) && (this.done == false) && ((year > 0) && (month == 0))) {
                    //System.out.println("(year > 0) && (month == 0))");
                    for (int m = 1; m < 13; m++) {
                        printMonth(year, m);
                    }
                }

                if ((this.jcError == false) && (this.done == false) && ((year == 0) && (month > 0))) {
                    // System.out.println("(year == 0) && (month > 0)");
                    year = this.cal.get(Calendar.YEAR);
                    printMonth(year, month);
                }

                if ((this.jcError == false) && (this.done == false) && ((year > 0) && (month > 0))) {
                    //System.out.println("(year > 0) && (month > 0)");
                    printMonth(year, month);
                }
            }
        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    void printMonth() {
        int year = this.cal.get(Calendar.YEAR);
        int month = this.cal.get(Calendar.MONTH);

        printMonthBody(year, month);
    }

    void printMonth(int year) {
        int month = this.cal.get(Calendar.MONTH);
        printMonthBody(year, month);

    }

    /**
     * Print the calendar for a month in a year
     *
     * @param year
     * @param month
     */
    void printMonth(int year, int month) {
        printMonthTitle(year, month);
        printMonthBody(year, month);

        this.done = true;
    }

    /**
     * Print the month title, e.g., May, 1999
     *
     * @param year
     * @param month
     */
    void printMonthTitle(int year, int month) {
        System.out.println("         " + getMonthName(month)
                + " " + year);
        System.out.println("-----------------------------");
        System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
    }

    /**
     * Get the English name for the month
     *
     * @param month
     * @return
     */
    String getMonthName(int month) {
        String monthName = null;
        switch (month) {
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
        }

        return monthName;
    }

    /**
     * Print month body
     *
     * @param year
     * @param month
     */
    void printMonthBody(int year, int month) {
        // Get start day of the week for the first date in the month
        int startDay = getStartDay(year, month);

        // Get number of days in the month
        int numberOfDaysInMonth = getNumberOfDaysInMonth(year, month);

        // Pad space before the first day of the month

        for (int i = 0; i < startDay; i++) {
            System.out.print("    ");
        }

        for (int i = 1; i <= numberOfDaysInMonth; i++) {
            if (i < 10) {
                System.out.print("   " + i);
            } else {
                System.out.print("  " + i);
            }

            if ((i + startDay) % 7 == 0) {
                System.out.println();
            }
        }

        System.out.println();
    }

    /**
     * Get the start day of month/1/year
     *
     * @param year
     * @param month
     * @return
     */
    int getStartDay(int year, int month) {
        final int START_DAY_FOR_JAN_1_1800 = 3;
        // Get total number of days from 1/1/1800 to month/1/year
        int totalNumberOfDays = getTotalNumberOfDays(year, month);

        // Return the start day for month/1/year
        return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
    }

    /**
     * Get the total number of days since January 1, 1800
     *
     * @param year
     * @param month
     * @return
     */
    int getTotalNumberOfDays(int year, int month) {
        int total = 0;

        // Get the total days from 1800 to 1/1/year
        for (int i = 1800; i < year; i++) {
            if (isLeapYear(i)) {
                total = total + 366;
            } else {
                total = total + 365;
            }
        }

        // Add days from Jan to the month prior to the calendar month
        for (int i = 1; i < month; i++) {
            total = total + getNumberOfDaysInMonth(year, i);
        }

        return total;
    }

    /**
     * Get the number of days in a month
     *
     * @param year
     * @param month
     * @return
     */
    int getNumberOfDaysInMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            return 31;
        }

        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }

        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return 0; // If month is incorrect
    }

    /**
     * Determine if it is a leap year
     *
     * @param year
     * @return
     */
    static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "calendar");
        helpTableHead.addRow("SYNOPSIS : ", " calendar [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Clear the console. ");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "calendar -h", "calendar --help");
        helpTable.addRow("-y", "--year", "required", "Define the year", "calendar -y 1991", "calendar --year 1991");
        helpTable.addRow("-m", "--month", "required", "Define the month (1 = Jan, 2 = Feb)", "calendar -m 3", "calendar --m 3");

        helpTable.print();
        this.done = true;
    }
}