package bo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import dao.PessoaDAO;
import dao.TokenDAO;
import dao.UsuarioDAO;
import domain.DominioMensagensPerfil;
import domain.DominioMensagensPessoa;
import domain.DominioMensagensSenha;
import domain.DominioMensagensToken;
import domain.DominioMensagensUsuario;
import domain.DominioRegrasDeSenha;
import domain.DominioRestricaoLogin;
import domain.DominioSenhaPadrao;
import domain.DominioTiposDeUsuario;
import dto.LoginRequisicaoDTO;
import dto.PessoaDTO;
import dto.UsuarioDTO;
import exception.PerfilException;
import exception.PessoaException;
import exception.SenhaException;
import exception.TokenException;
import exception.UsuarioException;
import util.HashUtil;
import util.JwtUtil;
import util.NullUtil;
import util.StringUtil;

/**
 * @author @gui_gomes_18
 */

@Component
@Scope("prototype")
public class LoginBO {
	
	private UsuarioDTO usuario;
	private LoginRequisicaoDTO loginRequisicao;
	
	@Autowired
	private TokenBO tokenBO;
	@Autowired
	private GeradorDeMensagemBO geradorDeMensagemBO;
	@Autowired
	private NotificacaoBO notificacaoBO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	@Autowired
	private PessoaDAO pessoaDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private JwtUtil jwtUtil;
	
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private ValidadorUsuario validadorUsuario;
	
	public LoginBO() {
		
	}
	
	public LoginBO(JwtUtil jwtUtil) {
	    this.jwtUtil = jwtUtil;
	}
	
	public UsuarioDTO efetuarLoginUsuario(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		return efetuarLoginUsuario();
	}
	
