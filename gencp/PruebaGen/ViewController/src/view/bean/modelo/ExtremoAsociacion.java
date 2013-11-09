package view.bean.modelo;

public class ExtremoAsociacion {
	private String limiteSup;
	private String limiteInf;
	private String nomClase;
	private String esNavegable;
	private String tipoAso;
	
	public String getEsNavegable() {
		return esNavegable;
	}
	public void setEsNavegable(String esNavegable) {
		this.esNavegable = esNavegable;
	}
	public String getTipoAso() {
		return tipoAso;
	}
	public void setTipoAso(String tipoAso) {
		this.tipoAso = tipoAso;
	}
	public String getLimiteSup() {
		return limiteSup;
	}
	public void setLimiteSup(String limiteSup) {
		this.limiteSup = limiteSup;
	}
	public String getLimiteInf() {
		return limiteInf;
	}
	public void setLimiteInf(String limiteInf) {
		this.limiteInf = limiteInf;
	}
	public String getNomClase() {
		return nomClase;
	}
	public void setNomClase(String nomClase) {
		this.nomClase = nomClase;
	}
}
