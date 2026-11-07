# Use Case: Turnier verwalten

## Überblick

**Use-Case-ID:** UC-001
**Use-Case-Name:** Turnier verwalten
**Primärer Akteur:** Organisator:in
**Ziel:** Ein Turnier als eigenständigen, referenzierbaren Anlass anlegen, seine Stammdaten
korrigieren und nicht mehr benötigte Turniere aus der Arbeitsansicht entfernen, damit jeder
Anlass getrennt geplant werden kann
**Auslöser:** Organisator:in will einen neuen Anlass planen oder die Stammdaten eines
bestehenden Anlasses ändern
**Status:** Draft
**Anforderungen:** [FR-001, FR-005, FR-018, NFR-002, NFR-005, NFR-010, NFR-011, NFR-014, NFR-020, C-013](../requirements.md)

## Vorbedingungen

- Organisator:in ist am System angemeldet ([UC-019](use-case-diagram.md), FR-026, NFR-010)
- Es existiert kein weiteres Vorwissen im System — UC-001 ist der erste Fachschritt eines
  Turniers; Teams, Gruppen, Spielfelder, Regeln und Phasen setzen ein Turnier bereits voraus

## Hauptszenario

1. Organisator:in ruft die Turnierübersicht auf.
2. System zeigt die aktiven Turniere mit Name, Datum, Ort und Planungsstand.
3. Organisator:in wählt «Turnier erstellen».
4. System zeigt ein leeres Formular für Name, Datum und Ort.
5. Organisator:in erfasst Name, Datum und Ort und bestätigt.
6. System validiert die Eingaben serverseitig (BR-005) und prüft den Namen gegen BR-002.
7. System vergibt eine eindeutige ID, legt das Turnier im Status `PLANNING` an und
   protokolliert die Anlage.
8. System zeigt das angelegte Turnier mit seiner ID an und benennt als nächste Schritte
   Spielfelder ([UC-004](use-case-diagram.md)), Turnierregeln ([UC-005](use-case-diagram.md))
   und Phasen ([UC-006](use-case-diagram.md)), damit die Organisator:in weiss, woran der
   Spielplan noch hängt.

## Alternative Abläufe

### A1: Turnier bearbeiten

**Auslöser:** Organisator:in wählt in Schritt 2 ein bestehendes Turnier und «bearbeiten»
**Ablauf:**

1. System zeigt das Formular mit Name, Datum und Ort des gewählten Turniers; die ID ist
   sichtbar, aber nicht änderbar (BR-001).
2. Organisator:in ändert die Werte und bestätigt.
3. System validiert wie in Schritt 6 und prüft den Namen gegen BR-002.
4. System speichert die Änderung und protokolliert sie.
5. Use Case endet.

### A2: Datum eines Turniers mit Spielplan ändern

**Auslöser:** Organisator:in ändert in A1 Schritt 2 das Datum, obwohl für das Turnier bereits
ein Spielplan existiert
**Ablauf:**

1. System weist darauf hin, dass ein Spielplan existiert, und nennt die Zahl der betroffenen
   Partien.
2. Organisator:in bestätigt die Änderung oder bricht ab.
3. Bei Bestätigung speichert das System das neue Datum und verschiebt die Zeitfenster des
   Spielplans mit — die Zuordnung von Partien zu Feldern und Reihenfolge bleibt unverändert
   (BR-006).
4. Bei Abbruch bleibt das Turnier unverändert; Use Case endet.

### A3: Turnier löschen, solange keine Ergebnisse erfasst sind

**Auslöser:** Organisator:in wählt in Schritt 2 ein Turnier und «löschen»; für das Turnier ist
kein Ergebnis erfasst (BR-003)
**Ablauf:**

1. System nennt, was mitgelöscht wird — Teams, Gruppen, Spielfelder, Phasen, Partien und
   offene Anmeldungen des Turniers — und fragt nach Bestätigung.
