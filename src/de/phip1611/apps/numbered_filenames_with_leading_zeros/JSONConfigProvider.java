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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Ließt die JSON-Konfigurationsdatei ein und stellt die geparsten Daten bereit.
 * @author phip1611
 */
public class JSONConfigProvider {
    /**
     * Pfad der Konfigurationsdatei.
     */
    public final String RELATIVE_CONFIGFILE_PATH = "src/de/phip1611/apps/numbered_filenames_with_leading_zeros/config.json";
    
    /**
     * Konfigurations-Datei.
     */
    private final File file;
    
    /**
     * JSON-Objekt (geparste Daten).
     */
    private JSONObject config;
    
    public JSONConfigProvider() {
        this.file = new File(this.RELATIVE_CONFIGFILE_PATH);
    }
    
    /**
     * Überprüft, ob die Konfigurationsdatei existiert und gelesen werden kann.
     * @return boolean
     */
    private boolean isReadable() {
        return file.isFile() && file.canRead();
    }
    
    /**
     * Lädt die Konfigurationsdatei und parst die JSON-Daten in ein JSON-Objekt.
     * @throws FileNotFoundException 
     */
    public void loadConfig() throws FileNotFoundException {
        if (!this.isReadable()) {
            throw new FileNotFoundException("Konfig-Datei ist nicht vorhanden oder konnte nicht gelesen werden.");
        }
        try {
            config = (JSONObject) new JSONParser().parse(new FileReader(RELATIVE_CONFIGFILE_PATH));
        } catch (IOException | ParseException ex) {
            System.err.println("Konfig-Datei konnte nicht erfolgreich geöffnet und geparst werden.");
        }
    }
    
    /**
     * Liefert das JSOn-Objekt zurück.
     * @return JSONObject
     */
    public JSONObject getConfig() {
        return this.config;
    }
}
