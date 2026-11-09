# Tournetty · Backend-Kontext

Spring-Boot-Backend für die Turnierplanung. Dieses Dokument ist die Eingabe für den
KI-Assistenten: nur das, was in keinem Trainingsdatensatz steht — unser Stack, unser
Vertrag, unsere Regeln. Das **Was** steht in [`SPEC.md`](../../../docs/SPEC.md) §1–§9,
das **Wie** in [`architecture.md`](../../../docs/architecture.md) und
[`package-structure.md`](../../../docs/package-structure.md) — nur lesen, wenn der Auftrag
darauf zeigt.


## Stack

- Java 25 · Spring Boot 4.1.1 · Maven Wrapper (`./mvnw`) · Modul `tournetty-webapp`,
  Group `ch.tournetty`.
- Starter: `webmvc`, `validation`, `webmvc-test`. **Keine Persistenz-Abhängigkeit im
  `pom.xml`** — es gibt noch kein JPA, kein Flyway, keinen Treiber.
- Deployment: Heroku. Frontend (React) ist eigenständig, Cloudflare.

## API-first — die Reihenfolge ist verbindlich

1. `docs/api/openapi.yaml` ändern (OpenAPI 3.1, wachsende Datei, nicht eine je Use Case).
2. `./mvnw generate-sources` — der `openapi-generator` erzeugt **nur Interfaces** nach
   `ch.tournetty.tournetty_webapp.api` und Modelle nach `…api.model`.
3. Controller implementiert das generierte Interface (`implements TournamentsApi`).

Generierten Code unter `target/` nie bearbeiten. Kein Endpunkt ohne Eintrag im Spec.

**Konventionen:** `/api/<ressource>` im Plural, kebab-case · Verben ausschliesslich über
HTTP-Methoden · Feldnamen englisch, Beschreibungen und Kommentare deutsch · Fehler nach
RFC 9457 (`application/problem+json`) mit stabilem `code`, bei Feldfehlern `errors[]`,
nie ein Stacktrace · Schreibzugriffe senden `version` mit; weicht sie ab, `409` mit
`VERSION_CONFLICT` und keine Änderung.

## Paketschnitt — fachlich, nicht nach Schichten (ADR-1)

```text
ch.tournetty/
├── tournament/  phase/  schedule/  court/  scoring/  proposal/  signup/  dashboard/
└── platform/    llm/ (Port · Adapter · Fake · Schema) · audit/ · persistence/
```

Ein neuer Turniermodus liegt in `schedule/rules/` — nicht verteilt über `controller/`,
`service/` und `model/`. Das heutige `controller/TournamentController` ist Altstand aus
dem Durchstich; neuer Code wächst im Fachschnitt.


## Tests

- `./mvnw test`. `unit/` für Regeln, Tabellen, Tie-Breaker — ohne Modell.
  `corpus/` für versionierte Fälle: Teamausfall, ungerade Teamzahl, Gleichstand,
  Feld fällt weg.
- Ein Test je Verhalten. KI-Anteile müssen ohne echten Modellaufruf prüfbar sein.

## Verbote

- Keine neue Abhängigkeit, kein Starter, keine Bibliothek ohne Rückfrage.
- Keine unnötigen Kommentare. Kein Kommentar, der den Code nacherzählt, keine Javadoc-Hülle
  ohne Inhalt, keine `// TODO`-Reste.

## Arbeitsweise

- Erst den Plan zeigen, dann Code.
- Ein Baustein je Diff. Diffs kurz und lesbar halten.
- Nichts umbenennen oder umstrukturieren, was nicht Teil des Auftrags ist.
