Separação do código:

Auth
Book (Author + Book)?
Lending
Reader
Top5Books


Foi implementado um sistema de "retry", caso não seja possivel estabelecer ligação com a segunda instancia à primeira tentativa.

As portas foram definidas da seguinte maneira:

| Serviço | Tipo       | Instância | Gama |
|--------|------------|-----------|------|
| Auth   |            |           | 2xxx |
| Book   |            |           | 3xxx | 
| Lending |            |           | 4xxx | 
| Reader |            |           | 5xxx |
| Top    |            |           | 6xxx |
|        | Command    |           | x0xx | 
|        | Query      |           | x1xx |
|        | DB Command |           | x2xx |
|        | BD Query   |           | x3xx |
|        |            | 1         | xx01 |
|        |            | 2         | xx02 |
|        |            | 10        | xx10 |
|        |            | 99        | xx99 |



Seguindo isso, as portas em uso por predefinição são:

| Serviço | Tipo       | Instância | Porta |
|---------|------------|-----------|-------|
| Auth    | -          | 1         | 2001  |
| Auth    | -          | 2         | 2002  |
| Auth    | DB         | 1         | 2201  |
| Auth    | DB         | 2         | 2202  |
| Book    | Command    | 1         | 3001  |  
| Book    | Command    | 2         | 3002  |  
| Book    | Query      | 1         | 3101  |
| Book    | Query      | 2         | 3102  |
| Book    | DB Command | 1         | 3201  |  
| Book    | DB Command | 2         | 3202  |  
| Book    | DB Query   | 1         | 3301  |
| Book    | DB Query   | 2         | 3302  |
| Lending | Command    | 1         | 4001  | 
| Lending | Command    | 2         | 4002  | 
| Lending | Query      | 1         | 4101  |
| Lending | Query      | 2         | 4102  |
| Lending | DB Command | 1         | 4201  | 
| Lending | DB Command | 2         | 4202  | 
| Lending | DB Query   | 1         | 4301  |
| Lending | DB Query   | 2         | 4302  |
| Reader  | Command    | 1         | 5001  | 
| Reader  | Command    | 2         | 5002  | 
| Reader  | Query      | 1         | 5101  |
| Reader  | Query      | 2         | 5102  |
| Reader  | DB Command | 1         | 5201  | 
| Reader  | DB Command | 2         | 5202  | 
| Reader  | DB Query   | 1         | 5301  |
| Reader  | DB Query   | 2         | 5302  |
| Top    | Query      | 1         | 6101  |
| Top    | Query      | 2         | 6102  | 
| Top    | DB Query   | 1         | 6301  | 
| Top    | DB Query   | 2         | 6302  | 


Cada microserviço subscreve as mensagens que necessita:

| Microserviço  | Queues Necessárias | Routing Keys Subscritas                  |
|---------------|--------------------|------------------------------------------|
| BookCom       | books.queue        | books.#                                  |
| BookQuery     | books.queue        | books.#                                  |
| LendingCom    | lendings.queue     | lendings.#, books.update, readers.update |
| LendingQuery	 | lendings.queue     | lendings.#, books.update, readers.update |
| ReaderCom     | readers.queue      | readers.#                                |
| ReaderQuery   | readers.queue      | readers.#                                |
| Top           | top.queue          | books.update, lendings.update            |

Microserviços enviam mensagens sempre que há alterações significativas:

| Microserviço | Ação                   | Routing Key Enviada |
|--------------|------------------------|---------------------|
| BookCom      | Atualização de Book    | books.update        |
| LendingCom   | Atualização de Lending | lendings.update     |
| ReaderCom    | Atualização de Reader  | readers.update      |

## Vista de Processos

### Nível 1
![GET-books-top5](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/GET-books-top5-Vista de processo nível 1.svg)
- GET-books-top5.puml
    - Representa uma vista de processo em que um cliente HTTP faz uma solicitação para obter a lista dos 5 melhores livros por meio da API /books/top-books. O sistema responde com os detalhes da lista em caso de sucesso (200 OK) ou retorna um erro interno do servidor (500 Internal Server Error) em caso de falha.
  
