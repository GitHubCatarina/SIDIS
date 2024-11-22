Foi implementado um sistema de "retry", caso não seja possivel estabelecer ligação com a segunda instancia à primeira tentativa.

As portas foram definidas da seguinte maneira:

<!-- TODO: Ver a situação das bases de dados -->
| Serviço                                                     | Tipo               | Instância | Gama |
|-------------------------------------------------------------|--------------------|-----------|------|
| Auth                                                        |                    |           | 2xxx |
| Book                                                        |                    |           | 3xxx | 
| Lending                                                     |                    |           | 4xxx | 
| Reader                                                      |                    |           | 5xxx |
|  REVER (pode ser preciso uma db para querry e para command) | **Base de dados**  |           | x0xx | 
|                                                             | Query              |           | x1xx |
|                                                             | Command            |           | x2xx |
|                                                             |                    | 1         | xx01 |
|                                                             |                    | 2         | xx02 |
|                                                             |                    | 10        | xx10 |
|                                                             |                    | 99        | xx99 |

Seguindo isso, as portas em uso por predefinição são:

| Serviço | Tipo    | Instância | Porta |
|---------|---------|-----------|-------|
| Auth    | -       | 1         | 2101  |
| Auth    | -       | 2         | 2102  |
| Book    | Query   | 1         | 3101  |
| Book    | Query   | 2         | 3102  |
| Book    | Command | 1         | 3201  |  
| Book    | Command | 2         | 3202  |  
| Lending | Query   | 1         | 4101  |
| Lending | Query   | 2         | 4102  |
| Lending | Command | 1         | 4201  | 
| Lending | Command | 2         | 4202  | 
| Reader  | Query   | 1         | 5101  |
| Reader  | Query   | 2         | 5102  |
| Reader  | Command | 1         | 5201  | 
| Reader  | Command | 2         | 5202  | 
