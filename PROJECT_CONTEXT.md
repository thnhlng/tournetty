# Projekt-Kontext — Tournetty

> **Zweck dieses Dokuments:** Kontext-Rahmen für KI-Werkzeuge (Claude Code, Copilot & Co.) und
> für neue Mitwirkende. Es beantwortet in Kurzform: *Was bauen wir, womit, nach welchen Regeln,
> und was darf die KI nicht.*
>
> **Verbindlichkeit:** Die Abschnitte 5 (Sicherheits-Leitplanken) und 6 (Regeln für den Einsatz
> von KI) sind bindend. Bei Widerspruch zwischen diesem Dokument und einem Prompt gilt dieses
> Dokument — Abweichungen müssen explizit begründet und im Chat benannt werden.
>
> **Abgrenzung:** Das *Was* steht in [`SPEC.md`](docs/SPEC.md), die ausführliche Herleitung mit
> C4-Sichten und ADR-Begründungen in [`projekt-konzeption.md`](projekt-konzeption.md).
> Dieses Dokument ist die verdichtete Arbeitsfassung, kein Ersatz für die beiden.
>
> **Version:** 0.7 · **Stand:** 04.11.2026 · Gliederung angelehnt an arc42

---

## 1. Zweck und Ziele

**Projekt:** Tournetty

**Zweck:**
Tournetty plant Plausch-Volleyball-Turniere für Organisationsteams. Das System nimmt
Team-Anmeldungen entgegen, erzeugt regelbasiert Gruppen- und K.-o.-Phasen, verteilt die Spiele
auf die verfügbaren Felder und Zeitfenster, erfasst Ergebnisse und berechnet Tabellen. Fällt
kurzfristig ein Team aus, schlägt eine KI eine Umplanung vor — anwenden darf sie der Mensch.

**Nicht-Ziele:**

* Turniermodus Schweizer System
* Erfassung einzelner Spieler eines Teams
* Anzeige von Sponsoren
* Selbstverwaltung / Mutation durch Teams, Login für Teams
* Live-Ticker
* Automatisches Anwenden von KI-Vorschlägen ohne Freigabe

**Stakeholder (Kurzform, ausführlich in `projekt-konzeption.md` §1.3):**
Organisationsteam · Teams/Teamverantwortliche · Vorort-Mitarbeitende · Publikum

---

## 2. Architektur

**Architekturstil:**
Modularer Monolith mit fachlichem Schnitt; Ports und Adapter für alles, was das System verlässt
(ADR-001, ADR-2).

**Begründung:**
Ein Team, ein Deployment-Ziel, weniger als 50 gleichzeitige Nutzende — verteilte Systeme
verbessern hier kein Qualitätsziel. Der fachliche Schnitt hält Erweiterungen um Turniermodi
lokal (Q3) und die Invarianten an einer Stelle prüfbar (Q1, Q4).

**Zentrale Architekturprinzipien:**

* **Regeln vor Modell:** der Spielplan entsteht deterministisch, die KI arbeitet nur am
  bestehenden Plan (ADR-3)
* **Ein Weg nach aussen:** LLM-Zugriff gebündelt hinter einem Port, mit Adapter und Fake (ADR-2)
* **Freigabe vor Wirkung:** kein Vorschlag verändert Daten vor der Annahme durch einen Menschen
  (ADR-5)
* **Konfiguration statt Code** für Modi, Punktesysteme, Tie-Breaker, Pausen- und Platzregeln
  (ADR-6)
* **Sofort antworten, im Hintergrund rechnen** (ADR-4)
* **Fremddaten werden an der Grenze normalisiert**, nie roh weiterverarbeitet — auch dann, wenn
  sie aus dem eigenen Frontend kommen (ADR-002)

---

## 3. Technologie-Stack

| Bereich | Wahl |
| --- | --- |
| Sprache | Java 25 |
| Backend-Framework | Spring Boot 4.1.1 (`spring-boot-starter-webmvc`) |
| Frontend | React, inkl. eigenem Anmeldeformular (ab Block 2 — ADR-002) |
| Datenbank | PostgreSQL (geplant, Heroku Postgres) |
| Testing | JUnit 5 via `spring-boot-starter-webmvc-test` |
| KI | externer LLM-Provider hinter `PlanAdvisorPort`, deterministischer `FakeAdvisor` für Tests |
| Build | Maven Wrapper (`./mvnw`) |
| Deployment | Backend auf Heroku, Frontend auf Cloudflare |

