# Requirements Catalog

Dieses Dokument enthält die funktionalen Anforderungen, nichtfunktionalen Anforderungen und
Rahmenbedingungen für Tournetty. Quelle: [`SPEC.md`](SPEC.md) §1–§9, Ziel und Scope in
[`vision.md`](vision.md).

## Functional Requirements

| ID     | Title                        | User Story                                                                                                                                | Priority | Status |
|--------|------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|----------|--------|
| FR-001 | Turnier verwalten            | Als Organisator möchte ich Turniere erstellen, bearbeiten und löschen, damit ich jeden Anlass getrennt planen kann.                       | High     | Open   |
| FR-002 | Teams verwalten              | Als Organisator möchte ich Teams erstellen, bearbeiten und abmelden, damit die Teilnehmerliste dem tatsächlichen Teilnehmerfeld entspricht. Ausgearbeitet als [UC-002](use-cases/use-case-list.md#uc-002--teams-verwalten). | High     | Open   |
| FR-003 | Gruppen verwalten            | Als Organisator möchte ich Gruppen erstellen und Teams zuordnen, damit die Gruppenphase gespielt werden kann.                             | High     | Open   |
| FR-004 | Spielfelder verwalten        | Als Organisator möchte ich Spielfelder erfassen, damit die Spiele auf die vorhandenen Felder verteilt werden können.                      | High     | Open   |
| FR-005 | Eindeutige Identität         | Als Organisator möchte ich, dass jedes Turnier, jede Gruppe, Phase, Partie und jedes Feld eine eindeutige ID und einen Namen trägt, damit Objekte referenzierbar bleiben. | High     | Open   |
| FR-006 | Anmeldeformular absenden     | Als Teamverantwortlicher möchte ich mein Team über ein Formular anmelden und sofort eine Bestätigung erhalten, damit ich weiss, dass die Anmeldung angekommen ist. | High     | Open   |
| FR-007 | Anmeldung validieren         | Als Organisator möchte ich, dass unvollständige Anmeldungen und doppelte Teamnamen abgelehnt und dem Absender begründet werden, damit keine unbrauchbaren Anmeldungen entstehen. | High     | Open   |
| FR-008 | Anmeldung annehmen           | Als Organisator möchte ich eingegangene Anmeldungen ansehen und annehmen oder ablehnen, damit nur geprüfte Anmeldungen zu Teams werden.    | High     | Open   |
| FR-009 | Phasen erstellen             | Als Organisator möchte ich die Phasen eines Turniers erstellen, damit der Ablauf von Gruppenphase zu K.-o.-Runde festgelegt ist.           | High     | Open   |
| FR-010 | Phasenfolge ändern           | Als Organisator möchte ich Phasen per Drag-and-Drop umordnen, damit ich die Reihenfolge ohne Neuanlage korrigieren kann.                   | Medium   | Open   |
| FR-011 | Spielplan generieren         | Als Organisator möchte ich den Spielplan generieren lassen — Gruppenphase als Round-Robin, K.-o.-Runden als Einzelspiel —, damit ich ihn nicht von Hand aufstellen muss. | High     | Open   |
| FR-012 | Spiele auf Felder verteilen  | Als Organisator möchte ich die Spiele gleichmässig auf die verfügbaren Felder und Zeitfenster verteilt bekommen, damit kein Feld leer steht. | High     | Open   |
| FR-013 | Spiele verschieben           | Als Organisator möchte ich Spiele per Drag-and-Drop auf ein anderes Feld oder Zeitfenster verschieben, damit ich den Plan vor Ort anpassen kann. | Medium   | Open   |
| FR-014 | Ergebnisse erfassen          | Als Vorort-Mitarbeitender möchte ich das Ergebnis einer Partie erfassen und korrigieren, damit der Turnierstand aktuell bleibt.            | High     | Open   |
| FR-015 | Tabelle berechnen            | Als Teilnehmer möchte ich die Gruppentabelle nach erfasstem Ergebnis sehen, damit der Stand der Gruppe nachvollziehbar ist.                | High     | Open   |
| FR-016 | K.-o.-Weiterleitung          | Als Organisator möchte ich, dass Sieger automatisch in die nächste K.-o.-Runde weitergeleitet werden, damit der Baum ohne Handarbeit füllt. | High     | Open   |
| FR-017 | Öffentliches Dashboard       | Als Teilnehmer oder Zuschauer möchte ich eine Übersicht mit Spielen und Tabellen öffnen, damit ich den Turnierverlauf verfolgen kann.      | Medium   | Open   |
| FR-018 | Administrations-Dashboard    | Als Organisator möchte ich eine Übersicht aller Entitäten eines Turniers mit Bearbeiten, Erstellen und Löschen, damit ich alles an einem Ort verwalten kann. | Medium   | Open   |
| FR-019 | KI-Vorschlag Gruppenanzahl   | Als Organisator möchte ich auf Anfrage einen KI-Vorschlag zur Gruppenaufteilung anhand der Teilnehmerzahl erhalten, damit ich die Einteilung nicht selbst durchrechnen muss. | Medium   | Open   |
| FR-020 | KI-Vorschlag Umplanung       | Als Organisator möchte ich bei einem Teamausfall einen KI-Vorschlag für einen angepassten Spielplan mit möglichst wenig Änderungen erhalten, damit ich am Turniertag schnell reagieren kann. | Medium   | Open   |
| FR-021 | Vorschlag begründet anzeigen | Als Organisator möchte ich zu jedem KI-Vorschlag die Begründung und die Liste der Änderungen sehen, damit ich den Vorschlag beurteilen kann. | High     | Open   |
| FR-022 | Vorschlag freigeben          | Als Organisator möchte ich einen KI-Vorschlag annehmen oder verwerfen, damit nur freigegebene Änderungen wirksam werden.                   | High     | Open   |
| FR-023 | Entscheide protokollieren    | Als Organisator möchte ich Vorschlag, Begründung und Entscheid im Protokoll nachlesen, damit später nachvollziehbar ist, wer was entschieden hat. | Medium   | Open   |
| FR-024 | Turnierregeln konfigurieren  | Als Organisator möchte ich Punktesystem, Tie-Breaker, Pausen- und Platzregeln einstellen, damit ein neues Turnierformat kein Release braucht. | Medium   | Open   |
| FR-025 | Spiele filtern               | Als Vorort-Mitarbeitender möchte ich Spiele nach Feld, Zeitfenster und Status filtern, damit ich die für mich relevanten Partien finde.     | Low      | Open   |
| FR-026 | Am System anmelden           | Als Organisator oder Vorort-Mitarbeitender möchte ich mich anmelden, damit schreibende Funktionen nur berechtigten Personen offenstehen.    | High     | Open   |

## Non-Functional Requirements

| ID      | Title                         | Requirement                                                                                                           | Category        | Priority | Status |
|---------|-------------------------------|-----------------------------------------------------------------------------------------------------------------------|-----------------|----------|--------|
| NFR-001 | Invariante «ein Team, ein Slot» | Kein Team darf im selben Zeitfenster für zwei Partien angesetzt sein. Die Prüfung liegt an einer Stelle und ist getestet. | Quality         | High     | Open   |
| NFR-002 | Nachrechenbarkeit             | Tabellenstände und K.-o.-Weiterleitungen müssen aus den erfassten Ergebnissen reproduzierbar sein.                      | Quality         | High     | Open   |
| NFR-003 | Feldauslastung                | Die verfügbaren Felder müssen pro Zeitfenster ausgelastet sein.                                                        | Quality         | Medium   | Open   |
| NFR-004 | Konfigurierbarkeit            | Turniermodi, Kategorien, Punktesysteme, Pausenzeiten und Platzregeln sind Konfiguration, nicht Code. Ein neues Punktesystem berührt genau ein Paket. | Maintainability | High     | Open   |
| NFR-005 | Deterministisches Verhalten   | Spielplanerstellung, Tabellenberechnung, Tie-Breaker und Regelprüfungen liefern bei gleicher Eingabe dasselbe Ergebnis. | Quality         | High     | Open   |
| NFR-006 | Testbarkeit des KI-Pfads      | Der KI-Pfad muss ohne Aufruf des echten Providers automatisiert prüfbar sein.                                           | Quality         | High     | Open   |
| NFR-007 | Sofortige Bestätigung         | Anmeldung und Ergebniserfassung werden sofort bestätigt, ohne auf eine Planberechnung zu warten.                        | Performance     | High     | Open   |
| NFR-008 | Berechnung im Hintergrund     | Spielplan- und Vorschlagsberechnungen dürfen nach der Bestätigung im Hintergrund laufen; der Zustand ist für den Nutzer sichtbar. | Performance     | Medium   | Open   |
| NFR-009 | Gleichzeitige Nutzende        | Das System trägt mindestens 50 gleichzeitige Nutzende.                                                                 | Scalability     | Medium   | Open   |
| NFR-010 | Kontosperrung                 | Nach fünf fehlgeschlagenen Login-Versuchen wird das Konto gesperrt.                                                     | Security        | High     | Open   |
| NFR-011 | Responsive Bedienung          | Die Anwendung ist auf Mobilgerät und Monitor bedienbar.                                                                | Usability       | High     | Open   |
| NFR-012 | Begründete KI-Ausgabe         | Jeder KI-Vorschlag enthält eine für den Organisator lesbare Begründung.                                                | Usability       | High     | Open   |
| NFR-013 | Freigabe vor Wirkung          | Ein KI-Vorschlag verändert keine Daten, bevor ein Mensch ihn annimmt.                                                   | Security        | High     | Open   |
| NFR-014 | Append-only-Protokoll         | Vorschlag, Begründung und Entscheid werden protokolliert und nicht überschrieben oder gelöscht.                        | Reliability     | High     | Open   |
| NFR-015 | Serverseitige Validierung     | Anmeldedaten gelten als ungeprüfte Fremddaten und werden serverseitig validiert und normalisiert. Browser-Validierung zählt nicht als Schutz. | Security        | High     | Open   |
| NFR-016 | Rate-Limit Anmeldung          | Der öffentliche Anmelde-Endpunkt ist pro IP begrenzt. Startwert zur Kalibrierung: 5 Anmeldungen pro IP und Stunde.      | Security        | High     | Open   |
| NFR-017 | Pseudonymisierter KI-Kontext  | Der an den Provider gesendete Planungskontext enthält keine Klarnamen und keine Kontaktdaten.                           | Security        | High     | Open   |
| NFR-018 | Zweistufige Antwortprüfung    | Jede Modellantwort wird gegen ein Schema und gegen die Turnierregeln geprüft, bevor sie angezeigt wird.                 | Security        | High     | Open   |
| NFR-019 | Geheimnisverwaltung           | API-Keys und Geheimnisse liegen ausschliesslich in Umgebungsvariablen, nie im Repository.                               | Security        | High     | Open   |
| NFR-020 | Verständliche Fehler          | Fachliche Fehler werden mit klarer, lesbarer Meldung zurückgegeben; Exceptions werden nicht still verschluckt.          | Usability       | Medium   | Open   |
| NFR-021 | Versionierter Testkorpus      | Für Teamausfall, ungerade Teamzahl, Gleichstand und Feldausfall existieren versionierte Testfälle mit erwartetem Ergebnis. | Quality         | Medium   | Open   |

## Constraints

| ID    | Title                        | Constraint                                                                                                     | Category    | Priority | Status |
|-------|------------------------------|----------------------------------------------------------------------------------------------------------------|-------------|----------|--------|
| C-001 | Java-Laufzeit                | Das Backend läuft auf Java 25.                                                                                 | Technical   | High     | Open   |
| C-002 | Backend-Framework            | Das Backend wird mit Spring Boot 4.1.1 (`spring-boot-starter-webmvc`) umgesetzt.                               | Technical   | High     | Open   |
| C-003 | Frontend-Framework           | Das Frontend wird mit React umgesetzt, inklusive eigenem Anmeldeformular.                                       | Technical   | High     | Open   |
| C-004 | Einzelne relationale Datenbank | Es wird genau eine relationale Datenbank verwendet (PostgreSQL), keine verteilten Speicher.                    | Technical   | High     | Open   |
| C-005 | Build-Werkzeug               | Der Build läuft über den Maven Wrapper (`./mvnw`).                                                              | Technical   | Medium   | Open   |
| C-006 | Deployment-Ziele             | Backend auf Heroku, Frontend auf Cloudflare, ein Deployment-Ziel je Teil.                                       | Operational | High     | Open   |
| C-007 | Kein eigenes Modell-Hosting  | Das Sprachmodell wird bei einem externen Provider eingekauft, nicht selbst betrieben.                           | Technical   | High     | Open   |
| C-008 | Kein externer Formular-Dienst | Die Anmeldung läuft über das eigene Frontend und einen eigenen öffentlichen Endpunkt, nicht über einen Formular-Dienst. | Technical   | High     | Open   |
| C-009 | Kein 24/7-Betrieb            | Kleines Team, kein eigener Betrieb rund um die Uhr; Verfügbarkeit nur zu üblichen Zeiten und am Turniertag.      | Operational | Medium   | Open   |
| C-010 | Kein Schweizer System        | Der Turniermodus Schweizer System wird nicht unterstützt.                                                       | Business    | High     | Open   |
| C-011 | Keine Spielerverwaltung      | Einzelne Spieler eines Teams werden nicht erfasst.                                                              | Business    | High     | Open   |
| C-012 | Keine Sponsorenanzeige       | Sponsoren werden nicht angezeigt.                                                                               | Business    | High     | Open   |
| C-013 | Kein Team-Login              | Teams erhalten keinen Login und können sich nicht selbst mutieren.                                              | Business    | High     | Open   |
| C-014 | Kein Live-Ticker             | Es gibt keinen Live-Ticker.                                                                                     | Business    | High     | Open   |
| C-015 | Kein automatischer KI-Plan   | Der initiale Spielplan wird deterministisch und regelbasiert erzeugt; die KI erzeugt ihn nicht.                  | Business    | High     | Open   |
| C-016 | Keine KI-Anwendung ohne Freigabe | KI-Vorschläge werden nie ohne menschliche Freigabe angewendet.                                              | Business    | High     | Open   |

## ID Prefixes Reference

| Prefix | Type                       | Example |
|--------|----------------------------|---------|
| FR     | Functional Requirement     | FR-001  |
| NFR    | Non-Functional Requirement | NFR-001 |
| C      | Constraint                 | C-001   |
| Q      | Quality Goal (SPEC §3)     | Q1      |

## Priority Reference

| Priority | Description                                                     |
|----------|-----------------------------------------------------------------|
| High     | Muss. Kernfunktion oder kritische Qualität.                     |
| Medium   | Soll. Wichtig, das System funktioniert aber auch ohne.          |
| Low      | Kann. Darf auf ein späteres Release verschoben werden.          |

## Status Reference

| Status      | Description                                      |
|-------------|--------------------------------------------------|
| Open        | Anforderung definiert, noch nicht umgesetzt.     |
| In Progress | Wird aktuell umgesetzt.                          |
| Implemented | Umsetzung fertig, Verifikation offen.            |
| Verified    | Getestet und bestätigt.                          |
| Deferred    | Auf ein späteres Release verschoben.             |
| Rejected    | Aus dem Scope entfernt.                          |

## NFR Categories Reference

| Category        | Description                               |
|-----------------|-------------------------------------------|
| Performance     | Geschwindigkeit, Durchsatz, Antwortzeit   |
| Scalability     | Umgang mit Wachstum                       |
| Availability    | Verfügbarkeit, Fehlertoleranz             |
| Security        | Authentifizierung, Autorisierung, Grenzen |
| Usability       | Bedienbarkeit, Verständlichkeit           |
| Maintainability | Codequalität, Dokumentation, Modularität  |
| Reliability     | Verlässlichkeit, Konsistenz, Protokoll    |
| Quality         | Korrektheit, Testbarkeit, Determinismus   |

## Constraint Categories Reference

| Category    | Description                                   |
|-------------|-----------------------------------------------|
| Technical   | Technologie-Stack, Plattformen, Integrationen |
| Business    | Scope, Budget, organisatorische Vorgaben      |
| Schedule    | Termine, Meilensteine                         |
| Regulatory  | Recht, Compliance, Normen                     |
| Operational | Deployment, Betrieb, Support                  |

## Quality Goals Reference

Priorisiert — bei Zielkonflikten gilt diese Reihenfolge (SPEC §3):

| ID | Quality Goal     | Erfüllt durch           |
|----|------------------|-------------------------|
| Q1 | Korrektheit      | NFR-001, NFR-002        |
| Q2 | Regelmässigkeit  | NFR-003                 |
| Q3 | Wartbarkeit      | NFR-004                 |
| Q4 | Testbarkeit      | NFR-005, NFR-006, NFR-021 |
| Q5 | Performance      | NFR-007, NFR-008, NFR-009 |

## Traceability to SPEC.md

| SPEC | Titel                              | Anforderungen                              |
|------|------------------------------------|--------------------------------------------|
| FR-1 | CRUD für Domain-Objekte            | FR-001, FR-002, FR-003, FR-004, FR-005     |
| FR-2 | Spielplan-Generierung              | FR-011, FR-020                             |
| FR-3 | Phasen für den Turnierablauf       | FR-009, FR-010                             |
| FR-4 | Zählen                             | FR-014, FR-015, FR-016                     |
| FR-5 | Feldzuteilung                      | FR-012, FR-013                             |
| FR-6 | Dashboard                          | FR-017, FR-018                             |
| FR-7 | Anmeldung über eigenes Formular    | FR-006, FR-007, FR-008                     |
| NfA-1 | Zuverlässigkeit und Stabilität    | NFR-009                                    |
| NfA-2 | Sicherheit                        | NFR-010, FR-026                            |
| NfA-3 | Benutzerfreundlichkeit            | NFR-011                                    |
| NfA-4 | Nachvollziehbarkeit               | NFR-012, NFR-013, NFR-014, FR-021, FR-022, FR-023 |

## Glossary

| Begriff      | Definition                                                            |
|--------------|-----------------------------------------------------------------------|
| Slot         | Zeitfenster, in dem auf einem Feld eine Partie stattfindet.           |
| Round-Robin  | Modus, in dem jedes Team der Gruppe gegen jedes andere spielt.        |
| Tie-Breaker  | Regel zur Auflösung von Gleichstand in der Tabelle.                   |
| HITL         | Human-in-the-Loop: ein Mensch bestätigt vor jeder irreversiblen Aktion. |
| ADR          | Architecture Decision Record (dokumentierter Architektur-Entscheid).  |
| NfA          | Nichtfunktionale Anforderung in `SPEC.md` (hier: NFR).                |
