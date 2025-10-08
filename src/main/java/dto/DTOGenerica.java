package dto;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class DTOGenerica {

	private Long usuarioId;
	private String login;
	private String tokenSessao;
	private String tokenSessaoLonga;
	private LocalDateTime dataUltimoAcesso;

	private PessoaDTO pessoa;

	public DTOGenerica() {
		capturarUsuarioDaSessao();
	}

	private void capturarUsuarioDaSessao() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UsuarioDTO) {
				UsuarioDTO usuario = (UsuarioDTO) principal;
				this.usuarioId = usuario.getChave();
				this.login = usuario.getLogin();
				this.tokenSessao = usuario.getToken().getTokenSessao();
				this.tokenSessaoLonga = usuario.getToken().getTokenDeSessaoLonga();
				this.dataUltimoAcesso = LocalDateTime.now();
				this.pessoa = usuario.getPessoa();
			}
		}
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public String getLogin() {
		return login;
	}

	public String getTokenSessao() {
		return tokenSessao;
	}

	public String getTokenSessaoLonga() {
		return tokenSessaoLonga;
	}

	public LocalDateTime getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public PessoaDTO getPessoa() {
		return pessoa;
	}

	public boolean usuarioLogado() {
		return this.usuarioId != null && this.tokenSessao != null;
	}

	public boolean possuiTokenSessaoLonga() {
		return this.tokenSessaoLonga != null;
	}

	public void atualizarUltimoAcesso() {
		this.dataUltimoAcesso = LocalDateTime.now();
	}
}