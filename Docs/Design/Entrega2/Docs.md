# Documentação de Arquitetura de Microserviços

## Visão Geral
Este documento descreve a arquitetura e funcionamento dos microserviços do sistema, abordando a comunicação entre instâncias, configuração de filas e exchanges, e as ações disparadas e consumidas por cada serviço. 

O projeto foi reestruturado utilizando RabbitMQ, um message broker baseado em AMQP, para assegurar a sincronização entre serviços. 

Adicionalmente, foi implementado o padrão Command-Query Responsibility Segregation (CQRS), separando as responsabilidades em microserviços de comando (modificação de dados) e de consulta (consultas de dados).

## Índice
<!-- TOC -->
* [Visão Geral](#visão-geral)
  * [Objetivo do Sistema](#objetivo-do-sistema)
  * [Configuração das Portas e Instâncias](#configuração-das-portas-e-instâncias)
    * [Esquema Usado](#esquema-usado)
    * [Tabela de Portas Padrão](#tabela-de-portas-padrão)
  * [Estratégia de Comunicação](#estratégia-de-comunicação)
    * [Uso de Fanout Exchanges](#uso-de-fanout-exchanges)
  * [Mensagens JSON e Conversão](#mensagens-json-e-conversão)
  * [Ações Disparadas pelos Microserviços](#ações-disparadas-pelos-microserviços)
    * [Tabela de Ações e Exchanges](#tabela-de-ações-e-exchanges)
  * [Subscrição de Filas](#subscrição-de-filas)
    * [Tabela de Subscrições de Filas](#tabela-de-subscrições-de-filas)
* [Vista de Processos](#vista-de-processos)
  * [Nível 1](#nível-1)
  * [Nível 2](#nível-2)
* [Vistas Físicas](#vistas-físicas)
  * [Vista Física Nível 1](#vista-física-nível-1)
  * [Vista Física Nível 2](#vista-física-nível-2)
  * [Vista Física Nível 3](#vista-física-nível-3)
* [Vistas Lógicas](#vistas-lógicas)
  * [Vista Lógica Nível 1](#vista-lógica-nível-1)
  * [Vista Lógica Nível 2](#vista-lógica-nível-2)
  * [Vista Lógica Nível 3](#vista-lógica-nível-3)
* [Notas Finais](#notas-finais)
<!-- TOC -->


### Objetivo do Sistema

O sistema é composto por múltiplos microserviços que colaboram de forma assíncrona através de mensageria, utilizando RabbitMQ para a troca de mensagens. A comunicação entre os microserviços é feita através de exchanges do tipo Fanout, que permitem a distribuição de mensagens para todas as queues associadas a uma determinada exchange.

### Configuração das Portas e Instâncias

Abaixo, apresenta-se a configuração de portas e instâncias para os diferentes serviços do sistema. Cada serviço pode ter múltiplas instâncias, sendo que as portas seguem um padrão de gama definida.

##### Esquema usado:

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
|        | DB Query   |           | x3xx |
|        |            | 1         | xx01 |
|        |            | 2         | xx02 |
|        |            | 10        | xx10 |
|        |            | 99        | xx99 |



#### Tabela de portas padrão:

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
| Top     | Query      | 1         | 6101  |
| Top     | Query      | 2         | 6102  | 
| Top     | DB Query   | 1         | 6301  | 
| Top     | DB Query   | 2         | 6302  | 


### Estratégia de Comunicação
#### Uso de Fanout Exchanges
Para a comunicação entre os microserviços, foi decidido o uso de Fanout Exchanges, um tipo de exchange que distribui todas as mensagens publicadas para todas as queues associadas a ela, sem a necessidade de utilizar routing keys. Esta abordagem simplifica a configuração, uma vez que todas as queues ligadas a uma Fanout Exchange receberão todas as mensagens publicadas nela.

##### Vantagens do Fanout Exchange:
- **Simplicidade:** Não requer definição de routing keys, o que elimina a complexidade do mapeamento entre chave e fila.
- **Escalabilidade:** Ideal para cenários em que várias instâncias de microserviços precisam de receber e processar o mesmo evento.
- **Desacoplamento:** Permite que os serviços consumidores não precisem de conhecer os detalhes de quem está a publicar as mensagens.


### Mensagens JSON e Conversão
Todos os microserviços utilizam o Jackson2JsonMessageConverter para a conversão das mensagens em formato JSON. Este padrão garante que todos os dados trocados entre os microserviços seguem o mesmo formato, evitando problemas de compatibilidade e facilitando a integração entre os diferentes serviços.


### Ações Disparadas pelos Microserviços
Os microserviços enviam mensagens para as exchanges sempre que ocorre uma alteração significativa no sistema. As mensagens enviadas são sempre publicadas na exchange correspondente, e os microserviços subscritos a essas queues processam essas mensagens.

##### Tabela de ações e exchanges
| **Microserviço** | **Ação**              | **Exchange**     |
|----------------|-----------------------|------------------|
| **Auth**         | Criação de Utilizador | auth.exchange    |
| **BookCom**        | Criação de Livro      | book.exchange    |
| **LendingCom**     | Criação de Empréstimo | lending.exchange |
| **ReaderCom**      | Criação de Leitor     | reader.exchange  |


### Subscrição de Filas
Cada microserviço subscreve às queues de interesse, permitindo-lhes consumir as mensagens que são enviadas pelas exchanges. As queues são configuradas de forma única para cada instância, utilizando o padrão UUID.randomUUID(), o que garante a unicidade das filas, especialmente útil em sistemas distribuídos com múltiplas instâncias de microserviços, como é o caso.

##### Tabela de Subscrições de Filas

| **Microserviço**   | **Queue**                                   |
|----------------|-----------------------------------------|
| **Auth**           | auth.queue                              | 
| **BookCom**        | book.queue                              | 
| **BookQuery**      | book.queue                              |
| **LendingCom**     | reader.queue, book.queue, lending.queue |
| **LendingQuery**	  | lending.queue                           |
| **ReaderCom**      | reader.queue                            |
| **ReaderQuery**    | reader.queue                            | 
| **Top**            | lending.queue                           | 


## Vista de Processos

### Nível 1
![GET-books-top5](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/GET-books-top5-Vista de processo nível 1.svg)

GET-books-top5.puml
   - Representa uma vista de processo em que um cliente HTTP faz uma solicitação para obter a lista dos 5 melhores livros por meio da API /books/top-books. O sistema responde com os detalhes da lista em caso de sucesso (200 OK) ou retorna um erro interno do servidor (500 Internal Server Error) em caso de falha.
  
![GET-reader](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/GET-reader-Vista de processo nível 1.svg)

GET-reader.puml
   - Mostra o fluxo de um cliente fazendo uma solicitação para obter detalhes de um leitor específico através da API /readers/{id}. O sistema retorna os dados do leitor em caso de sucesso (200 OK) ou uma mensagem de erro informando que o leitor não foi encontrado (404 Not Found).

![POST-books](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/POST-books-Vista de processo nível 1.svg)

POST-books.puml
   - Exibe o processo de criação de um novo livro, onde o cliente envia uma solicitação POST para /books. O sistema confirma a criação com um código 201 Created em caso de sucesso, ou retorna um erro de validação (400 Bad Request) se houver problemas com a solicitação.

![POST-lendings](/Docs/Design/Entrega2/Vistas de Processos/Nível 1/POST-lendings-Vista de processo nível 1.svg)

POST-lendings.puml
   - Demonstra o fluxo de criação de informações de um empréstimo, com o cliente enviando uma solicitação POST para /lendings. O sistema retorna um código 201 Created se o empréstimo for criado com sucesso, ou retorna um erro de validação (400 Bad Request) se houver problemas com a solicitação.

### Nível 2

![Com-1-servico](/Docs/Design/Entrega2/Vistas de Processos/Nível 2/Comunicacao_1_Microservico.svg)

Comunicação 1 Microserviço (Comunicacao_1_Microservico.puml)
  - Descreve a interação entre um cliente e duas instâncias do serviço de autenticação (Auth). 
  O cliente faz uma solicitação para criar um novo utilizador. 
  O serviço Auth processa a criação e envia uma notificação para o message broker, retornando um código 201 Created ao cliente. 
  O message broker, envia uma notificação para todas as instância de Auth, que também guarda o utilizador criado. 

      

![Com-2-servico](/Docs/Design/Entrega2/Vistas de Processos/Nível 2/Comunicacao_2_Microservico.svg)

Comunicação 2 Microserviços (Comunicacao_2_Microservico.puml)
- Mostra a comunicação entre os serviços de consulta e de comando de empréstimos (Lending) e o serviço de Top, assim como entre as instâncias de cada serviço. 
  O cliente solicita ao serviço Lending o empréstimo de um livro, que processa os detalhes e, caso esteja tudo correto, guarda o novo empréstimo e envia uma notificação para o message broker, retornando um código 201 Created ao cliente.
O message broker, envia uma notificação para todas as instância de LendingCom e LendingQuery e de Top, que também guarda o empréstimo criado.

- O processo é análogo na criação de um leitor (Reader), que é guardado em ReaderCom, ReaderQuery e LendingCom e para a criação de um livro (Book) que é guardado em BookCom, BookQuery e LendingCom.


## Vistas Físicas

### Vista Física Nível 1
![VistaFisica1](/Docs/Design/Entrega2/Vistas Físicas/Nível 1/Nivel1-Vista fisica nível 1.svg)

A vista física de nível 1 representa uma visão geral do sistema LMS (Learning Management System), mostrando-o como uma única unidade encapsulada. Essa vista destaca o sistema de forma simplificada, indicando que ele é um componente singular com interfaces e funcionalidades internas.


### Vista Física Nível 2
![VistaFisica2](/Docs/Design/Entrega2/Vistas Físicas/Nível 2/Nivel2-Vista física nível 2.svg)

A vista física de nível 2 representa a arquitetura de comunicação entre os microserviços, 
onde todos os serviços interagem com o Message Broker via protocolo AMQP. 
Cada microserviço, como Auth, BooksQuery, LendingsCom, etc, envia e recebe mensagens através do broker, 
garantindo a troca de informações de forma assíncrona e desacoplada. 
Este modelo facilita a escalabilidade e a flexibilidade do sistema, 
permitindo que diferentes serviços possam comunicar sem dependências diretas

### Vista Física Nível 3
![VistaFisica3](/Docs/Design/Entrega2/Vistas Físicas/Nível 3/Nivel3-Vista física nível 3.svg)

A vista física de nível 3 exemplifica a interação entre o serviço ReaderCom e a sua base de dados por TCP/IP. 
Este padrão de comunicação é aplicável de forma idêntica a todos os microserviços, 
garantindo que cada serviço se comunica com a sua respetiva base de dados utilizando protocolos de rede estabelecidos, 
mantendo a separação e a independência entre os componentes do sistema.




## Vistas Lógicas

### Vista Lógica Nível 1
![VistaLogica1](/Docs/Design/Entrega2/Vistas Lógicas/Nível 1/Nivel1-Vista Lógica Nível 1.svg)

A vista lógica de nível 1 representa a arquitetura de comunicação entre o sistema LMS e as suas APIs externas. Cada componente do sistema, como Auth, BookCom, LendingCom, entre outros, expõe interfaces HTTP (APIs) que são consumidas por diferentes serviços. As portas de comunicação (portout) ligam as funcionalidades internas do sistema às APIs correspondentes, facilitando a interação e troca de dados entre os microserviços e os sistemas externos.

# REVER
### Vista Lógica Nível 2
![VistaLogica2](/Docs/Design/Entrega2/Vistas Lógicas/Nível 2/Nivel2-Vista Lógica Nível 2.svg)

A vista lógica de nível 2 apresenta a interação entre os componentes internos do sistema LMS e as APIs externas. 
Cada componente possui portas de comunicação de entrada e saida que facilitam a troca de mensagens entre os sistemas internos e as APIs.
A comunicação entre os componentes é realizada via Message Broker, que publica e notifica eventos aos microserviços. 
As APIs externas são expostas pelas portas correspondentes, permitindo a interação com os serviços externos, como Auth API, BookCom API, LendingCom API, etc.

### Vista Lógica Nível 3
![VistaLogica3](/Docs/Design/Entrega2/Vistas Lógicas/Nível 3/Nivel3-Vista lógica nível 3.svg)


A vista lógica de nível 3 ilustra a estrutura interna de um componente de comando. 
Este componente contém um serviço ServiceReaderCom e uma base de dados ReaderCom Database. 
A comunicação entre eles é feita via portas, com o serviço a enviar e receber dados de forma interna, enquanto interage com a base de dados utilizando SQL. 
A Publish API recebe dados através da porta de entrada de ReaderCom, 
enquanto as respostas são enviadas por duas portas de saída, uma para a API HTTP e outra para o serviço de notificação Notify API.



## Notas finais
De forma a aumentar a eficiencia da base de dados, foi utilizado o mecanismo de allocation de sequence cache. As sequences armazenam os próximos valores de IDs num cache para acelerar a criação de novos registos.

Por vezes, quando o serviço reinicia, este cache é descartado e a base de dados aloca um novo bloco de valores (por exemplo, salta 51 IDs, indo para 52).
Isso leva a uma não continuidade dos IDs entre inicializações, mas não interfere com nenhuma regra de negócio.