package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.TokenBO;
import dto.ApiResponse;
import dto.LoginRequisicaoDTO;
import dto.TokenDTO;
import exception.TokenException;

/**
 * @author @gui_gomes_18
 */

@RequestMapping("/token")
@RestController
public class TokenWS {

	@Autowired @Lazy
	private TokenBO tokenBO;
	
	@PostMapping("/validarToken")
	public ResponseEntity<ApiResponse<Object>> validarToken(@RequestBody TokenDTO token) {
	    try {
	        boolean tokenValido = tokenBO.validarToken(token);
	        if (tokenValido) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Token válido", null));
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, true, "Token inválido ou expirado", null));
	    } catch(TokenException e) {
	    	return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, e.getMessage(), null));
		} catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro ao validar token", null));
	    }
	}
	
	@PostMapping("/reenviarTokenRecuperacaoDeSenha")
	public ResponseEntity<ApiResponse<Void>> reenviarTokenRecuperacaoDeSenha(@RequestBody LoginRequisicaoDTO loginRequisicao) {
	    try {
	        boolean reenviado = tokenBO.reenviarTokenRecuperacaoDeSenha(loginRequisicao);
	        if (reenviado) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Token reenviado com sucesso", null));
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, true, "Usuário não encontrado", null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro ao reenviar token", null));
	    }
	}
}
