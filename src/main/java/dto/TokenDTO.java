package dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author @gui_gomes_18
 */

public class TokenDTO extends DTOGenerica {
	
	@JsonIgnore
	private Long chave;
	private Long chaveUsuario;
	private String tokenRecuperacaoDeSenha;
	@JsonIgnore
	private LocalDateTime validadeTokenRecuperacaoDeSenha; 
	private String tokenSessao;
	@JsonIgnore
	private LocalDateTime validadeTokenSessao;
	private String tokenDeSessaoLonga;
	@JsonIgnore
	private LocalDateTime validadeTokenDeSessaoLonga;
	private Date dataCadastro;
	private Date dataExclusao;
	private Date dataUtilizacao;
	
	@JsonIgnore
	private UsuarioDTO usuario;

	public Long getChave() {
		return chave;
	}
	
	public void setChave(Long chave) {
		this.chave = chave;
	}
	
	public Long getChaveUsuario() {
		return chaveUsuario;
	}
	
	public void setChaveUsuario(Long chaveUsuario) {
		this.chaveUsuario = chaveUsuario;
	}
	
	public String getTokenRecuperacaoDeSenha() {
		return tokenRecuperacaoDeSenha;
	}
	
	public void setTokenRecuperacaoDeSenha(String tokenRecuperacaoDeSenha) {
		this.tokenRecuperacaoDeSenha = tokenRecuperacaoDeSenha;
	}
	
	public LocalDateTime getValidadeTokenRecuperacaoDeSenha() {
		return validadeTokenRecuperacaoDeSenha;
	}
	
	public void setValidadeTokenRecuperacaoDeSenha(LocalDateTime validadeTokenRecuperacaoDeSenha) {
		this.validadeTokenRecuperacaoDeSenha = validadeTokenRecuperacaoDeSenha;
	}
	
	public String getTokenSessao() {
		return tokenSessao;
	}
	
	public void setTokenSessao(String tokenSessao) {
		this.tokenSessao = tokenSessao;
	}
	
	public LocalDateTime getValidadeTokenSessao() {
		return validadeTokenSessao;
	}
	
	public void setValidadeTokenSessao(LocalDateTime validadeTokenSessao) {
		this.validadeTokenSessao = validadeTokenSessao;
	}
	
	public String getTokenDeSessaoLonga() {
		return tokenDeSessaoLonga;
	}
	
	public void setTokenDeSessaoLonga(String tokenDeSessaoLonga) {
		this.tokenDeSessaoLonga = tokenDeSessaoLonga;
	}
	
	public LocalDateTime getValidadeTokenDeSessaoLonga() {
		return validadeTokenDeSessaoLonga;
	}
	
	public void setValidadeTokenDeSessaoLonga(LocalDateTime validadeTokenDeSessaoLonga) {
		this.validadeTokenDeSessaoLonga = validadeTokenDeSessaoLonga;
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

	public Date getDataUtilizacao() {
		return dataUtilizacao;
	}

	public void setDataUtilizacao(Date dataUtilizacao) {
		this.dataUtilizacao = dataUtilizacao;
	}

	public UsuarioDTO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioDTO();
		}
		return usuario;
	}
	
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
}
