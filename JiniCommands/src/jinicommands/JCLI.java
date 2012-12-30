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
    TextConvert("textconvert", "Convert text/file to Binary, Hexadecimal, Base64."),
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
    JiniLicense("license", "Print JiniCommands License");
    
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
