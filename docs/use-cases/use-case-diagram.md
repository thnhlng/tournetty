# Use-Case-Diagramm

Dieses Dokument enthält das UML-Use-Case-Diagramm für Tournetty. Quelle sind ausschliesslich
die Dokumente unter `docs/`: [`requirements.md`](../requirements.md) (FR-001–FR-026),
[`vision.md`](../vision.md) (Scope, Target Users) und [`SPEC.md`](../SPEC.md) §4 (Akteure) und
§5 (funktionale Anforderungen).

* **UML-Quelle:** [`use-case-diagram.puml`](use-case-diagram.puml) — normative Notation,
  rendern mit `plantuml docs/use-cases/use-case-diagram.puml`
* **Mermaid-Fassung** unten — rendert direkt in Markdown, bildet UML-Notation nur annähernd ab
  (Mermaid kennt keinen eigenen Use-Case-Diagrammtyp)

## Akteure

| Akteur                | Art         | Rolle                                                              | Quelle              |
|-----------------------|-------------|--------------------------------------------------------------------|---------------------|
| Organisator:in        | primär      | Plant das Turnier, nimmt Anmeldungen an, gibt KI-Vorschläge frei.  | SPEC §4, vision.md  |
| Vorort-Mitarbeitende  | primär      | Erfasst und korrigiert Ergebnisse am Turniertag.                   | SPEC §4, vision.md  |
| Teamverantwortliche:r | primär      | Meldet das Team an und verfolgt den Spielplan.                     | vision.md           |
| Publikum              | primär      | Liest Spielplan und Tabellen.                                      | vision.md           |
| LLM-Provider          | sekundär    | Externes System, liefert Vorschläge. Genau ein Ausgang (ADR-2).    | SPEC §4, ADR-2      |

Teams erhalten keinen Login (C-013); der Teamverantwortliche agiert ausschliesslich über den
öffentlichen, unauthentifizierten Anmelde-Endpunkt und das öffentliche Dashboard.

## Diagramm

```mermaid
flowchart LR
  ORG(["Organisator:in"])
  STAFF(["Vorort-<br/>Mitarbeitende"])
  TEAM(["Team-<br/>verantwortliche:r"])
  PUBLIC(["Publikum"])
  LLM(["LLM-Provider<br/>«external system»"])

  subgraph SYS["Tournetty"]
    direction TB

    subgraph STAMM["Stammdaten und Konfiguration"]
      UC001("UC-001<br/>Turnier verwalten")
      UC002("UC-002<br/>Teams verwalten")
      UC003("UC-003<br/>Gruppen verwalten")
      UC004("UC-004<br/>Spielfelder verwalten")
      UC005("UC-005<br/>Turnierregeln konfigurieren")
    end

    subgraph ANM["Anmeldung"]
      UC009("UC-009<br/>Team anmelden")
      UC010("UC-010<br/>Anmeldung annehmen<br/>oder ablehnen")
      UC020("UC-020<br/>Anmeldedaten serverseitig<br/>validieren")
    end

    subgraph PLAN["Planung"]
      UC006("UC-006<br/>Phasen planen")
      UC007("UC-007<br/>Spielplan generieren")
      UC008("UC-008<br/>Spiele verschieben")
      UC021("UC-021<br/>Spiele auf Felder verteilen")
      UC022("UC-022<br/>Invariante 'ein Team,<br/>ein Slot' prüfen")
    end

    subgraph DURCH["Durchführung und Zählen"]
      UC011("UC-011<br/>Ergebnis erfassen")
      UC023("UC-023<br/>Tabelle berechnen")
      UC024("UC-024<br/>Sieger weiterleiten")
    end

    subgraph KI["KI mit Freigabe-Gate"]
      UC012("UC-012<br/>KI-Vorschlag<br/>Gruppenaufteilung")
      UC013("UC-013<br/>KI-Vorschlag<br/>Umplanung")
      UC014("UC-014<br/>Vorschlag freigeben<br/>oder verwerfen")
      UC015("UC-015<br/>Protokoll einsehen")
      UC025("UC-025<br/>Antwort gegen Schema<br/>und Regeln prüfen")
      UC026("UC-026<br/>Vorschlag mit<br/>Begründung anzeigen")
      UC027("UC-027<br/>Entscheid append-only<br/>protokollieren")
      UC028("UC-028<br/>Regelbasierten<br/>Rückfallvorschlag erzeugen")
    end

    subgraph SICHT["Sichten und Zugang"]
      UC016("UC-016<br/>Öffentliches<br/>Dashboard ansehen")
      UC017("UC-017<br/>Administrations-<br/>Dashboard öffnen")
      UC018("UC-018<br/>Spiele filtern")
      UC019("UC-019<br/>Am System anmelden")
    end
  end

  ORG --- UC001
  ORG --- UC002
  ORG --- UC003
  ORG --- UC004
  ORG --- UC005
  ORG --- UC006
  ORG --- UC007
  ORG --- UC008
  ORG --- UC010
  ORG --- UC011
  ORG --- UC012
  ORG --- UC013
  ORG --- UC014
  ORG --- UC015
  ORG --- UC017
  ORG --- UC019

  STAFF --- UC011
  STAFF --- UC018
  STAFF --- UC019

  TEAM --- UC009
  TEAM --- UC016
  PUBLIC --- UC016

  UC012 --- LLM
  UC013 --- LLM

  UC009 -.->|include| UC020
  UC010 -.->|include| UC002
  UC007 -.->|include| UC021
  UC007 -.->|include| UC022
  UC008 -.->|include| UC022
  UC011 -.->|include| UC023
  UC011 -.->|include| UC024
  UC012 -.->|include| UC025
  UC013 -.->|include| UC025
  UC012 -.->|include| UC026
  UC013 -.->|include| UC026
  UC014 -.->|include| UC027

  UC013 -.->|"extend: Team fällt aus"| UC002
  UC028 -.->|"extend: LLM nicht erreichbar"| UC013
  UC014 -.->|"extend: nur nach Freigabe"| UC007
```

