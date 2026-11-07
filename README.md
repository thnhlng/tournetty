# Tournetty

Monorepo für Tournetty.

| Verzeichnis         | Inhalt                                           |
| ------------------- | ------------------------------------------------ |
| `tournetty-webapp/` | Spring-Boot-Backend (Maven)                      |
| `tournetty-ui/`     | Next.js-Frontend (ADR-7), Static Export          |
| `docs/`             | Spec, Architektur, Anforderungen, API, Use Cases |

## Backend starten

```bash
cd tournetty-webapp
./mvnw spring-boot:run
./mvnw test
```

## Frontend starten

```bash
cd tournetty-ui
npm install
npm run dev
```
