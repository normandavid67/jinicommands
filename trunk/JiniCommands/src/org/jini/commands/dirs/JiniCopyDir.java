/*
 New BSD License
 Copyright (c) 2012, Norman David <normandavid67@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Norman David BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
  
 For a copy of the License type 'license'
 */
package org.jini.commands.dirs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private boolean done;
    private String fromLocation;
    private String toLocation = getWorkingDirectory() + File.separator;

    public JiniCopyDir() {
    }

    /**
     * In this method all the Command specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("from").withDescription("From").hasArg(true).isRequired(false).create("f"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("to").withDescription("TO").hasArg(true).isRequired(false).create("t"));

    }

    /**
     * In this method all the execution of the specific Command takes place
     */
    @Override
    @SuppressWarnings("static-access")
    public void executeCommand() {

        this.setJCLIOptions();
        //CommandLine jcCmd = null;
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);

            // Check for -h option
            if (jcCmd.hasOption('h')) {
                // Print the help
                printHelp();
            }



            if ((this.done == false) && (this.jcError == false)) {

                // Check for -f option : --from
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


                // Check for -t option : --to
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
                    // Compare the To From Directories
                    this.compareToFromDirs();
                }

                if (this.jcError == false) {
                    try {
                        // No Error copy the directories
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

    /**
     * Copy Directory
     *
     * @throws IOException
     */
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

    /**
     * Copies all files under srcDir to dstDir. If dstDir does not exist, it
     * will be created.
     *
     * @param srcDir
     * @param dstDir
     * @throws IOException
     */
    public void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                if (dstDir.mkdir() == false) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Could not create Directory " + dstDir.getAbsolutePath());
                }
            }

            if (this.jcError == false) {
                String[] children = srcDir.list();
                for (int i = 0; i < children.length; i++) {
                    this.copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
                }
            }
        } else {
            // This method is implemented in Copying a File
            copyFile(srcDir, dstDir);
        }
    }

    /**
     * Copies src file to dst file. If the dst file does not exist, it is
     * created
     *
     * @param src
     * @param dst
     * @throws IOException
     */
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

    /**
     * Compare To From Directories
     *
     */
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

    /**
     * Create a new Directory Name
     *
     * @param FileName
     * @return
     */
    private String createNewDirName(String FileName) {
        String tStamp = this.getTimeStamp();
        tStamp = tStamp.replaceAll(":", "-");

        return "(Copy " + tStamp + ")" + FileName;
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
     * Get a timestamp
     *
     * @return
     */
    private String getTimeStamp() {
        return Calendar.getInstance().getTime().toString();
    }

    /**
     * Prints out all Command Line Options in a table
     *
     */
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
