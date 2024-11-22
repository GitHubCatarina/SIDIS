## Vista de Processos

### Nível 1

- GET-books-top5.puml
  - Representa uma vista de processo em que um cliente HTTP faz uma solicitação para obter a lista dos 5 melhores livros por meio da API /books/top-books. O sistema responde com os detalhes da lista em caso de sucesso (200 OK) ou retorna um erro interno do servidor (500 Internal Server Error) em caso de falha.

- GET-reader.puml
  - Mostra o fluxo de um cliente fazendo uma solicitação para obter detalhes de um leitor específico através da API /readers/{id}. O sistema retorna os dados do leitor em caso de sucesso (200 OK) ou uma mensagem de erro informando que o leitor não foi encontrado (404 Not Found).

- POST-authors.puml
  - Exibe o processo de criação de um novo autor, onde o cliente envia uma solicitação POST para /authors. O sistema confirma a criação com um código 201 Created em caso de sucesso, ou retorna um erro de validação (400 Bad Request) se houver problemas com a solicitação.

- PUT-books.puml
  - Demonstra o fluxo de atualização de informações de um livro, com o cliente enviando uma solicitação PUT para /books/{isbn}. O sistema retorna um código 200 OK se o livro for atualizado com sucesso ou 404 Not Found se o livro não for encontrado.

### Nível 2

- Comunicação 1 Microserviço (Comunicacao_1_Microservico.puml)
  - Descreve a interação entre um cliente e duas instâncias do serviço de leitores (Reader). O cliente faz uma solicitação para criar um novo leitor. O serviço Reader processa a criação e a replica para a segunda instância para garantir consistência, retornando um código 201 Created ao cliente.

- Comunicação 2 Microserviços (Comunicacao_2_Microservico.puml)
  - Mostra a comunicação entre o serviço de empréstimos (Lending) e o serviço de leitores (Reader). O cliente solicita ao serviço Lending a lista dos top leitores, que busca detalhes em ambas as instâncias do serviço Reader para consistência de dados. Após a consulta, o serviço Lending replica os dados para sua segunda instância e retorna a lista ao cliente.

- Comunicação 3 Microserviços (Comunicacao_3_Microservico.puml)
  - Apresenta um cenário onde um cliente cria um novo empréstimo, envolvendo os serviços Lending, Reader e Book. O serviço Lending consulta os serviços Reader e Book em ambas as instâncias para validar a existência e a disponibilidade dos recursos antes de criar o empréstimo. Após a criação, os dados são replicados para a segunda instância do serviço Lending, e a confirmação é enviada ao cliente com um código 201 Created.