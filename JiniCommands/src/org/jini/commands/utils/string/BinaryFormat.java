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

package org.jini.commands.utils.string;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class BinaryFormat {
    
    public Boolean error = false;
    public ArrayList<String> errorMessages = new ArrayList<String>();

    
    public BinaryFormat() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

  
    
    void addErrorMessage(String error) {
        this.errorMessages.add(error);
    }


    public String decodeBinary(String input) {
        StringBuilder strText = new StringBuilder();

        if ((input != null) && (input.length() > 0)) {

            String[] chars = input.split(" ");
            for (int x = 0; x < chars.length; x++) {
                try {
                    int code = Integer.parseInt(chars[x], 2);
                    strText.append((char) code);
                } catch (Exception e) {
                    this.setError(true);
                    this.addErrorMessage("Error : Invalid Input. Text is not Binary.");
                    return "";
                }
            }
        }

        return strText.toString();
    }

    public String encodeBinary(String txt) {
        byte[] bytes = txt.getBytes();
        StringBuilder binaryText = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binaryText.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }

            binaryText.append(' ');
        }
        return binaryText.toString();
    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        BinaryFormat bf = new BinaryFormat();
        String input = "   Norman David      ";
        System.out.println("Input : " + input);

        String b = bf.encodeBinary(input);

        System.out.println("Binary : " + b);

        System.out.println("String : " + bf.decodeBinary(b));

    }
}
