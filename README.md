# Tournetty

Monorepo für Tournetty.

| Verzeichnis         | Inhalt                                              |
| ------------------- | --------------------------------------------------- |
| `tournetty-webapp/` | Spring-Boot-Backend (Maven), Doku und Konzeption    |
| `frontend/`         | React-Frontend (ADR-002) — noch nicht aufgesetzt    |

Der verbindliche Arbeitsrahmen liegt in `tournetty-webapp/PROJECT_CONTEXT.md`.

## Backend starten

```bash
cd tournetty-webapp
./mvnw spring-boot:run
./mvnw test
```
