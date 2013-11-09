package view.bean.controlador;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import view.bean.modelo.Metodo;
import view.bean.modelo.Modelo;
import view.bean.modelo.Oraculo;
import view.bean.modelo.Parametro;
import view.bean.modelo.Secuencia;
import view.bean.modelo.restriccion.Restriccion;
import view.bean.modelo.sut.ClaseBajoPrueba;
import view.bean.modelo.sut.MetodoBajoPrueba;
import view.bean.modelo.sut.ParametroBajoPrueba;


public class GeneracionCasos {
	/*
	 * Metodo: generaSecuencia
	 * Objetivo: Toma la secuencia original, establecida por el usuario,
	 * y a partir de ella genera otras secuencias con los parametros modificados
	 * utilizando los mecanismos de AVL y Particiones Equivalentes
	 */
	public void generaSecuencia(Modelo modelo,Secuencia sec, String path) 
		throws IOException{
		
		FileWriter fw = null ; //para la creacion del archivo de pruebas
		int cantPruebas = 0 ;
		for(ClaseBajoPrueba cBp : sec.getClasesBp()){ 
			//Paso 1- Recorrer las clases bajo prueba de la instancia Secuencia
			cantPruebas++;
			String clase = cBp.getNombre();
			String instancia = cBp.getInstancia(); 
			
			fw = new FileWriter(new File(
						path+"\\"+clase+"Test"+cantPruebas+".java"));
			 fw.write("import org.junit.*;\n");
			 fw.write("import static org.junit.Assert.*;\n\n");
			 fw.write("public class "+clase+"Test"+cantPruebas+" {\n\n");

			OraculoNuevo or = new OraculoNuevo();
			if(cBp.hayExcepcion() == false) //El usuario marco en MnuSecuencia la excepcion desde el checkbox 
				cBp.setExcepcion(or.excepcion(cBp, modelo)); // recorre la estructura de CBp y decide si hay un lanzamiento de excepcion
			
			 if(cBp.hayExcepcion()) fw.write("	@Test (expected=Exception.class) \n"); 
			 else{ fw.write("	@Test  \n"); }
			 
			 int cantP=1;
			 
			 fw.write("	public void casoPrueba"+cantP+"() throws Exception{\n\n");
			 fw.write("		"+clase+" "+instancia+" = new "
								+clase+"();\n");
			 /*Paso 2: Se imprime la primera secuencia, tal como se elaboro
			  * Llama al metodo doCasos
			  */
			 doPrePost(fw,modelo,cBp,null,null,instancia,false,0); 
			
			 //Para AVL
			 for(MetodoBajoPrueba mBp : cBp.getMetodosBajoPrueba()){
				 for(ParametroBajoPrueba pBp : mBp.getParBp()){
					 if(pBp.esLimite()){
						 fw.write("	@Test (expected=Exception.class) \n");
						 cantP+=1;
						 fw.write("	public void casoPrueba"+cantP+"() throws Exception{\n\n");
						 fw.write("		"+clase+" "+instancia+" = new "
											+clase+"();\n");
						 
						 doPrePost(fw,modelo,cBp,mBp,pBp,instancia,true,1);
						 
						 if(pBp.getTda().equals("int") || pBp.getTda().equals("float")
								 || pBp.getTda().equals("double") || pBp.getTda().equals("short")
							 || pBp.getTda().equals("long")|| pBp.getTda().equals("byte")){
							 
							 fw.write("	@Test (expected=Exception.class) \n");
							 cantP+=1;
							 fw.write("	public void casoPrueba"+cantP+"() throws Exception{\n\n");
							 fw.write("		"+clase+" "+instancia+" = new "
											+clase+"();\n");
						 
							 doPrePost(fw,modelo,cBp,mBp,pBp,instancia,true,2);
						 }
					 }
				 }
			 }
			 fw.write("}");//fin de la Clase xxxPruebaxx.java
			 fw.close();
		}//for each clase Bajo Prueba cBp
	}
	
