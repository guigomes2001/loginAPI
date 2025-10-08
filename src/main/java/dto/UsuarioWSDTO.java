package dto;

import java.util.Date;

public class UsuarioWSDTO {

	private Long chave;
	private String login;
	private String senha;
	private Date dataCadastro;
	private Date dataExclusao;
	private Date dataAlteracao;
	private boolean loginAutenticado;
	private String mensagem;
	private boolean loginBlindado;
	
	private PessoaDTO pessoa;
	private TokenDTO token;
	private SenhaDTO senhaDTO;
	private ResultadoLoginDTO resultadoLogin;
	
	public Long getChave() {
		return chave;
	}
	public void setChave(Long chave) {
		this.chave = chave;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
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
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public PessoaDTO getPessoa() {
		return pessoa;
	}
	public void setPessoa(PessoaDTO pessoa) {
		this.pessoa = pessoa;
	}
	public boolean isLoginAutenticado() {
		return loginAutenticado;
	}
	public void setLoginAutenticado(boolean loginAutenticado) {
		this.loginAutenticado = loginAutenticado;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public TokenDTO getToken() {
		return token;
	}
	public void setToken(TokenDTO token) {
		this.token = token;
	}
	public boolean isLoginBlindado() {
		return loginBlindado;
	}
	public void setLoginBlindado(boolean loginBlindado) {
		this.loginBlindado = loginBlindado;
	}
	public SenhaDTO getSenhaDTO() {
		return senhaDTO;
	}
	public void setSenhaDTO(SenhaDTO senhaDTO) {
		this.senhaDTO = senhaDTO;
	}
	public ResultadoLoginDTO getResultadoLogin() {
		return resultadoLogin;
	}
	public void setResultadoLogin(ResultadoLoginDTO resultadoLogin) {
		this.resultadoLogin = resultadoLogin;
	}
}