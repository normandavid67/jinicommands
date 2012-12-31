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
     * In this method all the specific Command Line options are defined.
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
     * In this method all the execution of the specific Command takes place
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
