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
package org.jini.commands.info;

import java.text.SimpleDateFormat;
import java.util.Date;
import jinicommands.JiniCmd;

/**
 * JiniCommands About
 *
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class About extends JiniCmd {

    static String jiniVersion = "0.1";

    @Override
    public void setJCLIOptions() {
    }

    @Override
    public void executeCommand() {
    }
    public static String getJiniCommandsVersion() {
        return jiniVersion;
    }

    public About() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        
        System.out.println("JiniCommands is a platform independant command line application written in Java. ");
        System.out.println("Jinicommands provides some easy to use Unix like commands for e.g. cd, date, findfiles, zipdir/unzip... ");
        System.out.println("");
        System.out.println("Motivation :  ");
        System.out.println("JiniCommands started as a small set of tools written to help in day to day work.");
        System.out.println("The author wanted to use the same commands on Unix and on Windows systems.");
        System.out.println("The majority of the commands are very similar to Unix commands.");
        System.out.println("Users familiar to Unix will be comfortable using JiniCommands");
        System.out.println("");
        
        System.out.println("JiniCommands version " + JiniLicense.getJiniCommandsVersion() + ", Copyright (C) " + ft.format(date) + " Norman David");
        System.out.println("");
        System.out.println("Disclaimer : This program is distributed in the hope that it will be useful,");
        System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  ");
        System.out.println("Type 'help' or '?' for a full list of all commands.");
        System.out.println("Type 'license' for License details.");
        System.out.println("");
    }
}
