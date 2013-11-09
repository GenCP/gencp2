
package view.bean;

import java.beans.IntrospectionException;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import org.xml.sax.SAXException;

import view.bean.controlador.CreaModelo;
import view.bean.modelo.Atributo;
import view.bean.modelo.Clase;
import view.bean.modelo.Metodo;
import view.bean.modelo.Modelo;
import view.bean.modelo.Parametro;

public class TreeModelAdapter {
    private Object _instance = null;
    private transient TreeModel _model = null;
    
    private RequestContext afContext;
    
    private Object _instanceProyectos = null;
    private transient TreeModel modelProyectos = null;

    private String listadoArchivosProy="";
    
    public TreeModelAdapter() {
        String arbolModelo = (String)RequestContext.getCurrentInstance().getPageFlowScope().get("arbolModelo");
        if("SI".equals(arbolModelo))
            construirArbolModelo();
        else
            construirArbolProyectos();
    }

    public TreeModel getModel() throws IntrospectionException {
        if (_model == null) {
            _model = new ChildPropertyTreeModel(_instance, "children");       
        }
        return _model;
    }

    public void setListInstance(List instance) {
        _instance = instance;
        _model = null;
    }

    private void construirArbolModelo() {
        afContext = RequestContext.getCurrentInstance();
        ArrayList<TreeItem> rootTreeItems = new ArrayList<TreeItem>();

        String dirArchivoDC = (String)afContext.getPageFlowScope().get("dirArchivoDC");
        dirArchivoDC = dirArchivoDC.replace('/', '\\');

        try {
            //una vez copiado el archivo xmi del diagrama de clases en el servidor
            //toca leerlo y crear un modelo para mostrar en la vista siguiente como un arbol
            CreaModelo cm =
                new CreaModelo(); //lee el xml del diagrama de clases y arma el diagrama (constructor por defecto)
            Modelo modelo = modelo = cm.Hallar(dirArchivoDC); //convierte archivo xml en Modelo
           
            //nodo raiz
            TreeItem treeItemModelo = new TreeItem("Modelo", null);
            rootTreeItems.add(treeItemModelo);

            ArrayList<TreeItem> treeItemModeloChildren = new ArrayList<TreeItem>();
            ArrayList<TreeItem> treeItemClaseChildren = new ArrayList<TreeItem>();
            //ArrayList<TreeItem> treeItemMetodoChildren = new ArrayList<TreeItem>();

            TreeItem treeItemClase = null;
            TreeItem treeItemAtributo = null;
            TreeItem treeItemMetodo = null;
            TreeItem treeItemParametro = null;

            for (Clase clase : modelo.getAllClases()) {
                treeItemClase = new TreeItem(clase.getNombre(), treeItemModelo);
                treeItemModeloChildren.add(treeItemClase);

                for (Atributo a : clase.getAtributos()) {
                    treeItemAtributo = new TreeItem(a.getTipoDato()+" "+a.getNombre(), treeItemClase);
                    treeItemClaseChildren.add(treeItemAtributo);
                }

                for (Metodo m : clase.getMetodos()) {
                    
                    String signaturaMetodo = m.getNombre() + "(";
//System.out.println(signaturaMetodo + m.getParametros().size());
                    for (Parametro p : m.getParametros()) {
                        
                        if( !p.getNombre().equalsIgnoreCase("return") ){
                            signaturaMetodo += p.getTda()+" "+p.getNombre() + ",";
                        }else{
                            signaturaMetodo = p.getTda() + " " + signaturaMetodo;
                        }
                        /*treeItemParametro = new TreeItem(p.getNombre(), null);
                        treeItemMetodoChildren.add(treeItemParametro);*/
                    } //cierre interno de parametros
                    
                    if(m.getParametros().size() > 1){
                        int ultimaComa = signaturaMetodo.length()-1;
                        signaturaMetodo = signaturaMetodo.substring(0, ultimaComa);    
                    }
                    signaturaMetodo += ")";
                    
                    
                    treeItemMetodo = new TreeItem(signaturaMetodo, treeItemClase);
                    treeItemClaseChildren.add(treeItemMetodo);

                    //treeItemMetodo.setChildren(treeItemMetodoChildren);
                    //treeItemMetodoChildren = new ArrayList<TreeItem>();

                } //cierre de metodos
                treeItemClase.setChildren(treeItemClaseChildren);
                treeItemClaseChildren = new ArrayList<TreeItem>();

            } //cierre de clases
            treeItemModelo.setChildren(treeItemModeloChildren);

            this.setListInstance(rootTreeItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//==============================================//
    public TreeModel getModelProyectos() throws IntrospectionException {
        if (modelProyectos == null) {
            modelProyectos = new ChildPropertyTreeModel(_instanceProyectos, "children");       
        }
        return modelProyectos;
    }
    
    public void setListInstanceProyectos(List instanceProyectos) {
        _instanceProyectos = instanceProyectos;
        modelProyectos = null;
    }
    
    private void construirArbolProyectos() {
        
        String directorioActual = System.getProperty("user.dir");
        directorioActual = directorioActual.replace('\\', '/');
        directorioActual += "/Proyectos/";
        
        File dirProy = new File (directorioActual);
        //debemos mostrar el arbol de directorios de la carpeta 'Proyectos'
        
        ArrayList<TreeItem> rootTreeItems = new ArrayList<TreeItem>();
        TreeItem treeItemPrincipal = new TreeItem("Proyectos", null);
        rootTreeItems.add(treeItemPrincipal);
        
        ArrayList<TreeItem> treeItemPrincipalChildrens = new ArrayList<TreeItem>();
        ArrayList<TreeItem> treeItemProyChildrens = new ArrayList<TreeItem>();
        ArrayList<TreeItem> treeItemSubDirChildrens = new ArrayList<TreeItem>();
        
        TreeItem treeItemProy = null;
        TreeItem treeItemSubDir = null;
        TreeItem treeItemArch = null;
        
        listarDirectorio(dirProy, ">");
        
        //System.out.println(listadoArchivosProy);
        
        StringTokenizer listTok = new StringTokenizer(listadoArchivosProy,"#");
        String tk = "";
        String nombreNodo="";
        while(listTok.hasMoreTokens()){
            tk = listTok.nextToken();
            if(tk.startsWith(">>>")){
                //es un archivo
                nombreNodo = tk.substring(tk.indexOf(">>>")+3);
                treeItemArch = new TreeItem(nombreNodo, null);
                treeItemSubDirChildrens.add(treeItemArch);   
            }
            else if(tk.startsWith(">>")){
                //es un subDirectorio del proyecto
                //cuando llego a un directorio, puede ser porque terminaron los archivos del anterior
                if(treeItemSubDir != null){
                    if(treeItemSubDirChildrens != null){
                        treeItemSubDir.setChildren(treeItemSubDirChildrens);
                        //limpio la lista de hijos del directorio anterior
                        treeItemSubDirChildrens = new ArrayList<TreeItem>();    
                    }
                }
                
                nombreNodo = tk.substring(tk.indexOf(">>")+2);
                treeItemSubDir = new TreeItem(nombreNodo, null);    
                treeItemProyChildrens.add(treeItemSubDir);
            }
            else if(tk.startsWith(">")){
                //es un proyecto
                //  agrego las listas al proyecto anterior
                if(treeItemProy != null){
                    if(treeItemProyChildrens != null){
                        treeItemProy.setChildren(treeItemProyChildrens);
                        //limpio las listas, para el proyecto siguiente
                        treeItemProyChildrens = new ArrayList<TreeItem>();    
                        treeItemSubDirChildrens = new ArrayList<TreeItem>();  
                        treeItemSubDir = null;
                        treeItemArch = null;
                    }
                }
                
                //cargo el proyecto siguiente en la lista de proyectos
                nombreNodo = tk.substring(tk.indexOf(">")+1);
                treeItemProy = new TreeItem(nombreNodo, null);    
                treeItemPrincipalChildrens.add(treeItemProy);
            }
        }
        if(treeItemProyChildrens != null && treeItemProyChildrens.size() > 0){
            treeItemProy.setChildren(treeItemProyChildrens); //hijos del ultimo proyecto
        }
        treeItemPrincipal.setChildren(treeItemPrincipalChildrens); //hijos del principal
        this.setListInstanceProyectos(rootTreeItems);   
    }

    public void listarDirectorio(File f, String separador) {
        File[] ficheros = f.listFiles();
        
        for (int x = 0; x < ficheros.length; x++) {
             listadoArchivosProy += separador + ficheros[x].getName()+"#";

            if (ficheros[x].isDirectory()) {
                String nuevo_separador;
                nuevo_separador = separador + ">";
                listarDirectorio(ficheros[x], nuevo_separador);
            }
        }
    }

}