**Noch nicht im Code vorhanden** (nicht als existierend annehmen): Persistenzschicht,
Frontend, LLM-Adapter. Der konkrete LLM-Provider ist offen (siehe Abschnitt 9).

---

## 4. Struktur und Konventionen

### 4.1 Paket-/Modulstruktur (Zielbild)

```text
ch.tournetty
├── tournament/     # FR-1 · Turnier, Team, Gruppe, Spielfeld
├── phase/          # FR-3 · Phasenfolge und Übergänge
├── schedule/       # FR-2 · rules/ · generator/ · invariants/
├── court/          # FR-5 · Feldzuteilung und Auslastung
├── scoring/        # FR-4 · Tabelle, Tie-Breaker, K.-o.-Weiterleitung
├── proposal/       # ADR-5 · Vorschlag, Begründung, Annahme
├── signup/         # FR-7 · ADR-002 · öffentlicher Anmelde-Endpunkt
├── dashboard/      # FR-6 · Lesesichten
└── platform/       # llm/ · audit/ · persistence/
```

```text
frontend/
├── admin/          # authentifiziert: Planung, Erfassung, Freigabe
└── signup/         # FR-7 · öffentliches Anmeldeformular
```

**Ist-Stand:** Das Skelett liegt aktuell unter `ch.tournetty.tournetty_webapp` mit einem
technisch geschnittenen `controller/`-Paket (`TournamentController`, Hello-World-Endpunkt).
Die Umstellung auf den fachlichen Schnitt oben steht noch aus — neue Fachlogik gehört in das
Zielbild, nicht in `controller/`.

### 4.2 Namenskonventionen

* **Klassen:** `UpperCamelCase`, fachlich benannt (`RoundRobinGenerator`, `CourtAssignment`),
  keine `…Manager`/`…Helper`
* **Methoden:** `lowerCamelCase`, Verb voran (`generateSchedule`, `acceptProposal`)
* **REST-Endpunkte:** `/api/<ressource>` im Plural, kebab-case
  (`/api/tournaments/{id}/schedule`); Verben ausschliesslich über HTTP-Methoden
* **Tests:** `<Klasse>Test` für Unit-Tests, `<Fall>CorpusTest` für versionierte
  Evaluationsfälle; Methoden nach `should…When…`
* **Datenbankobjekte:** `snake_case`, Tabellen im Plural (`tournaments`, `matches`,
  `court_assignments`), Fremdschlüssel `<tabelle_singular>_id`

### 4.3 Coding-Konventionen

* Kein Modul greift auf interne Klassen eines anderen Moduls zu — nur auf dessen öffentliche
  Schnittstelle
* Fachlogik enthält keine Framework-Annotationen, wo es vermeidbar ist; sie soll ohne
  Spring-Kontext testbar sein
* Turnierregeln stehen nie als `if`-Kaskade im Code, sondern als Konfiguration (ADR-6)
* Jede Änderung an `schedule/` braucht einen Test, der die Invariante «ein Team, ein Slot» prüft
* Keine Secrets im Code oder in `application.properties` — nur Umgebungsvariablen
* Fehler werden explizit behandelt; kein stilles Verschlucken von Exceptions

---

## 5. Qualitätsanforderungen

Die Reihenfolge gilt bei Zielkonflikten (siehe `SPEC.md` §3):

1. **Korrektheit** — Ein Team spielt höchstens ein Spiel pro Slot; Tabellen und
   K.-o.-Weiterleitungen sind nachrechenbar. Diese Invariante ist an *einer* Stelle
   implementiert und getestet.
2. **Wartbarkeit** — Turniermodi, Kategorien, Punktesysteme, Pausenzeiten und Platzregeln sind
   konfigurierbar statt fest im Code. Ein neues Punktesystem berührt genau ein Paket.
3. **Testbarkeit** — Spielplanerstellung, Tabellenberechnung, Tie-Breaker und Regelprüfungen
   sind deterministisch und laufen automatisiert — der KI-Pfad ebenfalls, gegen den Fake statt
   gegen den Provider.

Weitere Ziele: **Regelmässigkeit** (Felder pro Slot ausgelastet), **Performance** (Erfassung
antwortet sofort, Berechnung im Hintergrund), **Nachvollziehbarkeit** (KI begründet, Mensch
entscheidet, alles protokolliert).

---

