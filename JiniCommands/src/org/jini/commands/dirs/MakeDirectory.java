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
package org.jini.commands.dirs;

import java.io.File;
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
public class MakeDirectory extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Path where the Directory will be created").hasArg().isRequired(false).create("p"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("name").withDescription("Name of the Directory to be created").hasArg().isRequired(false).create("n"));


    }

    @Override
    public void executeCommand() {
        this.setJCLIOptions();
        String dirLocation = getWorkingDirectory();
        String dirName = "";

        CommandLine jcCmd = null;
        String args[] = this.convertToArray();
        try {
            jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                printHelp();
            }

            if (jcCmd.hasOption('p')) {
                dirLocation = jcCmd.getOptionValue("p").trim();

                if ((dirLocation != null) && (dirLocation.length() > 0)) {
                    if (this.isDir(dirLocation) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Directory [" + dirLocation + "] does not exsist.");
                    }
                }
            }

            if (jcCmd.hasOption('n')) {
                dirName = jcCmd.getOptionValue("n").trim();
            }

            if ((this.done == false) && ((dirName == null) || (dirName.length() == 0))) {
                this.setJcError(true);
                this.addErrorMessages("Error : Please define a directory name to create. Help type 'makedir -h' ");
            }


            if ((this.getJcError() == false) && (this.done == false)) {
                String newDirName = dirLocation + File.separator + dirName;
                this.createDirectory(newDirName);
            }


        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private void createDirectory(String destinationDir) {
        File dstDir = new File(destinationDir);

        if (!dstDir.exists()) {
            dstDir.mkdir();
            
            if (dstDir.exists()) {
                this.addResultMessages("Sucessfully created dir " + destinationDir);
            }
        } else {
            this.setJcError(true);
            this.addErrorMessages("Error : Directory exsists [" + destinationDir + "].");
        }
    }

    private boolean isDir(String directoryName) {
        if ((directoryName != null) && (directoryName.length() > 0)) {
            File dirTest = new File(directoryName);
            if (dirTest.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    private void printHelp() {
       
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "makedir");
        helpTableHead.addRow("SYNOPSIS : ", "makedir [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Creates a new Directory");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "makedir -h", "makedir --help");
        helpTable.addRow("-p", "--path", "required", "Path where the Directory will be created", "makedir -p c:\\", "makedir --path");
        helpTable.addRow("-n", "--name", "required", "Name of the Directory to be created", "makedir -p c:\\ -n NewDirectory", "makedir --path c:\\ --name NewDirectory");
        helpTable.print();

        this.done = true;
    }
}