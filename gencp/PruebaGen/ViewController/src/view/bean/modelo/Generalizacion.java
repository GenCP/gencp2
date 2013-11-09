package view.bean.modelo;

public class Generalizacion {
	
	private String padre;
	private String hijo;
	
	public String getPadre() {
		return padre;
	}
	public void setPadre(String padre) {
		this.padre = padre;
	}
	public String getHijo() {
		return hijo;
	}
	public void setHijo(String hijo) {
		this.hijo = hijo;
	}
	@Override
	public String toString() {
		return "superClase "+padre+" "+"subclase "+hijo;
	}

}
