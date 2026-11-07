# Paketstruktur

Dieses Dokument beschreibt den Paketschnitt von Tournetty. Die übrigen Architektursichten —
Kontextsicht und Containersicht — stehen in [`architecture.md`](architecture.md), das **Was**
in [`SPEC.md`](SPEC.md) §1–§9 und [`requirements.md`](requirements.md), die Architektur-Entscheide
(ADR-1 bis ADR-7) in [`SPEC.md`](SPEC.md#die-entscheide-auf-die-sich-alles-folgende-beruft).

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
