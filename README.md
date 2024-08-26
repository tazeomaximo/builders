# builders
#Projeto com vunerabilidade Swagger-ui

O projeto encontra-se na pasta __ms-cliente__

A configuração do __docker e docker-compose__ estão dentro da pasta __config__

O arquivo do __postman__ também encontra-se na pasta __config__

# Criando a imagem 

Caso seja necessário criar a imagem do docker basta entrar na pasta __config__ e rodar o comando abaixo.

```
docker build -t img-ms-cliente .
```
A imagem tem os seguintes parâmetros configuráveis.
```java
  MYSQL_HOST 127.0.0.1
  MYSQL_PORT 3306
  MYSQL_DATABASE builders
  MYSQL_USER_NAME builders
  MYSQL_PASSWORD builders
  PROFILES_ACTIVE prod
```

# Docker Composer

A imagem do projeto está criada no repositório público __gutodarbem/img-ms-cliente:1.0__

Basta executar o __docker-compose__ para subir a aplicação 
```
  docker-compose up -d
```

Acessar no browser o swagger [http://localhost:8202/ms-cliente/swagger-ui.html](http://localhost:8202/ms-cliente/swagger-ui.html).


# Processo criativo

* Pensando com um todo
* Desenvolvendo modelo de dados
* Desenvolvendo o swagger, pensamento de "swagger first"
* Contruindo a aplicação para funcionar
* Entendo como fazer a pesquisa genérica por qualquer campo do objeto cliente 
* Entendo como fazer a atualização parcial de forma mais genérica
* Aplicando refactor para melhorar a aplicação
* Desenvolvmento casos de teste unitário
* Contruindo o container docker
* Contruindo o docker-compose
* Disponíbiliznado a imagem da aplicação em um reposítorio público
* Alterado o docker-compose para pegar a imagem do reposítorio público

