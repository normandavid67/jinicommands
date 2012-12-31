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
 * Convert Text to Binary and vice versa
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

    /**
     * In this method all the specific Command Line options are defined.
     *
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);
        this.jcOptions.addOption(OptionBuilder.withLongOpt("file").withDescription("File to Convert").isRequired(false).hasArg().create("f"));
        //this.jcOptions.addOption(OptionBuilder.withLongOpt("outputfile").withDescription("Output File").isRequired(false).hasArg().create("of"));
        OptionGroup jcGroup = new OptionGroup();
        jcGroup.addOption(OptionBuilder.withLongOpt("texttobinary").withDescription("Convert text to Binary").create("ttb"));
        jcGroup.addOption(OptionBuilder.withLongOpt("binarytotext").withDescription("Convert binary to text").create("btt"));
        this.jcOptions.addOptionGroup(jcGroup);
    }

    /**
     * In this method all the execution of the specific Command takes place
     */
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

    /**
     * Prints out all Command Line Options in a table
     *
     */
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