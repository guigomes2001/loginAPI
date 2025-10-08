package dto;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author @gui_gomes_18
 */

public class PessoaDTO {
	
	private Long chave;
	private String nome;
	private String cpf;
	private LocalDate dataDeNascimento;
	private String telefone;
	private String email;
	private String mensagem;
	private Date dataCadastro;
	private Date dataExclusao;
	
	public PessoaDTO() {
		
	}
	
	public PessoaDTO(Long chave, String nome, String cpf, LocalDate dataDeNascimento, String telefone, String email, Date dataCadastro, Date dataExclusao) {
		this.chave = chave;
		this.nome = nome;
		this.cpf = cpf;
		this.dataDeNascimento = dataDeNascimento;
		this.telefone = telefone;
		this.email = email;
		this.dataCadastro = dataCadastro;
		this.dataExclusao = dataExclusao;
	}

	public Long getChave() {
		return chave;
	}

	public void setChave(Long chave) {
		this.chave = chave;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(LocalDate dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}
}
