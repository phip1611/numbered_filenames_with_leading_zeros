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

import java.io.FileNotFoundException;
import org.json.simple.JSONObject;

/**
 * Startet entweder das GUI oder die Konsolen-Anwendung.
 * @author phip1611
 */
public class Starter {
    /**
     * Konfigurationen aus der JSON-Datei.
     */
    private JSONObject config;
    
    /**
     * Instanz von JSONConfigProvider.
     */
    private JSONConfigProvider jcp;
    
    /**
     * Entscheidet, ob GUI oder Konsolenanwendung gestartet wird.
     */
    public Starter() {
        this.jcp = new JSONConfigProvider();
        try {
            this.jcp.loadConfig();
            this.config = jcp.getConfig();
            if ((boolean) this.config.get("gui_autostart")) {
                new GUI_Tool(this.config);
            } else {
                new CommandLineTool(this.config);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Konfigurationsdatei nicht gefunden. Programm aufgrund eines Fehlers beendet.");
        }
    }
}
