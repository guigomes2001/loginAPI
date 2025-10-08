package repository;

/**
 * @author @gui_gomes_18
 */

public interface TokenRepository {
	
	 final static String SQL_CADASTRAR_TOKEN_RECUPERACAO_SENHA = new StringBuilder()
        .append(" INSERT INTO CRT_TOKEN_RECUPERACAO_SENHA (CHAVE_USUARIO, TOKEN_RESET_SENHA, TEMPO_EXPIRACAO_TOKEN, DATA_CADASTRO) ")
        .append(" VALUES (?, ?, ?, NOW()) ").toString();

    final static String SQL_CADASTRAR_TOKEN_SESSAO = new StringBuilder()
        .append(" INSERT INTO CRT_TOKEN_SESSAO (CHAVE_USUARIO, TOKEN_SESSAO, TEMPO_EXPIRACAO_TOKEN, DATA_CADASTRO) ")
        .append(" VALUES (?, ?, ?, sysdate) ").toString();
    
    final static String SQL_CADASTRAR_TOKEN_SESSAO_LONGA = new StringBuilder()
            .append(" INSERT INTO CRT_TOKEN_SESSAO_LONGA (CHAVE_USUARIO, TOKEN_SESSAO_LONGA, TEMPO_EXPIRACAO_TOKEN, DATA_CADASTRO) ")
            .append(" VALUES (?, ?, ?, NOW()) ").toString();
    
    final static String SQL_CAPTURAR_USUARIO_POR_TOKEN_RECUPERACAO_DE_SENHA = new StringBuilder()
        .append(" SELECT u.CHAVE, u.LOGIN, u.SENHA, t.CHAVE, t.TOKEN_RESET_SENHA, t.TEMPO_EXPIRACAO_TOKEN ")
        .append(" FROM CRT_USUARIOS u ")
        .append(" JOIN CRT_TOKEN_RECUPERACAO_SENHA t ON u.CHAVE = t.CHAVE_USUARIO ")
        .append(" WHERE t.TOKEN_RESET_SENHA = ? AND DATA_EXCLUSAO IS NULL ").toString();
    
    final static String SQL_CAPTURAR_USUARIO_POR_TOKEN_RECUPERACAO_DE_SENHA_POR_USUARIO = new StringBuilder()
        .append(" SELECT u.CHAVE, u.LOGIN, u.SENHA, t.CHAVE, t.TOKEN_RESET_SENHA, t.TEMPO_EXPIRACAO_TOKEN ")
        .append(" FROM CRT_USUARIOS u ")
        .append(" JOIN CRT_TOKEN_RECUPERACAO_SENHA t ON u.CHAVE = t.CHAVE_USUARIO ")
        .append(" WHERE t.TOKEN_RESET_SENHA = ? AND DATA_EXCLUSAO IS NULL ").toString();
    
    final static String SQL_CAPTURAR_USUARIO_POR_TOKEN_SESSAO = new StringBuilder()
        .append(" SELECT U.CHAVE, U.LOGIN, U.SENHA, T.TOKEN_SESSAO, T.TEMPO_EXPIRACAO_TOKEN ")
        .append(" FROM CRT_USUARIOS U ")
        .append(" JOIN CRT_TOKEN_SESSAO T ON U.CHAVE = T.CHAVE_USUARIO ")
        .append(" WHERE T.TOKEN_SESSAO = ? ")
        .append("   AND u.DATA_EXCLUSAO IS NULL ")
        .append("   AND t.DATA_UTILIZACAO IS NULL ")
        .append("   AND t.DATA_EXCLUSAI IS NULL ").toString();

    final static String SQL_EXPIRAR_TOKEN_RECUPERACAO_DE_SENHA = new StringBuilder()
        .append(" UPDATE CRT_TOKEN_RECUPERACAO_SENHA ")
        .append(" SET DATA_UTILIZACAO = NOW() ")
        .append(" WHERE CHAVE_USUARIO = ? ")
        .append("  AND DATA_CADASTRO IS NOT NULL ")
        .append("  AND DATA_EXCLUSAO IS NULL ").toString();
    
    final static String SQL_EXPIRAR_TOKEN_SESSAO = new StringBuilder()
        .append(" UPDATE CRT_TOKEN_SESSAO ")
        .append(" SET DATA_UTILIZACAO = NOW() ")
        .append(" WHERE CHAVE_USUARIO = ? ") 
        .append("  AND DATA_CADASTRO IS NOT NULL ")
        .append("  AND DATA_EXCLUSAO IS NULL ").toString();
    
    final static String SQL_CAPTURAR_TOKEN_SESSAO_LONGA_JA_EXISTENTE = new StringBuilder()
        .append(" SELECT * ")
        .append(" FROM CRT_TOKEN_SESSAO_LONGA tsl ")
        .append(" WHERE tsl.CHAVE_USUARIO = ? AND tsl.DATA_EXCLUSAO IS NULL ").toString();
    
    final static String SQL_CAPTURAR_TOKEN_SESSAO_LONGA_USUARIO_AINDA_VALIDO = new StringBuilder()
        .append(" SELECT TOKEN_SESSAO_LONGA ")
        .append(" FROM CRT_TOKEN_SESSAO_LONGA TSL ")
        .append(" WHERE TSL.CHAVE_USUARIO = ? ")
        .append("   AND DATA_EXCLUSAO IS NULL ")
        .append("   AND DATA_UTILIZACAO IS NULL ").toString();
    
    final static String SQL_EXCLUIR_TOKEN_SESSAO_LONGA = new StringBuilder()
            .append(" UPDATE CRT_TOKEN_SESSAO_LONGA tsl ")
            .append(" SET tsl.DATA_EXCLUSAO = NOW() ")
            .append(" WHERE tsl.CHAVE_USUARIO = ? ").toString();
}
