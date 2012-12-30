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
public class Filename {

    private String fullPath;
    private String pathSeparator = File.separator; // OS Specific Path Seperator
    private char extensionSeparator;

    public Filename(String str, char ext) {
        fullPath = str;
        extensionSeparator = ext;
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        if (dot > 0) {
            return fullPath.substring(dot + 1);
        } else {
            return "";
        }
    }

    public String filename() { // gets filename without extension
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        if (sep > 0) {
            return fullPath.substring(sep + 1, dot);
        } else {
            return "";
        }
    }

    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        if (sep > 0) {
            return fullPath.substring(0, sep);
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        final String FPATH = " U:\\Documents\\Magellan\\MPP2\\a.zip";
        Filename myHomePage = new Filename(FPATH, '.');
        System.out.println("Extension = " + myHomePage.extension());
        System.out.println("Filename = " + myHomePage.filename());
        System.out.println("Path = " + myHomePage.path());
    }
}
