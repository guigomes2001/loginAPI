package dto;

public class ApiResponse<T> {
	private boolean sucesso;
	private boolean possuiErro;
	private String mensagem;
	private T dados;

	public ApiResponse() {
	}

	public ApiResponse(boolean sucesso, boolean possuiErro, String mensagem, T dados) {
		this.sucesso = sucesso;
		this.possuiErro = possuiErro;
		this.mensagem = mensagem;
		this.dados = dados;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}
	
	public boolean isPossuiErro() {
		return possuiErro;
	}
	
	public void setPossuiErro(boolean possuiErro) {
		this.possuiErro = possuiErro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public T getDados() {
		return dados;
	}

	public void setDados(T dados) {
		this.dados = dados;
	}
}
