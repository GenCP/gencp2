package view.Backing;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.StringTokenizer;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputFile;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.datatransfer.DataFlavor;
import oracle.adf.view.rich.datatransfer.Transferable;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DropEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.change.AttributeComponentChange;
import org.apache.myfaces.trinidad.change.ChangeManager;
import org.apache.myfaces.trinidad.change.MoveChildComponentChange;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.model.UploadedFile;

import view.bean.controlador.OraculoNuevo;
import view.bean.modelo.Modelo;

import view.bean.modelo.Secuencia;

import view.bean.modelo.sut.ClaseBajoPrueba;

import view.bean.modelo.sut.MetodoBajoPrueba;

import view.bean.modelo.sut.ParametroBajoPrueba;

import webservice.common.AppMoWebService;


public class BackingGenCP {
    private DCBindingContainer bc;
    private BindingContainer bindings;
    private RequestContext afContext;
    private FacesContext fctx;
    private UploadedFile uploadedFile;
    private RichInputFile inputFile;
    private String filename;
    private long filesize;
    private String filecontents;
    private String filetype;
    
    final static String [] carpetas = {"CU/","AD/","TO/","DC/","JU/"};
    private RichInputText metodoFB_InputText; //array de las carpetas internas de un proyecto
    private RichInputText metodoFA_InputText;
    private RichInputText metodoFE_InputText;

    private RichOutputText outputPrueba;
    private RichPanelFormLayout panelformlayout;
    private RichTree arbolProyectos;
    private RichInputText prueba_inputText;
    private RichPopup popupFB;
    private RichPopup popupFA;
    private RichPopup popupFE;


    public BackingGenCP() {
        afContext = RequestContext.getCurrentInstance();
        fctx = FacesContext.getCurrentInstance();
        bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String EjecutaBinding(String nombinding) {
        if (nombinding == null)
            nombinding = "Create";

        BindingContainer bindings = getBindings();
        OperationBinding operationBinding = 
            bindings.getOperationBinding(nombinding);
        Object result = operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return operationBinding.getErrors().toString();
        }
        return null;
    }

    public BindingContainer getBindings() {
        return bindings;
    }

    public void setBindings(BindingContainer bindings) {
        this.bindings = bindings;
    }