2. Organisator:in bestätigt.
3. System löscht das Turnier mit allen abhängigen Objekten und protokolliert die Löschung mit
   ID und Name des Turniers (BR-004).
4. Use Case endet.

### A4: Turnier mit erfassten Ergebnissen archivieren

**Auslöser:** Organisator:in wählt «löschen» für ein Turnier, für das mindestens ein Ergebnis
erfasst ist (BR-003)
**Ablauf:**

1. System erklärt, dass gespielte Turniere nachrechenbar bleiben müssen (NFR-002) und deshalb
   nicht gelöscht, sondern archiviert werden.
2. Organisator:in bestätigt die Archivierung oder bricht ab.
3. Bei Bestätigung setzt das System den Status auf `ARCHIVED` und protokolliert den
   Statuswechsel.
4. Das Turnier verschwindet aus der Turnierübersicht nach Schritt 2, bleibt aber über den
   Archivfilter lesbar; seine Daten bleiben unverändert erhalten (BR-007).
5. Use Case endet.

### A5: Turniername im selben Jahr bereits vergeben

**Auslöser:** Name und Jahr des Datums kollidieren mit einem bestehenden Turnier (Schritt 6
oder A1 Schritt 3)
**Ablauf:**

1. System lehnt das Speichern ab und benennt das kollidierende Turnier mit Name und Datum.
2. System hält die erfassten Eingaben im Formular, damit die Organisator:in nur den Namen
   anpassen muss.
3. Use Case setzt bei Schritt 5 fort.

### A6: Unvollständige oder unplausible Eingaben

**Auslöser:** Validierung in Schritt 6 oder A1 Schritt 3 schlägt fehl
**Ablauf:**

1. System lehnt das Speichern ab und nennt je betroffenem Feld den Grund — fehlender Name,
   fehlendes oder unlesbares Datum, fehlender Ort, Überlänge (BR-005, NFR-020).
2. System hält die übrigen Eingaben im Formular.
3. Use Case setzt bei Schritt 5 fort.

### A7: Keine Turniere vorhanden

**Auslöser:** Es existiert kein aktives Turnier (Schritt 2)
**Ablauf:**

1. System zeigt eine leere Übersicht und bietet «Turnier erstellen» als einzige Handlung an.
2. Use Case setzt bei Schritt 3 fort.

### A8: Turnier wurde zwischenzeitlich geändert

**Auslöser:** Das in A1 bearbeitete Turnier wurde seit Schritt 1 von einer anderen
Organisator:in geändert oder archiviert
**Ablauf:**

1. System lehnt das Speichern ab und weist auf die zwischenzeitliche Änderung hin.
2. System zeigt den aktuellen Stand des Turniers.
3. Use Case setzt bei Schritt 2 fort.

## Nachbedingungen

### Nachbedingungen bei Erfolg

- Das Turnier existiert mit eindeutiger ID, Name, Datum, Ort und einem Status aus `PLANNING`,
  `RUNNING`, `FINISHED`, `ARCHIVED`
- Die ID ist über alle Turniere eindeutig und unveränderlich
- Name und Jahr des Datums sind zusammen eindeutig (BR-002)
- Anlage, Änderung, Löschung und Archivierung sind protokolliert (BR-004)
- Kein Spielplan, Ergebnis oder Tabellenstand eines anderen Turniers ist verändert

### Nachbedingungen bei Fehlschlag

- Es entsteht kein Turnier ohne Name, Datum, Ort oder ID
- Es existiert kein zweites Turnier mit demselben Namen im selben Kalenderjahr
- Kein Turnier mit erfassten Ergebnissen ist gelöscht
- Es entsteht kein Turnier mit abhängigen Objekten, aber ohne Turnier — ein abgebrochener
  Löschvorgang hinterlässt keine verwaisten Teams, Partien oder Feldzuteilungen
- Ein abgelehnter Speichervorgang verändert das Turnier nicht, auch nicht teilweise

## Geschäftsregeln

### BR-001: Unveränderliche Identität

