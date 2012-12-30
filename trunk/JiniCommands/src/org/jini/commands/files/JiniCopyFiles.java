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
package org.jini.commands.files;

import java.io.File;
import java.io.FileFilter;
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
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jini.commands.files.filters.HiddenFileFilter;
import org.jini.commands.files.filters.SimpleFileFilter;
import org.jini.commands.helper.TablePrinter;
/**
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class JiniCopyFiles extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    ArrayList<File> jcList = new ArrayList<File>();
    boolean copyAll = false;
    private boolean done;
    private String fromLocation;
    private String toLocation;
    private boolean hiddenfiles;
    private boolean wildcard;
    private String wildcardOpt;
    private boolean verbose;

    void addFileList(File file) {
        this.jcList.add(file);
    }

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("all").withDescription("All files starting with . ").isRequired(false).create("a"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Verbose").isRequired(false).create("v"));

        OptionGroup jcGroup = new OptionGroup();
        jcGroup.addOption(OptionBuilder.withLongOpt("hiddenfiles").withDescription("Copy Hidden Files").isRequired(false).create("hf"));
        jcGroup.addOption(OptionBuilder.withLongOpt("wildcard").withDescription("Wild Card Option").hasArg(true).isRequired(false).create("wc"));
        this.jcOptions.addOptionGroup(jcGroup);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("from").withDescription("From").hasArg(true).isRequired(false).create("f"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("to").withDescription("TO").hasArg(true).isRequired(false).create("t"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("verbose").withDescription("Verbose").isRequired(false).create("v"));

    }

    @Override
    @SuppressWarnings("static-access")
    public void executeCommand() {
        this.setJCLIOptions();
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {
                printHelp();
            }

            if (jcCmd.hasOption("a")) {
                this.copyAll = true;
            }

            if (jcCmd.hasOption("f")) {
                String fromVal = jcCmd.getOptionValue("f").trim();

                if ((fromVal != null) || (fromVal.length() != 0)) {
                    this.fromLocation = fromVal;
                }

                if (this.isDir(this.fromLocation) == false) {
                    this.fromLocation = getWorkingDirectory() + File.separator + this.fromLocation;
                }
            }

            if (jcCmd.hasOption("t")) {
                String toVal = jcCmd.getOptionValue("t").trim();
                if ((toVal != null) || (toVal.length() != 0)) {
                    this.toLocation = toVal;
                }

                if (this.isDir(this.toLocation) == false) {
                    this.toLocation = getWorkingDirectory() + File.separator + this.toLocation;
                }

            }


            if (jcCmd.hasOption("hf")) {
                this.hiddenfiles = true;
            }

            if (jcCmd.hasOption("wc")) {
                this.wildcard = true;
                this.wildcardOpt = jcCmd.getOptionValue("wc");
            }
            if (jcCmd.hasOption("v")) {
                this.verbose = true;
            }

            if ((this.done == false) && (this.jcError == false)) {
                if (this.isDir(this.toLocation) == false) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : " + this.toLocation + " is not a Directory ");
                }

                if (this.isDir(this.fromLocation) == false) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : " + this.fromLocation + " is not a Directory ");
                }

                if (this.jcError == false) {
                    this.compareToFromDirs();
                }

                if ((this.done == false) && (this.jcError == false) && (this.hiddenfiles == true) && (this.wildcard == false)) {
                    this.copyHiddenFiles();
                }

                if ((this.done == false) && (this.jcError == false) && (this.hiddenfiles == false) && (this.wildcard == true)) {
                    this.copyWildCardFiles();
                }

                if ((this.done == false) && (this.jcError == false) && (this.hiddenfiles == false) && (this.wildcard == false)) {
                    this.copySimpleFiles();
                }
            }


        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }

    }

    private void copyWildCardFiles() {
        try {
            this.getWildCardFileList();

            for (File v : this.jcList) {

                File fromFile = new File(this.fromLocation + File.separator + v.getName());
                File toFile = new File(this.toLocation + File.separator + v.getName());

                if (toFile.exists()) {
                    toFile = new File(this.toLocation + File.separator + this.createNewFileName(v.getName()));
                }
                this.copy(fromFile, toFile);
                if (this.verbose) {
                    System.out.println("Copied file " + fromFile + " to " + toFile + "");
                }
            }

            this.done = true;
        } catch (IOException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private void copyHiddenFiles() {
        try {
            this.getHiddenFileList();

            for (File v : this.jcList) {

                File fromFile = new File(this.fromLocation + File.separator + v.getName());
                File toFile = new File(this.toLocation + File.separator + v.getName());

                if (toFile.exists()) {
                    toFile = new File(this.toLocation + File.separator + this.createNewFileName(v.getName()));
                }
                this.copy(fromFile, toFile);
                if (this.verbose) {
                    System.out.println("Copied Hidden file " + fromFile + " to " + toFile + "");
                }
            }

            this.done = true;
        } catch (IOException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private void copySimpleFiles() {
        try {
            this.getSimpleFileList();

            for (File v : this.jcList) {

                File fromFile = new File(this.fromLocation + File.separator + v.getName());
                File toFile = new File(this.toLocation + File.separator + v.getName());

                if (toFile.exists()) {
                    toFile = new File(this.toLocation + File.separator + this.createNewFileName(v.getName()));

                }

                this.copy(fromFile, toFile);

                if (this.verbose) {
                    System.out.println("Copied file " + fromFile + " to " + toFile + "");
                }
            }

            this.done = true;
        } catch (IOException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private String createNewFileName(String FileName) {

        String tStamp = this.getTimeStamp();
        tStamp = tStamp.replaceAll(":", "-");

        return "(Copy " + tStamp + ")" + FileName;

    }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    private void copy(File src, File dst) throws IOException {
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
            this.addErrorMessages("Error : Source (from) directory is not defined. ");
        }


        if ((this.jcError == false) && (this.toLocation == null)) {
            this.setJcError(true);
            this.addErrorMessages("Error : Destination (to) directory is not defined. ");
        }

        if ((this.jcError == false) && (this.toLocation.equals(this.fromLocation))) {
            this.setJcError(true);
            this.addErrorMessages("Error : Source (from) and Destination (to) directories are the same. ");
        }

    }

    private boolean checkFromDir(String directoryName) {
        if ((directoryName != null) && (directoryName.length() > 0)) {
            File dirTest = new File(directoryName);
            if (dirTest.isDirectory() == false) {
                return false;
            } else {
                return true;
            }
        }

        return false;
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

    private File[] getFromFileArray(FileFilter fileFilter) {
        File dir = new File(this.fromLocation);
        File[] files = dir.listFiles(fileFilter);
        return files;
    }

    private void getSimpleFileList() throws IOException {
        if ((this.getJcError() == false) && (this.done == false)) {
            File[] files = this.getFromFileArray(new SimpleFileFilter(this.copyAll));

            for (File f : files) {
                this.addFileList(f);
            }

        }
    }

    private void getHiddenFileList() throws IOException {
        if ((this.getJcError() == false) && (this.done == false)) {
            File[] files = this.getFromFileArray(new HiddenFileFilter(this.copyAll));

            for (File f : files) {
                this.addFileList(f);
            }
        }
    }

    private void getWildCardFileList() throws IOException {
        if ((this.getJcError() == false) && (this.done == false)) {
            File[] files = this.getFromFileArray(new WildcardFileFilter(this.wildcardOpt));

            for (File f : files) {
                this.addFileList(f);
            }
        }
    }

    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    private String getTimeStamp() {
        return Calendar.getInstance().getTime().toString();
    }

    private void printHelp() {

        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "copyfiles");
        helpTableHead.addRow("SYNOPSIS : ", "copyfiles [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Copy file/s from one directory to another.");
        helpTableHead.addRow("Note : ", "If file of the same name exsists then this text (DAY MONTH DAY HOUR-MINUTES-SECONDS TIME_ZONE YEAR) wll be added before the file name. ");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "copyfiles -h", "copyfiles --help");
        helpTable.addRow("-f ", "--from", "required", "Define source directory", "copyfiles -f C:\\FOLDER1 ", "copyfiles -from C:\\FOLDER1 ");
        helpTable.addRow("-t ", "--to", "required", "Define destination directory", "copyfiles -t C:\\FOLDER1 ", "copyfiles -to C:\\FOLDER2 ");
        helpTable.addRow("-all", "--all", "not required", "Include files whose name starts with .", "copyfiles -f C:\\FOLDER1 -t C:\\FOLDER2 --all", "copyfiles -from C:\\FOLDER1 -to C:\\FOLDER2 --all");
        helpTable.addRow("-v", "--verbose", "not required", "Print information of the files copied.", "copyfiles -f C:\\FOLDER1 -t C:\\FOLDER2 -v", "copyfiles -from C:\\FOLDER1 -to C:\\FOLDER2 --verbose");
        helpTable.addRow("-hf", "--hiddenfiles", "not required", "Copy Hidden files only", "copyfiles -f C:\\FOLDER1 -t C:\\FOLDER1 -hf", "copyfiles -from C:\\FOLDER1 -to C:\\FOLDER2 --hiddenfiles");
        helpTable.addRow("-wc", "--wildcard", "required", "Copy files according to the WildCard definations", "copyfiles -f C:\\FOLDER1 -t C:\\FOLDER2 -wc *.txt", "copyfiles -from C:\\FOLDER1 -to C:\\FOLDER2 --wildcard *.txt");
        helpTable.print();

        TablePrinter helpTableFoot = new TablePrinter("Option Groups : ", "");
        helpTableFoot.addRow("--hiddenfiles --wildcard", " These options are grouped. i.e only one can be defined at a given time.");

        helpTableFoot.print();

        this.done = true;
    }
}
