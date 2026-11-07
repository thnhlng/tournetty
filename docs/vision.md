# Vision

Tournetty ist eine Webanwendung, mit der Organisationsteams Plausch-Volleyball-Turniere
planen und durchführen. Das System nimmt Team-Anmeldungen entgegen, erzeugt regelbasiert
Gruppen- und K.-o.-Phasen, verteilt die Spiele auf Felder und Zeitfenster, erfasst
Ergebnisse und berechnet Tabellen.

Heute entstehen solche Turniere meist von Hand, mit Tabellenkalkulation, Papier und
Telefon. Das ist aufwändig und unflexibel: Eine kurzfristige Abmeldung zwingt das
Organisationsteam, Gruppen, Spielplan und Feldzuteilung erneut durchzurechnen, oft unter
Zeitdruck am Turniertag. Tournetty übernimmt diese Rechenarbeit und schlägt bei einem
Ausfall eine Umplanung vor. Angewendet wird sie erst, wenn ein Mensch sie freigibt.

Das System setzt auf nachrechenbare Regeln statt auf Vorhersagen. Es soll nicht möglichst
viele Funktionen haben, sondern einen Spielplan erzeugen, dem das Organisationsteam
vertrauen kann.

## Business Goals

* Organisationsteams planen ein Turnier mit Gruppenphase und K.-o.-Runde ohne
  Tabellenkalkulation
* Teams melden sich selbst an, der Organisator nimmt die Anmeldungen an oder lehnt sie ab
* Die verfügbaren Felder sind pro Zeitfenster ausgelastet, das Turnier bleibt im Zeitplan
* Ein Teamausfall führt zu einem geprüften Umplanungsvorschlag statt zu manuellem
  Neuaufbau des Spielplans
* Spielplan, Tabellen und K.-o.-Weiterleitungen bleiben jederzeit nachrechenbar
* Neue Turniermodi und Punktesysteme entstehen durch Konfiguration, nicht durch Code

## Key Principles

**Regeln vor Modell**
Der Spielplan entsteht deterministisch und regelbasiert. Die KI plant nicht, sie schlägt
Änderungen am bestehenden Plan vor.

**Freigabe vor Wirkung**
Kein Vorschlag verändert Daten, bevor ein Mensch ihn annimmt. Vorschlag, Begründung und
Entscheid werden protokolliert.

**Korrektheit vor Komfort**
Ein Team spielt höchstens ein Spiel pro Zeitfenster. Diese Invariante ist an einer Stelle
implementiert und getestet.

**Konfiguration statt Code**
Turniermodi, Punktesysteme, Tie-Breaker, Pausen- und Platzregeln sind einstellbar, damit
ein neues Turnierformat kein neues Release braucht.

**Fremddaten bleiben Fremddaten**
Auch Eingaben aus dem eigenen Anmeldeformular werden serverseitig geprüft. Validierung im
Browser ist Komfort, nicht Schutz.

## Scope

**In scope:**

* Anmeldung über ein eigenes Formular, mit Annahme durch den Organisator
* Turniere, Teams, Gruppen, Phasen und Spielfelder verwalten
* Spielplangenerierung: Gruppenphase als Round-Robin, danach K.-o.-Runden
* Verteilung der Spiele auf Felder und Zeitfenster
* Ergebniserfassung, Tabellen und Weiterleitung im K.-o.
* KI auf Anfrage: Vorschlag zur Gruppenaufteilung und zur Umplanung bei Teamausfall

**Out of scope:**

* Turniermodus Schweizer System
* Erfassung einzelner Spieler eines Teams
* Anzeige von Sponsoren
* Selbstverwaltung durch Teams und Team-Login
* Live-Ticker
* automatisches Anwenden von KI-Vorschlägen ohne Freigabe

## Target Users

* Organisationsteams, die ein Turnier planen und die Freigaben erteilen
* Vorort-Mitarbeitende, die während des Turniers Ergebnisse erfassen
* Teamverantwortliche, die ihr Team anmelden und den Spielplan verfolgen
* Publikum, das Spielplan und Tabellen nachschaut

## Quality Goals

* Korrektheit vor Vollständigkeit
* Ausgelastete Felder und ein Turnier, das im Zeitplan bleibt
* Konfigurierbares Verhalten statt eingebauter Turnierregeln
* Deterministisches, automatisiert prüfbares Verhalten, den KI-Pfad eingeschlossen
* Schnelle Rückmeldung bei Erfassung und Anmeldung, aufwändige Berechnung danach
* Einfache Architektur, die Änderungen trägt
