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

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
        this.jcOptions.addOption(OptionBuilder.withLongOpt("format").withDescription("Format").hasArg().hasOptionalArg().isRequired(false).create("f"));

    }

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