![GET-reader](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/GET-reader-Vista de processo nível 1.svg)
- GET-reader.puml
    - Mostra o fluxo de um cliente fazendo uma solicitação para obter detalhes de um leitor específico através da API /readers/{id}. O sistema retorna os dados do leitor em caso de sucesso (200 OK) ou uma mensagem de erro informando que o leitor não foi encontrado (404 Not Found).

![POST-authors](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/POST-authors-Vista de processo nível 1.svg)
- POST-authors.puml
    - Exibe o processo de criação de um novo autor, onde o cliente envia uma solicitação POST para /authors. O sistema confirma a criação com um código 201 Created em caso de sucesso, ou retorna um erro de validação (400 Bad Request) se houver problemas com a solicitação.

![PUT-books](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/PUT-books-Vista de processo nível 1.svg)
- PUT-books.puml
    - Demonstra o fluxo de atualização de informações de um livro, com o cliente enviando uma solicitação PUT para /books/{isbn}. O sistema retorna um código 200 OK se o livro for atualizado com sucesso ou 404 Not Found se o livro não for encontrado.

### Nível 2
![Com-1-seerico](/Docs/Design/Entrega2/Vistas de Processos/Nível 2/Comunicacao_1_Microservico.svg)

- Comunicação 1 Microserviço (Comunicacao_1_Microservico.puml)
    - Descreve a interação entre um cliente e duas instâncias do serviço de leitores (Reader). O cliente faz uma solicitação para criar um novo leitor. O serviço Reader processa a criação e envia uma notificação para o message broker, retornando um código 201 Created ao cliente. O message broker, envia uma notificação para a 2ª instância de reader, que também guarda o reader criado. 

TODO -> mudar de acordo com o código
- Comunicação 2 Microserviços (Comunicacao_2_Microservico.puml)
    - Mostra a comunicação entre o serviço de empréstimos (Lending) e o serviço de leitores (Reader). O cliente solicita ao serviço Lending a lista dos top leitores, que busca detalhes em ambas as instâncias do serviço Reader para consistência de dados. Após a consulta, o serviço Lending replica os dados para sua segunda instância e retorna a lista ao cliente.
TODO -> mudar de acordo com o código
- Comunicação 3 Microserviços (Comunicacao_3_Microservico.puml)
    - Apresenta um cenário onde um cliente cria um novo empréstimo, envolvendo os serviços Lending, Reader e Book. O serviço Lending consulta os serviços Reader e Book em ambas as instâncias para validar a existência e a disponibilidade dos recursos antes de criar o empréstimo. Após a criação, os dados são replicados para a segunda instância do serviço Lending, e a confirmação é enviada ao cliente com um código 201 Created.

## Vistas Físicas

### Vista Física Nível 1

- A vista física de nível 1 representa uma visão geral do sistema LMS (Learning Management System), mostrando-o como uma única unidade encapsulada. Essa vista destaca o sistema de forma simplificada, indicando que ele é um componente singular com interfaces e funcionalidades internas.


### Vista Física Nível 2

- A vista física de nível 2 detalha a arquitetura distribuída do sistema LMS, especificando as interações entre diferentes microserviços. Nesta vista, é possível observar que:
  - no1: Readers: Representa o microserviço responsável pela gestão dos leitores. Ele comunica-se com outros microserviços usando o message broker.
  - no2: Lendings: Microserviço responsável pela gestão de empréstimos, que interage tanto com o serviço de leitores quanto com o de livros.
  - no3: Books: Representa o microserviço que lida com a gestão dos livros.
- A comunicação entre os microserviços é indicada através das conexões HTTP.





De forma a aumentar a eficiencia da base de dados, foi utilizado o mecanismo de allocation de sequence cache. As sequences armazenam os próximos valores de IDs num cache para acelerar a criação de novos registos.
Quando o serviço reinicia, este cache é descartado e a base de dados aloca um novo bloco de valores (por exemplo, salta 51 IDs, indo para 52).
Isso leva a uma não continuidade dos IDs entre inicializações, mas não interfere com nenhuma regra de negócio.


Entregar:
US:
Criar reader + user
criar book
criar lending
get lending 
get book by id
Top 1 qualquer