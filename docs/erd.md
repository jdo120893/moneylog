        ┌─────────────────┐
        │      USER       │
        │─────────────────│
        │ PK id           │
        │    email (UQ)   │
        │    password     │
        │    nickname     │
        │    created_at   │
        └───────┬─────────┘
       1        │        1                 1
   ┌────────────┼──────────────┐           └───────────┐
   │ N          │ N            │ N                      │ N (도전)
┌──▼───────────────┐   ┌───────▼────────────────┐  ┌───▼────────────────┐
│    CATEGORY      │   │     TRANSACTION        │  │      BUDGET        │
│──────────────────│   │────────────────────────│  │────────────────────│
│ PK id            │1  │ PK id                  │  │ PK id              │
│ FK user_id       │──<│ FK user_id             │  │ FK user_id         │
│    name          │ N │ FK category_id ────────┼─<│ FK category_id(NUL)│
│    type          │   │    type                │  │    year_month      │
│    created_at    │   │    amount              │  │    limit_amount    │
└──────────────────┘   │    description         │  └────────────────────┘
                       │    transaction_date    │
                       │    created_at          │
                       │    updated_at          │
                       └────────────────────────┘