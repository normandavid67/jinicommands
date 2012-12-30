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
package org.jini.commands.compress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.Filename;
import org.jini.commands.helper.TablePrinter;

/**
 * Decompress a Zip File
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class Unzip extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    String directoryToZip = "";

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
        // option -f --file : File to unzip
        this.jcOptions.addOption(OptionBuilder.withLongOpt("file").withDescription("File to unzip").hasArg(true).isRequired(false).create("f"));
    }

    /**
     * Execute Command
     */
    @Override
    @SuppressWarnings("static-access")
    public void executeCommand() {

        this.setJCLIOptions();
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);

            // Check for -h option
            if (jcCmd.hasOption('h')) {
                // Print the help
                printHelp();
            }

            
            if ((this.done == false) && (this.jcError == false)) {
            
                // the -f --file option
                if (jcCmd.hasOption("f")) {
                    String file = jcCmd.getOptionValue("f").trim();

                    if ((file == null) || (file.length() == 0)) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : No Option defined for -f (--file) ");
                    }

                    if (this.isFile(file) == false) {
                        file = getWorkingDirectory() + File.separator + file;
                    }

                    if (this.isFile(file) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : " + file + " is not a valid file. ");
                    }

                    if (this.jcError == false) {

                        // create the to location path
                        String toLocationPath = getWorkingDirectory() + File.separator + getToFolderName(file);
                        // create the directory 
                        createDirectory(toLocationPath);

                        File zipFile = new File(file);
                        File toLocation = new File(toLocationPath);

                        try {
                            // Send options to the unzip method
                            this.unzip(zipFile, toLocation);
                        } catch (IOException ex) {
                            this.setJcError(true);
                            this.addErrorMessages("Error : " + ex.toString());
                        }
                    }
                } else {
                    this.setJcError(true);
                    this.addErrorMessages("Error : No Option defined for -f (--file)");
                }
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }
    

    /**
     * Get the name of the zipped file without the extension. 
     * 
     * @param toLocation
     * @return 
     */
    private String getToFolderName(String toLocation) {
        if ((toLocation != null) || (toLocation.length() > 0)) {
            Filename fn = new Filename(toLocation, '.');

            return fn.filename();
        }
        return "__DEFAULT__";
    }

    /**
     * Method where the zipped file will be decompressed
     * 
     * @param zip
     * @param extractTo
     * @throws IOException 
     */
    public void unzip(File zip, File extractTo) throws IOException {
        ZipFile archive = new ZipFile(zip);
        Enumeration e = archive.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            File file = new File(extractTo, entry.getName());
            if (entry.isDirectory() && !file.exists()) {
                file.mkdirs();
            } else {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                InputStream in = archive.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(file));

                byte[] buffer = new byte[8192];
                int read;

                while (-1 != (read = in.read(buffer))) {
                    out.write(buffer, 0, read);
                }

                in.close();
                out.close();
            }
        }
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "unzip");
        helpTableHead.addRow("SYNOPSIS : ", "unzip [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "UnZip a file. The File will expanded in the Location where you are. Type 'whereami' for your location. ");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "unzip -h", "unzip --help");
        helpTable.addRow("-f", "--file", " required", "Unzip a file", "unzip -f FileName.zip ", "unzip -f FileName.zip ");
        helpTable.print();


        this.done = true;
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
     * Check if a given path is a Directory or not
     *
     * @param String directoryName
     * @return Boolean
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
     * File Check
     * 
     * @param file
     * @return 
     */

    private boolean isFile(String file) {
        if ((file != null) && (file.length() > 0)) {
            File fileTest = new File(file);

            if (fileTest.isFile()) {

                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    
    /**
     * Get Directory name from path
     * 
     * @param path
     * @return 
     */

    private String getDirName(String path) {
        if ((path != null) && (path.length() > 0)) {
            File f = new File(path);
            return f.getName();
        }
        return "";
    }
    
    /**
     * Create a directory
     * 
     * @param destinationDir 
     */

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
}
