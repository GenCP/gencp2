package view.bean.modelo;

import java.util.ArrayList;

public class Asociacion {
	
	private String nombreAso;
	private ArrayList<ExtremoAsociacion> extremos = new ArrayList<ExtremoAsociacion>();
	
	public void setNombre(String n){
		nombreAso=n;
	}
	
	public void setExtremo(ExtremoAsociacion ea){
		extremos.add(ea);
	}
	
	public String getNombre(){
		return nombreAso;
	}
	
	public ArrayList<ExtremoAsociacion> getExtremos(){
		return extremos;
	}

	@Override
	public String toString() {
		String ext1 ="Clase: "+extremos.get(0).getNomClase()
		+"\nTipo Aso: "+extremos.get(0).getTipoAso()
		+"\nLimite Inf: "+extremos.get(0).getLimiteInf()
		+"\nLimite Sup: "+extremos.get(0).getLimiteSup()
		+"\n";
		String ext2 ="Clase: "+ extremos.get(1).getNomClase()
		+"\nTipo Aso: "+extremos.get(1).getTipoAso()
		+"\nLimite Inf: "+extremos.get(1).getLimiteInf()
		+"\nLimite Sup: "+extremos.get(1).getLimiteSup();
		return "Asociacion\n"+ext1+ext2+" \n";
	}
}
