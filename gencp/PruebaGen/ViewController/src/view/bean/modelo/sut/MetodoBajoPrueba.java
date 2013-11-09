package view.bean.modelo.sut;

import java.util.ArrayList;

import view.bean.modelo.Metodo;

public class MetodoBajoPrueba extends Metodo {
	
	private ArrayList<ParametroBajoPrueba> parBp;
	
	public MetodoBajoPrueba(String nombreMetodo, String visib, ArrayList<ParametroBajoPrueba> par){
		super();
		super.setNombre(nombreMetodo);
		super.setVisib(visib);
		this.parBp = par;
	}
	
	public ArrayList<ParametroBajoPrueba> getParBp(){
		return parBp;
	}
	
	@Override
	public String toString(){
		return super.getNombre().toString();
	}
	
	public boolean tieneLimite(){
		boolean flag = false;
		for(ParametroBajoPrueba pbp : parBp){
			if(pbp.esLimite==true){
				flag = true;
			}
		}
		return flag;
	}
	
	public ParametroBajoPrueba getParLimite(){
		ParametroBajoPrueba p = null;
		for(ParametroBajoPrueba pbp : parBp){
			if(pbp.esLimite==true){
				p = pbp;
			}
		}
		return p;
	}
	

}
