package view.bean.modelo;

import java.util.ArrayList;
import java.util.Random;

public class Metodo {

	int id;
	String nombre;
	String visib;
	ArrayList<Parametro> parametros;
 
 	public Metodo(){
 		Random unValor = new Random();
		id = unValor.nextInt();
 	}
 	
 	public Metodo(String nombre, String visib, ArrayList<Parametro> arrP){
 		Random unValor = new Random();
		id = unValor.nextInt();
		
		this.nombre = nombre;
		this.visib = visib;
		this.parametros = arrP;
 	}

	 public String getNombre() {
		return nombre;
	 }
	 
	 public void setNombre(String nombre) {
		this.nombre = nombre;
	 }
	
	 public String getVisib() {
		return visib;
	 }
	
	 public void setVisib(String visib) {
		this.visib = visib;
	 }
	 
	 public ArrayList<Parametro> getParametros() {
		return parametros;
	 }	
	 
	 public String getParametro(String nombrePar){
		 String s = "";
		 for(Parametro p : parametros){
			 if(p.getNombre()==nombrePar)
			//	 s = p.getTda();
				 s = p.getNombre();
		 }
		 return s;
	 }
	 
	 public Parametro getUnParametro(String nombrePar){
		 Parametro miPar=new Parametro();
		 for(Parametro p:parametros){
			 if(p.getNombre()==nombrePar){
				 miPar=p;
			 }
		 }
		 return miPar;
	 }
	 
	 public void setParametros(ArrayList<Parametro> parametros) {
		this.parametros = parametros;
	 }
	 
	 @Override
	public String toString(){
		return nombre+", "+visib+", "+parametros.toString();
	 }
	 
	 public int getId(){
		 return id;
	 }
	 
	 public void setId(int x){
		 id = x;
	 }//solo para casos especiales ver MenuSecuencia, generacion de metodos duplicados.

}