	private UsuarioDTO efetuarLoginUsuario() {
	    UsuarioDTO usuario = new UsuarioDTO();
	    try {
	        usuario = definirUsuario();
	        
	        if(!NullUtil.isNullOrEmpty(usuario) && !NullUtil.isNullOrEmpty(usuario.getPessoa().getChave())) {
	        	definirPessoaDoUsuario(usuario);
	        }
	        DominioRestricaoLogin restricao = verificarRestricao(usuario);
	        
	        if (restricao == DominioRestricaoLogin.NENHUMA) {
	            restricao = validarAcessoUsuario(usuario);
	        }

	        geradorDeMensagemBO.definirMensagemDeRespostaDoAcesso(restricao, usuario);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return usuario;
	}

	private UsuarioDTO definirUsuario() {
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			usuario = usuarioDAO.capturarUsuarioPorLogin(getLoginRequisicao().getLogin());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		definirTipoDoUsuario(usuario);
		definirRegrasDoUsuario(usuario);
		return usuario;
	}

	private void definirTipoDoUsuario(UsuarioDTO usuario) {
		DominioTiposDeUsuario tipoDeUsuario = DominioTiposDeUsuario.porCodigo(usuario.getCodigoTipoDeUsuario());
	    usuario.setDescricaoTipoDeUsuario(tipoDeUsuario.getDescricao());
	}
	

	private void definirRegrasDoUsuario(UsuarioDTO usuario) {
		DominioTiposDeUsuario tipoDeUsuario = DominioTiposDeUsuario.porCodigo(usuario.getCodigoTipoDeUsuario());
		usuario.setRegrasDeUsuarioDTO(tipoDeUsuario.definePermissoesDoUsuario());
	}

	private void definirPessoaDoUsuario(UsuarioDTO usuario) {
		PessoaDTO pessoa = new PessoaDTO();
		pessoa = pessoaDAO.capturarPessoaPorChave(usuario.getPessoa().getChave());
		usuario.setPessoa(pessoa);
	}

	private DominioRestricaoLogin verificarRestricao(UsuarioDTO usuario) {
	    if (NullUtil.isNullOrEmpty(usuario) || NullUtil.isNullOrEmpty(usuario.getLogin())) {
	        return DominioRestricaoLogin.USUARIO_INVALIDO;
	    }
	    if (!NullUtil.isNullOrEmpty(usuario.getDataExclusao())) {
	        return DominioRestricaoLogin.USUARIO_EXCLUIDO;
	    }
	    if (!NullUtil.isNullOrEmpty(usuario.getSenhaDTO()) 
	    		&& !NullUtil.isNullOrEmpty(usuario.getSenhaDTO().getDataBloqueioSenhaIncorreta())) {
	        return DominioRestricaoLogin.USUARIO_BLOQUEADO;
	    }
	    return DominioRestricaoLogin.NENHUMA;
	}

	public void efetuarLogoutUsuario(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		validarTokenParaLogout();
	}
	
	private void validarTokenParaLogout() {
	    try {
	        jwtUtil.validarToken(getLoginRequisicao().getToken().getTokenSessao());
	    } catch (Exception e) {
	        throw new RuntimeException(DominioMensagensToken.TOKEN_INVALIDO_OU_EXPIRADO.getMensagem());
	    }
	}
	
	private DominioRestricaoLogin validarAcessoUsuario(UsuarioDTO usuario) {
		boolean senhaCorreta = passwordEncoder.matches(getLoginRequisicao().getSenha(), usuario.getSenha());
		boolean primeiroAcessoComSenhaPadrao = passwordEncoder.matches(DominioSenhaPadrao.SENHA.getSenhaPadrao(), usuario.getSenha())
											   && getLoginRequisicao().getSenha().equals(DominioSenhaPadrao.SENHA.getSenhaPadrao());
		
	    if (senhaCorreta || primeiroAcessoComSenhaPadrao) {
	        definirAutenticacaoETokenSessao(usuario);
	        try {
	        	usuarioDAO.resetarQuantidadeSenhasIncorretas(usuario);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        return DominioRestricaoLogin.NENHUMA;
	    } else {
	        verificaQuantidadeSenhasIncorretas(usuario);
	        throw new SenhaException("Login ou senha inválidos.");
	    }
	}

	private void definirAutenticacaoETokenSessao(UsuarioDTO usuario) {
		usuario.setLoginAutenticado(true);
		gerarToken(usuario);
	}

	private void gerarToken(UsuarioDTO usuario) {
		usuario.getToken().setTokenSessao(jwtUtil.gerarTokenSessao(usuario.getChave(), usuario.getLogin(), usuario.getDescricaoTipoDeUsuario()));
		usuario.getToken().setTokenDeSessaoLonga(jwtUtil.gerarTokenDeSessaoLonga(usuario.getChave(), usuario.getLogin(), usuario.getDescricaoTipoDeUsuario()));
		
		if(!NullUtil.isNullOrEmpty(usuario.getToken().getTokenDeSessaoLonga())) {
			try {
				Boolean tokenSessaoLongaValidoJaCadastrado = tokenBO.isExisteTokenDeSessaoLongaValido(usuario);
				if(!tokenSessaoLongaValidoJaCadastrado) {
					LocalDateTime expira = LocalDateTime.now().plusDays(7);
					tokenDAO.cadastrarTokenSessaoLonga(usuario.getChave(), usuario.getToken().getTokenDeSessaoLonga(), expira);
				} else {
					usuario.getToken().setTokenDeSessaoLonga(tokenBO.capturarTokenDeSessaoLongaDoUsuarioAindaValido(usuario));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void verificaQuantidadeSenhasIncorretas(UsuarioDTO usuario) {
		int tentativasIncorretas = 0;
		
		try {
			usuarioDAO.atualizarQuantidadeSenhasIncorretas(getLoginRequisicao());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tentativasIncorretas = usuarioDAO.capturarQuantidadeSenhaIncorreta(usuario);
		
		if(tentativasIncorretas >= DominioRegrasDeSenha.LIMITE_TENTATIVAS_SENHA_INCORRETA) {
			try {
				usuarioDAO.bloquearUsuario(getLoginRequisicao());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new SenhaException("Usuário bloqueado após " + DominioRegrasDeSenha.LIMITE_TENTATIVAS_SENHA_INCORRETA + " tentativas incorretas.");
		}
	}
	
	public UsuarioDTO cadastrarUsuario(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		return cadastrarUsuario();
	}
	
	private UsuarioDTO cadastrarUsuario() {
		UsuarioDTO usuario = new UsuarioDTO();
		PessoaDTO pessoaRegistrada = new PessoaDTO();
	    try {
	    	int i = 0;
	        if (isLoginValidoParaCadastro(getLoginRequisicao())) {
	        	usuario = validadorUsuario.preencherUsuario(getLoginRequisicao(), usuario);
	        	usuario = validadorUsuario.definirRegrasDoUsuario(getLoginRequisicao(), usuario);
	        	
	            i = pessoaDAO.cadastrarPessoa(usuario.getPessoa());
	            if(i > 0) {
	            	
	            	pessoaRegistrada = pessoaDAO.capturarPessoaPorCPF(usuario.getPessoa().getCpf());
		          
	            	usuario.getPessoa().setChave(pessoaRegistrada.getChave());
	            	usuario.setSenha(gerarHashNovaSenha(DominioSenhaPadrao.SENHA.getSenhaPadrao()));
		            
		            usuarioDAO.cadastrarUsuario(usuario);
	
		            usuario = usuarioDAO.capturarUsuarioPorLogin(usuario.getLogin());
	            }
	        } else {
	        	throw new UsuarioException(DominioMensagensUsuario.ERRO_AO_CADASTRAR_O_USUARIO.getMensagem());
	        }
	    } catch(UsuarioException | PessoaException | PerfilException e) {
	    	throw e;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return usuario;
	}

	private boolean isLoginValidoParaCadastro(LoginRequisicaoDTO loginRequisicao) throws Exception {
		return !isLoginUsuarioExiste(loginRequisicao.getLogin()) 
					&& !possuiUsuarioVinculado(loginRequisicao.getPessoa().getCpf())
						&& isDadosPessoaDoUsuarioValidos(loginRequisicao.getPessoa())
							&& isTipoDePerfilDoLoginValido(loginRequisicao);
	}

	private boolean isTipoDePerfilDoLoginValido(LoginRequisicaoDTO loginRequisicao) {
		if (loginRequisicao.getCodigoTipoDeUsuario() == DominioTiposDeUsuario.NIVEL_ZERO.getCodigo()) {
			throw new PerfilException(DominioMensagensPerfil.NAO_E_POSSIVEL_CADASTRAR_USUARIO_DESSE_TIPO.getMensagem());
		}
		return true;
	}

	private boolean possuiUsuarioVinculado(String cpf) throws Exception {
	    PessoaDTO pessoa = pessoaDAO.capturarPessoaPorCPF(StringUtil.retirarCaracteresEspeciais(cpf));
	     if (!NullUtil.isNullOrEmpty(pessoa) && !NullUtil.isNullOrEmpty(pessoa.getChave())) {
	        UsuarioDTO usuario = usuarioDAO.capturarUsuarioPorChavePessoa(pessoa.getChave());
	        
	        if (!NullUtil.isNullOrEmpty(usuario) && !NullUtil.isNullOrEmpty(usuario.getChave())) {
	            return true;
	        } else if (NullUtil.isNullOrEmpty(usuario.getChave())) {
	            return false;
	        } else {
	            throw new PessoaException(DominioMensagensPessoa.JA_EXISTE_USUARIO_VINCULADO_A_PESSOA.getMensagem());
	        }
	    } else {
	        return false; 
	    }
	}
	
	private boolean isDadosPessoaDoUsuarioValidos(PessoaDTO pessoa) {
		return !NullUtil.isNullOrEmpty(pessoa)
				&& !NullUtil.isNullOrEmpty(pessoa.getCpf())
					&& !NullUtil.isNullOrEmpty(pessoa.getNome());
	}

	private boolean isLoginUsuarioExiste(String login) {
		int i = usuarioDAO.isLoginUsuarioExiste(login);
		if(i > 0) {
			return true;
		}
		return false;
	}
	
	public boolean excluirLogin(Long chave) throws Exception {
		try {
			int i = 0;
			i = usuarioDAO.excluirUsuario(chave);
			if(i > 0) {
				i = tokenBO.excluirTokenDeSessao(chave);
				return true;
			}
		} catch (UsuarioException e) {
			e.printStackTrace();
			throw new UsuarioException(DominioMensagensUsuario.NAO_FOI_POSSIVEL_EXCLUIR_O_USUARIO.getMensagem());
		}
		return false;
	}
	
	public void alterarSenha(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		alterarSenha();
	}
	
	private void alterarSenha() {
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			if(isDadosLoginDeRequisicaoValidos()) {
				if(isUsuarioExistente(getLoginRequisicao().getLogin())) {
					usuario = usuarioDAO.capturarUsuarioPorLogin(getLoginRequisicao().getLogin());
					
					validarDadosDaPessoaDoUsuario();
					validarSenhaAntiga(usuario);
					
					String hashNovaSenha = gerarHashNovaSenha(getLoginRequisicao().getSenha());
					atualizarSenhaDoUsuario(usuario, hashNovaSenha);
					expirarSessaoUsuario(getLoginRequisicao());
					enviarNotificacaoAlteracaoSenha(usuario);
				}
			}
		} catch(SenhaException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SenhaException(DominioMensagensSenha.NAO_FOI_POSSIVEL_ALTERAR_SENHA.getMensagem());
		}
	}

	private void atualizarSenhaDoUsuario(UsuarioDTO usuario, String hashNovaSenha) throws SQLException {
		if(isUsuarioValido(usuario) && isEmRedefinicaoParaSenhaPadrao(usuario)) {
			usuarioDAO.atualizarSenhaUsuario(usuario, hashNovaSenha);
			return;
		}
		
		if(isUsuarioValido(usuario)) {
			if(isNovaSenhaIgualASenhaAtual(usuario)) {
				throw new SenhaException(DominioMensagensSenha.SENHA_DEVE_SER_DIFERENTE_DA_ATUAL.getMensagem());
			}
			if(!isSenhaForte(getLoginRequisicao().getSenha())) {
				throw new SenhaException(DominioMensagensSenha.SENHA_FORTE.getMensagem());
			}
			usuarioDAO.atualizarSenhaUsuario(usuario, hashNovaSenha);
		}
	}

	private boolean isNovaSenhaIgualASenhaAtual(UsuarioDTO usuario) {
	    return HashUtil.verificarHash(getLoginRequisicao().getSenha(), usuario.getSenha());
	}

	private boolean isUsuarioValido(UsuarioDTO usuario) {
		return !NullUtil.isNullOrEmpty(usuario)
				&& !NullUtil.isNullOrEmpty(usuario.getChave())
				  && !NullUtil.isNullOrEmpty(usuario.getLogin());
	}

	private boolean isDadosLoginDeRequisicaoValidos() {
		return !NullUtil.isNullOrEmpty(getLoginRequisicao())
				&& !NullUtil.isNullOrEmpty(getLoginRequisicao().getLogin())
				  && !NullUtil.isNullOrEmpty(getLoginRequisicao().getSenha());
	}
	
	private boolean isDadosDoUsuarioValidos(UsuarioDTO usuario) {
		return !NullUtil.isNullOrEmpty(usuario)
				&& !NullUtil.isNullOrEmpty(usuario.getLogin())
				  && !NullUtil.isNullOrEmpty(usuario.getSenha());
	}

	private void validarDadosDaPessoaDoUsuario() throws Exception {
	    int i = isExistePessoaRegistrada(getLoginRequisicao().getPessoa());

	    if (i > 0) {
	        PessoaDTO pessoaCapturada = pessoaDAO.capturarPessoaPorCPF(getLoginRequisicao().getPessoa().getCpf());

	        if (!NullUtil.isNullOrEmpty(pessoaCapturada)) {
	            List<Integer> erros = validadorUsuario.validarDadosDaPessoaDoUsuario(getLoginRequisicao().getPessoa(), pessoaCapturada);

	            if (!erros.isEmpty()) {
	                List<String> mensagensErro = erros.stream().map(codigo -> geradorDeMensagemBO.gerarMensagemErro(codigo, pessoaCapturada)).toList();
	                throw new SenhaException(String.join(", ", mensagensErro));
	            }
	        }
	    } else {
	    	throw new PessoaException(DominioMensagensPessoa.DADOS_INVALIDOS.getMensagem());
	    }
	}

	private int isExistePessoaRegistrada(PessoaDTO pessoa) {
		int i = 0;
		try {
			i = pessoaDAO.isExistePessoaRegistrada(pessoa);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	private boolean isUsuarioExistente(String login) {
		int i = usuarioDAO.isLoginUsuarioExiste(login);
		if(i > 0) {
			return true;
		}
		return false;
	}
	
	private String gerarHashNovaSenha(String novaSenha) {
		String hashNovaSenha = passwordEncoder.encode(novaSenha);
		return hashNovaSenha;
	}
	
	public boolean recuperarSenha(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		return recuperarSenha();
	}

	private boolean recuperarSenha() {
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			if (isUsuarioExistente(getLoginRequisicao().getLogin())) {
				usuario = usuarioDAO.capturarUsuarioPorLogin(getLoginRequisicao().getLogin());
				gerarTokenRecuperacao(usuario);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void gerarTokenRecuperacao(UsuarioDTO usuario) {
	    if(isDadosDoUsuarioValidos(usuario)) {
	        String token = tokenBO.gerarTokenRecuperacaoSenha(usuario.getChave());
	        System.out.println("Token de recuperação: " + token);
	        enviarEmailRecuperacaoDeSenha(usuario, token);
	    }
	}
	
	public boolean redefinirSenha(LoginRequisicaoDTO loginRequisicao) throws Exception {
		setLoginRequisicao(loginRequisicao);
		return redefinirSenha();
	}

	private boolean redefinirSenha() throws Exception {
	    UsuarioDTO usuario = new UsuarioDTO();
	    boolean senhaRedefinida = false;
	    
	    if(!NullUtil.isNullOrEmpty(getLoginRequisicao().getToken()) && !NullUtil.isNullOrEmpty(getLoginRequisicao().getToken().getTokenRecuperacaoDeSenha())) {
	    	usuario = tokenDAO.capturarUsuarioPorTokenRecuperacaoSenha(getLoginRequisicao().getToken().getTokenRecuperacaoDeSenha());
	    	if(isDadosDoUsuarioValidos(usuario) && isTokenRecuperacaoDeSenhaValido(usuario)) {
	    		usuario.setEmRedefinicaoDeSenha(true);
	    		definirSenhaPadraoParaOUsuario(usuario);
	    		tokenDAO.expirarTokenRecuperacaoDeSenha(usuario.getToken().getChave());
	    		senhaRedefinida = true;
	    	}
	    } else {
	    	throw new TokenException(DominioMensagensToken.TOKEN_DE_RECUPERACAO_INVALIDO.getMensagem());
	    }
	    return senhaRedefinida;
	}

	private boolean isTokenRecuperacaoDeSenhaValido(UsuarioDTO usuario) {
		return !NullUtil.isNullOrEmpty(usuario.getToken()) 
				&& !NullUtil.isNullOrEmpty(usuario.getToken().getTokenRecuperacaoDeSenha())
				  &&  !NullUtil.isNullOrEmpty(usuario.getToken().getValidadeTokenRecuperacaoDeSenha())
				  	&& usuario.getToken().getValidadeTokenRecuperacaoDeSenha().isAfter(LocalDateTime.now());
	}
	
	private boolean isSenhaForte(String senha) {
	    if (senha == null) return false;
	    if (senha.length() < 8) return false;
	    if (!senha.matches(".*[A-Z].*")) return false;
	    if (!senha.matches(".*[a-z].*")) return false;
	    if (!senha.matches(".*[0-9].*")) return false;
	    if (!senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) return false;
	    return true;
	}
	
	public UsuarioDTO validarTokenSessaoLonga(UsuarioDTO usuario) {
		setUsuarioDTO(usuario);
		return validarTokenSessaoLonga();
	}
	
	private UsuarioDTO validarTokenSessaoLonga() {
		UsuarioDTO usuario = new UsuarioDTO();
		try {
			if (isTokenSessaoLongaValido(getUsuarioDTO())) {
				try {
					usuario = usuarioDAO.capturarUsuarioPorChave(getUsuarioDTO().getChave());
					if (!NullUtil.isNullOrEmpty(usuario) && !NullUtil.isNullOrEmpty(usuario.getLogin())) {
						definirPessoaDoUsuario(usuario);
						definirAutenticacaoETokenSessao(usuario);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(DominioMensagensToken.TOKEN_SESSAO_LONGA_INVALIDO_OU_EXPIRADO.getMensagem());
		}
		return usuario;
	}

	private boolean isTokenSessaoLongaValido(UsuarioDTO usuario) {
		boolean isTokenValido = false;
	    try {
	    	isTokenValido = tokenBO.isExisteTokenDeSessaoLongaValido(usuario);
	    	if(isTokenValido) {
	    		isTokenValido = true;
	    	}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return isTokenValido;
	}
	
	private void expirarSessaoUsuario(LoginRequisicaoDTO loginRequisicao) {
	    try {
	        String tokenSessaoAtual = loginRequisicao.getToken().getTokenSessao();
	        
	        if (!NullUtil.isNullOrEmpty(loginRequisicao.getToken()) && !NullUtil.isNullOrEmpty(tokenSessaoAtual)) {
	            tokenBO.adicionarTokenNaListaDeTokenInvalidos(tokenSessaoAtual);
	            expirarTokenDeSessaoDoUsuario(loginRequisicao);
	        }

	        System.out.println("Sessão/token do usuário foi invalidado com sucesso.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("Falha ao invalidar sessão/token do usuário.");
	    }
	}

	private void expirarTokenDeSessaoDoUsuario(LoginRequisicaoDTO loginRequisicao) throws Exception {
		UsuarioDTO usuario = usuarioDAO.capturarUsuarioPorLogin(loginRequisicao.getLogin());
		Boolean existeTokenSessaoLongaValido = tokenBO.isExisteTokenDeSessaoLongaValido(usuario);
		
		if(existeTokenSessaoLongaValido) {
			tokenBO.invalidarTokenSessaoLonga(usuario.getChave());
		}
	}

	private void validarSenhaAntiga(UsuarioDTO usuario) {
		if (!HashUtil.verificarHash(getLoginRequisicao().getSenhaAntiga(), usuario.getSenha())) {
	        throw new SenhaException(DominioMensagensSenha.SENHA_ATUAL_INCORRETA.getMensagem());
	    }
	}
	
	private void enviarNotificacaoAlteracaoSenha(UsuarioDTO usuario) {
	    try {
	        if (NullUtil.isNullOrEmpty(usuario) || NullUtil.isNullOrEmpty(usuario.getPessoa().getEmail())) {
	            System.err.println("Usuário ou e-mail não encontrado. Notificação não enviada.");
	            return;
	        }

	        String assunto = "🔒 Alteração de Senha Realizada";
	        String mensagem = String.format("""
	                Olá %s,

	                Informamos que sua senha foi alterada com sucesso em %s.

	                Caso não tenha sido você, recomendamos redefinir a senha imediatamente e entrar em contato com o suporte.

	                Atenciosamente,
	                Equipe de Segurança
	                """, 
	                usuario.getPessoa().getNome(), 
	                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(java.time.LocalDateTime.now())
	        );

	        notificacaoBO.enviarEmail(usuario.getPessoa().getEmail(), assunto, mensagem);
	        System.out.println("Notificação de alteração de senha enviada com sucesso para " + usuario.getPessoa().getEmail());
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("Falha ao enviar notificação de alteração de senha.");
	    }
	}
	
	private void enviarEmailRecuperacaoDeSenha(UsuarioDTO usuario, String token) {
	    String assunto = "🔑 Recuperação de Senha";
	    String mensagem = String.format("""
	        Olá %s,

	        Recebemos uma solicitação para redefinir sua senha.
	        Clique no link abaixo para continuar. Ele é válido por 5 minutos:

	        %s

	        Se você não fez essa solicitação, ignore este e-mail.

	        Equipe de Segurança
	    """, usuario.getPessoa().getNome(),
	       "https://criptocom.com/redefinir-senha?token=" + token
	    );

	    notificacaoBO.enviarEmail(usuario.getPessoa().getEmail(), assunto, mensagem);
	}
	

	private void definirSenhaPadraoParaOUsuario(UsuarioDTO usuario) throws Exception {
		try {
			String senhaPadrao = DominioSenhaPadrao.SENHA.getSenhaPadrao();
			senhaPadrao = gerarHashNovaSenha(senhaPadrao);
			atualizarSenhaDoUsuario(usuario, senhaPadrao);
		} catch (SenhaException e) {
			throw new SenhaException(DominioMensagensSenha.ERRO_AO_DEFINIR_SENHA_PADRAO.getMensagem());
		}
	}
	
	private boolean isEmRedefinicaoParaSenhaPadrao(UsuarioDTO usuario) {
		return !NullUtil.isNullOrEmpty(usuario) && usuario.isEmRedefinicaoDeSenha();
	}
	
	public boolean bloquearUsuario(Long chaveUsuario) throws Exception {
		boolean usuarioBloqueado = false;
		int i = 0;
		try {
			i = usuarioDAO.bloquearUsuarioPorChave(chaveUsuario);
			if(i > 0) {
				i = tokenBO.excluirTokenDeSessao(chaveUsuario);
				usuarioBloqueado = true;
			}
		} catch (UsuarioException e) {
			throw new UsuarioException(DominioMensagensUsuario.NAO_FOI_POSSIVEL_BLOQUEAR_O_USUARIO.getMensagem());
		}
		return usuarioBloqueado;
	}
	
	public boolean reativarUsuario(LoginRequisicaoDTO loginRequisicao) {
		setLoginRequisicao(loginRequisicao);
		return reativarUsuario();
	}
	
	private boolean reativarUsuario() {
		UsuarioDTO usuario = new UsuarioDTO();
		boolean usuarioReativado = false;
		
		try {
			if(isUsuarioExistente(getLoginRequisicao().getLogin())) {
				usuario = usuarioDAO.capturarUsuarioInativo(getLoginRequisicao().getLogin());
				
				if(isUsuarioValido(usuario) && !NullUtil.isNullOrEmpty(usuario.getDataExclusao())) {
					definirReativacaoUsuario(usuario);
					usuarioReativado = true;
				} else {
					throw new UsuarioException("Não é possível reativar o usuário! Usuário já está ativo.");
				}
			}
		} catch(UsuarioException e) { 
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
	        throw new UsuarioException("Erro inesperado ao reativar usuário.");
		}
		return usuarioReativado;
	}

	private void definirReativacaoUsuario(UsuarioDTO usuario) throws Exception {
		usuarioDAO.reativarUsuario(usuario);
	}

	private LoginRequisicaoDTO getLoginRequisicao() {
		if (loginRequisicao == null) {
			loginRequisicao = new LoginRequisicaoDTO();
		}
		return loginRequisicao;
	}
	
	private void setLoginRequisicao(LoginRequisicaoDTO loginRequisicao) {
		this.loginRequisicao = loginRequisicao;
	}
	
	private UsuarioDTO getUsuarioDTO() {
		if (usuario == null) {
			usuario = new UsuarioDTO();
		}
		return usuario;
	}
	
	private void setUsuarioDTO(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
}

