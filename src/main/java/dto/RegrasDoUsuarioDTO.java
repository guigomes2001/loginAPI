package dto;

public class RegrasDoUsuarioDTO {

	private boolean permiteGerenciarUsuarios;
	private boolean permiteCriarUsuariosComum;
	private boolean permiteCriarConvidados;
	private boolean permiteCriarAdministradores;
	private boolean permiteCriarNivelZero;
	private boolean permiteAcessarRelatorios;
	private boolean permiteAuditarAcessos;
	private boolean acessoBasico;
    private boolean acessoRestrito;
    
    public RegrasDoUsuarioDTO() {
    	
    }
    
	public RegrasDoUsuarioDTO(String valor) {
		
	}

	public boolean isPermiteGerenciarUsuarios() {
		return permiteGerenciarUsuarios;
	}
	public void setPermiteGerenciarUsuarios(boolean permiteGerenciarUsuarios) {
		this.permiteGerenciarUsuarios = permiteGerenciarUsuarios;
	}
	public boolean isPermiteCriarUsuariosComum() {
		return permiteCriarUsuariosComum;
	}
	public void setPermiteCriarUsuariosComum(boolean permiteCriarUsuariosComum) {
		this.permiteCriarUsuariosComum = permiteCriarUsuariosComum;
	}
	public boolean isPermiteCriarConvidados() {
		return permiteCriarConvidados;
	}
	public void setPermiteCriarConvidados(boolean permiteCriarConvidados) {
		this.permiteCriarConvidados = permiteCriarConvidados;
	}
	public boolean isPermiteCriarAdministradores() {
		return permiteCriarAdministradores;
	}
	public void setPermiteCriarAdministradores(boolean permiteCriarAdministradores) {
		this.permiteCriarAdministradores = permiteCriarAdministradores;
	}
	public boolean isPermiteCriarNivelZero() {
		return permiteCriarNivelZero;
	}
	public void setPermiteCriarNivelZero(boolean permiteCriarNivelZero) {
		this.permiteCriarNivelZero = permiteCriarNivelZero;
	}
	public boolean isPermiteAcessarRelatorios() {
		return permiteAcessarRelatorios;
	}
	public void setPermiteAcessarRelatorios(boolean permiteAcessarRelatorios) {
		this.permiteAcessarRelatorios = permiteAcessarRelatorios;
	}
	public boolean isPermiteAuditarAcessos() {
		return permiteAuditarAcessos;
	}
	public void setPermiteAuditarAcessos(boolean permiteAuditarAcessos) {
		this.permiteAuditarAcessos = permiteAuditarAcessos;
	}
	public boolean isAcessoBasico() {
		return acessoBasico;
	}
	public void setAcessoBasico(boolean acessoBasico) {
		this.acessoBasico = acessoBasico;
	}
	public boolean isAcessoRestrito() {
		return acessoRestrito;
	}
	public void setAcessoRestrito(boolean acessoRestrito) {
		this.acessoRestrito = acessoRestrito;
	}
}
