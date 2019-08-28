
create table usuario (
                         usuario_id bigint generated by default as identity,
                         cpf varchar(11) not null,
                         email varchar(255) not null,
                         is_ativo boolean,
                         nome varchar(255) not null,
                         senha varchar(255) not null,
                         primary key (usuario_id)
);
  create table artigos (
       artigos_id bigint generated by default as identity,
        conteudo text,
        data_criacao timestamp,
        descricao varchar(1000),
        nome varchar(60) not null,
        url varchar(1000),
        categoria_id bigint not null,
        usuario_id bigint not null,
        primary key (artigos_id)
    );


     create table categoria (
       categoria_id bigint generated by default as identity,
        nome varchar(255) not null,
        categoria_pai_id bigint,
        primary key (categoria_id)
    );

     create table endereco (
       id bigint generated by default as identity,
        bairro varchar(255),
        cep varchar(255),
        complemento varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        municipio_id bigint,
        usuario_id bigint,
        primary key (id)
    );

     create table estatistica (
       estatistica_id bigint generated by default as identity,
        artigos bigint,
        categoria bigint,
        date timestamp,
        usuarios bigint,
        primary key (estatistica_id)
    );
    create table municipio (
       municipio_id bigint not null,
        nome varchar(60) not null,
        uf varchar(2) not null,
        primary key (municipio_id)
    );

      create table perfis (
       usuario_usuario_id bigint not null,
        perfis integer
    );

      create table telefone (
       usuario_usuario_id bigint not null,
        telefones varchar(255)
    );



  alter table usuario
      add constraint UK_5171l57faosmj8myawaucatdw unique (email);

  alter table artigos
      add constraint FKjtk4pgbvgewp823spsppwkmqs
          foreign key (categoria_id)
              references categoria(categoria_id);

  alter table artigos
      add constraint FK4s7e5t96epy6d36qq37q333wx
          foreign key (usuario_id)
              references usuario(usuario_id);

  alter table categoria
      add constraint FK9gdokxfvkvnakuki35japqxdk
          foreign key (categoria_pai_id)
              references categoria(categoria_id);


  alter table endereco
      add constraint FKoeo83h2isst97j0vddk9mkji0
          foreign key (municipio_id)
              references municipio(municipio_id);

  alter table endereco
      add constraint FKekdpb8k6gmp3lllla9d1qgmxk
          foreign key (usuario_id)
              references usuario(usuario_id);

  alter table perfis
      add constraint FK5kab8jjyqan5im5y37di23dx0
          foreign key (usuario_usuario_id)
              references usuario(usuario_id);

  alter table telefone
      add constraint FK5wjfsq81srxa53bshkjfk2i8g
          foreign key (usuario_usuario_id)
              references usuario(usuario_id);