src/main/java/com/seuprojeto
|
├── application/            -> Casos de uso (serviços de aplicação)
│   ├── service/            -> Orquestra lógica de negócio (chama domínio + infra)
│   └── dto/                -> Objetos de entrada/saída da camada de aplicação
|
├── domain/                 -> Regras de negócio (core)
│   ├── model/              -> Entidades e Value Objects
│   ├── repository/         -> Interfaces de repositórios (contratos)
│   └── service/            -> Regras de negócio puras (sem dependência de infra)
|
├── infrastructure/         -> Implementações técnicas
│   ├── repository/         -> Implementações de banco (ex: JPA, Redis)
│   ├── http/               -> Clients HTTP externos
│   ├── config/             -> Configurações (Redis, DB, etc.)
│   └── messaging/          -> Se usar fila/eventos (Kafka, SQS, etc.)
|
├── interfaces/             -> Adaptadores de entrada/saída
│   ├── controller/         -> Controllers REST (Spring Web)
│   └── request/response/   -> DTOs específicos de API
|
└── shared/                 -> Utilitários comuns
├── exception/          -> Exceções customizadas
└── mapper/             -> MapStruct ou conversores DTO <-> Domain
