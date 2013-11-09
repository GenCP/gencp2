package appws;

import java.io.File;

import javax.jws.WebService;

import objectgen.activitymodel.ActivityDiagram;
import objectgen.activitymodel.ActivityDiagramGenerator;
import objectgen.activitymodel.xmi.XMIFileSaver;

import objectgen.testobjective.ITestObjectiveCriterion;
import objectgen.testobjective.TestObjectiveList;
import objectgen.testobjective.savers.ITestObjectivesSaver;
import objectgen.testobjective.savers.XMLSaver;

import objectgen.usecase.UseCaseXML;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import weblogic.jws.WLHttpTransport;


@WebService(targetNamespace = "http://appws/", endpointInterface = "appws.TestGenPortType")
@WLHttpTransport(serviceUri = "/TestGenPort", portName = "TestGenPort")
public class TestGen {


    public TestGen() {
        //--- Configure logs -----------
        BasicConfigurator.configure();
        //PropertyConfigurator.configure(/*path+/"./config/logs.prop");
        PropertyConfigurator.configure(System.getProperty("user.dir") + "//config//logs.prop");
        objectgen.configuration.Configuration.Create();

    }

    public String generarActivityDiagram(String templateFile, String activityDiagramFile) {
        String resultado = "";
        try {
            File f = new File(templateFile); //-->plantilla del caso de uso de entrada

            UseCaseXML uc = new UseCaseXML();
            uc.load(f);

            ActivityDiagram ad;
            ActivityDiagramGenerator adg = new ActivityDiagramGenerator();
            ad = adg.generate(uc); //--genera diagrama de actividades

            System.out.println(ad);

            /*guarda el archivo del diagrama de actividades*/
            f = new File(activityDiagramFile); //"AD-003 Sesion.xmi"
            XMIFileSaver fs = new XMIFileSaver();
            fs.save(f, ad); //-->guarda el diagrama como xmi

            resultado = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = e.getLocalizedMessage();
        }

        return resultado;
    }

    
    public String generarSecuencias(String templateFile, String testObjectiveFile) {
        String resultado = "";
        try {
            File f = new File(templateFile); //-->plantilla del caso de uso de entrada

            UseCaseXML uc = new UseCaseXML();
            uc.load(f);

            ActivityDiagram ad;
            ActivityDiagramGenerator adg = new ActivityDiagramGenerator();
            ad = adg.generate(uc); //--genera diagrama de actividades

            System.out.println(ad);

            ITestObjectiveCriterion tog = objectgen.testobjective.CoverageCriteriaFactory.Get();
            System.out.println("Criterion: " + objectgen.testobjective.CoverageCriteriaFactory.GetName());
            TestObjectiveList tol = tog.generate(ad); //-->genera objetivos de pruebas

            resultado = tol.toString(); //devuelve un string con la lista de TO
            
            //guardamos las secuencias en un xml dentro, cuyo nombre recibimos como parámetro
            File tof = new File(testObjectiveFile);//-->archivo para secuencias de salida
            ITestObjectivesSaver os = new XMLSaver();
            os.saveHeaderText ("Use case: " + uc.getUCId () + "\r\n" + 
                                "The " + tog + " criterion.");
            os.save (tof, tol); //guarda la lista de testObjectives
            
            
        } catch (Exception e) {
            resultado = e.getLocalizedMessage();
        }

        return resultado;
    }
    

    public static void main(String[] args) {
        TestGen tg = new TestGen();
        String templateFile =
            "C:/Users/sebas/Documents/Tesis/tesisIS/descargas/testGen/TestGen-0.2.3/Bendahan/UC-003 Sesion.xml";
        String activityDiagramFile =
            "C:/Users/sebas/Documents/Tesis/tesisIS/descargas/testGen/TestGen-0.2.3/Bendahan/AD-Sesion.xmi";
        String retorno = tg.generarActivityDiagram(templateFile, activityDiagramFile);
        System.out.println("retorno -->" + retorno);
    }

}
