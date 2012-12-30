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
package org.jini.commands.files.filters;

import java.io.File;
import java.io.FileFilter;
/**
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class HiddenFileFilter implements FileFilter {
   
    boolean showAll = false;
    
    public HiddenFileFilter(boolean showAll) {
        this.showAll = showAll;
    }
    
    
    @Override
    public boolean accept(File file) {
        if(file.isHidden() && file.isFile()){
            if (this.showAll == false) {
                return !file.getName().startsWith(".");
            } else {
                return true;
            }
        }
        return false;
    }
}

