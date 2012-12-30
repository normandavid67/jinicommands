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

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
        this.jcOptions.addOption(OptionBuilder.withLongOpt("search").withDescription("Search").isRequired(false).hasArg().create("s"));

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
            helpTableHead.addRow(x + "", j.getJiniCommand(), j.getShortDesc(), j.getJiniCommand()+" --help");
            x++;
        }

        helpTableHead.print();

        this.done = true;
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "help");
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
        if ((search != null)  && (search.length() > 0)) {

            ArrayList<JCLI> res = new ArrayList<JCLI>();

            for (JCLI j : JCLI.values()) {
                String command = j.getJiniCommand();
                String commandDesc = j.getShortDesc();

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
