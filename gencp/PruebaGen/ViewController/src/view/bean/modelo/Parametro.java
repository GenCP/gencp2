package view.bean.modelo;

public class Parametro {

	String nombre;
	String tda;
	Object ValorDef;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setTda(String tda) {
		this.tda = tda;
	}
	
	public String getTda() {
		return tda;
	}
	public void addTipo(String tda) {
		this.tda = tda;
	}
	
	public Object getValorDef(){
		return ValorDef;
	}
	
	public void setValorDef(Object valor){
		this.ValorDef=valor;
	}
	
	@Override
	public String toString() {
		String s =  tda;
		return s;
	}	
}
