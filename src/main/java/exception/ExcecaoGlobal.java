package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dto.ApiResponse;

public class ExcecaoGlobal {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGenerico(Exception ex) {
		ApiResponse<Void> response = new ApiResponse<>(false, true, "Erro inesperado: " + ex.getMessage(), null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	@ExceptionHandler(DadosPessoaInvalidosException.class)
	public ResponseEntity<ApiResponse<Void>> handleDadosPessoaInvalidos(DadosPessoaInvalidosException ex) {
		ApiResponse<Void> response = new ApiResponse<>(false, true, ex.getMessage(), null);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
