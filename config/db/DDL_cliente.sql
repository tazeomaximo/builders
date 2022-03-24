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

CREATE TABLE endereco(
        id_endereco int not null AUTO_INCREMENT,
        logradouro varchar(240) not null,
		complemento varchar(240) not null,
		bairro varchar(240) not null,
		localidade varchar(240) not null,
		uf varchar(2) not null,
		cep varchar(8) not null,
        PRIMARY KEY (id_endereco)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE tipo_endereco(
		id_tipo_endereco int not null AUTO_INCREMENT,
		descricao varchar(100) not null,
        PRIMARY KEY (id_tipo_endereco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE cliente_endereco(
		id_cliente_endereco int not null AUTO_INCREMENT,
        id_endereco int not null,
        id_cliente int not null,
		id_tipo_endereco int not null,
        PRIMARY KEY (id_cliente_endereco),
        FOREIGN KEY(id_cliente) REFERENCES cliente(id_cliente),
		FOREIGN KEY(id_endereco) REFERENCES endereco(id_endereco),
		FOREIGN KEY(id_tipo_endereco) REFERENCES tipo_endereco(id_tipo_endereco)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


