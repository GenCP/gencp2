package view.bean.controlador;
import java.util.Random;

import view.bean.modelo.Oraculo;
import view.bean.modelo.Parametro;

public class GenerarDatosEntradaAVL {
	int tdaInt;
	double tdaDouble;
	boolean tdaBoolean;
	String tdaString;
	long tdaLong;
	float tdaFloat;
	char tdaChar;
	short tdaShort;
	byte tdaByte;

	public Oraculo generaDatos(Parametro var, String tipoRel, String valor){
		
		String tda=var.getTda();
		Oraculo or = null;
		
		if(tda.equals("int")||tda.equals("Int")||tda.equals("Integer")){
			 or = new Oraculo("int");
			
			tdaInt = Integer.parseInt(valor); //este va a ser incorrecto
			if((tipoRel.equals(">"))){ //x>0, donde x es var y 0 es valor
				or.addValoresCorrectos(tdaInt+1);
				or.addValorIncorr(tdaInt);
				or.addValorIncorr(tdaInt-1);
			}
			else if(tipoRel.equals("<")){ 
				or.addValoresCorrectos(tdaInt-1);
				or.addValorIncorr(tdaInt);
				or.addValorIncorr(tdaInt+1);
			}
			else if(tipoRel.equals("=")){ //x>0, donde x es var y 0 es valor
				or.addValoresCorrectos(tdaInt);
				or.addValorIncorr(tdaInt-1);
				or.addValorIncorr(tdaInt+1);
			}
			else if((tipoRel.equals("<=")||tipoRel.equals("=<"))){ //x <= 99
				or.addValoresCorrectos(tdaInt);
				or.addValoresCorrectos(tdaInt-1);
				or.addValorIncorr(tdaInt+1);
				or.addValorIncorr(tdaInt+2);
			}
			else if((tipoRel.equals(">=")||tipoRel.equals("=>"))){ //x>=0, donde x es var y 0 es valor
				or.addValoresCorrectos(tdaInt);
				or.addValoresCorrectos(tdaInt+1);
				or.addValorIncorr(tdaInt-1);
				or.addValorIncorr(tdaInt-2);
			}
			else if((tipoRel.equals("!="))){ //x!=0, donde x es var y 0 es valor
				or.addValoresCorrectos(tdaInt-1);
				or.addValorIncorr(tdaInt);
			}
		}
		else if(tda.equals("double") || tda.equals("Double")){
			 or = new Oraculo("double");
			tdaDouble = Double.parseDouble(valor);
			if(tipoRel.equals(">")){
				or.addValoresCorrectos(tdaDouble+0.1);
				or.addValorIncorr(tdaDouble);
				or.addValorIncorr(tdaDouble-0.1);
			}
			else if(tipoRel.equals("<")){
				or.addValoresCorrectos(tdaDouble-0.1);
				or.addValorIncorr(tdaDouble);
				or.addValorIncorr(tdaDouble+0.1);
			}
			else if(tipoRel.equals(">=") || tipoRel.equals("=>")){
				or.addValoresCorrectos(tdaDouble);
				or.addValoresCorrectos(tdaDouble+0.1);
				or.addValorIncorr(tdaDouble-0.2);
				or.addValorIncorr(tdaDouble-0.1);	
			}
			else if(tipoRel.equals("<=") || tipoRel.equals("=<")){
				or.addValoresCorrectos(tdaDouble);
				or.addValoresCorrectos(tdaDouble-0.1);
				or.addValorIncorr(tdaDouble+0.1);
				or.addValorIncorr(tdaDouble+0.2);
			}
			else if(tipoRel.equals("=")){
				or.addValoresCorrectos(tdaDouble);
				or.addValorIncorr(tdaDouble-0.1);
			}
			else if(tipoRel.equals("!=")){
				or.addValoresCorrectos(tdaDouble-0.1);
				or.addValorIncorr(tdaDouble);	
			}
		}
		else if(tda.equals("boolean")){
			 or = new Oraculo("boolean");
			tdaBoolean = Boolean.parseBoolean(valor);
			System.out.println(tdaBoolean);
			if(tipoRel.equals("=") && tdaBoolean==true){
				or.addValoresCorrectos(true);
				or.addValorIncorr(false);
			}
			else if(tipoRel.equals("=") && tdaBoolean==false){
				or.addValoresCorrectos(false);
				or.addValorIncorr(true);
			}
			else if(tipoRel.equals("!=") && tdaBoolean==true){
				or.addValoresCorrectos(false);
				or.addValorIncorr(true);
			}
			else if(tipoRel.equals("!=")&& tdaBoolean==false){
				or.addValoresCorrectos(true);
				or.addValorIncorr(false);
			}
		}
		else if(tda.equals("String")|| tda.equals("string")){
			or = new Oraculo("String");
			or.addValoresCorrectos("\""+valor+"\"");
			or.addValorIncorr("\""+valor.substring(1)+"\"");
		}
		else if(tda.equals("char")|| tda.equals("Char")){
			char valorChar = valor.charAt(0);
			or = new Oraculo("char");
			if(tipoRel=="=" && valorChar=='x'){
				or.addValoresCorrectos("\'"+valorChar+"\'");
				or.addValorIncorr("\'z\'");
			}
			else if(tipoRel=="!=" && valorChar=='x'){
				or.addValorIncorr("\'x\'");
				or.addValoresCorrectos("\'z\'");
			}
			else if(tipoRel=="=" && valorChar!='x'){
				or.addValorIncorr("\'x\'");
				or.addValoresCorrectos("\'"+valorChar+"\'");
			}
			else if(tipoRel=="!=" && valorChar!='x'){
				or.addValoresCorrectos("\'x\'");
				or.addValorIncorr("\'"+valorChar+"\'");
			}
		}
		else if(tda.equals("long") 	|| tda.equals("Long")){
			 or = new Oraculo("long");
			tdaLong = Long.parseLong(valor);
			if(tipoRel.equals(">")){
				or.addValoresCorrectos(tdaLong+1);
				or.addValorIncorr(tdaLong);
				or.addValorIncorr(tdaLong-1);
			}
			else if(tipoRel.equals("<")){
				or.addValoresCorrectos(tdaLong-1);
				or.addValorIncorr(tdaLong);
				or.addValorIncorr(tdaLong+1);
			}
			else if(tipoRel.equals(">=") || tipoRel.equals("=>")){
				or.addValoresCorrectos(tdaLong);
				or.addValoresCorrectos(tdaLong+1);
				or.addValorIncorr(tdaLong-1);
				or.addValorIncorr(tdaLong-2);	
			}
			else if(tipoRel.equals("<=") || tipoRel.equals("=<")){
				or.addValoresCorrectos(tdaLong);
				or.addValoresCorrectos(tdaLong-1);
				or.addValorIncorr(tdaLong+1);
				or.addValorIncorr(tdaLong+2);
			}
		
			else if(tipoRel.equals("=")){
				or.addValoresCorrectos(tdaLong);
				or.addValorIncorr(tdaLong-1);
			}
			else if(tipoRel.equals("!=")){
				or.addValoresCorrectos(tdaLong-1);
				or.addValorIncorr(tdaLong);	
			}
		}
		else if(tda.equals("float") || tda.equals("Float")){
				 or = new Oraculo("float");
				tdaFloat = Float.parseFloat(valor);
				if(tipoRel.equals(">")){
					or.addValoresCorrectos(tdaFloat+0.1);
					or.addValorIncorr(tdaFloat);
					or.addValorIncorr(tdaFloat-0.1);
				}
				else if(tipoRel.equals("<")){
					or.addValoresCorrectos(tdaFloat-0.1);
					or.addValorIncorr(tdaFloat);
					or.addValorIncorr(tdaFloat+0.1);
				}
				else if(tipoRel.equals(">=") || tipoRel.equals("=>")){
					or.addValoresCorrectos(tdaFloat);
					or.addValoresCorrectos(tdaFloat+0.1);
					or.addValorIncorr(tdaFloat-0.1);
					or.addValorIncorr(tdaFloat-0.2);	
				}
				else if(tipoRel.equals("<=") || tipoRel.equals("=<")){
					or.addValoresCorrectos(tdaFloat);
					or.addValoresCorrectos(tdaFloat-0.1);
					or.addValorIncorr(tdaFloat+0.1);
					or.addValorIncorr(tdaFloat+0.2);
				}
				else if(tipoRel.equals("=")){
					or.addValoresCorrectos(tdaFloat);
					or.addValorIncorr(tdaFloat-0.1);
				}
				else if(tipoRel.equals("!=")){
					or.addValoresCorrectos(tdaFloat-0.1);
					or.addValorIncorr(tdaFloat);	
				}
			}
		else if(tda.equals("short") || tda.equals("Short")){
			 or = new Oraculo("short");
			tdaShort = Short.parseShort(valor);
			short s1 = 1;
			short s2 = -1;
			
			if(tipoRel.equals(">")){
				or.addValoresCorrectos((short)tdaShort+s1);
				or.addValorIncorr(tdaShort);
				or.addValorIncorr((short)tdaShort+s2);
			}
		else if(tipoRel.equals("<")){
				or.addValoresCorrectos((short)tdaShort+s2);
				or.addValorIncorr(tdaShort);
				or.addValorIncorr((short)tdaShort+s1);
			}
			else if(tipoRel.equals(">=") || tipoRel.equals("=>")){
				or.addValoresCorrectos(tdaShort);
				or.addValoresCorrectos((short)tdaShort+s1);
				or.addValorIncorr((short)tdaShort+s2);	
			}
			else if(tipoRel.equals("<=") || tipoRel.equals("=<")){
				or.addValoresCorrectos(tdaShort);
				or.addValoresCorrectos((short)tdaShort+s2);
				or.addValorIncorr((short)tdaShort+1);
			}
			else if(tipoRel.equals("=")){
				or.addValoresCorrectos(tdaShort);
				or.addValorIncorr((short)tdaShort-1);
			}
			else if(tipoRel.equals("!=")){
				or.addValoresCorrectos((short)tdaShort-1);
				or.addValorIncorr(tdaShort);	
			}
		}
		else if(tda.equals("byte") || tda.equals("Byte")){
			 or = new Oraculo("byte");
			tdaByte = Byte.parseByte(valor);
			byte s1 = 1;
			byte s2 = -1;
			
			if(tipoRel.equals(">")){
				or.addValoresCorrectos((byte)tdaByte+s1);
				or.addValorIncorr(tdaByte);
				or.addValorIncorr((byte)tdaByte+s2);
			}
			else if(tipoRel.equals("<")){
				or.addValoresCorrectos((byte)tdaByte+s2);
				or.addValorIncorr(tdaByte);
				or.addValorIncorr((byte)tdaByte+s1);
			}
			else if(tipoRel.equals(">=") || tipoRel.equals("=>")){
				or.addValoresCorrectos(tdaByte);
				or.addValoresCorrectos((byte)tdaByte+s1);
				or.addValorIncorr((byte)tdaByte+s2);	
			}
			else if(tipoRel.equals("<=") || tipoRel.equals("=<")){
				or.addValoresCorrectos(tdaByte);
				or.addValoresCorrectos((byte)tdaByte+s2);
				or.addValorIncorr((byte)tdaByte+1);
			}
			else if(tipoRel.equals("=")){
				or.addValoresCorrectos(tdaByte);
				or.addValorIncorr((byte)tdaByte-1);
			}
			else if(tipoRel.equals("!=")){
				or.addValoresCorrectos((byte)tdaByte-1);
				or.addValorIncorr(tdaByte);	
			}
		}
		
		return or;
	}//fin del metodo
	
