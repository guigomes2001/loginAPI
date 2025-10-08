package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import dto.PessoaDTO;
import util.NullUtil;

/**
 * @author @gui_gomes_18
 */

@Repository
public class PessoaDAO extends DAOGenerica {

	final static String SQL_CADASTRAR_PESSOA = new StringBuilder()
			.append(" INSERT INTO CRT_PESSOAS (NOME, CPF, DATA_NASCIMENTO, TELEFONE, EMAIL, DATA_CADASTRO) ")
			.append(" VALUES (?,?,?,?,?,NOW())").toString();
	
	final static String SQL_CAPTURAR_PESSOA = new StringBuilder()
			.append(" SELECT * FROM CRT_PESSOAS P ")
			.append(" WHERE CPF = ? AND DATA_EXCLUSAO IS NULL ").toString();
	
	final static String SQL_CAPTURAR_PESSOA_POR_CPF = new StringBuilder()
			.append(" SELECT * FROM CRT_PESSOAS P ")
			.append(" WHERE CPF = ? AND DATA_EXCLUSAO IS NULL ").toString();
	
	final static String SQL_CAPTURAR_PESSOA_POR_CHAVE = new StringBuilder()
			.append(" SELECT * FROM CRT_PESSOAS P ")
			.append(" WHERE CHAVE = ? AND DATA_EXCLUSAO IS NULL ").toString();
	
	public int cadastrarPessoa(PessoaDTO pessoa) throws Exception {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(SQL_CADASTRAR_PESSOA);
			pstmt.setString(1, pessoa.getNome());
			pstmt.setString(2, pessoa.getCpf());
			pstmt.setDate(3, !NullUtil.isNullOrEmpty(pessoa.getDataDeNascimento()) ? java.sql.Date.valueOf(pessoa.getDataDeNascimento()) : null);
			pstmt.setString(4, pessoa.getTelefone());
			pstmt.setString(5, pessoa.getEmail());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt);
		}
		return i;
	}

	public int isPessoaJaCadastrada(PessoaDTO pessoa) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 0;
		try {
			pstmt = getConexao().prepareStatement(SQL_CAPTURAR_PESSOA);
			pstmt.setString(1, pessoa.getCpf());
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

	public PessoaDTO capturarPessoaPorCPF(String cpf) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		PessoaDTO pessoa = new PessoaDTO();
		try {
			pstmt = getConexao().prepareStatement(SQL_CAPTURAR_PESSOA_POR_CPF);
			pstmt.setString(1, cpf);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				pessoa = preencherPessoa(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return pessoa;
	}

	private PessoaDTO preencherPessoa(ResultSet rs) throws SQLException {
		PessoaDTO pessoa = new PessoaDTO();
		pessoa.setChave(rs.getLong("CHAVE"));
		pessoa.setNome(rs.getString("NOME"));
		pessoa.setCpf(rs.getString("CPF"));
		pessoa.setDataDeNascimento(rs.getDate("DATA_NASCIMENTO").toLocalDate());
		pessoa.setTelefone(rs.getString("TELEFONE"));
		pessoa.setEmail(rs.getString("EMAIL"));
		pessoa.setDataCadastro(rs.getDate("DATA_CADASTRO"));
		pessoa.setDataExclusao(rs.getDate("DATA_EXCLUSAO"));
		return pessoa;
	}

	public PessoaDTO capturarPessoaPorChave(Long chave) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		PessoaDTO pessoa = new PessoaDTO();
		try {
			pstmt = getConexao().prepareStatement(SQL_CAPTURAR_PESSOA_POR_CHAVE);
			pstmt.setLong(1, chave);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pessoa = preencherPessoa(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(pstmt, rs);
		}
		return pessoa;
	}

	public int isExistePessoaRegistrada(PessoaDTO pessoa) throws SQLException {
		Connection conn = getConexao();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int i = 0;
		try {
			pstmt = conn.prepareStatement(SQL_CAPTURAR_PESSOA_POR_CPF);
			pstmt.setString(1, pessoa.getCpf());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				i = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fechaConexao(conn, pstmt, rs);
		}
		return i;
	}
}
