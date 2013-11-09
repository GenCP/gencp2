package view.bean.modelo.sut;

import view.bean.modelo.Parametro;

public class ParametroBajoPrueba extends Parametro {
	
	boolean esLimite;
	
	
	public ParametroBajoPrueba(String nombre, String tda, Object valor, boolean esLimite){
		super();
		super.setNombre(nombre);
		super.setTda(tda);
		super.setValorDef(valor);
		this.esLimite=esLimite;
	}
	
	public boolean esLimite(){
		return esLimite;
	}
	public void setEsLimite(boolean limite){
		esLimite = limite;
	}

}
