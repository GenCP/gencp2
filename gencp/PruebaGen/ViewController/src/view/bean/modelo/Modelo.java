package view.bean.modelo;

import java.util.ArrayList;

import view.bean.modelo.restriccion.Restriccion;

public class Modelo {
	private ArrayList<Clase> clases = new ArrayList<Clase>();
	private ArrayList<Asociacion> asociaciones = new ArrayList<Asociacion>();
	private ArrayList<Generalizacion> generalizaciones = new ArrayList<Generalizacion>();
	private ArrayList<Restriccion> restricciones = new ArrayList<Restriccion>();

	public void addClase(Clase c){
		clases.add(c);
	}
	
	public Clase getClase(String nombre){
		Clase x = new Clase();
		for(Clase c: clases){
			if(c.getNombre().equals(nombre)){
				x = c;
			}
		}
		return x;
	}
	
	public ArrayList<Clase> getAllClases(){
		return clases;
	}
	
	public void addAso(Asociacion a){
		asociaciones.add(a);
	}
	
	public Asociacion getAso(int x){
		return asociaciones.get(x);
	}
	
	public ArrayList<Asociacion> getAllAso(){
		return asociaciones;
	}
	
	public void addGen(Generalizacion ge){
		generalizaciones.add(ge);
	}
	
	public ArrayList<Generalizacion> getAllGen(){
		return generalizaciones;
	}
	public Generalizacion getGen(int x){
		return generalizaciones.get(x);
	}
	
	public boolean contieneClase(Clase c){
		boolean x=false;
		if(clases.contains(c)){
			x=true;
		}
		return x;
	}
	
	public void addRestriccion(Restriccion r){
		restricciones.add(r);
	}
	
	public ArrayList<Restriccion> getRestriccion(){
		return restricciones;
	}

	public void remRestriccion(Restriccion res){
		for( Restriccion r: restricciones){
			if(r.equals(res))
				restricciones.remove(r);
		}
	}
	
	public boolean contieneRestriccion(Clase c, Metodo m, Parametro p){
		boolean contiene=false;
		for(Restriccion r: restricciones){
			if(r.getClase().getNombre().equals(c.getNombre()) 
					&& r.getMetodo()!=null
					&& r.getMetodo().getNombre().equals(m.getNombre())
					&& r.getParametro().getNombre().equals(p.getNombre())){
				contiene=true;
			}
		}
		return contiene;
	}

	
	public Restriccion getResAtt(Clase c){
		Restriccion res = null;
		for(Restriccion r: restricciones){
			if(r.getClase().getNombre().equals(c.getNombre()) 
					&& r.getMetodo() == null
					&& r.getAtributo() != null){
				res = r;
			}
		}
		return res;
	}
	
	public Restriccion getRestPar(Clase c, Metodo m, Parametro p, String tipoRel){
		Object obj = new Object();
		Restriccion r = new Restriccion(c, m, p, "", tipoRel, obj);
		for(Restriccion res: restricciones){
			if(res.getClase().equals(c) 
					&& res.getMetodo().equals(m)
					&& res.getParametro().equals(p)
					&& res.getTipoRel().equals(tipoRel) ){
				r = res;
			}
		}
		return r;
	}
	
	public int getCantRes(){
		return restricciones.size();
	}
	
	public  void remRes(int idRes){
		for(Restriccion r: restricciones){
			if(r.getId()==idRes){
				restricciones.remove(r);
				break;
			}		
		}
	}//fin remRes
}