    /**
     * agrega un paso en el flujo basico
     * @return
     */
    public String agregarPasoFlujoBasico() {
        DCIteratorBinding ibfb = bc.findIteratorBinding("FlujoBasicoView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        
        Long ultimaPos = new Long(ibfb.getEstimatedRowCount());
        //crea una nueva fila para la tabla
        Row newRow = ibfb.getRowSetIterator().createRow();
        newRow.setAttribute("nroPasoFB", ibv.getCurrentRow().getAttribute("vNumeroFB"));
        newRow.setAttribute("descPasoFB", ibv.getCurrentRow().getAttribute("vDescripcionFB"));
        newRow.setAttribute("metodosAsocFB", this.metodoFB_InputText.getValue());
        ibfb.getRowSetIterator().insertRowAtRangeIndex(ultimaPos.intValue(), newRow);
        
        afContext.getPageFlowScope().put("nroPasoFB", ibfb.getEstimatedRowCount()+1);
        ibv.getCurrentRow().setAttribute("vNumeroFB", ibfb.getEstimatedRowCount()+1);
        
        ibv.getCurrentRow().setAttribute("vDescripcionFB","");
        this.metodoFB_InputText.setValue("");
        this.metodoFB_InputText.resetValue();
        ibv.getCurrentRow().setAttribute("vMetodoFB","");
        
        return null;
    }
    
    /**
     * agrega un paso en el flujo alternativo
     * @return
     */
    public String agregarPasoFlujoAlternativo(){
        DCIteratorBinding ibfa = bc.findIteratorBinding("FlujoAlternativoView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        Long ultimaPos = new Long(ibfa.getEstimatedRowCount());
        // crea una nueva fila para la tabla
        Row newRow = ibfa.getRowSetIterator().createRow();
        newRow.setAttribute("nroPasoFA", ibv.getCurrentRow().getAttribute("vNumeroFA"));
        newRow.setAttribute("descPasoFA", ibv.getCurrentRow().getAttribute("vDescripcionFA"));
        newRow.setAttribute("metodosAsocFA", this.metodoFA_InputText.getValue());
        
        ibfa.getRowSetIterator().insertRowAtRangeIndex(ultimaPos.intValue(), newRow);

        ibv.getCurrentRow().setAttribute("vNumeroFA", "");
        ibv.getCurrentRow().setAttribute("vDescripcionFA","");
        this.metodoFA_InputText.setValue("");
        this.metodoFA_InputText.resetValue();
        ibv.getCurrentRow().setAttribute("vMetodoFA","");
        
        
        return null;
    }

    /**
     * agrega un paso en el flujo de error
     * @return
     */
    public String agregarPasoFlujoError() {
        DCIteratorBinding ibfe = bc.findIteratorBinding("FlujoErrorView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        
        Long ultimaPos = new Long(ibfe.getEstimatedRowCount());
        // crea una nueva fila para la tabla
        Row newRow = ibfe.getRowSetIterator().createRow();
        newRow.setAttribute("nroPasoFE", ibv.getCurrentRow().getAttribute("vNumeroFE"));
        newRow.setAttribute("descPasoFE", ibv.getCurrentRow().getAttribute("vDescripcionFE"));
        newRow.setAttribute("metodosAsocFE", this.metodoFE_InputText.getValue());
        
        ibfe.getRowSetIterator().insertRowAtRangeIndex(ultimaPos.intValue(), newRow);
        
        ibv.getCurrentRow().setAttribute("vNumeroFE", "");
        ibv.getCurrentRow().setAttribute("vDescripcionFE","");
        this.metodoFE_InputText.setValue("");
        this.metodoFE_InputText.resetValue();
        ibv.getCurrentRow().setAttribute("vMetodoFE","");
        
        return null;
    }
    
    /**
     * Genera la plantilla y la guarda en un archivo xml
     * @return
     */
    public String generarSecuencia() {
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        String ubicacionPlantilla = generarPlantilla();
        
        if(ubicacionPlantilla == null){
            //no pasa nada, xq algo fallo en la generación de la plantilla
            bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", "No se pudo crear archivo de plantilla");
            return null;    
        }
        
        String nombreCU = ibv.getCurrentRow().getAttribute("vNombreCU")==null?"":ibv.getCurrentRow().getAttribute("vNombreCU").toString();
        
        String nombreProyecto = (String)afContext.getPageFlowScope().get("nombreProyecto");
        
        String directorioActual = System.getProperty("user.dir");
        directorioActual = directorioActual.replace('\\', '/');
        directorioActual += "/Proyectos/"+nombreProyecto;
        directorioActual += "/TO/"+nombreCU+".xml";
        
        String ubicacionTestObject = directorioActual;
        
        if(ubicacionPlantilla != null){
            //si se guardo la plantilla
            String resultadoGeneracion = generarDiagramaDeActividades(ubicacionPlantilla,nombreCU);
            if(resultadoGeneracion != null){
                //si se creo el diagrama de actividades
                String secuencias = generarSecuenciasEnWS(ubicacionPlantilla, ubicacionTestObject);
                if( secuencias != null ){
                    afContext.getPageFlowScope().put("Secuencias", secuencias);
                    System.out.println("Secuencias Generadas--> "+secuencias);    
                } 
            }
        }
        
        afContext.getPageFlowScope().put("nombreProyecto", nombreProyecto); //seteamos para ir a la otra pagina
        afContext.getPageFlowScope().put("nombreCU", nombreCU);         
        return "generarTO";
    }
    
    private boolean guardarPlantilla(String nombreCU, String contenido){
        nombreCU = "".equals(nombreCU)?"UC-000 NoName":nombreCU;
        Boolean Est = false; //guarda si el archivo se creo exitosamente o no
        
        try{
            //nombre proyecto, traido de la vista anterior
            String nombreProyecto = (String)afContext.getPageFlowScope().get("nombreProyecto");
            
            //si corre en el servidor seria C:\Users\sebas\AppData\Roaming\JDeveloper\system11.1.2.0.38.60.17\DefaultDomain\
            String ubicacion = System.getProperty("user.dir");
            //hacemos la conversion de los slash para poder pasarle al FileWriter
            ubicacion = ubicacion.replace('\\', '/');
            ubicacion += "/Proyectos/"+nombreProyecto+"/CU/"+nombreCU+".xml";
            FileWriter Carga_Escritura = new FileWriter(ubicacion);
            BufferedWriter Lev_Text = new BufferedWriter(Carga_Escritura);

            if(contenido == null){
                contenido = "";
            }
    
            Lev_Text.write(contenido);
            Lev_Text.close();
            Carga_Escritura.close();
    
            Est = true;
        }catch(FileNotFoundException e) {
            System.out.println("El Archivo No se encontro");
            Est = false;
        }catch(IOException e){
            System.out.println("ERROR: No se puede leer el archivo");
            Est = false;
        }
        
        return Est;        
    }

    private String generarPlantilla(){
        DCIteratorBinding ibfb = bc.findIteratorBinding("FlujoBasicoView1Iterator");
        DCIteratorBinding ibfa = bc.findIteratorBinding("FlujoAlternativoView1Iterator");
        DCIteratorBinding ibfe = bc.findIteratorBinding("FlujoErrorView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        
        String ubicacionPlantilla = null;
        
        String descripcionCU = ibv.getCurrentRow().getAttribute("vDescripcionCU")==null?"":ibv.getCurrentRow().getAttribute("vDescripcionCU").toString();
        String precondicionCU = ibv.getCurrentRow().getAttribute("vPrecondicionCU")==null?"":ibv.getCurrentRow().getAttribute("vPrecondicionCU").toString();
        String postcondicionCU = ibv.getCurrentRow().getAttribute("vPostcondicionCU")==null?"":ibv.getCurrentRow().getAttribute("vPostcondicionCU").toString();
        String nombreCU = ibv.getCurrentRow().getAttribute("vNombreCU")==null?"":ibv.getCurrentRow().getAttribute("vNombreCU").toString();
        
        String useCaseS = "";
        String flujoBasicoS = "";
        String flujoAlternativoS = "";
        String flujoErrorS = "";
        
        String metodosFlujoBasico = "";
        String metodosFlujoAlternativo = "";
        String metodosFlujoError = "";
            
        //hay flujo basico
        if(ibfb.getEstimatedRowCount() > 0){
            flujoBasicoS +="<mainSequence>";
            metodosFlujoBasico +="<mainSequenceMethods>";
            for(int i=0; i < ibfb.getEstimatedRowCount(); i++){
                ibfb.setCurrentRowIndexInRange(i);
                
                String paso = ibfb.getCurrentRow().getAttribute("nroPasoFB")==null?"":ibfb.getCurrentRow().getAttribute("nroPasoFB").toString();
                String desc = ibfb.getCurrentRow().getAttribute("descPasoFB")==null?"":ibfb.getCurrentRow().getAttribute("descPasoFB").toString();
                String meto = ibfb.getCurrentRow().getAttribute("metodosAsocFB")==null?"":ibfb.getCurrentRow().getAttribute("metodosAsocFB").toString();
                if("".equals(paso) || "".equals(desc)){
                    System.out.println("La fila no puede contener campos vacios");
                    break;
                }
                    
                flujoBasicoS += "<step id=\""+paso+"\">"+desc+"</step>";
                metodosFlujoBasico += "<method id=\""+paso+"\">"+meto+"</method>";
            }
            flujoBasicoS += "</mainSequence>";
            metodosFlujoBasico +="</mainSequenceMethods>";
                
        }else{
            System.out.println("No hay flujo básico. No es posible continuar");
            return null;
        }
        
        //hay flujo alternativo
        if(ibfa.getEstimatedRowCount() > 0){
            flujoAlternativoS +="<alternativeSteps>";
            metodosFlujoAlternativo += "<alternativeStepsMethods>";
            for(int i=0; i < ibfa.getEstimatedRowCount(); i++){
                ibfa.setCurrentRowIndexInRange(i);
                
                String paso = ibfa.getCurrentRow().getAttribute("nroPasoFA")==null?"":ibfa.getCurrentRow().getAttribute("nroPasoFA").toString();
                String desc = ibfa.getCurrentRow().getAttribute("descPasoFA")==null?"":ibfa.getCurrentRow().getAttribute("descPasoFA").toString();
                String meto = ibfa.getCurrentRow().getAttribute("metodosAsocFA")==null?"":ibfa.getCurrentRow().getAttribute("metodosAsocFA").toString();
                if("".equals(paso) || "".equals(desc)){
                    System.out.println("La fila no puede contener campos vacios");
                    break;
                }
                    
                flujoAlternativoS += "<astep id=\""+paso+"\">"+desc+"</astep>";
                metodosFlujoAlternativo += "<amethod id=\""+paso+"\">"+meto+"</amethod>";
            }
            flujoAlternativoS += "</alternativeSteps>";
            metodosFlujoAlternativo += "</alternativeStepsMethods>"; 
            
        }
        
        //hay flujo de error
        if(ibfe.getEstimatedRowCount() > 0){
            flujoErrorS +="<errorSteps>";
            metodosFlujoError +="<errorStepsMethods>";
            for(int i=0; i < ibfe.getEstimatedRowCount(); i++){
                ibfe.setCurrentRowIndexInRange(i);
                
                String paso = ibfe.getCurrentRow().getAttribute("nroPasoFE")==null?"":ibfe.getCurrentRow().getAttribute("nroPasoFE").toString();
                String desc = ibfe.getCurrentRow().getAttribute("descPasoFE")==null?"":ibfe.getCurrentRow().getAttribute("descPasoFE").toString();
                String meto = ibfe.getCurrentRow().getAttribute("metodosAsocFE")==null?"":ibfe.getCurrentRow().getAttribute("metodosAsocFE").toString();
                if("".equals(paso) || "".equals(desc)){
                    System.out.println("La fila no puede contener campos vacios");
                    break;
                }
                    
                flujoErrorS += "<estep id=\""+paso+"\">"+desc+"</estep>";
                metodosFlujoError += "<emethod id=\""+paso+"\">"+meto+"</emethod>";
            }
            flujoErrorS += "</errorSteps>";
            metodosFlujoError += "</errorStepsMethods>";
        }
        
        //construyo el contenedor principal
        useCaseS += "<useCase id=\""+nombreCU+"\">";
        useCaseS += "<description>"+descripcionCU+"</description>";
        useCaseS += "<precondition>"+precondicionCU+"</precondition>";
        useCaseS += "<postcondition>"+postcondicionCU+"</postcondition>";
        
        useCaseS += flujoBasicoS+flujoAlternativoS+flujoErrorS; //agregamos los flujos
        useCaseS += metodosFlujoBasico+metodosFlujoAlternativo+metodosFlujoError; //agregamos los metodos
        useCaseS += "</useCase>"; //cerramos el contenedor
        
        //genera el archivo xml de la plantilla
        if (guardarPlantilla(nombreCU,useCaseS)){
            System.out.println("se creo bien el archivo");    
            
            //nombre del proyecto traido de la vista anterior
            String nombreProyecto = (String)afContext.getPageFlowScope().get("nombreProyecto");
            
            String directoriActual = System.getProperty("user.dir");
            directoriActual = directoriActual.replace('\\', '/');
            directoriActual += "/Proyectos/"+nombreProyecto+"/CU/"+nombreCU+".xml";
            System.out.println("directoriActual-->"+directoriActual);
            ubicacionPlantilla = directoriActual;
            
        }else{
            System.out.println("algo paso y exploto todo!");    
            ubicacionPlantilla = null;
        }
        
        return ubicacionPlantilla;
    }
 
    public String generarDiagramaDeActividades(String templateDir, String nombreCU){
        
        AppMoWebService appMoWebService = null;
        String resultado = "";
        
        nombreCU = "".equals(nombreCU)?"AD-000 NoName":nombreCU;
        
        try{
            String amWS = "webservice.AppMoWebService";
            String config = "AppMoWebServiceLocal";
            
            appMoWebService = (AppMoWebService)oracle.jbo.client.Configuration.createRootApplicationModule(amWS,config);
            
            String nombreProyecto = (String)afContext.getPageFlowScope().get("nombreProyecto");
            
            String activityDiagramDir = System.getProperty("user.dir");
            //hacemos la conversion de los slash para poder pasarle al FileWriter
            activityDiagramDir = activityDiagramDir.replace('\\', '/');
            activityDiagramDir += "/Proyectos/"+nombreProyecto+"/AD/"+nombreCU+".xmi";
            
            resultado = appMoWebService.WSGenerarActivityDiagram(templateDir, activityDiagramDir);
            
            if(resultado.indexOf("Error") > -1){
                //si no hubo error entra aqui e informa
                bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", "El archivo se creó exitosamente en "+activityDiagramDir);    
            }else{
                bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", resultado);    
            }
            
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println("Error al generar archivo");
            bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", resultado); 
        }
        return "".equals(resultado)?null:resultado;        
    }

    public String generarSecuenciasEnWS(String templateDir, String testObjectiveFile){
        
        AppMoWebService appMoWebService = null;
        String resultado = "";
        
        try{
            String amWS = "webservice.AppMoWebService";
            String config = "AppMoWebServiceLocal";
            
            appMoWebService = (AppMoWebService)oracle.jbo.client.Configuration.createRootApplicationModule(amWS,config);
            
            resultado = appMoWebService.WSGenerarSecuencias(templateDir, testObjectiveFile);
            
            /*if(resultado.indexOf("Error") > -1){
                //si no hubo error entra aqui e informa
                bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", "La secuencia se generó exitosamente");    
            }else{
                bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", resultado);    
            }*/
            
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println("Error al generar archivo");
            bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", resultado); 
        }
        return "".equals(resultado)?null:resultado;        
    }

    public void setInputFile(RichInputFile inputFile) {
        this.inputFile = inputFile;
    }

    public RichInputFile getInputFile() {
        return inputFile;
    }

    public void prueba(){
         
    }

    public String crearProyecto() {
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        String nombreProyecto = (String)ibv.getCurrentRow().getAttribute("vNombreProyecto");
        FileWriter diagramaClases = null;
        PrintWriter pw = null;
        
        try{
            if(nombreProyecto == null){
                ibv.getCurrentRow().setAttribute("vMensajes","Se requiere nombre de proyecto");
                return null;
            }
            
            afContext.getPageFlowScope().put("nombreProyecto", nombreProyecto);
            
            String directorioActual = System.getProperty("user.dir");
            directorioActual = directorioActual.replace('\\', '/');
            directorioActual += "/Proyectos/";
            
            File dirProy = new File (directorioActual);
            //hacemos una comprobación de que el directorio no exista ya (el proyecto ya fue creado)
            boolean yaExisteDir=false;
            String [] listado = dirProy.list();
            for(int pos=0; pos < listado.length; pos++) {
                if(listado[pos].compareTo(nombreProyecto) == 0){
                    yaExisteDir = true;
                }
            }
            
            if(yaExisteDir){
                ibv.getCurrentRow().setAttribute("vMensajes","Ya existe un proyecto con este nombre, elija otro");
                return null;
            }
            
            //crea directorio del proyecto, si no existe aun
            directorioActual += nombreProyecto+"/";
            dirProy = new File (directorioActual);
            if( !dirProy.mkdir() ){
                ibv.getCurrentRow().setAttribute("vMensajes", "El directorio "+directorioActual+" no fue creado"); 
                return null;
            }
            
            //crea directorios internos
            for(int i=0; i < carpetas.length; i++){
                String directorioInterno = directorioActual + carpetas[i];
                File dirInterno = new File(directorioInterno);
                if( !dirInterno.mkdir() ){
                    ibv.getCurrentRow().setAttribute("vMensajes", "El directorio "+directorioInterno+" no fue creado"); 
                    return null;                    
                }
            }
            
            String dirArchivoDC = "";
            String directorioDC = "";
            
            
            //cargamos el archivo del diagrama de clases
            if(this.uploadedFile != null){
                directorioDC = directorioActual + carpetas[3];
                dirArchivoDC = directorioDC + this.filename;
                diagramaClases = new FileWriter(dirArchivoDC);
                pw = new PrintWriter(diagramaClases);
                pw.print(this.filecontents);
            }else{
                ibv.getCurrentRow().setAttribute("vMensajes", "Problemas al cargar archivo"); 
                return null;
            }
        
            //llevamos la construccion del arbol en la siguiente pagina
            afContext.getPageFlowScope().put("dirArchivoDC", dirArchivoDC);
        
        }catch(Exception e){
            e.printStackTrace();    
        }finally {
          try {
          // aprovechamos el finally para
          // asegurarnos que se cierra el fichero.
          if (null != diagramaClases)
             diagramaClases.close();
          } catch (Exception e2) {
             e2.printStackTrace();
          }
       }
        
        return "generarSec";
    }

    public void setUploadedFile(UploadedFile uploadedFile) {        
        this.uploadedFile = uploadedFile;
        this.filename = uploadedFile.getFilename();
        this.filesize = uploadedFile.getLength();
        this.filetype = uploadedFile.getContentType();

        try {
            this.filecontents = parseISToString(uploadedFile.getInputStream());
        } catch (IOException e) {
            bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", "Problemas al cargar archivo");
        }
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
    
    public String parseISToString(java.io.InputStream is) {
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try {
            String line = null;
            while ((line = din.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception ex) {
            ex.getMessage();
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }


    public DnDAction modelDropListener(DropEvent dropEvent) {
        Transferable dropTransferable = dropEvent.getTransferable();
        Object[] drinks = dropTransferable.getData(DataFlavor.OBJECT_ARRAY_FLAVOR);

        if (drinks != null) {
            UIComponent dropComponent = dropEvent.getDropComponent();

            // Update the specified property of the drop component with the Object[] dropped
            dropComponent.getAttributes().put("value", Arrays.toString(drinks));

            return DnDAction.COPY;

        } else {

            return DnDAction.NONE;

        }
    }


    public void setMetodoFB_InputText(RichInputText metodoFB_InputText) {
        this.metodoFB_InputText = metodoFB_InputText;
    }

    public RichInputText getMetodoFB_InputText() {
        return metodoFB_InputText;
    }



    public void setOutputPrueba(RichOutputText outputPrueba) {
        this.outputPrueba = outputPrueba;
    }

    public RichOutputText getOutputPrueba() {
        return outputPrueba;
    }

    public void setPanelformlayout(RichPanelFormLayout panelformlayout) {
        this.panelformlayout = panelformlayout;
    }

    public RichPanelFormLayout getPanelformlayout() {
        return panelformlayout;
    }

    public DnDAction handleItemDropFE(DropEvent dropEvent) {
        DCIteratorBinding ibfe = bc.findIteratorBinding("FlujoErrorView1Iterator");
        DCIteratorBinding ibfb = bc.findIteratorBinding("FlujoBasicoView1Iterator");
        DCIteratorBinding ibfa = bc.findIteratorBinding("FlujoAlternativoView1Iterator");
        try {
            DataFlavor<String> df = DataFlavor.getDataFlavor(String.class);
            String droppedValue = dropEvent.getTransferable().getData(df);
            
            if (droppedValue == null) {
                return DnDAction.NONE;
            } else {
                
                //la forma en que viene el valor es "nombreMetodo%clasePadre"
                StringTokenizer valueTk = new StringTokenizer(droppedValue,"%");
                
                String newValue = "";
                String padre = "";
                int c=0;
                while(valueTk.hasMoreTokens()){
                    if(c==0)
                        newValue = valueTk.nextToken()+";\n";
                    else
                        padre = valueTk.nextToken();
                    c++;
                }
                
                String oldValue = this.metodoFE_InputText.getValue()==null?"":this.metodoFE_InputText.getValue().toString();
                
                //primero busca si no esta definida en mi ubicación actual
                String nombreInstancia = buscarInstanciaEnCadena(padre, oldValue);
                
                if(nombreInstancia.equalsIgnoreCase("")){
                    //si no fue creada la instancia en la ubicacion actual, busco en el iterador del FB
                    nombreInstancia = buscarInstancia(padre, ibfb);
                    
                    if(nombreInstancia.equalsIgnoreCase("")){
                        //si no fue creada la instancia en el flujo basico, busco en el iterador del FA
                        nombreInstancia = buscarInstancia(padre,ibfa);
                        
                        if(nombreInstancia.equalsIgnoreCase("")){
                            //si no fue creada la instancia en el flujo alternativo, busco en el iterador del FE
                            nombreInstancia = buscarInstancia(padre,ibfe);
                            
                            if(nombreInstancia.equalsIgnoreCase("")){
                                //si no fue creada la instancia, la creo yo
                                String varName = padre.substring(0,2);
                                nombreInstancia = padre+" "+varName.toLowerCase()+" = new "+padre+"();\n"+varName.toLowerCase(); 
                                //resultaria algo asi como 'CajeroAutoamtico ca = new CajeroAutomatico();ca'    
                            }    
                        }
                          
                    }
                    
                }
                
                
                int espacio = newValue.indexOf(" ");
                String sentencia = nombreInstancia + "."+newValue.substring(espacio+1);
                //resultaria algo asi como 'CajeroAutoamtico ca = new CajeroAutomatico(); ca.setTarjeta()'
                
                if (dropEvent.getDropComponent() instanceof UIXEditableValue) {
                    ((UIXEditableValue)dropEvent.getDropComponent()).resetValue(); 
                }
                
                String suma = oldValue+sentencia;
                
                if (dropEvent.getDropComponent() instanceof UIXValue) 
                    ((UIXValue)dropEvent.getDropComponent()).setValue(suma); 
                
                DCIteratorBinding ibvp = bc.findIteratorBinding("ValoresDeParametrosView1Iterator");

                //newValue tiene la signatura del metodo
                //busco el primer parentesis y leo lo q esta entre comas (los parametros)
                //cada parametro iria en una fila del iterador
                setearParametros(newValue, ibvp);

                //solo si tiene parámetros lanzamos el popup, sino no justifica
                if(ibvp.getEstimatedRowCount() > 0){
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID,
                              ((UIXValue)dropEvent.getDropComponent())).add(RichPopup.PopupHints.HintTypes.HINT_LAUNCH_ID,
                                                                            ((UIXValue)dropEvent.getDropComponent())).add(RichPopup.PopupHints.HintTypes.HINT_ALIGN,
                                                                                                                          RichPopup.PopupHints.AlignTypes.ALIGN_AFTER_START);
                    popupFE.show(hints);
                    
                }                
            }
            
            
            return DnDAction.COPY;
        } catch (Exception ex) {
            System.out.println("item drop failed with : " + ex.getMessage());
            return DnDAction.NONE;
        }
    }

    public DnDAction handleItemDropFA(DropEvent dropEvent) {
        DCIteratorBinding ibfa = bc.findIteratorBinding("FlujoAlternativoView1Iterator");
        DCIteratorBinding ibfb = bc.findIteratorBinding("FlujoBasicoView1Iterator");
        try {
            DataFlavor<String> df = DataFlavor.getDataFlavor(String.class);
            String droppedValue = dropEvent.getTransferable().getData(df);
            
            if (droppedValue == null) {
                return DnDAction.NONE;
            } else {
                
                //la forma en que viene el valor es "nombreMetodo%clasePadre"
                StringTokenizer valueTk = new StringTokenizer(droppedValue,"%");
                
                String newValue = "";
                String padre = "";
                int c=0;
                while(valueTk.hasMoreTokens()){
                    if(c==0)
                        newValue = valueTk.nextToken()+";\n";
                    else
                        padre = valueTk.nextToken();
                    c++;
                }
                
                String oldValue = this.metodoFA_InputText.getValue()==null?"":this.metodoFA_InputText.getValue().toString();
                
                //primero busca si no esta definida en mi ubicación actual
                String nombreInstancia = buscarInstanciaEnCadena(padre, oldValue);
                
                if(nombreInstancia.equalsIgnoreCase("")){
                    //si no fue creada la instancia en la ubicacion actual, busco en el iterador FB
                    nombreInstancia = buscarInstancia(padre,ibfb);    
                    if(nombreInstancia.equalsIgnoreCase("")){
                        //si no encuentro en el flujoBasico busco en el flujoAlternativo
                        nombreInstancia = buscarInstancia(padre,ibfa);
                        if(nombreInstancia.equalsIgnoreCase("")){
                            //si no fue creada la instancia, la creo yo
                            String varName = padre.substring(0,2);
                            nombreInstancia = padre+" "+varName.toLowerCase()+" = new "+padre+"();\n"+varName.toLowerCase(); 
                            //resultaria algo asi como 'CajeroAutoamtico ca = new CajeroAutomatico();ca'    
                        }    
                    }
                }
                
                int espacio = newValue.indexOf(" ");
                String sentencia = nombreInstancia + "."+newValue.substring(espacio+1);
                //resultaria algo asi como 'CajeroAutoamtico ca = new CajeroAutomatico(); ca.setTarjeta()'
                
                if (dropEvent.getDropComponent() instanceof UIXEditableValue) {
                    ((UIXEditableValue)dropEvent.getDropComponent()).resetValue(); 
                }
                
                String suma = oldValue+sentencia;
                
                if (dropEvent.getDropComponent() instanceof UIXValue) 
                    ((UIXValue)dropEvent.getDropComponent()).setValue(suma); 
                
                DCIteratorBinding ibvp = bc.findIteratorBinding("ValoresDeParametrosView1Iterator");

                //newValue tiene la signatura del metodo
                //busco el primer parentesis y leo lo q esta entre comas (los parametros)
                //cada parametro iria en una fila del iterador
                setearParametros(newValue, ibvp);

                //solo si tiene parámetros lanzamos el popup, sino no justifica
                if(ibvp.getEstimatedRowCount() > 0){
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID,
                              ((UIXValue)dropEvent.getDropComponent())).add(RichPopup.PopupHints.HintTypes.HINT_LAUNCH_ID,
                                                                            ((UIXValue)dropEvent.getDropComponent())).add(RichPopup.PopupHints.HintTypes.HINT_ALIGN,
                                                                                                                          RichPopup.PopupHints.AlignTypes.ALIGN_AFTER_START);
                    popupFA.show(hints);
                    
                }                
            }
            
            return DnDAction.COPY;
        } catch (Exception ex) {
            System.out.println("item drop failed with : " + ex.getMessage());
            return DnDAction.NONE;
        }
    }

    public DnDAction handleItemDrop(DropEvent dropEvent) {
        DCIteratorBinding ibfb = bc.findIteratorBinding("FlujoBasicoView1Iterator");
        try {
            DataFlavor<String> df = DataFlavor.getDataFlavor(String.class);
            String droppedValue = dropEvent.getTransferable().getData(df);
            
            if (droppedValue == null) {
                return DnDAction.NONE;
            } else {
                
                //la forma en que viene el valor es "nombreMetodo%clasePadre"
                StringTokenizer valueTk = new StringTokenizer(droppedValue,"%");
                
                String newValue = "";
                String padre = "";
                int c=0;
                while(valueTk.hasMoreTokens()){
                    if(c==0)
                        newValue = valueTk.nextToken()+";\n";
                    else
                        padre = valueTk.nextToken();
                    c++;
                }
                
                String oldValue = this.metodoFB_InputText.getValue()==null?"":this.metodoFB_InputText.getValue().toString();
                
                //primero busca si no esta definida en mi ubicación actual
                String nombreInstancia = buscarInstanciaEnCadena(padre, oldValue);
                
                if(nombreInstancia.equalsIgnoreCase("")){
                    //si no fue creada la instancia en la ubicacion actual, busco en el iterador
                    nombreInstancia = buscarInstancia(padre,ibfb);
                    if(nombreInstancia.equalsIgnoreCase("")){
                        //si no fue creada la instancia, la creo yo
                        String varName = padre.substring(0,2);
                        nombreInstancia = padre+" "+varName.toLowerCase()+" = new "+padre+"();\n"+varName.toLowerCase(); 
                        //resultaria algo asi como 'CajeroAutoamtico ca = new CajeroAutomatico();ca'    
                    }
                    
                }
                
                
                int espacio = newValue.indexOf(" ");
                String sentencia = nombreInstancia + "."+newValue.substring(espacio+1);
                //resultaria algo asi como 'CajeroAutoamtico ca = new CajeroAutomatico(); ca.setTarjeta()'
                
                if (dropEvent.getDropComponent() instanceof UIXEditableValue) {
                    ((UIXEditableValue)dropEvent.getDropComponent()).resetValue(); 
                }
                
                String suma = oldValue+sentencia;
                
                if (dropEvent.getDropComponent() instanceof UIXValue) 
                    ((UIXValue)dropEvent.getDropComponent()).setValue(suma); 
                
                DCIteratorBinding ibvp = bc.findIteratorBinding("ValoresDeParametrosView1Iterator");

                //newValue tiene la signatura del metodo
                //busco el primer parentesis y leo lo q esta entre comas (los parametros)
                //cada parametro iria en una fila del iterador
                setearParametros(newValue, ibvp);

                //solo si tiene parámetros lanzamos el popup, sino no justifica
                if(ibvp.getEstimatedRowCount() > 0){
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID,
                              ((UIXValue)dropEvent.getDropComponent())).add(RichPopup.PopupHints.HintTypes.HINT_LAUNCH_ID,
                                                                            ((UIXValue)dropEvent.getDropComponent())).add(RichPopup.PopupHints.HintTypes.HINT_ALIGN,
                                                                                                                          RichPopup.PopupHints.AlignTypes.ALIGN_AFTER_START);
                    popupFB.show(hints);
                    
                }                
            }
            
            
            return DnDAction.COPY;
        } catch (Exception ex) {
            System.out.println("item drop failed with : " + ex.getMessage());
            return DnDAction.NONE;
        }
    }

    private void setearParametros(String metodo, DCIteratorBinding ib){
        int primerParentesis = metodo.indexOf("(");
        int ultimoParentesis = metodo.indexOf(")");
        
        afContext.getPageFlowScope().put("methodName", metodo);
        String parametros = metodo.substring(primerParentesis+1, ultimoParentesis);
        StringTokenizer parametrosTK = new StringTokenizer(parametros,",");
        
        while(parametrosTK.hasMoreTokens()){
            //el parametro llega como 'int param'
            String param = parametrosTK.nextToken();
            Row fila = ib.getRowSetIterator().createRow();
            fila.setAttribute("varType", param.substring(0, param.indexOf(" ")) );
            fila.setAttribute("varName", param.substring(param.indexOf(" ")+1) );
            ib.getRowSetIterator().insertRow(fila);
        }
        
    }

    public String buscarInstancia(String padre, DCIteratorBinding ib){
        String nombreInstancia = "";
        for(int i=0; i < ib.getEstimatedRowCount(); i++){
            ib.setCurrentRowIndexInRange(i);
            String metodo = (String)ib.getCurrentRow().getAttribute(2);  //pos 2 = metodoAsoc
            if(metodo != null){
                nombreInstancia = buscarInstanciaEnCadena(padre, metodo);
                if(!nombreInstancia.equalsIgnoreCase(""))
                    break;
            }
        }
        return nombreInstancia;
    }

    private String buscarInstanciaEnCadena(String padre, String metodo){
        String varName ="";
        int ini = metodo.indexOf(padre);
        if(ini > -1){
            metodo = metodo.substring(ini+padre.length()+1); //saca la clase 
            int espacio = metodo.indexOf(" ");    //saca el nombre de la instancia
            varName = metodo.substring(0, espacio);
        }
        return varName;
    }

    public DnDAction dragAndDrop(DropEvent dropEvent) {
        // Add event code here...
        return null;
    }

    public void setArbolProyectos(RichTree arbolProyectos) {
        this.arbolProyectos = arbolProyectos;
    }

    public RichTree getArbolProyectos() {
        return arbolProyectos;
    }

    public void setMetodoFA_InputText(RichInputText metodoFA_InputText) {
        this.metodoFA_InputText = metodoFA_InputText;
    }

    public RichInputText getMetodoFA_InputText() {
        return metodoFA_InputText;
    }

    public void setMetodoFE_InputText(RichInputText metodoFE_InputText) {
        this.metodoFE_InputText = metodoFE_InputText;
    }

    public RichInputText getMetodoFE_InputText() {
        return metodoFE_InputText;
    }

    public void valueChangeMetodosFB(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent != null){
            String valor = (String)valueChangeEvent.getNewValue();
            if(!this.metodoFB_InputText.getValue().equals(valor)){
                this.metodoFB_InputText.setValue(valor);
                this.metodoFB_InputText.resetValue();
            }
        }
    }

    public void valueChangeMetodosFA(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent != null){
            String valor = (String)valueChangeEvent.getNewValue();
            if(!this.metodoFA_InputText.getValue().equals(valor)){
                this.metodoFA_InputText.setValue(valor);
                this.metodoFA_InputText.resetValue();
            }
        }
    }

    public void valueChangeMetodosFE(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent != null){
            String valor = (String)valueChangeEvent.getNewValue();
            if(!this.metodoFE_InputText.getValue().equals(valor)){
                this.metodoFE_InputText.setValue(valor);
                this.metodoFE_InputText.resetValue();
            }
        }
    }

    public DnDAction handleDnDEvent(DropEvent dropEvent) {
        Transferable transferable = dropEvent.getTransferable();
        UIComponent dragComponent = null;
        dragComponent = transferable.getData(DataFlavor.UICOMPONENT_FLAVOR);
        UIComponent dropTarget = dropEvent.getDropComponent();
        if (dragComponent != null) {
            UIComponent dragComponentParent = dragComponent.getParent();
            if (dropTarget.equals(dragComponentParent.getParent())) {
                return DnDAction.NONE;
            } else {
                
                if (dropEvent.getDropComponent() instanceof UIXEditableValue) {
                    ((UIXEditableValue)dropEvent.getDropComponent()).resetValue(); 
                }
                
                String suma = dragComponent.getAttributes().get("value").toString();
                
                if (dropEvent.getDropComponent() instanceof UIXValue) 
                    ((UIXValue)dropEvent.getDropComponent()).setValue(suma); 
         
                //ValueChangeEvent vce = new ValueChangeEvent(((UIXValue)dropEvent.getDropComponent()),null,suma);
                //valueChangePrueba(vce);
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                  hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, ((UIXValue)dropEvent.getDropComponent())  )
                      .add(RichPopup.PopupHints.HintTypes.HINT_LAUNCH_ID, ((UIXValue)dropEvent.getDropComponent()))
                      .add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, 
                           RichPopup.PopupHints.AlignTypes.ALIGN_AFTER_START);
                  popupFB.show(hints);
                
                return DnDAction.MOVE;
            }
        }
        return DnDAction.NONE;
    }

