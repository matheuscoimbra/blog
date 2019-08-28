  insert
    into
        municipio
        (nome, uf, municipio_id)
    values
        ('São Luis', 'MA', 1);

 insert
    into
        usuario
        (usuario_id, cpf, email, is_ativo, nome, senha)
    values
        (1, '60398764336', 'teste@teste.com', true, 'Usuário Teste', '$2a$10$JfeR7r4T5vzQJuSAMADzguThEYZKgqtXNOXO4H0OWcLdCLlIom2Pm');


 insert
    into
        endereco
        (id, bairro, cep, complemento, logradouro, municipio_id, numero, usuario_id)
    values
        (1, 'Jardim','38220834', 'Apto 303','Rua Flores', 1, '300', 1) ;



 insert
    into
        perfis
        (usuario_usuario_id, perfis)
    values
        (1, 1);

         insert
    into
        telefone
        (usuario_usuario_id, telefones)
    values
        (1, '27363323');

  insert
  into
      categoria
  (categoria_id, nome, categoria_pai_id)
  values
  (1,'PROGRAMACAO',null);
