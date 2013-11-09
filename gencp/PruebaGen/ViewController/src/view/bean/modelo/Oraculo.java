package view.bean.modelo;

import java.util.ArrayList;

public class Oraculo {
	
	@SuppressWarnings("unchecked")
	private ArrayList valoresCorrectos ;
	@SuppressWarnings("unchecked")
	private ArrayList valoresIncorrectos;
	@SuppressWarnings("unused")
	private String tda;
	
	
	public Oraculo(String tda){
		if(tda.equals("boolean")||tda.equals("Boolean")){
			this.tda = "boolean";
			valoresCorrectos = new ArrayList<Boolean>();
			valoresIncorrectos = new ArrayList<Boolean>();
		}
		else if(tda.equals("char")||tda.equals("char")){
			//No acepta char como clase parametrizada
			this.tda="char";
			valoresCorrectos = new ArrayList<Object>();
			valoresIncorrectos = new ArrayList<Object>();
		}
		else if(tda.equals("byte")||tda.equals("Byte")){
			this.tda="byte";
			valoresCorrectos = new ArrayList<Byte>();
			valoresIncorrectos = new ArrayList<Byte>();
		}
		else if(tda.equals("short")||tda.equals("Short")){
			this.tda="short";
			valoresCorrectos = new ArrayList<Short>();
			valoresIncorrectos = new ArrayList<Short>();
		}
		else if(tda.equals("int")||tda.equals("Int")){
			this.tda="int";
			valoresCorrectos = new ArrayList<Integer>();
			valoresIncorrectos = new ArrayList<Integer>();
		}
		else if(tda.equals("long")||tda.equals("Long")){
			this.tda="long";
			valoresCorrectos = new ArrayList<Long>();
			valoresIncorrectos = new ArrayList<Long>();
		}
		else if(tda.equals("float")||tda.equals("Float")){
			this.tda="float";
			valoresCorrectos = new ArrayList<Float>();
			valoresIncorrectos = new ArrayList<Float>();
			
		}
		else if(tda.equals("double")||tda.equals("Double")){
			this.tda="double";
			valoresCorrectos = new ArrayList<Double>();
			valoresIncorrectos = new ArrayList<Double>();
		}
		else if(tda.equals("String")|| tda.equals("string")){
			this.tda="String";
			valoresCorrectos = new ArrayList<String>();
			valoresIncorrectos = new ArrayList<String>();
		}
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList getValoresCorrectos() {
			return valoresCorrectos;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getValoresIncorrectos() {
		return valoresIncorrectos;
	}
	
	@SuppressWarnings("unchecked")
	public void addValorIncorr(Object valor) {
			 valoresIncorrectos.add(valor);
	}
		
	@SuppressWarnings("unchecked")
	public void addValoresCorrectos(Object valor) {
			 valoresCorrectos.add(valor);	
	}

}
