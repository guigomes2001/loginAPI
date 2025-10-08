package dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author @gui_gomes_18
 */

@Entity
public class UsuarioDTO extends DTOGenerica {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chave;
	private String login;
	@JsonIgnore
	private String senha;
	private int codigoTipoDeUsuario;
	private String descricaoTipoDeUsuario;
	private Date dataCadastro;
	private Date dataExclusao;
	private Date dataAlteracao;
	private boolean loginAutenticado;
	private String mensagem;
	private boolean loginBlindado;
	private boolean possuiErro;
	private boolean emRedefinicaoDeSenha;
	
	private PessoaDTO pessoa;
	private TokenDTO token;
	@JsonIgnore
	private SenhaDTO senhaDTO;
	@JsonIgnore
	private ResultadoLoginDTO resultadoLogin;
	@JsonIgnore
	private RegrasDoUsuarioDTO regrasDeUsuarioDTO;
	
	public UsuarioDTO() {
		
	}
	
	public UsuarioDTO(Long chave, String login, String senha, int codigoTipoDeUsuario, String descricaoTipoDeUsuario, Date dataCadastro, Date dataExclusao, Date dataAlteracao, PessoaDTO pessoaDTO, boolean loginAutenticado, String mensagem) {
		this.chave = chave;
		this.login = login;
		this.senha = senha;
		this.codigoTipoDeUsuario = codigoTipoDeUsuario;
		this.descricaoTipoDeUsuario = descricaoTipoDeUsuario;
		this.dataCadastro = dataCadastro;
		this.dataExclusao = dataExclusao;
		this.dataAlteracao = dataAlteracao;
		this.loginAutenticado = loginAutenticado;
		this.mensagem = mensagem;
	}

	public Long getChave() {
		return chave;
	}

	public void setChave(Long chave) {
		this.chave = chave;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public int getCodigoTipoDeUsuario() {
		return codigoTipoDeUsuario;
	}
	
	public void setCodigoTipoDeUsuario(int codigoTipoDeUsuario) {
		this.codigoTipoDeUsuario = codigoTipoDeUsuario;
	}
	
	public String getDescricaoTipoDeUsuario() {
		return descricaoTipoDeUsuario;
	}
	
	public void setDescricaoTipoDeUsuario(String descricaoTipoDeUsuario) {
		this.descricaoTipoDeUsuario = descricaoTipoDeUsuario;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public PessoaDTO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaDTO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaDTO pessoa) {
		this.pessoa = pessoa;
	}
	
	public boolean isLoginAutenticado() {
		return loginAutenticado;
	}
	
	public void setLoginAutenticado(boolean loginAutenticado) {
		this.loginAutenticado = loginAutenticado;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public TokenDTO getToken() {
		if (token == null) {
			token = new TokenDTO();
		}
		return token;
	}
	
	public void setToken(TokenDTO token) {
		this.token = token;
	}

	public boolean isLoginBlindado() {
		return loginBlindado;
	}

	public void setLoginBlindado(boolean loginBlindado) {
		this.loginBlindado = loginBlindado;
	}
	
	public boolean isPossuiErro() {
		return possuiErro;
	}
	
	public void setPossuiErro(boolean possuiErro) {
		this.possuiErro = possuiErro;
	}
	
	public boolean isEmRedefinicaoDeSenha() {
		return emRedefinicaoDeSenha;
	}
	
	public void setEmRedefinicaoDeSenha(boolean emRedefinicaoDeSenha) {
		this.emRedefinicaoDeSenha = emRedefinicaoDeSenha;
	}
	
	public SenhaDTO getSenhaDTO() {
		if (senhaDTO == null) {
			senhaDTO = new SenhaDTO();
		}
		return senhaDTO;
	}
	
	public void setSenhaDTO(SenhaDTO senhaDTO) {
		this.senhaDTO = senhaDTO;
	}
	
	public ResultadoLoginDTO getResultadoLogin() {
		if (resultadoLogin == null) {
			resultadoLogin = new ResultadoLoginDTO();
		}
		return resultadoLogin;
	}
	
	public void setResultadoLogin(ResultadoLoginDTO resultadoLogin) {
		this.resultadoLogin = resultadoLogin;
	}
	
	public RegrasDoUsuarioDTO getRegrasDeUsuarioDTO() {
		if (regrasDeUsuarioDTO == null) {
			regrasDeUsuarioDTO = new RegrasDoUsuarioDTO();
		}
		return regrasDeUsuarioDTO;
	}
	
	public void setRegrasDeUsuarioDTO(RegrasDoUsuarioDTO regrasDeUsuarioDTO) {
		this.regrasDeUsuarioDTO = regrasDeUsuarioDTO;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(chave, dataAlteracao, dataCadastro, dataExclusao, login, pessoa, senha);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(chave, other.chave) && Objects.equals(dataAlteracao, other.dataAlteracao)
				&& Objects.equals(dataCadastro, other.dataCadastro) && Objects.equals(dataExclusao, other.dataExclusao)
				&& Objects.equals(login, other.login) && Objects.equals(pessoa, other.pessoa)
				&& Objects.equals(senha, other.senha);
	}

}
