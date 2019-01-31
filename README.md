doc folder contains draw.io files


MetaApp:
- Metadatan ganz allgemein als mehrere jsons, mit verlinkung jeweils zum Asset
- Trennung zwischen Datenbank für Metadaten+Bilder und Lucene / Index mit Sync bzw Plugin Architektur
- von außen abrufen, metadatenerzeugung separiert evtl. mit subscriber schema
- JSON Schema für verschiedene Metadatentypen -> alias Dokumentation für Bilddatenbank
- graphQL, Spring, Angular; anfragen evtl direkt aus dem frontend für Anfragen
- graphql baut anfragesprache für frontend zusammen (wird in den modelklassen definiert, welche zeitgleich von spring data in die db geschrieben werden können)
- schema für metadatentypen, modularisiert, frontend filter automatisch erzeugen
- exif als beispiel 
- Daten hochladen in Datenbank
- Metadaten hochladen in Datenbank -> Auswahl eines Metadatentyps und anzeige des Formats + Validation
- Sync zur Elastic Instanz
- Suche über Elastic Instanz über simples Frontend
- automatische Metadatenerzeugung und Hinzufügen über API ermöglichen -> subscriber Modell, sodass Docker Instanzen die Daten generieren


Kein Spring data nutzen, da sonst struktur der daten als Klassen vorgegeben werden muss.
http://mongodb.github.io/mongo-java-driver/3.9/driver/getting-started/quick-start/