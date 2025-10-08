package dto;

/**
 * @author @gui_gomes_18
 */

public class LoginRequisicaoDTO {
	
	private String login;
	private String senha;
	private String senhaAntiga;
	private Integer codigoTipoDeUsuario;
	
	private PessoaDTO pessoa;
	private TokenDTO token;
	
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
	
	public String getSenhaAntiga() {
		return senhaAntiga;
	}
	
	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}
	
	public Integer getCodigoTipoDeUsuario() {
		return codigoTipoDeUsuario;
	}
	public void setCodigoTipoDeUsuario(Integer codigoTipoDeUsuario) {
		this.codigoTipoDeUsuario = codigoTipoDeUsuario;
	}
	
	public PessoaDTO getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(PessoaDTO pessoa) {
		this.pessoa = pessoa;
	}
	
	public TokenDTO getToken() {
		return token;
	}
	
	public void setToken(TokenDTO token) {
		this.token = token;
	}
}
