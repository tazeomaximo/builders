USE builders;

SET character_set_client = utf8;
SET character_set_connection = utf8;
SET character_set_results = utf8;
SET collation_connection = utf8_general_ci;


CREATE TABLE cliente (
    id_cliente int not null AUTO_INCREMENT,
    email VARCHAR(250) NOT NULL,
    nome VARCHAR(250) NOT NULL,
	cpf VARCHAR(11) NOT NULL,
	dt_nascimento DATE NOT NULL,
    PRIMARY KEY (id_cliente)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

ALTER TABLE cliente
ADD UNIQUE INDEX cliente_email_unique (email ASC);

ALTER TABLE cliente
ADD UNIQUE INDEX cliente_cpf_unique (cpf ASC);


