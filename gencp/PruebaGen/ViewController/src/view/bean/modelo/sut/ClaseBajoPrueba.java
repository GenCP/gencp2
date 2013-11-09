package view.bean.modelo.sut;

import java.util.ArrayList;
import view.bean.modelo.Clase;

public class ClaseBajoPrueba extends Clase {

	private ArrayList<MetodoBajoPrueba> metodosBp ;
	private boolean hayExcepcion;	
	private ArrayList<ClaseBajoPrueba> mocks;
	
	public ClaseBajoPrueba(String nombreClase, ArrayList<MetodoBajoPrueba> mBp ){
		super();
		super.setNombre(nombreClase);
		this.metodosBp = mBp;
		this.hayExcepcion=false;	
	}
	
	public boolean hayExcepcion(){
		return hayExcepcion;
	}
	
	public void setExcepcion(boolean flag){
		hayExcepcion = flag;
	}
	
	
	public ArrayList<MetodoBajoPrueba> getMetodosBajoPrueba(){
		return metodosBp;
	}
	
	public ArrayList<ClaseBajoPrueba> getMocks(){
		return mocks;
	}
	
	public void setMocks(ArrayList<ClaseBajoPrueba> mocks){
		this.mocks = mocks; 
	}

	public String getInstancia() {
		return this.getNombre().substring(0, 2).toLowerCase();
	}
	
}
