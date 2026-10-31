# SPEC.md — Tournetty

> **Zweck dieses Dokuments:** `SPEC.md` ist der **Input für den Entwurf** — Ziel, Scope,
> Qualitätsziele, Anforderungen, Guardrails.
>
> **Bewusst nicht enthalten:** Architektur-Entscheide, Design-Prinzipien, Guardrail-Matrix,
> fertiges Kontextdiagramm — ebenso wenig die Mittel (asynchron? Fallback? wo wird gefiltert?).
> Die SPEC sagt **was** gelten muss, nicht **wie** es erreicht wird. Das **Wie** kommt später
> (Architektur / ADRs).
>
> **Aufbau:** Die Abschnitte 1–9 sind die SPEC — nur das **Was**. Ab
> [Teil B](#teil-b--das-skelett-das-daraus-folgt) beginnt das **Wie**: Entwurf, ADRs und
> Erwartungshorizont. Die Trennlinie dazwischen ist Absicht — Teil B darf sich auf die SPEC
> berufen, die SPEC sich nie auf Teil B.
>
> **Version:** 0.1 · **Status:** Entwurf

---

## 1. Ziel

<!-- 2–4 Sätze: Was tut das System? Nutze aktive Verben (nimmt entgegen, erzeugt, schlägt vor, eskaliert). -->
Tournetty plant Volleyball-Turniere für das Organisationsteam. Viele Organisationsteam organisieren ihre Turniere von Hand.
Dies ist in der Regel sehr zeitaufwändig und unflexibel.
Das Ziel dieses Projekts ist diese Schritte zu automatisieren und auf unvorhersehbare Ereignisse wie kurzfristige Abmeldungen, schneller zu reagieren zu können.
Es generiert Gruppenaufteilung, K.O. Runden nach eingestellten Regeln. Es verteilt die Matches auf die Spielfeldern, so dass die maximal ausgeschöpft sind.
Die Spielergebnisse werden aufgezeichnet und Ergebnisse werden getrackt.

- **Zielgruppe:** Organisationsteam, Spieler und Zuschauer
- **Wichtigster Nutzen:** Eine automatische statt manuelle Turnier-Planung. Der KI entlastet der Mensch bei unvorhersehbare Ereignisse z.B. ein Team ist nicht aufgetaucht.

Dieser MVP-Slice deckt ab: Teams melden sich an -> Teilnehmerliste aufgenommen -> Gruppeneinteilung sowie Spiele generieren

## 2. Scope

### Enthalten
<!-- 4–6 Punkte, je eine Zeile, jeweils mit der wichtigsten Eigenschaft dahinter -->
- Teams können sich über ein eigenes Anmeldeformular im Frontend anmelden (kein externer Formular-Dienst)
- Turnier Generierung nach Gruppenphase(Round-Robin) und K.O. Runden
- Teams, Gruppen und Spielfeldern können erstellt werden
- Gestützt auf Anzahl Teilnehmer, schlägt die KI auf Anfrage vor wie viel Gruppen erstellt werden soll.
- Bei spontane Teamausfälle kann die KI Platzzuteilungen anpassen, jedoch nur mit Bestätigung vom User
- Ergebnisse können erfasst, geändert, gelesen und gelöscht werden

### Nicht enthalten
<!-- Explizite Abgrenzung: was naheliegend wäre, aber bewusst draussen bleibt -->
- Turniermodel Swiss
- Spieler von Teams können erfasst werden
- Sponsoren können angezeigt werden
- Teams können sich selber mutieren
- Login für Teams
- Live-Ticker

## 3. Qualitätsziele

Priorisiert — **bei Konflikten gilt diese Reihenfolge**:

1. **Korrektheit** — Ein Team kann nur einen Match spielen pro Slot
2. **Regelmässigkeit** — Alle Feldern sind pro Slot ausgelastet
3. **Wartbarkeit** Turniermodi, Kategorien, Punktesysteme, Pausenzeiten und Platzregeln sind konfigurierbar statt fest im Code eingebaut.
4. **Testbarkeit** — Spielplanerstellung, Tabellenberechnung, Tie-Breaker und Regelprüfungen funktionieren deterministisch und können automatisiert getestet werden.
5. **Performance** — Anmeldungen und Änderungen werden sofort bestätigt; aufwendige Spielplanberechnungen können danach im Hintergrund erfolgen.

*Diese Qualitätsattribute treiben die Architekturwahl.*

## 4. Systemkontext

**Akteure:** Organisator(organisiert das Turnier) ·
Vorort-Mitarbeitende (trägt Ergebnisse ein) · Teilnehmer (verfolgt das Ergebnis)

**Nachbarsysteme:** externer LLM-Provider für Klassifikation und Turnierplanung · DB für Ergebnisse und Gruppen speichern

*Kein externer Formular-Dienst:* Das Anmeldeformular ist Teil des eigenen Frontends und schreibt über einen
öffentlichen, unauthentifizierten API-Endpunkt. Die Anmeldedaten stammen trotzdem von aussen und gelten als
Fremddaten (siehe Vertrauensgrenze in Teil B).

## 5. Funktionale Anforderungen

Je Anforderung mindestens ein Akzeptanzkriterium (Given/When/Then, verkürzt).

### FR-1 · CRUD für Domain-Objekte
- **When** Turnier, Gruppe, Phase, Spiel oder Spielfeld erstellt wird, **then** erhält das es eine eindeutige ID. Es enthält mindestens die Attribute ID und Name.

### FR-2 · Spielplan Generierung
- **When** der User auf Planung klickt, **then** wird eine Phase-Planung-Seite angezeigt, wo der User die Phasen erstellen kann (Gruppen, K.O Runden)
- **When** die Phase-Planung fertig ist, **then** können die Spiele generiert (Gruppenphase immer nach Round-Robin und K.O. Runden immer nur ein Spiel).
- **When** während dem Turnier ein Team sich disqualifiziert oder vor dem Turnier nicht ankommen, **then** wird KI den Spielplan mit möglichst wenig Änderungen neu generieren.

### FR-3 · Phase Erstellung für Turnierablauf
- **When** der User auf Phase erstellen klickt, **then** er die Phasen des Turniers erstellen z.B. dass zuerst Gruppenphase dann K.O. Runden.
- **When** der User auf eine Phase klickt **then** kann die Phasen per drag and drop verschoben werden.

### FR-4 · Zählen
- **When** das Ergebnis eines Spieles eingetragen wurde **then** werden diese angezeigt. Bei Gruppenspiele wird korrekt gezählt, bei K.O. Runden werden die Teams korrekt weitergeleitet.

### FR-5 · Feld Zuteilung 
- **When** der User auf Spielplan generieren klickt, **then** werden die Spiele auf die verfügbaren Felder gleichmässig verteilt. Die Felder sind ausgelastet.
- **When** Die Spiele sind verschiebbar by drag and drop.

### FR-6 · Dashboard
- **When** der User das Dashboard öffnet, **then** wird eine Übersicht vom Turnier mit den Spielen und Tabellen angezeigt.
- **When** das Administration-Team das Dashboard öffnet, **then** wird eine Übersicht von den Entitäten eines Turniers angezeigt. Diese können vom User bearbeitet, neu erstellt und gelöscht werden.

### FR-7 · Anmeldung über eigenes Formular
- **When** ein Team das Anmeldeformular des Frontends absendet, **then** wird die Anmeldung validiert und als Anmeldung zum Turnier gespeichert; das Team erhält sofort eine Bestätigung.
- **When** die Anmeldung unvollständig oder ein Teamname bereits vergeben ist, **then** wird sie abgelehnt und der Grund im Formular angezeigt.
- **When** der Organisator die Anmeldungen öffnet, **then** sieht er alle eingegangenen Anmeldungen und kann sie annehmen oder ablehnen, bevor daraus Teams werden.


## 6. Nichtfunktionale Anforderungen (SMART)

| ID    | Anforderung                        | Messgrösse / Prüfung                                                                                          |
|-------|------------------------------------|---------------------------------------------------------------------------------------------------------------|
| NfA-1 | Die App ist zuverlässig und stabil | Die App unterstützt 50 gleichzeitige Usern                                                                    |
| NfA-2 | Sicherheit                         | Nach 5 fehlgeschlagene Login-Versuche wird das Konto gesperrt                                                 |
| NfA-3 | User-Freundlich                    | App ist auf Mobile sowie Monitor abgestimmt.                                                                  |
| NfA-4 | Nachvollziehbarkeit                | Die KI begründet seine Erstellung, die angezeigten Änderung wird nur angewendet, wenn der User es akzeptiert. |

## 7. Rahmenbedingungen
- Kleines Team, ein Deployment-Ziel, kein eigener Betrieb rund um die Uhr. Hosting Backend auf Heroku und Frontend auf Cloudflare.
- MVP-Volumen < 50 gleichzeitige Teilnehmern auf Frontend.
- Kein eigenes Modell-Hosting; ein externer Provider wird eingekauft.
   

## 7. Evaluation

- **Testkorpus (versioniert):** <!-- welche Fälle, mit welchem erwarteten Ergebnis -->
- **Vor/nach Änderungen gemessen:** <!-- Metriken mit Verweis auf NfA-IDs -->
- **KI-Anteile müssen prüfbar sein** — auch ohne Aufruf des echten Modells.

## 8. Annahmen & offene Fragen

- **Annahme:** <!-- z. B. Volumen, Nutzerzahl — und was daraus folgt -->
- **Offen:** <!-- Schwellwert / Parameter mit Startwert, noch zu kalibrieren -->
- **Offen:** <!-- Entscheidung, die noch mit jemandem abzustimmen ist -->

## 9. Glossar

- **<!-- Begriff -->** — <!-- Definition in einem Satz -->
- **HITL** — Human-in-the-Loop: ein Mensch bestätigt vor jeder irreversiblen Aktion.
- **ADR** — Architecture Decision Record (dokumentierter Architektur-Entscheid).
- **NfA** — nichtfunktionale Anforderung / Qualitätsattribut.

---

# Teil B — Das Skelett, das daraus folgt

Das ist das **Übungsergebnis**: aus den Abschnitten 1–9 oben eine Kontext- und eine
Container-Sicht sowie eine Paketstruktur — die drei Sichten sind nach
[`architecture.md`](architecture.md) ausgelagert. Die SPEC sagt **was** gelten muss — ab hier
steht das **Wie**.

## Die Entscheide, auf die sich alles Folgende beruft

Jeder Entscheid nennt das Qualitätsziel, aus dem er folgt — und was er kostet.

| ID        | Entscheid                                                                                                                                         | Getragen von                                                        | Preis                                                                                                   |
|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| **ADR-1** | Modularer Monolith, fachlich geschnitten (nicht nach Schichten)                                                                                   | Q3 Wartbarkeit · Rahmenbedingung: kleines Team, ein Deployment-Ziel | Module müssen sich diszipliniert an ihre Grenzen halten — der Compiler erzwingt es nicht                |
| **ADR-2** | LLM nur hinter einem Port, ein Adapter, ein Fake; jede Antwort wird gegen ein Schema geprüft                                                      | Q4 Testbarkeit · NfA-4                                              | Der Port ist der kleinste gemeinsame Nenner aller Provider — providerspezifische Stärken bleiben liegen |
| **ADR-3** | Spielplan-Erzeugung ist **deterministisch und regelbasiert**. Die KI plant nicht, sie **schlägt vor**                                             | Q1 Korrektheit · Q4 Testbarkeit                                     | Bei Umplanung ist die KI auf das beschränkt, was der Regelkern validieren kann                          |
| **ADR-4** | Erfassung und Anmeldung antworten sofort; Plan- und Vorschlagsberechnung laufen als Job                                                           | Q5 Performance · NfA-1                                              | Zwei Zustände statt einem: «berechnet» und «wird berechnet» — die UI muss das zeigen                    |
| **ADR-5** | HITL-Gate: kein KI-Vorschlag wird persistiert, bevor der Mensch ihn annimmt. Vorschlag, Begründung und Entscheid werden append-only protokolliert | NfA-4 Nachvollziehbarkeit                                           | Jede Umplanung kostet einen Klick — auch die offensichtlich richtige                                    |
| **ADR-6** | Turniermodi, Punktesysteme, Tie-Breaker, Pausen- und Platzregeln als Konfiguration, nicht als Code                                                | Q3 Wartbarkeit                                                      | Ein Konfigurationsmodell ist zu bauen und zu validieren, bevor der erste Modus läuft                    |
| **ADR-7** | Anmeldung über eigenes React-Frontend statt externem Formular-Dienst; der öffentliche `signup`-Endpunkt validiert serverseitig und gilt als Grenze | NfA-3 User-Freundlichkeit · Q3 Wartbarkeit · Scope-Kontrolle        | Formular, Validierung, Spam-/Rate-Limit-Schutz und Betrieb des öffentlichen Endpunkts sind selbst zu bauen — das kann ein Dienst sonst |

## Architektursichten

Kontextsicht, Containersicht und Paketstruktur stehen in
[`architecture.md`](architecture.md).

---

# Teil C — Erwartungshorizont und typische Fehlgriffe

## Woran eine gute Lösung erkennbar ist

| Kriterium                | Was zu sehen sein muss                                                                 |
|--------------------------|----------------------------------------------------------------------------------------|
| **Herleitung**           | Jeder Struktur-Entscheid nennt das Qualitätsziel, aus dem er folgt                     |
| **Vertrauensgrenze**     | Eingezeichnet — und zwar **beidseitig**: Provider hinaus, öffentliche Anmeldung herein |
| **Regeln vor Modell**    | Der Spielplan entsteht deterministisch; die KI arbeitet am bestehenden Plan            |
| **Ein Weg nach aussen**  | Der LLM-Zugriff ist gebündelt, nicht über die Module verstreut                         |
| **Freigabe vor Wirkung** | Kein Vorschlag verändert Daten, bevor der Mensch ihn annimmt (NfA-4)                   |
| **Preis benannt**        | Zu jedem Entscheid steht, was er kostet                                                |
| **Scope gehalten**       | Kein Swiss-System, kein Team-Login, kein Live-Ticker — die SPEC schliesst sie aus      |

## Typische Fehlgriffe — live ansprechen

1. **Die KI generiert den Spielplan.** Der verlockendste Fehler, weil er sich nach dem
   Kern des Projekts anfühlt. Q1 steht aber an erster Stelle, und ein generierter Plan
   ist nicht prüfbar, sondern nur plausibel. Rückfrage: *Wie beweisen Sie, dass kein Team
   zweimal im selben Slot steht?* Round-Robin ist eine Regel — die schreibt man auf.
2. **Der Zielkonflikt wird nicht gesehen.** FR-2 verlangt bei einem Teamausfall
   «möglichst wenig Änderungen», Q2 verlangt ausgelastete Felder. Beides zugleich geht
   nicht: minimal-invasiv heisst Löcher im Feldplan, volle Auslastung heisst den halben
   Nachmittag umwerfen. Wer das benennt, hat begriffen, dass Architektur Abwägung ist —
   der stärkste Punkt in der Auswertung. Bewusst danach fragen, wenn niemand darauf kommt.
3. **Vertrauensgrenze fehlt oder sitzt falsch.** Häufig wird sie um die Datenbank
   gezogen. Die Datenbank ist innen. Und wer nur den Provider aussen zeichnet, hat die
   Anmeldung übersehen: Anmeldungen sind Fremddaten. Seit das Formular das eigene
   React-Frontend ist, wird der Fehler verlockender — «das ist doch unser Code». Der Code
   läuft im fremden Browser; verlassen kann man sich nur auf die Prüfung im `signup`-Modul.
   Rückfrage: *Was passiert, wenn jemand den Endpunkt direkt mit `curl` aufruft?*
4. **KI-Vorschlag wird direkt gespeichert.** Dann bricht NfA-4. Der Vorschlag muss bis
   zur Annahme wirkungslos bleiben — Entwurf, nicht Änderung. Rückfrage: *Was steht in
   der Datenbank, solange niemand entschieden hat?*
5. **Keine Schema- und Regelprüfung der Modellantwort.** Zwei Stufen, nicht eine: das
   Format kann stimmen und der Plan trotzdem ein Team doppelt setzen. Der Fall «Modell
   liefert Unsinn» ist der häufigste blinde Fleck.
6. **Schichten statt Fachlichkeit.** `controller/`, `service/`, `model/` — dann liegt ein
   neuer Turniermodus über alle drei verteilt. Rückfrage: *Wo müssen Sie überall hin,
   wenn ein Punktesystem dazukommt?*
7. **Regeln hart im Code.** Punktesysteme und Tie-Breaker als `if`-Kaskade widersprechen
   Q3 direkt. Rückfrage: *Was ändern Sie, wenn ein Turnier 2 Punkte für den Sieg vergibt?*
8. **Alles synchron.** Der LLM-Aufruf hängt im Request; die Erfassung wartet. Q5 und
   NfA-1 brechen. Rückfrage: *Wie lange dauert Ihr Modellaufruf, und wie lange darf eine
   Ergebniserfassung dauern?*
9. **Microservices, weil «modern».** Die Qualitätsziele geben das nicht her: kleines
   Team, unter 50 gleichzeitige Nutzende, ein Deployment-Ziel, Wartbarkeit und
   Testbarkeit vor Skalierung. Rückfrage: *Welches Qualitätsziel wird durch die
   Verteilung besser?* Der modulare Monolith mit fachlichem Schnitt hält den Weg dorthin
   offen — das ist der stärkere Zug.
10. **Scope-Ausweitung.** Live-Ticker, Team-Login, Spielerverwaltung, Swiss-System. Alles
    steht ausdrücklich unter «Nicht enthalten».

## Was in der SPEC selbst noch klemmt

Zwei Stellen, die im Entwurf auffallen und in die nächste SPEC-Fassung gehören:

- **NfA-2** fordert Kontosperrung nach fünf Fehlversuchen, während «Login für Teams»
  ausgeschlossen ist. Für wen gilt die Anforderung — nur für das Organisationsteam?
- **§7 Evaluation** ist leer, obwohl NfA-4 und Q4 einen versionierten Testkorpus
  voraussetzen. Ohne ihn lässt sich «die KI ist geprüft» nicht belegen.
- **Der öffentliche Anmelde-Endpunkt hat keine NfA.** Mit ADR-7 betreiben wir ihn selbst,
  aber die SPEC sagt nichts über Missbrauch: Wie viele Anmeldungen pro IP und Minute?
  Was passiert bei Spam-Anmeldungen? NfA-2 spricht nur über Login-Versuche und deckt
  einen unauthentifizierten Endpunkt nicht ab.

Beides sind SPEC-Lücken, keine Entwurfsfehler — und genau deshalb fallen sie erst beim
Entwerfen auf. Das ist normal und ein gutes Zeichen.

## Für die Auflösung im Plenum

Nicht das ganze Skelett zeigen. Zwei Dinge genügen: die **Vertrauensgrenze** in der
Kontextsicht — mit dem Hinweis, dass sie zwei Durchstiche hat — und die Frage, **welches
Qualitätsziel** den jeweiligen Entscheid getragen hat. Wer bis dahin nicht über
«wenig Änderungen gegen volle Auslastung» gestolpert ist, wird jetzt darauf gestossen.
Der Rest ergibt sich aus der Diskussion — und die Fassung hier ist ausdrücklich nur eine
mögliche Antwort.
