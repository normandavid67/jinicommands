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

/**
 * Prints a char at intervals of 500 miliseconds
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class PrintChar extends Thread {

    boolean stopPrinting = false;
    String outPutChar = ".";

    /**
     * flag to stop printing the char
     *
     * @param stopPrinting
     */
    public void setStopPrinting(boolean stopPrinting) {
        this.stopPrinting = stopPrinting;
    }

    /**
     * setter method for the char to be printed
     *
     * @param outPutChar
     */
    public void setOutPutChar(String outPutChar) {
        this.outPutChar = outPutChar;
    }

    /**
     * Start printing chars at 500 milisecond intervals
     */
    @Override
    public void run() {
        try {
            int counter = 0;

            for (;;) {
                if (this.stopPrinting == false) {

                    if (counter < 51) {
                        System.out.print(this.outPutChar);
                         counter++;
                    } else {
                        System.out.println(this.outPutChar);
                        counter = 1;

                    }
                    
                    Thread.sleep(500);
                } else {
                    break;
                }
            }

        } catch (InterruptedException ex) {
            System.out.print("Error : " + ex.toString());
        }
    }
}
