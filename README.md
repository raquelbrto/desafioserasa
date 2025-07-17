# Desafio Serasa API

API REST desenvolvida com Java 17 e Spring Boot para cadastro de pessoas com score e endereço.

### Tecnologias Utilizadas

* Java 17
* Spring
* Autenticação JWT
* H2
* Redis
* OpenFeign
* Docker
* JUnit
* Mockito

## Variáveis de Ambiente

- Java: JDK 17.
- Git: Para clonar o repositório.
- Docker: Para rodar a aplicação em um container.
- Redis para cache

##  Configuração do Projeto

1. Clone o Repositório

        git clone git@github.com:raquelbrto/desafioserasa.git

2. Acesse o diretorio 

        cd desafioserasa

3. Crie a imagem docker

   ```bash
   docker build -t desafioserasa .
   ```

4. Execute

   ```bash
   docker compose up --build
   ```
   
   A aplicação vai estar disponivel no endereço http://localhost:8085
## Testes

  Para facilitar os testes ao iniciar a aplicação é criado um usuari padrão admin
        
```json
{
    "login": "admin",
    "password": "admin123"
}
```
        
# Documentação da API

## Autenticação

### Login

```http
POST /api/v1/auth/login
```

| Parâmetro   | Tipo   | Descrição                                 |
|:----------- |:------ |:------------------------------------------|
| `login`     | string | **Obrigatório**. Login do usuário         |
| `password`  | string | **Obrigatório**. Senha do usuário         |

Exemplo de JSON de requisição:
```json
{
  "login": "admin",
  "password": "123456"
}
```

Exemplo de JSON de resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6"
}
```

### Registrar novo usuário

```http
POST /api/v1/auth/register
```

| Parâmetro   | Tipo   | Descrição                                 |
|:----------- |:------ |:------------------------------------------|
| `login`     | string | **Obrigatório**. Login do usuário         |
| `password`  | string | **Obrigatório**. Senha do usuário         |
| `role`      | string | **Obrigatório**. Papel do usuário (`ADMIN` ou `USER`) |

Exemplo de JSON de requisição:
```json
{
  "login": "novo_usuario",
  "password": "senha123",
  "role": "USER"
}
```

---

## Pessoas

### Criar pessoa

```http
POST /api/v1/persons
```

| Parâmetro         | Tipo    | Descrição                                 |
|:----------------- |:------- |:------------------------------------------|
| `name`            | string  | **Obrigatório**. Nome da pessoa           |
| `email`           | string  | **Obrigatório**. E-mail                   |
| `phone`           | string  | **Obrigatório**. Telefone                 |
| `age`             | int     | **Obrigatório**. Idade                    |
| `zipCode`         | string  | **Obrigatório**. CEP                      |
| `score`           | int     | **Obrigatório**. Score de crédito         |

Exemplo de JSON de requisição:
```json
{
  "name": "Raquel",
  "email": "raquel.aves@gmail.com",
  "phone": "84994885502",
  "age": 27,
  "zipCode": "59215000",
  "score": 1000
}
```

Exemplo de JSON de resposta:
```json
{
  "id": 1,
  "name": "Raquel",
  "email": "raquel.aves@gmail.com",
  "phone": "84994885502",
  "age": 27,
  "city": "Nova Cruz",
  "state": "RN",
  "zipCode": "59215000",
  "street": "Rua Exemplo",
  "neighborhood": "Centro",
  "score": 1000,
  "scoreDescription": "Recomendável"
}
```

### Buscar pessoa por ID

```http
GET /api/v1/persons/{id}
```

Retorna os dados de uma pessoa pelo seu identificador.

Exemplo de JSON de resposta:
```json
{
    "id": 1,
    "name": "Raquel",
    "email": "raquel.aves@gmail.com",
    "phone": "84994885502",
    "age": 27,
    "city": "Nova Cruz",
    "state": "RN",
    "zipCode": "59215000",
    "street": "",
    "neighborhood": "",
    "score": 1000,
    "scoreDescription": "Recomendável"
}
```

### Buscar pessoas com filtro e paginação

```http
GET /api/v1/persons/search-filter?name=Raquel&age=27&zipCode=59215000&page=0&size=10
```

Retorna uma página de pessoas filtrando por nome, idade e/ou CEP.

Exemplo de JSON de resposta:
```json
{
    "content": [
        {
            "id": 1,
            "name": "Raquel",
            "email": "raquel.aves@gmail.com",
            "phone": "84994885502",
            "age": 27,
            "city": "Nova Cruz",
            "state": "RN",
            "zipCode": "59215000",
            "street": "",
            "neighborhood": "",
            "score": 1000,
            "scoreDescription": "Recomendável"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": [],
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "first": true,
    "size": 20,
    "number": 0,
    "sort": [],
    "numberOfElements": 1,
    "empty": false
}
```

### Atualizar pessoa

```http
PUT /api/v1/persons/{id}
```

Exemplo de JSON de requisição:
```json
{
  "name": "Raquel",
  "email": "raquel.aves@gmail.com",
  "phone": "84994885502",
  "age": 27,
  "zipCode": "59215000",
  "score": 1000
}
```

Atualiza dados de uma pessoa existente.

### Deletar pessoa

```http
DELETE /api/v1/persons/{id}
```

Inativa uma pessoa(Exclusão logica).

### Ativar pessoa

```http
PUT /api/v1/persons/{id}/activate
```

Ativa uma pessoa inativa.

---

## Swagger

Acesse a documentação em: [`swagger-ui/index.html`](http://localhost:8085/swagger-ui/index.html)

A API tambem conta com um workspace no postman: https://app.getpostman.com/join-team?invite_code=7eab05d8973a85981d0d617190fef65129b3b6b065e99e201fb22979d1085546&target_code=f24b11afcd3a0b48bec1ada2efc8d81b

