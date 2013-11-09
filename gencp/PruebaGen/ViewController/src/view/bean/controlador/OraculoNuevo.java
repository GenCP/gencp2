package view.bean.controlador;


import view.bean.modelo.Metodo;
import view.bean.modelo.Modelo;
import view.bean.modelo.Parametro;
import view.bean.modelo.restriccion.Restriccion;
import view.bean.modelo.sut.ClaseBajoPrueba;
import view.bean.modelo.sut.MetodoBajoPrueba;
import view.bean.modelo.sut.ParametroBajoPrueba;

public class OraculoNuevo {

	public boolean excepcion(ClaseBajoPrueba cBp, Modelo modelo){
		boolean excepcion = false;
		 for(Restriccion res:modelo.getRestriccion())
		 {
			 if(cBp.getNombre().equals(res.getClase().getNombre()) 
				&& res.getMetodo()!=null )
			 {
				for(MetodoBajoPrueba metodo : cBp.getMetodosBajoPrueba())
				{ //metodos de la clase bajo prueba
					if(metodo.getNombre().equals(res.getMetodo().getNombre()))
					{
						for(ParametroBajoPrueba par : metodo.getParBp())
						{ //parametro del metodo bajo prueba
							for(Parametro parRes : res.getMetodo().getParametros())
							{
								if(par.getNombre().equals(parRes.getNombre()) && !par.getTda().contains("void"))
								{
												String relacion = res.getTipoRel();
												if(par.getTda().equals("short")){ //parametro del metodo bajo prueba
													short valor = Short.parseShort(par.getValorDef().toString());
													short valorRes = Short.parseShort(res.getValor().toString());
													if(relacion.equals(">") && (valor <= valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("<") &&(valor >= valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals(">=") &&(valor < valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals("<=") && (valor > valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
												}//enf if de tipo de dato Short
												else if(par.getTda().equals("int")){
													int valor = Integer.parseInt(par.getValorDef().toString());
													int valorRes = Integer.parseInt(res.getValor().toString());
													if(relacion.equals(">") && (valor <= valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("<") &&(valor >= valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals(">=") &&(valor < valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals("<=") && (valor > valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
												}//fin if de tipo Int
												else if(par.getTda().equals("long")){
													long valor = Long.parseLong(par.getValorDef().toString());
													long valorRes = Long.parseLong(res.getValor().toString());
													if(relacion.equals(">") && (valor <= valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("<") &&(valor >= valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals(">=") &&(valor < valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals("<=") && (valor > valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
												}//fin if de tipo Long
												else if(par.getTda().equals("float")){
													float valor = Float.parseFloat(par.getValorDef().toString());
													float valorRes = Float.parseFloat(res.getValor().toString());
													if(relacion.equals(">") && (valor <= valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("<") &&(valor >= valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals(">=") &&(valor < valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals("<=") && (valor > valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
												}//fin if de tipo Float
												else if(par.getTda().equals("double")){
													double valor = Double.parseDouble(par.getValorDef().toString());
													double valorRes = Double.parseDouble(res.getValor().toString());
													if(relacion.equals(">") && (valor <= valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("<") &&(valor >= valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals(">=") &&(valor < valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals("<=") && (valor > valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
												}//fin if de tipo Double
												else if(par.getTda().equals("byte")){
													byte valor = Byte.parseByte(par.getValorDef().toString());
													byte valorRes = Byte.parseByte(res.getValor().toString());
													if(relacion.equals(">") && (valor <= valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("<") &&(valor >= valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals(">=") &&(valor < valorRes) ){
														excepcion = true;
													}
													else if(relacion.equals("<=") && (valor > valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
												}//fin if de tipo Byte
												else if(par.getTda().equals("char")){
													if(par.getValorDef().toString().length()!=1){
														excepcion = true;
													}
													else{
														char valor = par.getValorDef().toString().charAt(0);
														char valorRes = res.getValor().toString().charAt(0);
														if(relacion.equals("!=") && (valor == valorRes)){
															excepcion = true;
														}
														else if(relacion.equals("=") && (valor != valorRes)){
															excepcion = true;
														}
													}												
												}//fin if de tipo Char
												else if (par.getTda().equals("boolean")){
													boolean valor = Boolean.parseBoolean(par.getValorDef().toString());
													boolean valorRes = Boolean.parseBoolean(res.getValor().toString());
													if(relacion.equals("!=") && (valor == valorRes)){
														excepcion = true;
													}
													else if(relacion.equals("=") && (valor != valorRes)){
														excepcion = true;
													}
												}//fin if de tipo Boolean
												else if (par.getTda().equals("String")){
													String valor = par.getValorDef().toString();
													String valorRes = res.getValor().toString();
													if(relacion.equals("!=") && (valor.equals(valorRes))){
														excepcion = true;
													}
													else if(relacion.equals("=") && (!valor.equals(valorRes))){
														excepcion = true;
													}
												}//fin if de tipo Boolean								
										}	
									}
								}
							}
						
					}
				}
			 }
		return excepcion;
	}//fin del metodo
	
	public Object getValorLimite(Modelo modelo,ClaseBajoPrueba cBp, MetodoBajoPrueba mBp, ParametroBajoPrueba pBp){
		for(Restriccion res:modelo.getRestriccion()){
			if(res.getMetodo()!=null 
					&& cBp.getNombre().equals(res.getClase().getNombre()) 
					&& mBp.getNombre().equals(res.getMetodo().getNombre())
					&& pBp.getNombre().equals(res.getParametro().getNombre())){
				return getAVL(res, pBp);
			}
			else {
				for( Metodo met : res.getMetodoBajoRes() ){
					for(Parametro p: met.getParametros()){
						if(met.getNombre().equals(mBp.getNombre())
								&& p.getNombre().equals(pBp.getNombre()))
						return	getAVL(res, pBp);
					}
				}
			}
		}//fin for each restriccion
		return null;
	}
		
		Object getAVL(Restriccion res,ParametroBajoPrueba pBp){
			Object valor = null;
			String relacion = res.getTipoRel();
			if(pBp.getTda().equals("byte")
				||	pBp.getTda().equals("short") 
				|| pBp.getTda().equals("int")
				|| pBp.getTda().equals("long")){ 
				long val = Long.parseLong(pBp.getValorDef().toString());
				if(relacion.equals(">")){ valor = val-1 ;	}
				else if(relacion.equals("<") ){	valor = val+1 ;	}
				else if(relacion.equals(">=")){	valor = val-1 ; }
				else if(relacion.equals("<=")){	valor = val+1 ;	}
				else if(relacion.equals("=")){	valor = val-1 ;	}
				else if(relacion.equals("!=") ){valor = val ; }
			}//enf if de tipos de datos numericos sin precision						
			else if(pBp.getTda().equals("float")|| pBp.getTda().equals("double")){
				double val = Double.parseDouble(pBp.getValorDef().toString());
				if(relacion.equals(">")){valor = val-0.1 ;}
				else if(relacion.equals("<")){valor = val+0.1;}
				else if(relacion.equals(">=")){	valor = val-0.1 ;}
				else if(relacion.equals("<=")){	valor = val+0.1 ;	}
				else if(relacion.equals("=")){	valor = val+0.1 ;}
				else if(relacion.equals("!=")){	valor = val ;}
			}//fin if de tipos de datos con presicion
		else if(pBp.getTda().equals("char")){
			if(!(pBp.getValorDef().toString().length()!=1)){
				char val = pBp.getValorDef().toString().charAt(0);
				if(relacion.equals("!=") && val=='x'){	valor = 'x' ;	}
				else if(relacion.equals("!=") && val!='x'){valor = 'y' ;}
				else if(relacion.equals("=") && val=='x'){	valor = 'y' ;	}
				else if (relacion.equals("=") && val!='x'){	valor = 'x' ;}
			}
		}//fin if de tipo Char
		else if (pBp.getTda().equals("boolean")){
			boolean val = Boolean.parseBoolean(pBp.getValorDef().toString());
			if(relacion.equals("!=") && val == true){valor = true ;}
			else if(relacion.equals("!=") && val==false){valor = false;	}
			else if(relacion.equals("=") && val==true){	valor = false ;}
			else if(relacion.equals("=") && val==false){valor = true;}
		}//fin if de tipo Boolean
		else if (pBp.getTda().equals("String")){
			String val = pBp.getValorDef().toString();
			String valorRes = res.getValor().toString();
			if(relacion.equals("!=")){valor = val;}
			else if(relacion.equals("=") && (!valor.equals(valorRes))){	valor = val.substring(0, 1);}
		}
			return valor;
		}//fin metodo getAVL

}
