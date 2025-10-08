package domain;

public enum DominioMensagensPessoa {
	
	JA_EXISTE_USUARIO_VINCULADO_A_PESSOA("Já existe um usuário ativo com o mesmo tipo de perfil vinculado á pessoa."),
	DADOS_INVALIDOS("Dados pessoa inválidos");
	
	private String mensagem;
	
	DominioMensagensPessoa(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
