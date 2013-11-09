
package webservice.proxy.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the webservice.proxy.types package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GenerarActivityDiagramResponse_QNAME = new QName("http://appws/", "generarActivityDiagramResponse");
    private final static QName _GenerarActivityDiagram_QNAME = new QName("http://appws/", "generarActivityDiagram");
    private final static QName _GenerarSecuenciasResponse_QNAME = new QName("http://appws/", "generarSecuenciasResponse");
    private final static QName _GenerarSecuencias_QNAME = new QName("http://appws/", "generarSecuencias");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: webservice.proxy.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenerarSecuencias }
     * 
     */
    public GenerarSecuencias createGenerarSecuencias() {
        return new GenerarSecuencias();
    }

    /**
     * Create an instance of {@link GenerarActivityDiagramResponse }
     * 
     */
    public GenerarActivityDiagramResponse createGenerarActivityDiagramResponse() {
        return new GenerarActivityDiagramResponse();
    }

    /**
     * Create an instance of {@link GenerarActivityDiagram }
     * 
     */
    public GenerarActivityDiagram createGenerarActivityDiagram() {
        return new GenerarActivityDiagram();
    }

    /**
     * Create an instance of {@link GenerarSecuenciasResponse }
     * 
     */
    public GenerarSecuenciasResponse createGenerarSecuenciasResponse() {
        return new GenerarSecuenciasResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarActivityDiagramResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://appws/", name = "generarActivityDiagramResponse")
    public JAXBElement<GenerarActivityDiagramResponse> createGenerarActivityDiagramResponse(GenerarActivityDiagramResponse value) {
        return new JAXBElement<GenerarActivityDiagramResponse>(_GenerarActivityDiagramResponse_QNAME, GenerarActivityDiagramResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarActivityDiagram }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://appws/", name = "generarActivityDiagram")
    public JAXBElement<GenerarActivityDiagram> createGenerarActivityDiagram(GenerarActivityDiagram value) {
        return new JAXBElement<GenerarActivityDiagram>(_GenerarActivityDiagram_QNAME, GenerarActivityDiagram.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarSecuenciasResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://appws/", name = "generarSecuenciasResponse")
    public JAXBElement<GenerarSecuenciasResponse> createGenerarSecuenciasResponse(GenerarSecuenciasResponse value) {
        return new JAXBElement<GenerarSecuenciasResponse>(_GenerarSecuenciasResponse_QNAME, GenerarSecuenciasResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarSecuencias }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://appws/", name = "generarSecuencias")
    public JAXBElement<GenerarSecuencias> createGenerarSecuencias(GenerarSecuencias value) {
        return new JAXBElement<GenerarSecuencias>(_GenerarSecuencias_QNAME, GenerarSecuencias.class, null, value);
    }

}