Jedes Turnier trägt eine systemseitig vergebene, systemweit eindeutige ID und einen Namen
(FR-005). Die ID wird nie geändert und nach einer Löschung nicht wiederverwendet, damit
Protokolleinträge dauerhaft auflösbar bleiben.

### BR-002: Turniername eindeutig je Kalenderjahr

Name und Kalenderjahr des Turnierdatums sind zusammen eindeutig. «Plauschturnier Herbst» darf
2026 und 2027 existieren, aber nicht zweimal im Jahr 2026. Der Vergleich ist unabhängig von
Gross- und Kleinschreibung und von führenden oder folgenden Leerzeichen.

### BR-003: Löschen nur ohne erfasste Ergebnisse

Ein Turnier wird hart gelöscht, solange für keine seiner Partien ein Ergebnis erfasst ist. Ist
mindestens ein Ergebnis erfasst, ist Löschen ausgeschlossen; an seine Stelle tritt die
Archivierung (A4). Ein generierter, aber noch nicht gespielter Spielplan verhindert das Löschen
nicht.

### BR-004: Protokollpflicht

Anlage, Änderung, Löschung und Archivierung eines Turniers werden mit Zeitpunkt, handelnder
Person, Turnier-ID und Turniername protokolliert. Protokolleinträge werden nicht überschrieben
und nicht gelöscht (NFR-014) — auch dann nicht, wenn das Turnier selbst gelöscht wird.

### BR-005: Serverseitige Validierung der Stammdaten

Name, Datum und Ort werden serverseitig geprüft: Name und Ort sind nicht leer und höchstens
120 Zeichen lang, das Datum ist ein gültiges Kalenderdatum. Eine Prüfung im React-Frontend
zählt nie als Schutzmassnahme. Ein Turnierdatum in der Vergangenheit ist erlaubt — Turniere
werden auch nachträglich erfasst.

### BR-006: Datumsänderung verschiebt, sie plant nicht neu

Eine Datumsänderung verschiebt bestehende Zeitfenster als Ganzes. Sie löst keine
Neugenerierung des Spielplans aus und verändert weder die Paarungen noch die Feldzuteilung;
dafür ist UC-007 zuständig.

### BR-007: Archivierung verändert keine Fachdaten

Die Archivierung setzt ausschliesslich den Status. Partien, Ergebnisse, Tabellen und
Protokolleinträge bleiben unverändert und nachrechenbar (NFR-002). Ein archiviertes Turnier ist
lesbar, aber nicht mehr bearbeitbar; auch Ergebnisse werden darin nicht mehr erfasst.

### BR-008: Nur aktive Turniere in der Arbeitsansicht

Die Turnierübersicht nach Schritt 2 zeigt Turniere im Status `PLANNING`, `RUNNING` und
`FINISHED`. Archivierte Turniere erscheinen nur über den Archivfilter und werden in keinem
anderen Use Case zur Auswahl angeboten.

### BR-009: Verwaltung ausschliesslich durch das Organisationsteam

Turniere werden allein von angemeldeten Organisator:innen verwaltet. Teams haben keinen Login
(C-013) und Vorort-Mitarbeitende keinen Zugriff auf Turnier-Stammdaten; sie erfassen Ergebnisse
(UC-011) und filtern Spiele (UC-018).

## Offene Punkte

- [ ] Der Status `ARCHIVED` ergänzt die Turnier-Entität um ein Feld, das in
  `docs/entity-model.md` noch nicht modelliert ist — die Datei ist leer.
- [ ] Ob der Übergang `PLANNING` → `RUNNING` → `FINISHED` automatisch aus dem Turnierdatum und
  dem Erfassungsstand folgt oder von der Organisator:in gesetzt wird, ist in UC-006
  (Phasen planen) zu entscheiden; UC-001 setzt lediglich `PLANNING` beim Anlegen.
- [ ] Ob ein archiviertes Turnier als Vorlage für ein neues dienen kann («Turnier duplizieren»),
  ist nicht gefordert und hier bewusst ausgelassen.
