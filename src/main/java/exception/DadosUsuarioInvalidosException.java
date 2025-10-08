package exception;

public class DadosUsuarioInvalidosException extends RuntimeException {
	
	public DadosUsuarioInvalidosException(String mensagem) {
		super(mensagem);
	}
}
