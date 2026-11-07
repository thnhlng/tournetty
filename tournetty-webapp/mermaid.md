``` mermaid

flowchart LR
User[User]
UI[UI]
API[Backend / API]
DB[(Database)]

    User --> UI
    UI --> API
    API --> DB

``` 