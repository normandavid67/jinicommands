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
package org.jini.commands.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.jini.commands.helper.TablePrinter;
import org.jini.commands.utils.string.BinaryFormat;

/**
 * 
 * @author Norman David
 * @since 0.1
 * @version 0.1
 */
public class TextConvert extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    private boolean texttobinary = false;
    private boolean binarytotext = false;
    private String filePath = "";
    private StringBuffer resultString = new StringBuffer();

    public TextConvert() {
    }

    
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
        this.jcOptions.addOption(OptionBuilder.withLongOpt("file").withDescription("File to Convert").isRequired(false).hasArg().create("f"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("outputfile").withDescription("Output File").isRequired(false).hasArg().create("of"));
        OptionGroup jcGroup = new OptionGroup();
        jcGroup.addOption(OptionBuilder.withLongOpt("texttobinary").withDescription("Convert text to Binary").create("ttb"));
        jcGroup.addOption(OptionBuilder.withLongOpt("binarytotext").withDescription("Convert binary to text").create("btt"));
        this.jcOptions.addOptionGroup(jcGroup);
    }

    @Override
    public void executeCommand() {
        this.setJCLIOptions();
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption("h")) {
                this.printHelp();
            }
            if (jcCmd.hasOption("ttb")) {
                this.texttobinary = true;
            }
            if (jcCmd.hasOption("btt")) {
                this.binarytotext = true;
            }
            if (jcCmd.hasOption("f")) {
                if ((this.done == false) && (this.jcError == false)) {
                    this.filePath = jcCmd.getOptionValue("f");
                }
            }
            if ((this.done == false) && (this.jcError == false) && (this.texttobinary == true)) {
                if ((this.filePath == null) || (this.filePath.length() == 0)) {
                    this.jcError = true;
                    this.addErrorMessages("Error : No File defined.");
                }
                if (this.jcError == false) {
                    if (isFileCanRead(this.filePath) == false) {
                        this.jcError = true;
                        this.addErrorMessages("Error : File does not exsist / File Unreadable.");
                    }
                }

                if (this.jcError == false) {
                    String txt = this.getFileContents(this.filePath);
                    this.resultString.append(this.convertTextToBinary(txt));
                    this.done = true;
                }
            }
            if ((this.done == false) && (this.jcError == false) && (this.binarytotext == true)) {
                String txt = this.getFileContents(this.filePath);
                this.resultString.append(this.convertBinaryToText(txt));
                this.done = true;
            }


            if (this.done == true) {
                System.out.println(this.resultString);
            }


        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + ex.getMessage());
        }
    }

    private String getFileContents(String FileName) {
        StringBuilder resStr = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(FileName));
            String str;
            while ((str = in.readLine()) != null) {
                resStr.append(str);
            }
            in.close();
        } catch (IOException e) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + e.toString());
        }
        return resStr.toString();
    }

    private boolean isFileCanRead(String path) {
        File f = new File(path);
        if ((f.isFile()) && (f.canRead())) {
            return true;
        }
        return false;
    }

    private String convertBinaryToText(String text) {
        if ((text != null) && (text.length() > 0)) {
            BinaryFormat bf = new BinaryFormat();
            if (bf.decodeBinary(text).length() > 0) {
                return bf.decodeBinary(text);
            } else {
                this.setJcError(bf.getError());
                this.addErrorMessages(bf.getErrorMessages().toString());
            }
        }
        return "";
    }

    private String convertTextToBinary(String text) {
        if ((text != null) && (text.length() > 0)) {
            BinaryFormat bf = new BinaryFormat();
            return bf.encodeBinary(text);
        }
        return "";
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "textconvert");
        helpTableHead.addRow("SYNOPSIS : ", "textconvert [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Convert text/file to Binary, Hexadecimal, Base64.");
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "help -h", "help --help");
        helpTable.addRow("-f", "--file", "required", "Path to file", "textconvert -f /PATH/TO/FILE", "textconvert --file /PATH/TO/FILE");
        helpTable.addRow("-ttb", "--texttobinary", "not required", "Convert text to Binary", "textconvert -ttb -f /PATH/TO/FILE", "textconvert --texttobinary --file /PATH/To/File");
        helpTable.addRow("-btt", "--binarytotext", "not required", "Convert Binary to text", "textconvert -btt -f /PATH/To/FileWithBinaryData", "textconvert --binarytotext --file /PATH/To/FileWithBinaryData");
        helpTable.print();
        this.done = true;
    }
}