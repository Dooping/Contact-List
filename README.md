# Base de Dados #
 
— INFORMAÇÕES SOBRE A BD 

	- Programa: mySql
	
	> Nome da base dados: SS
	> Nome da tabela: accounts
	> Campos da tabela : 
		- name (tipo:varchar)-> username do utilizador
		- pwdhash (tipo:varchar)-> password encriptada
		- logged_in (tipo:bit) -> flag de logged_in 
		- locked (tipo:bit)-> flag de locked

Caso não tenha o nome da base de dados , nome da tabela e campos da tabela coerentes com a informação descrita acima, pode editar-los no ficheiro DatabaseConnection.java

	public static final String DATABASE_NAME="SS";
	public static final String TABLE_NAME="accounts";
	private static final String NAME = "name";
	private static final String PWDHASH = "pwdhash";
	private static final String LOGGED_IN = "logged_in";
	private static final String LOCKED = "locked";


# Testar a Aplicação #

Para testar a aplicação pode utilizar dois tipos de user:
	
	Admin: ( name:root , password:root ) -> Este utilizador poderá fazer loggin, criar novos utilizadores, apagar utilizadores, mudar a sua password, bloquear utilizadores e fazer logout. 

	Normal User: Este tipo de utilizador pode fazer loggin, logout e mudar a sua password