# Base de Dados #
 
Informações sobre a BD 

	- Programa: mySql
	- correr o ficheiro ss.sql fornecido
	
	Será criada a base de dados SS com todas as tabelas necessárias e utilizadores para efeitos de teste.

	O user e password de acesso à base de dados por defeito é 'root' e 'root', estes valores podem ser alterados na classe DatabaseConnection.java mudando as constantes USER e PASSWORD.

# Pré-requisitos #

Para correr o projecto são necessários os seguintes recursos:

	- Apache Taglibs: https://tomcat.apache.org/download-taglibs.cgi 
	- Json Simple: http://www.java2s.com/Code/Jar/j/Downloadjsonsimple111jar.htm

# Testar a Aplicação #

Para testar a aplicação pode utilizar dois tipos de user:
	
	Admin ( name:root ): Este utilizador poderá fazer loggin, criar novos utilizadores, apagar utilizadores, mudar a sua password, bloquear utilizadores e fazer logout. 

	Normal User (name: 1, 2, 3, 4, 5): Este tipo de utilizador pode fazer loggin, logout e mudar a sua password

	A password de todos os utilizadores criados é 'root'.


# Pontos a Considerar #

	Todos os utilizadores podem efectuar as operações pedidas no Handout 1

	Por defeito, os dados do perfil (profissão, data de nascimento, idade, naturalidade, localidade), os contactos (email, número de telefone) e a descrição interna estão apenas disponíveis aos amigos. A descrição externa está disponível para todos. A lista de amigos é privada. É possível alterar todos estes níveis de visibilidade nas settings.

	Se um utilizador quiser aceder a um recurso que está apenas disponível aos amigos pode fazer um pedido de amizade e quando o utilizador aceitar este vai poder aceder aos recursos com permissão interna.

	Utilizou-se o nonce do owner do recurso para assinar as Capabilities, permitindo assim verificar que as capabilities foram geradas correctamente e permitindo também que um utilizador ao mudar as permissões dos recursos nas Settings faça revoke de todas as Capabilities emitidas antes mudando apenas o nonce.

	O utilizador 'root' ao ver a lista completa de utilizadores (Contact List) vê também os utilizadores que estão locked com fundo cinzento escuro, enquanto que os utilizadores normais apenas vêm os utilizadores que não estiverem locked.