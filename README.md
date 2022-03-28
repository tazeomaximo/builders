# builders


O projeto encontra-se na pasta __ms-cliente__

A configuração do __docker e docker-compose__ estão dentro da pasta __config__

O arquivo do __postman__ também encontra-se na pasta __config__

# Criando a imagem 

Caso seja necessário criar a imagem do docker basta entrar na pasta __config__ e rodar o comando abaixo.

```
docker build -t img-ms-cliente .
```

# Docker Composer

A imagem do projeto está criada no repositório público __gutodarbem/img-ms-cliente:1.0__

Basta executar o __docker-compose__ para subir a aplicação e acessar no browser o swagger [http://localhost:8202/ms-cliente/swagger-ui.html](http://localhost:8202/ms-cliente/swagger-ui.html).

