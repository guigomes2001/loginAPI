package dto;

import java.util.Date;

/**
 * @author @gui_gomes_18
 */

public class SenhaDTO {
	
    private String senhaDigitada;
    private String senhaHash;
    private String mensagemErro;
    private int qtdTentativasSenhaIncorreta;
    private Date dataBloqueioSenhaIncorreta;
    
    public SenhaDTO() {
    	
    }

    public SenhaDTO(String senhaDigitada) {
        this.senhaDigitada = senhaDigitada;
    }

    public String getSenhaDigitada() {
        return senhaDigitada;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
    
    public int getQtdTentativasSenhaIncorreta() {
		return qtdTentativasSenhaIncorreta;
	}
    
    public void setQtdTentativasSenhaIncorreta(int qtdTentativasSenhaIncorreta) {
		this.qtdTentativasSenhaIncorreta = qtdTentativasSenhaIncorreta;
	}
    
    public Date getDataBloqueioSenhaIncorreta() {
		return dataBloqueioSenhaIncorreta;
	}
    
    public void setDataBloqueioSenhaIncorreta(Date dataBloqueioSenhaIncorreta) {
		this.dataBloqueioSenhaIncorreta = dataBloqueioSenhaIncorreta;
	}
}