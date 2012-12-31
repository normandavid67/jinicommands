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
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

/**
 *
 * Prints out the License information of JiniCommands
 *
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class JiniLicense extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
   
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

    public JiniLicense() {

        Date date = new Date();
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");

        System.out.println("JiniCommands Version , " + JiniLicense.getJiniCommandsVersion() + "  29.12.2012" );
        System.out.println("");
        System.out.println("New BSD License");
        System.out.println("");
        System.out.println("Copyright (C) " + yy.format(date) + "  Norman David <normandavid67@gmail.com>");
        System.out.println("All rights reserved.");
        System.out.println("");
        System.out.println("Redistribution and use in source and binary forms, with or without");
        System.out.println("modification, are permitted provided that the following conditions are met:");
        System.out.println("* Redistributions of source code must retain the above copyright");
        System.out.println("  notice, this list of conditions and the following disclaimer.");
        System.out.println("* Redistributions in binary form must reproduce the above copyright");
        System.out.println("  notice, this list of conditions and the following disclaimer in the");
        System.out.println("  documentation and/or other materials provided with the distribution.");
        System.out.println("* Neither the name of the <organization> nor the");
        System.out.println("  names of its contributors may be used to endorse or promote products");
        System.out.println("  derived from this software without specific prior written permission.");
        System.out.println("");
        System.out.println("THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND");
        System.out.println("ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED");
        System.out.println("WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE");
        System.out.println("DISCLAIMED. IN NO EVENT SHALL Norman David BE LIABLE FOR ANY");
        System.out.println("DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES");
        System.out.println("(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;");
        System.out.println("LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND");
        System.out.println("ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT");
        System.out.println("(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS");
        System.out.println("SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
       
    }
}