    public void setPrueba_inputText(RichInputText prueba_inputText) {
        this.prueba_inputText = prueba_inputText;
    }

    public RichInputText getPrueba_inputText() {
        return prueba_inputText;
    }

    public void valueChangePrueba(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent != null){
            
        }
    }

    public void setPopupFB(RichPopup popup_prueba) {
        this.popupFB = popup_prueba;
    }

    public RichPopup getPopupFB() {
        return popupFB;
    }

    public void MetodoFB_dialogListener(DialogEvent dialogEvent) {
        DCIteratorBinding ibvp = bc.findIteratorBinding("ValoresDeParametrosView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        String nuevoMetodo = "";
        if (dialogEvent != null) {
            if(dialogEvent.getOutcome().compareTo(dialogEvent.getOutcome().ok) == 0){
                //deberia recuperar la ultima linea del inputText de metodos
                //esta ultima linea deberia de ser la del metodo que fue arrastrado
                //cuyos parametros estamos seteando
                
                String oldValue = this.metodoFB_InputText.getValue()==null?"":this.metodoFB_InputText.getValue().toString();
                
                StringTokenizer valueST = new StringTokenizer(oldValue,";");
                String ultMetodo = "";
                int cantMet = valueST.countTokens();
                
                if(cantMet == 1){
                    ultMetodo = oldValue;    
                }else{
                    int c = 0;
                    while(valueST.hasMoreTokens()){
                        ultMetodo = valueST.nextToken();
                        c++;
                        if(c == (cantMet-1) )
                            break;
                    }    
                }
                
                //después del ciclo, deberia tener el ultimo metodo
                if( !"".equals(ultMetodo) ){
                    
                    int parentsis1 = ultMetodo.indexOf("(");
                    int iniUltMet = oldValue.indexOf(ultMetodo);
                    oldValue = oldValue.substring(0, iniUltMet==0?iniUltMet:iniUltMet+1); //saco el ultimo metodo de los valores viejos
                    
                    nuevoMetodo = ultMetodo.substring(0,parentsis1+1);     
                    
                    for (int i = 0; i < ibvp.getEstimatedRowCount(); i++) {
                        ibvp.setCurrentRowIndexInRange(i);
                        if(ibvp.getCurrentRow().getAttribute("varValue") == null || "".equals(ibvp.getCurrentRow().getAttribute("varValue")) ){
                            ibv.getCurrentRow().setAttribute("vMensajes","falta el valor del parametro "+i+"del metodo "+ultMetodo);
                            nuevoMetodo = "";
                            break;        
                        }
                        
                        nuevoMetodo += ibvp.getCurrentRow().getAttribute("varValue") + ",";
                    }   
                    
                    if( !"".equals(nuevoMetodo) ){
                        
                        nuevoMetodo = nuevoMetodo.substring(0,nuevoMetodo.lastIndexOf(",")==-1?0:nuevoMetodo.lastIndexOf(","));    //saco la ultima coma del nuevo metodo
                        int ultimoEnter = oldValue.lastIndexOf("\n");
                        if(ultimoEnter != -1)
                            oldValue = oldValue.substring(0, ultimoEnter); //borramos el ultimo enter
                        oldValue += nuevoMetodo + ");"; //agrego ultimo parentesis, punto y coma y enter
                        EjecutaBinding("LimpiarValoresDeParametros"); //limpio el iterador de variables          
                    }
                    
                    
                }

                this.metodoFB_InputText.resetValue();
                this.metodoFB_InputText.setValue(oldValue);
                afContext.getCurrentInstance().addPartialTarget(metodoFB_InputText);
                afContext.getPageFlowScope().put("methodName", "");
            }
                
        }

    }
 
    public void MetodoFA_dialogListener(DialogEvent dialogEvent) {
        DCIteratorBinding ibvp = bc.findIteratorBinding("ValoresDeParametrosView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        String nuevoMetodo = "";
        if (dialogEvent != null) {
            if(dialogEvent.getOutcome().compareTo(dialogEvent.getOutcome().ok) == 0){
                //deberia recuperar la ultima linea del inputText de metodos
                //esta ultima linea deberia de ser la del metodo que fue arrastrado
                //cuyos parametros estamos seteando
                
                String oldValue = this.metodoFA_InputText.getValue()==null?"":this.metodoFA_InputText.getValue().toString();
                
                StringTokenizer valueST = new StringTokenizer(oldValue,";");
                String ultMetodo = "";
                int cantMet = valueST.countTokens();
                
                if(cantMet == 1){
                    ultMetodo = oldValue;    
                }else{
                    int c = 0;
                    while(valueST.hasMoreTokens()){
                        ultMetodo = valueST.nextToken();
                        c++;
                        if(c == (cantMet-1) )
                            break;
                    }    
                }
                
                //después del ciclo, deberia tener el ultimo metodo
                if( !"".equals(ultMetodo) ){
                    
                    int parentsis1 = ultMetodo.indexOf("(");
                    int iniUltMet = oldValue.indexOf(ultMetodo);
                    oldValue = oldValue.substring(0, iniUltMet==0?iniUltMet:iniUltMet+1); //saco el ultimo metodo de los valores viejos
                    
                    nuevoMetodo = ultMetodo.substring(0,parentsis1+1);     
                    
                    for (int i = 0; i < ibvp.getEstimatedRowCount(); i++) {
                        ibvp.setCurrentRowIndexInRange(i);
                        if(ibvp.getCurrentRow().getAttribute("varValue") == null || "".equals(ibvp.getCurrentRow().getAttribute("varValue")) ){
                            ibv.getCurrentRow().setAttribute("vMensajes","falta el valor del parametro "+i+"del metodo "+ultMetodo);
                            nuevoMetodo = "";
                            break;        
                        }
                        
                        nuevoMetodo += ibvp.getCurrentRow().getAttribute("varValue") + ",";
                    }   
                    
                    if( !"".equals(nuevoMetodo) ){
                        
                        nuevoMetodo = nuevoMetodo.substring(0,nuevoMetodo.lastIndexOf(",")==-1?0:nuevoMetodo.lastIndexOf(","));    //saco la ultima coma del nuevo metodo
                        int ultimoEnter = oldValue.lastIndexOf("\n");
                        if(ultimoEnter != -1)
                            oldValue = oldValue.substring(0, ultimoEnter); //borramos el ultimo enter
                        oldValue += nuevoMetodo + ");"; //agrego ultimo parentesis, punto y coma y enter
                        EjecutaBinding("LimpiarValoresDeParametros"); //limpio el iterador de variables          
                    }
                    
                    
                }

                this.metodoFA_InputText.resetValue();
                this.metodoFA_InputText.setValue(oldValue);
                afContext.getCurrentInstance().addPartialTarget(metodoFA_InputText);
                afContext.getPageFlowScope().put("methodName", "");
            }
                
        }

    }
    
    public void MetodoFE_dialogListener(DialogEvent dialogEvent) {
        DCIteratorBinding ibvp = bc.findIteratorBinding("ValoresDeParametrosView1Iterator");
        DCIteratorBinding ibv = bc.findIteratorBinding("variables");
        String nuevoMetodo = "";
        if (dialogEvent != null) {
            if(dialogEvent.getOutcome().compareTo(dialogEvent.getOutcome().ok) == 0){
                //deberia recuperar la ultima linea del inputText de metodos
                //esta ultima linea deberia de ser la del metodo que fue arrastrado
                //cuyos parametros estamos seteando
                
                String oldValue = this.metodoFE_InputText.getValue()==null?"":this.metodoFE_InputText.getValue().toString();
                
                StringTokenizer valueST = new StringTokenizer(oldValue,";");
                String ultMetodo = "";
                int cantMet = valueST.countTokens();
                
                if(cantMet == 1){
                    ultMetodo = oldValue;    
                }else{
                    int c = 0;
                    while(valueST.hasMoreTokens()){
                        ultMetodo = valueST.nextToken();
                        c++;
                        if(c == (cantMet-1) )
                            break;
                    }    
                }
                
                //después del ciclo, deberia tener el ultimo metodo
                if( !"".equals(ultMetodo) ){
                    
                    int parentsis1 = ultMetodo.indexOf("(");
                    int iniUltMet = oldValue.indexOf(ultMetodo);
                    oldValue = oldValue.substring(0, iniUltMet==0?iniUltMet:iniUltMet+1); //saco el ultimo metodo de los valores viejos
                    
                    nuevoMetodo = ultMetodo.substring(0,parentsis1+1);     
                    
                    for (int i = 0; i < ibvp.getEstimatedRowCount(); i++) {
                        ibvp.setCurrentRowIndexInRange(i);
                        if(ibvp.getCurrentRow().getAttribute("varValue") == null || "".equals(ibvp.getCurrentRow().getAttribute("varValue")) ){
                            ibv.getCurrentRow().setAttribute("vMensajes","falta el valor del parametro "+i+"del metodo "+ultMetodo);
                            nuevoMetodo = "";
                            break;        
                        }
                        
                        nuevoMetodo += ibvp.getCurrentRow().getAttribute("varValue") + ",";
                    }   
                    
                    if( !"".equals(nuevoMetodo) ){
                        
                        nuevoMetodo = nuevoMetodo.substring(0,nuevoMetodo.lastIndexOf(",")==-1?0:nuevoMetodo.lastIndexOf(","));    //saco la ultima coma del nuevo metodo
                        int ultimoEnter = oldValue.lastIndexOf("\n");
                        if(ultimoEnter != -1)
                            oldValue = oldValue.substring(0, ultimoEnter); //borramos el ultimo enter
                        oldValue += nuevoMetodo + ");"; //agrego ultimo parentesis, punto y coma y enter
                        EjecutaBinding("LimpiarValoresDeParametros"); //limpio el iterador de variables          
                    }
                    
                    
                }

                this.metodoFE_InputText.resetValue();
                this.metodoFE_InputText.setValue(oldValue);
                afContext.getCurrentInstance().addPartialTarget(metodoFE_InputText);
                afContext.getPageFlowScope().put("methodName", "");
            }
                
        }

    }
    /*
     * Metodo: generaSecuencia
     * Objetivo: Toma la secuencia original, establecida por el usuario,
     * y a partir de ella genera otras secuencias con los parametros modificados
     * utilizando los mecanismos de AVL y Particiones Equivalentes
     * la secuencia llega asi:

    Test objectives: 5
    =======================
    1::: 1, 2, D01, 2.1, End,
    2::: 1, 2, D01, D02, 3.1, End,
    3::: 1, 2, D01, D02, 3, 4, 5, D03, 5.1, End,
    4::: 1, 2, D01, D02, 3, 4, 5, D03, 6, D04, 6.1, End,
    5::: 1, 2, D01, D02, 3, 4, 5, D03, 6, D04, 7, 8, 9, End,
    Loops:

     */

    public String generaJunit(String path) {
        DCIteratorBinding ibfb = bc.findIteratorBinding("FlujoBasicoView1Iterator");
        DCIteratorBinding ibfa = bc.findIteratorBinding("FlujoAlternativoView1Iterator");
        DCIteratorBinding ibfe = bc.findIteratorBinding("FlujoErrorView1Iterator");

        FileWriter fw = null; //para la creacion del archivo de pruebas
        
        try {
            String secuencias = (String)afContext.getPageFlowScope().get("Secuencias");

            String nombreCU = (String)afContext.getPageFlowScope().get("nombreCU");
            //String nombreCU = "003-Sesion";
            ArrayList<String> secArray = new ArrayList<String>();
            
            //nombre del proyecto traido de la vista anterior
            String nombreProyecto = (String)afContext.getPageFlowScope().get("nombreProyecto");
            String directoriActual = System.getProperty("user.dir");
            directoriActual += "\\Proyectos\\"+nombreProyecto+"\\TO\\"+nombreCU+".xml";
            String pathTO = directoriActual;
            
            //debe recibir el archivo en un processScope de la vista anterior
            //String pathTO = "C:\\Users\\sebas\\AppData\\Roaming\\JDeveloper\\system11.1.2.0.38.60.17\\DefaultDomain\\TO\\003-Sesion.xml";
            
            ArrayList<String> descArray = obtenerDescripcionesDeArchivoTO(pathTO);

            //sacamos cada secuencia y la metemos en un array de Strings
            StringTokenizer secTK = new StringTokenizer(secuencias, "\n");
            int c = 0;
            while (secTK.hasMoreTokens()) {
                String element = secTK.nextToken();//1::: 1, 2, D01, 2.1, End,
                //element = element.substring(0, element.lastIndexOf(","));  //borramos la ultima coma
                
                int primerEspacio = element.indexOf(" "); //sacamos el indice de la primera posicion ejemplo=>  1::: 1
                if (c > 1 && !element.contains("Loops:")) { //no guardamos Loops ni los titulos
                    secArray.add(element.substring(primerEspacio + 1));
                }
                c++;
            }
            //c = secArray.size()-1; //eliminamos el ultimo elemento, que es loops
            //secArray.remove(c);
            
            //reconstruimos el nombre del caso de uso, le sacamos los numeros y las rayitas
            nombreCU = corregirNombre(nombreCU);
            nombreCU = "CU"+nombreCU;
            
            //debe ser un solo file por cada caso de uso y dentro de el, un metodo por cada secuencia de prueba!
            fw = new FileWriter(new File(path + "\\" + nombreCU + "Test.java"));
            fw.write("import org.junit.*;\n");
            fw.write("import static org.junit.Assert.*;\n\n");
            fw.write("public class " + nombreCU + "Test {\n\n");
            
            int cantPruebas = 0; //una prueba = una secuencia
            for (int i = 0; i < secArray.size(); i++) {
                //Paso 1: Recorrer la Secuencia seleccionada
                cantPruebas++;
            
                fw.write("\t@Test  \n");

                fw.write("\tpublic void casoPrueba" + cantPruebas + "() throws Exception{\n\n");

                //Paso 2: Se imprime la primera secuencia, tal como se elaboro
                StringTokenizer pasosSec = new StringTokenizer(secArray.get(i), ","); //sacamos cada paso de la secuencia
                while (pasosSec.hasMoreTokens()) {
                    String comentario = "";
                    String metodos = "";
                    String paso = pasosSec.nextToken();
                    comentario = buscarDescripcion(paso.trim(),descArray.get(i)); //buscamos la descripcion correspondiente al paso
                    fw.write("\n\t\t/* "+comentario+" */\n"); //escribo la descripcion como comentario
                    
                    if (paso.contains(".")) {
                        //no es flujo básico,
                        //pude ser flujo error
                        metodos = buscarMetodos(paso.trim(), ibfe);
                        //o flujo alternativo
                        if ("".equals(metodos)) {
                            metodos = buscarMetodos(paso.trim(), ibfa);
                        }
                    } else {
                        //es flujo basico
                        metodos = buscarMetodos(paso.trim(), ibfb);
                    }

                    if (!("".equals(metodos))) {
                        //si hay metodos
                        StringTokenizer metodosST = new StringTokenizer(metodos,";");
                        while(metodosST.hasMoreTokens()){
                            String unMetodo = metodosST.nextToken();
                            fw.write("\n\t\t"+ unMetodo.trim() + ";");    
                        }
                    }
                }

                fw.write("\t}\n\n"); //fin de la secuencia x
            } //para cada secuencia
            
            fw.write("}"); //fin de la Clase xxxTestxx.java
            fw.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw); 

            if(fw != null){
                try{
                    fw.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }    
            }
                
            return "ER:"+e.getMessage()+" "+sw.toString();
        }
        
        return "OK";
    }


    public String corregirNombre(String nombreCU){
        nombreCU = nombreCU.replace("-", "");//elimina las rayas del nombre del archivo
        
        char[] nombreCUenChar = nombreCU.toCharArray();
        String[] numeros = {"0","1","2","3","4","5","6","7","8","9"};
        int c = 0;
        while(c < nombreCUenChar.length){
            nombreCU = nombreCU.replace(numeros[c],""); //elimina los numeros del nombre del archivo
            c++;
        }
        
        return nombreCU;
    }

    private String buscarMetodos(String paso,DCIteratorBinding ib){
        String metodosAsociados = "";
        
        for(int i=0; i < ib.getEstimatedRowCount(); i++){
            ib.setCurrentRowIndexInRange(i);
            String nroPaso = (String)ib.getCurrentRow().getAttribute(0); //pos 0 = nroPaso
            if(nroPaso.equalsIgnoreCase(paso)){
                metodosAsociados = ib.getCurrentRow().getAttribute(2)==null?"":ib.getCurrentRow().getAttribute(2).toString(); //pos 2 = metodoAsoc
                break;
            }
        }
        
        return metodosAsociados;
    }


    public String generarObjetivos() {
      
        String nombreProyecto = (String)afContext.getPageFlowScope().get("nombreProyecto");
        //String nombreProyecto = "CajeroAutomatico";
        String directorioActual = System.getProperty("user.dir");
        directorioActual = directorioActual.replace('\\', '/');
        directorioActual += "/Proyectos/"+nombreProyecto;
        directorioActual += "/JU/";
        
        String path = directorioActual;
        String respuesta = generaJunit(path);
        if("OK".equals(respuesta)){
            bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", "Los archivos Junits se crearon correctamente!");
        }else{
            bc.findIteratorBinding("variables").getCurrentRow().setAttribute("vMensajes", respuesta);    
        }
        return null;
    }
    
    
    public ArrayList<String> obtenerDescripcionesDeArchivoTO(String path){
        ArrayList<String> descripciones = new ArrayList<String>();
        try{
            File archivo = new File(path);

            if (archivo.isFile() == true) {
                try {
                    FileReader fr = new FileReader(archivo);
                    BufferedReader entrada = new BufferedReader(fr);
                    String s;
                    while ((s = entrada.readLine()) != null) {
                        String valor = "";
                        if (s != null && s.indexOf("<Description>") != -1) {

                            s = s.substring(s.indexOf(">") + 1);

                            while (s != null && s.indexOf("</Description>") == -1) {
                                if(!"".equalsIgnoreCase(s)){
                                    valor += s + "#";    
                                }
                                s = entrada.readLine();
                            }
                            valor = valor.substring(0, valor.lastIndexOf("#")); //borramos el ultimo separador
                            descripciones.add(valor);
                        }
                    }

                    entrada.close();
                    fr.close();
                } catch (IOException e) {
                    throw new Exception("Error al leer archivo de TestObjectives");
                }
            } else{
                throw new Exception("No se encuentra el archivo");
            }

        }catch(Exception e){
            e.printStackTrace();    
        }
        
        return descripciones;
    }


    public String buscarDescripcion(String paso, String descripciones){
        String descripcion = "";
        StringTokenizer descTk = new StringTokenizer(descripciones,"#"); 
        while(descTk.hasMoreTokens()){
            String elemento = descTk.nextToken(); //llegaria algo asi => 1: The client inserts the card.#
            int dosPts = elemento.indexOf(":"); 
            if(dosPts > -1){
                String nroPaso = elemento.substring(0, (dosPts)); //obtengo el nro del paso
                if(paso.equalsIgnoreCase(nroPaso.trim())){
                    descripcion = elemento; //encontre el paso, retorno su descripción
                    break;
                }    
            }else if(paso.contains("End")){ //cuando llego al END es porque termino
                descripcion = "The Secuence Ends";
            }
        }
        
        return descripcion;    
    }


    public void pruebaDialogListener(DialogEvent dialogEvent) {
        //this.popup_prueba.getParent().getId();
        this.prueba_inputText.resetValue();
        this.prueba_inputText.setValue("algo");
        afContext.getCurrentInstance().addPartialTarget(prueba_inputText);
        //this.prueba_inputText.resetValue();
        //((UIXEditableValue)dialogEvent.getComponent()).resetValue(); 

        //((UIXValue)dialogEvent.getComponent()).setValue("OK"); 
    }

    public void setPopupFA(RichPopup popupFA) {
        this.popupFA = popupFA;
    }

    public RichPopup getPopupFA() {
        return popupFA;
    }

    public void setPopupFE(RichPopup popupFE) {
        this.popupFE = popupFE;
    }

    public RichPopup getPopupFE() {
        return popupFE;
    }
}
