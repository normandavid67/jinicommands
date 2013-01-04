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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Abstract Class which the individual JiniCommand classes implement. Here all
 * the generic methods used by all commands reside.
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public abstract class JiniCmd {

    public ArrayList<String> jCmdList = new ArrayList<String>();
    public Boolean jcError = false;
    public ArrayList<String> errorMessages = new ArrayList<String>();
    public ArrayList<String> resultMessages = new ArrayList<String>();

    /**
     * Setter method of the command line arguments
     *
     * @param args
     */
    public void setArgs(String[] args) {
        Collections.addAll(this.jCmdList, args);
        Iterator<String> i = this.jCmdList.iterator();
        while (i.hasNext()) {
            String s = i.next();

            // Remove all trailing blank spaces
            s = s.trim();

            // Blank lines and new Lines are ignored. Helps if a command is copy -> pasted
            if (s == null || s.isEmpty() || s.equals("\n") || s.equals("\t")) {
                i.remove();
            }
        }
    }

    /**
     * Converts from ArrayList<String> to String[]
     *
     * @return String[]
     */
    public String[] convertToArray() {
        String[] cmds = this.jCmdList.toArray(new String[this.jCmdList.size()]);

        return cmds;
    }

    /**
     * Getter method for general JiniError flag.
     *
     * @return Boolean
     */
    public Boolean getJcError() {
        return jcError;
    }

    /**
     * Setter method for general JiniError flag.
     *
     * @param jcError
     */
    public void setJcError(Boolean jcError) {
        this.jcError = jcError;
    }

    /**
     * Add Error Messages to ArrayList<String> errorMessages
     *
     * @param Message
     */
    public void addErrorMessages(String Message) {
        if ((Message != null) && (Message.length() > 0)) {
            this.errorMessages.add(Message);
        }
    }

    /**
     * Get all Error Messages
     *
     * @return sb
     */
    public String getErrorMessages() {
        StringBuilder sb = new StringBuilder();
        for (String s : errorMessages) {
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Add Error Messages to ArrayList<String> resultMessages
     *
     * @param Message
     */
    public void addResultMessages(String Message) {
        if ((Message != null) && (Message.length() > 0)) {
            this.resultMessages.add(Message);
        }
    }

     /**
     * Get all Result Messages
     *
     * @return String sb
     */
    
    public String getResultMessages() {
        StringBuilder sb = new StringBuilder();
        for (String s : resultMessages) {
            sb.append(s);
        }

        return sb.toString();
    }

    /**
     * Abstract Method to set command line options
     */
    public abstract void setJCLIOptions();

    /**
     * Abstract Method to execute the command.
     */
    public abstract void executeCommand();
}