	/*
	 * Metodo: doCasos
	 * Objetivo:
	 * - Escribe los objetos ficticios (mocks), en el caso que existan 
	 * - Escribe las Precondiciones y Postcondiciones de un metodo
	 */
	void doPrePost(FileWriter fw,Modelo modelo,ClaseBajoPrueba cBp,MetodoBajoPrueba mBp,
			     ParametroBajoPrueba pBp,String instancia,boolean esLimite, int op)throws IOException {
		 if(cBp.getMocks().size()>0){
			 for(ClaseBajoPrueba mock : cBp.getMocks()) {				
				 fw.write("		"+mock.getNombre()+" "+mock.getInstancia()+" = new "
							+mock.getNombre()+"();\n");
				
					for(Metodo metMocks : mock.getMetodos()){
						 fw.write("		"+mock.getInstancia()+"."+metMocks.getNombre()+formatParametros2(metMocks,false, null)); 	
					}
			 }
			 fw.write("\n");
		} //Iterando dentro de una clase bajo prueba dentro de la secuencia.
		
		for(MetodoBajoPrueba m : cBp.getMetodosBajoPrueba()){
		//Imprime la precondicion para el metodo
		
			ParametroBajoPrueba p = m.getParBp().get(0); // return es el primer parametro siempre
		 if(p.getValorDef().toString().equals("")) { /* si el valor es void entonces estara vacio*/
		  for(Restriccion res : modelo.getRestriccion()){ /*Restricciones en forma de Precondiciones*/
		   if(!res.getMetodoBajoRes().isEmpty()){
			for(Metodo mRes : res.getMetodoBajoRes()){
			 if(res.getAtributo()!= null && res.getTipoRes().equals("Pre") 
				 && res.getClase().getNombre().equals(cBp.getNombre())
				 && mRes.getNombre().equals(m.getNombre())){
				 			
					String nomAt = res.getAtributo().getNombre(); 
					nomAt = nomAt.substring(0,1).toUpperCase() + 
							nomAt.substring(1, nomAt.length()) ; 									
					
					String lineaPre = instancia+"."+"get"+nomAt+"() "+res.getTipoRel()+" "+
	 	 							  formateoCadena(res.getValor(), res.getAtributo().getTipoDato())+");";
					
					fw.write("		"+"assertTrue("+lineaPre+"/* @Pre */ \n ");
			 }
			}
		   }
		  }//FIN DE PRECONDICIONES	 
		  //El metodo de la secuencia
		  if(!esLimite)
			  fw.write("		"+instancia+"."+m.getNombre()+formateoParametros(m,null, false, op)+"\n");
		  else{
			  for(ParametroBajoPrueba pB : mBp.getParBp()){
				  if(pB.equals(pBp)){
					  fw.write("		"+instancia+"."+m.getNombre()+formateoParametros(m,pBp, true, op)+"\n");
				  }
			  }	 
		  }
				
 		 for(Restriccion res : modelo.getRestriccion()){ //imprimir la postcondiciones
					 if(!res.getMetodoBajoRes().isEmpty()){
						 for(Metodo mRes : res.getMetodoBajoRes()){
							 if(res.getAtributo()!=null && res.getTipoRes().equals("Post")
								 && res.getClase().getNombre().equals(cBp.getNombre())
								 && mRes.getNombre().equals(m.getNombre())) {
								 String nomAt = res.getAtributo().getNombre(); //saldo
								 nomAt = nomAt.substring(0,1).toUpperCase() + 
								 			nomAt.substring(1, nomAt.length()) ; //Saldo
				 	 		 				 
								 String lineaPost = instancia+"."+"get"+nomAt+"() "+res.getTipoRel()+" "+
				 	 								formateoCadena(res.getValor(), res.getAtributo().getTipoDato())+");";
							
								 fw.write("		"+"assertTrue("+lineaPost+"/* @Post */ \n");
							 }
						 }
					 }
				 }//fin POST
		}//fin  IF es el parametro de retorno es void
			 	
		else if(!p.getTda().equals("void")) {
			String lineaPos = "("+p.getTda()+") "+formateoCadena(p.getValorDef(), p.getTda())
				 +", "+instancia+"."+m.getNombre()+"());";
			fw.write("		assertEquals("+lineaPos+ " \n");
		}
				 
	}//fin del for each metodo in cBp
		 
	fw.write("		}\n\n"); //fin de la prueba, primer Caso de Prueba.
	}
/*
 * Metodo: formatParametros
 * Objetivo: agrega caracteres especiales, para cumplir con la sintaxis java
 * Ej. 	en caso char agrega ''
 * 		en string ""
 * 
 */
	public String formateoParametros(MetodoBajoPrueba m,ParametroBajoPrueba pB, boolean avl, int op){
		int cantPar = m.getParBp().size();
		String resultado = "(";
		for(ParametroBajoPrueba p : m.getParBp()){
			if(!p.getNombre().equals("return")){
				if(m.getParBp().indexOf(p)==cantPar-1 && cantPar==2){//Si es el unico  parmetro 
					if(p.getTda().equals("char") && (!p.equals(pB) || !avl)) //valorDef si el parametro no es limite
						resultado = "('"+p.getValorDef().toString()+"');\n"; 

					else if(p.getTda().equals("char") && avl && p.equals(pB))
						resultado = "('"+getAVL(p,op).toString()+"');\n"; 

					else if(p.getTda().equals("String") && (!p.equals(pB) || !avl)) 
						resultado = "(\""+p.getValorDef().toString()+"\");\n";	
				
					else if(p.getTda().equals("String") && avl && p.equals(pB))
						resultado = "(\""+getAVL(p,op).toString().substring(0, 1)+"\");\n";	//OJO
					
					else if(!avl || (!p.equals(pB)))
						resultado ="("+ p.getValorDef().toString()+");\n";
					else if(avl && p.equals(pB))
							resultado ="("+ getAVL(p,op).toString()+");\n";
				}// fin if si es el unico parametro
				else if(m.getParBp().indexOf(p)==cantPar-1 && cantPar!=2){
					//si sos el ultimo parametro
					if(p.getTda().equals("char") && (!avl || !p.equals(pB)))
						resultado =resultado +"'"+p.getValorDef().toString()+"');\n";
					else if(p.getTda().equals("char") && p.equals(pB)&& avl)
						resultado =resultado +"'"+getAVL(p,op)+"');\n";
					else if(p.getTda().equals("String") && (!avl || !p.equals(pB)))
						resultado =resultado +  "\""+p.getValorDef().toString()+"\");\n";
					else if (p.getTda().equals("String")&& p.equals(pB) && avl)
						resultado = resultado + "\"" +getAVL(p,op).toString()+"\");\n";
					else if(!avl || (!p.equals(pB)))
						resultado = resultado + "" +p.getValorDef().toString()+");\n";
					else if (p.equals(pB) && avl)
						resultado = resultado + "" +getAVL(p,op).toString()+");\n";
					}
				else if(m.getParBp().indexOf(p)<cantPar-1 && cantPar!=2){
					//si no es ultimo ni unico
					if(p.getTda().equals("char") && (!avl || (!p.equals(pB))))
						resultado = resultado + "'"+p.getValorDef().toString()+"', ";
					else if(p.getTda().equals("char")&& p.equals(pB) && avl)
						resultado = resultado + "'"+getAVL(p,op).toString()+"', ";
					else if(p.getTda().equals("String") && (!avl || (!p.equals(pB))))
						resultado =resultado + "\""+p.getValorDef().toString()+"\", ";
					else if(p.getTda().equals("String") && p.equals(pB) && avl)
						resultado =resultado + "\""+getAVL(p, op).toString()+"\", ";
					//TODO hay que ver por que al pasar dos parametros 
					//toma los dos iguales cuando hay restriccion
					else if (!avl || (!p.equals(pB)))
						resultado = resultado + p.getValorDef().toString()+", ";
					else if(avl && p.equals(pB))
						resultado = resultado + getAVL(p,op).toString()+", ";	
				}//fin if no es el ultimo
			} //fin if parametro != return
			else if(p.getNombre().equals("return") && cantPar==1)
				resultado = resultado +");\n\n";
		}// for each parametro
		return resultado;
	}
	
