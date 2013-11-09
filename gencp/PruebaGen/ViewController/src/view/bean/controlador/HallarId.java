package view.bean.controlador;
	import java.io.File;
	import java.io.IOException;
	import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.Node;
	import org.w3c.dom.traversal.DocumentTraversal;
	import org.w3c.dom.traversal.NodeFilter;
	import org.w3c.dom.traversal.NodeIterator;

	public class HallarId {
	
		private boolean b = false;
		private String TipoDato="";
		
		public String Extraer(String ref, String ArchivoOrigen){
			try{	
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				File arch = new File(ArchivoOrigen);
				Document document = builder.parse(arch);
				DocumentTraversal dt = (DocumentTraversal) document;
				NodeIterator it = dt.createNodeIterator(document.getDocumentElement(),
							NodeFilter.SHOW_ELEMENT,new ObjectFilter(),true);
				
				Node n = it.nextNode();
				while (n != null && b==false){
					 rDom(n,ref);
					n = it.nextNode();
					it.getFilter();
				}	
			}//cierre try
			catch(Exception e){
				e.printStackTrace();
			}
		return TipoDato;
		}//fin extraer
		
		public void rDom(Node n, String ref) throws IOException{//recorrer el arbol	
                        
			if("UML:DataType".contains(n.getNodeName())/*&& n.getParentNode().getTextContent().contains("UML:Namespace.ownedElement")*/||
					("UML:Class".contains(n.getNodeName())))
			{
			    //System.out.println("n.getNodeName() -->"+n.getNodeName());
			    //System.out.println("n.getParentNode().getTextContent() -->"+n.getParentNode().getTextContent());
                            
				String id = n.getAttributes().getNamedItem("xmi.id").getTextContent();
				//int y = id.length();
				//id = id.substring(8,y-1);
				if(id.compareTo(ref) == 0){
					TipoDato = n.getAttributes().getNamedItem("name").getTextContent();
                              //          System.out.println("TipoDato -->"+TipoDato);
					//int larg = TipoDato.length()-1;
					//TipoDato = TipoDato.substring(6, larg);
					b=true;
				}	
			}
			
		}
		
		private static class ObjectFilter implements NodeFilter 
		{
			public short acceptNode(Node n) {
				if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if (e.getAttributeNode("xmi.id") != null)
					return NodeFilter.FILTER_ACCEPT;
				}
					return NodeFilter.FILTER_REJECT;
				}
		}
	}//cierre de la clase