## Was im Diagramm bewusst so steht

* **UC-007 hängt nicht am LLM-Provider.** Die Spielplangenerierung ist deterministisch und
  regelbasiert (ADR-3, C-015). Nur UC-012 und UC-013 haben eine Verbindung nach aussen.
* **Genau zwei Use Cases erreichen den Provider** — über denselben Port (ADR-2, NFR-017).
* **UC-014 ist das Gate.** Kein Vorschlag wirkt vor der Annahme durch einen Menschen
  (ADR-5, NFR-013, C-016); deshalb `extend` von UC-014 auf UC-007, nicht `include`.
* **UC-020 ist eigenständig modelliert**, weil die Validierung die Vertrauensgrenze bildet:
  UC-009 ist der einzige unauthentifizierte Schreibfall (NFR-015, NFR-016).
* **UC-019 ist kein `include` jedes Admin-Falls.** Der Zugang wird einmal modelliert
  (FR-026, NFR-010); alle Fälle von Organisator:in und Vorort-Mitarbeitenden setzen ihn voraus.
* **UC-028 als `extend`** deckt den Rückfallpfad bei LLM-Ausfall ab (ADR-003); die Freigabe
  über UC-014 gilt unverändert.

## Traceability zu den Anforderungen

| Use Case | Titel                                         | Anforderungen           |
|----------|-----------------------------------------------|-------------------------|
| UC-001   | Turnier verwalten                             | FR-001, FR-005          |
| UC-002   | Teams verwalten                               | FR-002, FR-005          |
| UC-003   | Gruppen verwalten                             | FR-003, FR-005          |
| UC-004   | Spielfelder verwalten                         | FR-004, FR-005          |
| UC-005   | Turnierregeln konfigurieren                   | FR-024, NFR-004         |
| UC-006   | Phasen planen                                 | FR-009, FR-010          |
| UC-007   | Spielplan generieren                          | FR-011, NFR-005, NFR-008 |
| UC-008   | Spiele verschieben                            | FR-013                  |
| UC-009   | Team anmelden                                 | FR-006, NFR-007         |
| UC-010   | Anmeldung annehmen oder ablehnen              | FR-008                  |
| UC-011   | Ergebnis erfassen                             | FR-014, NFR-007         |
| UC-012   | KI-Vorschlag Gruppenaufteilung anfordern      | FR-019                  |
| UC-013   | KI-Vorschlag Umplanung anfordern              | FR-020, NFR-008         |
| UC-014   | Vorschlag freigeben oder verwerfen            | FR-022, NFR-013         |
| UC-015   | Protokoll einsehen                            | FR-023, NFR-014         |
| UC-016   | Öffentliches Dashboard ansehen                | FR-017, NFR-011         |
| UC-017   | Administrations-Dashboard öffnen              | FR-018                  |
| UC-018   | Spiele filtern                                | FR-025                  |
| UC-019   | Am System anmelden                            | FR-026, NFR-010         |
| UC-020   | Anmeldedaten serverseitig validieren          | FR-007, NFR-015, NFR-016 |
| UC-021   | Spiele auf Felder verteilen                   | FR-012, NFR-003         |
| UC-022   | Invariante «ein Team, ein Slot» prüfen        | NFR-001                 |
| UC-023   | Tabelle berechnen                             | FR-015, NFR-002         |
| UC-024   | Sieger in nächste K.-o.-Runde weiterleiten    | FR-016, NFR-002         |
| UC-025   | Modellantwort gegen Schema und Regeln prüfen  | NFR-018, NFR-006        |
| UC-026   | Vorschlag mit Begründung anzeigen             | FR-021, NFR-012         |
| UC-027   | Entscheid append-only protokollieren          | FR-023, NFR-014         |
| UC-028   | Regelbasierten Rückfallvorschlag erzeugen     | FR-020 (ADR-003)        |

Jede funktionale Anforderung FR-001 bis FR-026 ist genau einem Use Case zugeordnet.
