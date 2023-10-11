create table algafood.forma_pagamento (
	id bigint not null auto_increment,
	descricao varchar(60) not null,
	primary key (id)
) engine=InnoDB default charset=utf8;

create table algafood.grupo (
	id bigint not null auto_increment,
	nome varchar(60) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table algafood.grupo_permissao (
	grupo_id bigint not null,
	permissao_id bigint not null,
	
	primary key (grupo_id, permissao_id)
) engine=InnoDB default charset=utf8;

create table algafood.permissao (
	id bigint not null auto_increment,
	descricao varchar(60) not null,
	nome varchar(100) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table algafood.produto (
	id bigint not null auto_increment,
	restaurante_id bigint not null,
	nome varchar(80) not null,
	descricao text not null,
	preco decimal(10,2) not null,
	ativo tinyint(1) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table algafood.restaurante (
	id bigint not null auto_increment,
	cozinha_id bigint not null,
	nome varchar(80) not null,
	taxa_frete decimal(10,2) not null,
	data_atualizacao datetime not null,
	data_cadastro datetime not null,
	
	endereco_cidade_id bigint,
	endereco_cep varchar(9),
	endereco_logradouro varchar(100),
	endereco_numero varchar(20),
	endereco_complemento varchar(60),
	endereco_bairro varchar(60),
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table algafood.restaurante_forma_pagamento (
	restaurante_id bigint not null,
	forma_pagamento_id bigint not null,
	
	primary key (restaurante_id, forma_pagamento_id)
) engine=InnoDB default charset=utf8;

create table algafood.usuario (
	id bigint not null auto_increment,
	nome varchar(80) not null,
	email varchar(255) not null,
	senha varchar(255) not null,
	data_cadastro datetime not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table algafood.usuario_grupo (
	usuario_id bigint not null,
	grupo_id bigint not null,
	
	primary key (usuario_id, grupo_id)
) engine=InnoDB default charset=utf8;

alter table algafood.grupo_permissao add constraint fk_grupo_permissao_permissao 
foreign key (permissao_id) references algafood.permissao (id);

alter table algafood.grupo_permissao add constraint fk_grupo_permissao_grupo 
foreign key (grupo_id) references algafood.grupo (id);

alter table algafood.produto add constraint fk_produto_restaurante 
foreign key (restaurante_id) references algafood.restaurante (id);

alter table algafood.restaurante add constraint fk_restaurante_cozinha 
foreign key (cozinha_id) references algafood.cozinha (id);

alter table algafood.restaurante add constraint fk_restaurante_cidade 
foreign key (endereco_cidade_id) references algafood.cidade (id);

alter table algafood.restaurante_forma_pagamento add constraint fk_rest_forma_pagto_forma_pagto 
foreign key (forma_pagamento_id) references algafood.forma_pagamento (id);

alter table algafood.restaurante_forma_pagamento add constraint fk_rest_forma_pagto_restaurante 
foreign key (restaurante_id) references algafood.restaurante (id);

alter table algafood.usuario_grupo add constraint fk_usuario_grupo_grupo 
foreign key (grupo_id) references algafood.grupo (id);

alter table algafood.usuario_grupo add constraint fk_usuario_grupo_usuario 
foreign key (usuario_id) references algafood.usuario (id);