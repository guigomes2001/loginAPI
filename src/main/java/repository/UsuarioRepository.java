package repository;

/**
 * @author @gui_gomes_18
 */

public interface UsuarioRepository {
	
	public final String SQL_CAPTURAR_USUARIO_POR_LOGIN = new StringBuilder()
			.append(" SELECT * FROM CRT_USUARIOS ")
			.append(" WHERE LOGIN = ? ")
			.append("   AND DATA_EXCLUSAO IS NULL").toString();
	
	public final String SQL_CAPTURAR_USUARIO_POR_CHAVE = new StringBuilder()
			.append(" SELECT * FROM CRT_USUARIOS ")
			.append(" WHERE CHAVE = ? ").toString();
	
	public final String SQL_CAPTURAR_USUARIO_POR_CHAVE_PESSOA = new StringBuilder()
			.append(" SELECT * FROM CRT_USUARIOS ")
			.append(" WHERE CHAVE_PESSOA = ? AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_CAPTURAR_USUARIO_POR_TOKEN_SESSAO = new StringBuilder()
			.append(" SELECT * FROM CRT_USUARIOS ")
			.append(" WHERE TOKEN_SESSAO = ? AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_CAPTURAR_USUARIO_CADASTRADO = new StringBuilder()
			.append(" SELECT * FROM CRT_USUARIOS ")
			.append(" WHERE LOGIN = ? ")
			.append("   AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_CADASTRAR_USUARIO = new StringBuilder()
				.append(" INSERT INTO CRT_USUARIOS (")
				.append("    LOGIN, ")
				.append("    SENHA, ")
				.append("    TIPO_DE_USUARIO, ")
				.append("    DATA_CADASTRO, ")
				.append("    CHAVE_PESSOA )")
				.append(" VALUES(?,?,?,NOW(),?) ").toString();
	
	public final String SQL_EXCLUIR_USUARIO = new StringBuilder()
			.append(" UPDATE CRT_USUARIOS ")
			.append(" SET DATA_EXCLUSAO = NOW() ")
			.append(" WHERE CHAVE = ? ").toString();
	
	public final String SQL_ATUALIZAR_SENHA_USUARIO = new StringBuilder()
			.append(" UPDATE CRT_USUARIOS ")
			.append(" SET SENHA = ?, ")
			.append(" DATA_ALTERACAO = NOW() ")
			.append(" WHERE CHAVE = ? AND LOGIN = ? ").toString();
	
	public final String SQL_ATUALIZAR_QUANTIDADE_SENHA_INCORRETA_USUARIO = new StringBuilder()
		    .append(" UPDATE CRT_USUARIOS ")
		    .append(" SET QTD_TENTATIVAS_SENHA_INCORRETA = IFNULL(QTD_TENTATIVAS_SENHA_INCORRETA, 0) + 1, ")
		    .append("     DATA_ALTERACAO = NOW() ")
		    .append(" WHERE LOGIN = ? ").toString();
	
	public final String SQL_RESETAR_TENTATIVAS_SENHA_INCORRETA = new StringBuilder()
		    .append(" UPDATE CRT_USUARIOS ")
		    .append(" SET QTD_TENTATIVAS_SENHA_INCORRETA = 0, ")
		    .append("     DATA_ALTERACAO = NOW() ")
		    .append(" WHERE LOGIN = ? ")
		    .append("   AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_CONSULTAR_TENTATIVAS_SENHA_INCORRETA = new StringBuilder()
		    .append(" SELECT QTD_TENTATIVAS_SENHA_INCORRETA ")
		    .append(" FROM CRT_USUARIOS ")
		    .append(" WHERE LOGIN = ? ")
		    .append("  AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_BLOQUEAR_LOGIN = new StringBuilder()
			.append(" UPDATE CRT_USUARIOS ")
			.append(" SET DATA_BLOQUEIO = NOW() ")
			.append(" WHERE LOGIN = ? ")
			.append("   AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_BLOQUEAR_LOGIN_POR_CHAVE = new StringBuilder()
			.append(" UPDATE CRT_USUARIOS ")
			.append(" SET DATA_BLOQUEIO = NOW() ")
			.append(" WHERE CHAVE = ? ")
			.append("   AND DATA_EXCLUSAO IS NULL ").toString();
	
	public final String SQL_REATIVAR_USUARIO = new StringBuilder()
			.append(" UPDATE CRT_USUARIOS ")
			.append(" SET DATA_EXCLUSAO = NULL ")
			.append(" WHERE CHAVE_USUARIO = ? ").toString();
	
	public final String SQL_CAPTURAR_USUARIO_INATIVO = new StringBuilder()
			.append(" SELECT * FROM CRT_USUARIOS ")
			.append(" WHERE LOGIN = ? ")
			.append("   AND DATA_EXCLUSAO IS NOT NULL").toString();
}
