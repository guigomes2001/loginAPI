package domain;

public enum DominioSenhaPadrao {
	SENHA("criptocom@1234");
	
	private String senhaPadrao;
	
	DominioSenhaPadrao(String senhaPadrao) {
		this.senhaPadrao = senhaPadrao;
	}

	public String getSenhaPadrao() {
		return senhaPadrao;
	}
}