	 Object getAVL(Parametro p, int op){
		  GenerarDatosEntradaAVL gd = new GenerarDatosEntradaAVL();
		  Parametro par = new Parametro();
		  par.setTda(p.getTda());
		  String tda = p.getTda();
		  if(op==1 && (tda.equals("byte")|| tda.equals("short")||tda.equals("int")
				  		||tda.equals("long")||tda.equals("float")||tda.equals("double"))){
			  Oraculo or = gd.generaDatos(par, ">=", p.getValorDef().toString());
			  return  or.getValoresIncorrectos().get(0);  
		  }
		  else if(op==1 && (tda.equals("boolean") || tda.equals("String") || tda.equals("char"))){
			  Oraculo or = gd.generaDatos(par, "!=", p.getValorDef().toString());
			  return  or.getValoresIncorrectos().get(0);
		  }
		  else if(op==2 && (tda.equals("byte")|| tda.equals("short")||tda.equals("int")
			  		||tda.equals("long")||tda.equals("float")||tda.equals("double"))){
			  Oraculo or = gd.generaDatos(par, "=<", p.getValorDef().toString());
			  return  or.getValoresIncorrectos().get(0);
		  }
		  else if(op==2 && (tda.equals("boolean") || tda.equals("String") || tda.equals("char"))){
			  Oraculo or = gd.generaDatos(par, "!=", p.getValorDef().toString());
			  return  or.getValoresIncorrectos().get(0);
		  }
		  else
			  return null;
	}
	
