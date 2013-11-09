package view.bean.modelo;

import java.util.ArrayList;
import java.util.Random;

public class Clase {	
	int id;
	String nombre;
	ArrayList<Atributo> atributos;
	ArrayList<Metodo> metodos;
	String visib;
	String ancestro;

	public Clase(){
		Random unValor = new Random();
		id = unValor.nextInt();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public ArrayList<Metodo> getMetodos() {
		return metodos;
	}
	
	public Atributo getAtributo(String nombre){
		Atributo miAt = null;
		for(Atributo a: atributos){
			if(a.getNombre().equals(nombre)){
				miAt = a;
			}
		}
		return miAt;
	}
	
	public Metodo getMetodo(String nombreMet){
		Metodo miMetodo = new Metodo();
		for(Metodo m: metodos){
			if(m.getNombre().equals(nombreMet))
					miMetodo=m;
		}	
		return miMetodo;
	}
	
	public Metodo getMetodo(int x){
		return metodos.get(x);
	}
	
	public void setMetodos(ArrayList<Metodo> metodos) {
		this.metodos = metodos;
	}
	
	public void setMetodo(Metodo m){
		metodos.add(m);
	}
	
	public void setAtributo(Atributo a){
		atributos.add(a);
	}
			
	public String getAncestro() {
		return ancestro;
	}
	
	public void setAncestro(String ancestro) {
		this.ancestro = ancestro;
	}

	public String getVisib() {
		return visib;
	}

	public void setVisib(String visib) {
		this.visib = visib;
	}

	public ArrayList<Atributo> getAtributos() {
		return atributos;
	}

	public void setAtributos(ArrayList<Atributo> atributos) {
		this.atributos = atributos;
	}

	@Override
	public String toString() {
		String Dclase=nombre+" "+metodos.toString();
		return Dclase;
	}
	
	public int getId(){
		return id;
	}
}
