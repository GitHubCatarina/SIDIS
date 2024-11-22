## Vistas Físicas

### Vista Física Nível 1

- A vista física de nível 1 representa uma visão geral do sistema LMS (Learning Management System), mostrando-o como uma única unidade encapsulada. Essa vista destaca o sistema de forma simplificada, indicando que ele é um componente singular com interfaces e funcionalidades internas.


### Vista Física Nível 2

- A vista física de nível 2 detalha a arquitetura distribuída do sistema LMS, especificando as interações entre diferentes microserviços. Nesta vista, é possível observar que:
  - no1: Readers: Representa o microserviço responsável pela gestão dos leitores. Ele se comunica com outros microserviços usando HTTP.
  - no2: Lendings: Microserviço responsável pela gestão de empréstimos, que interage tanto com o serviço de leitores quanto com o de livros.
  - no3: Books: Representa o microserviço que lida com a gestão dos livros. 
- A comunicação entre os microserviços é indicada através das conexões HTTP.
