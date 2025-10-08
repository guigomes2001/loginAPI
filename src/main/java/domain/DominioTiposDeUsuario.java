package domain;

import dto.RegrasDoUsuarioDTO;

public enum DominioTiposDeUsuario {
	
    NIVEL_ZERO(0, "Nível Zero", true, true, true, true, true, true, true, true, false),
    ADMIN(1, "Administrador", true, true, true, true, false, true, false, true, false),
    COORDENADOR_DE_SEGURANCA(2, "Coordenador de Segurança", false, false, false, false, false, false, true, true, false),
    MODERADOR(3, "Moderador", false, true, true, false, false, false, false, true, false),
    USUARIO_COMUM(4, "Usuário Comum", false, false, false, false, false, false, false, true, false),
    CONVIDADO(5, "Convidado", false, false, false, false, false, false, false, false, true);

    private final int codigo;
    private final String descricao;
    
    private final boolean permiteGerenciarUsuarios;
    private final boolean permiteCriarUsuariosComum;
    private final boolean permiteCriarConvidados;
    private final boolean permiteCriarAdministradores;
    private final boolean permiteCriarNivelZero;
    private final boolean permiteAcessarRelatorios;
    private final boolean permiteAuditarAcessos;
    private final boolean acessoBasico;
    private final boolean acessoRestrito;

    DominioTiposDeUsuario(int codigo, String descricao,
                          boolean permiteGerenciarUsuarios,
                          boolean permiteCriarUsuariosComum,
                          boolean permiteCriarConvidados,
                          boolean permiteCriarAdministradores,
                          boolean permiteCriarNivelZero,
                          boolean permiteAcessarRelatorios,
                          boolean permiteAuditarAcessos,
                          boolean acessoBasico,
                          boolean acessoRestrito) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.permiteGerenciarUsuarios = permiteGerenciarUsuarios;
        this.permiteCriarUsuariosComum = permiteCriarUsuariosComum;
        this.permiteCriarConvidados = permiteCriarConvidados;
        this.permiteCriarAdministradores = permiteCriarAdministradores;
        this.permiteCriarNivelZero = permiteCriarNivelZero;
        this.permiteAcessarRelatorios = permiteAcessarRelatorios;
        this.permiteAuditarAcessos = permiteAuditarAcessos;
        this.acessoBasico = acessoBasico;
        this.acessoRestrito = acessoRestrito;
    }

    public RegrasDoUsuarioDTO definePermissoesDoUsuario() {
        RegrasDoUsuarioDTO regrasDoUsuario = new RegrasDoUsuarioDTO(this.descricao);
        regrasDoUsuario.setPermiteGerenciarUsuarios(permiteGerenciarUsuarios);
        regrasDoUsuario.setPermiteCriarUsuariosComum(permiteCriarUsuariosComum);
        regrasDoUsuario.setPermiteCriarConvidados(permiteCriarConvidados);
        regrasDoUsuario.setPermiteCriarAdministradores(permiteCriarAdministradores);
        regrasDoUsuario.setPermiteCriarNivelZero(permiteCriarNivelZero);
        regrasDoUsuario.setPermiteAcessarRelatorios(permiteAcessarRelatorios);
        regrasDoUsuario.setPermiteAuditarAcessos(permiteAuditarAcessos);
        regrasDoUsuario.setAcessoBasico(acessoBasico);
        regrasDoUsuario.setAcessoRestrito(acessoRestrito);
        return regrasDoUsuario;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static DominioTiposDeUsuario porCodigo(int codigo) {
        for (DominioTiposDeUsuario tipo : values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}