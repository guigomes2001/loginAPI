package domain;

/**
 * @author @gui_gomes_18
 */

public enum DominioRestricaoLogin {
    NENHUMA(0),
    SENHA_INCORRETA(-1),
    USUARIO_INVALIDO(-2),
    USUARIO_EXCLUIDO(-3),
    USUARIO_BLOQUEADO(-4);

    private final int codigo;

    DominioRestricaoLogin(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}