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

import java.io.File;
/**
 * Calculates size of a File in Kilobytes, Megabytes, Gigabytes, Terabytes.
 * 
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class FileSize {

    public double getFileSizeBytes(File file) {
        if (file.exists()) {
            double bytes = file.length();

            return bytes;
        }
        return 0;

    }

    public double convertBytesToKilobytes(double bytes) {
        double kilobytes = (bytes / 1024);
        return kilobytes;

    }

    public double convertBytesToMegabytes(double bytes) {

        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);

        return megabytes;
    }

    public double convertBytesToGigabytes(double bytes) {

        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);

        return gigabytes;

    }

    public double convertBytesToTerabytes(double bytes) {

        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);
        double terabytes = (gigabytes / 1024);

        return terabytes;
    }

    public double convertBytesToPetabytes(double bytes) {

        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);
        double terabytes = (gigabytes / 1024);
        double petabytes = (terabytes / 1024);

        return petabytes;

    }
}
