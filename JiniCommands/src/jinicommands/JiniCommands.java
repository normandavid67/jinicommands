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
package jinicommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jini.commands.compress.Unzip;
import org.jini.commands.compress.ZipDirectory;
import org.jini.commands.dirs.ChangeDirectory;
import org.jini.commands.dirs.DeleteDir;
import org.jini.commands.dirs.JiniCopyDir;
import org.jini.commands.dirs.MakeDirectory;
import org.jini.commands.exceptions.JiniFatalError;
import org.jini.commands.files.FindFiles;
import org.jini.commands.files.JiniCopyFiles;
import org.jini.commands.files.List;
import org.jini.commands.info.*;
import org.jini.commands.utils.*;

/**
 * Starting point of JiniCommands In the main method the user input is read.
 *
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class JiniCommands {

    boolean cmdFound = false;
    static double minJVM = 1.6;
    static String prompt = "JiniCommands>";
    static String jiniVersion = "0.1";

    /**
     * Setter method for the user input and to sart executing the command
     *
     * @param JiniCmd jc
     * @param String[] args
     *
     */
    private void setArgsExec(JiniCmd jc, String[] args) {

        // Set the flag that the command was found.
        this.cmdFound = true;

        // Sets the user input
        jc.setArgs(args);

        // Executes the command
        jc.executeCommand();


        // Get the Error from the individual Command Class
        if (jc.getJcError() == true) {
            // Print out the Errors on the console
            System.err.println(jc.getErrorMessages());
        } else {
            // Print out the Result Messages
            System.out.println(jc.getResultMessages());
        }
    }

    /**
     * Constructor method. First we loop through all the Enums to find the
     * correct command User Command line arguments are then passed to the
     * executing class
     *
     * @param args
     */
    public JiniCommands(String[] args) {
        // Start only if there are more than 0 arguements
        if (args.length > 0) {
            // Loop through the Enums and read the values
            for (JCLI j : JCLI.values()) {
                // Compare the Enum to the Command line input
                if (j.getJiniCommand().equals(args[0])) {

                    // if found create a new object of the command class and 
                    // pass the Command line arguments to the new object                   
                    switch (j) {
                        // Lists files in a Directory
                        case List:
                            setArgsExec(new List(), args);
                            break;
                        // Prints your User Name
                        case WhoAmI:
                            setArgsExec(new WhoAmI(), args);
                            break;
                        // Prints your location
                        case WhereAmI:
                            setArgsExec(new WhereAmI(), args);
                            break;
                        // Change Location
                        case ChangeDirectory:
                            setArgsExec(new ChangeDirectory(), args);
                            break;
                        // Prints present Date
                        case JiniDate:
                            setArgsExec(new JiniDate(), args);
                            break;
                        // Clears the prompt
                        case Clear:
                            setArgsExec(new Clear(), args);
                            break;
                        // Exits JiniCommands
                        case JiniCommandsExit:
                            setArgsExec(new JiniCommandsExit(), args);
                            break;
                        // Copy file/s from one location to another.
                        case JiniCopyFiles:
                            setArgsExec(new JiniCopyFiles(), args);
                            break;
                        // Copy file/s from one location to another. Alias of JiniCopyFiles
                        case JiniCopyFile:
                            setArgsExec(new JiniCopyFiles(), args);
                            break;
                        // Copies Directories from one location to another. Including Subdirectories
                        case JiniCopyDir:
                            setArgsExec(new JiniCopyDir(), args);
                            break;
                        // Prints a list of all Jini Commands
                        case JiniHelp:
                            setArgsExec(new JiniHelp(), args);
                            break;
                        // Prints a list of all Jini Commands (alias of 'help')
                        case JiniQuestion:
                            setArgsExec(new JiniHelp(), args);
                            break;
                        //  Prints OS Information
                        case OS:
                            setArgsExec(new OS(), args);
                            break;
                        // Prints Java Virtual Machine Information
                        case Java:
                            setArgsExec(new Java(), args);
                            break;
                        // Compresses a Directory to a ZIP file
                        case JiniZipDir:
                            setArgsExec(new ZipDirectory(), args);
                            break;
                        // Decompress a Zipped file
                        case JiniUnZip:
                            setArgsExec(new Unzip(), args);
                            break;
                        // Create a Directory    
                        case MakeDirectory:
                            setArgsExec(new MakeDirectory(), args);
                            break;
                        // Delete a Directory
                        case DeleteDir:
                            setArgsExec(new DeleteDir(), args);
                            break;
                        // Prints Calendar    
                        case PrintCalendar:
                            setArgsExec(new PrintCalendar(), args);
                            break;
                        // Search for File/s
                        case FindFiles:
                            setArgsExec(new FindFiles(), args);
                            break;
                        // Search for File/s    
                        case FindFile:
                            setArgsExec(new FindFiles(), args);
                            break;
                        // Date Calculations
                        case DateCalc:
                            setArgsExec(new DateCalc(), args);
                            break;
                        // Prints the time & date in a Time Zone    
                        case TimeInZone:
                            setArgsExec(new TimeInZone(), args);
                            break;
                        // List Roots    
                        case ListRoots:
                            setArgsExec(new ListRoots(), args);
                            break;
                        // List Roots. Alias of ListRoots
                        case ListDrives:
                            setArgsExec(new ListRoots(), args);
                            break;
                        // Convert text/file from text to Binary and Binary to text.    
                        case TextConvert:
                            setArgsExec(new TextConvert(), args);
                            break;
                        // Print JiniCommands License    
                        case JiniLicense:
                            setArgsExec(new JiniLicense(), args);
                            break;
                       // Print JiniCommands License    
                        case JiniAbout:
                            setArgsExec(new About(), args);
                            break;
                    }
                }
            }
        }
        // If no command is found. Print this default error message.
        if (this.cmdFound == false) {
            System.out.println("Error : Command not found. Type 'help' for a list of commands.");
        }
    }

    /**
     * Convert a String array to String
     *
     * @param a
     * @param separator
     * @return result
     */
    public static String arrayToString(String[] a, String separator) {
        if (a == null || separator == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        if (a.length > 0) {
            result.append(a[0]);
            for (int i = 1; i < a.length; i++) {
                result.append(separator);
                result.append(a[i]);
            }
        }
        return result.toString();
    }

    /**
     * Check the JVM Version. (Minimum 1.6)
     *
     * @throws JiniFatalError
     */
    private static void checkJVM() throws JiniFatalError {

        String java = System.getProperty("java.specification.version");
        String javaHome = System.getProperty("java.home");
        double version = Double.valueOf(java);

        if (version < minJVM) {
            throw new JiniFatalError("You need at least Java " + minJVM + " to run JiniCommands. Found Java : " + version + " [" + javaHome + "]");
        }
    }

    /**
     * Prints out this default message at the start of the application.
     *
     */
    private static void printInitialMessage() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy");

        System.out.println("JiniCommands version " + JiniLicense.getJiniCommandsVersion() + ", Copyright (C) " + ft.format(date) + " Norman David");
        System.out.println("");
        System.out.println("Disclaimer : This program is distributed in the hope that it will be useful,");
        System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  ");

        System.out.println("");
        System.out.println("Type 'about' for information on JiniCommands.");
        System.out.println("Type 'help' or '?' for a list of all commands.");
        System.out.println("Type 'license' for License details.");
        System.out.println("");
    }

    /**
     * Returns the JiniCommands Version
     *
     * @return jiniVersion
     */
    public static String getJiniCommandsVersion() {
        return jiniVersion;
    }

    /**
     * main method of Class JiniCommands also the starting point of the
     * application
     *
     * @param args
     * @throws JiniFatalError
     */
    public static void main(String[] args) throws JiniFatalError {

        // Check the JVM Version. (Minimum 1.6)
        checkJVM();

        // Print the initial message
        printInitialMessage();

        // Print out the prompt
        System.out.print(prompt);

        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = br.readLine()) != null) {

                // Remove trailing whitespace
                line = line.trim();

                // Do not process anything if the line is empty
                if (line.isEmpty() == false) {
                    String args3[] = line.split(" ");

                    for (int i = 0; i < args3.length; i++) {
                        args3[i] = args3[i].trim();
                    }

                    // Create an new instance of JiniCommands and pass the incomming CLI input 
                    JiniCommands jCds = new JiniCommands(args3);
                    jCds.toString();

                    // Print out the prompt
                    System.out.print(prompt);
                }
            }
        } catch (IOException ioe) {
            throw new JiniFatalError(ioe.toString());
        }
    }
}
