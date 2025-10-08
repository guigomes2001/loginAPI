package dto;

import domain.DominioRestricaoLogin;

/**
 * @author @gui_gomes_18
 */

public class ResultadoLoginDTO {

	private boolean sucesso;
	private String mensagem;
	private DominioRestricaoLogin restricao;
	private Long chaveUsuario;
	private String loginUsuario;
	private String tokenSessao;
	private boolean senhaIncorreta;
	
	private UsuarioDTO usuario;
	
	public ResultadoLoginDTO() {
		
	}
	
	public ResultadoLoginDTO(UsuarioDTO usuario) {
		this.sucesso = usuario.getResultadoLogin().isSucesso();
		this.mensagem = usuario.getResultadoLogin().getMensagem();
		this.restricao = usuario.getResultadoLogin().getRestricao();
		this.chaveUsuario = usuario.getChave();
		this.loginUsuario = usuario.getLogin();
		this.tokenSessao = usuario.getToken() != null ? usuario.getToken().getTokenSessao() : null;
	}
	
	public boolean isSucesso() {
		return sucesso;
	}
	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public DominioRestricaoLogin getRestricao() {
		return restricao;
	}
	public void setRestricao(DominioRestricaoLogin restricao) {
		this.restricao = restricao;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public Long getChaveUsuario() {
		return chaveUsuario;
	}
	public void setChaveUsuario(Long chaveUsuario) {
		this.chaveUsuario = chaveUsuario;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	public String getTokenSessao() {
		return tokenSessao;
	}
	public void setTokenSessao(String tokenSessao) {
		this.tokenSessao = tokenSessao;
	}
	public boolean isSenhaIncorreta() {
		return senhaIncorreta;
	}
	public void setSenhaIncorreta(boolean senhaIncorreta) {
		this.senhaIncorreta = senhaIncorreta;
	}
}
