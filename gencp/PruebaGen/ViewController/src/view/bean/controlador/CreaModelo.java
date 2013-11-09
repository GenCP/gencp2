package view.bean.controlador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import view.bean.controlador.HallarId;
import view.bean.modelo.Asociacion;
import view.bean.modelo.Atributo;
import view.bean.modelo.Clase;
import view.bean.modelo.ExtremoAsociacion;
import view.bean.modelo.Generalizacion;
import view.bean.modelo.Metodo;
import view.bean.modelo.Modelo;
import view.bean.modelo.Parametro;

public class CreaModelo {

	public static String _origen =" ";
	public Modelo modelo = new Modelo();
	
	/**
	 * recibe un archivo xml y construye un objeto de tipo Modelo, a partir de el
	 * @param archivo
	 * @return objeto de tipo Modelo
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public Modelo Hallar(String archivo)  throws IOException, ParserConfigurationException, 
															SAXException{
		_origen = archivo;
		DocumentBuilder builder = 
							DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		File arch = new File(_origen);
		Document document = builder.parse(arch);
		DocumentTraversal dt = (DocumentTraversal) document;
		
		NodeIterator it = dt.createNodeIterator(document.getDocumentElement(),
							NodeFilter.SHOW_ELEMENT,new ObjectFilter(),true);
		
		Node n = it.nextNode();
		while (n != null){

			Clase clase = HallarClase(n, _origen);
		
			if(!"null".contains(clase.getNombre())){
				modelo.addClase(clase);
			}
			
			Asociacion a = hallarAsociaciones(n);
			
			if(!a.getExtremos().isEmpty()){
				modelo.addAso(a);
			}
			
			Generalizacion ge = hallarGen(n);
			
			if(ge.getPadre()!=null){
				modelo.addGen(ge);
			}
			
			n = it.nextNode();
			it.getFilter();
		}
		
		actualizaHerencia();
		return modelo;
	}//cierre del metodo
		


	public static Clase HallarClase(Node n, String _origen) throws IOException {//recorrer el arbol
		if(n.getNodeName().contains("UML:Class")
					&& "UML:Namespace.ownedElement".contains(n.getParentNode().getLocalName()) 
								&&n.hasChildNodes()){
			
			String nClase=n.getAttributes().getNamedItem("name").getTextContent();
			String visClase = n.getAttributes().getNamedItem("visibility").getTextContent();
			//int cVis = visClase.length()-1;
			//int cNom = nClase.length() -1;
			//nClase= nClase.substring(6, cNom);
			//visClase = visClase.substring(12,cVis);
			ArrayList<Metodo> metodos =	hallarMetodos(n);
			ArrayList<Atributo> atributos = hallarAtributos(n);
			Clase c = new Clase();
			c.setNombre(nClase);
			c.setVisib(visClase);
			c.setMetodos(metodos);
			c.setAtributos(atributos);
		return c;
		}//cierre del if de clase
		else{
			Clase c = new Clase();
			c.setNombre("null");
		return c;
		}
	}//cierre metodo HallarClases
	
	public static ArrayList<Metodo> hallarMetodos(Node n){
		ArrayList<Metodo> metodos = new ArrayList<Metodo>();
		NodeList nodosClase = n.getChildNodes(); //los hijos del nodo clase	
		for(int x=0; x<nodosClase.getLength(); x++){
			Node n2 = nodosClase.item(x);
			if ("UML:Classifier.feature".contains(n2.getNodeName())){					
				NodeList nl2 = n2.getChildNodes(); //hijos del nodo classifier.feature, es decir atributos y clases.		
				for(int y=0; y<nl2.getLength(); y++){ //iterando por classifier clasifier.feature
					Node n3 = nl2.item(y);
					if("UML:Operation".contains(n3.getNodeName())){
						String nomMetodo = n3.getAttributes().getNamedItem("name").getTextContent();
						String visMetodo = n3.getAttributes().getNamedItem("visibility").getTextContent();
						//int cantM = nomMetodo.length() -1;
						//nomMetodo = nomMetodo.substring(6, cantM);
						//int	VisMet = visMetodo.length()-1;
						//visMetodo = visMetodo.substring(12, VisMet);
						Metodo m = new Metodo();
						m.setNombre(nomMetodo);
						ArrayList<Parametro> parametros=hallarParametros(n3);
						m.setParametros(parametros);
						m.setVisib(visMetodo);
						metodos.add(m);
					}//IF de Operation
				}//FOR de Classifier.feature	
			}//if de classifier.feature
		}//FOR de Class
		return metodos;
	}//cierre del metodo
	
	public static ArrayList<Atributo> hallarAtributos(Node n){
		ArrayList<Atributo> atributos = new ArrayList<Atributo>();
		NodeList nodosAtt = n.getChildNodes(); //los hijos del nodo clase	
		for(int x=0; x<nodosAtt.getLength(); x++){
			Node n2 = nodosAtt.item(x);
			if ("UML:Classifier.feature".contains(n2.getNodeName())){					
				NodeList nl2 = n2.getChildNodes(); //hijos del nodo classifier.feature, es decir atributos y clases.
				for(int y=0; y<nl2.getLength(); y++){ //iterando por classifier clasifier.feature
					Node n3 = nl2.item(y);
					if("UML:Attribute".contains(n3.getNodeName())){
						String nom = n3.getAttributes().getNamedItem("name").getTextContent();
						String vis = n3.getAttributes().getNamedItem("visibility").getTextContent();
						//int cantA = nom.length()-1;
						//nom = nom.substring(6, cantA);
						//int cantV = vis.length()-1;
						//vis = vis.substring(12, cantV);
			
						NodeList nodosAtri = n3.getChildNodes();
						for(int z=0; z<nodosAtri.getLength(); z++){  
							Node n4 = nodosAtri.item(z);
							if("UML:StructuralFeature.type".contains(n4.getNodeName()) || 
									"UML2:TypedElement.type".contains(n4.getNodeName())){ //UML es Argo , UML2 es Poseidon
								NodeList nl = n4.getChildNodes();
								for(int a=0; a<nl.getLength();a++){
									Node n5 = nl.item(a);
									if("UML:DataType".contains(n5.getNodeName())||("UML:Class".contains(n5.getNodeName()))){
										String refAtt = n5.getAttributes().getNamedItem("xmi.idref").getTextContent();
										//int larg = refAtt.length();
										//refAtt=refAtt.substring(11,larg-1);
										HallarId h = new HallarId();
										String tda = h.Extraer(refAtt, _origen);
										Atributo att = new Atributo();
										att.setNombre(nom);
										att.setModificador(vis);
										att.setTipoDato(tda);
										atributos.add(att);
									}
								}
							}
						}
					}// Cierre del IF Attribute
				}
			}//if classifier.feature
		}
		return atributos;
	}
			
	public static ArrayList<Parametro> hallarParametros(Node n3){
		ArrayList<Parametro> parametros = new ArrayList<Parametro>();
		NodeList nl3 = n3.getChildNodes();//hijos de Operation
			for(int zz=0; zz<nl3.getLength(); zz++){  //iterando por operation
				Node nn = nl3.item(zz);
				if("UML:BehavioralFeature.parameter".contains(nn.getNodeName())){
					NodeList nl4 = nn.getChildNodes();//hijos de behavioral 
					for(int a=0; a<nl4.getLength(); a++){
						Node n5 = nl4.item(a);
						if("UML:Parameter".contains(n5.getNodeName())){
							String nomParm = n5.getAttributes().getNamedItem("name").getTextContent();
							//int cantP = nomParm.length()-1;
							//nomParm = nomParm.substring(6, cantP);			
							NodeList nl5 = n5.getChildNodes();//hijos de parameter
								for(int b=0; b<nl5.getLength(); b++){
									Node n6 = nl5.item(b);
									if("UML:Parameter.type".contains(n6.getNodeName()) ||
											("UML2:TypedElement.type".contains(n6.getNodeName()))){
										NodeList nl6 =  n6.getChildNodes();
										for(int c=0; c<nl6.getLength(); c++){ // iterando por los hijos de  parameter type, para encontrar data type
											Node n7 = nl6.item(c);
											if("UML:DataType".contains(n7.getNodeName().toString()) 
													||"UML:Class".contains(n7.getNodeName())){
											String idref = n7.getAttributes().getNamedItem("xmi.idref").getTextContent();
											//int larg = idref.length();
											//idref = idref.substring(11, larg-1);
											HallarId h2 = new HallarId();
											String tipoDato = h2.Extraer(idref, _origen);
											Parametro p = new Parametro();
											p.addTipo(tipoDato);
											p.setNombre(nomParm);
											parametros.add(p); 
											}//cierre de if del Tipo de Dato
										}//cierre del for de Paramater.type
									}//if de parameter.type
								}//for de parameter
							}//if de parameter
						}//for de behavioral
					}//if de behavioralfeature.parameter
				}// for de operacion
				return parametros;
			}
        
	public static Asociacion hallarAsociaciones(Node n){//recorrer el arbol	
		Asociacion as = new Asociacion();
		if(n.getNodeName().contains("UML:Association")&& 
						"UML:Namespace.ownedElement".contains(n.getParentNode().getLocalName())){
				
					NamedNodeMap att = n.getAttributes();
					for(int a=0; a<att.getLength(); a++){
						Node nodo = att.item(a);
						if("name".contains(nodo.getTextContent())){
							String nomAso=n.getAttributes().getNamedItem("name").getTextContent();
							//int cNomAso = nomAso.length()-1;
							//nomAso= nomAso.substring(6, cNomAso);
							as.setNombre(nomAso);				
						}
					}//cierre de for
					Node n2 = n.getChildNodes().item(1);
					if("UML:Association.connection".contains(n2.getNodeName())){
						NodeList nl2 = n2.getChildNodes();//nl2 son nodos de connection
						for(int y=0; y<nl2.getLength();y++){
							Node n3 = nl2.item(y);
							if("UML:AssociationEnd".contains(n3.getNodeName())){
								String navegable=n3.getAttributes().getNamedItem("isNavigable").getTextContent();
								String tipoAgg=n3.getAttributes().getNamedItem("aggregation").getTextContent();
								//int cNav = navegable.length()-1;
								//navegable=navegable.substring(13, cNav);
								//int cTip = tipoAgg.length()-1;
								//tipoAgg=tipoAgg.substring(13, cTip);
								
								ExtremoAsociacion ea = new ExtremoAsociacion();
								ea.setEsNavegable(navegable);
								ea.setTipoAso(tipoAgg);

								String lower = " ";
								String upper = " ";
								
								NodeList nl3 = n3.getChildNodes();
								for(int x=0;x<nl3.getLength();x++){
									Node n4=nl3.item(x);
									if("UML:AssociationEnd.multiplicity".contains(n4.getNodeName())){
										Node n5 = n4.getChildNodes().item(1);
										if("UML:Multiplicity".contains(n5.getNodeName())){
											Node n6 = n5.getChildNodes().item(1);
											if("UML:Multiplicity.range".contains(n6.getNodeName())){
												Node n7 = n6.getChildNodes().item(1);
												if("UML:MultiplicityRange".contains(n7.getNodeName())){
													 lower=n7.getAttributes().getNamedItem("lower").getTextContent();
													 upper=n7.getAttributes().getNamedItem("upper").getTextContent();
													 //int cLow = lower.length()-1;
													 //lower = lower.substring(7, cLow);
													 //int cUpp = upper.length()-1;
													 //upper = upper.substring(7, cUpp);
													 ea.setLimiteSup(upper);
													 ea.setLimiteInf(lower);													
												}
											}				
										}
									}
									else //sino es AssociationEnd.multiplicity
									if("UML:AssociationEnd.participant".contains(n4.getNodeName())){
										Node n9 = n4.getChildNodes().item(1);
										if("UML:Class".contains(n9.getNodeName())){
											String idref = n9.getAttributes().getNamedItem("xmi.idref").getTextContent();
											//int c = idref.length();
											//idref=idref.substring(11,c-1);
											HallarId h = new HallarId();
											String nomClase = h.Extraer(idref, _origen);		
											ea.setNomClase(nomClase);	
										}
									}
								}
								as.setExtremo(ea);
							}
						}
					}
			} //cierre primer if
			return as;
	}
	
	public Generalizacion hallarGen(Node n){
		Generalizacion ge = new Generalizacion();
		if(n.getNodeName().contains("UML:Generalization")&& 
				"UML:Namespace.ownedElement".contains(n.getParentNode().getLocalName())) {
					NodeList nl = n.getChildNodes();//child y parent
					for(int x=0; x<nl.getLength();x++){
						Node n2 = nl.item(x);  
						if(n2.hasChildNodes()){
							NodeList nl2 = n2.getChildNodes(); //hijos de child y parent o sea Class
							for(int y=0; y<nl2.getLength(); y++){
								Node n3 = nl2.item(y); //class
								if(n3.hasAttributes()){				
									String idref = n3.getAttributes().getNamedItem("xmi.idref").getTextContent();
									//int c = idref.length();
									//idref=idref.substring(11,c-1);
									HallarId h = new HallarId();
									String clase = h.Extraer(idref, _origen);			
									if("UML:Generalization.parent".contains(n2.getNodeName())){
										ge.setPadre(clase);
									}
									else{
										ge.setHijo(clase);
									}
								}
							}
						}
					}
		}
		return ge;
	}
	private void actualizaHerencia() 
	{
		for(Generalizacion gen : modelo.getAllGen())
		{
			String nomClasePadre = gen.getPadre();
			String nomClaseHijo = gen.getHijo();
			Clase padre = modelo.getClase(nomClasePadre);
			Clase hijo = modelo.getClase(nomClaseHijo);
			for(Metodo m : padre.getMetodos())
			{
				if("public".contains(m.getVisib()) )
				{
					hijo.setMetodo(m);
				}
			}
		}
		
	}
		
	private static class ObjectFilter implements NodeFilter	{
		public short acceptNode(Node n) {
//			if (n.getNodeType() == Node.ELEMENT_NODE) {
//				Element e = (Element) n;
//				if (e.getAttributeNode("xmi.id") != null)
				return NodeFilter.FILTER_ACCEPT;
//				}
//					return NodeFilter.FILTER_REJECT;
				}
		}
}