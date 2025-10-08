package bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import domain.DominioTiposDeUsuario;
import dto.LoginRequisicaoDTO;
import dto.PessoaDTO;
import dto.UsuarioDTO;
import util.NullUtil;
import util.ObjectUtil;
import util.StringUtil;

/**
 * @author @gui_gomes_18
 */

@Component
public class ValidadorUsuario {
	
	public UsuarioDTO definirRegrasDoUsuario(LoginRequisicaoDTO loginRequisicao, UsuarioDTO usuario) {
		DominioTiposDeUsuario tipoDeUsuario = DominioTiposDeUsuario.porCodigo(loginRequisicao.getCodigoTipoDeUsuario());
		usuario.setRegrasDeUsuarioDTO(tipoDeUsuario.definePermissoesDoUsuario());
		return usuario;
	}
	
	public UsuarioDTO preencherUsuario(LoginRequisicaoDTO loginRequisicao, UsuarioDTO usuario) {
	    UsuarioDTO usuarioDTO = new UsuarioDTO();
	    usuarioDTO.setLogin(loginRequisicao.getLogin());
	    usuarioDTO.setSenha(loginRequisicao.getSenha());
	    usuarioDTO.setCodigoTipoDeUsuario(loginRequisicao.getCodigoTipoDeUsuario());

	    PessoaDTO pessoaDTO = new PessoaDTO();
	    pessoaDTO.setNome(loginRequisicao.getPessoa().getNome());
	    pessoaDTO.setCpf(StringUtil.retirarCaracteresEspeciais(loginRequisicao.getPessoa().getCpf()));
	    if(!NullUtil.isNullOrEmpty(loginRequisicao.getPessoa().getDataDeNascimento())) {
	    	pessoaDTO.setDataDeNascimento(loginRequisicao.getPessoa().getDataDeNascimento());
	    }
	    pessoaDTO.setTelefone(loginRequisicao.getPessoa().getTelefone());
	    pessoaDTO.setEmail(loginRequisicao.getPessoa().getEmail());
	    
	    usuarioDTO.setPessoa(pessoaDTO);
	    return usuarioDTO;
	}

	public List<Integer> validarDadosDaPessoaDoUsuario(PessoaDTO pessoa, PessoaDTO pessoaCapturada) {
	    List<Integer> erros = new ArrayList<>();

	    if (NullUtil.isNullOrEmpty(pessoaCapturada)) {
	        erros.add(-99);
	        return erros;
	    }
	    if (ObjectUtil.notEquals(pessoa.getChave(), pessoaCapturada.getChave())) {
	        erros.add(-1);
	    }
	    if (ObjectUtil.notEquals(pessoa.getNome(), pessoaCapturada.getNome())) {
	        erros.add(-2);
	    }
	    if (ObjectUtil.notEquals(pessoa.getCpf(), pessoaCapturada.getCpf())) {
	        erros.add(-3);
	    }
	    if (ObjectUtil.notEquals(pessoa.getDataDeNascimento(), pessoaCapturada.getDataDeNascimento())) {
	        erros.add(-4);
	    }
	    if (ObjectUtil.notEquals(StringUtil.retirarCaracteresEspeciais(pessoa.getTelefone()), StringUtil.retirarCaracteresEspeciais(pessoaCapturada.getTelefone()))) {
	        erros.add(-5);
	    }
	    if (ObjectUtil.notEquals(pessoa.getEmail(), pessoaCapturada.getEmail())) {
	        erros.add(-6);
	    }
	    return erros;
	}
}
