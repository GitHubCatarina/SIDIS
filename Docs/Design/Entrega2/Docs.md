Separação do código:

Auth
Book (Author + Book)?
Lending
Reader
Top5Books


Foi implementado um sistema de "retry", caso não seja possivel estabelecer ligação com a segunda instancia à primeira tentativa.

As portas foram definidas da seguinte maneira:

<!-- TODO: Ver a situação das bases de dados -->
| Serviço | Tipo       | Instância | Gama |
|---------|------------|-----------|------|
| Auth    |            |           | 2xxx |
| Book    |            |           | 3xxx | 
| Lending |            |           | 4xxx | 
| Reader  |            |           | 5xxx |
| Tops    |            |           | 6xxx |
|         | Command    |           | x0xx | 
|         | Query      |           | x1xx |
|         | DB Command |           | x2xx |
|         | BD Query   |           | x3xx |
|         |            | 1         | xx01 |
|         |            | 2         | xx02 |
|         |            | 10        | xx10 |
|         |            | 99        | xx99 |



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
| Tops    | Query      | 1         | 6101  |
| Tops    | Query      | 2         | 6102  | 
| Tops    | DB Query   | 1         | 6301  | 
| Tops    | DB Query   | 2         | 6302  | 
