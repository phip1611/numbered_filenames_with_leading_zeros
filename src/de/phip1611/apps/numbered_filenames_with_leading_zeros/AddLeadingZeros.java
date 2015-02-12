/*
Nummerierte Dateinamen mit führenden Nullen (Tool)
---------------------------------------------------
 Urheber: Philipp Schuster / @phip1611 / http://phip1611.de
 Lizenz: CC BY-NC-SA 4.0 (Creative Commons)

----------------
 Rechte Dritter:
   Dieses Tool verwendet "json simple" https://code.google.com/p/json-simple/ in Version 1.1.1, welches unter einer Apache License 2.0 veröffentlicht wurde.

   Kopie der APACHE LICENSE 2.0
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may Licensed under the Apache License, Version 2.0 (the "License");obtain a copy of the License at
     
         http://www.apache.org/licenses/LICENSE-2.0
     
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License. 
 */

package de.phip1611.apps.numbered_filenames_with_leading_zeros;

import java.io.File;

/**
 * Fügt bei allen Nummerierten Dateien im Verzeichnis führende Nullen hinzu.
 * @author phip1611
 */
public class AddLeadingZeros {
    
    private File directory;
    private File[] files;
    private String pattern;
    private String delimiter;
    private String delimiterClose;
    private int digits;
    
    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    public void openDirectory() {
        this.files = this.directory.listFiles();
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    public void execute() {
        this.setDelimiterClose();
        this.openDirectory();
        this.lookForHighestFilenameCountName();
        this.renameFiles();
    }
    
    private void setDelimiterClose() {
        switch (this.delimiter) {
            case "[":
                this.delimiter = "\\[";
                this.delimiterClose = "\\]";
                break;
            case "{":
                this.delimiter = "\\{";
                this.delimiterClose = "\\}";
                break;
            case "<":
                this.delimiter = "\\<";
                this.delimiterClose = "\\>";
                break;
            case "(":
            default:
                this.delimiter = "\\(";
                this.delimiterClose = "\\)";
                break;
        }
    }
    
    
    
    public String[] getExampleFiles() {
        int i = 0;
        String[] fileNames = new String[3];
        
        for (File file : files) {
           if (file.getName().matches(this.pattern)) {
               fileNames[i] = file.getName();
               i++;
           }
           if (i>2) { // wenn mehr
               break;
           }
        }
        return fileNames;
    }
    
    private void lookForHighestFilenameCountName() {
        int count, highestCount;
        count = 0;
        highestCount = 0;
        for (File file : files) {
            if (file.getName().matches(this.pattern)) {
                count = Integer.valueOf(file.getName().split(delimiter)[1].split(delimiterClose)[0]);
                if (count > highestCount) {
                    highestCount = count;
                }
            }
        }
        this.digits = String.valueOf(highestCount).length();
    }
    
    private void renameFiles() {
        int count, countDigits, digitsDifference;
        count = countDigits = digitsDifference = 0;
        String completeNewFilePath, filenameBeforeDelimiter, filenameAfterDelimiter, sCount;
        for (File tmp : this.files) {
            if (tmp.getName().matches(this.pattern)) {
                filenameBeforeDelimiter = tmp.getName().split(this.delimiter)[0];
                filenameAfterDelimiter  = tmp.getName().split(this.delimiterClose)[1];
                completeNewFilePath = tmp.getAbsolutePath().replace(tmp.getName(), "");
                completeNewFilePath = completeNewFilePath+filenameBeforeDelimiter;
                
                sCount = tmp.getName().split(this.delimiter)[1].split(this.delimiterClose)[0];
                count = Integer.valueOf(sCount);
                
                // Bereits vorhandene führende nullen entfernen
                sCount = String.valueOf(count);
                countDigits = String.valueOf(count).length();
                digitsDifference = this.digits - countDigits;
                for (int i=0;i<digitsDifference;i++) {
                    sCount = "0"+sCount;
                }
                completeNewFilePath = completeNewFilePath +
                        this.delimiter.replace("\\", "") +
                        sCount +
                        this.delimiterClose.replace("\\", "") +
                        filenameAfterDelimiter;
                tmp.renameTo(new File(completeNewFilePath));
            }
        }
    }
}
