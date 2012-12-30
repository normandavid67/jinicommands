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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.*;

/**
 * Copy Directory
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class JiniCopyDir extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    ArrayList<File> jcList = new ArrayList<File>();
    private boolean done;
    private String fromLocation;
    private String toLocation = getWorkingDirectory() + File.separator;

    public JiniCopyDir() {
    }

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("from").withDescription("From").hasArg(true).isRequired(false).create("f"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("to").withDescription("TO").hasArg(true).isRequired(false).create("t"));

    }

    @Override
    @SuppressWarnings("static-access")
    public void executeCommand() {

        this.setJCLIOptions();
        CommandLine jcCmd = null;
        String args[] = this.convertToArray();
        try {
            jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                printHelp();
            }



            if ((this.done == false) && (this.jcError == false)) {
                if (jcCmd.hasOption("f")) {
                    String fromVal = jcCmd.getOptionValue("f").trim();

                    if ((fromVal == null) || (fromVal.length() == 0)) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : No Option defined for -f (--from) ");
                    } else {
                        this.fromLocation = fromVal;
                    }

                    if (this.isDir(this.fromLocation) == false) {
                        this.fromLocation = getWorkingDirectory() + File.separator + this.fromLocation;
                    }
                } else {
                    this.setJcError(true);
                    this.addErrorMessages("Error : No Option defined for -f (--from) ");
                }

                if (jcCmd.hasOption("t")) {
                    String toVal = jcCmd.getOptionValue("t").trim();
                    if ((toVal == null) || (toVal.length() == 0)) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : No Option defined for -t (--to) ");
                    } else {
                        this.toLocation = toVal;
                    }

                    if (this.isDir(this.toLocation) == false) {
                        this.toLocation = getWorkingDirectory() + File.separator + this.toLocation;
                    }
                }


                
                if (this.jcError == false) {
                    this.compareToFromDirs();
                }

                if (this.jcError == false) {
                    try {
                        this.copySimpleDir();
                    } catch (IOException ex) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : " + ex.getMessage());
                    }
                }
            }
        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    void copySimpleDir() throws IOException {
        File srcDir = new File(this.fromLocation);
        File dstDir = new File(this.toLocation + File.separator + srcDir.getName());
        File dstDirTest = new File(this.toLocation + File.separator + srcDir.getName());
        if (dstDirTest.exists()) {

            dstDir = new File(this.toLocation + File.separator + createNewDirName(srcDir.getName()));
        }

        if ((this.done == false) && (this.jcError == false)) {
            this.copyDirectory(srcDir, dstDir);
        }
    }

    // Copies all files under srcDir to dstDir.
    // If dstDir does not exist, it will be created.
    public void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }

            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                this.copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }
        } else {
            // This method is implemented in Copying a File
            copyFile(srcDir, dstDir);
        }
    }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void compareToFromDirs() {

        if ((this.jcError == false) && (this.fromLocation == null)) {
            this.setJcError(true);
            this.addErrorMessages("Error : -f (--from) directory is not defined. ");
        }


        if ((this.jcError == false) && (this.toLocation == null)) {
            this.setJcError(true);
            this.addErrorMessages("Error : -t (--to) directory is not defined. ");
        }

        if ((this.jcError == false) && (this.toLocation.equals(this.fromLocation))) {
            this.setJcError(true);
            this.addErrorMessages("Error : -f (--from) and  -t (--to) directories are the same. ");
        }

    }

    private String createNewDirName(String FileName) {
        String tStamp = this.getTimeStamp();
        tStamp = tStamp.replaceAll(":", "-");

        return "(Copy " + tStamp + ")" + FileName;
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

    private String getTimeStamp() {
        return Calendar.getInstance().getTime().toString();
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "copydir");
        helpTableHead.addRow("SYNOPSIS : ", " copydir [OPTION]... [DIRECTORY]..");
        helpTableHead.addRow("DESCRIPTION : ", " Copy directory from one location to another.");
        helpTableHead.print();

        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "list -h", "list --help");
        helpTable.addRow("-f ", "--from", "required", "Define source directory", "copydir -f C:\\FOLDER1 ", "copydir -from C:\\FOLDER1 ");
        helpTable.addRow("-t ", "--to", "not required", "Define destination directory.", "copydir -t C:\\FOLDER1 ", "copydir -to C:\\FOLDER2 ");
        helpTable.addRow(" ", " ", " ", "If not defined then the directory is written in the present directory.", " ", " ");

        helpTable.print();

        this.done = true;

    }
}
