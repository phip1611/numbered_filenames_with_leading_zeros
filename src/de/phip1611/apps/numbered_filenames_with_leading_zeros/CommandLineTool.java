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
import java.util.Scanner;
import org.json.simple.JSONObject;

/**
 * Initialisiert das Tool über die Kommandozeile und gibt Hinweise aus.
 * @author phip1611
 */
public final class CommandLineTool {
    /**
     * Input-Stream Scanner für die Kommandozeile.
     */
    private final Scanner scanner;
    
    /**
     * JSON-Objekt mit den Konfigurationseinträgen.
     */
    private final JSONObject config;
    
    /**
     * Instanz der Klasse AddLeadingZeros.
     */
    private final AddLeadingZeros alzTool;
    
    /**
     * Verzeichnis mit den Dateien.
     */
    private File directory;
    
    /**
     * RegEx-Muster für Dateinamen im Verzeichnis.
     */
    private String filenamePattern;
    
    /**
     * Trennzeichen für Nummer in Dateiname.
     */
    private String delimiter;
    
    /**
     * Initialisiert die Kommandozeilen-Schnittstelle zum Tool.
     * @param config JSON-Objekt mit Konfigurationseinträgen.
     */
    public CommandLineTool(JSONObject config) {
        this.scanner = new Scanner(System.in);
        this.config = config;
        this.alzTool = new AddLeadingZeros();
        this.run();
    }
    
    /**
     * Läuft eine Prozedur komplett durch: Nutzereingaben entgegen nehmen und das tool ausführen.
     */
    private void run() {
        String input;
        boolean repeat = true;
        
        showCopyrightInfo();
        showToolInfo();
        
        /* Verzeichnis abfragen */
        do {
            input = askUserForAction("Verzeichnis (absoluter Pfad):");
            if (input.equals("help")) {
                showToolInfo();
            } else if (input.equals("exit")) {
                System.exit(0);
            } else {
                this.directory = new File(input);
                if (this.directory.isDirectory()) {
                    repeat = false;
                }
            }
        } while (repeat);
        
        /* Trennzeichen abfragen */
        repeat = true;
        do {
            input = askUserForAction(this.config.get("delimiter_notice").toString());
            if (input.length()<=1) { // EIN Trennzeichen eingeben
                if (input.isEmpty() || input.equals(" ") || input.equals("-")) {
                    this.delimiter = this.config.get("default_delimiter").toString();
                    repeat = false;
                } switch (input) {
                    case "help":
                        showToolInfo();
                        repeat = false;
                        break;
                    case "exit":
                        System.exit(0);
                    case "(":
                        this.delimiter = input;
                        repeat = false;
                        break;
                    case "[":
                        this.delimiter = input;
                        repeat = false;
                        break;
                    case "{":
                        this.delimiter = input;
                        repeat = false;
                        break;
                    case "<":
                        this.delimiter = input;
                        repeat = false;
                        break;
                }
            }
            
        } while (repeat);
        
        /* Dateinamen-Muster (RegEx) abfragen. */
        repeat = true;
        do {
            input = askUserForAction("Standard-RegEx: "+this.config.get("default_filename_pattern").toString()+"\r\n"+this.config.get("filename_pattern_notice").toString());
            if (input.isEmpty() || input.equals(" ") || input.equals("-")) {
                this.filenamePattern = this.config.get("default_filename_pattern").toString();
                repeat = false;
            } else if (input.equals("help")) {
                showToolInfo();
            } else if (input.equals("exit")) {
                System.exit(0);
            } else {
                this.filenamePattern = input;
                repeat = false;
            }
        } while (repeat);
        
        /* Nach Bestätigung fragen. */
        repeat = true;
        do {
            input = askUserForAction("Wirklich fortfahren? \"ja\" eingeben!\r\nVerzeichnis: "+this.directory+"\r\nTrennzeichen: "+this.delimiter+"\r\nDateinamen-Muster: "+this.filenamePattern+"\r\n\"ja\" eingeben!");
            if (!input.toLowerCase().equals("ja")) {
                switch (input) {
                    case "help":
                        showToolInfo();
                        break;
                    case "exit":
                        System.exit(0);
                }
            } else {
                repeat = false;
            }
        } while (repeat);
        
        
        
        this.alzTool.setDirectory(this.directory);
        this.alzTool.setDelimiter(this.delimiter);
        this.alzTool.setPattern(this.filenamePattern);
        this.alzTool.openDirectory();
        String[] exampleFilesFromDirectory = this.alzTool.getExampleFiles();
        
        System.out.println("---------------------------------------------");
        System.out.println("Beispieldateien aus dem Verzeichnis, die zu dem Muster passen:");
        for (String filename : exampleFilesFromDirectory) {
            if (filename != null) {
                System.out.println(filename);
            }
        }
        this.alzTool.execute();
        
    }
    
    /**
     * Gibt Copyright-Informationen aus.
     */
    public void showCopyrightInfo() {
        System.out.println("---------------------------------------------");
        System.out.println("Urheber: "+this.config.get("copyright_holder_name"));
        System.out.println(this.config.get("copyright_message"));
        System.out.println("Lizenz: "+this.config.get("copyright_licence"));
        
        System.out.println("Rechte Dritter: "+this.config.get("third_parties_notice"));
    }
    
    /**
     * Gibt Informationen zum Tool aus.
     */
    public void showToolInfo() {
        System.out.println("---------------------------------------------");
        System.out.println(this.config.get("description"));
        System.out.println("Windows nummeriert mit dem Muster 1, 2, .. 11 .. 121");
        System.out.println("Dieses Programm ergaenzt fuehrenden Nullen: 001, 002 .. 011 .. 121");
        System.out.println("Die Nummer muss in Klammern stehen: Bild_(1).jpg --> Bild_(001).jpg");
        System.out.println("---------------------------------------------");
        System.out.println("Befehle \"exit\" und \"help\" können jederzeit genutzt werden.");
    }
    
    /**
     * Nutzereingabe über die Konsole.
     * @return String Nutzereingabe
     */
    private String askUserForAction() {
        return askUserForAction("");
    }
    
    /**
     * Nutzereingabe über die Konsole.
     * @return String Nutzereingabe
     * @param caption Beschriftung der Eingabe.
     * @return String Nutzereingabe
     */
    private String askUserForAction(String caption) {
        System.out.println("---------------------------------------------");
        System.out.println("Bitte Eingabe taetigen!");
        if (!caption.isEmpty()) {
            System.out.println(caption);
        }
        return scanner.nextLine();
    }
    
    /**
     * Zeichen für Konsole umwandeln, um Fehler zu vermeiden. (z.B. Umlaute)
     */
    private void escape() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
