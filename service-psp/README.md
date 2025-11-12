# Microsserviço: service-psp (Participante do Sistema de Pagamentos)

Este microsserviço é um dos pilares da simulação do ecossistema Pix. Ele é responsável por simular as operações de um **Participante do Sistema de Pagamentos** (como um Banco, Fintech ou Instituição de Pagamento).

Sua principal responsabilidade é ser o **"dono da verdade"** sobre contas, usuários e, o mais importante, **saldos**.

## 1. Principais Responsabilidades

* **Gerenciamento de PSPs:** Expõe APIs para cadastrar e consultar os próprios "Bancos" (PSPs) no sistema.
* **Gerenciamento de Usuários:** Expõe APIs para cadastrar e consultar os "Clientes" (Usuários) vinculados a um PSP.
* **Gerenciamento de Contas:** Expõe APIs para cadastrar e consultar "Contas Bancárias", incluindo consulta de saldo e depósitos simulados.
* **Processamento de Transações (Eventos):** É a parte mais crítica. Este serviço ouve eventos do `service-spi` (via RabbitMQ) para executar as operações financeiras:
    * **Debitar** o valor da conta do pagador.
    * **Creditar** o valor na conta do recebedor.
    * Garante a **atomicidade** e **consistência** do saldo (ex: não permite saldo negativo em uma transação Pix).

## 2. Stack de Tecnologias

* **Java 17**
* **Spring Boot 3+**
* **Spring Web** (APIs REST)
* **Spring Data JPA** (Persistência com PostgreSQL)
* **Spring AMQP** (Comunicação com RabbitMQ)
* **SpringDoc OpenAPI (Swagger)** (Documentação de API)
* **PostgreSQL** (Banco de Dados)
* **Docker & Docker Compose** (Ambiente de execução)
* **Lombok** (Redução de boilerplate)

## 3. 🚀 Acesso à Documentação da API (Swagger)

Para facilitar o desenvolvimento e os testes, este serviço utiliza o **SpringDoc OpenAPI** para gerar uma documentação de API interativa (Swagger UI).

Após iniciar o serviço (seja pela IDE ou Docker), você pode acessar a documentação nos seguintes endpoints:

* **Swagger UI (Interface Gráfica):**
  [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

* **Definição OpenAPI (JSON):**
  [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

### Principais APIs Expostas:

* `POST /api/v1/psps` - Cria um novo PSP (Banco).
* `GET /api/v1/psps/{id}` - Busca um PSP.
* `POST /api/v1/users` - Cria um novo Usuário (Cliente).
* `GET /api/v1/users/{id}` - Busca um Usuário.
* `POST /api/v1/accounts` - Cria uma nova Conta Bancária.
* `GET /api/v1/accounts/{id}/balance` - Consulta o saldo de uma conta.
* `POST /api/v1/accounts/{id}/deposit` - (Simulação) Adiciona fundos a uma conta para testes.

## 4. 📨 Arquitetura de Eventos (RabbitMQ)

Este serviço é fundamental para a SAGA de transação do Pix. Ele se comunica de forma assíncrona com o `service-spi` através do **RabbitMQ**.

* **Exchange (Tópico) Principal:** `pix-events-exchange`

### Eventos Consumidos (Ouvinte)

O `service-psp` escuta as seguintes filas:

1.  **Fila: `queue-requested-debit`**
    * **Routing Key:** `rk-requested-debit`
    * **O que faz:** Ao receber uma mensagem (do `service-spi`), tenta debitar o valor da conta do pagador.

2.  **Fila: `queue-requested-credit`**
    * **Routing Key:** `rk-requested-credit`
    * **O que faz:** Ao receber uma mensagem (do `service-spi`), tenta creditar o valor na conta do recebedor.

### Eventos Publicados (Publicador)

Após processar um evento, o `service-psp` publica o resultado de volta no `pix-events-exchange` usando as seguintes Routing Keys:

* `rk-debit-made`: Avisa que o débito foi concluído com sucesso.
* `rk-debit-failed`: Avisa que o débito falhou (ex: saldo insuficiente, conta não encontrada).
* `rk-credit-made`: Avisa que o crédito foi concluído com sucesso.
* `rk-credit-failed`: Avisa que o crédito falhou (ex: conta inativa, conta não encontrada).

## 5. 🐳 Executando Localmente (Docker)

A forma mais simples de executar este serviço com suas dependências é usando o `docker-compose.yml` (minimalista) localizado na raiz do monorepo.

### Pré-requisitos

1.  **Docker e Docker Compose** instalados.
2.  Um arquivo `.env` na raiz do monorepo com as credenciais do banco e do RabbitMQ.

### Executando (Ambiente Completo)

1.  Navegue até a **raiz do monorepo**.
2.  Execute o comando para subir o `service-psp` e suas dependências (`postgres-psp` e `rabbitmq`):

    ```bash
    docker-compose up --build -d service-psp postgres-psp rabbitmq
    ```

3.  O serviço estará disponível em `http://localhost:8081`.

### Executando (IDE + Infra Docker)

Se preferir rodar a aplicação Spring Boot pela sua IDE (para debug), você pode subir apenas a infraestrutura:

1.  Na raiz do monorepo, execute:
    ```bash
    docker-compose up -d postgres-psp rabbitmq
    ```
2.  Inicie a aplicação `ServicePspApplication.java` diretamente da sua IDE. O Spring Boot se conectará ao PostgreSQL e ao RabbitMQ em execução nos contêineres Docker.

## 6. ⚙️ Variáveis de Ambiente Essenciais

Este serviço depende das seguintes variáveis de ambiente para funcionar. Elas são injetadas pelo `docker-compose.yml` a partir do arquivo `.env`.

| Variável | Descrição | Exemplo no `.env`                                   |
| :--- | :--- |:----------------------------------------------------|
| `SPRING_DATASOURCE_URL` | URL de conexão JDBC com o banco | `jdbc:postgresql://postgres-psp:5432/nome_do_banco` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados | `DB_USER=seu_usuario`                               |
| `SPRING_DATASOURCE_PASSWORD`| Senha do banco de dados | `DB_PASSWORD=sua_senha`                             |
| `SPRING_RABBITMQ_HOST` | Hostname do serviço RabbitMQ | `rabbitmq` (nome do serviço no Docker)              |
| `SPRING_RABBITMQ_USERNAME`| Usuário do RabbitMQ | `RABBITMQ_USER=seu_usuario`                               |
| `SPRING_RABBITMQ_PASSWORD`| Senha do RabbitMQ | `RABBITMQ_PASSWORD=sua_senha`                           |
| `PSP_DB_NAME` | Nome do banco (usado pelo Compose) | `PSP_DB_NAME=db_psp`                                |
| `DB_USER` | Usuário (usado pelo Compose) | `DB_USER=seu_usuario`                                |
| `DB_PASSWORD` | Senha (usado pelo Compose) | `DB_PASSWORD=sua_senha`                         |

### 7. Autor
Criado por **Jusley H. Souza**