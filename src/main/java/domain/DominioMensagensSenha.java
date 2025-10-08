package domain;

public enum DominioMensagensSenha {
	
	ERRO_AO_DEFINIR_SENHA_PADRAO("Ocorreu um erro ao definir a senha padrão."),
	SENHA_ATUAL_INCORRETA("A senha atual informada está incorreta!"),
	SENHA_FORTE("A nova senha deve conter ao menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e símbolos especiais."),
	SENHA_DEVE_SER_DIFERENTE_DA_ATUAL("A nova senha deve ser diferente da senha atual!"),
	NAO_FOI_POSSIVEL_ALTERAR_SENHA("Não foi possível alterar a senha");
	
	private String mensagem;
	
	DominioMensagensSenha(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
