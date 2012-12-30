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

import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;


/**
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class WhoAmI extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    boolean doneFlag = false;

    public WhoAmI() {
    }

    @Override
    public void setJCLIOptions() {

        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
    }

    @Override
    public void executeCommand() {
        this.setJCLIOptions();
        String args[] = this.convertToArray();

        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                printHelp();
                this.doneFlag = true;
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.jcError = true;
            this.addErrorMessages("Error :" + ex.getMessage());
        }

        if ((this.doneFlag == false) && (this.jcError == false)) {
            this.printUserInfo();
        }
    }

    private void printUserInfo() {
        String userName = System.getProperty("user.name");
        String userHome = System.getProperty("user.home");
        String userLang = System.getProperty("user.language");

        TablePrinter helpTableHead = new TablePrinter("User Info : ", "");
        if ((userName != null) && (userName.isEmpty() == false)) {
            helpTableHead.addRow("User Name : ", userName);
        }
        if ((userHome != null) && (userHome.isEmpty() == false)) {
            helpTableHead.addRow("Home Directory : ", userHome);
        }
        if ((userLang != null) && (userLang.isEmpty() == false)) {
            helpTableHead.addRow("Language : ", userLang);
        }

        helpTableHead.print();
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "whoami");
        helpTableHead.addRow("SYNOPSIS : ", " whoami [OPTION]... ");
        helpTableHead.addRow("DESCRIPTION : ", " Prints a table of Username, Home Directory & Language of the User");
        helpTableHead.print();

        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "list -h", "list --help");

        helpTable.print();

    }
}
