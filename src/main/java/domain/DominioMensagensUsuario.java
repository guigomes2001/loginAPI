package domain;

public enum DominioMensagensUsuario {

	ERRO_AO_CADASTRAR_O_USUARIO("Ocorreu um erro inesperado ao validar o usuário para cadastro."),
	NAO_FOI_POSSIVEL_EXCLUIR_O_USUARIO("Não foi possível excluir o usuário. Tente novamente"),
	NAO_FOI_POSSIVEL_BLOQUEAR_O_USUARIO("Não foi possível bloquear o usuário. Tente novamente");
	
	private String mensagem;
	
	DominioMensagensUsuario(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
