package domain;

public enum DominioMensagensPerfil {
	
	NAO_E_POSSIVEL_CADASTRAR_USUARIO_DESSE_TIPO("Não é possível cadastrar um usuário deste tipo! Contate o administrador do sistema para criar esse tipo de usuário.");
	
	private String mensagem;
	
	DominioMensagensPerfil(String mensagen) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
