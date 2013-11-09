
package webservice.proxy.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generarSecuencias complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generarSecuencias">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="templateFile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="testObjectiveFile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generarSecuencias", propOrder = {
    "templateFile",
    "testObjectiveFile"
})
public class GenerarSecuencias {

    @XmlElement(required = true, nillable = true)
    protected String templateFile;
    @XmlElement(required = true, nillable = true)
    protected String testObjectiveFile;

    /**
     * Gets the value of the templateFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateFile() {
        return templateFile;
    }

    /**
     * Sets the value of the templateFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateFile(String value) {
        this.templateFile = value;
    }

    /**
     * Gets the value of the testObjectiveFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestObjectiveFile() {
        return testObjectiveFile;
    }

    /**
     * Sets the value of the testObjectiveFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestObjectiveFile(String value) {
        this.testObjectiveFile = value;
    }

}
