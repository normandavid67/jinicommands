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

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import jinicommands.JiniCmd;
import org.apache.commons.cli.*;
import org.jini.commands.helper.TablePrinter;
import org.jini.commands.utils.PrintChar;

/**
 *
 * Compress a Directory in ZIP
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class ZipDirectory extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    String directoryToZip = "";

    /**
     * In this method all the Command specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
        this.jcOptions.addOption(OptionBuilder.withLongOpt("directory").withDescription("Directory to zip").hasArg(true).isRequired(false).create("d"));
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

            // Read the -h option
            if (jcCmd.hasOption('h')) {
                // Print help
                printHelp();
            }

            if ((this.done == false) && (this.jcError == false)) {

                // read the -d option
                if (jcCmd.hasOption("d")) {
                    String dir = jcCmd.getOptionValue("d").trim();

                    // check if the option is not null or empty
                    if ((dir == null) || (dir.length() == 0)) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : No Option defined for -d (--directory) ");
                    }


                    // check if the option is a directory
                    // with this the user does not have to give a full path.
                    if (this.isDir(dir) == false) {
                        dir = getWorkingDirectory() + File.separator + dir;
                    }

                    if (this.isDir(dir) == false) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : " + dir + " is not a directory. ");
                    }

                    if (this.jcError == false) {
                        // Helper class to print chars
                        PrintChar printChar = new PrintChar();
                        printChar.setOutPutChar(">");

                        String dirName = this.getDirName(dir);
                        // create a path to a OS specific temp directory
                        String tempZipFileName = this.getOsSpecificTempDirectory() + File.separator + "" + dirName + ".zip";


                        // Create a zipped file in the temporary location
                        File zft = new File(tempZipFileName);

                        try {
                            //Start printing a series of > till the directory is zipped
                            printChar.start();

                            this.zipDirectory(new File(dir), zft);
                            System.out.println("Zipped Directory : " + dir);
                            System.out.println("Created Zip File : " + tempZipFileName);

                            // If all is okay move the file to the working Directory
                            if (this.jcError == false) {
                                this.moveFile(tempZipFileName, getWorkingDirectory() + File.separator + dirName + ".zip");
                            }

                            // Stop printing the chars
                            printChar.setStopPrinting(true);

                        } catch (IOException ex) {
                            this.setJcError(true);
                            this.addErrorMessages("Error : " + ex.toString());
                        }
                    }

                } else {
                    this.setJcError(true);
                    this.addErrorMessages("Error : No Option defined for -d (--directory) ");
                }
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    /**
     * Move file from one location to another
     *
     *
     * @param fromLocation
     * @param toLocation
     */
    private void moveFile(String fromLocation, String toLocation) {

        try {

            File fromfile = new File(fromLocation);
            File tofile = new File(toLocation);

            InputStream inStream = new FileInputStream(fromfile);
            OutputStream outStream = new FileOutputStream(tofile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.close();

            //delete the original file
            fromfile.delete();

            System.out.println("File was moved successful!");

        } catch (IOException e) {

            this.setJcError(true);
            this.addErrorMessages("Error : " + e.toString());
        }

    }

    /**
     * Getter method for OS specific temporary file
     *
     * @return
     */
    private String getOsSpecificTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Zip a directory
     *
     * @param directory
     * @param zip
     * @throws IOException
     */
    public void zipDirectory(File directory, File zip) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
        zip(directory, directory, zos);
        zos.close();

        this.done = true;
    }

    /**
     * zip helper method
     *
     * @param directory
     * @param base
     * @param zos
     * @throws IOException
     */
    private void zip(File directory, File base, ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                        base.getPath().length() + 1));
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
        }
    }

    /**
     * Unzip file
     *
     * Note: unused in this command
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
     * Print out all the options of zipdir
     *
     */
    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "zipdir");
        helpTableHead.addRow("SYNOPSIS : ", "zipdir [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Zip a directory. ");
        helpTableHead.addRow(" ", "Note : There must be enough space in the temporary directory of your OS. Temp Directory of your OS : " + this.getOsSpecificTempDirectory());
        helpTableHead.addRow(" ", "First the zip file is created in the Temp Directory and then moved to your location. Type 'whereami' for your location. ");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "zipdir -h", "zipdir --help");
        helpTable.addRow("-d", "--directory", " required", "Zip a directory", "zipdir -d C:\\FOLDER1 ", "zipdir -d C:\\FOLDER1");
        helpTable.print();


        this.done = true;
    }

    /**
     * Getter method of the users working directory
     *
     * @return
     */
    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * Directory test method
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
     * Getter method of directory name
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
}
