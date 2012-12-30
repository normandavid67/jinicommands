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