# 🏦 Service DICT - Diretório de Identificadores de Contas Transacionais

Microsserviço que simula o **DICT (Diretório de Identificadores de Contas Transacionais)** do ecossistema **Pix**.

O **DICT** é a camada central responsável por realizar o mapeamento entre uma **Chave Pix** (como um CPF, celular, e-mail ou chave-aleatória) e os dados da conta transacional completa no **PSP (Participante do Sistema de Pagamentos)**.  
Este serviço garante a **unicidade da chave** em todo o sistema.

---

## 🚀 Como Executar Localmente

Este serviço foi projetado para rodar em conjunto com o **service-psp** e o **postgres-dict**.

### ✅ Pré-requisitos

- Java Development Kit (**JDK**) 17+
- Apache Maven 3+
- Docker e Docker Compose

---

### 1. ⚙️ Configuração do Banco de Dados

O **DICT** utiliza seu próprio banco de dados **PostgreSQL**, isolado do *service-psp*.

Certifique-se de que a seção `postgres-dict` esteja adicionada e configurada no seu arquivo `docker-compose.yml` principal, e que o serviço de banco de dados esteja iniciado:

```bash
docker-compose up -d postgres-dict
```
---

### 2. 🏗️ Build e Execução

O build do projeto Maven deve ser realizado antes de empacotá-lo em um Docker:

```bash
1. Navegue até a raiz do projeto
cd [caminho_para_service-dict]

2. Compile e empacote o JAR
mvn clean install -DskipTests

3. Inicie o contêiner do serviço
docker-compose up -d service-dict
```
---

### O serviço estará acessível em:

 👉 http://localhost:8082

---

## 🌐 Endpoints da API

O DICT expõe três endpoints principais: registro, consulta e deleção de chaves.

| Método     | Caminho                   | Descrição                                |
| ---------- | ------------------------- | ---------------------------------------- |
| **POST**   | `/api/v1/keys`            | Registra uma nova Chave Pix.             |
| **GET**    | `/api/v1/keys/{keyValue}` | Busca os dados da conta associados a uma Chave Pix. |
| **DELETE** | `/api/v1/keys/{keyValue}` | Remove uma Chave Pix.                    |

---
### Exemplo de Registro de Chave Pix
Um PSP (no caso, o service-psp simulado) registra uma chave:

```bash
{
  "keyType": "EMAIL",
  "keyValue": "teste@exemplo.com",
  "accountId": "f2e9d242-8c11-4f10-9112-9c99e9c5f403",
  "userId": "e8a7c64a-3f0e-473d-82d0-7a3a9a7a9a7a",
  "pspId": "4c6c06b2-6a4a-4a6c-941c-34a9b58e72d2"
}
```

Observação: Para que o registro funcione, accountId, userId e pspId devem ser IDs válidos e existentes no banco de dados do service-psp.

---

## 3. 🚀 Acesso à Documentação da API (Swagger)

Para facilitar o desenvolvimento e os testes, este serviço utiliza o **SpringDoc OpenAPI** para gerar uma documentação de API interativa (Swagger UI).

Após iniciar o serviço (seja pela IDE ou Docker), você pode acessar a documentação nos seguintes endpoints:

* **Swagger UI (Interface Gráfica):**
  [http://localhost:8082/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

* **Definição OpenAPI (JSON):**
  [http://localhost:8082/v3/api-docs](http://localhost:8081/v3/api-docs)

---

## 🛠️ Tecnologias Utilizadas

* **Java 17 (Spring Boot)**

* **Spring Data JPA (Hibernate)**

* **PostgreSQL**

* **SpringDoc (Swagger)**

* **Lombok**

---
### 7. Autor
Criado por **Jusley H. Souza**
