package domain;

public enum DominioMensagensToken {
    TOKEN_INVALIDO_OU_EXPIRADO("Token inválido ou expirado."),
    TOKEN_NAO_INFORMADO("Token não informado."),
    TOKEN_BLOQUEADO("Token está na lista de bloqueados."),
    TOKEN_SESSAO_LONGA_INVALIDO_OU_EXPIRADO("Token de sessão longa inválido ou expirado"),
    TOKEN_DE_RECUPERACAO_INVALIDO("Token de recuperação inválido!");

    private final String mensagem;

    DominioMensagensToken(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}