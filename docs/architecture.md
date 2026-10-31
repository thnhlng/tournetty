# Architecture

Dieses Dokument enthält die Architektursichten für Tournetty: Kontextsicht, Containersicht und
Paketstruktur. Es beschreibt das **Wie** — das **Was** steht in [`SPEC.md`](SPEC.md) §1–§9 und
[`requirements.md`](requirements.md), die Architektur-Entscheide (ADR-1 bis ADR-7) in
[`SPEC.md`](SPEC.md#die-entscheide-auf-die-sich-alles-folgende-beruft).

## Kontextsicht

```text
   ┌──────────────────┐   ┌────────────────────────┐   ┌───────────────────┐
   │  Organisator:in  │   │ Vorort-Mitarbeitende   │   │ Teams · Publikum  │
   └────────┬─────────┘   └───────────┬────────────┘   └─────────┬─────────┘
            │ plant, bestätigt        │ trägt Ergebnisse ein     │ liest Spielplan,
            ▼                         ▼                          ▼ meldet Team an
      ╔═══════════════════════════════════════════════════════════════════╗
      ║                            Tournetty                              ║
      ║   Turnier · Phasen · Spielplan · Feldzuteilung · Ergebnisse ·     ║
      ║   Anmeldung · Vorschlag mit Freigabe · Dashboard · Datenbank      ║
      ╚═══════╤═══════════════════════════════════════════╤═══════════════╝
              │ Planungskontext, ohne Klarnamen           │ Anmeldungen: fremd,
              │ (Slots, Felder, Gruppen)                  │ roh, ungeprüft
   ╌╌╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌  Vertrauensgrenze  ╌╌╌╌╌╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌
              ▼   ▲ Vorschlag + Begründung                ▲ öffentliches
      ┌───────┴───┴──────┐                        ┌───────┴──────────┐
      │  LLM-Provider    │  extern                │ Anmeldeformular  │  eigenes
      └──────────────────┘                        │  (React, public) │  Frontend,
                                                  └──────────────────┘  fremde Eingaben
```

Die gestrichelte Linie ist der Kern der Aufgabe. Sie hat bei Tournetty **zwei**
Durchstiche, nicht einen: hinaus zum Provider und **herein** über die Anmeldung.
Dass das Formular jetzt unser eigenes React-Frontend ist, verschiebt die Grenze **nicht** —
das Frontend läuft im Browser fremder Leute, der Anmelde-Endpunkt ist öffentlich und
unauthentifiziert. Was dort hereinkommt, ist so wenig vertrauenswürdig wie das, was
hinausgeht; Validierung im Browser ist Komfort, nicht Schutz. Die Datenbank liegt innen.

## Containersicht

```text
  [ React-SPA · Cloudflare ]  Admin-UI  ╎  öffentliches Anmeldeformular ╌╌╌┐
            │                           ╎                                  ╎
            ▼                           ╎ ein Deployment, zwei Zonen       ▼
  ┌──────────────────────── REST-API · Heroku ──────────────────────────┐
  │                                                        [ signup ]   │  Validierung,
  │   tournament ──▶ phase ──▶ schedule ──▶ court                  │    │  Normalisierung,
  │    FR-1           FR-3      FR-2         FR-5                  │    │  Rate-Limit
  │      │              │         │            │                   │    │  FR-7
  │      │              │         └──(Job)─────┘                   │    │  ADR-4
  │      ▼              ▼              ▼                           ▼    │
  │  ┌──────────────────────────── Datenbank ──────────────────────────┐│
  │  └───▲──────────────────▲──────────────────▲────────────────▲──────┘│
  │      │                  │                  │                │       │
  │   scoring            dashboard          proposal ◀──── Protokoll    │
  │   FR-4                 FR-6                │  ▲          ADR-5      │
  │                                            ▼  │ nur nach Freigabe   │
  │                                    [ Regel- und Schemaprüfung ]     │
  │                                            │            ADR-2/ADR-3 │
  │                                       [ LLM-Port ] ──▶ [ Adapter ]  │
  └────────────────────────────────────────────────────────────╎────────┘
                                                               ╎
                                                        ╌╌╌╌╌╌▶ Provider
```

Vier Dinge liest man aus dem Bild ab:

- **`schedule` erzeugt Pläne ohne das Modell.** Round-Robin und K.-o.-Baum sind Regeln,
  keine Vorhersage — ADR-3. Q1 verlangt eine Invariante («ein Team, ein Slot»), die man
  prüfen kann, nicht eine, die meistens hält.
- **Der Job entkoppelt Antwort von Berechnung.** Die API bestätigt, der Plan entsteht
  danach — ADR-4, Q5.
- **Der einzige Weg nach aussen führt durch Port und Adapter, der einzige Weg zurück in
  die Datenbank durch Prüfung und Freigabe** — ADR-2 und ADR-5. Ein `proposal` ist bis
  zur Annahme ein Datensatz ohne Wirkung.
- **Das eigene Frontend ist kein Vertrauensbeweis.** Die SPA ist ein Deployment mit zwei
  Zonen: Admin-Sichten hinter Authentifizierung, Anmeldeformular öffentlich. Der
  öffentliche Pfad läuft durch `signup` und wird dort geprüft, als käme er von irgendwo —
  ADR-7.

## Paketstruktur

```text
tournetty-webapp/
├── src/main/java/ch/tournetty/
│   ├── tournament/         # FR-1 · Turnier, Team, Gruppe, Spielfeld — ID und Name
│   ├── phase/              # FR-3 · Phasenfolge, Reihenfolge, Übergang Gruppe → K.o.
│   ├── schedule/           # FR-2 · ADR-3 · deterministische Erzeugung
│   │   ├── rules/          # ADR-6 · Modi, Pausen, Platzregeln als Konfiguration
│   │   ├── generator/      # Round-Robin, K.-o.-Baum
│   │   └── invariants/     # Q1 · ein Team, ein Slot — hier und nirgends sonst
│   ├── court/              # FR-5 · Feldzuteilung, Auslastung, Verschieben — Q2
│   ├── scoring/            # FR-4 · Tabelle, Tie-Breaker, Weiterleitung im K.-o.
│   ├── proposal/           # ADR-5 · Vorschlag, Begründung, Annahme, Verwerfen
│   ├── signup/             # FR-7 · ADR-7 · öffentlicher Anmelde-Endpunkt
│   │                       #   Validierung, Duplikate, Annahme durch Organisator
│   ├── dashboard/          # FR-6 · Lesesichten Organisation und Publikum
│   └── platform/
│       ├── llm/
│       │   ├── PlanAdvisorPort.java    # ADR-2 · was die Fachlogik kennt
│       │   ├── ProviderAdapter.java    # der eine Weg nach aussen
│       │   ├── FakeAdvisor.java        # deterministisch, für Tests
│       │   └── ProposalSchema.java     # Ausgabe prüfen, bevor sie zählt
│       ├── audit/                      # NfA-4 · append-only
│       └── persistence/
├── src/test/java/ch/tournetty/
│   ├── unit/               # Regeln, Tabellen, Tie-Breaker — ohne Modell (Q4)
│   └── corpus/             # versionierte Fälle: Teamausfall, ungerade Teamzahl,
│                           # Gleichstand, Feld fällt weg (SPEC §7 Evaluation)
├── frontend/               # React · eigenständig baubar, Deployment Cloudflare
│   ├── admin/              # authentifiziert: Planung, Erfassung, Freigabe
│   └── signup/             # FR-7 · ADR-7 · öffentliches Anmeldeformular
└── pom.xml
```

Der Schnitt folgt der Fachlichkeit, nicht der Technik. Kommt ein Turniermodus dazu,
liegt die Arbeit in `schedule/rules/` — nicht verteilt über `controller/`, `service/`
und `model/`. Stack-neutral gedacht: entscheidend sind die **Grenzen**, nicht die
Sprache; Java und Spring Boot sind hier nur die Rahmenbedingung aus SPEC §7.