## 6. Sicherheits-Leitplanken

Die folgenden Regeln sind **verbindlich**:

* Daten aus dem Anmeldeformular gelten als ungeprüfte Fremddaten und werden ausschliesslich
  über `signup/` serverseitig validiert und normalisiert eingelesen. Validierung im
  React-Frontend zählt nie als Schutzmassnahme.
* Der `signup`-Endpunkt ist der einzige unauthentifizierte Schreib-Endpunkt. Er darf
  ausschliesslich Anmeldungen im Status `PENDING` anlegen und unterliegt einem Rate-Limit pro IP.
* Es gibt genau einen Ausgang zum LLM-Provider (`PlanAdvisorPort`); kein Modul ruft den
  Provider direkt.
* Der an den Provider gesendete Planungskontext ist pseudonymisiert — keine Klarnamen, keine
  Kontaktdaten.
* Jede Modellantwort wird zweistufig geprüft: **Schema** und **Turnierregeln**.
* KI-generierte Inhalte werden nicht ungeprüft übernommen.
* Das Anwenden eines KI-Spielplanvorschlags benötigt immer menschliche Freigabe.
* Geheimnisse/API-Keys dürfen nicht im Repository gespeichert werden — nur Umgebungsvariablen.
* Schreibende Endpunkte sind authentifiziert; nach fünf Fehlversuchen wird das Konto gesperrt
  (NfA-2).
* Vorschlag, Begründung und Entscheid werden append-only protokolliert und nicht überschrieben.

---

## 7. Regeln für den Einsatz von KI

**Die KI darf:**

* Umplanungen am bestehenden Spielplan vorschlagen, inklusive Begründung
* auf Anfrage eine Gruppenaufteilung anhand der Teilnehmerzahl vorschlagen
* Boilerplate und Vorschläge generieren
* Tests oder Dokumentationsentwürfe vorschlagen

**Die KI darf nicht selbstständig:**

* den initialen Spielplan erzeugen — das ist deterministisch und regelbasiert (ADR-3)
* Daten in der Datenbank verändern, insbesondere keine Spiele, Feldzuteilungen oder Ergebnisse
* sicherheitskritische Aktionen ausführen
* Architekturentscheidungen ohne Prüfung als verbindlich festlegen

**KI-generierter Code muss vor der Übernahme geprüft werden auf:**
fachliche Korrektheit · Sicherheitsprobleme · Architekturkonformität · unnötige Abhängigkeiten
· Fehlerbehandlung · Testbarkeit

---

## 8. Aktuelle Architekturentscheidungen

| ADR     | Entscheidung                                                                                                                   | Status   |
| ------- | ------------------------------------------------------------------------------------------------------------------------------ | -------- |
| ADR-001 | Modularer Monolith, fachlich geschnitten, mit Ports und Adaptern                                                                | Accepted |
| ADR-2   | LLM nur hinter einem Port, ein Adapter, ein Fake; Antwort gegen Schema geprüft                                                  | Accepted |
| ADR-3   | Spielplanerzeugung deterministisch und regelbasiert; die KI schlägt nur vor                                                     | Accepted |
| ADR-4   | Erfassung antwortet sofort; Plan- und Vorschlagsberechnung laufen als Job                                                       | Accepted |
| ADR-5   | HITL-Gate: kein Vorschlag wird ohne Annahme persistiert, Protokoll append-only                                                  | Accepted |
| ADR-6   | Turniermodi, Punktesysteme, Tie-Breaker, Pausen- und Platzregeln als Konfiguration                                              | Accepted |
| ADR-002 | Eigenes Anmeldeformular im React-Frontend statt externem Formular-Dienst; öffentlicher `signup`-Endpunkt validiert serverseitig | Accepted |
| ADR-003 | Umplanungsvorschläge fallen bei LLM-Ausfall auf den deterministischen Regelkern zurück, gekennzeichnet und ohne Begründungstext; Freigabe gilt unverändert | Accepted |

Volltext und Begründungen: `projekt-konzeption.md`.

---

## 9. Offene Punkte

* [ ] NfA-2 fordert Kontosperrung nach fünf Fehlversuchen, während «Login für Teams»
  ausgeschlossen ist — für wen genau gilt die Anforderung? *Annahme bis auf Weiteres: nur
  Organisationsteam und Vorort-Mitarbeitende.*
