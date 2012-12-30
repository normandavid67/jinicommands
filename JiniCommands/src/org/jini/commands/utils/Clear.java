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
 */
package org.jini.commands.utils;

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

public class Clear extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    int lines = 90;
    private boolean done;

    public Clear() {
    }

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("lines").withDescription("Lines to clear").isRequired(false).hasArg().create("l"));
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

            if (jcCmd.hasOption('l')) {
                try {
                    this.lines = Integer.parseInt(jcCmd.getOptionValue("l"));
                } catch (Exception ex) {
                    this.setJcError(true);
                    this.addErrorMessages("Error :" + ex.getMessage());
                }

            }
            if (this.done == false) {
                this.printNewLines();
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private void printNewLines() {
        for (int l = 0; l < this.lines; l++) {
            System.out.println("");
        }

        this.done = true;
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "clear");
        helpTableHead.addRow("SYNOPSIS : ", "clear [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Clear the console. ");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "clear -h", "clear --help");
        helpTable.addRow("-l", "--lines", "required", "Number of Lines to clear.", "clear -l 25", "clear --lines 25");
        helpTable.print();
        this.done = true;
    }
}
