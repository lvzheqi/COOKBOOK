
package de.hs.inform.lyuz.cookbook.model.cookml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}text"/>
 *         &lt;sequence>
 *           &lt;element ref="{}step" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "text",
    "step"
})
@XmlRootElement(name = "preparation")
public class Preparation implements Serializable{

    protected String text;
    protected List<Step> step;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the step property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the step property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Step }
     * 
     * 
     */
    public List<Step> getStep() {
        if (step == null) {
            step = new ArrayList<Step>();
        }
        return this.step;
    }

}