	//verificar
	public String formatParametros2(Metodo m, boolean avl, Object valor){
		int cantPar = m.getParametros().size();
		String resultado = "(";
		for(Parametro p : m.getParametros()){
			if(!p.getNombre().equals("return")){
				if(m.getParametros().indexOf(p)==cantPar-1 && cantPar==2){ 
					// es el unico  parmetro
					if(p.getTda().equals("char") && avl==false){
						resultado = "('"+p.getValorDef().toString()+"');\n"; 
					}
					else if(p.getTda().equals("char") && avl==true){
						resultado = "('"+valor.toString()+"');\n"; 
					}
					else if(p.getTda().equals("String") && avl==false){
						resultado = "(\""+p.getValorDef().toString()+"\");\n";	
					}
					else if(p.getTda().equals("String") && avl==true){
						resultado = "(\""+valor.toString()+"\");\n";	
					}
					else if(avl==false){
						resultado ="("+ p.getValorDef().toString()+");\n";
					}
					else if(avl==true){
							resultado ="("+ valor.toString()+");\n";
						}
				}// fin if si es el unico parametro
				else if(m.getParametros().indexOf(p)==cantPar-1 && cantPar!=2){
					//si sos el ultimo parametro
					if(p.getTda().equals("char")&&avl==false){
						resultado =resultado +"'"+p.getValorDef().toString()+"');\n";
					}
					else if(p.getTda().equals("char")&&avl==true){
						resultado =resultado +"'"+valor+"');\n";
					}
					else if(p.getTda().equals("String") && avl==false){
						resultado =resultado +  "\""+p.getValorDef().toString()+"\");\n";
					}
					else if (p.getTda().equals("String")&&avl==true){
						resultado = resultado + "\"" +valor.toString()+"\");\n";
					}
					else if(avl==false){
						resultado = resultado + "" +p.getValorDef().toString()+");\n";
					}
					else if (avl==true){
						resultado = resultado + "" +valor.toString()+");\n";
					}
				}
				else if(m.getParametros().indexOf(p)<cantPar-1 && cantPar!=2){
					//si no es el ultimo
					if(p.getTda().equals("char") && avl==false){
						resultado = resultado + "'"+p.getValorDef().toString()+"', ";
					}
					else if(p.getTda().equals("char")&& avl==true){
						resultado = resultado + "'"+valor.toString()+"', ";
					}
					else if(p.getTda().equals("String") && avl==false){
						resultado =resultado + "\""+p.getValorDef().toString()+"\", ";
					}
					else if(p.getTda().equals("String") && avl==true){
						resultado =resultado + "\""+valor.toString()+"\", ";
					}
					//TODO hay que ver por que al pasar dos parametros 
					//toma los dos iguales cuando hay restriccion
					else if (avl == false){
						resultado = resultado + p.getValorDef().toString()+", ";
					}
					else if(avl == true){
						resultado = resultado + valor.toString()+", ";
					}
				}//fin if no es el ultimo
			} //fin if parametro != return
			else if(p.getNombre().equals("return") && cantPar==1)
			{
				resultado = resultado +");\n\n";
			}
		}// for each parametro
		return resultado;
	}
	
	private String formateoCadena(Object obj, Object tda){
		String r = "";
		if(tda.toString().equals("String")){
			r = "\""+obj.toString()+"\"";
		}
		else if(tda.toString().equals("char")){
			r = "'"+obj.toString()+"'";  
		}
		else{
			r = obj.toString();
		}
		return r;
	}
}//fin de la clase