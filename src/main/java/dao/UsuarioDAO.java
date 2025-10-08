package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import dto.LoginRequisicaoDTO;
import dto.UsuarioDTO;
import repository.UsuarioRepository;

/**
 * @author @gui_gomes_18
 */

@Repository
public class UsuarioDAO extends DAOGenerica {
	
	public UsuarioDTO capturarUsuarioPorLogin(String login) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CAPTURAR_USUARIO_POR_LOGIN);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				usuario = comporUsuarioCapturado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt, rs);
		}
		return usuario;
	}
	
	public UsuarioDTO capturarUsuarioPorChave(Long chave) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CAPTURAR_USUARIO_POR_CHAVE);
			pstmt.setLong(1, chave);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				usuario = comporUsuarioCapturado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return usuario;
	}

	private UsuarioDTO comporUsuarioCapturado(ResultSet rs) throws SQLException {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setChave(rs.getLong("CHAVE"));
		usuario.setLogin(rs.getString("LOGIN"));
		usuario.setSenha(rs.getString("SENHA"));
		usuario.setCodigoTipoDeUsuario(rs.getInt("TIPO_DE_USUARIO"));
		usuario.setDataCadastro(rs.getDate("DATA_CADASTRO"));
		usuario.setDataAlteracao(rs.getDate("DATA_ALTERACAO"));
		usuario.setDataExclusao(rs.getDate("DATA_EXCLUSAO"));
		usuario.getPessoa().setChave(rs.getLong("CHAVE_PESSOA"));
		usuario.getSenhaDTO().setQtdTentativasSenhaIncorreta(rs.getInt("QTD_TENTATIVAS_SENHA_INCORRETA"));
		usuario.getSenhaDTO().setDataBloqueioSenhaIncorreta(rs.getDate("DATA_BLOQUEIO"));
		return usuario;
	}

	public int isLoginUsuarioExiste(String login) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 0;
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CAPTURAR_USUARIO_CADASTRADO);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				i = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return i;
	}
	
	public int cadastrarUsuario(UsuarioDTO usuario) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_CADASTRAR_USUARIO);
			pstmt.setString(1, usuario.getLogin());
			pstmt.setString(2, usuario.getSenha());
			pstmt.setLong(3, usuario.getCodigoTipoDeUsuario());
			pstmt.setLong(4, usuario.getPessoa().getChave());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}
	
	public int excluirUsuario(Long chave) throws Exception {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_EXCLUIR_USUARIO);
			pstmt.setLong(1, chave);
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}

	public int atualizarSenhaUsuario(UsuarioDTO usuario, String senha) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_ATUALIZAR_SENHA_USUARIO);
			pstmt.setString(1, senha);
			pstmt.setLong(2, usuario.getChave());
			pstmt.setString(3, usuario.getLogin());
			
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}

	public UsuarioDTO capturarUsuarioPorChavePessoa(Long chavePessoa) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CAPTURAR_USUARIO_POR_CHAVE_PESSOA);
			pstmt.setLong(1, chavePessoa);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				usuario = comporUsuarioCapturado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return usuario;
	}

	public UsuarioDTO capturarUsuarioPorTokenSessao(String tokenSessao) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CAPTURAR_USUARIO_POR_TOKEN_SESSAO);
			pstmt.setString(1, tokenSessao);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				usuario = comporUsuarioCapturado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return usuario;
	}

	public int atualizarQuantidadeSenhasIncorretas(LoginRequisicaoDTO loginRequisicao) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_ATUALIZAR_QUANTIDADE_SENHA_INCORRETA_USUARIO);
			pstmt.setString(1, loginRequisicao.getLogin());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}

	public int resetarQuantidadeSenhasIncorretas(UsuarioDTO usuario) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_RESETAR_TENTATIVAS_SENHA_INCORRETA);
			pstmt.setString(1, usuario.getLogin());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}

	public int capturarQuantidadeSenhaIncorreta(UsuarioDTO usuario) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 0;
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CONSULTAR_TENTATIVAS_SENHA_INCORRETA);
			pstmt.setString(1, usuario.getLogin());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return i;
	}

	public int bloquearUsuario(LoginRequisicaoDTO loginRequisicao) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_BLOQUEAR_LOGIN);
			pstmt.setString(1, loginRequisicao.getLogin());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}
	
	public int bloquearUsuarioPorChave(Long chaveUsuario) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_BLOQUEAR_LOGIN);
			pstmt.setLong(1, chaveUsuario);
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}

	public int reativarUsuario(UsuarioDTO usuario) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		
		int i = 0;
		try {
			pstmt = conn.prepareStatement(UsuarioRepository.SQL_REATIVAR_USUARIO);
			pstmt.setLong(1, usuario.getChave());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public UsuarioDTO capturarUsuarioInativo(String login) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			pstmt = getConexao().prepareStatement(UsuarioRepository.SQL_CAPTURAR_USUARIO_INATIVO);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				usuario = comporUsuarioCapturado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return usuario;
	}
}