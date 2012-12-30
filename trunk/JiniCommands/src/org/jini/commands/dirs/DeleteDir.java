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
 * Command to delete a Directory
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class DeleteDir extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    private boolean force;

    /**
     * Setter method of all Command Line options
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        // option -h --help : Help
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        // option -p --path 
        this.jcOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Path where the Directory will be created").hasArg().isRequired(false).create("p"));
    }

    /**
     * Execute Command
     */
    @Override
    public void executeCommand() {
        this.setJCLIOptions();



        CommandLine jcCmd = null;
        String args[] = this.convertToArray();
        try {
            jcCmd = this.jcParser.parse(this.jcOptions, args);
            
            // Check for -h option
            if (jcCmd.hasOption('h')) {
                 // Print the help
                printHelp();
            }

            // Check for -p option : Path
            if (jcCmd.hasOption('p')) {

                if (jcCmd.getOptionValue("p").length() == 0) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a directory to delete.");
                } else {

                    String delDirectory = jcCmd.getOptionValue("p");

                    if (this.isDir(delDirectory) == false) {
                        delDirectory = getWorkingDirectory() + File.separator + delDirectory;
                    }

                    if (this.isDir(delDirectory) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Directory [" + delDirectory + "] does not exsist.");
                    }

                    if ((this.getJcError() == false) && (this.done == false)) {
                        this.deleteDir(delDirectory);
                    }
                }
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    /**
     * Remove Directory
     * 
     * @param delDir 
     */
    
    private void deleteDir(String delDir) {
        File dDir = new File(delDir);

        if (removeDirectory(dDir) == true) {
            this.done = true;
            this.addResultMessages("Sucessfully Deleted Directory [" + delDir + "].");
        }

        if (dDir.exists()) {
            this.setJcError(true);
            this.addErrorMessages("Error : Unable to Delete Directory [" + delDir + "].");
        }

        this.done = true;
    }

    /**
     * Remove a directory and all of its contents.
     *
     * The results of executing File.delete() on a File object that represents a
     * directory seems to be platform dependent. This method removes the
     * directory and all of its contents.
     *
     * @return true if the complete directory was removed, false if it could not
     * be. If false is returned then some of the files in the directory may have
     * been removed.
     *
     */
    public boolean removeDirectory(File directory) {

        if (directory == null) {
            return false;
        }
        if (!directory.exists()) {
            return true;
        }
        if (!directory.isDirectory()) {
            return false;
        }

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                System.out.println("\t Deleted : " + entry);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry)) {
                        return false;
                    }
                } else {
                    if (!entry.delete()) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Unable to Delete [" + entry + "].");
                        return false;
                    }
                }
            }
        }

        return directory.delete();
    }

    /**
     * Directory tester
     *
     * @param directoryName
     * @return
     */
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

    /**
     * Getter method of the location of the user.
     *
     * @return
     */
    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {

        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "deldir");
        helpTableHead.addRow("SYNOPSIS : ", "deldir [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Deletes a Directory");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "makedir -h", "makedir --help");
        helpTable.addRow("-p", "--path", "required", "Absolute Path to the Directory you want deleted", "deldir -p c:\\MyDir", "deldir --path c:\\MyDir");
        helpTable.addRow("-p", "--path", "required", "Relative Path to the Directory you want deleted", "deldir -p MyDir", "deldir --path MyDir");

        helpTable.print();

        this.done = true;
    }
}