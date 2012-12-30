/*
 * Licensed under GNU GENERAL PUBLIC LICENSE Version 1 you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/gpl-1.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * For a copy of the License type 'license'
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

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("year").withDescription("From Location").isRequired(false).hasArg().create("y"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("month").withDescription("To Location").isRequired(false).hasArg().create("m"));

    }

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

    /** Print the calendar for a month in a year */
    void printMonth(int year, int month) {
        printMonthTitle(year, month);
        printMonthBody(year, month);

        this.done = true;
    }

    /** Print the month title, e.g., May, 1999 */
    void printMonthTitle(int year, int month) {
        System.out.println("         " + getMonthName(month)
                + " " + year);
        System.out.println("-----------------------------");
        System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
    }

    /** Get the English name for the month */
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

    /** Print month body */
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

    /** Get the start day of month/1/year */
    int getStartDay(int year, int month) {
        final int START_DAY_FOR_JAN_1_1800 = 3;
        // Get total number of days from 1/1/1800 to month/1/year
        int totalNumberOfDays = getTotalNumberOfDays(year, month);

        // Return the start day for month/1/year
        return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
    }

    /** Get the total number of days since January 1, 1800 */
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

    /** Get the number of days in a month */
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

    /** Determine if it is a leap year */
    static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

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