	public Object alteatoria(Parametro p){
		Object o = new Object();
		Random unValor = new Random();

		if(p.getTda().equals("int")||p.getTda().equals("Integer")){
			 o = (int) unValor.nextInt();
		}
		else if(p.getTda().equals("double")||p.getTda().equals("Double")){
			o = (double) unValor.nextDouble();
		}
		else if(p.getTda().equals("boolean")||p.getTda().equals("Boolean")){
			o = (boolean) unValor.nextBoolean();
		}
		else if(p.getTda().equals("String")||p.getTda().equals("string")){
			o = "\"prueba99999\"";
		}
		else if(p.getTda().equals("long")||p.getTda().equals("Long")){
			o = (long) unValor.nextLong();
		}
		else if(p.getTda().equals("float")||p.getTda().equals("Float")){
			o = (float) unValor.nextFloat();
		}
		else if(p.getTda().equals("char")||p.getTda().equals("Char")){
			o = '_';//ver una mejor forma de generar un valor aleatorio
		}
		else if(p.getTda().equals("short")||p.getTda().equals("Short")){
			o = 127; //esto no es random
		}
		else if(p.getTda().equals("byte") || p.getTda().equals("Byte")){
			byte[] bytes = new byte[1] ; 
			unValor.nextBytes(bytes);
			o = bytes[0];
		}
		return o;
	}
}
