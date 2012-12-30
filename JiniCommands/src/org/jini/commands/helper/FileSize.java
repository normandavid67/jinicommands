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
package org.jini.commands.helper;

import java.io.File;
/**
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
