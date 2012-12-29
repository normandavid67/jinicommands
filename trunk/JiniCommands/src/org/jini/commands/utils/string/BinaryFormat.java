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
package org.jini.commands.utils.string;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Convert Text to Binary & Binary to text
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

    /**
     * getter method for error flag
     * @return 
     */
    public Boolean getError() {
        return error;
    }
    /**
     * setter method for error flag
     * @param error 
     */

    public void setError(Boolean error) {
        this.error = error;
    }

    
    /**
     * getter method for all error messages
     * @return 
     */
    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }
    
    /**
     * add error message to ArrayList<String> errorMessages
     * @param error 
     */

    void addErrorMessage(String error) {
        this.errorMessages.add(error);
    }

    /**
     * Decode Binary to plain text
     *
     * @param input
     * @return
     */
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

    /**
     * Encode text to binary
     * 
     * @param txt
     * @return 
     */
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
