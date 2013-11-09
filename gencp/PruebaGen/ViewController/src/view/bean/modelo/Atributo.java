package view.bean.modelo;

public class Atributo {

	private String nombre;
	private String tipoDato;
	private String modificador;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}
	public String getModificador() {
		return modificador;
	}
	public void setModificador(String modificador) {
		this.modificador = modificador;
	}
	@Override
	public String toString(){
		return modificador+" "+nombre+" "+tipoDato;
	}
	
}
