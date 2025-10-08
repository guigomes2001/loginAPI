package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.LoginBO;
import dto.ApiResponse;
import dto.LoginRequisicaoDTO;
import dto.UsuarioDTO;
import exception.PerfilException;
import exception.PessoaException;
import exception.SenhaException;
import exception.TokenException;
import exception.UsuarioException;
import jakarta.validation.Valid;
import util.NullUtil;

/**
 * @author @gui_gomes_18
 */

@RequestMapping("/loginAPI")
@RestController
public class LoginWS {
	
	@Autowired @Lazy
	private LoginBO loginBO;
	
	@PostMapping("/logar")
	public ResponseEntity<ApiResponse<?>> efetuarLoginUsuario(@Valid @RequestBody LoginRequisicaoDTO loginRequisicao) {
	    try {
	        UsuarioDTO usuario = loginBO.efetuarLoginUsuario(loginRequisicao);

	        if (!NullUtil.isNullOrEmpty(usuario) 
	        		&& !NullUtil.isNullOrEmpty(usuario.isLoginAutenticado())
	        		   && usuario.isLoginAutenticado()) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Login realizado com sucesso", usuario));
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, true, "Login ou senha inválidos", null));
	    } catch(UsuarioException | SenhaException e) { 
	    	return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, e.getMessage(), null));
		} catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro no sistema", null));
	    }
	}
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<?>> efetuarLogoutUsuario(@RequestBody LoginRequisicaoDTO loginRequisicao) {
	    try {
	    	loginBO.efetuarLogoutUsuario(loginRequisicao);
	    	return ResponseEntity.ok(new ApiResponse<>(true, false, "Usuário deslogado com sucesso. Descarte o token no front-end.", null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro no sistema", null));
	    }
	}
	
	@PostMapping("/renovarSessao")
	public ResponseEntity<ApiResponse<?>> renovarSessao(@RequestBody UsuarioDTO usuarioDTO) {
	    try {
	        UsuarioDTO usuario = loginBO.validarTokenSessaoLonga(usuarioDTO);

	        if (!NullUtil.isNullOrEmpty(usuario) && !NullUtil.isNullOrEmpty(usuario.getLogin())) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Sessão renovada com sucesso", usuario));
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, true, "Token inválido ou expirado", null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro no sistema", null));
	    }
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<ApiResponse<UsuarioDTO>> cadastrarUsuario(@RequestBody LoginRequisicaoDTO loginRequisicao) {
	    try {
	        UsuarioDTO usuario = loginBO.cadastrarUsuario(loginRequisicao);
	        if (!NullUtil.isNullOrEmpty(usuario)) {
	            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, false, "Usuário cadastrado com sucesso! Senha padrão de primeiro acesso 'criptocom@1234'", usuario));
	        }
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, true, "Erro ao cadastrar usuário", null));
	    } catch(UsuarioException | PessoaException | PerfilException e) { 
	    	return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, e.getMessage(), null));
		} catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro ao cadastrar usuário", null));
	    }
	}
	
	@DeleteMapping("/excluirUsuario/{chaveUsuario}")
	public ResponseEntity<ApiResponse<Void>> excluirLogin(@PathVariable Long chaveUsuario) {
	    try {
	        boolean excluido = loginBO.excluirLogin(chaveUsuario);
	        if (excluido) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Usuário excluído com sucesso", null));
	        }
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, true, "Ocorreu um erro ao excluir o usuário", null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PutMapping("/bloquearUsuario/{chaveUsuario}")
	public ResponseEntity<ApiResponse<Void>> bloquearUsuario(@PathVariable Long chaveUsuario) {
	    try {
	        boolean bloqueado = loginBO.bloquearUsuario(chaveUsuario);
	        if (bloqueado) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Usuário bloqueado com sucesso", null));
	        }
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, true, "Falha ao bloquear usuário", null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro interno", null));
	    }
	}
	
	@PutMapping("/alterarSenha")
	public ResponseEntity<ApiResponse<Void>> alterarSenha(@RequestBody LoginRequisicaoDTO loginRequisicao) {
		if (!isDadosLoginRequisicaoValidos(loginRequisicao)) {
		    return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, "Dados obrigatórios não informados", null));
		}
	    try {
	        loginBO.alterarSenha(loginRequisicao);
	        return ResponseEntity.ok(new ApiResponse<>(true, false, "Senha alterada com sucesso", null));
	    } catch (SenhaException | PessoaException e) {
	    	return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, e.getMessage(), null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro interno no servidor", null));
	    }
	}

	private boolean isDadosLoginRequisicaoValidos(LoginRequisicaoDTO loginRequisicao) {
	    return !(NullUtil.isNullOrEmpty(loginRequisicao) 
	          || NullUtil.isNullOrEmpty(loginRequisicao.getLogin()) 
	          || NullUtil.isNullOrEmpty(loginRequisicao.getSenha()));
	}
	
	@PostMapping("/recuperarSenha")
	public ResponseEntity<ApiResponse<Object>> recuperarSenha(@RequestBody LoginRequisicaoDTO loginRequisicao) {
		if (!isDadosLoginRequisicaoValidos(loginRequisicao)) {
	        return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, "Dados de requisição inválidos.", null));
	    }
		boolean recuperacaoRealizada= loginBO.recuperarSenha(loginRequisicao);
		
		if (recuperacaoRealizada) {
	        return ResponseEntity.ok(new ApiResponse<>(true, false, "Token enviado para o usuário", null));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, true, "Usuário não encontrado", null));
	    }
	}
	
	@PostMapping("/redefinirSenha")
	public ResponseEntity<ApiResponse<Object>> redefinirSenha(@RequestBody LoginRequisicaoDTO loginRequisicao) {
	    if (!isDadosLoginRequisicaoValidos(loginRequisicao)) {
	        return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, "Dados de requisição inválidos.", null));
	    }

	    try {
	        boolean redefinicaoRealizada = loginBO.redefinirSenha(loginRequisicao);
	        if (redefinicaoRealizada) {
	            return ResponseEntity.ok(new ApiResponse<>(true, false, "Senha alterada com sucesso para o usuario" + loginRequisicao.getLogin() + "! Senha padrão de primeiro acesso 'criptocom@1234' ", null));
	        }
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, true, "Token inválido ou expirado.", null));
	    } catch (SenhaException | TokenException e) {
	        return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, e.getMessage(), null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro inesperado ao redefinir senha.", null));
	    }
	}
	
	@PostMapping("/reativarUsuario")
	public ResponseEntity<ApiResponse<Object>> reativarUsuario(@RequestBody LoginRequisicaoDTO loginRequisicao) {
		if(!isDadosLoginRequisicaoValidos(loginRequisicao)) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, "Dados de requisição inválidos.", null));
		}
		
		try {
			if (isDadosLoginRequisicaoValidos(loginRequisicao)) {
				boolean usuarioReativado = loginBO.reativarUsuario(loginRequisicao);
				
				if (usuarioReativado) {
					return ResponseEntity.ok(new ApiResponse<>(true, false, "Usuário '" + loginRequisicao.getLogin() + "' reativado com sucesso! A senha permanece a última senha até que seja solicitado a recuperação/alteração.",null));
				}
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, true, "Login inválido.", null));
		} catch(UsuarioException e) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, true, e.getMessage(), null));
		} catch (Exception e) {
			 e.printStackTrace();
		     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, true, "Erro inesperado ao ativar usuário.", null));
		}
	}
}