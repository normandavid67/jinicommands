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


/**
 * Each element represents a JiniCommand. A short Description is provided for Command Line Interface.
 * Each Command provided a more detailed help.
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public enum JCLI {
    /**
     * Lists files in a Directory
     */
    List("list", "Lists Files / Directories"),
    /**
     * Copy file/s from one location to another.
     */
    JiniCopyFiles("copyfiles", "Copies file/s from one location to another."),
    /**
     * Copy file/s from one location to another. Alias of JiniCopyFiles
     */
    JiniCopyFile("copyfile", "Copies file/s from one location to another (alias of 'copyfiles')"), // Alias of JiniCopyFiles

    /**
     * Change Location
     */
    ChangeDirectory("cd", "Change Directory"),
    /**
     * Copies Directories from one location to another. Including Subdirectories
     */
    JiniCopyDir("copydir", "Copies Directories from one location to another"),
    /**
     * Create a Directory
     */
    MakeDirectory("makedir", "Create a Directory"),
    /**
     * Deletes a Directory. Including Subdirectories.
     */
    DeleteDir("deldir", "Deletes a Directory"),
    /**
     * Search for File/s
     */
    FindFiles("findfiles", "Search for File/s"),
    /**
     * Search for File/s. Alias of findfiles
     */
    FindFile("findfile", "Search for File/s (alias of 'findfiles')"),
    /**
     * Prints Java Virtual Machine Information
     */
    Java("javainfo", "Prints Java Virtual Machine Information"),
    /**
     * Prints a list of all Jini Commands
     */
    JiniHelp("help", "Prints a list of all Jini Commands"),
    
    
    /**
     * Prints a list of all Jini Commands
     */
    JiniQuestion("?", "Prints a list of all Jini Commands (alias of 'help')"),
    
    /**
     * Prints a list of all Jini Commands
     */
    JiniAbout("about", "Prints short information of JiniCommands"),
    
    /**
     * List Roots
     */
    ListRoots("listroots", "List Roots"),
    /**
     * List Roots. Alias of ListRoots
     */
    ListDrives("listdrives", "List Roots"),
    /**
     * Prints OS Information
     */
    OS("osinfo", "Prints OS Information"),
    /**
     * Prints your location
     */
    WhereAmI("whereami", "Prints your location"),
    /**
     * Prints your User Name
     */
    WhoAmI("whoami", "Prints your User Name"),
    /**
     * Prints present Date
     */
    JiniDate("date", "Prints present Date"),
    /**
     * Date Calculations
     */
    DateCalc("datecalc", "Date Calculations"),
    /**
     * Prints Calendar
     */
    PrintCalendar("calendar", "Prints Calendar"),
    /**
     * Prints the time & date in a Time Zone
     */
    TimeInZone("timeinzone", "Prints the time & date in a Time Zone"),
    /**
     * Convert text/file from text to Binary and Binary to text.
     */
    TextConvert("textconvert", "Convert text/file from to Binary"),
    /**
     * Clears the prompt
     */
    Clear("clear", "Clears the prompt"),
    /**
     * Exits JiniCommands
     */
    JiniCommandsExit("exit", "Exit JiniCommands"),
    /**
     * Compresses a Directory to a ZIP file
     */
    JiniZipDir("zipdir", "Compresses a Directory to a ZIP file"),
    /**
     * Unzip a Zipped file
     */
    JiniUnZip("unzip", "Unzip a Zipped file"),
    /**
     * Print JiniCommands License
     */
    JiniLicense("license", "Print JiniCommands License"),
    /**
     * Start a MultiCast 
     */
    MulticastSender("multicastsender", "Multcast Sender. Multicast data at regular intervals.");
    
    private String jiniCommand;
    private String shortDesc;
    
    /**
     * Set the command name and short description
     * 
     * @param jiniCommand
     * @param shortDesc 
     */

    JCLI(String jiniCommand, String shortDesc) {
        this.jiniCommand = jiniCommand;
        this.shortDesc = shortDesc;
    }

    /**
     * Getter method for the Command
     * 
     * @return String jiniCommand
     */
    public String getJiniCommand() {
        return this.jiniCommand;
    }
    
    /**
     * Getter method for Short Description 
     * @return String shortDesc
     */
    public String getShortDesc() {
        return this.shortDesc;
    }
}
