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
package org.jini.commands.helper;

import java.util.Locale;

/**
 * OS Validator
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class OSValidator {

    public static boolean isWindows() {

        Locale loc = Locale.getDefault();
        String os = System.getProperty("os.name").toLowerCase(loc);
        // windows
        return (os.indexOf("win") >= 0);

    }

    public static boolean isMac() {
        Locale loc = Locale.getDefault();
        String os = System.getProperty("os.name").toLowerCase(loc);
        // Mac
        return (os.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {
        Locale loc = Locale.getDefault();
        String os = System.getProperty("os.name").toLowerCase(loc);
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

    public static boolean isSolaris() {
        Locale loc = Locale.getDefault();
        String os = System.getProperty("os.name").toLowerCase(loc);
        // Solaris
        return (os.indexOf("sunos") >= 0);

    }

    public static void main(String[] args) {
        if (isWindows()) {
            System.out.println("This is Windows");
        } else if (isMac()) {
            System.out.println("This is Mac");
        } else if (isUnix()) {
            System.out.println("This is Unix or Linux");
        } else if (isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not support!!");
        }
    }
}