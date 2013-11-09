package view.bean.modelo.restriccion;
import java.util.ArrayList;
import java.util.Random;

import view.bean.controlador.GenerarDatosEntradaAVL;
import view.bean.modelo.Atributo;
import view.bean.modelo.Clase;
import view.bean.modelo.Metodo;
import view.bean.modelo.Oraculo;
import view.bean.modelo.Parametro;


public class Restriccion {
	
	private int id;
	private Clase clase;
	private Atributo atributo;
	private Metodo metodo;
	private Parametro parametro; 
	private String tipoRestriccion; //pre|post|inv
	private String tipoRelacion; //>, <, ==, >=, <=
	private Object valor;  //999, 1, 123, gato, true
	private Oraculo oraculo; //Valores correctos e Incorrectos
	private Object[][] vPars; 
	//Experimental////////////////////
	private ArrayList<Metodo> metodosBajoRes; 
	//Experimental////////////////////
	
	
	
	//vPars usado cuando la restriccion es POST, 
	//especifica los parametros de entrada mientras que valor hace referencia al
	//parametro de salida.
	
	public Restriccion(Clase c, Metodo m, Parametro p, 
			String tRes, String tRel, Object v){
		//constructor para precondiciones sobre parametros
		
		Random unValor = new Random();
		id = unValor.nextInt();
		clase = c;
		atributo = null;
		metodo = m;
		parametro = p;
		tipoRestriccion = tRes;
		tipoRelacion = tRel;
		valor = v;
		oraculo = generaOraculo(p, tRel, v.toString());
		metodosBajoRes = new ArrayList<Metodo>();
		
	}
	
	public Restriccion(Clase c, Atributo a, Object miVal, String tRes, 
			String tRel, ArrayList<Metodo> mets){
		//pre y post condiciones sobre atributos 
		Random unValor = new Random();
		id = unValor.nextInt();
		clase = c;
		metodo = null;
		atributo = a;
		tipoRestriccion = tRes;
		tipoRelacion = tRel;
		valor = miVal;
		metodosBajoRes = mets;
	}
	
	public Restriccion(Clase c, Metodo m, Parametro p,
			String tRes, String tRel, Object v, Object[][] vPars ){
		//constructor para post condiciones
		clase = c;
		metodo = m;
		parametro = p;
		tipoRestriccion = tRes;
		tipoRelacion = tRel;
		valor = v;
		this.vPars = vPars;
		oraculo = generaOraculo(p, tRel, v.toString());
		Random unValor = new Random();
		id = unValor.nextInt();
		metodosBajoRes = new ArrayList<Metodo>();
	}

	
	public Clase getClase(){
		return clase;
	}
	
	public Atributo getAtributo(){
		return atributo;
	}
	
	public Metodo getMetodo(){
		return metodo;
	}
	
	public Parametro getParametro(){
		return parametro;
	}
	
	public Parametro getParametro(String nombrePar, String tda){
		Parametro p = new Parametro();
		for(Parametro par : metodo.getParametros()){
			if(par.getNombre().equals(nombrePar) && par.getTda().equals(tda)){
				p = par;
			}
		}
		return p;
	}
		
	public void setValor(String nombrePar, String valorPar){ 	
		String tda=parametro.getTda();
		if(tda.contains("string")||tda.contains("String"))
			valor=valorPar;
		else if(tda.contains("int")||tda.contains("Int"))
			valor=Integer.valueOf(valorPar);
		else if(tda.contains("double")||tda.contains("Double"))
			valor=Double.valueOf(valorPar);
		else if(tda.contains("boolean")||tda.contains("Boolean"))
			valor=Boolean.valueOf(valorPar);
		else if(tda.contains("long")||tda.contains("Long"))
			valor=Long.valueOf(valorPar);
		else if(tda.contains("void")||tda.contains("Void"))
			valor=null;
	}
	
	public String getTipoRes(){
		return tipoRestriccion;
	}

	public String getTipoRel(){
		return tipoRelacion;
	}

	public Object getValor(){
		return valor;
	}
	
	@Override
	public String toString(){
		
		if(metodo !=null){
		return tipoRestriccion+" "+clase.getNombre()+
							"."+metodo.getNombre()+
							" ("+parametro.getNombre()+
							" "+tipoRelacion+" "+valor+")";
		}
		else{
			return tipoRestriccion+" "+clase.getNombre()+
							"."+atributo.getNombre()+
							tipoRelacion+valor.toString()+" --> "+metodosBajoRes.get(0).getNombre();
		}
	}
	
	public Oraculo generaOraculo(Parametro p, String tipoRel, Object val){
		GenerarDatosEntradaAVL gde = new GenerarDatosEntradaAVL();
		return gde.generaDatos(p, tipoRel, val.toString());
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getValoresCorrectos(){
		return oraculo.getValoresCorrectos();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getValoresIncorrectos(){
		return oraculo.getValoresIncorrectos();
	}
	
	public int getId(){
		return id;
	}
	
	public Object[][] getVPars(){
		return vPars;
	}
	
	public ArrayList<Metodo> getMetodoBajoRes(){
		return metodosBajoRes;
	}
}
