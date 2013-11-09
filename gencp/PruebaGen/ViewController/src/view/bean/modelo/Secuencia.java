package view.bean.modelo;

import java.util.ArrayList;

import view.bean.modelo.sut.ClaseBajoPrueba;

public class Secuencia {
	
	ArrayList<ClaseBajoPrueba> clases;
	

	public Secuencia(){
		clases = new ArrayList<ClaseBajoPrueba>();
		//inicializa el array
	}

	public ArrayList<ClaseBajoPrueba> getClasesBp() {
		return clases;
	}

	public void setClasesBp(ArrayList<ClaseBajoPrueba> clases) {
		this.clases = clases;
	}
	
	public void setClaseBp(ClaseBajoPrueba c){
		clases.add(c);
	}
	
	public void remClaseBp(int indice){
		for(ClaseBajoPrueba cBp: clases){
			if(indice==cBp.getId()){
				clases.remove(cBp);
			}
		}
	}
	public int size(){
		return clases.size();
	}
}
