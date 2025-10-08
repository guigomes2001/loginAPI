package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import dto.TokenDTO;
import dto.UsuarioDTO;
import repository.TokenRepository;

/**
 * @author @gui_gomes_18
 */

@Repository
public class TokenDAO extends DAOGenerica {

    public void cadastrarTokenRecuperacaoSenha(Long chaveUsuario, String token, LocalDateTime expira) {
    	Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = getConexao().prepareStatement(TokenRepository.SQL_CADASTRAR_TOKEN_RECUPERACAO_SENHA);
            pstmt.setLong(1, chaveUsuario);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(expira));
            pstmt.setString(3, token);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(conn, pstmt);
        }
    }

    public UsuarioDTO capturarUsuarioPorTokenRecuperacaoSenha(String token) throws SQLException {
    	Connection conn = getConexao();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        UsuarioDTO usuario = new UsuarioDTO();
        try {
            pstmt = conn.prepareStatement(TokenRepository.SQL_CAPTURAR_USUARIO_POR_TOKEN_RECUPERACAO_DE_SENHA);
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();

            if (rs.next()) {
            	usuario = preencherUsuarioETokenDeRecuperacaoSenha(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(pstmt, rs);
        }
        return usuario;
    }

	private UsuarioDTO preencherUsuarioETokenDeRecuperacaoSenha(ResultSet rs) throws SQLException {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setChave(rs.getLong("CHAVE"));
		usuario.setLogin(rs.getString("LOGIN"));
		usuario.setSenha(rs.getString("SENHA"));

		TokenDTO tokenDTO = new TokenDTO();
		tokenDTO.setChave(rs.getLong("CHAVE"));
		tokenDTO.setTokenRecuperacaoDeSenha(rs.getString("TOKEN_RESET_SENHA"));
		tokenDTO.setValidadeTokenRecuperacaoDeSenha(rs.getTimestamp("TEMPO_EXPIRACAO_TOKEN").toLocalDateTime());
		usuario.setToken(tokenDTO);

		return usuario;
	}

    public void expirarTokenRecuperacaoDeSenha(Long chaveUsuario) throws SQLException {
    	Connection conn = getConexao();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(TokenRepository.SQL_EXPIRAR_TOKEN_RECUPERACAO_DE_SENHA);
            pstmt.setLong(1, chaveUsuario);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(conn, pstmt);
        }
    }

    public void cadastrarTokenSessao(Long chaveUsuario, String token, LocalDateTime expira) throws SQLException {
    	Connection conn = getConexao();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(TokenRepository.SQL_CADASTRAR_TOKEN_SESSAO);
            pstmt.setLong(1, chaveUsuario);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(expira));
            pstmt.setString(3, token);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(conn, pstmt);
        }
    }

	public UsuarioDTO capturarUsuarioPorTokenSessao(String token) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = getConexao().prepareStatement(TokenRepository.SQL_CAPTURAR_USUARIO_POR_TOKEN_SESSAO);
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return preencherUsuarioETokenSessao(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(pstmt, rs);
        }
        return null;
    }

	private UsuarioDTO preencherUsuarioETokenSessao(ResultSet rs) throws SQLException {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setChave(rs.getLong("CHAVE"));
		usuario.setLogin(rs.getString("LOGIN"));
		usuario.setSenha(rs.getString("SENHA"));

		TokenDTO tokenDTO = new TokenDTO();
		tokenDTO.setChave(rs.getLong("CHAVE"));
		tokenDTO.setTokenRecuperacaoDeSenha(rs.getString("TOKEN_SESSAO"));
		tokenDTO.setValidadeTokenRecuperacaoDeSenha(rs.getTimestamp("TEMPO_EXPIRACAO_TOKEN").toLocalDateTime());
		usuario.setToken(tokenDTO);

		return usuario;
	}

	 public void expirarTokenSessao(Long chaveUsuario) throws SQLException {
	    Connection conn = getConexao();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(TokenRepository.SQL_EXPIRAR_TOKEN_SESSAO);
            pstmt.setLong(1, chaveUsuario);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(conn, pstmt);
        }
    }

	 public void cadastrarTokenSessaoLonga(Long chaveUsuario, String token, LocalDateTime expira) throws SQLException {
    	Connection conn = getConexao();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(TokenRepository.SQL_CADASTRAR_TOKEN_SESSAO_LONGA);
            pstmt.setLong(1, chaveUsuario);
            pstmt.setString(2, token);
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(expira));
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fechaConexao(conn, pstmt);
        }
    }

	public Boolean isExisteTokenDeSessaoLongaValido(UsuarioDTO usuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        Boolean i = Boolean.FALSE;
        try {
			pstmt = getConexao().prepareStatement(TokenRepository.SQL_CAPTURAR_TOKEN_SESSAO_LONGA_JA_EXISTENTE);
			pstmt.setLong(1, usuario.getChave());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				i = Boolean.TRUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return i;
	}

	public String capturarTokenDeSessaoLongaDoUsuarioAindaValido(UsuarioDTO usuario) {
		PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String token = "";
        try {
        	pstmt = getConexao().prepareStatement(TokenRepository.SQL_CAPTURAR_TOKEN_SESSAO_LONGA_USUARIO_AINDA_VALIDO);
			pstmt.setLong(1, usuario.getChave());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				token = rs.getString("TOKEN_SESSAO_LONGA");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	public int excluirTokenDeSessaoLonga(Long chaveUsuario) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(TokenRepository.SQL_EXCLUIR_TOKEN_SESSAO_LONGA);
			pstmt.setLong(1, chaveUsuario);
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public TokenDTO capturarUsuarioPorTokenRecuperacaoPorChaveUsuario(UsuarioDTO usuario) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		TokenDTO token = new TokenDTO();
		try {
			pstmt = conn.prepareStatement(TokenRepository.SQL_CAPTURAR_USUARIO_POR_TOKEN_RECUPERACAO_DE_SENHA_POR_USUARIO);
			pstmt.setLong(1, usuario.getChave());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				token = preencherTokenDoUsuario(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	private TokenDTO preencherTokenDoUsuario(ResultSet rs) throws SQLException {
		TokenDTO token = new TokenDTO();
		token.setChave(rs.getLong("CHAVE"));
		token.setChaveUsuario(rs.getLong("CHAVE_USUARIO"));
		token.setTokenRecuperacaoDeSenha(rs.getString("TOKEN_RECUPERACAO"));
		token.setValidadeTokenRecuperacaoDeSenha(rs.getTimestamp("TEMPO_EXPIRACAO_TOKEN").toLocalDateTime());
		token.setDataCadastro(rs.getDate("DATA_CADASTRO"));
		token.setDataExclusao(rs.getDate("DATA_EXCLUSAO"));
		token.setDataUtilizacao(rs.getDate("DATA_UTILIZACAO"));
		return token;
	}
}