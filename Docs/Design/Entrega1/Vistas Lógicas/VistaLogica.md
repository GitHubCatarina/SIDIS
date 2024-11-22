## Vistas Lógicas 

### Vista Lógica Nível 1
A vista lógica de nível 1 mostra como os diferentes serviços interagem entre si e com os clientes externos. Cada serviço é representado como um componente que se comunica com os outros via APIs HTTP.

Descrição:

- HTTP Reader API: Exposta pelo serviço de leitores, permitindo operações como obtenção e gerenciamento de leitores.
- HTTP Book API: Exposta pelo serviço de livros para operações relacionadas a livros.
- HTTP Lending API: Exposta pelo serviço de empréstimos, responsável por gerenciar transações de empréstimos.
- HTTP Authentication API: Exposta pelo serviço de autenticação, que cuida da segurança e controle de acesso.

### Vista Lógica Nível 2
Este nível mostra como os serviços estão organizados dentro do sistema LMS (Library Management System). Cada serviço é representado como um componente dentro do sistema, destacando as APIs que interagem com cada serviço.

- LMS: Inclui os componentes Readers, Lendings, Books e Auth.
- Comunicação: Indica as conexões HTTP entre os componentes.


### Vista Lógica Nível 3
A vista lógica de nível 3 detalha a arquitetura interna de cada serviço, mostrando como as diferentes camadas do serviço interagem.

Componentes:
- Controller: Interface de entrada do serviço, responsável por expor endpoints REST.
- Service: Contém a lógica de negócios e é responsável por orquestrar as operações.
- Repository: Lida com o acesso a dados e comunicação com a base de dados.
- JPA: Representa a interface para comunicação com a camada de persistência (SQL).

A comunicação entre essas camadas é representada por conexões que mostram como os dados fluem através do sistema, desde a entrada na API até a persistência no banco de dados.