* [ ] Für den öffentlichen `signup`-Endpunkt (ADR-002) fehlt eine nichtfunktionale Anforderung:
  Wie viele Anmeldungen pro IP und Minute sind zulässig, und was passiert bei Überschreitung?
  *Startwert zur Kalibrierung: 5 Anmeldungen pro IP und Stunde.*
* [ ] Braucht die Anmeldung eine Bestätigung per E-Mail (Double Opt-in), oder genügt die Annahme
  durch den Organisator? *Annahme bis auf Weiteres: die Annahme durch den Organisator genügt.*
* [ ] `SPEC.md` §7 «Evaluation» ist leer, obwohl Q4 und NfA-4 einen versionierten Testkorpus
  voraussetzen — die Fälle aus `projekt-konzeption.md` §6.2 übernehmen.
* [ ] Zielkonflikt FR-2 («möglichst wenig Änderungen») gegen Q2 (volle Feldauslastung): Wie wird
  die Abwägung dem Organisator zur Auswahl gestellt — zwei Varianten oder ein Schieberegler?
* [ ] Wahl des konkreten LLM-Providers und des Kostenrahmens ist offen
* [ ] Persistenz noch nicht implementiert: JPA/Hibernate oder JDBC, Migrationswerkzeug
  (Flyway/Liquibase)
* [ ] `docs/entity-model.md` ist leer, hält aber inzwischen entschiedene Attribute zurück: der
  Turnierstatus `PLANNING`/`RUNNING`/`FINISHED`/`ARCHIVED` aus UC-001 ist nirgends modelliert.
* [ ] Wer setzt den Übergang `PLANNING` → `RUNNING` → `FINISHED` — leitet ihn das System aus
  Turnierdatum und Erfassungsstand ab, oder setzt ihn der Organisator? *UC-001 setzt beim
  Anlegen nur `PLANNING`; zu entscheiden beim Ausarbeiten von UC-006 (Phasen planen).*

---

## 10. Änderungshistorie

| Version | Datum      | Änderung                                                                                                                                                                                                                             |
| ------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 0.1     | 03.09.2026 | Initiale Version                                                                                                                                                                                                                      |
| 0.2     | 07.09.2026 | Kontext-Dokument aus `SPEC.md` v0.1 abgeleitet und vollständig ausgefüllt                                                                                                                                                              |
| 0.3     | 07.09.2026 | ADR-002: eigenes Anmeldeformular statt externem Formular-Dienst; C4-Sichten, Vertrauensgrenze 2, Modulstruktur (`intake` → `signup`) und Sicherheits-Leitplanken angepasst                                                            |
| 0.4     | 24.09.2026 | Stakeholder überarbeitet: auf vier Stakeholder verdichtet, Akteurstabelle und C4-Diagramme angeglichen                                                                                                                                 |
| 0.5     | 24.09.2026 | Als eigenständige Datei `PROJECT_CONTEXT.md` aus `projekt-konzeption.md` §7 herausgelöst; Verbindlichkeits- und Abgrenzungshinweis ergänzt; Ist-Stand von Modulstruktur und Stack gegenüber dem Zielbild kenntlich gemacht; `CLAUDE.md` bindet das Dokument als KI-Kontext ein |
| 0.6     | 31.10.2026 | UC-002 (Teams verwalten) ausgearbeitet; offener Punkt zum Umgang mit gespielten Partien eines ausgefallenen Teams entschieden — Konfigurationsregel `withdrawalHandling` mit `ANNUL`/`FORFEIT`; ADR-003 ergänzt (regelbasierter Rückfallpfad bei LLM-Ausfall) |
| 0.7     | 04.11.2026 | UC-001 (Turnier verwalten) ausgearbeitet in `docs/use-cases/uc-001-turnier-verwalten.md`; drei Fachentscheide festgelegt — hartes Löschen nur ohne erfasste Ergebnisse, danach Statuswechsel auf `ARCHIVED` (BR-003/BR-007, NFR-002); Turniername eindeutig je Kalenderjahr (BR-002); Turnier-Stammdaten sind Name, Datum und Ort, Felder/Regeln/Phasen bleiben bei UC-004/005/006. Turnierstatus `PLANNING`/`RUNNING`/`FINISHED`/`ARCHIVED` eingeführt; FR-001 und FR-005 in `docs/requirements.md` auf UC-001 verlinkt |
| ...     |            |                                                                                                                                                                                                                                        |
| 1.0     |            |                                                                                                                                                                                                                                        |
