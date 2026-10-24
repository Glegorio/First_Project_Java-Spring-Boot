# CRUD Produtos — API REST com Spring Boot

API REST para gerenciamento de produtos, desenvolvida com **Java 21**, **Spring Boot**, **MySQL** e **Swagger UI**.

O projeto tem finalidade educacional e aplica conceitos de orientação a objetos, Spring MVC, organização em camadas, persistência com JPA e comunicação HTTP.

Permite cadastrar, listar, consultar, atualizar e excluir produtos, com persistência dos dados no MySQL e documentação interativa para executar as operações pelo navegador.

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Organização do projeto](#organização-do-projeto)
- [Modelo de dados](#modelo-de-dados)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Documentação e Swagger](#documentação-e-swagger)
- [Endpoints](#endpoints)
- [Exemplos de requisições](#exemplos-de-requisições)
- [Atualização completa e parcial](#atualização-completa-e-parcial)
- [Tratamento de erros](#tratamento-de-erros)
- [Verificação manual](#verificação-manual)
- [Configuração e credenciais](#configuração-e-credenciais)
- [Problemas comuns](#problemas-comuns)
- [Estado atual e próximas melhorias](#estado-atual-e-próximas-melhorias)
- [Conceitos praticados](#conceitos-praticados)

## Sobre o projeto

O CRUD Produtos foi desenvolvido como um projeto de estudo para compreender a construção de uma API REST com Spring Boot.

O domínio escolhido é um cadastro de produtos, com nome, descrição, preço e quantidade em estoque.

O desenvolvimento foi dividido em etapas:

1. Criação e execução da aplicação.
2. Configuração do Swagger.
3. Integração com o MySQL.
4. Criação da entidade e do Repository.
5. Implementação de cadastro e listagem.
6. Implementação de consulta individual, atualização e exclusão.
7. Implementação de atualização parcial com PATCH.

A aplicação utiliza respostas JSON para as operações de produtos. O Swagger UI permite consultar os contratos e executar requisições.

## Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem de programação |
| Spring Boot 4.1.2 | Configuração e inicialização da aplicação |
| Spring Web / Spring MVC | Implementação dos endpoints REST |
| Spring Data JPA | Abstração das operações de persistência |
| Hibernate | Mapeamento entre entidades Java e tabelas |
| MySQL | Banco de dados relacional |
| MySQL Connector/J | Driver de conexão com o MySQL |
| springdoc-openapi 3.1.1 | Geração da documentação OpenAPI e integração com Swagger UI |
| Maven | Gerenciamento de dependências e build |
| Git | Controle de versão |

O MySQL Workbench pode ser utilizado para criar o banco e consultar os registros.

## Funcionalidades

- [x] Verificar se a API está respondendo.
- [x] Cadastrar produtos.
- [x] Listar produtos em ordem crescente de ID.
- [x] Consultar produto pelo ID.
- [x] Atualizar todos os campos editáveis de um produto.
- [x] Atualizar somente os campos informados.
- [x] Excluir produtos.
- [x] Retornar HTTP 404 quando um produto não existe.
- [x] Persistir os dados no MySQL.
- [x] Documentar e executar endpoints pelo Swagger.
- [x] Separar os dados da API em DTOs.

## Arquitetura

A aplicação utiliza **Spring MVC para a camada HTTP**, com organização em camadas de Controller, Service e Repository.

Por ser uma API REST, os dados de produtos são devolvidos em JSON. Não há uma camada de páginas HTML implementada.

| Camada | Responsabilidade |
|---|---|
| Controller | Receber requisições HTTP e construir respostas |
| Service | Coordenar operações e delimitar transações |
| Repository | Executar operações de persistência |
| Model | Representar a entidade persistida |
| DTO | Definir os dados de entrada e saída da API |
| Exception | Representar situações excepcionais da aplicação |

### Fluxo de uma requisição

1. O cliente envia uma requisição HTTP.
2. O Controller recebe os parâmetros e o corpo da requisição.
3. O Controller chama o Service.
4. O Service utiliza o Repository.
5. O Repository acessa o MySQL por meio do JPA e do Hibernate.
6. O resultado é convertido em DTO de resposta.
7. O Controller devolve o status HTTP e, quando aplicável, o corpo JSON.

### Decisões de implementação

- Injeção de dependência por construtor.
- Uso de `record` para representar DTOs.
- Uso de `BigDecimal` para o preço.
- Identificadores gerados pelo banco.
- Uso de transações na camada Service.
- Retorno de DTOs nas operações de produtos.
- Separação entre atualização completa com PUT e parcial com PATCH.
- Verificação de existência antes de consultar, atualizar ou excluir individualmente.

## Organização do projeto

O pacote principal é:

`br.com.glegorio.crudprodutos`

| Localização | Arquivo | Responsabilidade |
|---|---|---|
| Pacote principal | `CrudprodutosApplication.java` | Inicialização da aplicação |
| `controller` | `StatusController.java` | Endpoint de status |
| `controller` | `ProdutoController.java` | Endpoints de produtos |
| `dto` | `ProdutoCadastro.java` | Entrada para cadastro e atualização completa |
| `dto` | `ProdutoAtualizacaoParcial.java` | Entrada para atualização parcial |
| `dto` | `ProdutoResposta.java` | Representação dos produtos nas respostas |
| `exception` | `ProdutoNaoEncontradoException.java` | Exceção para produto inexistente |
| `model` | `Produto.java` | Entidade JPA |
| `repository` | `ProdutoRepository.java` | Persistência de produtos |
| `service` | `ProdutoService.java` | Operações e transações de produtos |
| `src/main/resources` | `application.yaml` | Configurações locais |

Na raiz do projeto:

| Arquivo | Finalidade |
|---|---|
| `pom.xml` | Dependências e configuração do build |
| `mvnw` | Maven Wrapper para Linux e macOS |
| `mvnw.cmd` | Maven Wrapper para Windows |
| `.gitignore` | Regras de arquivos ignorados |
| `README.md` | Documentação do projeto |

## Modelo de dados

A entidade `Produto` é mapeada para a tabela `produtos`.

| Campo | Tipo Java | Tipo no MySQL | Características |
|---|---|---|---|
| `id` | `Long` | `BIGINT` | Chave primária e geração automática |
| `nome` | `String` | `VARCHAR(120)` | Não aceita NULL |
| `descricao` | `String` | `VARCHAR(500)` | Aceita NULL |
| `preco` | `BigDecimal` | `DECIMAL(12,2)` | Não aceita NULL |
| `quantidade` | `Integer` | `INT` | Não aceita NULL |

### Identificador

O ID utiliza:

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

No MySQL, a geração utiliza uma coluna com `AUTO_INCREMENT`.

O cliente não deve enviar ID no corpo das operações de cadastro ou atualização.

### Preço

O preço utiliza `BigDecimal` e uma coluna com precisão de 12 dígitos, sendo dois após a vírgula.

Exemplo de representação no JSON:

```json
{
  "preco": 89.90
}
```

Números decimais no JSON utilizam ponto.

### Restrições atuais

As configurações de coluna definem restrições de armazenamento, como tamanho e ausência de NULL.

A validação de entrada com Bean Validation ainda não foi implementada. Portanto, o estado atual não impede de forma explícita todos os casos de nome vazio, preço negativo ou quantidade negativa.

## Pré-requisitos

Para executar localmente:

- JDK 21.
- MySQL Server instalado e em execução.
- Git, para clonar o repositório.
- Acesso à internet na primeira execução para baixar as dependências.

Ferramentas opcionais:

- IntelliJ IDEA ou outra IDE com suporte a Java.
- MySQL Workbench.

Não é necessário instalar o Maven separadamente, pois o projeto possui Maven Wrapper.

Confira o JDK:

```bash
java -version
```

```bash
javac -version
```

Ambos devem indicar a versão 21.

## Como executar

### 1. Clonar o repositório

Substitua `URL_DO_REPOSITORIO` pelo endereço deste repositório:

```bash
git clone URL_DO_REPOSITORIO crudprodutos
```

Entre na pasta:

```bash
cd crudprodutos
```

### 2. Criar o banco e o usuário

Conecte-se ao MySQL com um usuário administrativo.

Substitua `SUBSTITUA_POR_UMA_SENHA_LOCAL` pela senha escolhida antes de executar:

```sql
CREATE DATABASE IF NOT EXISTS crudprodutos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'crudprodutos_app'@'localhost'
    IDENTIFIED BY 'SUBSTITUA_POR_UMA_SENHA_LOCAL';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON crudprodutos.*
    TO 'crudprodutos_app'@'localhost';
```

Se o usuário já existir, o comando `CREATE USER IF NOT EXISTS` não altera sua senha.

Para atualizar a senha de um usuário existente:

```sql
ALTER USER 'crudprodutos_app'@'localhost'
    IDENTIFIED BY 'SUBSTITUA_POR_UMA_SENHA_LOCAL';
```

### 3. Criar o arquivo de configuração local

O arquivo `application.yaml` é ignorado pelo Git e deve ser criado localmente em:

```text
src/main/resources/application.yaml
```

Conteúdo:

```yaml
spring:
  application:
    name: crudprodutos

  datasource:
    url: jdbc:mysql://localhost:3306/crudprodutos
    username: crudprodutos_app
    password: "SUBSTITUA_POR_UMA_SENHA_LOCAL"

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

Informe a mesma senha configurada no MySQL.

Se o servidor utilizar outro endereço ou porta, ajuste `spring.datasource.url`.

### 4. Executar a aplicação

#### Windows — PowerShell

```powershell
.\mvnw.cmd spring-boot:run
```

#### Linux ou macOS

```bash
./mvnw spring-boot:run
```

Se necessário, permita a execução do Wrapper:

```bash
chmod +x mvnw
```

#### IntelliJ IDEA

1. Abra a pasta que contém o `pom.xml`.
2. Aguarde o carregamento das dependências Maven.
3. Configure o SDK do projeto como Java 21.
4. Abra `CrudprodutosApplication.java`.
5. Execute o método `main`.

### 5. Conferir a inicialização

O terminal deve apresentar mensagens semelhantes a:

```text
HikariPool-1 - Start completed.
Started CrudprodutosApplication
```

A aplicação ficará disponível em:

[http://localhost:8080](http://localhost:8080)

Não há uma página inicial implementada na raiz. Utilize os endereços dos endpoints ou o Swagger.

### Criação das tabelas

Com `ddl-auto: update`, o Hibernate cria a tabela `produtos` caso ela ainda não exista, utilizando o mapeamento da entidade.

Essa configuração facilita o desenvolvimento local. O projeto ainda não utiliza migrações versionadas de banco.

## Documentação e Swagger

| Recurso | Endereço |
|---|---|
| Swagger UI | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| OpenAPI em JSON | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) |
| Status da API | [http://localhost:8080/api/status](http://localhost:8080/api/status) |

O endereço do Swagger pode redirecionar para `/swagger-ui/index.html`.

Para executar uma operação:

1. Abra o grupo correspondente.
2. Selecione o endpoint.
3. Clique em **Try it out**.
4. Informe os parâmetros e o corpo, quando necessários.
5. Clique em **Execute**.
6. Confira o status e o corpo da resposta.

Os exemplos apresentados automaticamente pelo Swagger não representam os dados atuais do banco.

## Endpoints

URL base local:

```text
http://localhost:8080
```

| Método | Endpoint | Descrição | Sucesso |
|---|---|---|---|
| GET | `/api/status` | Verifica se a API responde | `200 OK` |
| POST | `/api/produtos` | Cadastra um produto | `201 Created` |
| GET | `/api/produtos` | Lista os produtos | `200 OK` |
| GET | `/api/produtos/{id}` | Consulta pelo ID | `200 OK` |
| PUT | `/api/produtos/{id}` | Atualiza todos os campos editáveis | `200 OK` |
| PATCH | `/api/produtos/{id}` | Atualiza os campos informados | `200 OK` |
| DELETE | `/api/produtos/{id}` | Exclui um produto | `204 No Content` |

As operações individuais retornam `404 Not Found` quando o ID não corresponde a um produto existente.

## Exemplos de requisições

Os exemplos abaixo consideram a aplicação em execução localmente.

### Verificar o status

```http
GET /api/status
```

Resposta:

```text
API de produtos funcionando!
```

Status: `200 OK`.

Esse endpoint confirma que a API responde. Ele não executa uma consulta específica para verificar a disponibilidade do banco.

### Cadastrar um produto

```http
POST /api/produtos
Content-Type: application/json
```

Corpo:

```json
{
  "nome": "Teclado USB",
  "descricao": "Teclado com fio e layout ABNT2",
  "preco": 89.90,
  "quantidade": 10
}
```

Resposta de exemplo:

```json
{
  "id": 1,
  "nome": "Teclado USB",
  "descricao": "Teclado com fio e layout ABNT2",
  "preco": 89.90,
  "quantidade": 10
}
```

Status: `201 Created`.

A resposta inclui o cabeçalho `Location`, apontando para o endereço do produto criado.

Exemplo:

```text
Location: http://localhost:8080/api/produtos/1
```

O ID efetivamente gerado pode ser diferente do apresentado no exemplo.

### Listar produtos

```http
GET /api/produtos
```

Resposta de exemplo:

```json
[
  {
    "id": 1,
    "nome": "Teclado USB",
    "descricao": "Teclado com fio e layout ABNT2",
    "preco": 89.90,
    "quantidade": 10
  },
  {
    "id": 2,
    "nome": "Mouse USB",
    "descricao": "Mouse optico com fio",
    "preco": 45.50,
    "quantidade": 20
  }
]
```

Status: `200 OK`.

Os produtos são retornados em ordem crescente de ID.

Quando não existem produtos:

```json
[]
```

A listagem atual não possui paginação.

### Buscar pelo ID

```http
GET /api/produtos/1
```

Resposta de exemplo:

```json
{
  "id": 1,
  "nome": "Teclado USB",
  "descricao": "Teclado com fio e layout ABNT2",
  "preco": 89.90,
  "quantidade": 10
}
```

Status: `200 OK`.

Se o produto não existir: `404 Not Found`.

### Atualizar todos os campos editáveis

```http
PUT /api/produtos/1
Content-Type: application/json
```

Corpo:

```json
{
  "nome": "Teclado USB ABNT2",
  "descricao": "Teclado com fio e teclas silenciosas",
  "preco": 99.90,
  "quantidade": 15
}
```

Resposta de exemplo:

```json
{
  "id": 1,
  "nome": "Teclado USB ABNT2",
  "descricao": "Teclado com fio e teclas silenciosas",
  "preco": 99.90,
  "quantidade": 15
}
```

Status: `200 OK`.

Todos os campos editáveis são atribuídos a partir do corpo enviado. O ID permanece o mesmo.

Se o produto não existir: `404 Not Found`.

### Atualizar somente o preço

```http
PATCH /api/produtos/1
Content-Type: application/json
```

Corpo:

```json
{
  "preco": 129.90
}
```

Resposta de exemplo:

```json
{
  "id": 1,
  "nome": "Teclado USB ABNT2",
  "descricao": "Teclado com fio e teclas silenciosas",
  "preco": 129.90,
  "quantidade": 15
}
```

Status: `200 OK`.

Nome, descrição e quantidade são preservados.

### Atualizar preço e quantidade

```http
PATCH /api/produtos/1
Content-Type: application/json
```

Corpo:

```json
{
  "preco": 149.90,
  "quantidade": 8
}
```

Somente os dois campos informados são alterados.

### Limpar a descrição

Na implementação atual, envie uma string vazia:

```http
PATCH /api/produtos/1
Content-Type: application/json
```

```json
{
  "descricao": ""
}
```

### Excluir um produto

```http
DELETE /api/produtos/1
```

Status: `204 No Content`.

A resposta não possui corpo.

Uma nova consulta ao mesmo ID retorna `404 Not Found`.

## Atualização completa e parcial

### PUT

O PUT recebe os dados completos de edição.

Para modificar somente o preço utilizando PUT, também é necessário enviar os valores de nome, descrição e quantidade que devem permanecer.

O endpoint atual não preserva automaticamente campos omitidos no PUT.

### PATCH

O PATCH permite enviar apenas os campos que precisam mudar.

| Conteúdo recebido | Comportamento atual |
|---|---|
| Campo omitido | Mantém o valor existente |
| Campo com `null` | Mantém o valor existente |
| Campo com valor não nulo | Substitui o valor existente |
| `quantidade: 0` | Atualiza a quantidade para zero |
| `descricao: ""` | Atualiza a descrição para texto vazio |
| Objeto vazio `{}` | Mantém os dados e retorna o produto existente |

Essa implementação utiliza um DTO com campos opcionais. Ela não implementa os formatos JSON Patch ou JSON Merge Patch.

Não é possível definir um campo como NULL pelo PATCH atual, pois NULL representa “manter o valor existente”.

## Tratamento de erros

### Produto inexistente

A classe `ProdutoNaoEncontradoException` representa a tentativa de consultar, atualizar ou excluir um produto inexistente.

Ela utiliza:

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
```

Quando essa exceção é lançada durante uma requisição, o Spring retorna:

```text
404 Not Found
```

### Corpo das respostas de erro

O projeto ainda utiliza o tratamento padrão do Spring para o corpo dos erros.

Portanto, não há um contrato próprio e padronizado para mensagens de erro nesta etapa. A mensagem da exceção pode não aparecer no corpo, dependendo da configuração.

### Dados inválidos

A validação de entrada ainda será adicionada.

Alguns dados inválidos podem ser recusados durante a conversão do JSON ou pelas restrições do banco. O tratamento desses casos ainda não foi padronizado.

## Verificação manual

A verificação funcional é realizada pelo Swagger UI e por consultas no MySQL.

Não há uma suíte automatizada de testes documentada neste estágio do projeto.

### Roteiro sugerido

1. Inicie a aplicação.
2. Execute `GET /api/status` e confira o retorno `200`.
3. Cadastre um produto com POST e confira o retorno `201`.
4. Copie o ID retornado.
5. Liste os produtos e confirme o cadastro.
6. Consulte o produto pelo ID.
7. Atualize todos os campos com PUT.
8. Altere somente o preço com PATCH.
9. Confira se os demais campos foram preservados.
10. Reinicie a aplicação e confirme a persistência dos dados.
11. Exclua o produto e confira o retorno `204`.
12. Consulte o ID excluído e confira o retorno `404`.
13. Tente atualizar e excluir novamente esse ID e confira o retorno `404`.

### Consultar os registros no MySQL

```sql
USE crudprodutos;

SELECT
    id,
    nome,
    descricao,
    preco,
    quantidade
FROM produtos
ORDER BY id;
```

### Conferir a estrutura da tabela

```sql
DESCRIBE produtos;
```

## Configuração e credenciais

O arquivo local `application.yaml` contém a configuração de conexão e não deve ser enviado com credenciais ao repositório.

A regra utilizada no `.gitignore` é:

```gitignore
/src/main/resources/application.yaml
```

Após clonar o projeto, cada desenvolvedor deve criar seu próprio arquivo seguindo o exemplo deste README.

Se o arquivo já estiver sendo acompanhado pelo Git, remova-o apenas do versionamento:

```bash
git rm --cached src/main/resources/application.yaml
```

Esse comando mantém o arquivo no computador.

Para verificar a regra:

```bash
git check-ignore -v src/main/resources/application.yaml
```

Se uma credencial já foi publicada em um commit, removê-la do arquivo atual não a apaga do histórico. Nesse caso, altere a senha no MySQL e atualize a configuração local.

## Problemas comuns

| Problema | Verificação |
|---|---|
| `Access denied ... (using password: YES)` | Confira usuário e senha; uma senha foi enviada, mas a autenticação foi recusada |
| `Access denied ... (using password: NO)` | Confira se a senha está preenchida e se alguma configuração a sobrescreve com valor vazio |
| `Unknown database 'crudprodutos'` | Crie o banco antes de iniciar a aplicação |
| `Communications link failure` | Confira se o MySQL está em execução, além de host e porta |
| `Unable to determine Dialect without JDBC metadata` | Examine o erro anterior; uma conexão recusada pode impedir a identificação do banco |
| Porta `8080` ocupada | Encerre outra execução ou altere `server.port` |
| Swagger retorna `404` | Confira a dependência springdoc, recarregue o Maven e reinicie |
| Configuração perdida após clonar | Recrie o `application.yaml`, pois ele é ignorado pelo Git |
| PUT alterou campos que deveriam permanecer | Envie os valores atuais de todos os campos ou utilize PATCH |
| PATCH alterou mais campos do que o esperado | Apague o JSON de exemplo do Swagger e envie somente os campos desejados |

Se alterar `server.port`, ajuste também a porta utilizada nos endereços de acesso.

## Estado atual e próximas melhorias

### Implementado

- [x] Inicialização da aplicação com Java 21 e Spring Boot.
- [x] Integração com MySQL.
- [x] Entidade `Produto`.
- [x] Repository com Spring Data JPA.
- [x] Camada Service com transações.
- [x] Controllers REST.
- [x] DTOs com records.
- [x] Cadastro e listagem.
- [x] Consulta por ID.
- [x] Atualização completa.
- [x] Atualização parcial.
- [x] Exclusão.
- [x] Resposta 404 para produto inexistente.
- [x] Swagger UI e documentação OpenAPI.

### Próximas melhorias

- [ ] Validar campos com Bean Validation.
- [ ] Impedir nome vazio ou composto apenas por espaços.
- [ ] Impedir preço e quantidade negativos.
- [ ] Validar limites de tamanho e precisão antes da persistência.
- [ ] Padronizar respostas de erro.
- [ ] Tratar erros de conversão e JSON inválido.
- [ ] Documentar respostas de erro no OpenAPI.
- [ ] Adicionar paginação à listagem.
- [ ] Adicionar pesquisa por nome.
- [ ] Separar configurações por ambiente.
- [ ] Controlar alterações do banco com migrações.

O projeto está em desenvolvimento e tem finalidade educacional. Ainda não inclui autenticação, autorização ou preparação para publicação em produção.

## Conceitos praticados

- Classes e encapsulamento.
- Construtores, getters e setters.
- Interfaces e tipos genéricos.
- Records e DTOs.
- Injeção de dependência.
- Organização em camadas.
- Spring MVC e Controllers REST.
- Mapeamento objeto-relacional.
- Entidades JPA e geração de identificadores.
- Persistência com Spring Data JPA.
- Transações.
- Uso de `Optional`.
- Conversão de listas com Streams.
- Métodos HTTP e códigos de resposta.
- Diferença entre PUT e PATCH.
- Documentação interativa com Swagger.
- Configuração com YAML.
- Controle de versão com Git.