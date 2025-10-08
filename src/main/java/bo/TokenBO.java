package bo;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dao.TokenDAO;
import dao.UsuarioDAO;
import domain.DominioMensagensToken;
import dto.LoginRequisicaoDTO;
import dto.TokenDTO;
import dto.UsuarioDTO;
import exception.TokenException;
import util.JwtUtil;
import util.NullUtil;

@Component
@Scope("prototype")
public class TokenBO {

	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private UsuarioDAO usuarioDAO;
	
    private final SecureRandom secureRandom = new SecureRandom();
    private final Set<String> tokensInvalidos = ConcurrentHashMap.newKeySet();
    @Autowired
    private JwtUtil jwtUtil;

    private TokenDTO tokenDTO;
    private LoginRequisicaoDTO loginRequisicaoDTO;

    private static final long TEMPO_EXPIRACAO_RECUPERACAO_SENHA_MINUTOS = 5;
    private static final long TEMPO_EXPIRACAO_SESSAO_MINUTOS = 60;

    public String gerarTokenRecuperacaoSenha(Long chaveUsuario) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(TEMPO_EXPIRACAO_RECUPERACAO_SENHA_MINUTOS);
        tokenDAO.cadastrarTokenRecuperacaoSenha(chaveUsuario, token, expiracao);
        return token;
    }

    public boolean isTokenRecuperacaoSenhaValido(String token) {
        try {
            UsuarioDTO usuario = tokenDAO.capturarUsuarioPorTokenRecuperacaoSenha(token);
            if (NullUtil.isNullOrEmpty(usuario)
                    || NullUtil.isNullOrEmpty(usuario.getToken())
                    || NullUtil.isNullOrEmpty(usuario.getToken().getTokenRecuperacaoDeSenha())
                    || NullUtil.isNullOrEmpty(usuario.getToken().getValidadeTokenRecuperacaoDeSenha())) {
                return false;
            }
            return usuario.getToken().getValidadeTokenRecuperacaoDeSenha().isAfter(LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UsuarioDTO capturarUsuarioPorTokenRecuperacaoSenha(String token) {
    	UsuarioDTO usuario = new UsuarioDTO();
        try {
        	if (!isTokenRecuperacaoSenhaValido(token)) {
        		throw new RuntimeException("Token de recuperalçao de senha inválido");
        	}
        	usuario = tokenDAO.capturarUsuarioPorTokenRecuperacaoSenha(token);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return usuario;
    }

    public void invalidarToken(Long chaveUsuario) {
    	try {
			tokenDAO.expirarTokenRecuperacaoDeSenha(chaveUsuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public String gerarTokenSessao(Long chaveUsuario) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(TEMPO_EXPIRACAO_SESSAO_MINUTOS);
        try {
			tokenDAO.cadastrarTokenSessao(chaveUsuario, token, expiracao);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return token;
    }
    
    public UsuarioDTO capturarUsuarioPorTokenSessao(String token) {
        if (!validarTokenSessao(token)) {
            return null;
        }
        return tokenDAO.capturarUsuarioPorTokenSessao(token);
    }

    public boolean validarTokenSessao(String token) {
        UsuarioDTO usuario = tokenDAO.capturarUsuarioPorTokenSessao(token);
        if (usuario == null || usuario.getToken().getValidadeTokenSessao() == null) {
            return false;
        }
        return usuario.getToken().getValidadeTokenSessao().isAfter(LocalDateTime.now());
    }

    public void invalidarTokenSessao(Long chaveUsuario) {
        try {
			tokenDAO.expirarTokenSessao(chaveUsuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void invalidarTokenSessaoLonga(Long chaveUsuario) {
        try {
			tokenDAO.excluirTokenDeSessaoLonga(chaveUsuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	public Boolean isExisteTokenDeSessaoLongaValido(UsuarioDTO usuario) {
		Boolean i = Boolean.FALSE;
		try {
			i = tokenDAO.isExisteTokenDeSessaoLongaValido(usuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	public String capturarTokenDeSessaoLongaDoUsuarioAindaValido(UsuarioDTO usuario) {
		String tokenDeSessaoLongaDoUsuario = "";
		try {
			tokenDeSessaoLongaDoUsuario = tokenDAO.capturarTokenDeSessaoLongaDoUsuarioAindaValido(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenDeSessaoLongaDoUsuario;
	}
	
	public boolean isTokenDeSessaoLongaDoUsuarioAindaValido(UsuarioDTO usuario) {
		boolean isTokenAindaValido = false;
		try {
			isTokenAindaValido = tokenDAO.isExisteTokenDeSessaoLongaValido(usuario);
			if(isTokenAindaValido) {
				isTokenAindaValido = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isTokenAindaValido;
	}

	public int excluirTokenDeSessao(Long chaveUsuario) {
		int i = 0;
		try {
			i = tokenDAO.excluirTokenDeSessaoLonga(chaveUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public void adicionarTokenNaListaDeTokenInvalidos(String token) {
		tokensInvalidos.add(token);
	}
	
	public boolean isTokenNaListaDeInvalidos(String token) {
		return tokensInvalidos.contains(token);
	}
	
	public boolean validarToken(TokenDTO token) {
		setTokenDTO(token);
		return validarToken();
	}

	private boolean validarToken() {
		Boolean tokenSessaoValido = Boolean.FALSE;
		try {
			if (NullUtil.isNullOrEmpty(getTokenDTO()) || NullUtil.isNullOrEmpty(getTokenDTO().getTokenSessao())) {
				throw new TokenException(DominioMensagensToken.TOKEN_NAO_INFORMADO.getMensagem());
			}
			
			jwtUtil.validarToken(getTokenDTO().getTokenSessao());
			if(!isTokenNaListaDeInvalidos(getTokenDTO().getTokenSessao())) {
				tokenSessaoValido = Boolean.TRUE;
			}
		} catch (TokenException e) {
			throw new TokenException(DominioMensagensToken.TOKEN_INVALIDO_OU_EXPIRADO.getMensagem());
		}
		return tokenSessaoValido;
	}

	public boolean reenviarTokenRecuperacaoDeSenha(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		return reenviarTokenRecuperacaoDeSenha();
	}
	
	private boolean reenviarTokenRecuperacaoDeSenha() {
		UsuarioDTO usuario = new UsuarioDTO();
		Boolean tokenReenviado = Boolean.FALSE;
		
		try {
			if(isDadosLoginDeRequisicaoValidos(getLoginRequisicao())) {
				usuario = usuarioDAO.capturarUsuarioPorLogin(getLoginRequisicao().getLogin());
				
				if(isUsuarioValido(usuario)) {
					TokenDTO tokenAtual = tokenDAO.capturarUsuarioPorTokenRecuperacaoPorChaveUsuario(usuario);
					if(isTokenRecuperacaoDeSenhaValido(usuario)) {
						enviarEmailRecuperacaoDeSenha(usuario, tokenAtual.getTokenRecuperacaoDeSenha());
						tokenReenviado = Boolean.TRUE;
					} else {
						 String novoToken = gerarTokenRecuperacaoSenha(usuario.getChave());
						 enviarEmailRecuperacaoDeSenha(usuario, novoToken);
						 tokenReenviado = Boolean.TRUE;
					}
				}
			}
		} catch (Exception e) {
			throw new TokenException("");
		}
		return tokenReenviado;
	}
	
	private boolean isDadosLoginDeRequisicaoValidos(LoginRequisicaoDTO loginRequisicao) {
		return !NullUtil.isNullOrEmpty(loginRequisicao)
				&& !NullUtil.isNullOrEmpty(loginRequisicao.getLogin())
				  && !NullUtil.isNullOrEmpty(loginRequisicao.getSenha());
	}
	
	private boolean isUsuarioValido(UsuarioDTO usuario) {
		return !NullUtil.isNullOrEmpty(usuario)
				&& !NullUtil.isNullOrEmpty(usuario.getChave())
				  && !NullUtil.isNullOrEmpty(usuario.getLogin());
	}
	
	private boolean isTokenRecuperacaoDeSenhaValido(UsuarioDTO usuario) {
		return !NullUtil.isNullOrEmpty(usuario.getToken()) 
				&& !NullUtil.isNullOrEmpty(usuario.getToken().getTokenRecuperacaoDeSenha())
				  &&  !NullUtil.isNullOrEmpty(usuario.getToken().getValidadeTokenRecuperacaoDeSenha())
				  	&& usuario.getToken().getValidadeTokenRecuperacaoDeSenha().isAfter(LocalDateTime.now());
	}
	
	private void enviarEmailRecuperacaoDeSenha(UsuarioDTO usuario, String tokenRecuperacaoDeSenha) {
		
	}

	private TokenDTO getTokenDTO() {
		if (tokenDTO == null) {
			tokenDTO = new TokenDTO();
		}
		return tokenDTO;
	}
	
	private void setTokenDTO(TokenDTO tokenDTO) {
		this.tokenDTO = tokenDTO;
	}
	
	private LoginRequisicaoDTO getLoginRequisicao() {
		if (loginRequisicaoDTO== null) {
			loginRequisicaoDTO = new LoginRequisicaoDTO();
		}
		return loginRequisicaoDTO;
	}
	
	private void setLoginRequisicao(LoginRequisicaoDTO loginRequisicaoDTO) {
		this.loginRequisicaoDTO = loginRequisicaoDTO;
	}
}
