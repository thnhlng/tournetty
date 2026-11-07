# tournetty-ui

React-Frontend von Tournetty (ADR-7). Next.js 16 · App Router · TypeScript · CSS.

Stand: Grundgerüst mit Hello-World-Page. Admin-Bereich und öffentliches
Anmeldeformular (FR-7) sind noch nicht aufgesetzt.

## Entwickeln

```bash
npm install
npm run dev     # http://localhost:3000
npm run build   # Static Export nach out/
npm run lint
```

## Static Export

`next.config.ts` setzt `output: "export"` — `npm run build` schreibt ein reines
HTML/CSS/JS-Bundle nach `out/`, das Cloudflare ausliefert. Es gibt kein
Server-Runtime: kein SSR, keine Route Handlers, keine Middleware, keine Server
Actions. Alle Daten holt der Browser per `fetch` von der REST-API
(`../tournetty-webapp`, Heroku).

Weil das Frontend im Browser fremder Leute läuft, ist Validierung hier Komfort,
nicht Schutz — die Grenze liegt im `signup`-Modul des Backends
([`docs/architecture.md`](../docs/architecture.md)).
