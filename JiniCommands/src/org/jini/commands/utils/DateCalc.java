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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;
/**
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */

public class DateCalc extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public DateCalc() {
    }

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        OptionGroup jcGroup = new OptionGroup();
        Option diff = new Option("diff", "difference", false, "Calculate Difference between dates.");
        diff.setArgs(2);
        jcGroup.addOption(diff);

        Option addDays = new Option("a", "add", false, "Add days to date.");
        addDays.setArgs(2);
        jcGroup.addOption(addDays);

        this.jcOptions.addOptionGroup(jcGroup);
    }

    @Override
    public void executeCommand() {
        this.setJCLIOptions();
        String args[] = this.convertToArray();

        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                this.printHelp();
            }

            if (jcCmd.hasOption("diff")) {
                String[] vals = jcCmd.getOptionValues("diff");

                if (vals.length != 2) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define 2 dates to calculate the difference.");
                }



                if (this.jcError == false) {
                    String d1 = vals[0];
                    String d2 = vals[1];

                    Date date1 = this.parseDate(d1);
                    Date date2 = this.parseDate(d2);

                    if (this.jcError == false) {
                        this.calcDateDifference(date1, date2);
                    }
                }
            }


            if (jcCmd.hasOption("a")) {
                String[] addVals = jcCmd.getOptionValues("a");

                if (addVals.length != 2) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a Date and number to add.");
                }



                if (this.jcError == false) {
                    String d1 = addVals[0];
                    int add = 0;
                    Date dateAdd = null;


                    try {
                        add = Integer.parseInt(addVals[1]);
                    } catch (Exception x) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : [" + addVals[1] + "] in not a number.");
                    }

                    if (this.jcError == false) {
                        dateAdd = this.parseDate(d1);
                    }

                    if (this.jcError == false) {
                        this.addDaysToDate(dateAdd, add);
                    }
                }
            }
        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private void addDaysToDate(Date date, int days) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);

        c1.add(Calendar.DATE, days);
        System.out.println("[" + days + "] days from date [" + sdf.format(date.getTime()) + "] the date will be : " + sdf.format(c1.getTime()));

        this.done = true;
    }

    private void calcDateDifference(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        if (this.jcError == false) {
            long diff = date1.getTime() - date2.getTime();

            System.out.println("Difference between [" + sdf.format(date1.getTime()) + "] and [" + sdf.format(date2.getTime()) + "] is " + (diff / (1000L * 60L * 60L * 24L)) + " days.");
        }
        this.done = true;


    }

    private Date parseDate(String date) {
        Date d = null;
       
        DateFormat df = DateFormat.getDateInstance();
        try {
            SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.yyyy");
            d = dateform.parse(date);
        } catch (Exception e) {

            this.setJcError(true);
            this.addErrorMessages("Error : [" + date + "] is not a date");
            System.out.println("Error: " + e.toString());
        }

        return d;
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "datecalc");
        helpTableHead.addRow("SYNOPSIS : ", "datecalc [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Perform Date calculations");
        helpTableHead.addRow("Example 1 : ", "datecalc ");

        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "date -h", "date --help");
        helpTable.addRow("-diff", "--difference", "2 dates required", "Calculate difference between 2 valid dates.", "datecalc -diff 27.7.1900 17.07.1900", "datecalc --difference 27.7.1900 17.07.1900");
        helpTable.addRow("-a", "--add", "2 arguments required (Date, Number)", "Caclulate the date after the number of defined days.", "datecalc -a 27.7.1900 10", "datecalc --add 27.7.1900 10");


        helpTable.print();

        this.done = true;
    }
}