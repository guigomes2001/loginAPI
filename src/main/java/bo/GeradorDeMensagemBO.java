package bo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import domain.DominioRestricaoLogin;
import dto.PessoaDTO;
import dto.UsuarioDTO;

/**
 * @author @gui_gomes_18
 */

@Component
@Scope("prototype")
public class GeradorDeMensagemBO {

	public void definirMensagemDeRespostaDoAcesso(DominioRestricaoLogin restricao, UsuarioDTO usuario) {
	    usuario.getResultadoLogin().setRestricao(restricao);

	    switch (restricao) {
	        case NENHUMA -> {
	            usuario.getResultadoLogin().setSucesso(true);
	            usuario.getResultadoLogin().setMensagem("Login realizado com sucesso");
	        }
	        case USUARIO_INVALIDO, SENHA_INCORRETA -> {
	            usuario.getResultadoLogin().setSucesso(false);
	            usuario.getResultadoLogin().setMensagem("Login ou senha incorreta");
	        }
	        case USUARIO_EXCLUIDO -> {
	            usuario.getResultadoLogin().setSucesso(false);
	            usuario.getResultadoLogin().setMensagem("Usuário excluído");
	        }
	        case USUARIO_BLOQUEADO -> {
	            usuario.getResultadoLogin().setSucesso(false);
	            usuario.getResultadoLogin().setMensagem("Usuário bloqueado");
	        }
	    }
	}
	
	public String gerarMensagemErro(int i, PessoaDTO pessoa) {
	    switch (i) {
	        case -1:
	            return "Chave da pessoa inválida!";
	        case -2:
	            return "Nome da pessoa inválido!";
	        case -3:
	            return "CPF inválido!";
	        case -4:
	            return "Data de nascimento inválida!";
	        case -5:
	            return "Telefone inválido!";
	        case -6:
	            return "Email inválido!";
	        case -99:
	            return "Pessoa não encontrada!";
	        default:
	            return "Dados da pessoa inválidos!";
	    }
	}
}
